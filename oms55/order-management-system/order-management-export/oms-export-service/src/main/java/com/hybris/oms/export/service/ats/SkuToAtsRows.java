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
package com.hybris.oms.export.service.ats;

import com.hybris.oms.service.ats.AtsResult.AtsRow;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class storess a Sku and corresponding set of AtsRows.
 * 
 * Introducing this class was needed because IT IS allowed to export/poll
 * empty ATS values, which could not be store in the AtsResult class.
 */


public class SkuToAtsRows
{
	private final String sku;

	private final Collection<AtsRow> atsRows;

	public SkuToAtsRows(final String sku)
	{
		if (sku == null)
		{
			throw new IllegalArgumentException("Sku must not be null.");
		}
		this.sku = sku;
		this.atsRows = new ArrayList<AtsRow>();
	}

	public String getSku()
	{
		return this.sku;
	}

	public void addAtsRow(final AtsRow row)
	{
		this.atsRows.add(row);
	}

	public Collection<AtsRow> getAtsRows()
	{
		return this.atsRows;
	}

}
