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
package com.hybris.oms.rest.web.inventory;

import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.domain.inventory.ItemStatus;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class ItemStatusesTest
{
	private static final String STATUS_CODE = "statusCode";

	@InjectMocks
	private ItemStatusesResource itemStatuses;
	@InjectMocks
	private InventoryStatusResource inventoryStatusResource;
	@Mock
	private InventoryFacade inventoryServiceApi;

	private ItemStatus itemStatus;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		this.itemStatus = new ItemStatus();
		Mockito.when(this.inventoryServiceApi.createItemStatus(this.itemStatus)).thenReturn(this.itemStatus);
		Mockito.when(this.inventoryServiceApi.getItemStatusByStatusCode(STATUS_CODE)).thenReturn(this.itemStatus);
	}

	@Test
	public void testCreateItemStatus()
	{
		this.itemStatuses.createItemStatus(this.itemStatus);
	}

	@Test
	public void testFindAllItemStatuses()
	{
		this.itemStatuses.findAllItemStatuses();
	}

	@Test
	public void testGetItemStatusByStatusCode()
	{
		this.inventoryStatusResource.getItemStatusByStatusCode(STATUS_CODE);
	}

}
