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
import com.hybris.oms.domain.ats.AtsLocalQuantities;
import com.hybris.oms.domain.ats.AtsQuantity;
import com.hybris.oms.service.ats.AtsResult;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class AtsLocalQuantitiesConverterTest
{

	private static final String SKU = "sku";
	private static final String ATS_ID = "atsId";
	private static final String LOCATION_ID = "locationId";
	private static final Integer QUANTITY = 5;
	private static final Long MILLISTIME = null;

	@Autowired
	private Converter<AtsResult, List<AtsLocalQuantities>> atsLocalQuantitiesConverter;

	@Test
	public void shouldConvertAtsResult()
	{
		final AtsResult result = new AtsResult();
		result.addResult(LOCATION_ID, SKU, ATS_ID, QUANTITY.intValue(), MILLISTIME);
		final List<AtsLocalQuantities> atsLocalQuantities = this.atsLocalQuantitiesConverter.convert(result);
		Assert.assertEquals(1, atsLocalQuantities.size());

		final AtsLocalQuantities atsLocalQuantity = atsLocalQuantities.get(0);
		Assert.assertEquals(LOCATION_ID, atsLocalQuantity.getLocationId());

		final AtsQuantity atsQuantity = atsLocalQuantity.getAtsQuantities().get(0);
		Assert.assertEquals(SKU, atsQuantity.getSku());
		Assert.assertEquals(ATS_ID, atsQuantity.getAtsId());
		Assert.assertEquals(QUANTITY, Integer.valueOf(atsQuantity.getQuantity().getValue()));
	}

}
