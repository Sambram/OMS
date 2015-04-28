

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


import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import com.hybris.commons.dto.impl.PropertyAwareEntityDto;
/**
* ReturnLineRejection
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ReturnLineRejection extends PropertyAwareEntityDto implements Serializable
{

	public final static long serialVersionUID = -1002605813L;

	@XmlElement
	@XmlID
	private String rejectionId;

	private Integer quantity;

	private String responsible;

	private ReviewReason reason;

	private String returnOrderLineId;

	
	public ReturnLineRejection(){}

	protected ReturnLineRejection(Builder builder)
	{
		super(builder); 
		setRejectionId(builder.getRejectionId());
		setQuantity(builder.getQuantity());
		setResponsible(builder.getResponsible());
		setReason(builder.getReason());
		setReturnOrderLineId(builder.getReturnOrderLineId());
	
	}

                
                    @Override
                    public String getId()
                    {
                        return this.rejectionId;
                    }
				
            

	/**
	* gets 
	*
	* @returns String
	*/
	public String getRejectionId()
	{
		return rejectionId;
	}

	/**
	* sets 
	*
	*/
	public void setRejectionId(String rejectionId)
	{
		this.rejectionId = rejectionId;
	}

	/**
	* gets 
	*
	* @returns Integer
	*/
	public Integer getQuantity()
	{
		return quantity;
	}

	/**
	* sets 
	*
	*/
	public void setQuantity(Integer quantity)
	{
		this.quantity = quantity;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getResponsible()
	{
		return responsible;
	}

	/**
	* sets 
	*
	*/
	public void setResponsible(String responsible)
	{
		this.responsible = responsible;
	}

	/**
	* gets 
	*
	* @returns ReviewReason
	*/
	public ReviewReason getReason()
	{
		return reason;
	}

	/**
	* sets 
	*
	*/
	public void setReason(ReviewReason reason)
	{
		this.reason = reason;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getReturnOrderLineId()
	{
		return returnOrderLineId;
	}

	/**
	* sets 
	*
	*/
	public void setReturnOrderLineId(String returnOrderLineId)
	{
		this.returnOrderLineId = returnOrderLineId;
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
		private String rejectionId;
		private Integer quantity;
		private String responsible;
		private ReviewReason reason;
		private String returnOrderLineId;
		
	
		/**
		* sets 
		*
		*/
		public T setRejectionId(String rejectionId)
		{
			this.rejectionId = rejectionId;
			return self();
		}

		private String getRejectionId()
		{
			return rejectionId;
		}
	
		/**
		* sets 
		*
		*/
		public T setQuantity(Integer quantity)
		{
			this.quantity = quantity;
			return self();
		}

		private Integer getQuantity()
		{
			return quantity;
		}
	
		/**
		* sets 
		*
		*/
		public T setResponsible(String responsible)
		{
			this.responsible = responsible;
			return self();
		}

		private String getResponsible()
		{
			return responsible;
		}
	
		/**
		* sets 
		*
		*/
		public T setReason(ReviewReason reason)
		{
			this.reason = reason;
			return self();
		}

		private ReviewReason getReason()
		{
			return reason;
		}
	
		/**
		* sets 
		*
		*/
		public T setReturnOrderLineId(String returnOrderLineId)
		{
			this.returnOrderLineId = returnOrderLineId;
			return self();
		}

		private String getReturnOrderLineId()
		{
			return returnOrderLineId;
		}
	
		protected abstract T self();

		public ReturnLineRejection build(){
			return new ReturnLineRejection(this);
		}
	}
}

