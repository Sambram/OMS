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
package com.hybris.oms.service.shipment;

import com.hybris.kernel.api.Page;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.shipping.ShipmentQueryObject;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.managedobjects.shipment.ShippingAndHandlingData;
import com.hybris.oms.service.service.Flushable;
import com.hybris.oms.service.workflow.executor.WorkflowExecutor;
import com.hybris.oms.service.workflow.executor.shipment.ShipmentWorkflowExecutor;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * The Interface ShipmentService.
 */
public interface ShipmentService extends Flushable
{
	/**
	 * Find all shipments by order id.
	 * 
	 * @param orderId
	 * @return list of shipments that belong to a given order
	 * @deprecated use {@link ShipmentService#findShipmentsByOrder(OrderData)}
	 */
	@Deprecated
	List<ShipmentData> findAllShipmentsByOrderId(String orderId);

	/**
	 * Find all shipments by order id.
	 * 
	 * @param order order to find shipments for.
	 * @return list of shipments that belong to a given order
	 */
	List<ShipmentData> findShipmentsByOrder(OrderData order);

	/**
	 * Find paged shipments by ShipmentQueryObject
	 * <p/>
	 * Returns paged shipments filtered by how the ShipmentQueryObject is populated.
	 * 
	 * @return Page of ShipmentData
	 */
	Page<ShipmentData> findPagedShipmentsByQuery(ShipmentQueryObject shipmentQueryObject);

	/**
	 * Find a shipment by shipment id.
	 * 
	 * @param shipmentId the shipment id
	 * @return ShipmentData
	 * @throws EntityNotFoundException the shipment not found exception
	 */
	ShipmentData getShipmentById(Long shipmentId) throws EntityNotFoundException;

	/**
	 * Update a shipment's status.
	 * 
	 * @param olqStatus
	 * @return The updated shipment
	 */
	ShipmentData updateShipmentStatus(ShipmentData shipment, final OrderLineQuantityStatusData olqStatus);

	/**
	 * Creates shipments for the given OLQs.
	 * 
	 * Starts a shipment workflow for each newly created {@link ShipmentData} using a {@link WorkflowExecutor}.
	 * 
	 * @see ShipmentWorkflowExecutor#execute(ShipmentData)
	 * 
	 * @param olqs the list of order line quantity requested to build the shipment
	 * @return the list of shipments created
	 */
	List<ShipmentData> createShipmentsByOLQs(List<OrderLineQuantityData> olqs);

	/**
	 * Update a shipment's status.
	 * 
	 * @param shipment the shipment to update.
	 * @param statusTenantPreferenceKey tenant preference key.
	 * @return The updated shipment
	 */
	ShipmentData updateShipmentStatus(ShipmentData shipment, String statusTenantPreferenceKey);

	/**
	 * Throws a IllegalStateException if a shipment workflow process was already started.
	 * 
	 * @param shipment
	 * @throws {@link IllegalStateException}
	 */
	void checkIfShipmentWorkflowAlreadyStarted(ShipmentData shipment) throws IllegalStateException;

	/**
	 * Removes the original shipment and unassigned any OLQs from this shipment.
	 * Reallocates the OLQs with the given olqIds to the new location.
	 * Finally, creates new shipments for the unassigned OLQs
	 * 
	 * @param reallocatedOlqs the OLQs ids which will be reallocated
	 * @param shipmentData the shipment concerned by the reallocation
	 * @return the list of shipments created after reallocation
	 * 
	 * @deprecated Use {@link ShipmentService#splitShipmentByOlqs(ShipmentData, List)} and
	 *             {@link ShipmentService#reallocateShipment(ShipmentData, StockroomLocationData)}
	 */
	@Deprecated
	List<ShipmentData> reallocateOlqsToNewShipmentsAndDeleteInitialShipment(Map<Long, String> reallocatedOlqs,
			ShipmentData shipmentData);

	/**
	 * Creates shipments for any OLQs without shipment by order id.
	 * 
	 * @param orderId the order id
	 * @throws EntityValidationException
	 * @return the list of shipments created
	 * @deprecated use {@link ShipmentService#createShipmentsForOrder(OrderData)}
	 */
	@Deprecated
	List<ShipmentData> createShipmentsByOrderId(String orderId) throws EntityValidationException;

	/**
	 * Creates shipments for any OLQs on this order that do not belong to a shipment.
	 * 
	 * Starts a shipment workflow for each newly created {@link ShipmentData} using a {@link WorkflowExecutor}.
	 * 
	 * @see ShipmentWorkflowExecutor#execute(ShipmentData)
	 * 
	 * @param order the order
	 * @throws EntityValidationException
	 * @return the list of shipments created
	 */
	List<ShipmentData> createShipmentsForOrder(OrderData order);

	/**
	 * Assigns the first shipmentId to shippingAndHandling, if unassigned.
	 * Then calls capturePayment in CIS and sets the shipment status to "payment captured"
	 * Sets the following prices for the shipment:
	 * - {@link ShipmentData#AMOUNTCAPTUREDVALUE} - {@link ShippingAndHandlingData#SHIPPINGPRICE_SUBTOTALVALUE} -
	 * {@link ShippingAndHandlingData#SHIPPINGPRICE_TAXVALUE}
	 * 
	 * @param shipment the shipment to update
	 * @return the updated shipment object
	 */
	ShipmentData capturePayment(ShipmentData shipment);

	/**
	 * If the label url of shipment delivery is <tt>null</tt>, calls createShipmentLabels in CIS and updates the label
	 * url.
	 * Then, retrieves the shipping label by calling getShipmentLabel in CIS.
	 * 
	 * @param shipmentData
	 * @return byte array of shipping labels
	 */
	byte[] getShippingLabels(ShipmentData shipmentData);

	/**
	 * Invoice taxes for a given shipment.
	 * Sets the following prices for the shipment:
	 * - {@link ShipmentData#MERCHANDISEPRICE_TAXCOMMITTEDVALUE} -
	 * {@link ShippingAndHandlingData#SHIPPINGPRICE_TAXCOMMITTEDVALUE}
	 * 
	 * @param shipment the shipment
	 * @return the updated shipment object
	 */
	ShipmentData invoiceTaxes(ShipmentData shipment);

	/**
	 * Calculate and set the following prices for a shipment:
	 * - {@link ShipmentData#TOTALGOODSITEMQUANTITYVALUE} - {@link ShipmentData#MERCHANDISEPRICE_SUBTOTALVALUE} -
	 * {@link ShipmentData#MERCHANDISEPRICE_TAXVALUE}
	 * 
	 * The following prices will not be modified:
	 * - {@link ShipmentData#AMOUNTCAPTUREDVALUE} - {@link ShipmentData#INSURANCEVALUEAMOUNTVALUE} -
	 * {@link ShipmentData#MERCHANDISEPRICE_TAXCOMMITTEDVALUE}
	 * 
	 * @param shipment
	 * @return the updated shipment
	 */
	ShipmentData computeShipmentPrices(ShipmentData shipment);

	/**
     * Decline shipment changes the quantity of unassigned value of an order line,
     * also it deletes the order line quantities and the shipment itself.
     *
     * Another thing this worker does is set to banned to all item locations related with this shipment,
     * that means the sourcing will not use the specific item locations to the next ship.
     *
	 * @param shipment - the shipment to decline
	 */
	void declineShipment(ShipmentData shipment);

	/**
	 * Re-Allocate a shipment
	 * 
	 * @param shipment - the shipment to reallocate
	 * @param location - the location to reallocate the shipment to
	 */
	void reallocateShipment(ShipmentData shipment, StockroomLocationData location);

	/**
	 * Split a shipment
	 * 
	 * @param shipment - the existing shipment from which we want to remove olqs to place into a new shipment
	 * @param olqs - list of olqs to be moved to a new shipment
	 * @return The new {@link ShipmentData} that resulted from the split
	 */
	ShipmentData splitShipmentByOlqs(ShipmentData shipment, List<OrderLineQuantityData> olqs);

	/**
	 * Split a shipment by orderline quantities and their quantity value.
	 * 
	 * @param shipment - The shipment to be split.
	 * @param olqQuantity - Map containing the orderline quantities to be split and their quantity value.
	 * @return the newly created shipment containing the data split out of the original shipment
	 */
	ShipmentData splitShipmentByOlqQuantities(ShipmentData shipment, Map<OrderLineQuantityData, Quantity> olqQuantity);

	/**
	 * Deletes a shipment
	 * 
	 * @param shipment - The shipment to be deleted
	 */
	void deleteShipment(ShipmentData shipment);

	/**
	 * Cancels a shipment
	 * 
	 * @param shipment
	 * 
	 * @deprecated - this method has no use any more.
	 */
	@Deprecated
	void cancelShipment(ShipmentData shipment);

	/**
	 * Finds all Shipments updated after a date.
	 *
	 *
	 * @param aDate the cut-off date
	 * @return shipments updated after a date
	 */
	public Page<ShipmentData> findAllShipmentsUpdatedAfter(final Date aDate);

}
