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
package com.hybris.oms.service.workflow.jmx;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;


@ManagedResource(objectName = "mbeans:name=OmsWorkflowProcessMonitor")
public class WorkflowProcessMonitor
{

	private final AtomicInteger totalOrderProcessCount = new AtomicInteger(0);
	private final AtomicInteger numRunningOrderProcesses = new AtomicInteger(0);
	private final AtomicInteger totalShipmentProcessCount = new AtomicInteger(0);
	private final AtomicInteger numRunningShipmentProcesses = new AtomicInteger(0);

    private int totalReturnProcessCount = 0;
    private int numRunningReturnProcesses = 0;

	@ManagedAttribute(description = "Get the total number of order processes started.")
	public int getTotalOrderProcessCount()
	{
		return totalOrderProcessCount.get();
	}

	@ManagedAttribute(description = "Get the number of order processes currently running.")
	public int getNumRunningOrderProcesses()
	{
		return numRunningOrderProcesses.get();
	}

	@ManagedOperation(description = "Reset the totalOrderProcessCount variable to zero.")
	public void resetTotalOrderProcessCount()
	{
		totalOrderProcessCount.set(0);
	}

	@ManagedAttribute(description = "Get the total number of shipment processes started.")
	public int getTotalShipmentProcessCount()
	{
		return totalShipmentProcessCount.get();
	}

	@ManagedAttribute(description = "Get the number of shipment processes currently running.")
	public int getNumRunningShipmentProcesses()
	{
		return numRunningShipmentProcesses.get();
	}

	@ManagedOperation(description = "Reset the totalShipmentProcessCount variable to zero.")
	public void resetTotalShipmentProcessCount()
	{
		totalShipmentProcessCount.set(0);
	}

	public void incrementNumOrderProcesses()
	{
		totalOrderProcessCount.incrementAndGet();
		numRunningOrderProcesses.incrementAndGet();
	}

	public void decrementRunningOrderProcesses()
	{
		numRunningOrderProcesses.decrementAndGet();
	}

	public void incrementNumShipmentProcesses()
	{
		totalShipmentProcessCount.incrementAndGet();
		numRunningShipmentProcesses.incrementAndGet();
	}

	public void decrementRunningShipmentProcesses()
    {
		numRunningShipmentProcesses.decrementAndGet();
    }


    @ManagedAttribute(description = "Get the total number of Return processes started.")
    public int getTotalReturnProcessCount()
    {
        return this.totalReturnProcessCount;
    }

    @ManagedAttribute(description = "Get the number of Return processes currently running.")
    public int getNumRunningReturnProcesses()
    {
        return this.numRunningReturnProcesses;
    }

    @ManagedOperation(description = "Reset the totalReturnProcessCount variable to zero.")
    public void resetTotalReturnProcessCount()
    {
        this.totalReturnProcessCount = 0;
    }

    public void incrementNumReturnProcesses()
    {
        this.totalReturnProcessCount++;
        this.numRunningReturnProcesses++;
    }

    public void decrementRunningReturnProcesses()
    {
        this.numRunningReturnProcesses--;
    }

}
