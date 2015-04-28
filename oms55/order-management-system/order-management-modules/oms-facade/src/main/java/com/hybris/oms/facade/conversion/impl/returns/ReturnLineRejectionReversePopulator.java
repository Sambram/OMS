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
package com.hybris.oms.facade.conversion.impl.returns;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.Populator;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.oms.domain.returns.ReturnLineRejection;
import com.hybris.oms.service.managedobjects.returns.ReturnLineRejectionData;
import com.hybris.oms.service.managedobjects.returns.ReturnOrderLineData;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.StringUtils;


/**
 * Converts return line rejection dto to data.
 */
public class ReturnLineRejectionReversePopulator implements Populator<ReturnLineRejection, ReturnLineRejectionData>
{
	private PersistenceManager persistenceManager;

	@Override
	public void populate(final ReturnLineRejection source, final ReturnLineRejectionData target) throws ConversionException,
			IllegalArgumentException
	{
		target.setQuantity(source.getQuantity());
		target.setResponsible(source.getResponsible());
		target.setReason(source.getReason().name());
	}

	@Override
	public void populateFinals(final ReturnLineRejection source, final ReturnLineRejectionData target) throws ConversionException,
			IllegalArgumentException
	{
		try
		{
			target.setMyReturnOrderLine(persistenceManager.getByIndex(ReturnOrderLineData.UX_RETURNORDERLINES_RETURNORDERLINEID,
					Long.valueOf(source.getReturnOrderLineId())));
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new ConversionException(e.getMessage());
		}

		if (!StringUtils.isEmpty(source.getRejectionId()))
		{
			target.setRejectionId(new Long(source.getRejectionId()));
		}
	}

	@Required
	public void setPersistenceManager(final PersistenceManager persistenceManager)
	{
		this.persistenceManager = persistenceManager;
	}
}
