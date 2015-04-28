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
import com.hybris.commons.conversion.Populator;
import com.hybris.commons.conversion.impl.EnumToEnumConverter;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.locationrole.LocationRole;
import com.hybris.oms.facade.conversion.common.AddressReverseConverter;
import com.hybris.oms.service.managedobjects.basestore.BaseStoreData;
import com.hybris.oms.service.managedobjects.i18n.CountryData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Reverse converter for location dto to data.
 */
public class LocationReversePopulator implements Populator<Location, StockroomLocationData>
{
	private PersistenceManager persistenceManager;
	private AddressReverseConverter addressReverseConverter;

	private final EnumToEnumConverter<com.hybris.oms.domain.locationrole.LocationRole, //
	com.hybris.oms.service.managedobjects.inventory.LocationRole> locationRoleConverter = //
	new EnumToEnumConverter<com.hybris.oms.domain.locationrole.LocationRole, com.hybris.oms.service.managedobjects.inventory.LocationRole>()
	{
		@Override
		public Class<com.hybris.oms.service.managedobjects.inventory.LocationRole> getTargetClass()
		{
			return com.hybris.oms.service.managedobjects.inventory.LocationRole.class;
		}
	};

	protected EnumToEnumConverter<com.hybris.oms.domain.locationrole.LocationRole, //
	com.hybris.oms.service.managedobjects.inventory.LocationRole> getLocationRoleConverter()
	{
		return this.locationRoleConverter;
	}

	@Override
	public void populateFinals(final Location source, final StockroomLocationData target) throws ConversionException,
			IllegalArgumentException
	{
		target.setLocationId(source.getLocationId());
	}

	@Override
	public void populate(final Location source, final StockroomLocationData target) throws ConversionException
	{
		target.setDescription(source.getDescription());
		target.setStoreName(source.getStoreName());
		target.setTaxAreaId(source.getTaxAreaId());
		target.setPriority(source.getPriority());
		target.setAddress(this.addressReverseConverter.convert(source.getAddress()));
		target.setActive(source.getActive());
		target.setAbsoluteInventoryThreshold(source.getAbsoluteInventoryThreshold());
		target.setPercentageInventoryThreshold(source.getPercentageInventoryThreshold());
		target.setUsePercentageThreshold(source.getUsePercentageThreshold());
		final Set<String> roles = new HashSet<>();
		if (source.getLocationRoles() != null && !source.getLocationRoles().isEmpty())
		{
			for (final LocationRole role : source.getLocationRoles())
			{
				roles.add(role.name());
			}
			target.setLocationRoles(roles);
		}

		this.populateBaseStores(source, target);
		this.populateShipToCountriesCodes(source, target);
	}

	protected void populateBaseStores(final Location source, final StockroomLocationData target)
	{
		if (CollectionUtils.isNotEmpty(source.getBaseStores()))
		{
			final Set<BaseStoreData> baseStores = new HashSet<>();
			for (final String baseStoreName : source.getBaseStores())
			{
				try
				{
					baseStores.add(persistenceManager.getByIndex(BaseStoreData.UX_BASESTORES_NAME, baseStoreName));
				}
				catch (final ManagedObjectNotFoundException e)
				{
					throw new ConversionException("baseStoreName=" + baseStoreName + " doesn't exist!", e);
				}
			}
			target.setBaseStores(baseStores);
		}
		else
		{
			if (CollectionUtils.isNotEmpty(target.getBaseStores()))
			{
				target.setBaseStores(Collections.<BaseStoreData>emptySet());
			}
		}
	}

	protected void populateShipToCountriesCodes(final Location source, final StockroomLocationData target)
	{
		final Set<CountryData> shipToCountries = new HashSet<CountryData>();

		// By default a location will ship to country where it exists
		if (CollectionUtils.isEmpty(source.getShipToCountriesCodes()) && source.getAddress() != null
				&& source.getAddress().getCountryIso3166Alpha2Code() != null)
		{
			final Set<String> defaultShipToCountriesCodes = new HashSet<>();
			defaultShipToCountriesCodes.add(source.getAddress().getCountryIso3166Alpha2Code());
			source.setShipToCountriesCodes(defaultShipToCountriesCodes);
		}

		for (final String countryCode : source.getShipToCountriesCodes())
		{
			try
			{
				final CountryData country = this.persistenceManager.getByIndex(CountryData.UX_COUNTRIES_CODE, countryCode);
				shipToCountries.add(country);
			}
			catch (final ManagedObjectNotFoundException e)
			{
				throw new ConversionException("countryCode=" + countryCode + " doesn't exist!", e);
			}
		}

		target.setShipToCountries(shipToCountries);
	}

	@Required
	public void setPersistenceManager(final PersistenceManager persistenceManager)
	{
		this.persistenceManager = persistenceManager;
	}

	@Required
	public void setAddressReverseConverter(final AddressReverseConverter addressReverseConverter)
	{
		this.addressReverseConverter = addressReverseConverter;
	}

}
