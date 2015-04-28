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
package com.hybris.oms.facade.validation.impl.ats;

import com.hybris.oms.facade.ats.AtsRequest;
import com.hybris.oms.facade.validation.ValidationContext;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;


public class AtsRequestValidatorTest
{
	private final AtsRequestValidator atsRequestValidator = new AtsRequestValidator();
	private ValidationContext context;
	private AtsRequest atsRequest;

	@Before
	public void setUp()
	{
		this.context = new ValidationContext();
		this.atsRequest = this.buildAtsRequest();
	}

	@Test
	public void shouldValidate()
	{
		this.atsRequestValidator.validate("AtsRequest", this.context, this.atsRequest);
		Assert.assertFalse(this.context.containsFailures());
	}

	@Test
	public void shouldFailWithNullCollection()
	{
		this.atsRequest.setSkus(null);
		this.atsRequestValidator.validate("AtsRequest", this.context, this.atsRequest);
		Assert.assertEquals(2, this.context.getFailureCount());
	}

	@Test
	public void shouldFailWithEmptyCollection()
	{
		this.atsRequest.setSkus(new HashSet<String>());
		this.atsRequestValidator.validate("AtsRequest", this.context, this.atsRequest);
		Assert.assertEquals(1, this.context.getFailureCount());
	}

	@Test
	public void shouldFailWith1EmptySku()
	{
		final Set<String> skus = Sets.newHashSet("");
		this.atsRequest.setSkus(skus);
		this.atsRequestValidator.validate("AtsRequest", this.context, this.atsRequest);
		Assert.assertEquals(1, this.context.getFailureCount());
	}

	/**
	 * Build a fully valid ats request.
	 * 
	 * @return valid {@link AtsRequest} object
	 */
	private AtsRequest buildAtsRequest()
	{
		final AtsRequest object = new AtsRequest();
		object.setSkus(Sets.newHashSet("SKU123"));
		return object;
	}
}
