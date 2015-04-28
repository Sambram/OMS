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
package com.hybris.oms.api.basestore;

import com.hybris.oms.api.Pageable;
import com.hybris.oms.domain.basestore.BaseStore;
import com.hybris.oms.domain.basestore.BaseStoreQueryObject;
import com.hybris.oms.domain.exception.DuplicateEntityException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;


/**
 * The Interface BaseStore Api.
 */
public interface BaseStoreFacade
{

	/**
	 * Created base store.
	 * 
	 * @category DATA ONBOARDING
	 * 
	 * @param baseStore the base store to be created <dt><b>Preconditions:</b>
	 *           <dd>
	 *           BaseStore.name must not be blank.
	 *           <dd>
	 * @return created base store
	 * @throws EntityValidationException if preconditions are not met.
	 * @throws DuplicateEntityException if base store already exists.
	 */
	BaseStore createBaseStore(final BaseStore baseStore) throws EntityValidationException, DuplicateEntityException;

	/**
	 * Update base store.
	 * 
	 * @category DATA ONBOARDING
	 * 
	 * @param baseStore the base store to be updated
	 * @return updated base store
	 * @throws EntityValidationException if preconditions are not met.
	 * @throws EntityNotFoundException if baseStore doesn't exist.
	 */
	BaseStore updateBaseStore(final BaseStore baseStore) throws EntityValidationException, EntityNotFoundException;

	/**
	 * Get base store by name.
	 * 
	 * @param baseStoreName the name of the base store
	 * @return base store you are searching
	 * @throws EntityNotFoundException if baseStore doesn't exist.
	 */
	BaseStore getBaseStoreByName(final String baseStoreName) throws EntityNotFoundException;

	/**
	 * Get all base stores.
	 * 
	 * @category OMS-UI
	 * 
	 * @param queryObject {@link com.hybris.oms.domain.basestore.BaseStoreQueryObject} the conditions you are looking for
	 * @return pageable List of base stores
	 * @throws EntityValidationException if the query object preconditions are not met
	 */
	Pageable<BaseStore> findAllBaseStoresByQuery(final BaseStoreQueryObject queryObject) throws EntityValidationException;

}
