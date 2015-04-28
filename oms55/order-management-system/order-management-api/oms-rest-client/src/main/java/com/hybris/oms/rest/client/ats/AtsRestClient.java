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
package com.hybris.oms.rest.client.ats;

import com.hybris.commons.client.RestCallBuilder;
import com.hybris.commons.client.RestResponseException;
import com.hybris.oms.api.ats.AtsFacade;
import com.hybris.oms.domain.ats.AtsFormula;
import com.hybris.oms.domain.ats.AtsLocalQuantities;
import com.hybris.oms.domain.ats.AtsQuantities;
import com.hybris.oms.domain.exception.DuplicateEntityException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.exception.HybrisSystemException;
import com.hybris.oms.rest.client.web.DefaultRestClient;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.sun.jersey.api.client.GenericType;


public class AtsRestClient extends DefaultRestClient implements AtsFacade
{

	private static final String LOCATION_ROLES = "locationRoles";
	private static final String BASESTORE_NAME = "baseStore";
	private static final String COUNTRY_CODES = "shipToCountriesCodes";
	private static final String SKU = "sku";
	private static final String ATS_ID = "atsId";
	private static final String LOC_ID = "locId";

	private static final GenericType<Collection<AtsFormula>> FORMULAS = new GenericType<Collection<AtsFormula>>()
	{/* NOPMD */};
	private static final GenericType<List<AtsLocalQuantities>> LOCAL_QUANTITIES = new GenericType<List<AtsLocalQuantities>>()
	{/* NOPMD */};

	@Override
	public void createFormula(final AtsFormula formula) throws EntityValidationException, DuplicateEntityException
	{
		try
		{
			getClient().call("/ats/formula").post(AtsFormula.class, formula).result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public void updateFormula(final String atsId, final AtsFormula formula) throws EntityValidationException,
			EntityNotFoundException
	{
		try
		{
			getClient().call("/ats/formula/%s", atsId).put(AtsFormula.class, formula).result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public void deleteFormula(final String atsId) throws EntityNotFoundException
	{
		try
		{
			getClient().call("/ats/formula/%s", atsId).delete().result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Collection<AtsFormula> findAllFormulas()
	{
		try
		{
			return getClient().call("/ats/formula").get(FORMULAS).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public AtsFormula getFormulaById(final String atsId) throws EntityNotFoundException
	{
		try
		{
			return getClient().call("/ats/formula/%s", atsId).get(AtsFormula.class).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public AtsQuantities findGlobalAts(final Set<String> skus, final Set<String> atsIds) throws EntityNotFoundException
	{
		try
		{
			final RestCallBuilder builder = getClient().call("/ats");
			addCollectionParam(SKU, skus, builder);
			addCollectionParam(ATS_ID, atsIds, builder);
			return builder.get(AtsQuantities.class).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public List<AtsLocalQuantities> findAtsByBaseStore(final Set<String> skus, final Set<String> atsIds, final String baseStore,
			final Set<String> countryCodes, final Set<String> locationRoles) throws EntityNotFoundException,
			EntityValidationException
	{
		try
		{
			final RestCallBuilder builder = getClient().call("/ats/baseStore");
			addCollectionParam(SKU, skus, builder);
			addCollectionParam(ATS_ID, atsIds, builder);
			addParam(BASESTORE_NAME, baseStore, builder);
			addCollectionParam(COUNTRY_CODES, countryCodes, builder);
			addCollectionParam(LOCATION_ROLES, locationRoles, builder);
			return builder.get(LOCAL_QUANTITIES).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public List<AtsLocalQuantities> findLocalAts(final Set<String> skus, final Set<String> locations, final Set<String> atsIds)
			throws EntityNotFoundException
	{
		try
		{
			final RestCallBuilder builder = getClient().call("/ats/local");
			addCollectionParam(LOC_ID, locations, builder);
			addCollectionParam(SKU, skus, builder);
			addCollectionParam(ATS_ID, atsIds, builder);
			return builder.get(LOCAL_QUANTITIES).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

}
