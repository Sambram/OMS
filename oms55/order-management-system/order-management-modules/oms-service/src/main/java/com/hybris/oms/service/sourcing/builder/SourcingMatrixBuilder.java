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
package com.hybris.oms.service.sourcing.builder;

import com.hybris.oms.service.ats.AtsResult;
import com.hybris.oms.service.ats.AtsService;
import com.hybris.oms.service.sourcing.RadianCoordinates;
import com.hybris.oms.service.sourcing.SourcingLine;
import com.hybris.oms.service.sourcing.context.LocationInfo;
import com.hybris.oms.service.sourcing.context.SourcingLocation;
import com.hybris.oms.service.sourcing.context.SourcingMatrix;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;


/**
 * Builder to create a {@link SourcingMatrix} by retrieving ATS from {@link AtsService}. Instantiates
 * {@link LocationInfo} from existing coordinates and
 * {@link com.hybris.oms.service.managedobjects.inventory.StockroomLocationData}.
 */
public class SourcingMatrixBuilder
{
	private static final Logger LOG = LoggerFactory.getLogger(SourcingMatrixBuilder.class);

	private SourcingMatrixRowBuilder matrixRowBuilder;

	private SourcingMatrixTotalsRowBuilder matrixTotalsRowBuilder;

	/**
	 * Creates a new {@link SourcingMatrix} from the given parameters. Retrieves the full location data from the
	 * database.
	 * 
	 * @param sourcingLines the sourcing lines; can be empty or <tt>null</tt>.
	 * @param atsId the atsId to use for retrieving ATS. Cannot be blank.
	 * @param filterLocationIds an optional {@link Set} of locationIds which should be used exclusively for sourcing
	 * @param shipToCoordinates optional ship to coordinates
	 * @return {@link SourcingMatrix}
	 * @throws IllegalArgumentException if the atsId is blank
	 */
	public SourcingMatrix createSourcingMatrix(final List<SourcingLine> sourcingLines, final String atsId,
			final Set<String> filterLocationIds, final RadianCoordinates shipToCoordinates)
	{
		Preconditions.checkArgument(StringUtils.isNotBlank(atsId), "atsId cannot be blank");
		final Map<String, List<SourcingLocation>> matrix;
		AtsResult atsResult = null;
		List<SourcingLocation> totalsRow = null;
		if (CollectionUtils.isEmpty(sourcingLines))
		{
			LOG.debug("Sourcing lines were empty");
			matrix = Collections.emptyMap();
		}
		else
		{
			atsResult = new AtsResult();
			matrix = new HashMap<>();
			LOG.debug("Creating Source Matrix for {} sourcing lines with ATS {}", sourcingLines.size(), atsId);
			final Map<String, LocationInfo> locationInfos = new HashMap<>();
			for (final SourcingLine line : sourcingLines)
			{
				final List<SourcingLocation> row = matrixRowBuilder.getMatrixRow(line, atsId, filterLocationIds, shipToCoordinates,
						atsResult, locationInfos);
				if (row != null)
				{
					matrix.put(line.getLineId(), row);
				}
			}
			totalsRow = matrixTotalsRowBuilder.calculateTotals(sourcingLines, matrix, atsResult, locationInfos);
		}
		return new SourcingMatrix(matrix, totalsRow, atsResult, atsId);
	}

	public void setMatrixRowBuilder(final SourcingMatrixRowBuilder matrixRowBuilder)
	{
		this.matrixRowBuilder = matrixRowBuilder;
	}

	public void setMatrixTotalsRowBuilder(final SourcingMatrixTotalsRowBuilder matrixTotalsRowBuilder)
	{
		this.matrixTotalsRowBuilder = matrixTotalsRowBuilder;
	}

}
