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
package com.hybris.oms.service.workflow.worker.order;

import static com.hybris.oms.service.workflow.CoreWorkflowConstants.KEY_OUTCOME_NAME;
import static com.hybris.oms.service.workflow.WorkflowConstants.KEY_ORDER_ID;

import com.hybris.oms.domain.exception.RemoteRequestException;
import com.hybris.oms.domain.remote.exception.InvalidGeolocationResponseException;
import com.hybris.oms.service.cis.CisService;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.order.impl.OrderDataStaticUtils;
import com.hybris.oms.service.workflow.worker.impl.AbstractWorkItemWorker;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Geocoding Worker will perform geocoding of order shipping address is required.
 */
public class GeocodingWorkItemWorker extends AbstractWorkItemWorker
{
	private static final Logger LOG = LoggerFactory.getLogger(GeocodingWorkItemWorker.class);

	private OrderService orderService;

	private CisService cisService;

	@Override
	protected void executeInternal(final Map<String, Object> parameters)
	{
		final String orderId = this.getStringVariable(KEY_ORDER_ID, parameters);

		LOG.debug("Starting geocoding work item on order id: {}", orderId);
		boolean success = true;
		try
		{
			final OrderData order = this.orderService.getOrderByOrderId(orderId);

			/**
			 * Only bother doing the actual geocoding call if:
			 * 1. The order is not entirely BOPIS.
			 * 2. The order's shipping address contains invalid geocodes.
			 */
			if (!OrderDataStaticUtils.isCompletelyPickup(order) && !OrderDataStaticUtils.hasValidShippingAddressGeocodes(order))
			{
				order.setShippingAddress(this.cisService.geocodeAddress(order.getShippingAddress()));
			}
		}
		catch (final InvalidGeolocationResponseException | RemoteRequestException e)
		{
			LOG.warn("Error running geocoding.", e);
			success = true;
		}
		finally
		{
			parameters.put(KEY_OUTCOME_NAME, success);
			LOG.debug("Done geocoding work item on order id: {}. Outcome: {}", orderId, Boolean.toString(success));
		}
	}

	protected OrderService getOrderService()
	{
		return orderService;
	}

	protected CisService getCisService()
	{
		return cisService;
	}

	@Required
	public void setOrderService(final OrderService orderService)
	{
		this.orderService = orderService;
	}

	@Required
	public void setCisService(final CisService cisService)
	{
		this.cisService = cisService;
	}
}
