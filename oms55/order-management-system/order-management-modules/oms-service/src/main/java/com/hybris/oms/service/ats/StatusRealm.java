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
package com.hybris.oms.service.ats;


/**
 * Defines a realm representing a specific type of aggregates used in terms of an ATS formula.
 */
public interface StatusRealm
{
	StatusRealm INVENTORY = new StatusRealm()
	{
		private static final String REALM_PREFIX = "I";
		private static final String REALM_NAME = "Inventory";

		@Override
		public String getPrefix()
		{
			return REALM_PREFIX;
		}

		@Override
		public String toString()
		{
			return REALM_NAME;
		}
	};

	StatusRealm ORDER = new StatusRealm()
	{
		private static final String REALM_PREFIX = "O";
		private static final String REALM_NAME = "Order";

		@Override
		public String getPrefix()
		{
			return REALM_PREFIX;
		}

		@Override
		public String toString()
		{
			return REALM_NAME;
		}
	};

	StatusRealm FUTUR = new StatusRealm()
	{
		private static final String REALM_PREFIX = "F";
		private static final String REALM_NAME = "Futur";

		@Override
		public String getPrefix()
		{
			return REALM_PREFIX;
		}

		@Override
		public String toString()
		{
			return REALM_NAME;
		}
	};


	/**
	 * Returns the prefix to be used inside the formula.
	 */
	String getPrefix();

}
