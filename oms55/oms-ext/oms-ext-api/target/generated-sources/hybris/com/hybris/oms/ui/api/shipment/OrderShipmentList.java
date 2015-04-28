

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

package com.hybris.oms.ui.api.shipment;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;


import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import com.hybris.oms.ui.api.shipment.OrderShipmentDetail;
/**
* OrderShipmentList
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlRootElement
public class OrderShipmentList  implements Serializable
{

	public final static long serialVersionUID = 2048456728L;

	private List<OrderShipmentDetail> orderShipmentDetails = new ArrayList();

	
	public OrderShipmentList(){}

	protected OrderShipmentList(Builder builder)
	{
	
		setOrderShipmentDetails(builder.getOrderShipmentDetails());
	
	}

			
	
	public void addOrderShipment(final OrderShipmentDetail orderShipmentVO)
	{
		if (this.orderShipmentDetails == null)
		{
			this.orderShipmentDetails = new java.util.ArrayList<>();
		}
		this.orderShipmentDetails.add(orderShipmentVO);
	}

	public List<OrderShipmentDetail> getList()
	{
		return java.util.Collections.unmodifiableList(this.orderShipmentDetails);
	}

	@javax.xml.bind.annotation.XmlElement(name = "orderShipments")
	public List<OrderShipmentDetail> getOrderShipmentDetailForJaxb()
	{
		return this.orderShipmentDetails;
	}

	public void initializeOrders(final List<OrderShipmentDetail> orderShipmentsVOs)
	{
		assert this.orderShipmentDetails.isEmpty();
		for (final OrderShipmentDetail orderShipmentVO : orderShipmentsVOs)
		{
			this.addOrderShipment(orderShipmentVO);
		}
	}

	public void setOrderShipmentDetailForJaxb(final List<OrderShipmentDetail> orderShipmentDetail)
	{
		this.orderShipmentDetails = orderShipmentDetail;
	}
			
			

	/**
	* gets 
	*
	* @returns List<OrderShipmentDetail>
	*/
	public List<OrderShipmentDetail> getOrderShipmentDetails()
	{
		return orderShipmentDetails;
	}

	/**
	* sets 
	*
	*/
	public void setOrderShipmentDetails(List<OrderShipmentDetail> orderShipmentDetails)
	{
		this.orderShipmentDetails = orderShipmentDetails;
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
		private List<OrderShipmentDetail> orderShipmentDetails;
		
	
		/**
		* sets 
		*
		*/
		public T setOrderShipmentDetails(List<OrderShipmentDetail> orderShipmentDetails)
		{
			this.orderShipmentDetails = orderShipmentDetails;
			return self();
		}

		private List<OrderShipmentDetail> getOrderShipmentDetails()
		{
			return orderShipmentDetails;
		}
	
		protected abstract T self();

		public OrderShipmentList build(){
			return new OrderShipmentList(this);
		}
	}
}

