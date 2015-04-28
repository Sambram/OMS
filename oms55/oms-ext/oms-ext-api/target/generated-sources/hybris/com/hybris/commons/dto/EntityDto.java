

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

package com.hybris.commons.dto;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;


import java.io.Serializable;
/**
* Base class of all Entity DTO instances providing a contract of equality.
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
public abstract class EntityDto  implements Serializable, Dto
{

	public final static long serialVersionUID = 696022904L;

	
	public EntityDto(){}

	protected EntityDto(Builder builder)
	{
	
	
	}


	public abstract String getId();
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityDto other = (EntityDto) obj;
		if (getId() == null)
		{
			if (other.getId() != null)
				return false;
		}
		else if (!getId().equals(other.getId()))
			return false;
		return true;
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
		
	
		protected abstract T self();

	}
}

