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
package com.hybris.oms.service.inventory.impl;

import com.hybris.kernel.api.CriteriaQuery;
import com.hybris.kernel.api.RawRestrictions;
import com.hybris.kernel.api.Restriction;
import com.hybris.oms.domain.inventory.BinQueryObject;
import com.hybris.oms.service.managedobjects.inventory.BinData;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;


public class SchemalessInventoryQueryFactory extends InventoryQueryFactory
{
	public static final String BIN_SIZE = "size";
	public static final String BIN_COLOR = "color";
	public static final String BIN_SHAPE = "shape";
	public static final String BIN_WEIGHT = "weight";
	public static final String BIN_IS_FRAGILE = "is_fragile";
	public static final String BIN_IS_WEAVED = "is_weaved";
	public static final String BIN_IS_COVERED = "is_covered";
	public static final String BIN_IS_PRETTY = "is_pretty";
	public static final String BIN_HEIGHT = "height";
	public static final String BIN_WIDTH = "width";

	private boolean allowNull;

	@Override
	public CriteriaQuery<BinData> findBinsByQuery(final BinQueryObject queryObject)
	{
		final CriteriaQuery<BinData> criteriaQuery = this.query(BinData.class);

		final List<Restriction> restrictions = new ArrayList<>();

		final String binDescription = queryObject.getDescription();
		final String binSize = queryObject.getValue(BIN_SIZE);
		final String binColor = queryObject.getValue(BIN_COLOR);
		final String binShape = queryObject.getValue(BIN_SHAPE);
		final String binWeight = queryObject.getValue(BIN_WEIGHT);
		final String binIsFragile = queryObject.getValue(BIN_IS_FRAGILE);
		final String binIsWeaved = queryObject.getValue(BIN_IS_WEAVED);
		final String binIsCovered = queryObject.getValue(BIN_IS_COVERED);
		final String binIsPretty = queryObject.getValue(BIN_IS_PRETTY);
		final String binWidth = queryObject.getValue(BIN_WIDTH);
		final String binHeight = queryObject.getValue(BIN_HEIGHT);

		addRestriction(restrictions, BIN_SIZE, binSize);

		addRestriction(restrictions, BIN_COLOR, binColor);

		addRestriction(restrictions, BinData.DESCRIPTION.name(), binDescription);

		addRestriction(restrictions, BIN_SHAPE, binShape);

		addRestriction(restrictions, BIN_WEIGHT, binWeight);

		addRestriction(restrictions, BIN_IS_FRAGILE, binIsFragile);

		addRestriction(restrictions, BIN_IS_WEAVED, binIsWeaved);

		addRestriction(restrictions, BIN_IS_COVERED, binIsCovered);

		addRestriction(restrictions, BIN_IS_PRETTY, binIsPretty);

		addRestriction(restrictions, BIN_WIDTH, binWidth);

		addRestriction(restrictions, BIN_HEIGHT, binHeight);

		buildCriteriaQuery(criteriaQuery, restrictions);

		return criteriaQuery;
	}

	private void addRestriction(final List<Restriction> restrictions, final String attr, final String value)
	{
		if (allowNull || !StringUtils.isEmpty(value))
		{
			restrictions.add(RawRestrictions.eq(attr, value));
		}
	}

	private void buildCriteriaQuery(final CriteriaQuery<BinData> criteriaQuery, final List<Restriction> restrictions)
	{
		boolean hasWhere = false;
		for (final Restriction restriction : restrictions)
		{
			if (!hasWhere)
			{
				criteriaQuery.where(restriction);
				hasWhere = true;
			}
			else
			{
				criteriaQuery.and(restriction);
			}
		}
	}

	public boolean isAllowNull()
	{
		return allowNull;
	}

	/**
	 * When set to true, this flag allows for null value restriction to be added to the criteria.
	 */
	public void setAllowNull(final boolean allowNull)
	{
		this.allowNull = allowNull;
	}
}
