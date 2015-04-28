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
package com.hybris.oms.service.sourcing.strategy;

/**
 * Extension point for splitting on the order or orderline level.
 */
public interface SourcingSplitOptions
{
	SourcingSplitOptions SPLIT = new SourcingSplitOptions()
	{
		@Override
		public String getAction()
		{
			return "SPLIT";
		}

		@Override
		public String toString()
		{
			return getAction();
		}

		@Override
		public boolean allowSplitting()
		{
			return true;
		}
	};

	SourcingSplitOptions CANCELLED = new SourcingSplitOptions()
	{
		@Override
		public String getAction()
		{
			return "CANCELLED";
		}

		@Override
		public String toString()
		{
			return getAction();
		}

		@Override
		public boolean allowSplitting()
		{
			return false;
		}
	};

	SourcingSplitOptions ON_HOLD = new SourcingSplitOptions()
	{
		@Override
		public String getAction()
		{
			return "ON_HOLD";
		}

		@Override
		public String toString()
		{
			return getAction();
		}

		@Override
		public boolean allowSplitting()
		{
			return false;
		}
	};

	/**
	 * Return <tt>true</tt> when splitting is allowed.
	 * 
	 * @return <tt>true</tt> when splitting is allowed
	 */
	boolean allowSplitting();

	/**
	 * Returns an action name if splitting prohibits sourcing this line. By default, this is persisted as
	 * orderline.status if it is not <tt>null</tt>.
	 * 
	 * @return orderline status, can be <tt>null</tt>
	 */
	String getAction();


}
