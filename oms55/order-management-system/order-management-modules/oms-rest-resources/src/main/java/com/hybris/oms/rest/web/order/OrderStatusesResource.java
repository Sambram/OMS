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
import com.hybris.oms.domain.order.OrderLineQuantityStatus;
import com.hybris.oms.domain.order.jaxb.OrderLineQuantityStatusesList;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;


/**
 * WebResource exposing {@link OrderFacade} http://localhost:8080/oms-rest-webapp/webresources/statuses/orders.
 */
@Component
@Path("/statuses/orders")
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class OrderStatusesResource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderStatusesResource.class);

	@Autowired
	private OrderFacade orderFacade;

	/**
	 * Create one Order Line Quantity Status.
	 * 
	 * @param status
	 *           the {@link OrderLineQuantityStatus} object to be created
	 * 
	 * @return A Response object containing the created OrderLineQuantityStatus object embedded in it.
	 */
	@POST
	@Secured(RoleConstants.ADMIN)
	public Response createOrderLineQuantityStatus(final OrderLineQuantityStatus status)
	{
		LOGGER.trace("createOrderLineQuantityStatus");
		final GenericEntity<OrderLineQuantityStatus> entity = new OrderLineQuantityStatusEntity(
				this.orderFacade.createOrderLineQuantityStatus(status));
		return Response.ok(entity).build();
	}

	/**
	 * Get a List of all Order Line Quantity Statuses.
	 * 
	 * @return a Response object containing the list of OrderLineQuantityStatus object embedded in it.
	 */
	@GET
	@Secured({RoleConstants.ADMIN, RoleConstants.ACCELERATOR})
	public Response findAllOrderLineQuantityStatuses()
	{
		LOGGER.trace("findAllOrderLineQuantityStatuses");
		final OrderLineQuantityStatusesList result = new OrderLineQuantityStatusesList();
		final List<OrderLineQuantityStatus> list = this.orderFacade.findAllOrderLineQuantityStatuses();
		if (list != null)
		{
			result.initialize(list);
		}
		final GenericEntity<OrderLineQuantityStatusesList> entity = new OrderLineQuantityStatusesListEntity(result);
		return Response.ok().entity(entity).build();

	}

	/**
	 * Update an existing Order Line Quantity Status.
	 * 
	 * @param status
	 *           the {@link OrderLineQuantityStatus} object to be updated
	 * 
	 * @return a Response object containing the updated OrderLineQuantityStatus object embedded in it.
	 */
	@PUT
	@Secured(RoleConstants.ADMIN)
	public Response updateOrderLineQuantityStatus(final OrderLineQuantityStatus status)
	{
		LOGGER.trace("updateOrderLineQuantityStatus");
		final GenericEntity<OrderLineQuantityStatus> entity = new OrderLineQuantityStatusEntity(
				this.orderFacade.updateOrderLineQuantityStatus(status));
		return Response.ok().entity(entity).build();
	}

	/**
	 * The Class OrderLineQuantityStatusEntity.
	 */
	private static final class OrderLineQuantityStatusEntity extends GenericEntity<OrderLineQuantityStatus>
	{

		/**
		 * Instantiates a new order line quantity status entity.
		 * 
		 * @param entity the entity
		 */
		private OrderLineQuantityStatusEntity(final OrderLineQuantityStatus entity)
		{
			super(entity);
		}
	}

	/**
	 * The Class OrderLineQuantityStatusesListEntity.
	 */
	private static final class OrderLineQuantityStatusesListEntity extends GenericEntity<OrderLineQuantityStatusesList>
	{

		/**
		 * Instantiates a new order line quantity statuses list entity.
		 * 
		 * @param entity the entity
		 */
		private OrderLineQuantityStatusesListEntity(final OrderLineQuantityStatusesList entity)
		{
			super(entity);
		}
	}
}
