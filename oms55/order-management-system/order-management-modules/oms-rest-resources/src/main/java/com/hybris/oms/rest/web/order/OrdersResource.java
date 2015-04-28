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
package com.hybris.oms.rest.web.order;

import com.hybris.commons.web.authorization.RoleConstants;
import com.hybris.oms.api.order.OrderFacade;
import com.hybris.oms.api.shipment.ShipmentFacade;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.shipping.Shipment;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.enunciate.jaxrs.TypeHint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;


/**
 * WebResource exposing {@link OrderFacade} http://localhost:8080/oms-rest-webapp/webresources/orders.
 */

@Component
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Path("/orders")
public class OrdersResource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(OrdersResource.class);

	private static final String ORDER_ID = "orderId";

	@Autowired
	private OrderFacade orderFacade;

	@Autowired
	private ShipmentFacade shipmentServices;

	@POST
	@TypeHint(Order.class)
	@Secured({RoleConstants.ADMIN, RoleConstants.ACCELERATOR})
	public Response createOrder(final Order order)
	{
		LOGGER.trace("createOrder");
		final Order createdOrder = orderFacade.createOrder(order);
		return Response.ok().entity(createdOrder).build();
	}

	@GET
	@Path("/{orderId}")
	@TypeHint(Order.class)
	@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.ACCELERATOR, RoleConstants.SHIPPING_USER,
			RoleConstants.INSTOREPICKUP_USER, RoleConstants.INSTOREPICKUPANDSHIPPING_USER})
	public Response getOrderByOrderId(@PathParam(ORDER_ID) final String orderId)
	{
		final Response.ResponseBuilder response = Response.ok();
		final Order order = this.orderFacade.getOrderByOrderId(orderId);
		response.entity(order);
		return response.build();
	}

	@GET
	@Path("/{orderId}/shipments")
	@Secured({RoleConstants.ADMIN, RoleConstants.SHIPPING_USER, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.ACCELERATOR,
			RoleConstants.INSTOREPICKUP_USER, RoleConstants.INSTOREPICKUPANDSHIPPING_USER})
	public Collection<Shipment> getShipmentsByOrderId(@PathParam(ORDER_ID) final String orderId)
	{
		LOGGER.trace("getShipmentsByOrderId");
		return this.shipmentServices.getShipmentsByOrderId(orderId);
	}

	@POST
	@Path("/{orderId}/fulfill")
	@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.ACCELERATOR, RoleConstants.SHIPPING_USER,
			RoleConstants.INSTOREPICKUP_USER, RoleConstants.INSTOREPICKUPANDSHIPPING_USER})
	public Response fulfillOrder(@PathParam(ORDER_ID) final String orderId)
	{
		LOGGER.trace("fulfillOrder");
		orderFacade.fulfill(orderId);
		return Response.ok().build();
	}

	@POST
	@Path("/{orderId}/cancel/unfulfilled")
	@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.ACCELERATOR, RoleConstants.SHIPPING_USER,
			RoleConstants.INSTOREPICKUP_USER, RoleConstants.INSTOREPICKUPANDSHIPPING_USER})
	public Response cancelUnfulfilled(@PathParam(ORDER_ID) final String orderId)
	{
		LOGGER.trace("cancelUnfulfilled");
		orderFacade.cancelUnfulfilled(orderId);
		return Response.ok().build();
	}

	@DELETE
	@Path("/{orderId}/cancel")
	@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.ACCELERATOR, RoleConstants.SHIPPING_USER,
			RoleConstants.INSTOREPICKUP_USER, RoleConstants.INSTOREPICKUPANDSHIPPING_USER})
	@Deprecated
	public Response cancelOrder(@PathParam(ORDER_ID) final String orderId)
	{
		LOGGER.trace("cancelOrder");
		final Order order = this.orderFacade.cancelOrder(orderId);
		return Response.ok().entity(order).build();
	}

}
