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
import com.hybris.oms.domain.rule.custom.ChangeOrderStatusRuleAction;
import com.hybris.oms.domain.rule.domain.RuleAction;
import com.hybris.oms.service.managedobjects.rule.RuleActionData;


/**
 * Converts given {@link RuleActionData} Managed Object into {@link ChangeOrderStatusRuleAction} DTO.
 * This converter does not populate RuleElement.rule property, this has to be handled externally.
 */
public class RuleActionPopulator extends RuleElementPopulator<RuleActionData, RuleAction>
{
	@Override
	public void populate(final RuleActionData source, final RuleAction target) throws ConversionException
	{
		super.populate(source, target);
		target.setSalience(source.getSalience());
	}

}
