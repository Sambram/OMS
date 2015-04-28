

/*
 * [y] hybris Core+ Platform
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

package com.hybris.oms.domain.inventory;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;


import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.locationrole.LocationRole;
import java.util.HashSet;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import com.hybris.commons.dto.impl.PropertyAwareEntityDto;
/**
* Location
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Location extends PropertyAwareEntityDto implements Serializable
{

	public final static long serialVersionUID = -435786354L;

	private String locationId;

	private String description;

	private int priority;

	private String storeName;

	private String taxAreaId;

	private Address address;

	private int absoluteInventoryThreshold;

	private int percentageInventoryThreshold;

	private boolean usePercentageThreshold;

	private boolean active = true;

	private Date creationTime;

	private Date modifiedTime;

	@XmlElement
	private Set<LocationRole> locationRoles = new HashSet();

	@XmlElement
	private Set<String> baseStores = new HashSet();

	@XmlElement
	private Set<String> shipToCountriesCodes = new HashSet();

	
	public Location(){}

	protected Location(Builder builder)
	{
		super(builder); 
		setLocationId(builder.getLocationId());
		setDescription(builder.getDescription());
		setPriority(builder.getPriority());
		setStoreName(builder.getStoreName());
		setTaxAreaId(builder.getTaxAreaId());
		setAddress(builder.getAddress());
		setAbsoluteInventoryThreshold(builder.getAbsoluteInventoryThreshold());
		setPercentageInventoryThreshold(builder.getPercentageInventoryThreshold());
		setUsePercentageThreshold(builder.getUsePercentageThreshold());
		setActive(builder.getActive());
		setCreationTime(builder.getCreationTime());
		setModifiedTime(builder.getModifiedTime());
		setLocationRoles(builder.getLocationRoles());
		setBaseStores(builder.getBaseStores());
		setShipToCountriesCodes(builder.getShipToCountriesCodes());
	
	}


	public Location(final String locationId)
	{
		if (locationId == null)
		{
			throw new java.lang.IllegalStateException("Location Id must not be null");
		}
		this.locationId = locationId;
	}
	
	@Override
	public String getId()
	{
		return locationId;
	}

    		

	/**
	* gets 
	*
	* @returns String
	*/
	public String getLocationId()
	{
		return locationId;
	}

	/**
	* sets 
	*
	*/
	public void setLocationId(String locationId)
	{
		this.locationId = locationId;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getDescription()
	{
		return description;
	}

	/**
	* sets 
	*
	*/
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	* gets 
	*
	* @returns int
	*/
	public int getPriority()
	{
		return priority;
	}

	/**
	* sets 
	*
	*/
	public void setPriority(int priority)
	{
		this.priority = priority;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getStoreName()
	{
		return storeName;
	}

	/**
	* sets 
	*
	*/
	public void setStoreName(String storeName)
	{
		this.storeName = storeName;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getTaxAreaId()
	{
		return taxAreaId;
	}

	/**
	* sets 
	*
	*/
	public void setTaxAreaId(String taxAreaId)
	{
		this.taxAreaId = taxAreaId;
	}

	/**
	* gets 
	*
	* @returns Address
	*/
	public Address getAddress()
	{
		return address;
	}

	/**
	* sets 
	*
	*/
	public void setAddress(Address address)
	{
		this.address = address;
	}

	/**
	* gets 
	*
	* @returns int
	*/
	public int getAbsoluteInventoryThreshold()
	{
		return absoluteInventoryThreshold;
	}

	/**
	* sets 
	*
	*/
	public void setAbsoluteInventoryThreshold(int absoluteInventoryThreshold)
	{
		this.absoluteInventoryThreshold = absoluteInventoryThreshold;
	}

	/**
	* gets 
	*
	* @returns int
	*/
	public int getPercentageInventoryThreshold()
	{
		return percentageInventoryThreshold;
	}

	/**
	* sets 
	*
	*/
	public void setPercentageInventoryThreshold(int percentageInventoryThreshold)
	{
		this.percentageInventoryThreshold = percentageInventoryThreshold;
	}

	/**
	* gets 
	*
	* @returns boolean
	*/
	public boolean getUsePercentageThreshold()
	{
		return usePercentageThreshold;
	}

	/**
	* sets 
	*
	*/
	public void setUsePercentageThreshold(boolean usePercentageThreshold)
	{
		this.usePercentageThreshold = usePercentageThreshold;
	}

	/**
	* gets 
	*
	* @returns boolean
	*/
	public boolean getActive()
	{
		return active;
	}

	/**
	* sets 
	*
	*/
	public void setActive(boolean active)
	{
		this.active = active;
	}

	/**
	* gets 
	*
	* @returns Date
	*/
	public Date getCreationTime()
	{
		return creationTime;
	}

	/**
	* sets 
	*
	*/
	public void setCreationTime(Date creationTime)
	{
		this.creationTime = creationTime;
	}

	/**
	* gets 
	*
	* @returns Date
	*/
	public Date getModifiedTime()
	{
		return modifiedTime;
	}

	/**
	* sets 
	*
	*/
	public void setModifiedTime(Date modifiedTime)
	{
		this.modifiedTime = modifiedTime;
	}

	/**
	* gets 
	*
	* @returns Set<LocationRole>
	*/
	public Set<LocationRole> getLocationRoles()
	{
		return locationRoles;
	}

	/**
	* sets 
	*
	*/
	public void setLocationRoles(Set<LocationRole> locationRoles)
	{
		this.locationRoles = locationRoles;
	}

	/**
	* gets 
	*
	* @returns Set<String>
	*/
	public Set<String> getBaseStores()
	{
		return baseStores;
	}

	/**
	* sets 
	*
	*/
	public void setBaseStores(Set<String> baseStores)
	{
		this.baseStores = baseStores;
	}

	/**
	* gets 
	*
	* @returns Set<String>
	*/
	public Set<String> getShipToCountriesCodes()
	{
		return shipToCountriesCodes;
	}

	/**
	* sets 
	*
	*/
	public void setShipToCountriesCodes(Set<String> shipToCountriesCodes)
	{
		this.shipToCountriesCodes = shipToCountriesCodes;
	}


	@Override
	public String toString()
	{
		return reflectionToString(this, SHORT_PREFIX_STYLE);
	}

	private static class Builder2 extends Builder<Builder2>
	{
		@Override
		protected Builder2 self()
		{
			return this;
		}
	}

	public static Builder<?> builder()
	{
		return new Builder2();
	}

	public abstract static class Builder<T extends Builder<T>> extends PropertyAwareEntityDto.Builder<T>
	{
		private String locationId;
		private String description;
		private int priority;
		private String storeName;
		private String taxAreaId;
		private Address address;
		private int absoluteInventoryThreshold;
		private int percentageInventoryThreshold;
		private boolean usePercentageThreshold;
		private boolean active;
		private Date creationTime;
		private Date modifiedTime;
		private Set<LocationRole> locationRoles;
		private Set<String> baseStores;
		private Set<String> shipToCountriesCodes;
		
	
		/**
		* sets 
		*
		*/
		public T setLocationId(String locationId)
		{
			this.locationId = locationId;
			return self();
		}

		private String getLocationId()
		{
			return locationId;
		}
	
		/**
		* sets 
		*
		*/
		public T setDescription(String description)
		{
			this.description = description;
			return self();
		}

		private String getDescription()
		{
			return description;
		}
	
		/**
		* sets 
		*
		*/
		public T setPriority(int priority)
		{
			this.priority = priority;
			return self();
		}

		private int getPriority()
		{
			return priority;
		}
	
		/**
		* sets 
		*
		*/
		public T setStoreName(String storeName)
		{
			this.storeName = storeName;
			return self();
		}

		private String getStoreName()
		{
			return storeName;
		}
	
		/**
		* sets 
		*
		*/
		public T setTaxAreaId(String taxAreaId)
		{
			this.taxAreaId = taxAreaId;
			return self();
		}

		private String getTaxAreaId()
		{
			return taxAreaId;
		}
	
		/**
		* sets 
		*
		*/
		public T setAddress(Address address)
		{
			this.address = address;
			return self();
		}

		private Address getAddress()
		{
			return address;
		}
	
		/**
		* sets 
		*
		*/
		public T setAbsoluteInventoryThreshold(int absoluteInventoryThreshold)
		{
			this.absoluteInventoryThreshold = absoluteInventoryThreshold;
			return self();
		}

		private int getAbsoluteInventoryThreshold()
		{
			return absoluteInventoryThreshold;
		}
	
		/**
		* sets 
		*
		*/
		public T setPercentageInventoryThreshold(int percentageInventoryThreshold)
		{
			this.percentageInventoryThreshold = percentageInventoryThreshold;
			return self();
		}

		private int getPercentageInventoryThreshold()
		{
			return percentageInventoryThreshold;
		}
	
		/**
		* sets 
		*
		*/
		public T setUsePercentageThreshold(boolean usePercentageThreshold)
		{
			this.usePercentageThreshold = usePercentageThreshold;
			return self();
		}

		private boolean getUsePercentageThreshold()
		{
			return usePercentageThreshold;
		}
	
		/**
		* sets 
		*
		*/
		public T setActive(boolean active)
		{
			this.active = active;
			return self();
		}

		private boolean getActive()
		{
			return active;
		}
	
		/**
		* sets 
		*
		*/
		public T setCreationTime(Date creationTime)
		{
			this.creationTime = creationTime;
			return self();
		}

		private Date getCreationTime()
		{
			return creationTime;
		}
	
		/**
		* sets 
		*
		*/
		public T setModifiedTime(Date modifiedTime)
		{
			this.modifiedTime = modifiedTime;
			return self();
		}

		private Date getModifiedTime()
		{
			return modifiedTime;
		}
	
		/**
		* sets 
		*
		*/
		public T setLocationRoles(Set<LocationRole> locationRoles)
		{
			this.locationRoles = locationRoles;
			return self();
		}

		private Set<LocationRole> getLocationRoles()
		{
			return locationRoles;
		}
	
		/**
		* sets 
		*
		*/
		public T setBaseStores(Set<String> baseStores)
		{
			this.baseStores = baseStores;
			return self();
		}

		private Set<String> getBaseStores()
		{
			return baseStores;
		}
	
		/**
		* sets 
		*
		*/
		public T setShipToCountriesCodes(Set<String> shipToCountriesCodes)
		{
			this.shipToCountriesCodes = shipToCountriesCodes;
			return self();
		}

		private Set<String> getShipToCountriesCodes()
		{
			return shipToCountriesCodes;
		}
	
		protected abstract T self();

		public Location build(){
			return new Location(this);
		}
	}
}

