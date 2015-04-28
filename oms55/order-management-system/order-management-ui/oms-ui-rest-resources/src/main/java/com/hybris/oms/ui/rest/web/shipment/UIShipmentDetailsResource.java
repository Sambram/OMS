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
package com.hybris.oms.ui.rest.web.shipment;

import com.hybris.commons.web.authorization.RoleConstants;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.domain.shipping.ShipmentQueryObject;
import com.hybris.oms.rest.web.util.QueryObjectPopulator;
import com.hybris.oms.rest.web.util.RestUtil;
import com.hybris.oms.ui.api.shipment.ShipmentDetail;
import com.hybris.oms.ui.api.shipment.UiShipmentFacade;

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
 * WebResource exposing {@link UiShipmentFacade} http://localhost:8080/oms-rest-webapp/webresources/uishipmentDetails.
 */
@Component
@Path("/uishipmentdetails")
@Consumes({MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_XML})
public class UIShipmentDetailsResource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(UIShipmentDetailsResource.class);

	@Autowired
	private UiShipmentFacade uiShipmentFacade;

	@Autowired
	@Qualifier("uriQueryObjectPopulator")
	private QueryObjectPopulator<UriInfo> queryObjectPopulator;

	/**
	 * Get a list of ShipmentDetail regarding the query parameters informed.
	 * 
	 * @param uriInfo
	 *           URI info.
	 * 
	 * @return a Response object containing the list of orders {@link com.hybris.oms.domain.order.OrderQuerySupport}
	 *         {@link com.hybris.oms.domain.SortDirection}
	 */
	@GET
	@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.ACCELERATOR, RoleConstants.SHIPPING_USER,
			RoleConstants.INSTOREPICKUP_USER, RoleConstants.INSTOREPICKUPANDSHIPPING_USER})
	public Response findShipmentsByQuery(@Context final UriInfo uriInfo)
	{
		LOGGER.trace("findShipmentByQuery");

		final ShipmentQueryObject queryObject = new ShipmentQueryObject();
		this.queryObjectPopulator.populate(uriInfo, queryObject);

		final Pageable<ShipmentDetail> pagedShipment = this.uiShipmentFacade.findShipmentDetailsByQuery(queryObject);

		final GenericEntity<List<ShipmentDetail>> entity = new GenericEntity<List<ShipmentDetail>>(pagedShipment.getResults())
		{/* NOPMD */
		};

		final Response.ResponseBuilder responseBuilder = RestUtil.createResponsePagedHeaders(pagedShipment.getNextPage(),
				pagedShipment.getPreviousPage(), pagedShipment.getTotalPages(), pagedShipment.getTotalRecords());
		return responseBuilder.entity(entity).build();
	}
}
