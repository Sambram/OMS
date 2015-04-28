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
 * Generated managedobject class for type ReturnLineRejectionData first defined at extension <code>return</code>.
 * <p/>
 * Contains return rejections history.
 */
public interface ReturnLineRejectionData extends ManagedObject, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "ReturnLineRejectionData";
	
	/** <i>Generated constant</i> - Attribute key of <code>ReturnLineRejectionData.responsible</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnLineRejectionData, String> RESPONSIBLE = new AttributeType<>("responsible");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnLineRejectionData.myReturnOrderLine</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnLineRejectionData, com.hybris.oms.service.managedobjects.returns.ReturnOrderLineData> MYRETURNORDERLINE = new AttributeType<>("myReturnOrderLine");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnLineRejectionData.quantity</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnLineRejectionData, Integer> QUANTITY = new AttributeType<>("quantity");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnLineRejectionData.reason</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnLineRejectionData, String> REASON = new AttributeType<>("reason");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnLineRejectionData.rejectionId</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnLineRejectionData, Long> REJECTIONID = new AttributeType<>("rejectionId");

	/** <i>Generated constant</i> - Index of <code>ReturnLineRejectionData</code> type defined at extension <code>return</code>. */
	UniqueIndexSingle<ReturnLineRejectionData, Long> UX_RETURNLINEREJECTION_REJECTIONID = new UniqueIndexSingle<>("UX_returnLineRejection_rejectionId", ReturnLineRejectionData.class);

	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnLineRejectionData.rejectionId</code> attribute defined at extension <code>return</code>.
	* <b>Value for this attribute is auto-generated at entity creation time using available sequence generator!</b>
	 * <p/>
	 * Unique return rejection identifier.
	 * 
	 * @return the rejectionId
	 */
	@javax.validation.constraints.NotNull
	long getRejectionId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnLineRejectionData.quantity</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * Quantity rejected.
	 * 
	 * @return the quantity
	 */
	@javax.validation.constraints.NotNull
	int getQuantity();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnLineRejectionData.responsible</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * Who rejected the return.
	 * 
	 * @return the responsible
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getResponsible();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnLineRejectionData.reason</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * Reason if it's necessary.
	 * 
	 * @return the reason
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getReason();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnLineRejectionData.myReturnOrderLine</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * A handle to the parent return order line.
	 * 
	 * @return the myReturnOrderLine
	 */
	com.hybris.oms.service.managedobjects.returns.ReturnOrderLineData getMyReturnOrderLine();
	

	/**
	 * <i>Generated method</i> - Setter of <code>ReturnLineRejectionData.rejectionId</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * Unique return rejection identifier.
	 *
	 * @param value the rejectionId
	 */
	void setRejectionId(final long value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnLineRejectionData.quantity</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * Quantity rejected.
	 *
	 * @param value the quantity
	 */
	void setQuantity(final int value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnLineRejectionData.responsible</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * Who rejected the return.
	 *
	 * @param value the responsible
	 */
	void setResponsible(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnLineRejectionData.reason</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * Reason if it's necessary.
	 *
	 * @param value the reason
	 */
	void setReason(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnLineRejectionData.myReturnOrderLine</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * A handle to the parent return order line.
	 *
	 * @param value the myReturnOrderLine
	 */
	void setMyReturnOrderLine(final com.hybris.oms.service.managedobjects.returns.ReturnOrderLineData value);
	
}
