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
package com.hybris.oms.service.workflow.worker.impl;

import com.hybris.oms.service.workflow.worker.WorkItemWorker;
import com.hybris.oms.service.workflow.worker.order.FulfillmentCompletionWorkItemWorker;
import com.hybris.oms.service.workflow.worker.order.FulfillmentWorkItemWorker;
import com.hybris.oms.service.workflow.worker.order.GeocodingWorkItemWorker;
import com.hybris.oms.service.workflow.worker.order.ShipmentsCompletionWorkItemWorker;
import com.hybris.oms.service.workflow.worker.shipment.DeclineShipmentWorkItemWorker;
import com.hybris.oms.service.workflow.worker.shipment.PaymentWorkItemWorker;
import com.hybris.oms.service.workflow.worker.shipment.ReallocateShipmentWorkItemWorker;
import com.hybris.oms.service.workflow.worker.shipment.TaxesWorkItemWorker;

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-service-spring-test.xml"})
public class WorkItemWorkerFactoryTest
{
	// Order workflow service tasks
	public static final String ACTION_GEOCODING = "WFE_GEOCODING";
	public static final String ACTION_FULFILLMENT = "WFE_FULFILLMENT";
	public static final String ACTION_FULFILLMENT_COMPLETION = "WFE_FULFILLMENT_COMPLETION";
	public static final String ACTION_SHIPMENTS_COMPLETION = "WFE_SHIPMENTS_COMPLETION";

	// Shipment workflow service tasks
	public static final String ACTION_PAYMENT = "WFE_PAYMENT_CAPTURE";
	public static final String ACTION_TAXES = "WFE_TAX_INVOICE";
	public static final String ACTION_SHIPMENT_SUCCESS = "WFE_SUCCESS_SHIPMENT";
	public static final String ACTION_SHIPMENT_FAIL_PAYMENT = "WFE_FAILED_PAYMENT";
	public static final String ACTION_SHIPMENT_FAIL_TAXES = "WFE_FAILED_TAX";
	public static final String ACTION_SHIPMENT_CANCEL = "WFE_CANCELLED_SHIPMENT";
	public static final String ACTION_SHIPMENT_DECLINE = "WFE_DECLINED_SHIPMENT";
	public static final String ACTION_SHIPMENT_REALLOCATE = "WFE_REALLOCATE_SHIPMENT";

	private static final String INVALID = "INVALID";

	@Autowired
	private WorkItemWorkerFactory factory;

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowException()
	{
		this.factory.getWorkItemWorker(INVALID);
	}

	@Test
	public void shouldReturnGeocodingWorker()
	{
		final WorkItemWorker worker = this.factory.getWorkItemWorker(ACTION_GEOCODING);
		Assertions.assertThat(worker).isInstanceOf(GeocodingWorkItemWorker.class);
	}

	@Test
	public void shouldReturnOrderCompletionWorker()
	{
		final WorkItemWorker worker = this.factory.getWorkItemWorker(ACTION_SHIPMENTS_COMPLETION);
		Assertions.assertThat(worker).isInstanceOf(ShipmentsCompletionWorkItemWorker.class);
	}

	@Test
	public void shouldReturnFulfillmentWorker()
	{
		final WorkItemWorker worker = this.factory.getWorkItemWorker(ACTION_FULFILLMENT);
		Assertions.assertThat(worker).isInstanceOf(FulfillmentWorkItemWorker.class);
	}

	@Test
	public void shouldReturnFulfillmentCompletionWorker()
	{
		final WorkItemWorker worker = this.factory.getWorkItemWorker(ACTION_FULFILLMENT_COMPLETION);
		Assertions.assertThat(worker).isInstanceOf(FulfillmentCompletionWorkItemWorker.class);
	}

	@Test
	public void shouldReturnPaymentWorker()
	{
		final WorkItemWorker worker = this.factory.getWorkItemWorker(ACTION_PAYMENT);

		Assertions.assertThat(worker).isInstanceOf(PaymentWorkItemWorker.class);
	}

	@Test
	public void shouldReturnTaxesWorker()
	{
		final WorkItemWorker worker = this.factory.getWorkItemWorker(ACTION_TAXES);
		Assertions.assertThat(worker).isInstanceOf(TaxesWorkItemWorker.class);
	}

	@Test
	public void shouldReturnShipmentDeclineWorker()
	{
		final WorkItemWorker worker = this.factory.getWorkItemWorker(ACTION_SHIPMENT_DECLINE);
		Assertions.assertThat(worker).isInstanceOf(DeclineShipmentWorkItemWorker.class);
	}

	@Test
	public void shouldReturnShipmentReallocateWorker()
	{
		final WorkItemWorker worker = this.factory.getWorkItemWorker(ACTION_SHIPMENT_REALLOCATE);
		Assertions.assertThat(worker).isInstanceOf(ReallocateShipmentWorkItemWorker.class);
	}

}
