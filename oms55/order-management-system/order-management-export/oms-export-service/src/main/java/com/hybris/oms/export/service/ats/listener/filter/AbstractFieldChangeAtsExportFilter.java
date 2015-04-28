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
package com.hybris.oms.export.service.ats.listener.filter;

import com.hybris.kernel.api.KernelEvent;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Abstract implementation of the {@link AtsExportFilter} that will allow to export based on whether certain
 * fields in the {@link KernelEvent} have been modified.
 */
public abstract class AbstractFieldChangeAtsExportFilter implements AtsExportFilter
{
	private final Logger LOG = LoggerFactory.getLogger(AbstractFieldChangeAtsExportFilter.class);

	@Override
	public final boolean isExportable(final KernelEvent event)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Executing field change strategy with event: " + event);
		}

		boolean export = false;
		final Set<String> changedFields = getChangedFields(event);
		final Set<String> exportFields = getExportFields();

		// Check for changes that could trigger an export.
		if (!CollectionUtils.isEmpty(changedFields) && !CollectionUtils.isEmpty(exportFields))
		{
			for (final String field : exportFields)
			{
				if (changedFields.contains(field))
				{
					export = true;
					break;
				}
			}
		}
		return export;
	}

	/**
	 * Gets the fields that, if changed, will trigger an export.
	 *
	 * @return the fields
	 */
	public abstract Set<String> getExportFields();

	/**
	 * Get the fields that have changed.
	 *
	 * @param event - holds the previous and current values of the fields
	 * @return the changed fields
	 */
	protected final Set<String> getChangedFields(final KernelEvent event)
	{
		final Set<String> changes = new HashSet<>();
		final Map<String, Object> previousValues = event.getPreviousValues();
		final Map<String, Object> currentValues = event.getCurrentValues();

		// If previous values are not empty, then add all fields as changes.
		if (MapUtils.isNotEmpty(previousValues))
		{
			changes.addAll(previousValues.keySet());
		}

		// 1. If current values are not empty and set of changes is empty, then add all fields as changes.
		// 2. If current values are not empty and set of changes is not empty, then remove fields that were not modified.
		if (MapUtils.isNotEmpty(currentValues))
		{
			if (changes.isEmpty())
			{
				changes.addAll(currentValues.keySet());
			}
			else
			{
				// Remove unmodified fields.
				for (final Entry<String, Object> entry : currentValues.entrySet())
				{
					if (currentValues.get(entry.getKey()) == null && previousValues.get(entry.getKey()) == null)
					{
						changes.remove(entry.getKey());
					}
					else if (currentValues.get(entry.getKey()) == null && previousValues.get(entry.getKey()) != null)
					{
						break;
					}
					else if (previousValues.get(entry.getKey()) == null && currentValues.get(entry.getKey()) != null)
					{
						break;
					}
					else if (currentValues.get(entry.getKey()).equals(previousValues.get(entry.getKey())))
					{
						changes.remove(entry.getKey());
					}
				}
			}
		}
		return changes;
	}
}
