

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


import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import java.io.Serializable;
import com.hybris.oms.domain.types.Price;
import com.hybris.commons.dto.impl.PropertyAwareEntityDto;
/**
* Shipment And Handling
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ShippingAndHandling extends PropertyAwareEntityDto implements Serializable
{

	public final static long serialVersionUID = 1884837289L;

	private String firstShipmentId;

	private String orderId;

	private Price shippingPrice;

	
	public ShippingAndHandling(){}

	protected ShippingAndHandling(Builder builder)
	{
		super(builder); 
		setFirstShipmentId(builder.getFirstShipmentId());
		setOrderId(builder.getOrderId());
		setShippingPrice(builder.getShippingPrice());
	
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
	public String getFirstShipmentId()
	{
		return firstShipmentId;
	}

	/**
	* sets 
	*
	*/
	public void setFirstShipmentId(String firstShipmentId)
	{
		this.firstShipmentId = firstShipmentId;
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
	* @returns Price
	*/
	public Price getShippingPrice()
	{
		return shippingPrice;
	}

	/**
	* sets 
	*
	*/
	public void setShippingPrice(Price shippingPrice)
	{
		this.shippingPrice = shippingPrice;
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
		private String firstShipmentId;
		private String orderId;
		private Price shippingPrice;
		
	
		/**
		* sets 
		*
		*/
		public T setFirstShipmentId(String firstShipmentId)
		{
			this.firstShipmentId = firstShipmentId;
			return self();
		}

		private String getFirstShipmentId()
		{
			return firstShipmentId;
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
		public T setShippingPrice(Price shippingPrice)
		{
			this.shippingPrice = shippingPrice;
			return self();
		}

		private Price getShippingPrice()
		{
			return shippingPrice;
		}
	
		protected abstract T self();

		public ShippingAndHandling build(){
			return new ShippingAndHandling(this);
		}
	}
}

