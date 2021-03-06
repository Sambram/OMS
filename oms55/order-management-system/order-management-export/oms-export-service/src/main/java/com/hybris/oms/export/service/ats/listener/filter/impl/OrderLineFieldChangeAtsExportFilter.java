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
package com.hybris.oms.export.service.ats.listener.filter.impl;

import com.hybris.oms.export.service.ats.listener.filter.AbstractFieldChangeAtsExportFilter;
import com.hybris.oms.export.service.ats.listener.filter.AtsExportFilter;
import com.hybris.oms.service.managedobjects.order.OrderLineData;

import java.util.HashSet;
import java.util.Set;


/**
 * {@link AtsExportFilter} that will use allow to export if one of the following fields were changed:
 * <ul>
 * <li>{@link OrderLineData#QUANTITYUNASSIGNEDVALUE}</li>
 * </ul>
 */
public class OrderLineFieldChangeAtsExportFilter extends AbstractFieldChangeAtsExportFilter
{
	@Override
	public Set<String> getExportFields()
	{
		final Set<String> fields = new HashSet<>();
		fields.add(OrderLineData.QUANTITYUNASSIGNEDVALUE.name());
		return fields;
	}
}
