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
package com.hybris.oms.service.ats;

import com.hybris.oms.service.managedobjects.ats.AtsFormulaData;

import java.util.List;
import java.util.Set;


/**
 * Services for ATS calculation.
 */
public interface AtsService
{
	/**
	 * Creates an ATS formula.
	 * 
	 * @throws {@link FormulaSyntaxException}
	 */
	void createFormula(String atsId, String formula, String name, String description) throws FormulaSyntaxException;

	/**
	 * Updates an ATS formula.
	 * 
	 * @throws {@link FormulaSyntaxException}
	 */
	void updateFormula(String atsId, String formula, String name, String description) throws FormulaSyntaxException;

	/**
	 * Deletes a formula immediately.
	 */
	void deleteFormula(String atsId);

	/**
	 * Retrieves all formulas matching the given atsIds. If the given {@link Set} of atsIds is <tt>null</tt> or empty,
	 * all existing ATS formulas are returned.
	 * 
	 * @return formulas
	 */
	List<AtsFormulaData> findFormulas(Set<String> atsIds);

	/**
	 * Retrieves a formula by id.
	 * 
	 * @return {@link AtsFormulaData}
	 */
	AtsFormulaData getFormulaById(String atsId);

	/**
	 * Returns the result of a local ATS calculation.
	 * 
	 * @param skus if <tt>null</tt> or empty, return result for all SKUs
	 * @param locations if <tt>null</tt> or empty, return result for all locations
	 * @param atsIds if <tt>null</tt> or empty, return result for all formulas
	 * @return {@link AtsResult}, never <tt>null</tt>
	 */
	AtsResult getLocalAts(Set<String> skus, Set<String> locations, Set<String> atsIds);

	/**
	 * Returns the result of a global ATS calculation.
	 * 
	 * @param skus if <tt>null</tt> or empty, return result for all SKUs
	 * @param atsIds if <tt>null</tt> or empty, return result for all formulas
	 * @return {@link AtsResult}, never <tt>null</tt>
	 */
	AtsResult getGlobalAts(Set<String> skus, Set<String> atsIds);

	/**
	 * Returns the default atsId defined in the tenant preferences (ats.calculator).
	 * 
	 * @return atsId
	 */
	String getDefaultAtsId();

}
