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

import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.service.ats.StatusRealm;
import com.hybris.oms.service.managedobjects.ats.AtsFormulaData;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


/**
 * Integration test for {@link DefaultAggregationToFormulaMatcher}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-service-spring-test.xml"})
@Transactional
public class AggregationToFormulaMatcherIntegrationTest
{
	@Autowired
	private PersistenceManager persistenceManager;

	@Autowired
	private DefaultAggregationToFormulaMatcher mapper;

	@Before
	public void doBefore()
	{
		// create default formulas
		final AtsFormulaData formula1 = this.persistenceManager.create(AtsFormulaData.class);
		formula1.setAtsId("ats1");
		formula1.setDescription("complicated");
		formula1.setFormula("I[ON_HAND]-T");
		formula1.setName("myFirstFormula");

		final AtsFormulaData formula2 = this.persistenceManager.create(AtsFormulaData.class);
		formula2.setAtsId("ats2");
		formula2.setDescription("complicated");
		formula2.setFormula("I[ON_HAND]-O[PICKED]");
		formula2.setName("mySecondFormula");
		persistenceManager.flush();
	}

	@Test
	public void matchingAggregationToFormulaForInventory()
	{
		assertThat(mapper.hasMatchingFormula("ON_HAND", StatusRealm.INVENTORY), is(true));
	}

	@Test
	public void matchingAggregationToFormulaForOrder()
	{
		assertTrue(mapper.hasMatchingFormula("PICKED", StatusRealm.ORDER));
	}

	@Test
	public void notMatchingAnyFormula()
	{
		assertFalse(mapper.hasMatchingFormula("ON_HAND", StatusRealm.ORDER));
	}

	@Test
	public void notMatchingForInventory()
	{
		assertFalse(mapper.hasMatchingFormula("PICKED", StatusRealm.INVENTORY));
	}

	@Test
	public void matchingAggregationToFormulaByPassedFormulaIdsForInventory()
	{
		assertThat(mapper.hasMatchingFormula("ON_HAND", StatusRealm.INVENTORY, Lists.newArrayList("ats1")), is(true));
	}

	@Test
	public void matchingAggregationToFormulaByPassedFormulaIdsForOrder()
	{
		assertThat(mapper.hasMatchingFormula("PICKED", StatusRealm.ORDER, Lists.newArrayList("ats2")), is(true));
	}

	@Test
	public void notMatchingAnyFormulaByPassedFormulaIDsForOrder()
	{
		assertThat(mapper.hasMatchingFormula("NO_MORE", StatusRealm.ORDER, Lists.newArrayList("ats1", "ats2")), is(false));
	}

	@Test
	public void notMatchingAnyFormulaByPassedFormulaIDsForInventory()
	{
		assertThat(mapper.hasMatchingFormula("NO_MORE", StatusRealm.ORDER, Lists.newArrayList("ats1", "ats2")), is(false));
	}

	@Test
	public void cannotFindIdOfPassedFormulas()
	{
		assertThat(mapper.hasMatchingFormula("ON_HAND", StatusRealm.INVENTORY, Lists.newArrayList("1111")), is(false));
	}

	@Test
	public void matchingFormulaByPassedFormulaIdsForOneWrongId()
	{
		assertThat(mapper.hasMatchingFormula("ON_HAND", StatusRealm.INVENTORY, Lists.newArrayList("ats1", "ats2", "111")),
				is(true));
	}

	@Test
	public void getAllFormulas()
	{
		final List<String> expectedFormulas = Lists.newArrayList("I[ON_HAND]-O[PICKED]", "I[ON_HAND]-T");
		final List<AtsFormulaData> actualFormulas = mapper.findAllFormulas();
		assertThat(actualFormulas.size(), equalTo(expectedFormulas.size()));
		final List<String> formulas = Lists.newArrayList(actualFormulas.get(0).getFormula(), actualFormulas.get(1).getFormula());
		assertThat(formulas.contains(expectedFormulas.get(0)), is(true));
		assertThat(formulas.contains(expectedFormulas.get(1)), is(true));
	}

}
