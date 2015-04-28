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
package com.hybris.oms.export.api.ats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;


public class AvailabilityToSellDTOMarshallerTest
{
	@Test
	public void testXMLFormat() throws IOException, JAXBException
	{
		final AvailabilityToSellDTO dto = this.unmarshall("/test-ats-dto.xml");
		assertNotNull("Successfully unmarshalled.", dto);
		assertEquals("two quantities.", 2, dto.getQuantities().size());
		for (final AvailabilityToSellQuantityDTO quantity : dto.getQuantities())
		{
			assertEquals("correct sku.", "123456", quantity.getSkuId());
			if (quantity.getLocationId() == null)
			{
				assertEquals("value", 88, quantity.getQuantity());
			}
			else
			{
				assertEquals("value", 5, quantity.getQuantity());
				assertEquals("locationId", "location", quantity.getLocationId());
			}
		}

		// remarshall
		final String xml = this.marshall(dto);
		assertNotNull("Successfully marshalled.", xml);
		assertEquals("Marshalled content does not contain character sequence 'quantities'.", -1, xml.indexOf("quantities"));
	}

	private AvailabilityToSellDTO unmarshall(final String resourceName) throws IOException, JAXBException
	{
		try (final InputStream in = this.getClass().getResourceAsStream(resourceName))
		{
			return this.unmarshall(in);
		}
	}

	private AvailabilityToSellDTO unmarshall(final InputStream in) throws JAXBException
	{
		final Unmarshaller marshaller = JAXBContext.newInstance(AvailabilityToSellDTO.class, AvailabilityToSellQuantityDTO.class)
				.createUnmarshaller();
		return (AvailabilityToSellDTO) marshaller.unmarshal(in);
	}

	private String marshall(final Object obj) throws JAXBException
	{
		final Marshaller marshaller = JAXBContext.newInstance(AvailabilityToSellDTO.class, AvailabilityToSellQuantityDTO.class)
				.createMarshaller();
		final StringWriter out = new StringWriter();
		marshaller.marshal(obj, out);
		return out.toString();
	}
}
