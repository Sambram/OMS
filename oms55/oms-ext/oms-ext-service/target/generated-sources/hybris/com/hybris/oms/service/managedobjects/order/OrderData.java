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
 
package com.hybris.oms.service.managedobjects.order;

import com.hybris.kernel.api.*;

import com.hybris.kernel.api.ManagedObject;

    
/**
 * Generated managedobject class for type OrderData first defined at extension <code>order</code>.
 * <p/>
 * Contains data on when the order was placed.
 */
public interface OrderData extends ManagedObject, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "OrderData";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderData.username</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderData, String> USERNAME = new AttributeType<>("username");
	/** <i>Generated constant</i> - Attribute key of <code>OrderData.shippingAndHandling</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderData, com.hybris.oms.service.managedobjects.shipment.ShippingAndHandlingData> SHIPPINGANDHANDLING = new AttributeType<>("shippingAndHandling");
	/** <i>Generated constant</i> - Attribute key of <code>OrderData.shippingTaxCategory</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderData, String> SHIPPINGTAXCATEGORY = new AttributeType<>("shippingTaxCategory");
	/** <i>Generated constant</i> - Attribute key of <code>OrderData.priorityLevelCode</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderData, String> PRIORITYLEVELCODE = new AttributeType<>("priorityLevelCode");
	/** <i>Generated constant</i> - Attribute key of <code>OrderData.orderLines</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderData, java.util.List<com.hybris.oms.service.managedobjects.order.OrderLineData>> ORDERLINES = new AttributeType<>("orderLines");
	/** <i>Generated constant</i> - Attribute key of <code>OrderData.version</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderData, Long> VERSION = new AttributeType<>("version");
	/** <i>Generated constant</i> - Attribute key of <code>OrderData.shippingFirstName</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderData, String> SHIPPINGFIRSTNAME = new AttributeType<>("shippingFirstName");
	/** <i>Generated constant</i> - Attribute key of <code>OrderData.orderId</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderData, String> ORDERID = new AttributeType<>("orderId");
	/** <i>Generated constant</i> - Attribute key of <code>OrderData.scheduledShippingDate</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderData, java.util.Date> SCHEDULEDSHIPPINGDATE = new AttributeType<>("scheduledShippingDate");
	/** <i>Generated constant</i> - Attribute key of <code>OrderData.baseStore</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderData, com.hybris.oms.service.managedobjects.basestore.BaseStoreData> BASESTORE = new AttributeType<>("baseStore");
	/** <i>Generated constant</i> - Attribute key of <code>OrderData.issueDate</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderData, java.util.Date> ISSUEDATE = new AttributeType<>("issueDate");
	/** <i>Generated constant</i> - Attribute key of <code>OrderData.currencyCode</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderData, String> CURRENCYCODE = new AttributeType<>("currencyCode");
	/** <i>Generated constant</i> - Attribute key of <code>OrderData.shippingMethod</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderData, String> SHIPPINGMETHOD = new AttributeType<>("shippingMethod");
	/** <i>Generated constant</i> - Attribute key of <code>OrderData.emailid</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderData, String> EMAILID = new AttributeType<>("emailid");
	/** <i>Generated constant</i> - Attribute key of <code>OrderData.shippingAddress</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderData, com.hybris.oms.service.managedobjects.types.AddressVT> SHIPPINGADDRESS = new AttributeType<>("shippingAddress");
	AttributeType<OrderData, String> SHIPPINGADDRESS_ADDRESSLINE1 = new AttributeType<>("shippingAddress#addressLine1");
	AttributeType<OrderData, String> SHIPPINGADDRESS_ADDRESSLINE2 = new AttributeType<>("shippingAddress#addressLine2");
	AttributeType<OrderData, String> SHIPPINGADDRESS_CITYNAME = new AttributeType<>("shippingAddress#cityName");
	AttributeType<OrderData, String> SHIPPINGADDRESS_COUNTRYSUBENTITY = new AttributeType<>("shippingAddress#countrySubentity");
	AttributeType<OrderData, String> SHIPPINGADDRESS_POSTALZONE = new AttributeType<>("shippingAddress#postalZone");
	AttributeType<OrderData, Double> SHIPPINGADDRESS_LATITUDEVALUE = new AttributeType<>("shippingAddress#latitudeValue");
	AttributeType<OrderData, Double> SHIPPINGADDRESS_LONGITUDEVALUE = new AttributeType<>("shippingAddress#longitudeValue");
	AttributeType<OrderData, String> SHIPPINGADDRESS_COUNTRYISO3166ALPHA2CODE = new AttributeType<>("shippingAddress#countryIso3166Alpha2Code");
	AttributeType<OrderData, String> SHIPPINGADDRESS_COUNTRYNAME = new AttributeType<>("shippingAddress#countryName");
	AttributeType<OrderData, String> SHIPPINGADDRESS_NAME = new AttributeType<>("shippingAddress#name");
	AttributeType<OrderData, String> SHIPPINGADDRESS_PHONENUMBER = new AttributeType<>("shippingAddress#phoneNumber");
	/** <i>Generated constant</i> - Attribute key of <code>OrderData.state</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderData, String> STATE = new AttributeType<>("state");
	/** <i>Generated constant</i> - Attribute key of <code>OrderData.lastName</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderData, String> LASTNAME = new AttributeType<>("lastName");
	/** <i>Generated constant</i> - Attribute key of <code>OrderData.contact</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderData, com.hybris.oms.service.managedobjects.types.ContactVT> CONTACT = new AttributeType<>("contact");
	AttributeType<OrderData, String> CONTACT_CHANNELCODE = new AttributeType<>("contact#channelCode");
	AttributeType<OrderData, String> CONTACT_ELECTRONICMAIL = new AttributeType<>("contact#electronicMail");
	AttributeType<OrderData, String> CONTACT_NAME = new AttributeType<>("contact#name");
	AttributeType<OrderData, String> CONTACT_NOTE = new AttributeType<>("contact#note");
	AttributeType<OrderData, String> CONTACT_TELEFAX = new AttributeType<>("contact#telefax");
	AttributeType<OrderData, String> CONTACT_TELEPHONE = new AttributeType<>("contact#telephone");
	AttributeType<OrderData, String> CONTACT_VALUE = new AttributeType<>("contact#value");
	/** <i>Generated constant</i> - Attribute key of <code>OrderData.stockroomLocationIds</code> attribute defined at extension <code>order</code>. */
	CollectionAttributeType<OrderData, String> STOCKROOMLOCATIONIDS = new CollectionAttributeType<>("stockroomLocationIds");
	/** <i>Generated constant</i> - Attribute key of <code>OrderData.shippingLastName</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderData, String> SHIPPINGLASTNAME = new AttributeType<>("shippingLastName");
	/** <i>Generated constant</i> - Attribute key of <code>OrderData.firstName</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderData, String> FIRSTNAME = new AttributeType<>("firstName");
	/** <i>Generated constant</i> - Attribute key of <code>OrderData.customerLocale</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderData, String> CUSTOMERLOCALE = new AttributeType<>("customerLocale");
	/** <i>Generated constant</i> - Attribute key of <code>OrderData.paymentInfos</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderData, java.util.List<com.hybris.oms.service.managedobjects.order.PaymentInfoData>> PAYMENTINFOS = new AttributeType<>("paymentInfos");

	/** <i>Generated constant</i> - Index of <code>OrderData</code> type defined at extension <code>order</code>. */
	UniqueIndexSingle<OrderData, String> UX_ORDERS_ORDERID = new UniqueIndexSingle<>("UX_orders_orderId", OrderData.class);

	/**
	 * <i>Generated method</i> - Getter of the <code>OrderData.state</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Current order state.
	 * 
	 * @return the state
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getState();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderData.contact</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Order contact.
	 * 
	 * @return the contact
	 */
	com.hybris.oms.service.managedobjects.types.ContactVT getContact();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderData.currencyCode</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Currency code.
	 * 
	 * @return the currencyCode
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getCurrencyCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderData.customerLocale</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * a string representation of a Locale object, consisting of language, country, etc.
	 * 
	 * @return the customerLocale
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getCustomerLocale();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderData.emailid</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Customer's Email Id.
	 * 
	 * @return the emailid
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getEmailid();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderData.firstName</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Customer's First Name.
	 * 
	 * @return the firstName
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getFirstName();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderData.issueDate</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Order issue Date.
	 * 
	 * @return the issueDate
	 */
	@javax.validation.constraints.NotNull
	java.util.Date getIssueDate();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderData.scheduledShippingDate</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Order Scheduled Shipping Date.
	 * 
	 * @return the scheduledShippingDate
	 */
	java.util.Date getScheduledShippingDate();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderData.lastName</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Customer's Last name.
	 * 
	 * @return the lastName
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getLastName();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderData.orderId</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Unique business code of the order.
	 * 
	 * @return the orderId
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getOrderId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderData.orderLines</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Order line containing item and order line quantities.
	 * 
	 * @return the orderLines
	 */
	@org.apache.bval.constraints.NotEmpty
	java.util.List<com.hybris.oms.service.managedobjects.order.OrderLineData> getOrderLines();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderData.paymentInfos</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Order containing list of payment information.
	 * 
	 * @return the paymentInfos
	 */
	@org.apache.bval.constraints.NotEmpty
	java.util.List<com.hybris.oms.service.managedobjects.order.PaymentInfoData> getPaymentInfos();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderData.priorityLevelCode</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * .
	 * 
	 * @return the priorityLevelCode
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getPriorityLevelCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderData.shippingAddress</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Order shipping address.
	 * 
	 * @return the shippingAddress
	 */
	@javax.validation.constraints.NotNull
	com.hybris.oms.service.managedobjects.types.AddressVT getShippingAddress();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderData.shippingAndHandling</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Shipping and handling costs.
	 * 
	 * @return the shippingAndHandling
	 */
	@javax.validation.constraints.NotNull
	com.hybris.oms.service.managedobjects.shipment.ShippingAndHandlingData getShippingAndHandling();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderData.shippingFirstName</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * .
	 * 
	 * @return the shippingFirstName
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getShippingFirstName();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderData.shippingLastName</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * .
	 * 
	 * @return the shippingLastName
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getShippingLastName();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderData.shippingMethod</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * .
	 * 
	 * @return the shippingMethod
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getShippingMethod();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderData.shippingTaxCategory</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * .
	 * 
	 * @return the shippingTaxCategory
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getShippingTaxCategory();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderData.username</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Username of the user who created this order.
	 * 
	 * @return the username
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getUsername();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderData.stockroomLocationIds</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Filter list of eligible locations for sourcing.
	 * 
	 * @return the stockroomLocationIds
	 */
	java.util.List<java.lang.String> getStockroomLocationIds();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderData.baseStore</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Base store that the order belongs to.
	 * 
	 * @return the baseStore
	 */
	com.hybris.oms.service.managedobjects.basestore.BaseStoreData getBaseStore();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderData.version</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * .
	 * 
	 * @return the version
	 */
	java.lang.Long getVersion();
	

	/**
	 * <i>Generated method</i> - Setter of <code>OrderData.state</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Current order state.
	 *
	 * @param value the state
	 */
	void setState(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderData.contact</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Order contact.
	 *
	 * @param value the contact
	 */
	void setContact(final com.hybris.oms.service.managedobjects.types.ContactVT value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderData.currencyCode</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Currency code.
	 *
	 * @param value the currencyCode
	 */
	void setCurrencyCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderData.customerLocale</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * a string representation of a Locale object, consisting of language, country, etc.
	 *
	 * @param value the customerLocale
	 */
	void setCustomerLocale(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderData.emailid</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Customer's Email Id.
	 *
	 * @param value the emailid
	 */
	void setEmailid(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderData.firstName</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Customer's First Name.
	 *
	 * @param value the firstName
	 */
	void setFirstName(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderData.issueDate</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Order issue Date.
	 *
	 * @param value the issueDate
	 */
	void setIssueDate(final java.util.Date value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderData.scheduledShippingDate</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Order Scheduled Shipping Date.
	 *
	 * @param value the scheduledShippingDate
	 */
	void setScheduledShippingDate(final java.util.Date value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderData.lastName</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Customer's Last name.
	 *
	 * @param value the lastName
	 */
	void setLastName(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderData.orderId</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Unique business code of the order.
	 *
	 * @param value the orderId
	 */
	void setOrderId(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderData.orderLines</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Order line containing item and order line quantities.
	 *
	 * @param value the orderLines
	 */
	void setOrderLines(final java.util.List<com.hybris.oms.service.managedobjects.order.OrderLineData> value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderData.paymentInfos</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Order containing list of payment information.
	 *
	 * @param value the paymentInfos
	 */
	void setPaymentInfos(final java.util.List<com.hybris.oms.service.managedobjects.order.PaymentInfoData> value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderData.priorityLevelCode</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the priorityLevelCode
	 */
	void setPriorityLevelCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderData.shippingAddress</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Order shipping address.
	 *
	 * @param value the shippingAddress
	 */
	void setShippingAddress(final com.hybris.oms.service.managedobjects.types.AddressVT value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderData.shippingAndHandling</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Shipping and handling costs.
	 *
	 * @param value the shippingAndHandling
	 */
	void setShippingAndHandling(final com.hybris.oms.service.managedobjects.shipment.ShippingAndHandlingData value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderData.shippingFirstName</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the shippingFirstName
	 */
	void setShippingFirstName(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderData.shippingLastName</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the shippingLastName
	 */
	void setShippingLastName(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderData.shippingMethod</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the shippingMethod
	 */
	void setShippingMethod(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderData.shippingTaxCategory</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the shippingTaxCategory
	 */
	void setShippingTaxCategory(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderData.username</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Username of the user who created this order.
	 *
	 * @param value the username
	 */
	void setUsername(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderData.stockroomLocationIds</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Filter list of eligible locations for sourcing.
	 *
	 * @param value the stockroomLocationIds
	 */
	void setStockroomLocationIds(final java.util.List<java.lang.String> value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderData.baseStore</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Base store that the order belongs to.
	 *
	 * @param value the baseStore
	 */
	void setBaseStore(final com.hybris.oms.service.managedobjects.basestore.BaseStoreData value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderData.version</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the version
	 */
	void setVersion(final java.lang.Long value);
	
}
