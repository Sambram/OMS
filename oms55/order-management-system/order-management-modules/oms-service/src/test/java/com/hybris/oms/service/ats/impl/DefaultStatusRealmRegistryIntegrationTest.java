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
package com.hybris.oms.service.ats.impl;

import com.hybris.oms.service.ats.StatusRealm;
import com.hybris.oms.service.inventory.impl.dataaccess.AggFutureItemQuantityBySkuLocationStatus;
import com.hybris.oms.service.inventory.impl.dataaccess.AggFutureItemQuantityBySkuStatus;
import com.hybris.oms.service.inventory.impl.dataaccess.AggItemQuantityByItemIdLocationStatus;
import com.hybris.oms.service.inventory.impl.dataaccess.AggItemQuantityByItemIdStatus;
import com.hybris.oms.service.order.impl.dataaccess.AggOrderLineQuantityBySkuLocationStatus;
import com.hybris.oms.service.order.impl.dataaccess.AggOrderLineQuantityBySkuStatus;
import com.hybris.oms.service.order.impl.dataaccess.AggQuantityUnassignedBySku;

import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Integration test for {@link DefaultStatusRealmRegistry}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-service-spring-test.xml"})
public class DefaultStatusRealmRegistryIntegrationTest
{
	@Autowired
	private DefaultStatusRealmRegistry registry;

	@Test
	public void shouldReturnAggregateClass()
	{
		Assert.assertEquals(AggItemQuantityByItemIdStatus.class, registry.getAggregateClassForRealm(StatusRealm.INVENTORY, true));
		Assert.assertEquals(AggItemQuantityByItemIdLocationStatus.class,
				registry.getAggregateClassForRealm(StatusRealm.INVENTORY, false));
		Assert.assertEquals(AggOrderLineQuantityBySkuStatus.class, registry.getAggregateClassForRealm(StatusRealm.ORDER, true));
		Assert.assertEquals(AggOrderLineQuantityBySkuLocationStatus.class,
				registry.getAggregateClassForRealm(StatusRealm.ORDER, false));
		Assert.assertEquals(AggFutureItemQuantityBySkuStatus.class, registry.getAggregateClassForRealm(StatusRealm.FUTUR, true));
		Assert.assertEquals(AggFutureItemQuantityBySkuLocationStatus.class,
				registry.getAggregateClassForRealm(StatusRealm.FUTUR, false));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldRejectInvalidRealm()
	{
		registry.getAggregateClassForRealm(null, true);
	}

	@Test
	public void shouldReturnClassForUnassignedQuantity()
	{
		Assert.assertEquals(AggQuantityUnassignedBySku.class, registry.getAggClassForUnassignedQuantity());
	}

	@Test
	public void shouldReturnRealmForAggregateClass()
	{
		Assert.assertEquals(StatusRealm.INVENTORY, registry.getRealmForAggregateClass(AggItemQuantityByItemIdStatus.class));
		Assert.assertEquals(StatusRealm.INVENTORY, registry.getRealmForAggregateClass(AggItemQuantityByItemIdLocationStatus.class));
		Assert.assertEquals(StatusRealm.ORDER, registry.getRealmForAggregateClass(AggOrderLineQuantityBySkuLocationStatus.class));
		Assert.assertEquals(StatusRealm.ORDER, registry.getRealmForAggregateClass(AggOrderLineQuantityBySkuStatus.class));
		Assert.assertEquals(StatusRealm.FUTUR, registry.getRealmForAggregateClass(AggFutureItemQuantityBySkuLocationStatus.class));
		Assert.assertEquals(StatusRealm.FUTUR, registry.getRealmForAggregateClass(AggFutureItemQuantityBySkuStatus.class));
		Assert.assertNull(registry.getRealmForAggregateClass(AggQuantityUnassignedBySku.class));
	}

	@Test
	public void shouldReturnRealmForPrefix()
	{
		Assert.assertEquals(StatusRealm.INVENTORY, registry.getRealmForPrefix("I"));
		Assert.assertEquals(StatusRealm.ORDER, registry.getRealmForPrefix("O"));
		Assert.assertEquals(StatusRealm.FUTUR, registry.getRealmForPrefix("F"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldRejectInvalidPrefix()
	{
		registry.getRealmForPrefix("NA");
	}

	@Test
	public void shouldReturnRegisteredRealms()
	{
		Assertions.assertThat(registry.getRegisteredRealms()).containsOnly(StatusRealm.INVENTORY, StatusRealm.ORDER,
				StatusRealm.FUTUR);
	}

}
