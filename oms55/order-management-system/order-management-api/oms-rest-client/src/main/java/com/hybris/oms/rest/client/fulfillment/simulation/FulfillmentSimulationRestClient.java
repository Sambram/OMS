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
package com.hybris.oms.rest.client.fulfillment.simulation;

import com.hybris.commons.client.RestResponseException;
import com.hybris.oms.api.fulfillment.SourceSimulationFacade;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.HybrisSystemException;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.order.jaxb.SourceSimulationParameter;
import com.hybris.oms.rest.client.web.DefaultRestClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class OmsSourceSimulationHybrisWS.
 */
public class FulfillmentSimulationRestClient extends DefaultRestClient implements SourceSimulationFacade
{

	private static final Logger LOGGER = LoggerFactory.getLogger(FulfillmentSimulationRestClient.class);

	@Override
	public Location findBestSourcingLocation(final SourceSimulationParameter sourceSimulationParameter)
			throws EntityNotFoundException
	{
		LOGGER.debug("Source simulation");

		try
		{
			return getClient().call("source").post(Location.class, sourceSimulationParameter).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

}
