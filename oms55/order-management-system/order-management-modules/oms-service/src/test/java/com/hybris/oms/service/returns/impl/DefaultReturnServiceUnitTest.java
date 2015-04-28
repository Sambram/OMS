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
package com.hybris.oms.service.returns.impl;

import static com.hybris.oms.service.workflow.WorkflowConstants.STATE_RETURN_CANCELED;
import static com.hybris.oms.service.workflow.WorkflowConstants.STATE_RETURN_UPDATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;
import com.hybris.oms.service.managedobjects.preference.TenantPreferenceData;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.managedobjects.returns.ReturnDataPojo;
import com.hybris.oms.service.managedobjects.returns.ReturnLineRejectionData;
import com.hybris.oms.service.managedobjects.returns.ReturnLineRejectionDataPojo;
import com.hybris.oms.service.managedobjects.returns.ReturnOrderLineData;
import com.hybris.oms.service.managedobjects.returns.ReturnOrderLineDataPojo;
import com.hybris.oms.service.managedobjects.types.QuantityVT;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.preference.TenantPreferenceService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;


public class DefaultReturnServiceUnitTest
{
	private static final String ORDERLINE_ID = "1234";
	@InjectMocks
	@Spy
	private DefaultReturnService defaultReturnService;

	@Mock
	private TenantPreferenceData tpData;

	@Mock
	private ReturnData aReturn;

	@Mock
	private ReturnOrderLineData returnOrderLine;

	@Mock
	private OrderData order;

	@Mock
	private OrderLineData orderLine;

	@Mock
	private OrderLineQuantityData olq;

	@Mock
	private OrderLineQuantityStatusData olqStatus;

	@Mock
	private OrderService orderService;

	@Mock
	private TenantPreferenceService tenantPreferenceService;

	@Mock
	private ReturnQueryFactory returnQueryFactory;

	@Mock
	private PersistenceManager persistenceManager;

	private static final String OLQ_STATUSES = "STEP1|STEP2";
	private static final String OLQ_STATUS_2 = "STEP2";
	private static final String OLQ_STATUS_3 = "STEP3";
	private static final String SKU_1 = "1234";
	private static final String SKU_2 = "9876";

	@Before
	public void setUp() throws InterruptedException
	{
		MockitoAnnotations.initMocks(this);

		when(persistenceManager.create(ReturnLineRejectionData.class)).thenReturn(new ReturnLineRejectionDataPojo());
	}

	@Test
	public void testIsShippingRefundedReturnNull()
	{
		Long returnId = 1l;
		assertFalse(defaultReturnService.isShippingRefundedReturnNull(returnId, aReturn));

		returnId = null;
		when(aReturn.isShippingRefunded()).thenReturn(Boolean.FALSE);
		assertFalse(defaultReturnService.isShippingRefundedReturnNull(returnId, aReturn));

		when(aReturn.isShippingRefunded()).thenReturn(Boolean.TRUE);
		when(aReturn.getState()).thenReturn(STATE_RETURN_CANCELED);
		assertFalse(defaultReturnService.isShippingRefundedReturnNull(returnId, aReturn));

		when(aReturn.getState()).thenReturn(STATE_RETURN_UPDATE);
		assertTrue(defaultReturnService.isShippingRefundedReturnNull(returnId, aReturn));
	}

	@Test
	public void testIsShippingRefundedReturnNotNull()
	{
		Long returnId = null;
		assertFalse(defaultReturnService.isShippingRefundedReturnNotNull(returnId, aReturn));

		returnId = 1l;
		when(aReturn.getReturnId()).thenReturn(1l);
		assertFalse(defaultReturnService.isShippingRefundedReturnNotNull(returnId, aReturn));

		when(aReturn.getReturnId()).thenReturn(2l);
		when(aReturn.isShippingRefunded()).thenReturn(Boolean.FALSE);
		assertFalse(defaultReturnService.isShippingRefundedReturnNotNull(returnId, aReturn));

		when(aReturn.isShippingRefunded()).thenReturn(Boolean.TRUE);
		when(aReturn.getState()).thenReturn(STATE_RETURN_CANCELED);
		assertFalse(defaultReturnService.isShippingRefundedReturnNotNull(returnId, aReturn));

		when(aReturn.getState()).thenReturn(STATE_RETURN_UPDATE);
		assertTrue(defaultReturnService.isShippingRefundedReturnNotNull(returnId, aReturn));
	}

	@Test
	public void shouldIncrementReturnableQuantity()
	{
		when(orderService.getOrderLineByOrderIdAndOrderLineId(Mockito.anyString(), Mockito.anyString())).thenReturn(orderLine);
		when(tenantPreferenceService.getTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_RETURN_SHIPPED_STATUS))
				.thenReturn(tpData);
		when(tpData.getValue()).thenReturn(OLQ_STATUSES);
		when(order.getOrderLines()).thenReturn(ImmutableList.of(orderLine));


		// Quantity should not be incremented
		assertEquals(5, defaultReturnService.incrementReturnableQuantity(null, null, 5));

		when(orderLine.getOrderLineQuantities()).thenReturn(ImmutableList.of(olq));
		when(olq.getStatus()).thenReturn(olqStatus);
		when(olqStatus.getStatusCode()).thenReturn(OLQ_STATUS_3);

		assertEquals(5, defaultReturnService.incrementReturnableQuantity(null, null, 5));

		// Now, quantity should be incremented
		when(olqStatus.getStatusCode()).thenReturn(OLQ_STATUS_2);
		when(olq.getQuantityValue()).thenReturn(6);

		// Expected value is 11 (5 initially + 6 after increment)
		assertEquals(11, defaultReturnService.incrementReturnableQuantity(null, null, 5));
	}

	@Test
	public void shouldDecreaseReturnableQuantity()
	{
		doReturn(ImmutableList.of(aReturn)).when(defaultReturnService).findReturnsByOrderId(Mockito.anyString());
		final Long returnId = 1l;
		when(aReturn.getReturnId()).thenReturn(returnId);
		when(aReturn.getOrder()).thenReturn(order);
		when(aReturn.getState()).thenReturn(STATE_RETURN_UPDATE);
		when(aReturn.getReturnOrderLines()).thenReturn(ImmutableList.of(returnOrderLine));
		when(orderService.getOrderLineByOrderIdAndOrderLineId(Mockito.anyString(), Mockito.anyString())).thenReturn(orderLine);
		when(orderLine.getOrderLineId()).thenReturn(ORDERLINE_ID);

		// Quantity should not be decreased
		assertEquals(9, defaultReturnService.decreaseReturnableQuantity(returnId, null, ORDERLINE_ID, 9));

		when(returnOrderLine.getQuantity()).thenReturn(new QuantityVT(null, 3));
		when(aReturn.getReturnId()).thenReturn(2L);

		// Quantity should be decreased
		assertEquals(6, defaultReturnService.decreaseReturnableQuantity(returnId, null, ORDERLINE_ID, 9));
	}

	@Test(expected = EntityValidationException.class)
	public void shouldNotGetReturnableQuantity()
	{
		defaultReturnService.getReturnableQuantity(aReturn, order, null, null);
		when(order.getOrderId()).thenReturn(null);
	}

	@Test
	public void shouldGetQuantityRejected_NoRejections()
	{
		final ReturnOrderLineData localReturnOrderLine = new ReturnOrderLineDataPojo();
		final Integer quantityRejected = defaultReturnService.getQuantityRejected(localReturnOrderLine);
		assertEquals(Integer.valueOf(0), quantityRejected);
	}

	@Test
	public void shouldGetQuantityRejected_WithRejections()
	{
		final ReturnOrderLineData localReturnOrderLine = new ReturnOrderLineDataPojo();

		final ReturnLineRejectionData returnLineRejection1 = new ReturnLineRejectionDataPojo();
		returnLineRejection1.setMyReturnOrderLine(localReturnOrderLine);
		returnLineRejection1.setQuantity(5);

		final ReturnLineRejectionData returnLineRejection2 = new ReturnLineRejectionDataPojo();
		returnLineRejection2.setMyReturnOrderLine(localReturnOrderLine);
		returnLineRejection2.setQuantity(3);

		localReturnOrderLine.setReturnLineRejections(Lists.newArrayList(returnLineRejection1, returnLineRejection2));

		final Integer quantityRejected = defaultReturnService.getQuantityRejected(localReturnOrderLine);
		assertEquals(Integer.valueOf(8), quantityRejected);
	}

	@Test
	public void shouldAcceptItems_NoRejections()
	{
		final ReturnData theReturn = new ReturnDataPojo();
		final ReturnOrderLineData localReturnOrderLine = new ReturnOrderLineDataPojo();
		localReturnOrderLine.setMyReturn(theReturn);
		localReturnOrderLine.setQuantity(new QuantityVT("units", 5));
		theReturn.setReturnOrderLines(Lists.newArrayList(localReturnOrderLine));

		defaultReturnService.acceptAllNonRejectedItems(theReturn);
		assertEquals(1, localReturnOrderLine.getReturnLineRejections().size());
		assertEquals(0, localReturnOrderLine.getReturnLineRejections().get(0).getQuantity());
	}

	@Test
	public void shouldAcceptItems_PartiallyRejected()
	{
		final ReturnData theReturn = new ReturnDataPojo();
		final ReturnOrderLineData localReturnOrderLine = new ReturnOrderLineDataPojo();
		localReturnOrderLine.setMyReturn(theReturn);
		localReturnOrderLine.setQuantity(new QuantityVT("units", 5));
		theReturn.setReturnOrderLines(Lists.newArrayList(localReturnOrderLine));

		final ReturnLineRejectionData returnLineRejection = new ReturnLineRejectionDataPojo();
		returnLineRejection.setMyReturnOrderLine(localReturnOrderLine);
		returnLineRejection.setQuantity(2);
		localReturnOrderLine.setReturnLineRejections(Lists.newArrayList(returnLineRejection));

		defaultReturnService.acceptAllNonRejectedItems(theReturn);
		assertEquals(2, localReturnOrderLine.getReturnLineRejections().size());
		assertEquals(0, localReturnOrderLine.getReturnLineRejections().get(1).getQuantity());
	}

	@Test
	public void shouldAcceptItems_AllRejected()
	{
		final ReturnData theReturn = new ReturnDataPojo();
		final ReturnOrderLineData localReturnOrderLine = new ReturnOrderLineDataPojo();
		localReturnOrderLine.setMyReturn(theReturn);
		localReturnOrderLine.setQuantity(new QuantityVT("units", 5));
		theReturn.setReturnOrderLines(Lists.newArrayList(localReturnOrderLine));

		final ReturnLineRejectionData returnLineRejection1 = new ReturnLineRejectionDataPojo();
		returnLineRejection1.setMyReturnOrderLine(localReturnOrderLine);
		returnLineRejection1.setQuantity(2);
		final ReturnLineRejectionData returnLineRejection2 = new ReturnLineRejectionDataPojo();
		returnLineRejection2.setMyReturnOrderLine(localReturnOrderLine);
		returnLineRejection2.setQuantity(3);
		localReturnOrderLine.setReturnLineRejections(Lists.newArrayList(returnLineRejection1, returnLineRejection2));

		defaultReturnService.acceptAllNonRejectedItems(theReturn);
		assertEquals(2, localReturnOrderLine.getReturnLineRejections().size());
	}
}
