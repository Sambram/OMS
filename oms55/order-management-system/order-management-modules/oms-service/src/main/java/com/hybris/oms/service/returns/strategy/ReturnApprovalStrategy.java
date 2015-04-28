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

import com.hybris.oms.service.managedobjects.returns.ReturnData;


/**
 * Strategy for determining whether a return requires manual approval or not.
 */
public interface ReturnApprovalStrategy
{

	/**
	 * Checks if the given return requires manual approval before it can proceed.
	 * 
	 * @param theReturn - the return that might require approval
	 * @return <tt>true</tt> if the return requires approval, <tt>false</tt> otherwise
	 */
	boolean requiresApproval(final ReturnData theReturn);
}
