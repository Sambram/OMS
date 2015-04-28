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
 * Populates {@link ShipmentData} Managed Object with data provided by {@link ShipmentDetails} DTO.
 */
public class ShipmentDetailsReversePopulator extends AbstractPopulator<ShipmentDetails, ShipmentData>
{
	/**
	 * Populates target based on source.
	 */
	@Override
	public void populate(final ShipmentDetails source, final ShipmentData target) throws ConversionException
	{
		target.setHeightUnitCode(source.getHeightUnitCode());
		target.setHeightValue(source.getHeightValue());
		target.setInsuranceValueAmountValue(source.getInsuranceValueAmountValue());
		target.setLengthValue(source.getLengthValue());
		target.setShippingMethod(source.getShippingMethod());
		target.setGrossWeightUnitCode(source.getWeightUnitCode());
		target.setGrossWeightValue(source.getWeightValue());
		target.setWidthValue(source.getWidthValue());
	}
}
