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
package com.hybris.oms.facade.conversion.impl.rule;

import com.hybris.commons.conversion.Converter;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.domain.rule.domain.RuleAction;
import com.hybris.oms.domain.rule.domain.RuleParameter;
import com.hybris.oms.service.managedobjects.rule.RuleActionData;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class RuleActionConverterTest
{
	@Autowired
	private PersistenceManager persistenceManager;

	@Autowired
	private Converter<RuleActionData, RuleAction> ruleActionConverter;

	private RuleTestHelpers testUtils;

	@Before
	public void setUp()
	{
		this.testUtils = new RuleTestHelpers();
		this.testUtils.createObjects(this.persistenceManager);
	}


	@Test
	@Transactional
	public void testConvertingRuleConditions()
	{
		// given
		final RuleActionData source = this.testUtils.getRuleActionData1();

		// when
		final RuleAction target = this.ruleActionConverter.convert(source);

		// then
		assertValid(target);
	}

	@Test
	@Transactional
	public void testConvertingRuleConditionsWithFlush()
	{
		// given
		final RuleActionData source = this.testUtils.getRuleActionData1();
		this.persistenceManager.flush();

		// when
		final RuleAction target = this.ruleActionConverter.convert(source);

		// then
		assertValid(target);
	}

	private void assertValid(final RuleAction target)
	{
		Assert.assertEquals("Action", target.getKind());
		Assert.assertEquals(1, target.getParameters().size());
		final List<RuleParameter> params = new ArrayList<>(target.getParameters());

		Assert.assertEquals(RuleTestHelpers.RP6_DISPLAY_TEXT, params.get(0).getDisplayText());
		Assert.assertEquals(RuleTestHelpers.RP6_VALUE, params.get(0).getValue());

		Assert.assertEquals(RuleTestHelpers.RA1_SEQUENCE_NR, target.getSequenceNr());
		Assert.assertEquals(RuleTestHelpers.RA1_SALIENCE, target.getSalience());
	}
}
