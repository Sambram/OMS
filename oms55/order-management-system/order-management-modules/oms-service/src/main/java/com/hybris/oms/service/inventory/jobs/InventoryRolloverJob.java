/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *  
 */
package com.hybris.oms.service.inventory.jobs;

import com.hybris.commons.tenant.TenantContextService;
import com.hybris.commons.tenant.TenantContextTemplate;
import com.hybris.kernel.api.JobWorkerBean;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.persistence.initialization.InitializationService;
import com.hybris.oms.service.inventory.InventoryServiceConstants;
import com.hybris.oms.service.inventory.impl.DefaultInventoryService;
import com.hybris.oms.service.managedobjects.inventory.CurrentItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.FutureItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.ItemQuantityData;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;


/**
 * The Class InventoryRolloverJob. Rolls over {@link FutureItemQuantityData} to {@link CurrentItemQuantityData} based on
 * date. Only item locations with bin code equal to {@link InventoryServiceConstants#DEFAULT_BIN} will be rolled over.
 */
public class InventoryRolloverJob implements JobWorkerBean
{
	private static final long serialVersionUID = -6999094947639023307L;

	private String beanName;

	private transient InitializationService initializationService;

	private transient DefaultInventoryService inventoryService;

	private transient PersistenceManager persistenceManager;

	private transient TenantContextService tenantContextService;

	private transient PlatformTransactionManager transactionManager;

	/**
	 * @UsedBy("InventoryRolloverJobTest")
	 */
	@Override
	@Transactional
	public void execute(final Serializable params)
	{
		for (final String tenant : this.initializationService.getInitializedTenants())
		{
			this.tenantContextService.executeWithTenant(new MyTenantContext(tenant));
		}
	}


	@Override
	public String getBeanName()
	{
		return this.beanName;
	}

	@Override
	public void setBeanName(final String name)
	{
		this.beanName = name;
	}

	protected void convertFuturetoCurrentItemQuantity(final FutureItemQuantityData futureItemQuantity, final ItemLocationData owner)
	{
		final CurrentItemQuantityData currentItemQuantity = this.persistenceManager.create(CurrentItemQuantityData.class);
		currentItemQuantity.setQuantityValue(futureItemQuantity.getQuantityValue());
		currentItemQuantity.setOwner(owner);
		currentItemQuantity.setStatusCode(futureItemQuantity.getStatusCode());
	}

	protected void createItemLocationCurrentAndPassFutureItemQuantity(final FutureItemQuantityData futureItemQuantity)
	{
		final ItemLocationData futureItemLocation = futureItemQuantity.getOwner();

		final ItemLocationData itemLocationCurrent = this.persistenceManager.create(ItemLocationData.class);
		itemLocationCurrent.setItemId(futureItemLocation.getItemId());
		itemLocationCurrent.setStockroomLocation(futureItemLocation.getStockroomLocation());
		itemLocationCurrent.setBin(futureItemLocation.getBin());
		itemLocationCurrent.setFuture(false);

		this.convertFuturetoCurrentItemQuantity(futureItemQuantity, itemLocationCurrent);
	}

	protected void passFutureItemQuantityToCurrentItemLocation(final FutureItemQuantityData futureItemQuantity,
			final ItemLocationData itemLocationCurrent)
	{
		boolean hasCounterpart = false;
		for (final ItemQuantityData currentItemQuantity : itemLocationCurrent.getItemQuantities())
		{
			if (futureItemQuantity.getStatusCode().equalsIgnoreCase(currentItemQuantity.getStatusCode()))
			{
				hasCounterpart = true;
				int sumQuantity = currentItemQuantity.getQuantityValue();
				sumQuantity += futureItemQuantity.getQuantityValue();
				currentItemQuantity.setQuantityValue(sumQuantity);
				break;
			}
		}

		if (!hasCounterpart)
		{
			this.convertFuturetoCurrentItemQuantity(futureItemQuantity, itemLocationCurrent);
		}
	}

	protected void rolloverFutureToCurrent(final FutureItemQuantityData futureItemQuantity)
	{
		final ItemLocationData futureItemLocation = futureItemQuantity.getOwner();
		final ItemLocationData currentItemLocation = this.inventoryService
				.findOptionalItemLocationCurrentBySkuAndLocationIdAndBinCode(futureItemLocation.getItemId(), futureItemLocation
						.getStockroomLocation().getLocationId(), futureItemLocation.getBin().getBinCode());
		if (currentItemLocation == null)
		{
			this.createItemLocationCurrentAndPassFutureItemQuantity(futureItemQuantity);
		}
		else
		{
			this.passFutureItemQuantityToCurrentItemLocation(futureItemQuantity, currentItemLocation);
		}

		this.persistenceManager.remove(futureItemQuantity);
		this.persistenceManager.flush();

		// remove futureItemLocation if empty itemQuantities
		this.persistenceManager.refresh(futureItemLocation);
		if (futureItemLocation.getItemQuantities().isEmpty())
		{
			this.persistenceManager.remove(futureItemLocation);
			this.persistenceManager.flush();
		}
	}

	protected InitializationService getInitializationService()
	{
		return initializationService;
	}

	protected DefaultInventoryService getInventoryService()
	{
		return inventoryService;
	}

	protected PersistenceManager getPersistenceManager()
	{
		return persistenceManager;
	}

	protected TenantContextService getTenantContextService()
	{
		return tenantContextService;
	}

	protected PlatformTransactionManager getTransactionManager()
	{
		return transactionManager;
	}

	@Required
	public void setInitializationService(final InitializationService initializationService)
	{
		this.initializationService = initializationService;
	}

	@Required
	public void setInventoryService(final DefaultInventoryService inventoryService)
	{
		this.inventoryService = inventoryService;
	}

	@Required
	public void setPersistenceManager(final PersistenceManager persistenceManager)
	{
		this.persistenceManager = persistenceManager;
	}

	@Required
	public void setTenantContextService(final TenantContextService tenantContextService)
	{
		this.tenantContextService = tenantContextService;
	}

	@Required
	public void setTransactionManager(final PlatformTransactionManager transactionManager)
	{
		this.transactionManager = transactionManager;
	}

	private final class MyTenantContext extends TenantContextTemplate<Void>
	{
		private MyTenantContext(final String tenant)
		{
			super(tenant);
		}

		@Override
		public Void execute()
		{
			new TransactionTemplate(InventoryRolloverJob.this.transactionManager).execute(new TransactionCallbackWithoutResult()
			{
				@Override
				protected void doInTransactionWithoutResult(final TransactionStatus arg0)
				{
					final List<FutureItemQuantityData> futureItemQuantityData = InventoryRolloverJob.this.inventoryService
							.findAllExpectedDeliveryDate();

					for (final FutureItemQuantityData futureItemQuantity : futureItemQuantityData)
					{
						InventoryRolloverJob.this.rolloverFutureToCurrent(futureItemQuantity);
					}
				}
			});
			return null;
		}
	}
}
