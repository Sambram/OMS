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
package com.hybris.oms.facade.conversion.impl.inventory;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.impl.AbstractPopulator;
import com.hybris.commons.conversion.impl.EnumToEnumConverter;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.locationrole.LocationRole;
import com.hybris.oms.service.managedobjects.basestore.BaseStoreData;
import com.hybris.oms.service.managedobjects.i18n.CountryData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.types.AddressVT;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Converter for location data to dto.
 */
public class LocationPopulator extends AbstractPopulator<StockroomLocationData, Location>
{
	private Converter<AddressVT, Address> addressConverter;

	private final EnumToEnumConverter<com.hybris.oms.service.managedobjects.inventory.LocationRole, //
	com.hybris.oms.domain.locationrole.LocationRole> locationRoleConverter = //
	new EnumToEnumConverter<//
	com.hybris.oms.service.managedobjects.inventory.LocationRole, //
	com.hybris.oms.domain.locationrole.LocationRole>()
	{
		@Override
		public Class<com.hybris.oms.domain.locationrole.LocationRole> getTargetClass()
		{
			return com.hybris.oms.domain.locationrole.LocationRole.class;
		}
	};

	protected EnumToEnumConverter<com.hybris.oms.service.managedobjects.inventory.LocationRole, //
	com.hybris.oms.domain.locationrole.LocationRole> getLocationRoleConverter()
	{
		return this.locationRoleConverter;
	}

	@Override
	public void populate(final StockroomLocationData source, final Location target) throws ConversionException
	{
		target.setCreationTime(source.getCreationTime());
		target.setModifiedTime(source.getModifiedTime());

		target.setLocationId(source.getLocationId());
		target.setDescription(source.getDescription());
		target.setStoreName(source.getStoreName());
		target.setTaxAreaId(source.getTaxAreaId());
		target.setPriority(source.getPriority());
		target.setAddress(this.addressConverter.convert(source.getAddress()));
		target.setActive(source.isActive());
		target.setAbsoluteInventoryThreshold(source.getAbsoluteInventoryThreshold());
		target.setPercentageInventoryThreshold(source.getPercentageInventoryThreshold());
		target.setUsePercentageThreshold(source.isUsePercentageThreshold());
		final Set<LocationRole> roles = new HashSet<>();
		if (source.getLocationRoles() != null && !source.getLocationRoles().isEmpty())
		{
			for (final String role : source.getLocationRoles())
			{
				roles.add(LocationRole.valueOf(role));
			}
			target.setLocationRoles(roles);
		}

		populateShipToCountries(source, target);
		populateLocationRoles(source, target);
		populateBaseStores(source, target);
	}

	protected void populateShipToCountries(final StockroomLocationData source, final Location target)
	{
		if (CollectionUtils.isNotEmpty(source.getShipToCountries()))
		{
			final Set<String> countryCodes = new HashSet<>();
			for (final CountryData country : source.getShipToCountries())
			{
				countryCodes.add(country.getCode());
			}
			target.setShipToCountriesCodes(countryCodes);
		}
	}

	protected void populateLocationRoles(final StockroomLocationData source, final Location target)
	{
		if (CollectionUtils.isNotEmpty(source.getLocationRoles()))
		{
			final Set<LocationRole> roles = new HashSet<>();
			for (final String role : source.getLocationRoles())
			{
				roles.add(LocationRole.valueOf(role));
			}
			target.setLocationRoles(roles);
		}
	}

	protected void populateBaseStores(final StockroomLocationData source, final Location target)
	{
		if (CollectionUtils.isNotEmpty(source.getBaseStores()))
		{
			final Set<String> baseStoreNames = new HashSet<>();
			for (final BaseStoreData baseStore : source.getBaseStores())
			{
				baseStoreNames.add(baseStore.getName());
			}
			target.setBaseStores(baseStoreNames);
		}
	}

	@Required
	public void setAddressConverter(final Converter<AddressVT, Address> addressConverter)
	{
		this.addressConverter = addressConverter;
	}

}
