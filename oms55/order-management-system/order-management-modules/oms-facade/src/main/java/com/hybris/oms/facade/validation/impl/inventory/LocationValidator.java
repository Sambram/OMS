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

import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.locationrole.LocationRole;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.facade.validation.field.FieldValidatorFactory;
import com.hybris.oms.facade.validation.impl.AbstractValidator;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Sets;


/**
 * Validator for {@link Location}.
 */
public class LocationValidator extends AbstractValidator<Location>
{
	private static final Logger LOG = LoggerFactory.getLogger(LocationValidator.class);
	private static final int ONE_HUNDRED = 100;

	private Validator<String> countryValidator;

	@Override
	protected void validateInternal(final ValidationContext context, final Location location)
	{
		LOG.debug("Validating location.");

		// Temporary fix OMSWSC-76 - If no location role is provided then add shipping role by default
		if (CollectionUtils.isEmpty(location.getLocationRoles()))
		{
			location.setLocationRoles(Sets.newHashSet(LocationRole.SHIPPING));
		}
		FieldValidatorFactory.getStringFieldValidator(context).notBlank("Location.locationId", location.getLocationId());
		FieldValidatorFactory.getNumberFieldValidator(context)
				//
				.equalOrGreaterThan("Location.percentageInventoryThreshold", location.getPercentageInventoryThreshold(), 0)
				//
				.equalOrLessThan("Location.percentageInventoryThreshold", location.getPercentageInventoryThreshold(), ONE_HUNDRED)
				.equalOrGreaterThan("Location.absoluteInventoryThreshold", location.getAbsoluteInventoryThreshold(), 0);

		this.validateShipToCountries(context, location);
	}

	protected void validateShipToCountries(final ValidationContext context, final Location location)
	{
		// shipToCountriesCodes is not mandatory and therefore will only be validated when it exists
		if (CollectionUtils.isNotEmpty(location.getShipToCountriesCodes()))
		{
			FieldValidatorFactory.getGenericFieldValidator(context).validateCollection("Location.shipToCountriesCodes",
					location.getShipToCountriesCodes(), this.countryValidator);
		}
	}

	@Required
	public void setCountryValidator(final Validator<String> countryValidator)
	{
		this.countryValidator = countryValidator;
	}

	protected Validator<String> getCountryValidator()
	{
		return countryValidator;
	}
}
