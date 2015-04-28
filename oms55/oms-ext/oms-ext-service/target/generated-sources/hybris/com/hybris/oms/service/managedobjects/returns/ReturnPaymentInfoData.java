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
 * Generated managedobject class for type ReturnPaymentInfoData first defined at extension <code>return</code>.
 * <p/>
 * Contains return payment information.
 */
public interface ReturnPaymentInfoData extends ManagedObject, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "ReturnPaymentInfoData";
	
	/** <i>Generated constant</i> - Attribute key of <code>ReturnPaymentInfoData.myReturn</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnPaymentInfoData, com.hybris.oms.service.managedobjects.returns.ReturnData> MYRETURN = new AttributeType<>("myReturn");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnPaymentInfoData.returnPaymentType</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnPaymentInfoData, String> RETURNPAYMENTTYPE = new AttributeType<>("returnPaymentType");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnPaymentInfoData.returnPaymentAmount</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnPaymentInfoData, com.hybris.oms.service.managedobjects.types.AmountVT> RETURNPAYMENTAMOUNT = new AttributeType<>("returnPaymentAmount");
	AttributeType<ReturnPaymentInfoData, String> RETURNPAYMENTAMOUNT_CURRENCYCODE = new AttributeType<>("returnPaymentAmount#currencyCode");
	AttributeType<ReturnPaymentInfoData, Double> RETURNPAYMENTAMOUNT_VALUE = new AttributeType<>("returnPaymentAmount#value");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnPaymentInfoData.taxReversed</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnPaymentInfoData, com.hybris.oms.service.managedobjects.types.AmountVT> TAXREVERSED = new AttributeType<>("taxReversed");
	AttributeType<ReturnPaymentInfoData, String> TAXREVERSED_CURRENCYCODE = new AttributeType<>("taxReversed#currencyCode");
	AttributeType<ReturnPaymentInfoData, Double> TAXREVERSED_VALUE = new AttributeType<>("taxReversed#value");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnPaymentInfoData.version</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnPaymentInfoData, Long> VERSION = new AttributeType<>("version");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnPaymentInfoData.returnPaymentInfoId</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnPaymentInfoData, Long> RETURNPAYMENTINFOID = new AttributeType<>("returnPaymentInfoId");

	/** <i>Generated constant</i> - Index of <code>ReturnPaymentInfoData</code> type defined at extension <code>return</code>. */
	UniqueIndexSingle<ReturnPaymentInfoData, Long> UX_RETURNPAYMENTINFO_RETURNPAYMENTINFOID = new UniqueIndexSingle<>("UX_returnPaymentInfo_returnPaymentInfoId", ReturnPaymentInfoData.class);

	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnPaymentInfoData.returnPaymentInfoId</code> attribute defined at extension <code>return</code>.
	* <b>Value for this attribute is auto-generated at entity creation time using available sequence generator!</b>
	 * <p/>
	 * Unique return paymentInfo identifier.
	 * 
	 * @return the returnPaymentInfoId
	 */
	@javax.validation.constraints.NotNull
	long getReturnPaymentInfoId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnPaymentInfoData.returnPaymentType</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * Return Payment Type.
	 * 
	 * @return the returnPaymentType
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getReturnPaymentType();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnPaymentInfoData.returnPaymentAmount</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * Return Payment Amount.
	 * 
	 * @return the returnPaymentAmount
	 */
	com.hybris.oms.service.managedobjects.types.AmountVT getReturnPaymentAmount();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnPaymentInfoData.myReturn</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * A handle to the parent return.
	 * 
	 * @return the myReturn
	 */
	com.hybris.oms.service.managedobjects.returns.ReturnData getMyReturn();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnPaymentInfoData.taxReversed</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * the amount of tax that has been reversed.
	 * 
	 * @return the taxReversed
	 */
	com.hybris.oms.service.managedobjects.types.AmountVT getTaxReversed();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnPaymentInfoData.version</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * .
	 * 
	 * @return the version
	 */
	java.lang.Long getVersion();
	

	/**
	 * <i>Generated method</i> - Setter of <code>ReturnPaymentInfoData.returnPaymentInfoId</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * Unique return paymentInfo identifier.
	 *
	 * @param value the returnPaymentInfoId
	 */
	void setReturnPaymentInfoId(final long value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnPaymentInfoData.returnPaymentType</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * Return Payment Type.
	 *
	 * @param value the returnPaymentType
	 */
	void setReturnPaymentType(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnPaymentInfoData.returnPaymentAmount</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * Return Payment Amount.
	 *
	 * @param value the returnPaymentAmount
	 */
	void setReturnPaymentAmount(final com.hybris.oms.service.managedobjects.types.AmountVT value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnPaymentInfoData.myReturn</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * A handle to the parent return.
	 *
	 * @param value the myReturn
	 */
	void setMyReturn(final com.hybris.oms.service.managedobjects.returns.ReturnData value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnPaymentInfoData.taxReversed</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * the amount of tax that has been reversed.
	 *
	 * @param value the taxReversed
	 */
	void setTaxReversed(final com.hybris.oms.service.managedobjects.types.AmountVT value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnPaymentInfoData.version</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the version
	 */
	void setVersion(final java.lang.Long value);
	
}
