

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

package com.hybris.oms.domain.returns;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;


import com.hybris.oms.domain.types.Measure;
import com.hybris.oms.domain.types.Amount;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import com.hybris.commons.dto.impl.PropertyAwareEntityDto;
/**
* Return Shipment
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlRootElement
public class ReturnShipment extends PropertyAwareEntityDto implements Serializable
{

	public final static long serialVersionUID = -676033436L;

	private String returnShipmentId;

	private String shippingMethod;

	private String packageDescription;

	private String note;

	private Measure grossWeight;

	private Measure height;

	private Measure length;

	private Measure width;

	private Amount insuranceValueAmount;

	private String labelUrl;

	private String trackingId;

	private String trackingUrl;

	
	public ReturnShipment(){}

	protected ReturnShipment(Builder builder)
	{
		super(builder); 
		setReturnShipmentId(builder.getReturnShipmentId());
		setShippingMethod(builder.getShippingMethod());
		setPackageDescription(builder.getPackageDescription());
		setNote(builder.getNote());
		setGrossWeight(builder.getGrossWeight());
		setHeight(builder.getHeight());
		setLength(builder.getLength());
		setWidth(builder.getWidth());
		setInsuranceValueAmount(builder.getInsuranceValueAmount());
		setLabelUrl(builder.getLabelUrl());
		setTrackingId(builder.getTrackingId());
		setTrackingUrl(builder.getTrackingUrl());
	
	}

				
                    @Override
                    public String getId()
                    {
                        return returnShipmentId;
                    }
				
			

	/**
	* gets 
	*
	* @returns String
	*/
	public String getReturnShipmentId()
	{
		return returnShipmentId;
	}

	/**
	* sets 
	*
	*/
	public void setReturnShipmentId(String returnShipmentId)
	{
		this.returnShipmentId = returnShipmentId;
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
	public String getTrackingId()
	{
		return trackingId;
	}

	/**
	* sets 
	*
	*/
	public void setTrackingId(String trackingId)
	{
		this.trackingId = trackingId;
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
		private String returnShipmentId;
		private String shippingMethod;
		private String packageDescription;
		private String note;
		private Measure grossWeight;
		private Measure height;
		private Measure length;
		private Measure width;
		private Amount insuranceValueAmount;
		private String labelUrl;
		private String trackingId;
		private String trackingUrl;
		
	
		/**
		* sets 
		*
		*/
		public T setReturnShipmentId(String returnShipmentId)
		{
			this.returnShipmentId = returnShipmentId;
			return self();
		}

		private String getReturnShipmentId()
		{
			return returnShipmentId;
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
		public T setTrackingId(String trackingId)
		{
			this.trackingId = trackingId;
			return self();
		}

		private String getTrackingId()
		{
			return trackingId;
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
	
		protected abstract T self();

		public ReturnShipment build(){
			return new ReturnShipment(this);
		}
	}
}

