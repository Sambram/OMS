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

import static org.junit.Assert.assertEquals;

import com.hybris.oms.api.ats.AtsFacade;
import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.domain.ats.AtsQuantities;
import com.hybris.oms.domain.locationrole.LocationRole;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class FutureAtsAggregationIntegrationTest extends RestClientIntegrationTest
{
	private static final String LOCATION_ID = "locIdFuture";

	private static final String ATS_SKU = "FutureSkuTest";
	private static final String ATS_ID_FUTURE = "FUTURE";
	private static final String ATS_ID_FULL = "FULL";

	@Autowired
	private AtsFacade atsFacade;

	Set<String> skus = new HashSet<String>();
	Set<String> atsIds = new HashSet<String>();

	OmsInventory nextMonthInventory;
	OmsInventory nextYearInventory;
	OmsInventory currentInventory;

	@After
	public void deleteInventories()
	{
		this.deleteInventory(nextMonthInventory);
		this.deleteInventory(nextYearInventory);
		this.deleteInventory(currentInventory);
	}

	@Test
	public void futureInventoryAtsAggregationTest()
	{
		final Calendar calendar = Calendar.getInstance();
		// add one month to the date/calendar
		calendar.add(Calendar.MONTH, 1);
		final Date futureDate = calendar.getTime();
		// createInventory is creating an inventory with a quantity of 100 by default.
		nextMonthInventory = createInventory(ATS_SKU, LOCATION_ID, LocationRole.SHIPPING, null, null, futureDate);

		// Create current inventory
		currentInventory = createInventory(ATS_SKU, LOCATION_ID, LocationRole.SHIPPING, null, null, null);

		skus.add(ATS_SKU);
		atsIds.add(ATS_ID_FUTURE);

		AtsQuantities atsQuantities = this.atsFacade.findGlobalAts(skus, atsIds);
		assertEquals(1, atsQuantities.getAtsQuantities().size());
		assertEquals(100, atsQuantities.getAtsQuantities().get(0).getQuantity().getValue());

		// Create another futur inventory even further in the futur.
		calendar.add(Calendar.YEAR, 1);
		final Date furtherFutureDate = calendar.getTime();
		nextYearInventory = createInventory(ATS_SKU, LOCATION_ID, LocationRole.SHIPPING, null, null, furtherFutureDate);

		atsQuantities = this.atsFacade.findGlobalAts(skus, atsIds);
		assertEquals(1, atsQuantities.getAtsQuantities().size());
		assertEquals(200, atsQuantities.getAtsQuantities().get(0).getQuantity().getValue());


		atsIds.clear();
		atsIds.add(ATS_ID_FULL);
		atsQuantities = this.atsFacade.findGlobalAts(skus, atsIds);
		assertEquals(1, atsQuantities.getAtsQuantities().size());
		assertEquals(300, atsQuantities.getAtsQuantities().get(0).getQuantity().getValue());
	}

}
