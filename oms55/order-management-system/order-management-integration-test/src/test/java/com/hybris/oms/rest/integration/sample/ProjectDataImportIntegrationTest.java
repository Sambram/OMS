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
package com.hybris.oms.rest.integration.sample;

import com.hybris.kernel.initapp.api.InitializationFacade;
import com.hybris.kernel.initapp.rest.client.platform.MultiRestClient;

import java.util.Collections;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Tests if OMS's project data (in kernel format) can be loaded using the init app rest resource.
 * 
 * The project data is needed to create a "Test Account" needed for the Accelerator.
 * This is done using the "createTestAccount" method from Subscription which creates an "account" and,
 * besides loading essential data, also triggers loading of sample data for each "remote" service.
 * 
 * Each service must provide it's own project data (in kernel format) and it must be placed in
 * classpath ('classpath:/META-INF/project-data-*.mcsv').
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-initapp-test-spring.xml"})
public class ProjectDataImportIntegrationTest
{
	private static final String TENANT_CODE = "sample001";

	@Value("${oms.initapp.client.endpoint.uri}")
	private String uri;

	@Resource(name = "platformInitAppInitializationClient")
	private MultiRestClient<InitializationFacade> initializationClient;

	@Before
	public void createTenant()
	{
		initializationClient.withURI(uri).createTenants(Collections.singleton(TENANT_CODE));
	}

	@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	@Test
	public void loadSampleDataForService()
	{
		initializationClient.withURI(uri).loadProjectData(TENANT_CODE);
	}

	@After
	public void deleteTenant()
	{
		initializationClient.withURI(uri).removeTenants(Collections.singleton(TENANT_CODE));
	}
}
