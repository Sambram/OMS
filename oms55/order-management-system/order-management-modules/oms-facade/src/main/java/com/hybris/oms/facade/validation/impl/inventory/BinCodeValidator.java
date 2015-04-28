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
 * Bin code validator.
 */
public class BinCodeValidator extends AbstractValidator<BinCodeLocationIdDTO>
{

	private static final Logger LOG = LoggerFactory.getLogger(BinCodeValidator.class);

	private InventoryService inventoryService;

	@Override
	public void validateInternal(final ValidationContext context, final BinCodeLocationIdDTO dto)
	{
		LOG.debug("Validating bin code + location id: {} + {}", dto.getBinCode(), dto.getLocationId());
		try
		{
			if (StringUtils.isBlank(dto.getBinCode()) || StringUtils.isBlank(dto.getLocationId()))
			{
				throw new EntityNotFoundException(String.format("Bin code not found with bin code \"%s\" and location id \"%s\"",
						dto.getBinCode(), dto.getLocationId()));
			}
			this.inventoryService.getBinByBinCodeLocationId(dto.getBinCode(), dto.getLocationId());
		}
		catch (final EntityNotFoundException e)
		{
			context.reportFailure(this.getClass().getName(),
					new Failure(FieldValidationType.INVALID, "binCode + locationId", dto.getBinCode() + " + " + dto.getLocationId(),
							null));
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
