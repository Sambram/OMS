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
import com.hybris.kernel.api.annotations.Grouped;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;


@AggregationPojo(name = "AggOrderLineQuantityStatus", typeCode = OrderLineQuantityData._TYPECODE, live = false, sendEvent = false, cronExpression = "0 2 0 * * ?")
public class AggOrderLineQuantityStatus implements StatusQuantityCountingAggregation
{
	@Grouped(priority = 1, attribute = "status.statusCode")
	public String statusCode; // NOPMD aggregation fields have to be public

	@Aggregate(attribute = "id", operator = AggregateOperator.COUNT)
	public long quantity; // NOPMD aggregation fields have to be public

	@Override
	public String getStatusCode()
	{
		return this.statusCode;
	}

	@Override
	public long getQuantity()
	{
		return this.quantity;
	}
}
