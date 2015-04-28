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
package com.hybris.oms.domain.inventory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The Class BinList.
 * 
 * @author JRenato
 */
@XmlRootElement(name = "bins")
@XmlAccessorType(XmlAccessType.FIELD)
public class BinList implements Serializable
{


	private static final long serialVersionUID = 6214834110243592475L;
	/**
	 * The bins.
	 */
	@XmlElement(name = "bin")
	private List<Bin> bins = new ArrayList<>();

	/**
	 * Adds the item location.
	 * 
	 * @param bin the item location
	 */
	public void addBin(final Bin bin)
	{
		if (this.bins == null)
		{
			this.bins = new ArrayList<>();
		}
		this.bins.add(bin);
	}

	/**
	 * Gets the number of bins.
	 * 
	 * @return the number of bins
	 */
	public int getNumberOfBins()
	{
		return this.bins.size();
	}

	/**
	 * Gets the bins.
	 * 
	 * @return the bins
	 */
	public List<Bin> getBins()
	{
		return Collections.unmodifiableList(this.bins);
	}

	/**
	 * Initialize bins.
	 * 
	 * @param newBins the bins
	 */
	public void initializeBins(final List<Bin> newBins)
	{
		assert this.bins.isEmpty();
		for (final Bin bin : newBins)
		{
			this.addBin(bin);
		}
	}

	/**
	 * Removes the bin.
	 * 
	 * @param bin the bin
	 */
	public void removeBin(final Bin bin)
	{
		this.bins.remove(bin);
	}
}
