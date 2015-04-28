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
package com.hybris.oms.ui.facade.order;

import com.hybris.oms.api.Pageable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.hybris.kernel.api.ImportService;
import com.hybris.kernel.utils.ClasspathScanningResourceFetcher;
import com.hybris.kernel.utils.ResourceFetcher;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.order.OrderQueryObject;
import com.hybris.oms.ui.api.order.UIOrder;
import com.hybris.oms.ui.api.order.UIOrderDetails;
import com.hybris.oms.ui.api.order.UiOrderFacade;

/**
 * Integration test for {@link DefaultUiOrderFacade}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/oms-ui-facade-spring-test.xml")
public class DefaultUiOrderFacadeIntegrationTest
{
	private static final String INVALID = "INVALID";
	private static final String ORDER_ID = "23";
	private static final String FIRST_NAME = "JohnSmith";
	
	@Autowired
	private ImportService importService;
	@Autowired
	private UiOrderFacade uiOrderFacade;
	
	private final ResourceFetcher fetcher = new ClasspathScanningResourceFetcher();

	@Before
	public void setUpMcsvResources()
	{
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/order/test-order-data-import.mcsv")[0]);
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/tenantPreference/test-tenantPreference-data-import.mcsv")[0]);
	}

	@Test
	@Transactional
	public void shouldGetUIOrderByOrderId()
	{
		final UIOrder order = this.uiOrderFacade.getUIOrderByOrderId(ORDER_ID);
		Assert.assertEquals(ORDER_ID, order.getOrderId());
	}
	
	@Test(expected = EntityNotFoundException.class)
	@Transactional
	public void shouldGetUIOrderByOrderIdNotFound()
	{
		this.uiOrderFacade.getUIOrderByOrderId(INVALID);
	}
	
	@Test
	@Transactional
	public void shouldGetUIOrderDetailsByOrderId()
	{
		final UIOrderDetails orderDetails = this.uiOrderFacade.getUIOrderDetailsByOrderId(ORDER_ID);
		Assert.assertEquals(ORDER_ID, orderDetails.getOrderId());
	}
	
	@Test(expected = EntityNotFoundException.class)
	@Transactional
	public void shouldGetUIOrderDetailsByOrderIdNotFound()
	{
		this.uiOrderFacade.getUIOrderDetailsByOrderId(INVALID);
	}
	
	@Test
	@Transactional
	public void shouldFindOrdersByQuery()
	{
		final OrderQueryObject queryObject = new OrderQueryObject();
		queryObject.setPageNumber(0);
		queryObject.setPageSize(100);
		queryObject.setFirstName(FIRST_NAME);
		
		final Pageable<UIOrder> orders = this.uiOrderFacade.findOrdersByQuery(queryObject);
		Assert.assertEquals(Long.valueOf(1), orders.getTotalRecords());
		Assert.assertEquals(FIRST_NAME, orders.getResults().get(0).getFirstName());
	}
	
	@Test(expected = EntityValidationException.class)
	@Transactional
	public void shouldFindOrdersByQueryInvalidPageNumber()
	{
		final OrderQueryObject queryObject = new OrderQueryObject();
		queryObject.setPageNumber(-1);
		queryObject.setPageSize(100);
		
		this.uiOrderFacade.findOrdersByQuery(queryObject);
	}
	
	@Test(expected = EntityValidationException.class)
	@Transactional
	public void shouldFindOrdersByQueryInvalidPageSize()
	{
		final OrderQueryObject queryObject = new OrderQueryObject();
		queryObject.setPageNumber(0);
		queryObject.setPageSize(5000);
		
		this.uiOrderFacade.findOrdersByQuery(queryObject);
	}

}

