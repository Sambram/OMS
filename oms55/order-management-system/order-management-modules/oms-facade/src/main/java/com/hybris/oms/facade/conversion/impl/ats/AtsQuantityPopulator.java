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
import com.hybris.oms.domain.ats.AtsQuantity;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.service.ats.AtsResult.AtsRow;

/**
 * 
 * Converts {@link AtsRow} into a {@link AtsQuantity} DTO.
 */
public class AtsQuantityPopulator extends AbstractPopulator<AtsRow, AtsQuantity>
{

	@Override
	public void populate(final AtsRow row, final AtsQuantity atsQuantity) throws ConversionException
	{
		atsQuantity.setAtsId(row.getKey().getAtsId());
		atsQuantity.setSku(row.getKey().getSku());
		atsQuantity.setQuantity(new Quantity("units", row.getValue().intValue()));
	}

}
