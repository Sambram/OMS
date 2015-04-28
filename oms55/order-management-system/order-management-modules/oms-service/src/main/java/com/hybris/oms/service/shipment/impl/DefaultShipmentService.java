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
package com.hybris.oms.service.shipment.impl;

import com.google.common.base.Preconditions;
import com.hybris.kernel.api.Page;
import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.domain.remote.exception.InvalidShipmentLabelResponseException;
import com.hybris.oms.domain.shipping.ShipmentQueryObject;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.service.cis.CisService;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.order.*;
import com.hybris.oms.service.managedobjects.preference.TenantPreferenceData;
import com.hybris.oms.service.managedobjects.shipment.DeliveryData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.managedobjects.shipment.ShippingAndHandlingData;
import com.hybris.oms.service.managedobjects.types.AmountVT;
import com.hybris.oms.service.managedobjects.types.PriceVT;
import com.hybris.oms.service.managedobjects.types.QuantityVT;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.order.impl.OrderDataStaticUtils;
import com.hybris.oms.service.preference.TenantPreferenceService;
import com.hybris.oms.service.service.AbstractHybrisService;
import com.hybris.oms.service.shipment.ShipmentService;
import com.hybris.oms.service.shipment.strategy.OlqGroupingStrategy;
import com.hybris.oms.service.workflow.UserTaskForm;
import com.hybris.oms.service.workflow.executor.WorkflowExecutor;
import com.hybris.oms.service.workflow.executor.shipment.ShipmentAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.*;

import static com.hybris.oms.service.workflow.WorkflowConstants.STATE_QUEUED;


/**
 * Default implementation of {@link ShipmentService}.
 */
public class DefaultShipmentService extends AbstractHybrisService implements ShipmentService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultShipmentService.class);
	private static final String PICKUP_METHOD = "pickup";
	private InventoryService inventoryService;
	private OrderService orderService;
	private ShipmentQueryFactory shipmentQueryFactory;
	private CisService cisService;
	private OlqGroupingStrategy olqGroupingStrategy;
	private WorkflowExecutor<ShipmentData> shipmentWorkflowExecutor;
	private TenantPreferenceService tenantPreferenceService;
	private int shipmentPollPageSize;

	@Override
	public List<ShipmentData> findAllShipmentsByOrderId(final String orderId)
	{
		LOGGER.trace("findAllShipmentsByOrderId");

		List<ShipmentData> shipments;
		try
		{
			final OrderData orderData = this.orderService.getOrderByOrderId(orderId);

			shipments = this.findAll(this.shipmentQueryFactory.findAllShipmentsByOrder(orderData));
		}
		catch (final EntityNotFoundException | IllegalArgumentException ignored)
		{
			if (LOGGER.isTraceEnabled())
			{
				LOGGER.trace("kernel thrown " + ignored.getMessage());
			}
			// Kernel throws an Illegal Argument Exception when the IN operator does not have any arguments.
			// OrderService throws an Order Not Found Exception if the order id does not exists. But it is not this
			// method's responsibility to also throw this exception.
			shipments = new ArrayList<>();
		}

		return shipments;
	}

	@Override
	public List<ShipmentData> findShipmentsByOrder(final OrderData order)
	{
		LOGGER.trace("findAllShipmentsByOrderId");

		List<ShipmentData> shipments = new ArrayList<>();
		try
		{
			shipments = this.findAll(this.shipmentQueryFactory.findAllShipmentsByOrder(order));
		}
		catch (final IllegalArgumentException ignored)
		{
			if (LOGGER.isTraceEnabled())
			{
				LOGGER.trace("kernel thrown " + ignored.getMessage());
			}

		}

		return shipments;
	}

	@Override
	public Page<ShipmentData> findPagedShipmentsByQuery(final ShipmentQueryObject shipmentQueryObject)
	{
		LOGGER.trace("findPagedShipmentsByQuery");
		final int[] pageNumberAndSize = this.getPageNumberAndSize(shipmentQueryObject, 0, this.getQueryPageSizeDefault());

		return this.findPaged(this.shipmentQueryFactory.findShipmentsByQuery(shipmentQueryObject), pageNumberAndSize[0],
				pageNumberAndSize[1]);
	}

	@Override
	public ShipmentData getShipmentById(final Long shipmentId)
	{
		LOGGER.trace("getShipmentById");
		Preconditions.checkNotNull(shipmentId, "Shipment id cannot be null.");

		try
		{
			return this.findOneSingle(this.shipmentQueryFactory.getShipmentById(shipmentId));
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException("Shipment not found. Shipment Id: " + shipmentId, e);
		}
	}

	@Override
	public void checkIfShipmentWorkflowAlreadyStarted(final ShipmentData shipment)
	{
		if (shipment.getState() != null && !STATE_QUEUED.equals(shipment.getState()))
		{
			throw new IllegalStateException(String.format(
					"Cannot perform requested action, shipment[%s] workflow process already started.", shipment.getShipmentId()));
		}
	}

	@Override
	public ShipmentData updateShipmentStatus(final ShipmentData shipment, final String statusTenantPreferenceKey)
	{
		final OrderLineQuantityStatusData olqStatus = this.orderService
				.getOrderLineQuantityStatusByTenantPreferenceKey(statusTenantPreferenceKey);
		this.updateShipmentStatus(shipment, olqStatus);
		return shipment;
	}

	@Override
	public ShipmentData updateShipmentStatus(final ShipmentData shipment, final OrderLineQuantityStatusData olqStatus)
	{
		if (olqStatus == null)
		{
			throw new IllegalArgumentException("Order line quantity status cannot be null.");
		}
		shipment.setOlqsStatus(olqStatus.getStatusCode());
		final List<Long> olqIds = OrderDataStaticUtils.getOlqIdsForShipment(shipment);
		if (!olqIds.isEmpty())
		{
			for (final OrderLineQuantityData olq : this.orderService.getOrderLineQuantitiesByOlqIds(olqIds))
			{
				orderService.updateOrderLineQuantityStatus(olq, olqStatus);
			}
		}
		return shipment;
	}

	@Override
	public List<ShipmentData> createShipmentsByOrderId(final String orderId)
	{
		final OrderData order = this.orderService.getOrderByOrderId(orderId);
		return createShipmentsForOrder(order);
	}

	@Override
	public List<ShipmentData> createShipmentsForOrder(final OrderData order)
	{
		final List<ShipmentData> shipments = new ArrayList<>();

		// Check to see if this order has order line quantities that were not assigned to shipments
		final List<OrderLineQuantityData> olqs = OrderDataStaticUtils.getAllOrderLineQuantitiesUnassignedToShipments(order);

		if (!olqs.isEmpty())
		{
			shipments.addAll(this.createShipmentsByOLQs(olqs));
		}

		// Start shipment workflows
		for (final ShipmentData shipment : shipments)
		{
			this.shipmentWorkflowExecutor.execute(shipment);
		}
		return shipments;
	}

	@Override
	public List<ShipmentData> reallocateOlqsToNewShipmentsAndDeleteInitialShipment(final Map<Long, String> reallocatedOlqs,
			final ShipmentData shipmentData)
			{
		this.checkIfShipmentWorkflowAlreadyStarted(shipmentData);

		final OrderLineQuantityStatusData olqStatusData = this.orderService
				.getOrderLineQuantityStatusByTenantPreferenceKey(TenantPreferenceConstants.PREF_KEY_OLQSTATUS_ALLOCATED);
		final List<OrderLineQuantityData> olqs = this.orderService.getOrderLineQuantitiesByOlqIds(OrderDataStaticUtils
				.getOlqIdsForShipment(shipmentData));
		for (final OrderLineQuantityData olq : olqs)
		{
			olq.setShipment(null);
			final String reallocLocId = reallocatedOlqs.get(olq.getOlqId());
			if (reallocLocId != null)
			{
				orderService.updateOrderLineQuantityStatus(olq, olqStatusData);
				olq.setStockroomLocationId(reallocLocId);
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug("Changing OLQ id {} status to {} and location id to {}.", new Object[] { olq.getOlqId(),
							olq.getStatus().getStatusCode(), olq.getStockroomLocationId() });
				}
			}
		}
		this.deleteShipment(shipmentData);
		if (LOGGER.isDebugEnabled())
		{
			for (final OrderLineQuantityData olq : olqs)
			{
				LOGGER.debug("Create shipment for OLQ {}: locId={},status={}",
						new Object[] { olq.getOlqId(), olq.getStockroomLocationId(), olq.getStatus().getStatusCode() });
			}
		}
		final List<ShipmentData> shipments = this.createShipmentsByOLQs(olqs);

		// Start shipment workflow
		for (final ShipmentData shipment : shipments)
		{
			this.shipmentWorkflowExecutor.execute(shipment);
		}
		return shipments;
			}

	@Override
	public ShipmentData capturePayment(final ShipmentData shipment)
	{
		lockShippingAndHandling(shipment);
		setFirstShipmentIdIfUndefined(shipment);

		this.cisService.capturePayment(shipment);

		final ShipmentData updatedShipment = this.updateShipmentStatus(shipment,
				TenantPreferenceConstants.PREF_KEY_OLQSTATUS_PAYMENT_CAPTURED);
		return updatedShipment;
	}

	@Override
	public byte[] getShippingLabels(final ShipmentData shipmentData)
	{
		// If the label url is not in OMS, call to create it and store it
		if (shipmentData.getDelivery().getLabelUrl() == null)
		{
			final String labelUrl = this.cisService.createShipmentLabel(shipmentData);

			if (labelUrl != null)
			{
				shipmentData.getDelivery().setLabelUrl(labelUrl);
			}
			else
			{
				throw new InvalidShipmentLabelResponseException("CIS did not return any labelUrl");
			}
		}

		return this.cisService.getShipmentLabel(shipmentData);
	}

	@Override
	public ShipmentData invoiceTaxes(final ShipmentData shipment)
	{
		lockShippingAndHandling(shipment);
		setFirstShipmentIdIfUndefined(shipment);

		this.cisService.invoiceTaxes(shipment);

		final ShipmentData updatedShipment = this.updateShipmentStatus(shipment,
				TenantPreferenceConstants.PREF_KEY_OLQSTATUS_TAX_INVOICED);
		return updatedShipment;
	}

	/**
	 * Create a shipments from a list of OLQs.
	 *
	 * @param olqs line quantities list
	 * @return List of ShipmentData
	 */
	@Override
	public List<ShipmentData> createShipmentsByOLQs(final List<OrderLineQuantityData> olqs)
	{
		LOGGER.trace("Create shipments by order line quantities");

		final Iterator<OrderLineQuantityData> olqIterator = olqs.iterator();
		while (olqIterator.hasNext())
		{
			final OrderLineQuantityData olq = olqIterator.next();
			final ShipmentData shipment = olq.getShipment();
			if (shipment != null)
			{
				throw new IllegalArgumentException("OLQ Id: " + olq.getOlqId() + " assigned to Shipment Id: "
						+ shipment.getShipmentId() + " already");
			}
		}

		final List<List<OrderLineQuantityData>> groupedOLQs = olqGroupingStrategy.groupOlqs(olqs);
		final List<ShipmentData> shipments = new ArrayList<>();
		final OrderData order = olqs.get(0).getOrderLine().getMyOrder();

		for (final List<OrderLineQuantityData> olqGroup : groupedOLQs)
		{
			// Get the location at which the olq's are allocated to obtain the address.
			final StockroomLocationData location = this.inventoryService.getLocationByLocationId(olqGroup.get(0)
					.getStockroomLocationId());
			final ShipmentData shipment = this.createNewShipment(order, olqGroup, location);

			if (location.getAddress() == null)
			{
				LOGGER.debug("The location {} has no address", location.getLocationId());
			}

			shipments.add(shipment);

			// Set shipment status to "ALLOCATED"
			updateShipmentStatus(shipment, TenantPreferenceConstants.PREF_KEY_OLQSTATUS_ALLOCATED);
		}
		return shipments;
	}

	@Override
	public ShipmentData computeShipmentPrices(final ShipmentData shipment)
	{
		final List<OrderLineQuantityData> olqs = ShipmentDataStaticUtils.getShipmentOrderLineQuantities(shipment);

		if (!olqs.isEmpty())
		{
			final QuantityVT calculateTotalGoodsItemQuantity = ShipmentDataStaticUtils.calculateTotalGoodsItemQuantity(olqs);
			shipment.setTotalGoodsItemQuantityUnitCode(calculateTotalGoodsItemQuantity.getUnitCode());
			shipment.setTotalGoodsItemQuantityValue(calculateTotalGoodsItemQuantity.getValue());
			final AmountVT subTotal = ShipmentDataStaticUtils.calculateShipmentSubtotalAmount(olqs);
			final AmountVT tax = ShipmentDataStaticUtils.calculateShipmentTaxAmount(olqs);
			final PriceVT merchandisePrice = new PriceVT(subTotal.getCurrencyCode(), subTotal.getValue(), tax.getCurrencyCode(),
					tax.getValue(), null, 0.0d);
			shipment.setMerchandisePrice(merchandisePrice);
		}
		else
		{
			shipment.setTotalGoodsItemQuantityUnitCode(null);
			shipment.setTotalGoodsItemQuantityValue(0);
			final PriceVT merchandisePrice = new PriceVT(null, 0.0d, null, 0.0d, null, 0.0d);
			shipment.setMerchandisePrice(merchandisePrice);
		}

		return shipment;
	}

	@Override
	public void declineShipment(final ShipmentData shipment)
	{
		LOGGER.debug("Decline shipment {}", shipment.getShipmentId());

		final List<OrderLineQuantityData> shipmentOlqs = ShipmentDataStaticUtils.getShipmentOrderLineQuantities(shipment);
		for (final OrderLineQuantityData olq : shipmentOlqs)
		{
			final OrderLineData parentOrderLine = olq.getOrderLine();
			final int parentOrderLineQuantityUnassigned = parentOrderLine.getQuantityUnassignedValue();
			parentOrderLine.setQuantityUnassignedValue(olq.getQuantityValue() + parentOrderLineQuantityUnassigned);
		}

		orderService.deleteOrderLineQuantities(shipmentOlqs);
		deleteShipment(shipment);
	}

	@Override
	@Deprecated
	public void cancelShipment(final ShipmentData shipment)
	{
		final UserTaskForm form = new UserTaskForm();
		form.putAction(ShipmentAction.CANCEL.name());
		this.shipmentWorkflowExecutor.completeUserTask(shipment, form);
	}

	@Override
	public void reallocateShipment(final ShipmentData shipment, final StockroomLocationData location)
	{
		LOGGER.debug("Re-Allocate shipment {} to location {}", shipment.getShipmentId(), location.getLocationId());

		this.checkIfShipmentWorkflowAlreadyStarted(shipment);
		this.updateShipmentStatus(shipment, TenantPreferenceConstants.PREF_KEY_OLQSTATUS_ALLOCATED);
		shipment.setStockroomLocationId(location.getLocationId());

		// Update the location id for each OLQ in the shipment
		final List<OrderLineQuantityData> olqs = ShipmentDataStaticUtils.getShipmentOrderLineQuantities(shipment);
		for (final OrderLineQuantityData olq : olqs)
		{
			olq.setStockroomLocationId(location.getLocationId());
		}
	}

	@Override
	public ShipmentData splitShipmentByOlqs(final ShipmentData shipment, final List<OrderLineQuantityData> olqs)
	{
		LOGGER.debug("Splitting shipment {} by olqs", shipment.getShipmentId());

		for (final OrderLineQuantityData olq : olqs)
		{
			LOGGER.trace("Removing order line quantity {} from shipment {}", olq.getOlqId(), shipment.getShipmentId());
			olq.setShipment(null);
		}
		computeShipmentPrices(shipment);
		return assignOlqsToNewShipment(shipment, olqs);
	}

	@Override
	public ShipmentData splitShipmentByOlqQuantities(final ShipmentData shipment,
			final Map<OrderLineQuantityData, Quantity> olqQuantity)
	{
		final List<OrderLineQuantityData> newOlqs = new ArrayList<>();
		LOGGER.debug("Splitting and confirming shipment {} by olq and quantity", shipment.getShipmentId());

		for (final Map.Entry<OrderLineQuantityData, Quantity> entry : olqQuantity.entrySet())
		{
			final OrderLineQuantityData newOlq = this.splitOlq(entry.getKey(), entry.getValue());
			newOlqs.add(newOlq);

			// If original olq is now empty, then delete it
			if (entry.getKey().getQuantityValue() == 0)
			{
				orderService.deleteOrderLineQuantities(Collections.singletonList(entry.getKey()));
			}
		}
		computeShipmentPrices(shipment);
		return assignOlqsToNewShipment(shipment, newOlqs);
	}

	/**
	 * Assigns olqs to a new shipment and computes the new shipment's totals.
	 *
	 * @param originalShipment - the shipment that the new shipment is being split from
	 * @param olqs             - the olqs to assign to a new shipment
	 * @return the new shipment
	 */
	protected ShipmentData assignOlqsToNewShipment(final ShipmentData originalShipment, final List<OrderLineQuantityData> olqs)
	{
		final List<ShipmentData> newShipments = this.createShipmentsByOLQs(olqs);
		Preconditions.checkArgument(newShipments.size() == 1, "Splitting shipments cannot produce more than 1 shipment.");
		final ShipmentData newShipment = newShipments.get(0);
		newShipment.setOlqsStatus(originalShipment.getOlqsStatus());
		newShipment.setOriginalShipmentId(originalShipment.getShipmentId());
		this.computeShipmentPrices(newShipment);

		// Start shipment workflow
		flush();
		this.shipmentWorkflowExecutor.execute(newShipment);
		return newShipment;
	}

	/**
	 * Split an olq's quantity out into a new olq.
	 *
	 * @param olq
	 * @param quantity
	 * @return the new olq containing the splitted quantity
	 */
	protected OrderLineQuantityData splitOlq(final OrderLineQuantityData olq, final Quantity quantity)
	{
		LOGGER.debug("Splitting olq {} quantity {}", olq.getOlqId(), quantity);

		// Decrement olq by specified quantity
		if (olq.getQuantityValue() - quantity.getValue() < 0)
		{
			throw new EntityValidationException(String.format(
					"Olq \"%s\" quantity \"%s\" requested for split exceeds available quantity \"%s\" ", olq.getOlqId(),
					quantity.getValue(), olq.getQuantityValue()));
		}
		else
		{
			olq.setQuantityValue(olq.getQuantityValue() - quantity.getValue());
		}

		// Create new Olq with specified quantity
		final OrderLineQuantityData newOlq = this.getPersistenceManager().create(OrderLineQuantityData.class);
		newOlq.setOrderLine(olq.getOrderLine());
		newOlq.setQuantityUnitCode(quantity.getUnitCode());
		newOlq.setQuantityValue(quantity.getValue());
		newOlq.setStatus(olq.getStatus());
		newOlq.setStockroomLocationId(olq.getStockroomLocationId());

		// Assign new olq to same orderline as original olq
		olq.getOrderLine().getOrderLineQuantities().add(newOlq);
		flush();

		return newOlq;
	}

	@Override
	public void deleteShipment(final ShipmentData shipment)
	{
		try
		{
			this.getPersistenceManager().remove(shipment.getId());
		}
		catch (final IllegalArgumentException | ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException("Shipment not found. Hybris ID: " + shipment.getId(), e);
		}
	}

	protected ShipmentData createNewShipment(final OrderData order, final List<OrderLineQuantityData> olqGroup,
			final StockroomLocationData location)
	{
		// Create a new shipment and set its attributes
		final ShipmentData shipment = this.getPersistenceManager().create(ShipmentData.class);
		shipment.setCurrencyCode(order.getCurrencyCode());
		shipment.setShipFrom(location.getAddress());
		shipment.setOrderFk(order);
		shipment.setStockroomLocationId(location.getLocationId());
		shipment.setPriorityLevelCode(order.getPriorityLevelCode());
		shipment.setShippingMethod(olqGroup.get(0).getOrderLine().getPickupStoreId() == null ? order.getShippingMethod()
				: PICKUP_METHOD);
		shipment.setTaxCategory(olqGroup.get(0).getOrderLine().getTaxCategory());
		shipment.setFirstArrivalStockroomLocationId(location.getLocationId());
		shipment.setLastExitStockroomLocationId(location.getLocationId());
		shipment.setShippingAndHandling(order.getShippingAndHandling());

		// Assign every olq in the group to the same shipment.
		// Ensure the shipment has a value for it's "olqStatus" attribute, if there's status on olqs we iterate over.
		for (final OrderLineQuantityData olq : olqGroup)
		{
			if (shipment.getOlqsStatus() == null && olq.getStatus() != null)
			{
				shipment.setOlqsStatus(olq.getStatus().getStatusCode());
			}

			olq.setShipment(shipment);
		}

		// Add the authorization urls
		final List<String> authorizationUrls = new ArrayList<>();
		for (final PaymentInfoData paymentInfoData : order.getPaymentInfos())
		{
			authorizationUrls.add(paymentInfoData.getAuthUrl());
		}
		shipment.setAuthUrls(authorizationUrls);

		// Sets delivery data
		final DeliveryData delivery = this.getPersistenceManager().create(DeliveryData.class);
		delivery.setDeliveryAddress(order.getShippingAddress());
		shipment.setDelivery(delivery);

		// Set the merchandise price. Calculate subtotal and tax amounts.
		this.computeShipmentPrices(shipment);

		// BOPIS
		shipment.setPickupInStore(olqGroup.get(0).getOrderLine().getPickupStoreId() != null);

		this.supplyShipmentDetailsDefaults(shipment);
		flush();
		return shipment;
	}

	protected void supplyShipmentDetailsDefaults(final ShipmentData shipment)
	{
		final TenantPreferenceData grossWeightUnitCodePref = this.tenantPreferenceService
				.getOptionalTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_SHIPMENT_PROPERTY_GROSS_WEIGHT_UNIT_CODE);
		final TenantPreferenceData grossWeightValuePref = this.tenantPreferenceService
				.getOptionalTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_SHIPMENT_PROPERTY_GROSS_WEIGHT_VALUE);
		final TenantPreferenceData heightUnitCodePref = this.tenantPreferenceService
				.getOptionalTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_SHIPMENT_PROPERTY_HEIGHT_UNIT_CODE);
		final TenantPreferenceData heightValuePref = this.tenantPreferenceService
				.getOptionalTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_SHIPMENT_PROPERTY_HEIGHT_VALUE);
		final TenantPreferenceData widthValuePref = this.tenantPreferenceService
				.getOptionalTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_SHIPMENT_PROPERTY_WIDTH_VALUE);
		final TenantPreferenceData lengthValuePref = this.tenantPreferenceService
				.getOptionalTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_SHIPMENT_PROPERTY_LENGTH_VALUE);
		final TenantPreferenceData descriptionPref = this.tenantPreferenceService
				.getOptionalTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_SHIPMENT_PROPERTY_DESCRIPTION);
		final TenantPreferenceData insuranceValueAmountValuePref = this.tenantPreferenceService
				.getOptionalTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_SHIPMENT_PROPERTY_INSURANCE_VALUE_AMOUNT_VALUE);

		shipment.setInsuranceValueAmountValue(Double.parseDouble((insuranceValueAmountValuePref == null) ? "0.0d"
				: insuranceValueAmountValuePref.getValue()));
		shipment.setWidthValue(Float.parseFloat((widthValuePref == null) ? "0.0f" : widthValuePref.getValue()));
		shipment.setLengthValue(Float.parseFloat((lengthValuePref == null) ? "0.0f" : lengthValuePref.getValue()));
		shipment.setHeightValue(Float.parseFloat((heightValuePref == null) ? "0.0f" : heightValuePref.getValue()));
		shipment.setHeightUnitCode((heightUnitCodePref == null) ? "" : heightUnitCodePref.getValue());
		shipment.setGrossWeightValue(Float.parseFloat((grossWeightValuePref == null) ? "0.0f" : grossWeightValuePref.getValue()));
		shipment.setGrossWeightUnitCode((grossWeightUnitCodePref == null) ? "" : grossWeightUnitCodePref.getValue());
		shipment.setPackageDescription((descriptionPref == null) ? "" : descriptionPref.getValue());
	}

	/**
	 * Sets a lock on {@link ShippingAndHandlingData}. This will raise optimistic locking failures in other parallel
	 * processes before calling CIS methods which should be only called once (e.g. capture payment, tax invoice).
	 */
	public void lockShippingAndHandling(final ShipmentData shipment)
	{
		final ShippingAndHandlingData lock = shipment.getShippingAndHandling();
		// this will touch the ManagedObject to make sure a new version is written
		lock.setFirstShipmentId(lock.getFirstShipmentId());
		this.flush();
	}

	public void setFirstShipmentIdIfUndefined(final ShipmentData shipment)
	{
		if (shipment.getShippingAndHandling() != null && shipment.getShippingAndHandling().getFirstShipmentId() == null
				&& !shipment.isPickupInStore())
		{
			shipment.getShippingAndHandling().setFirstShipmentId(Long.toString(shipment.getShipmentId()));
		}
	}

	@Override
	public Page<ShipmentData> findAllShipmentsUpdatedAfter(final Date aDate)
	{
		return findPaged(this.shipmentQueryFactory.getShipmentsByModifiedTimeGreaterThanQuery(aDate), 0, shipmentPollPageSize);
	}


	protected InventoryService getInventoryService()
	{
		return inventoryService;
	}

	@Required
	public void setInventoryService(final InventoryService inventoryService)
	{
		this.inventoryService = inventoryService;
	}

	protected OrderService getOrderService()
	{
		return orderService;
	}

	@Required
	public void setOrderService(final OrderService orderService)
	{
		this.orderService = orderService;
	}

	protected ShipmentQueryFactory getShipmentQueryFactory()
	{
		return shipmentQueryFactory;
	}

	@Required
	public void setShipmentQueryFactory(final ShipmentQueryFactory shipmentQueryFactory)
	{
		this.shipmentQueryFactory = shipmentQueryFactory;
	}

	public CisService getCisService()
	{
		return cisService;
	}

	@Required
	public void setCisService(final CisService cisService)
	{
		this.cisService = cisService;
	}

	protected OlqGroupingStrategy getOlqGroupingStrategy()
	{
		return olqGroupingStrategy;
	}

	@Required
	public void setOlqGroupingStrategy(final OlqGroupingStrategy olqGroupingStrategy)
	{
		this.olqGroupingStrategy = olqGroupingStrategy;
	}

	protected WorkflowExecutor<ShipmentData> getShipmentWorkflowExecutor()
	{
		return shipmentWorkflowExecutor;
	}

	@Required
	public void setShipmentWorkflowExecutor(final WorkflowExecutor<ShipmentData> shipmentWorkflowExecutor)
	{
		this.shipmentWorkflowExecutor = shipmentWorkflowExecutor;
	}

	protected TenantPreferenceService getTenantPreferenceService()
	{
		return tenantPreferenceService;
	}

	@Required
	public void setTenantPreferenceService(final TenantPreferenceService tenantPreferenceService)
	{
		this.tenantPreferenceService = tenantPreferenceService;
	}

	public int getShipmentPollPageSize()
	{
		return shipmentPollPageSize;
	}

	public void setShipmentPollPageSize(int shipmentPollPageSize)
	{
		this.shipmentPollPageSize = shipmentPollPageSize;
	}
}
