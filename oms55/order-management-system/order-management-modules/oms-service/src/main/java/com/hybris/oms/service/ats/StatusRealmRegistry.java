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

import java.util.Set;




/**
 * Registry mapping {@link StatusRealm} to available aggregation classes.
 */
public interface StatusRealmRegistry
{

	/**
	 * Returns a local or global aggregate class for the given realm.
	 * 
	 * @return aggregate class, never <tt>null</tt>
	 * @throws IllegalArgumentException if the realm is not registered or <tt>null</tt>.
	 */
	Class<? extends AtsQuantityAggregate> getAggregateClassForRealm(StatusRealm realm, boolean global);

	/**
	 * Returns an aggregate class for retrieving unassigned quantity for a SKU.
	 */
	Class<? extends AtsUnassignedQuantity> getAggClassForUnassignedQuantity();

	/**
	 * Returns the {@link StatusRealm} for the aggregate class or <tt>null</tt>.
	 * 
	 * @return realm or <tt>null</tt> if there is no realm assigned
	 */
	StatusRealm getRealmForAggregateClass(Class<? extends AtsAggregate> aggregateClass);

	/**
	 * Returns the {@link StatusRealm} for the given prefix.
	 * 
	 * @return realm, never <tt>null</tt>
	 * @throws IllegalArgumentException if the prefix is not registered or blank.
	 */
	StatusRealm getRealmForPrefix(String prefix);

	/**
	 * Returns a list of registered status realms.
	 * 
	 * @return list of {@link StatusRealm}, never <tt>null</tt>
	 */
	Set<StatusRealm> getRegisteredRealms();

}
