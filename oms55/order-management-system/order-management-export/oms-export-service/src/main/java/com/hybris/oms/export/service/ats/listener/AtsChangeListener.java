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
package com.hybris.oms.export.service.ats.listener;

import com.hybris.kernel.api.KernelEvent;
import com.hybris.kernel.api.KernelEventAware;
import com.hybris.kernel.api.ManagedObject;
import com.hybris.kernel.api.annotations.KernelEventHandler;
import com.hybris.kernel.api.exceptions.PrimaryKeyViolationException;
import com.hybris.oms.export.service.ats.ATSExportTriggerService;
import com.hybris.oms.export.service.ats.impl.ATSAggregationUtils;
import com.hybris.oms.export.service.ats.listener.filter.AtsExportFilter;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;


/**
 * This class acts as a controller to all ats change events. When a typecode described in the property
 * <tt>oms.export.ats.typecodes</tt> is changed (created, updated, deleted) then the
 * {@link AtsChangeListener#onEvent(KernelEvent)} method is called.
 *
 * <p>
 * For each typecode found in <tt>oms.export.ats.typecodes</tt>, there should be a matching
 * {@link AtsChangeTypecodeHandler} that will handle the typecode-specific behaviour.
 *
 */
public class AtsChangeListener implements KernelEventAware, InitializingBean, ApplicationContextAware
{
	private static final Logger LOG = LoggerFactory.getLogger(AtsChangeListener.class);

	private ATSExportTriggerService atsExportTriggerService;
	private PlatformTransactionManager transactionManager;
	private ApplicationContext applicationContext;

	/**
	 * Not set via Spring DI, but introspection in the {@link #afterPropertiesSet()} <br/>
	 * Contains the spring bean registered to handle the managed object with class name = key of the Map
	 */
	@SuppressWarnings("rawtypes")
	private Map<String, AtsChangeTypecodeHandler> atsTypecodeHandlers;

	/**
	 * The method to be called asynchronously when a change to a {@link ManagedObject} occurs that might impact ATS.
	 *
	 * @param event - the previous and current values of the fields in the modified {@link ManagedObject}
	 */
	@KernelEventHandler(typeCodes = {"${oms.export.ats.typecodes}"})
	public void onEvent(final KernelEvent event)
	{

		final Map<String, Object> values = ATSAggregationUtils.getDefaultValues(event);
		if (MapUtils.isEmpty(values))
		{
			LOG.warn("Cannot perform export: no values found in kernel event.");
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug("ATS change listener fired with default values " + values);
		}

		try
		{
			// Execute trigger in a transaction
			final TransactionTemplate template = new TransactionTemplate(this.transactionManager);
			template.execute(new TransactionCallbackWithoutResult()
			{
				@Override
				protected void doInTransactionWithoutResult(final TransactionStatus arg0)
				{
					if (LOG.isDebugEnabled())
					{
						LOG.debug("Processing event: " + event);
					}
					/*
					 * Get the AtsTypecodeHelper for the typecode in the kernel event
					 */
					final AtsChangeTypecodeHandler<?> typecodeHelper = atsTypecodeHandlers.get(event.getHybrisId().getTypeCode());
					if (typecodeHelper == null)
					{
						LOG.warn("Cannot perform export: no typecode helper registered for typecode ["
								+ event.getHybrisId().getTypeCode() + "].");
						return;
					}

					/*
					 * Get the AtsExportableStrategy according to the current event type, then check if it allows export.
					 * If the strategy allows export, then trigger the export.
					 */
					final AtsExportFilter atsExportableStrategy = typecodeHelper.getAtsExportFilter(event.getEventType());
					if (atsExportableStrategy == null)
					{
						LOG.warn("Cannot perform export: no ats exportable strategy registered for event type ["
								+ event.getEventType().name() + "].");
						return;
					}
					if (atsExportableStrategy.isExportable(event))
					{
						/*
						 * Get the sku that is to be exported.
						 */
						String sku = null;
						try
						{
							sku = typecodeHelper.getSku(values);
						}
						catch (final AtsChangeListenerException e)
						{
							LOG.warn("Cannot perform export: an exception occurred when trying to get the sku.", e);
							return;
						}
						if (StringUtils.isEmpty(sku))
						{
							LOG.warn("Cannot perform export: no sku was found in kernel event values " + values.toString());
							return;
						}

						/*
						 * Get the location id that is to be exported.
						 */
						String locationId = null;
						try
						{
							locationId = typecodeHelper.getLocationId(values);
						}
						catch (final AtsChangeListenerException e)
						{
							LOG.warn("Cannot perform export: an exception occurred when trying to get the locationId.", e);
							return;
						}
						if (StringUtils.isEmpty(sku))
						{
							LOG.warn("Cannot perform export: no locationId was found in kernel event values " + values.toString());
							return;
						}

						atsExportTriggerService.triggerExport(sku, locationId);
					}
				}
			});
		}
		catch (@SuppressWarnings("unused") final PrimaryKeyViolationException ignored)
		{
			LOG.debug("Sku already marked for export.");
		}
	}

	@Override
	public void setApplicationContext(final ApplicationContext arg0) throws BeansException
	{
		this.applicationContext = arg0;
	}

	public ATSExportTriggerService getAtsExportTriggerService()
	{
		return atsExportTriggerService;
	}

	/**
	 * Called by Spring.
	 *
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void afterPropertiesSet() throws Exception
	{
		atsTypecodeHandlers = new HashMap<>();

		final Map<String, AtsChangeTypecodeHandler> typecodeHelpers = applicationContext
				.getBeansOfType(AtsChangeTypecodeHandler.class);

		Class type = null;

		final Set<String> typeCodes = new HashSet<>();
		for (final AtsChangeTypecodeHandler typecodeHelper : typecodeHelpers.values())
		{
			type = (Class) ((ParameterizedType) typecodeHelper.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			atsTypecodeHandlers.put(type.getSimpleName(), typecodeHelper);
			typeCodes.add(type.getSimpleName());
		}

		LOG.info("Registered the following typecode handlers: " + atsTypecodeHandlers);
	}

	@Required
	public void setAtsExportTriggerService(final ATSExportTriggerService atsExportTriggerService)
	{
		this.atsExportTriggerService = atsExportTriggerService;
	}

	public PlatformTransactionManager getTransactionManager()
	{
		return transactionManager;
	}

	@Required
	public void setTransactionManager(final PlatformTransactionManager transactionManager)
	{
		this.transactionManager = transactionManager;
	}

	public void setAtsChangeTypecodeHandlers(
			@SuppressWarnings("rawtypes") final Map<String, AtsChangeTypecodeHandler> atsTypecodeHandlers)
	{
		this.atsTypecodeHandlers = atsTypecodeHandlers;
	}

}
