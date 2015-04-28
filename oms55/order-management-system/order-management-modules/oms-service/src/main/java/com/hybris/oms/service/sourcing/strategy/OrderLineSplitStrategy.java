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

import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.sourcing.SourcingLine;

import java.util.List;


/**
 * Strategy for splitting an order line into {@link SourcingLine}s. Allows for splitting an order line into
 * multiple sourcing lines. Each sourcing line is identified with a unique lineId.
 */
public interface OrderLineSplitStrategy
{
	/**
	 * Creates one or many {@link SourcingLine}s from the given {@link OrderLineData}.
	 * 
	 * @param orderLine orderLine
	 * @return list of {@link SourcingLine}s, never <tt>null</tt>
	 */
	List<SourcingLine> getSourcingLines(OrderLineData orderLine);

}
