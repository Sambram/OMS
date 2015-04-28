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
 */
package com.hybris.oms.custom.services;

import com.hybris.oms.service.managedobjects.order.OrderData;


/**
 *
 */
public interface TestService
{

	void print(String msg);

	OrderData getMyOrder(String orderId);

	void insert();

	void saveEmp();
}
