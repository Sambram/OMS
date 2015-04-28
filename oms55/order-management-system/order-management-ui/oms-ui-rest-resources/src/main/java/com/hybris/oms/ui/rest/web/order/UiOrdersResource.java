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
package com.hybris.oms.ui.rest.web.order;

import com.hybris.commons.web.authorization.RoleConstants;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.domain.order.OrderQueryObject;
import com.hybris.oms.rest.web.util.QueryObjectPopulator;
import com.hybris.oms.rest.web.util.RestUtil;
import com.hybris.oms.ui.api.order.UIOrder;
import com.hybris.oms.ui.api.order.UiOrderFacade;

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

import org.codehaus.enunciate.jaxrs.TypeHint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;


/**
 * WebResource exposing {@link UiOrderFacade} http://localhost:8080/oms-rest-webapp/webresources/uiorders.
 */
@Component
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Path("/uiorders")
public class UiOrdersResource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(UiOrdersResource.class);

	@Autowired
	private UiOrderFacade uiOrderFacade;

	@Autowired
	@Qualifier("uriQueryObjectPopulator")
	private QueryObjectPopulator<UriInfo> queryObjectPopulator;

	/**
	 * Get a list of {@link UIOrder} regarding the query parameters informed.
	 * 
	 * @param uriInfo
	 *           URI info.
	 * @return a Response object containing the list of UIOrders {@link com.hybris.oms.domain.order.OrderQuerySupport}
	 *         {@link com.hybris.oms.domain.SortDirection}
	 */
	@GET
	@TypeHint(UIOrder.class)
	@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.ACCELERATOR, RoleConstants.SHIPPING_USER,
			RoleConstants.INSTOREPICKUP_USER, RoleConstants.INSTOREPICKUPANDSHIPPING_USER})
	public Response findOrdersByQuery(@Context final UriInfo uriInfo)
	{
		LOGGER.trace("findOrdersByQuery");

		final OrderQueryObject queryObject = new OrderQueryObject();
		this.queryObjectPopulator.populate(uriInfo, queryObject);

		final Pageable<UIOrder> pagedOrders = this.uiOrderFacade.findOrdersByQuery(queryObject);
		final GenericEntity<List<UIOrder>> entity = new GenericEntity<List<UIOrder>>(pagedOrders.getResults())
		{// NOPMD
		};

		final Response.ResponseBuilder responseBuilder = RestUtil.createResponsePagedHeaders(pagedOrders.getNextPage(),
				pagedOrders.getPreviousPage(), pagedOrders.getTotalPages(), pagedOrders.getTotalRecords());
		return responseBuilder.entity(entity).build();
	}

}
