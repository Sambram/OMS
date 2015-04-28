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
 * Represents an aggregated quantity used during ATS calculation.
 */
public interface AtsQuantityAggregate extends AtsAggregate
{
	/**
	 * Returns the statusCode associated with the quantity.
	 */
	String getStatusCode();

	/**
	 * Returns the aggregated quantity.
	 */
	int getQuantity();

}
