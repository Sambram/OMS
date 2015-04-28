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

import com.hybris.oms.service.ats.StatusRealm;
import com.hybris.oms.service.managedobjects.ats.AtsFormulaData;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;


public class DefaultAggregationToFormulaMatcherTest
{
	private DefaultAggregationToFormulaMatcher matcher;

	@Test
	public void mappingFormulaForInventory()
	{
		// create mapper which has formula with same status code and inventory.
		matcher = createMapperWithStatusCodeAndRealm("ON_HAND", StatusRealm.INVENTORY);
		Assert.assertTrue(matcher.hasMatchingFormula("ON_HAND", StatusRealm.INVENTORY));
	}

	@Test
	public void mappingFormulaForOrder()
	{
		matcher = createMapperWithStatusCodeAndRealm("ON_HAND", StatusRealm.ORDER);
		Assert.assertTrue(matcher.hasMatchingFormula("ON_HAND", StatusRealm.ORDER));
	}

	@Test
	public void noMappingForInventory()
	{
		// different realm should not match. match = true only if status code and realm is same.
		matcher = createMapperWithStatusCodeAndRealm("ON_HAND", StatusRealm.ORDER);
		Assert.assertFalse(matcher.hasMatchingFormula("ON_HAND", StatusRealm.INVENTORY));
	}

	@Test
	public void noMappingForOrder()
	{
		matcher = createMapperWithStatusCodeAndRealm("ON_HAND", StatusRealm.INVENTORY);
		Assert.assertFalse(matcher.hasMatchingFormula("ON_HAND", StatusRealm.ORDER));
	}

	@Test
	public void noMappingForDifferentStatusCode()
	{
		matcher = createMapperWithStatusCodeAndRealm("bla", StatusRealm.INVENTORY);
		Assert.assertFalse(matcher.hasMatchingFormula("ON_HAND", StatusRealm.INVENTORY));
	}

	@Test(expected = IllegalArgumentException.class)
	public void mapEmptyStatusCodeWithInventory()
	{
		matcher = createMapperWithStatusCodeAndRealm("ON_HAND", StatusRealm.INVENTORY);
		Assert.assertTrue(matcher.hasMatchingFormula("", StatusRealm.INVENTORY));
	}

	@Test(expected = IllegalArgumentException.class)
	public void mapEmptyStatusCodeWithOrder()
	{
		matcher = createMapperWithStatusCodeAndRealm("ON_HAND", StatusRealm.ORDER);
		Assert.assertTrue(matcher.hasMatchingFormula("", StatusRealm.ORDER));
	}

	@Test(expected = IllegalArgumentException.class)
	public void mapNullStatusCodeForInventory()
	{
		matcher = createMapperWithStatusCodeAndRealm("ON_HAND", StatusRealm.INVENTORY);
		Assert.assertTrue(matcher.hasMatchingFormula(null, StatusRealm.INVENTORY));
	}

	@Test(expected = IllegalArgumentException.class)
	public void mapNullStatusCodeForOrder()
	{
		matcher = createMapperWithStatusCodeAndRealm("ON_HAND", StatusRealm.ORDER);
		Assert.assertTrue(matcher.hasMatchingFormula(null, StatusRealm.ORDER));
	}

	@Test(expected = IllegalArgumentException.class)
	public void mapNullRealm()
	{
		matcher = createMapperWithStatusCodeAndRealm("ON_HAND", StatusRealm.INVENTORY);
		Assert.assertTrue(matcher.hasMatchingFormula("ON_HAND", null));
	}

	@Test(expected = IllegalArgumentException.class)
	public void mapNullFormulas()
	{
		matcher = createMapperWithStatusCodeAndRealm("ON_HAND", StatusRealm.INVENTORY);
		Assert.assertTrue(matcher.hasMatchingFormula("ON_HAND", StatusRealm.INVENTORY, null));
	}

	private DefaultAggregationToFormulaMatcher createMapperWithStatusCodeAndRealm(final String on_hand, final StatusRealm realm)
	{
		return matcher = new DefaultAggregationToFormulaMatcher()
		{
			@Override
			protected List<ParsedFormula> parseFormulas(final List<AtsFormulaData> formulas)
			{
				final Operand operand = new Operand(false, realm, on_hand);
				final ParsedFormula parsedFormula = new ParsedFormula("atsId", Collections.singletonList(operand), false);
				return Collections.singletonList(parsedFormula);
			}

			// just for mocking
			@Override
			protected List<AtsFormulaData> findAllFormulas()
			{
				return null;
			}
		};
	}

}
