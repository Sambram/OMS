

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


import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
/**
* ATS quantities
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class AtsQuantities  implements Serializable
{

	public final static long serialVersionUID = 1006365941L;

	@XmlElement(name = "ats")
	private List<AtsQuantity> atsQuantities = new ArrayList();

	
	public AtsQuantities(){}

	protected AtsQuantities(Builder builder)
	{
	
		setAtsQuantities(builder.getAtsQuantities());
	
	}


	public void addAtsQuantity(final AtsQuantity atsQuantity)
	{
		this.atsQuantities.add(atsQuantity);
	}

    		

	/**
	* gets 
	*
	* @returns List<AtsQuantity>
	*/
	public List<AtsQuantity> getAtsQuantities()
	{
		return atsQuantities;
	}

	/**
	* sets 
	*
	*/
	public void setAtsQuantities(List<AtsQuantity> atsQuantities)
	{
		this.atsQuantities = atsQuantities;
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
		private List<AtsQuantity> atsQuantities;
		
	
		/**
		* sets 
		*
		*/
		public T setAtsQuantities(List<AtsQuantity> atsQuantities)
		{
			this.atsQuantities = atsQuantities;
			return self();
		}

		private List<AtsQuantity> getAtsQuantities()
		{
			return atsQuantities;
		}
	
		protected abstract T self();

		public AtsQuantities build(){
			return new AtsQuantities(this);
		}
	}
}

