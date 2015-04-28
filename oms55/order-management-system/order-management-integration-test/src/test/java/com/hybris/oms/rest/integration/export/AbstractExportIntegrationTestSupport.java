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
package com.hybris.oms.rest.integration.export;

import com.hybris.oms.export.api.ExportFacade;
import com.hybris.oms.export.api.ats.AvailabilityToSellDTO;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.LoggerFactory;


public abstract class AbstractExportIntegrationTestSupport<T> extends RestClientIntegrationTest
{
	protected abstract ExportFacade<T> getClient();

	private final static String ATS_FORMULA = "WEB";

	protected static final long NOW = new Date().getTime();

	void clearQueue()
	{
		// clearing queue
		while (!(this.getClient().pollChanges(-1, NOW, ATS_FORMULA).getLatestChange() == null))
		{
			getClient().unmarkSkuForExport(new Date().getTime());
			LoggerFactory.getLogger(this.getClass()).warn("There are still changes in the queue. Dropping them.");
		}
	}

	@Test
	public void testPollWithEmpty()
	{
		final AvailabilityToSellDTO result = this.getClient().pollChanges(500, NOW, ATS_FORMULA);
		Assert.assertTrue(result.getLatestChange() == null);
	}
}
