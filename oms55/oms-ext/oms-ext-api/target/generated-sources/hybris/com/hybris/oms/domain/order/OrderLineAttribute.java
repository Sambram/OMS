

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
* Order Line Attributes
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class OrderLineAttribute extends PropertyAwareEntityDto implements Serializable
{

	public final static long serialVersionUID = 838611703L;

	private String id;

	private String description;

	
	public OrderLineAttribute(){}

	protected OrderLineAttribute(Builder builder)
	{
		super(builder); 
		setId(builder.getId());
		setDescription(builder.getDescription());
	
	}


	public OrderLineAttribute(final String description, final String id)
	{
		this.description = description;
		this.id = id;
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
		private String id;
		private String description;
		
	
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
		public T setDescription(String description)
		{
			this.description = description;
			return self();
		}

		private String getDescription()
		{
			return description;
		}
	
		protected abstract T self();

		public OrderLineAttribute build(){
			return new OrderLineAttribute(this);
		}
	}
}

