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
package com.hybris.oms.facade.validation.impl.returns;

import static org.mockito.Mockito.when;

import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderDataPojo;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineDataPojo;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityDataPojo;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusDataPojo;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentDataPojo;
import com.hybris.oms.service.order.OrderService;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
@Ignore
public class ValidateReturnItemsUnitTest
{

	public static final String SHIPPED_STATUS = "SHIPPED";
	public static final String NOT_YET_SHIPPED_STATUS = "NOT_YET_SHIPPED";
	public static final String SKU_ID_1 = "sku1";
	public static final int ONE = 1;
	public static final int HUNDRED = 100;

	public static final String SKU_ID_2 = "sku2";

	@Mock
	private OrderService mockedOrderService;

	@Spy
	@InjectMocks
	private ReturnValidator spyReturnValidator;


	/**
	 * Create a Simple OrderData (Pojo) </br>
	 * - 1 order with 1 orderLine, 1 item ... </br>
	 * - Shipment is confirmed </br>
	 * 
	 * @return
	 */
	public OrderData buildConfirmedSimpleOrder()
	{

		final OrderData orderD = new OrderDataPojo();

		final OrderLineQuantityData sku_1_olqd = newOlqd(ONE, SHIPPED_STATUS);

		final OrderLineData sku_1_old = newOld(SKU_ID_1, sku_1_olqd);
		orderD.setOrderLines(oldList(sku_1_old));

		final ShipmentData shipmentD = newShipmentd(orderD, SHIPPED_STATUS, sku_1_olqd);

		spyReturnValidator.setShipmentList(new ArrayList(Arrays.asList(new ShipmentData[]{shipmentD})));

		when(mockedOrderService.getOrderLinesByShipment(shipmentD)).thenReturn(orderD.getOrderLines());


		return orderD;
	}



	public void addSecondShippedOrderLineToShipment(final OrderData orderD)
	{

		final OrderLineQuantityData sku_2_olqd = newOlqd(HUNDRED, SHIPPED_STATUS);

		final OrderLineData sku_2_old = newOld(SKU_ID_2, sku_2_olqd);
		orderD.getOrderLines().add(sku_2_old);

		final ShipmentData shipmentD = spyReturnValidator.getShipmentList().get(0);
		sku_2_olqd.setShipment(shipmentD);

		when(mockedOrderService.getOrderLinesByShipment(shipmentD)).thenReturn(orderD.getOrderLines());

	}

	/**
	 * @param orderd
	 * @param status
	 * @param olqd if not null, olqd.setShipment(newShipment)
	 * @return
	 */
	public static ShipmentData newShipmentd(final OrderData orderd, final String status, final OrderLineQuantityData olqd)
	{
		final ShipmentData shipmentD = new ShipmentDataPojo();
		shipmentD.setOrderFk(orderd);
		shipmentD.setOlqsStatus(status);

		if (olqd != null)
		{
			olqd.setShipment(shipmentD);
		}

		return shipmentD;
	}


	public static OrderLineData newOld(final String skuId, final OrderLineQuantityData... olqds)
	{
		final OrderLineData old = new OrderLineDataPojo();
		old.setSkuId(skuId);
		old.setOrderLineQuantities(olqdList(olqds));

		return old;
	}

	public static OrderLineQuantityData newOlqd(final int quantity, final String status)
	{
		final OrderLineQuantityData olqd = new OrderLineQuantityDataPojo();
		olqd.setQuantityValue(quantity);
		olqd.setStatus(new OrderLineQuantityStatusDataPojo(status));
		return olqd;
	}

	/**
	 * @return an ArrayList so it is possible to modify its content
	 */
	public static ArrayList<OrderLineQuantityData> olqdList(final OrderLineQuantityData... olqds)
	{
		final ArrayList<OrderLineQuantityData> olqdList = new ArrayList<>();
		for (final OrderLineQuantityData anOlqd : olqds)
		{
			olqdList.add(anOlqd);
		}

		return olqdList;
	}

	/**
	 * @return an ArrayList so it is possible to modify its content
	 */
	public static ArrayList<OrderLineData> oldList(final OrderLineData... olds)
	{
		final ArrayList<OrderLineData> oldList = new ArrayList<>();

		for (final OrderLineData anOld : olds)
		{
			oldList.add(anOld);
		}

		return oldList;
	}

}
