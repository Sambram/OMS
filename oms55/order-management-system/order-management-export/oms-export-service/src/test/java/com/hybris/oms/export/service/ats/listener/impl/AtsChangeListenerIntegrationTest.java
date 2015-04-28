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
package com.hybris.oms.export.service.ats.listener.impl;

import static com.hybris.oms.service.util.OmsTestUtils.delay;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.kernel.engine.jdbc.impl.JdbcPersistenceEngine;
import com.hybris.oms.export.service.managedobjects.ats.ExportSkus;
import com.hybris.oms.service.managedobjects.ats.AtsFormulaData;
import com.hybris.oms.service.managedobjects.inventory.BinData;
import com.hybris.oms.service.managedobjects.inventory.CurrentItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.FutureItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.ItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;
import com.hybris.oms.service.managedobjects.order.PaymentInfoData;
import com.hybris.oms.service.managedobjects.shipment.ShippingAndHandlingData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.util.OmsTestUtils;

import java.util.Collections;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/oms-export-service-spring-test.xml")
public class AtsChangeListenerIntegrationTest
{

	private static final String SKU = "ABC";
	private static final String LOC = "123";
	private static final String OLQ_STATUS_NEWER = "NEWER";
	private static final String OLQ_STATUS_NEW = "NEW";
	@Autowired
	private PersistenceManager persistenceManager;
	@Autowired
	private PlatformTransactionManager transactionManager;
	@Autowired
	private JdbcPersistenceEngine persistenceEngine;

	@Before
	public void setUp()
	{
		// Empty
	}

	@After
	public void cleanData()
	{
		OmsTestUtils.cleanUp(persistenceEngine);
		persistenceEngine.removeAll(ExportSkus._TYPECODE);
	}

	@Test
	public void triggerEvent_EditCurrentInventory()
	{
		// GIVEN
		final TransactionTemplate template = new TransactionTemplate(transactionManager);
		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				createAtsFormulas();
				createLocationAndInventory();
			}
		});
		delay(500);
		persistenceEngine.removeAll(ExportSkus._TYPECODE);

		// WHEN
		template.execute(new TransactionCallbackWithoutResult()
		{

			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				final ItemQuantityData inventory = persistenceManager.createCriteriaQuery(CurrentItemQuantityData.class)
						.uniqueResult();
				inventory.setQuantityValue(10);
			}
		});
		delay(500);

		// THEN
		template.execute(new TransactionCallbackWithoutResult()
		{

			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				final ExportSkus sku = persistenceManager.createCriteriaQuery(ExportSkus.class).uniqueResult();
				assertThat(sku.getSku(), equalTo(SKU));
				assertThat(sku.getLocationId(), equalTo(LOC));
			}
		});
	}

	@Test
	public void triggerEvent_EditFutureInventory()
	{
		// GIVEN
		final TransactionTemplate template = new TransactionTemplate(transactionManager);
		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				createAtsFormulas();
				createLocationAndInventory();
			}
		});
		delay(500);
		persistenceEngine.removeAll(ExportSkus._TYPECODE);

		// WHEN
		template.execute(new TransactionCallbackWithoutResult()
		{

			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				final ItemQuantityData inventory = persistenceManager.createCriteriaQuery(FutureItemQuantityData.class)
						.uniqueResult();
				inventory.setQuantityValue(10);
			}
		});
		delay(500);

		// THEN
		template.execute(new TransactionCallbackWithoutResult()
		{

			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				final ExportSkus sku = persistenceManager.createCriteriaQuery(ExportSkus.class).uniqueResult();
				assertThat(sku.getSku(), equalTo(SKU));
				assertThat(sku.getLocationId(), equalTo(LOC));
			}
		});
	}

	@Test
	public void triggerEvent_OLQStatusChange()
	{
		// GIVEN
		final TransactionTemplate template = new TransactionTemplate(transactionManager);

		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				createAtsFormulas();
				createLocationAndInventory();
				createOrder();
			}
		});
		delay(500);
		persistenceEngine.removeAll(ExportSkus._TYPECODE);

		// WHEN
		template.execute(new TransactionCallbackWithoutResult()
		{

			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				final OrderLineQuantityData olq = persistenceManager.createCriteriaQuery(OrderLineQuantityData.class).uniqueResult();
				final OrderLineQuantityStatusData olqStatus_Newer = persistenceManager.getByIndex(
						OrderLineQuantityStatusData.UX_ORDERLINEQUANTITYSTATUSES_STATUSCODE, OLQ_STATUS_NEWER);
				olq.setStatus(olqStatus_Newer);
			}
		});
		delay(500);

		// THEN
		template.execute(new TransactionCallbackWithoutResult()
		{

			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				final ExportSkus sku = persistenceManager.createCriteriaQuery(ExportSkus.class).uniqueResult();
				assertThat(sku.getSku(), equalTo(SKU));
				assertThat(sku.getLocationId(), equalTo(LOC));
			}
		});
	}

	@Test
	public void triggerEvent_OLQQuantityChange()
	{
		// GIVEN
		final TransactionTemplate template = new TransactionTemplate(transactionManager);
		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				createAtsFormulas();
				createLocationAndInventory();
				createOrder();
			}
		});
		delay(500);
		persistenceEngine.removeAll(ExportSkus._TYPECODE);

		// WHEN
		template.execute(new TransactionCallbackWithoutResult()
		{

			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				final OrderLineQuantityData olq = persistenceManager.createCriteriaQuery(OrderLineQuantityData.class).uniqueResult();
				olq.setQuantityValue(50);
			}
		});
		delay(500);

		// THEN
		template.execute(new TransactionCallbackWithoutResult()
		{

			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				final ExportSkus sku = persistenceManager.createCriteriaQuery(ExportSkus.class).uniqueResult();
				assertThat(sku.getSku(), equalTo(SKU));
				assertThat(sku.getLocationId(), equalTo(LOC));
			}
		});
	}

	@Test(expected = ManagedObjectNotFoundException.class)
	public void triggerNoEvent_LocationChange()
	{
		// GIVEN
		final TransactionTemplate template = new TransactionTemplate(transactionManager);
		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				createAtsFormulas();
				createLocationAndInventory();
				createOrder();
			}
		});
		delay(500);
		persistenceEngine.removeAll(ExportSkus._TYPECODE);

		// WHEN
		template.execute(new TransactionCallbackWithoutResult()
		{

			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				final OrderLineQuantityData olq = persistenceManager.createCriteriaQuery(OrderLineQuantityData.class).uniqueResult();
				olq.setStockroomLocationId("NEW LOCATION");
			}
		});
		delay(500);

		// THEN
		template.execute(new TransactionCallbackWithoutResult()
		{

			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				persistenceManager.createCriteriaQuery(ExportSkus.class).uniqueResult();
			}
		});

		fail(); // ManagedObjectNotFoundException must be thrown
	}

	@Test
	public void triggerEvent_OrderLineQuantityUnassignedChange()
	{
		// GIVEN
		final TransactionTemplate template = new TransactionTemplate(transactionManager);
		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				createAtsFormulas();
				createLocationAndInventory();
				createOrder();
			}
		});
		delay(500);
		persistenceEngine.removeAll(ExportSkus._TYPECODE);

		// WHEN
		template.execute(new TransactionCallbackWithoutResult()
		{

			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				final OrderLineData orderLine = persistenceManager.createCriteriaQuery(OrderLineData.class).uniqueResult();
				orderLine.setQuantityUnassignedValue(5);
			}
		});
		delay(500);

		// THEN
		template.execute(new TransactionCallbackWithoutResult()
		{

			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				final ExportSkus sku = persistenceManager.createCriteriaQuery(ExportSkus.class).uniqueResult();
				assertThat(sku.getSku(), equalTo(SKU));
				assertThat(sku.getLocationId(), equalTo("GLOBAL"));
			}
		});
	}

	private void createAtsFormulas()
	{
		final AtsFormulaData onHandFormula = persistenceManager.create(AtsFormulaData.class);
		onHandFormula.setAtsId("on_hand");
		onHandFormula.setDescription("on hand only");
		onHandFormula.setName("on_hand");
		onHandFormula.setFormula("I[ON_HAND]+F[TRANSIT]-O[" + OLQ_STATUS_NEW + "]-O[" + OLQ_STATUS_NEWER + "]");
	}

	private void createLocationAndInventory()
	{
		final StockroomLocationData location = persistenceManager.create(StockroomLocationData.class);
		location.setLocationId(LOC);
		location.setLocationRoles(Collections.singleton("SHIPPING"));

		final BinData bin = persistenceManager.create(BinData.class);
		bin.setBinCode("123");
		bin.setStockroomLocation(location);

		final ItemLocationData itemLocation = persistenceManager.create(ItemLocationData.class);
		itemLocation.setStockroomLocation(location);
		itemLocation.setBin(bin);
		itemLocation.setItemId(SKU);

		final ItemQuantityData itemQuantity = persistenceManager.create(CurrentItemQuantityData.class);
		itemQuantity.setQuantityUnitCode("unit");
		itemQuantity.setQuantityValue(5);
		itemQuantity.setStatusCode("ON_HAND");
		itemQuantity.setOwner(itemLocation);
		itemLocation.setItemQuantities(Collections.singletonList(itemQuantity));

		final ItemLocationData itemLocation2 = persistenceManager.create(ItemLocationData.class);
		itemLocation2.setStockroomLocation(location);
		itemLocation2.setBin(bin);
		itemLocation2.setItemId(SKU);

		final ItemQuantityData itemQuantity2 = persistenceManager.create(FutureItemQuantityData.class);
		itemQuantity2.setQuantityUnitCode("unit");
		itemQuantity2.setQuantityValue(5);
		itemQuantity2.setStatusCode("TRANSIT");
		itemQuantity2.setExpectedDeliveryDate(new Date());
		itemQuantity2.setOwner(itemLocation2);
		itemLocation2.setItemQuantities(Collections.singletonList(itemQuantity2));
	}

	private void createOrder()
	{
		final OrderLineQuantityStatusData olqStatus_NEW = persistenceManager.create(OrderLineQuantityStatusData.class);
		olqStatus_NEW.setActive(Boolean.TRUE);
		olqStatus_NEW.setDescription("A new status");
		olqStatus_NEW.setStatusCode(OLQ_STATUS_NEW);

		final OrderLineQuantityStatusData olqStatus_NEWER = persistenceManager.create(OrderLineQuantityStatusData.class);
		olqStatus_NEWER.setActive(Boolean.TRUE);
		olqStatus_NEWER.setDescription("A newer status");
		olqStatus_NEWER.setStatusCode(OLQ_STATUS_NEWER);

		final OrderData order = persistenceManager.create(OrderData.class);
		order.setEmailid("kick@chuck-norris.ouch");
		order.setFirstName("Chuck");
		order.setLastName("Norris");
		order.setIssueDate(new Date());
		order.setOrderId("foo-bar");
		order.setShippingAddress(new AddressVT("1 Roundhouse Avenue", null, "Kicktown", "NY", "00001", null, null, "US",
				"United Stated of Chuck Norris", "Chuckie", null));
		order.setShippingMethod("roundhouse kick to the face");

		final OrderLineData orderLine = persistenceManager.create(OrderLineData.class);
		orderLine.setMyOrder(order);
		orderLine.setOrderLineId("foo-bar");
		orderLine.setQuantityUnassignedValue(10);
		orderLine.setQuantityValue(10);
		orderLine.setSkuId(SKU);
		orderLine.setTaxCategory("No Tax");
		orderLine.setUnitPriceValue(5.00d);
		orderLine.setUnitTaxValue(5.00d);
		orderLine.setLocationRoles(Collections.singleton("KICKING ROLE"));
		order.setOrderLines(Collections.singletonList(orderLine));

		final OrderLineQuantityData olq = persistenceManager.create(OrderLineQuantityData.class);
		olq.setOrderLine(orderLine);
		olq.setQuantityValue(10);
		olq.setStatus(olqStatus_NEW);
		olq.setStockroomLocationId("123");
		orderLine.setOrderLineQuantities(Collections.singletonList(olq));

		final PaymentInfoData paymentInfo = persistenceManager.create(PaymentInfoData.class);
		paymentInfo.setAuthUrl("http://chuck.norris.com/authorize");
		paymentInfo.setPaymentInfoType("Roundhouse kick to the face.");
		paymentInfo.setBillingAddress(new AddressVT("1 Roundhouse Avenue", null, "Kicktown", "NY", "00001", null, null, "US",
				"United Stated of Chuck Norris", "Chuckie", null));
		paymentInfo.setCaptureId("999");
		paymentInfo.setMyOrder(order);
		order.setPaymentInfos(Collections.singletonList(paymentInfo));

		final ShippingAndHandlingData sAndH = persistenceManager.create(ShippingAndHandlingData.class);
		sAndH.setOrderId("foo-bar");
		order.setShippingAndHandling(sAndH);
	}

}
