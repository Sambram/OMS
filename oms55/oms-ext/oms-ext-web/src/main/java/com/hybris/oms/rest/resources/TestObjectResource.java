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
package com.hybris.oms.rest.resources;

import com.hybris.oms.custom.services.TestService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@Consumes({MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_XML})
@Path("/testApp")
public class TestObjectResource
{
	@Autowired
	private TestService testService;
	
	@GET
	@Path("/insert")
	public void insertEmp(String orderId)
	{
		System.out.println("\n Checked : ");
		testService.insert();
	}

}
