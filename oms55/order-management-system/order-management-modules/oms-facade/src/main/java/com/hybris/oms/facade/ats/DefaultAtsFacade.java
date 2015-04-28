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
package com.hybris.oms.facade.ats;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hybris.oms.api.Pageable;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.util.Converters;
import com.hybris.oms.api.ats.AtsFacade;
import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.domain.ats.AtsFormula;
import com.hybris.oms.domain.ats.AtsLocalQuantities;
import com.hybris.oms.domain.ats.AtsQuantities;
import com.hybris.oms.domain.ats.AtsQuantity;
import com.hybris.oms.domain.exception.DuplicateEntityException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.inventory.LocationQueryObject;
import com.hybris.oms.facade.validation.FailureHandler;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.service.ats.AtsResult;
import com.hybris.oms.service.ats.AtsResult.AtsRow;
import com.hybris.oms.service.ats.AtsService;
import com.hybris.oms.service.ats.DuplicateFormulaException;
import com.hybris.oms.service.managedobjects.ats.AtsFormulaData;


/**
 * Default implementation of {@link AtsFacade}.
 */
public class DefaultAtsFacade implements AtsFacade
{
	private AtsService atsService;

	private Converter<AtsFormulaData, AtsFormula> atsFormulaConverter;

	private Converter<AtsResult, List<AtsLocalQuantities>> atsLocalQuantitiesConverter;

	private Converter<AtsRow, AtsQuantity> atsQuantityConverter;

	private Converters converters;

	private Validator<AtsRequest> atsRequestValidator;

	private Validator<AtsFormula> atsFormulaValidator;

	private FailureHandler entityValidationHandler;

	private InventoryFacade inventoryFacade;

	@Override
	@Transactional
	public void createFormula(final AtsFormula formula)
	{
		this.atsFormulaValidator.validate("AtsFormula", formula, entityValidationHandler);
		try
		{
			this.atsService.createFormula(formula.getAtsId(), formula.getFormula(), formula.getName(), formula.getDescription());
		}
		catch (final DuplicateFormulaException e)
		{
			throw new DuplicateEntityException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional
	public void updateFormula(final String atsId, final AtsFormula formula)
	{
		this.atsFormulaValidator.validate("AtsFormula", formula, entityValidationHandler);
		this.atsService.updateFormula(atsId, formula.getFormula(), formula.getName(), formula.getDescription());
	}

	@Override
	@Transactional
	public void deleteFormula(final String atsId)
	{
		this.atsService.deleteFormula(atsId);
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<AtsFormula> findAllFormulas()
	{
		return this.copyToDtos(this.atsService.findFormulas(null));
	}

	@Override
	@Transactional(readOnly = true)
	public AtsFormula getFormulaById(final String atsId)
	{
		return this.copyToDto(this.atsService.getFormulaById(atsId));
	}

	@Override
	@Transactional(readOnly = true)
	public AtsQuantities findGlobalAts(final Set<String> skus, final Set<String> atsIds)
	{
		final AtsRequest atsRequest = this.populateAtsRequest(atsIds, skus, null);
		this.atsRequestValidator.validate("AtsRequest", atsRequest, this.entityValidationHandler);

		final AtsQuantities result = new AtsQuantities();
		final AtsResult ats = this.atsService.getGlobalAts(skus, atsIds);
		for (final AtsRow row : ats.getResults())
		{
			final AtsQuantity atsQuantity = this.atsQuantityConverter.convert(row);
			result.addAtsQuantity(atsQuantity);
		}
		return result;
	}

	// TODO: Logic should not be in the facade
	@Override
	@Transactional(readOnly = true)
	public List<AtsLocalQuantities> findAtsByBaseStore(final Set<String> skus, final Set<String> atsIds, final String baseStore,
			final Set<String> countryCodes, final Set<String> locationRoles) throws EntityNotFoundException,
			EntityValidationException
	{

		final LocationQueryObject queryObject = new LocationQueryObject();
		if (baseStore != null)
		{
			queryObject.setBaseStores(Collections.singletonList(baseStore));
		}
		if (countryCodes != null)
		{
			queryObject.setCountries(new ArrayList<String>(countryCodes));
		}
		if (locationRoles != null && !locationRoles.isEmpty())
		{
			queryObject.setLocationRoles(new ArrayList<>(locationRoles));
		}
		queryObject.setPageSize(100);

		// get first page
		queryObject.setPageNumber(0);
		final Pageable<Location> pageableLocations = inventoryFacade.findStockRoomLocationsByQuery(queryObject);
		final List<Location> locations = new ArrayList<>();
		locations.addAll(pageableLocations.getResults());

		// get rest of pages
		for (int i = 1; i < pageableLocations.getTotalPages(); i++)
		{
			queryObject.setPageNumber(i);
			locations.addAll(inventoryFacade.findStockRoomLocationsByQuery(queryObject).getResults());
		}

		final Set<String> locationsIds = new HashSet<>();
		for (final Location location : locations)
		{
			locationsIds.add(location.getLocationId());
		}

		if (locationsIds.isEmpty())
		{
			return Collections.emptyList();
		}
		else
		{
			return findLocalAts(skus, locationsIds, atsIds);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<AtsLocalQuantities> findLocalAts(final Set<String> skus, final Set<String> locations, final Set<String> atsIds)
	{
		final AtsRequest atsRequest = this.populateAtsRequest(atsIds, skus, locations);
		this.atsRequestValidator.validate("AtsRequest", atsRequest, this.entityValidationHandler);

		final AtsResult ats = this.atsService.getLocalAts(skus, locations, atsIds);
		return this.atsLocalQuantitiesConverter.convert(ats);
	}

	@Required
	public void setAtsService(final AtsService atsService)
	{
		this.atsService = atsService;
	}

	@Required
	public void setAtsFormulaConverter(final Converter<AtsFormulaData, AtsFormula> atsFormulaConverter)
	{
		this.atsFormulaConverter = atsFormulaConverter;
	}

	@Required
	public void setConverters(final Converters converters)
	{
		this.converters = converters;
	}

	@Required
	public void setAtsRequestValidator(final Validator<AtsRequest> atsRequestValidator)
	{
		this.atsRequestValidator = atsRequestValidator;
	}

	@Required
	public void setAtsFormulaValidator(final Validator<AtsFormula> atsFormulaValidator)
	{
		this.atsFormulaValidator = atsFormulaValidator;
	}

	@Required
	public void setEntityValidationHandler(final FailureHandler entityValidationHandler)
	{
		this.entityValidationHandler = entityValidationHandler;
	}

	@Required
	public void setInventoryFacade(final InventoryFacade inventoryFacade)
	{
		this.inventoryFacade = inventoryFacade;
	}

	@Required
	public void setAtsLocalQuantitiesConverter(final Converter<AtsResult, List<AtsLocalQuantities>> atsLocalQuantitiesConverter)
	{
		this.atsLocalQuantitiesConverter = atsLocalQuantitiesConverter;
	}

	@Required
	public void setAtsQuantityConverter(final Converter<AtsRow, AtsQuantity> atsQuantityConverter)
	{
		this.atsQuantityConverter = atsQuantityConverter;
	}

	protected AtsService getAtsService()
	{
		return atsService;
	}

	protected InventoryFacade getInventoryFacade()
	{
		return inventoryFacade;
	}

	protected FailureHandler getEntityValidationHandler()
	{
		return entityValidationHandler;
	}

	protected Validator<AtsFormula> getAtsFormulaValidator()
	{
		return atsFormulaValidator;
	}

	protected Validator<AtsRequest> getAtsRequestValidator()
	{
		return atsRequestValidator;
	}

	protected Converters getConverters()
	{
		return converters;
	}

	protected Converter<AtsFormulaData, AtsFormula> getAtsFormulaConverter()
	{
		return atsFormulaConverter;
	}

	protected Converter<AtsResult, List<AtsLocalQuantities>> getAtsLocalQuantitiesConverter()
	{
		return atsLocalQuantitiesConverter;
	}

	protected Converter<AtsRow, AtsQuantity> getAtsQuantityConverter()
	{
		return atsQuantityConverter;
	}

	protected AtsRequest populateAtsRequest(final Set<String> atsIds, final Set<String> skus, final Set<String> locations)
	{
		final AtsRequest atsRequest = new AtsRequest();
		atsRequest.setAtsIds(atsIds);
		atsRequest.setSkus(skus);
		atsRequest.setLocations(locations);
		return atsRequest;
	}

	protected Collection<AtsFormula> copyToDtos(final List<AtsFormulaData> formulas)
	{
		return this.converters.convertAll(formulas, this.atsFormulaConverter);
	}

	protected AtsFormula copyToDto(final AtsFormulaData formula)
	{
		return this.atsFormulaConverter.convert(formula);
	}

}
