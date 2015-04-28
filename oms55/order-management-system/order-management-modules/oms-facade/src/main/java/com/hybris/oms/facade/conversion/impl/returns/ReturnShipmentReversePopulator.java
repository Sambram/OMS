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
package com.hybris.oms.facade.conversion.impl.returns;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.impl.AbstractPopulator;
import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.domain.returns.ReturnShipment;
import com.hybris.oms.service.managedobjects.preference.TenantPreferenceData;
import com.hybris.oms.service.managedobjects.returns.ReturnShipmentData;
import com.hybris.oms.service.preference.TenantPreferenceService;

import org.springframework.beans.factory.annotation.Required;


public class ReturnShipmentReversePopulator extends AbstractPopulator<ReturnShipment, ReturnShipmentData>
{
	private TenantPreferenceService tenantPreferenceService;

	@Override
	public void populate(final ReturnShipment source, final ReturnShipmentData target) throws ConversionException
	{
		final TenantPreferenceData grossWeightUnitCodePref = this.tenantPreferenceService
				.getOptionalTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_SHIPMENT_PROPERTY_GROSS_WEIGHT_UNIT_CODE);
		final TenantPreferenceData grossWeightValuePref = this.tenantPreferenceService
				.getOptionalTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_SHIPMENT_PROPERTY_GROSS_WEIGHT_VALUE);
		final TenantPreferenceData heightUnitCodePref = this.tenantPreferenceService
				.getOptionalTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_SHIPMENT_PROPERTY_HEIGHT_UNIT_CODE);
		final TenantPreferenceData heightValuePref = this.tenantPreferenceService
				.getOptionalTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_SHIPMENT_PROPERTY_HEIGHT_VALUE);
		final TenantPreferenceData widthValuePref = this.tenantPreferenceService
				.getOptionalTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_SHIPMENT_PROPERTY_WIDTH_VALUE);
		final TenantPreferenceData lengthUnitCodePref = this.tenantPreferenceService
				.getOptionalTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_SHIPMENT_PROPERTY_LENGTH_UNIT_CODE);
		final TenantPreferenceData lengthValuePref = this.tenantPreferenceService
				.getOptionalTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_SHIPMENT_PROPERTY_LENGTH_VALUE);
		final TenantPreferenceData descriptionPref = this.tenantPreferenceService
				.getOptionalTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_SHIPMENT_PROPERTY_DESCRIPTION);
		final TenantPreferenceData insuranceValueAmountValuePref = this.tenantPreferenceService
				.getOptionalTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_SHIPMENT_PROPERTY_INSURANCE_VALUE_AMOUNT_VALUE);


		// if values are provided it takes presence over default values
		if (source.getGrossWeight() != null)
		{
			target.setGrossWeightUnitCode(source.getGrossWeight().getUnitCode());
			target.setGrossWeightValue(source.getGrossWeight().getValue());
		}
		else
		{
			target.setGrossWeightUnitCode((grossWeightUnitCodePref == null) ? "" : grossWeightUnitCodePref.getValue());
			target.setGrossWeightValue(Float.parseFloat((grossWeightValuePref == null) ? "0.0f" : grossWeightValuePref.getValue()));
		}

		if (source.getHeight() != null)
		{
			target.setHeightUnitCode(source.getHeight().getUnitCode());
			target.setHeightValue(source.getHeight().getValue());
		}
		else
		{
			target.setHeightUnitCode((heightUnitCodePref == null) ? "" : heightUnitCodePref.getValue());
			target.setHeightValue(Float.parseFloat((heightValuePref == null) ? "0.0f" : heightValuePref.getValue()));
		}

		if (source.getLabelUrl() != null)
		{
			target.setLabelUrl(source.getLabelUrl());
		}
		if (source.getLength() != null)
		{

			target.setLengthUnitCode(source.getLength().getUnitCode());
			target.setLengthValue(source.getLength().getValue());
		}
		else
		{
			target.setLengthUnitCode((lengthUnitCodePref == null) ? "" : lengthUnitCodePref.getValue());
			target.setLengthValue(Float.parseFloat((lengthValuePref == null) ? "0.0f" : lengthValuePref.getValue()));
		}
		if (source.getWidth() != null)
		{
			target.setWidthUnitCode(source.getWidth().getUnitCode());
			target.setWidthValue(source.getWidth().getValue());
		}
		else
		{
			target.setWidthUnitCode((lengthUnitCodePref == null) ? "" : lengthUnitCodePref.getValue());
			target.setWidthValue(Float.parseFloat((widthValuePref == null) ? "0.0f" : widthValuePref.getValue()));
		}

		if (source.getNote() != null)
		{
			target.setNote(source.getNote());
		}
		if (source.getPackageDescription() != null)
		{
			target.setPackageDescription(source.getPackageDescription());
		}
		else
		{
			target.setShippingMethod((descriptionPref == null) ? "" : descriptionPref.getValue());
		}

		if (source.getShippingMethod() != null)
		{
			target.setShippingMethod(source.getShippingMethod());
		}

		if (source.getTrackingId() != null)
		{
			target.setTrackingId(source.getTrackingId());
		}

		if (source.getTrackingUrl() != null)
		{
			target.setTrackingUrl(source.getTrackingUrl());
		}
		if (source.getInsuranceValueAmount() != null)
		{
			target.setInsuranceValueAmountCurrencyCode(source.getInsuranceValueAmount().getCurrencyCode());
			target.setInsuranceValueAmountValue(source.getInsuranceValueAmount().getValue());
		}
		else
		{
			target.setInsuranceValueAmountValue(Double.parseDouble((insuranceValueAmountValuePref == null) ? "0.0d"
					: insuranceValueAmountValuePref.getValue()));
		}
	}

	protected TenantPreferenceService getTenantPreferenceService()
	{
		return tenantPreferenceService;
	}

	@Required
	public void setTenantPreferenceService(final TenantPreferenceService tenantPreferenceService)
	{
		this.tenantPreferenceService = tenantPreferenceService;
	}

}
