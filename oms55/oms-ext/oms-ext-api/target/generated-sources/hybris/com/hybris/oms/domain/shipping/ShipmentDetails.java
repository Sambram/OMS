

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


import com.hybris.commons.dto.xml.bind.MapStringAdapter;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessorType;
import com.hybris.commons.dto.PropertyAwareDto;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/**
* Shipment Details
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ShipmentDetails  implements Serializable, PropertyAwareDto
{

	public final static long serialVersionUID = -1025228591L;

	private double insuranceValueAmountValue;

	private float widthValue;

	private float lengthValue;

	private float heightValue;

	private float weightValue;

	private String heightUnitCode;

	private String weightUnitCode;

	private String shippingMethod;

	@XmlJavaTypeAdapter(MapStringAdapter.class)
	@XmlElement(required = false)
	private Map<String, Serializable> properties;

	
	public ShipmentDetails(){}

	protected ShipmentDetails(Builder builder)
	{
	
		setInsuranceValueAmountValue(builder.getInsuranceValueAmountValue());
		setWidthValue(builder.getWidthValue());
		setLengthValue(builder.getLengthValue());
		setHeightValue(builder.getHeightValue());
		setWeightValue(builder.getWeightValue());
		setHeightUnitCode(builder.getHeightUnitCode());
		setWeightUnitCode(builder.getWeightUnitCode());
		setShippingMethod(builder.getShippingMethod());
		setProperties(builder.getProperties());
	
	}


	@Override
	public String getProperty(final String name)
	{
		return properties == null ? null : (String) properties.get(name);
	}

	@Override
	public String getProperty(final String name, final java.util.Locale locale)
	{
		if (properties == null)
		{
			return null;
		}
		final java.util.Map<java.util.Locale, String> locProperty = (java.util.Map<java.util.Locale, String>) properties.get(name);
		if (locProperty == null)
		{
			return null;
		}
		return locProperty.get(locale);
	}

	@Override
	public void setProperty(final String name, final String value, final java.util.Locale locale)
	{
		if (properties == null)
		{
			properties = new java.util.HashMap<>();
		}
		java.util.Map<java.util.Locale, String> locProperty = (java.util.Map<java.util.Locale, String>) properties.get(name);
		if (locProperty == null)
		{
			locProperty = new java.util.HashMap<>();
		}
		locProperty.put(locale, value);
		properties.put(name, (Serializable) locProperty);
	}

	@Override
	public void setProperty(final String name, final String value)
	{
		if (properties == null)
		{
			properties = new java.util.HashMap<>();
		}
		properties.put(name, value);
	}

    		

	/**
	* gets 
	*
	* @returns double
	*/
	public double getInsuranceValueAmountValue()
	{
		return insuranceValueAmountValue;
	}

	/**
	* sets 
	*
	*/
	public void setInsuranceValueAmountValue(double insuranceValueAmountValue)
	{
		this.insuranceValueAmountValue = insuranceValueAmountValue;
	}

	/**
	* gets 
	*
	* @returns float
	*/
	public float getWidthValue()
	{
		return widthValue;
	}

	/**
	* sets 
	*
	*/
	public void setWidthValue(float widthValue)
	{
		this.widthValue = widthValue;
	}

	/**
	* gets 
	*
	* @returns float
	*/
	public float getLengthValue()
	{
		return lengthValue;
	}

	/**
	* sets 
	*
	*/
	public void setLengthValue(float lengthValue)
	{
		this.lengthValue = lengthValue;
	}

	/**
	* gets 
	*
	* @returns float
	*/
	public float getHeightValue()
	{
		return heightValue;
	}

	/**
	* sets 
	*
	*/
	public void setHeightValue(float heightValue)
	{
		this.heightValue = heightValue;
	}

	/**
	* gets 
	*
	* @returns float
	*/
	public float getWeightValue()
	{
		return weightValue;
	}

	/**
	* sets 
	*
	*/
	public void setWeightValue(float weightValue)
	{
		this.weightValue = weightValue;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getHeightUnitCode()
	{
		return heightUnitCode;
	}

	/**
	* sets 
	*
	*/
	public void setHeightUnitCode(String heightUnitCode)
	{
		this.heightUnitCode = heightUnitCode;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getWeightUnitCode()
	{
		return weightUnitCode;
	}

	/**
	* sets 
	*
	*/
	public void setWeightUnitCode(String weightUnitCode)
	{
		this.weightUnitCode = weightUnitCode;
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
	* @returns Map<String, Serializable>
	*/
	public Map<String, Serializable> getProperties()
	{
		return properties;
	}

	/**
	* sets 
	*
	*/
	public void setProperties(Map<String, Serializable> properties)
	{
		this.properties = properties;
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
		private double insuranceValueAmountValue;
		private float widthValue;
		private float lengthValue;
		private float heightValue;
		private float weightValue;
		private String heightUnitCode;
		private String weightUnitCode;
		private String shippingMethod;
		private Map<String, Serializable> properties;
		
	
		/**
		* sets 
		*
		*/
		public T setInsuranceValueAmountValue(double insuranceValueAmountValue)
		{
			this.insuranceValueAmountValue = insuranceValueAmountValue;
			return self();
		}

		private double getInsuranceValueAmountValue()
		{
			return insuranceValueAmountValue;
		}
	
		/**
		* sets 
		*
		*/
		public T setWidthValue(float widthValue)
		{
			this.widthValue = widthValue;
			return self();
		}

		private float getWidthValue()
		{
			return widthValue;
		}
	
		/**
		* sets 
		*
		*/
		public T setLengthValue(float lengthValue)
		{
			this.lengthValue = lengthValue;
			return self();
		}

		private float getLengthValue()
		{
			return lengthValue;
		}
	
		/**
		* sets 
		*
		*/
		public T setHeightValue(float heightValue)
		{
			this.heightValue = heightValue;
			return self();
		}

		private float getHeightValue()
		{
			return heightValue;
		}
	
		/**
		* sets 
		*
		*/
		public T setWeightValue(float weightValue)
		{
			this.weightValue = weightValue;
			return self();
		}

		private float getWeightValue()
		{
			return weightValue;
		}
	
		/**
		* sets 
		*
		*/
		public T setHeightUnitCode(String heightUnitCode)
		{
			this.heightUnitCode = heightUnitCode;
			return self();
		}

		private String getHeightUnitCode()
		{
			return heightUnitCode;
		}
	
		/**
		* sets 
		*
		*/
		public T setWeightUnitCode(String weightUnitCode)
		{
			this.weightUnitCode = weightUnitCode;
			return self();
		}

		private String getWeightUnitCode()
		{
			return weightUnitCode;
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
		public T setProperties(Map<String, Serializable> properties)
		{
			this.properties = properties;
			return self();
		}

		private Map<String, Serializable> getProperties()
		{
			return properties;
		}
	
		protected abstract T self();

		public ShipmentDetails build(){
			return new ShipmentDetails(this);
		}
	}
}

