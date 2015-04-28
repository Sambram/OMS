

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

package com.hybris.oms.domain.inventory;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;


import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import com.hybris.commons.dto.impl.PropertyAwareEntityDto;
/**
* Bin
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Bin extends PropertyAwareEntityDto implements Serializable
{

	public final static long serialVersionUID = 860715294L;

	@XmlAttribute
	private String binCode;

	private String locationId;

	private String description;

	private int priority;

	
	public Bin(){}

	protected Bin(Builder builder)
	{
		super(builder); 
		setBinCode(builder.getBinCode());
		setLocationId(builder.getLocationId());
		setDescription(builder.getDescription());
		setPriority(builder.getPriority());
	
	}


	@Override
	public String getId()
	{
		return this.locationId + "_" + this.binCode;
	}

    		

	/**
	* gets 
	*
	* @returns String
	*/
	public String getBinCode()
	{
		return binCode;
	}

	/**
	* sets 
	*
	*/
	public void setBinCode(String binCode)
	{
		this.binCode = binCode;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getLocationId()
	{
		return locationId;
	}

	/**
	* sets 
	*
	*/
	public void setLocationId(String locationId)
	{
		this.locationId = locationId;
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
	* @returns int
	*/
	public int getPriority()
	{
		return priority;
	}

	/**
	* sets 
	*
	*/
	public void setPriority(int priority)
	{
		this.priority = priority;
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
		private String binCode;
		private String locationId;
		private String description;
		private int priority;
		
	
		/**
		* sets 
		*
		*/
		public T setBinCode(String binCode)
		{
			this.binCode = binCode;
			return self();
		}

		private String getBinCode()
		{
			return binCode;
		}
	
		/**
		* sets 
		*
		*/
		public T setLocationId(String locationId)
		{
			this.locationId = locationId;
			return self();
		}

		private String getLocationId()
		{
			return locationId;
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
		public T setPriority(int priority)
		{
			this.priority = priority;
			return self();
		}

		private int getPriority()
		{
			return priority;
		}
	
		protected abstract T self();

		public Bin build(){
			return new Bin(this);
		}
	}
}

