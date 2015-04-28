

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

package com.hybris.oms.ui.api.order;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;


import com.hybris.commons.dto.xml.bind.DateAdapter;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.hybris.commons.dto.impl.PropertyAwareEntityDto;
/**
* UIOrder
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class UIOrder extends PropertyAwareEntityDto implements Serializable
{

	public final static long serialVersionUID = -1779777300L;

	private String currencyCode;

	private String customerLocale;

	private String emailid;

	private String firstName;

	private String lastName;

	@XmlElement
	private List<String> locationIds;

	private Date scheduledShippingDate;

	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date issueDate;

	@XmlID
	private String orderId;

	private String priorityLevelCode;

	private String shippingFirstName;

	private String shippingLastName;

	private String shippingMethod;

	private String shippingTaxCategory;

	private String username;

	private String baseStoreName;

	
	public UIOrder(){}

	protected UIOrder(Builder builder)
	{
		super(builder); 
		setCurrencyCode(builder.getCurrencyCode());
		setCustomerLocale(builder.getCustomerLocale());
		setEmailid(builder.getEmailid());
		setFirstName(builder.getFirstName());
		setLastName(builder.getLastName());
		setLocationIds(builder.getLocationIds());
		setScheduledShippingDate(builder.getScheduledShippingDate());
		setIssueDate(builder.getIssueDate());
		setOrderId(builder.getOrderId());
		setPriorityLevelCode(builder.getPriorityLevelCode());
		setShippingFirstName(builder.getShippingFirstName());
		setShippingLastName(builder.getShippingLastName());
		setShippingMethod(builder.getShippingMethod());
		setShippingTaxCategory(builder.getShippingTaxCategory());
		setUsername(builder.getUsername());
		setBaseStoreName(builder.getBaseStoreName());
	
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
		private String customerLocale;
		private String emailid;
		private String firstName;
		private String lastName;
		private List<String> locationIds;
		private Date scheduledShippingDate;
		private Date issueDate;
		private String orderId;
		private String priorityLevelCode;
		private String shippingFirstName;
		private String shippingLastName;
		private String shippingMethod;
		private String shippingTaxCategory;
		private String username;
		private String baseStoreName;
		
	
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
	
		protected abstract T self();

		public UIOrder build(){
			return new UIOrder(this);
		}
	}
}

