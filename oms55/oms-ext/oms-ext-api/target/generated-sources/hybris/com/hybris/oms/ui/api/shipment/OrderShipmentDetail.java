

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


import com.hybris.oms.domain.ats.AtsLocalQuantities;
import java.util.Map;
import java.util.List;
import com.hybris.oms.domain.order.OrderLine;
import java.io.Serializable;
import com.hybris.oms.domain.inventory.Bin;
import com.hybris.oms.domain.order.OrderLineQuantity;
/**
* OrderShipmentDetail
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
public class OrderShipmentDetail  implements Serializable
{

	public final static long serialVersionUID = 1276917257L;

	private OrderLine orderLine;

	private OrderLineQuantity orderLineQuantity;

	private Map<AtsLocalQuantities, String> locationATS;

	private List<Bin> bins;

	
	public OrderShipmentDetail(){}

	protected OrderShipmentDetail(Builder builder)
	{
	
		setOrderLine(builder.getOrderLine());
		setOrderLineQuantity(builder.getOrderLineQuantity());
		setLocationATS(builder.getLocationATS());
		setBins(builder.getBins());
	
	}


	/**
	* gets 
	*
	* @returns OrderLine
	*/
	public OrderLine getOrderLine()
	{
		return orderLine;
	}

	/**
	* sets 
	*
	*/
	public void setOrderLine(OrderLine orderLine)
	{
		this.orderLine = orderLine;
	}

	/**
	* gets 
	*
	* @returns OrderLineQuantity
	*/
	public OrderLineQuantity getOrderLineQuantity()
	{
		return orderLineQuantity;
	}

	/**
	* sets 
	*
	*/
	public void setOrderLineQuantity(OrderLineQuantity orderLineQuantity)
	{
		this.orderLineQuantity = orderLineQuantity;
	}

	/**
	* gets 
	*
	* @returns Map<AtsLocalQuantities, String>
	*/
	public Map<AtsLocalQuantities, String> getLocationATS()
	{
		return locationATS;
	}

	/**
	* sets 
	*
	*/
	public void setLocationATS(Map<AtsLocalQuantities, String> locationATS)
	{
		this.locationATS = locationATS;
	}

	/**
	* gets 
	*
	* @returns List<Bin>
	*/
	public List<Bin> getBins()
	{
		return bins;
	}

	/**
	* sets 
	*
	*/
	public void setBins(List<Bin> bins)
	{
		this.bins = bins;
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

	public abstract static class Builder<T extends Builder<T>> 
	{
		private OrderLine orderLine;
		private OrderLineQuantity orderLineQuantity;
		private Map<AtsLocalQuantities, String> locationATS;
		private List<Bin> bins;
		
	
		/**
		* sets 
		*
		*/
		public T setOrderLine(OrderLine orderLine)
		{
			this.orderLine = orderLine;
			return self();
		}

		private OrderLine getOrderLine()
		{
			return orderLine;
		}
	
		/**
		* sets 
		*
		*/
		public T setOrderLineQuantity(OrderLineQuantity orderLineQuantity)
		{
			this.orderLineQuantity = orderLineQuantity;
			return self();
		}

		private OrderLineQuantity getOrderLineQuantity()
		{
			return orderLineQuantity;
		}
	
		/**
		* sets 
		*
		*/
		public T setLocationATS(Map<AtsLocalQuantities, String> locationATS)
		{
			this.locationATS = locationATS;
			return self();
		}

		private Map<AtsLocalQuantities, String> getLocationATS()
		{
			return locationATS;
		}
	
		/**
		* sets 
		*
		*/
		public T setBins(List<Bin> bins)
		{
			this.bins = bins;
			return self();
		}

		private List<Bin> getBins()
		{
			return bins;
		}
	
		protected abstract T self();

		public OrderShipmentDetail build(){
			return new OrderShipmentDetail(this);
		}
	}
}

