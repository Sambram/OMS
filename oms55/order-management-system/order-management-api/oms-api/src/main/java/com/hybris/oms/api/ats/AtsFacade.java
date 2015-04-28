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
package com.hybris.oms.api.ats;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.hybris.oms.domain.ats.AtsFormula;
import com.hybris.oms.domain.ats.AtsLocalQuantities;
import com.hybris.oms.domain.ats.AtsQuantities;
import com.hybris.oms.domain.exception.DuplicateEntityException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;


/**
 * Facade to retrieve global and local ATS (Available To Sell) and manage the ATS formulas.
 */
public interface AtsFacade
{
	/**
	 * Creates an ATS formula.
	 * 
	 * @category OMS-UI
	 * 
	 * @param formula the formula to create.
	 * 
	 *           <dt><b>Preconditions:</b>
	 *           <dd>
	 *           formula.atsId must not be blank.
	 *           <dd>
	 *           formula.name must not be blank.
	 *           <dd>
	 *           formula.formula must not be blank.
	 *           <dd>
	 *           formula.formula must have at least one operand.
	 *           <dd>
	 *           formula.formula must not contain only threshold.
	 *           <dd>
	 *           formula.formula must not contain duplicate threshold definition.
	 *           <dd>
	 *           formula.formula Threshold may only be subtracted.
	 * @throws DuplicateEntityException if formula already exists.
	 * @throws EntityValidationException if preconditions are not met.
	 */
	void createFormula(AtsFormula formula) throws DuplicateEntityException, EntityValidationException;

	/**
	 * Updates an ATS formula.
	 * 
	 * @category OMS-UI
	 * 
	 * @param atsId the formula id to update
	 * @param formula the formula to update.
	 * 
	 *           <dt><b>Preconditions:</b>
	 *           <dd>
	 *           formula.atsId must not be blank.
	 *           <dd>
	 *           formula.name must not be blank.
	 *           <dd>
	 *           formula.formula must not be blank.
	 *           <dd>
	 *           formula.formula must have at least one operand.
	 *           <dd>
	 *           formula.formula must not contain only threshold.
	 *           <dd>
	 *           formula.formula must not contain duplicate threshold definition.
	 *           <dd>
	 *           formula.formula Threshold may only be subtracted.
	 * @throws EntityNotFoundException if the formula was not found
	 * @throws EntityValidationException if preconditions are not met.
	 */
	void updateFormula(String atsId, AtsFormula formula) throws EntityNotFoundException, EntityValidationException;

	/**
	 * Deletes a formula immediately. 
	 * 
	 * @category OMS-UI
	 * 
	 * <dt><b>Preconditions:</b>
	 * <dd>
	 * atsId must not be blank.
	 * <dd>
	 * 
	 * @param atsId the formula id to delete
	 * @throws EntityNotFoundException if the ats formula was not found
	 */
	void deleteFormula(String atsId) throws EntityNotFoundException;

	/**
	 * Retrieves all formulas in the system.
	 * 
	 * @category OMS-UI
	 * 
	 * @return a Collection of formulas
	 */
	Collection<AtsFormula> findAllFormulas();

	/**
	 * Retrieves a formula by id.
	 * 
	 * @param atsId the formula id to update
	 * @return a formula
	 * @throws EntityNotFoundException
	 */
	AtsFormula getFormulaById(String atsId) throws EntityNotFoundException;

	/**
	 * Returns a list of global ATS calculations filtered with the given skus and formula IDs.
	 * 
	 * @param set of skus if <tt>null</tt> or empty, return result for all SKUs
	 * @param set of atsIds if <tt>null</tt> or empty, return result for all formulas.
	 * 
	 *           <dt><b>Preconditions:</b>
	 *           <dd>
	 *           skus must not be null.
	 *           <dd>
	 *           skus must not be empty.
	 * @return a list of global ATS calculations
	 * @throws EntityNotFoundException if global ats was not found
	 * @throws EntityValidationException if preconditions are not met.
	 */
	AtsQuantities findGlobalAts(Set<String> skus, Set<String> atsIds) throws EntityNotFoundException, EntityValidationException;

	/**
	 * Returns a list of local ATS calculations filtered with the given skus, locations and formula IDs.
	 * 
	 * @category PLATFORM EXTENSION - omsats
	 * 
	 * @param skus set of skus if <tt>null</tt> or empty, return result for all SKUs
	 * @param locations set of locations if <tt>null</tt> or empty, return result for all locations
	 * @param atsIds set of atsIds if <tt>null</tt> or empty, return result for all formulas.
	 * 
	 *           <dt><b>Preconditions:</b>
	 *           <dd>
	 *           skus must not be null.
	 *           <dd>
	 *           skus must not be empty.
	 * @return list of {@link AtsLocalQuantities}, never <tt>null</tt>
	 * @throws EntityNotFoundException if local ats was not found
	 * @throws EntityValidationException if preconditions are not met.
	 */
	List<AtsLocalQuantities> findLocalAts(Set<String> skus, Set<String> locations, Set<String> atsIds)
			throws EntityNotFoundException, EntityValidationException;

	/**
	 * Returns a list of local ATS calculations filtered with Base Store parameters.
	 * 
	 * @category EXTERNAL
	 * 
	 * @param skus set of skus if <tt>null</tt> or empty, return result for all SKUs.
	 * @param atsIds set of atsIds if <tt>null</tt> or empty, return result for all formulas.
	 * @param baseStore name of the Base Store.
	 * @param countryCodes country of the Location. This is corresponds to a
	 *           {@link com.hybris.oms.domain.address.Address#getCountryIso3166Alpha2Code()} property of Location.
	 * @param locationRoles Location roles correspond to {@link com.hybris.oms.domain.locationrole.LocationRole} values.
	 * @return list of {@link AtsLocalQuantities} (may be empty), never <tt>null</tt>
	 * @throws EntityNotFoundException if not ats was found
	 * @throws EntityValidationException if preconditions are not met.
	 */
	List<AtsLocalQuantities> findAtsByBaseStore(Set<String> skus, Set<String> atsIds, String baseStore, Set<String> countryCodes,
			Set<String> locationRoles) throws EntityNotFoundException, EntityValidationException;

}
