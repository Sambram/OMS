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
package com.hybris.oms.service.basestore.impl;

import com.hybris.kernel.api.Page;
import com.hybris.oms.domain.basestore.BaseStoreQueryObject;
import com.hybris.oms.service.basestore.BaseStoreService;
import com.hybris.oms.service.managedobjects.basestore.BaseStoreData;
import com.hybris.oms.service.service.AbstractHybrisService;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the base store service.
 */
public class DefaultBaseStoreService extends AbstractHybrisService implements BaseStoreService
{

	private BaseStoreQueryFactory baseStoreQueryFactory;

	@Override
	public BaseStoreData findBaseStoreByName(final String baseStoreName)
	{
		return this.baseStoreQueryFactory.getBaseStoreByName(baseStoreName).optionalUniqueResult();
	}

	@Override
	public Page<BaseStoreData> findPagedBaseStoresByQuery(final BaseStoreQueryObject queryObject)
	{

		final int[] pageNumberAndSize = this.getPageNumberAndSize(queryObject, 0, this.getQueryPageSizeDefault());

		return this.findPaged(this.baseStoreQueryFactory.getAllBaseStoresQuery(), pageNumberAndSize[0], pageNumberAndSize[1]);
	}

	protected BaseStoreQueryFactory getBaseStoreQueryFactory()
	{
		return baseStoreQueryFactory;
	}

	@Required
	public void setBaseStoreQueryFactory(final BaseStoreQueryFactory baseStoreQueryFactory)
	{
		this.baseStoreQueryFactory = baseStoreQueryFactory;
	}

}
