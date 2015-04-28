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

import com.hybris.kernel.api.exceptions.TypeCodeNotFoundException;
import com.hybris.kernel.persistence.StaleStateException;
import com.hybris.oms.domain.exception.ModifiedSinceLastReadException;
import com.hybris.oms.domain.exception.PersistenceException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public class PersistenceExceptionAdviceTest
{

	private PersistenceExceptionAdvice advice;

	@Before
	public void instanciateClassUnderTest()
	{
		advice = new PersistenceExceptionAdvice();
	}

	@Test(expected = PersistenceException.class)
	public void shouldSupportNull()
	{
		advice.afterThrowingSystemException(null);
	}

	@Test(expected = PersistenceException.class)
	public void shouldThrowPersistenceException()
	{
		advice.afterThrowingSystemException(new TypeCodeNotFoundException("PersistenceExceptionAdviceTest"));
	}

	@Test(expected = ModifiedSinceLastReadException.class)
	public void shouldThrowStaleStateException()
	{
		advice.afterThrowingStaleStateException(new StaleStateException("PersistenceExceptionAdviceTest"));
	}

}
