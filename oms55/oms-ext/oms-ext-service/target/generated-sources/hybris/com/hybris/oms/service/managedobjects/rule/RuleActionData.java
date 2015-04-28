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

import com.hybris.oms.service.managedobjects.rule.RuleElementData;

    
/**
 * Generated managedobject class for type RuleActionData first defined at extension <code>rule</code>.
 * <p/>
 * Contains rule elements - actions.
 */
public interface RuleActionData extends RuleElementData, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "RuleActionData";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleActionData.salience</code> attribute defined at extension <code>rule</code>. */
	AttributeType<RuleActionData, Integer> SALIENCE = new AttributeType<>("salience");


	/**
	 * <i>Generated method</i> - Getter of the <code>RuleActionData.salience</code> attribute defined at extension <code>rule</code>.
	 * <p/>
	 * Rule action salience.
	 * 
	 * @return the salience
	 */
	int getSalience();
	

	/**
	 * <i>Generated method</i> - Setter of <code>RuleActionData.salience</code> attribute defined at extension <code>rule</code>.  
	 * <p/>
	 * Rule action salience.
	 *
	 * @param value the salience
	 */
	void setSalience(final int value);
	
}
