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
import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.order.PaymentInfo;
import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.domain.shipping.ShippingAndHandling;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.domain.types.Contact;
import com.hybris.oms.domain.types.Price;
import com.hybris.oms.service.itemlocation.builders.AddressBuilder;
import com.hybris.oms.service.managedobjects.basestore.BaseStoreData;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.preference.TenantPreferenceData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.util.OmsTestUtils;

import java.util.Date;

import javax.annotation.Resource;

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
public class OrderReverseConverterTest
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
	private static final String CURRENCY_CODE = "code";
	private static final Double SUBTOTAL_AMOUNT = 5D;
	private static final Double TAX_AMOUNT = 1D;

	private static final String BASESTORE_NAME = "baseStoreName";

	@Autowired
	private Converter<Order, OrderData> orderReverseConverter;

	@Autowired
	private PersistenceManager persistenceManager;
	@Autowired
	private PlatformTransactionManager transactionManager;
	@Resource
	private JdbcPersistenceEngine persistenceEngine;
	@Autowired
	private AggregationService aggregationService;

	private Date date;
	private AddressVT address;

	@Before
	public void setUp()
	{
		// given
		this.date = new Date();
		this.address = AddressBuilder.anAddress().buildAddressVT();

		final BaseStoreData baseStoreData = persistenceManager.create(BaseStoreData.class);
		baseStoreData.setName(BASESTORE_NAME);
		OmsTestUtils.clearAggregates(aggregationService);

		new TransactionTemplate(transactionManager).execute(new TransactionCallback<Void>()
		{
			@Override
			public Void doInTransaction(final TransactionStatus status)
			{
				final TenantPreferenceData tenantPreferenceData6 = persistenceManager.create(TenantPreferenceData.class);
				tenantPreferenceData6.setProperty(TenantPreferenceConstants.PREF_KEY_SHIPPING_WITHIN_DAYS);
				tenantPreferenceData6.setValue("2");

				persistenceManager.flush();
				return null;
			}
		});
	}

	@Transactional
	@Test
	public void reverseConvertingNotFlushedOrderData()
	{
		// given
		final Order order = createOrder();

		// when
		final OrderData orderData = this.orderReverseConverter.convert(order);

		// then
		verifyHomeMadeConverter1(orderData);
		verifyHomeMadeConverter2(orderData);
		verifyHomeMadeConverter3(orderData);
		verifyHomeMadeConverter4(orderData);
	}

	@Transactional
	@Test
	public void testFillingCurrencyCodeFromOrder()
	{
		// given
		final Order order = createOrder();
		// when
		final OrderData orderData = this.orderReverseConverter.convert(order);
		Assert.assertEquals(orderData.getShippingAndHandling().getShippingPrice().getSubTotalValue(), SUBTOTAL_AMOUNT);
		Assert.assertEquals(orderData.getShippingAndHandling().getShippingPrice().getSubTotalCurrencyCode(), CURRENCY_CODE);
		Assert.assertEquals(orderData.getShippingAndHandling().getShippingPrice().getTaxValue(), TAX_AMOUNT);
		Assert.assertEquals(orderData.getShippingAndHandling().getShippingPrice().getTaxCurrencyCode(), CURRENCY_CODE);

	}

	private void verifyHomeMadeConverter4(final OrderData orderData)
	{
		Assert.assertNotNull(orderData.getOrderLines());
		Assert.assertEquals(1, orderData.getOrderLines().size());
		Assert.assertEquals(orderData, orderData.getOrderLines().get(0).getMyOrder());

		Assert.assertNotNull(orderData.getPaymentInfos());
		Assert.assertEquals(1, orderData.getPaymentInfos().size());
		Assert.assertEquals(orderData, orderData.getPaymentInfos().get(0).getMyOrder());

		Assert.assertNotNull(orderData.getShippingAndHandling());
	}

	private void verifyHomeMadeConverter3(final OrderData orderData)
	{
		Assert.assertEquals(OrderTestUtils.ORDER_ID, orderData.getOrderId());
		Assert.assertEquals(CURRENCY_CODE, orderData.getPriorityLevelCode());
		Assert.assertEquals(this.address, orderData.getShippingAddress());
		Assert.assertEquals(NAME, orderData.getShippingFirstName());
		Assert.assertEquals(LAST_NAME, orderData.getShippingLastName());
		Assert.assertEquals(METHOD, orderData.getShippingMethod());
		Assert.assertEquals(TAX_CATEGORY, orderData.getShippingTaxCategory());
		Assert.assertEquals(USERNAME, orderData.getUsername());
		Assert.assertEquals(STRING_NUMBER, orderData.getStockroomLocationIds().get(0));
	}

	private void verifyHomeMadeConverter2(final OrderData orderData)
	{
		Assert.assertEquals(EMAIL_ID, orderData.getEmailid());
		Assert.assertEquals(NAME, orderData.getFirstName());
		Assert.assertEquals(this.date, orderData.getIssueDate());
		Assert.assertEquals(LAST_NAME, orderData.getLastName());
	}

	private void verifyHomeMadeConverter1(final OrderData orderData)
	{
		Assert.assertTrue(orderData.getId() == null);
		Assert.assertEquals(CURRENCY_CODE, orderData.getContact().getChannelCode());
		Assert.assertEquals(MAIL, orderData.getContact().getElectronicMail());
		Assert.assertEquals(NAME, orderData.getContact().getName());
		Assert.assertEquals(NOTE, orderData.getContact().getNote());
		Assert.assertEquals(TELEFAX, orderData.getContact().getTelefax());
		Assert.assertEquals(TELEPHONE, orderData.getContact().getTelephone());
		Assert.assertEquals(STRING_NUMBER, orderData.getContact().getValue());
		Assert.assertEquals(LOCALE, orderData.getCustomerLocale());
		Assert.assertEquals(CURRENCY_CODE, orderData.getCurrencyCode());
		Assert.assertNotNull(orderData.getBaseStore());
		Assert.assertEquals(BASESTORE_NAME, orderData.getBaseStore().getName());
	}

	private Order createOrder()
	{
		final Order order = new Order();
		order.setCurrencyCode(CURRENCY_CODE);
		order.setContact(new Contact(MAIL, NAME, NOTE, TELEFAX, TELEPHONE, CURRENCY_CODE, STRING_NUMBER));
		order.setCustomerLocale(LOCALE);
		order.setEmailid(EMAIL_ID);
		order.setFirstName(NAME);
		order.setIssueDate(this.date);
		order.setLastName(LAST_NAME);
		order.setOrderId(OrderTestUtils.ORDER_ID);
		order.setPriorityLevelCode(CURRENCY_CODE);
		order.setShippingAddress(AddressBuilder.anAddress().buildAddressDTO());
		order.setShippingFirstName(NAME);
		order.setShippingLastName("lastName");
		order.setShippingMethod(METHOD);
		order.setShippingTaxCategory(TAX_CATEGORY);
		order.setUsername(USERNAME);
		order.setLocationIds(ImmutableList.of(STRING_NUMBER));

		order.setOrderLines(ImmutableList.of(new OrderLine()));
		order.setPaymentInfos(ImmutableList.of(new PaymentInfo()));
		order.setShippingAndHandling(new ShippingAndHandling());

		order.getShippingAndHandling().setShippingPrice(new Price());
		order.getShippingAndHandling().getShippingPrice().setSubTotal(new Amount());
		order.getShippingAndHandling().getShippingPrice().getSubTotal().setValue(SUBTOTAL_AMOUNT);
		order.getShippingAndHandling().getShippingPrice().setTax(new Amount());
		order.getShippingAndHandling().getShippingPrice().getTax().setValue(TAX_AMOUNT);
		order.setBaseStoreName(BASESTORE_NAME);

		return order;
	}

	@After
	public void tearDown()
	{
		OmsTestUtils.cleanUp(persistenceEngine);
	}

}
