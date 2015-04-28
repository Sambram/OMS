

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


import com.hybris.oms.domain.address.Address;
import javax.xml.bind.annotation.XmlAccessorType;
import com.hybris.oms.domain.types.Amount;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import java.io.Serializable;
import com.hybris.commons.dto.impl.PropertyAwareEntityDto;
/**
* Payment Info
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class PaymentInfo extends PropertyAwareEntityDto implements Serializable
{

	public final static long serialVersionUID = -1637010370L;

	private String authUrl;

	private Address billingAddress;

	private Amount paymentAmount;

	private String paymentInfoType;

	private String id;

	private String captureId;

	
	public PaymentInfo(){}

	protected PaymentInfo(Builder builder)
	{
		super(builder); 
		setAuthUrl(builder.getAuthUrl());
		setBillingAddress(builder.getBillingAddress());
		setPaymentAmount(builder.getPaymentAmount());
		setPaymentInfoType(builder.getPaymentInfoType());
		setId(builder.getId());
		setCaptureId(builder.getCaptureId());
	
	}


	/**
	* gets 
	*
	* @returns String
	*/
	public String getAuthUrl()
	{
		return authUrl;
	}

	/**
	* sets 
	*
	*/
	public void setAuthUrl(String authUrl)
	{
		this.authUrl = authUrl;
	}

	/**
	* gets 
	*
	* @returns Address
	*/
	public Address getBillingAddress()
	{
		return billingAddress;
	}

	/**
	* sets 
	*
	*/
	public void setBillingAddress(Address billingAddress)
	{
		this.billingAddress = billingAddress;
	}

	/**
	* gets 
	*
	* @returns Amount
	*/
	public Amount getPaymentAmount()
	{
		return paymentAmount;
	}

	/**
	* sets 
	*
	*/
	public void setPaymentAmount(Amount paymentAmount)
	{
		this.paymentAmount = paymentAmount;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getPaymentInfoType()
	{
		return paymentInfoType;
	}

	/**
	* sets 
	*
	*/
	public void setPaymentInfoType(String paymentInfoType)
	{
		this.paymentInfoType = paymentInfoType;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getId()
	{
		return id;
	}

	/**
	* sets 
	*
	*/
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getCaptureId()
	{
		return captureId;
	}

	/**
	* sets 
	*
	*/
	public void setCaptureId(String captureId)
	{
		this.captureId = captureId;
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
		private String authUrl;
		private Address billingAddress;
		private Amount paymentAmount;
		private String paymentInfoType;
		private String id;
		private String captureId;
		
	
		/**
		* sets 
		*
		*/
		public T setAuthUrl(String authUrl)
		{
			this.authUrl = authUrl;
			return self();
		}

		private String getAuthUrl()
		{
			return authUrl;
		}
	
		/**
		* sets 
		*
		*/
		public T setBillingAddress(Address billingAddress)
		{
			this.billingAddress = billingAddress;
			return self();
		}

		private Address getBillingAddress()
		{
			return billingAddress;
		}
	
		/**
		* sets 
		*
		*/
		public T setPaymentAmount(Amount paymentAmount)
		{
			this.paymentAmount = paymentAmount;
			return self();
		}

		private Amount getPaymentAmount()
		{
			return paymentAmount;
		}
	
		/**
		* sets 
		*
		*/
		public T setPaymentInfoType(String paymentInfoType)
		{
			this.paymentInfoType = paymentInfoType;
			return self();
		}

		private String getPaymentInfoType()
		{
			return paymentInfoType;
		}
	
		/**
		* sets 
		*
		*/
		public T setId(String id)
		{
			this.id = id;
			return self();
		}

		private String getId()
		{
			return id;
		}
	
		/**
		* sets 
		*
		*/
		public T setCaptureId(String captureId)
		{
			this.captureId = captureId;
			return self();
		}

		private String getCaptureId()
		{
			return captureId;
		}
	
		protected abstract T self();

		public PaymentInfo build(){
			return new PaymentInfo(this);
		}
	}
}

