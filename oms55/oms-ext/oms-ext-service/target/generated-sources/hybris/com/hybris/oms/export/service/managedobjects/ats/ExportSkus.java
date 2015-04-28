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
 
package com.hybris.oms.export.service.managedobjects.ats;

import com.hybris.kernel.api.*;

import com.hybris.kernel.api.ManagedObject;

  
/**
 * Generated managedobject class for type ExportSkus first defined at extension <code>ats-export</code>.
 * <p/>
 * Contains list of skus marked for export..
 */
public interface ExportSkus extends ManagedObject
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "ExportSkus";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExportSkus.locationId</code> attribute defined at extension <code>ats-export</code>. */
	AttributeType<ExportSkus, String> LOCATIONID = new AttributeType<>("locationId");
	/** <i>Generated constant</i> - Attribute key of <code>ExportSkus.sku</code> attribute defined at extension <code>ats-export</code>. */
	AttributeType<ExportSkus, String> SKU = new AttributeType<>("sku");
	/** <i>Generated constant</i> - Attribute key of <code>ExportSkus.latestChange</code> attribute defined at extension <code>ats-export</code>. */
	AttributeType<ExportSkus, Long> LATESTCHANGE = new AttributeType<>("latestChange");

	/** <i>Generated constant</i> - Index of <code>ExportSkus</code> type defined at extension <code>ats-export</code>. */
	UniqueIndexDiadic<ExportSkus, String, String> UQ_SKU_LOCATIONID = new UniqueIndexDiadic<>("UQ_sku_locationId", ExportSkus.class);

	/**
	 * <i>Generated method</i> - Getter of the <code>ExportSkus.sku</code> attribute defined at extension <code>ats-export</code>.
	 * <p/>
	 * The sku marked for export..
	 * 
	 * @return the sku
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getSku();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExportSkus.locationId</code> attribute defined at extension <code>ats-export</code>.
	 * <p/>
	 * The location id marked for export.
	 * 
	 * @return the locationId
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getLocationId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExportSkus.latestChange</code> attribute defined at extension <code>ats-export</code>.
	 * <p/>
	 * Timestamp (in long) of latest change.
	 * 
	 * @return the latestChange
	 */
	@javax.validation.constraints.NotNull
	long getLatestChange();
	

	/**
	 * <i>Generated method</i> - Setter of <code>ExportSkus.sku</code> attribute defined at extension <code>ats-export</code>.  
	 * <p/>
	 * The sku marked for export..
	 *
	 * @param value the sku
	 */
	void setSku(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ExportSkus.locationId</code> attribute defined at extension <code>ats-export</code>.  
	 * <p/>
	 * The location id marked for export.
	 *
	 * @param value the locationId
	 */
	void setLocationId(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ExportSkus.latestChange</code> attribute defined at extension <code>ats-export</code>.  
	 * <p/>
	 * Timestamp (in long) of latest change.
	 *
	 * @param value the latestChange
	 */
	void setLatestChange(final long value);
	
}
