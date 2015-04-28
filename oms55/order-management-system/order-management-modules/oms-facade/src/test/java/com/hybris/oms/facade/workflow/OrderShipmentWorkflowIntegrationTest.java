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
package com.hybris.oms.facade.workflow;

import static com.hybris.oms.service.util.OmsTestUtils.cleanUp;
import static com.hybris.oms.service.util.OmsTestUtils.clearAggregates;
import static com.hybris.oms.service.util.OmsTestUtils.delay;
import static com.hybris.oms.service.util.OmsTestUtils.waitForInventory;
import static com.hybris.oms.service.util.OmsTestUtils.waitForProcesses;
import static com.hybris.oms.service.util.OmsTestUtils.waitForWorkflowTask;
import static com.hybris.oms.service.workflow.WorkflowConstants.FULFILL_INCOMPLETE_USER_TASK;

import com.hybris.kernel.api.AggregationService;
import com.hybris.kernel.api.ImportService;
import com.hybris.kernel.api.JobSchedulerService;
import com.hybris.kernel.engine.jdbc.impl.JdbcPersistenceEngine;
import com.hybris.oms.api.ats.AtsFacade;
import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.api.order.OrderFacade;
import com.hybris.oms.api.preference.PreferenceFacade;
import com.hybris.oms.api.shipment.ShipmentFacade;
import com.hybris.oms.domain.ats.AtsLocalQuantities;
import com.hybris.oms.domain.ats.AtsQuantity;
import com.hybris.oms.domain.exception.InvalidOperationException;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.locationrole.LocationRole;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.order.OrderLineAttribute;
import com.hybris.oms.domain.order.PaymentInfo;
import com.hybris.oms.domain.preference.TenantPreference;
import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.domain.remote.exception.InvalidGeolocationResponseException;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.domain.shipping.ShippingAndHandling;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.domain.types.Contact;
import com.hybris.oms.domain.types.Price;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.service.cis.CisService;
import com.hybris.oms.service.itemlocation.builders.AddressBuilder;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.shipment.ShipmentService;
import com.hybris.oms.service.util.OmsTestUtils;
import com.hybris.oms.service.workflow.WorkflowTestConstants;
import com.hybris.oms.service.workflow.executor.WorkflowExecutor;
import com.hybris.oms.service.workflow.worker.order.FulfillmentWorkItemWorker;
import com.hybris.oms.service.workflow.worker.order.GeocodingWorkItemWorker;
import com.hybris.oms.service.workflow.worker.shipment.PaymentWorkItemWorker;
import com.hybris.oms.service.workflow.worker.shipment.TaxesWorkItemWorker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


/**
 * Test to make sure Order Workflow works as expected.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/oms-facade-workflow-spring-test.xml"})
@Ignore("Test takes way to long to run.")
public class OrderShipmentWorkflowIntegrationTest
{
	private static final Logger LOG = LoggerFactory.getLogger(OrderShipmentWorkflowIntegrationTest.class);

	private static final String LOC_ID_1 = "LOCATION_ID_1";
	private static final String LOC_ID_2 = "LOCATION_ID_2";
	private static final String SKU_1 = "SKU_1";
	private static final String SKU_2 = "SKU_2";

	private static final String ON_HAND = "ON_HAND";
	private static final String NOT_AVAILABLE = "NOT_AVAILABLE";
	private static final String ORDER_ID = "ORDER_ID";
	private static final String ORDER_LINE_ID_1 = "ORDER_LINE_ID_1";
	private static final String ORDER_LINE_ID_2 = "ORDER_LINE_ID_2";
	private static final int QUANTITY = 10;
	private static final int ZERO = 0;
	private static final String ORDERLINE_ATTRIBUTE_VALUE = "OrderLine.AttributeValue";
	private static final String ORDERLINE_ATTRIBUTE_ID = "OrderLine.AttributeId";
	private static List<TenantPreference> ORIGINAL_PREFERENCES;

	@Autowired
	private AggregationService aggregationService;
	@Autowired
	private JobSchedulerService schedulerService;
	@Autowired
	private ImportService importService;
	@Autowired
	private JdbcPersistenceEngine persistenceEngine;

	@Autowired
	private AtsFacade atsFacade;
	@Autowired
	private OrderFacade orderFacade;
	@Autowired
	private InventoryFacade inventoryFacade;
	@Autowired
	private ShipmentFacade shipmentFacade;
	@Autowired
	private PreferenceFacade preferenceFacade;

	@Autowired
	private ShipmentService shipmentService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private CisService cisService;

	@Autowired
	private WorkflowExecutor<OrderData> orderWorkflowExecutor;
	@Autowired
	private WorkflowExecutor<ShipmentData> shipmentWorkflowExecutor;

	@Autowired
	private GeocodingWorkItemWorker geoWorkItemWorker;
	@Autowired
	private FulfillmentWorkItemWorker fulfillmentWorkItemWorker;
	@Autowired
	private PaymentWorkItemWorker paymentWorkItemWorker;
	@Autowired
	private TaxesWorkItemWorker taxesWorkItemWorker;

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private PlatformTransactionManager txManager;

	@Before
	public void setUp()
	{
		clearAggregates(aggregationService);
		// OmsTestUtils.unscheduleJobs(schedulerService);
		this.importService.loadMcsvResource(new ClassPathResource("/META-INF/essential-data-import.mcsv"));
		this.storeOriginalTenantPreferences();
		this.createLocationAndInventory();
		waitForInventory(aggregationService, SKU_1);
		waitForInventory(aggregationService, SKU_2);

		// Make sure ATS is correct before running the test
		final List<AtsLocalQuantities> ats = atsFacade.findLocalAts(Sets.newHashSet(SKU_1, SKU_2),
				Sets.newHashSet(LOC_ID_1, LOC_ID_2), Collections.singleton("WEB"));
		for (final AtsLocalQuantities localAts : ats)
		{
			for (final AtsQuantity atsQuantity : localAts.getAtsQuantities())
			{
				LOG.info("Current ATS for SKU [{}], LOCATION [{}], FORMULA [{}] = {}.", atsQuantity.getSku(),
						localAts.getLocationId(), atsQuantity.getAtsId(), atsQuantity.getQuantity().getValue());
				Assert.assertEquals(QUANTITY, atsQuantity.getQuantity().getValue());
			}
		}
	}

	@After
	public void tearDown()
	{
		this.resetTenantPreferencesToOriginalState();
		cleanUp(persistenceEngine);
	}

	/* Waits for all shipment workflows */
	@Test
	public void shouldBeOrderWorkflowComplete_WaitsForAllShipmentWorkflows()
	{
		final Order order = this.orderFacade.createOrder(this.buildOrder(true));
		final String orderID = order.getOrderId();
		waitForWorkflowTask(WorkflowTestConstants.ACTIVITY_ID_SHIPMENT_ACTION,
				getShipmentWorkflowBusinessKey(getShipments(order, 2).get(0)), runtimeService);
		assertOrderWorkflowInProgress(order);

		final List<Shipment> shipments = getShipments(order, 2);
		final String firstShipmentID = shipments.get(0).getShipmentId();
		final String secondShipmentID = shipments.get(1).getShipmentId();
		shipmentFacade.confirmShipment(firstShipmentID);
		assertOrderWorkflowInProgress(order);
		shipmentFacade.confirmShipment(secondShipmentID);
		waitForProcesses(this.runtimeService);

		assertOrderWorkflowEnded(orderID);
		assertShipmentWorkflowEnded(firstShipmentID);
		assertShipmentWorkflowEnded(secondShipmentID);
	}

	/* Automatic re-fulfillment */
	@Test
	public void shouldRefulfillAfterDecline_FromOtherLocation()
	{
		createInventory(SKU_1, LOC_ID_2, ON_HAND, QUANTITY);
		createInventory(SKU_1, LOC_ID_2, NOT_AVAILABLE, ZERO);

		final Order order = this.orderFacade.createOrder(this.buildOrder());
		waitForWorkflowTask(WorkflowTestConstants.ACTIVITY_ID_SHIPMENT_ACTION,
				getShipmentWorkflowBusinessKey(getShipments(order).get(0)), runtimeService);

		final Shipment shipmentBefore = getShipments(order).get(0);
		shipmentFacade.declineShipment(shipmentBefore.getShipmentId());

		waitForWorkflowTask(WorkflowTestConstants.ACTIVITY_ID_SHIPMENT_ACTION,
				getShipmentWorkflowBusinessKey(getShipments(order).get(0)), runtimeService);

		final Shipment shipmentAfter = getShipments(order).get(0);
		Assert.assertNotSame(shipmentBefore.getShipmentId(), shipmentAfter.getShipmentId());
		Assert.assertNotSame(shipmentBefore.getLocation(), shipmentAfter.getLocation());
	}

	@Test
	public void shouldFailRefulfillAfterDecline_CannotRefulfillFromSameLocation()
	{
		final Order order = this.orderFacade.createOrder(this.buildOrder());
		final String orderID = order.getOrderId();
		waitForWorkflowTask(WorkflowTestConstants.ACTIVITY_ID_SHIPMENT_ACTION,
				getShipmentWorkflowBusinessKey(getShipments(order).get(0)), runtimeService);

		shipmentFacade.declineShipment(getShipments(order).get(0).getShipmentId());

		delay();
		Assert.assertTrue(shipmentFacade.getShipmentsByOrderId(orderID).isEmpty());
		Assert.assertEquals(QUANTITY, order.getOrderLines().get(0).getQuantityUnassigned().getValue());
	}

	/* Skip Fulfillment */
	@Test
	public void shouldSkipFulfill_GeocodeSuccess()
	{
		toggleWorkflowStep(TenantPreferenceConstants.PREF_KEY_WF_EXEC_TASK_FULFILLMENT, Boolean.FALSE);

		Order order = this.buildOrder();
		final String orderID = order.getOrderId();
		order.getShippingAddress().setLatitudeValue(null);
		order.getShippingAddress().setLongitudeValue(null);
		orderFacade.createOrder(order);
		waitForProcesses(this.runtimeService);

		assertOrderWorkflowEnded(orderID);
		order = orderFacade.getOrderByOrderId(ORDER_ID);
		Assert.assertNotNull(order.getShippingAddress().getLatitudeValue());
		Assert.assertNotNull(order.getShippingAddress().getLongitudeValue());
		Assert.assertEquals(Double.valueOf(0), order.getShippingAddress().getLatitudeValue());
		Assert.assertEquals(Double.valueOf(0), order.getShippingAddress().getLongitudeValue());
	}

	@Test
	public void shouldSkipFulfill_GeocodeFailed()
	{
		toggleWorkflowStep(TenantPreferenceConstants.PREF_KEY_WF_EXEC_TASK_FULFILLMENT, Boolean.FALSE);

		// Setup mock to fail geocoding
		final CisService cisServiceMock = Mockito.mock(CisService.class);
		geoWorkItemWorker.setCisService(cisServiceMock);
		Mockito.when(cisServiceMock.geocodeAddress(Mockito.any(AddressVT.class))).thenThrow(
				new InvalidGeolocationResponseException("*****Failing on purpose.*****"));

		try
		{
			Order order = this.buildOrder();
			final String orderID = order.getOrderId();
			order.getShippingAddress().setLatitudeValue(null);
			order.getShippingAddress().setLongitudeValue(null);
			orderFacade.createOrder(order);
			waitForProcesses(this.runtimeService);

			assertOrderWorkflowEnded(orderID);
			order = orderFacade.getOrderByOrderId(ORDER_ID);
			Assert.assertNull(order.getShippingAddress().getLatitudeValue());
			Assert.assertNull(order.getShippingAddress().getLongitudeValue());
		}
		finally
		{
			geoWorkItemWorker.setCisService(cisService);
		}
	}

	/* Split Shipments */
	@Test
	public void shouldSplitShipment_ByOlqs()
	{
		final Order order = this.orderFacade.createOrder(buildOrder());

		final Shipment shipment = getShipments(order).get(0);
		waitForWorkflowTask(WorkflowTestConstants.ACTIVITY_ID_SHIPMENT_ACTION,
				getShipmentWorkflowBusinessKey(shipment), runtimeService);

		shipmentFacade.splitShipmentByOlqs(shipment.getShipmentId(), Collections.singleton(shipment.getOlqIds().get(0)));

		final List<Shipment> shipments = getShipments(order, 2);
		shipmentFacade.confirmShipment(shipments.get(0).getShipmentId());
		shipmentFacade.confirmShipment(shipments.get(1).getShipmentId());
		waitForProcesses(runtimeService);

		assertShipmentWorkflowEnded(shipments.get(0).getShipmentId());
		assertShipmentWorkflowEnded(shipments.get(1).getShipmentId());
	}

	@Test
	public void shouldSplitShipment_ByQuantities()
	{
		final Order order = this.orderFacade.createOrder(buildOrder());

		final Shipment shipment = getShipments(order).get(0);
		waitForWorkflowTask(WorkflowTestConstants.ACTIVITY_ID_SHIPMENT_ACTION,
				getShipmentWorkflowBusinessKey(shipment), runtimeService);

		final Map<String, Integer> quantitiesMap = new HashMap<>();
		quantitiesMap.put(shipment.getOlqIds().get(0), 5);
		shipmentFacade.splitShipmentByOlqQuantities(shipment.getShipmentId(), quantitiesMap);

		final List<Shipment> shipments = getShipments(order, 2);
		shipmentFacade.confirmShipment(shipments.get(0).getShipmentId());
		shipmentFacade.confirmShipment(shipments.get(1).getShipmentId());
		waitForProcesses(runtimeService);

		assertShipmentWorkflowEnded(shipments.get(0).getShipmentId());
		assertShipmentWorkflowEnded(shipments.get(1).getShipmentId());
	}

	/* Fulfill Complete & Shipment Confirm Success */
	@Test
	public void shouldFulfillComplete_AndShipmentConfirm_NoSkip()
	{
		final Order order = this.orderFacade.createOrder(this.buildOrder());
		final String orderID = order.getOrderId();
		waitForWorkflowTask(WorkflowTestConstants.ACTIVITY_ID_SHIPMENT_ACTION,
				getShipmentWorkflowBusinessKey(getShipments(order).get(0)), runtimeService);
		shipmentFacade.confirmShipment(getShipments(order).get(0).getShipmentId());
		waitForProcesses(this.runtimeService);

		assertOrderWorkflowEnded(orderID);
		final Shipment shipment = getShipments(order).get(0);
		assertShipmentWorkflowEnded(shipment.getShipmentId());
		assertShipmentStatus(shipment, TenantPreferenceConstants.PREF_KEY_OLQSTATUS_SHIPPED);
		Assert.assertTrue(shipment.getAmountCaptured().getValue() > 0);
		Assert.assertTrue(shipment.getMerchandisePrice().getTaxCommitted().getValue() > 0);
	}

	@Test
	public void shouldFulfillComplete_AndShipmentConfirm_SkipPayment()
	{
		toggleWorkflowStep(TenantPreferenceConstants.PREF_KEY_WF_EXEC_TASK_PAYMENTCAPTURE, Boolean.FALSE);

		final Order order = this.orderFacade.createOrder(this.buildOrder());
		final String orderID = order.getOrderId();
		waitForWorkflowTask(WorkflowTestConstants.ACTIVITY_ID_SHIPMENT_ACTION,
				getShipmentWorkflowBusinessKey(getShipments(order).get(0)), runtimeService);
		shipmentFacade.confirmShipment(getShipments(order).get(0).getShipmentId());
		waitForProcesses(this.runtimeService);

		assertOrderWorkflowEnded(orderID);
		final Shipment shipment = getShipments(order).get(0);
		assertShipmentWorkflowEnded(shipment.getShipmentId());
		assertShipmentStatus(shipment, TenantPreferenceConstants.PREF_KEY_OLQSTATUS_SHIPPED);
		Assert.assertTrue(shipment.getAmountCaptured().getValue() == 0);
		Assert.assertTrue(shipment.getMerchandisePrice().getTaxCommitted().getValue() > 0);
	}

	@Test
	public void shouldFulfillComplete_AndShipmentConfirm_SkipTaxes()
	{
		toggleWorkflowStep(TenantPreferenceConstants.PREF_KEY_WF_EXEC_TASK_TAXINVOICE, Boolean.FALSE);

		final Order order = this.orderFacade.createOrder(this.buildOrder());
		final String orderID = order.getOrderId();
		waitForWorkflowTask(WorkflowTestConstants.ACTIVITY_ID_SHIPMENT_ACTION,
				getShipmentWorkflowBusinessKey(getShipments(order).get(0)), runtimeService);
		shipmentFacade.confirmShipment(getShipments(order).get(0).getShipmentId());
		waitForProcesses(this.runtimeService);

		assertOrderWorkflowEnded(orderID);
		final Shipment shipment = getShipments(order).get(0);
		assertShipmentWorkflowEnded(shipment.getShipmentId());
		assertShipmentStatus(shipment, TenantPreferenceConstants.PREF_KEY_OLQSTATUS_SHIPPED);
		Assert.assertTrue(shipment.getAmountCaptured().getValue() > 0);
		Assert.assertTrue(shipment.getMerchandisePrice().getTaxCommitted().getValue() == 0);
	}

	@Test
	public void shouldFulfillComplete_AndShipmentConfirm_SkipPaymentAndTaxes()
	{
		toggleWorkflowStep(TenantPreferenceConstants.PREF_KEY_WF_EXEC_TASK_PAYMENTCAPTURE, Boolean.FALSE);
		toggleWorkflowStep(TenantPreferenceConstants.PREF_KEY_WF_EXEC_TASK_TAXINVOICE, Boolean.FALSE);

		final Order order = this.orderFacade.createOrder(this.buildOrder());
		final String orderID = order.getOrderId();
		waitForWorkflowTask(WorkflowTestConstants.ACTIVITY_ID_SHIPMENT_ACTION,
				getShipmentWorkflowBusinessKey(getShipments(order).get(0)), runtimeService);
		shipmentFacade.confirmShipment(getShipments(order).get(0).getShipmentId());
		waitForProcesses(this.runtimeService);

		assertOrderWorkflowEnded(orderID);
		final Shipment shipment = getShipments(order).get(0);
		assertShipmentWorkflowEnded(shipment.getShipmentId());
		assertShipmentStatus(shipment, TenantPreferenceConstants.PREF_KEY_OLQSTATUS_SHIPPED);
		Assert.assertTrue(shipment.getAmountCaptured().getValue() == 0);
		Assert.assertTrue(shipment.getMerchandisePrice().getTaxCommitted().getValue() == 0);
	}

	/* Fulfill Complete & Shipment Confirm Failure */
	@Test
	public void shouldFulfillComplete_AndShipmentConfirm_FailedPayment()
	{
		// Setup mock to fail payment
		final ShipmentService shipmentServiceMock = Mockito.mock(ShipmentService.class);
		paymentWorkItemWorker.setShipmentService(shipmentServiceMock);
		Mockito.when(shipmentServiceMock.capturePayment((Mockito.any(ShipmentData.class)))).thenThrow(
				new IllegalArgumentException("*****Failing on purpose.*****"));
		try
		{
			final Order order = this.orderFacade.createOrder(this.buildOrder());
			final String orderID = order.getOrderId();
			waitForWorkflowTask(WorkflowTestConstants.ACTIVITY_ID_SHIPMENT_ACTION,
					getShipmentWorkflowBusinessKey(getShipments(order).get(0)), runtimeService);
			shipmentFacade.confirmShipment(getShipments(order).get(0).getShipmentId());
			waitForProcesses(this.runtimeService);

			assertOrderWorkflowEnded(orderID);
			final Shipment shipment = getShipments(order).get(0);
			assertShipmentWorkflowEnded(shipment.getShipmentId());
			assertShipmentStatus(shipment, TenantPreferenceConstants.PREF_KEY_OLQSTATUS_ALLOCATED);
			Assert.assertTrue(shipment.getAmountCaptured().getValue() == 0);
		}
		finally
		{
			// Reset DefaultShipmentService
			this.paymentWorkItemWorker.setShipmentService(this.shipmentService);
		}
	}

	@Test
	public void shouldFulfillComplete_AndShipmentConfirm_FailedTaxes()
	{
		// Setup mock to fail taxes
		final ShipmentService shipmentServiceMock = Mockito.mock(ShipmentService.class);
		taxesWorkItemWorker.setShipmentService(shipmentServiceMock);
		Mockito.when(shipmentServiceMock.invoiceTaxes(Mockito.any(ShipmentData.class))).thenThrow(
				new IllegalArgumentException("*****Failing on purpose.*****"));
		try
		{
			final Order order = this.orderFacade.createOrder(this.buildOrder());
			final String orderID = order.getOrderId();
			waitForWorkflowTask(WorkflowTestConstants.ACTIVITY_ID_SHIPMENT_ACTION,
					getShipmentWorkflowBusinessKey(getShipments(order).get(0)), runtimeService);
			shipmentFacade.confirmShipment(getShipments(order).get(0).getShipmentId());
			waitForProcesses(this.runtimeService);

			assertOrderWorkflowEnded(orderID);
			final Shipment shipment = getShipments(order).get(0);
			assertShipmentWorkflowEnded(shipment.getShipmentId());
			assertShipmentStatus(shipment, TenantPreferenceConstants.PREF_KEY_OLQSTATUS_PAYMENT_CAPTURED);
			Assert.assertTrue(shipment.getAmountCaptured().getValue() > 0);
			Assert.assertTrue(shipment.getMerchandisePrice().getTaxCommitted().getValue() == 0);
		}
		finally
		{
			// Reset DefaultShipmentService
			this.taxesWorkItemWorker.setShipmentService(this.shipmentService);
		}
	}

	/* Fulfill Complete & Shipment Cancel */
	@Test
	public void shouldFulfillComplete_AndShipmentCancel()
	{
		final Order order = this.orderFacade.createOrder(this.buildOrder());
		final String orderID = order.getOrderId();
		waitForWorkflowTask(WorkflowTestConstants.ACTIVITY_ID_SHIPMENT_ACTION,
				getShipmentWorkflowBusinessKey(getShipments(order).get(0)), runtimeService);
		final String shipmentID = getShipments(order).get(0).getShipmentId();
		shipmentFacade.cancelShipment(shipmentID);
		waitForProcesses(this.runtimeService);

		assertOrderWorkflowEnded(orderID);
		assertShipmentWorkflowEnded(shipmentID);
		assertShipmentStatus(getShipments(order).get(0), TenantPreferenceConstants.PREF_KEY_OLQSTATUS_CANCELED);
	}

	/* Fulfill Partial */
	@Test
	public void shouldFulfillPartial_AndShipmentConfirm_CancelRemainder()
	{
		final Order order = this.buildOrder();
		order.getOrderLines().get(0).getQuantity().setValue(100);
		order.getOrderLines().get(0).getQuantityUnassigned().setValue(100);
		this.orderFacade.createOrder(order);
		waitForWorkflowTask(WorkflowTestConstants.ACTIVITY_ID_SHIPMENT_ACTION,
				getShipmentWorkflowBusinessKey(getShipments(order).get(0)), runtimeService);
		final String shipmentID = getShipments(order).get(0).getShipmentId();
		shipmentFacade.confirmShipment(shipmentID);

		waitForWorkflowTask(FULFILL_INCOMPLETE_USER_TASK, getOrderWorkflowBusinessKey(order), runtimeService);
		orderFacade.cancelUnfulfilled(order.getOrderId());

		waitForProcesses(this.runtimeService);

		final Order orderDB = this.orderFacade.getOrderByOrderId(ORDER_ID);
		Assert.assertEquals(1, orderDB.getOrderLineQuantities().size());
		Assert.assertEquals(10, orderDB.getOrderLineQuantities().get(0).getQuantity().getValue());
		Assert.assertEquals(0, orderDB.getOrderLines().get(0).getQuantityUnassigned().getValue());

		assertShipmentStatus(getShipments(order).get(0), TenantPreferenceConstants.PREF_KEY_OLQSTATUS_SHIPPED);
	}

	@Test
	public void shouldFulfillPartial_AndShipmentConfirm_RetryFulfillment_Success()
	{
		final Order order = this.buildOrder();
		order.getOrderLines().get(0).getQuantity().setValue(100);
		order.getOrderLines().get(0).getQuantityUnassigned().setValue(100);
		this.orderFacade.createOrder(order);
		waitForWorkflowTask(WorkflowTestConstants.ACTIVITY_ID_SHIPMENT_ACTION,
				getShipmentWorkflowBusinessKey(getShipments(order).get(0)), runtimeService);
		String shipmentID = getShipments(order).get(0).getShipmentId();
		shipmentFacade.confirmShipment(shipmentID);

		waitForWorkflowTask(FULFILL_INCOMPLETE_USER_TASK, getOrderWorkflowBusinessKey(order), runtimeService);

		// Add more inventory before retrying fulfillment
		createInventory(SKU_1, LOC_ID_2, ON_HAND, 100);
		createInventory(SKU_1, LOC_ID_2, NOT_AVAILABLE, ZERO);
		orderFacade.fulfill(order.getOrderId());

		waitForWorkflowTask(WorkflowTestConstants.ACTIVITY_ID_SHIPMENT_ACTION,
				getShipmentWorkflowBusinessKey(getShipments(order, 2).get(1)), runtimeService);
		shipmentID = getShipments(order, 2).get(1).getShipmentId();
		shipmentFacade.confirmShipment(shipmentID);

		waitForProcesses(this.runtimeService);

		final Order orderDB = this.orderFacade.getOrderByOrderId(ORDER_ID);
		Assert.assertEquals(2, orderDB.getOrderLineQuantities().size());
		Assert.assertEquals(10, orderDB.getOrderLineQuantities().get(0).getQuantity().getValue());
		Assert.assertEquals(90, orderDB.getOrderLineQuantities().get(1).getQuantity().getValue());
		Assert.assertEquals(0, orderDB.getOrderLines().get(0).getQuantityUnassigned().getValue());
	}

	@Test
	public void shouldFulfillPartial_AndShipmentConfirm_RetryFulfillment_FailAndCancel()
	{
		final Order order = this.buildOrder();
		order.getOrderLines().get(0).getQuantity().setValue(100);
		order.getOrderLines().get(0).getQuantityUnassigned().setValue(100);
		this.orderFacade.createOrder(order);
		waitForWorkflowTask(WorkflowTestConstants.ACTIVITY_ID_SHIPMENT_ACTION,
				getShipmentWorkflowBusinessKey(getShipments(order).get(0)), runtimeService);
		final String shipmentID = getShipments(order).get(0).getShipmentId();
		shipmentFacade.confirmShipment(shipmentID);

		waitForWorkflowTask(FULFILL_INCOMPLETE_USER_TASK, getOrderWorkflowBusinessKey(order), runtimeService);

		// DO NOT Add more inventory before retrying fulfillment
		orderFacade.fulfill(order.getOrderId());

		waitForWorkflowTask(FULFILL_INCOMPLETE_USER_TASK, getOrderWorkflowBusinessKey(order), runtimeService);
		orderFacade.cancelUnfulfilled(order.getOrderId());

		waitForProcesses(this.runtimeService);

		final Order orderDB = this.orderFacade.getOrderByOrderId(ORDER_ID);
		Assert.assertEquals(1, orderDB.getOrderLineQuantities().size());
		Assert.assertEquals(10, orderDB.getOrderLineQuantities().get(0).getQuantity().getValue());
		Assert.assertEquals(0, orderDB.getOrderLines().get(0).getQuantityUnassigned().getValue());
	}

	@Test
	public void shouldFulfillPartial_CancelShipment_CancelRemainder()
	{
		final Order order = this.buildOrder();
		order.getOrderLines().get(0).getQuantity().setValue(100);
		order.getOrderLines().get(0).getQuantityUnassigned().setValue(100);
		this.orderFacade.createOrder(order);
		waitForWorkflowTask(WorkflowTestConstants.ACTIVITY_ID_SHIPMENT_ACTION,
				getShipmentWorkflowBusinessKey(getShipments(order).get(0)), runtimeService);
		shipmentFacade.cancelShipment(getShipments(order).get(0).getShipmentId());

		waitForWorkflowTask(FULFILL_INCOMPLETE_USER_TASK, getOrderWorkflowBusinessKey(order), runtimeService);
		orderFacade.cancelUnfulfilled(order.getOrderId());
		waitForProcesses(this.runtimeService);

		// Assert that the order is partially sourced
		final Order orderDB = this.orderFacade.getOrderByOrderId(ORDER_ID);
		Assert.assertEquals(10, orderDB.getOrderLineQuantities().get(0).getQuantity().getValue());
		Assert.assertEquals(0, orderDB.getOrderLines().get(0).getQuantityUnassigned().getValue());

		assertShipmentStatus(getShipments(order).get(0), TenantPreferenceConstants.PREF_KEY_OLQSTATUS_CANCELED);
	}

	/* Fulfillment Failed */
	@Test
	public void shouldFailFulfillment_FailSourcing_ThenCancelRemainder()
	{
		final Order order = this.buildOrder();
		order.getOrderLines().get(0).getQuantity().setValue(100);
		order.getOrderLines().get(0).getQuantityUnassigned().setValue(100);
		order.getOrderLines().get(0).setSkuId("INVALID");
		this.orderFacade.createOrder(order);
		waitForWorkflowTask(FULFILL_INCOMPLETE_USER_TASK, getOrderWorkflowBusinessKey(order), runtimeService);
		orderFacade.cancelUnfulfilled(order.getOrderId());

		waitForProcesses(this.runtimeService);

		// Assert that no olqs were created
		final Order orderDB = this.orderFacade.getOrderByOrderId(ORDER_ID);
		Assert.assertTrue(orderDB.getOrderLineQuantities().isEmpty());
		Assert.assertEquals(0, orderDB.getOrderLines().get(0).getQuantityUnassigned().getValue());
	}

	@Test
	public void shouldFailSourcing_AutomaticRetry_ThenCancelRemainder()
	{
		final Order order = this.buildOrder();
		order.getOrderLines().get(0).getQuantity().setValue(100);
		order.getOrderLines().get(0).getQuantityUnassigned().setValue(100);
		order.getOrderLines().get(0).setSkuId("INVALID");
		this.orderFacade.createOrder(order);
		waitForWorkflowTask(FULFILL_INCOMPLETE_USER_TASK, getOrderWorkflowBusinessKey(order), runtimeService);
		delay(5500);
		waitForWorkflowTask(FULFILL_INCOMPLETE_USER_TASK, getOrderWorkflowBusinessKey(order), runtimeService);

		orderFacade.cancelUnfulfilled(order.getOrderId());

		waitForProcesses(this.runtimeService);

		// Assert that no olqs were created
		final Order orderDB = this.orderFacade.getOrderByOrderId(ORDER_ID);
		Assert.assertTrue(orderDB.getOrderLineQuantities().isEmpty());
		Assert.assertEquals(0, orderDB.getOrderLines().get(0).getQuantityUnassigned().getValue());
	}

	@Test
	public void shouldFailFulfillment_FailAllocation_ThenCancelRemainder()
	{
		// Setup mock for allocation that will fail
		final ShipmentService shipmentServiceMock = Mockito.mock(ShipmentService.class);
		fulfillmentWorkItemWorker.setShipmentService(shipmentServiceMock);
		Mockito.when(shipmentServiceMock.createShipmentsForOrder(Mockito.any(OrderData.class))).thenThrow(
				new IllegalArgumentException("*****Failing on purpose.*****"));
		try
		{
			final Order order = this.buildOrder();
			order.getOrderLines().get(0).getQuantity().setValue(100);
			order.getOrderLines().get(0).getQuantityUnassigned().setValue(100);
			this.orderFacade.createOrder(order);
			waitForWorkflowTask(FULFILL_INCOMPLETE_USER_TASK, getOrderWorkflowBusinessKey(order), runtimeService);
			orderFacade.cancelUnfulfilled(order.getOrderId());

			waitForProcesses(this.runtimeService);

			// Assert that no olqs were created
			final Order orderDB = this.orderFacade.getOrderByOrderId(ORDER_ID);
			Assert.assertTrue(orderDB.getOrderLineQuantities().isEmpty());
			Assert.assertEquals(0, orderDB.getOrderLines().get(0).getQuantityUnassigned().getValue());
		}
		finally
		{
			// Reset DefaultShipmentService
			this.fulfillmentWorkItemWorker.setShipmentService(this.shipmentService);
		}
	}

	/* ORDER CANCELLATION TESTS */
	// FIXME: Remove these tests once OrderFacade.cancelOrder has been removed.
	@Test
	public void shouldCancelOrder_ShipmentWaiting()
	{
		final Order order = this.orderFacade.createOrder(this.buildOrder());
		final String orderID = order.getOrderId();
		waitForWorkflowTask(WorkflowTestConstants.ACTIVITY_ID_SHIPMENT_ACTION,
				getShipmentWorkflowBusinessKey(getShipments(order).get(0)), runtimeService);
		this.orderFacade.cancelOrder(orderID);
		waitForProcesses(this.runtimeService);
		assertShipmentStatus(getShipments(order).get(0), TenantPreferenceConstants.PREF_KEY_OLQSTATUS_CANCELED);
	}

	@Test(expected = InvalidOperationException.class)
	public void shouldFailCancelOrder_ShipmentCancelled()
	{
		final Order order = this.orderFacade.createOrder(this.buildOrder());
		final String orderID = order.getOrderId();
		waitForWorkflowTask(WorkflowTestConstants.ACTIVITY_ID_SHIPMENT_ACTION,
				getShipmentWorkflowBusinessKey(getShipments(order).get(0)), runtimeService);
		final String shipmentID = getShipments(order).get(0).getShipmentId();
		shipmentFacade.cancelShipment(shipmentID);
		waitForProcesses(this.runtimeService);

		this.orderFacade.cancelOrder(orderID);
	}

	@Test(expected = InvalidOperationException.class)
	public void shouldFailCancelOrder_ShipmentConfirmed()
	{
		final Order order = this.orderFacade.createOrder(this.buildOrder());
		final String orderID = order.getOrderId();
		waitForWorkflowTask(WorkflowTestConstants.ACTIVITY_ID_SHIPMENT_ACTION,
				getShipmentWorkflowBusinessKey(getShipments(order).get(0)), runtimeService);
		final String shipmentID = getShipments(order).get(0).getShipmentId();
		shipmentFacade.confirmShipment(shipmentID);
		waitForProcesses(this.runtimeService);

		this.orderFacade.cancelOrder(orderID);
	}

	/**
	 * Toggles a workflow step on/off.
	 *
	 * @param tenantPreferenceKey - the {@link TenantPreference} key representing the workflow step.
	 * @param toggle - {@link Boolean} representing whether to enable ({@link Boolean#TRUE}) or disable (
	 *           {@link Boolean#FALSE}) a workflow step.
	 */
	private void toggleWorkflowStep(final String tenantPreferenceKey, final Boolean toggle)
	{
		final TenantPreference preference = preferenceFacade.getTenantPreferenceByKey(tenantPreferenceKey);
		preference.setValue(toggle.toString());
		preferenceFacade.updateTenantPreference(preference);
	}

	/**
	 * Get the shipment from the order.
	 *
	 * @param order
	 * @return shipments
	 */
	private List<Shipment> getShipments(final Order order)
	{
		return getShipments(order, 1);
	}

	/**
	 * Get the shipment from the order.
	 *
	 * @param order
	 * @param qty - the number of shipments that must exist until we return
	 * @return shipments
	 */
	private List<Shipment> getShipments(final Order order, final int qty)
	{
		List<Shipment> shipments = new ArrayList<>();
		boolean present = false;
		while (!present)
		{
			try
			{
				shipments = (List<Shipment>) shipmentFacade.getShipmentsByOrderId(order.getOrderId());
				if (shipments.isEmpty() || shipments.size() != qty)
				{
					throw new IllegalStateException("No Shipments found for order " + order.getOrderId());
				}
				present = true;
			}
			catch (final IllegalStateException e)
			{
				delay();
			}
		}
		Collections.sort(shipments, new Comparator<Shipment>()
				{
			@Override
			public int compare(final Shipment o1, final Shipment o2)
			{
				return o1.getShipmentId().compareTo(o2.getShipmentId());
			}
				});
		return shipments;
	}

	/**
	 * Assert that the given shipment is in the status represented by the tenant preference key provided.
	 *
	 * @param shipment - the shipment to which to verify the status
	 * @param key - the key of the tenant preference of the
	 */
	private void assertShipmentStatus(final Shipment shipment, final String key)
	{
		final TransactionTemplate template = new TransactionTemplate(txManager);
		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				final OrderLineQuantityStatusData olqsd = orderService.getOrderLineQuantityStatusByTenantPreferenceKey(key);
				Assert.assertEquals(shipment.getOlqsStatus(), olqsd.getStatusCode());
			}
		});
	}

	/**
	 * Assert that the workflow represented by the given order has ended.
	 *
	 * @param orderID
	 */
	private void assertOrderWorkflowEnded(final String orderID)
	{
		final TransactionTemplate template = new TransactionTemplate(txManager);
		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				final OrderData object = orderService.getOrderByOrderId(orderID);
				Assert.assertTrue(orderWorkflowExecutor.hasExistingWorkflow(object));
				Assert.assertFalse(orderWorkflowExecutor.isWorkflowInProgress(object));
			}
		});
	}


	/**
	 * Assert that the workflow represented by the given order still in progress.
	 *
	 * @param order
	 */
	private void assertOrderWorkflowInProgress(final Order order)
	{
		final TransactionTemplate template = new TransactionTemplate(txManager);
		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				final OrderData object = orderService.getOrderByOrderId(order.getOrderId());
				Assert.assertTrue(orderWorkflowExecutor.isWorkflowInProgress(object));
			}
		});
	}

	/**
	 * Assert that the workflow represented by the given shipment has ended.
	 *
	 * @param shipmentID
	 */
	private void assertShipmentWorkflowEnded(final String shipmentID)
	{
		final TransactionTemplate template = new TransactionTemplate(txManager);
		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				final ShipmentData object = shipmentService.getShipmentById(Long.valueOf(shipmentID));
				Assert.assertTrue(shipmentWorkflowExecutor.hasExistingWorkflow(object));
				Assert.assertFalse(shipmentWorkflowExecutor.isWorkflowInProgress(object));
			}
		});
	}

	/**
	 * Get the workflow business key represented by the shipment id.
	 *
	 * @param shipment
	 * @return business key
	 */
	private String getShipmentWorkflowBusinessKey(final Shipment shipment)
	{
		return new TransactionTemplate(txManager).execute(new TransactionCallback<String>()
				{
			@Override
			public String doInTransaction(final TransactionStatus status)
			{
				return shipmentWorkflowExecutor.getBusinessKey(shipmentService.getShipmentById(Long.valueOf(shipment.getShipmentId())));
			}
				});
	}

	/**
	 * Get the workflow business key represented by the order id.
	 *
	 * @param order
	 * @return business key
	 */
	private String getOrderWorkflowBusinessKey(final Order order)
	{
		return new TransactionTemplate(txManager).execute(new TransactionCallback<String>()
				{
			@Override
			public String doInTransaction(final TransactionStatus status)
			{
				return orderWorkflowExecutor.getBusinessKey(orderService.getOrderByOrderId(order.getOrderId()));
			}
				});
	}

	/**
	 * Create two new {@link Location}s and add inventory to them.
	 */
	private void createLocationAndInventory()
	{
		createLocation(LOC_ID_1);
		createLocation(LOC_ID_2);
		createInventory(SKU_1, LOC_ID_1, ON_HAND, QUANTITY);
		createInventory(SKU_1, LOC_ID_1, NOT_AVAILABLE, ZERO);
		createInventory(SKU_2, LOC_ID_2, ON_HAND, QUANTITY);
		createInventory(SKU_2, LOC_ID_2, NOT_AVAILABLE, ZERO);
	}

	/**
	 * Creates a new {@link Location} with given location id.
	 *
	 * @param locationId
	 */
	private void createLocation(final String locationId)
	{
		final Location location = new Location();
		location.setLocationId(locationId);
		location.setPriority(1);
		location.setShipToCountriesCodes(Collections.singleton("CA"));
		location.setLocationRoles(Collections.singleton(LocationRole.SHIPPING));
		location.setAddress(AddressBuilder.anAddress().buildAddressDTO());
		this.inventoryFacade.createStockRoomLocation(location);
	}

	/**
	 * Creates inventory for a given sku/location/status.
	 *
	 * @param sku
	 * @param locationId
	 * @param status
	 * @param quantity
	 */
	private void createInventory(final String sku, final String locationId, final String status, final int quantity)
	{
		final OmsInventory inventory = new OmsInventory();
		inventory.setSkuId(sku);
		inventory.setLocationId(locationId);
		inventory.setStatus(status);
		inventory.setQuantity(quantity);
		this.inventoryFacade.createInventory(inventory);
	}

	private Order buildOrder()
	{
		return buildOrder(false);
	}

	/**
	 * Build a new {@link Order} object.
	 *
	 * @param numShipments - the number of shipments that should be created
	 * @return a new order
	 */
	private Order buildOrder(final boolean twoShipments)
	{
		final Order order = new Order();
		order.setOrderId(ORDER_ID);

		order.setUsername("IntegrationTest");
		order.setFirstName("Chuck");
		order.setLastName("Norris");
		order.setEmailid("chuck.norris@hybris.com");
		order.setShippingFirstName("shippingFirstName");
		order.setShippingLastName("shippingLastName");
		order.setShippingTaxCategory("shippingTaxCategory");
		order.setIssueDate(Calendar.getInstance().getTime());
		order.setCurrencyCode("USD");

		final ShippingAndHandling shippingAndHandling = new ShippingAndHandling();
		shippingAndHandling.setOrderId(order.getOrderId());
		shippingAndHandling.setShippingPrice(new Price(new Amount("USD", 2d), new Amount("USD", 0.5d), new Amount("USD", 0d)));

		order.setShippingAndHandling(shippingAndHandling);
		order.setContact(new Contact(null, "arvato", null, null, "1234567", null, null));
		order.setShippingAddress(AddressBuilder.anAddress().buildAddressDTO());
		order.setShippingMethod("DOM.EP");
		order.setPriorityLevelCode("STANDARD");

		final OrderLine orderLine = this.buildOrderLine(ORDER_LINE_ID_1, SKU_1, new Quantity("unit", QUANTITY), new Quantity(
				"unit", QUANTITY), new Amount("USD", 1d), new Amount("USD", 0.15d), "AE514", LocationRole.SHIPPING,
				new OrderLineAttribute(ORDERLINE_ATTRIBUTE_VALUE, ORDERLINE_ATTRIBUTE_ID));
		order.setOrderLines(Lists.newArrayList(orderLine));

		if (twoShipments)
		{
			final OrderLine orderLine2 = this.buildOrderLine(ORDER_LINE_ID_2, SKU_2, new Quantity("unit", QUANTITY), new Quantity(
					"unit", QUANTITY), new Amount("USD", 1d), new Amount("USD", 0.15d), "AE514", LocationRole.SHIPPING,
					new OrderLineAttribute(ORDERLINE_ATTRIBUTE_VALUE, ORDERLINE_ATTRIBUTE_ID));
			order.getOrderLines().add(orderLine2);
		}

		final PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setPaymentInfoType("Visa");
		paymentInfo.setAuthUrl("http://authURL.hybris.com");
		paymentInfo.setBillingAddress(AddressBuilder.anAddress().buildAddressDTO());

		order.setPaymentInfos(Lists.newArrayList(paymentInfo));
		return order;
	}

	/**
	 * Build a new {@link OrderLine} object.
	 *
	 * @return a new OrderLine
	 */
	private OrderLine buildOrderLine(final String orderLineId, final String skuId, final Quantity quantity,
			final Quantity quantityUnassigned, final Amount unitPrice, final Amount unitTax, final String taxCategory,
			final LocationRole locationRole, final OrderLineAttribute orderlineAttribute)
	{
		final OrderLine orderLine = new OrderLine();
		orderLine.setOrderLineId(orderLineId);
		orderLine.setSkuId(skuId);
		orderLine.setQuantity(quantity);
		orderLine.setQuantityUnassigned(quantityUnassigned);
		orderLine.setUnitPrice(unitPrice);
		orderLine.setUnitTax(unitTax);
		orderLine.setTaxCategory(taxCategory);

		final HashSet<LocationRole> roles = new HashSet<>();
		roles.add(locationRole);
		orderLine.setLocationRoles(roles);
		orderLine.addOrderLineAttribute(orderlineAttribute);
		return orderLine;
	}

	/**
	 * Stores a list of tenant preferences
	 */
	private void storeOriginalTenantPreferences()
	{
		ORIGINAL_PREFERENCES = new ArrayList<TenantPreference>();

		final TenantPreference preferenceFulfillment = preferenceFacade
				.getTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_WF_EXEC_TASK_FULFILLMENT);
		final TenantPreference preferencePayment = preferenceFacade
				.getTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_WF_EXEC_TASK_PAYMENTCAPTURE);
		final TenantPreference preferenceTaxes = preferenceFacade
				.getTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_WF_EXEC_TASK_TAXINVOICE);

		ORIGINAL_PREFERENCES.add(preferenceFulfillment);
		ORIGINAL_PREFERENCES.add(preferencePayment);
		ORIGINAL_PREFERENCES.add(preferenceTaxes);
	}

	/**
	 * Reset modified tenant preferences to original state
	 */
	private void resetTenantPreferencesToOriginalState()
	{
		for (final TenantPreference originalPreference : ORIGINAL_PREFERENCES)
		{
			final TenantPreference currentPreference = preferenceFacade.getTenantPreferenceByKey(originalPreference.getProperty());
			currentPreference.setValue(originalPreference.getValue());
			preferenceFacade.updateTenantPreference(currentPreference);
		}
	}

}
