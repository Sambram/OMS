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
package com.hybris.oms.domain.basestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "baseStores")
public class BaseStoreList implements Serializable
{


	private static final long serialVersionUID = 4279386105543155884L;

	@XmlElement(name = "baseStore")
	private List<BaseStore> BaseStores = new ArrayList<>();

	public void addBaseStore(final BaseStore baseStore)
	{
		if (this.BaseStores == null)
		{
			this.BaseStores = new ArrayList<>();
		}
		this.BaseStores.add(baseStore);
	}


	public int getNumberOfBaseStores()
	{
		return this.BaseStores.size();
	}


	public List<BaseStore> getBaseStores()
	{
		return Collections.unmodifiableList(this.BaseStores);
	}


	public void initializeBaseStores(final List<BaseStore> newBaseStores)
	{
		assert this.BaseStores.isEmpty();
		for (final BaseStore baseStore : newBaseStores)
		{
			this.addBaseStore(baseStore);
		}
	}

	public void removeBaseStore(final BaseStore baseStore)
	{
		this.BaseStores.remove(baseStore);
	}
}
