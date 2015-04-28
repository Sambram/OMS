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
package com.hybris.oms.domain.remote.exception;

import com.hybris.oms.domain.exception.HybrisSystemException;


/**
 * Exception thrown if performing create or get shipment label returns an invalid response.
 * Invalid response: Empty result set
 */
public class InvalidShipmentLabelResponseException extends HybrisSystemException
{
	private static final long serialVersionUID = 7903367300541130725L;

	/**
	 * @param message - the cause of this exception
	 */
	public InvalidShipmentLabelResponseException(final String message)
	{
		super(message);
	}

	/**
	 * @param message - the cause of this exception
	 * @param cause - the Throwable that caused this exception
	 */
	public InvalidShipmentLabelResponseException(final String message, final Throwable cause)
	{
		super(message, cause);
	}
}
