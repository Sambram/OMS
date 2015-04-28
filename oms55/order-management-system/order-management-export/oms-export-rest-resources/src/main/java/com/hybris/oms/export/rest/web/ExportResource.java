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
package com.hybris.oms.export.rest.web;

import com.hybris.commons.web.authorization.RoleConstants;
import com.hybris.oms.export.api.ExportFacade;
import com.hybris.oms.export.api.ExportTriggerResultDTO;
import com.hybris.oms.export.api.ats.AvailabilityToSellDTO;

import java.net.HttpURLConnection;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.access.annotation.Secured;


/**
 * Abstract resource exposing the {@link ExportFacade} per REST.
 */
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Secured({RoleConstants.ADMIN, RoleConstants.ACCELERATOR})
public abstract class ExportResource<T>
{
	protected abstract ExportFacade<T> getExportFacade();

	/**
	 * Retrieves the data marked for export.
	 *
	 * <p>
	 * <h3>HTTP Response Status Code:</h3>
	 * </p>
	 *
	 * <ol>
	 * <li>200 - data marked for export are successfully returned</li>
	 * <li>500 - Internal problem occurred</li>
	 * </ol>
	 *
	 * @param amountLimit
	 *           the maximum of updates to retrieve with this call, use zero or non-negative number to not set an
	 *           explicit limit
	 * @return a collection of data for export with a maximum size of passed limit or empty collection if no data was
	 *         marked for export
	 */
	@GET
	@Path("/poll")
	public AvailabilityToSellDTO pollChanges(@QueryParam("limit") final int amountLimit,
			@javax.validation.constraints.NotNull @javax.ws.rs.QueryParam("pollingTime") final Long pollingTime,
			@QueryParam("atsId") final String atsId)
	{
		if (StringUtils.isBlank(atsId) || pollingTime == null)
		{
			final String missingField = StringUtils.isBlank(atsId) ? "atsid" : "pollingTime ";
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST)
					.entity(missingField + " parameter is mandatory").build());
		}
		return this.getExportFacade().pollChanges(amountLimit, pollingTime, atsId);
		// no exceptions to handle
	}

	/**
	 * Triggers export of all currently available entities in the system.
	 * This means all currently available entities will be marked for export.
	 *
	 * <p>
	 * <h3>HTTP Response Status Code:</h3>
	 * </p>
	 *
	 * <ol>
	 * <li>200 - trigger process was successful</li>
	 * <li>500 - Internal problem occurred</li>
	 * </ol>
	 *
	 * @return the amount of data marked for export
	 */
	@POST
	@Path("/export")
	public ExportTriggerResultDTO triggerFullExport()
	{
		final ExportTriggerResultDTO result = new ExportTriggerResultDTO();
		result.setAmount(this.getExportFacade().triggerFullExport());
		return result;
		// no exceptions to handle
	}

	/**
	 * Triggers export of the given entity identified by <code>identifier</code>.
	 * This means the data identified by the given identifier will be marked for export.
	 *
	 * <p>
	 * <h3>HTTP Response Status Code:</h3>
	 * </p>
	 *
	 * <ol>
	 * <li>200 - trigger process was successful</li>
	 * <li>500 - Internal problem occurred</li>
	 * </ol>
	 *
	 * @param identifier1 the first identifier for the trigger
	 */
	@POST
	@Path("/export/{identifier1}/{identifier2}")
	public void triggerExport(@PathParam("identifier1") final String identifier1,
			@PathParam("identifier2") final String identifier2)
	{
		this.getExportFacade().triggerExport(identifier1, identifier2);
	}

	/**
	 * Unmark the skus for export; that means they will be delete from ExportSkus table.
	 *
	 * <p>
	 * <h3>HTTP Response Status Code:</h3>
	 * </p>
	 *
	 * <ol>
	 * <li>200 - process was successful</li>
	 * <li>500 - Internal problem occurred</li>
	 * </ol>
	 *
	 * @param latestChange time, as long, since the lastest changes were delete.
	 */
	@DELETE
	public void unmarkSkuForExport(@NotNull @QueryParam("latestChange") final Long latestChange)
	{
		getExportFacade().unmarkSkuForExport(latestChange);
	}
}
