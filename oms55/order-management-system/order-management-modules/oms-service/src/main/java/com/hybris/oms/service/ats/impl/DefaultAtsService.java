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
package com.hybris.oms.service.ats.impl;


import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Preconditions;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.kernel.api.exceptions.PrimaryKeyViolationException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.service.ats.AtsResult;
import com.hybris.oms.service.ats.AtsService;
import com.hybris.oms.service.ats.DuplicateFormulaException;
import com.hybris.oms.service.managedobjects.ats.AtsFormulaData;
import com.hybris.oms.service.preference.TenantPreferenceService;


/**
 * Default implementation of {@link AtsService}.
 */
public class DefaultAtsService implements AtsService
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultAtsService.class);

	private PersistenceManager persistenceManager;

	private TenantPreferenceService tenantPreferenceService;

	private CalculationContextBuilder contextBuilder;

	private AtsCalculator atsCalculator;

	private AtsQueryFactory queryFactory;


	@Override
	public void createFormula(final String atsId, final String formula, final String name, final String description)
	{
		this.createOrUpdateFormula(atsId, formula, name, description, true);
	}

	@Override
	public void updateFormula(final String atsId, final String formula, final String name, final String description)
	{
		this.createOrUpdateFormula(atsId, formula, name, description, false);
	}

	@Override
	public void deleteFormula(final String atsId)
	{
		final AtsFormulaData formulaData = this.getFormulaById(atsId);
		this.persistenceManager.remove(formulaData);
	}

	@Override
	public List<AtsFormulaData> findFormulas(final Set<String> atsIds)
	{
		return queryFactory.findFormulas(atsIds).resultList();
	}

	@Override
	public AtsFormulaData getFormulaById(final String atsId)
	{
		try
		{
			return queryFactory.getFormulaById(atsId).uniqueResult();
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException("Formula not found (" + atsId + ')', e);
		}
	}

	@Override
	public String getDefaultAtsId()
	{
		String atsId = null;
		try
		{
			atsId = tenantPreferenceService.getTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_ATS_CALCULATOR).getValue();
		}
		catch (final EntityNotFoundException e)
		{
			LOG.error("Could not retrieve ats.calculator", e);
		}
		if (StringUtils.isBlank(atsId))
		{
			throw new EntityNotFoundException("Default formula undefined in tenant preferences ("
					+ TenantPreferenceConstants.PREF_KEY_ATS_CALCULATOR + ")");
		}
		return atsId;
	}

	@Override
	public AtsResult getGlobalAts(final Set<String> filterSkus, final Set<String> atsIds)
	{
		return calculateAts(filterSkus, null, atsIds, true);
	}

	@Override
	public AtsResult getLocalAts(final Set<String> filterSkus, final Set<String> filterLocations, final Set<String> atsIds)
	{
		return calculateAts(filterSkus, filterLocations, atsIds, false);
	}

	protected AtsResult calculateAts(final Set<String> filterSkus, final Set<String> filterLocations, final Set<String> atsIds,
			final boolean global)
	{
		final List<AtsFormulaData> formulas = findFormulas(atsIds);
		final CalculationContext context = contextBuilder.buildContext(filterSkus, filterLocations, atsIds, global, formulas);
		return atsCalculator.calculateAts(formulas, context);
	}

	protected void createOrUpdateFormula(final String atsId, final String formula, final String name, final String description,
			final boolean create)
	{
		final AtsFormulaData formulaData;
		if (create)
		{
			formulaData = persistenceManager.create(AtsFormulaData.class);
			formulaData.setAtsId(atsId);
		}
		else
		{
			formulaData = getFormulaById(atsId);
			Preconditions.checkArgument(atsId.equals(formulaData.getAtsId()),
					"atsId {} cannot be updated to {}. Please create a new instance instead.", formulaData.getAtsId(), atsId);
		}
		formulaData.setFormula(formula);
		formulaData.setName(name);
		formulaData.setDescription(description);
		try
		{
			this.persistenceManager.flush();
		}
		catch (final PrimaryKeyViolationException e)
		{
			throw new DuplicateFormulaException("Formula " + atsId + " already exists", e);
		}
	}

	protected AtsCalculator getAtsCalculator()
	{
		return atsCalculator;
	}

	@Required
	public void setAtsCalculator(final AtsCalculator atsCalculator)
	{
		this.atsCalculator = atsCalculator;
	}

	protected CalculationContextBuilder getContextBuilder()
	{
		return contextBuilder;
	}

	@Required
	public void setContextBuilder(final CalculationContextBuilder contextBuilder)
	{
		this.contextBuilder = contextBuilder;
	}

	protected PersistenceManager getPersistenceManager()
	{
		return persistenceManager;
	}

	@Required
	public void setPersistenceManager(final PersistenceManager persistenceManager)
	{
		this.persistenceManager = persistenceManager;
	}

	protected TenantPreferenceService getTenantPreferenceService()
	{
		return tenantPreferenceService;
	}

	@Required
	public void setTenantPreferenceService(final TenantPreferenceService tenantPreferenceService)
	{
		this.tenantPreferenceService = tenantPreferenceService;
	}

	protected AtsQueryFactory getQueryFactory()
	{
		return queryFactory;
	}

	@Required
	public void setQueryFactory(final AtsQueryFactory queryFactory)
	{
		this.queryFactory = queryFactory;
	}

}
