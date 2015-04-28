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
import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.api.order.OrderFacade;
import com.hybris.oms.api.shipment.ShipmentFacade;
import com.hybris.oms.domain.ats.AtsFormula;
import com.hybris.oms.domain.ats.AtsQuantities;
import com.hybris.oms.domain.ats.AtsQuantity;
import com.hybris.oms.domain.locationrole.LocationRole;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class AtsAggregationIntegrationTest extends RestClientIntegrationTest
{
	private static final String LOCATION_ID = "locId";
	private static final String ORDER_ID = "orderId";
	private static final String ORDER_LINE_ID = "orderLineId";
	private static final int ORDER_QTY = 5;

	private static final String ATS_SKU = "SkuTest";
	private static final String ATS_ID_SOURCED = "sourced";
	private static final String ATS_ID_ALLOCATED = "allocated";
	private static final String ATS_ID_SHIPPED = "shipped";

	private static final String ATS_FORMULA_SOURCED = "O[SOURCED]";
	private static final String ATS_FORMULA_ALLOCATED = "O[ALLOCATED]";
	private static final String ATS_FORMULA_SHIPPED = "O[SHIPPED]";

	@Autowired
	private AtsFacade atsFacade;

	@Autowired
	private InventoryFacade inventoryFacade;

	@Autowired
	private OrderFacade orderFacade;

	@Autowired
	private ShipmentFacade shipmentFacade;

	private AtsFormula sourcedFormula;
	private AtsFormula allocatedFormula;
	private AtsFormula shippedFormula;

	Set<String> skus = new HashSet<String>();
	Set<String> atsIds = new HashSet<String>();


	@After
	public void deleteFormula()
	{
		this.atsFacade.deleteFormula(ATS_ID_SOURCED);
		this.atsFacade.deleteFormula(ATS_ID_ALLOCATED);
		this.atsFacade.deleteFormula(ATS_ID_SHIPPED);
	}

	@Test
	public void atsAggregationStatusTest()
	{
		skus.add(ATS_SKU);
		atsIds.add(ATS_ID_SOURCED);
		atsIds.add(ATS_ID_ALLOCATED);
		atsIds.add(ATS_ID_SHIPPED);

		sourcedFormula = this.buildFormula(ATS_ID_SOURCED, ATS_FORMULA_SOURCED);
		this.atsFacade.createFormula(sourcedFormula);
		allocatedFormula = this.buildFormula(ATS_ID_ALLOCATED, ATS_FORMULA_ALLOCATED);
		this.atsFacade.createFormula(allocatedFormula);
		shippedFormula = this.buildFormula(ATS_ID_SHIPPED, ATS_FORMULA_SHIPPED);
		this.atsFacade.createFormula(shippedFormula);

		createInventory(ATS_SKU, LOCATION_ID, LocationRole.SHIPPING);
		final Order order = buildOrder(ATS_SKU, ORDER_ID, ORDER_LINE_ID, ORDER_QTY);
		this.orderFacade.createOrder(order);

		int shipmentsCreated = 0;
		String status = "";
		while (shipmentsCreated == 0 || !status.equals("ALLOCATED"))
		{
			final Collection<Shipment> shipments = shipmentFacade.getShipmentsByOrderId(ORDER_ID);
			shipmentsCreated = shipments.size();
			if (shipmentsCreated > 0)
			{
				status = shipments.iterator().next().getOlqsStatus();
			}
		}
		final AtsQuantities atsQuantities = this.atsFacade.findGlobalAts(skus, atsIds);
		final AtsQuantity atsQtyAllocation = atsQuantities.getAtsQuantities().get(0);
		assertEquals(ORDER_QTY, atsQtyAllocation.getQuantity().getValue());

		final Collection<Shipment> shipments = shipmentFacade.getShipmentsByOrderId(ORDER_ID);
		final Iterator<Shipment> it = shipments.iterator();
		while (it.hasNext())
		{
			final Shipment shipment = it.next();
			shipmentFacade.confirmShipment(shipment.getShipmentId());
		}

		while (!status.equals("SHIPPED"))
		{
			final Collection<Shipment> confirmedShipments = shipmentFacade.getShipmentsByOrderId(ORDER_ID);
			status = confirmedShipments.iterator().next().getOlqsStatus();
		}

		final AtsQuantities atsQuantitiesShipped = this.atsFacade.findGlobalAts(skus, atsIds);
		final AtsQuantity atsQtyShipped = atsQuantitiesShipped.getAtsQuantities().get(0);
		assertEquals(ORDER_QTY, atsQtyShipped.getQuantity().getValue());
	}

	private AtsFormula buildFormula(final String atsId, final String formula)
	{
		final AtsFormula myFormula = new AtsFormula();
		myFormula.setAtsId(atsId);
		myFormula.setName("name");
		myFormula.setDescription("description");
		myFormula.setFormula(formula);
		return myFormula;
	}


}
