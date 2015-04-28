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
package com.hybris.oms.service.cis.impl;

import com.hybris.cis.api.model.CisOrder;
import com.hybris.cis.api.tax.model.CisTaxDoc;
import com.hybris.cis.api.tax.model.CisTaxLine;
import com.hybris.cis.client.rest.tax.TaxClient;
import com.hybris.cis.client.rest.tax.mock.TaxMockClientImpl;
import com.hybris.commons.client.RestResponse;
import com.hybris.oms.domain.exception.RemoteRequestException;
import com.hybris.oms.service.cis.CisConverter;
import com.hybris.oms.service.cis.CisService;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.managedobjects.types.AmountVT;
import com.hybris.oms.service.managedobjects.types.PriceVT;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.shipment.ShipmentService;
import com.hybris.oms.service.shipment.impl.ShipmentDataStaticUtils;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


public class TaxService
{

	private static final Logger LOG = LoggerFactory.getLogger(TaxService.class);
	private TaxClient taxClient;

	private CisConverter cisConverter;
	private ShipmentService shipmentService;
	private OrderService orderService;


	/**
	 * Invoice taxes for a given shipment. Sets the taxes committed in the shipment.
	 * 
	 * @param shipment
	 * @throws RemoteRequestException
	 */
	public void invoiceTaxes(final ShipmentData shipment)
	{
		final CisOrder cisOrder = convertToCisParamFormat(shipment);
		final RestResponse<CisTaxDoc> result = callCisToInvoice(shipment, cisOrder);

		final Double[] merchs = calculateMerchandiseTax(result);
		final double total = merchs[0];
		final double shippingTaxes = merchs[1];
		setmerchandisePriceTaxCommitted(shipment, total);
		setShippingPriceTaxCommitted(shipment, shippingTaxes);
		setInvoiceLocationInFirstShipment(result.getLocation(), shipment);
	}

	protected void setInvoiceLocationInFirstShipment(final URI location, final ShipmentData shipmentData)
	{
		final List<ShipmentData> shipmentList = this.shipmentService.findShipmentsByOrder(shipmentData.getOrderFk());
		if (shipmentList == null || shipmentList.isEmpty())
		{
			throw new IllegalArgumentException("Shipment List for order " + shipmentData.getOrderFk().getOrderId()
					+ " can't be null or empty");
		}
		for (final ShipmentData shipment : shipmentList)
		{
			if (ShipmentDataStaticUtils.isFirstShipmentForOrder(shipment))
			{
				shipment.setLocation(location.toString());
			}
		}
	}


	public void revertTax(final ReturnData aReturn)
	{
		final RestResponse<CisTaxDoc> result = callCisToRevertTax(aReturn);

		final Double[] merchs = calculateMerchandiseTax(result);
		final double totalTax = merchs[0];

		setTotalTaxToFirstPaymentInfoOfReturn(aReturn, totalTax);

	}

	protected void setTotalTaxToFirstPaymentInfoOfReturn(final ReturnData aReturn, final double totalTax)
	{
		aReturn.getReturnPaymentInfos().setTaxReversed(new AmountVT(getFirstLineCurrencyCode(aReturn), totalTax));
	}

	protected String getFirstLineCurrencyCode(final ReturnData aReturn)
	{
		return orderService.getOrderLineByOrderIdAndOrderLineId(aReturn.getOrder().getOrderId(),
				aReturn.getReturnOrderLines().get(0).getOrderLineId()).getUnitTaxCurrencyCode();
	}

	protected RestResponse<CisTaxDoc> callCisToRevertTax(final ReturnData aReturn)
	{
		RestResponse<CisTaxDoc> result;
		final CisOrder cisOrder = convertReturnToCisParamFormat(aReturn);
		final URI cisURI = fetchCisURILocation(aReturn);
		try
		{
			result = this.taxClient.adjust(buildReturnRefForTaxClient(aReturn), cisURI, cisOrder);
		}
		catch (final RuntimeException e)
		{
			throw new RemoteRequestException("An error occurred during communication with CIS :" + e.getMessage(), e);
		}

		return result;
	}

	protected String buildReturnRefForTaxClient(final ReturnData aReturn)
	{
		return aReturn.getOrder().getOrderId().concat(".").concat(String.valueOf(aReturn.getReturnId()));
	}

	protected URI fetchCisURILocation(final ReturnData aReturn)
	{
		URI cisURI = URI.create("");
		final List<ShipmentData> shipmentList = this.shipmentService.findShipmentsByOrder(aReturn.getOrder());
		if (shipmentList == null || shipmentList.isEmpty())
		{
			throw new IllegalArgumentException("Shipment List for order " + aReturn.getOrder().getOrderId()
					+ " can't be null or empty");
		}
		for (final ShipmentData shipment : shipmentList)
		{
			if (ShipmentDataStaticUtils.isFirstShipmentForOrder(shipment))
			{
				cisURI = URI.create(shipment.getLocation());
			}
		}
		return cisURI;
	}

	protected CisOrder convertToCisParamFormat(final ShipmentData shipment)
	{
		return this.cisConverter.convertOmsShipmentToCisOrder(shipment);
	}

	protected CisOrder convertReturnToCisParamFormat(final ReturnData aReturn)
	{
		return this.cisConverter.convertOmsReturnToCisOrder(aReturn);
	}

	protected RestResponse<CisTaxDoc> callCisToInvoice(final ShipmentData shipment, final CisOrder cisOrder)
	{
		RestResponse<CisTaxDoc> result = null;
		try
		{
			result = this.taxClient.invoice(
					shipment.getOrderFk().getOrderId().concat(".").concat(Long.toString(shipment.getShipmentId())), cisOrder);
		}
		catch (final RuntimeException e)
		{
			throw new RemoteRequestException("An error occurred during communication with CIS :" + e.getMessage(), e);
		}
		return result;
	}

	/**
	 * Calculate shipment merchandise tax committed: Equals the (sum of all line item tax totals) - (line item tax total
	 * for shipping taxes).
	 */
	protected Double[] calculateMerchandiseTax(final RestResponse<CisTaxDoc> result)
	{
		double total = 0d;
		double shippingTaxes = 0d;
		if (result != null)
		{
			for (final CisTaxLine lineItem : result.getResult().getLineItems())
			{
				if (!lineItem.getId().equals(Integer.toString(CisService.INVOICE_TAX_SHIPPING_LINE_ITEM_ID)))
				{
					total += lineItem.getTotalTax().doubleValue();
				}
				else
				{
					shippingTaxes = lineItem.getTotalTax().doubleValue();
				}
			}
		}
		return new Double[]{total, shippingTaxes};
	}

	protected void setmerchandisePriceTaxCommitted(final ShipmentData shipment, final double total)
	{
		final AmountVT shipmentMerchandiseTaxCommitted = new AmountVT(shipment.getMerchandisePrice().getSubTotalCurrencyCode(),
				total);
		final PriceVT merchandisePrice = changeTaxCommitedForPrice(shipment.getMerchandisePrice(), shipmentMerchandiseTaxCommitted);
		shipment.setMerchandisePrice(merchandisePrice);
	}

	/**
	 * If shipment is first shipment then set its shipping tax committed From the line item with the
	 * INVOICE_TAX_SHIPPING_LINE_ITEM_ID.
	 */
	protected void setShippingPriceTaxCommitted(final ShipmentData shipment, final double shippingTaxes)
	{
		if (ShipmentDataStaticUtils.isFirstShipmentForOrder(shipment))
		{
			final AmountVT shipmentShippingTaxCommitted = new AmountVT(shipment.getShippingAndHandling().getShippingPrice()
					.getSubTotalCurrencyCode(), shippingTaxes);
			final PriceVT shippingPrice = changeTaxCommitedForPrice(shipment.getShippingAndHandling().getShippingPrice(),
					shipmentShippingTaxCommitted);
			shipment.getShippingAndHandling().setShippingPrice(shippingPrice);
		}
	}

	protected PriceVT changeTaxCommitedForPrice(final PriceVT source, final AmountVT taxCommited)
	{
		return new PriceVT(source.getSubTotalCurrencyCode(), source.getSubTotalValue(), source.getTaxCurrencyCode(),
				source.getTaxValue(), taxCommited.getCurrencyCode(), taxCommited.getValue());
	}

	protected CisConverter getCisConverter()
	{
		return cisConverter;
	}

	protected TaxClient getTaxClient()
	{
		return taxClient;
	}

	protected ShipmentService getShipmentService()
	{
		return shipmentService;
	}

	protected OrderService getOrderService()
	{
		return orderService;
	}

	@Required
	public void setCisConverter(final CisConverter cisConverter)
	{
		this.cisConverter = cisConverter;
	}

	@Required
	public void setTaxClient(final TaxClient taxClient)
	{
		this.taxClient = taxClient;
	}

	@Required
	public void setShipmentService(final ShipmentService shipmentService)
	{
		this.shipmentService = shipmentService;
	}

	@Required
	public void setOrderService(final OrderService orderService)
	{
		this.orderService = orderService;
	}

	public boolean isClientMockUsed()
	{
		boolean b = false;

		try
		{
			b = (this.taxClient instanceof TaxMockClientImpl);

		}
		catch (final Exception e)
		{
			LOG.warn("exception while checking if TaxService use a clientMock. Return false.", e);
		}
		return b;
	}
}
