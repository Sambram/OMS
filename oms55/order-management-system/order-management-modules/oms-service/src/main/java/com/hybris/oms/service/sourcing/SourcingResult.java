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
package com.hybris.oms.service.sourcing;

import java.util.List;
import java.util.Map;



/**
 * Contains the result of a previous sourcing operation.
 */
public interface SourcingResult
{

	public enum ResultEnum
	{
		EMPTY, FAILURE, SUCCESS, PARTIAL
	}

	/**
	 * Returns the input lines used for sourcing.
	 * 
	 * @return list of {@link SourcingLine}
	 */
	List<SourcingLine> getInputLines();

	/**
	 * Returns the {@link List} of created {@link SourcingOLQ}s, never <tt>null</tt>.
	 */
	List<SourcingOLQ> getSourcingOlqs();

	/**
	 * Returns the overall status of a previous sourcing operation.
	 * 
	 * @return {@link SourcingResult.ResultEnum}
	 */
	SourcingResult.ResultEnum getStatus();

	/**
	 * Returns the name of the strategy that changed the overall status.
	 * 
	 * @return name of the strategy or <tt>null</tt>
	 */
	String getStrategyName();

	/**
	 * Returns <tt>true</tt> if the result is empty.
	 */
	boolean isEmpty();

	/**
	 * Returns an unmodifiable {@link Map} of Properties, never <tt>null</tt>.
	 * 
	 * @return optional propertiesk, never <tt>null</tt>
	 */
	Map<String, Object> getProperties();

	/**
	 * Returns unmodifiable {@link Map} of line actions for all processed lines, never <tt>null</tt>.
	 * 
	 * @return actionName for all lineIds
	 */
	Map<String, String> getLineActions();

	/**
	 * Returns any action associated to an orderLineId or <tt>null</tt> if no action was set.
	 * 
	 * @param lineId lineId
	 * @return action for the given lineId or <tt>null</tt>
	 */
	String getActionForLineId(String lineId);

	/**
	 * Returns unmodifiable {@link Map} of statuses for all processed lines, never <tt>null</tt>.
	 * 
	 * @return {@link ResultEnum} for all lineIds
	 */
	Map<String, SourcingResult.ResultEnum> getLineStatus();

	/**
	 * Returns the process status for an orderLineId or <tt>null</tt> if the line is not processed.
	 * 
	 * @param lineId lineId
	 * @return result for the given lineId or <tt>null</tt>
	 */
	SourcingResult.ResultEnum getStatusForLineId(String lineId);


}
