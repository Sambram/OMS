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
package com.hybris.oms.service.sample;

import com.hybris.kernel.api.ImportService;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


/**
 * Tests if OMS's project-data exists and has the proper formatting in order to be loaded by the importService from
 * kernel.
 * 
 * The project data is needed to create a "Test Account" needed for the Accelerator.
 * This is done using the "createTestAccount" method from Subscription which creates an "account" and,
 * besides loading essential data, also triggers loading of project data for each "remote" service.
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-service-spring-test.xml"})
public class ProjectDataImportTest
{
	@Autowired
	private ImportService importService;

	@Resource
	private PersistenceManager persistenceManager;

	@Test
	@Transactional
	public void loadProjectData()
	{
		Assert.assertTrue(persistenceManager.createCriteriaQuery(StockroomLocationData.class).resultList().isEmpty());

		importService.loadProjectData();

		Assert.assertTrue(persistenceManager.createCriteriaQuery(StockroomLocationData.class).resultList().size() > 0);
	}
}
