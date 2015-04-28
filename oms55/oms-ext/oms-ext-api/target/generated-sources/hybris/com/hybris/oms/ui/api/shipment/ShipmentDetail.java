

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


import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.commons.dto.Dto;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import com.hybris.oms.domain.order.Order;
/**
* ShipmentDetail
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlRootElement
public class ShipmentDetail  implements Serializable, Dto
{

	public final static long serialVersionUID = 1209041482L;

	private Location location;

	private Order order;

	private Shipment shipment;

	
	public ShipmentDetail(){}

	protected ShipmentDetail(Builder builder)
	{
	
		setLocation(builder.getLocation());
		setOrder(builder.getOrder());
		setShipment(builder.getShipment());
	
	}

			
				@Override
				public boolean equals(final Object otherObject)
				{
					if (this == otherObject)
					{
						return true;
					}
					if (otherObject == null || this.getClass() != otherObject.getClass())
					{
						return false;
					}
					return java.util.Objects.equals(shipment, ((ShipmentDetail) otherObject).shipment);
				}
				
				@Override
				public int hashCode()
				{
					return java.util.Objects.hash(shipment);
				}
			
			

	/**
	* gets 
	*
	* @returns Location
	*/
	public Location getLocation()
	{
		return location;
	}

	/**
	* sets 
	*
	*/
	public void setLocation(Location location)
	{
		this.location = location;
	}

	/**
	* gets 
	*
	* @returns Order
	*/
	public Order getOrder()
	{
		return order;
	}

	/**
	* sets 
	*
	*/
	public void setOrder(Order order)
	{
		this.order = order;
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

	public abstract static class Builder<T extends Builder<T>> 
	{
		private Location location;
		private Order order;
		private Shipment shipment;
		
	
		/**
		* sets 
		*
		*/
		public T setLocation(Location location)
		{
			this.location = location;
			return self();
		}

		private Location getLocation()
		{
			return location;
		}
	
		/**
		* sets 
		*
		*/
		public T setOrder(Order order)
		{
			this.order = order;
			return self();
		}

		private Order getOrder()
		{
			return order;
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

		public ShipmentDetail build(){
			return new ShipmentDetail(this);
		}
	}
}

