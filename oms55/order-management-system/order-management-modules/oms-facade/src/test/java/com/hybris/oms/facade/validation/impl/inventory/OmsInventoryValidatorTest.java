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

import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.Validator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.anyString;


@RunWith(MockitoJUnitRunner.class)
public class OmsInventoryValidatorTest
{
	private static final String LOCATION_ID_VALID = "123";
	private static final String ITEM_STATUS_CODE_VALID = "ON_HAND";
	private static final String BIN_CODE = "1";

	@InjectMocks
	private final OmsInventoryValidator omsInventoryValidator = new OmsInventoryValidator();

	@Mock
	private Validator<String> itemStatusCodeValidator;

	@Mock
	private Validator<String> locationIdValidator;

	@Mock
	private Validator<BinCodeLocationIdDTO> binCodeValidator;

	private ValidationContext context;
	private OmsInventory omsInventory;

	@Before
	public void setUp()
	{
		context = new ValidationContext();
		omsInventory = this.buildOmsInventory();
	}

	@Test
	public void shouldValidateWithBin()
	{
		omsInventoryValidator.validate("OmsInventory", context, omsInventory);

		assertFalse(context.containsFailures());

		verify(itemStatusCodeValidator).validate("OmsInventory.itemStatusCode", context, omsInventory.getStatus());
		verify(locationIdValidator).validate("OmsInventory.locationId", context, omsInventory.getLocationId());
		verify(binCodeValidator).validate(eq("OmsInventory.binCode + locationId"), eq(context), any(BinCodeLocationIdDTO.class));
	}

	@Test
	public void shouldValidateWithBinAndQuantity()
	{
		omsInventory.setQuantity(1);
		omsInventoryValidator.validate("OmsInventory", context, omsInventory);

		assertFalse(context.containsFailures());
	}

	@Test
	public void shouldValidateWithNullBin()
	{
		omsInventory.setBinCode(null);
		omsInventoryValidator.validate("OmsInventory", context, omsInventory);

		assertFalse(context.containsFailures());

		verify(itemStatusCodeValidator).validate("OmsInventory.itemStatusCode", context, omsInventory.getStatus());
		verify(locationIdValidator).validate("OmsInventory.locationId", context, omsInventory.getLocationId());
		verify(binCodeValidator, never()).validate(anyString(), any(ValidationContext.class),any(BinCodeLocationIdDTO.class));
	}

	@Test
	public void shouldFailValidationBlanks()
	{
		omsInventory.setSkuId("");
		omsInventoryValidator.validate("OmsInventory", context, omsInventory);

		assertEquals(1, context.getFailureCount());
	}

	private OmsInventory buildOmsInventory()
	{
		final OmsInventory inventory = new OmsInventory();
		inventory.setSkuId("SKU");
		inventory.setLocationId(LOCATION_ID_VALID);
		inventory.setStatus(ITEM_STATUS_CODE_VALID);
		inventory.setQuantity(0);
		inventory.setBinCode(BIN_CODE);
		return inventory;
	}

}
