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
 
package com.hybris.oms.service.managedobjects.inventory;

import com.hybris.kernel.api.*;

import com.hybris.kernel.api.ManagedObject;

    
/**
 * Generated managedobject class for type StockroomLocationData first defined at extension <code>inventory</code>.
 * <p/>
 * Contains data for locations.
 */
public interface StockroomLocationData extends ManagedObject, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "StockroomLocationData";
	
	/** <i>Generated constant</i> - Attribute key of <code>StockroomLocationData.address</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<StockroomLocationData, com.hybris.oms.service.managedobjects.types.AddressVT> ADDRESS = new AttributeType<>("address");
	AttributeType<StockroomLocationData, String> ADDRESS_ADDRESSLINE1 = new AttributeType<>("address#addressLine1");
	AttributeType<StockroomLocationData, String> ADDRESS_ADDRESSLINE2 = new AttributeType<>("address#addressLine2");
	AttributeType<StockroomLocationData, String> ADDRESS_CITYNAME = new AttributeType<>("address#cityName");
	AttributeType<StockroomLocationData, String> ADDRESS_COUNTRYSUBENTITY = new AttributeType<>("address#countrySubentity");
	AttributeType<StockroomLocationData, String> ADDRESS_POSTALZONE = new AttributeType<>("address#postalZone");
	AttributeType<StockroomLocationData, Double> ADDRESS_LATITUDEVALUE = new AttributeType<>("address#latitudeValue");
	AttributeType<StockroomLocationData, Double> ADDRESS_LONGITUDEVALUE = new AttributeType<>("address#longitudeValue");
	AttributeType<StockroomLocationData, String> ADDRESS_COUNTRYISO3166ALPHA2CODE = new AttributeType<>("address#countryIso3166Alpha2Code");
	AttributeType<StockroomLocationData, String> ADDRESS_COUNTRYNAME = new AttributeType<>("address#countryName");
	AttributeType<StockroomLocationData, String> ADDRESS_NAME = new AttributeType<>("address#name");
	AttributeType<StockroomLocationData, String> ADDRESS_PHONENUMBER = new AttributeType<>("address#phoneNumber");
	/** <i>Generated constant</i> - Attribute key of <code>StockroomLocationData.locationId</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<StockroomLocationData, String> LOCATIONID = new AttributeType<>("locationId");
	/** <i>Generated constant</i> - Attribute key of <code>StockroomLocationData.active</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<StockroomLocationData, Boolean> ACTIVE = new AttributeType<>("active");
	/** <i>Generated constant</i> - Attribute key of <code>StockroomLocationData.shipToCountries</code> attribute defined at extension <code>inventory</code>. */
	CollectionAttributeType<StockroomLocationData, com.hybris.oms.service.managedobjects.i18n.CountryData> SHIPTOCOUNTRIES = new CollectionAttributeType<>("shipToCountries");
	/** <i>Generated constant</i> - Attribute key of <code>StockroomLocationData.description</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<StockroomLocationData, String> DESCRIPTION = new AttributeType<>("description");
	/** <i>Generated constant</i> - Attribute key of <code>StockroomLocationData.usePercentageThreshold</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<StockroomLocationData, Boolean> USEPERCENTAGETHRESHOLD = new AttributeType<>("usePercentageThreshold");
	/** <i>Generated constant</i> - Attribute key of <code>StockroomLocationData.baseStores</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<StockroomLocationData, java.util.Set<com.hybris.oms.service.managedobjects.basestore.BaseStoreData>> BASESTORES = new AttributeType<>("baseStores");
	/** <i>Generated constant</i> - Attribute key of <code>StockroomLocationData.version</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<StockroomLocationData, Long> VERSION = new AttributeType<>("version");
	/** <i>Generated constant</i> - Attribute key of <code>StockroomLocationData.taxAreaId</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<StockroomLocationData, String> TAXAREAID = new AttributeType<>("taxAreaId");
	/** <i>Generated constant</i> - Attribute key of <code>StockroomLocationData.priority</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<StockroomLocationData, Integer> PRIORITY = new AttributeType<>("priority");
	/** <i>Generated constant</i> - Attribute key of <code>StockroomLocationData.percentageInventoryThreshold</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<StockroomLocationData, Integer> PERCENTAGEINVENTORYTHRESHOLD = new AttributeType<>("percentageInventoryThreshold");
	/** <i>Generated constant</i> - Attribute key of <code>StockroomLocationData.locationRoles</code> attribute defined at extension <code>inventory</code>. */
	CollectionAttributeType<StockroomLocationData, String> LOCATIONROLES = new CollectionAttributeType<>("locationRoles");
	/** <i>Generated constant</i> - Attribute key of <code>StockroomLocationData.absoluteInventoryThreshold</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<StockroomLocationData, Integer> ABSOLUTEINVENTORYTHRESHOLD = new AttributeType<>("absoluteInventoryThreshold");
	/** <i>Generated constant</i> - Attribute key of <code>StockroomLocationData.storeName</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<StockroomLocationData, String> STORENAME = new AttributeType<>("storeName");

	/** <i>Generated constant</i> - Index of <code>StockroomLocationData</code> type defined at extension <code>inventory</code>. */
	UniqueIndexSingle<StockroomLocationData, String> UX_STOCKROOMLOCATIONS_LOCATIONID = new UniqueIndexSingle<>("UX_stockroomlocations_locationId", StockroomLocationData.class);

	/**
	 * <i>Generated method</i> - Getter of the <code>StockroomLocationData.locationId</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * Location id.
	 * 
	 * @return the locationId
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getLocationId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockroomLocationData.description</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * Location description.
	 * 
	 * @return the description
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getDescription();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockroomLocationData.storeName</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * Store name.
	 * 
	 * @return the storeName
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getStoreName();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockroomLocationData.taxAreaId</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * Tax ID based on the area.
	 * 
	 * @return the taxAreaId
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getTaxAreaId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockroomLocationData.priority</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * Priority.
	 * 
	 * @return the priority
	 */
	int getPriority();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockroomLocationData.address</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * Location Address with location coordinate (lat/lng).
	 * 
	 * @return the address
	 */
	com.hybris.oms.service.managedobjects.types.AddressVT getAddress();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockroomLocationData.active</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * Active status.
	 * 
	 * @return the active
	 */
	boolean isActive();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockroomLocationData.absoluteInventoryThreshold</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * Provides the absolute threshold for local ATS.
	 * 
	 * @return the absoluteInventoryThreshold
	 */
	int getAbsoluteInventoryThreshold();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockroomLocationData.percentageInventoryThreshold</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * Provides the percentage threshold for local ATS..
	 * 
	 * @return the percentageInventoryThreshold
	 */
	@javax.validation.constraints.Min(0)
	@javax.validation.constraints.Max(100)
	int getPercentageInventoryThreshold();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockroomLocationData.usePercentageThreshold</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * Enables percentage threshold for local ATS.
	 * 
	 * @return the usePercentageThreshold
	 */
	boolean isUsePercentageThreshold();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockroomLocationData.locationRoles</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * Location Roles (shipping, pickup).
	 * 
	 * @return the locationRoles
	 */
	@javax.validation.constraints.NotNull
	java.util.Set<java.lang.String> getLocationRoles();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockroomLocationData.baseStores</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * base stores.
	 * 
	 * @return the baseStores
	 */
	java.util.Set<com.hybris.oms.service.managedobjects.basestore.BaseStoreData> getBaseStores();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockroomLocationData.shipToCountries</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * Set of countries that a stockroomlocation ships to.
	 * 
	 * @return the shipToCountries
	 */
	java.util.Set<com.hybris.oms.service.managedobjects.i18n.CountryData> getShipToCountries();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockroomLocationData.version</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * .
	 * 
	 * @return the version
	 */
	java.lang.Long getVersion();
	

	/**
	 * <i>Generated method</i> - Setter of <code>StockroomLocationData.locationId</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * Location id.
	 *
	 * @param value the locationId
	 */
	void setLocationId(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockroomLocationData.description</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * Location description.
	 *
	 * @param value the description
	 */
	void setDescription(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockroomLocationData.storeName</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * Store name.
	 *
	 * @param value the storeName
	 */
	void setStoreName(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockroomLocationData.taxAreaId</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * Tax ID based on the area.
	 *
	 * @param value the taxAreaId
	 */
	void setTaxAreaId(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockroomLocationData.priority</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * Priority.
	 *
	 * @param value the priority
	 */
	void setPriority(final int value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockroomLocationData.address</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * Location Address with location coordinate (lat/lng).
	 *
	 * @param value the address
	 */
	void setAddress(final com.hybris.oms.service.managedobjects.types.AddressVT value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockroomLocationData.active</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * Active status.
	 *
	 * @param value the active
	 */
	void setActive(final boolean value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockroomLocationData.absoluteInventoryThreshold</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * Provides the absolute threshold for local ATS.
	 *
	 * @param value the absoluteInventoryThreshold
	 */
	void setAbsoluteInventoryThreshold(final int value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockroomLocationData.percentageInventoryThreshold</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * Provides the percentage threshold for local ATS..
	 *
	 * @param value the percentageInventoryThreshold
	 */
	void setPercentageInventoryThreshold(final int value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockroomLocationData.usePercentageThreshold</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * Enables percentage threshold for local ATS.
	 *
	 * @param value the usePercentageThreshold
	 */
	void setUsePercentageThreshold(final boolean value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockroomLocationData.locationRoles</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * Location Roles (shipping, pickup).
	 *
	 * @param value the locationRoles
	 */
	void setLocationRoles(final java.util.Set<java.lang.String> value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockroomLocationData.baseStores</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * base stores.
	 *
	 * @param value the baseStores
	 */
	void setBaseStores(final java.util.Set<com.hybris.oms.service.managedobjects.basestore.BaseStoreData> value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockroomLocationData.shipToCountries</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * Set of countries that a stockroomlocation ships to.
	 *
	 * @param value the shipToCountries
	 */
	void setShipToCountries(final java.util.Set<com.hybris.oms.service.managedobjects.i18n.CountryData> value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockroomLocationData.version</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the version
	 */
	void setVersion(final java.lang.Long value);
	
}
