

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

package com.hybris.commons.dto.impl;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;


import javax.annotation.concurrent.NotThreadSafe;
import com.hybris.commons.dto.xml.bind.MapStringAdapter;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessorType;
import com.hybris.commons.dto.PropertyAwareDto;
import com.hybris.commons.dto.Dto;
import javax.xml.bind.annotation.XmlAccessType;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/**
* Represents a dto which is property aware but not an Entity Dto.
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlAccessorType(XmlAccessType.FIELD)
@NotThreadSafe
public class AbstractPropertyAwareDto  implements Serializable, PropertyAwareDto, Dto
{

	public final static long serialVersionUID = 933043910L;

	@XmlJavaTypeAdapter(MapStringAdapter.class)
	@XmlElement(required = false)
	private Map<String, Serializable> properties;

	
	public AbstractPropertyAwareDto(){}

	protected AbstractPropertyAwareDto(Builder builder)
	{
	
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
		final Map<java.util.Locale, String> locProperty = (Map<java.util.Locale, String>) properties.get(name);
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
		Map<java.util.Locale, String> locProperty = (Map<java.util.Locale, String>) properties.get(name);
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
		private Map<String, Serializable> properties;
		
	
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

		public AbstractPropertyAwareDto build(){
			return new AbstractPropertyAwareDto(this);
		}
	}
}

