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
package com.hybris.oms.service.sourcing;

import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.order.OrderData;

import java.util.List;
import java.util.Set;


/**
 * Service to source the given {@link SourcingLine}s according to a given configuration.
 */
public interface SourcingService
{
	/**
	 * Retrieves the order by orderId and attempts to source any lines with unassigned quantity.
	 * 
	 * @param orderId orderId, must not be <tt>null</tt>.
	 * @return {@link OrderData} with newly created OLQs.
	 * @throws IllegalArgumentException if orderId is <tt>null</tt>
	 * @throws EntityNotFoundException if an orderLine for an OLQ cannot be, or if the order for the orderId cannot be
	 *            found
	 */
	OrderData sourceOrder(String orderId) throws EntityNotFoundException;

	/**
	 * Attempts to assign OLQs for all sourcing lines using a list of configured strategies.
	 * 
	 * @param sourcingLines the sourcing lines; if empty or <tt>null</tt>, an empty result is returned.
	 * @return {@link SourcingResult}
	 */
	SourcingResult sourceInput(final List<SourcingLine> sourcingLines);

	/**
	 * Attempts to assign OLQs for all sourcing lines using a list of configured strategies.
	 * 
	 * @param sourcingLines the sourcing lines; if empty, an empty result is returned.
	 * @param filterLocationIds optional {@link Set} of locationIds which should be used exclusively for sourcing, can be
	 *           <tt>null</tt>.
	 * @param shipToCoordinates optional shipping coordinates used in distance calculations, can be <tt>null</tt>.
	 * @return {@link SourcingResult}
	 */
	SourcingResult sourceInput(final List<SourcingLine> sourcingLines, final Set<String> filterLocationIds,
			final RadianCoordinates shipToCoordinates);

	/**
	 * Simplified attempt to simulate sourcing. Uses distance, ats if atsId is set, and priority for sorting locations.
	 * 
	 * @param sourcingLines set of lines to simulate sourcing for, cannot be <tt>null</tt> or empty.
	 * @param atsId optional ats formula ID.
	 * @param filterLocationIds an optional filter on locationIds, can be <tt>null</tt>.
	 * @param shipToCoordinates optional shipping coordinates used in distance calculations, can be <tt>null</tt>.
	 * @return {@LocationData} of the best matching location
	 */
	StockroomLocationData simulateSourcing(List<SourcingLine> sourcingLines, String atsId, final Set<String> filterLocationIds,
			final RadianCoordinates shipToCoordinates);

	/**
	 * Find all options available for splitting orders and order lines during sourcing.
	 * 
	 * @return list of String representing available splitting options
	 */
	Set<String> findAllSourcingSplitOptions();

	/**
	 * Find all options available for comparing locations during sourcing.
	 * 
	 * @return list of String representing available sourcing location comparators
	 */
	Set<String> findAllSourcingLocationComparators();
}
