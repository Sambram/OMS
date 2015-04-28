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
package com.hybris.oms.rest.web.inventory;

import com.hybris.commons.web.authorization.RoleConstants;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.domain.inventory.Bin;
import com.hybris.oms.domain.inventory.BinList;
import com.hybris.oms.domain.inventory.BinQueryObject;
import com.hybris.oms.rest.web.util.QueryObjectPopulator;
import com.hybris.oms.rest.web.util.RestUtil;

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
 * WebResource exposing {@link InventoryFacade} http://localhost:8080/oms-rest-webapp/webresources/bins.
 */
@Component
@Path("/bins")
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class BinsResource
{
	private static final Logger LOG = LoggerFactory.getLogger(BinsResource.class);

	@Autowired
	private InventoryFacade inventoryServiceApi;

	@Autowired
	@Qualifier("uriQueryObjectPopulator")
	private QueryObjectPopulator<UriInfo> queryObjectPopulator;

	/**
	 * Get a list of bins according to given query parameters.
	 * 
	 * @param uriInfo
	 *           URI info.
	 * 
	 * @return A Response object containing a list of Bins embedded in it.
	 */
	@GET
	@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.INVENTORY_MANAGER})
	public Response findBinsByQuery(@Context final UriInfo uriInfo)
	{
		LOG.trace("findBinsByQuery");
		final BinList result = new BinList();

		final BinQueryObject queryObject = new BinQueryObject();
		this.queryObjectPopulator.populate(uriInfo, queryObject);

		final Pageable<Bin> pagedBins = this.inventoryServiceApi.findBinsByQuery(queryObject);
		if (pagedBins.getResults() != null)
		{
			result.initializeBins(pagedBins.getResults());
		}

		final Response.ResponseBuilder responseBuilder = RestUtil.createResponsePagedHeaders(pagedBins.getNextPage(),
				pagedBins.getPreviousPage(), pagedBins.getTotalPages(), pagedBins.getTotalRecords());

		final GenericEntity<BinList> entity = new BinListEntity(result);
		return responseBuilder.entity(entity).build();
	}

	private static final class BinListEntity extends GenericEntity<BinList>
	{
		private BinListEntity(final BinList entity)
		{
			super(entity);
		}
	}
}
