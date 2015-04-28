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
package com.hybris.oms.facade.validation.impl.inventory;

import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.facade.validation.Failure;
import com.hybris.oms.facade.validation.FieldValidationType;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.impl.AbstractValidator;
import com.hybris.oms.service.inventory.InventoryService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Location Id validator.
 */
public class LocationIdValidator extends AbstractValidator<String>
{

	private static final Logger LOG = LoggerFactory.getLogger(LocationIdValidator.class);

	private InventoryService inventoryService;

	@Override
	public void validateInternal(final ValidationContext context, final String locationId)
	{
		LOG.debug("Validating location id: {}", locationId);
		try
		{
			if (StringUtils.isBlank(locationId))
			{
				throw new EntityNotFoundException("Location not found with blank locationId");
			}
			this.inventoryService.getLocationByLocationId(locationId);
		}
		catch (final EntityNotFoundException e)
		{
			context.reportFailure(this.getClass().getName(),
					new Failure(FieldValidationType.INVALID, "locationId", locationId, null));
		}
	}

	@Required
	public void setInventoryService(final InventoryService inventoryService)
	{
		this.inventoryService = inventoryService;
	}

	protected InventoryService getInventoryService()
	{
		return inventoryService;
	}
}
