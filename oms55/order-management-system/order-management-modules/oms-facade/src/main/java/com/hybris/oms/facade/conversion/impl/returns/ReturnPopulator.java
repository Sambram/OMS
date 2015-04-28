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

import org.springframework.beans.factory.annotation.Required;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.impl.AbstractPopulator;
import com.hybris.commons.conversion.util.Converters;
import com.hybris.oms.domain.returns.Return;
import com.hybris.oms.domain.returns.ReturnOrderLine;
import com.hybris.oms.domain.returns.ReturnPaymentInfo;
import com.hybris.oms.domain.returns.ReturnShipment;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.managedobjects.returns.ReturnOrderLineData;
import com.hybris.oms.service.managedobjects.returns.ReturnPaymentInfoData;
import com.hybris.oms.service.managedobjects.returns.ReturnShipmentData;


public class ReturnPopulator extends AbstractPopulator<ReturnData, Return>
{

	private Converters converters;
	private Converter<ReturnOrderLineData, ReturnOrderLine> returnOrderLineConverter;
	private Converter<ReturnPaymentInfoData, ReturnPaymentInfo> returnPaymentInfoConverter;
	private Converter<ReturnShipmentData, ReturnShipment> returnShipmentConverter;

	@Override
	public void populate(final ReturnData returnDataSrc, final Return returnDtoTarget) throws ConversionException
	{
		returnDtoTarget.setReturnId(String.valueOf(returnDataSrc.getReturnId()));
		returnDtoTarget.setOrderId(returnDataSrc.getOrder().getOrderId());
		returnDtoTarget.setShippingRefunded(returnDataSrc.isShippingRefunded());
		returnDtoTarget.setState(returnDataSrc.getState());
		returnDtoTarget.setReturnReasonCode(returnDataSrc.getReturnReasonCode());

		if (returnDataSrc.getReturnLocation() != null)
		{
			returnDtoTarget.setReturnLocationId(returnDataSrc.getReturnLocation().getLocationId());
		}
		if (returnDataSrc.getCustomRefundAmount() != null)
		{
			returnDtoTarget.setCustomTotalRefundAmount(new Amount(returnDataSrc.getCustomRefundAmount().getCurrencyCode(),
					returnDataSrc.getCustomRefundAmount().getValue()));
		}
		else
		{
			returnDtoTarget.setCustomTotalRefundAmount(new Amount());
	}
		if (returnDataSrc.getCalculatedRefundAmount() != null)
		{
			returnDtoTarget.setCalculatedTotalRefundAmount(new Amount(returnDataSrc.getCalculatedRefundAmount().getCurrencyCode(),
					returnDataSrc.getCalculatedRefundAmount().getValue()));
		}
		else
		{
			returnDtoTarget.setCalculatedTotalRefundAmount(new Amount());
		}


		populateReturnOrderLines(returnDataSrc, returnDtoTarget);
		populateReturnPaymentInfo(returnDataSrc, returnDtoTarget);
		populateReturnShipmentInfo(returnDataSrc, returnDtoTarget);
	}

	protected void populateReturnOrderLines(final ReturnData source, final Return target)
	{
		target.setReturnOrderLines(this.converters.convertAll(source.getReturnOrderLines(), this.returnOrderLineConverter));
	}

	protected void populateReturnPaymentInfo(final ReturnData source, final Return target)
	{
		target.setReturnPaymentInfos(this.returnPaymentInfoConverter.convert(source.getReturnPaymentInfos()));
	}

	protected void populateReturnShipmentInfo(final ReturnData source, final Return target)
	{
		target.setReturnShipment(returnShipmentConverter.convert(source.getReturnShipment()));
	}

	@Required
	public void setConverters(final Converters converters)
	{
		this.converters = converters;
	}

	@Required
	public void setReturnOrderLineConverter(final Converter<ReturnOrderLineData, ReturnOrderLine> returnOrderLineConverter)
	{
		this.returnOrderLineConverter = returnOrderLineConverter;
	}

	@Required
	public void setReturnPaymentInfoConverter(final Converter<ReturnPaymentInfoData, ReturnPaymentInfo> returnPaymentInfoConverter)
	{
		this.returnPaymentInfoConverter = returnPaymentInfoConverter;
	}

	public Converter<ReturnShipmentData, ReturnShipment> getReturnShipmentConverter()
	{
		return returnShipmentConverter;
	}

	@Required
	public void setReturnShipmentConverter(final Converter<ReturnShipmentData, ReturnShipment> returnShipmentConverter)
	{
		this.returnShipmentConverter = returnShipmentConverter;
	}

}
