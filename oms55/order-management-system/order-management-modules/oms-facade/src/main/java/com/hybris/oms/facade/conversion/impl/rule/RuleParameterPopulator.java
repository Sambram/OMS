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
import com.hybris.commons.conversion.impl.AbstractPopulator;
import com.hybris.commons.conversion.impl.EnumToEnumConverter;
import com.hybris.oms.domain.rule.domain.RuleParameter;
import com.hybris.oms.service.managedobjects.rule.RuleParameterData;


/**
 * Converts given {@link RuleParameterData} Managed Object to {@link RuleParameter} DTO.
 * This converter does not populate RuleParameter.ruleElement property, this has to be handled externally.
 */
public class RuleParameterPopulator extends AbstractPopulator<RuleParameterData, RuleParameter>
{
	private final EnumToEnumConverter<com.hybris.oms.service.managedobjects.rule.RuleParameterKey, //
	com.hybris.oms.domain.rule.enums.RuleParameterKey> ruleParameterKeyConverter = //
	new EnumToEnumConverter<com.hybris.oms.service.managedobjects.rule.RuleParameterKey, //
	com.hybris.oms.domain.rule.enums.RuleParameterKey>()
	{
		@Override
		public Class<com.hybris.oms.domain.rule.enums.RuleParameterKey> getTargetClass()
		{
			return com.hybris.oms.domain.rule.enums.RuleParameterKey.class;
		}
	};

	@Override
	public void populate(final RuleParameterData source, final RuleParameter target) throws ConversionException
	{
		target.setDisplayText(source.getDisplayText());
		target.setKey(this.ruleParameterKeyConverter.convert(source.getKey()));
		target.setValue(source.getValue());
	}

}
