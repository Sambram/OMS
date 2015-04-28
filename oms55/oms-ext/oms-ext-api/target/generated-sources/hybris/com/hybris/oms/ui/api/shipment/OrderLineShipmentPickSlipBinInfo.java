

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


import com.hybris.oms.domain.types.Quantity;
import java.util.List;
import com.hybris.commons.dto.Dto;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import com.hybris.oms.domain.inventory.Bin;
/**
* OrderLineShipmentPickSlipBinInfo
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlRootElement
public class OrderLineShipmentPickSlipBinInfo  implements Serializable, Dto
{

	public final static long serialVersionUID = 1972196169L;

	private String orderLineSkuId;

	private String orderLineNote;

	private Quantity orderLineQuantity;

	private List<Bin> bins;

	
	public OrderLineShipmentPickSlipBinInfo(){}

	protected OrderLineShipmentPickSlipBinInfo(Builder builder)
	{
	
		setOrderLineSkuId(builder.getOrderLineSkuId());
		setOrderLineNote(builder.getOrderLineNote());
		setOrderLineQuantity(builder.getOrderLineQuantity());
		setBins(builder.getBins());
	
	}


	/**
	* gets 
	*
	* @returns String
	*/
	public String getOrderLineSkuId()
	{
		return orderLineSkuId;
	}

	/**
	* sets 
	*
	*/
	public void setOrderLineSkuId(String orderLineSkuId)
	{
		this.orderLineSkuId = orderLineSkuId;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getOrderLineNote()
	{
		return orderLineNote;
	}

	/**
	* sets 
	*
	*/
	public void setOrderLineNote(String orderLineNote)
	{
		this.orderLineNote = orderLineNote;
	}

	/**
	* gets 
	*
	* @returns Quantity
	*/
	public Quantity getOrderLineQuantity()
	{
		return orderLineQuantity;
	}

	/**
	* sets 
	*
	*/
	public void setOrderLineQuantity(Quantity orderLineQuantity)
	{
		this.orderLineQuantity = orderLineQuantity;
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
		private String orderLineSkuId;
		private String orderLineNote;
		private Quantity orderLineQuantity;
		private List<Bin> bins;
		
	
		/**
		* sets 
		*
		*/
		public T setOrderLineSkuId(String orderLineSkuId)
		{
			this.orderLineSkuId = orderLineSkuId;
			return self();
		}

		private String getOrderLineSkuId()
		{
			return orderLineSkuId;
		}
	
		/**
		* sets 
		*
		*/
		public T setOrderLineNote(String orderLineNote)
		{
			this.orderLineNote = orderLineNote;
			return self();
		}

		private String getOrderLineNote()
		{
			return orderLineNote;
		}
	
		/**
		* sets 
		*
		*/
		public T setOrderLineQuantity(Quantity orderLineQuantity)
		{
			this.orderLineQuantity = orderLineQuantity;
			return self();
		}

		private Quantity getOrderLineQuantity()
		{
			return orderLineQuantity;
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

		public OrderLineShipmentPickSlipBinInfo build(){
			return new OrderLineShipmentPickSlipBinInfo(this);
		}
	}
}

