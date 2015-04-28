

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

package com.hybris.oms.domain.ats;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;


import com.hybris.oms.domain.types.Quantity;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
/**
* ATS quantity
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class AtsQuantity  implements Serializable
{

	public final static long serialVersionUID = 718614322L;

	@XmlAttribute
	private String sku;

	@XmlAttribute
	private String atsId;

	private Quantity quantity;

	
	public AtsQuantity(){}

	protected AtsQuantity(Builder builder)
	{
	
		setSku(builder.getSku());
		setAtsId(builder.getAtsId());
		setQuantity(builder.getQuantity());
	
	}


	/**
	* gets 
	*
	* @returns String
	*/
	public String getSku()
	{
		return sku;
	}

	/**
	* sets 
	*
	*/
	public void setSku(String sku)
	{
		this.sku = sku;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getAtsId()
	{
		return atsId;
	}

	/**
	* sets 
	*
	*/
	public void setAtsId(String atsId)
	{
		this.atsId = atsId;
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
		private String sku;
		private String atsId;
		private Quantity quantity;
		
	
		/**
		* sets 
		*
		*/
		public T setSku(String sku)
		{
			this.sku = sku;
			return self();
		}

		private String getSku()
		{
			return sku;
		}
	
		/**
		* sets 
		*
		*/
		public T setAtsId(String atsId)
		{
			this.atsId = atsId;
			return self();
		}

		private String getAtsId()
		{
			return atsId;
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
	
		protected abstract T self();

		public AtsQuantity build(){
			return new AtsQuantity(this);
		}
	}
}

