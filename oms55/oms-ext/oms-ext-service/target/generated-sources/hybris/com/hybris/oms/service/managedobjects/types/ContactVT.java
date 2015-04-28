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
 * Generated valuetype class for type ContactVT first defined at extension <code>valuetypes</code>.
 * <p/>
 * Describes a contactable person or organisation department..
 */
@SuppressWarnings("serial")
public class ContactVT implements java.io.Serializable
{
	/**<i>Generated value type code constant.</i>*/
	public String _TYPECODE = "ContactVT";

	
	private final String channelCode;
	private final String electronicMail;
	private final String name;
	private final String note;
	private final String telefax;
	private final String telephone;
	private final String value;


	public ContactVT(final String channelCode, final String electronicMail, final String name, final String note, final String telefax, final String telephone, final String value) {
		this.channelCode = channelCode;
		this.electronicMail = electronicMail;
		this.name = name;
		this.note = note;
		this.telefax = telefax;
		this.telephone = telephone;
		this.value = value;
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContactVT.channelCode</code> attribute defined at extension <code>valuetypes</code>.
	 * <p/>
	 * 
	 * @return the channelCode
	 */
	public String getChannelCode() {
		return this.channelCode;
	}

		
	/**
	 * <i>Generated method</i> - Getter of the <code>ContactVT.electronicMail</code> attribute defined at extension <code>valuetypes</code>.
	 * <p/>
	 * 
	 * @return the electronicMail
	 */
	public String getElectronicMail() {
		return this.electronicMail;
	}

		
	/**
	 * <i>Generated method</i> - Getter of the <code>ContactVT.name</code> attribute defined at extension <code>valuetypes</code>.
	 * <p/>
	 * 
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

		
	/**
	 * <i>Generated method</i> - Getter of the <code>ContactVT.note</code> attribute defined at extension <code>valuetypes</code>.
	 * <p/>
	 * 
	 * @return the note
	 */
	public String getNote() {
		return this.note;
	}

		
	/**
	 * <i>Generated method</i> - Getter of the <code>ContactVT.telefax</code> attribute defined at extension <code>valuetypes</code>.
	 * <p/>
	 * 
	 * @return the telefax
	 */
	public String getTelefax() {
		return this.telefax;
	}

		
	/**
	 * <i>Generated method</i> - Getter of the <code>ContactVT.telephone</code> attribute defined at extension <code>valuetypes</code>.
	 * <p/>
	 * 
	 * @return the telephone
	 */
	public String getTelephone() {
		return this.telephone;
	}

		
	/**
	 * <i>Generated method</i> - Getter of the <code>ContactVT.value</code> attribute defined at extension <code>valuetypes</code>.
	 * <p/>
	 * 
	 * @return the value
	 */
	public String getValue() {
		return this.value;
	}

	

	/**
	 * Generated hashCode() method
	 */
	@Override
	public int hashCode()
	{
	return com.google.common.base.Objects.hashCode(
		this.channelCode, this.electronicMail, this.name, this.note, this.telefax, this.telephone, this.value);
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

		final ContactVT other = (ContactVT) obj;
		
		return com.google.common.base.Objects.equal(this.channelCode, other.channelCode)
			&& com.google.common.base.Objects.equal(this.electronicMail, other.electronicMail)
			&& com.google.common.base.Objects.equal(this.name, other.name)
			&& com.google.common.base.Objects.equal(this.note, other.note)
			&& com.google.common.base.Objects.equal(this.telefax, other.telefax)
			&& com.google.common.base.Objects.equal(this.telephone, other.telephone)
			&& com.google.common.base.Objects.equal(this.value, other.value);
	}
	
	/**
	 * Generated toString() method
	 */
	@Override
	public String toString()
	{
		return com.google.common.base.Objects.toStringHelper(this)
			.addValue(this.channelCode) //
			.addValue(this.electronicMail) //
			.addValue(this.name) //
			.addValue(this.note) //
			.addValue(this.telefax) //
			.addValue(this.telephone) //
			.addValue(this.value) //
			.toString();
	}
}