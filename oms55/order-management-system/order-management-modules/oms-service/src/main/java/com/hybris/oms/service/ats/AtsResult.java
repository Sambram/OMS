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
package com.hybris.oms.service.ats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.concurrent.NotThreadSafe;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;


/**
 * Represents the result of a ATS calculation.
 */
@NotThreadSafe
public class AtsResult
{

	private final Map<Key, Integer> result = new HashMap<AtsResult.Key, Integer>();

	public void addAll(final AtsResult otherResult)
	{
		this.result.putAll(otherResult.result);
	}

	/**
	 * Adds a ATS calculation result.
	 */
	public void addResult(final String locationId, final String sku, final String atsId, final int quantity, final Long millisTime)
	{
		this.result.put(new Key(sku, atsId, locationId, millisTime), Integer.valueOf(quantity));
	}

	/**
	 * Returns a single global ATS calculation result for given parameters.
	 * If there is no calculated result available, 0 is returned.
	 *
	 * @UsedBy("DefaultAtsServiceIntegrationTest", "DefaultAtsServiceBenchmarkTest")
	 * @return calculated global ATS result, if available, otherwise <tt>0</tt>
	 */
	public Integer getResult(final String sku, final String atsId, final Long millisTime)
	{
		return this.getResult(sku, atsId, null, millisTime);
	}

	/**
	 * Returns a single ATS calculation result for given parameters.
	 * If there is no calculated result available, 0 is returned.
	 *
	 * @UsedBy("DefaultAtsServiceIntegrationTest", "DefaultAtsServiceBenchmarkTest")
	 * @return calculated ATS result, if available, otherwise <tt>0</tt>
	 */
	public Integer getResult(final String sku, final String atsId, final String locationId, final Long millisTime)
	{
		final Integer ats = this.result.get(new Key(sku, atsId, locationId, millisTime));
		return ats == null ? NumberUtils.INTEGER_ZERO : ats;
	}

	/**
	 * Returns all result as a list of {@link AtsRow}.
	 */
	public List<AtsRow> getResults()
	{
		final List<AtsRow> results = new ArrayList<>();
		for (final Entry<Key, Integer> entry : this.result.entrySet())
		{
			results.add(new AtsRow(entry.getKey(), entry.getValue()));
		}
		return results;
	}

	/**
	 * Returns <tt>true</tt> if the result is empty.
	 */
	public boolean isEmpty()
	{
		return result.isEmpty();
	}

	@Override
	public String toString()
	{
		return Objects.toStringHelper(this).addValue(result).toString();
	}

	/**
	 * Ats result row.
	 */
	public static class AtsRow
	{
		private final Key key;
		private final Integer value;

		public AtsRow(final Key key, final Integer value)
		{
			this.key = key;
			this.value = value;
		}

		public Key getKey()
		{
			return this.key;
		}

		public Integer getValue()
		{
			return this.value;
		}
	}

	/**
	 * Key to retrieve a ATS calculation value.
	 */
	public static class Key
	{
		private final String atsId;

		private final String locationId;

		private final String sku;

		private final Long millisTime;

		/**
		 * @param sku
		 * @param atsId
		 * @param locationId
		 * @param millisTime
		 *           <dt><b>Preconditions:</b>
		 *           <dd>sku must not be blank.
		 *           <dd>atsId must not be blank.
		 * @throws IllegalArgumentException if preconditions are not met.
		 */
		public Key(final String sku, final String atsId, final String locationId, final Long millisTime)
		{
			Preconditions.checkArgument(StringUtils.isNotBlank(sku));
			Preconditions.checkArgument(StringUtils.isNotBlank(atsId));
			this.sku = sku;
			this.atsId = atsId;
			this.locationId = locationId;
			this.millisTime = millisTime;
		}

		@Override
		public boolean equals(final Object obj)
		{
			if (obj == null)
			{
				return false;
			}
			if (this.getClass() != obj.getClass())
			{
				return false;
			}
			final Key other = (Key) obj;
			return Objects.equal(this.sku, other.sku) && Objects.equal(this.atsId, other.atsId)
					&& Objects.equal(this.locationId, other.locationId);

		}

		public String getAtsId()
		{
			return this.atsId;
		}

		public String getLocationId()
		{
			return this.locationId;
		}

		public String getSku()
		{
			return this.sku;
		}

		public Long getMillisTime()
		{
			return this.millisTime;
		}

		@Override
		public int hashCode()
		{
			return Objects.hashCode(this.sku, this.atsId, this.locationId);
		}

		@Override
		public String toString()
		{
			return Objects.toStringHelper(this).addValue(this.sku).addValue(this.atsId).addValue(this.locationId).toString();
		}

	}

}
