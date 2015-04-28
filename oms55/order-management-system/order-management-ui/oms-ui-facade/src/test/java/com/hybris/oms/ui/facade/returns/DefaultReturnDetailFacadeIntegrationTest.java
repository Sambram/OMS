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
package com.hybris.oms.ui.facade.returns;

import static org.junit.Assert.assertEquals;

import com.hybris.kernel.api.ImportService;
import com.hybris.kernel.utils.ClasspathScanningResourceFetcher;
import com.hybris.kernel.utils.ResourceFetcher;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.returns.ReturnFacade;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.returns.Return;
import com.hybris.oms.domain.returns.ReturnQueryObject;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.ui.api.returns.ReturnDetail;
import com.hybris.oms.ui.api.returns.ReturnDetailFacade;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableList;


/**
 * Integration test for {@link DefaultReturnDetailFacade}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/oms-ui-facade-spring-test.xml")
public class DefaultReturnDetailFacadeIntegrationTest
{
	private static final String RETURN_ID = "8";
	private static final String ORDER_ID = "12";
	private static final String ORDER_ID_2 = "83";

	@Autowired
	private ImportService importService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private ReturnFacade returnFacade;
	@Autowired
	private ReturnDetailFacade returnDetailFacade;

	private final ResourceFetcher fetcher = new ClasspathScanningResourceFetcher();

	@Before
	public void setUpMcsvResources()
	{
		this.importService
				.loadMcsvResource(this.fetcher.fetchResources("/tenantPreference/test-tenantPreference-data-import.mcsv")[0]);
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/order/test-order-data-import.mcsv")[0]);
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/returns/test-return-data-import.mcsv")[0]);
	}

	@Test
	@Transactional
	public void shouldGetReturnDetailsByReturnId()
	{
		final ReturnDetail returnDetail = this.returnDetailFacade.getReturnDetailById(RETURN_ID);
		assertEquals(RETURN_ID, returnDetail.getReturn().getReturnId());
	}

	@Test
	@Transactional
	public void shouldFindReturnsByQuery()
	{
		final OrderData order = orderService.getOrderByOrderId(ORDER_ID);
		assertEquals(ORDER_ID, order.getOrderId());

		final ReturnQueryObject queryObject = new ReturnQueryObject();
		queryObject.setPageNumber(0);
		queryObject.setPageSize(100);
		queryObject.setOrderIds(ImmutableList.of(ORDER_ID));

		final Pageable<Return> returns = this.returnFacade.findReturnsByQuery(queryObject);
		assertEquals(Long.valueOf(2), returns.getTotalRecords());
		assertEquals(ORDER_ID, returns.getResults().get(0).getOrderId());
		assertEquals(ORDER_ID, returns.getResults().get(1).getOrderId());
	}

	@Test
	@Transactional
	public void shouldBuildReturnDetailByOrderId()
	{
		final ReturnDetail returnDetail = this.returnDetailFacade.buildReturnDetailByOrderId(ORDER_ID_2);
		assertEquals(null, returnDetail.getReturn().getReturnId());
		assertEquals(ORDER_ID_2, returnDetail.getOrder().getOrderId());
		assertEquals(false, returnDetail.getReturn().getShippingRefunded());
		assertEquals(2, returnDetail.getReturn().getReturnOrderLines().size());
		assertEquals(50, returnDetail.getReturn().getReturnOrderLines().get(0).getQuantity().getValue());
		assertEquals(25, returnDetail.getReturn().getReturnOrderLines().get(1).getQuantity().getValue());
	}

	@Test(expected = EntityValidationException.class)
	@Transactional
	public void shouldFindReturnsByQueryInvalidPageNumber()
	{
		final ReturnQueryObject queryObject = new ReturnQueryObject();
		queryObject.setPageNumber(-1);
		queryObject.setPageSize(100);

		this.returnDetailFacade.findReturnDetailsByQuery(queryObject);
	}

	@Test(expected = EntityValidationException.class)
	@Transactional
	public void shouldFindReturnsByQueryInvalidPageSize()
	{
		final ReturnQueryObject queryObject = new ReturnQueryObject();
		queryObject.setPageNumber(0);
		queryObject.setPageSize(5000);

		this.returnFacade.findReturnsByQuery(queryObject);
	}

}
