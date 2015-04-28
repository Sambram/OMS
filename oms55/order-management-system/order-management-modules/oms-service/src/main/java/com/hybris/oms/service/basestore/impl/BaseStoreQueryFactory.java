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

import com.hybris.kernel.api.CriteriaQuery;
import com.hybris.kernel.api.Restrictions;
import com.hybris.oms.service.managedobjects.basestore.BaseStoreData;
import com.hybris.oms.service.service.AbstractQueryFactory;


public class BaseStoreQueryFactory extends AbstractQueryFactory
{
	/**
	 * Get a query for a base store by base store name.
	 * 
	 * @param baseStoreName
	 * @return query
	 */
	public CriteriaQuery<BaseStoreData> getBaseStoreByName(final String baseStoreName)
	{
		return this.query(BaseStoreData.class).where(Restrictions.eq(BaseStoreData.NAME, baseStoreName));
	}

	/**
	 * Get all base stores.
	 * 
	 * @return base stores
	 */
	public CriteriaQuery<BaseStoreData> getAllBaseStoresQuery()
	{

		return this.query(BaseStoreData.class);
	}
}
