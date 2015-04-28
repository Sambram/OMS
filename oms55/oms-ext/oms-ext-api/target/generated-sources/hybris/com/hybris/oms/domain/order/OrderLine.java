

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


import com.hybris.oms.domain.order.OrderlineFulfillmentType;
import com.hybris.oms.domain.types.Quantity;
import java.util.ArrayList;
import com.hybris.oms.domain.locationrole.LocationRole;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessorType;
import com.hybris.oms.domain.types.Amount;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import com.hybris.commons.dto.impl.PropertyAwareEntityDto;
/**
* Order Line
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class OrderLine extends PropertyAwareEntityDto implements Serializable
{

	public final static long serialVersionUID = -567786710L;

	private String orderLineId;

	@XmlElement
	private List<OrderLineQuantity> orderLineQuantities = new ArrayList();

	@XmlElement
	private List<OrderLineAttribute> orderLineAttributes = new ArrayList();

	@XmlElement
	private Set<LocationRole> locationRoles;

	private String note;

	private String orderLineStatus;

	private String pickupStoreId;

	private String skuId;

	private String taxCategory;

	private Quantity quantity;

	private Quantity quantityUnassigned;

	private Amount unitPrice;

	private Amount unitTax;

	private OrderlineFulfillmentType fulfillmentType;

	
	public OrderLine(){}

	protected OrderLine(Builder builder)
	{
		super(builder); 
		setOrderLineId(builder.getOrderLineId());
		setOrderLineQuantities(builder.getOrderLineQuantities());
		setOrderLineAttributes(builder.getOrderLineAttributes());
		setLocationRoles(builder.getLocationRoles());
		setNote(builder.getNote());
		setOrderLineStatus(builder.getOrderLineStatus());
		setPickupStoreId(builder.getPickupStoreId());
		setSkuId(builder.getSkuId());
		setTaxCategory(builder.getTaxCategory());
		setQuantity(builder.getQuantity());
		setQuantityUnassigned(builder.getQuantityUnassigned());
		setUnitPrice(builder.getUnitPrice());
		setUnitTax(builder.getUnitTax());
		setFulfillmentType(builder.getFulfillmentType());
	
	}


	public void addOrderLineQuantity(final OrderLineQuantity orderLineQuantity)
	{
		if (this.orderLineQuantities == null)
		{
			this.orderLineQuantities = new java.util.ArrayList<>();
		}
		this.orderLineQuantities.add(orderLineQuantity);
	}
	
	public void addOrderLineAttribute(final OrderLineAttribute orderLineAttribute)
	{
		if (this.orderLineAttributes == null)
		{
			this.orderLineAttributes = new java.util.ArrayList<>();
		}
		this.orderLineAttributes.add(orderLineAttribute);
	}
	
	@Override
	public String getId()
	{
		return orderLineId;
	}

    		

	/**
	* gets 
	*
	* @returns String
	*/
	public String getOrderLineId()
	{
		return orderLineId;
	}

	/**
	* sets 
	*
	*/
	public void setOrderLineId(String orderLineId)
	{
		this.orderLineId = orderLineId;
	}

	/**
	* gets 
	*
	* @returns List<OrderLineQuantity>
	*/
	public List<OrderLineQuantity> getOrderLineQuantities()
	{
		return orderLineQuantities;
	}

	/**
	* sets 
	*
	*/
	public void setOrderLineQuantities(List<OrderLineQuantity> orderLineQuantities)
	{
		this.orderLineQuantities = orderLineQuantities;
	}

	/**
	* gets 
	*
	* @returns List<OrderLineAttribute>
	*/
	public List<OrderLineAttribute> getOrderLineAttributes()
	{
		return orderLineAttributes;
	}

	/**
	* sets 
	*
	*/
	public void setOrderLineAttributes(List<OrderLineAttribute> orderLineAttributes)
	{
		this.orderLineAttributes = orderLineAttributes;
	}

	/**
	* gets 
	*
	* @returns Set<LocationRole>
	*/
	public Set<LocationRole> getLocationRoles()
	{
		return locationRoles;
	}

	/**
	* sets 
	*
	*/
	public void setLocationRoles(Set<LocationRole> locationRoles)
	{
		this.locationRoles = locationRoles;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getNote()
	{
		return note;
	}

	/**
	* sets 
	*
	*/
	public void setNote(String note)
	{
		this.note = note;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getOrderLineStatus()
	{
		return orderLineStatus;
	}

	/**
	* sets 
	*
	*/
	public void setOrderLineStatus(String orderLineStatus)
	{
		this.orderLineStatus = orderLineStatus;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getPickupStoreId()
	{
		return pickupStoreId;
	}

	/**
	* sets 
	*
	*/
	public void setPickupStoreId(String pickupStoreId)
	{
		this.pickupStoreId = pickupStoreId;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getSkuId()
	{
		return skuId;
	}

	/**
	* sets 
	*
	*/
	public void setSkuId(String skuId)
	{
		this.skuId = skuId;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getTaxCategory()
	{
		return taxCategory;
	}

	/**
	* sets 
	*
	*/
	public void setTaxCategory(String taxCategory)
	{
		this.taxCategory = taxCategory;
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
	* @returns Quantity
	*/
	public Quantity getQuantityUnassigned()
	{
		return quantityUnassigned;
	}

	/**
	* sets 
	*
	*/
	public void setQuantityUnassigned(Quantity quantityUnassigned)
	{
		this.quantityUnassigned = quantityUnassigned;
	}

	/**
	* gets 
	*
	* @returns Amount
	*/
	public Amount getUnitPrice()
	{
		return unitPrice;
	}

	/**
	* sets 
	*
	*/
	public void setUnitPrice(Amount unitPrice)
	{
		this.unitPrice = unitPrice;
	}

	/**
	* gets 
	*
	* @returns Amount
	*/
	public Amount getUnitTax()
	{
		return unitTax;
	}

	/**
	* sets 
	*
	*/
	public void setUnitTax(Amount unitTax)
	{
		this.unitTax = unitTax;
	}

	/**
	* gets 
	*
	* @returns OrderlineFulfillmentType
	*/
	public OrderlineFulfillmentType getFulfillmentType()
	{
		return fulfillmentType;
	}

	/**
	* sets 
	*
	*/
	public void setFulfillmentType(OrderlineFulfillmentType fulfillmentType)
	{
		this.fulfillmentType = fulfillmentType;
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
		private String orderLineId;
		private List<OrderLineQuantity> orderLineQuantities;
		private List<OrderLineAttribute> orderLineAttributes;
		private Set<LocationRole> locationRoles;
		private String note;
		private String orderLineStatus;
		private String pickupStoreId;
		private String skuId;
		private String taxCategory;
		private Quantity quantity;
		private Quantity quantityUnassigned;
		private Amount unitPrice;
		private Amount unitTax;
		private OrderlineFulfillmentType fulfillmentType;
		
	
		/**
		* sets 
		*
		*/
		public T setOrderLineId(String orderLineId)
		{
			this.orderLineId = orderLineId;
			return self();
		}

		private String getOrderLineId()
		{
			return orderLineId;
		}
	
		/**
		* sets 
		*
		*/
		public T setOrderLineQuantities(List<OrderLineQuantity> orderLineQuantities)
		{
			this.orderLineQuantities = orderLineQuantities;
			return self();
		}

		private List<OrderLineQuantity> getOrderLineQuantities()
		{
			return orderLineQuantities;
		}
	
		/**
		* sets 
		*
		*/
		public T setOrderLineAttributes(List<OrderLineAttribute> orderLineAttributes)
		{
			this.orderLineAttributes = orderLineAttributes;
			return self();
		}

		private List<OrderLineAttribute> getOrderLineAttributes()
		{
			return orderLineAttributes;
		}
	
		/**
		* sets 
		*
		*/
		public T setLocationRoles(Set<LocationRole> locationRoles)
		{
			this.locationRoles = locationRoles;
			return self();
		}

		private Set<LocationRole> getLocationRoles()
		{
			return locationRoles;
		}
	
		/**
		* sets 
		*
		*/
		public T setNote(String note)
		{
			this.note = note;
			return self();
		}

		private String getNote()
		{
			return note;
		}
	
		/**
		* sets 
		*
		*/
		public T setOrderLineStatus(String orderLineStatus)
		{
			this.orderLineStatus = orderLineStatus;
			return self();
		}

		private String getOrderLineStatus()
		{
			return orderLineStatus;
		}
	
		/**
		* sets 
		*
		*/
		public T setPickupStoreId(String pickupStoreId)
		{
			this.pickupStoreId = pickupStoreId;
			return self();
		}

		private String getPickupStoreId()
		{
			return pickupStoreId;
		}
	
		/**
		* sets 
		*
		*/
		public T setSkuId(String skuId)
		{
			this.skuId = skuId;
			return self();
		}

		private String getSkuId()
		{
			return skuId;
		}
	
		/**
		* sets 
		*
		*/
		public T setTaxCategory(String taxCategory)
		{
			this.taxCategory = taxCategory;
			return self();
		}

		private String getTaxCategory()
		{
			return taxCategory;
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
		public T setQuantityUnassigned(Quantity quantityUnassigned)
		{
			this.quantityUnassigned = quantityUnassigned;
			return self();
		}

		private Quantity getQuantityUnassigned()
		{
			return quantityUnassigned;
		}
	
		/**
		* sets 
		*
		*/
		public T setUnitPrice(Amount unitPrice)
		{
			this.unitPrice = unitPrice;
			return self();
		}

		private Amount getUnitPrice()
		{
			return unitPrice;
		}
	
		/**
		* sets 
		*
		*/
		public T setUnitTax(Amount unitTax)
		{
			this.unitTax = unitTax;
			return self();
		}

		private Amount getUnitTax()
		{
			return unitTax;
		}
	
		/**
		* sets 
		*
		*/
		public T setFulfillmentType(OrderlineFulfillmentType fulfillmentType)
		{
			this.fulfillmentType = fulfillmentType;
			return self();
		}

		private OrderlineFulfillmentType getFulfillmentType()
		{
			return fulfillmentType;
		}
	
		protected abstract T self();

		public OrderLine build(){
			return new OrderLine(this);
		}
	}
}

