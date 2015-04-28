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

import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.facade.validation.field.FieldValidatorFactory;
import com.hybris.oms.facade.validation.impl.AbstractValidator;
import com.hybris.oms.service.inventory.InventoryServiceConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;

/**
 * Oms Inventory Validator.
 */
public class OmsInventoryValidator extends AbstractValidator<OmsInventory>
{
	private static final Logger LOG = LoggerFactory.getLogger(LocationIdValidator.class);

	private Validator<String> itemStatusCodeValidator;

	private Validator<String> locationIdValidator;

	private Validator<BinCodeLocationIdDTO> binCodeValidator;

	@Override
	protected void validateInternal(final ValidationContext context, final OmsInventory omsInventory)
	{
		LOG.debug("Validating oms inventory.");

		FieldValidatorFactory.getStringFieldValidator(context).notBlank("OmsInventory.skuId", omsInventory.getSkuId());

        if(omsInventory.getDeliveryDate() != null){
            Date today = new Date();
            FieldValidatorFactory.getDateFieldValidator(context).equalOrGreaterThan("OmsInventory.deliveryDate", omsInventory.getDeliveryDate(), today);
        }

		this.itemStatusCodeValidator.validate("OmsInventory.itemStatusCode", context, omsInventory.getStatus());
		this.locationIdValidator.validate("OmsInventory.locationId", context, omsInventory.getLocationId());

		// Only validate bin code if one is provided.
		if (omsInventory.getBinCode() != null)
		{
			this.binCodeValidator.validate("OmsInventory.binCode + locationId", context,
					new BinCodeLocationIdDTO(omsInventory.getBinCode(), omsInventory.getLocationId()));
		}
	}

	@Required
	public void setBinCodeValidator(final Validator<BinCodeLocationIdDTO> binCodeValidator)
	{
		this.binCodeValidator = binCodeValidator;
	}

	@Required
	public void setItemStatusCodeValidator(final Validator<String> itemStatusCodeValidator)
	{
		this.itemStatusCodeValidator = itemStatusCodeValidator;
	}

	@Required
	public void setLocationIdValidator(final Validator<String> locationIdValidator)
	{
		this.locationIdValidator = locationIdValidator;
	}

	protected Validator<BinCodeLocationIdDTO> getBinCodeValidator()
	{
		return binCodeValidator;
	}

	protected Validator<String> getItemStatusCodeValidator()
	{
		return itemStatusCodeValidator;
	}

	protected Validator<String> getLocationIdValidator()
	{
		return locationIdValidator;
	}
}
