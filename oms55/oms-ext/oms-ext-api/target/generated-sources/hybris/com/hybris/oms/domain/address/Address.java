

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

package com.hybris.oms.domain.address;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

import com.hybris.oms.domain.ValueType;
import java.lang.Double;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
/**
* Represents an Address.
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlRootElement
public class Address  implements Serializable, ValueType<Address>
{

	public final static long serialVersionUID = 1607924113L;

	private String addressLine1;

	private String addressLine2;

	private String cityName;

	private String countryIso3166Alpha2Code;

	private String countryName;

	private String countrySubentity;

	private Double latitudeValue;

	private Double longitudeValue;

	private String postalZone;

	private String name;

	private String phoneNumber;

	
	public Address(){}

	protected Address(Builder builder)
	{
	
		setAddressLine1(builder.getAddressLine1());
		setAddressLine2(builder.getAddressLine2());
		setCityName(builder.getCityName());
		setCountryIso3166Alpha2Code(builder.getCountryIso3166Alpha2Code());
		setCountryName(builder.getCountryName());
		setCountrySubentity(builder.getCountrySubentity());
		setLatitudeValue(builder.getLatitudeValue());
		setLongitudeValue(builder.getLongitudeValue());
		setPostalZone(builder.getPostalZone());
		setName(builder.getName());
		setPhoneNumber(builder.getPhoneNumber());
	
	}


	public Address(final Address address)
	{
		this.addressLine1 = address.getAddressLine1();
		this.addressLine2 = address.getAddressLine2();
		this.cityName = address.getCityName();
		this.countrySubentity = address.getCountrySubentity();
		this.postalZone = address.getPostalZone();
		this.latitudeValue = address.getLatitudeValue();
		this.longitudeValue = address.getLatitudeValue();
		this.countryIso3166Alpha2Code = address.getCountryIso3166Alpha2Code();
		this.countryName = address.getCountryName();
		this.name = address.getName();
		this.phoneNumber = address.getPhoneNumber();
	}
	
	public Address(final String addressLine1, final String addressLine2, final String cityName, final String countrySubentity,
			final String postalZone, final Double latitudeValue, final Double longitudeValue, final String countryIso3166Alpha2Code,
			final String countryName, final String name, final String phoneNumber)
	{
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.cityName = cityName;
		this.countrySubentity = countrySubentity;
		this.postalZone = postalZone;
		this.latitudeValue = latitudeValue;
		this.longitudeValue = longitudeValue;
		this.countryIso3166Alpha2Code = countryIso3166Alpha2Code;
		this.countryName = countryName;
		this.name = name;
		this.phoneNumber = phoneNumber;
	}

    		

	/**
	* gets 
	*
	* @returns String
	*/
	public String getAddressLine1()
	{
		return addressLine1;
	}

	/**
	* sets 
	*
	*/
	public void setAddressLine1(String addressLine1)
	{
		this.addressLine1 = addressLine1;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getAddressLine2()
	{
		return addressLine2;
	}

	/**
	* sets 
	*
	*/
	public void setAddressLine2(String addressLine2)
	{
		this.addressLine2 = addressLine2;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getCityName()
	{
		return cityName;
	}

	/**
	* sets 
	*
	*/
	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getCountryIso3166Alpha2Code()
	{
		return countryIso3166Alpha2Code;
	}

	/**
	* sets 
	*
	*/
	public void setCountryIso3166Alpha2Code(String countryIso3166Alpha2Code)
	{
		this.countryIso3166Alpha2Code = countryIso3166Alpha2Code;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getCountryName()
	{
		return countryName;
	}

	/**
	* sets 
	*
	*/
	public void setCountryName(String countryName)
	{
		this.countryName = countryName;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getCountrySubentity()
	{
		return countrySubentity;
	}

	/**
	* sets 
	*
	*/
	public void setCountrySubentity(String countrySubentity)
	{
		this.countrySubentity = countrySubentity;
	}

	/**
	* gets 
	*
	* @returns Double
	*/
	public Double getLatitudeValue()
	{
		return latitudeValue;
	}

	/**
	* sets 
	*
	*/
	public void setLatitudeValue(Double latitudeValue)
	{
		this.latitudeValue = latitudeValue;
	}

	/**
	* gets 
	*
	* @returns Double
	*/
	public Double getLongitudeValue()
	{
		return longitudeValue;
	}

	/**
	* sets 
	*
	*/
	public void setLongitudeValue(Double longitudeValue)
	{
		this.longitudeValue = longitudeValue;
	}

	/**
	* gets 
	*
	* @returns String
	*/
	public String getPostalZone()
	{
		return postalZone;
	}

	/**
	* sets 
	*
	*/
	public void setPostalZone(String postalZone)
	{
		this.postalZone = postalZone;
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
	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	/**
	* sets 
	*
	*/
	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}


	@Override
	public int hashCode()
	{
		return new HashCodeBuilder()
				.append(addressLine1)
				.append(addressLine2)
				.append(cityName)
				.append(countryIso3166Alpha2Code)
				.append(countrySubentity)
				.append(postalZone)
			.toHashCode();
	}

	@Override
	public boolean equals(final Object obj)
	{
		if(obj instanceof Address){
			final Address other = (Address) obj;
			return new EqualsBuilder()
				.append(addressLine1, other.addressLine1)
				.append(addressLine2, other.addressLine2)
				.append(cityName, other.cityName)
				.append(countryIso3166Alpha2Code, other.countryIso3166Alpha2Code)
				.append(countrySubentity, other.countrySubentity)
				.append(postalZone, other.postalZone)
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
		private String addressLine1;
		private String addressLine2;
		private String cityName;
		private String countryIso3166Alpha2Code;
		private String countryName;
		private String countrySubentity;
		private Double latitudeValue;
		private Double longitudeValue;
		private String postalZone;
		private String name;
		private String phoneNumber;
		
	
		/**
		* sets 
		*
		*/
		public T setAddressLine1(String addressLine1)
		{
			this.addressLine1 = addressLine1;
			return self();
		}

		private String getAddressLine1()
		{
			return addressLine1;
		}
	
		/**
		* sets 
		*
		*/
		public T setAddressLine2(String addressLine2)
		{
			this.addressLine2 = addressLine2;
			return self();
		}

		private String getAddressLine2()
		{
			return addressLine2;
		}
	
		/**
		* sets 
		*
		*/
		public T setCityName(String cityName)
		{
			this.cityName = cityName;
			return self();
		}

		private String getCityName()
		{
			return cityName;
		}
	
		/**
		* sets 
		*
		*/
		public T setCountryIso3166Alpha2Code(String countryIso3166Alpha2Code)
		{
			this.countryIso3166Alpha2Code = countryIso3166Alpha2Code;
			return self();
		}

		private String getCountryIso3166Alpha2Code()
		{
			return countryIso3166Alpha2Code;
		}
	
		/**
		* sets 
		*
		*/
		public T setCountryName(String countryName)
		{
			this.countryName = countryName;
			return self();
		}

		private String getCountryName()
		{
			return countryName;
		}
	
		/**
		* sets 
		*
		*/
		public T setCountrySubentity(String countrySubentity)
		{
			this.countrySubentity = countrySubentity;
			return self();
		}

		private String getCountrySubentity()
		{
			return countrySubentity;
		}
	
		/**
		* sets 
		*
		*/
		public T setLatitudeValue(Double latitudeValue)
		{
			this.latitudeValue = latitudeValue;
			return self();
		}

		private Double getLatitudeValue()
		{
			return latitudeValue;
		}
	
		/**
		* sets 
		*
		*/
		public T setLongitudeValue(Double longitudeValue)
		{
			this.longitudeValue = longitudeValue;
			return self();
		}

		private Double getLongitudeValue()
		{
			return longitudeValue;
		}
	
		/**
		* sets 
		*
		*/
		public T setPostalZone(String postalZone)
		{
			this.postalZone = postalZone;
			return self();
		}

		private String getPostalZone()
		{
			return postalZone;
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
		public T setPhoneNumber(String phoneNumber)
		{
			this.phoneNumber = phoneNumber;
			return self();
		}

		private String getPhoneNumber()
		{
			return phoneNumber;
		}
	
		protected abstract T self();

		public Address build(){
			return new Address(this);
		}
	}
}

