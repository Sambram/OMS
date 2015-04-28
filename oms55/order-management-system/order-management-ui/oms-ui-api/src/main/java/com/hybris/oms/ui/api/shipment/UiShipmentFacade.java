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
package com.hybris.oms.ui.api.shipment;

import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.shipment.ShipmentFacade;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.exception.InvalidOperationException;
import com.hybris.oms.domain.shipping.ShipmentQueryObject;

import java.util.List;


public interface UiShipmentFacade
{

	/**
	 * Gets the shipment by id.
	 * 
	 * @category OMS-UI
	 * 
	 * @param shipmentId the shipment id to fetch
	 * @return the {@link ShipmentDetail} by id
	 * @throws EntityNotFoundException - if the shipment id does not exist
	 */
	ShipmentDetail getShipmentDetailById(final String shipmentId) throws EntityNotFoundException;

	/**
	 * Searches for shipments regarding the criteria {@link ShipmentQueryObject} informed.
	 * available features.
	 * 
	 * @category OMS-UI
	 * 
	 * @param shipmentQueryObject - The shipment query object.
	 * 
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
	 * @return A list of ShipmentDetail
	 * @throws EntityValidationException - if preconditions are not met.
	 */
	Pageable<ShipmentDetail> findShipmentDetailsByQuery(ShipmentQueryObject shipmentQueryObject) throws EntityValidationException;

	/**
	 * Returns OLQ list as {@link OrderShipmentDetail} with OLQ,Shipment&Item Details for each Order.
	 * 
	 * @category OMS-UI
	 * 
	 * @param orderId the order id to fetch
	 * @param allLocationDisplay
	 * @return A list of OrderShipmentDetail
	 * @throws EntityNotFoundException if order shipment detail with the provided order id is not found
	 */
	List<OrderShipmentDetail> findOrderShipmentDetailsByOrderId(final String orderId, final boolean allLocationDisplay)
			throws EntityNotFoundException;

	/**
	 * Returns list of Shipment and Item details.
	 * 
	 * @category OMS-UI
	 * 
	 * @param shipmentId the shipment id to fetch
	 * @param allLocationDisplay
	 * @return A list of OrderShipmentDetail
	 * @throws EntityNotFoundException if order shipment detail with the provided id is not found
	 */
	List<OrderShipmentDetail> findOrderShipmentDetailsByShipmentId(final String shipmentId, final boolean allLocationDisplay)
			throws EntityNotFoundException;

	/**
	 * Searches for shipments regarding the criteria {@link ShipmentQueryObject} informed.
	 * available features.
	 * 
	 * @category OMS-UI
	 * 
	 * @param shipmentQueryObject - The shipment query object.
	 * 
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
	 * @return A list of {@link com.hybris.oms.ui.api.shipment.UIShipment}
	 * @throws EntityValidationException - if preconditions are not met.
	 */
	Pageable<UIShipment> findUIShipmentsByQuery(final ShipmentQueryObject shipmentQueryObject) throws EntityValidationException;

	/**
	 * Gets the UIShipment by id.
	 * 
	 * @category OMS-UI
	 * 
	 * @param shipmentId the shipment id to fetch
	 * @return the {@link com.hybris.oms.ui.api.shipment.UIShipment} by id
	 * @throws EntityNotFoundException the shipment not found exception
	 */
	UIShipment getUIShipmentById(final String shipmentId) throws EntityNotFoundException;

	/**
	 * Removes All OLQs from a shipment.
	 * 
	 * @category OMS-UI
	 * 
	 * @param shipmentId the shipment id to remove
	 * @throws EntityNotFoundException - if shipment with the provided id is not found
	 * @throws InvalidOperationException - if this operation cannot be performed from the shipment's current state
	 * 
	 * @deprecated Use {@link ShipmentFacade#declineShipment(String)} instead.
	 */
	@Deprecated
	void removeAllOrderLineQuantitiesFromShipment(final String shipmentId) throws EntityNotFoundException,
			InvalidOperationException;

	/**
	 * Removes an OLQ by id from a shipment.
	 * 
	 * @category OMS-UI
	 * 
	 * @param shipmentId - the shipment id of the olq to be removed
	 * @param olqId - the olq id to remove
	 * @throws EntityNotFoundException - if shipment with the provided id is not found
	 * @throws InvalidOperationException - if this operation cannot be performed from the shipment's current state
	 * 
	 * @deprecated Use combination of {@link ShipmentFacade#splitShipmentByOlqs(String, java.util.Set)} and
	 *             {@link ShipmentFacade#declineShipment(String)} instead.
	 */
	@Deprecated
	void removeOrderLineQuantityFromShipment(final String shipmentId, final String olqId) throws EntityNotFoundException,
			InvalidOperationException;

	/**
	 * Get a list of bins for a given shipment.
	 * 
	 * @category OMS-UI
	 * 
	 * @param shipmentId the shipment id to search
	 * @return PickSlipBinInfo {@link PickSlipBinInfo} aggregate Order lines and bins.
	 * @throws EntityNotFoundException if any pick slip bin info with the provided shipment id is not found
	 */
	PickSlipBinInfo getBinInfoForPickSlipByShipmentId(final String shipmentId) throws EntityNotFoundException;

}
