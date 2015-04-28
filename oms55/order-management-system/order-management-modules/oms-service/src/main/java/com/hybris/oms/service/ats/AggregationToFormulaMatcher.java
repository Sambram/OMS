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


import java.util.List;

/**
 * interface to provide matching between formula and aggregation.
 * It returns boolean value if passed aggregation match any formula.
 * Mapper parse formula and check if aggregation status code match any formula status code based on status realm.
 */
public interface AggregationToFormulaMatcher
{

	/**
	 * Method to check if passed aggregation is matching any formula.
	 *
	 * @param aggregationStatusCode statusCode of Aggregation.
	 * @param statusRealm statusRealm of Aggregation.
	 * @return true if passed aggregation object match any formula else return false.
	 * @throws IllegalArgumentException if status code and realm null or empty
	 */
	boolean hasMatchingFormula(final String aggregationStatusCode, final StatusRealm statusRealm);

	/**
	 * Method for check if passing aggregation match any formula passed to this method.
	 *
	 * @param aggregationStatusCode status code of aggregation.
	 * @param statusRealm status realm of aggregation
	 * @param formulaIds  list of formula ids to check if they match aggregation.
	 * @return if there is a match between formulas and aggregation return true else return false;
	 * @throws IllegalArgumentException if status code, realm or formula null
	 */
	boolean hasMatchingFormula(final String aggregationStatusCode, final StatusRealm statusRealm, final List<String> formulaIds);
}
