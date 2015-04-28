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
package com.hybris.oms.export.service.ats.listener;

/**
 * Exception indicating that the {@link AtsChangeListener} could not find the sku.
 */
public class AtsChangeListenerException extends Exception
{

	private static final long serialVersionUID = -359584865943722038L;

	public AtsChangeListenerException(final String message)
	{
		super(message);
	}

	public AtsChangeListenerException(final String message, final Throwable cause)
	{
		super(message, cause);
	}
}
