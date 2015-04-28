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
 * Generated valuetype class for type AmountVT first defined at extension <code>valuetypes</code>.
 * <p/>
 * Describes a Monetary Amount.
 */
@SuppressWarnings("serial")
public class AmountVT implements java.io.Serializable
{
	/**<i>Generated value type code constant.</i>*/
	public String _TYPECODE = "AmountVT";

	
	private final String currencyCode;
	private final Double value;


	public AmountVT(final String currencyCode, final Double value) {
		this.currencyCode = currencyCode;
		this.value = value;
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AmountVT.currencyCode</code> attribute defined at extension <code>valuetypes</code>.
	 * <p/>
	 * 
	 * @return the currencyCode
	 */
	public String getCurrencyCode() {
		return this.currencyCode;
	}

		
	/**
	 * <i>Generated method</i> - Getter of the <code>AmountVT.value</code> attribute defined at extension <code>valuetypes</code>.
	 * <p/>
	 * 
	 * @return the value
	 */
	public Double getValue() {
		return this.value;
	}

	

	/**
	 * Generated hashCode() method
	 */
	@Override
	public int hashCode()
	{
	return com.google.common.base.Objects.hashCode(
		this.currencyCode, this.value);
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

		final AmountVT other = (AmountVT) obj;
		
		return com.google.common.base.Objects.equal(this.currencyCode, other.currencyCode)
			&& com.google.common.base.Objects.equal(this.value, other.value);
	}
	
	/**
	 * Generated toString() method
	 */
	@Override
	public String toString()
	{
		return com.google.common.base.Objects.toStringHelper(this)
			.addValue(this.currencyCode) //
			.addValue(this.value) //
			.toString();
	}
}