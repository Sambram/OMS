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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.managedobjects.returns.ReturnDataPojo;
import com.hybris.oms.service.managedobjects.returns.ReturnShipmentDataPojo;

import org.junit.Test;


public class InStoreVersusOnlineStrategyTest
{
	private final InStoreVersusOnlineStrategy strategy = new InStoreVersusOnlineStrategy();

	@Test
	public void shouldRequireApproval()
	{
		final ReturnShipmentDataPojo returnShipment = new ReturnShipmentDataPojo();
		final ReturnData theReturn = new ReturnDataPojo();
		theReturn.setReturnShipment(returnShipment);

		final boolean result = strategy.requiresApproval(theReturn);
		assertTrue(result);
	}

	@Test
	public void shouldNotRequireApproval()
	{
		final ReturnData theReturn = new ReturnDataPojo();

		final boolean result = strategy.requiresApproval(theReturn);
		assertFalse(result);
	}
}
