

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

package com.hybris.oms.domain.returns;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;


import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import com.hybris.commons.dto.impl.PropertyAwareEntityDto;
/**
* ReturnReview
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ReturnReview extends PropertyAwareEntityDto implements Serializable
{

	public final static long serialVersionUID = 1431860551L;

	private String returnId;

	@XmlElement
	private List<ReturnLineRejection> returnLineRejections = new ArrayList();

	
	public ReturnReview(){}

	protected ReturnReview(Builder builder)
	{
		super(builder); 
		setReturnId(builder.getReturnId());
		setReturnLineRejections(builder.getReturnLineRejections());
	
	}

                
                    @Override
                    public String getId()
                    {
                        return null;
                    }
				
            

	/**
	* gets 
	*
	* @returns String
	*/
	public String getReturnId()
	{
		return returnId;
	}

	/**
	* sets 
	*
	*/
	public void setReturnId(String returnId)
	{
		this.returnId = returnId;
	}

	/**
	* gets 
	*
	* @returns List<ReturnLineRejection>
	*/
	public List<ReturnLineRejection> getReturnLineRejections()
	{
		return returnLineRejections;
	}

	/**
	* sets 
	*
	*/
	public void setReturnLineRejections(List<ReturnLineRejection> returnLineRejections)
	{
		this.returnLineRejections = returnLineRejections;
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
		private String returnId;
		private List<ReturnLineRejection> returnLineRejections;
		
	
		/**
		* sets 
		*
		*/
		public T setReturnId(String returnId)
		{
			this.returnId = returnId;
			return self();
		}

		private String getReturnId()
		{
			return returnId;
		}
	
		/**
		* sets 
		*
		*/
		public T setReturnLineRejections(List<ReturnLineRejection> returnLineRejections)
		{
			this.returnLineRejections = returnLineRejections;
			return self();
		}

		private List<ReturnLineRejection> getReturnLineRejections()
		{
			return returnLineRejections;
		}
	
		protected abstract T self();

		public ReturnReview build(){
			return new ReturnReview(this);
		}
	}
}

