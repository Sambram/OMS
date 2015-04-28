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

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.impl.AbstractPopulator;
import com.hybris.commons.conversion.impl.EnumToEnumConverter;
import com.hybris.commons.conversion.util.Converters;
import com.hybris.oms.domain.rule.domain.Rule;
import com.hybris.oms.domain.rule.domain.RuleAction;
import com.hybris.oms.domain.rule.domain.RuleCondition;
import com.hybris.oms.service.managedobjects.rule.RuleActionData;
import com.hybris.oms.service.managedobjects.rule.RuleConditionsData;
import com.hybris.oms.service.managedobjects.rule.RuleData;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

/**
 * Converts {@link RuleData} Managed Object into {@link Rule} DTO.
 */
public class RulePopulator extends AbstractPopulator<RuleData, Rule> {
	private Converter<RuleConditionsData, RuleCondition> ruleConditionsConverter;

	private Converter<RuleActionData, RuleAction> ruleActionConverter;

	private Converters converters;

	private final EnumToEnumConverter<com.hybris.oms.service.managedobjects.rule.Operator, //
	com.hybris.oms.domain.rule.enums.Operator> operatorConverter = //
	new EnumToEnumConverter<com.hybris.oms.service.managedobjects.rule.Operator, com.hybris.oms.domain.rule.enums.Operator>() {
		@Override
		public Class<com.hybris.oms.domain.rule.enums.Operator> getTargetClass() {
			return com.hybris.oms.domain.rule.enums.Operator.class;
		}
	};

	protected EnumToEnumConverter<com.hybris.oms.service.managedobjects.rule.Operator, //
	com.hybris.oms.domain.rule.enums.Operator> getOperatorConverter() {
		return this.operatorConverter;
	}

	@Override
	public void populate(final RuleData source, final Rule target)
			throws ConversionException {
		target.setConditionOperator(this.getOperatorConverter().convert(
				source.getConditionOperator()));
		target.setDescription(source.getDescription());
		target.setEligibilityOperator(this.getOperatorConverter().convert(
				source.getEligibilityOperator()));
		target.setEnabled(source.isEnabled());
		target.setEndDate(source.getEndDate());
		target.setName(source.getName());
		target.setRuleActions(this.populateActions(source.getRuleActions()));
		target.setRuleConditions(this.populateConditions(source
				.getRuleConditions()));
		target.setRuleId(source.getRuleId());
		target.setStartDate(source.getStartDate());
	}

	protected List<RuleAction> populateActions(
			final List<RuleActionData> ruleActionList) {
		return this.converters.convertAll(ruleActionList,
				this.ruleActionConverter);
	}

	protected List<RuleCondition> populateConditions(
			final List<RuleConditionsData> ruleConditionsList) {
		return this.converters.convertAll(ruleConditionsList,
				this.ruleConditionsConverter);
	}

	@Required
	public void setRuleConditionsConverter(
			final Converter<RuleConditionsData, RuleCondition> ruleConditionsConverter) {
		this.ruleConditionsConverter = ruleConditionsConverter;
	}

	@Required
	public void setRuleActionConverter(
			final Converter<RuleActionData, RuleAction> ruleActionConverter) {
		this.ruleActionConverter = ruleActionConverter;
	}

	@Required
	public void setConverters(final Converters converters) {
		this.converters = converters;
	}
}
