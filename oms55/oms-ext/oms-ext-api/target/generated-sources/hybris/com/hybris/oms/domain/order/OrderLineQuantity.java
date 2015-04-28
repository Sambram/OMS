

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


import com.hybris.oms.domain.types.Quantity;
import javax.xml.bind.annotation.XmlAccessorType;
import com.hybris.oms.domain.shipping.Shipment;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import java.io.Serializable;
import com.hybris.commons.dto.impl.PropertyAwareEntityDto;
/**
* Order Line Quantities
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class OrderLineQuantity extends PropertyAwareEntityDto implements Serializable
{

	public final static long serialVersionUID = -1711885004L;

	private String olqId;

	private String location;

	private Quantity quantity;

	private OrderLineQuantityStatus status;

	private Shipment shipment;

	
	public OrderLineQuantity(){}

	protected OrderLineQuantity(Builder builder)
	{
		super(builder); 
		setOlqId(builder.getOlqId());
		setLocation(builder.getLocation());
		setQuantity(builder.getQuantity());
		setStatus(builder.getStatus());
		setShipment(builder.getShipment());
	
	}


	@Override
	public String getId()
	{
		return olqId;
	}

    		

	/**
	* gets 
	*
	* @returns String
	*/
	public String getOlqId()
	{
		return olqId;
	}

	/**
	* sets 
	*
	*/
	public void setOlqId(String olqId)
	{
		this.olqId = olqId;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getLocation()
	{
		return location;
	}

	/**
	* sets 
	*
	*/
	public void setLocation(String location)
	{
		this.location = location;
	}

	/**
	* gets 
	*
	* @returns Quantity
	*/
	public Quantity getQuantity()
	{
		return quantity;
	}

	/**
	* sets 
	*
	*/
	public void setQuantity(Quantity quantity)
	{
		this.quantity = quantity;
	}

	/**
	* gets 
	*
	* @returns OrderLineQuantityStatus
	*/
	public OrderLineQuantityStatus getStatus()
	{
		return status;
	}

	/**
	* sets 
	*
	*/
	public void setStatus(OrderLineQuantityStatus status)
	{
		this.status = status;
	}

	/**
	* gets 
	*
	* @returns Shipment
	*/
	public Shipment getShipment()
	{
		return shipment;
	}

	/**
	* sets 
	*
	*/
	public void setShipment(Shipment shipment)
	{
		this.shipment = shipment;
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
		private String olqId;
		private String location;
		private Quantity quantity;
		private OrderLineQuantityStatus status;
		private Shipment shipment;
		
	
		/**
		* sets 
		*
		*/
		public T setOlqId(String olqId)
		{
			this.olqId = olqId;
			return self();
		}

		private String getOlqId()
		{
			return olqId;
		}
	
		/**
		* sets 
		*
		*/
		public T setLocation(String location)
		{
			this.location = location;
			return self();
		}

		private String getLocation()
		{
			return location;
		}
	
		/**
		* sets 
		*
		*/
		public T setQuantity(Quantity quantity)
		{
			this.quantity = quantity;
			return self();
		}

		private Quantity getQuantity()
		{
			return quantity;
		}
	
		/**
		* sets 
		*
		*/
		public T setStatus(OrderLineQuantityStatus status)
		{
			this.status = status;
			return self();
		}

		private OrderLineQuantityStatus getStatus()
		{
			return status;
		}
	
		/**
		* sets 
		*
		*/
		public T setShipment(Shipment shipment)
		{
			this.shipment = shipment;
			return self();
		}

		private Shipment getShipment()
		{
			return shipment;
		}
	
		protected abstract T self();

		public OrderLineQuantity build(){
			return new OrderLineQuantity(this);
		}
	}
}

