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
 
package com.hybris.oms.service.managedobjects.ats;

import com.hybris.kernel.api.*;

import com.hybris.kernel.api.ManagedObject;

    
/**
 * Generated managedobject class for type AtsFormulaData first defined at extension <code>ats</code>.
 * <p/>
 * Contains ATS Formula data.
 */
public interface AtsFormulaData extends ManagedObject, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "AtsFormulaData";
	
	/** <i>Generated constant</i> - Attribute key of <code>AtsFormulaData.description</code> attribute defined at extension <code>ats</code>. */
	AttributeType<AtsFormulaData, String> DESCRIPTION = new AttributeType<>("description");
	/** <i>Generated constant</i> - Attribute key of <code>AtsFormulaData.formula</code> attribute defined at extension <code>ats</code>. */
	AttributeType<AtsFormulaData, String> FORMULA = new AttributeType<>("formula");
	/** <i>Generated constant</i> - Attribute key of <code>AtsFormulaData.name</code> attribute defined at extension <code>ats</code>. */
	AttributeType<AtsFormulaData, String> NAME = new AttributeType<>("name");
	/** <i>Generated constant</i> - Attribute key of <code>AtsFormulaData.atsId</code> attribute defined at extension <code>ats</code>. */
	AttributeType<AtsFormulaData, String> ATSID = new AttributeType<>("atsId");
	/** <i>Generated constant</i> - Attribute key of <code>AtsFormulaData.version</code> attribute defined at extension <code>ats</code>. */
	AttributeType<AtsFormulaData, Long> VERSION = new AttributeType<>("version");

	/** <i>Generated constant</i> - Index of <code>AtsFormulaData</code> type defined at extension <code>ats</code>. */
	UniqueIndexSingle<AtsFormulaData, String> UX_ATSFORMULAS_ATSID = new UniqueIndexSingle<>("UX_atsFormulas_atsId", AtsFormulaData.class);

	/**
	 * <i>Generated method</i> - Getter of the <code>AtsFormulaData.atsId</code> attribute defined at extension <code>ats</code>.
	 * <p/>
	 * Formula ID.
	 * 
	 * @return the atsId
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getAtsId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AtsFormulaData.formula</code> attribute defined at extension <code>ats</code>.
	 * <p/>
	 * Formula.
	 * 
	 * @return the formula
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getFormula();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AtsFormulaData.name</code> attribute defined at extension <code>ats</code>.
	 * <p/>
	 * Formula name.
	 * 
	 * @return the name
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getName();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AtsFormulaData.description</code> attribute defined at extension <code>ats</code>.
	 * <p/>
	 * .
	 * 
	 * @return the description
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getDescription();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AtsFormulaData.version</code> attribute defined at extension <code>ats</code>.
	 * <p/>
	 * .
	 * 
	 * @return the version
	 */
	java.lang.Long getVersion();
	

	/**
	 * <i>Generated method</i> - Setter of <code>AtsFormulaData.atsId</code> attribute defined at extension <code>ats</code>.  
	 * <p/>
	 * Formula ID.
	 *
	 * @param value the atsId
	 */
	void setAtsId(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>AtsFormulaData.formula</code> attribute defined at extension <code>ats</code>.  
	 * <p/>
	 * Formula.
	 *
	 * @param value the formula
	 */
	void setFormula(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>AtsFormulaData.name</code> attribute defined at extension <code>ats</code>.  
	 * <p/>
	 * Formula name.
	 *
	 * @param value the name
	 */
	void setName(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>AtsFormulaData.description</code> attribute defined at extension <code>ats</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the description
	 */
	void setDescription(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>AtsFormulaData.version</code> attribute defined at extension <code>ats</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the version
	 */
	void setVersion(final java.lang.Long value);
	
}
