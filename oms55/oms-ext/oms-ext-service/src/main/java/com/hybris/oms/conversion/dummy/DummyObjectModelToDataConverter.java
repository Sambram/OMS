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
package com.hybris.oms.conversion.dummy;

import com.hybris.commons.conversion.impl.AbstractPopulatingConverter;
import com.hybris.oms.domain.DummyObjectData;
import com.hybris.oms.api.dto.DummyObject;
import com.hybris.oms.services.DummyObjectService;


/**
 * Implementation of {@link AbstractPopulatingConverter} to convert {@link DummyObject} to {@link DummyObjectData}.
 */
public class DummyObjectModelToDataConverter extends AbstractPopulatingConverter<DummyObject, DummyObjectData>
{

	private DummyObjectService dummyObjectService;

	@Override
	protected DummyObjectData createTarget()
	{
		return dummyObjectService.createDummyObject();
	}

	public void setDummyObjectService(final DummyObjectService dummyObjectService)
	{
		this.dummyObjectService = dummyObjectService;
	}

}
