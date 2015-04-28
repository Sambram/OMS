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
package com.hybris.oms.service.sourcing.strategy;

import com.hybris.oms.service.sourcing.SourcingLine;
import com.hybris.oms.service.sourcing.SourcingOLQ;
import com.hybris.oms.service.sourcing.context.ProcessStatus;
import com.hybris.oms.service.sourcing.context.SourcingContext;



/**
 * Represents a strategy to be used to assign OLQs for all {@link com.hybris.oms.service.sourcing.SourcingLine}s.
 */
public interface SourcingStrategy
{
	/**
	 * Adds {@link SourcingOLQ}s to {@link ProcessStatus} for all {@link SourcingLine}s.
	 * 
	 * @param context the {@link SourcingContext} providing access to input, result and configuration data
	 */
	void source(SourcingContext context);

}
