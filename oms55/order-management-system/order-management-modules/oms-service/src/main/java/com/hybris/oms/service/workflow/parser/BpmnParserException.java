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
package com.hybris.oms.service.workflow.parser;

/**
 * Exception thrown if a bpmn diagram cannot be parsed.
 */
public class BpmnParserException extends RuntimeException
{

	private static final long serialVersionUID = -3740574998396808087L;

	public BpmnParserException(final String message)
	{
		super(message);
	}

	public BpmnParserException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

}
