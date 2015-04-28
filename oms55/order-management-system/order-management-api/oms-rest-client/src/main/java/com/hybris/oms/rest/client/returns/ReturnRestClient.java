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
package com.hybris.oms.rest.client.returns;

import com.hybris.commons.client.RestCallBuilder;
import com.hybris.commons.client.RestResponse;
import com.hybris.commons.client.RestResponseException;
import com.hybris.oms.api.PageInfo;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.PagedResults;
import com.hybris.oms.api.returns.ReturnFacade;
import com.hybris.oms.domain.JaxbBaseList;
import com.hybris.oms.domain.QueryObject;
import com.hybris.oms.domain.exception.DuplicateEntityException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.exception.HybrisSystemException;
import com.hybris.oms.domain.exception.InvalidOperationException;
import com.hybris.oms.domain.returns.Return;
import com.hybris.oms.domain.returns.ReturnQueryObject;
import com.hybris.oms.domain.returns.ReturnReview;
import com.hybris.oms.rest.client.util.RestCallPopulator;
import com.hybris.oms.rest.client.util.RestUtil;
import com.hybris.oms.rest.client.web.DefaultRestClient;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.sun.jersey.api.client.GenericType;


/**
 * The Class Return Rest Client.
 */
public class ReturnRestClient extends DefaultRestClient implements ReturnFacade
{


	private static final Logger LOGGER = LoggerFactory.getLogger(ReturnRestClient.class);
	private static final String RETURN = "/returns";
	private static final String RETURN_REASONS = "/returnReasons";

	private RestCallPopulator<QueryObject<?>> queryObjectRestCallPopulator;

	@Override
	public Return createReturn(final Return returnDto) throws EntityValidationException, DuplicateEntityException
	{
		LOGGER.trace("createReturn");
		try
		{
			return getClient().call(RETURN).post(Return.class, returnDto).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public void createReturnReview(final ReturnReview returnReview) throws EntityValidationException, DuplicateEntityException
	{
		LOGGER.trace("createReturnReview");
		try
		{
			getClient().call("/returns/%s/reviews", returnReview.getReturnId()).post(ReturnReview.class, returnReview).result();
		}
		catch (final RestResponseException e)
		{
			LOGGER.error(e.getMessage(), e);
		}
	}

	@Override
	public Return updateReturn(final Return returnDto) throws EntityValidationException, EntityNotFoundException
	{
		LOGGER.trace("updateReturn");

		try
		{
			return getClient().call(RETURN).put(Return.class, returnDto).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Return getReturnById(final String returnId) throws EntityNotFoundException
	{
		LOGGER.trace("getReturnById with Id {}", returnId);
		try
		{
			return getClient().call(RETURN + "/%s", returnId).get(Return.class).getResult();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Return fixTaxReverseFailure(final String returnId) throws EntityNotFoundException, InvalidOperationException
	{
		LOGGER.trace("fixTaxReverseFailure with Id {}", returnId);
		try
		{
			return getClient().call(RETURN + "/%s/taxes/reverse", returnId).put(Return.class).getResult();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Return cancelReturn(final String returnId) throws EntityNotFoundException
	{
		LOGGER.trace("cancelReturn with Id {}", returnId);
		try
		{
			return getClient().call(RETURN + "/%s/cancel", returnId).put(Return.class).getResult();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Pageable<Return> findReturnsByQuery(final ReturnQueryObject returnQueryObject)
	{
		try
		{
			final RestCallBuilder call = getClient().call("returns");
			this.queryObjectRestCallPopulator.populate(returnQueryObject, call);
			final RestResponse<List<Return>> response = call.get(new GenericType<List<Return>>()
			{ /* NOPMD */
			});

			final List<Return> returns = response.result();
			final PageInfo pageInfo = RestUtil.getHeaderPageInfo(response);
			return new PagedResults<Return>(returns, pageInfo);
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

	@Override
	public Return autoRefundReturn(final String returnId) throws EntityNotFoundException, InvalidOperationException
	{
		LOGGER.trace("autoRefundReturn with Id {}", returnId);
		try
		{
			return getClient().call(RETURN + "/%s/refund/auto", returnId).put(Return.class).getResult();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Return manualRefundReturn(final String returnId) throws EntityNotFoundException, InvalidOperationException
	{
		LOGGER.trace("autoRefundReturn with Id {}", returnId);
		try
		{
			return getClient().call(RETURN + "/%s/refund/manual", returnId).put(Return.class).getResult();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public List<String> returnReasonCodes()
	{
		LOGGER.trace("returnReasonCodes");
		try
		{
			return getClient().call(RETURN_REASONS + "/codes").get(JaxbBaseList.class).getResult().getList();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}
}
