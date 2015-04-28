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
package com.hybris.oms.facade.order;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.hybris.oms.api.order.OrderFacade;
import com.hybris.oms.domain.exception.DuplicateEntityException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.order.OrderLineQuantityStatus;

/**
 * Integration test for {@link DefaultOrderFacade}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/oms-facade-spring-test.xml"})
public class DefaultOrderFacadeIntegrationTest {

	private static final String OLQ_STATUS_CODE = "STATUS_CODE";
	private static final String DESCRIPTION = "DESCRIPTION";
	
	@Autowired
	private OrderFacade orderFacade;
	
	@Test
	@Transactional
	public void shouldCreateOrderLineQuantityStatus()
	{
		OrderLineQuantityStatus olqStatus = new OrderLineQuantityStatus();
		olqStatus.setStatusCode(OLQ_STATUS_CODE);
		olqStatus.setDescription(DESCRIPTION);
		olqStatus = this.orderFacade.createOrderLineQuantityStatus(olqStatus);
		Assert.assertEquals(OLQ_STATUS_CODE, olqStatus.getStatusCode());
	}
	
	@Test(expected = EntityValidationException.class)
	@Transactional
	public void shouldCreateInvalidOrderLineQuantityStatus()
	{
		final OrderLineQuantityStatus olqStatus = new OrderLineQuantityStatus();
		olqStatus.setStatusCode("");
		this.orderFacade.createOrderLineQuantityStatus(olqStatus);
	}
	
	@Test(expected = DuplicateEntityException.class)
	@Transactional
	public void shouldCreateDuplicateOrderLineQuantityStatus()
	{
		final OrderLineQuantityStatus olqStatus = new OrderLineQuantityStatus();
		olqStatus.setStatusCode(OLQ_STATUS_CODE);
		olqStatus.setDescription(DESCRIPTION);
		this.orderFacade.createOrderLineQuantityStatus(olqStatus);
		this.orderFacade.createOrderLineQuantityStatus(olqStatus);
	}
}
