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
package com.hybris.oms.rest.web.returns;


import com.hybris.commons.web.authorization.RoleConstants;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.returns.ReturnFacade;
import com.hybris.oms.domain.returns.Return;
import com.hybris.oms.domain.returns.ReturnQueryObject;
import com.hybris.oms.rest.web.util.QueryObjectPopulator;
import com.hybris.oms.rest.web.util.RestUtil;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.codehaus.enunciate.jaxrs.TypeHint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;


@Service
@Path("/returns")
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class ReturnsResource
{

	@Autowired
	private ReturnFacade returnFacade;

	@Autowired
	@Qualifier("uriQueryObjectPopulator")
	private QueryObjectPopulator<UriInfo> queryObjectPopulator;

	/**
	 * Get a list of {@link Return} regarding the query parameters informed.
	 * 
	 * @param uriInfo
	 *           URI info.
	 * @return a Response object containing the list of Returns {@link com.hybris.oms.domain.returns.ReturnQuerySupport}
	 *         {@link com.hybris.oms.domain.SortDirection}
	 */
	@GET
	@TypeHint(Return.class)
	@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.INVENTORY_MANAGER})
	public Response findReturnsByQuery(@Context final UriInfo uriInfo)
	{
		final ReturnQueryObject queryObject = new ReturnQueryObject();
		this.queryObjectPopulator.populate(uriInfo, queryObject);

		final Pageable<Return> pagedReturns = this.returnFacade.findReturnsByQuery(queryObject);
		final GenericEntity<List<Return>> entity = new GenericEntity<List<Return>>(pagedReturns.getResults())
		{// NOPMD
		};

		final Response.ResponseBuilder responseBuilder = RestUtil.createResponsePagedHeaders(pagedReturns.getNextPage(),
				pagedReturns.getPreviousPage(), pagedReturns.getTotalPages(), pagedReturns.getTotalRecords());
		return responseBuilder.entity(entity).build();
	}

	/**
	 * Create Return.
	 * 
	 * @param aReturn object of type Return to be created
	 * @return response with the DTO object representing the created return
	 */
	@POST
	@TypeHint(Return.class)
	@Secured({RoleConstants.ADMIN, RoleConstants.INVENTORY_MANAGER})
	public Response createReturn(final Return aReturn)
	{
		final Return createdReturn = returnFacade.createReturn(aReturn);
		return Response.ok().entity(createdReturn).build();
	}

	/**
	 * Update Return
	 * 
	 * @param aReturn object of type Return to be updated
	 * @return response with the DTO object representing the updated Return
	 */
	@PUT
	@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.INVENTORY_MANAGER})
	public Response updateReturn(final Return aReturn)
	{
		final Return updatedReturn = returnFacade.updateReturn(aReturn);
		return Response.ok().entity(updatedReturn).build();
	}

	/**
	 * Get Return by id.
	 * 
	 * @param returnId the return unique Identifier to be retrieved
	 * @return response with the DTO object representing requested Return
	 */
	@GET
	@Path("/{returnId}")
	@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.INVENTORY_MANAGER})
	public Response getReturnById(@PathParam("returnId") final String returnId)
	{
		final Return aReturn = returnFacade.getReturnById(returnId);
		return Response.ok().entity(aReturn).build();
	}

	/**
	 * Cancel a Return
	 * 
	 * @param returnId object of type Return to be updated with Cancel State
	 * @return response with the DTO object representing the updated Return
	 */
	@PUT
	@Path("/{returnId}/cancel")
	@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.INVENTORY_MANAGER})
	public Response cancelReturn(@PathParam("returnId") final String returnId)
	{
		final Return canceledReturn = returnFacade.cancelReturn(returnId);
		return Response.ok().entity(canceledReturn).build();
	}

	/**
	 * AutoRefund a Return </br>
	 * 
	 * @param returnId the Id of the {@link Return} to be auto refund
	 * @return response with the DTO object representing the updated Return
	 */
	@PUT
	@Path("/{returnId}/refund/auto")
	@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.INVENTORY_MANAGER})
	public Response autoRefundReturn(@PathParam("returnId") final String returnId)
	{
		final Return aReturn = returnFacade.autoRefundReturn(returnId);
		return Response.ok().entity(aReturn).build();
	}


	/**
	 * ManualRefund a Return </br>
	 * 
	 * @param returnId the Id of the {@link Return} to be auto refund
	 * @return response with the DTO object representing the updated Return
	 */
	@PUT
	@Path("/{returnId}/refund/manual")
	@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.INVENTORY_MANAGER})
	public Response manualRefundReturn(@PathParam("returnId") final String returnId)
	{
		final Return aReturn = returnFacade.manualRefundReturn(returnId);
		return Response.ok().entity(aReturn).build();
	}


	/**
	 * Fix the tax reverse failure on a Return </br>
	 * In fact, just indicate that someone, somehow fixed it, and we can unblock the return process
	 * 
	 * @param returnId id of object of type Return to be fixed.
	 * @return response with the DTO object representing the updated Return
	 */
	@PUT
	@Path("/{returnId}/taxes/reverse")
	@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.INVENTORY_MANAGER})
	public Response fixTaxReverse(@PathParam("returnId") final String returnId)
	{
		final Return aReturn = returnFacade.fixTaxReverseFailure(returnId);
		return Response.ok().entity(aReturn).build();
	}
}
