

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

package com.hybris.oms.api.inventory;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;


import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.io.Serializable;
/**
* Simplified inventory view.
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlRootElement
public class OmsInventory  implements Serializable
{

	public final static long serialVersionUID = -428735676L;

	private Date deliveryDate;

	private String locationId;

	private String skuId;

	private String status;

	private String unitCode;

	private String binCode;

	private int quantity;

	
	public OmsInventory(){}

	protected OmsInventory(Builder builder)
	{
	
		setDeliveryDate(builder.getDeliveryDate());
		setLocationId(builder.getLocationId());
		setSkuId(builder.getSkuId());
		setStatus(builder.getStatus());
		setUnitCode(builder.getUnitCode());
		setBinCode(builder.getBinCode());
		setQuantity(builder.getQuantity());
	
	}


	/**
	 * Instantiates a new oms inventory.
	 *
	 * @param deliveryDate the delivery date
	 * @param locationId the location id
	 * @param skuId the sku id
	 * @param status the status
	 * @throws IllegalArgumentException if preconditions are not met.
	 */
	public OmsInventory(final Date deliveryDate, final String locationId, final String skuId, final String status)
			throws IllegalArgumentException
	{
		this.deliveryDate = deliveryDate;
		this.locationId = locationId;
		this.skuId = skuId;
		this.status = status;
	}

	/**
	 * Instantiates a new oms inventory.
	 *
	 * @param deliveryDate the delivery date
	 * @param locationId the location id
	 * @param skuId the sku id
	 * @param status the status
	 * @param binCode the binCode
	 * @throws IllegalArgumentException if preconditions are not met.
	 */
	public OmsInventory(final Date deliveryDate, final String locationId, final String skuId, final String status,
			final String binCode) throws IllegalArgumentException
	{
		this(deliveryDate, locationId, skuId, status);
		this.binCode = binCode;
	}

	public OmsInventory(final OmsInventory other) throws IllegalArgumentException
	{
		this.deliveryDate = other.getDeliveryDate();
		this.locationId = other.getLocationId();
		this.quantity = other.getQuantity();
		this.skuId = other.getSkuId();
		this.status = other.getStatus();
		this.unitCode = other.getUnitCode();
		this.binCode = other.getBinCode();
	}
			
			

	/**
	* gets 
	*
	* @returns Date
	*/
	public Date getDeliveryDate()
	{
		return deliveryDate;
	}

	/**
	* sets 
	*
	*/
	public void setDeliveryDate(Date deliveryDate)
	{
		this.deliveryDate = deliveryDate;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getLocationId()
	{
		return locationId;
	}

	/**
	* sets 
	*
	*/
	public void setLocationId(String locationId)
	{
		this.locationId = locationId;
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
	* @returns String
	*/
	public String getUnitCode()
	{
		return unitCode;
	}

	/**
	* sets 
	*
	*/
	public void setUnitCode(String unitCode)
	{
		this.unitCode = unitCode;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getBinCode()
	{
		return binCode;
	}

	/**
	* sets 
	*
	*/
	public void setBinCode(String binCode)
	{
		this.binCode = binCode;
	}

	/**
	* gets 
	*
	* @returns int
	*/
	public int getQuantity()
	{
		return quantity;
	}

	/**
	* sets 
	*
	*/
	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
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
		private Date deliveryDate;
		private String locationId;
		private String skuId;
		private String status;
		private String unitCode;
		private String binCode;
		private int quantity;
		
	
		/**
		* sets 
		*
		*/
		public T setDeliveryDate(Date deliveryDate)
		{
			this.deliveryDate = deliveryDate;
			return self();
		}

		private Date getDeliveryDate()
		{
			return deliveryDate;
		}
	
		/**
		* sets 
		*
		*/
		public T setLocationId(String locationId)
		{
			this.locationId = locationId;
			return self();
		}

		private String getLocationId()
		{
			return locationId;
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
		public T setUnitCode(String unitCode)
		{
			this.unitCode = unitCode;
			return self();
		}

		private String getUnitCode()
		{
			return unitCode;
		}
	
		/**
		* sets 
		*
		*/
		public T setBinCode(String binCode)
		{
			this.binCode = binCode;
			return self();
		}

		private String getBinCode()
		{
			return binCode;
		}
	
		/**
		* sets 
		*
		*/
		public T setQuantity(int quantity)
		{
			this.quantity = quantity;
			return self();
		}

		private int getQuantity()
		{
			return quantity;
		}
	
		protected abstract T self();

		public OmsInventory build(){
			return new OmsInventory(this);
		}
	}
}

