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
package com.hybris.oms.rest.integration.export;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.hybris.oms.api.ats.AtsFacade;
import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.domain.ats.AtsQuantities;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.export.api.ExportFacade;
import com.hybris.oms.export.api.ats.ATSExportFacade;
import com.hybris.oms.export.api.ats.AvailabilityToSellDTO;
import com.hybris.oms.export.api.ats.AvailabilityToSellQuantityDTO;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/*
 * Check what is wrong with these tests which is failing randomly !!!
 * Where we have "assertTrue(xxxxx.size() > 0)" should be fix to get the right value
 */
@Ignore
public class AtsExportIntegrationTest extends AbstractExportIntegrationTestSupport<AvailabilityToSellDTO>
{
	private static final String ATS_FORMULA = "ON_HAND";
	private static final int RETRIES = 10;

	@Autowired
	private ATSExportFacade atsExportClient;

	@Autowired
	private AtsFacade atsFacade;

	@Autowired
	private InventoryFacade inventoryFacade;

	private OmsInventory inventory;

	private OmsInventory inventory2;

	@Override
	public ExportFacade<AvailabilityToSellDTO> getClient()
	{
		return atsExportClient;
	}

	@Before
	public void setUp()
	{
		final String locationId = generateLocationId();
		final String sku = generateSku();
		final String sku2 = generateSku();
		inventory = buildInventory(sku, locationId, ON_HAND, null, 5);
		inventory2 = buildInventory(sku2, locationId, ON_HAND, null, 5);
		final Location location = buildLocation(locationId);
		inventoryFacade.createStockRoomLocation(location);
		inventoryFacade.createInventory(inventory);
		inventoryFacade.createInventory(inventory2);
		for (int i = 0; i < RETRIES; i++)
		{
			final AtsQuantities result = atsFacade.findGlobalAts(Collections.singleton(sku), Collections.singleton(ATS_FORMULA));
			if (!result.getAtsQuantities().isEmpty())
			{
				break;
			}
			delay();
		}

		clearQueue();
	}

	@After
	public void tearDown()
	{
		inventoryFacade.deleteInventory(inventory);
		inventoryFacade.deleteInventory(inventory2);
	}

	@Test
	public void testExportSingle()
	{
		atsExportClient.triggerExport("blahblahblah", "blahblah");
		final AvailabilityToSellDTO result = atsExportClient.pollChanges(500, NOW, ATS_FORMULA);
		assertTrue(result.getQuantities().size() > 0);
		final AvailabilityToSellQuantityDTO ats = result.getQuantities().iterator().next();
		assertNotNull("ATS not null.", ats);
		assertEquals(0, ats.getQuantity());
	}

	@Test
	public void testExport()
	{
		atsExportClient.triggerExport(inventory.getSkuId(), inventory.getLocationId());
		AvailabilityToSellDTO results = atsExportClient.pollChanges(1, NOW, ATS_FORMULA);
		assertTrue(results.getQuantities().size() > 0);

		final AvailabilityToSellQuantityDTO result = results.getQuantities().iterator().next();
		assertNotNull(result);
		assertFalse(results.getQuantities().size() == 0);
		assertQuantities(results.getQuantities());

		results = atsExportClient.pollChanges(-1, NOW, ATS_FORMULA);
		assertTrue(results.getLatestChange() == null);
	}

	@Test
	public void testFullExport()
	{
		final int nbAtsToExport = atsExportClient.triggerFullExport();
		assertTrue("At least 2 changes put to queue.", nbAtsToExport >= 2);

		// poll for every changes. Do many call in case one is not enough
		AvailabilityToSellDTO atsChanges = atsExportClient.pollChanges(nbAtsToExport, NOW, ATS_FORMULA);
		while (atsChanges.getQuantities().size() < nbAtsToExport)
		{
			atsChanges = atsExportClient.pollChanges(-1, NOW, ATS_FORMULA);
		}

		assertTrue("At least 2 changes polled.", atsChanges.getQuantities().size() >= 2);

		assertTrue("sku: " + inventory.getSkuId() + " should be exported", checkSkuIdInAtsChanges(inventory.getSkuId(), atsChanges));
		assertTrue("sku: " + inventory2.getSkuId() + " should be exported",
				checkSkuIdInAtsChanges(inventory2.getSkuId(), atsChanges));

		atsExportClient.unmarkSkuForExport(new Date().getTime());

		atsChanges = atsExportClient.pollChanges(-1, NOW, ATS_FORMULA);
		assertEquals("No more changes available.", 0, atsChanges.getQuantities().size());
	}

	private boolean checkSkuIdInAtsChanges(final String skuId, final AvailabilityToSellDTO atsChanges)
	{
		for (final AvailabilityToSellQuantityDTO atsChange : atsChanges.getQuantities())
		{
			if (skuId.equals(atsChange.getSkuId()))
			{
				return true;
			}
		}

		return false;

	}

	private void assertQuantities(final Collection<AvailabilityToSellQuantityDTO> quantities)
	{
		boolean foundGlobal = false;
		boolean foundLocal = false;
		for (final AvailabilityToSellQuantityDTO qty : quantities)
		{
			foundGlobal |= (qty.getLocationId() == null);
			foundLocal |= (qty.getLocationId() != null);
			assertEquals("Quantity is 5", 5, qty.getQuantity());
		}
		assertTrue(foundGlobal);
		assertTrue(foundLocal);
	}
}
