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
import com.hybris.oms.api.shipment.ShipmentFacade;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.ui.api.shipment.OrderShipmentDetail;
import com.hybris.oms.ui.api.shipment.OrderShipmentList;
import com.hybris.oms.ui.api.shipment.UiShipmentFacade;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;


/**
 * * WebResource exposing {@link ShipmentFacade}
 * http://localhost:8080/oms-rest-webapp/webresources/uiordershipments/order/{orderId}.
 */
@Component
@Path("/uiordershipments/order/{orderId}")
@Consumes({MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_XML})
public class OrderShipmentsOrderIdResource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderShipmentsOrderIdResource.class);

	@Autowired
	private UiShipmentFacade uiShipmentFacade;

	/**
	 * Get all OLQs assigned to Order.
	 * 
	 * @param orderId
	 *           the order id to fetch olq list for.
	 * @param allLocationDisplay
	 *           all location display switch to enable displaying all locations
	 * @return Response with the {@link OrderShipmentList} embedded in it
	 * @throws EntityNotFoundException
	 *            thrown when no order object found that corresponds to the passed order id
	 */
	@GET
	@Secured({RoleConstants.ADMIN, RoleConstants.SHIPPING_USER, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.ACCELERATOR,
			RoleConstants.INSTOREPICKUP_USER, RoleConstants.INSTOREPICKUPANDSHIPPING_USER})
	public Response getOLQListForOrder(@PathParam("orderId") final String orderId,
			@QueryParam("allLocationDisplay") final boolean allLocationDisplay) throws EntityNotFoundException
	{
		LOGGER.trace("getOLQListForOrder");
		final OrderShipmentList resultList = new OrderShipmentList();

		final List<OrderShipmentDetail> ordrShpmntDtails = this.uiShipmentFacade.findOrderShipmentDetailsByOrderId(orderId,
				allLocationDisplay);

		if (ordrShpmntDtails != null)
		{
			resultList.initializeOrders(ordrShpmntDtails);
		}

		final GenericEntity<OrderShipmentList> entity = new OrderShipmentEntity(resultList);
		return Response.ok().entity(entity).build();
	}

	private static final class OrderShipmentEntity extends GenericEntity<OrderShipmentList>
	{
		private OrderShipmentEntity(final OrderShipmentList entity)
		{
			super(entity);
		}
	}
}
