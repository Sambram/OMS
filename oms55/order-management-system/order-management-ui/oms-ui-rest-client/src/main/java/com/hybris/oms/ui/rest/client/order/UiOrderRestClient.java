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
package com.hybris.oms.ui.rest.client.order;

import java.util.List;

import com.hybris.oms.api.PageInfo;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.PagedResults;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.commons.client.RestCallBuilder;
import com.hybris.commons.client.RestResponse;
import com.hybris.commons.client.RestResponseException;
import com.hybris.oms.domain.QueryObject;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.HybrisSystemException;
import com.hybris.oms.domain.order.OrderQueryObject;
import com.hybris.oms.rest.client.util.RestCallPopulator;
import com.hybris.oms.rest.client.util.RestUtil;
import com.hybris.oms.rest.client.web.DefaultRestClient;
import com.hybris.oms.ui.api.order.UIOrder;
import com.hybris.oms.ui.api.order.UIOrderDetails;
import com.hybris.oms.ui.api.order.UiOrderFacade;
import com.sun.jersey.api.client.GenericType;


/**
 * This class provides an API for accessing the OMS Ui Orders Rest service.
 * 
 */
public class UiOrderRestClient extends DefaultRestClient implements UiOrderFacade
{

	private RestCallPopulator<QueryObject<?>> queryObjectRestCallPopulator;

	@Override
	public Pageable<UIOrder> findOrdersByQuery(final OrderQueryObject queryObject)
	{
		final RestCallBuilder call = getClient().call("uiorders");

		this.queryObjectRestCallPopulator.populate(queryObject, call);

		try
		{
			final RestResponse<List<UIOrder>> response = call.get(new GenericType<List<UIOrder>>()
			{});
			final List<UIOrder> ordersList = response.result();
			final PageInfo pageInfo = RestUtil.getHeaderPageInfo(response);
			return new PagedResults<>(ordersList, pageInfo);
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
			return null;
		}
	}

	@Override
	public UIOrder getUIOrderByOrderId(String orderId) throws EntityNotFoundException
	{
		try
		{
			return getClient().call("uiorders/%s", orderId).get(UIOrder.class).result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
			return null;
		}
	}

	@Override
	public UIOrderDetails getUIOrderDetailsByOrderId(String orderId) throws EntityNotFoundException
	{
		try
		{
			return getClient().call("uiorderdetails/%s", orderId).get(UIOrderDetails.class).result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
			return null;
		}
	}

	@Required
	public void setQueryObjectRestCallPopulator(final RestCallPopulator<QueryObject<?>> queryObjectRestCallPopulator)
	{
		this.queryObjectRestCallPopulator = queryObjectRestCallPopulator;
	}
}
