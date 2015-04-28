

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

package com.hybris.oms.domain.types;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

import com.hybris.oms.domain.ValueType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
/**
* Represents an individual&#x27;s contact information.
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlRootElement
public class Contact  implements Serializable, ValueType<Contact>
{

	public final static long serialVersionUID = 566395172L;

	private String channelCode;

	private String electronicMail;

	private String name;

	private String note;

	private String telefax;

	private String telephone;

	private String value;

	
	public Contact(){}

	protected Contact(Builder builder)
	{
	
		setChannelCode(builder.getChannelCode());
		setElectronicMail(builder.getElectronicMail());
		setName(builder.getName());
		setNote(builder.getNote());
		setTelefax(builder.getTelefax());
		setTelephone(builder.getTelephone());
		setValue(builder.getValue());
	
	}


	public Contact(final String electronicMail, final String name, final String note, final String telefax, final String telephone, final String channelCode, final String value)
	{
		this.electronicMail = electronicMail;
		this.name = name;
		this.note = note;
		this.telefax = telefax;
		this.telephone = telephone;
		this.channelCode = channelCode;
		this.value = value;
	}

    		

	/**
	* gets 
	*
	* @returns String
	*/
	public String getChannelCode()
	{
		return channelCode;
	}

	/**
	* sets 
	*
	*/
	public void setChannelCode(String channelCode)
	{
		this.channelCode = channelCode;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getElectronicMail()
	{
		return electronicMail;
	}

	/**
	* sets 
	*
	*/
	public void setElectronicMail(String electronicMail)
	{
		this.electronicMail = electronicMail;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getName()
	{
		return name;
	}

	/**
	* sets 
	*
	*/
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getNote()
	{
		return note;
	}

	/**
	* sets 
	*
	*/
	public void setNote(String note)
	{
		this.note = note;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getTelefax()
	{
		return telefax;
	}

	/**
	* sets 
	*
	*/
	public void setTelefax(String telefax)
	{
		this.telefax = telefax;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getTelephone()
	{
		return telephone;
	}

	/**
	* sets 
	*
	*/
	public void setTelephone(String telephone)
	{
		this.telephone = telephone;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getValue()
	{
		return value;
	}

	/**
	* sets 
	*
	*/
	public void setValue(String value)
	{
		this.value = value;
	}


	@Override
	public int hashCode()
	{
		return new HashCodeBuilder()
				.append(electronicMail)
				.append(name)
				.append(telefax)
				.append(telephone)
			.toHashCode();
	}

	@Override
	public boolean equals(final Object obj)
	{
		if(obj instanceof Contact){
			final Contact other = (Contact) obj;
			return new EqualsBuilder()
				.append(electronicMail, other.electronicMail)
				.append(name, other.name)
				.append(telefax, other.telefax)
				.append(telephone, other.telephone)
			.isEquals();
		} else{
			return false;
		}
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

	public abstract static class Builder<T extends Builder<T>> 
	{
		private String channelCode;
		private String electronicMail;
		private String name;
		private String note;
		private String telefax;
		private String telephone;
		private String value;
		
	
		/**
		* sets 
		*
		*/
		public T setChannelCode(String channelCode)
		{
			this.channelCode = channelCode;
			return self();
		}

		private String getChannelCode()
		{
			return channelCode;
		}
	
		/**
		* sets 
		*
		*/
		public T setElectronicMail(String electronicMail)
		{
			this.electronicMail = electronicMail;
			return self();
		}

		private String getElectronicMail()
		{
			return electronicMail;
		}
	
		/**
		* sets 
		*
		*/
		public T setName(String name)
		{
			this.name = name;
			return self();
		}

		private String getName()
		{
			return name;
		}
	
		/**
		* sets 
		*
		*/
		public T setNote(String note)
		{
			this.note = note;
			return self();
		}

		private String getNote()
		{
			return note;
		}
	
		/**
		* sets 
		*
		*/
		public T setTelefax(String telefax)
		{
			this.telefax = telefax;
			return self();
		}

		private String getTelefax()
		{
			return telefax;
		}
	
		/**
		* sets 
		*
		*/
		public T setTelephone(String telephone)
		{
			this.telephone = telephone;
			return self();
		}

		private String getTelephone()
		{
			return telephone;
		}
	
		/**
		* sets 
		*
		*/
		public T setValue(String value)
		{
			this.value = value;
			return self();
		}

		private String getValue()
		{
			return value;
		}
	
		protected abstract T self();

		public Contact build(){
			return new Contact(this);
		}
	}
}

