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
 * Base interface for all aggregates used by the ATS service.
 */
public interface AtsAggregate
{
	/**
	 * Returns the sku of the item.
	 */
	String getSku();

}
