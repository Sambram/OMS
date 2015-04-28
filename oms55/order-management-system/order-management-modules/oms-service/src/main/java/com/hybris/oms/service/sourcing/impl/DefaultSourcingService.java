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

import com.hybris.oms.service.ats.AtsService;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.sourcing.RadianCoordinates;
import com.hybris.oms.service.sourcing.SourcingLine;
import com.hybris.oms.service.sourcing.SourcingResult;
import com.hybris.oms.service.sourcing.SourcingService;
import com.hybris.oms.service.sourcing.builder.SourcingComparatorFactory;
import com.hybris.oms.service.sourcing.builder.SourcingContextBuilder;
import com.hybris.oms.service.sourcing.builder.SourcingMatrixBuilder;
import com.hybris.oms.service.sourcing.context.ProcessStatus;
import com.hybris.oms.service.sourcing.context.SourcingContext;
import com.hybris.oms.service.sourcing.context.SourcingLocation;
import com.hybris.oms.service.sourcing.context.SourcingMatrix;
import com.hybris.oms.service.sourcing.context.SourcingSplitOption;
import com.hybris.oms.service.sourcing.strategy.LineActionsStrategy;
import com.hybris.oms.service.sourcing.strategy.LocationsFilterStrategy;
import com.hybris.oms.service.sourcing.strategy.OrderLineSplitStrategy;
import com.hybris.oms.service.sourcing.strategy.SourcingResultPersistenceStrategy;
import com.hybris.oms.service.sourcing.strategy.SourcingStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Preconditions;


/**
 * Default implementation of {@link SourcingService}.
 */
@SuppressWarnings("PMD.TooManyPublicMethods")
public class DefaultSourcingService implements SourcingService, InitializingBean
{
	private static final Logger LOG = LoggerFactory.getLogger(SourcingService.class);

	private SourcingContextBuilder contextBuilder;

	private SourcingMatrixBuilder sourcingMatrixBuilder;

	private String atsDescriptor;

	private String distanceDescriptor;

	private String sequenceDescriptor;

	private SourcingComparatorFactory comparatorFactory;

	private Comparator<SourcingLocation> atsComparator;

	private Comparator<SourcingLocation> distanceComparator;

	private Comparator<SourcingLocation> sequenceComparator;

	private SourcingResultPersistenceStrategy resultPersistenceStrategy;

	private LineActionsStrategy lineActionsStrategy;

	private OrderService orderService;

	private InventoryService inventoryService;

	private AtsService atsService;

	private LocationsFilterStrategy<OrderData, Set<String>> locationsFilterStrategy;

	private OrderLineSplitStrategy orderLineSplitStrategy;

	private List<SourcingStrategy> sourcingStrategies;

	private SourcingResultConverter sourcingResultConverter;

	private Map<String, SourcingSplitOption> sourcingSplitOptions;

	private SourcingComparatorFactory sourcingComparatorFactory;

	@Override
	public OrderData sourceOrder(final String orderId)
	{
		Preconditions.checkArgument(orderId != null, "orderId cannot be null");
		final OrderData order = this.orderService.getOrderByOrderId(orderId);
		final List<SourcingLine> sourcingLines = getUnassignedSourcingLines(order);
		if (sourcingLines != null)
		{
			final Set<String> locationIds = locationsFilterStrategy.filter(order, new HashSet<String>());
			final RadianCoordinates shippingCoordinates = getShipToCoordinates(order);
			final SourcingResult sourcingResult = this.sourceInput(sourcingLines, locationIds, shippingCoordinates);
			lineActionsStrategy.processLineActions(order, sourcingResult);
			resultPersistenceStrategy.persistSourcingResult(order, sourcingResult);
		}
		return order;
	}

	protected RadianCoordinates getShipToCoordinates(final OrderData order)
	{
		RadianCoordinates shippingCoordinates = null;
		if (order.getShippingAddress() != null)
		{
			shippingCoordinates = RadianCoordinates.fromOptionalDegrees(order.getShippingAddress().getLatitudeValue(), order
					.getShippingAddress().getLongitudeValue());
		}
		return shippingCoordinates;
	}

	protected List<SourcingLine> getUnassignedSourcingLines(final OrderData order)
	{
		List<SourcingLine> sourcingLines = null;
		for (final OrderLineData orderLine : order.getOrderLines())
		{
			if (orderLine.getQuantityUnassignedValue() > 0)
			{
				if (sourcingLines == null)
				{
					sourcingLines = new ArrayList<>();
				}
				sourcingLines.addAll(orderLineSplitStrategy.getSourcingLines(orderLine));
			}
		}
		return sourcingLines;
	}

	@Override
	public SourcingResult sourceInput(final List<SourcingLine> sourcingLines)
	{
		return this.sourceInput(sourcingLines, null, null);
	}

	@Override
	public SourcingResult sourceInput(final List<SourcingLine> sourcingLines, final Set<String> filterLocationIds,
			final RadianCoordinates shipToCoordinates)
	{
		Preconditions.checkArgument(sourcingLines != null, "sourcingLines cannot be null");
		SourcingResult result = null;
		if (CollectionUtils.isEmpty(sourcingLines))
		{
			LOG.warn("Empty sourcing lines - nothing to process");
			result = new DefaultSourcingResult();
		}
		else
		{
			final SourcingContext context = this.contextBuilder.createContext(sourcingLines, filterLocationIds, shipToCoordinates);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Starting sourceInput using {}", context.toString());
			}
			for (final SourcingStrategy strategy : this.sourcingStrategies)
			{
				strategy.source(context);
				if (context.getProcessStatus().isProcessFinished())
				{
					break;
				}
			}
			final ProcessStatus status = context.getProcessStatus();
			result = sourcingResultConverter.buildSourcingResult(sourcingLines, status);
			if (result.isEmpty())
			{
				LOG.warn("Empty sourcing result for sourcingLines {}: {}", sourcingLines, result);
			}
			else if (LOG.isDebugEnabled())
			{
				LOG.debug("sourceInput finished: {}", result);
			}
		}
		return result;
	}

	@Override
	public StockroomLocationData simulateSourcing(final List<SourcingLine> sourcingLines, final String atsId,
			final Set<String> filterLocationIds, final RadianCoordinates shippingCoordinates)
	{
		Preconditions.checkArgument(CollectionUtils.isNotEmpty(sourcingLines), "sourcingLines cannot be empty");
		String sourcingAtsId = atsId;
		final Comparator<SourcingLocation> comparator = this.getComparatorForSimulation(atsId, shippingCoordinates);
		if (StringUtils.isBlank(sourcingAtsId))
		{
			sourcingAtsId = this.atsService.getDefaultAtsId();
		}
		final SourcingMatrix matrix = this.sourcingMatrixBuilder.createSourcingMatrix(sourcingLines, sourcingAtsId,
				filterLocationIds, shippingCoordinates);
		if (LOG.isDebugEnabled())
		{
			LOG.debug("simulateSourcing uses comparator {} on {}", comparator, matrix);
		}
		final List<SourcingLocation> locations = matrix.getTotalSourcingLocations(comparator);
		StockroomLocationData result = null;
		if (!locations.isEmpty())
		{
			final String locationId = locations.get(0).getInfo().getLocationId();
			result = this.inventoryService.getLocationByLocationId(locationId);
			LOG.debug("simulateSourcing returns location {}", locationId);
		}
		else
		{
			LOG.warn("Empty sourcing result for sourcingLines {}: {}", sourcingLines, matrix);
		}
		return result;
	}

	protected Comparator<SourcingLocation> getComparatorForSimulation(final String atsId,
			final RadianCoordinates shippingCoordinates)
	{
		Comparator<SourcingLocation> comparator;
		if (StringUtils.isNotBlank(atsId))
		{
			comparator = this.atsComparator;
		}
		else if (shippingCoordinates != null)
		{
			comparator = this.distanceComparator;
		}
		else
		{
			comparator = this.sequenceComparator;
		}
		return comparator;
	}

	@Override
	public Set<String> findAllSourcingSplitOptions()
	{
		return Collections.unmodifiableSet(this.sourcingSplitOptions.keySet());
	}

	@Override
	public Set<String> findAllSourcingLocationComparators()
	{
		final Set<String> keyset = new HashSet<>(sourcingComparatorFactory.getComparatorMap().keySet());
		keyset.remove(SourcingComparatorFactory.getDefaultComparatorId());
		return keyset;
	}

	@Required
	public void setSourcingStrategies(final List<SourcingStrategy> sourcingStrategies)
	{
		this.sourcingStrategies = sourcingStrategies;
	}

	@Required
	public void setAtsDescriptor(final String atsDescriptor)
	{
		this.atsDescriptor = atsDescriptor;
	}

	@Required
	public void setDistanceDescriptor(final String distanceDescriptor)
	{
		this.distanceDescriptor = distanceDescriptor;
	}

	@Required
	public void setSequenceDescriptor(final String sequenceDescriptor)
	{
		this.sequenceDescriptor = sequenceDescriptor;
	}

	@Required
	public void setOrderLineSplitStrategy(final OrderLineSplitStrategy orderLineSplitStrategy)
	{
		this.orderLineSplitStrategy = orderLineSplitStrategy;
	}

	@Required
	public void setSourcingResultBuilder(final SourcingResultConverter sourcingResultConverter)
	{
		this.sourcingResultConverter = sourcingResultConverter;
	}

	@Required
	public void setContextBuilder(final SourcingContextBuilder contextBuilder)
	{
		this.contextBuilder = contextBuilder;
	}

	@Required
	public void setComparatorFactory(final SourcingComparatorFactory comparatorFactory)
	{
		this.comparatorFactory = comparatorFactory;
	}

	@Required
	public void setResultPersistenceStrategy(final SourcingResultPersistenceStrategy resultPersistenceStrategy)
	{
		this.resultPersistenceStrategy = resultPersistenceStrategy;
	}

	@Required
	public void setSourcingMatrixBuilder(final SourcingMatrixBuilder sourcingMatrixBuilder)
	{
		this.sourcingMatrixBuilder = sourcingMatrixBuilder;
	}

	@Required
	public void setOrderService(final OrderService orderService)
	{
		this.orderService = orderService;
	}

	@Required
	public void setInventoryService(final InventoryService inventoryService)
	{
		this.inventoryService = inventoryService;
	}

	@Required
	public void setAtsService(final AtsService atsService)
	{
		this.atsService = atsService;
	}

	@Required
	public void setLocationsFilterStrategy(final LocationsFilterStrategy<OrderData, Set<String>> locationsFilterStrategy)
	{
		this.locationsFilterStrategy = locationsFilterStrategy;
	}

	@Required
	public void setLineActionsStrategy(final LineActionsStrategy lineActionsStrategy)
	{
		this.lineActionsStrategy = lineActionsStrategy;
	}

	@Required
	public void setSourcingSplitOptions(final Map<String, SourcingSplitOption> sourcingSplitOptions)
	{
		this.sourcingSplitOptions = sourcingSplitOptions;
	}

	@Required
	public void setSourcingComparatorFactory(final SourcingComparatorFactory sourcingComparatorFactory)
	{
		this.sourcingComparatorFactory = sourcingComparatorFactory;
	}

	protected List<SourcingStrategy> getSourcingStrategies()
	{
		return sourcingStrategies;
	}

	protected SourcingContextBuilder getContextBuilder()
	{
		return contextBuilder;
	}

	protected SourcingMatrixBuilder getSourcingMatrixBuilder()
	{
		return sourcingMatrixBuilder;
	}

	protected String getAtsDescriptor()
	{
		return atsDescriptor;
	}

	protected String getDistanceDescriptor()
	{
		return distanceDescriptor;
	}

	protected String getSequenceDescriptor()
	{
		return sequenceDescriptor;
	}

	protected SourcingComparatorFactory getComparatorFactory()
	{
		return comparatorFactory;
	}

	protected Comparator<SourcingLocation> getAtsComparator()
	{
		return atsComparator;
	}

	protected Comparator<SourcingLocation> getDistanceComparator()
	{
		return distanceComparator;
	}

	protected Comparator<SourcingLocation> getSequenceComparator()
	{
		return sequenceComparator;
	}

	protected SourcingResultPersistenceStrategy getResultPersistenceStrategy()
	{
		return resultPersistenceStrategy;
	}

	protected OrderService getOrderService()
	{
		return orderService;
	}

	protected InventoryService getInventoryService()
	{
		return inventoryService;
	}

	protected AtsService getAtsService()
	{
		return atsService;
	}

	protected OrderLineSplitStrategy getOrderLineSplitStrategy()
	{
		return orderLineSplitStrategy;
	}

	protected SourcingResultConverter getSourcingResultBuilder()
	{
		return sourcingResultConverter;
	}

	protected LocationsFilterStrategy<OrderData, Set<String>> getLocationsFilterStrategy()
	{
		return locationsFilterStrategy;
	}

	protected LineActionsStrategy getLineActionsStrategy()
	{
		return lineActionsStrategy;
	}

	@Override
	public void afterPropertiesSet()
	{
		this.atsComparator = this.comparatorFactory.createComparator(this.atsDescriptor);
		this.distanceComparator = this.comparatorFactory.createComparator(this.distanceDescriptor);
		this.sequenceComparator = this.comparatorFactory.createComparator(this.sequenceDescriptor);
		Preconditions.checkArgument(CollectionUtils.isNotEmpty(sourcingStrategies), "sourcingStrategies cannot be empty");
	}

}
