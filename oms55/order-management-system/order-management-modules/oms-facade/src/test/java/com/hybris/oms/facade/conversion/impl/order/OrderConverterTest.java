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
package com.hybris.oms.facade.conversion.impl.order;

import com.hybris.commons.conversion.Converter;
import com.hybris.kernel.api.AggregationService;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.engine.jdbc.impl.JdbcPersistenceEngine;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.domain.types.Contact;
import com.hybris.oms.service.itemlocation.builders.AddressBuilder;
import com.hybris.oms.service.managedobjects.basestore.BaseStoreData;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;
import com.hybris.oms.service.managedobjects.preference.TenantPreferenceData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.managedobjects.types.ContactVT;
import com.hybris.oms.service.util.OmsTestUtils;

import java.util.Date;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.collect.ImmutableList;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class OrderConverterTest
{
	private static final String STATE = "state";
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
	private Converter<OrderData, Order> orderConverter;
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
				tenantPreferenceData.setProperty(TenantPreferenceConstants.PREF_KEY_OLQSTATUS_CANCELED);
				tenantPreferenceData.setValue("CANCELLED");

				final TenantPreferenceData tenantPreferenceData2 = persistenceManager.create(TenantPreferenceData.class);
				tenantPreferenceData2.setProperty(TenantPreferenceConstants.PREF_KEY_OLQSTATUS_TAX_INVOICED);
				tenantPreferenceData2.setValue("TAX_INVOICED");

				final TenantPreferenceData tenantPreferenceData3 = persistenceManager.create(TenantPreferenceData.class);
				tenantPreferenceData3.setProperty(TenantPreferenceConstants.PREF_KEY_OLQSTATUS_PAYMENT_CAPTURED);
				tenantPreferenceData3.setValue("PAYMENT_CAPTURED");

				final TenantPreferenceData tenantPreferenceData4 = persistenceManager.create(TenantPreferenceData.class);
				tenantPreferenceData4.setProperty(TenantPreferenceConstants.PREF_KEY_OLQSTATUS_SHIPPED);
				tenantPreferenceData4.setValue("SHIPPED");

				final TenantPreferenceData tenantPreferenceData5 = persistenceManager.create(TenantPreferenceData.class);
				tenantPreferenceData5.setProperty(TenantPreferenceConstants.PREF_KEY_OLQSTATUS_MANUAL_DECLINED);
				tenantPreferenceData5.setValue("MANUAL_DECLINED");

				final TenantPreferenceData tenantPreferenceData6 = persistenceManager.create(TenantPreferenceData.class);
				tenantPreferenceData6.setProperty(TenantPreferenceConstants.PREF_KEY_SHIPPING_WITHIN_DAYS);
				tenantPreferenceData6.setValue("2");

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
	public void testXmlMarshalling() throws JAXBException
	{
		final JAXBContext jaxbContext = JAXBContext.newInstance(Order.class);
		final Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(this.orderConverter.convert(this.orderData), System.out);
	}

	@Transactional
	@Test
	public void convertingNotFlushedOrderData()
	{
		// when
		final Order order = this.orderConverter.convert(this.orderData);

		// then
		assertValid(order);
	}

	@Transactional
	@Test
	public void convertingFlushedOrderData()
	{
		this.persistenceManager.flush();

		// when
		final Order order = this.orderConverter.convert(this.orderData);

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

	private void assertValid(final Order order)
	{
		contactAssertions(order.getContact());
		Assert.assertEquals(STATE, order.getState());
		Assert.assertEquals(CODE, order.getCurrencyCode());
		Assert.assertEquals(LOCALE, order.getCustomerLocale());
		Assert.assertEquals(EMAIL_ID, order.getEmailid());
		Assert.assertEquals(NAME, order.getFirstName());
		Assert.assertEquals(this.date, order.getIssueDate());
		Assert.assertEquals(LAST_NAME, order.getLastName());
		Assert.assertEquals(OrderTestUtils.ORDER_ID, order.getOrderId());
		Assert.assertEquals(CODE, order.getPriorityLevelCode());
		Assert.assertEquals(AddressBuilder.anAddress().buildAddressDTO(), order.getShippingAddress());
		Assert.assertEquals(NAME, order.getShippingFirstName());
		Assert.assertEquals(LAST_NAME, order.getShippingLastName());
		Assert.assertEquals(METHOD, order.getShippingMethod());
		Assert.assertEquals(TAX_CATEGORY, order.getShippingTaxCategory());
		Assert.assertEquals(USERNAME, order.getUsername());
		Assert.assertEquals(ImmutableList.of(STRING_NUMBER), order.getLocationIds());

		Assert.assertNotNull(order.getOrderLines());
		Assert.assertEquals(1, order.getOrderLines().size());
		Assert.assertNotNull(order.getPaymentInfos());
		Assert.assertEquals(1, order.getPaymentInfos().size());
		Assert.assertNotNull(order.getShippingAndHandling());

		Assert.assertEquals(BASESTORE_NAME, order.getBaseStoreName());

	}

	private void contactAssertions(final Contact contact)
	{
		Assert.assertEquals(CODE, contact.getChannelCode());
		Assert.assertEquals(MAIL, contact.getElectronicMail());
		Assert.assertEquals(NAME, contact.getName());
		Assert.assertEquals(NOTE, contact.getNote());
		Assert.assertEquals(TELEFAX, contact.getTelefax());
		Assert.assertEquals(TELEPHONE, contact.getTelephone());
		Assert.assertEquals(STRING_NUMBER, contact.getValue());
	}

}
