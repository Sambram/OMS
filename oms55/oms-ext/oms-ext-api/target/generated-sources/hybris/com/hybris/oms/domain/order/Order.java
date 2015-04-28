

/*
 * [y] hybris Core+ Platform
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

package com.hybris.oms.domain.order;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;


import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.ActionableDto;
import com.hybris.commons.dto.xml.bind.DateAdapter;
import com.hybris.oms.domain.shipping.ShippingAndHandling;
import java.lang.Deprecated;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import java.io.Serializable;
import com.hybris.oms.domain.types.Contact;
import com.hybris.commons.dto.impl.PropertyAwareEntityDto;
/**
* Order
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Order extends PropertyAwareEntityDto implements ActionableDto, Serializable
{

	public final static long serialVersionUID = 384550717L;

	private String currencyCode;

	private Contact contact;

	private String customerLocale;

	private String emailid;

	private String firstName;

	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date issueDate;

	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date scheduledShippingDate;

	private String lastName;

	@XmlElement
	private List<String> locationIds;

	private String orderId;

	@XmlElement
	private List<OrderLine> orderLines = new ArrayList();

	@XmlElement
	private List<PaymentInfo> paymentInfos = new ArrayList();

	private String priorityLevelCode;

	private Address shippingAddress;

	private ShippingAndHandling shippingAndHandling;

	private String shippingFirstName;

	private String shippingLastName;

	private String shippingMethod;

	private String shippingTaxCategory;

	private String username;

	private String baseStoreName;

	private Set<String> actions;

	private Boolean preOrder;

	@Deprecated
	private boolean cancellable;

	@Deprecated
	private String state;

	private String dummyNewProp;

	
	public Order(){}

	protected Order(Builder builder)
	{
		super(builder); 
		setCurrencyCode(builder.getCurrencyCode());
		setContact(builder.getContact());
		setCustomerLocale(builder.getCustomerLocale());
		setEmailid(builder.getEmailid());
		setFirstName(builder.getFirstName());
		setIssueDate(builder.getIssueDate());
		setScheduledShippingDate(builder.getScheduledShippingDate());
		setLastName(builder.getLastName());
		setLocationIds(builder.getLocationIds());
		setOrderId(builder.getOrderId());
		setOrderLines(builder.getOrderLines());
		setPaymentInfos(builder.getPaymentInfos());
		setPriorityLevelCode(builder.getPriorityLevelCode());
		setShippingAddress(builder.getShippingAddress());
		setShippingAndHandling(builder.getShippingAndHandling());
		setShippingFirstName(builder.getShippingFirstName());
		setShippingLastName(builder.getShippingLastName());
		setShippingMethod(builder.getShippingMethod());
		setShippingTaxCategory(builder.getShippingTaxCategory());
		setUsername(builder.getUsername());
		setBaseStoreName(builder.getBaseStoreName());
		setActions(builder.getActions());
		setPreOrder(builder.getPreOrder());
		setCancellable(builder.getCancellable());
		setState(builder.getState());
		setDummyNewProp(builder.getDummyNewProp());
	
	}


	public void addOrderLine(final OrderLine line)
	{
		if (this.orderLines == null)
		{
			this.orderLines = new java.util.ArrayList<>();
		}
		this.orderLines.add(line);
	}
	
	public void addPaymentInfo(final PaymentInfo paymentInfo)
	{
		if (this.paymentInfos == null)
		{
			this.paymentInfos = new java.util.ArrayList<>();
		}
		this.paymentInfos.add(paymentInfo);
	}
	
	public List<OrderLineQuantity> getListOrderLinesQuantities()
	{
		final List<OrderLineQuantity> olqs = new java.util.ArrayList<>();
		if (this.orderLines != null)
		{
			for (final OrderLine orderLine : this.orderLines)
			{
				olqs.addAll(orderLine.getOrderLineQuantities());
			}
		}
		return olqs;
	}
	
	public int getNumberOfOrderLines()
	{
		return this.orderLines.size();
	}
	
	public List<OrderLineQuantity> getOrderLineQuantities()
	{
		final List<OrderLineQuantity> olqs = new java.util.ArrayList<>();
		for (final OrderLineQuantity olq : this.getListOrderLinesQuantities())
		{
			olqs.add(olq);
		}
		return olqs;
	}
	
	@Override
	public String getId()
	{
		return orderId;
	}

    		

	/**
	* gets 
	*
	* @returns String
	*/
	public String getCurrencyCode()
	{
		return currencyCode;
	}

	/**
	* sets 
	*
	*/
	public void setCurrencyCode(String currencyCode)
	{
		this.currencyCode = currencyCode;
	}

	/**
	* gets 
	*
	* @returns Contact
	*/
	public Contact getContact()
	{
		return contact;
	}

	/**
	* sets 
	*
	*/
	public void setContact(Contact contact)
	{
		this.contact = contact;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getCustomerLocale()
	{
		return customerLocale;
	}

	/**
	* sets 
	*
	*/
	public void setCustomerLocale(String customerLocale)
	{
		this.customerLocale = customerLocale;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getEmailid()
	{
		return emailid;
	}

	/**
	* sets 
	*
	*/
	public void setEmailid(String emailid)
	{
		this.emailid = emailid;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getFirstName()
	{
		return firstName;
	}

	/**
	* sets 
	*
	*/
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	/**
	* gets 
	*
	* @returns Date
	*/
	public Date getIssueDate()
	{
		return issueDate;
	}

	/**
	* sets 
	*
	*/
	public void setIssueDate(Date issueDate)
	{
		this.issueDate = issueDate;
	}

	/**
	* gets 
	*
	* @returns Date
	*/
	public Date getScheduledShippingDate()
	{
		return scheduledShippingDate;
	}

	/**
	* sets 
	*
	*/
	public void setScheduledShippingDate(Date scheduledShippingDate)
	{
		this.scheduledShippingDate = scheduledShippingDate;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getLastName()
	{
		return lastName;
	}

	/**
	* sets 
	*
	*/
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	/**
	* gets 
	*
	* @returns List<String>
	*/
	public List<String> getLocationIds()
	{
		return locationIds;
	}

	/**
	* sets 
	*
	*/
	public void setLocationIds(List<String> locationIds)
	{
		this.locationIds = locationIds;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getOrderId()
	{
		return orderId;
	}

	/**
	* sets 
	*
	*/
	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}

	/**
	* gets 
	*
	* @returns List<OrderLine>
	*/
	public List<OrderLine> getOrderLines()
	{
		return orderLines;
	}

	/**
	* sets 
	*
	*/
	public void setOrderLines(List<OrderLine> orderLines)
	{
		this.orderLines = orderLines;
	}

	/**
	* gets 
	*
	* @returns List<PaymentInfo>
	*/
	public List<PaymentInfo> getPaymentInfos()
	{
		return paymentInfos;
	}

	/**
	* sets 
	*
	*/
	public void setPaymentInfos(List<PaymentInfo> paymentInfos)
	{
		this.paymentInfos = paymentInfos;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getPriorityLevelCode()
	{
		return priorityLevelCode;
	}

	/**
	* sets 
	*
	*/
	public void setPriorityLevelCode(String priorityLevelCode)
	{
		this.priorityLevelCode = priorityLevelCode;
	}

	/**
	* gets 
	*
	* @returns Address
	*/
	public Address getShippingAddress()
	{
		return shippingAddress;
	}

	/**
	* sets 
	*
	*/
	public void setShippingAddress(Address shippingAddress)
	{
		this.shippingAddress = shippingAddress;
	}

	/**
	* gets 
	*
	* @returns ShippingAndHandling
	*/
	public ShippingAndHandling getShippingAndHandling()
	{
		return shippingAndHandling;
	}

	/**
	* sets 
	*
	*/
	public void setShippingAndHandling(ShippingAndHandling shippingAndHandling)
	{
		this.shippingAndHandling = shippingAndHandling;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getShippingFirstName()
	{
		return shippingFirstName;
	}

	/**
	* sets 
	*
	*/
	public void setShippingFirstName(String shippingFirstName)
	{
		this.shippingFirstName = shippingFirstName;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getShippingLastName()
	{
		return shippingLastName;
	}

	/**
	* sets 
	*
	*/
	public void setShippingLastName(String shippingLastName)
	{
		this.shippingLastName = shippingLastName;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getShippingMethod()
	{
		return shippingMethod;
	}

	/**
	* sets 
	*
	*/
	public void setShippingMethod(String shippingMethod)
	{
		this.shippingMethod = shippingMethod;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getShippingTaxCategory()
	{
		return shippingTaxCategory;
	}

	/**
	* sets 
	*
	*/
	public void setShippingTaxCategory(String shippingTaxCategory)
	{
		this.shippingTaxCategory = shippingTaxCategory;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getUsername()
	{
		return username;
	}

	/**
	* sets 
	*
	*/
	public void setUsername(String username)
	{
		this.username = username;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getBaseStoreName()
	{
		return baseStoreName;
	}

	/**
	* sets 
	*
	*/
	public void setBaseStoreName(String baseStoreName)
	{
		this.baseStoreName = baseStoreName;
	}

	/**
	* gets 
	*
	* @returns Set<String>
	*/
	public Set<String> getActions()
	{
		return actions;
	}

	/**
	* sets 
	*
	*/
	public void setActions(Set<String> actions)
	{
		this.actions = actions;
	}

	/**
	* gets 
	*
	* @returns Boolean
	*/
	public Boolean getPreOrder()
	{
		return preOrder;
	}

	/**
	* sets 
	*
	*/
	public void setPreOrder(Boolean preOrder)
	{
		this.preOrder = preOrder;
	}

	/**
	* gets 
	*
	* @returns boolean
	*/
	public boolean getCancellable()
	{
		return cancellable;
	}

	/**
	* sets 
	*
	*/
	public void setCancellable(boolean cancellable)
	{
		this.cancellable = cancellable;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getState()
	{
		return state;
	}

	/**
	* sets 
	*
	*/
	public void setState(String state)
	{
		this.state = state;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getDummyNewProp()
	{
		return dummyNewProp;
	}

	/**
	* sets 
	*
	*/
	public void setDummyNewProp(String dummyNewProp)
	{
		this.dummyNewProp = dummyNewProp;
	}


	@Override
	public String toString()
	{
		return reflectionToString(this, SHORT_PREFIX_STYLE);
	}

	private static class Builder2 extends Builder<Builder2>
	{
		@Override
		protected Builder2 self()
		{
			return this;
		}
	}

	public static Builder<?> builder()
	{
		return new Builder2();
	}

	public abstract static class Builder<T extends Builder<T>> extends PropertyAwareEntityDto.Builder<T>
	{
		private String currencyCode;
		private Contact contact;
		private String customerLocale;
		private String emailid;
		private String firstName;
		private Date issueDate;
		private Date scheduledShippingDate;
		private String lastName;
		private List<String> locationIds;
		private String orderId;
		private List<OrderLine> orderLines;
		private List<PaymentInfo> paymentInfos;
		private String priorityLevelCode;
		private Address shippingAddress;
		private ShippingAndHandling shippingAndHandling;
		private String shippingFirstName;
		private String shippingLastName;
		private String shippingMethod;
		private String shippingTaxCategory;
		private String username;
		private String baseStoreName;
		private Set<String> actions;
		private Boolean preOrder;
		private boolean cancellable;
		private String state;
		private String dummyNewProp;
		
	
		/**
		* sets 
		*
		*/
		public T setCurrencyCode(String currencyCode)
		{
			this.currencyCode = currencyCode;
			return self();
		}

		private String getCurrencyCode()
		{
			return currencyCode;
		}
	
		/**
		* sets 
		*
		*/
		public T setContact(Contact contact)
		{
			this.contact = contact;
			return self();
		}

		private Contact getContact()
		{
			return contact;
		}
	
		/**
		* sets 
		*
		*/
		public T setCustomerLocale(String customerLocale)
		{
			this.customerLocale = customerLocale;
			return self();
		}

		private String getCustomerLocale()
		{
			return customerLocale;
		}
	
		/**
		* sets 
		*
		*/
		public T setEmailid(String emailid)
		{
			this.emailid = emailid;
			return self();
		}

		private String getEmailid()
		{
			return emailid;
		}
	
		/**
		* sets 
		*
		*/
		public T setFirstName(String firstName)
		{
			this.firstName = firstName;
			return self();
		}

		private String getFirstName()
		{
			return firstName;
		}
	
		/**
		* sets 
		*
		*/
		public T setIssueDate(Date issueDate)
		{
			this.issueDate = issueDate;
			return self();
		}

		private Date getIssueDate()
		{
			return issueDate;
		}
	
		/**
		* sets 
		*
		*/
		public T setScheduledShippingDate(Date scheduledShippingDate)
		{
			this.scheduledShippingDate = scheduledShippingDate;
			return self();
		}

		private Date getScheduledShippingDate()
		{
			return scheduledShippingDate;
		}
	
		/**
		* sets 
		*
		*/
		public T setLastName(String lastName)
		{
			this.lastName = lastName;
			return self();
		}

		private String getLastName()
		{
			return lastName;
		}
	
		/**
		* sets 
		*
		*/
		public T setLocationIds(List<String> locationIds)
		{
			this.locationIds = locationIds;
			return self();
		}

		private List<String> getLocationIds()
		{
			return locationIds;
		}
	
		/**
		* sets 
		*
		*/
		public T setOrderId(String orderId)
		{
			this.orderId = orderId;
			return self();
		}

		private String getOrderId()
		{
			return orderId;
		}
	
		/**
		* sets 
		*
		*/
		public T setOrderLines(List<OrderLine> orderLines)
		{
			this.orderLines = orderLines;
			return self();
		}

		private List<OrderLine> getOrderLines()
		{
			return orderLines;
		}
	
		/**
		* sets 
		*
		*/
		public T setPaymentInfos(List<PaymentInfo> paymentInfos)
		{
			this.paymentInfos = paymentInfos;
			return self();
		}

		private List<PaymentInfo> getPaymentInfos()
		{
			return paymentInfos;
		}
	
		/**
		* sets 
		*
		*/
		public T setPriorityLevelCode(String priorityLevelCode)
		{
			this.priorityLevelCode = priorityLevelCode;
			return self();
		}

		private String getPriorityLevelCode()
		{
			return priorityLevelCode;
		}
	
		/**
		* sets 
		*
		*/
		public T setShippingAddress(Address shippingAddress)
		{
			this.shippingAddress = shippingAddress;
			return self();
		}

		private Address getShippingAddress()
		{
			return shippingAddress;
		}
	
		/**
		* sets 
		*
		*/
		public T setShippingAndHandling(ShippingAndHandling shippingAndHandling)
		{
			this.shippingAndHandling = shippingAndHandling;
			return self();
		}

		private ShippingAndHandling getShippingAndHandling()
		{
			return shippingAndHandling;
		}
	
		/**
		* sets 
		*
		*/
		public T setShippingFirstName(String shippingFirstName)
		{
			this.shippingFirstName = shippingFirstName;
			return self();
		}

		private String getShippingFirstName()
		{
			return shippingFirstName;
		}
	
		/**
		* sets 
		*
		*/
		public T setShippingLastName(String shippingLastName)
		{
			this.shippingLastName = shippingLastName;
			return self();
		}

		private String getShippingLastName()
		{
			return shippingLastName;
		}
	
		/**
		* sets 
		*
		*/
		public T setShippingMethod(String shippingMethod)
		{
			this.shippingMethod = shippingMethod;
			return self();
		}

		private String getShippingMethod()
		{
			return shippingMethod;
		}
	
		/**
		* sets 
		*
		*/
		public T setShippingTaxCategory(String shippingTaxCategory)
		{
			this.shippingTaxCategory = shippingTaxCategory;
			return self();
		}

		private String getShippingTaxCategory()
		{
			return shippingTaxCategory;
		}
	
		/**
		* sets 
		*
		*/
		public T setUsername(String username)
		{
			this.username = username;
			return self();
		}

		private String getUsername()
		{
			return username;
		}
	
		/**
		* sets 
		*
		*/
		public T setBaseStoreName(String baseStoreName)
		{
			this.baseStoreName = baseStoreName;
			return self();
		}

		private String getBaseStoreName()
		{
			return baseStoreName;
		}
	
		/**
		* sets 
		*
		*/
		public T setActions(Set<String> actions)
		{
			this.actions = actions;
			return self();
		}

		private Set<String> getActions()
		{
			return actions;
		}
	
		/**
		* sets 
		*
		*/
		public T setPreOrder(Boolean preOrder)
		{
			this.preOrder = preOrder;
			return self();
		}

		private Boolean getPreOrder()
		{
			return preOrder;
		}
	
		/**
		* sets 
		*
		*/
		public T setCancellable(boolean cancellable)
		{
			this.cancellable = cancellable;
			return self();
		}

		private boolean getCancellable()
		{
			return cancellable;
		}
	
		/**
		* sets 
		*
		*/
		public T setState(String state)
		{
			this.state = state;
			return self();
		}

		private String getState()
		{
			return state;
		}
	
		/**
		* sets 
		*
		*/
		public T setDummyNewProp(String dummyNewProp)
		{
			this.dummyNewProp = dummyNewProp;
			return self();
		}

		private String getDummyNewProp()
		{
			return dummyNewProp;
		}
	
		protected abstract T self();

		public Order build(){
			return new Order(this);
		}
	}
}

