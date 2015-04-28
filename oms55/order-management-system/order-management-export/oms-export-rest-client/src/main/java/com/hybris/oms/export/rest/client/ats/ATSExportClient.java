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
package com.hybris.oms.export.rest.client.ats;

import com.hybris.commons.client.RestCallBuilder;
import com.hybris.commons.client.RestResponseException;
import com.hybris.oms.export.api.ats.ATSExportFacade;
import com.hybris.oms.export.api.ats.AvailabilityToSellDTO;
import com.hybris.oms.export.rest.client.AbstractExportClient;

import com.sun.jersey.api.client.GenericType;


/**
 * Client implementation for {@link ATSExportFacade} remoting all calls per REST.
 */
public class ATSExportClient extends AbstractExportClient<AvailabilityToSellDTO> implements ATSExportFacade
{
	@Override
	public AvailabilityToSellDTO pollChanges(final int amountLimit, final Long pollingTime, final String atsId)
	{
		try
		{
			final RestCallBuilder call = getClient().call(this.getResourceRoot() + "/poll");
			if (amountLimit != 0)
			{
				call.queryParam("limit", Integer.toString(amountLimit));
			}
			if (pollingTime == null)
			{
				throw new RuntimeException("Param latestChange is required!");
			}
			call.queryParam("pollingTime", Long.toString(pollingTime));
			call.queryParam("atsId", atsId);
			return call.get(new GenericType<AvailabilityToSellDTO>()
					{// empty
					}).result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap();
			return null;
		}
	}

	@Override
	public void unmarkSkuForExport(final Long latestChange)
	{
		try
		{
			// final RestCallBuilder call = getClient().call(getResourceRoot() + "/" +latestChange);
			final RestCallBuilder call = getClient().call(getResourceRoot());
			call.queryParam("latestChange", Long.toString(latestChange));

			call.delete();
		}
		catch (final RestResponseException e)
		{
			e.unwrap();
		}
	}

	@Override
	protected String getResourceRoot()
	{
		return "ats/export";
	}

}
