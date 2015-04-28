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
package com.hybris.oms.facade.conversion.impl.basestore;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.Populator;
import com.hybris.oms.domain.basestore.BaseStore;
import com.hybris.oms.service.managedobjects.basestore.BaseStoreData;


/**
 * Reverse converter for base store dto to data.
 */
public class BaseStoreReversePopulator implements Populator<BaseStore, BaseStoreData>
{

	@Override
	public void populateFinals(final BaseStore source, final BaseStoreData target) throws ConversionException
	{
		target.setName(source.getName());
	}

	@Override
	public void populate(final BaseStore source, final BaseStoreData target) throws ConversionException
	{
		target.setDescription(source.getDescription());
	}

}
