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
package com.hybris.oms.service.returns.strategy.impl;

import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.returns.strategy.ReturnApprovalStrategy;


/**
 * If the return is in store, then it requires manual approval. If the return is online, then it does not require manual
 * approval.
 * 
 * <p>
 * To determine if a return is in store versus online, we examine the {@link ReturnData#RETURNSHIPMENT}. If this is not
 * null, then the return is in store. If this is not null, then this return is online.
 * </p>
 */
public class InStoreVersusOnlineStrategy implements ReturnApprovalStrategy
{

	@Override
	public boolean requiresApproval(final ReturnData theReturn)
	{
		return theReturn.getReturnShipment() != null;
	}

}
