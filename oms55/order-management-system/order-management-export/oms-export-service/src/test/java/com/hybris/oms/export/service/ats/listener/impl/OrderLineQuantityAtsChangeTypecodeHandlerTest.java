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
package com.hybris.oms.export.service.ats.listener.impl;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import com.hybris.kernel.api.HybrisId;
import com.hybris.kernel.api.ManagedObject;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.oms.export.service.ats.listener.AtsChangeListenerException;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class OrderLineQuantityAtsChangeTypecodeHandlerTest
{

	private static final String SKU = "ABC";

	@InjectMocks
	private final OrderLineQuantityAtsChangeTypecodeHandler atsChangeTypecodeHandler = new OrderLineQuantityAtsChangeTypecodeHandler();

	@Mock
	private PersistenceManager persistenceManager;

	private HybrisId id;
	@Mock
	private OrderLineData orderLineData;

	@Before
	public void setUp()
	{
		id = HybrisId.valueOf("tenant|typecode|value");
		when(persistenceManager.get(id)).thenReturn(orderLineData);
	}

	@Test
	public void shouldGetSkuFromOrderLine() throws AtsChangeListenerException
	{
		// GIVEN
		final Map<String, Object> values = getValues();
		when(orderLineData.getSkuId()).
				thenReturn(SKU);

		// WHEN
		final String sku = atsChangeTypecodeHandler.getSku(values);

		// THEN
		assertThat(sku, equalTo(SKU));
	}


	@Test(expected = AtsChangeListenerException.class)
	public void shouldNotFindSku_ManagedObjectNotFoundException() throws AtsChangeListenerException
	{
		// GIVEN
		final Map<String, Object> values = getValues();
		when(persistenceManager.get(id)).
				thenThrow(new ManagedObjectNotFoundException());

		// WHEN
		atsChangeTypecodeHandler.getSku(values);

		// THEN
		fail(); //AtsChangeListenerException must be thrown
	}

	private Map<String, Object> getValues()
	{
		final Map<String, Object> map = new HashMap<>();
		map.put(ManagedObject.MODIFIEDTIME.name(), new Date());
		map.put(OrderLineQuantityData.ORDERLINE.name(), id);
		return map;
	}
}
