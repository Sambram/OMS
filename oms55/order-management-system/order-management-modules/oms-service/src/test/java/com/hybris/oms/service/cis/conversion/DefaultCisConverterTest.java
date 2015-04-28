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
package com.hybris.oms.service.cis.conversion;

import com.hybris.cis.api.avs.model.AvsResult;
import com.hybris.cis.api.geolocation.model.CisLocationRequest;
import com.hybris.cis.api.model.CisAddress;
import com.hybris.cis.api.model.CisAddressType;
import com.hybris.cis.api.model.CisDecision;
import com.hybris.cis.api.model.CisLineItem;
import com.hybris.cis.api.model.CisOrder;
import com.hybris.cis.api.payment.model.CisPaymentRequest;
import com.hybris.cis.api.payment.model.CisPaymentTransactionResult;
import com.hybris.cis.api.shipping.model.CisShipment;
import com.hybris.kernel.api.HybrisId;
import com.hybris.kernel.api.ImportService;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.utils.ClasspathScanningResourceFetcher;
import com.hybris.kernel.utils.ResourceFetcher;
import com.hybris.oms.service.cis.CisService;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.inventory.impl.DefaultInventoryService;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.managedobjects.types.AmountVT;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.order.impl.DefaultOrderService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-service-cis-spring-test.xml"})
@SuppressWarnings({"PMD.ExcessiveImports"})
public class DefaultCisConverterTest
{
	private static final String ADDRESS_CITY_NAME = "testAddressCityName";
	private static final String ADDRESS_COUNTRY = "testAddressCountry";
	private static final String ADDRESS_COUNTRY_SUBENTITY = "testAddressCountrySubEntity";
	private static final String ADDRESS_LINE_1 = "testAddressLine1";
	private static final String ADDRESS_LINE_2 = "testAddressLine2";
	private static final String ADDRESS_LINE_3 = "testAddressLine3";
	private static final String ADDRESS_LINE_4 = "testAddressLine4";
	private static final String ADDRESS_POSTAL_ZONE = "testAddressPostalZone";
	private static final String ADDRESS_STREET_NAME = "testAddressStreetName";
	private static final String COMPANY_NAME = "companyName";
	private static final String HYBRIS_ID_LOCATION = "single|StockroomLocationData|1";
	private static final String HYBRIS_ID_SHIPMENT = "single|ShipmentData|1";
	private static final String HYBRIS_ID_FIRST_SHIPMENT = "single|ShipmentData|9";
	private static final String HYBRIS_ID_SECOND_SHIPMENT = "single|ShipmentData|10";
	private static final String LATITUDE_1 = "12";
	private static final String LONGITUDE_1 = "1";
	private static final String ORDER_FIRST_NAME = "orderFirstName";
	private static final String ORDER_HYBRIS_ID = "single|OrderData|1";
	private static final String ORDER_LAST_NAME = "orderLastName";
	private static final String PHONE_NUMBER = "0123456789";
	private static final String SHIPMENT_HYBRIS_ID = "single|ShipmentData|1";
	private static final String SHIPMENT_METHOD = "STANDARD";
	private static final String ZIP_CODE_1 = "testZipCode1";
	private static final String ZIP_CODE_2 = "testZipCode2";
	private static final String GROSS_WEIGHT_CODE = "KG";

	private AmountVT amount;
	private CisAddress cisAddress;
	private CisAddress cisAddress2;

	@InjectMocks
	private final CisAddressConverter cisAddressConverter = new CisAddressConverter();
	@InjectMocks
	private final DefaultCisConverter cisConverter = new DefaultCisConverter();
	@InjectMocks
	private final CisPaymentConverter cisPaymentConverter = new CisPaymentConverter();

	private CisPaymentRequest cisPaymentRequest;

	@InjectMocks
	private final CisShipmentConverter cisShipmentConverter = new CisShipmentConverter();

	private final ResourceFetcher fetcher = new ClasspathScanningResourceFetcher();

	@Autowired
	private ImportService importService;
	@Mock
	private final InventoryService inventoryService = new DefaultInventoryService();

	private StockroomLocationData locationData;
	private AddressVT omsAddress;
	private OrderData orderData;

	@Mock
	private final OrderService orderService = new DefaultOrderService();
	@Autowired
	private PersistenceManager persistenceManager;

	private ShipmentData shipmentData;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/shipment/test-shipment-data-import.mcsv")[0]);
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/order/test-order-data-import.mcsv")[0]);
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/inventory/test-inventory-data-import.mcsv")[0]);

		this.populateOmsAddress();
		this.populateCisAddress();
		this.populateAvsResult();
		this.populatePayment();
		this.populateLocation();
		this.populateCisAddress2();
		this.populateOmsShipmentAndOrder();
		cisShipmentConverter.setCisAddressConverter(cisAddressConverter);
		cisConverter.setCisAddressConverter(cisAddressConverter);
	}

	@Test
	@Transactional
	public final void testConvertCisAddressToOmsLocationCoordinate()
	{
		final Double[] result = this.cisAddressConverter.convertCisAddressToOmsLocationCoordinate(this.cisAddress2);
		Assert.assertNotNull(result);
		Assert.assertNotNull(result[0]);
		Assert.assertNotNull(result[1]);
	}

	@Test
	@Transactional
	public final void testConvertCisPaymentRequestToOmsAmount()
	{
		final AmountVT result = this.cisPaymentConverter.convertCisPaymentRequestToOmsAmount(this.cisPaymentRequest);
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getCurrencyCode());
		Assert.assertNotNull(result.getValue());
	}

	@Test
	@Transactional
	public void testConvertCisPaymentTransactionResultToOmsAmount()
	{
		final CisPaymentRequest paymentRequest = new CisPaymentRequest();
		paymentRequest.setCurrency("CAD");
		paymentRequest.setAmount(BigDecimal.valueOf(4.8d));

		final CisPaymentTransactionResult paymentTransactionResult = new CisPaymentTransactionResult();
		paymentTransactionResult.setRequest(paymentRequest);
		paymentTransactionResult.setAmount(BigDecimal.valueOf(15d));

		final AmountVT result = this.cisPaymentConverter.convertCisPaymentTransactionResultToOmsAmount(paymentTransactionResult);
		Assert.assertNotNull(result);
		Assert.assertEquals("CAD", result.getCurrencyCode());
		Assert.assertEquals(15d, result.getValue(), 0.001d);
	}

	@Test
	@Transactional
	public final void testConvertOmsAddressToCisLocationRequest()
	{
		final ShipmentData shipment = this.persistenceManager.get(HybrisId.valueOf(HYBRIS_ID_SHIPMENT));
		final CisLocationRequest result = this.cisConverter.convertOmsAddressToCisLocationRequest(shipment.getShipFrom());
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getAddresses());
	}

	@Test
	@Transactional
	public final void testConvertOmsAmountToCisPaymentRequest()
	{
		final CisPaymentRequest result = this.cisPaymentConverter.convertOmsAmountToCisPaymentRequest(this.amount);
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getAmount());
		Assert.assertNotNull(result.getCurrency());
	}

	@Test
	@Transactional
	public void testConvertOmsShipmentToCisOrder()
	{
		final ShipmentData shipment = this.persistenceManager.get(HybrisId.valueOf(HYBRIS_ID_SHIPMENT));

		final CisOrder cisOrder = this.cisShipmentConverter.convertOmsShipmentToCisOrder(shipment);
		Assert.assertNotNull(cisOrder);
		Assert.assertTrue(CollectionUtils.isNotEmpty(cisOrder.getAddresses()));
		Assert.assertTrue(CollectionUtils.isNotEmpty(cisOrder.getLineItems()));
		Assert.assertEquals(10d, cisOrder.getLineItems().get(0).getUnitPrice().doubleValue(), 0.001d);
	}

	@Transactional
	@Test
	public void testOnlyOneShipmentHasShippingLineItem()
	{
		final ShipmentData firstShipment = this.persistenceManager.get(HybrisId.valueOf(HYBRIS_ID_FIRST_SHIPMENT));
		final ShipmentData secondShipment = this.persistenceManager.get(HybrisId.valueOf(HYBRIS_ID_SECOND_SHIPMENT));

		final CisOrder cisOrderFirstShipment = this.cisShipmentConverter.convertOmsShipmentToCisOrder(firstShipment);
		final CisOrder cisOrderSecondShipment = this.cisShipmentConverter.convertOmsShipmentToCisOrder(secondShipment);

		Assert.assertNotNull(cisOrderFirstShipment);
		Assert.assertNotNull(cisOrderSecondShipment);
		Assert.assertTrue(CollectionUtils.isNotEmpty(cisOrderFirstShipment.getAddresses()));
		Assert.assertTrue(CollectionUtils.isNotEmpty(cisOrderFirstShipment.getLineItems()));
		Assert.assertTrue(CollectionUtils.isNotEmpty(cisOrderSecondShipment.getAddresses()));
		Assert.assertTrue(CollectionUtils.isNotEmpty(cisOrderSecondShipment.getLineItems()));
		Assert.assertTrue(this.hasShippingLineItem(cisOrderFirstShipment));
		Assert.assertFalse(this.hasShippingLineItem(cisOrderSecondShipment));
	}

	@Test
	@Transactional
	public void testConvertOmsShipmentToCisPaymentRequest()
	{
		final ShipmentData shipment = this.persistenceManager.get(HybrisId.valueOf(HYBRIS_ID_FIRST_SHIPMENT));
		final CisPaymentRequest paymentRequest = this.cisShipmentConverter.convertOmsShipmentToCisPaymentRequest(shipment);
		Assert.assertEquals("USD", paymentRequest.getCurrency());
		Assert.assertEquals(BigDecimal.valueOf(48, 1), paymentRequest.getAmount());
		final ShipmentData shipment2 = this.persistenceManager.get(HybrisId.valueOf(HYBRIS_ID_SECOND_SHIPMENT));
		final CisPaymentRequest paymentRequest2 = this.cisShipmentConverter.convertOmsShipmentToCisPaymentRequest(shipment2);
		Assert.assertNotNull(paymentRequest2);
		Assert.assertEquals("USD", paymentRequest2.getCurrency());
		Assert.assertEquals(BigDecimal.valueOf(50, 1), paymentRequest2.getAmount());
	}

	@Test
	@Transactional
	public final void testConvertOmsShipmentToCisShipment()
	{
		Mockito.when(this.inventoryService.getLocationByLocationId(Mockito.anyString())).thenReturn(this.locationData);

		final CisShipment result = this.cisShipmentConverter.convertOmsShipmentToCisShipment(this.orderData, this.shipmentData);
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getAddresses());
		Assert.assertNotNull(result.getServiceMethod());
		Assert.assertNotNull(result.getAddressByType(CisAddressType.SHIP_FROM));
		Assert.assertNotNull(result.getAddressByType(CisAddressType.SHIP_TO));
		Assert.assertEquals(result.getAddresses().size(), 2);

		final CisAddress cisAddressFrom = result.getAddressByType(CisAddressType.SHIP_FROM);
		Assert.assertEquals(cisAddressFrom.getFirstName(), ORDER_FIRST_NAME);
		Assert.assertEquals(cisAddressFrom.getLastName(), ORDER_LAST_NAME);
		Assert.assertEquals(cisAddressFrom.getAddressLine1(), ADDRESS_LINE_1);
		Assert.assertEquals(cisAddressFrom.getAddressLine2(), ADDRESS_LINE_2);
		Assert.assertEquals(cisAddressFrom.getCity(), ADDRESS_CITY_NAME);
		Assert.assertEquals(cisAddressFrom.getPhone(), PHONE_NUMBER);
		Assert.assertEquals(cisAddressFrom.getCompany(), COMPANY_NAME);


	}


	private void populateAvsResult()
	{
		final AvsResult avsResult = new AvsResult();
		avsResult.setDecision(CisDecision.ACCEPT);
		avsResult.setSuggestedAddresses(Arrays.asList(this.cisAddress));
	}

	private void populateCisAddress()
	{
		this.cisAddress = new CisAddress(ADDRESS_STREET_NAME, ADDRESS_POSTAL_ZONE, ADDRESS_CITY_NAME, ADDRESS_COUNTRY_SUBENTITY,
				ADDRESS_COUNTRY);
		this.cisAddress.setAddressLine2(ADDRESS_LINE_2);
		this.cisAddress.setAddressLine3(ADDRESS_LINE_3);
		this.cisAddress.setAddressLine4(ADDRESS_LINE_4);
	}

	private void populateCisAddress2()
	{
		this.cisAddress2 = new CisAddress();
		this.cisAddress2.setLatitude(LATITUDE_1);
		this.cisAddress2.setLongitude(LONGITUDE_1);
	}

	private void populateLocation()
	{
		final List<String> zipCodes = new ArrayList<String>();
		zipCodes.add(ZIP_CODE_1);
		zipCodes.add(ZIP_CODE_2);

		this.locationData = this.persistenceManager.get(HybrisId.valueOf(HYBRIS_ID_LOCATION));
		this.locationData.setAddress(this.omsAddress);
	}

	private void populateOmsAddress()
	{
		this.omsAddress = new AddressVT(ADDRESS_LINE_1, ADDRESS_LINE_2, ADDRESS_CITY_NAME, ADDRESS_COUNTRY_SUBENTITY,
				ADDRESS_POSTAL_ZONE, null, null, null, null, COMPANY_NAME, PHONE_NUMBER);
	}

	private void populateOmsShipmentAndOrder()
	{
		// To make sure this object is populated
		this.shipmentData = this.persistenceManager.get(HybrisId.valueOf(SHIPMENT_HYBRIS_ID));
		this.shipmentData.setShipFrom(this.omsAddress);
		this.shipmentData.setShippingMethod(SHIPMENT_METHOD);
		this.shipmentData.setGrossWeightUnitCode(GROSS_WEIGHT_CODE);

		this.orderData = this.persistenceManager.get(HybrisId.valueOf(ORDER_HYBRIS_ID));
		this.orderData.setFirstName(ORDER_FIRST_NAME);
		this.orderData.setLastName(ORDER_LAST_NAME);
		this.orderData.setShippingFirstName(ORDER_FIRST_NAME);
		this.orderData.setShippingLastName(ORDER_LAST_NAME);
		this.orderData.setShippingAddress(this.omsAddress);
	}

	private void populatePayment()
	{
		this.cisPaymentRequest = new CisPaymentRequest();
		this.cisPaymentRequest.setAmount(new BigDecimal("88"));
		this.cisPaymentRequest.setCurrency("CAD");
		this.amount = new AmountVT("CAD", 88d);
	}

	private boolean hasShippingLineItem(final CisOrder cisOrder)
	{
		for (final CisLineItem item : cisOrder.getLineItems())
		{
			if (item.getId() == CisService.INVOICE_TAX_SHIPPING_LINE_ITEM_ID)
			{
				return true;
			}
		}

		return false;
	}
}
