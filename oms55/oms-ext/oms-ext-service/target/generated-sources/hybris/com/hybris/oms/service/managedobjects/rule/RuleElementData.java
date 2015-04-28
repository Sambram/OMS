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
 
package com.hybris.oms.service.managedobjects.rule;

import com.hybris.kernel.api.*;

import com.hybris.kernel.api.ManagedObject;

    
/**
 * Generated managedobject class for type RuleElementData first defined at extension <code>rule</code>.
 * <p/>
 * Contains rule elements.
 */
public interface RuleElementData extends ManagedObject, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "RuleElementData";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleElementData.parameters</code> attribute defined at extension <code>rule</code>. */
	AttributeType<RuleElementData, java.util.List<com.hybris.oms.service.managedobjects.rule.RuleParameterData>> PARAMETERS = new AttributeType<>("parameters");
	/** <i>Generated constant</i> - Attribute key of <code>RuleElementData.rule</code> attribute defined at extension <code>rule</code>. */
	AttributeType<RuleElementData, com.hybris.oms.service.managedobjects.rule.RuleData> RULE = new AttributeType<>("rule");
	/** <i>Generated constant</i> - Attribute key of <code>RuleElementData.sequenceNr</code> attribute defined at extension <code>rule</code>. */
	AttributeType<RuleElementData, Integer> SEQUENCENR = new AttributeType<>("sequenceNr");
	/** <i>Generated constant</i> - Attribute key of <code>RuleElementData.version</code> attribute defined at extension <code>rule</code>. */
	AttributeType<RuleElementData, Long> VERSION = new AttributeType<>("version");


	/**
	 * <i>Generated method</i> - Getter of the <code>RuleElementData.sequenceNr</code> attribute defined at extension <code>rule</code>.
	 * <p/>
	 * Sequence number.
	 * 
	 * @return the sequenceNr
	 */
	@javax.validation.constraints.NotNull
	int getSequenceNr();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleElementData.rule</code> attribute defined at extension <code>rule</code>.
	 * <p/>
	 * Rule this element belongs to.
	 * 
	 * @return the rule
	 */
	@javax.validation.constraints.NotNull
	com.hybris.oms.service.managedobjects.rule.RuleData getRule();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleElementData.parameters</code> attribute defined at extension <code>rule</code>.
	 * <p/>
	 * Element contains parameters.
	 * 
	 * @return the parameters
	 */
	java.util.List<com.hybris.oms.service.managedobjects.rule.RuleParameterData> getParameters();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleElementData.version</code> attribute defined at extension <code>rule</code>.
	 * <p/>
	 * .
	 * 
	 * @return the version
	 */
	java.lang.Long getVersion();
	

	/**
	 * <i>Generated method</i> - Setter of <code>RuleElementData.sequenceNr</code> attribute defined at extension <code>rule</code>.  
	 * <p/>
	 * Sequence number.
	 *
	 * @param value the sequenceNr
	 */
	void setSequenceNr(final int value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleElementData.rule</code> attribute defined at extension <code>rule</code>.  
	 * <p/>
	 * Rule this element belongs to.
	 *
	 * @param value the rule
	 */
	void setRule(final com.hybris.oms.service.managedobjects.rule.RuleData value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleElementData.parameters</code> attribute defined at extension <code>rule</code>.  
	 * <p/>
	 * Element contains parameters.
	 *
	 * @param value the parameters
	 */
	void setParameters(final java.util.List<com.hybris.oms.service.managedobjects.rule.RuleParameterData> value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleElementData.version</code> attribute defined at extension <code>rule</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the version
	 */
	void setVersion(final java.lang.Long value);
	
}
