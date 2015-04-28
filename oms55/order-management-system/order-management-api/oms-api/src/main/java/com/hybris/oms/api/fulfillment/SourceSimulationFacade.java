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
package com.hybris.oms.api.fulfillment;

import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.order.jaxb.SourceSimulationParameter;


/**
 * Facade to proceed the Sourcing Simulation.
 */
public interface SourceSimulationFacade
{

	/**
	 * Following Sourcing Parameters, Retrieves the best location for the sourcing.
	 * 
	 * @category PLATFORM EXTENSION - omsorders
	 * 
	 * @param sourceSimulationParameter
	 *           which contains skuQuantityList, atsId, address and a list of Locations <dt><b>Precondition:</b>
	 *           <dd>
	 *           sourceSimulationParameter must not be null.
	 * 
	 * @return the Location where the sourcing should be done.
	 * 
	 * @throws IllegalArgumentException if preconditions are not met.
	 * @throws EntityNotFoundException if location was not found
	 * 
	 */
	Location findBestSourcingLocation(SourceSimulationParameter sourceSimulationParameter) throws IllegalArgumentException,
			EntityNotFoundException;
}
