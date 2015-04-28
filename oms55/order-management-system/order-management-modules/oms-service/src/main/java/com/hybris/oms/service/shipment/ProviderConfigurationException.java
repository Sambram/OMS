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
package com.hybris.oms.service.shipment;

public class ProviderConfigurationException extends RuntimeException
{
	private static final long serialVersionUID = -666277544132970083L;

	public ProviderConfigurationException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public ProviderConfigurationException(final String message)
	{
		super(message);
	}
}
