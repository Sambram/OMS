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
package com.hybris.oms.service.inventory.impl;

import com.hybris.kernel.api.CollectionRestrictions;
import com.hybris.kernel.api.CriteriaExpression;
import com.hybris.kernel.api.CriteriaQuery;
import com.hybris.kernel.api.RawRestrictions;
import com.hybris.kernel.api.Restriction;
import com.hybris.kernel.api.Restrictions;
import com.hybris.oms.domain.SortDirection;
import com.hybris.oms.domain.inventory.BinQueryObject;
import com.hybris.oms.domain.inventory.BinQuerySupport;
import com.hybris.oms.domain.inventory.ItemLocationQuerySupport;
import com.hybris.oms.domain.inventory.ItemLocationsQueryObject;
import com.hybris.oms.domain.inventory.LocationQueryObject;
import com.hybris.oms.domain.inventory.LocationQuerySortSupport;
import com.hybris.oms.service.inventory.InventoryServiceConstants;
import com.hybris.oms.service.managedobjects.basestore.BaseStoreData;
import com.hybris.oms.service.managedobjects.i18n.CountryData;
import com.hybris.oms.service.managedobjects.inventory.BinData;
import com.hybris.oms.service.managedobjects.inventory.CurrentItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.FutureItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.ItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemStatusData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.service.AbstractQueryFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;


public class InventoryQueryFactory extends AbstractQueryFactory
{
	private static final Logger LOG = LoggerFactory.getLogger(InventoryQueryFactory.class);
	private static final String LOC = "loc";
	private static final String LOC2 = "loc.";
	private static final String ITEM_LOC = "itemloc";
	private static final String ITEM_LOC_DOT = "itemloc.";
	private static final String BIN = "bin";
	private static final String BIN_DOT = "bin.";
	private static final String OWNER = "owner.";
	private static final Map<String, String> ITEM_LOCATION_SUPPORT_MAPPING = new HashMap<>();
	private static final Map<String, String> LOCATION_QUERY_SUPPORT_MAPPING = new HashMap<>();
	private static final Map<String, String> BIN_SUPPORT_MAPPING = new HashMap<>();

	static
	{
		ITEM_LOCATION_SUPPORT_MAPPING.put(ItemLocationQuerySupport.DEFAULT.name(), "loc.description");
		ITEM_LOCATION_SUPPORT_MAPPING.put(ItemLocationQuerySupport.LOCATION_DESCRIPTION.name(), "loc.description");
		ITEM_LOCATION_SUPPORT_MAPPING.put(ItemLocationQuerySupport.LOCATION_STORENAME.name(), "loc.storeName");
		ITEM_LOCATION_SUPPORT_MAPPING.put(ItemLocationQuerySupport.SKU_ID.name(), "itemId");

		LOCATION_QUERY_SUPPORT_MAPPING.put(LocationQuerySortSupport.DEFAULT.name(), "locationId");
		LOCATION_QUERY_SUPPORT_MAPPING.put(LocationQuerySortSupport.LOCATION_ID.name(), "locationId");
		LOCATION_QUERY_SUPPORT_MAPPING.put(LocationQuerySortSupport.LOCATION_DESCRIPTION.name(), "description");
		LOCATION_QUERY_SUPPORT_MAPPING.put(LocationQuerySortSupport.LOCATION_STORENAME.name(), "storeName");
		LOCATION_QUERY_SUPPORT_MAPPING.put(LocationQuerySortSupport.LOCATION_PRIORITY.name(), "priority");

		BIN_SUPPORT_MAPPING.put(BinQuerySupport.DEFAULT.name(), "binCode");
		BIN_SUPPORT_MAPPING.put(BinQuerySupport.BIN_CODE.name(), "binCode");
		BIN_SUPPORT_MAPPING.put(BinQuerySupport.BIN_PRIORITY.name(), "priority");
		BIN_SUPPORT_MAPPING.put(BinQuerySupport.SKU_ID.name(), "itemId");
		BIN_SUPPORT_MAPPING.put(BinQuerySupport.BIN_DESCRIPTION.name(), "description");
		BIN_SUPPORT_MAPPING.put(BinQuerySupport.LOCATION_ID.name(), "loc.locationId");
	}

	private long locationsQueryTtl;

	/**
	 * Finds item locations at the stockroom location level.
	 * 
	 * @param queryObject
	 * @return
	 */
	public CriteriaQuery<ItemLocationData> findItemLocationByQuery(final ItemLocationsQueryObject queryObject)
	{
		final List<String> skuIds = queryObject.getSkuIds();
		final List<String> locationIds = queryObject.getLocationIds();

		CriteriaQuery<ItemLocationData> criteriaQuery = this.query(ItemLocationData.class);

		final List<Restriction> restrictions = new ArrayList<>();

		criteriaQuery = criteriaQuery.join(StockroomLocationData.class, LOC).on(ItemLocationData.STOCKROOMLOCATION)
				.join(BinData.class, BIN).on(ItemLocationData.BIN);

		// Filter to only use DEFAULT BIN
		restrictions.add(RawRestrictions.eq(BIN_DOT + BinData.BINCODE.name(), InventoryServiceConstants.DEFAULT_BIN));

		// Filtering on "locationId"
		if (CollectionUtils.isNotEmpty(locationIds))
		{
			restrictions.add(RawRestrictions.in("loc.locationId", locationIds.toArray()));
		}

		// Filtering on "itemId"
		if (CollectionUtils.isNotEmpty(skuIds))
		{
			restrictions.add(RawRestrictions.in(ItemLocationData.ITEMID.name(), skuIds.toArray()));
		}

		// Applying collected restrictions to the CriteriaQuery object.
		if (!restrictions.isEmpty())
		{
			criteriaQuery = criteriaQuery.where(restrictions.remove(0));

			for (final Restriction restriction : restrictions)
			{
				criteriaQuery = criteriaQuery.and(restriction);
			}
		}

		// Apply ordering
		criteriaQuery = criteriaQuery.order(ITEM_LOCATION_SUPPORT_MAPPING.get(queryObject.getSorting().getAttribute()),
				SortDirection.ASC.equals(queryObject.getSorting().getDirection()));

		return criteriaQuery;
	}

	/**
	 * Find all item locations for a given bin code.
	 * 
	 * @param locationId
	 * @param binCode
	 * @return query
	 */
	public CriteriaQuery<ItemLocationData> findItemLocationByLocationIdBinCode(final String locationId, final String binCode)
	{
		return this.query(ItemLocationData.class).join(BinData.class, BIN).on(ItemLocationData.BIN)
				.join(StockroomLocationData.class, LOC).on(ItemLocationData.STOCKROOMLOCATION)
				.where(RawRestrictions.eq(BIN_DOT + BinData.BINCODE.name(), binCode))
				.and(RawRestrictions.eq(LOC2 + StockroomLocationData.LOCATIONID.name(), locationId));
	}

	public CriteriaQuery<ItemLocationData> findItemLocationCurrentsBySkusAndLocationsQuery(final List<String> locationIds,
			final String[] skus)
	{
		return this.query(ItemLocationData.class).join(StockroomLocationData.class, LOC).on(ItemLocationData.STOCKROOMLOCATION)
				.where(Restrictions.in(ItemLocationData.ITEMID, skus))
				.and(RawRestrictions.in("loc.locationId", locationIds.toArray()));
	}

	/**
	 * Builds a {@link CriteriaQuery} for finding StockroomLocationData.
	 * 
	 * @param queryObject
	 * @param searchByWildcardAllowed
	 *           allow for searching by wildcard
	 * @return CriteriaQuery for query execution or <strong>null</strong>. Null result means that query based on given
	 *         queryObject will always return empty result set, hence there's no need to construct (and execute)
	 *         CriteriaQuery.
	 */
	public CriteriaQuery<StockroomLocationData> findLocationsByQueryObject(final LocationQueryObject queryObject,
			final boolean searchByWildcardAllowed)
	{
		final String locationName = queryObject.getLocationName();
		final String priority = queryObject.getPriority();

		final List<String> locationIds = queryObject.getLocationIds();
		final List<String> baseStores = queryObject.getBaseStores();
		final List<String> countryCodes = queryObject.getCountries();
		final List<String> locationRoles = queryObject.getLocationRoles();

		CriteriaQuery<StockroomLocationData> criteriaQuery = this.query(StockroomLocationData.class);

		final List<CriteriaExpression<?, ?>> restrictions = new ArrayList<>();

		// Filtering on "locationId"
		filterLocationIds(searchByWildcardAllowed, locationIds, restrictions);

		// Filtering on "locationName"
		if (StringUtils.isNotEmpty(locationName))
		{
			restrictions.add(Restrictions.like(StockroomLocationData.STORENAME, '%' + locationName + '%'));
		}

		// Filtering on "priority"
		if (StringUtils.isNotEmpty(priority))
		{
			restrictions.add(Restrictions.eq(StockroomLocationData.PRIORITY, Integer.valueOf(priority)));
		}

		if (CollectionUtils.isNotEmpty(baseStores))
		{
			criteriaQuery.join(BaseStoreData.class, "bs").on(StockroomLocationData.BASESTORES);
			restrictions.add(RawRestrictions.in("bs.name", baseStores.toArray())); //
		}

		if (CollectionUtils.isNotEmpty(locationRoles))
		{
			restrictions.add(CollectionRestrictions.containsAll(StockroomLocationData.LOCATIONROLES,
					locationRoles.toArray(new String[locationRoles.size()])));
		}

		if (filterCountryCodes(countryCodes, restrictions))
		{
			criteriaQuery = null;
		}
		else
		{
			applyRestrictions(criteriaQuery, restrictions);
			criteriaQuery.order(LOCATION_QUERY_SUPPORT_MAPPING.get(queryObject.getSorting().getAttribute()),
					SortDirection.ASC.equals(queryObject.getSorting().getDirection()));
		}

		// by default only cache internal queries without wildcard on locationId and storeName
		if (!searchByWildcardAllowed)
		{
			applyLocationsQueryTtl(criteriaQuery);
		}
		return criteriaQuery;
	}

	/**
	 * Sets the time to live for a cached locations query. By default, only internal location queries without wildcard
	 * are cached.
	 * 
	 * @param criteriaQuery
	 *           current {@link CriteriaQuery}, can be <tt>null</tt> if no countries match the given search criteria.
	 */
	protected void applyLocationsQueryTtl(final CriteriaQuery<StockroomLocationData> criteriaQuery)
	{
		if (locationsQueryTtl > 0 && criteriaQuery != null)
		{
			criteriaQuery.setTTL(locationsQueryTtl);
		}
	}

	protected boolean filterCountryCodes(final List<String> countryCodes, final List<CriteriaExpression<?, ?>> restrictions)
	{
		boolean emptyResult = false;
		if (CollectionUtils.isNotEmpty(countryCodes))
		{
			final List<CountryData> countryList = this.query(CountryData.class)
					.where(Restrictions.in(CountryData.CODE, countryCodes.toArray(new String[countryCodes.size()]))).resultList();
			if (countryList.size() != countryCodes.size())
			{
				LOG.warn("Unknown country code provided: " + countryCodes);
			}
			if (countryList.isEmpty())
			{
				emptyResult = true;
			}
			else
			{
				restrictions.add(CollectionRestrictions.containsAny(StockroomLocationData.SHIPTOCOUNTRIES,
						countryList.toArray(new CountryData[countryList.size()])));
			}
		}
		return emptyResult;
	}


	protected void filterLocationIds(final boolean searchByWildcardAllowed, final List<String> locationIds,
			final List<CriteriaExpression<?, ?>> restrictions)
	{
		if (locationIds.size() == 1)
		{
			final String locationId = locationIds.get(0);
			if (searchByWildcardAllowed)
			{
				restrictions.add(Restrictions.like(StockroomLocationData.LOCATIONID, '%' + locationId + '%'));
			}
			else
			{
				restrictions.add(Restrictions.eq(StockroomLocationData.LOCATIONID, locationId));
			}
		}
		// or Filtering on "locationIds"
		else if (locationIds.size() > 1)
		{
			restrictions.add(Restrictions.in(StockroomLocationData.LOCATIONID, locationIds.toArray(new String[locationIds.size()])));
		}
	}

	protected CriteriaQuery<StockroomLocationData> applyRestrictions(final CriteriaQuery<StockroomLocationData> criteriaQuery,
			final List<CriteriaExpression<?, ?>> restrictions)
	{
		boolean firstRestriction = true;
		for (final CriteriaExpression<?, ?> restriction : restrictions)
		{
			if (firstRestriction)
			{
				criteriaQuery.where(restriction);
				firstRestriction = false;
			}
			else
			{
				criteriaQuery.and(restriction);
			}
		}
		return criteriaQuery;
	}

	/**
	 * Gets the all expected delivery dates query.
	 * 
	 * @return the all expected delivery dates query
	 */
	public CriteriaQuery<FutureItemQuantityData> getAllExpectedDeliveryDatesQuery()
	{
		return this.query(FutureItemQuantityData.class).where(
				Restrictions.le(FutureItemQuantityData.EXPECTEDDELIVERYDATE, new Date()));
	}

	/**
	 * Get a query for a list of bins by sku id and location id.
	 * 
	 * @param skuId
	 *           the sku id
	 * @param locationId
	 *           the location id
	 * @param future
	 *           a boolean that specifies current/future inventory
	 * @param orderBy
	 *           the field name to order by
	 * @param direction
	 *           the direction of ordering
	 * @return query
	 */
	public CriteriaQuery<BinData> getAllBinsBySkuIdAndLocationIdQuery(final String skuId, final String locationId,
			final boolean future, final BinQuerySupport orderBy, final SortDirection direction)
	{
		CriteriaQuery<BinData> criteriaQuery = this
				.query(BinData.class)
				.join(ItemLocationData.class, ITEM_LOC)
				.join(StockroomLocationData.class, LOC)
				.where(RawRestrictions.attrEq(BinData.ID.name(), ITEM_LOC_DOT + ItemLocationData.BIN.name()))
				.and(RawRestrictions.attrEq(ITEM_LOC_DOT + ItemLocationData.STOCKROOMLOCATION.name(),
						LOC2 + StockroomLocationData.ID.name()))
				.and(RawRestrictions.eq(ITEM_LOC_DOT + ItemLocationData.ITEMID.name(), skuId))
				.and(RawRestrictions.eq(LOC2 + StockroomLocationData.LOCATIONID.name(), locationId))
				.and(RawRestrictions.eq(ITEM_LOC_DOT + ItemLocationData.FUTURE.name(), Boolean.valueOf(future)))
				.and(RawRestrictions.not(RawRestrictions.eq(BinData.BINCODE.name(), InventoryServiceConstants.DEFAULT_BIN)));
		criteriaQuery = criteriaQuery.order(BIN_SUPPORT_MAPPING.get(orderBy.name()), SortDirection.ASC.equals(direction));
		return criteriaQuery;
	}

	/**
	 * Get a query for an item locations by sku id and location id.
	 * 
	 * @param skuId
	 *           the sku id
	 * @param locationId
	 *           the location id
	 * @return query
	 */
	public CriteriaQuery<ItemLocationData> getAllItemLocationBySkuIdAndLocationIdQuery(final String skuId,
			final String locationId, final boolean future)
	{
		return this.query(ItemLocationData.class).join(StockroomLocationData.class, LOC).on(ItemLocationData.STOCKROOMLOCATION)
				.join(BinData.class, BIN).on(ItemLocationData.BIN).where(Restrictions.eq(ItemLocationData.ITEMID, skuId))
				.and(RawRestrictions.eq(LOC2 + StockroomLocationData.LOCATIONID.name(), locationId))
				.and(RawRestrictions.eq(BIN_DOT + BinData.BINCODE.name(), InventoryServiceConstants.DEFAULT_BIN))
				.and(Restrictions.eq(ItemLocationData.FUTURE, Boolean.valueOf(future)));
	}

	public CriteriaQuery<ItemLocationData> getAllItemLocationsBySkuAndLocationQuery(final String skuId,
			final String locationId)
	{
		return this.query(ItemLocationData.class).join(StockroomLocationData.class, LOC).on(ItemLocationData.STOCKROOMLOCATION)
				.where(Restrictions.eq(ItemLocationData.ITEMID, skuId))
				.and(RawRestrictions.eq(LOC2 + StockroomLocationData.LOCATIONID.name(), locationId));
	}

	/**
	 * Get a query for an item locations by sku id and location id.
	 * 
	 * @param skuId
	 *           the sku id
	 * @param locationId
	 *           the location id
	 * @return query
	 */
	public CriteriaQuery<ItemLocationData> getAllItemLocationBySkuIdAndLocationIdAndBinCodeQuery(final String skuId,
			final String locationId, final String binCode, final boolean future)
	{
		return this.query(ItemLocationData.class).join(StockroomLocationData.class, LOC).on(ItemLocationData.STOCKROOMLOCATION)
				.join(BinData.class, BIN).on(ItemLocationData.BIN).where(Restrictions.eq(ItemLocationData.ITEMID, skuId))
				.and(RawRestrictions.eq(LOC2 + StockroomLocationData.LOCATIONID.name(), locationId))
				.and(RawRestrictions.eq(BIN_DOT + BinData.BINCODE.name(), binCode))
				.and(Restrictions.eq(ItemLocationData.FUTURE, Boolean.valueOf(future)));
	}

	/**
	 * Get a query for a list of item location futures by date.
	 * 
	 * @param date
	 *           the date
	 * @return query
	 */
	public CriteriaQuery<ItemLocationData> getAllItemLocationFuturesByDateQuery(final Date date)
	{
		return this.query(ItemLocationData.class).join(FutureItemQuantityData.class, "q").on(ItemLocationData.ITEMQUANTITIES)
				.where(RawRestrictions.le("q.expectedDeliveryDate", date))
				.and(RawRestrictions.eq(ItemLocationData.FUTURE.name(), Boolean.TRUE));
	}

	/**
	 * Get a query for a list of item locations (current + future) by location id.
	 * 
	 * @param locationId
	 *           the location id
	 * @return query
	 */
	public CriteriaQuery<ItemLocationData> getAllItemLocationsByLocationIdQuery(final String locationId)
	{
		return this.query(ItemLocationData.class).join(StockroomLocationData.class, LOC).on(ItemLocationData.STOCKROOMLOCATION)
				.where(RawRestrictions.eq(LOC2 + StockroomLocationData.LOCATIONID.name(), locationId));
	}

	/**
	 * Get a query for a list of all current and future item locations together. (Only item location with default bin
	 * will be returned).
	 * 
	 * @return query
	 */
	public CriteriaQuery<ItemLocationData> getAllItemLocationsQuery()
	{
		return this.query(ItemLocationData.class).join(BinData.class, BIN).on(ItemLocationData.BIN)
				.where(RawRestrictions.eq(BIN_DOT + BinData.BINCODE.name(), InventoryServiceConstants.DEFAULT_BIN));
	}

	/**
	 * Get a query for a list of all possible item statuses.
	 * 
	 * @return query
	 */
	public CriteriaQuery<ItemStatusData> getAllItemStatusesQuery()
	{
		return this.query(ItemStatusData.class);
	}

	/**
	 * @param locationIds
	 *           {@link Set} of location IDs, must not be empty.
	 * @return {@link CriteriaQuery}
	 * @throws IllegalArgumentException
	 *            if locationIds is <tt>null</tt> or empty
	 */
	public CriteriaQuery<StockroomLocationData> findLocationsByLocationIds(final Set<String> locationIds)
			throws IllegalArgumentException
	{
		Preconditions.checkArgument(CollectionUtils.isNotEmpty(locationIds), "locationIds cannot be empty");
		final CriteriaQuery<StockroomLocationData> query = query(StockroomLocationData.class).where(
				Restrictions.in(StockroomLocationData.LOCATIONID, locationIds.toArray(new String[locationIds.size()])));
		applyLocationsQueryTtl(query);
		return query;
	}

	public CriteriaQuery<? extends ItemQuantityData> getItemQuantityBySkuLocationStatusQuery(final String skuId,
			final String locationId, final String statusCode, final Date expectedDeliveryDate)
	{
		return expectedDeliveryDate != null ? getFutureItemQuantityBySkuLocationStatusDateQuery(skuId, locationId, statusCode,
				expectedDeliveryDate) : getCurrentItemQuantityBySkuLocationStatusQuery(skuId, locationId, statusCode);
	}

	public CriteriaQuery<? extends ItemQuantityData> getItemQuantityBySkuLocationBinCodeStatusQuery(final String skuId,
			final String locationId, final String binCode, final String statusCode, final Date expectedDeliveryDate)
	{
		return expectedDeliveryDate != null ? getFutureItemQuantityBySkuLocationBinCodeStatusDateQuery(skuId, locationId, binCode,
				statusCode, expectedDeliveryDate) : getCurrentItemQuantityBySkuLocationBinCodeStatusQuery(skuId, locationId, binCode,
				statusCode);
	}

	/**
	 * Query to get {@link CurrentItemQuantityData} by sku - location - status. Only item locations with bin code equal
	 * to {@link InventoryServiceConstants#DEFAULT_BIN} will be part of the query.
	 * 
	 * @param skuId
	 * @param locationId
	 * @param statusCode
	 * @return {@link CriteriaQuery}
	 */
	public CriteriaQuery<CurrentItemQuantityData> getCurrentItemQuantityBySkuLocationStatusQuery(final String skuId,
			final String locationId, final String statusCode)
	{
		return this
				.query(CurrentItemQuantityData.class)
				.join(ItemLocationData.class, "owner")
				.on(CurrentItemQuantityData.OWNER)
				.join(StockroomLocationData.class, LOC)
				.join(BinData.class, BIN)
				.where(Restrictions.eq(CurrentItemQuantityData.STATUSCODE, statusCode))
				.and(RawRestrictions.eq(OWNER + ItemLocationData.ITEMID.name(), skuId))
				.and(RawRestrictions.eq(LOC2 + StockroomLocationData.LOCATIONID.name(), locationId))
				.and(RawRestrictions.attrEq(LOC2 + StockroomLocationData.ID.name(), OWNER + ItemLocationData.STOCKROOMLOCATION.name()))
				.and(RawRestrictions.eq(BIN_DOT + BinData.BINCODE.name(), InventoryServiceConstants.DEFAULT_BIN))
				.and(RawRestrictions.attrEq(BIN_DOT + BinData.ID.name(), OWNER + ItemLocationData.BIN.name()))
				.and(RawRestrictions.eq(OWNER + ItemLocationData.FUTURE.name(), Boolean.FALSE));
	}

	/**
	 * Query to get {@link FutureItemQuantityData} by sku - location - status. Only item locations with bin code equal to
	 * {@link InventoryServiceConstants#DEFAULT_BIN} will be part of the query.
	 * 
	 * @param skuId
	 * @param locationId
	 * @param statusCode
	 * @return {@link CriteriaQuery}
	 */
	public CriteriaQuery<FutureItemQuantityData> getFutureItemQuantityBySkuLocationStatusDateQuery(final String skuId,
			final String locationId, final String statusCode, final Date expectedDate)
	{
		return this
				.query(FutureItemQuantityData.class)
				.join(ItemLocationData.class, "owner")
				.on(FutureItemQuantityData.OWNER)
				.join(StockroomLocationData.class, LOC)
				.join(BinData.class, BIN)
				.where(Restrictions.eq(FutureItemQuantityData.STATUSCODE, statusCode))
				.and(Restrictions.eq(FutureItemQuantityData.EXPECTEDDELIVERYDATE, DateUtils.truncate(expectedDate, Calendar.DATE)))
				.and(RawRestrictions.eq(OWNER + ItemLocationData.ITEMID.name(), skuId))
				.and(RawRestrictions.eq(LOC2 + StockroomLocationData.LOCATIONID.name(), locationId))
				.and(RawRestrictions.attrEq(LOC2 + StockroomLocationData.ID.name(), OWNER + ItemLocationData.STOCKROOMLOCATION.name()))
				.and(RawRestrictions.eq(BIN_DOT + BinData.BINCODE.name(), InventoryServiceConstants.DEFAULT_BIN))
				.and(RawRestrictions.attrEq(BIN_DOT + BinData.ID.name(), OWNER + ItemLocationData.BIN.name()))
				.and(RawRestrictions.eq(OWNER + ItemLocationData.FUTURE.name(), Boolean.TRUE));
	}

	/**
	 * Query to get {@link CurrentItemQuantityData} by sku - location - status. Only item locations with bin code equal
	 * to provided bin code will be part of the query.
	 * 
	 * @param skuId
	 * @param locationId
	 * @param statusCode
	 * @return {@link CriteriaQuery}
	 */
	public CriteriaQuery<CurrentItemQuantityData> getCurrentItemQuantityBySkuLocationBinCodeStatusQuery(final String skuId,
			final String locationId, final String binCode, final String statusCode)
	{
		return this
				.query(CurrentItemQuantityData.class)
				.join(ItemLocationData.class, "owner")
				.on(CurrentItemQuantityData.OWNER)
				.join(StockroomLocationData.class, LOC)
				.join(BinData.class, BIN)
				.where(Restrictions.eq(CurrentItemQuantityData.STATUSCODE, statusCode))
				.and(RawRestrictions.eq(OWNER + ItemLocationData.ITEMID.name(), skuId))
				.and(RawRestrictions.eq(LOC2 + StockroomLocationData.LOCATIONID.name(), locationId))
				.and(RawRestrictions.attrEq(LOC2 + StockroomLocationData.ID.name(), OWNER + ItemLocationData.STOCKROOMLOCATION.name()))
				.and(RawRestrictions.eq(BIN_DOT + BinData.BINCODE.name(), binCode))
				.and(RawRestrictions.attrEq(BIN_DOT + BinData.ID.name(), OWNER + ItemLocationData.BIN.name()))
				.and(RawRestrictions.eq(OWNER + ItemLocationData.FUTURE.name(), Boolean.FALSE));
	}

	/**
	 * Query to get {@link FutureItemQuantityData} by sku - location - status. Only item locations with bin code equal to
	 * provided bin code will be part of the query.
	 * 
	 * @param skuId
	 * @param locationId
	 * @param statusCode
	 * @return {@link CriteriaQuery}
	 */
	public CriteriaQuery<FutureItemQuantityData> getFutureItemQuantityBySkuLocationBinCodeStatusDateQuery(final String skuId,
			final String locationId, final String binCode, final String statusCode, final Date expectedDate)
	{
		return this
				.query(FutureItemQuantityData.class)
				.join(ItemLocationData.class, "owner")
				.on(FutureItemQuantityData.OWNER)
				.join(StockroomLocationData.class, LOC)
				.join(BinData.class, BIN)
				.where(Restrictions.eq(FutureItemQuantityData.STATUSCODE, statusCode))
				.and(Restrictions.eq(FutureItemQuantityData.EXPECTEDDELIVERYDATE, DateUtils.truncate(expectedDate, Calendar.DATE)))
				.and(RawRestrictions.eq(OWNER + ItemLocationData.ITEMID.name(), skuId))
				.and(RawRestrictions.eq(LOC2 + StockroomLocationData.LOCATIONID.name(), locationId))
				.and(RawRestrictions.attrEq(LOC2 + StockroomLocationData.ID.name(), OWNER + ItemLocationData.STOCKROOMLOCATION.name()))
				.and(RawRestrictions.eq(BIN_DOT + BinData.BINCODE.name(), binCode))
				.and(RawRestrictions.attrEq(BIN_DOT + BinData.ID.name(), OWNER + ItemLocationData.BIN.name()))
				.and(RawRestrictions.eq(OWNER + ItemLocationData.FUTURE.name(), Boolean.TRUE));
	}

	/**
	 * Get a query for an item status by status code.
	 * 
	 * @param statusCode
	 *           the status code
	 * @return query
	 */
	public CriteriaQuery<ItemStatusData> getInventoryStatusByStatusCodeQuery(final String statusCode)
	{
		return this.query(ItemStatusData.class).where(Restrictions.eq(ItemStatusData.STATUSCODE, statusCode));
	}

	public CriteriaQuery<CurrentItemQuantityData> getItemQuantityByStatusCode(final String sku, final String statusCode)
	{
		return this.query(CurrentItemQuantityData.class).join(ItemLocationData.class, "il").on(ItemQuantityData.OWNER)
				.where(RawRestrictions.eq("il." + ItemLocationData.ITEMID.name(), sku))
				.and(Restrictions.eq(CurrentItemQuantityData.STATUSCODE, statusCode));
	}

	/**
	 * Gets a query for an bin by query object.
	 * 
	 * @param queryObject
	 *           with parameters
	 * @return query
	 */
	public CriteriaQuery<BinData> findBinsByQuery(final BinQueryObject queryObject)
	{

		final String locationId = queryObject.getLocationId();
		final String binCode = queryObject.getBinCode();

		CriteriaQuery<BinData> criteriaQuery = this.query(BinData.class);

		final List<Restriction> restrictions = new ArrayList<>();

		criteriaQuery = criteriaQuery.join(StockroomLocationData.class, LOC).on(BinData.STOCKROOMLOCATION);

		// Filtering out default bin code
		restrictions.add(Restrictions.not(Restrictions.eq(BinData.BINCODE, InventoryServiceConstants.DEFAULT_BIN)));

		// Filtering on "locationId"
		if (StringUtils.isNotEmpty(locationId))
		{
			restrictions.add(RawRestrictions.eq("loc.locationId", locationId));
		}

		// Filtering on "binCode"
		if (StringUtils.isNotEmpty(binCode))
		{
			restrictions.add(Restrictions.eq(BinData.BINCODE, binCode));
		}

		// Applying collected restrictions to the CriteriaQuery object.
		if (!restrictions.isEmpty())
		{
			criteriaQuery = criteriaQuery.where(restrictions.remove(0));

			for (final Restriction restriction : restrictions)
			{
				criteriaQuery = criteriaQuery.and(restriction);
			}
		}

		// Apply ordering
		criteriaQuery = criteriaQuery.order(BIN_SUPPORT_MAPPING.get(queryObject.getSorting().getAttribute()),
				SortDirection.ASC.equals(queryObject.getSorting().getDirection()));

		return criteriaQuery;
	}

	protected long getLocationsQueryTtl()
	{
		return locationsQueryTtl;
	}

	public void setLocationsQueryTtl(final long locationsQueryTtl)
	{
		Preconditions.checkArgument(locationsQueryTtl >= 0, "locationQueryTtl has to be >= 0");
		this.locationsQueryTtl = locationsQueryTtl;
	}

}
