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
package com.hybris.oms.ui.api.returns;

import com.hybris.oms.api.Pageable;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.returns.Return;
import com.hybris.oms.domain.returns.ReturnQueryObject;


public interface ReturnDetailFacade
{

	/**
	 * Gets the return by id.
	 * 
	 * @category OMS-UI
	 * 
	 * @param returnId the return id to fetch
	 * @return the {@link Return} by id
	 * @throws EntityNotFoundException - if the return id does not exist
	 */
	ReturnDetail getReturnDetailById(final String returnId) throws EntityNotFoundException;

	/**
	 * Searches for returns regarding the criteria {@link ReturnQueryObject} informed.
	 * available features.
	 * 
	 * @category OMS-UI
	 * 
	 * @param returnQueryObject - The return query object.
	 * 
	 *           <dt><b>Preconditions:</b>
	 *           <dd>
	 *           returnQueryObject.pageNumber must not be null.
	 *           <dd>
	 *           returnQueryObject.pageNumber must not be less than zero.
	 *           <dd>
	 *           returnQueryObject.pageSize must not be null.
	 *           <dd>
	 *           returnQueryObject.pageSize must not be greater than zero.
	 *           <dd>
	 *           returnQueryObject.pageSize must not be greater than max allowed page size.
	 * @return A list of ReturnDetail
	 * @throws EntityValidationException - if preconditions are not met.
	 */
	Pageable<ReturnDetail> findReturnDetailsByQuery(ReturnQueryObject returnQueryObject) throws EntityValidationException;

	/**
	 * Build a new ReturnDetail object by linked order Id.
	 * 
	 * @category OMS-UI
	 * 
	 * @param orderId
	 *           the order id which we build the returnDetail object.
	 * @return the {@link ReturnDetail}
	 * @throws EntityNotFoundException
	 *            - if the return id does not exist
	 */
	ReturnDetail buildReturnDetailByOrderId(final String orderId) throws EntityNotFoundException;

}
