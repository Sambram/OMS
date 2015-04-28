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
package com.hybris.oms.ui.facade.conversion.impl.order;

import com.hybris.commons.conversion.Converter;
import com.hybris.kernel.api.AggregationService;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.engine.jdbc.impl.JdbcPersistenceEngine;
import com.hybris.oms.facade.conversion.impl.order.OrderTestUtils;
import com.hybris.oms.service.itemlocation.builders.AddressBuilder;
import com.hybris.oms.service.managedobjects.basestore.BaseStoreData;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;
import com.hybris.oms.service.managedobjects.preference.TenantPreferenceData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.managedobjects.types.ContactVT;
import com.hybris.oms.service.util.OmsTestUtils;
import com.hybris.oms.ui.api.order.UIOrder;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.collect.ImmutableList;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-ui-facade-spring-test.xml"})
public class UiOrderConverterTest
{
	private static final String USERNAME = "username";
	private static final String TAX_CATEGORY = "taxCategory";
	private static final String METHOD = "method";
	private static final String LAST_NAME = "lastName";
	private static final String EMAIL_ID = "eid";
	private static final String LOCALE = "locale";
	private static final String STRING_NUMBER = "1";
	private static final String TELEPHONE = "telephone";
	private static final String TELEFAX = "telefax";
	private static final String NOTE = "note";
	private static final String NAME = "name";
	private static final String MAIL = "mail";
	private static final String CODE = "code";
	private static final String BASESTORE_NAME = "baseStoreName";

	@Autowired
	@Qualifier("uIOrderConverter")
	private Converter<OrderData, UIOrder> uIorderConverter;

	@Autowired
	private PersistenceManager persistenceManager;
	@Autowired
	private PlatformTransactionManager transactionManager;
	@Resource
	private JdbcPersistenceEngine persistenceEngine;
	@Autowired
	private AggregationService aggregationService;
	private OrderTestUtils orderTestUtils;
	private OrderData orderData;
	private Date date;
	private AddressVT address;

	@Before
	public void setUp()
	{
		// given
		this.date = new Date();
		this.address = AddressBuilder.anAddress().buildAddressVT();
		this.orderTestUtils = new OrderTestUtils();
		this.orderTestUtils.createObjects(this.persistenceManager);
		this.orderData = this.prepareData();

		OmsTestUtils.clearAggregates(aggregationService);

		new TransactionTemplate(transactionManager).execute(new TransactionCallback<Void>()
				{
			@Override
			public Void doInTransaction(final TransactionStatus status)
			{
				final TenantPreferenceData tenantPreferenceData = persistenceManager.create(TenantPreferenceData.class);
				tenantPreferenceData.setProperty("olqstatus.canceled");
				tenantPreferenceData.setValue("CANCELLED");

				final TenantPreferenceData tenantPreferenceData2 = persistenceManager.create(TenantPreferenceData.class);
				tenantPreferenceData2.setProperty("olqstatus.tax_invoiced");
				tenantPreferenceData2.setValue("TAX_INVOICED");

				final TenantPreferenceData tenantPreferenceData3 = persistenceManager.create(TenantPreferenceData.class);
				tenantPreferenceData3.setProperty("olqstatus.payment_captured");
				tenantPreferenceData3.setValue("PAYMENT_CAPTURED");

				final TenantPreferenceData tenantPreferenceData4 = persistenceManager.create(TenantPreferenceData.class);
				tenantPreferenceData4.setProperty("olqstatus.shipped");
				tenantPreferenceData4.setValue("SHIPPED");

				final TenantPreferenceData tenantPreferenceData5 = persistenceManager.create(TenantPreferenceData.class);
				tenantPreferenceData5.setProperty("olqstatus.manual_declined");
				tenantPreferenceData5.setValue("MANUAL_DECLINED");

				final OrderLineQuantityStatusData orderLineQuantityStatusData = persistenceManager
						.create(OrderLineQuantityStatusData.class);
				orderLineQuantityStatusData.setStatusCode("CANCELLED");

				final OrderLineQuantityStatusData orderLineQuantityStatusData2 = persistenceManager
						.create(OrderLineQuantityStatusData.class);
				orderLineQuantityStatusData2.setStatusCode("TAX_INVOICED");

				final OrderLineQuantityStatusData orderLineQuantityStatusData3 = persistenceManager
						.create(OrderLineQuantityStatusData.class);
				orderLineQuantityStatusData3.setStatusCode("SHIPPED");

				final OrderLineQuantityStatusData orderLineQuantityStatusData4 = persistenceManager
						.create(OrderLineQuantityStatusData.class);
				orderLineQuantityStatusData4.setStatusCode("PAYMENT_CAPTURED");

				final OrderLineQuantityStatusData orderLineQuantityStatusData5 = persistenceManager
						.create(OrderLineQuantityStatusData.class);
				orderLineQuantityStatusData5.setStatusCode("MANUAL_DECLINED");

				persistenceManager.flush();
				return null;
			}
				});

	}

	@After
	public void tearDown()
	{
		OmsTestUtils.cleanUp(persistenceEngine);
	}

	@Transactional
	@Test
	public void convertingNotFlushedOrderData()
	{
		// when
		final UIOrder order = this.uIorderConverter.convert(this.orderData);

		// then
		assertValid(order);
	}

	@Transactional
	@Test
	public void convertingFlushedOrderData()
	{
		this.persistenceManager.flush();

		// when
		final UIOrder order = this.uIorderConverter.convert(this.orderData);

		// then
		assertValid(order);
	}

	private OrderData prepareData()
	{
		final BaseStoreData baseStoreData = persistenceManager.create(BaseStoreData.class);
		baseStoreData.setName(BASESTORE_NAME);

		final OrderData resultData = this.orderTestUtils.getOrderData();
		resultData.setCurrencyCode(CODE);
		resultData.setContact(new ContactVT(CODE, MAIL, NAME, NOTE, TELEFAX, TELEPHONE, STRING_NUMBER));
		resultData.setCustomerLocale(LOCALE);
		resultData.setEmailid(EMAIL_ID);
		resultData.setFirstName(NAME);
		resultData.setIssueDate(this.date);
		resultData.setLastName(LAST_NAME);
		resultData.setOrderId(OrderTestUtils.ORDER_ID);
		resultData.setPriorityLevelCode(CODE);
		resultData.setShippingAddress(this.address);
		resultData.setShippingFirstName(NAME);
		resultData.setShippingLastName(LAST_NAME);
		resultData.setShippingMethod(METHOD);
		resultData.setShippingTaxCategory(TAX_CATEGORY);
		resultData.setUsername(USERNAME);
		resultData.setStockroomLocationIds(ImmutableList.of(STRING_NUMBER));
		resultData.setBaseStore(baseStoreData);

		return resultData;
	}

	private void assertValid(final UIOrder order)
	{
		Assert.assertEquals(CODE, order.getCurrencyCode());
		Assert.assertEquals(LOCALE, order.getCustomerLocale());
		Assert.assertEquals(EMAIL_ID, order.getEmailid());
		Assert.assertEquals(NAME, order.getFirstName());
		Assert.assertEquals(this.date, order.getIssueDate());
		Assert.assertEquals(LAST_NAME, order.getLastName());
		Assert.assertEquals(OrderTestUtils.ORDER_ID, order.getOrderId());
		Assert.assertEquals(CODE, order.getPriorityLevelCode());
		Assert.assertEquals(NAME, order.getShippingFirstName());
		Assert.assertEquals(LAST_NAME, order.getShippingLastName());
		Assert.assertEquals(METHOD, order.getShippingMethod());
		Assert.assertEquals(TAX_CATEGORY, order.getShippingTaxCategory());
		Assert.assertEquals(USERNAME, order.getUsername());
		Assert.assertEquals(ImmutableList.of(STRING_NUMBER), order.getLocationIds());
		Assert.assertEquals(BASESTORE_NAME, order.getBaseStoreName());
	}

}
