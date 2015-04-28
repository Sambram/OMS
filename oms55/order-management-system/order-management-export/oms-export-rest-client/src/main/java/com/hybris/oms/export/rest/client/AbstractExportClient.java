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
package com.hybris.oms.export.rest.client;

import com.hybris.commons.client.GenericRestClient;
import com.hybris.commons.client.RestResponseException;
import com.hybris.oms.export.api.ExportFacade;
import com.hybris.oms.export.api.ExportTriggerResultDTO;
import com.hybris.oms.export.api.ats.AvailabilityToSellDTO;


/**
 * Abstract client implementation for {@link ExportFacade} remoting all calls per REST.
 */
public abstract class AbstractExportClient<T> implements ExportFacade<T>
{
	private GenericRestClient client;

	protected abstract String getResourceRoot();

	@Override
	public int triggerFullExport()
	{
		try
		{
			return client.call(this.getResourceRoot() + "/export").post(ExportTriggerResultDTO.class).result().getAmount();
		}
		catch (final RestResponseException e)
		{
			e.unwrap();
			return -1;
		}
	}

	@Override
	public void triggerExport(final String identifier1, final String identifier2)
	{
		if (identifier1 == null)
		{
			throw new IllegalArgumentException("Identifiers to export must not be null.");
		}
		if (identifier2 == null)
		{
			throw new IllegalArgumentException("Identifiers to export must not be null.");
		}
		try
		{
			client.call(this.getResourceRoot() + "/export/%s/%s", identifier1, identifier2).post().result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap();
		}
	}

	@Override
	public abstract AvailabilityToSellDTO pollChanges(final int amountLimit, final Long pollingTime, final String atsId);

	public void setClient(final GenericRestClient client)
	{
		this.client = client;
	}

	protected GenericRestClient getClient()
	{
		return client;
	}
}
