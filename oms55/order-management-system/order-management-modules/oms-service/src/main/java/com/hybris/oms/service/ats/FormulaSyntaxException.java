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

/**
 * Exception thrown if a formula is syntactically incorrect.
 */
public class FormulaSyntaxException extends IllegalArgumentException
{

	private static final long serialVersionUID = -6051706835361671910L;

	public FormulaSyntaxException(final String message)
	{
		super(message);
	}

	public FormulaSyntaxException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

}
