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

import com.hybris.kernel.api.CriteriaQuery;
import com.hybris.kernel.api.Restrictions;
import com.hybris.oms.service.ats.AtsService;
import com.hybris.oms.service.managedobjects.ats.AtsFormulaData;
import com.hybris.oms.service.service.AbstractQueryFactory;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.base.Preconditions;


/**
 * Factory for queries used by {@link AtsService}
 */
public class AtsQueryFactory extends AbstractQueryFactory
{
	private long atsFormulaQueryTtl;

	/**
	 * Returns the query to find all formulas with matching atsIds. If the given {@link Set} of atsIds is <tt>null</tt>
	 * or empty, all existing ATS formulas are returned.
	 * 
	 * @return {@link CriteriaQuery}
	 */
	public CriteriaQuery<AtsFormulaData> findFormulas(final Set<String> atsIds)
	{
		final CriteriaQuery<AtsFormulaData> query = query(AtsFormulaData.class);
		applyAtsFormulaQueryTtl(query);
		if (CollectionUtils.isNotEmpty(atsIds))
		{
			query.where(Restrictions.in(AtsFormulaData.ATSID, atsIds.toArray(new String[atsIds.size()])));
		}
		return query;
	}

	/**
	 * Returns the query to find a formula by atsId.
	 * 
	 * @param atsId id identifying an ATS formula
	 * @return {@link CriteriaQuery}
	 */
	public CriteriaQuery<AtsFormulaData> getFormulaById(final String atsId)
	{
		final CriteriaQuery<AtsFormulaData> query = query(AtsFormulaData.class).where(Restrictions.eq(AtsFormulaData.ATSID, atsId));
		applyAtsFormulaQueryTtl(query);
		return query;
	}

	/**
	 * Sets the time to live in milliseconds on the query if atsFormulaQueryTtl > 0.
	 */
	protected void applyAtsFormulaQueryTtl(final CriteriaQuery<AtsFormulaData> query)
	{
		if (atsFormulaQueryTtl > 0)
		{
			query.setTTL(atsFormulaQueryTtl);
		}
	}

	protected long getAtsFormulaQueryTtl()
	{
		return atsFormulaQueryTtl;
	}

	public void setAtsFormulaQueryTtl(final long atsFormulaQueryTtl)
	{
		Preconditions.checkArgument(atsFormulaQueryTtl >= 0, "atsFormulaQueryTtl has to be >= 0");
		this.atsFormulaQueryTtl = atsFormulaQueryTtl;
	}

}
