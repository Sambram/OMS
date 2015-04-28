

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

package com.hybris.oms.ui.api.returns;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;


import com.hybris.oms.domain.returns.Return;
import com.hybris.commons.dto.Dto;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import com.hybris.oms.domain.order.Order;
/**
* ReturnDetail
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlRootElement
public class ReturnDetail  implements Serializable, Dto
{

	public final static long serialVersionUID = 832586880L;

	private Order order;

	private Return aReturn;

	private boolean previouslyrefundedshippingcost;

	private boolean onlineReturn;

	
	public ReturnDetail(){}

	protected ReturnDetail(Builder builder)
	{
	
		setOrder(builder.getOrder());
		setAReturn(builder.getAReturn());
		setPreviouslyrefundedshippingcost(builder.getPreviouslyrefundedshippingcost());
		setOnlineReturn(builder.getOnlineReturn());
	
	}

				
                   	@Override
					public boolean equals(final Object otherObject)
					{
						if (this == otherObject)
						{
							return true;
						}
						if (otherObject == null || this.getClass() != otherObject.getClass())
						{
							return false;
						}
						return java.util.Objects.equals(aReturn, ((ReturnDetail) otherObject).aReturn)
								&& java.util.Objects.equals(order, ((ReturnDetail) otherObject).order);
					}
					
					@Override
					public int hashCode()
					{
						return java.util.Objects.hash(aReturn);
					}
					
					public boolean isOnlineReturn()
					{
						return onlineReturn;
					}
					
					public Return getReturn()
					{
						return this.aReturn;
					}
					
					public void setReturn(final Return aReturn)
					{
						this.aReturn = aReturn;
					}
					
					
				
			

	/**
	* gets 
	*
	* @returns Order
	*/
	public Order getOrder()
	{
		return order;
	}

	/**
	* sets 
	*
	*/
	public void setOrder(Order order)
	{
		this.order = order;
	}

	/**
	* gets 
	*
	* @returns Return
	*/
	public Return getAReturn()
	{
		return aReturn;
	}

	/**
	* sets 
	*
	*/
	public void setAReturn(Return aReturn)
	{
		this.aReturn = aReturn;
	}

	/**
	* gets 
	*
	* @returns boolean
	*/
	public boolean getPreviouslyrefundedshippingcost()
	{
		return previouslyrefundedshippingcost;
	}

	/**
	* sets 
	*
	*/
	public void setPreviouslyrefundedshippingcost(boolean previouslyrefundedshippingcost)
	{
		this.previouslyrefundedshippingcost = previouslyrefundedshippingcost;
	}

	/**
	* gets 
	*
	* @returns boolean
	*/
	public boolean getOnlineReturn()
	{
		return onlineReturn;
	}

	/**
	* sets 
	*
	*/
	public void setOnlineReturn(boolean onlineReturn)
	{
		this.onlineReturn = onlineReturn;
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
		private Order order;
		private Return aReturn;
		private boolean previouslyrefundedshippingcost;
		private boolean onlineReturn;
		
	
		/**
		* sets 
		*
		*/
		public T setOrder(Order order)
		{
			this.order = order;
			return self();
		}

		private Order getOrder()
		{
			return order;
		}
	
		/**
		* sets 
		*
		*/
		public T setAReturn(Return aReturn)
		{
			this.aReturn = aReturn;
			return self();
		}

		private Return getAReturn()
		{
			return aReturn;
		}
	
		/**
		* sets 
		*
		*/
		public T setPreviouslyrefundedshippingcost(boolean previouslyrefundedshippingcost)
		{
			this.previouslyrefundedshippingcost = previouslyrefundedshippingcost;
			return self();
		}

		private boolean getPreviouslyrefundedshippingcost()
		{
			return previouslyrefundedshippingcost;
		}
	
		/**
		* sets 
		*
		*/
		public T setOnlineReturn(boolean onlineReturn)
		{
			this.onlineReturn = onlineReturn;
			return self();
		}

		private boolean getOnlineReturn()
		{
			return onlineReturn;
		}
	
		protected abstract T self();

		public ReturnDetail build(){
			return new ReturnDetail(this);
		}
	}
}

