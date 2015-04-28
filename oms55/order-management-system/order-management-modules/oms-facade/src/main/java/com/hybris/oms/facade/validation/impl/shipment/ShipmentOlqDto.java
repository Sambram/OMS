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
package com.hybris.oms.facade.validation.impl.shipment;

import com.hybris.oms.domain.shipping.Shipment;

import java.util.Set;


/**
 * Dto used when validating that olqs belong to a shipment.
 */
public class ShipmentOlqDto
{
	private final Shipment shipment;
	private final Set<String> olqIds;

	public ShipmentOlqDto(final Shipment shipment, final Set<String> olqIds)
	{
		this.shipment = shipment;
		this.olqIds = olqIds;
	}

	public Shipment getShipment()
	{
		return shipment;
	}

	public Set<String> getOlqIds()
	{
		return olqIds;
	}
}
