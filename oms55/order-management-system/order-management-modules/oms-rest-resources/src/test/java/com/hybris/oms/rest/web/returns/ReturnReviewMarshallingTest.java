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
package com.hybris.oms.rest.web.returns;

import com.hybris.oms.domain.returns.ReturnLineRejection;
import com.hybris.oms.domain.returns.ReturnReview;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import static org.junit.Assert.assertEquals;

import com.hybris.oms.domain.returns.ReviewReason;
import org.junit.Test;


public class ReturnReviewMarshallingTest
{
	@Test
	public void marshallAndUnmarshall() throws JAXBException
	{
		final JAXBContext context = JAXBContext.newInstance(ReturnReview.class, ReturnLineRejection.class);

		final Writer outStream = new StringWriter();
		final Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		final ReturnReview returnReview = new ReturnReview();
		returnReview.setReturnId("1");

		final ReturnLineRejection returnLineRejection = new ReturnLineRejection();
		returnLineRejection.setQuantity(Integer.valueOf(10));
		returnLineRejection.setReason(ReviewReason.DAMAGED);
		returnLineRejection.setReturnOrderLineId("1");
		returnLineRejection.setRejectionId("1");
		returnLineRejection.setResponsible("Some Guy");
		returnReview.setReturnLineRejections(Collections.singletonList(returnLineRejection));

		marshaller.marshal(returnReview, System.out);
		marshaller.marshal(returnReview, outStream);

		final Unmarshaller unmarshaller = context.createUnmarshaller();
		final ReturnReview result = (ReturnReview) unmarshaller.unmarshal(new StringReader(outStream.toString()));
		assertEquals("1", result.getReturnId());
		assertEquals("1", result.getReturnLineRejections().get(0).getReturnOrderLineId());
	}
}
