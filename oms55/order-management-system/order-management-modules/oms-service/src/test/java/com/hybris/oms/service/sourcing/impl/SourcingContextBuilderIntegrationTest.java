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
package com.hybris.oms.service.sourcing.impl;

import com.hybris.kernel.api.ImportService;
import com.hybris.oms.service.sourcing.SourcingLine;
import com.hybris.oms.service.sourcing.builder.SourcingContextBuilder;
import com.hybris.oms.service.sourcing.context.ProcessStatus;
import com.hybris.oms.service.sourcing.context.PropertiesBuilder;
import com.hybris.oms.service.sourcing.context.SourcingConfiguration;
import com.hybris.oms.service.sourcing.context.SourcingContext;
import com.hybris.oms.service.sourcing.context.SourcingLocation;
import com.hybris.oms.service.sourcing.context.SourcingSplitOption;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


/**
 * Integration test for {@link SourcingContextBuilder}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-service-spring-test.xml"})
public class SourcingContextBuilderIntegrationTest
{
	private static final String TEST_PROP_KEY = "test";
	private static final Integer TEST_PROP_VALUE = 51;

	@Autowired
	private SourcingContextBuilder builder;

	@Autowired
	@Qualifier("sourcingAtsComparator")
	private Comparator<SourcingLocation> atsComparator;

	@Autowired
	@Qualifier("sourcingDistanceComparator")
	private Comparator<SourcingLocation> distanceComparator;

	@Autowired
	@Qualifier("sourcingPriorityComparator")
	private Comparator<SourcingLocation> sequenceComparator;

	@Autowired
	@Qualifier("sourcingDefaultComparator")
	private Comparator<SourcingLocation> defaultComparator;

	@Autowired
	private ImportService importService;

	@Before
	public void setUp()
	{
		importService.loadMcsvResource(new ClassPathResource("/META-INF/essential-data-import.mcsv"));
	}

	@Test
	@Transactional
	public void shouldCreateContext()
	{

		final SourcingContext context = this.builder.createContext(Collections.<SourcingLine>emptyList(), null, null);
		final SourcingConfiguration conf = context.getConfiguration();
		Assert.assertEquals(false, conf.isMinimizeShipments());
		Assert.assertEquals(SourcingSplitOption.SPLIT, conf.getOrderSplit());
		Assert.assertEquals(SourcingSplitOption.SPLIT, conf.getOrderLineSplit());
		final SourcingLocation.CompositeComparator<SourcingLocation> orderComp = (SourcingLocation.CompositeComparator<SourcingLocation>) conf
				.getOrderComparator();
		Assertions.assertThat(orderComp.getComparators()).containsSequence(this.distanceComparator, this.sequenceComparator,
				this.defaultComparator);
		final SourcingLocation.CompositeComparator<SourcingLocation> olComp = (SourcingLocation.CompositeComparator<SourcingLocation>) conf
				.getOrderLineComparator();
		Assertions.assertThat(olComp.getComparators()).containsSequence(this.distanceComparator, this.sequenceComparator,
				this.defaultComparator);
		final SourcingLocation.CompositeComparator<SourcingLocation> olqComp = (SourcingLocation.CompositeComparator<SourcingLocation>) conf
				.getOlqComparator();
		Assertions.assertThat(olqComp.getComparators()).containsSequence(this.atsComparator, this.distanceComparator,
				this.sequenceComparator, this.defaultComparator);
		Assert.assertEquals("WEB", conf.getAtsId());
		final ProcessStatus status = context.getProcessStatus();
		Assertions.assertThat(status.getProcessedLines()).isEmpty();
		Assertions.assertThat(status.getSourcingOlqs()).isEmpty();
		Assert.assertFalse(status.isProcessFinished());
		Assertions.assertThat(context.getSourcingLines()).isEmpty();
		Assert.assertTrue("sourcingMatrix is empty", context.getSourcingMatrix().isEmpty());
	}

	@Test
	@Transactional
	public void shouldPopulateProperties()
	{
		builder.setPropertiesBuilders(Collections.<PropertiesBuilder<Void>>singletonList(new PropertiesBuilder<Void>()
				{

			@Override
			public Map<String, Object> addProperties(final Void source, final Map<String, Object> properties)
			{
				return Collections.<String, Object>singletonMap(TEST_PROP_KEY, TEST_PROP_VALUE);
			}

				}));
		final SourcingContext context = this.builder.createContext(Collections.<SourcingLine>emptyList(), null, null);
		Assert.assertEquals(1, context.getConfiguration().getProperties().size());
		Assert.assertEquals(TEST_PROP_VALUE, context.getConfiguration().getProperties().get(TEST_PROP_KEY));
	}
}
