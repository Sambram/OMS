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
package com.hybris.oms.facade.validation.impl.basestore;

import com.hybris.oms.domain.basestore.BaseStore;
import com.hybris.oms.facade.validation.ValidationContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class BaseStoreValidatorTest
{

	private final BaseStoreValidator baseStoreValidator = new BaseStoreValidator();

	private ValidationContext context;
	private BaseStore baseStore;

	@Before
	public void setUp()
	{
		this.context = new ValidationContext();
		this.baseStore = buildBaseStore();
	}

	@Test
	public void shouldValidate()
	{
		this.baseStoreValidator.validate("BaseStore", context, this.baseStore);
		Assert.assertFalse(this.context.containsFailures());
	}

	@Test
	public void shouldFailValidationBlanks()
	{
		this.baseStore.setName(" ");
		this.baseStoreValidator.validate("BaseStore", context, this.baseStore);
		Assert.assertEquals(1, context.getFailureCount());
	}

	private BaseStore buildBaseStore()
	{
		final BaseStore result = new BaseStore();
		result.setName("baseStoreName");
		return result;
	}
}
