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

import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.basestore.BaseStoreFacade;
import com.hybris.oms.domain.basestore.BaseStore;
import com.hybris.oms.domain.basestore.BaseStoreQueryObject;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class BaseStoreResourceTest
{
	@Mock
	private BaseStoreFacade baseStoreFacade;

	@InjectMocks
	private BaseStoreResource baseStoreResource;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetBaseStore()
	{
		// call tested method
		final Response response = this.baseStoreResource.getBaseStoreByName("name");

		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

		// verify if façade was called
		Mockito.verify(this.baseStoreFacade).getBaseStoreByName("name");
	}


	@Test
	public void testGetBaseStores()
	{
		@SuppressWarnings("unchecked")
		final Pageable<BaseStore> pagableBaseStore = Mockito.mock(Pageable.class);
		Mockito.when(baseStoreFacade.findAllBaseStoresByQuery(Mockito.any(BaseStoreQueryObject.class))).thenReturn(pagableBaseStore);
		// call tested method
		final Response response = this.baseStoreResource.findAllBaseStoresByQuery(1, 1);

		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

		// verify if façade was called
		Mockito.verify(this.baseStoreFacade).findAllBaseStoresByQuery(Mockito.any(BaseStoreQueryObject.class));
	}

	@Test
	public void testCreateBaseStore()
	{
		final BaseStore baseStore = new BaseStore();
		baseStore.setName("name");

		final UriBuilder builder = Mockito.mock(UriBuilder.class);
		Mockito.when(builder.path(Mockito.anyString())).thenReturn(builder);

		Mockito.when(baseStoreFacade.createBaseStore(baseStore)).thenReturn(baseStore);

		// call tested method
		final Response response = this.baseStoreResource.createBaseStore(baseStore);

		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Assert.assertEquals(baseStore, response.getEntity());

		// verify if façade was called
		Mockito.verify(this.baseStoreFacade).createBaseStore(baseStore);
	}

	@Test
	public void testUpdateBaseStore()
	{
		final BaseStore baseStore = new BaseStore();
		baseStore.setName("name");

		final UriBuilder builder = Mockito.mock(UriBuilder.class);
		Mockito.when(builder.path(Mockito.anyString())).thenReturn(builder);

		Mockito.when(baseStoreFacade.updateBaseStore(baseStore)).thenReturn(baseStore);

		// call tested method
		final Response response = this.baseStoreResource.updateBaseStore(baseStore);

		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Assert.assertEquals(baseStore, response.getEntity());

		// verify if façade was called
		Mockito.verify(this.baseStoreFacade).updateBaseStore(baseStore);
	}

}
