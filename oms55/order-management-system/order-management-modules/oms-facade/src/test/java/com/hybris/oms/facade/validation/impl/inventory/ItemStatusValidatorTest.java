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

import com.hybris.oms.domain.inventory.ItemStatus;
import com.hybris.oms.facade.validation.ValidationContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class ItemStatusValidatorTest
{
	private static final String ITEM_STATUS = "ON_HAND";
	private static final String DESCRIPTION = "DESCRIPTION";

	private final ItemStatusValidator itemStatusValidator = new ItemStatusValidator();
	private ValidationContext context;

	@Before
	public void prepareContext()
	{
		this.context = new ValidationContext();
	}

	@Test
	public void shouldValidate()
	{
		this.itemStatusValidator.validate("ItemStatus", this.context, buildItemStatus());
		Assert.assertFalse(this.context.containsFailures());
	}

	@Test
	public void shouldFailBlanks()
	{
		final ItemStatus itemStatus = buildItemStatus();
		itemStatus.setStatusCode(null);
		itemStatus.setDescription("  ");
		this.itemStatusValidator.validate("ItemStatus", this.context, itemStatus);
		Assert.assertEquals(2, this.context.getFailureCount());
	}

	/**
	 * Build a fully valid {@link ItemStatus}.
	 */
	private ItemStatus buildItemStatus()
	{
		final ItemStatus itemStatus = new ItemStatus();
		itemStatus.setStatusCode(ITEM_STATUS);
		itemStatus.setDescription(DESCRIPTION);
		return itemStatus;
	}
}
