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
package com.hybris.oms.export.rest.web.ats;

import com.hybris.commons.web.authorization.RoleConstants;
import com.hybris.oms.export.api.ats.ATSExportFacade;
import com.hybris.oms.export.api.ats.AvailabilityToSellDTO;
import com.hybris.oms.export.rest.web.ExportResource;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;


/**
 * Resource exposing the {@link ATSExportFacade} per REST.
 */
@Component
@Path("/ats/export")
@Secured({RoleConstants.ADMIN, RoleConstants.ACCELERATOR})
public class ATSExportResource extends ExportResource<AvailabilityToSellDTO>
{
	@Autowired
	private ATSExportFacade atsExportFacade;

	@Override
	protected ATSExportFacade getExportFacade()
	{
		return this.atsExportFacade;
	}
}
