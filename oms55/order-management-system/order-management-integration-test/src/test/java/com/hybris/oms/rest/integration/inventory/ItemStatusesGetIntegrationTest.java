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
package com.hybris.oms.rest.integration.inventory;

import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.inventory.ItemStatus;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * End-to-end integration tests.
 * This class should contain only methods to retrieve data (GET).
 */
public class ItemStatusesGetIntegrationTest extends RestClientIntegrationTest
{

	private static final String INVALID_STATUS_CODE = "INVALID";

	@Autowired
	private InventoryFacade inventoryFacade;

	@Test(expected = EntityNotFoundException.class)
	public void getNotFoundGetItemStatusByStatusCode() throws EntityNotFoundException
	{
		this.inventoryFacade.getItemStatusByStatusCode(INVALID_STATUS_CODE);
	}

	@Test
	public void testGetItemStatusByStatusCode() throws EntityNotFoundException
	{
		final ItemStatus itemStatus = this.inventoryFacade.getItemStatusByStatusCode(ON_HAND);
		Assert.assertEquals(ON_HAND, itemStatus.getStatusCode());
		Assert.assertEquals(ON_HAND, itemStatus.getId());
	}

}
