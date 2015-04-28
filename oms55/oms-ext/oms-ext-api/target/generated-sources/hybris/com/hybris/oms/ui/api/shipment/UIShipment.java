

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

package com.hybris.oms.ui.api.shipment;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;


import com.hybris.oms.domain.ActionableDto;
import com.hybris.commons.dto.xml.bind.DateAdapter;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.hybris.commons.dto.impl.PropertyAwareEntityDto;
/**
* UIShipment
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class UIShipment extends PropertyAwareEntityDto implements ActionableDto, Serializable
{

	public final static long serialVersionUID = -1786239345L;

	private String orderId;

	private Long shipmentId;

	private String firstName;

	private String lastName;

	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date orderDate;

	private String shippingMethod;

	private String locationName;

	private String status;

	@XmlElement
	private Set<String> actions;

	private Date scheduledShippingDate;

	
	public UIShipment(){}

	protected UIShipment(Builder builder)
	{
		super(builder); 
		setOrderId(builder.getOrderId());
		setShipmentId(builder.getShipmentId());
		setFirstName(builder.getFirstName());
		setLastName(builder.getLastName());
		setOrderDate(builder.getOrderDate());
		setShippingMethod(builder.getShippingMethod());
		setLocationName(builder.getLocationName());
		setStatus(builder.getStatus());
		setActions(builder.getActions());
		setScheduledShippingDate(builder.getScheduledShippingDate());
	
	}

			
				@Override
				public String getId()
				{
					return String.valueOf(getShipmentId());
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
	* @returns Long
	*/
	public Long getShipmentId()
	{
		return shipmentId;
	}

	/**
	* sets 
	*
	*/
	public void setShipmentId(Long shipmentId)
	{
		this.shipmentId = shipmentId;
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
	* @returns Date
	*/
	public Date getOrderDate()
	{
		return orderDate;
	}

	/**
	* sets 
	*
	*/
	public void setOrderDate(Date orderDate)
	{
		this.orderDate = orderDate;
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
	public String getLocationName()
	{
		return locationName;
	}

	/**
	* sets 
	*
	*/
	public void setLocationName(String locationName)
	{
		this.locationName = locationName;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getStatus()
	{
		return status;
	}

	/**
	* sets 
	*
	*/
	public void setStatus(String status)
	{
		this.status = status;
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
		private String orderId;
		private Long shipmentId;
		private String firstName;
		private String lastName;
		private Date orderDate;
		private String shippingMethod;
		private String locationName;
		private String status;
		private Set<String> actions;
		private Date scheduledShippingDate;
		
	
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
		public T setShipmentId(Long shipmentId)
		{
			this.shipmentId = shipmentId;
			return self();
		}

		private Long getShipmentId()
		{
			return shipmentId;
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
		public T setOrderDate(Date orderDate)
		{
			this.orderDate = orderDate;
			return self();
		}

		private Date getOrderDate()
		{
			return orderDate;
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
		public T setLocationName(String locationName)
		{
			this.locationName = locationName;
			return self();
		}

		private String getLocationName()
		{
			return locationName;
		}
	
		/**
		* sets 
		*
		*/
		public T setStatus(String status)
		{
			this.status = status;
			return self();
		}

		private String getStatus()
		{
			return status;
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
		public T setScheduledShippingDate(Date scheduledShippingDate)
		{
			this.scheduledShippingDate = scheduledShippingDate;
			return self();
		}

		private Date getScheduledShippingDate()
		{
			return scheduledShippingDate;
		}
	
		protected abstract T self();

		public UIShipment build(){
			return new UIShipment(this);
		}
	}
}

