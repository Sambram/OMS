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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.domain.returns.ReturnLineRejection;
import com.hybris.oms.domain.returns.ReviewReason;
import com.hybris.oms.service.managedobjects.returns.ReturnLineRejectionData;
import com.hybris.oms.service.managedobjects.returns.ReturnOrderLineData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class ReturnLineRejectionReversePopulatorTest
{
	ReturnLineRejectionData returnLineRejectionData;

	ReturnOrderLineData returnOrderLineData;

	ReturnLineRejectionReversePopulator populator;

	ReturnLineRejection returnLineRejection;

	final String RESPONSIBLE = "John";

	@Autowired
	private PersistenceManager persistenceManager;

	@Before
	public void setup()
	{
		populator = new ReturnLineRejectionReversePopulator();
		returnLineRejection = new ReturnLineRejection();
	}

	@Transactional
	@Test
	public void itShouldPopulateFromDataObjectToDTOObject()
	{
		returnLineRejection.setQuantity(5);
		returnLineRejection.setResponsible(RESPONSIBLE);
		returnLineRejection.setReason(ReviewReason.DAMAGED);
		returnLineRejection.setReturnOrderLineId("15");

		returnOrderLineData = persistenceManager.create(ReturnOrderLineData.class);
		returnLineRejectionData = persistenceManager.create(ReturnLineRejectionData.class);
		returnLineRejectionData.setMyReturnOrderLine(returnOrderLineData);

		populator.populate(returnLineRejection, returnLineRejectionData);

		assertEquals(5, returnLineRejectionData.getQuantity());
		assertTrue(RESPONSIBLE.equals(returnLineRejectionData.getResponsible()));
		assertTrue(returnLineRejectionData.getReason().equals(ReviewReason.DAMAGED.name()));
		assertTrue(returnLineRejectionData.getMyReturnOrderLine().getReturnOrderLineId() > 0);
	}
}
