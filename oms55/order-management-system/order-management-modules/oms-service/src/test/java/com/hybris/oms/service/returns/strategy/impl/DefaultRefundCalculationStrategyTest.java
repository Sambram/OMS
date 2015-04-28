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
package com.hybris.oms.service.returns.strategy.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.hybris.kernel.api.ImportService;
import com.hybris.kernel.utils.ClasspathScanningResourceFetcher;
import com.hybris.kernel.utils.ResourceFetcher;
import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.service.managedobjects.preference.TenantPreferenceData;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.managedobjects.types.AmountVT;
import com.hybris.oms.service.preference.TenantPreferenceService;
import com.hybris.oms.service.returns.ReturnService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/oms-service-spring-test.xml")
@SuppressWarnings(
{ "PMD.ExcessiveImports" })
public class DefaultRefundCalculationStrategyTest
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultRefundCalculationStrategyTest.class);

	private static final long RETURN_ID = 21;
	public static final int RETURN_ID1 = 26;
	public static final int RETURN_ID2 = 27;
	public static final int RETURN_ID3 = 28;
	public static final int ZERO = 0;

	private final ResourceFetcher fetcher = new ClasspathScanningResourceFetcher();
	@Autowired
	private ReturnService returnService;
	@Autowired
	DefaultRefundCalculationStrategy DefaultRefundCalculationStrategy;
	@Autowired
	private ImportService importService;
	@Autowired
	private TenantPreferenceService tenantPreferenceService;


	@Before
	public void setUp()
	{
		LOG.debug("Load testing values");
		this.importService
				.loadMcsvResource(this.fetcher.fetchResources("/tenantPreference/test-tenantPreference-data-import.mcsv")[0]);
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/order/test-order-data-import.mcsv")[0]);
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/shipment/test-shipment-data-import.mcsv")[0]);
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/returns/test-return-data-import.mcsv")[0]);
	}

	@Test
	@Transactional
	public void testCalculateRefundAmountWithoutShipping()
	{
		final ReturnData aReturn = this.returnService.findReturnById(RETURN_ID);
		// method under test
		final AmountVT result = DefaultRefundCalculationStrategy.calculateRefundAmount(aReturn);
		// 33 = ((10 + 1)* 2 )+ ((10 + 1) * 1 )
		Assert.assertEquals((Object) 33.0, result.getValue());
	}

	@Test
	@Transactional
	public void testCalculateRefundTotalAmount()
	{
		final TenantPreferenceData tenantPreference = this.tenantPreferenceService
				.getTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_RETURN_INCL_SHIPPING_COST);
		tenantPreference.setValue("True");
		final ReturnData aReturn = returnService.findReturnById(RETURN_ID);
		aReturn.setShippingRefunded(true);
		final AmountVT result = DefaultRefundCalculationStrategy.calculateRefundAmount(aReturn);
		// 38.5 = ((10 + 1)* 2 )+ ((10 + 1) * 1 ) + shipping of 4.5 + 1.0 Tax on Shipping
		Assert.assertEquals((Object) 38.5, result.getValue());
	}

	@Test
	@Transactional
	public void testCalculateRefundTotalAmountWithAllRejected()
	{
		final ReturnData aReturn = returnService.findReturnById(RETURN_ID1);
		final AmountVT result = DefaultRefundCalculationStrategy.calculateRefundAmount(aReturn);
		// the quantity requested to be returned is 5
		// we expect a value of 0 Refunded because there are 2 ReturnLineRejections of values (2 and 3)
		Assert.assertEquals(Double.valueOf(ZERO), result.getValue());
	}

	@Test
	@Transactional
	public void testCalculateRefundTotalAmountWithAllApproved()
	{
		final ReturnData aReturn = returnService.findReturnById(RETURN_ID2);
		final AmountVT result = DefaultRefundCalculationStrategy.calculateRefundAmount(aReturn);
		// the quantity requested to be returned is 10
		// we expect a full refund since there is one Rejection with Quantity 0
		// 165 = (10 * (1.5 + 15))
		Assert.assertEquals(Double.valueOf(165), result.getValue());
	}

	@Test
	@Transactional
	public void testCalculateRefundTotalAmountWithPartialRejection()
	{
		final ReturnData aReturn = returnService.findReturnById(RETURN_ID3);
		final AmountVT result = DefaultRefundCalculationStrategy.calculateRefundAmount(aReturn);
		// the quantity requested to be returned is 10
		// there is 1 rejection quantity is 5
		// 82.5 = ((10 - 5) * (1.5 + 15))
		Assert.assertEquals(Double.valueOf(82.5), result.getValue());
	}

}
