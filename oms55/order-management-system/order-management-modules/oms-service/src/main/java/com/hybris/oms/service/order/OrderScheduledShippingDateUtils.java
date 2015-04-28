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
package com.hybris.oms.service.order;

import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.service.preference.TenantPreferenceService;
import com.hybris.oms.service.preference.impl.DefaultTenantPreferenceService;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Required;


/**
 * Calculates ScheduledShippingDate based on Tenant Preference, if the original scheduledShippingDate in order is null
 */
public final class OrderScheduledShippingDateUtils
{
	private TenantPreferenceService tenantPreferenceService;

	public Date populateScheduledShippingDate(final Order order)
	{
		if (order.getScheduledShippingDate() != null)
		{
			return order.getScheduledShippingDate();
		}
		else
		{
			final Calendar cal = Calendar.getInstance();
			cal.setTime(order.getIssueDate());
			final int shipInDays = Integer.parseInt(tenantPreferenceService.getTenantPreferenceByKey(
					TenantPreferenceConstants.PREF_KEY_SHIPPING_WITHIN_DAYS).getValue());
			cal.add(Calendar.DAY_OF_YEAR, shipInDays);
			return cal.getTime();
		}
	}

	@Required
	public void setTenantPreferenceService(final DefaultTenantPreferenceService tenantPreferenceService)
	{
		this.tenantPreferenceService = tenantPreferenceService;
	}

}
