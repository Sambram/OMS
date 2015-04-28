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

import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.service.managedobjects.rule.Operator;
import com.hybris.oms.service.managedobjects.rule.RuleActionData;
import com.hybris.oms.service.managedobjects.rule.RuleConditionsData;
import com.hybris.oms.service.managedobjects.rule.RuleData;
import com.hybris.oms.service.managedobjects.rule.RuleElementData;
import com.hybris.oms.service.managedobjects.rule.RuleParameterData;
import com.hybris.oms.service.managedobjects.rule.RuleParameterKey;

import java.util.Arrays;
import java.util.Date;


/**
 * Helper class for creating stub object for Rule domain conversion tests.
 */
public class RuleTestHelpers
{
	public static final Date RULE_START_DATE = new java.util.Date();
	public static final Date RULE_END_DATE = new java.util.Date(RULE_START_DATE.getTime() + 10000000);
	public static final String RULE_DESCRIPTION = "RuleDescription";
	public static final Operator RULE_ELIGIBILITY_OPERATOR = Operator.AND;
	public static final boolean RULE_ENABLED = false;
	public static final Operator RULE_CONDITION_OPERATOR = Operator.OR;
	public static final String RULE_NAME = "Name";
	public static final String RULE_ID = "RuleId";
	public static final int RC1_SEQUENCE_NR = 1;
	public static final int RC2_SEQUENCE_NR = 2;
	public static final int RA1_SEQUENCE_NR = 3;
	public static final int RA2_SEQUENCE_NR = 4;
	public static final int RA1_SALIENCE = 5;
	public static final int RA2_SALIENCE = 6;

	public static final String RP1_VALUE = "Value1";
	public static final String RP2_VALUE = "Value2";
	public static final String RP3_VALUE = "Value3";
	public static final String RP4_VALUE = "Value4";
	public static final String RP5_VALUE = "Value5";
	public static final String RP6_VALUE = "Value6";

	public static final String RP1_DISPLAY_TEXT = "DisplayText1";
	public static final String RP2_DISPLAY_TEXT = "DisplayText2";
	public static final String RP3_DISPLAY_TEXT = "DisplayText3";
	public static final String RP4_DISPLAY_TEXT = "DisplayText4";
	public static final String RP5_DISPLAY_TEXT = "DisplayText5";
	public static final String RP6_DISPLAY_TEXT = "DisplayText6";

	private RuleData ruleData;
	private RuleConditionsData ruleConditionsData1;
	private RuleConditionsData ruleConditionsData2;
	private RuleActionData ruleActionData1;
	private RuleActionData ruleActionData2;

	private RuleParameterData ruleParameterData1;
	private RuleParameterData ruleParameterData2;
	private RuleParameterData ruleParameterData3;
	private RuleParameterData ruleParameterData4;
	private RuleParameterData ruleParameterData5;
	private RuleParameterData ruleParameterData6;

	public RuleParameterData getRuleParameterData1()
	{
		return this.ruleParameterData1;
	}

	public RuleParameterData getRuleParameterData2()
	{
		return this.ruleParameterData2;
	}

	public RuleParameterData getRuleParameterData3()
	{
		return this.ruleParameterData3;
	}

	public RuleParameterData getRuleParameterData4()
	{
		return this.ruleParameterData4;
	}

	public RuleParameterData getRuleParameterData5()
	{
		return this.ruleParameterData5;
	}

	public RuleParameterData getRuleParameterData6()
	{
		return this.ruleParameterData6;
	}

	public RuleConditionsData getRuleConditionsData1()
	{
		return this.ruleConditionsData1;
	}

	public RuleConditionsData getRuleConditionsData2()
	{
		return this.ruleConditionsData2;
	}

	public RuleActionData getRuleActionData1()
	{
		return this.ruleActionData1;
	}

	public RuleActionData getRuleActionData2()
	{
		return this.ruleActionData2;
	}

	public RuleData getRuleData()
	{
		return this.ruleData;
	}

	public void createObjects(final PersistenceManager persistenceManager)
	{
		this.ruleData = this.createRuleData(persistenceManager);

		this.ruleConditionsData1 = this.createRuleConditionsData(RC1_SEQUENCE_NR, this.ruleData, persistenceManager);
		this.ruleConditionsData2 = this.createRuleConditionsData(RC2_SEQUENCE_NR, this.ruleData, persistenceManager);
		this.ruleData.setRuleConditions(Arrays.asList(this.ruleConditionsData1, this.ruleConditionsData2));

		this.ruleActionData1 = this.createRuleActionData(RA1_SEQUENCE_NR, RA1_SALIENCE, this.ruleData, persistenceManager);
		this.ruleActionData2 = this.createRuleActionData(RA2_SEQUENCE_NR, RA2_SALIENCE, this.ruleData, persistenceManager);
		this.ruleData.setRuleActions(Arrays.asList(this.ruleActionData1, this.ruleActionData2));

		this.ruleParameterData1 = this.createRuleParameterData(RP1_DISPLAY_TEXT, RP1_VALUE, this.ruleConditionsData1,
				persistenceManager);
		this.ruleParameterData2 = this.createRuleParameterData(RP2_DISPLAY_TEXT, RP2_VALUE, this.ruleConditionsData1,
				persistenceManager);
		this.ruleParameterData3 = this.createRuleParameterData(RP3_DISPLAY_TEXT, RP3_VALUE, this.ruleConditionsData1,
				persistenceManager);

		this.ruleConditionsData1.setParameters(Arrays.asList(this.ruleParameterData1, this.ruleParameterData2,
				this.ruleParameterData3));

		this.ruleParameterData4 = this.createRuleParameterData(RP4_DISPLAY_TEXT, RP4_VALUE, this.ruleConditionsData2,
				persistenceManager);
		this.ruleParameterData5 = this.createRuleParameterData(RP5_DISPLAY_TEXT, RP5_VALUE, this.ruleConditionsData2,
				persistenceManager);
		this.ruleConditionsData2.setParameters(Arrays.asList(this.ruleParameterData4, this.ruleParameterData5));

		this.ruleParameterData6 = this.createRuleParameterData(RP6_DISPLAY_TEXT, RP6_VALUE, this.ruleActionData1,
				persistenceManager);
		this.ruleActionData1.setParameters(Arrays.asList(this.ruleParameterData6));
	}

	private RuleParameterData createRuleParameterData(final String displayText, final String value, final RuleElementData owner,
			final PersistenceManager persistenceManager)
	{
		final RuleParameterData result = persistenceManager.create(RuleParameterData.class);
		result.setDisplayText(displayText);
		result.setKey(RuleParameterKey.ACTION_INVENTORY_STRATEGY);
		result.setRuleElement(owner);
		result.setValue(value);

		return result;
	}

	private RuleConditionsData createRuleConditionsData(final int sequenceNr, final RuleData rule,
			final PersistenceManager persistenceManager)
	{
		final RuleConditionsData result = persistenceManager.create(RuleConditionsData.class);
		result.setSequenceNr(sequenceNr);
		result.setParameters(null);
		result.setRule(rule);
		return result;
	}

	private RuleActionData createRuleActionData(final int sequenceNr, final int salience, final RuleData rule,
			final PersistenceManager persistenceManager)
	{
		final RuleActionData result = persistenceManager.create(RuleActionData.class);
		result.setSequenceNr(sequenceNr);
		result.setSalience(salience);
		result.setParameters(null);
		result.setRule(rule);
		return result;
	}

	private RuleData createRuleData(final PersistenceManager persistenceManager)
	{
		final RuleData rule = persistenceManager.create(RuleData.class);
		rule.setConditionOperator(RULE_CONDITION_OPERATOR);
		rule.setDescription(RULE_DESCRIPTION);
		rule.setEligibilityOperator(RULE_ELIGIBILITY_OPERATOR);
		rule.setEnabled(RULE_ENABLED);
		rule.setEndDate(RULE_END_DATE);
		rule.setName(RULE_NAME);
		rule.setRuleId(RULE_ID);
		rule.setStartDate(RULE_START_DATE);
		return rule;
	}
}
