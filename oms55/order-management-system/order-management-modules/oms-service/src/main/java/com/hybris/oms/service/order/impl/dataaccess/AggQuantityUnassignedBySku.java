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
package com.hybris.oms.service.order.impl.dataaccess;

import com.hybris.kernel.api.AggregateOperator;
import com.hybris.kernel.api.annotations.Aggregate;
import com.hybris.kernel.api.annotations.AggregationPojo;
import com.hybris.kernel.api.annotations.Grouped;
import com.hybris.oms.service.ats.AtsUnassignedQuantity;
import com.hybris.oms.service.managedobjects.order.OrderLineData;


@AggregationPojo(name = "AggQuantityUnassignedBySku", typeCode = OrderLineData._TYPECODE, live = true, sendEvent = true, cronExpression = "0 0 0 * * ?")
public class AggQuantityUnassignedBySku implements AtsUnassignedQuantity
{
	@Grouped(priority = 1, attribute = "skuId")
	public String sku; // NOPMD aggregation fields have to be public

	@Aggregate(attribute = "quantityUnassignedValue", operator = AggregateOperator.SUM)
	public int quantityUnassigned; // NOPMD aggregation fields have to be public

	@Override
	public String getSku()
	{
		return this.sku;
	}

	@Override
	public int getQuantityUnassigned()
	{
		return this.quantityUnassigned;
	}

}
