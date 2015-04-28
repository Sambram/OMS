/*
 * [y] hybris Platform
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
 
package com.hybris.oms.service.managedobjects.returns;

import com.hybris.kernel.api.*;

import com.hybris.kernel.api.ManagedObject;

    
/**
 * Generated managedobject class for type ReturnData first defined at extension <code>return</code>.
 * <p/>
 * Contains data on the Return Object.
 */
public interface ReturnData extends ManagedObject, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "ReturnData";
	
	/** <i>Generated constant</i> - Attribute key of <code>ReturnData.shippingRefunded</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnData, Boolean> SHIPPINGREFUNDED = new AttributeType<>("shippingRefunded");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnData.returnPaymentInfos</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnData, com.hybris.oms.service.managedobjects.returns.ReturnPaymentInfoData> RETURNPAYMENTINFOS = new AttributeType<>("returnPaymentInfos");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnData.version</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnData, Long> VERSION = new AttributeType<>("version");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnData.returnId</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnData, Long> RETURNID = new AttributeType<>("returnId");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnData.customRefundAmount</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnData, com.hybris.oms.service.managedobjects.types.AmountVT> CUSTOMREFUNDAMOUNT = new AttributeType<>("customRefundAmount");
	AttributeType<ReturnData, String> CUSTOMREFUNDAMOUNT_CURRENCYCODE = new AttributeType<>("customRefundAmount#currencyCode");
	AttributeType<ReturnData, Double> CUSTOMREFUNDAMOUNT_VALUE = new AttributeType<>("customRefundAmount#value");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnData.calculatedRefundAmount</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnData, com.hybris.oms.service.managedobjects.types.AmountVT> CALCULATEDREFUNDAMOUNT = new AttributeType<>("calculatedRefundAmount");
	AttributeType<ReturnData, String> CALCULATEDREFUNDAMOUNT_CURRENCYCODE = new AttributeType<>("calculatedRefundAmount#currencyCode");
	AttributeType<ReturnData, Double> CALCULATEDREFUNDAMOUNT_VALUE = new AttributeType<>("calculatedRefundAmount#value");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnData.returnLocation</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnData, com.hybris.oms.service.managedobjects.inventory.StockroomLocationData> RETURNLOCATION = new AttributeType<>("returnLocation");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnData.order</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnData, com.hybris.oms.service.managedobjects.order.OrderData> ORDER = new AttributeType<>("order");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnData.state</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnData, String> STATE = new AttributeType<>("state");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnData.returnOrderLines</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnData, java.util.List<com.hybris.oms.service.managedobjects.returns.ReturnOrderLineData>> RETURNORDERLINES = new AttributeType<>("returnOrderLines");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnData.returnReasonCode</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnData, String> RETURNREASONCODE = new AttributeType<>("returnReasonCode");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnData.returnShipment</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnData, com.hybris.oms.service.managedobjects.returns.ReturnShipmentData> RETURNSHIPMENT = new AttributeType<>("returnShipment");

	/** <i>Generated constant</i> - Index of <code>ReturnData</code> type defined at extension <code>return</code>. */
	UniqueIndexSingle<ReturnData, Long> UX_RETURNS_RETURNID = new UniqueIndexSingle<>("UX_returns_returnId", ReturnData.class);

	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnData.returnId</code> attribute defined at extension <code>return</code>.
	* <b>Value for this attribute is auto-generated at entity creation time using available sequence generator!</b>
	 * <p/>
	 * the id of the return.
	 * 
	 * @return the returnId
	 */
	@javax.validation.constraints.NotNull
	long getReturnId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnData.state</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * Current state of the return.
	 * 
	 * @return the state
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getState();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnData.order</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * order.
	 * 
	 * @return the order
	 */
	com.hybris.oms.service.managedobjects.order.OrderData getOrder();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnData.returnLocation</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * The stockoom location where the return is returned to.
	 * 
	 * @return the returnLocation
	 */
	com.hybris.oms.service.managedobjects.inventory.StockroomLocationData getReturnLocation();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnData.returnShipment</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * The shipment that contains the returned items.
	 * 
	 * @return the returnShipment
	 */
	com.hybris.oms.service.managedobjects.returns.ReturnShipmentData getReturnShipment();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnData.customRefundAmount</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * total amount of the whole return(can be modified by UI user).
	 * 
	 * @return the customRefundAmount
	 */
	com.hybris.oms.service.managedobjects.types.AmountVT getCustomRefundAmount();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnData.calculatedRefundAmount</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * total amount of the whole return calculated by OMS.
	 * 
	 * @return the calculatedRefundAmount
	 */
	com.hybris.oms.service.managedobjects.types.AmountVT getCalculatedRefundAmount();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnData.returnReasonCode</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * code of the return reason.
	 * 
	 * @return the returnReasonCode
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getReturnReasonCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnData.returnOrderLines</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * list of returned items.
	 * 
	 * @return the returnOrderLines
	 */
	@javax.validation.constraints.NotNull
	@org.apache.bval.constraints.NotEmpty
	java.util.List<com.hybris.oms.service.managedobjects.returns.ReturnOrderLineData> getReturnOrderLines();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnData.returnPaymentInfos</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * return contains return payment information.
	 * 
	 * @return the returnPaymentInfos
	 */
	com.hybris.oms.service.managedobjects.returns.ReturnPaymentInfoData getReturnPaymentInfos();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnData.shippingRefunded</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * true if shipping is refunded.
	 * 
	 * @return the shippingRefunded
	 */
	@javax.validation.constraints.NotNull
	boolean isShippingRefunded();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnData.version</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * .
	 * 
	 * @return the version
	 */
	java.lang.Long getVersion();
	

	/**
	 * <i>Generated method</i> - Setter of <code>ReturnData.returnId</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * the id of the return.
	 *
	 * @param value the returnId
	 */
	void setReturnId(final long value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnData.state</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * Current state of the return.
	 *
	 * @param value the state
	 */
	void setState(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnData.order</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * order.
	 *
	 * @param value the order
	 */
	void setOrder(final com.hybris.oms.service.managedobjects.order.OrderData value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnData.returnLocation</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * The stockoom location where the return is returned to.
	 *
	 * @param value the returnLocation
	 */
	void setReturnLocation(final com.hybris.oms.service.managedobjects.inventory.StockroomLocationData value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnData.returnShipment</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * The shipment that contains the returned items.
	 *
	 * @param value the returnShipment
	 */
	void setReturnShipment(final com.hybris.oms.service.managedobjects.returns.ReturnShipmentData value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnData.customRefundAmount</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * total amount of the whole return(can be modified by UI user).
	 *
	 * @param value the customRefundAmount
	 */
	void setCustomRefundAmount(final com.hybris.oms.service.managedobjects.types.AmountVT value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnData.calculatedRefundAmount</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * total amount of the whole return calculated by OMS.
	 *
	 * @param value the calculatedRefundAmount
	 */
	void setCalculatedRefundAmount(final com.hybris.oms.service.managedobjects.types.AmountVT value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnData.returnReasonCode</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * code of the return reason.
	 *
	 * @param value the returnReasonCode
	 */
	void setReturnReasonCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnData.returnOrderLines</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * list of returned items.
	 *
	 * @param value the returnOrderLines
	 */
	void setReturnOrderLines(final java.util.List<com.hybris.oms.service.managedobjects.returns.ReturnOrderLineData> value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnData.returnPaymentInfos</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * return contains return payment information.
	 *
	 * @param value the returnPaymentInfos
	 */
	void setReturnPaymentInfos(final com.hybris.oms.service.managedobjects.returns.ReturnPaymentInfoData value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnData.shippingRefunded</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * true if shipping is refunded.
	 *
	 * @param value the shippingRefunded
	 */
	void setShippingRefunded(final boolean value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnData.version</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the version
	 */
	void setVersion(final java.lang.Long value);
	
}
