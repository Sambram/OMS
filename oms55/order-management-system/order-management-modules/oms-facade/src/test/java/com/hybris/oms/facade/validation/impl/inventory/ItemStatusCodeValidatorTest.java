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
package com.hybris.oms.facade.validation.impl.inventory;

import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.managedobjects.inventory.ItemStatusData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(value = MockitoJUnitRunner.class)
public class ItemStatusCodeValidatorTest
{

	private static final String ITEM_STATUS_CODE = "ON_HAND";
	private static final String INVALID = "INVALID";

	@InjectMocks
	private final ItemStatusCodeValidator itemStatusCodeValidator = new ItemStatusCodeValidator();

	@Mock
	private InventoryService mockedInventoryService;

	@Mock
	private ItemStatusData mockedItemStatus;

	private ValidationContext context;

	@Before
	public void prepareContext()
	{
		this.context = new ValidationContext();
	}

	@Test
	public void shouldValidate()
	{
		Mockito.when(this.mockedInventoryService.getItemStatusByStatusCode(ITEM_STATUS_CODE)).thenReturn(this.mockedItemStatus);
		this.itemStatusCodeValidator.validate("ItemStatusCode", this.context, ITEM_STATUS_CODE);
		Assert.assertFalse(this.context.containsFailures());
	}

	@Test
	public void shouldFailValidation()
	{
		Mockito.when(this.mockedInventoryService.getItemStatusByStatusCode(INVALID)).thenThrow(
				new EntityNotFoundException("Item status not found."));
		this.itemStatusCodeValidator.validate("ItemStatusCode", this.context, INVALID);
		Assert.assertEquals(1, this.context.getFailureCount());
	}

}
