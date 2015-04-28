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
import com.hybris.oms.domain.rule.domain.RuleParameter;
import com.hybris.oms.domain.rule.enums.RuleParameterKey;
import com.hybris.oms.service.managedobjects.rule.RuleParameterData;

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
public class RuleParameterConverterTest
{
	@Autowired
	private PersistenceManager persistenceManager;

	@Autowired
	private Converter<RuleParameterData, RuleParameter> ruleParameterConverter;

	private RuleParameter target;
	private RuleTestHelpers testUtils;

	@Before
	public void setUp()
	{
		this.testUtils = new RuleTestHelpers();
		this.testUtils.createObjects(this.persistenceManager);

		this.target = new RuleParameter();
	}

	@Test
	@Transactional
	public void testConvertingRuleParameter()
	{
		// given
		final RuleParameterData source = this.testUtils.getRuleParameterData1();

		// when
		this.target = this.ruleParameterConverter.convert(source);

		// then
		assertValid();
	}

	@Test
	@Transactional
	public void testConvertingRuleParameterWithFlush()
	{
		// given
		final RuleParameterData source = this.testUtils.getRuleParameterData1();
		this.persistenceManager.flush();

		// when
		this.target = this.ruleParameterConverter.convert(source);

		// then
		assertValid();
	}

	private void assertValid()
	{
		Assert.assertEquals(RuleTestHelpers.RP1_DISPLAY_TEXT, this.target.getDisplayText());
		Assert.assertEquals(RuleParameterKey.ACTION_INVENTORY_STRATEGY, this.target.getKey());
		Assert.assertEquals(null, this.target.getParameterId()); // Ignored
		Assert.assertEquals(RuleTestHelpers.RP1_VALUE, this.target.getValue());
	}
}
