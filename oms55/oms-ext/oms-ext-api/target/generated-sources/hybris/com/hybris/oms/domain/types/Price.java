

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
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
/**
* Represents a Price with subtotal, taxes and committed taxes.
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlRootElement
public class Price  implements ValueType<Price>, Serializable
{

	public final static long serialVersionUID = -397405177L;

	private Amount subTotal;

	private Amount tax;

	private Amount taxCommitted;

	
	public Price(){}

	protected Price(Builder builder)
	{
	
		setSubTotal(builder.getSubTotal());
		setTax(builder.getTax());
		setTaxCommitted(builder.getTaxCommitted());
	
	}


	public Price(final Amount subTotal, final Amount tax, final Amount taxCommitted)
	{
		super();
		this.subTotal = subTotal;
		this.tax = tax;
		this.taxCommitted = taxCommitted;
	}

    		

	/**
	* gets 
	*
	* @returns Amount
	*/
	public Amount getSubTotal()
	{
		return subTotal;
	}

	/**
	* sets 
	*
	*/
	public void setSubTotal(Amount subTotal)
	{
		this.subTotal = subTotal;
	}

	/**
	* gets 
	*
	* @returns Amount
	*/
	public Amount getTax()
	{
		return tax;
	}

	/**
	* sets 
	*
	*/
	public void setTax(Amount tax)
	{
		this.tax = tax;
	}

	/**
	* gets 
	*
	* @returns Amount
	*/
	public Amount getTaxCommitted()
	{
		return taxCommitted;
	}

	/**
	* sets 
	*
	*/
	public void setTaxCommitted(Amount taxCommitted)
	{
		this.taxCommitted = taxCommitted;
	}


	@Override
	public int hashCode()
	{
		return new HashCodeBuilder()
				.append(subTotal)
				.append(tax)
				.append(taxCommitted)
			.toHashCode();
	}

	@Override
	public boolean equals(final Object obj)
	{
		if(obj instanceof Price){
			final Price other = (Price) obj;
			return new EqualsBuilder()
				.append(subTotal, other.subTotal)
				.append(tax, other.tax)
				.append(taxCommitted, other.taxCommitted)
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
		private Amount subTotal;
		private Amount tax;
		private Amount taxCommitted;
		
	
		/**
		* sets 
		*
		*/
		public T setSubTotal(Amount subTotal)
		{
			this.subTotal = subTotal;
			return self();
		}

		private Amount getSubTotal()
		{
			return subTotal;
		}
	
		/**
		* sets 
		*
		*/
		public T setTax(Amount tax)
		{
			this.tax = tax;
			return self();
		}

		private Amount getTax()
		{
			return tax;
		}
	
		/**
		* sets 
		*
		*/
		public T setTaxCommitted(Amount taxCommitted)
		{
			this.taxCommitted = taxCommitted;
			return self();
		}

		private Amount getTaxCommitted()
		{
			return taxCommitted;
		}
	
		protected abstract T self();

		public Price build(){
			return new Price(this);
		}
	}
}

