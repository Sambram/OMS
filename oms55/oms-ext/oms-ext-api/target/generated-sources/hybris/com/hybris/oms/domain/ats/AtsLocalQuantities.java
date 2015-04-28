

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

package com.hybris.oms.domain.ats;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import java.io.Serializable;
/**
* Local ATS quantities
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "atsLocal")
public class AtsLocalQuantities extends AtsQuantities implements Serializable
{

	public final static long serialVersionUID = -1716413768L;

	private String locationId;

	
	public AtsLocalQuantities(){}

	protected AtsLocalQuantities(Builder builder)
	{
		super(builder); 
		setLocationId(builder.getLocationId());
	
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


	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().appendSuper(super.hashCode())
				.append(locationId)
			.toHashCode();
	}

	@Override
	public boolean equals(final Object obj)
	{
		if(obj instanceof AtsLocalQuantities){
			final AtsLocalQuantities other = (AtsLocalQuantities) obj;
			return new EqualsBuilder().appendSuper(super.equals(obj))
				.append(locationId, other.locationId)
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

	public abstract static class Builder<T extends Builder<T>> extends AtsQuantities.Builder<T>
	{
		private String locationId;
		
	
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
	
		protected abstract T self();

		public AtsLocalQuantities build(){
			return new AtsLocalQuantities(this);
		}
	}
}

