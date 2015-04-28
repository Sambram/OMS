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
package com.hybris.oms.rest.client.basestore;

import com.hybris.commons.client.RestCallBuilder;
import com.hybris.commons.client.RestResponse;
import com.hybris.commons.client.RestResponseException;
import com.hybris.oms.api.PageInfo;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.PagedResults;
import com.hybris.oms.api.basestore.BaseStoreFacade;
import com.hybris.oms.domain.QueryObject;
import com.hybris.oms.domain.basestore.BaseStore;
import com.hybris.oms.domain.basestore.BaseStoreList;
import com.hybris.oms.domain.basestore.BaseStoreQueryObject;
import com.hybris.oms.domain.exception.HybrisSystemException;
import com.hybris.oms.rest.client.util.RestCallPopulator;
import com.hybris.oms.rest.client.util.RestUtil;
import com.hybris.oms.rest.client.web.DefaultRestClient;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * The Class BaseStoreRestClient.
 */
public class BaseStoreRestClient extends DefaultRestClient implements BaseStoreFacade
{
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseStoreRestClient.class);
	private static final String BASESTORE = "/basestore";

	private RestCallPopulator<QueryObject<?>> queryObjectRestCallPopulator;

	@Override
	public BaseStore createBaseStore(final BaseStore baseStore)
	{
		LOGGER.trace("createBaseStore");
		try
		{
			return getClient().call("basestore").post(BaseStore.class, baseStore).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public BaseStore updateBaseStore(final BaseStore baseStore)
	{
		LOGGER.trace("updateBaseStore");
		try
		{
			return getClient().call("basestore").put(BaseStore.class, baseStore).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public BaseStore getBaseStoreByName(final String baseStoreName)
	{
		LOGGER.trace("getBaseStoreByName with name {}", baseStoreName);
		try
		{
			return getClient().call("basestore/%s", baseStoreName).get(BaseStore.class).getResult();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Pageable<BaseStore> findAllBaseStoresByQuery(final BaseStoreQueryObject queryObject)
	{
		LOGGER.trace("findAllBaseStoresByQuery");

		final RestCallBuilder call = getClient().call(BASESTORE);

		this.queryObjectRestCallPopulator.populate(queryObject, call);

		try
		{
			final RestResponse<?> response = call.get(BaseStoreList.class);
			final BaseStoreList baseStoreList = (BaseStoreList) response.result();
			final List<BaseStore> baseStores = baseStoreList.getBaseStores();
			final PageInfo pageInfo = RestUtil.getHeaderPageInfo(response);
			return new PagedResults<BaseStore>(baseStores, pageInfo);
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Required
	public void setQueryObjectRestCallPopulator(final RestCallPopulator<QueryObject<?>> queryObjectRestCallPopulator)
	{
		this.queryObjectRestCallPopulator = queryObjectRestCallPopulator;
	}
}
