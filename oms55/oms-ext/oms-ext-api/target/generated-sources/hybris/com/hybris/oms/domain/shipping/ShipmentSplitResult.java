

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
import com.hybris.commons.dto.Dto;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import java.io.Serializable;
/**
* Shipment Split Result
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ShipmentSplitResult  implements Serializable, Dto
{

	public final static long serialVersionUID = -75440260L;

	private Shipment originalShipment;

	private Shipment newShipment;

	
	public ShipmentSplitResult(){}

	protected ShipmentSplitResult(Builder builder)
	{
	
		setOriginalShipment(builder.getOriginalShipment());
		setNewShipment(builder.getNewShipment());
	
	}


	public ShipmentSplitResult(final Shipment newShipment, final Shipment originalShipment)
	{
		this.newShipment = newShipment;
		this.originalShipment = originalShipment;
	}

    		

	/**
	* gets 
	*
	* @returns Shipment
	*/
	public Shipment getOriginalShipment()
	{
		return originalShipment;
	}

	/**
	* sets 
	*
	*/
	public void setOriginalShipment(Shipment originalShipment)
	{
		this.originalShipment = originalShipment;
	}

	/**
	* gets 
	*
	* @returns Shipment
	*/
	public Shipment getNewShipment()
	{
		return newShipment;
	}

	/**
	* sets 
	*
	*/
	public void setNewShipment(Shipment newShipment)
	{
		this.newShipment = newShipment;
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
		private Shipment originalShipment;
		private Shipment newShipment;
		
	
		/**
		* sets 
		*
		*/
		public T setOriginalShipment(Shipment originalShipment)
		{
			this.originalShipment = originalShipment;
			return self();
		}

		private Shipment getOriginalShipment()
		{
			return originalShipment;
		}
	
		/**
		* sets 
		*
		*/
		public T setNewShipment(Shipment newShipment)
		{
			this.newShipment = newShipment;
			return self();
		}

		private Shipment getNewShipment()
		{
			return newShipment;
		}
	
		protected abstract T self();

		public ShipmentSplitResult build(){
			return new ShipmentSplitResult(this);
		}
	}
}

