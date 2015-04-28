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
package com.hybris.oms.ui.rest.web.preference;

import com.hybris.commons.web.authorization.RoleConstants;
import com.hybris.oms.api.preference.PreferenceFacade;
import com.hybris.oms.domain.preference.TenantPreference;
import com.hybris.oms.ui.api.preference.LocalizedString;
import com.hybris.oms.ui.api.preference.UiPreferenceFacade;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
 * WebResource exposing {@link PreferenceFacade}
 * http://localhost:8080/oms-rest-webapp/webresources/tenantPreferences/{key}/descriptions.
 */
@Component
@Path("/tenantPreferences/{key}/descriptions")
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class LocalizedPreferenceDescriptionResource
{

	private static final Logger LOGGER = LoggerFactory.getLogger(LocalizedPreferenceDescriptionResource.class);
	@Autowired
	private PreferenceFacade preferenceFacade;
	@Autowired
	private UiPreferenceFacade uiPreferenceFacade;

	/**
	 * Get Localized Descriptions of a Tenant Preference by key.
	 * 
	 * @param key The key is mapped on TenantPreference.Property to be fetched
	 * @return A Response object containing a list of {@link LocalizedString} object embedded in it.
	 */
	@GET
	@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.ACCELERATOR, RoleConstants.SHIPPING_USER,
			RoleConstants.INSTOREPICKUP_USER, RoleConstants.INSTOREPICKUPANDSHIPPING_USER})
	public Response getLocalizedTenantPreferenceDescription(@PathParam("key") final String key)
	{
		LOGGER.trace("getLocalizedTenantPreferenceDescription");
		final TenantPreference tenantPreference = this.preferenceFacade.getTenantPreferenceByKey(key);
		final LocalizedString localizedDescriptions = this.uiPreferenceFacade
				.getLocalizedTenantPreferenceDescription(tenantPreference.getProperty());

		final GenericEntity<LocalizedString> entity = new GenericEntity<LocalizedString>(localizedDescriptions)
		{// empty as always
		};

		return Response.ok().entity(entity).build();
	}

	/**
	 * Update Localized Descriptions of a Tenant Preference by tenant preference.
	 * 
	 * @param key The key is mapped on TenantPreference.Property to be updated.
	 * @param mapOfLocalizedDescriptions the map of localized descriptions to be updated
	 * @return A Response object containing a list of {@link TenantPreference} object embedded in it.
	 */
	@PUT
	@Secured({RoleConstants.ADMIN})
	public Response updateLocalizedTenantPreferenceDescription(@PathParam("key") final String key,
			final LocalizedString mapOfLocalizedDescriptions)
	{
		LOGGER.trace("updateLocalizedTenantPreferenceDescription");
		final TenantPreference tenantPreference = this.preferenceFacade.getTenantPreferenceByKey(key);
		this.uiPreferenceFacade.updateLocalizedTenantPreferenceDescription(key, mapOfLocalizedDescriptions);
		final GenericEntity<TenantPreference> entity = new TenantPreferenceEntity(tenantPreference);
		return Response.ok().entity(entity).build();
	}

	/**
	 * The Class TenantPreferenceEntity.
	 */
	private static final class TenantPreferenceEntity extends GenericEntity<TenantPreference>
	{
		/**
		 * Instantiates a new tenant preference entity.
		 * 
		 * @param entity the entity
		 */
		private TenantPreferenceEntity(final TenantPreference entity)
		{
			super(entity);
		}
	}

}
