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
package com.hybris.oms.service.health.impl;


import com.hybris.kernel.api.KernelEventAware;
import com.hybris.kernel.api.KernelEventType;
import com.hybris.kernel.api.annotations.KernelEventHandler;
import com.hybris.kernel.persistence.event.DefaultKernelEvent;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


/**
 * Helper class implementing semaphores for waiting for the asynchronous kernel events.
 */
public class KernelEventSemaphore implements KernelEventAware
{
	public static final long RELEASE_DELAY_MS = 100;

	private static final int ACQUIRE_TIMEOUT_SECONDS = 100;

	private final Semaphore orderCreatedSemaphore = new Semaphore(0);
	private final Semaphore itemCreatedSemaphore = new Semaphore(0);
	private final Semaphore itemUpdatedSemaphore = new Semaphore(0);

	/**
	 * Invoked when an inventory item is created.
	 * 
	 * @param event kernel event
	 */
	@KernelEventHandler(events = {KernelEventType.CREATE}, typeCodes = {"BinData", "StockroomLocationData",
			"CurrentItemQuantityData", "ItemStatusData", "ItemLocationData"})
	public void onItemCreated(final DefaultKernelEvent event)
	{
		delay();
		itemCreatedSemaphore.release();
	}

	/**
	 * Invoked when an order is created.
	 * 
	 * @param event kernel event
	 */
	@KernelEventHandler(events = {KernelEventType.CREATE}, typeCodes = {"OrderData"})
	public void onOrderCreated(final DefaultKernelEvent event)
	{
		delay();
		orderCreatedSemaphore.release();
	}

	/**
	 * Invoked when an inventory quantity is updated.
	 * 
	 * @param event kernel event
	 */
	@KernelEventHandler(events = {KernelEventType.UPDATE}, typeCodes = {"CurrentItemQuantityData"})
	public void onItemUpdated(final DefaultKernelEvent event)
	{
		delay();
		itemUpdatedSemaphore.release();
	}

	/**
	 * Waits until given number of orders is created.
	 * 
	 * @param numberOfOrders number of orders
	 */
	public void waitForOrdersCreated(final int numberOfOrders)
	{
		try
		{
			orderCreatedSemaphore.tryAcquire(numberOfOrders, ACQUIRE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
		}
		catch (final InterruptedException e)
		{
			//
		}
	}

	/**
	 * Waits until given number of inventories is created.
	 * 
	 * @param numberOfInventories number of inventories
	 */
	public void waitForItemsCreated(final int numberOfInventories)
	{
		try
		{
			itemCreatedSemaphore.tryAcquire(numberOfInventories, ACQUIRE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
		}
		catch (final InterruptedException e)
		{
			//
		}

	}

	/**
	 * Waits until given number of inventories is updated.
	 * 
	 * @param numberOfInventories number of orders
	 */
	public void waitForItemsUpdated(final int numberOfInventories)
	{
		try
		{
			itemUpdatedSemaphore.tryAcquire(numberOfInventories, ACQUIRE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
		}
		catch (final InterruptedException e)
		{
			//
		}

	}

	private void delay()
	{
		if (RELEASE_DELAY_MS > 0)
		{
			try
			{
				Thread.sleep(RELEASE_DELAY_MS);
			}
			catch (final InterruptedException e)
			{
				//
			}
		}
	}

}
