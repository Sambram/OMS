

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
import com.hybris.oms.domain.types.Quantity;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import com.hybris.oms.domain.order.OrderLine;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import com.hybris.commons.dto.impl.PropertyAwareEntityDto;
/**
* Return Order Line
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ReturnOrderLine extends PropertyAwareEntityDto implements Serializable
{

	public final static long serialVersionUID = -2002855836L;

	@XmlElement
	private OrderLine orderLine;

	@XmlElement
	@XmlID
	private String returnOrderLineId;

	private String returnOrderLineStatus;

	@XmlElement
	private Quantity quantity;

	@XmlElement
	private List<ReturnLineRejection> returnLineRejections = new ArrayList();

	
	public ReturnOrderLine(){}

	protected ReturnOrderLine(Builder builder)
	{
		super(builder); 
		setOrderLine(builder.getOrderLine());
		setReturnOrderLineId(builder.getReturnOrderLineId());
		setReturnOrderLineStatus(builder.getReturnOrderLineStatus());
		setQuantity(builder.getQuantity());
		setReturnLineRejections(builder.getReturnLineRejections());
	
	}

				
                    @Override
                    public String getId()
                    {
                        return this.returnOrderLineId;
                    }
				
			

	/**
	* gets 
	*
	* @returns OrderLine
	*/
	public OrderLine getOrderLine()
	{
		return orderLine;
	}

	/**
	* sets 
	*
	*/
	public void setOrderLine(OrderLine orderLine)
	{
		this.orderLine = orderLine;
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

	/**
	* gets 
	*
	* @returns String
	*/
	public String getReturnOrderLineStatus()
	{
		return returnOrderLineStatus;
	}

	/**
	* sets 
	*
	*/
	public void setReturnOrderLineStatus(String returnOrderLineStatus)
	{
		this.returnOrderLineStatus = returnOrderLineStatus;
	}

	/**
	* gets 
	*
	* @returns Quantity
	*/
	public Quantity getQuantity()
	{
		return quantity;
	}

	/**
	* sets 
	*
	*/
	public void setQuantity(Quantity quantity)
	{
		this.quantity = quantity;
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
		private OrderLine orderLine;
		private String returnOrderLineId;
		private String returnOrderLineStatus;
		private Quantity quantity;
		private List<ReturnLineRejection> returnLineRejections;
		
	
		/**
		* sets 
		*
		*/
		public T setOrderLine(OrderLine orderLine)
		{
			this.orderLine = orderLine;
			return self();
		}

		private OrderLine getOrderLine()
		{
			return orderLine;
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
	
		/**
		* sets 
		*
		*/
		public T setReturnOrderLineStatus(String returnOrderLineStatus)
		{
			this.returnOrderLineStatus = returnOrderLineStatus;
			return self();
		}

		private String getReturnOrderLineStatus()
		{
			return returnOrderLineStatus;
		}
	
		/**
		* sets 
		*
		*/
		public T setQuantity(Quantity quantity)
		{
			this.quantity = quantity;
			return self();
		}

		private Quantity getQuantity()
		{
			return quantity;
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

		public ReturnOrderLine build(){
			return new ReturnOrderLine(this);
		}
	}
}

