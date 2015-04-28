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
package com.hybris.oms.facade.conversion.impl.ats;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.impl.AbstractPopulator;
import com.hybris.oms.domain.ats.AtsFormula;
import com.hybris.oms.service.managedobjects.ats.AtsFormulaData;


/**
 * Converts {@link AtsFormulaData} Managed Object into {@link AtsFormula} DTO.
 */
public class AtsFormulaPopulator extends AbstractPopulator<AtsFormulaData, AtsFormula>
{
	@Override
	public void populate(final AtsFormulaData source, final AtsFormula target) throws ConversionException
	{
		target.setAtsId(source.getAtsId());
		target.setDescription(source.getDescription());
		target.setFormula(source.getFormula());
		target.setName(source.getName());
	}

}
