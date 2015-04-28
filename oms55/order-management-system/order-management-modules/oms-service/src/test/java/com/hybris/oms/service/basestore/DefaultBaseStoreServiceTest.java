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
package com.hybris.oms.service.basestore;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.hybris.kernel.api.ImportService;
import com.hybris.kernel.api.Page;
import com.hybris.kernel.utils.ClasspathScanningResourceFetcher;
import com.hybris.kernel.utils.ResourceFetcher;
import com.hybris.oms.domain.basestore.BaseStoreQueryObject;
import com.hybris.oms.service.managedobjects.basestore.BaseStoreData;


@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-service-spring-test.xml"})
public class DefaultBaseStoreServiceTest
{

	private static final String NOT_EXISTING_BASE_STORE_NAME = "notExistingBaseStoreName";
	private static final String BASE_STORE_NAME = "baseStore1";

	private final ResourceFetcher fetcher = new ClasspathScanningResourceFetcher();

	@Autowired
	private ImportService importService;

	@Autowired
	private BaseStoreService baseStoreService;

	@Before
	public void setUp() throws InterruptedException
	{
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/basestore/test-basestore-data-import.mcsv")[0]);
	}

	@Test
	@Transactional
	public void testBaseStoreExistence()
	{
		final BaseStoreData baseStore1 = this.baseStoreService.findBaseStoreByName(BASE_STORE_NAME);

		Assert.assertNotNull(baseStore1);
		Assert.assertEquals(BASE_STORE_NAME, baseStore1.getDescription());
		Assert.assertEquals(BASE_STORE_NAME, baseStore1.getName());
		Assert.assertEquals(3, baseStore1.getStockroomLocations().size());

		final BaseStoreData baseStore2 = this.baseStoreService.findBaseStoreByName(NOT_EXISTING_BASE_STORE_NAME);
		Assert.assertNull(baseStore2);
	}

	@Test
	@Transactional
	public void testBaseStoresExistance()
	{
		final BaseStoreQueryObject baseStoreQueryObject = new BaseStoreQueryObject();
		final Page<BaseStoreData> baseStores = this.baseStoreService.findPagedBaseStoresByQuery(baseStoreQueryObject);
		Assert.assertNotNull(baseStores);
		Assert.assertEquals(1, baseStores.getTotalElements());
	}

}
