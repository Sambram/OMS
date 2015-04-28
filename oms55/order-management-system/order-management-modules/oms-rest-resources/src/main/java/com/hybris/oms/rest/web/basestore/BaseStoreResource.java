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
package com.hybris.oms.rest.web.basestore;

import com.hybris.commons.web.authorization.RoleConstants;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.basestore.BaseStoreFacade;
import com.hybris.oms.domain.basestore.BaseStore;
import com.hybris.oms.domain.basestore.BaseStoreList;
import com.hybris.oms.domain.basestore.BaseStoreQueryObject;
import com.hybris.oms.rest.web.util.RestUtil;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.enunciate.jaxrs.TypeHint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;


/**
 * WebResource exposing {@link BaseStoreFacade} http://localhost:8080/oms-rest-webapp/webresources/basestore.
 */
@Component
@Path("/basestore")
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class BaseStoreResource
{
	@Autowired
	private BaseStoreFacade baseStoreFacade;

	/**
	 * Created base store.
	 * 
	 * @param baseStore object of type BaseStore to be created
	 * @return response with the DTO object representing the created base store
	 */
	@POST
	@TypeHint(BaseStore.class)
	@Secured({RoleConstants.ADMIN, RoleConstants.INVENTORY_MANAGER})
	public Response createBaseStore(final BaseStore baseStore)
	{
		final BaseStore createdBaseStore = baseStoreFacade.createBaseStore(baseStore);
		return Response.ok().entity(createdBaseStore).build();
	}

	/**
	 * Update base store.
	 * 
	 * @param baseStore object of type BaseStore to be updated
	 * @return response with the DTO object representing the updated base store
	 */
	@PUT
	@Secured({RoleConstants.ADMIN, RoleConstants.INVENTORY_MANAGER})
	public Response updateBaseStore(final BaseStore baseStore)
	{
		final BaseStore updatedBaseStore = baseStoreFacade.updateBaseStore(baseStore);
		return Response.ok().entity(updatedBaseStore).build();
	}

	/**
	 * Get base store by name.
	 * 
	 * @param baseStoreName the base store name (id) to be retrieved
	 * @return response with the DTO object representing requested base store
	 */
	@GET
	@Path("/{baseStoreName}")
	@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.INVENTORY_MANAGER})
	public Response getBaseStoreByName(@PathParam("baseStoreName") final String baseStoreName)
	{
		final BaseStore baseStore = baseStoreFacade.getBaseStoreByName(baseStoreName);
		return Response.ok().entity(baseStore).build();
	}


	/**
	 * Get all base stores.
	 * 
	 * @param pageNumber
	 *           the page number to be retrieved. First page is 0.
	 * @param pageSize
	 *           the page size to be viewed.
	 * @return response with the DTO object representing a list of requested base stores
	 */
	@GET
	@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.INVENTORY_MANAGER})
	public Response findAllBaseStoresByQuery(@QueryParam("pn") final Integer pageNumber, @QueryParam("ps") final Integer pageSize)
	{
		final BaseStoreQueryObject queryObject = new BaseStoreQueryObject();
		queryObject.setPageNumber(pageNumber);
		queryObject.setPageSize(pageSize);

		final BaseStoreList result = new BaseStoreList();

		final Pageable<BaseStore> pagedBaseStores = this.baseStoreFacade.findAllBaseStoresByQuery(queryObject);

		if (pagedBaseStores.getResults() != null)
		{
			result.initializeBaseStores(pagedBaseStores.getResults());
		}

		final Response.ResponseBuilder responseBuilder = RestUtil.createResponsePagedHeaders(pagedBaseStores.getNextPage(),
				pagedBaseStores.getPreviousPage(), pagedBaseStores.getTotalPages(), pagedBaseStores.getTotalRecords());

		final GenericEntity<BaseStoreList> entity = new BaseStoreListEntity(result);
		return responseBuilder.entity(entity).build();
	}


	/**
	 * The Class BaseStoreListEntity.
	 */
	private static final class BaseStoreListEntity extends GenericEntity<BaseStoreList>
	{

		/**
		 * Instantiates a new base store list entity.
		 * 
		 * @param entity of type BaseStoreList
		 */
		BaseStoreListEntity(final BaseStoreList entity)
		{
			super(entity);
		}
	}

}
