

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


import javax.xml.bind.annotation.XmlAccessorType;
import com.hybris.oms.domain.types.Amount;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import com.hybris.commons.dto.impl.PropertyAwareEntityDto;
/**
* Return Payment Info
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ReturnPaymentInfo extends PropertyAwareEntityDto implements Serializable
{

	public final static long serialVersionUID = -601471385L;

	private Amount returnPaymentAmount;

	@XmlElement
	private String returnPaymentType;

	@XmlElement
	private String returnPaymentInfoId;

	private Amount taxReversed;

	
	public ReturnPaymentInfo(){}

	protected ReturnPaymentInfo(Builder builder)
	{
		super(builder); 
		setReturnPaymentAmount(builder.getReturnPaymentAmount());
		setReturnPaymentType(builder.getReturnPaymentType());
		setReturnPaymentInfoId(builder.getReturnPaymentInfoId());
		setTaxReversed(builder.getTaxReversed());
	
	}

				
                    @Override
                    public String getId()
                    {
                        return this.returnPaymentInfoId;
                    }
				
			

	/**
	* gets 
	*
	* @returns Amount
	*/
	public Amount getReturnPaymentAmount()
	{
		return returnPaymentAmount;
	}

	/**
	* sets 
	*
	*/
	public void setReturnPaymentAmount(Amount returnPaymentAmount)
	{
		this.returnPaymentAmount = returnPaymentAmount;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getReturnPaymentType()
	{
		return returnPaymentType;
	}

	/**
	* sets 
	*
	*/
	public void setReturnPaymentType(String returnPaymentType)
	{
		this.returnPaymentType = returnPaymentType;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getReturnPaymentInfoId()
	{
		return returnPaymentInfoId;
	}

	/**
	* sets 
	*
	*/
	public void setReturnPaymentInfoId(String returnPaymentInfoId)
	{
		this.returnPaymentInfoId = returnPaymentInfoId;
	}

	/**
	* gets 
	*
	* @returns Amount
	*/
	public Amount getTaxReversed()
	{
		return taxReversed;
	}

	/**
	* sets 
	*
	*/
	public void setTaxReversed(Amount taxReversed)
	{
		this.taxReversed = taxReversed;
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
		private Amount returnPaymentAmount;
		private String returnPaymentType;
		private String returnPaymentInfoId;
		private Amount taxReversed;
		
	
		/**
		* sets 
		*
		*/
		public T setReturnPaymentAmount(Amount returnPaymentAmount)
		{
			this.returnPaymentAmount = returnPaymentAmount;
			return self();
		}

		private Amount getReturnPaymentAmount()
		{
			return returnPaymentAmount;
		}
	
		/**
		* sets 
		*
		*/
		public T setReturnPaymentType(String returnPaymentType)
		{
			this.returnPaymentType = returnPaymentType;
			return self();
		}

		private String getReturnPaymentType()
		{
			return returnPaymentType;
		}
	
		/**
		* sets 
		*
		*/
		public T setReturnPaymentInfoId(String returnPaymentInfoId)
		{
			this.returnPaymentInfoId = returnPaymentInfoId;
			return self();
		}

		private String getReturnPaymentInfoId()
		{
			return returnPaymentInfoId;
		}
	
		/**
		* sets 
		*
		*/
		public T setTaxReversed(Amount taxReversed)
		{
			this.taxReversed = taxReversed;
			return self();
		}

		private Amount getTaxReversed()
		{
			return taxReversed;
		}
	
		protected abstract T self();

		public ReturnPaymentInfo build(){
			return new ReturnPaymentInfo(this);
		}
	}
}

