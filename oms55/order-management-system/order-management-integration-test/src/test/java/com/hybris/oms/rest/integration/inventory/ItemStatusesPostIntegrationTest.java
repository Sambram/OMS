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

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.domain.exception.DuplicateEntityException;
import com.hybris.oms.domain.inventory.ItemStatus;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;


/**
 * End-to-end integration tests.
 * This class should contain only methods that manipulate data (POST, PUT, DELETE).
 */
public class ItemStatusesPostIntegrationTest extends RestClientIntegrationTest
{
	private static final String DESCRIPTION = "description";

	@Autowired
	private InventoryFacade inventoryFacade;

	@Test
	public void testCreateItemStatus()
	{
		final String statusCode = this.generateRandomString();
		ItemStatus itemStatus = this.buildItemStatus(statusCode, DESCRIPTION);
		itemStatus = this.inventoryFacade.createItemStatus(itemStatus);
		Assert.assertEquals(statusCode, itemStatus.getStatusCode());
		Assert.assertEquals(DESCRIPTION, itemStatus.getDescription());
	}
	
	@Test(expected = DuplicateEntityException.class)
	public void shouldFailCreateItemStatusDuplicate()
	{
		final String statusCode = this.generateRandomString();
		final ItemStatus itemStatus = this.buildItemStatus(statusCode, DESCRIPTION);
		
		this.inventoryFacade.createItemStatus(itemStatus);
		this.inventoryFacade.createItemStatus(itemStatus);
		Assert.fail("DuplicateEntityException expected");
	}

}
