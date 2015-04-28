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
package com.hybris.oms.service.sourcing.context;

import com.hybris.oms.service.sourcing.PropertiesSupport;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.base.Preconditions;


/**
 * Default implementation of {@link SourcingConfiguration}.
 */
public class SourcingConfiguration extends PropertiesSupport
{
	private final boolean minimizeShipments;
	private final String atsId;
	private final Comparator<SourcingLocation> orderComparator;
	private final Comparator<SourcingLocation> orderLineComparator;
	private final Comparator<SourcingLocation> olqComparator;
	private final SourcingSplitOption orderSplit;
	private final SourcingSplitOption orderLineSplit;

	public SourcingConfiguration(final String atsId, final boolean minimizeShipments)
	{
		this(atsId, minimizeShipments, SourcingSplitOption.SPLIT, SourcingSplitOption.SPLIT);
	}

	public SourcingConfiguration(final String atsId, final boolean minimizeShipments, final SourcingSplitOption orderSplit,
			final SourcingSplitOption orderLineSplit)
	{
		this(atsId, minimizeShipments, orderSplit, orderLineSplit, null, null);
	}

	/**
	 * Creates an {@link SourcingConfiguration} for internal usage.
	 * 
	 * @throws IllegalArgumentException if the atsId is blank
	 */
	@SuppressWarnings("PMD.ExcessiveParameterList")
	public SourcingConfiguration(final String atsId, final boolean minimizeShipments, final SourcingSplitOption orderSplit,
			final SourcingSplitOption orderLineSplit, final List<Comparator<SourcingLocation>> comparators,
			final Map<String, Object> properties)
	{
		super(properties);
		Preconditions.checkArgument(StringUtils.isNotBlank(atsId), "atsId cannot be blank");
		this.atsId = atsId;
		this.minimizeShipments = minimizeShipments;
		this.orderSplit = orderSplit;
		this.orderLineSplit = orderLineSplit;
		if (comparators == null)
		{
			orderComparator = new SourcingLocation.LocationIdComparator();
			orderLineComparator = orderComparator;
			olqComparator = orderComparator;
		}
		else
		{
			this.orderComparator = comparators.get(0);
			this.orderLineComparator = comparators.get(1);
			this.olqComparator = comparators.get(2);
		}
	}

	/**
	 * @return the configured atsId used for ATS calculations.
	 */
	public String getAtsId()
	{
		return this.atsId;
	}

	/**
	 * Checks shipment.
	 * 
	 * @return <tt>true</tt> if item grouping is enabled in the tenant preferences.
	 */
	public boolean isMinimizeShipments()
	{
		return this.minimizeShipments;
	}

	/**
	 * Gets order comparator.
	 * 
	 * @return the {@link Comparator} to use for a sorting matrix row on the order level.
	 */
	public Comparator<SourcingLocation> getOrderComparator()
	{
		return this.orderComparator;
	}

	/**
	 * Gets orderline comparator.
	 * 
	 * @return the {@link Comparator} to use for sorting a matrix row on the orderline level.
	 */
	public Comparator<SourcingLocation> getOrderLineComparator()
	{
		return this.orderLineComparator;
	}

	/**
	 * Gets olq comparator.
	 * 
	 * @return the {@link Comparator} to use for sorting a matrix rowon the olq level.
	 */
	public Comparator<SourcingLocation> getOlqComparator()
	{
		return this.olqComparator;
	}

	/**
	 * Gets order split.
	 * 
	 * @return the {@link SourcingSplitOption} to use on the order level.
	 */
	public SourcingSplitOption getOrderSplit()
	{
		return this.orderSplit;
	}

	/**
	 * Gets orderline split.
	 * 
	 * @return the {@link SourcingSplitOption} to use on the orderline level.
	 */
	public SourcingSplitOption getOrderLineSplit()
	{
		return this.orderLineSplit;
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("atsId", this.atsId)
				.append("minimizeShipments", this.minimizeShipments).append("orderComparator", this.orderComparator)
				.append("orderLineComparator", this.orderLineComparator).append("olqComparator", this.olqComparator)
				.append("orderSplit", this.orderSplit).append("orderLineSplit", this.orderLineSplit).toString();
	}

}
