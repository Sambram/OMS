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
package com.hybris.oms.rest.integration.ats;

import com.hybris.oms.api.ats.AtsFacade;
import com.hybris.oms.api.basestore.BaseStoreFacade;
import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.api.order.OrderFacade;
import com.hybris.oms.domain.ats.AtsFormula;
import com.hybris.oms.domain.ats.AtsLocalQuantities;
import com.hybris.oms.domain.ats.AtsQuantities;
import com.hybris.oms.domain.ats.AtsQuantity;
import com.hybris.oms.domain.basestore.BaseStore;
import com.hybris.oms.domain.exception.DuplicateEntityException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.inventory.ItemStatus;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.locationrole.LocationRole;
import com.hybris.oms.domain.order.OrderLineQuantityStatus;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.fest.assertions.Assertions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.ImmutableSet;


/**
 * Client integration test for {@link AtsFacade}.
 */
public class AtsFacadeClientIntegrationTest extends RestClientIntegrationTest
{
	private static final String ATS_SKU = "AtsTest";
	private static final String ATS_ID = "test";

	@Autowired
	private AtsFacade atsFacade;

	@Autowired
	private InventoryFacade inventoryFacade;

	@Autowired
	private BaseStoreFacade baseStoreFacade;

	@Autowired
	private OrderFacade orderFacade;

	private AtsFormula formula;

	@Before
	public void createFormula()
	{
		formula = this.buildFormula();
		this.atsFacade.createFormula(formula);
	}

	@After
	public void deleteFormula()
	{
		try
		{
			this.atsFacade.deleteFormula(ATS_ID);
		}
		catch (final EntityNotFoundException e)
		{
			// already deleted
		}
	}

	@Test
	public void shouldMaintainFormula() throws DuplicateEntityException, EntityNotFoundException
	{
		this.verifyResult(formula, this.atsFacade.getFormulaById(ATS_ID));
		final Collection<AtsFormula> formulas = this.atsFacade.findAllFormulas();
		AtsFormula testResult = null;
		for (final AtsFormula result : formulas)
		{
			if (result.getAtsId().equals(ATS_ID))
			{
				testResult = result;
				break;
			}
		}
		this.verifyResult(formula, testResult);
		formula.setName("name2");
		formula.setDescription("description2");
		formula.setFormula("I[NOT_AVAILABLE]+I[ON_HAND]");
		this.atsFacade.updateFormula(ATS_ID, formula);
		this.verifyResult(formula, this.atsFacade.getFormulaById(ATS_ID));
		this.atsFacade.deleteFormula(ATS_ID);
		try
		{
			this.atsFacade.getFormulaById(ATS_ID);
			Assert.fail("formula should be deleted");
		}
		catch (final EntityNotFoundException e)
		{
			// success
		}
	}

	@Test(expected = DuplicateEntityException.class)
	public void shouldThrowDuplicateFormula() throws DuplicateEntityException, EntityNotFoundException
	{
		this.atsFacade.createFormula(formula);
	}

	@Test(expected = EntityValidationException.class)
	public void shouldValidateFormula() throws DuplicateEntityException, EntityNotFoundException
	{
		final AtsFormula myFormula = this.buildFormula();
		myFormula.setFormula("I[INVALID]");
		this.atsFacade.createFormula(myFormula);
	}

	@Test
	public void shouldCalculateGlobalAts() throws EntityNotFoundException, DuplicateEntityException
	{
		OmsInventory inventory = null;
		try
		{
			inventory = createInventory(ATS_SKU, generateLocationId());

			final AtsQuantities result = this.atsFacade.findGlobalAts(Collections.singleton(ATS_SKU), Collections.singleton(ATS_ID));
			Assertions.assertThat(result.getAtsQuantities()).hasSize(1);
			this.verifyGlobalAts(ATS_ID, result.getAtsQuantities().get(0));
		}
		finally
		{
			this.inventoryFacade.deleteInventory(inventory);
		}
	}

	private void verifyGlobalAts(final String atsId, final AtsQuantity quantity)
	{
		Assert.assertEquals(atsId, quantity.getAtsId());
		Assert.assertEquals(ATS_SKU, quantity.getSku());
		Assert.assertEquals("units", quantity.getQuantity().getUnitCode());
		Assert.assertEquals(100, quantity.getQuantity().getValue());
	}

	@Test
	public void shouldCalculateLocalAts() throws EntityNotFoundException, DuplicateEntityException
	{
		OmsInventory inventory = null;
		try
		{
			final String locationId = generateLocationId();
			inventory = createInventory(ATS_SKU, locationId);

			final List<AtsLocalQuantities> result = this.atsFacade.findLocalAts(Collections.singleton(ATS_SKU),
					Collections.singleton(inventory.getLocationId()), Collections.singleton(ATS_ID));
			Assertions.assertThat(result).hasSize(1);
			final AtsLocalQuantities localQuantities = result.get(0);
			Assertions.assertThat(localQuantities.getAtsQuantities()).hasSize(1);
			this.verifyLocalAts(ATS_ID, localQuantities.getAtsQuantities().iterator().next());
		}
		finally
		{
			this.inventoryFacade.deleteInventory(inventory);
		}
	}

	@Test
	public void shouldCalculateAtsByBaseStore() throws EntityNotFoundException, DuplicateEntityException
	{
		OmsInventory inventory = null;
		try
		{
			// build a BaseStore
			final BaseStore baseStore = this.buildBaseStore(generateBaseStoreId());
			this.baseStoreFacade.createBaseStore(baseStore);

			// create an Inventory with a single Location linked to the baseStore
			final String locationId = generateLocationId();
			inventory = createInventory(ATS_SKU, locationId, ImmutableSet.of(baseStore.getId()));

			// build a second BaseStore
			final BaseStore otherBaseStore = this.buildBaseStore(generateBaseStoreId());
			this.baseStoreFacade.createBaseStore(otherBaseStore);

			// link the location to otherBaseStore
			final Location toUpdate = inventoryFacade.getStockRoomLocationByLocationId(locationId);
			toUpdate.getBaseStores().add(otherBaseStore.getId());
			final Location updated = inventoryFacade.updateStockRoomLocation(toUpdate);
			Assert.assertEquals(2, updated.getBaseStores().size());

			// call method under test
			final List<AtsLocalQuantities> result = this.atsFacade.findAtsByBaseStore(Collections.singleton(ATS_SKU),
					Collections.singleton(ATS_ID), baseStore.getId(), null, null);
			// though the same location linked to 2 different baseStores only one result is expected
			Assertions.assertThat(result).hasSize(1);
			final AtsLocalQuantities localQuantities = result.get(0);
			Assertions.assertThat(localQuantities.getAtsQuantities()).hasSize(1);
			Assert.assertEquals(100, result.get(0).getAtsQuantities().get(0).getQuantity().getValue());
			Assertions.assertThat(localQuantities.getLocationId()).isEqualTo(locationId);
		}
		finally
		{
			this.inventoryFacade.deleteInventory(inventory);
		}
	}

	@Test
	public void shouldCalculateAtsByBaseStoreWithLocationRole() throws EntityNotFoundException, DuplicateEntityException
	{
		OmsInventory inventory = null;
		try
		{
			// create a BaseStore
			final BaseStore baseStore = this.buildBaseStore(generateBaseStoreId());
			this.baseStoreFacade.createBaseStore(baseStore);

			// create an Inventory with a single location liked to the baseStore
			final String locationId = generateLocationId();
			inventory = createInventory(ATS_SKU, locationId, LocationRole.PICKUP, null, ImmutableSet.of(baseStore.getId()));
			final List<AtsLocalQuantities> result = this.atsFacade.findAtsByBaseStore(Collections.singleton(ATS_SKU),
					Collections.singleton(ATS_ID), baseStore.getId(), null, Collections.singleton("PICKUP"));
			// default location role for created location is PICKUP so we expect 1 results
			Assertions.assertThat(result).hasSize(1);
			Assert.assertEquals(100, result.get(0).getAtsQuantities().get(0).getQuantity().getValue());
			Assertions.assertThat(result.get(0).getLocationId()).isEqualTo(locationId);
		}
		finally
		{
			this.inventoryFacade.deleteInventory(inventory);
		}
	}

	@Test
	public void shouldCalculateAtsByBaseStoreWithInvalidLocationRole() throws EntityNotFoundException, DuplicateEntityException
	{
		OmsInventory inventory = null;
		BaseStore baseStore = null;
		try
		{
			final String locationId = generateLocationId();
			final String baseStoreId = generateBaseStoreId();
			// default location role for created location is SHIPPING
			inventory = createInventory(ATS_SKU, locationId);
			baseStore = this.buildBaseStore(baseStoreId);
			this.baseStoreFacade.createBaseStore(baseStore);
			// calling with invalid location role
			final List<AtsLocalQuantities> result = this.atsFacade.findAtsByBaseStore(Collections.singleton(ATS_SKU),
					Collections.singleton(ATS_ID), baseStoreId, null, Collections.singleton("PICKUP"));
			// we expect no results, since no locations matching given criteria exist.
			Assert.assertEquals(0, result.size());
		}
		finally
		{
			this.inventoryFacade.deleteInventory(inventory);
		}
	}

	@Test
	public void shouldNOTCalculateAtsByBaseStoreWithInvalidCountry() throws EntityNotFoundException, DuplicateEntityException
	{
		OmsInventory inventory = null;
		BaseStore baseStore = null;
		try
		{
			final String locationId = generateLocationId();
			final String baseStoreId = generateBaseStoreId();
			inventory = createInventory(ATS_SKU, locationId, LocationRole.PICKUP);
			baseStore = this.buildBaseStore(baseStoreId);
			this.baseStoreFacade.createBaseStore(baseStore);
			final List<AtsLocalQuantities> result = this.atsFacade.findAtsByBaseStore(Collections.singleton(ATS_SKU),
					Collections.singleton(ATS_ID), baseStoreId, Collections.singleton("INVALIDCOUNTRY"),
					Collections.singleton("PICKUP"));
			// we expect no results, since no locations ships to INVALIDCOUNTRY.
			Assert.assertEquals(0, result.size());
		}
		finally
		{
			this.inventoryFacade.deleteInventory(inventory);
		}
	}

	@Test
	public void shouldNOTCalculateAtsByBaseStoreWithInvalidCountryAndInvalidRole() throws EntityNotFoundException,
			DuplicateEntityException
	{
		OmsInventory inventory = null;
		BaseStore baseStore = null;
		try
		{
			final String locationId = generateLocationId();
			final String baseStoreId = generateBaseStoreId();
			inventory = createInventory(ATS_SKU, locationId, LocationRole.PICKUP);
			baseStore = this.buildBaseStore(baseStoreId);
			this.baseStoreFacade.createBaseStore(baseStore);
			final List<AtsLocalQuantities> result = this.atsFacade.findAtsByBaseStore(Collections.singleton(ATS_SKU),
					Collections.singleton(ATS_ID), baseStoreId, Collections.singleton("INVALIDCOUNTRY"),
					Collections.singleton("INVALID"));
			Assert.assertEquals(0, result.size());
		}
		finally
		{
			this.inventoryFacade.deleteInventory(inventory);
		}
	}

	private void verifyLocalAts(final String atsId, final AtsQuantity quantity)
	{
		Assert.assertEquals(atsId, quantity.getAtsId());
		Assert.assertEquals(ATS_SKU, quantity.getSku());
		Assert.assertEquals("units", quantity.getQuantity().getUnitCode());
		Assert.assertEquals(100, quantity.getQuantity().getValue());
	}

	private AtsFormula buildFormula()
	{
		final AtsFormula myFormula = new AtsFormula();
		myFormula.setAtsId(ATS_ID);
		myFormula.setName("name");
		myFormula.setDescription("description");
		myFormula.setFormula("I[ON_HAND]");
		return myFormula;
	}

	private void verifyResult(final AtsFormula myFormula, final AtsFormula result)
	{
		Assert.assertNotNull(result);
		Assert.assertEquals(myFormula.getAtsId(), result.getAtsId());
		Assert.assertEquals(myFormula.getName(), result.getName());
		Assert.assertEquals(myFormula.getDescription(), result.getDescription());
		Assert.assertEquals(myFormula.getAtsId(), result.getId());
	}



	// Verify validation when user delete no-existing formula
	@Test(expected = EntityNotFoundException.class)
	public void shouldThrowEntityNotFoundFormula()
	{
		this.atsFacade.deleteFormula(ATS_ID);
		this.atsFacade.deleteFormula(ATS_ID);
		Assert.fail("throw entityNotFoundException when delete non-existing formula");
	}

	// Verify the validation for update ATS formula content
	@Test(expected = EntityValidationException.class)
	public void shouldThrowEntityValidationInvalidDatainATSFormula()
	{
		formula.setFormula("I[NOT_AVAILABLE]+dummyData");
		this.atsFacade.updateFormula(ATS_ID, formula);
	}

	@Test(expected = EntityValidationException.class)
	public void shouldThrowEntityValidationInvalidInventoryinATSFormula()
	{
		formula.setFormula("I[dummyData]");
		this.atsFacade.updateFormula(ATS_ID, formula);
	}

	@Test(expected = EntityValidationException.class)
	public void shouldThrowEntityValidationInvalidOlqinATSFormula()
	{
		formula.setFormula("O[dummyData]");
		this.atsFacade.updateFormula(ATS_ID, formula);
	}

	// Verify the validation for empty formula name
	@Test(expected = EntityValidationException.class)
	public void shouldThrowEntityValidationFormulaName()
	{
		formula.setName("");
		this.atsFacade.updateFormula(ATS_ID, formula);
	}

	// verify the Threshold buffer (T) can be modified and deleted in the formula
	@Test
	public void shouldMaintainFormulawithTreshold()
	{
		try
		{
			formula.setFormula("I[ON_HAND]-T");
			this.atsFacade.updateFormula(ATS_ID, formula);
			this.verifyResult(formula, this.atsFacade.getFormulaById(ATS_ID));
			formula.setFormula("I[ON_HAND]");
			this.atsFacade.updateFormula(ATS_ID, formula);
			this.verifyResult(formula, this.atsFacade.getFormulaById(ATS_ID));
		}
		catch (final EntityValidationException e)
		{
			Assert.fail("treshold should be able to add and delete from the formula");
		}
	}

	// verify all the inventory and OLQ statuses can be used in the ATS formula
	@Test
	public void testAllInventoryAndOlqStatusinFormula()
	{
		try
		{
			final List<OrderLineQuantityStatus> defaultOLQStatusList = this.orderFacade.findAllOrderLineQuantityStatuses();
			final List<ItemStatus> defaultItemStatusList = this.inventoryFacade.findAllItemStatuses();
			for (final ItemStatus itemStatus : defaultItemStatusList)
			{
				for (final OrderLineQuantityStatus olqStatus : defaultOLQStatusList)
				{
					formula.setFormula("O[" + olqStatus.getStatusCode() + "]" + "+I[" + itemStatus.getStatusCode() + "]");
					this.atsFacade.updateFormula(ATS_ID, formula);
				}
			}
		}
		catch (final EntityValidationException e)
		{
			Assert.fail("all the inventory and OLQ status should be valid for formula");
		}
	}

	// verify user can create new OLQ status and applied in the ATS formula
	@Test
	public void testCreateNewOlqStatusinFormula()
	{
		// create new OLQ status
		final String statusCode = this.generateRandomString();
		OrderLineQuantityStatus olqStatus = new OrderLineQuantityStatus();
		olqStatus.setStatusCode(statusCode);
		olqStatus.setActive(true);
		olqStatus.setDescription("Description");
		olqStatus = this.orderFacade.createOrderLineQuantityStatus(olqStatus);
		Assert.assertEquals(statusCode, olqStatus.getStatusCode());
		Assert.assertTrue(olqStatus.getActive());
		try
		{
			// create new formula and using new OLQ status
			formula.setFormula("O[" + statusCode + "]");
			this.atsFacade.updateFormula(ATS_ID, formula);
		}
		catch (final EntityValidationException e)
		{
			Assert.fail("the new OLQ status should be valid for formula");
		}
	}

	// verify user can create new inventory status and applied in the ATS formula
	@Test
	public void testCreateNewInventoryStatusinFormula()
	{
		// create new inventory status
		final String statusCode = this.generateRandomString();
		ItemStatus itemStatus = this.buildItemStatus(statusCode, "DESCRIPTION");
		itemStatus = this.inventoryFacade.createItemStatus(itemStatus);
		Assert.assertEquals(statusCode, itemStatus.getStatusCode());
		Assert.assertEquals("DESCRIPTION", itemStatus.getDescription());
		try
		{
			// create new formula and using new inventory status
			formula.setFormula("I[" + statusCode + "]");
			this.atsFacade.updateFormula(ATS_ID, formula);
		}
		catch (final EntityValidationException e)
		{
			Assert.fail("the new inventory status should be valid for formula");
		}
	}

	// Verify ATS calculation result with "+" in the formula
	@Test
	public void testPlusFunctioninFormula()
	{
		// create inventory with On_HAND quantity 5
		final String locationId = this.generateLocationId();
		final String skuId = this.generateSku();
		final OmsInventory inventory = this.buildInventory(skuId, locationId, ON_HAND, null, 5);
		final Location location = this.buildLocation(locationId);
		this.inventoryFacade.createStockRoomLocation(location);
		final OmsInventory createdInventory = this.inventoryFacade.createUpdateInventory(inventory);
		try
		{
			// Check ATS
			final List<AtsLocalQuantities> result = this.atsFacade.findLocalAts(Collections.singleton(skuId),
					Collections.singleton(inventory.getLocationId()), Collections.singleton(ATS_ID));
			Assert.assertEquals(5, result.get(0).getAtsQuantities().get(0).getQuantity().getValue());
			// update formula using +
			formula.setFormula("I[ON_HAND]+I[ON_HAND]");
			this.atsFacade.updateFormula(ATS_ID, formula);
			// check ATS again
			final List<AtsLocalQuantities> result1 = this.atsFacade.findLocalAts(Collections.singleton(skuId),
					Collections.singleton(inventory.getLocationId()), Collections.singleton(ATS_ID));
			Assert.assertEquals(10, result1.get(0).getAtsQuantities().get(0).getQuantity().getValue());
		}
		finally
		{
			this.inventoryFacade.deleteInventory(createdInventory);
		}
	}

	// Verify local & global ATS calculation result with "-" in the formula
	@Test
	public void testMinusFunctioninFormula()
	{
		// create inventory with On_HAND quantity 5
		final String locationId = this.generateLocationId();
		final String skuId = this.generateSku();
		final OmsInventory inventory = this.buildInventory(skuId, locationId, ON_HAND, null, 5);
		final Location location = this.buildLocation(locationId);
		this.inventoryFacade.createStockRoomLocation(location);
		final OmsInventory createdInventory = this.inventoryFacade.createUpdateInventory(inventory);
		delay(1000);

		try
		{
			// Check ATS
			final List<AtsLocalQuantities> result = this.atsFacade.findLocalAts(Collections.singleton(skuId),
					Collections.singleton(inventory.getLocationId()), Collections.singleton(ATS_ID));
			Assert.assertEquals(5, result.get(0).getAtsQuantities().get(0).getQuantity().getValue());
			// update formula using -
			formula.setFormula("I[ON_HAND]-I[ON_HAND]-I[ON_HAND]");
			this.atsFacade.updateFormula(ATS_ID, formula);
			// check ATS again
			final List<AtsLocalQuantities> result1 = this.atsFacade.findLocalAts(Collections.singleton(skuId),
					Collections.singleton(inventory.getLocationId()), Collections.singleton(ATS_ID));
			Assert.assertEquals(-5, result1.get(0).getAtsQuantities().get(0).getQuantity().getValue());
		}
		finally
		{
			this.inventoryFacade.deleteInventory(createdInventory);
		}
	}
}
