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
package com.hybris.oms.rest.integration.returns;

import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.api.returns.ReturnFacade;
import com.hybris.oms.api.shipment.ShipmentFacade;
import com.hybris.oms.domain.exception.DuplicateEntityException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.InvalidOperationException;
import com.hybris.oms.domain.returns.*;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


public class ReturnPostIntegrationTest extends RestClientIntegrationTest
{

	private static final String NOT_EXISTING_RETURN_ID = "9999999";
	public static final String CANCELED = "CANCELED";
	public static final String WFE_SUCCESS_TAX_REVERSE = "WFE_SUCCESS_TAX_REVERSE";
	private static final String ORDER_LINE_ID = "123456";
	@Autowired
	private InventoryFacade inventoryFacade;
	@Autowired
	private ReturnFacade returnFacade;
	@Autowired
	private ShipmentFacade shipmentFacade;
	private OmsInventory inventory;
	private List<Shipment> shipments;
	private Return aReturn;

	@After
	public void tearDown()
	{
		if (this.inventory != null)
		{
			this.inventoryFacade.deleteInventory(this.inventory);
		}
	}

	@Test
	public void testCreateInStoreReturn()
	{
		// when
		prepareAReturnForSimpleShipment(1);
		createDefaultReturn();
		final Return resultedReturn = returnFacade.getReturnById(aReturn.getReturnId());

		// then
		this.validateReturn(resultedReturn);
		assertEquals(aReturn.getReturnId(), resultedReturn.getReturnId());
	}


	@Test
	public void testCreateReturnReviews()
	{
		prepareAReturnForSimpleShipment(11, 10);
		createOnlineReturn();

		final ReturnLineRejection returnLineRejectionApproved = new ReturnLineRejection();
		returnLineRejectionApproved.setReason(ReviewReason.DAMAGED);
		returnLineRejectionApproved.setReturnOrderLineId(aReturn.getReturnOrderLines().get(0).getReturnOrderLineId());
		returnLineRejectionApproved.setQuantity(1);
		returnLineRejectionApproved.setResponsible("The Responsible");

		final List<ReturnLineRejection> returnLineRejections = new ArrayList<>();
		returnLineRejections.add(returnLineRejectionApproved);

		final ReturnReview returnReview = new ReturnReview();
		returnReview.setReturnId(aReturn.getReturnId());
		returnReview.setReturnLineRejections(returnLineRejections);

		returnFacade.autoRefundReturn(aReturn.getReturnId());
		delay();

		returnFacade.createReturnReview(returnReview);

		final Return returnPersisted = returnFacade.getReturnById(aReturn.getReturnId());
		final ReturnOrderLine returnOrderLinePersisted = returnPersisted.getReturnOrderLines().get(0);

		assertFalse(returnOrderLinePersisted.getReturnLineRejections().isEmpty());
		assertEquals(1, returnOrderLinePersisted.getReturnLineRejections().size());
	}

	@Test
	public void testCreateOnlineReturn()
	{
		// when
		prepareAReturnForSimpleShipment(1);
		createOnlineReturn();
		final Return resultedReturn = returnFacade.getReturnById(aReturn.getReturnId());

		// then
		this.validateReturn(resultedReturn);
		assertEquals(aReturn.getReturnId(), resultedReturn.getReturnId());
		assertNotNull(resultedReturn.getReturnShipment());
		assertNotNull(resultedReturn.getReturnShipment().getReturnShipmentId());
	}

	@Test
	public void shouldHaveReturnActions()
	{
		prepareAReturnForSimpleShipment(1);
		createDefaultReturn();
		final Return theReturn = returnFacade.getReturnById(aReturn.getReturnId());

		Assert.assertTrue(theReturn.getActions().contains("CANCEL"));
		Assert.assertTrue(theReturn.getActions().contains("UPDATE"));
		Assert.assertTrue(theReturn.getActions().contains("MANUAL_REFUND"));
		Assert.assertTrue(theReturn.getActions().contains("AUTO_REFUND"));
	}

	@Test(expected = DuplicateEntityException.class)
	public void testCreateDuplicateReturn()
	{
		// given
		prepareAReturnForSimpleShipment(1);
		setIdsOnReturn(this.aReturn);

		// when
		this.returnFacade.createReturn(aReturn);
		this.returnFacade.createReturn(aReturn);
	}

	@Test
	public void shouldAcceptNullIdsOnReturn()
	{
		// when
		prepareAReturnForSimpleShipment(1);
		setIdsOnReturn(aReturn);
		this.aReturn.setReturnId(null);
		this.aReturn.getReturnOrderLines().get(0).setReturnOrderLineId(null);
		returnFacade.createReturn(this.aReturn);
	}

	@Test(expected = EntityNotFoundException.class)
	public void shouldThrowWhenUpdateWithNotExistingDBReturn()
	{
		prepareAReturnForSimpleShipment(1);
		this.aReturn.setReturnId(NOT_EXISTING_RETURN_ID);
		returnFacade.updateReturn(this.aReturn);
	}

	@Test
	public void testUpdateReturn()
	{
		// given
		prepareAReturnForSimpleShipment(1);
		createDefaultReturn();

		// when
		this.aReturn.setCustomTotalRefundAmount(new Amount("Test", 20.0));


		returnFacade.updateReturn(this.aReturn);
		final Return updatedReturn = returnFacade.getReturnById(aReturn.getReturnId());

		// then
		assertThat(updatedReturn.getReturnId(), is(aReturn.getReturnId()));
		assertThat(updatedReturn.getCustomTotalRefundAmount(), is(aReturn.getCustomTotalRefundAmount()));
		validateReturn(updatedReturn);
	}

	@Test
	public void testCancelReturn()
	{
		// given
		prepareAReturnForSimpleShipment(1);
		createDefaultReturn();

		// when
		returnFacade.cancelReturn(aReturn.getReturnId());
		final Return canceledReturn = returnFacade.getReturnById(aReturn.getReturnId());

		// then
		assertEquals(aReturn.getReturnId(), canceledReturn.getReturnId());
		assertEquals(CANCELED, canceledReturn.getState());
	}

	// ///////////////////////////////////////////////////////
	@Test(expected = InvalidOperationException.class)
	public void testCancelReturn_CancelledReturn()
	{
		// given
		prepareAReturnForSimpleShipment(1);
		createDefaultReturn();
		returnFacade.cancelReturn(aReturn.getReturnId());
		delay();
		final Return canceledReturn = returnFacade.getReturnById(aReturn.getReturnId());

		// when
		returnFacade.autoRefundReturn(canceledReturn.getReturnId());
	}

	@Test(expected = InvalidOperationException.class)
	public void testCancelReturn_AutoRefundedReturn()
	{
		// given
		prepareAReturnForSimpleShipment(1);
		createDefaultReturn();
		returnFacade.autoRefundReturn(aReturn.getReturnId());
		delay();
		final Return autoRefundedReturn = returnFacade.getReturnById(aReturn.getReturnId());

		// when
		returnFacade.autoRefundReturn(autoRefundedReturn.getReturnId());
	}

	@Test(expected = InvalidOperationException.class)
	public void testCancelReturn_ManualRefundedReturn()
	{
		// given
		prepareAReturnForSimpleShipment(1);
		createDefaultReturn();
		returnFacade.manualRefundReturn(aReturn.getReturnId());
		delay();
		final Return manualRefundedReturn = returnFacade.getReturnById(aReturn.getReturnId());

		// when
		returnFacade.autoRefundReturn(manualRefundedReturn.getReturnId());
	}

	// //////////////////////////////////////////////////


	@Test
	public void manualRefundReturn() throws InterruptedException
	{

		// given
		prepareAReturnForSimpleShipment(1);
		createDefaultReturn();

		// when
		returnFacade.manualRefundReturn(aReturn.getReturnId());
		delay();
		final Return acceptedReturn = returnFacade.getReturnById(aReturn.getReturnId());

		// then
		assertEquals(aReturn.getReturnId(), acceptedReturn.getReturnId());
		assertEquals(WFE_SUCCESS_TAX_REVERSE, acceptedReturn.getState());
	}

	/**
	 * Check that a manualRefund is not supported if the return is completed
	 *
	 * @throws InterruptedException
	 */
	@Test(expected = InvalidOperationException.class)
	public void unsupportedManualRefundReturn() throws InterruptedException
	{
		// given
		prepareAReturnForSimpleShipment(1);
		createDefaultReturn();
		returnFacade.manualRefundReturn(aReturn.getReturnId());
		delay();


		// when
		returnFacade.manualRefundReturn(aReturn.getReturnId());
	}

	@Test
	public void autoRefundReturn() throws InterruptedException
	{
		// given
		prepareAReturnForSimpleShipment(1);
		createDefaultReturn();

		// when
		returnFacade.autoRefundReturn(aReturn.getReturnId());
		delay();
		final Return acceptedReturn = returnFacade.getReturnById(aReturn.getReturnId());

		// then
		assertEquals(aReturn.getReturnId(), acceptedReturn.getReturnId());
		assertEquals(WFE_SUCCESS_TAX_REVERSE, acceptedReturn.getState());
	}

	/**
	 * Check that an autoRefund is not supported if the return is completed
	 *
	 * @throws InterruptedException
	 */
	@Test(expected = InvalidOperationException.class)
	public void unsupportedAutoRefundReturn() throws InterruptedException
	{
		// given
		prepareAReturnForSimpleShipment(1);
		createDefaultReturn();
		returnFacade.manualRefundReturn(aReturn.getReturnId());
		delay();

		// when
		returnFacade.autoRefundReturn(aReturn.getReturnId());
	}


	// ///////////////////////////////////////////////////////
	@Test(expected = InvalidOperationException.class)
	public void testAutoRefund_CancelledReturn()
	{
		// given
		prepareAReturnForSimpleShipment(1);
		createDefaultReturn();
		returnFacade.cancelReturn(aReturn.getReturnId());
		final Return canceledReturn = returnFacade.getReturnById(aReturn.getReturnId());

		// when
		returnFacade.autoRefundReturn(canceledReturn.getReturnId());
	}

	@Test(expected = InvalidOperationException.class)
	public void testAutoRefund_AutoRefundedReturn()
	{
		// given
		prepareAReturnForSimpleShipment(1);
		createDefaultReturn();
		returnFacade.autoRefundReturn(aReturn.getReturnId());
		delay();
		final Return autoRefundedReturn = returnFacade.getReturnById(aReturn.getReturnId());


		// when
		returnFacade.autoRefundReturn(autoRefundedReturn.getReturnId());
	}

	@Test(expected = InvalidOperationException.class)
	public void testAutoRefund_ManualRefundedReturn()
	{
		// given
		prepareAReturnForSimpleShipment(1);
		createDefaultReturn();
		returnFacade.manualRefundReturn(aReturn.getReturnId());
		delay();
		final Return manualRefundedReturn = returnFacade.getReturnById(aReturn.getReturnId());

		// when
		returnFacade.autoRefundReturn(manualRefundedReturn.getReturnId());
	}

	// //////////////////////////////////////////////////
	/**
	 * Create a new return and wait so the workflow has the time to start and go to the user task ("confirm refund")
	 *
	 * @return
	 */
	private Return createDefaultReturn()
	{
		setIdsOnReturn(aReturn);
		final Return newReturn = returnFacade.createReturn(aReturn);
		delay();

		return newReturn;
	}

	private Return createOnlineReturn()
	{
		setIdsOnReturn(aReturn);
		aReturn.setReturnShipment(ReturnTestUtils.createReturnShipmentDto());

		final Return newReturn = returnFacade.createReturn(aReturn);
		delay();

		return newReturn;
	}

	private void setIdsOnReturn(final Return aReturn)
	{

		aReturn.setReturnId(getUniqueIdBasedOnCurrentTime());
		aReturn.getReturnOrderLines().get(0).setReturnOrderLineId(getUniqueIdBasedOnCurrentTime());
		aReturn.getReturnPaymentInfos().setReturnPaymentInfoId(getUniqueIdBasedOnCurrentTime());
	}

	private String getUniqueIdBasedOnCurrentTime()
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return dateFormat.format(cal.getTime());
	}

	private void validateReturn(final Return someReturn)
	{
		assertNotNull(someReturn);
		assertNotNull(someReturn.getReturnOrderLines());
		assertEquals(1, someReturn.getReturnOrderLines().size());
		assertNotNull(someReturn.getReturnPaymentInfos());
	}

	public void prepareAReturnForSimpleShipment(final int returnQuantity)
	{
		final String orderId = "order_" + this.generateRandomString();
		inventory = createInventory();
		this.shipments = this.buildShipments(inventory.getSkuId(), orderId, inventory.getLocationId(), ORDER_LINE_ID);
		this.aReturn = ReturnTestUtils.createReturnDto(orderId, inventory.getSkuId(), returnQuantity);
		this.aReturn.getReturnOrderLines().get(0).getOrderLine().setOrderLineId(ORDER_LINE_ID);

		for (final Shipment shipment : this.shipments)
		{
			this.shipmentFacade.confirmShipment(shipment.getShipmentId());
		}
		this.delay(); // shipment must be confirmed
	}

	public void prepareAReturnForSimpleShipment(final int originalQuantity, final int returnQuantity)
	{
		final String orderId = "order_" + this.generateRandomString();
		inventory = createInventory();
		this.shipments = this.buildShipments(inventory.getSkuId(), orderId, inventory.getLocationId(), ORDER_LINE_ID,
				originalQuantity);
		this.aReturn = ReturnTestUtils.createReturnDto(orderId, inventory.getSkuId(), returnQuantity);
		this.aReturn.getReturnOrderLines().get(0).getOrderLine().setOrderLineId(ORDER_LINE_ID);

		for (final Shipment shipment : this.shipments)
		{
			this.shipmentFacade.confirmShipment(shipment.getShipmentId());
		}
		this.delay(); // shipment must be confirmed
	}

}
