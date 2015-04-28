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
 * Generated managedobject class for type RuleParameterData first defined at extension <code>rule</code>.
 * <p/>
 * Contains rule parameters.
 */
public interface RuleParameterData extends ManagedObject, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "RuleParameterData";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleParameterData.version</code> attribute defined at extension <code>rule</code>. */
	AttributeType<RuleParameterData, Long> VERSION = new AttributeType<>("version");
	/** <i>Generated constant</i> - Attribute key of <code>RuleParameterData.ruleElement</code> attribute defined at extension <code>rule</code>. */
	AttributeType<RuleParameterData, com.hybris.oms.service.managedobjects.rule.RuleElementData> RULEELEMENT = new AttributeType<>("ruleElement");
	/** <i>Generated constant</i> - Attribute key of <code>RuleParameterData.displayText</code> attribute defined at extension <code>rule</code>. */
	AttributeType<RuleParameterData, String> DISPLAYTEXT = new AttributeType<>("displayText");
	/** <i>Generated constant</i> - Attribute key of <code>RuleParameterData.key</code> attribute defined at extension <code>rule</code>. */
	AttributeType<RuleParameterData, com.hybris.oms.service.managedobjects.rule.RuleParameterKey> KEY = new AttributeType<>("key");
	/** <i>Generated constant</i> - Attribute key of <code>RuleParameterData.value</code> attribute defined at extension <code>rule</code>. */
	AttributeType<RuleParameterData, String> VALUE = new AttributeType<>("value");


	/**
	 * <i>Generated method</i> - Getter of the <code>RuleParameterData.displayText</code> attribute defined at extension <code>rule</code>.
	 * <p/>
	 * Text.
	 * 
	 * @return the displayText
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getDisplayText();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleParameterData.key</code> attribute defined at extension <code>rule</code>.
	 * <p/>
	 * Parameter key.
	 * 
	 * @return the key
	 */
	@javax.validation.constraints.NotNull
	com.hybris.oms.service.managedobjects.rule.RuleParameterKey getKey();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleParameterData.value</code> attribute defined at extension <code>rule</code>.
	 * <p/>
	 * Parameter value.
	 * 
	 * @return the value
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getValue();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleParameterData.ruleElement</code> attribute defined at extension <code>rule</code>.
	 * <p/>
	 * Rule element this parameter belongs to.
	 * 
	 * @return the ruleElement
	 */
	@javax.validation.constraints.NotNull
	com.hybris.oms.service.managedobjects.rule.RuleElementData getRuleElement();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleParameterData.version</code> attribute defined at extension <code>rule</code>.
	 * <p/>
	 * .
	 * 
	 * @return the version
	 */
	java.lang.Long getVersion();
	

	/**
	 * <i>Generated method</i> - Setter of <code>RuleParameterData.displayText</code> attribute defined at extension <code>rule</code>.  
	 * <p/>
	 * Text.
	 *
	 * @param value the displayText
	 */
	void setDisplayText(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleParameterData.key</code> attribute defined at extension <code>rule</code>.  
	 * <p/>
	 * Parameter key.
	 *
	 * @param value the key
	 */
	void setKey(final com.hybris.oms.service.managedobjects.rule.RuleParameterKey value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleParameterData.value</code> attribute defined at extension <code>rule</code>.  
	 * <p/>
	 * Parameter value.
	 *
	 * @param value the value
	 */
	void setValue(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleParameterData.ruleElement</code> attribute defined at extension <code>rule</code>.  
	 * <p/>
	 * Rule element this parameter belongs to.
	 *
	 * @param value the ruleElement
	 */
	void setRuleElement(final com.hybris.oms.service.managedobjects.rule.RuleElementData value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleParameterData.version</code> attribute defined at extension <code>rule</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the version
	 */
	void setVersion(final java.lang.Long value);
	
}
