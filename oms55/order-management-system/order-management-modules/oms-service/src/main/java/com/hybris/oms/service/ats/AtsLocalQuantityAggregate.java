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
 * Represents an aggregated local quantity used during ATS calculation.
 */
public interface AtsLocalQuantityAggregate extends AtsQuantityAggregate
{
	/**
	 * The locationId the quantity is aggregated for.
	 */
	String getLocationId();

}
