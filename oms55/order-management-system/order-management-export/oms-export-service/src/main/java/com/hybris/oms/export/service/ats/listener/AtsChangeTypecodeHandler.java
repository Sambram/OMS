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
import com.hybris.kernel.api.KernelEventType;
import com.hybris.kernel.api.ManagedObject;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.export.service.ats.listener.filter.AtsExportFilter;
import com.hybris.oms.export.service.ats.listener.filter.impl.AlwaysExportAtsExportFilter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;


/**
 * Each typecode that is picked up by the {@link AtsChangeListener} should extend this class to indicate how to get the
 * sku out of the tracked values.</br>
 * This is done by overriding the {@link AtsChangeTypecodeHandler#getSku(Map)}
 * <p>
 * This class contains 3 {@link AtsExportFilter} instances: one for each kernel event type (create, update delete).
 * </br> The default implementation of each of these strategies is the {@link AlwaysExportAtsExportFilter}.
 * </p>
 */
public abstract class AtsChangeTypecodeHandler<T extends ManagedObject> implements InitializingBean
{
	private AtsExportFilter creationAtsExportFilter;
	private AtsExportFilter updateAtsExportFilter;
	private AtsExportFilter deletionAtsExportFilter;
	private Map<KernelEventType, AtsExportFilter> atsExportFiltersMap;
	private PersistenceManager persistenceManager;

	/**
	 * Get the sku field from the {@link KernelEvent} values
	 *
	 * @param values
	 * @return sku; never <tt>null</tt>
	 * @throws AtsChangeListenerException - if the sku cannot be found
	 */
	public abstract String getSku(final Map<String, Object> values) throws AtsChangeListenerException;

	/**
	 * Get the locationId field from the {@link KernelEvent} values
	 *
	 * @param values
	 * @return locationId; never <tt>null</tt>
	 * @throws AtsChangeListenerException - if the locationId cannot be found
	 */
	public abstract String getLocationId(final Map<String, Object> values) throws AtsChangeListenerException;

	/**
	 * Get the value from the map.
	 *
	 * @param eventType
	 * @return the {@link AtsExportFilter} mapped to the given {@link KernelEventType} or <tt>null</tt> if not
	 *         found.
	 */
	public AtsExportFilter getAtsExportFilter(final KernelEventType eventType)
	{
		return atsExportFiltersMap.get(eventType);
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		atsExportFiltersMap = new HashMap<>();
		atsExportFiltersMap.put(KernelEventType.CREATE, creationAtsExportFilter);
		atsExportFiltersMap.put(KernelEventType.UPDATE, updateAtsExportFilter);
		atsExportFiltersMap.put(KernelEventType.DELETE, deletionAtsExportFilter);
	}

	public PersistenceManager getPersistenceManager()
	{
		return persistenceManager;
	}

	@Required
	public void setPersistenceManager(final PersistenceManager persistenceManager)
	{
		this.persistenceManager = persistenceManager;
	}

	public AtsExportFilter getCreationAtsExportFilter()
	{
		return creationAtsExportFilter;
	}

	@Required
	public void setCreationAtsExportFilter(final AtsExportFilter creationAtsExportFilter)
	{
		this.creationAtsExportFilter = creationAtsExportFilter;
	}

	public AtsExportFilter getUpdateAtsExportFilter()
	{
		return updateAtsExportFilter;
	}

	@Required
	public void setUpdateAtsExportFilter(final AtsExportFilter updateAtsExportFilter)
	{
		this.updateAtsExportFilter = updateAtsExportFilter;
	}

	public AtsExportFilter getDeletionAtsExportFilter()
	{
		return deletionAtsExportFilter;
	}

	@Required
	public void setDeletionAtsExportFilter(final AtsExportFilter deletionAtsExportFilter)
	{
		this.deletionAtsExportFilter = deletionAtsExportFilter;
	}

	public Map<KernelEventType, AtsExportFilter> getAtsExportFiltersMap()
	{
		return atsExportFiltersMap;
	}

}
