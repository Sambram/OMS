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
package com.hybris.oms.service.health.agg;

import com.hybris.kernel.api.AggregateOperator;
import com.hybris.kernel.api.annotations.Aggregate;
import com.hybris.kernel.api.annotations.AggregationPojo;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;


@AggregationPojo(name = "AggStockroomLocation", typeCode = StockroomLocationData._TYPECODE, live = false, sendEvent = false, cronExpression = "0 3 0 * * ?")
public class AggStockroomLocation implements QuantityCountingAggregation
{
	@Aggregate(attribute = "id", operator = AggregateOperator.COUNT)
	public long quantity; // NOPMD aggregation fields have to be public

	@Override
	public long getQuantity()
	{
		return this.quantity;
	}
}
