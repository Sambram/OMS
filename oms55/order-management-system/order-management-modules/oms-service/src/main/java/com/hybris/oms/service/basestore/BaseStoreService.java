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
package com.hybris.oms.service.basestore;

import com.hybris.kernel.api.Page;
import com.hybris.oms.domain.basestore.BaseStoreQueryObject;
import com.hybris.oms.service.managedobjects.basestore.BaseStoreData;
import com.hybris.oms.service.service.Flushable;


/**
 * Base Store service.
 */
public interface BaseStoreService extends Flushable
{

	/**
	 * Get a base store by base store name.
	 * 
	 * 
	 * @param baseStoreName
	 * @return base store
	 */
	BaseStoreData findBaseStoreByName(final String baseStoreName);

	/**
	 * Get the list of base stores.
	 * 
	 * @return page of Base Stores
	 */
	Page<BaseStoreData> findPagedBaseStoresByQuery(final BaseStoreQueryObject queryObject);
}
