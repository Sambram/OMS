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
package com.hybris.oms.api.returns;

import com.hybris.oms.api.Pageable;
import com.hybris.oms.domain.exception.DuplicateEntityException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.exception.InvalidOperationException;
import com.hybris.oms.domain.returns.Return;
import com.hybris.oms.domain.returns.ReturnQueryObject;
import com.hybris.oms.domain.returns.ReturnReview;

import java.util.List;


/**
 * The Interface Return Api.
 */
public interface ReturnFacade
{

	/**
	 * Creates a new Return with given parameters.
	 * 
	 * @param returnDto
	 *           the Return object values to use to create the new Return. <dt><b>Preconditions:</b>
	 *           <dd>
	 *           Return.returnId is not empty and not null
	 *           <dd>
	 *           Return.returnOrderLines is not empty and not null {@link com.hybris.oms.domain.returns.ReturnOrderLine}
	 *           <dd>
	 *           Return.returnPaymentInfo is not null {@link com.hybris.oms.domain.returns.ReturnPaymentInfo}
	 *           <dd>
	 * @return the created Return
	 * @throws EntityValidationException
	 *            - Return validation failed.
	 * @throws DuplicateEntityException
	 *            - the Return already exists.
	 */
	Return createReturn(final Return returnDto) throws EntityValidationException, DuplicateEntityException;

	/**
	 * update a Return with given parameters.
	 * 
	 * @param returnDto
	 *           the Return object values to update.
	 * @return the updated Return
	 * @throws EntityValidationException
	 *            - Return validation failed.
	 * @throws EntityNotFoundException
	 *            - the Return not found.
	 */
	Return updateReturn(final Return returnDto) throws EntityValidationException, EntityNotFoundException;

	/**
	 * Gets a specific return with a specific return id.
	 * 
	 * @param returnId
	 *           the return id to fetch
	 * @return the Return object searched
	 * @throws EntityNotFoundException
	 *            if Return was not found
	 */
	Return getReturnById(final String returnId) throws EntityNotFoundException;

	/**
	 * Cancels the Return with the given ID.
	 * 
	 * @param returnId
	 *           the return to be cancelled. <dt><b>Preconditions:</b>
	 *           <dd>
	 *           Return.ReturnId is not empty and not null
	 *           <dd>
	 * @return the cancelled return
	 * @throws EntityNotFoundException
	 *            - if Return was not found
	 * @throws com.hybris.oms.domain.exception.InvalidOperationException
	 *            - if the return is already cancelled
	 */
	Return cancelReturn(final String returnId) throws EntityNotFoundException, InvalidOperationException;

	/**
	 * Fix Tax Reverse on the Return with the given ID. <br/>
	 * Unblock and close the process of a Return which had a failure during tax reverse
	 * 
	 * @param returnId
	 *           the return to be fix. <dt><b>Preconditions:</b>
	 *           <dd>
	 *           Return.ReturnId is not empty and not null
	 *           <dd>
	 * @return the fixed return
	 * @throws EntityNotFoundException
	 *            - if Return was not found
	 * @throws com.hybris.oms.domain.exception.InvalidOperationException
	 *            - if the return is not blocked because of a tax reverse failure
	 */
	Return fixTaxReverseFailure(final String returnId) throws EntityNotFoundException, InvalidOperationException;

	/**
	 * Auto Refund the Return with the given ID. Auto Refund will trigger Payment refund and then to Tax reverse steps in
	 * the Return Workflow.
	 * 
	 * @param returnId
	 *           the return to be auto refund. <dt><b>Preconditions:</b>
	 *           <dd>
	 *           Return.ReturnId is not empty and not null
	 *           <dd>
	 * @return the return
	 * @throws EntityNotFoundException
	 *            - if Return was not found
	 * @throws com.hybris.oms.domain.exception.InvalidOperationException
	 *            - if the return is not in a supported state. its workflow does not allow it.
	 */
	Return autoRefundReturn(final String returnId) throws EntityNotFoundException, InvalidOperationException;

	/**
	 * Manual Refund the Return with the given ID. Manual Refund will skip Payment Refund step and go directly to
	 * 
	 * @param returnId
	 *           the return to be manually refund. <dt><b>Preconditions:</b>
	 *           <dd>
	 *           Return.ReturnId is not empty and not null
	 *           <dd>
	 * @return the return
	 * @throws EntityNotFoundException
	 *            - if Return was not found
	 * @throws com.hybris.oms.domain.exception.InvalidOperationException
	 *            - if the return is not in a supported state. its workflow does not allow it.
	 */
	Return manualRefundReturn(final String returnId) throws EntityNotFoundException, InvalidOperationException;

	/**
	 * Provides the list of return reason codes that can be applied to returns.
	 * 
	 * @return A list of the supported return codes as @String
	 */
	List<String> returnReasonCodes();

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
	 * @return A list of {@link com.hybris.oms.domain.returns.Return}
	 * @throws EntityValidationException - if preconditions are not met.
	 */
	Pageable<Return> findReturnsByQuery(final ReturnQueryObject returnQueryObject) throws EntityValidationException;

	/**
	 * Creates new Return Line Rejections for a specific Return with given Return Review.
	 *
	 * @param returnReview
	 * @throws EntityValidationException
	 * @throws DuplicateEntityException
	 */
	void createReturnReview(final ReturnReview returnReview) throws EntityValidationException, DuplicateEntityException;

}
