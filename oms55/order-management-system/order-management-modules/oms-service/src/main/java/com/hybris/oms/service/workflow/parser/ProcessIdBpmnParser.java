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
package com.hybris.oms.service.workflow.parser;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


/**
 * Parses an XML bpmn diagram using XPath expressions.
 */
public class ProcessIdBpmnParser implements InitializingBean
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessIdBpmnParser.class);
	private String xpathExpession;

	/**
	 * Parse a bpmn {@link Resource} to extract a string matching a given expression.
	 * 
	 * @param resource
	 * @return the process id
	 */
	public String parseResource(final Resource resource)
	{
		LOGGER.info("Parsing resource {} with XPath expression [{}].", resource, xpathExpession);

		InputStream in = null;
		try
		{
			final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

			final DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();

			in = resource.getInputStream();
			final Document xmlDocument = builder.parse(in);

			final XPath xPath = XPathFactory.newInstance().newXPath();
			final String processId = xPath.compile(xpathExpession).evaluate(xmlDocument);

			LOGGER.info("Parser found processId [{}].", processId);
			return processId;
		}
		catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e)
		{
			throw new BpmnParserException(String.format("Can't parse resource %s.", resource), e);
		}
		finally
		{
			IOUtils.closeQuietly(in);
		}
	}

	protected String getXpathExpession()
	{
		return xpathExpession;
	}

	@Required
	public void setXpathExpession(final String xpathExpession)
	{
		this.xpathExpession = xpathExpession;
	}

	@Override
	public void afterPropertiesSet()
	{
		final XPathFactory factory = XPathFactory.newInstance();
		final XPath xpath = factory.newXPath();

		try
		{
			xpath.compile(xpathExpession);
		}
		catch (final Exception e)
		{
			throw new IllegalArgumentException(String.format("Invalid xPath expression %s.", xpathExpession), e);
		}
	}
}
