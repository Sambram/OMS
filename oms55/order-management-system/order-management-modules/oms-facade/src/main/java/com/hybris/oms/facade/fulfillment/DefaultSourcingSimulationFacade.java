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
package com.hybris.oms.facade.fulfillment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.hybris.commons.conversion.Converter;
import com.hybris.oms.api.fulfillment.SourceSimulationFacade;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.RemoteRequestException;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.inventory.SkuQuantity;
import com.hybris.oms.domain.order.jaxb.SourceSimulationParameter;
import com.hybris.oms.domain.remote.exception.InvalidGeolocationResponseException;
import com.hybris.oms.facade.conversion.common.AddressReverseConverter;
import com.hybris.oms.service.cis.CisService;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.sourcing.RadianCoordinates;
import com.hybris.oms.service.sourcing.SourcingLine;
import com.hybris.oms.service.sourcing.SourcingService;


/**
 * Abstract fulfillment server will implement all of the fulfillment logic and delegate the retrieval of ordered
 * inventory to each child implementation.
 * 
 * @author jrenato
 */

public class DefaultSourcingSimulationFacade implements SourceSimulationFacade
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultSourcingSimulationFacade.class);

	private CisService cisService;
	private SourcingService sourcingService;
	private Converter<StockroomLocationData, Location> locationConverter;
	private AddressReverseConverter addressReverseConverter;

	@Override
	@Transactional(readOnly = true)
	public Location findBestSourcingLocation(final SourceSimulationParameter sourceSimulationParameter)
			throws EntityNotFoundException
	{
		//FIXME: Create a validator for these checks.
		Preconditions.checkArgument(sourceSimulationParameter != null, "sourceSimulationParameter cannot be null");
		Preconditions.checkArgument(sourceSimulationParameter.getSkuQuantities() != null, "skuQuantities cannot be null");

		final String atsId = sourceSimulationParameter.getAtsId();
		final List<SourcingLine> lines = new ArrayList<>();
		int orderLineId = 1;
		for (final SkuQuantity skuQuantity : sourceSimulationParameter.getSkuQuantities())
		{
			lines.add(new SourcingLine(skuQuantity.getSku(), skuQuantity.getQuantity().getValue(), Integer.toString(orderLineId++),
					null, null, (sourceSimulationParameter.getAddress() != null ? sourceSimulationParameter.getAddress()
							.getCountryIso3166Alpha2Code() : null)));
		}
		final RadianCoordinates shippingCoordinates = retrieveRadianCoordinates(sourceSimulationParameter);

		Set<String> locationFilter = null;
		if (CollectionUtils.isNotEmpty(sourceSimulationParameter.getLocationIds()))
		{
			locationFilter = new HashSet<>(sourceSimulationParameter.getLocationIds());
		}
		final StockroomLocationData location = this.sourcingService.simulateSourcing(lines, atsId, locationFilter,
				shippingCoordinates);
		if (location == null)
		{
			throw new EntityNotFoundException("Item location not found");
		}
		return this.locationConverter.convert(location);
	}

	@Required
	public void setCisService(final CisService cisService)
	{
		this.cisService = cisService;
	}

	@Required
	public void setSourcingService(final SourcingService sourcingService)
	{
		this.sourcingService = sourcingService;
	}

	@Required
	public void setLocationConverter(final Converter<StockroomLocationData, Location> locationConverter)
	{
		this.locationConverter = locationConverter;
	}

	@Required
	public void setAddressReverseConverter(final AddressReverseConverter addressReverseConverter)
	{
		this.addressReverseConverter = addressReverseConverter;
	}

	protected RadianCoordinates retrieveRadianCoordinates(final SourceSimulationParameter sourceSimulationParameter)
	{
		RadianCoordinates shippingCoordinates = null;
		if (sourceSimulationParameter.getAddress() != null)
		{
			final Address address = sourceSimulationParameter.getAddress();
			shippingCoordinates = RadianCoordinates.fromOptionalDegrees(address.getLatitudeValue(), address.getLongitudeValue());
			// only call CIS if sourcing is simulated by distance. If atsId is given this takes precedence right now.
			if (shippingCoordinates == null && StringUtils.isBlank(sourceSimulationParameter.getAtsId()))
			{
				final AddressVT addressVT = this.addressReverseConverter.convert(address);
				shippingCoordinates = this.retrieveCoordinatesFromCis(addressVT);
			}
		}
		return shippingCoordinates;
	}

	protected RadianCoordinates retrieveCoordinatesFromCis(final AddressVT address)
	{
		RadianCoordinates result = null;
		try
		{
			final AddressVT geocoded = this.cisService.geocodeAddress(address);
			result = RadianCoordinates.fromOptionalDegrees(geocoded.getLatitudeValue(), geocoded.getLongitudeValue());
		}
		catch (final InvalidGeolocationResponseException e)
		{
			LOG.warn("Invalid geolocation, using default strategy", e);
		}
		catch (final RemoteRequestException e)
		{
			LOG.warn("Could not geocode address, using default strategy", e);
		}
		return result;
	}

}
