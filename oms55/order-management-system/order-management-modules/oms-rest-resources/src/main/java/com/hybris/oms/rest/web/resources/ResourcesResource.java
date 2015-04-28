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
package com.hybris.oms.rest.web.resources;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.LoggerFactory;

import com.sun.jersey.server.wadl.WadlApplicationContext;
import com.sun.jersey.spi.resource.Singleton;
import com.sun.research.ws.wadl.Application;


/**
 * http://localhost:8080/oms-rest-webapp/webresources/wadl.
 */

@Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_XML})
@Singleton
@Path("/wadl")
public class ResourcesResource
{
	private final WadlApplicationContext appContext;
	private Application application;
	private byte[] wadlHtml;

	/**
	 * Instantiates a new wadl in html format.
	 * 
	 * @param wadlContext
	 *           the wadl context
	 */
	public ResourcesResource(@Context final WadlApplicationContext wadlContext)
	{
		this.appContext = wadlContext;
	}

	/**
	 * Gets the wadl.
	 * 
	 * @param uriInfo
	 *           the uri info
	 * @return the wadl
	 */
	@GET
	public Response getWadl(@Context final UriInfo uriInfo)
	{
		synchronized (this)
		{
			this.application = this.appContext.getApplication(uriInfo).getApplication();
			if (this.wadlHtml == null)
			{
				try
				{
					final byte[] wadlXml = this.getCurrentXmlWadlIntoByteArray();

					final StreamSource xslSource = this.createStreamSourceFromWadlStylesheet();

					final Transformer transformer = this.getTransformer(xslSource);

					final StreamSource xmlSource = this.createStreamSource(wadlXml);

					this.wadlHtml = this.transformToOutputStream(transformer, xmlSource);

				}
				catch (final IOException | TransformerException | JAXBException e)
				{
					LoggerFactory.getLogger(this.getClass()).error("Failed to create HTML documentation of WADL.", e);
					return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).type(MediaType.TEXT_PLAIN)
							.build();
				}
			}
			return Response.ok(new ByteArrayInputStream(this.wadlHtml)).build();
		}
	}


	/**
	 * Creates the stream source.
	 * 
	 * @param wadlXmlRepresentation the wadl xml representation
	 * @return the stream source
	 */
	private StreamSource createStreamSource(final byte[] wadlXmlRepresentation)
	{
		final InputStream xmlStream = new BufferedInputStream(new ByteArrayInputStream(wadlXmlRepresentation));
		return new StreamSource(xmlStream);

	}


	private byte[] getCurrentXmlWadlIntoByteArray() throws JAXBException, IOException
	{
		final Marshaller marshaller = this.appContext.getJAXBContext().createMarshaller();
		final ByteArrayOutputStream stream = new ByteArrayOutputStream();
		marshaller.marshal(this.application, stream);
		final byte[] wadlXmlRepresentation = stream.toByteArray();
		stream.close();
		return wadlXmlRepresentation;
	}


	/**
	 * Gets the transformer.
	 * 
	 * @param xslSource the xsl source
	 * @return the transformer
	 * @throws TransformerConfigurationException the transformer configuration exception
	 */
	private Transformer getTransformer(final StreamSource xslSource) throws TransformerConfigurationException
	{
		final TransformerFactory tFactory = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl", this.getClass()
				.getClassLoader());
		return tFactory.newTransformer(xslSource);
	}

	/**
	 * Transform to output stream.
	 * 
	 * @param transformer the transformer
	 * @param xmlSource the xml source
	 * @return the byte[]
	 * @throws TransformerException the transformer exception
	 */
	private byte[] transformToOutputStream(final Transformer transformer, final StreamSource xmlSource)
			throws TransformerException
	{
		final ByteArrayOutputStream htmlOutputStream = new ByteArrayOutputStream();
		transformer.transform(xmlSource, new StreamResult(htmlOutputStream));

		return htmlOutputStream.toByteArray();

	}

	/**
	 * Creates the stream source from wadl stylesheet.
	 * 
	 * @return the stream source
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 */
	protected StreamSource createStreamSourceFromWadlStylesheet() throws IOException
	{
		final String xslResource = "/wadl_documentation.xsl";
		final URL xslUrl = this.getClass().getResource(xslResource);
		final InputStream xslStream = xslUrl.openStream();
		return new StreamSource(xslStream);
	}

}
