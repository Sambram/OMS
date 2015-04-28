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
 * Generated valuetype class for type QuantityVT first defined at extension <code>valuetypes</code>.
 * <p/>
 * Describes a Quantity.
 */
@SuppressWarnings("serial")
public class QuantityVT implements java.io.Serializable
{
	/**<i>Generated value type code constant.</i>*/
	public String _TYPECODE = "QuantityVT";

	
	private final String unitCode;
	private final int value;


	public QuantityVT(final String unitCode, final int value) {
		this.unitCode = unitCode;
		this.value = value;
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>QuantityVT.unitCode</code> attribute defined at extension <code>valuetypes</code>.
	 * <p/>
	 * 
	 * @return the unitCode
	 */
	public String getUnitCode() {
		return this.unitCode;
	}

		
	/**
	 * <i>Generated method</i> - Getter of the <code>QuantityVT.value</code> attribute defined at extension <code>valuetypes</code>.
	 * <p/>
	 * 
	 * @return the value
	 */
	public int getValue() {
		return this.value;
	}

	

	/**
	 * Generated hashCode() method
	 */
	@Override
	public int hashCode()
	{
	return com.google.common.base.Objects.hashCode(
		this.unitCode, this.value);
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

		final QuantityVT other = (QuantityVT) obj;
		
		return com.google.common.base.Objects.equal(this.unitCode, other.unitCode)
			&& com.google.common.base.Objects.equal(this.value, other.value);
	}
	
	/**
	 * Generated toString() method
	 */
	@Override
	public String toString()
	{
		return com.google.common.base.Objects.toStringHelper(this)
			.addValue(this.unitCode) //
			.addValue(this.value) //
			.toString();
	}
}