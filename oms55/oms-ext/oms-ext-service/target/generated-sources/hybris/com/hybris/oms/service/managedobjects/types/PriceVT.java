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
 
package com.hybris.oms.service.managedobjects.types;


/**
 * Generated valuetype class for type PriceVT first defined at extension <code>valuetypes</code>.
 * <p/>
 * Describes prices..
 */
@SuppressWarnings("serial")
public class PriceVT implements java.io.Serializable
{
	/**<i>Generated value type code constant.</i>*/
	public String _TYPECODE = "PriceVT";

	
	private final String subTotalCurrencyCode;
	private final Double subTotalValue;
	private final String taxCurrencyCode;
	private final Double taxValue;
	private final String taxCommittedCurrencyCode;
	private final Double taxCommittedValue;


	public PriceVT(final String subTotalCurrencyCode, final Double subTotalValue, final String taxCurrencyCode, final Double taxValue, final String taxCommittedCurrencyCode, final Double taxCommittedValue) {
		this.subTotalCurrencyCode = subTotalCurrencyCode;
		this.subTotalValue = subTotalValue;
		this.taxCurrencyCode = taxCurrencyCode;
		this.taxValue = taxValue;
		this.taxCommittedCurrencyCode = taxCommittedCurrencyCode;
		this.taxCommittedValue = taxCommittedValue;
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PriceVT.subTotalCurrencyCode</code> attribute defined at extension <code>valuetypes</code>.
	 * <p/>
	 * 
	 * @return the subTotalCurrencyCode
	 */
	public String getSubTotalCurrencyCode() {
		return this.subTotalCurrencyCode;
	}

		
	/**
	 * <i>Generated method</i> - Getter of the <code>PriceVT.subTotalValue</code> attribute defined at extension <code>valuetypes</code>.
	 * <p/>
	 * 
	 * @return the subTotalValue
	 */
	public Double getSubTotalValue() {
		return this.subTotalValue;
	}

		
	/**
	 * <i>Generated method</i> - Getter of the <code>PriceVT.taxCurrencyCode</code> attribute defined at extension <code>valuetypes</code>.
	 * <p/>
	 * 
	 * @return the taxCurrencyCode
	 */
	public String getTaxCurrencyCode() {
		return this.taxCurrencyCode;
	}

		
	/**
	 * <i>Generated method</i> - Getter of the <code>PriceVT.taxValue</code> attribute defined at extension <code>valuetypes</code>.
	 * <p/>
	 * 
	 * @return the taxValue
	 */
	public Double getTaxValue() {
		return this.taxValue;
	}

		
	/**
	 * <i>Generated method</i> - Getter of the <code>PriceVT.taxCommittedCurrencyCode</code> attribute defined at extension <code>valuetypes</code>.
	 * <p/>
	 * 
	 * @return the taxCommittedCurrencyCode
	 */
	public String getTaxCommittedCurrencyCode() {
		return this.taxCommittedCurrencyCode;
	}

		
	/**
	 * <i>Generated method</i> - Getter of the <code>PriceVT.taxCommittedValue</code> attribute defined at extension <code>valuetypes</code>.
	 * <p/>
	 * 
	 * @return the taxCommittedValue
	 */
	public Double getTaxCommittedValue() {
		return this.taxCommittedValue;
	}

	

	/**
	 * Generated hashCode() method
	 */
	@Override
	public int hashCode()
	{
	return com.google.common.base.Objects.hashCode(
		this.subTotalCurrencyCode, this.subTotalValue, this.taxCurrencyCode, this.taxValue, this.taxCommittedCurrencyCode, this.taxCommittedValue);
	}
	
	/**
	 * Generated equals() method
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}

		final PriceVT other = (PriceVT) obj;
		
		return com.google.common.base.Objects.equal(this.subTotalCurrencyCode, other.subTotalCurrencyCode)
			&& com.google.common.base.Objects.equal(this.subTotalValue, other.subTotalValue)
			&& com.google.common.base.Objects.equal(this.taxCurrencyCode, other.taxCurrencyCode)
			&& com.google.common.base.Objects.equal(this.taxValue, other.taxValue)
			&& com.google.common.base.Objects.equal(this.taxCommittedCurrencyCode, other.taxCommittedCurrencyCode)
			&& com.google.common.base.Objects.equal(this.taxCommittedValue, other.taxCommittedValue);
	}
	
	/**
	 * Generated toString() method
	 */
	@Override
	public String toString()
	{
		return com.google.common.base.Objects.toStringHelper(this)
			.addValue(this.subTotalCurrencyCode) //
			.addValue(this.subTotalValue) //
			.addValue(this.taxCurrencyCode) //
			.addValue(this.taxValue) //
			.addValue(this.taxCommittedCurrencyCode) //
			.addValue(this.taxCommittedValue) //
			.toString();
	}
}