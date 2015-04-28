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
package com.hybris.oms.service.health.impl;

import com.hybris.commons.tenant.TenantContextTemplate;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;


/**
 * Helper class for executing operation for given tenant within one transaction.
 */
@SuppressWarnings({"PMD.SignatureDeclareThrowsException", "PMD.AvoidCatchingGenericException"})
public abstract class TransactionalTenantOperation extends TenantContextTemplate<Void>
{
	private final PlatformTransactionManager transactionManager;

	public TransactionalTenantOperation(final String tenant, final PlatformTransactionManager transactionManager)
	{
		super(tenant);
		this.transactionManager = transactionManager;
	}

	protected abstract void executeInTransaction() throws Exception;

	@Override
	public final Void execute() throws Exception
	{
		final TransactionTemplate template = new TransactionTemplate(transactionManager);
		return template.execute(new TransactionCallback<Void>()
		{
			@Override
			public Void doInTransaction(final TransactionStatus status)
			{
				try
				{
					executeInTransaction();
					return null;
				}
				// CHECKSTYLE IGNORE IllegalCatchCheck NEXT 1
				catch (final Exception e)
				{
					throw new IllegalStateException("Error executing the transaction", e);
				}
			}
		});
	}
}
