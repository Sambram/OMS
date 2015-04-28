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
package com.hybris.oms.service.health.impl;

import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.api.Restrictions;
import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.oms.service.inventory.InventoryServiceConstants;
import com.hybris.oms.service.managedobjects.inventory.BinData;
import com.hybris.oms.service.managedobjects.inventory.CurrentItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.ItemStatusData;
import com.hybris.oms.service.managedobjects.inventory.LocationRole;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;
import com.hybris.oms.service.managedobjects.order.PaymentInfoData;
import com.hybris.oms.service.managedobjects.shipment.ShippingAndHandlingData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.managedobjects.types.PriceVT;
import com.hybris.oms.service.order.business.builders.OrderLineBuilder;

import java.util.Calendar;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;


/**
 * Helper class for the {@link DefaultHealthServiceTest} integration test.
 */
public class HealthServiceTestDataBuilder
{

	@Autowired
	private PersistenceManager persistenceManager;

	/**
	 * Creates locations, inventories, bins, base stores and item quantities.
	 */
	public void createInventoryData()
	{

		final StockroomLocationData location1 = buildLocationData("loc1");
		final StockroomLocationData location2 = buildLocationData("loc2");

		final BinData bin1 = buildBinData(location1);
		final BinData bin2 = buildBinData(location2);

		final ItemLocationData inventory1 = buildItemLocationData("sku1", location1, bin2);
		final ItemLocationData inventory2 = buildItemLocationData("sku2", location1, bin1);
		buildItemLocationData("sku1", location2, bin1);

		buildItemStatusData("status1");
		buildItemStatusData("status2");

		buildItemQuantityData(13, inventory1, "status1");
		buildItemQuantityData(15, inventory1, "status2");
		buildItemQuantityData(12, inventory2, "status1");
	}

	/**
	 * Creates more locations, inventories, bins, base stores and item quantities.
	 */
	public void createMoreInventoryData()
	{
		final StockroomLocationData location3 = buildLocationData("loc3");
		final BinData bin3 = buildBinData(location3);

		final ItemLocationData inventory2 = buildItemLocationData("sku3", location3, bin3);
		buildItemQuantityData(12, inventory2, "status1");
	}

	/**
	 * Updates one inventory.
	 */
	public void updateInventory()
	{
		final CurrentItemQuantityData inventory = persistenceManager.createCriteriaQuery(CurrentItemQuantityData.class)
				.resultList().get(0);// get any inventory

		inventory.setQuantityValue(50);

	}

	/**
	 * Creates an order with a single order line quantity of given status in given location
	 * 
	 * @param orderId the order id unique for the tenant
	 * @param orderLineId the order line unique for the order
	 * @param orderLineQuantityId the order line quantity unique for the tenant
	 * @param statusCode the status code
	 * @param locationId location id
	 * @return order
	 */
	public OrderData createOrder(final String orderId, final String orderLineId, final long orderLineQuantityId,
			final String statusCode, final String locationId)
	{
		final OrderData order = this.persistenceManager.create(OrderData.class);
		order.setOrderId(orderId);

		order.setUsername("someUser");
		order.setFirstName("firstName");
		order.setLastName("lastName");
		order.setEmailid("test@test.de");

		order.setIssueDate(Calendar.getInstance().getTime());
		order.setShippingAddress(new AddressVT("test", "test", "test", "test", "test", 0d, 0d, "test", "test", null, null));
		order.setShippingMethod("test");

		final OrderLineData orderLine = buildOrderLine(orderLineId);
		orderLine.setMyOrder(order);
		order.setOrderLines(Lists.newArrayList(orderLine));
		OrderLineQuantityStatusData olqStatus;

		try
		{
			olqStatus = persistenceManager.createCriteriaQuery(OrderLineQuantityStatusData.class)
					.where(Restrictions.eq(OrderLineQuantityStatusData.STATUSCODE, statusCode)).uniqueResult();
		}
		catch (final ManagedObjectNotFoundException e)
		{
			olqStatus = buildOrderLineQuantityStatus(statusCode);
		}
		final OrderLineQuantityData orderLineQuantity = buildOrderLineQuantity(orderLineQuantityId, olqStatus, orderLine,
				locationId);
		orderLine.setOrderLineQuantities(Collections.singletonList(orderLineQuantity));

		final ShippingAndHandlingData shippingAndHandlingData = this.persistenceManager.create(ShippingAndHandlingData.class);
		shippingAndHandlingData.setOrderId(order.getOrderId());
		shippingAndHandlingData.setShippingPrice(new PriceVT("test", 0d, "test", 0d, "test", 0d));
		order.setShippingAndHandling(shippingAndHandlingData);


		final PaymentInfoData paymentInfo = this.persistenceManager.create(PaymentInfoData.class);
		paymentInfo.setBillingAddress(new AddressVT("test", "test", "test", "test", "test", 0d, 0d, "test", "test", null, null));
		paymentInfo.setMyOrder(order);
		paymentInfo.setPaymentInfoType("test");
		paymentInfo.setAuthUrl("test");
		order.setPaymentInfos(Lists.newArrayList(paymentInfo));

		persistenceManager.flush();

		return order;
	}

	/**
	 * Creates an order line with given id.
	 * 
	 * @param orderLineId order line id
	 * @return order line
	 */
	private OrderLineData buildOrderLine(final String orderLineId)
	{
		final OrderLineData orderLine = this.persistenceManager.create(OrderLineData.class);

		orderLine.setOrderLineId(orderLineId);
		orderLine.setSkuId("test");
		OrderLineBuilder.setQuantitiesAndUnits(orderLine, "test", 0);
		orderLine.setTaxCategory("test");
		orderLine.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING.getCode()));

		return orderLine;
	}

	/**
	 * Creates an order line quantity with given id and status, for given order line and location
	 * 
	 * @param orderLineQuantityId
	 * @param orderLineQuantityStatus
	 * @param orderLine
	 * @param locationId
	 * @return order line quantity
	 */
	private OrderLineQuantityData buildOrderLineQuantity(final long orderLineQuantityId,
			final OrderLineQuantityStatusData orderLineQuantityStatus, final OrderLineData orderLine, final String locationId)
	{
		final OrderLineQuantityData orderLineQuantity = this.persistenceManager.create(OrderLineQuantityData.class);
		orderLineQuantity.setOlqId(orderLineQuantityId);
		orderLineQuantity.setStatus(orderLineQuantityStatus);
		orderLineQuantity.setOrderLine(orderLine);
		orderLineQuantity.setStockroomLocationId(locationId);
		orderLineQuantity.setQuantityValue(10);
		return orderLineQuantity;
	}

	/**
	 * Creates order line quantity status.
	 * 
	 * @param statusCode status code
	 * @return order line quantity status
	 */
	private OrderLineQuantityStatusData buildOrderLineQuantityStatus(final String statusCode)
	{
		final OrderLineQuantityStatusData orderLineQuantityStatus = this.persistenceManager
				.create(OrderLineQuantityStatusData.class);
		orderLineQuantityStatus.setStatusCode(statusCode);
		return orderLineQuantityStatus;
	}


	/**
	 * Creates bin data.
	 * 
	 * @param location location
	 * @return bin data
	 */
	private BinData buildBinData(final StockroomLocationData location)
	{
		final BinData bin = this.persistenceManager.create(BinData.class);
		bin.setBinCode(InventoryServiceConstants.DEFAULT_BIN);
		bin.setStockroomLocation(location);
		bin.setPriority(1);
		return bin;
	}

	/**
	 * Creates item quantity data.
	 * 
	 * @param quantity item quantity
	 * @param inventory inventory
	 * @param status inventory status
	 * @return item quantity data
	 */
	private CurrentItemQuantityData buildItemQuantityData(final int quantity, final ItemLocationData inventory, final String status)
	{
		final CurrentItemQuantityData quantityData = this.persistenceManager.create(CurrentItemQuantityData.class);
		quantityData.setQuantityValue(quantity);
		quantityData.setOwner(inventory);
		quantityData.setStatusCode(status);
		return quantityData;
	}

	/**
	 * Creates item status data.
	 * 
	 * @param statusId status id
	 * @return item status data
	 */
	private ItemStatusData buildItemStatusData(final String statusId)
	{
		final ItemStatusData status = this.persistenceManager.create(ItemStatusData.class);
		status.setStatusCode(statusId);
		status.setDescription(statusId);
		return status;
	}

	/**
	 * Creates inventory data.
	 * 
	 * @param skuId sku (product) id
	 * @param location location
	 * @param bin bin
	 * @return inventory data
	 */
	private ItemLocationData buildItemLocationData(final String skuId, final StockroomLocationData location, final BinData bin)
	{
		final ItemLocationData inventory = this.persistenceManager.create(ItemLocationData.class);
		inventory.setItemId(skuId);
		inventory.setStockroomLocation(location);
		inventory.setFuture(false);
		inventory.setBin(bin);
		return inventory;
	}

	/**
	 * Creates stockroom location data.
	 * 
	 * @param locationId location id
	 * @return stockroom location
	 */
	private StockroomLocationData buildLocationData(final String locationId)
	{
		final StockroomLocationData location = this.persistenceManager.create(StockroomLocationData.class);
		location.setLocationId(locationId);
		location.setLocationRoles(Collections.singleton(LocationRole.PICKUP.getCode()));
		return location;
	}
}
