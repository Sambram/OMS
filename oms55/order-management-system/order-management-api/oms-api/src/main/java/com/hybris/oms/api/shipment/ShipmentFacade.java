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
package com.hybris.oms.api.shipment;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.hybris.oms.api.Pageable;
import com.hybris.oms.domain.BatchResult;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.exception.InvalidOperationException;
import com.hybris.oms.domain.exception.RemoteRequestException;
import com.hybris.oms.domain.remote.exception.InvalidShipmentLabelResponseException;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.domain.shipping.ShipmentDetails;
import com.hybris.oms.domain.shipping.ShipmentQueryObject;
import com.hybris.oms.domain.shipping.ShipmentSplitResult;


/**
 * The Interface ShipmentServices Api.
 */
@SuppressWarnings("PMD.TooManyPublicMethods")
public interface ShipmentFacade
{

	/**
	 * Confirm shipment. Updates all OLQS statuses in this shipment to "SHIPPED".
	 * 
	 * @param shipmentId the shipment id to confirm
	 * @return The updated shipment
	 * @throws EntityNotFoundException if shipment with the provided id is not found
	 * @throws InvalidOperationException - if this operation cannot be performed from the shipment's current state
	 * @category OMS-UI
	 */
	Shipment confirmShipment(final String shipmentId) throws EntityNotFoundException, InvalidOperationException;

	/**
	 * Searches for shipments regarding the criteria {@link ShipmentQueryObject} informed.
	 * 
	 * @param shipmentQueryObject - The shipment query object.
	 *           <p/>
	 *           <dt><b>Preconditions:</b>
	 *           <dd>
	 *           shipmentQueryObject.pageNumber must not be null.
	 *           <dd>
	 *           shipmentQueryObject.pageNumber must not be less than zero.
	 *           <dd>
	 *           shipmentQueryObject.pageSize must not be null.
	 *           <dd>
	 *           shipmentQueryObject.pageSize must not be greater than zero.
	 *           <dd>
	 *           shipmentQueryObject.pageSize must not be greater than max allowed page size.
	 * @return the list of pageable shipment
	 * @throws EntityValidationException if preconditions are not met.
	 * @category EXTERNAL
	 */
	Pageable<Shipment> findShipmentsByQuery(final ShipmentQueryObject shipmentQueryObject) throws EntityValidationException;

	/**
	 * Gets the shipment by id.
	 * 
	 * @param shipmentId the shipment id to fetch
	 * @return the shipment by id
	 * @throws EntityNotFoundException the shipment not found exception
	 * @category OMS-UI
	 */
	Shipment getShipmentById(final String shipmentId) throws EntityNotFoundException;

	/**
	 * Gets shipments by order id.
	 * 
	 * @param orderId the order id to fetch
	 * @return the shipment by order id
	 * @throws EntityNotFoundException the order not found exception
	 * @category OMS-UI
	 */
	Collection<Shipment> getShipmentsByOrderId(final String orderId) throws EntityNotFoundException;

	/**
	 * Get the shipping labels.
	 * 
	 * @param shipmentId the shipment id to fetch
	 * @return byte[] containing the label
	 * @throws RemoteRequestException if any communication problem with CIS occur
	 * @throws EntityNotFoundException if shipment with the provided id is not found
	 * @throws InvalidShipmentLabelResponseException if CIS did not return any labelUrl
	 * @category OMS-UI
	 */
	byte[] retrieveShippingLabelsByShipmentId(final String shipmentId) throws EntityNotFoundException,
			InvalidShipmentLabelResponseException, RemoteRequestException;

	/**
	 * Pack shipment. Updates all OLQS statuses in this shipment to "PACKED".
	 * 
	 * @param shipmentId the shipment id to pack
	 * @return The updated shipment
	 * @throws EntityNotFoundException the shipment not found exception
	 * @throws InvalidOperationException - if this operation cannot be performed from the shipment's current state
	 * @category OMS-UI
	 */
	Shipment packShipment(final String shipmentId) throws EntityNotFoundException, InvalidOperationException;

	/**
	 * Pick shipment. Updates all olqs' statuses in this shipment to "PICKED".
	 * 
	 * @param shipmentId the shipment id to pick
	 * @return The updated shipment
	 * @throws EntityNotFoundException the shipment not found exception
	 * @throws InvalidOperationException - if this operation cannot be performed from the shipment's current state
	 * @category OMS-UI
	 */
	Shipment pickShipment(final String shipmentId) throws EntityNotFoundException, InvalidOperationException;

	/**
	 * Update shipment details.
	 * 
	 * @param shipmentId the shipment id to update
	 * @param shipmentDetails the shipment details to update {@link ShipmentDetails}
	 * @throws EntityNotFoundException the shipment not found exception
	 * @category OMS-UI
	 */
	void updateShipmentDetails(final String shipmentId, final ShipmentDetails shipmentDetails) throws EntityNotFoundException;

	/**
	 * Update status for a given shipment.
	 * 
	 * @param shipmentId the shipment id to update
	 * @param statusCode the new status code to assign
	 * @return the updated shipment
	 * @throws EntityNotFoundException if shipment with the provided id is not found
	 * @throws EntityValidationException if the statusCode provided does not exist
	 * @category EXTERNAL
	 */
	Shipment updateShipmentStatus(final String shipmentId, final String statusCode) throws EntityNotFoundException,
			EntityValidationException;

	/**
	 * Cancel shipment. Updates all OLQS statuses in this shipment to "CANCELLED".
	 * 
	 * @param shipmentId the shipment id to cancel
	 * @return The updated shipment
	 * @throws EntityNotFoundException - if shipment with the provided id is not found
	 * @throws InvalidOperationException - if this operation cannot be performed from the shipment's current state
	 * @category OMS-UI
	 */
	Shipment cancelShipment(final String shipmentId) throws EntityNotFoundException, InvalidOperationException;

	/**
	 * Decline the specified shipment.
	 * 
	 * @param shipmentId - the id of the shipment to decline
	 * @throws EntityNotFoundException - if the shipment with the provided id is not found
	 * @throws InvalidOperationException - if this operation cannot be performed from the shipment's current state
	 */
	void declineShipment(final String shipmentId) throws EntityNotFoundException, InvalidOperationException;

	/**
	 * Reallocate the specified shipment to the specified location
	 * 
	 * @param shipmentId - the id of the shipment to reallocate
	 * @param locationId - the id of the stockroom location to reallocate this shipment to
	 * @return The updated shipment
	 * @throws EntityNotFoundException - if the shipment with the provided id is not found
	 *            - if the location with the provided id is not found
	 * @throws InvalidOperationException - if this operation cannot be performed from the shipment's current state
	 */
	Shipment reallocateShipment(final String shipmentId, final String locationId) throws EntityNotFoundException,
			InvalidOperationException;

	/**
	 * Move the indicated OLQs out of the indicated shipment and into a new shipment.
	 * 
	 * @param shipmentId - the existing shipment from which we want to remove olqs to place into a new shipment
	 * @param olqIds - set of olq ids to be moved to a new shipment
	 * @return The new shipment that contains the olqs provided
	 * @throws EntityNotFoundException - occurs if the shipment is not found
	 *            - occurs if one of the olqs is not found
	 * @throws EntityValidationException - occurs if one the olqs does not belong to the shipment provided
	 * @throws InvalidOperationException - occurs if this operation cannot be performed from the shipment's current state
	 */
	ShipmentSplitResult splitShipmentByOlqs(final String shipmentId, final Set<String> olqIds) throws EntityNotFoundException,
			EntityValidationException, InvalidOperationException;

	/**
	 * Split a shipment by orderline quantities and their quantity value.
	 * 
	 * @param shipmentId - The shipment to be split.
	 * @param olqIdQuantityValueMap - A container which holds a map of orderline quantities to be split and their
	 *           quantity value.
	 * @return A list of shipments resulted from the split {@link Shipment}.
	 * @throws EntityNotFoundException
	 *            - occurs if the shipment is not found
	 *            - occurs if one of the olqs is not found
	 * @throws EntityValidationException - occurs if one the olqs does not belong to the shipment provided
	 * @throws InvalidOperationException - occurs if this operation cannot be performed from the shipment's current state
	 */
	ShipmentSplitResult splitShipmentByOlqQuantities(final String shipmentId, final Map<String, Integer> olqIdQuantityValueMap)
			throws EntityNotFoundException, EntityValidationException, InvalidOperationException;

	/**
	 * Decline Shipments by shipment ids.
	 * 
	 * @param shipmentIds - A set of shipment ids to be declined.
	 * @return A BatchResult object {@link com.hybris.oms.domain.BatchResult} containing a list of
	 *         {@link com.hybris.oms.domain.ProcessedItem} and {@link com.hybris.oms.domain.FailedProcessedItem}.
	 * @throws EntityNotFoundException - If no shipment for shipment id is not found
	 */
	BatchResult declineShipments(final Set<String> shipmentIds) throws EntityNotFoundException, InvalidOperationException;

	/**
	 * Cancel Shipments by shipment ids.
	 * 
	 * @param shipmentIds - A set of shipment ids to be cancelled.
	 * @return A BatchResult object {@link com.hybris.oms.domain.BatchResult} containing a list of
	 *         {@link com.hybris.oms.domain.ProcessedItem} and {@link com.hybris.oms.domain.FailedProcessedItem}.
	 * @throws EntityNotFoundException - If no shipment for shipment id is not found
	 */
	BatchResult cancelShipments(final Set<String> shipmentIds) throws EntityNotFoundException, InvalidOperationException;

	/**
	 * Confirm Shipments by shipment ids.
	 * 
	 * @param shipmentIds - A set of shipment ids to be confirmed.
	 * @return A BatchResult object {@link com.hybris.oms.domain.BatchResult} containing a list of
	 *         {@link com.hybris.oms.domain.ProcessedItem} and {@link com.hybris.oms.domain.FailedProcessedItem}.
	 * @throws EntityNotFoundException - If no shipment for shipment id is not found
	 */
	BatchResult confirmShipments(final Set<String> shipmentIds) throws EntityNotFoundException, InvalidOperationException;


	/**
	 * Manual Capture the shipment with the given ID. Manual Capture will assume that a manual payment outside of the oms
	 * system has been arranged. manual capture will jump to tax invoice step.
	 * 
	 * 
	 * @param shipmentId
	 *           The shipment to be manually captured
	 * 
	 * @return A Shipment object {@link com.hybris.oms.domain.BatchResult} containing the shipment object
	 * @throws EntityNotFoundException
	 *            - if shipment was not found
	 * @throws com.hybris.oms.domain.exception.InvalidOperationException
	 *            - if the shipment is not in a supported state. its workflow does not allow it.
	 **/

	Shipment manualCapture(String shipmentId) throws EntityNotFoundException, InvalidOperationException;
}
