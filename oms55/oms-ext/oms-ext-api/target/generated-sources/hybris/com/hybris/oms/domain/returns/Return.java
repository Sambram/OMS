

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
import com.hybris.oms.domain.returns.ReturnShipment;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import com.hybris.oms.domain.returns.ReturnPaymentInfo;
import com.hybris.oms.domain.ActionableDto;
import com.hybris.oms.domain.types.Amount;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import java.io.Serializable;
import com.hybris.commons.dto.impl.PropertyAwareEntityDto;
/**
* Return
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "return")
public class Return extends PropertyAwareEntityDto implements ActionableDto, Serializable
{

	public final static long serialVersionUID = 401815080L;

	@XmlElement
	@XmlID
	private String returnId;

	private String state;

	@XmlElement
	private String orderId;

	private Amount customTotalRefundAmount;

	private Amount calculatedTotalRefundAmount;

	@XmlElement
	private String returnReasonCode;

	@XmlElement
	private List<ReturnOrderLine> returnOrderLines = new ArrayList();

	private String returnLocationId;

	private ReturnShipment returnShipment;

	@XmlElement
	private ReturnPaymentInfo returnPaymentInfos;

	@XmlElement
	private Boolean shippingRefunded = Boolean.FALSE;

	private Set<String> actions;

	
	public Return(){}

	protected Return(Builder builder)
	{
		super(builder); 
		setReturnId(builder.getReturnId());
		setState(builder.getState());
		setOrderId(builder.getOrderId());
		setCustomTotalRefundAmount(builder.getCustomTotalRefundAmount());
		setCalculatedTotalRefundAmount(builder.getCalculatedTotalRefundAmount());
		setReturnReasonCode(builder.getReturnReasonCode());
		setReturnOrderLines(builder.getReturnOrderLines());
		setReturnLocationId(builder.getReturnLocationId());
		setReturnShipment(builder.getReturnShipment());
		setReturnPaymentInfos(builder.getReturnPaymentInfos());
		setShippingRefunded(builder.getShippingRefunded());
		setActions(builder.getActions());
	
	}

				
                    @Override
                    public String getId()
                    {
                        return this.returnId;
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
	* @returns String
	*/
	public String getState()
	{
		return state;
	}

	/**
	* sets 
	*
	*/
	public void setState(String state)
	{
		this.state = state;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getOrderId()
	{
		return orderId;
	}

	/**
	* sets 
	*
	*/
	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}

	/**
	* gets 
	*
	* @returns Amount
	*/
	public Amount getCustomTotalRefundAmount()
	{
		return customTotalRefundAmount;
	}

	/**
	* sets 
	*
	*/
	public void setCustomTotalRefundAmount(Amount customTotalRefundAmount)
	{
		this.customTotalRefundAmount = customTotalRefundAmount;
	}

	/**
	* gets 
	*
	* @returns Amount
	*/
	public Amount getCalculatedTotalRefundAmount()
	{
		return calculatedTotalRefundAmount;
	}

	/**
	* sets 
	*
	*/
	public void setCalculatedTotalRefundAmount(Amount calculatedTotalRefundAmount)
	{
		this.calculatedTotalRefundAmount = calculatedTotalRefundAmount;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getReturnReasonCode()
	{
		return returnReasonCode;
	}

	/**
	* sets 
	*
	*/
	public void setReturnReasonCode(String returnReasonCode)
	{
		this.returnReasonCode = returnReasonCode;
	}

	/**
	* gets 
	*
	* @returns List<ReturnOrderLine>
	*/
	public List<ReturnOrderLine> getReturnOrderLines()
	{
		return returnOrderLines;
	}

	/**
	* sets 
	*
	*/
	public void setReturnOrderLines(List<ReturnOrderLine> returnOrderLines)
	{
		this.returnOrderLines = returnOrderLines;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getReturnLocationId()
	{
		return returnLocationId;
	}

	/**
	* sets 
	*
	*/
	public void setReturnLocationId(String returnLocationId)
	{
		this.returnLocationId = returnLocationId;
	}

	/**
	* gets 
	*
	* @returns ReturnShipment
	*/
	public ReturnShipment getReturnShipment()
	{
		return returnShipment;
	}

	/**
	* sets 
	*
	*/
	public void setReturnShipment(ReturnShipment returnShipment)
	{
		this.returnShipment = returnShipment;
	}

	/**
	* gets 
	*
	* @returns ReturnPaymentInfo
	*/
	public ReturnPaymentInfo getReturnPaymentInfos()
	{
		return returnPaymentInfos;
	}

	/**
	* sets 
	*
	*/
	public void setReturnPaymentInfos(ReturnPaymentInfo returnPaymentInfos)
	{
		this.returnPaymentInfos = returnPaymentInfos;
	}

	/**
	* gets 
	*
	* @returns Boolean
	*/
	public Boolean getShippingRefunded()
	{
		return shippingRefunded;
	}

	/**
	* sets 
	*
	*/
	public void setShippingRefunded(Boolean shippingRefunded)
	{
		this.shippingRefunded = shippingRefunded;
	}

	/**
	* gets 
	*
	* @returns Set<String>
	*/
	public Set<String> getActions()
	{
		return actions;
	}

	/**
	* sets 
	*
	*/
	public void setActions(Set<String> actions)
	{
		this.actions = actions;
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
		private String state;
		private String orderId;
		private Amount customTotalRefundAmount;
		private Amount calculatedTotalRefundAmount;
		private String returnReasonCode;
		private List<ReturnOrderLine> returnOrderLines;
		private String returnLocationId;
		private ReturnShipment returnShipment;
		private ReturnPaymentInfo returnPaymentInfos;
		private Boolean shippingRefunded;
		private Set<String> actions;
		
	
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
		public T setState(String state)
		{
			this.state = state;
			return self();
		}

		private String getState()
		{
			return state;
		}
	
		/**
		* sets 
		*
		*/
		public T setOrderId(String orderId)
		{
			this.orderId = orderId;
			return self();
		}

		private String getOrderId()
		{
			return orderId;
		}
	
		/**
		* sets 
		*
		*/
		public T setCustomTotalRefundAmount(Amount customTotalRefundAmount)
		{
			this.customTotalRefundAmount = customTotalRefundAmount;
			return self();
		}

		private Amount getCustomTotalRefundAmount()
		{
			return customTotalRefundAmount;
		}
	
		/**
		* sets 
		*
		*/
		public T setCalculatedTotalRefundAmount(Amount calculatedTotalRefundAmount)
		{
			this.calculatedTotalRefundAmount = calculatedTotalRefundAmount;
			return self();
		}

		private Amount getCalculatedTotalRefundAmount()
		{
			return calculatedTotalRefundAmount;
		}
	
		/**
		* sets 
		*
		*/
		public T setReturnReasonCode(String returnReasonCode)
		{
			this.returnReasonCode = returnReasonCode;
			return self();
		}

		private String getReturnReasonCode()
		{
			return returnReasonCode;
		}
	
		/**
		* sets 
		*
		*/
		public T setReturnOrderLines(List<ReturnOrderLine> returnOrderLines)
		{
			this.returnOrderLines = returnOrderLines;
			return self();
		}

		private List<ReturnOrderLine> getReturnOrderLines()
		{
			return returnOrderLines;
		}
	
		/**
		* sets 
		*
		*/
		public T setReturnLocationId(String returnLocationId)
		{
			this.returnLocationId = returnLocationId;
			return self();
		}

		private String getReturnLocationId()
		{
			return returnLocationId;
		}
	
		/**
		* sets 
		*
		*/
		public T setReturnShipment(ReturnShipment returnShipment)
		{
			this.returnShipment = returnShipment;
			return self();
		}

		private ReturnShipment getReturnShipment()
		{
			return returnShipment;
		}
	
		/**
		* sets 
		*
		*/
		public T setReturnPaymentInfos(ReturnPaymentInfo returnPaymentInfos)
		{
			this.returnPaymentInfos = returnPaymentInfos;
			return self();
		}

		private ReturnPaymentInfo getReturnPaymentInfos()
		{
			return returnPaymentInfos;
		}
	
		/**
		* sets 
		*
		*/
		public T setShippingRefunded(Boolean shippingRefunded)
		{
			this.shippingRefunded = shippingRefunded;
			return self();
		}

		private Boolean getShippingRefunded()
		{
			return shippingRefunded;
		}
	
		/**
		* sets 
		*
		*/
		public T setActions(Set<String> actions)
		{
			this.actions = actions;
			return self();
		}

		private Set<String> getActions()
		{
			return actions;
		}
	
		protected abstract T self();

		public Return build(){
			return new Return(this);
		}
	}
}

