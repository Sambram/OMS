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
package com.hybris.oms.rest.resources;


import com.hybris.oms.api.DummyObjectFacade;
import com.hybris.oms.custom.facade.TestFacade;
import com.hybris.oms.custom.services.TestService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.hybris.oms.api.dto.DummyObject;
import com.hybris.oms.domain.order.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * REST resource to handle request relevant to Dummy object.
 */
@Consumes({MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_XML})
@Path("/dummy")
public class DummyObjectResource
{
	private DummyObjectFacade dummyObjectFacade;

	@Autowired
	private TestService testService;
	
	@Autowired
	TestFacade testFacade;

	@Required
	public void setDummyObjectFacade(final DummyObjectFacade facade)
	{
		this.dummyObjectFacade = facade;
	}

	@POST
	public Response create(@RequestBody final DummyObject dummyObject)
	{
		dummyObjectFacade.create(dummyObject);
		return Response.status(Response.Status.CREATED).build();
	}

	@GET
	@Path("/{name}")
	public Response getDummyObject(@PathParam("name") final String name)
	{
		final DummyObject dummyObject = dummyObjectFacade.get(name);
		return Response.ok().entity(dummyObject).build();
	}

	@GET
	@Path("/order")
	public Response getFavoriteOrder()
	{
		final Order favoriteOrder = dummyObjectFacade.getMyFavoriteOrder();
		return Response.ok().entity(favoriteOrder).build();
	}

	@GET
	@Path("/test")
	public void getAllDummyObjects()
	{
		testService.print("\n WELCOME TO OMS(TEST)");
		// return dummyObjectFacade.getAll();
	}

	@GET
	@Path("/myorder")
	public void getMyOrder(String orderId)
	{
		System.out.println("\n\t getMyOrder() -> DummyObjectResource");
		
		// Display EmailId and Username
		//testService.getMyOrder("15481123719086096-00002001");
		
		
		// return dummyObjectFacade.getAll();
	}
	
	@GET
	@Path("/insert")
	public void insertEmp(String orderId)
	{
		System.out.println("\n\n insert in DummyObjectResource : ");
		testService.insert();
	}
	
	@POST
	public void saveEmp(String orderId)
	{
		System.out.println("\n saveEmp() in DummyObjectResource  ");
		testFacade.saveEmp();
	}

}
