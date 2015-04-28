

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

package com.hybris.oms.domain.shipping;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;


import com.hybris.oms.domain.address.Address;
import javax.xml.bind.annotation.XmlAccessorType;
import com.hybris.commons.dto.Dto;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import java.io.Serializable;
import com.hybris.commons.dto.impl.PropertyAwareEntityDto;
/**
* Delivery
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Delivery extends PropertyAwareEntityDto implements Serializable, Dto
{

	public final static long serialVersionUID = -1590416442L;

	private String deliveryId;

	private String quantityUnitCode;

	private int quantityValue;

	private Date actualDeliveryDate;

	private Date latestDeliveryDate;

	private String trackingID;

	private String trackingUrl;

	private String labelUrl;

	private String deliveryLocationId;

	private Address deliveryAddress;

	
	public Delivery(){}

	protected Delivery(Builder builder)
	{
		super(builder); 
		setDeliveryId(builder.getDeliveryId());
		setQuantityUnitCode(builder.getQuantityUnitCode());
		setQuantityValue(builder.getQuantityValue());
		setActualDeliveryDate(builder.getActualDeliveryDate());
		setLatestDeliveryDate(builder.getLatestDeliveryDate());
		setTrackingID(builder.getTrackingID());
		setTrackingUrl(builder.getTrackingUrl());
		setLabelUrl(builder.getLabelUrl());
		setDeliveryLocationId(builder.getDeliveryLocationId());
		setDeliveryAddress(builder.getDeliveryAddress());
	
	}


	public void setQuantity(final com.hybris.oms.domain.types.Quantity quantity)
	{
		this.quantityUnitCode = quantity.getUnitCode();
		this.quantityValue = quantity.getValue();
	}
	
	@Override
	public String getId()
	{
		return deliveryId;
	}

    		

	/**
	* gets 
	*
	* @returns String
	*/
	public String getDeliveryId()
	{
		return deliveryId;
	}

	/**
	* sets 
	*
	*/
	public void setDeliveryId(String deliveryId)
	{
		this.deliveryId = deliveryId;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getQuantityUnitCode()
	{
		return quantityUnitCode;
	}

	/**
	* sets 
	*
	*/
	public void setQuantityUnitCode(String quantityUnitCode)
	{
		this.quantityUnitCode = quantityUnitCode;
	}

	/**
	* gets 
	*
	* @returns int
	*/
	public int getQuantityValue()
	{
		return quantityValue;
	}

	/**
	* sets 
	*
	*/
	public void setQuantityValue(int quantityValue)
	{
		this.quantityValue = quantityValue;
	}

	/**
	* gets 
	*
	* @returns Date
	*/
	public Date getActualDeliveryDate()
	{
		return actualDeliveryDate;
	}

	/**
	* sets 
	*
	*/
	public void setActualDeliveryDate(Date actualDeliveryDate)
	{
		this.actualDeliveryDate = actualDeliveryDate;
	}

	/**
	* gets 
	*
	* @returns Date
	*/
	public Date getLatestDeliveryDate()
	{
		return latestDeliveryDate;
	}

	/**
	* sets 
	*
	*/
	public void setLatestDeliveryDate(Date latestDeliveryDate)
	{
		this.latestDeliveryDate = latestDeliveryDate;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getTrackingID()
	{
		return trackingID;
	}

	/**
	* sets 
	*
	*/
	public void setTrackingID(String trackingID)
	{
		this.trackingID = trackingID;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getTrackingUrl()
	{
		return trackingUrl;
	}

	/**
	* sets 
	*
	*/
	public void setTrackingUrl(String trackingUrl)
	{
		this.trackingUrl = trackingUrl;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getLabelUrl()
	{
		return labelUrl;
	}

	/**
	* sets 
	*
	*/
	public void setLabelUrl(String labelUrl)
	{
		this.labelUrl = labelUrl;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getDeliveryLocationId()
	{
		return deliveryLocationId;
	}

	/**
	* sets 
	*
	*/
	public void setDeliveryLocationId(String deliveryLocationId)
	{
		this.deliveryLocationId = deliveryLocationId;
	}

	/**
	* gets 
	*
	* @returns Address
	*/
	public Address getDeliveryAddress()
	{
		return deliveryAddress;
	}

	/**
	* sets 
	*
	*/
	public void setDeliveryAddress(Address deliveryAddress)
	{
		this.deliveryAddress = deliveryAddress;
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
		private String deliveryId;
		private String quantityUnitCode;
		private int quantityValue;
		private Date actualDeliveryDate;
		private Date latestDeliveryDate;
		private String trackingID;
		private String trackingUrl;
		private String labelUrl;
		private String deliveryLocationId;
		private Address deliveryAddress;
		
	
		/**
		* sets 
		*
		*/
		public T setDeliveryId(String deliveryId)
		{
			this.deliveryId = deliveryId;
			return self();
		}

		private String getDeliveryId()
		{
			return deliveryId;
		}
	
		/**
		* sets 
		*
		*/
		public T setQuantityUnitCode(String quantityUnitCode)
		{
			this.quantityUnitCode = quantityUnitCode;
			return self();
		}

		private String getQuantityUnitCode()
		{
			return quantityUnitCode;
		}
	
		/**
		* sets 
		*
		*/
		public T setQuantityValue(int quantityValue)
		{
			this.quantityValue = quantityValue;
			return self();
		}

		private int getQuantityValue()
		{
			return quantityValue;
		}
	
		/**
		* sets 
		*
		*/
		public T setActualDeliveryDate(Date actualDeliveryDate)
		{
			this.actualDeliveryDate = actualDeliveryDate;
			return self();
		}

		private Date getActualDeliveryDate()
		{
			return actualDeliveryDate;
		}
	
		/**
		* sets 
		*
		*/
		public T setLatestDeliveryDate(Date latestDeliveryDate)
		{
			this.latestDeliveryDate = latestDeliveryDate;
			return self();
		}

		private Date getLatestDeliveryDate()
		{
			return latestDeliveryDate;
		}
	
		/**
		* sets 
		*
		*/
		public T setTrackingID(String trackingID)
		{
			this.trackingID = trackingID;
			return self();
		}

		private String getTrackingID()
		{
			return trackingID;
		}
	
		/**
		* sets 
		*
		*/
		public T setTrackingUrl(String trackingUrl)
		{
			this.trackingUrl = trackingUrl;
			return self();
		}

		private String getTrackingUrl()
		{
			return trackingUrl;
		}
	
		/**
		* sets 
		*
		*/
		public T setLabelUrl(String labelUrl)
		{
			this.labelUrl = labelUrl;
			return self();
		}

		private String getLabelUrl()
		{
			return labelUrl;
		}
	
		/**
		* sets 
		*
		*/
		public T setDeliveryLocationId(String deliveryLocationId)
		{
			this.deliveryLocationId = deliveryLocationId;
			return self();
		}

		private String getDeliveryLocationId()
		{
			return deliveryLocationId;
		}
	
		/**
		* sets 
		*
		*/
		public T setDeliveryAddress(Address deliveryAddress)
		{
			this.deliveryAddress = deliveryAddress;
			return self();
		}

		private Address getDeliveryAddress()
		{
			return deliveryAddress;
		}
	
		protected abstract T self();

		public Delivery build(){
			return new Delivery(this);
		}
	}
}

