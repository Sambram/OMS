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
package com.hybris.oms.domain.order.jaxb;

import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.inventory.SkuQuantity;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * Parameter object used for sourcing simulation.
 */
@XmlRootElement(name = "sourceSimulationParameter")
public class SourceSimulationParameter implements Serializable
{
	private static final long serialVersionUID = 6244294935537979064L;

	private List<SkuQuantity> skuQuantities;

	private Address address;

	private String atsId;

	private List<String> locationIds;

	public SourceSimulationParameter()
	{
		// defaut constructor
	}

	public SourceSimulationParameter(final List<SkuQuantity> skuQuantities, final String atsId, final Address address,
			final List<String> locationIds)
	{
		this.skuQuantities = skuQuantities;
		this.address = address;
		this.atsId = atsId;
		this.locationIds = locationIds;
	}

	public Address getAddress()
	{
		return this.address;
	}

	public String getAtsId()
	{
		return this.atsId;
	}

	public List<String> getLocationIds()
	{
		return this.locationIds;
	}

	/**
	 * Gets the skuQuantities.
	 * 
	 * @return the skuQuantities
	 */
	public List<SkuQuantity> getSkuQuantities()
	{
		return this.skuQuantities;
	}

	public void setAddress(final Address address)
	{
		this.address = address;
	}

	public void setAtsId(final String atsId)
	{
		this.atsId = atsId;
	}

	public void setLocationIds(final List<String> locationIds)
	{
		this.locationIds = locationIds;
	}

	public void setSkuQuantities(final List<SkuQuantity> skuQuantities)
	{
		this.skuQuantities = skuQuantities;
	}

}
