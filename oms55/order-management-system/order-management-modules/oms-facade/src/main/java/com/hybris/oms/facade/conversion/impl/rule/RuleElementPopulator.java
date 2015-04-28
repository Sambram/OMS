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
import com.hybris.commons.conversion.util.Converters;
import com.hybris.oms.domain.rule.domain.RuleElement;
import com.hybris.oms.domain.rule.domain.RuleParameter;
import com.hybris.oms.service.managedobjects.rule.RuleElementData;
import com.hybris.oms.service.managedobjects.rule.RuleParameterData;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Provides basic logic for converting given {@link RuleElementData} Managed Object into {@link RuleElement} DTO
 * subclasses.
 * This converter does not populate RuleElement.rule property, this has to be handled externally.
 */
public abstract class RuleElementPopulator<S extends RuleElementData, T extends RuleElement> extends AbstractPopulator<S, T>
{
	private Converter<RuleParameterData, RuleParameter> ruleParameterConverter;
	private Converters converters;

	@Override
	public void populate(final S source, final T target) throws ConversionException
	{
		target.setParameters(this.populateParameters(source.getParameters()));
		target.setSequenceNr(source.getSequenceNr());
	}

	protected List<RuleParameter> populateParameters(final List<RuleParameterData> ruleParameterList)
	{
		return this.converters.convertAll(ruleParameterList, this.ruleParameterConverter);
	}

	@Required
	public void setRuleParameterConverter(final Converter<RuleParameterData, RuleParameter> ruleParameterConverter)
	{
		this.ruleParameterConverter = ruleParameterConverter;
	}

	@Required
	public void setConverters(final Converters converters)
	{
		this.converters = converters;
	}
}
