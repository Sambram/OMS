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
package com.hybris.oms.facade.shipment;

import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.Populator;
import com.hybris.commons.conversion.util.Converters;
import com.hybris.kernel.api.Page;
import com.hybris.oms.api.PageInfo;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.PagedResults;
import com.hybris.oms.api.shipment.ShipmentFacade;
import com.hybris.oms.domain.BatchResult;
import com.hybris.oms.domain.FailedProcessedItem;
import com.hybris.oms.domain.ProcessedItem;
import com.hybris.oms.domain.QueryObject;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.exception.InvalidOperationException;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.domain.shipping.ShipmentDetails;
import com.hybris.oms.domain.shipping.ShipmentQueryObject;
import com.hybris.oms.domain.shipping.ShipmentSplitResult;
import com.hybris.oms.facade.validation.FailureHandler;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.facade.validation.impl.shipment.ShipmentOlqDto;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.shipment.ShipmentService;
import com.hybris.oms.service.workflow.UserTaskForm;
import com.hybris.oms.service.workflow.WorkflowConstants;
import com.hybris.oms.service.workflow.WorkflowException;
import com.hybris.oms.service.workflow.executor.UserTaskData;
import com.hybris.oms.service.workflow.executor.WorkflowExecutor;
import com.hybris.oms.service.workflow.executor.shipment.ShipmentAction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import static com.hybris.oms.domain.preference.TenantPreferenceConstants.PREF_KEY_OLQSTATUS_CANCELED;
import static com.hybris.oms.domain.preference.TenantPreferenceConstants.PREF_KEY_OLQSTATUS_MANUAL_DECLINED;
import static com.hybris.oms.domain.preference.TenantPreferenceConstants.PREF_KEY_OLQSTATUS_PACKED;
import static com.hybris.oms.domain.preference.TenantPreferenceConstants.PREF_KEY_OLQSTATUS_PICKED;
import static com.hybris.oms.service.workflow.WorkflowConstants.KEY_OLQ_STATUS;
import static com.hybris.oms.service.workflow.executor.shipment.ShipmentAction.CANCEL;
import static com.hybris.oms.service.workflow.executor.shipment.ShipmentAction.DECLINE;
import static com.hybris.oms.service.workflow.executor.shipment.ShipmentAction.MANUAL_CAPTURE;
import static com.hybris.oms.service.workflow.executor.shipment.ShipmentAction.REALLOCATE;
import static com.hybris.oms.service.workflow.executor.shipment.ShipmentAction.SPLIT_OLQ;
import static com.hybris.oms.service.workflow.executor.shipment.ShipmentAction.SPLIT_QUANTITIES;
import static com.hybris.oms.service.workflow.executor.shipment.ShipmentAction.STATUS_UPDATE;


/**
 * Default implementation of {@link ShipmentFacade}.
 */
@SuppressWarnings({"PMD.TooManyPublicMethods", "PMD.ExcessiveImports"})
public class DefaultShipmentFacade implements ShipmentFacade
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultShipmentFacade.class);
	public static final String CAPTURE_ACTION_USER_TASK = "CaptureAction_UserTask";
	private InventoryService inventoryService;
	private OrderService orderService;
	private ShipmentService shipmentService;
	private WorkflowExecutor<ShipmentData> shipmentWorkflowExecutor;

	private Converter<ShipmentData, Shipment> shipmentConverter;
	private Populator<ShipmentDetails, ShipmentData> shipmentDetailsReversePopulator;
	private Converters converters;

	private Validator<String> locationIdValidator;
	private Validator<QueryObject<?>> queryObjectValidator;
	private Validator<String> orderLineQuantityStatusCodeValidator;
	private Validator<ShipmentOlqDto> shipmentOlqValidator;
	private FailureHandler entityValidationHandler;

	@Value("${oms.facade.maximumSynchronousOperation}")
	private int maximumSynchronousOperation;

	@Transactional
	@Override
	public Shipment confirmShipment(final String shipmentId)
	{
		LOGGER.trace("confirmShipment");
		final Long shipmentIdLong = this.getStringIdAsLong(shipmentId);
		final ShipmentData shipment = this.shipmentService.getShipmentById(shipmentIdLong);

		final UserTaskForm form = new UserTaskForm();
		form.putAction(ShipmentAction.CONFIRM.name());
		shipmentWorkflowExecutor.completeUserTask(shipment, form);
		return this.shipmentConverter.convert(shipment);
	}

	@Override
	@Transactional(readOnly = true)
	public Pageable<Shipment> findShipmentsByQuery(final ShipmentQueryObject shipmentQueryObject)
	{
		LOGGER.trace("findAllShipmentsByQuery");
		this.queryObjectValidator.validate("ShipmentQueryObject", shipmentQueryObject, this.entityValidationHandler);

		final Page<ShipmentData> pagedShipmentData = this.shipmentService.findPagedShipmentsByQuery(shipmentQueryObject);
		final List<Shipment> shipments = this.converters.convertAll(pagedShipmentData.getContent(), this.shipmentConverter);
		final PageInfo pageInfo = new PageInfo();

		pageInfo.setTotalPages(pagedShipmentData.getTotalPages());
		pageInfo.setTotalResults(pagedShipmentData.getTotalElements());
		pageInfo.setPageNumber(pagedShipmentData.getNumber());

		return new PagedResults<Shipment>(shipments, pageInfo);
	}

	@Override
	@Transactional(readOnly = true)
	public Shipment getShipmentById(final String shipmentId)
	{
		LOGGER.trace("getShipmentById");
		final Long shipmentIdLong = this.getStringIdAsLong(shipmentId);
		final ShipmentData shipmentData = this.shipmentService.getShipmentById(shipmentIdLong);
		return this.shipmentConverter.convert(shipmentData);
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<Shipment> getShipmentsByOrderId(final String orderId)
	{
		LOGGER.trace("getShipmentsByOrderId");
		final OrderData order = this.orderService.getOrderByOrderId(orderId);
		final List<ShipmentData> shipmentDataList = this.shipmentService.findShipmentsByOrder(order);
		return this.converters.convertAll(shipmentDataList, this.shipmentConverter);
	}

	@Override
	@Transactional
	public byte[] retrieveShippingLabelsByShipmentId(final String shipmentId)
	{
		LOGGER.trace("getShippingLabels");
		final Long shipmentIdLong = this.getStringIdAsLong(shipmentId);
		final ShipmentData shipmentData = this.shipmentService.getShipmentById(shipmentIdLong);
		return this.shipmentService.getShippingLabels(shipmentData);
	}

	@Override
	@Transactional
	public Shipment packShipment(final String shipmentId) throws EntityNotFoundException
	{
		LOGGER.trace("Pack shipment {}", shipmentId);
		final OrderLineQuantityStatusData olqStatus = orderService
				.getOrderLineQuantityStatusByTenantPreferenceKey(PREF_KEY_OLQSTATUS_PACKED);
		return this.updateShipmentStatus(shipmentId, olqStatus.getStatusCode());
	}

	@Override
	@Transactional
	public Shipment pickShipment(final String shipmentId) throws EntityNotFoundException
	{
		LOGGER.trace("Pick shipment {}", shipmentId);
		final OrderLineQuantityStatusData olqStatus = orderService
				.getOrderLineQuantityStatusByTenantPreferenceKey(PREF_KEY_OLQSTATUS_PICKED);
		return this.updateShipmentStatus(shipmentId, olqStatus.getStatusCode());
	}

	@Override
	@Transactional
	public void updateShipmentDetails(final String shipmentId, final ShipmentDetails shipmentDetails)
			throws EntityNotFoundException
	{
		LOGGER.trace("updateShipmentDetails");
		final Long shipmentIdLong = this.getStringIdAsLong(shipmentId);
		final ShipmentData shipmentData = this.shipmentService.getShipmentById(shipmentIdLong);

		// update
		this.shipmentDetailsReversePopulator.populate(shipmentDetails, shipmentData);
	}

	@Override
	@Transactional
	public Shipment updateShipmentStatus(final String shipmentId, final String olqStatusCode)
	{
		LOGGER.info("updateShipmentStatus");

		// Get the shipment and olq status and call the service
		final Long shipmentIdLong = this.getStringIdAsLong(shipmentId);
		final ShipmentData shipment = this.shipmentService.getShipmentById(shipmentIdLong);
		this.orderLineQuantityStatusCodeValidator.validate("statusCode", olqStatusCode, entityValidationHandler);

		final OrderLineQuantityStatusData olqStatus = this.orderService.getOrderLineQuantityStatusByStatusCode(olqStatusCode);
		final UserTaskForm form = new UserTaskForm();
		form.putAction(STATUS_UPDATE.name());
		form.put(KEY_OLQ_STATUS, olqStatus.getStatusCode());
		this.shipmentWorkflowExecutor.completeUserTask(shipment, form);
		return this.shipmentConverter.convert(shipment);
	}

	@Transactional
	@Override
	public Shipment cancelShipment(final String shipmentId)
	{
		LOGGER.trace("cancelShipment");
		final Long shipmentIdLong = this.getStringIdAsLong(shipmentId);
		final ShipmentData shipment = this.shipmentService.getShipmentById(shipmentIdLong);
		final OrderLineQuantityStatusData olqStatus = orderService
				.getOrderLineQuantityStatusByTenantPreferenceKey(PREF_KEY_OLQSTATUS_CANCELED);

		final UserTaskForm form = new UserTaskForm();
		form.putAction(STATUS_UPDATE.name());
		form.putActionQueue(CANCEL.name());
		form.put(KEY_OLQ_STATUS, olqStatus.getStatusCode());

		this.shipmentWorkflowExecutor.completeUserTask(shipment, form);
		return this.shipmentConverter.convert(shipment);
	}

	@Transactional
	@Override
	public void declineShipment(final String shipmentId) throws EntityNotFoundException, InvalidOperationException
	{
		LOGGER.trace("Decline shipment {}", shipmentId);
		final Long shipmentIdLong = this.getStringIdAsLong(shipmentId);
		final ShipmentData shipment = this.shipmentService.getShipmentById(shipmentIdLong);
		final OrderLineQuantityStatusData olqStatus = orderService
				.getOrderLineQuantityStatusByTenantPreferenceKey(PREF_KEY_OLQSTATUS_MANUAL_DECLINED);

		final UserTaskForm form = new UserTaskForm();
		form.putAction(STATUS_UPDATE.name());
		form.putActionQueue(DECLINE.name());
		form.put(KEY_OLQ_STATUS, olqStatus.getStatusCode());

		this.shipmentWorkflowExecutor.completeUserTask(shipment, form);
	}

	@Transactional
	@Override
	public Shipment reallocateShipment(final String shipmentId, final String locationId) throws EntityNotFoundException,
			InvalidOperationException
	{
		LOGGER.trace("Re-Allocate shipment {} to location {}", shipmentId, locationId);

		final Long shipmentIdLong = this.getStringIdAsLong(shipmentId);
		final ShipmentData shipment = this.shipmentService.getShipmentById(shipmentIdLong);
		this.locationIdValidator.validate("Location", locationId, this.entityValidationHandler);

		final UserTaskForm form = new UserTaskForm();
		form.putAction(REALLOCATE.name());
		form.put(WorkflowConstants.KEY_LOCATION_ID, locationId);
		shipmentWorkflowExecutor.completeUserTask(shipment, form);

		return this.shipmentConverter.convert(shipment);
	}

	@Transactional
	@Override
	public ShipmentSplitResult splitShipmentByOlqs(final String shipmentId, final Set<String> olqIds)
			throws EntityNotFoundException, EntityValidationException, InvalidOperationException
	{
		LOGGER.trace("Splitting shipment {} by olqs {}", shipmentId, olqIds);
		final Shipment shipment = getShipmentById(shipmentId);
		shipmentOlqValidator.validate("ShipmentOlqs", new ShipmentOlqDto(shipment, olqIds), this.entityValidationHandler);

		// Complete shipment user task with "SPLIT_OLQ" action.
		final ShipmentData shipmentData = this.shipmentService.getShipmentById(getStringIdAsLong(shipmentId));

		final UserTaskForm form = new UserTaskForm();
		form.putAction(SPLIT_OLQ.name());
		shipmentWorkflowExecutor.completeUserTask(shipmentData, form, new UserTaskData(olqIds));

		// TODO OMS-509 : Flush here to force commit the transaction spawned by the workflow work item worker previously.
		// Else the new shipment created by the split would not be found. We should find a better way to do this.
		this.shipmentService.flush();

		// Get new shipment
		final String newShipmentId = (String) shipmentWorkflowExecutor.getParameter(shipmentData,
				WorkflowConstants.KEY_SPLIT_SHIPMENT_ID);
		final Shipment newShipment = getShipmentById(newShipmentId);

		return new ShipmentSplitResult(newShipment, this.shipmentConverter.convert(shipmentData));
	}

	@Transactional
	@Override
	public ShipmentSplitResult splitShipmentByOlqQuantities(final String shipmentId,
			final Map<String, Integer> olqIdQuantityValueMap)
	{
		LOGGER.trace("Splitting shipment {} by olq quantities", shipmentId);
		final Shipment shipment = getShipmentById(shipmentId);
		shipmentOlqValidator.validate("ShipmentOlqs", new ShipmentOlqDto(shipment, olqIdQuantityValueMap.keySet()),
				this.entityValidationHandler);

		// Complete shipment user task with "SPLIT_QUANTITIES" action.
		final ShipmentData originalShipment = this.shipmentService.getShipmentById(getStringIdAsLong(shipmentId));

		final UserTaskForm form = new UserTaskForm();
		form.putAction(SPLIT_QUANTITIES.name());
		shipmentWorkflowExecutor.completeUserTask(originalShipment, form, new UserTaskData(olqIdQuantityValueMap));

		// TODO OMS-509 : Flush here to force commit the transaction spawned by the workflow work item worker previously.
		// Else the new shipment created by the split would not be found. We should find a better way to do this.
		this.shipmentService.flush();

		// Get new shipment
		final String newShipmentId = (String) shipmentWorkflowExecutor.getParameter(originalShipment,
				WorkflowConstants.KEY_SPLIT_SHIPMENT_ID);
		final Shipment newShipment = getShipmentById(newShipmentId);

		return new ShipmentSplitResult(newShipment, this.shipmentConverter.convert(originalShipment));
	}

	@Transactional
	@Override
	public BatchResult declineShipments(final Set<String> shipmentIds) throws EntityNotFoundException, IllegalArgumentException
	{
		final BatchResult result = new BatchResult();
		this.validateShipmentIdsForBatchProcess(shipmentIds);

		for (final String shipmentId : shipmentIds)
		{
			try
			{
				final ShipmentData shipment = this.shipmentService.getShipmentById(this.getStringIdAsLong(shipmentId));
				declineShipment(Long.toString(shipment.getShipmentId()));

				final ProcessedItem processedItem = new ProcessedItem();
				processedItem.setBusinessId(shipmentId);

				result.addProcessedItem(processedItem);
			}
			catch (final InvalidOperationException | EntityNotFoundException | WorkflowException exception)
			{
				final FailedProcessedItem failedItem = new FailedProcessedItem();
				failedItem.setBusinessId(shipmentId);
				failedItem.setExceptionClass(exception.getClass().getSimpleName());
				failedItem.setExceptionMessage(exception.getMessage());
				result.addProcessedItem(failedItem);
			}
		}
		return result;
	}

	@Transactional
	@Override
	public BatchResult cancelShipments(final Set<String> shipmentIds) throws EntityNotFoundException, IllegalArgumentException
	{
		final BatchResult result = new BatchResult();
		this.validateShipmentIdsForBatchProcess(shipmentIds);

		for (final String shipmentId : shipmentIds)
		{
			try
			{
				final ShipmentData shipment = this.shipmentService.getShipmentById(this.getStringIdAsLong(shipmentId));
				this.cancelShipment(Long.toString(shipment.getShipmentId()));

				final ProcessedItem processedItem = new ProcessedItem();
				processedItem.setBusinessId(shipmentId);

				result.addProcessedItem(processedItem);
			}
			catch (final InvalidOperationException | EntityNotFoundException | WorkflowException exception)
			{
				final FailedProcessedItem failedItem = new FailedProcessedItem();
				failedItem.setBusinessId(shipmentId);
				failedItem.setExceptionClass(exception.getClass().getSimpleName());
				failedItem.setExceptionMessage(exception.getMessage());
				result.addProcessedItem(failedItem);
			}
		}
		return result;
	}

	@Transactional
	@Override
	public BatchResult confirmShipments(final Set<String> shipmentIds) throws EntityNotFoundException, IllegalArgumentException
	{
		final BatchResult result = new BatchResult();
		this.validateShipmentIdsForBatchProcess(shipmentIds);
		for (final String shipmentId : shipmentIds)
		{
			try
			{
				final ShipmentData shipment = this.shipmentService.getShipmentById(this.getStringIdAsLong(shipmentId));
				this.confirmShipment(Long.toString(shipment.getShipmentId()));

				final ProcessedItem processedItem = new ProcessedItem();
				processedItem.setBusinessId(shipmentId);

				result.addProcessedItem(processedItem);
			}
			catch (final InvalidOperationException | EntityNotFoundException | WorkflowException exception)
			{
				final FailedProcessedItem failedItem = new FailedProcessedItem();
				failedItem.setBusinessId(shipmentId);
				failedItem.setExceptionClass(exception.getClass().getSimpleName());
				failedItem.setExceptionMessage(exception.getMessage());
				result.addProcessedItem(failedItem);
			}
		}
		return result;
	}

	@Override
	public Shipment manualCapture(final String shipmentId) throws EntityNotFoundException, InvalidOperationException
	{
		LOGGER.trace("manualCapture for shipment:  {}", shipmentId);
		final ShipmentData shipmentData = this.shipmentService.getShipmentById(this.getStringIdAsLong(shipmentId));

		if (shipmentData == null)
		{
			throw new EntityNotFoundException("Shipment not found for ID: " + shipmentId);
		}
		final UserTaskForm form = new UserTaskForm();
		form.putAction(MANUAL_CAPTURE.name());
		form.addTaskDefinitionKey(CAPTURE_ACTION_USER_TASK);
		this.shipmentWorkflowExecutor.completeUserTask(shipmentData, form);
		return this.shipmentConverter.convert(shipmentData);
	}

	protected void validateShipmentIdsForBatchProcess(final Set<String> shipmentIds)
	{
		if (shipmentIds == null || shipmentIds.isEmpty())
		{
			throw new IllegalArgumentException("Shipment Ids cannot be null or empty.");
		}
		if (shipmentIds.size() > this.maximumSynchronousOperation)
		{
			throw new InvalidOperationException(
					String.format(
							"Cannot perform more than %s operations. Please look for oms.facade.maximumSynchronousOperation to increase or decrease this number.",
							this.maximumSynchronousOperation));
		}
	}

	protected List<Long> getStringIdsAsLong(final Collection<String> ids)
	{
		final List<Long> longIds = new ArrayList<Long>();

		if (!CollectionUtils.isEmpty(ids))
		{
			for (final String id : ids)
			{
				longIds.add(getStringIdAsLong(id));
			}
		}

		return longIds;
	}

	protected Long getStringIdAsLong(final String id)
	{
		try
		{
			return Long.valueOf(id);
		}
		catch (final NumberFormatException e)
		{
			throw new EntityValidationException("Id should represent long value, but was: " + id, e);
		}
	}

	protected void setMaximumSynchronousOperation(final int maximumSynchronousOperation)
	{
		this.maximumSynchronousOperation = maximumSynchronousOperation;
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

	protected Converter<ShipmentData, Shipment> getShipmentConverter()
	{
		return shipmentConverter;
	}

	@Required
	public void setShipmentConverter(final Converter<ShipmentData, Shipment> shipmentConverter)
	{
		this.shipmentConverter = shipmentConverter;
	}

	protected Populator<ShipmentDetails, ShipmentData> getShipmentDetailsReversePopulator()
	{
		return shipmentDetailsReversePopulator;
	}

	@Required
	public void setShipmentDetailsReversePopulator(final Populator<ShipmentDetails, ShipmentData> shipmentDetailsReversePopulator)
	{
		this.shipmentDetailsReversePopulator = shipmentDetailsReversePopulator;
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

	protected FailureHandler getEntityValidationHandler()
	{
		return entityValidationHandler;
	}

	@Required
	public void setEntityValidationHandler(final FailureHandler entityValidationHandler)
	{
		this.entityValidationHandler = entityValidationHandler;
	}

	protected ShipmentService getShipmentService()
	{
		return shipmentService;
	}

	@Required
	public void setShipmentService(final ShipmentService shipmentService)
	{
		this.shipmentService = shipmentService;
	}

	protected Validator<String> getOrderLineQuantityStatusCodeValidator()
	{
		return orderLineQuantityStatusCodeValidator;
	}

	@Required
	public void setOrderLineQuantityStatusCodeValidator(final Validator<String> orderLineQuantityStatusCodeValidator)
	{
		this.orderLineQuantityStatusCodeValidator = orderLineQuantityStatusCodeValidator;
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

	@Required
	public void setShipmentOlqValidator(final Validator<ShipmentOlqDto> shipmentOlqValidator)
	{
		this.shipmentOlqValidator = shipmentOlqValidator;
	}

	protected Validator<ShipmentOlqDto> getShipmentOlqValidator()
	{
		return this.shipmentOlqValidator;
	}

	@Required
	public void setLocationIdValidator(final Validator<String> locationIdValidator)
	{
		this.locationIdValidator = locationIdValidator;
	}

	protected Validator<String> getLocationIdValidator()
	{
		return this.locationIdValidator;
	}

}
