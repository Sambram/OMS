

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


import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import java.io.Serializable;
import com.hybris.commons.dto.impl.PropertyAwareEntityDto;
/**
* Order Statuses
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class OrderLineQuantityStatus extends PropertyAwareEntityDto implements Serializable
{

	public final static long serialVersionUID = -1091344054L;

	private String statusCode;

	private String description;

	private boolean active;

	
	public OrderLineQuantityStatus(){}

	protected OrderLineQuantityStatus(Builder builder)
	{
		super(builder); 
		setStatusCode(builder.getStatusCode());
		setDescription(builder.getDescription());
		setActive(builder.getActive());
	
	}


	@Override
	public String getId()
	{
		return statusCode;
	}

    		

	/**
	* gets 
	*
	* @returns String
	*/
	public String getStatusCode()
	{
		return statusCode;
	}

	/**
	* sets 
	*
	*/
	public void setStatusCode(String statusCode)
	{
		this.statusCode = statusCode;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getDescription()
	{
		return description;
	}

	/**
	* sets 
	*
	*/
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	* gets 
	*
	* @returns boolean
	*/
	public boolean getActive()
	{
		return active;
	}

	/**
	* sets 
	*
	*/
	public void setActive(boolean active)
	{
		this.active = active;
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
		private String statusCode;
		private String description;
		private boolean active;
		
	
		/**
		* sets 
		*
		*/
		public T setStatusCode(String statusCode)
		{
			this.statusCode = statusCode;
			return self();
		}

		private String getStatusCode()
		{
			return statusCode;
		}
	
		/**
		* sets 
		*
		*/
		public T setDescription(String description)
		{
			this.description = description;
			return self();
		}

		private String getDescription()
		{
			return description;
		}
	
		/**
		* sets 
		*
		*/
		public T setActive(boolean active)
		{
			this.active = active;
			return self();
		}

		private boolean getActive()
		{
			return active;
		}
	
		protected abstract T self();

		public OrderLineQuantityStatus build(){
			return new OrderLineQuantityStatus(this);
		}
	}
}

