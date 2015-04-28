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
package com.hybris.oms.facade.conversion.impl.returns;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.impl.AbstractPopulator;
import com.hybris.oms.domain.returns.ReturnShipment;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.domain.types.Measure;
import com.hybris.oms.service.managedobjects.returns.ReturnShipmentData;


public class ReturnShipmentPopulator extends AbstractPopulator<ReturnShipmentData, ReturnShipment>
{

	@Override
	public void populate(final ReturnShipmentData source, final ReturnShipment target) throws ConversionException
	{
		target.setGrossWeight(new Measure(source.getGrossWeightUnitCode(), source.getGrossWeightValue()));
		target.setHeight(new Measure(source.getHeightUnitCode(), source.getHeightValue()));
		target.setInsuranceValueAmount(new Amount(source.getInsuranceValueAmountCurrencyCode(), source
				.getInsuranceValueAmountValue()));
		target.setLabelUrl(source.getLabelUrl());
		target.setLength(new Measure(source.getLengthUnitCode(), source.getLengthValue()));
		target.setNote(source.getNote());
		target.setPackageDescription(source.getPackageDescription());
		target.setReturnShipmentId(Long.toString(source.getReturnShipmentId()));
		target.setShippingMethod(source.getShippingMethod());
		target.setTrackingId(source.getTrackingId());
		target.setTrackingUrl(source.getTrackingUrl());
		target.setWidth(new Measure(source.getWidthUnitCode(), source.getWidthValue()));
	}
}
