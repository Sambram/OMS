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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.Populator;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.oms.domain.returns.Return;
import com.hybris.oms.domain.returns.ReturnOrderLine;
import com.hybris.oms.domain.returns.ReturnPaymentInfo;
import com.hybris.oms.domain.returns.ReturnShipment;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.managedobjects.returns.ReturnOrderLineData;
import com.hybris.oms.service.managedobjects.returns.ReturnPaymentInfoData;
import com.hybris.oms.service.managedobjects.returns.ReturnShipmentData;
import com.hybris.oms.service.managedobjects.types.AmountVT;
import com.hybris.oms.service.managedobjects.types.QuantityVT;
import com.hybris.oms.service.order.OrderService;


public class ReturnReversePopulator implements Populator<Return, ReturnData>
{
	private PersistenceManager persistenceManager;
	private Converter<ReturnOrderLine, ReturnOrderLineData> returnOrderLineReverseConverter;
	private Converter<ReturnShipment, ReturnShipmentData> returnShipmentReverseConverter;
	private Converter<ReturnPaymentInfo, ReturnPaymentInfoData> returnPaymentInfoReverseConverter;
	private Converter<Amount, AmountVT> amountReverseConverter;
	private Converter<Quantity, QuantityVT> quantityReverseConverter;
	private OrderService orderService;

	@Override
	public void populateFinals(final Return source, final ReturnData target) throws ConversionException
	{
		if (source.getReturnId() != null && !source.getReturnId().isEmpty())
		{
			target.setReturnId(Long.parseLong(source.getReturnId()));
		}
		target.setShippingRefunded(source.getShippingRefunded());
	}

	@Override
	public void populate(final Return sourceDto, final ReturnData targetData) throws ConversionException
	{

		if (sourceDto.getCustomTotalRefundAmount() != null)
		{
			targetData.setCustomRefundAmount(new AmountVT(sourceDto.getCustomTotalRefundAmount().getCurrencyCode(), sourceDto
					.getCustomTotalRefundAmount().getValue()));
		}
		if (sourceDto.getCalculatedTotalRefundAmount() != null)
		{
			targetData.setCalculatedRefundAmount(new AmountVT(sourceDto.getCalculatedTotalRefundAmount().getCurrencyCode(),
					sourceDto.getCalculatedTotalRefundAmount().getValue()));
		}
		targetData.setReturnReasonCode(sourceDto.getReturnReasonCode());

		populateReturnLocation(sourceDto, targetData);
		populateReturnPaymentInfos(sourceDto, targetData);
		populateReturnShipment(sourceDto, targetData);
		populateOrderId(sourceDto, targetData);
		populateReturnOrderLines(sourceDto, targetData);
	}


	protected void populateOrderId(final Return source, final ReturnData target)
	{
		final String orderId = source.getOrderId();
		try
		{
			final OrderData orderData = persistenceManager.getByIndex(OrderData.UX_ORDERS_ORDERID, orderId);
			target.setOrder(orderData);
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new ConversionException("Order with Order Id= " + orderId + " doesn't exist!", e);
		}
	}

	protected void populateReturnLocation(final Return source, final ReturnData target)
	{
		final String returnLocationId = source.getReturnLocationId();
		if (returnLocationId != null)
		{
			try
			{
				final StockroomLocationData location = persistenceManager.getByIndex(
						StockroomLocationData.UX_STOCKROOMLOCATIONS_LOCATIONID, returnLocationId);
				target.setReturnLocation(location);
			}
			catch (final ManagedObjectNotFoundException e)
			{
				throw new ConversionException("Location with location id= " + returnLocationId + " doesn't exist.", e);
			}
		}
	}

	protected void populateReturnPaymentInfos(final Return source, final ReturnData target)
	{
		try
		{
			final ReturnPaymentInfoData updateRpiData = findExistingReturnPaymentInfo(source.getReturnPaymentInfos().getId());

			// Update existing object
			updateRpiData.setReturnPaymentAmount(amountReverseConverter.convert(source.getReturnPaymentInfos()
					.getReturnPaymentAmount()));
			updateRpiData.setTaxReversed(amountReverseConverter.convert(source.getReturnPaymentInfos().getTaxReversed()));
			updateRpiData.setReturnPaymentType(source.getReturnPaymentInfos().getReturnPaymentType());
			persistenceManager.createOrUpdate(updateRpiData);
			target.setReturnPaymentInfos(updateRpiData);
		}
		catch (final ManagedObjectNotFoundException e)
		{
			target.setReturnPaymentInfos(this.returnPaymentInfoReverseConverter.convert(source.getReturnPaymentInfos()));
		}
	}

	protected void populateReturnShipment(final Return source, final ReturnData target)
	{
		target.setReturnShipment(returnShipmentReverseConverter.convert(source.getReturnShipment()));
	}

	protected void populateReturnOrderLines(final Return returnDto, final ReturnData returnData)
	{

		final List<ReturnOrderLineData> rolList = new ArrayList<ReturnOrderLineData>();
		for (final ReturnOrderLine rOl : returnDto.getReturnOrderLines())
		{
			try
			{
				final ReturnOrderLineData updateRolData = findExistingReturnOrderLine(returnDto.getReturnId(), rOl.getOrderLine()
						.getSkuId());

				// Update existing object
				updateRolData.setReturnOrderLineStatus(rOl.getReturnOrderLineStatus());
				updateRolData.setQuantity(quantityReverseConverter.convert(rOl.getQuantity()));
				updateRolData.setOrderLineId(rOl.getOrderLine().getOrderLineId());
				persistenceManager.createOrUpdate(updateRolData);
				rolList.add(updateRolData);
			}
			catch (final ManagedObjectNotFoundException e)
			{
				// Object not found, it will be created
				if (rOl.getQuantity() != null && rOl.getQuantity().getValue() != 0)
				{
					final ReturnOrderLineData createRolData = returnOrderLineReverseConverter.convert(rOl);
					createRolData.setOrderLineId(rOl.getOrderLine().getOrderLineId());
					createRolData.setMyReturn(returnData);
					persistenceManager.createOrUpdate(createRolData);
					rolList.add(createRolData);
				}
			}

		}

		if (!rolList.isEmpty())
		{
			returnData.setReturnOrderLines(rolList);
		}
	}

	protected ReturnData findExistingReturn(final String returnId) throws ManagedObjectNotFoundException
	{
		if (returnId == null)
		{
			throw new ManagedObjectNotFoundException();
		}
		return this.persistenceManager.getByIndex(ReturnData.UX_RETURNS_RETURNID, Long.parseLong(returnId));
	}

	protected ReturnPaymentInfoData findExistingReturnPaymentInfo(final String returnPaymentInfoId)
			throws ManagedObjectNotFoundException
	{
		if (returnPaymentInfoId == null)
		{
			throw new ManagedObjectNotFoundException();
		}
		return this.persistenceManager.getByIndex(ReturnPaymentInfoData.UX_RETURNPAYMENTINFO_RETURNPAYMENTINFOID,
				Long.parseLong(returnPaymentInfoId));
	}

	protected ReturnOrderLineData findExistingReturnOrderLine(final String returnId, final String skuId)
			throws ManagedObjectNotFoundException
	{

		final ReturnData returnData = findExistingReturn(returnId);

		ReturnOrderLineData updateRolData = null;
		for (final ReturnOrderLineData existingRol : returnData.getReturnOrderLines())
		{
			final OrderLineData orderLineData = orderService.getOrderLineByOrderIdAndOrderLineId(returnData.getOrder().getOrderId(),
					existingRol.getOrderLineId());
			if (orderLineData.getSkuId().equals(skuId))
			{
				updateRolData = existingRol;
			}
		}

		if (updateRolData == null)
		{
			throw new ManagedObjectNotFoundException();
		}

		return updateRolData;
	}

	@Required
	public void setPersistenceManager(final PersistenceManager persistenceManager)
	{
		this.persistenceManager = persistenceManager;
	}

	@Required
	public void setReturnOrderLineReverseConverter(
			final Converter<ReturnOrderLine, ReturnOrderLineData> returnOrderLineReverseConverter)
	{
		this.returnOrderLineReverseConverter = returnOrderLineReverseConverter;
	}

	@Required
	public void setReturnPaymentInfoReverseConverter(
			final Converter<ReturnPaymentInfo, ReturnPaymentInfoData> returnPaymentInfoReverseConverter)
	{
		this.returnPaymentInfoReverseConverter = returnPaymentInfoReverseConverter;
	}

	@Required
	public void setAmountReverseConverter(final Converter<Amount, AmountVT> amountReverseConverter)
	{
		this.amountReverseConverter = amountReverseConverter;
	}

	@Required
	public void setQuantityReverseConverter(final Converter<Quantity, QuantityVT> quantityReverseConverter)
	{
		this.quantityReverseConverter = quantityReverseConverter;
	}

	@Required
	public void setOrderService(final OrderService orderService)
	{
		this.orderService = orderService;
	}

	public Converter<ReturnShipment, ReturnShipmentData> getReturnShipmentReverseConverter()
	{
		return returnShipmentReverseConverter;
	}

	@Required
	public void setReturnShipmentReverseConverter(
			final Converter<ReturnShipment, ReturnShipmentData> returnShipmentReverseConverter)
	{
		this.returnShipmentReverseConverter = returnShipmentReverseConverter;
	}
}
