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
package com.hybris.oms.facade.conversion.impl.shipment;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.impl.AbstractPopulator;
import com.hybris.oms.domain.shipping.ShipmentDetails;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;


/**
 * Converts {@link ShipmentData} Managed Object into {@link ShipmentDetails} DTO.
 */
public class ShipmentDetailsPopulator extends AbstractPopulator<ShipmentData, ShipmentDetails>
{

	@Override
	public void populate(final ShipmentData source, final ShipmentDetails target) throws ConversionException
	{
		target.setHeightUnitCode(source.getHeightUnitCode());
		target.setHeightValue(source.getHeightValue());
		target.setInsuranceValueAmountValue(source.getInsuranceValueAmountValue());
		target.setLengthValue(source.getLengthValue());
		target.setShippingMethod(source.getShippingMethod());
		target.setWeightUnitCode(source.getGrossWeightUnitCode());
		target.setWeightValue(source.getGrossWeightValue());
		target.setWidthValue(source.getWidthValue());
	}
}
