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
 * Generated managedobject class for type RuleData first defined at extension <code>rule</code>.
 * <p/>
 * Contains data for each rule.
 */
public interface RuleData extends ManagedObject, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "RuleData";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleData.eligibilityOperator</code> attribute defined at extension <code>rule</code>. */
	AttributeType<RuleData, com.hybris.oms.service.managedobjects.rule.Operator> ELIGIBILITYOPERATOR = new AttributeType<>("eligibilityOperator");
	/** <i>Generated constant</i> - Attribute key of <code>RuleData.conditionOperator</code> attribute defined at extension <code>rule</code>. */
	AttributeType<RuleData, com.hybris.oms.service.managedobjects.rule.Operator> CONDITIONOPERATOR = new AttributeType<>("conditionOperator");
	/** <i>Generated constant</i> - Attribute key of <code>RuleData.description</code> attribute defined at extension <code>rule</code>. */
	LocalizedAttributeType<RuleData, String> DESCRIPTION = new LocalizedAttributeType<>("description");
	/** <i>Generated constant</i> - Attribute key of <code>RuleData.ruleActions</code> attribute defined at extension <code>rule</code>. */
	AttributeType<RuleData, java.util.List<com.hybris.oms.service.managedobjects.rule.RuleActionData>> RULEACTIONS = new AttributeType<>("ruleActions");
	/** <i>Generated constant</i> - Attribute key of <code>RuleData.startDate</code> attribute defined at extension <code>rule</code>. */
	AttributeType<RuleData, java.util.Date> STARTDATE = new AttributeType<>("startDate");
	/** <i>Generated constant</i> - Attribute key of <code>RuleData.version</code> attribute defined at extension <code>rule</code>. */
	AttributeType<RuleData, Long> VERSION = new AttributeType<>("version");
	/** <i>Generated constant</i> - Attribute key of <code>RuleData.enabled</code> attribute defined at extension <code>rule</code>. */
	AttributeType<RuleData, Boolean> ENABLED = new AttributeType<>("enabled");
	/** <i>Generated constant</i> - Attribute key of <code>RuleData.ruleConditions</code> attribute defined at extension <code>rule</code>. */
	AttributeType<RuleData, java.util.List<com.hybris.oms.service.managedobjects.rule.RuleConditionsData>> RULECONDITIONS = new AttributeType<>("ruleConditions");
	/** <i>Generated constant</i> - Attribute key of <code>RuleData.name</code> attribute defined at extension <code>rule</code>. */
	AttributeType<RuleData, String> NAME = new AttributeType<>("name");
	/** <i>Generated constant</i> - Attribute key of <code>RuleData.ruleId</code> attribute defined at extension <code>rule</code>. */
	AttributeType<RuleData, String> RULEID = new AttributeType<>("ruleId");
	/** <i>Generated constant</i> - Attribute key of <code>RuleData.endDate</code> attribute defined at extension <code>rule</code>. */
	AttributeType<RuleData, java.util.Date> ENDDATE = new AttributeType<>("endDate");

	/** <i>Generated constant</i> - Index of <code>RuleData</code> type defined at extension <code>rule</code>. */
	UniqueIndexSingle<RuleData, String> UX_RULES_RULEID = new UniqueIndexSingle<>("UX_rules_ruleId", RuleData.class);

	/**
	 * <i>Generated method</i> - Getter of the <code>RuleData.ruleId</code> attribute defined at extension <code>rule</code>.
	 * <p/>
	 * Rule id.
	 * 
	 * @return the ruleId
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getRuleId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleData.name</code> attribute defined at extension <code>rule</code>.
	 * <p/>
	 * Rule name.
	 * 
	 * @return the name
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getName();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleData.description</code> attribute defined at extension <code>rule</code>.
	 * <p/>
	 * Rule description.
	 * 
	 * @return the description
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getDescription();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleData.description</code> attribute defined at extension <code>rule</code>.
	 * <p/>
	 * Rule description.
	 * 
	 * @return the description
	 */
	java.lang.String getDescription(final java.util.Locale locale);	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleData.eligibilityOperator</code> attribute defined at extension <code>rule</code>.
	 * <p/>
	 * Operator (AND/OR).
	 * 
	 * @return the eligibilityOperator
	 */
	@javax.validation.constraints.NotNull
	com.hybris.oms.service.managedobjects.rule.Operator getEligibilityOperator();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleData.conditionOperator</code> attribute defined at extension <code>rule</code>.
	 * <p/>
	 * Operator (AND/OR).
	 * 
	 * @return the conditionOperator
	 */
	@javax.validation.constraints.NotNull
	com.hybris.oms.service.managedobjects.rule.Operator getConditionOperator();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleData.enabled</code> attribute defined at extension <code>rule</code>.
	 * <p/>
	 * Rule is enabled.
	 * 
	 * @return the enabled
	 */
	@javax.validation.constraints.NotNull
	boolean isEnabled();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleData.startDate</code> attribute defined at extension <code>rule</code>.
	 * <p/>
	 * Start date.
	 * 
	 * @return the startDate
	 */
	java.util.Date getStartDate();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleData.endDate</code> attribute defined at extension <code>rule</code>.
	 * <p/>
	 * End date.
	 * 
	 * @return the endDate
	 */
	java.util.Date getEndDate();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleData.ruleActions</code> attribute defined at extension <code>rule</code>.
	 * <p/>
	 * Rule contains rule actions.
	 * 
	 * @return the ruleActions
	 */
	java.util.List<com.hybris.oms.service.managedobjects.rule.RuleActionData> getRuleActions();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleData.ruleConditions</code> attribute defined at extension <code>rule</code>.
	 * <p/>
	 * Rule contains rule conditions.
	 * 
	 * @return the ruleConditions
	 */
	java.util.List<com.hybris.oms.service.managedobjects.rule.RuleConditionsData> getRuleConditions();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleData.version</code> attribute defined at extension <code>rule</code>.
	 * <p/>
	 * .
	 * 
	 * @return the version
	 */
	java.lang.Long getVersion();
	

	/**
	 * <i>Generated method</i> - Setter of <code>RuleData.ruleId</code> attribute defined at extension <code>rule</code>.  
	 * <p/>
	 * Rule id.
	 *
	 * @param value the ruleId
	 */
	void setRuleId(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleData.name</code> attribute defined at extension <code>rule</code>.  
	 * <p/>
	 * Rule name.
	 *
	 * @param value the name
	 */
	void setName(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleData.description</code> attribute defined at extension <code>rule</code>.  
	 * <p/>
	 * Rule description.
	 *
	 * @param value the description
	 */
	void setDescription(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleData.description</code> attribute defined at extension <code>rule</code>.  
	 * <p/>
	 * Rule description.
	 *
	 * @param value the description
	 */
	void setDescription(final java.lang.String value, final java.util.Locale locale);	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleData.eligibilityOperator</code> attribute defined at extension <code>rule</code>.  
	 * <p/>
	 * Operator (AND/OR).
	 *
	 * @param value the eligibilityOperator
	 */
	void setEligibilityOperator(final com.hybris.oms.service.managedobjects.rule.Operator value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleData.conditionOperator</code> attribute defined at extension <code>rule</code>.  
	 * <p/>
	 * Operator (AND/OR).
	 *
	 * @param value the conditionOperator
	 */
	void setConditionOperator(final com.hybris.oms.service.managedobjects.rule.Operator value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleData.enabled</code> attribute defined at extension <code>rule</code>.  
	 * <p/>
	 * Rule is enabled.
	 *
	 * @param value the enabled
	 */
	void setEnabled(final boolean value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleData.startDate</code> attribute defined at extension <code>rule</code>.  
	 * <p/>
	 * Start date.
	 *
	 * @param value the startDate
	 */
	void setStartDate(final java.util.Date value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleData.endDate</code> attribute defined at extension <code>rule</code>.  
	 * <p/>
	 * End date.
	 *
	 * @param value the endDate
	 */
	void setEndDate(final java.util.Date value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleData.ruleActions</code> attribute defined at extension <code>rule</code>.  
	 * <p/>
	 * Rule contains rule actions.
	 *
	 * @param value the ruleActions
	 */
	void setRuleActions(final java.util.List<com.hybris.oms.service.managedobjects.rule.RuleActionData> value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleData.ruleConditions</code> attribute defined at extension <code>rule</code>.  
	 * <p/>
	 * Rule contains rule conditions.
	 *
	 * @param value the ruleConditions
	 */
	void setRuleConditions(final java.util.List<com.hybris.oms.service.managedobjects.rule.RuleConditionsData> value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleData.version</code> attribute defined at extension <code>rule</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the version
	 */
	void setVersion(final java.lang.Long value);
	
}
