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
package com.hybris.oms.ui.rest.client.returns;

import com.hybris.commons.client.RestCallBuilder;
import com.hybris.commons.client.RestResponse;
import com.hybris.commons.client.RestResponseException;
import com.hybris.oms.api.PageInfo;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.PagedResults;
import com.hybris.oms.domain.QueryObject;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.HybrisSystemException;
import com.hybris.oms.domain.exception.RemoteRequestException;
import com.hybris.oms.domain.returns.ReturnQueryObject;
import com.hybris.oms.rest.client.util.RestCallPopulator;
import com.hybris.oms.rest.client.util.RestUtil;
import com.hybris.oms.rest.client.web.DefaultRestClient;
import com.hybris.oms.ui.api.returns.ReturnDetail;
import com.hybris.oms.ui.api.returns.ReturnDetailFacade;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.sun.jersey.api.client.GenericType;


/**
 * The Class UiReturnRestClient.
 */
public class ReturnDetailRestClient extends DefaultRestClient implements ReturnDetailFacade
{
	private RestCallPopulator<QueryObject<?>> queryObjectRestCallPopulator;

	@Override
	public Pageable<ReturnDetail> findReturnDetailsByQuery(final ReturnQueryObject returnQueryObject)
			throws EntityNotFoundException, RemoteRequestException
	{
		try
		{
			final RestCallBuilder call = getClient().call("returndetails");
			this.queryObjectRestCallPopulator.populate(returnQueryObject, call);
			final RestResponse<List<ReturnDetail>> response = call.get(new GenericType<List<ReturnDetail>>()
			{ /* NOPMD */});

			final List<ReturnDetail> returnDetails = response.result();
			final PageInfo pageInfo = RestUtil.getHeaderPageInfo(response);
			return new PagedResults<ReturnDetail>(returnDetails, pageInfo);
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
			return null;
		}
	}

	@Override
	public ReturnDetail getReturnDetailById(final String returnId) throws EntityNotFoundException
	{
		try
		{
			final RestCallBuilder call = getClient().call("returndetails/%s", returnId);
			final RestResponse<ReturnDetail> response = call.get(ReturnDetail.class);
			return response.result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
			return null;
		}
	}

	@Override
	public ReturnDetail buildReturnDetailByOrderId(final String orderId) throws EntityNotFoundException
	{
		try
		{
			return getClient().call("/orders/%s/returnDetails/build", orderId).get(ReturnDetail.class).result();
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
