

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

package com.hybris.oms.api.dto;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;


import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import java.io.Serializable;
import com.hybris.commons.dto.impl.PropertyAwareEntityDto;
/**
* Sample DTO for oms-extension
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class DummyObject extends PropertyAwareEntityDto implements Serializable
{

	public final static long serialVersionUID = -387254986L;

	@XmlID
	private String name;

	private String dummyProperty;

	
	public DummyObject(){}

	protected DummyObject(Builder builder)
	{
		super(builder); 
		setName(builder.getName());
		setDummyProperty(builder.getDummyProperty());
	
	}

				
	@Override
	public String getId()
	{
		return this.name;
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
	public String getDummyProperty()
	{
		return dummyProperty;
	}

	/**
	* sets 
	*
	*/
	public void setDummyProperty(String dummyProperty)
	{
		this.dummyProperty = dummyProperty;
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
		private String dummyProperty;
		
	
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
		public T setDummyProperty(String dummyProperty)
		{
			this.dummyProperty = dummyProperty;
			return self();
		}

		private String getDummyProperty()
		{
			return dummyProperty;
		}
	
		protected abstract T self();

		public DummyObject build(){
			return new DummyObject(this);
		}
	}
}

