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
package com.hybris.oms.rest.web.returns;


import com.hybris.oms.api.returns.ReturnFacade;
import com.hybris.oms.domain.returns.Return;
import com.hybris.oms.service.workflow.CoreWorkflowConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

public class ReturnResourceTest {

	@Mock
	private ReturnFacade returnFacade;
	@InjectMocks
	private ReturnsResource returnResource;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetReturn() {
		final Response response = this.returnResource.getReturnById("id");

		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

		// verify if façade was called
		Mockito.verify(this.returnFacade).getReturnById("id");
	}

	@Test
	public void testCreateReturn() {
		final Return aReturn = new Return();
		aReturn.setReturnId("Id");

		final UriBuilder builder = Mockito.mock(UriBuilder.class);
		Mockito.when(builder.path(Mockito.anyString())).thenReturn(builder);

		Mockito.when(returnFacade.createReturn(aReturn)).thenReturn(aReturn);

		// call tested method
		final Response response = this.returnResource.createReturn(aReturn);

		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Assert.assertEquals(aReturn, response.getEntity());

		// verify if façade was called
		Mockito.verify(this.returnFacade).createReturn(aReturn);
	}

	@Test
	public void testUpdateReturn() {
		final Return aReturn = new Return();
		aReturn.setReturnId("ID");

		final UriBuilder builder = Mockito.mock(UriBuilder.class);
		Mockito.when(builder.path(Mockito.anyString())).thenReturn(builder);

		Mockito.when(returnFacade.updateReturn(aReturn)).thenReturn(aReturn);

		// call tested method
		final Response response = this.returnResource.updateReturn(aReturn);

		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Assert.assertEquals(aReturn, response.getEntity());

		// verify if façade was called
		Mockito.verify(this.returnFacade).updateReturn(aReturn);
	}

    @Test
    public void testCancelReturn() {
        final Return aReturn = new Return();
        aReturn.setReturnId("ID");

        final UriBuilder builder = Mockito.mock(UriBuilder.class);
        Mockito.when(builder.path(Mockito.anyString())).thenReturn(builder);

        Mockito.when(returnFacade.cancelReturn("ID")).thenReturn(aReturn);

        // call tested method
        final Response response = this.returnResource.cancelReturn("ID");

        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assert.assertEquals(aReturn, response.getEntity());

        // verify if façade was called
        Mockito.verify(this.returnFacade).cancelReturn("ID");
    }
}
