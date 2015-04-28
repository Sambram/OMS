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
import com.hybris.oms.service.managedobjects.inventory.BinData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(value = MockitoJUnitRunner.class)
public class BinCodeValidatorTest
{

	private static final String BIN_CODE = "BIN_123";
	private static final String LOCATION_ID = "LOC_123";
	private static final String INVALID = "INVALID";

	@InjectMocks
	private final BinCodeValidator binCodeValidator = new BinCodeValidator();

	@Mock
	private InventoryService mockedInventoryService;

	@Mock
	private BinData mockedBin;

	private ValidationContext context;

	@Before
	public void prepareContext()
	{
		this.context = new ValidationContext();
	}

	@Test
	public void shouldValidate()
	{
		Mockito.when(this.mockedInventoryService.getBinByBinCodeLocationId(BIN_CODE, LOCATION_ID)).thenReturn(this.mockedBin);
		this.binCodeValidator.validate("BinCode + LocationId", this.context, new BinCodeLocationIdDTO(BIN_CODE, LOCATION_ID));
		Assert.assertFalse(this.context.containsFailures());
	}

	@Test
	public void shouldFailValidation()
	{
		Mockito.when(this.mockedInventoryService.getBinByBinCodeLocationId(BIN_CODE, INVALID)).thenThrow(
				new EntityNotFoundException("Bin not found."));
		this.binCodeValidator.validate("BinCode + LocationId", this.context, new BinCodeLocationIdDTO(BIN_CODE, INVALID));
		Assert.assertEquals(1, this.context.getFailureCount());
	}

}
