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
import com.hybris.oms.domain.rule.domain.RuleCondition;
import com.hybris.oms.domain.rule.domain.RuleParameter;
import com.hybris.oms.service.managedobjects.rule.RuleConditionsData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
public class RuleConditionsConverterTest
{
	@Autowired
	private PersistenceManager persistenceManager;

	@Autowired
	private Converter<RuleConditionsData, RuleCondition> ruleConditionsConverter;

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
		final RuleConditionsData source = this.testUtils.getRuleConditionsData1();

		// when
		final RuleCondition target = this.ruleConditionsConverter.convert(source);

		// then
		assertValid(target);
	}

	@Test
	@Transactional
	public void testConvertingRuleConditionsWithFlush()
	{
		// given
		final RuleConditionsData source = this.testUtils.getRuleConditionsData1();
		this.persistenceManager.flush();

		// when
		final RuleCondition target = this.ruleConditionsConverter.convert(source);

		// then
		assertValid(target);
	}

	private void assertValid(final RuleCondition target)
	{
		Assert.assertEquals("Condition", target.getKind());
		Assert.assertEquals(3, target.getParameters().size());
		final List<RuleParameter> params = new ArrayList<>(target.getParameters());
		Collections.sort(params, new Comparator<RuleParameter>()
		{
			@Override
			public int compare(final RuleParameter o1, final RuleParameter o2)
			{
				return o1.getDisplayText().compareTo(o2.getDisplayText());
			}
		});

		Assert.assertEquals(RuleTestHelpers.RP1_DISPLAY_TEXT, params.get(0).getDisplayText());
		Assert.assertEquals(RuleTestHelpers.RP1_VALUE, params.get(0).getValue());

		Assert.assertEquals(RuleTestHelpers.RP2_DISPLAY_TEXT, params.get(1).getDisplayText());
		Assert.assertEquals(RuleTestHelpers.RP2_VALUE, params.get(1).getValue());

		Assert.assertEquals(RuleTestHelpers.RP3_DISPLAY_TEXT, params.get(2).getDisplayText());
		Assert.assertEquals(RuleTestHelpers.RP3_VALUE, params.get(2).getValue());

		Assert.assertEquals(RuleTestHelpers.RC1_SEQUENCE_NR, target.getSequenceNr());
	}
}
