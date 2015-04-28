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

package com.hybris.oms.facade.order;

import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.Populator;
import com.hybris.commons.conversion.util.Converters;
import com.hybris.kernel.api.Page;
import com.hybris.kernel.api.exceptions.PrimaryKeyViolationException;
import com.hybris.kernel.api.exceptions.ValidationException;
import com.hybris.oms.api.order.OrderFacade;
import com.hybris.oms.domain.QueryObject;
import com.hybris.oms.domain.exception.DuplicateEntityException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.exception.InvalidOperationException;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.order.OrderLineQuantityStatus;
import com.hybris.oms.domain.order.UpdatedSinceList;
import com.hybris.oms.facade.validation.FailureHandler;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.shipment.ShipmentService;
import com.hybris.oms.service.sourcing.SourcingService;
import com.hybris.oms.service.util.ValidationUtils;
import com.hybris.oms.service.workflow.UserTaskForm;
import com.hybris.oms.service.workflow.executor.WorkflowExecutor;
import com.hybris.oms.service.workflow.executor.order.OrderAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.hybris.oms.service.workflow.WorkflowConstants.FULFILL_INCOMPLETE_USER_TASK;
import static com.hybris.oms.service.workflow.WorkflowConstants.STATE_ORDER_NEW;


/**
 * Default implementation of {@link OrderFacade}.
 */
@Transactional
@SuppressWarnings("PMD.ExcessiveImports")
public class DefaultOrderFacade implements OrderFacade
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultOrderFacade.class);
	private OrderService orderService;
    private ShipmentService shipmentService;
	private Validator<QueryObject<?>> queryObjectValidator;
	private SourcingService sourcingService;
	private Converter<OrderLineQuantityStatusData, OrderLineQuantityStatus> orderLineQuantityStatusConverter;
	private Converter<OrderLineQuantityStatus, OrderLineQuantityStatusData> orderLineQuantityStatusReverseConverter;
	private Populator<OrderLineQuantityStatus, OrderLineQuantityStatusData> orderLineQuantityStatusReversePopulator;
	private Converter<OrderData, Order> orderConverter;
	private Converter<Order, OrderData> orderReverseConverter;
	private Converter<OrderLineData, OrderLine> orderLineConverter;
	private Converters converters;
	private Validator<Order> orderValidator;
	private Validator<OrderLineQuantityStatus> orderLineQuantityStatusValidator;
	private FailureHandler entityValidationHandler;
	private WorkflowExecutor<OrderData> orderWorkflowExecutor;

	@Value("${oms.facade.disableOrderValidation}")
	private boolean disableOrderValidation;

	@Override
	public Order createOrder(final Order order)
	{
		LOG.trace("createOrder");

		try
		{
			if (!this.disableOrderValidation)
			{
				this.orderValidator.validate("Order", order, this.entityValidationHandler);
			}
			final OrderData orderData = this.orderReverseConverter.convert(order);

			this.mapOrderFk(orderData);
			this.orderService.flush();

			this.orderWorkflowExecutor.execute(orderData);
			order.setState(STATE_ORDER_NEW);

			return this.orderConverter.convert(orderData);
		}
		catch (final PrimaryKeyViolationException e)
		{
			LOG.debug("Duplicate Key Exception during order creation", e);
			throw new DuplicateEntityException("Order with order id " + order.getOrderId() + " already exists.", e);
		}
		catch (final ValidationException e)
		{
			LOG.debug("Validation Exception during order creation", e);
			throw new EntityValidationException(ValidationUtils.getValidationMessage(e.getConstraintViolations()), e);
		}
	}

	@Override
	public void fulfill(final String orderId)
	{
		LOG.trace("fulfill");
		final OrderData order = orderService.getOrderByOrderId(orderId);

		final UserTaskForm form = new UserTaskForm();
		form.putAction(OrderAction.FULFILL.name());
		form.addTaskDefinitionKey(FULFILL_INCOMPLETE_USER_TASK);
		orderWorkflowExecutor.completeUserTask(order, form);
	}

	@Override
	public void cancelUnfulfilled(final String orderId)
	{
		LOG.trace("cancelUnfulfilled");
		final OrderData order = orderService.getOrderByOrderId(orderId);

		final UserTaskForm form = new UserTaskForm();
		form.putAction(OrderAction.CANCEL_UNFULFILLED.name());
		form.addTaskDefinitionKey(FULFILL_INCOMPLETE_USER_TASK);
		orderWorkflowExecutor.completeUserTask(order, form);
	}

	@Deprecated
	@Override
	public Order cancelOrder(final String orderId)
	{
		LOG.trace("cancelOrder");

		final OrderData orderData = this.orderService.getOrderByOrderId(orderId);

		try
		{
			this.orderService.cancelOrder(orderData);
		}
		catch (final IllegalStateException e)
		{
			throw new InvalidOperationException("Cannot cancel order with orderId: " + orderId + ".", e);
		}

		return this.orderConverter.convert(orderData);
	}

	@Override
	public OrderLineQuantityStatus createOrderLineQuantityStatus(final OrderLineQuantityStatus newStatus)
	{
		LOG.trace("createOrderLineQuantityStatus");
		final OrderLineQuantityStatusData olqs = this.orderLineQuantityStatusReverseConverter.convert(newStatus);
		this.orderLineQuantityStatusValidator.validate("order line quantity status", newStatus, entityValidationHandler);

		try
		{
			this.orderService.flush();
		}
		catch (final PrimaryKeyViolationException e)
		{
			throw new DuplicateEntityException("Order line quantity status with status code " + newStatus.getStatusCode()
					+ " already exists.", e);
		}
		return this.orderLineQuantityStatusConverter.convert(olqs);
	}

	@Override
	public List<OrderLineQuantityStatus> findAllOrderLineQuantityStatuses()
	{
		LOG.trace("findAllOrderLineQuantityStatuses");
		final List<OrderLineQuantityStatusData> olqs = this.orderService.getAllOrderLineQuantityStatuses();

		return this.converters.convertAll(olqs, this.orderLineQuantityStatusConverter);
	}

	@Override
	public Order getOrderByOrderId(final String orderId)
	{
		LOG.trace("getOrderWithOrderId");
		final OrderData orderData = this.orderService.getOrderByOrderId(orderId);
		return this.orderConverter.convert(orderData);
	}

	@Override
	public OrderLineQuantityStatus updateOrderLineQuantityStatus(final OrderLineQuantityStatus status)
	{
		LOG.trace("updateOrderLineQuantityStatus");
		this.orderLineQuantityStatusValidator.validate("order line quantity status", status, entityValidationHandler);
		final OrderLineQuantityStatusData olqStatusData = this.orderService.getOrderLineQuantityStatusByStatusCode(status
				.getStatusCode());
		this.orderLineQuantityStatusReversePopulator.populate(status, olqStatusData);
		return this.orderLineQuantityStatusConverter.convert(olqStatusData);
	}

	@Override
	public UpdatedSinceList<String> getOrderIdsUpdatedAfter(final Date aDate)
	{
		Date lastShipmentModifiedDate = aDate;
		Date lastOrderModifiedDate = aDate;

		final Page<ShipmentData> shipmentDataList = this.shipmentService.findAllShipmentsUpdatedAfter(aDate);
		final Set<String> orderIdsFromShipments = new HashSet<>(shipmentDataList.getSize());

		for (final ShipmentData shipment : shipmentDataList.getContent())
		{
			orderIdsFromShipments.add(shipment.getOrderFk().getOrderId());
			if (lastShipmentModifiedDate.before(shipment.getModifiedTime()))
				lastShipmentModifiedDate = shipment.getModifiedTime();
		}

		// Find all orders updated after the date
		final Page<OrderData> orderDataList = this.orderService.findOrdersUpdatedAfter(aDate);

		final Set<String> orderIdsFromOrders = new HashSet<>(orderDataList.getSize());
		for (final OrderData order : orderDataList.getContent())
		{
			orderIdsFromOrders.add(order.getOrderId());
			if (lastOrderModifiedDate.before(order.getModifiedTime()))
				lastOrderModifiedDate = order.getModifiedTime();
		}

		//Merge the orderIds obtained from updatedOrders and updatedShipments
		orderIdsFromOrders.addAll(orderIdsFromShipments);

		final List<String> orderIds = new ArrayList<String>(orderIdsFromOrders);
		// set minimum of the last modified order date or last modified shipment date which ever is before
		return new UpdatedSinceList<>(
				lastOrderModifiedDate.before(lastShipmentModifiedDate) ? lastOrderModifiedDate : lastShipmentModifiedDate, orderIds);
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

	protected Validator<QueryObject<?>> getQueryObjectValidator()
	{
		return queryObjectValidator;
	}

	@Required
	public void setQueryObjectValidator(final Validator<QueryObject<?>> queryObjectValidator)
	{
		this.queryObjectValidator = queryObjectValidator;
	}

	protected SourcingService getSourcingService()
	{
		return sourcingService;
	}

	@Required
	public void setSourcingService(final SourcingService sourcingService)
	{
		this.sourcingService = sourcingService;
	}

	protected Converter<OrderLineQuantityStatusData, OrderLineQuantityStatus> getOrderLineQuantityStatusConverter()
	{
		return orderLineQuantityStatusConverter;
	}

	@Required
	public void setOrderLineQuantityStatusConverter(
			final Converter<OrderLineQuantityStatusData, OrderLineQuantityStatus> orderLineQuantityStatusConverter)
	{
		this.orderLineQuantityStatusConverter = orderLineQuantityStatusConverter;
	}

	protected Converter<OrderLineQuantityStatus, OrderLineQuantityStatusData> getOrderLineQuantityStatusReverseConverter()
	{
		return orderLineQuantityStatusReverseConverter;
	}

	@Required
	public void setOrderLineQuantityStatusReverseConverter(
			final Converter<OrderLineQuantityStatus, OrderLineQuantityStatusData> orderLineQuantityStatusReverseConverter)
	{
		this.orderLineQuantityStatusReverseConverter = orderLineQuantityStatusReverseConverter;
	}

	protected Populator<OrderLineQuantityStatus, OrderLineQuantityStatusData> getOrderLineQuantityStatusReversePopulator()
	{
		return orderLineQuantityStatusReversePopulator;
	}

	@Required
	public void setOrderLineQuantityStatusReversePopulator(
			final Populator<OrderLineQuantityStatus, OrderLineQuantityStatusData> orderLineQuantityStatusReversePopulator)
	{
		this.orderLineQuantityStatusReversePopulator = orderLineQuantityStatusReversePopulator;
	}

	protected Converter<OrderData, Order> getOrderConverter()
	{
		return orderConverter;
	}

	@Required
	public void setOrderConverter(final Converter<OrderData, Order> orderConverter)
	{
		this.orderConverter = orderConverter;
	}

	protected Converter<Order, OrderData> getOrderReverseConverter()
	{
		return orderReverseConverter;
	}

	@Required
	public void setOrderReverseConverter(final Converter<Order, OrderData> orderReverseConverter)
	{
		this.orderReverseConverter = orderReverseConverter;
	}

	protected Converter<OrderLineData, OrderLine> getOrderLineConverter()
	{
		return orderLineConverter;
	}

	@Required
	public void setOrderLineConverter(final Converter<OrderLineData, OrderLine> orderLineConverter)
	{
		this.orderLineConverter = orderLineConverter;
	}

	protected Converters getConverters()
	{
		return converters;
	}

	@Required
	public void setConverters(final Converters converters)
	{
		this.converters = converters;
	}

	protected Validator<Order> getOrderValidator()
	{
		return orderValidator;
	}

	@Required
	public void setOrderValidator(final Validator<Order> orderValidator)
	{
		this.orderValidator = orderValidator;
	}

	protected Validator<OrderLineQuantityStatus> getOrderLineQuantityStatusValidator()
	{
		return orderLineQuantityStatusValidator;
	}

	@Required
	public void setOrderLineQuantityStatusValidator(final Validator<OrderLineQuantityStatus> orderLineQuantityStatusValidator)
	{
		this.orderLineQuantityStatusValidator = orderLineQuantityStatusValidator;
	}

	protected FailureHandler getEntityValidationHandler()
	{
		return entityValidationHandler;
	}

	@Required
	public void setEntityValidationHandler(final FailureHandler entityValidationHandler)
	{
		this.entityValidationHandler = entityValidationHandler;
	}

	protected WorkflowExecutor<OrderData> getOrderWorkflowExecutor()
	{
		return orderWorkflowExecutor;
	}

	@Required
	public void setOrderWorkflowExecutor(final WorkflowExecutor<OrderData> orderWorkflowExecutor)
	{
		this.orderWorkflowExecutor = orderWorkflowExecutor;
	}

    public ShipmentService getShipmentService() {
        return shipmentService;
    }

    @Required
    public void setShipmentService(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

	/*
	 * OMSWSTHREE-135: map orderFk for Shipments if these are provided with the order.
	 */
	protected void mapOrderFk(final OrderData orderData)
	{
		if (orderData != null)
		{
			for (final OrderLineData ol : orderData.getOrderLines())
			{
				for (final OrderLineQuantityData olq : ol.getOrderLineQuantities())
				{
					if (olq.getShipment() != null)
					{
						olq.getShipment().setOrderFk(orderData);
					}
				}
			}
		}
	}

}
