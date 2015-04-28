

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

package com.hybris.oms.domain.basestore;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;


import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import java.io.Serializable;
import com.hybris.commons.dto.impl.PropertyAwareEntityDto;
/**
* Base Store
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class BaseStore extends PropertyAwareEntityDto implements Serializable
{

	public final static long serialVersionUID = 1446641304L;

	private String name;

	private String description;

	
	public BaseStore(){}

	protected BaseStore(Builder builder)
	{
		super(builder); 
		setName(builder.getName());
		setDescription(builder.getDescription());
	
	}


	@Override
	public String getId()
	{
		return name;
	}

    		

	/**
	* gets 
	*
	* @returns String
	*/
	public String getName()
	{
		return name;
	}

	/**
	* sets 
	*
	*/
	public void setName(String name)
	{
		this.name = name;
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
		private String name;
		private String description;
		
	
		/**
		* sets 
		*
		*/
		public T setName(String name)
		{
			this.name = name;
			return self();
		}

		private String getName()
		{
			return name;
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

		public BaseStore build(){
			return new BaseStore(this);
		}
	}
}

