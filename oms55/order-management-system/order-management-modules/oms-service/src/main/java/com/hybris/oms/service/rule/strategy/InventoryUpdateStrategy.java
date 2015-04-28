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
package com.hybris.oms.service.rule.strategy;

/**
 * Interface for all inventory update strategies. An inventory update strategy is responsible for calculating the
 * quantity by which we want to modify the inventory.
 */
public interface InventoryUpdateStrategy
{
	/**
	 * Calculate the quantity by which to update the inventory.
	 * 
	 * @param dto - data transfer object to hold information required for the strategy
	 * @return the quantity by which we want to update the inventory
	 */
	int calculateInventoryUpdateQuantity(InventoryUpdateDto dto);
}
