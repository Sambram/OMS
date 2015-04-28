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
package com.hybris.oms.ui.rest.web.returns;

import com.hybris.commons.web.authorization.RoleConstants;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.domain.returns.ReturnQueryObject;
import com.hybris.oms.rest.web.util.QueryObjectPopulator;
import com.hybris.oms.rest.web.util.RestUtil;
import com.hybris.oms.ui.api.returns.ReturnDetail;
import com.hybris.oms.ui.api.returns.ReturnDetailFacade;
import com.hybris.oms.ui.rest.web.shipment.UIShipmentDetailsResource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;


/**
 * WebResource exposing {@link ReturnDetailFacade} http://localhost:8080/oms-rest-webapp/webresources/returndetails.
 */
@Component
@Path("/returndetails")
@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.ACCELERATOR})
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class ReturnDetailsResource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(UIShipmentDetailsResource.class);

	@Autowired
	private ReturnDetailFacade returnDetailFacade;

	@Autowired
	@Qualifier("uriQueryObjectPopulator")
	private QueryObjectPopulator<UriInfo> queryObjectPopulator;

	/**
	 * Get a list of ReturnDetail regarding the query parameters informed.
	 * 
	 * @param uriInfo
	 *           URI info.
	 * 
	 * @return a Response object containing the list of return details
	 *         {@link com.hybris.oms.domain.returns.ReturnQuerySupport} {@link com.hybris.oms.domain.SortDirection}
	 */
	@GET
	@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER})
	public Response findReturnDetailsByQuery(@Context final UriInfo uriInfo)
	{
		LOGGER.trace("findReturnDetailsByQuery");

		final ReturnQueryObject queryObject = new ReturnQueryObject();
		this.queryObjectPopulator.populate(uriInfo, queryObject);

		final Pageable<ReturnDetail> pagedReturns = this.returnDetailFacade.findReturnDetailsByQuery(queryObject);

		final GenericEntity<List<ReturnDetail>> entity = new GenericEntity<List<ReturnDetail>>(pagedReturns.getResults())
		{/* NOPMD */
		};

		final Response.ResponseBuilder responseBuilder = RestUtil.createResponsePagedHeaders(pagedReturns.getNextPage(),
				pagedReturns.getPreviousPage(), pagedReturns.getTotalPages(), pagedReturns.getTotalRecords());
		return responseBuilder.entity(entity).build();
	}

}
