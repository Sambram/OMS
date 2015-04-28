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
package com.hybris.oms.service.ats;

/**
 * Represents the aggregated unassigned quantity for a SKU used during ATS calculation.
 */
public interface AtsUnassignedQuantity extends AtsAggregate
{

	/**
	 * Returns the unassigned quantity.
	 */
	int getQuantityUnassigned();

}
