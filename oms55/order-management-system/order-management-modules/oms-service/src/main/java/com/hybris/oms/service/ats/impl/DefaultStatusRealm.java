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
package com.hybris.oms.service.ats.impl;

import com.hybris.oms.service.ats.StatusRealm;


/**
 * Default implementation of {@link StatusRealm}
 */
public class DefaultStatusRealm implements StatusRealm
{
	private String name;
	private String prefix;

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	@Override
	public String getPrefix()
	{
		return prefix;
	}

	public void setPrefix(final String prefix)
	{
		this.prefix = prefix;
	}

}
