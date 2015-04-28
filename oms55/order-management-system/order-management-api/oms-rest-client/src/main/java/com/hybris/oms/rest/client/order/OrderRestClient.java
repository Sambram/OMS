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
package com.hybris.oms.rest.client.order;

import com.hybris.commons.client.RestResponseException;
import com.hybris.oms.api.order.OrderFacade;
import com.hybris.oms.domain.adapter.DateAdaptor;
import com.hybris.oms.domain.exception.HybrisSystemException;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.OrderLineQuantityStatus;
import com.hybris.oms.domain.order.UpdatedSinceList;
import com.hybris.oms.domain.order.jaxb.OrderLineQuantityStatusesList;
import com.hybris.oms.rest.client.web.DefaultRestClient;

import java.util.Date;
import java.util.List;


/**
 * This class provides an API for accessing the OMS Orders Rest service.
 * 
 */
public class OrderRestClient extends DefaultRestClient implements OrderFacade
{

	@Override
	public Order createOrder(final Order order)
	{
		try
		{
			return getClient().call("orders").post(Order.class, order).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public void fulfill(final String orderId)
	{
		try
		{
			getClient().call("orders/%s/fulfill", orderId).post().result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public void cancelUnfulfilled(final String orderId)
	{
		try
		{
			getClient().call("orders/%s/cancel/unfulfilled", orderId).post().result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Order cancelOrder(final String orderId)
	{
		try
		{
			return getClient().call("orders/%s/cancel", orderId).delete(Order.class).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}


	@Override
	public OrderLineQuantityStatus createOrderLineQuantityStatus(final OrderLineQuantityStatus status)
	{
		try
		{
			return getClient().call("statuses/orders").post(OrderLineQuantityStatus.class, status).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}


	@Override
	public List<OrderLineQuantityStatus> findAllOrderLineQuantityStatuses()
	{
		try
		{
			return getClient().call("statuses/orders").get(OrderLineQuantityStatusesList.class).result().getList();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Order getOrderByOrderId(final String orderId)
	{
		try
		{
			return getClient().call("orders/%s", orderId).get(Order.class).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public OrderLineQuantityStatus updateOrderLineQuantityStatus(final OrderLineQuantityStatus status)
	{
		try
		{
			return getClient().call("statuses/orders").put(OrderLineQuantityStatus.class, status).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public UpdatedSinceList<String> getOrderIdsUpdatedAfter(final Date aDate)
	{
		try
		{
			return getClient().call("ordersIds/").queryParam("updatedSince", new DateAdaptor().marshal(aDate))
					.get(UpdatedSinceList.class).getResult();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

}
