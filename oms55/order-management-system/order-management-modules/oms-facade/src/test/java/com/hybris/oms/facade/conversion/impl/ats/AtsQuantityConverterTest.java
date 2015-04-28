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
package com.hybris.oms.facade.conversion.impl.ats;

import com.hybris.commons.conversion.Converter;
import com.hybris.oms.domain.ats.AtsQuantity;
import com.hybris.oms.service.ats.AtsResult.AtsRow;
import com.hybris.oms.service.ats.AtsResult.Key;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class AtsQuantityConverterTest
{

	private static final String SKU = "sku";
	private static final String ATS_ID = "atsId";
	private static final String LOCATION_ID = "locationId";
	private static final Integer QUANTITY = 5;
	private static final Long MILLISTIME = null;

	@Autowired
	private Converter<AtsRow, AtsQuantity> atsQuantityConverter;

	@Test
	public void shouldConvertAtsRow()
	{
		final AtsRow row = new AtsRow(new Key(SKU, ATS_ID, LOCATION_ID, MILLISTIME), QUANTITY);
		final AtsQuantity atsQuantity = this.atsQuantityConverter.convert(row);

		Assert.assertEquals(SKU, atsQuantity.getSku());
		Assert.assertEquals(ATS_ID, atsQuantity.getAtsId());
		Assert.assertEquals(QUANTITY, Integer.valueOf(atsQuantity.getQuantity().getValue()));
	}

}
