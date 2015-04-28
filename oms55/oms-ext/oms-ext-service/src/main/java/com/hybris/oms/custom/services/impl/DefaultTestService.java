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
package com.hybris.oms.custom.services.impl;

import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.oms.custom.services.TestService;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.order.impl.OrderQueryFactory;
import com.hybris.oms.service.service.AbstractHybrisService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 */
public class DefaultTestService extends AbstractHybrisService implements TestService
{

	private OrderQueryFactory orderQueries;

	@Autowired
	PersistenceManager pm;

	@Override
	public void print(final String msg)
	{
		System.out.println(msg);

	}

	@Override
	@Transactional
	public OrderData getMyOrder(final String orderId)
	{
		OrderData orderData = null;
		try
		{
			System.out.println("\n getMyOrder() -> Service");
			orderData = this.findOneSingle(this.orderQueries.getOrdersByIdQuery(orderId));
			System.out.println("\n EmailID   " + orderData.getEmailid());
			System.out.println("\n FirstName " + orderData.getFirstName());

		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException("Order Id is not correct, " + orderId, e);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}


	public OrderQueryFactory getOrderQueries()
	{
		return orderQueries;
	}

	public void setOrderQueries(final OrderQueryFactory orderQueries)
	{
		this.orderQueries = orderQueries;
	}

	@Override
	@Transactional
	public void insert()
	{
		System.out.println("\n insert in service()");
		// YTODO Auto-generated method stub
		// final Emp emp = pm.create(Emp.class);
		// emp.setName("Sam");
		// pm.flush();
	}


	@Override
	public void saveEmp()
	{
		System.out.println("\n saveEmp() in service");

	}

}
