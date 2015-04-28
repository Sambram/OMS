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
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.domain.ats.AtsFormula;
import com.hybris.oms.service.managedobjects.ats.AtsFormulaData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class AtsFormulaConverterTest
{
	private static final String NAME = "Name";
	private static final String FORMULA = "Formula";
	private static final String DESCRIPTION = "Description";
	private static final String ATS_ID = "AtsId";

	@Autowired
	private Converter<AtsFormulaData, AtsFormula> atsFormulaConverter;

	@Autowired
	private PersistenceManager persistenceManager;

	private AtsFormulaData atsFormulaData;

	@Before
	public void setUp()
	{
		// given
		this.atsFormulaData = this.persistenceManager.create(AtsFormulaData.class);
		this.atsFormulaData.setAtsId(ATS_ID);
		this.atsFormulaData.setDescription(DESCRIPTION);
		this.atsFormulaData.setFormula(FORMULA);
		this.atsFormulaData.setName(NAME);
	}

	@Transactional
	@Test
	public void testConvertAtsFormulaData()
	{
		// when
		final AtsFormula atsFormula = this.atsFormulaConverter.convert(this.atsFormulaData);

		// then
		assertValid(atsFormula);
	}

	private void assertValid(final AtsFormula atsFormula)
	{
		Assert.assertEquals(ATS_ID, atsFormula.getAtsId());
		Assert.assertEquals(DESCRIPTION, atsFormula.getDescription());
		Assert.assertEquals(FORMULA, atsFormula.getFormula());
		Assert.assertEquals(NAME, atsFormula.getName());
	}
}
