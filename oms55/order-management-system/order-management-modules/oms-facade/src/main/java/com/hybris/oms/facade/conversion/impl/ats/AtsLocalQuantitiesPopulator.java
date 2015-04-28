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
package com.hybris.oms.facade.conversion.impl.ats;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.impl.AbstractPopulator;
import com.hybris.oms.domain.ats.AtsLocalQuantities;
import com.hybris.oms.domain.ats.AtsQuantity;
import com.hybris.oms.service.ats.AtsResult;
import com.hybris.oms.service.ats.AtsResult.AtsRow;

/**
 * 
 * Converts {@link AtsResult} into a list of {@link AtsLocalQuantities} DTO.
 */
public class AtsLocalQuantitiesPopulator extends AbstractPopulator<AtsResult, List<AtsLocalQuantities>>
{

	private Converter<AtsRow, AtsQuantity> atsQuantityConverter;
	
	@Override
	public void populate(final AtsResult ats, final List<AtsLocalQuantities> result) throws ConversionException
	{
		final Map<String, AtsLocalQuantities> locationQuantities = new LinkedHashMap<>();
		for (final AtsRow row : ats.getResults())
		{
			final String locationId = row.getKey().getLocationId();
			AtsLocalQuantities atsQuantities = locationQuantities.get(locationId);
			if (atsQuantities == null)
			{
				atsQuantities = new AtsLocalQuantities();
				atsQuantities.setLocationId(locationId);
				locationQuantities.put(locationId, atsQuantities);
			}
			final AtsQuantity atsQuantity = this.atsQuantityConverter.convert(row);
			atsQuantities.addAtsQuantity(atsQuantity);
		}

		for (final AtsLocalQuantities quantities : locationQuantities.values())
		{
			result.add(quantities);
		}
	}

	protected Converter<AtsRow, AtsQuantity> getAtsQuantityConverter()
	{
		return atsQuantityConverter;
	}

	@Required
	public void setAtsQuantityConverter(Converter<AtsRow, AtsQuantity> atsQuantityConverter) 
	{
		this.atsQuantityConverter = atsQuantityConverter;
	}

}
