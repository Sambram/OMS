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

import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.service.managedobjects.preference.TenantPreferenceData;
import com.hybris.oms.service.preference.TenantPreferenceService;
import com.hybris.oms.service.sourcing.RadianCoordinates;
import com.hybris.oms.service.sourcing.SourcingLine;
import com.hybris.oms.service.sourcing.context.ProcessStatus;
import com.hybris.oms.service.sourcing.context.PropertiesBuilder;
import com.hybris.oms.service.sourcing.context.PropertiesBuilderSupport;
import com.hybris.oms.service.sourcing.context.SourcingConfiguration;
import com.hybris.oms.service.sourcing.context.SourcingContext;
import com.hybris.oms.service.sourcing.context.SourcingLocation;
import com.hybris.oms.service.sourcing.context.SourcingMatrix;
import com.hybris.oms.service.sourcing.context.SourcingSplitOption;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


/**
 * Builder to setup the {@link SourcingContext} from the tenant preferences.
 */
public abstract class SourcingContextBuilder
{
	private TenantPreferenceService preferenceService;
	private SourcingMatrixBuilder matrixBuilder;
	private SourcingComparatorFactory comparatorFactory;
	private Map<String, SourcingSplitOption> splitOptionMap;
	private List<PropertiesBuilder<Void>> propertiesBuilders;

	public SourcingContext createContext(final List<SourcingLine> sourcingLines, final Set<String> filterLocationIds,
			final RadianCoordinates shippingCoordinates)
	{
		final SourcingConfiguration configuration = this.createSourcingConfiguration();
		final SourcingMatrix sourcingMatrix = this.matrixBuilder.createSourcingMatrix(sourcingLines, configuration.getAtsId(),
				filterLocationIds, shippingCoordinates);
		final ProcessStatus status = getProcessStatus();
		return new SourcingContext(sourcingLines, sourcingMatrix, configuration, status);
	}

	/**
	 * Lookup method to instantiate a new {@link ProcessStatus}.
	 */
	public abstract ProcessStatus getProcessStatus();

	protected SourcingConfiguration createSourcingConfiguration()
	{
		final TenantPreferenceData pref = this.preferenceService
				.getTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_ATS_CALCULATOR);
		final String atsId = pref.getValue();
		final boolean minimizeShipments = this.getBooleanPreference(TenantPreferenceConstants.PREF_KEY_ITEM_GROUPING);
		final Comparator<SourcingLocation> orderComparator = this
				.getComparator(TenantPreferenceConstants.PREF_KEY_SOURCING_ORDER_LEVEL);
		final Comparator<SourcingLocation> orderLineComparator = this
				.getComparator(TenantPreferenceConstants.PREF_KEY_SOURCING_OLINE_LVL);
		final Comparator<SourcingLocation> olqComparator = this
				.getComparator(TenantPreferenceConstants.PREF_KEY_SOURCING_OLQ_LEVEL);
		final SourcingSplitOption orderSplit = this.getSplitOption(TenantPreferenceConstants.PREF_KEY_ORDER_SPLITSTRATEGY);
		final SourcingSplitOption orderLineSplit = this.getSplitOption(TenantPreferenceConstants.PREF_KEY_ORDERLINE_SPLITSTRATEGY);
		final Map<String, Object> properties = PropertiesBuilderSupport.buildProperties(null, propertiesBuilders);
		final SourcingConfiguration configuration = new SourcingConfiguration(atsId, minimizeShipments, orderSplit, orderLineSplit,
				Arrays.asList(orderComparator, orderLineComparator, olqComparator), properties);
		return configuration;
	}

	protected boolean getBooleanPreference(final String key)
	{
		final TenantPreferenceData pref = this.preferenceService.getTenantPreferenceByKey(key);
		final String value = pref.getValue();
		return value != null && value.equalsIgnoreCase(Boolean.TRUE.toString());
	}

	protected Comparator<SourcingLocation> getComparator(final String key)
	{
		final TenantPreferenceData pref = this.preferenceService.getTenantPreferenceByKey(key);
		return this.comparatorFactory.createComparator(pref.getValue());
	}

	protected SourcingSplitOption getSplitOption(final String key)
	{
		SourcingSplitOption result = null;
		final TenantPreferenceData pref = this.preferenceService.getTenantPreferenceByKey(key);
		if (pref.getValue() != null)
		{
			result = splitOptionMap.get(pref.getValue());
		}
		if (result == null)
		{
			throw new IllegalArgumentException("Unknown SourcingSplitOption for key=" + key + ", value=" + pref.getValue());
		}
		return result;
	}

	public void setPropertiesBuilders(final List<PropertiesBuilder<Void>> propertiesBuilders)
	{
		this.propertiesBuilders = propertiesBuilders;
	}

	@Required
	public void setSplitOptionMap(final Map<String, SourcingSplitOption> splitOptionMap)
	{
		this.splitOptionMap = splitOptionMap;
	}

	@Required
	public void setPreferenceService(final TenantPreferenceService preferenceService)
	{
		this.preferenceService = preferenceService;
	}

	@Required
	public void setComparatorFactory(final SourcingComparatorFactory comparatorFactory)
	{
		this.comparatorFactory = comparatorFactory;
	}

	@Required
	public void setMatrixBuilder(final SourcingMatrixBuilder matrixBuilder)
	{
		this.matrixBuilder = matrixBuilder;
	}

	protected TenantPreferenceService getPreferenceService()
	{
		return preferenceService;
	}

	protected SourcingComparatorFactory getComparatorFactory()
	{
		return comparatorFactory;
	}

	protected SourcingMatrixBuilder getMatrixBuilder()
	{
		return matrixBuilder;
	}

	protected Map<String, SourcingSplitOption> getSplitOptionMap()
	{
		return splitOptionMap;
	}

	protected List<PropertiesBuilder<Void>> getPropertiesBuilders()
	{
		return propertiesBuilders;
	}

}
