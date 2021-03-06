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
package com.hybris.oms.facade.order;

import com.hybris.commons.conversion.Converter;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.engine.jdbc.impl.JdbcPersistenceEngine;
import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.api.order.OrderFacade;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.locationrole.LocationRole;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.order.PaymentInfo;
import com.hybris.oms.domain.preference.TenantPreference;
import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.domain.shipping.ShippingAndHandling;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.domain.types.Price;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.service.managedobjects.i18n.CountryData;
import com.hybris.oms.service.managedobjects.i18n.CurrencyData;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;
import com.hybris.oms.service.managedobjects.preference.TenantPreferenceData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.preference.TenantPreferenceService;
import com.hybris.oms.service.shipment.ShipmentService;
import com.hybris.oms.service.util.OmsTestUtils;

import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableSet;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/oms-facade-spring-test.xml"})
@SuppressWarnings({"PMD.ExcessiveImports"})
public class CurrencySupportTest
{
	private static final String ISO4217_CURRENCY = Currency.getInstance(Locale.CANADA_FRENCH).toString();
	private static final String LOCATION_ID = "locationId";
	private static final String MANDATORY_FIELD_VALUE = "CurrencySupportTest";
	private static final String ORDER_ID = "orderId";
	@Resource(name = "inventoryServiceFacade")
	private InventoryFacade inventoryServices;
	@Resource(name = "orderServicesFacade")
	private OrderFacade orderServices;
	@Autowired
	private OrderService orderService;
	@Autowired
	private PersistenceManager persistenceManager;
	@Autowired
	private ShipmentService shipmentService;
	@Autowired
	private TenantPreferenceService tenantPreferenceService;
	@Autowired
	private Converter<TenantPreference, TenantPreferenceData> tenantPreferenceReverseConverter;
	@Resource
	private JdbcPersistenceEngine persistenceEngine;

	@Before
	public void createTestData()
	{
		final CurrencyData currencyData = persistenceManager.create(CurrencyData.class);
		currencyData.setCode(ISO4217_CURRENCY);
		currencyData.setName(ISO4217_CURRENCY);
		final CountryData countryData = persistenceManager.create(CountryData.class);
		countryData.setCode("US");
		countryData.setName("US");

		final TenantPreferenceData tenantPreferenceData = persistenceManager.create(TenantPreferenceData.class);
		tenantPreferenceData.setProperty(TenantPreferenceConstants.PREF_KEY_OLQSTATUS_CANCELED);
		tenantPreferenceData.setValue("CANCELLED");

		final TenantPreferenceData tenantPreferenceData2 = persistenceManager.create(TenantPreferenceData.class);
		tenantPreferenceData2.setProperty(TenantPreferenceConstants.PREF_KEY_OLQSTATUS_TAX_INVOICED);
		tenantPreferenceData2.setValue("TAX_INVOICED");

		final TenantPreferenceData tenantPreferenceData3 = persistenceManager.create(TenantPreferenceData.class);
		tenantPreferenceData3.setProperty(TenantPreferenceConstants.PREF_KEY_OLQSTATUS_PAYMENT_CAPTURED);
		tenantPreferenceData3.setValue("PAYMENT_CAPTURED");

		final TenantPreferenceData tenantPreferenceData4 = persistenceManager.create(TenantPreferenceData.class);
		tenantPreferenceData4.setProperty(TenantPreferenceConstants.PREF_KEY_OLQSTATUS_SHIPPED);
		tenantPreferenceData4.setValue("SHIPPED");

		final TenantPreferenceData tenantPreferenceData5 = persistenceManager.create(TenantPreferenceData.class);
		tenantPreferenceData5.setProperty(TenantPreferenceConstants.PREF_KEY_OLQSTATUS_MANUAL_DECLINED);
		tenantPreferenceData5.setValue("MANUAL_DECLINED");

		final TenantPreferenceData tenantPreferenceData6 = persistenceManager.create(TenantPreferenceData.class);
		tenantPreferenceData6.setProperty(TenantPreferenceConstants.PREF_KEY_SHIPPING_WITHIN_DAYS);
		tenantPreferenceData6.setValue("2");

		final OrderLineQuantityStatusData orderLineQuantityStatusData = persistenceManager
				.create(OrderLineQuantityStatusData.class);
		orderLineQuantityStatusData.setStatusCode("CANCELLED");

		final OrderLineQuantityStatusData orderLineQuantityStatusData2 = persistenceManager
				.create(OrderLineQuantityStatusData.class);
		orderLineQuantityStatusData2.setStatusCode("TAX_INVOICED");

		final OrderLineQuantityStatusData orderLineQuantityStatusData3 = persistenceManager
				.create(OrderLineQuantityStatusData.class);
		orderLineQuantityStatusData3.setStatusCode("SHIPPED");

		final OrderLineQuantityStatusData orderLineQuantityStatusData4 = persistenceManager
				.create(OrderLineQuantityStatusData.class);
		orderLineQuantityStatusData4.setStatusCode("PAYMENT_CAPTURED");

		final OrderLineQuantityStatusData orderLineQuantityStatusData5 = persistenceManager
				.create(OrderLineQuantityStatusData.class);
		orderLineQuantityStatusData5.setStatusCode("MANUAL_DECLINED");

		persistenceManager.flush();
	}

	@After
	public void tearDown()
	{
		OmsTestUtils.cleanUp(persistenceEngine);
	}

	/**
	 * This is a very important test for currency support within the system.
	 */
	@Transactional
	@Test
	public void testIso4217CurrencySupport()
	{
		this.prepareTenantData();

		final Order order = new Order();
		final OrderLine line = new OrderLine();
		final PaymentInfo paymentInfo = new PaymentInfo();
		order.addOrderLine(line);
		order.addPaymentInfo(paymentInfo);

		// set a Currency to the Order
		order.setCurrencyCode(ISO4217_CURRENCY);
		line.setUnitPrice(new Amount(ISO4217_CURRENCY, 1.0d));
		line.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		order.setOrderId(ORDER_ID);

		this.mandatoryFields(order, line, paymentInfo);
		// save the Order
		this.orderServices.createOrder(order);
		this.addOlqsToOrderData(ORDER_ID);
		// create Shipment
		final List<ShipmentData> createdShipments = this.shipmentService.createShipmentsForOrder(this.orderService
				.getOrderByOrderId(ORDER_ID));

		// has the Order currency been transferred to the Order?
		Assert.assertEquals(ISO4217_CURRENCY, createdShipments.get(0).getMerchandisePrice().getSubTotalCurrencyCode());
	}

	private void createTenantPreference(final TenantPreference tenantPreference)
	{
		this.tenantPreferenceReverseConverter.convert(tenantPreference);
		this.tenantPreferenceService.flush();
	}

	private void mandatoryFields(final Order order, final OrderLine line, final PaymentInfo paymentInfo)
	{
		final Address address = new Address("2207 7th Avenue", "a", "New York", "NY", "10027", 40.812356, -73.945857, "US",
				"U.S.A.", "aa", "11");
		order.setUsername(MANDATORY_FIELD_VALUE);
		order.setEmailid(MANDATORY_FIELD_VALUE);
		order.setShippingMethod(MANDATORY_FIELD_VALUE);
		order.setFirstName(MANDATORY_FIELD_VALUE);
		order.setLastName(MANDATORY_FIELD_VALUE);
		order.setShippingAddress(address);
		order.setIssueDate(new Date());
		order.setShippingAndHandling(this.createShippingAndHandling(order.getOrderId()));
		order.setShippingFirstName(MANDATORY_FIELD_VALUE);
		order.setShippingLastName(MANDATORY_FIELD_VALUE);
		order.setShippingTaxCategory(MANDATORY_FIELD_VALUE);
		line.setSkuId(MANDATORY_FIELD_VALUE);
		line.setOrderLineId(MANDATORY_FIELD_VALUE);
		line.setTaxCategory(MANDATORY_FIELD_VALUE);
		line.setUnitPrice(new Amount(ISO4217_CURRENCY, 1.0d));
		line.setUnitTax(new Amount(ISO4217_CURRENCY, 1.0d));
		line.setQuantity(new Quantity("piece", 1));
		line.setQuantityUnassigned(new Quantity("piece", 1));
		paymentInfo.setAuthUrl(MANDATORY_FIELD_VALUE);
		paymentInfo.setBillingAddress(address);
		paymentInfo.setPaymentInfoType(MANDATORY_FIELD_VALUE);
	}

	private void prepareTenantData()
	{
		final Address address = new Address();
		address.setAddressLine1("addressline1");

		final Location fulfillmentCenter = new Location();
		fulfillmentCenter.setAddress(address);
		fulfillmentCenter.setLocationId(LOCATION_ID);
		fulfillmentCenter.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		this.inventoryServices.createStockRoomLocation(fulfillmentCenter);

		final TenantPreference tenantPreference = new TenantPreference();
		tenantPreference.setProperty("olqstatus.allocated");
		tenantPreference.setValue(MANDATORY_FIELD_VALUE);
		this.createTenantPreference(tenantPreference);
	}

	private void addOlqsToOrderData(final String orderId)
	{
		final OrderData orderData = this.orderService.getOrderByOrderId(orderId);
		final OrderLineData orderLineData = orderData.getOrderLines().get(0);

		final OrderLineQuantityStatusData orderLineQuantityStatusData = this.persistenceManager
				.create(OrderLineQuantityStatusData.class);
		orderLineQuantityStatusData.setStatusCode(MANDATORY_FIELD_VALUE);

		final OrderLineQuantityData orderLineQuantityData = this.persistenceManager.create(OrderLineQuantityData.class);
		orderLineQuantityData.setStockroomLocationId(LOCATION_ID);
		orderLineQuantityData.setQuantityValue(234);
		orderLineQuantityData.setStatus(orderLineQuantityStatusData);
		orderLineQuantityData.setOrderLine(orderLineData);

		this.persistenceManager.flush();
		this.persistenceManager.refresh(orderLineData);
	}

	private ShippingAndHandling createShippingAndHandling(final String orderId)
	{
		final ShippingAndHandling shippingAndHandling = new ShippingAndHandling();
		shippingAndHandling.setOrderId(orderId);
		shippingAndHandling.setShippingPrice(new Price());

		return shippingAndHandling;
	}
}
