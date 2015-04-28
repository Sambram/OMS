

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


import com.hybris.oms.domain.types.Quantity;
import java.util.ArrayList;
import com.hybris.oms.domain.shipping.OrderlinesFulfillmentType;
import com.hybris.oms.domain.types.Measure;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.ActionableDto;
import com.hybris.oms.domain.types.Amount;
import java.lang.Deprecated;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import java.io.Serializable;
import com.hybris.oms.domain.types.Price;
import com.hybris.commons.dto.impl.PropertyAwareEntityDto;
/**
* Shipment
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Shipment extends PropertyAwareEntityDto implements ActionableDto, Serializable
{

	public final static long serialVersionUID = 1850049145L;

	private Amount amountCaptured;

	private String currencyCode;

	private Delivery delivery;

	private String firstArrivalLocationId;

	private Measure grossVolume;

	private Measure grossWeight;

	private String handlingCode;

	private String handlingInstructions;

	private Measure height;

	private String information;

	private Amount insuranceValueAmount;

	private String lastExitLocationId;

	private Measure length;

	private String location;

	private Price merchandisePrice;

	private Measure netWeight;

	private String olqsStatus;

	private String orderId;

	private boolean pickupInStore;

	private String priorityLevelCode;

	private Address shipFrom;

	@XmlAttribute
	private String shipmentId;

	private ShippingAndHandling shippingAndHandling;

	private String shippingMethod;

	private String taxCategory;

	private Quantity totalGoodsItemQuantity;

	private Measure width;

	private String packageDescription;

	@XmlElement
	private List<String> authUrls = new ArrayList();

	@XmlElement(name = "olqId")
	@XmlElementWrapper(name = "olqIds")
	private List<String> olqIds = new ArrayList();

	@XmlElement
	private Set<String> actions;

	@Deprecated
	private String state;

	private OrderlinesFulfillmentType orderLinesFulfillmentType;

	
	public Shipment(){}

	protected Shipment(Builder builder)
	{
		super(builder); 
		setAmountCaptured(builder.getAmountCaptured());
		setCurrencyCode(builder.getCurrencyCode());
		setDelivery(builder.getDelivery());
		setFirstArrivalLocationId(builder.getFirstArrivalLocationId());
		setGrossVolume(builder.getGrossVolume());
		setGrossWeight(builder.getGrossWeight());
		setHandlingCode(builder.getHandlingCode());
		setHandlingInstructions(builder.getHandlingInstructions());
		setHeight(builder.getHeight());
		setInformation(builder.getInformation());
		setInsuranceValueAmount(builder.getInsuranceValueAmount());
		setLastExitLocationId(builder.getLastExitLocationId());
		setLength(builder.getLength());
		setLocation(builder.getLocation());
		setMerchandisePrice(builder.getMerchandisePrice());
		setNetWeight(builder.getNetWeight());
		setOlqsStatus(builder.getOlqsStatus());
		setOrderId(builder.getOrderId());
		setPickupInStore(builder.getPickupInStore());
		setPriorityLevelCode(builder.getPriorityLevelCode());
		setShipFrom(builder.getShipFrom());
		setShipmentId(builder.getShipmentId());
		setShippingAndHandling(builder.getShippingAndHandling());
		setShippingMethod(builder.getShippingMethod());
		setTaxCategory(builder.getTaxCategory());
		setTotalGoodsItemQuantity(builder.getTotalGoodsItemQuantity());
		setWidth(builder.getWidth());
		setPackageDescription(builder.getPackageDescription());
		setAuthUrls(builder.getAuthUrls());
		setOlqIds(builder.getOlqIds());
		setActions(builder.getActions());
		setState(builder.getState());
		setOrderLinesFulfillmentType(builder.getOrderLinesFulfillmentType());
	
	}


	@Override
	public String getId()
	{
		return shipmentId;
	}

    		

	/**
	* gets 
	*
	* @returns Amount
	*/
	public Amount getAmountCaptured()
	{
		return amountCaptured;
	}

	/**
	* sets 
	*
	*/
	public void setAmountCaptured(Amount amountCaptured)
	{
		this.amountCaptured = amountCaptured;
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
	* @returns Delivery
	*/
	public Delivery getDelivery()
	{
		return delivery;
	}

	/**
	* sets 
	*
	*/
	public void setDelivery(Delivery delivery)
	{
		this.delivery = delivery;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getFirstArrivalLocationId()
	{
		return firstArrivalLocationId;
	}

	/**
	* sets 
	*
	*/
	public void setFirstArrivalLocationId(String firstArrivalLocationId)
	{
		this.firstArrivalLocationId = firstArrivalLocationId;
	}

	/**
	* gets 
	*
	* @returns Measure
	*/
	public Measure getGrossVolume()
	{
		return grossVolume;
	}

	/**
	* sets 
	*
	*/
	public void setGrossVolume(Measure grossVolume)
	{
		this.grossVolume = grossVolume;
	}

	/**
	* gets 
	*
	* @returns Measure
	*/
	public Measure getGrossWeight()
	{
		return grossWeight;
	}

	/**
	* sets 
	*
	*/
	public void setGrossWeight(Measure grossWeight)
	{
		this.grossWeight = grossWeight;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getHandlingCode()
	{
		return handlingCode;
	}

	/**
	* sets 
	*
	*/
	public void setHandlingCode(String handlingCode)
	{
		this.handlingCode = handlingCode;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getHandlingInstructions()
	{
		return handlingInstructions;
	}

	/**
	* sets 
	*
	*/
	public void setHandlingInstructions(String handlingInstructions)
	{
		this.handlingInstructions = handlingInstructions;
	}

	/**
	* gets 
	*
	* @returns Measure
	*/
	public Measure getHeight()
	{
		return height;
	}

	/**
	* sets 
	*
	*/
	public void setHeight(Measure height)
	{
		this.height = height;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getInformation()
	{
		return information;
	}

	/**
	* sets 
	*
	*/
	public void setInformation(String information)
	{
		this.information = information;
	}

	/**
	* gets 
	*
	* @returns Amount
	*/
	public Amount getInsuranceValueAmount()
	{
		return insuranceValueAmount;
	}

	/**
	* sets 
	*
	*/
	public void setInsuranceValueAmount(Amount insuranceValueAmount)
	{
		this.insuranceValueAmount = insuranceValueAmount;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getLastExitLocationId()
	{
		return lastExitLocationId;
	}

	/**
	* sets 
	*
	*/
	public void setLastExitLocationId(String lastExitLocationId)
	{
		this.lastExitLocationId = lastExitLocationId;
	}

	/**
	* gets 
	*
	* @returns Measure
	*/
	public Measure getLength()
	{
		return length;
	}

	/**
	* sets 
	*
	*/
	public void setLength(Measure length)
	{
		this.length = length;
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
	* @returns Price
	*/
	public Price getMerchandisePrice()
	{
		return merchandisePrice;
	}

	/**
	* sets 
	*
	*/
	public void setMerchandisePrice(Price merchandisePrice)
	{
		this.merchandisePrice = merchandisePrice;
	}

	/**
	* gets 
	*
	* @returns Measure
	*/
	public Measure getNetWeight()
	{
		return netWeight;
	}

	/**
	* sets 
	*
	*/
	public void setNetWeight(Measure netWeight)
	{
		this.netWeight = netWeight;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getOlqsStatus()
	{
		return olqsStatus;
	}

	/**
	* sets 
	*
	*/
	public void setOlqsStatus(String olqsStatus)
	{
		this.olqsStatus = olqsStatus;
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
	* @returns boolean
	*/
	public boolean getPickupInStore()
	{
		return pickupInStore;
	}

	/**
	* sets 
	*
	*/
	public void setPickupInStore(boolean pickupInStore)
	{
		this.pickupInStore = pickupInStore;
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
	* @returns Address
	*/
	public Address getShipFrom()
	{
		return shipFrom;
	}

	/**
	* sets 
	*
	*/
	public void setShipFrom(Address shipFrom)
	{
		this.shipFrom = shipFrom;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getShipmentId()
	{
		return shipmentId;
	}

	/**
	* sets 
	*
	*/
	public void setShipmentId(String shipmentId)
	{
		this.shipmentId = shipmentId;
	}

	/**
	* gets 
	*
	* @returns ShippingAndHandling
	*/
	public ShippingAndHandling getShippingAndHandling()
	{
		return shippingAndHandling;
	}

	/**
	* sets 
	*
	*/
	public void setShippingAndHandling(ShippingAndHandling shippingAndHandling)
	{
		this.shippingAndHandling = shippingAndHandling;
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
	public Quantity getTotalGoodsItemQuantity()
	{
		return totalGoodsItemQuantity;
	}

	/**
	* sets 
	*
	*/
	public void setTotalGoodsItemQuantity(Quantity totalGoodsItemQuantity)
	{
		this.totalGoodsItemQuantity = totalGoodsItemQuantity;
	}

	/**
	* gets 
	*
	* @returns Measure
	*/
	public Measure getWidth()
	{
		return width;
	}

	/**
	* sets 
	*
	*/
	public void setWidth(Measure width)
	{
		this.width = width;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getPackageDescription()
	{
		return packageDescription;
	}

	/**
	* sets 
	*
	*/
	public void setPackageDescription(String packageDescription)
	{
		this.packageDescription = packageDescription;
	}

	/**
	* gets 
	*
	* @returns List<String>
	*/
	public List<String> getAuthUrls()
	{
		return authUrls;
	}

	/**
	* sets 
	*
	*/
	public void setAuthUrls(List<String> authUrls)
	{
		this.authUrls = authUrls;
	}

	/**
	* gets 
	*
	* @returns List<String>
	*/
	public List<String> getOlqIds()
	{
		return olqIds;
	}

	/**
	* sets 
	*
	*/
	public void setOlqIds(List<String> olqIds)
	{
		this.olqIds = olqIds;
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
	* @returns String
	*/
	public String getState()
	{
		return state;
	}

	/**
	* sets 
	*
	*/
	public void setState(String state)
	{
		this.state = state;
	}

	/**
	* gets 
	*
	* @returns OrderlinesFulfillmentType
	*/
	public OrderlinesFulfillmentType getOrderLinesFulfillmentType()
	{
		return orderLinesFulfillmentType;
	}

	/**
	* sets 
	*
	*/
	public void setOrderLinesFulfillmentType(OrderlinesFulfillmentType orderLinesFulfillmentType)
	{
		this.orderLinesFulfillmentType = orderLinesFulfillmentType;
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
		private Amount amountCaptured;
		private String currencyCode;
		private Delivery delivery;
		private String firstArrivalLocationId;
		private Measure grossVolume;
		private Measure grossWeight;
		private String handlingCode;
		private String handlingInstructions;
		private Measure height;
		private String information;
		private Amount insuranceValueAmount;
		private String lastExitLocationId;
		private Measure length;
		private String location;
		private Price merchandisePrice;
		private Measure netWeight;
		private String olqsStatus;
		private String orderId;
		private boolean pickupInStore;
		private String priorityLevelCode;
		private Address shipFrom;
		private String shipmentId;
		private ShippingAndHandling shippingAndHandling;
		private String shippingMethod;
		private String taxCategory;
		private Quantity totalGoodsItemQuantity;
		private Measure width;
		private String packageDescription;
		private List<String> authUrls;
		private List<String> olqIds;
		private Set<String> actions;
		private String state;
		private OrderlinesFulfillmentType orderLinesFulfillmentType;
		
	
		/**
		* sets 
		*
		*/
		public T setAmountCaptured(Amount amountCaptured)
		{
			this.amountCaptured = amountCaptured;
			return self();
		}

		private Amount getAmountCaptured()
		{
			return amountCaptured;
		}
	
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
		public T setDelivery(Delivery delivery)
		{
			this.delivery = delivery;
			return self();
		}

		private Delivery getDelivery()
		{
			return delivery;
		}
	
		/**
		* sets 
		*
		*/
		public T setFirstArrivalLocationId(String firstArrivalLocationId)
		{
			this.firstArrivalLocationId = firstArrivalLocationId;
			return self();
		}

		private String getFirstArrivalLocationId()
		{
			return firstArrivalLocationId;
		}
	
		/**
		* sets 
		*
		*/
		public T setGrossVolume(Measure grossVolume)
		{
			this.grossVolume = grossVolume;
			return self();
		}

		private Measure getGrossVolume()
		{
			return grossVolume;
		}
	
		/**
		* sets 
		*
		*/
		public T setGrossWeight(Measure grossWeight)
		{
			this.grossWeight = grossWeight;
			return self();
		}

		private Measure getGrossWeight()
		{
			return grossWeight;
		}
	
		/**
		* sets 
		*
		*/
		public T setHandlingCode(String handlingCode)
		{
			this.handlingCode = handlingCode;
			return self();
		}

		private String getHandlingCode()
		{
			return handlingCode;
		}
	
		/**
		* sets 
		*
		*/
		public T setHandlingInstructions(String handlingInstructions)
		{
			this.handlingInstructions = handlingInstructions;
			return self();
		}

		private String getHandlingInstructions()
		{
			return handlingInstructions;
		}
	
		/**
		* sets 
		*
		*/
		public T setHeight(Measure height)
		{
			this.height = height;
			return self();
		}

		private Measure getHeight()
		{
			return height;
		}
	
		/**
		* sets 
		*
		*/
		public T setInformation(String information)
		{
			this.information = information;
			return self();
		}

		private String getInformation()
		{
			return information;
		}
	
		/**
		* sets 
		*
		*/
		public T setInsuranceValueAmount(Amount insuranceValueAmount)
		{
			this.insuranceValueAmount = insuranceValueAmount;
			return self();
		}

		private Amount getInsuranceValueAmount()
		{
			return insuranceValueAmount;
		}
	
		/**
		* sets 
		*
		*/
		public T setLastExitLocationId(String lastExitLocationId)
		{
			this.lastExitLocationId = lastExitLocationId;
			return self();
		}

		private String getLastExitLocationId()
		{
			return lastExitLocationId;
		}
	
		/**
		* sets 
		*
		*/
		public T setLength(Measure length)
		{
			this.length = length;
			return self();
		}

		private Measure getLength()
		{
			return length;
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
		public T setMerchandisePrice(Price merchandisePrice)
		{
			this.merchandisePrice = merchandisePrice;
			return self();
		}

		private Price getMerchandisePrice()
		{
			return merchandisePrice;
		}
	
		/**
		* sets 
		*
		*/
		public T setNetWeight(Measure netWeight)
		{
			this.netWeight = netWeight;
			return self();
		}

		private Measure getNetWeight()
		{
			return netWeight;
		}
	
		/**
		* sets 
		*
		*/
		public T setOlqsStatus(String olqsStatus)
		{
			this.olqsStatus = olqsStatus;
			return self();
		}

		private String getOlqsStatus()
		{
			return olqsStatus;
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
		public T setPickupInStore(boolean pickupInStore)
		{
			this.pickupInStore = pickupInStore;
			return self();
		}

		private boolean getPickupInStore()
		{
			return pickupInStore;
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
		public T setShipFrom(Address shipFrom)
		{
			this.shipFrom = shipFrom;
			return self();
		}

		private Address getShipFrom()
		{
			return shipFrom;
		}
	
		/**
		* sets 
		*
		*/
		public T setShipmentId(String shipmentId)
		{
			this.shipmentId = shipmentId;
			return self();
		}

		private String getShipmentId()
		{
			return shipmentId;
		}
	
		/**
		* sets 
		*
		*/
		public T setShippingAndHandling(ShippingAndHandling shippingAndHandling)
		{
			this.shippingAndHandling = shippingAndHandling;
			return self();
		}

		private ShippingAndHandling getShippingAndHandling()
		{
			return shippingAndHandling;
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
		public T setTotalGoodsItemQuantity(Quantity totalGoodsItemQuantity)
		{
			this.totalGoodsItemQuantity = totalGoodsItemQuantity;
			return self();
		}

		private Quantity getTotalGoodsItemQuantity()
		{
			return totalGoodsItemQuantity;
		}
	
		/**
		* sets 
		*
		*/
		public T setWidth(Measure width)
		{
			this.width = width;
			return self();
		}

		private Measure getWidth()
		{
			return width;
		}
	
		/**
		* sets 
		*
		*/
		public T setPackageDescription(String packageDescription)
		{
			this.packageDescription = packageDescription;
			return self();
		}

		private String getPackageDescription()
		{
			return packageDescription;
		}
	
		/**
		* sets 
		*
		*/
		public T setAuthUrls(List<String> authUrls)
		{
			this.authUrls = authUrls;
			return self();
		}

		private List<String> getAuthUrls()
		{
			return authUrls;
		}
	
		/**
		* sets 
		*
		*/
		public T setOlqIds(List<String> olqIds)
		{
			this.olqIds = olqIds;
			return self();
		}

		private List<String> getOlqIds()
		{
			return olqIds;
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
		public T setState(String state)
		{
			this.state = state;
			return self();
		}

		private String getState()
		{
			return state;
		}
	
		/**
		* sets 
		*
		*/
		public T setOrderLinesFulfillmentType(OrderlinesFulfillmentType orderLinesFulfillmentType)
		{
			this.orderLinesFulfillmentType = orderLinesFulfillmentType;
			return self();
		}

		private OrderlinesFulfillmentType getOrderLinesFulfillmentType()
		{
			return orderLinesFulfillmentType;
		}
	
		protected abstract T self();

		public Shipment build(){
			return new Shipment(this);
		}
	}
}

