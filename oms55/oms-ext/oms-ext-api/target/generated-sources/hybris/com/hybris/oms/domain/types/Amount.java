

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

package com.hybris.oms.domain.types;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

import com.hybris.oms.domain.ValueType;
import java.lang.Double;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
/**
* Represents an Amount with currency code and value.
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlRootElement
public class Amount  implements ValueType<Amount>, Serializable
{

	public final static long serialVersionUID = -541450947L;

	private String currencyCode;

	private Double value;

	
	public Amount(){}

	protected Amount(Builder builder)
	{
	
		setCurrencyCode(builder.getCurrencyCode());
		setValue(builder.getValue());
	
	}


	public Amount(final String currencyCode, final Double value)
	{
		this.currencyCode = currencyCode;
		this.value = value;
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
	* @returns Double
	*/
	public Double getValue()
	{
		return value;
	}

	/**
	* sets 
	*
	*/
	public void setValue(Double value)
	{
		this.value = value;
	}


	@Override
	public int hashCode()
	{
		return new HashCodeBuilder()
				.append(currencyCode)
				.append(value)
			.toHashCode();
	}

	@Override
	public boolean equals(final Object obj)
	{
		if(obj instanceof Amount){
			final Amount other = (Amount) obj;
			return new EqualsBuilder()
				.append(currencyCode, other.currencyCode)
				.append(value, other.value)
			.isEquals();
		} else{
			return false;
		}
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
		private String currencyCode;
		private Double value;
		
	
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
		public T setValue(Double value)
		{
			this.value = value;
			return self();
		}

		private Double getValue()
		{
			return value;
		}
	
		protected abstract T self();

		public Amount build(){
			return new Amount(this);
		}
	}
}

