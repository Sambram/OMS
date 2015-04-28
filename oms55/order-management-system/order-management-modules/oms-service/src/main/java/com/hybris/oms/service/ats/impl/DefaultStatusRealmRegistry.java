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

import com.hybris.oms.service.ats.AtsAggregate;
import com.hybris.oms.service.ats.AtsLocalQuantityAggregate;
import com.hybris.oms.service.ats.AtsQuantityAggregate;
import com.hybris.oms.service.ats.AtsUnassignedQuantity;
import com.hybris.oms.service.ats.StatusRealm;
import com.hybris.oms.service.ats.StatusRealmRegistry;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;

import com.google.common.base.Preconditions;


/**
 * Default implementation of {@link StatusRealmRegistry}
 */
public class DefaultStatusRealmRegistry implements StatusRealmRegistry, InitializingBean
{
	private List<RealmInfo> realmInfos;
	private Map<StatusRealm, RealmInfo> realmInfoMap;
	private Map<Class<? extends AtsQuantityAggregate>, StatusRealm> class2Realm;
	private Class<? extends AtsUnassignedQuantity> atsUnassignedQuantity;

	@Override
	public Class<? extends AtsQuantityAggregate> getAggregateClassForRealm(final StatusRealm realm, final boolean global)
	{
		final RealmInfo info = realmInfoMap.get(realm);
		Preconditions.checkArgument(info != null, "Unknown realm %s provided", realm);
		return global ? info.getGlobalAggregateClass() : info.getLocalAggregateClass();
	}

	@Override
	public StatusRealm getRealmForAggregateClass(final Class<? extends AtsAggregate> aggregateClass)
	{
		return class2Realm.get(aggregateClass);
	}

	@Override
	public StatusRealm getRealmForPrefix(final String prefix)
	{
		StatusRealm result = null;
		for (final StatusRealm realm : realmInfoMap.keySet())
		{
			if (realm.getPrefix().equals(prefix))
			{
				result = realm;
				break;
			}
		}
		Preconditions.checkArgument(result != null, "Unknown realm prefix %s provided", prefix);
		return result;
	}

	@Override
	public Set<StatusRealm> getRegisteredRealms()
	{
		return Collections.unmodifiableSet(realmInfoMap.keySet());
	}

	@Override
	public Class<? extends AtsUnassignedQuantity> getAggClassForUnassignedQuantity()
	{
		return atsUnassignedQuantity;
	}

	@Override
	public void afterPropertiesSet()
	{
		Preconditions.checkArgument(CollectionUtils.isNotEmpty(realmInfos), "realmInfos cannot be empty");
		realmInfoMap = new HashMap<>();
		class2Realm = new HashMap<>();
		final Set<String> prefixes = new HashSet<>();
		for (final RealmInfo info : realmInfos)
		{
			Preconditions.checkNotNull(info.getGlobalAggregateClass(), "globalAggregateClass cannot be null");
			Preconditions.checkNotNull(info.getLocalAggregateClass(), "localAggregateClass cannot be null");
			Preconditions.checkArgument(!info.getLocalAggregateClass().equals(info.getGlobalAggregateClass()),
					"global and localAggregateClass cannot be equal");
			Preconditions.checkNotNull(info.getRealm(), "realm cannot be null");
			realmInfoMap.put(info.getRealm(), info);
			class2Realm.put(info.getGlobalAggregateClass(), info.getRealm());
			class2Realm.put(info.getLocalAggregateClass(), info.getRealm());
			prefixes.add(info.getRealm().getPrefix());
		}
		Preconditions.checkArgument(prefixes.size() == realmInfos.size(), "Prefixes are defined multiple times");
		Preconditions.checkNotNull(atsUnassignedQuantity, "atsUnassignedQuantity cannot be null");
		realmInfos = null;
	}

	public void setRealmInfos(final List<RealmInfo> realmInfos)
	{
		this.realmInfos = realmInfos;
	}

	protected Class<? extends AtsUnassignedQuantity> getAtsUnassignedQuantity()
	{
		return atsUnassignedQuantity;
	}

	public void setAtsUnassignedQuantity(final Class<? extends AtsUnassignedQuantity> atsUnassignedQuantity)
	{
		this.atsUnassignedQuantity = atsUnassignedQuantity;
	}

	/**
	 * Bean used for configuring realms in Spring.
	 */
	public static class RealmInfo
	{
		private Class<AtsQuantityAggregate> globalAggregateClass;
		private Class<AtsLocalQuantityAggregate> localAggregateClass;
		private StatusRealm realm;

		public Class<AtsQuantityAggregate> getGlobalAggregateClass()
		{
			return globalAggregateClass;
		}

		public void setGlobalAggregateClass(final Class<AtsQuantityAggregate> globalAggregateClass)
		{
			this.globalAggregateClass = globalAggregateClass;
		}

		public Class<AtsLocalQuantityAggregate> getLocalAggregateClass()
		{
			return localAggregateClass;
		}

		public void setLocalAggregateClass(final Class<AtsLocalQuantityAggregate> localAggregateClass)
		{
			this.localAggregateClass = localAggregateClass;
		}

		public StatusRealm getRealm()
		{
			return realm;
		}

		public void setRealm(final StatusRealm realm)
		{
			this.realm = realm;
		}
	}

}
