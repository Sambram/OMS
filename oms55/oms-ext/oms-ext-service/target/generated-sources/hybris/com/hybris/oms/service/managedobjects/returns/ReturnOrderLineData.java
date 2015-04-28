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
 * Generated managedobject class for type ReturnOrderLineData first defined at extension <code>return</code>.
 * <p/>
 * Contains data on the return order line.
 */
public interface ReturnOrderLineData extends ManagedObject, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "ReturnOrderLineData";
	
	/** <i>Generated constant</i> - Attribute key of <code>ReturnOrderLineData.returnOrderLineId</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnOrderLineData, Long> RETURNORDERLINEID = new AttributeType<>("returnOrderLineId");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnOrderLineData.orderLineId</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnOrderLineData, String> ORDERLINEID = new AttributeType<>("orderLineId");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnOrderLineData.returnOrderLineStatus</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnOrderLineData, String> RETURNORDERLINESTATUS = new AttributeType<>("returnOrderLineStatus");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnOrderLineData.version</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnOrderLineData, Long> VERSION = new AttributeType<>("version");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnOrderLineData.quantity</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnOrderLineData, com.hybris.oms.service.managedobjects.types.QuantityVT> QUANTITY = new AttributeType<>("quantity");
	AttributeType<ReturnOrderLineData, String> QUANTITY_UNITCODE = new AttributeType<>("quantity#unitCode");
	AttributeType<ReturnOrderLineData, Integer> QUANTITY_VALUE = new AttributeType<>("quantity#value");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnOrderLineData.returnLineRejections</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnOrderLineData, java.util.List<com.hybris.oms.service.managedobjects.returns.ReturnLineRejectionData>> RETURNLINEREJECTIONS = new AttributeType<>("returnLineRejections");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnOrderLineData.myReturn</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnOrderLineData, com.hybris.oms.service.managedobjects.returns.ReturnData> MYRETURN = new AttributeType<>("myReturn");

	/** <i>Generated constant</i> - Index of <code>ReturnOrderLineData</code> type defined at extension <code>return</code>. */
	UniqueIndexSingle<ReturnOrderLineData, Long> UX_RETURNORDERLINES_RETURNORDERLINEID = new UniqueIndexSingle<>("UX_returnOrderLines_returnOrderLineId", ReturnOrderLineData.class);

	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnOrderLineData.myReturn</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * A handle to the parent return.
	 * 
	 * @return the myReturn
	 */
	@javax.validation.constraints.NotNull
	com.hybris.oms.service.managedobjects.returns.ReturnData getMyReturn();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnOrderLineData.returnOrderLineId</code> attribute defined at extension <code>return</code>.
	* <b>Value for this attribute is auto-generated at entity creation time using available sequence generator!</b>
	 * <p/>
	 * Return order line id.
	 * 
	 * @return the returnOrderLineId
	 */
	@javax.validation.constraints.NotNull
	long getReturnOrderLineId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnOrderLineData.returnOrderLineStatus</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * status of the item being returned.
	 * 
	 * @return the returnOrderLineStatus
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getReturnOrderLineStatus();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnOrderLineData.orderLineId</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * linked order line.
	 * 
	 * @return the orderLineId
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getOrderLineId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnOrderLineData.quantity</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * quantity of the item that is requested to be returned.
	 * 
	 * @return the quantity
	 */
	@javax.validation.constraints.NotNull
	com.hybris.oms.service.managedobjects.types.QuantityVT getQuantity();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnOrderLineData.returnLineRejections</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * list of return line rejections.
	 * 
	 * @return the returnLineRejections
	 */
	java.util.List<com.hybris.oms.service.managedobjects.returns.ReturnLineRejectionData> getReturnLineRejections();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnOrderLineData.version</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * .
	 * 
	 * @return the version
	 */
	java.lang.Long getVersion();
	

	/**
	 * <i>Generated method</i> - Setter of <code>ReturnOrderLineData.myReturn</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * A handle to the parent return.
	 *
	 * @param value the myReturn
	 */
	void setMyReturn(final com.hybris.oms.service.managedobjects.returns.ReturnData value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnOrderLineData.returnOrderLineId</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * Return order line id.
	 *
	 * @param value the returnOrderLineId
	 */
	void setReturnOrderLineId(final long value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnOrderLineData.returnOrderLineStatus</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * status of the item being returned.
	 *
	 * @param value the returnOrderLineStatus
	 */
	void setReturnOrderLineStatus(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnOrderLineData.orderLineId</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * linked order line.
	 *
	 * @param value the orderLineId
	 */
	void setOrderLineId(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnOrderLineData.quantity</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * quantity of the item that is requested to be returned.
	 *
	 * @param value the quantity
	 */
	void setQuantity(final com.hybris.oms.service.managedobjects.types.QuantityVT value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnOrderLineData.returnLineRejections</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * list of return line rejections.
	 *
	 * @param value the returnLineRejections
	 */
	void setReturnLineRejections(final java.util.List<com.hybris.oms.service.managedobjects.returns.ReturnLineRejectionData> value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnOrderLineData.version</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the version
	 */
	void setVersion(final java.lang.Long value);
	
}
