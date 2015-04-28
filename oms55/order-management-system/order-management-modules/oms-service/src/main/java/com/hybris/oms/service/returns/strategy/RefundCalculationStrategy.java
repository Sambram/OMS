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
package com.hybris.oms.service.returns.strategy;

import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.managedobjects.types.AmountVT;


/**
 * Strategy to calculate Return Refund Amounts
 */

public interface RefundCalculationStrategy
{

	/**
	 * calculates the amount to be refunded for a return.
	 * 
	 * @param theReturn
	 *           - the return to calculate refund amount for.
	 * @return the refund amount calculated for the provided return
	 * @throws EntityNotFoundException
	 */
	AmountVT calculateRefundAmount(final ReturnData theReturn) throws EntityNotFoundException;
}
