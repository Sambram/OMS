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
package com.hybris.oms.facade;

import com.hybris.kernel.api.exceptions.SystemException;
import com.hybris.kernel.persistence.StaleStateException;
import com.hybris.oms.domain.exception.ModifiedSinceLastReadException;
import com.hybris.oms.domain.exception.PersistenceException;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Aspect
public class PersistenceExceptionAdvice
{
	private static final Logger LOG = LoggerFactory.getLogger(PersistenceExceptionAdvice.class);
	private static final String PERSISTENCE_EXP_MESSAGE = "Database error while trying to fulfill your request";
	private static final String EX_MSG_CONFLICT = "Conflict occured while trying to update a record in the database.";

	@AfterThrowing(pointcut = "within(com.hybris.oms.facade..*)", throwing = "exception")
	public void afterThrowingSystemException(final SystemException exception)
	{
		LOG.warn("A kernel exception has been thrown ", exception);
		throw new PersistenceException(PERSISTENCE_EXP_MESSAGE, exception);
	}

	@AfterThrowing(pointcut = "within(com.hybris.oms.facade..*)", throwing = "exception")
	public void afterThrowingStaleStateException(final StaleStateException exception)
	{
		LOG.warn("A kernel exception has been thrown ", exception);
		throw new ModifiedSinceLastReadException(EX_MSG_CONFLICT, exception);
	}
}
