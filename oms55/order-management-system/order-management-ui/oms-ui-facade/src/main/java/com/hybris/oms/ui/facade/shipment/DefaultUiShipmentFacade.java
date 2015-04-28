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
package com.hybris.oms.ui.facade.shipment;

import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.util.Converters;
import com.hybris.kernel.api.Page;
import com.hybris.oms.api.PageInfo;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.PagedResults;
import com.hybris.oms.domain.QueryObject;
import com.hybris.oms.domain.ats.AtsLocalQuantities;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.exception.InvalidOperationException;
import com.hybris.oms.domain.exception.RemoteRequestException;
import com.hybris.oms.domain.inventory.Bin;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.order.OrderLineQuantity;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.domain.shipping.ShipmentQueryObject;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.facade.validation.FailureHandler;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.service.ats.AtsResult;
import com.hybris.oms.service.ats.AtsService;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.managedobjects.inventory.BinData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.order.impl.OrderDataStaticUtils;
import com.hybris.oms.service.shipment.ShipmentService;
import com.hybris.oms.service.shipment.impl.ShipmentDataStaticUtils;
import com.hybris.oms.ui.api.shipment.OrderLineShipmentPickSlipBinInfo;
import com.hybris.oms.ui.api.shipment.OrderShipmentDetail;
import com.hybris.oms.ui.api.shipment.PickSlipBinInfo;
import com.hybris.oms.ui.api.shipment.ShipmentDetail;
import com.hybris.oms.ui.api.shipment.UIShipment;
import com.hybris.oms.ui.api.shipment.UiShipmentFacade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.collect.Sets;


/**
 * Default implementation of {@link UiShipmentFacade}.
 * TODO: Move all business logic to oms-ui-services
 */
public class DefaultUiShipmentFacade implements UiShipmentFacade
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultUiShipmentFacade.class);
	private AtsService atsService;
	private Converters converters;
	private InventoryService inventoryService;
	private Converter<StockroomLocationData, Location> locationConverter;
	private OrderService orderService;
	private Converter<ShipmentData, Shipment> shipmentConverter;
	private ShipmentService shipmentService;
	private Validator<QueryObject<?>> queryObjectValidator;
	private Converter<OrderLineQuantityData, OrderLineQuantity> orderLineQuantityConverter;
	private Converter<OrderLineData, OrderLine> orderLineConverter;
	private Converter<OrderData, Order> orderConverter;
	private Converter<AtsResult, List<AtsLocalQuantities>> atsLocalQuantitiesConverter;
	private Converter<ShipmentData, UIShipment> uiShipmentConverter;
	private Converter<BinData, Bin> binConverter;
	private FailureHandler entityValidationHandler;


	@Autowired
	private PlatformTransactionManager transactionManager;

	@Override
	@Transactional(readOnly = true)
	public Pageable<ShipmentDetail> findShipmentDetailsByQuery(final ShipmentQueryObject shipmentQueryObject)
	{
		this.queryObjectValidator.validate("ShipmentQueryObject", shipmentQueryObject, this.entityValidationHandler);

		final Page<ShipmentData> pagedShipmentData = this.shipmentService.findPagedShipmentsByQuery(shipmentQueryObject);
		final Collection<Shipment> shipments = this.converters.convertAll(pagedShipmentData.getContent(), this.shipmentConverter);
		final List<ShipmentDetail> shipmentDetails = this.buildShipmentDetails(shipments);

		final PageInfo pageInfo = new PageInfo();
		pageInfo.setTotalPages(pagedShipmentData.getTotalPages());
		pageInfo.setTotalResults(pagedShipmentData.getTotalElements());
		pageInfo.setPageNumber(pagedShipmentData.getNumber());
		return new PagedResults<ShipmentDetail>(shipmentDetails, pageInfo);
	}

	@Override
	@Transactional(readOnly = true)
	public ShipmentDetail getShipmentDetailById(final String shipmentId) throws EntityNotFoundException
	{
		final ShipmentData shipmentData = this.shipmentService.getShipmentById(Long.parseLong(shipmentId));
		final Shipment shipment = this.shipmentConverter.convert(shipmentData);
		return this.buildShipmentDetail(shipment);
	}

	@Override
	@Transactional(readOnly = true)
	public Pageable<UIShipment> findUIShipmentsByQuery(final ShipmentQueryObject shipmentQueryObject)
	{
		this.queryObjectValidator.validate("ShipmentQueryObject", shipmentQueryObject, this.entityValidationHandler);
		final Page<ShipmentData> pagedShipmentData = this.shipmentService.findPagedShipmentsByQuery(shipmentQueryObject);
		final List<UIShipment> UIShipments = this.converters.convertAll(pagedShipmentData.getContent(), this.uiShipmentConverter);

		final PageInfo pageInfo = new PageInfo();
		pageInfo.setTotalPages(pagedShipmentData.getTotalPages());
		pageInfo.setTotalResults(pagedShipmentData.getTotalElements());
		pageInfo.setPageNumber(pagedShipmentData.getNumber());
		return new PagedResults<UIShipment>(UIShipments, pageInfo);
	}

	@Override
	@Transactional(readOnly = true)
	public UIShipment getUIShipmentById(final String shipmentId) throws EntityNotFoundException
	{
		final Long shipmentIdLong = this.getShipmentIdAsLong(shipmentId);
		final ShipmentData shipmentData = this.shipmentService.getShipmentById(shipmentIdLong);
		return this.uiShipmentConverter.convert(shipmentData);
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrderShipmentDetail> findOrderShipmentDetailsByOrderId(final String orderId, final boolean allLocationDisplay)
	{
		final OrderData orderData = this.orderService.getOrderByOrderId(orderId);
		final Order order = this.orderConverter.convert(orderData);
		return this.buildOrderShipmentVO(order.getOrderLines());
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrderShipmentDetail> findOrderShipmentDetailsByShipmentId(final String shipmentId, final boolean allLocationDisplay)
			throws EntityNotFoundException, RemoteRequestException
	{
		final Long shipmentIdLong = this.getShipmentIdAsLong(shipmentId);
		final ShipmentData shipmentData = this.shipmentService.getShipmentById(shipmentIdLong);
		final List<Long> shipmentOlqIds = OrderDataStaticUtils.getOlqIdsForShipment(shipmentData);
		final Shipment shipment = this.shipmentConverter.convert(shipmentData);

		List<OrderShipmentDetail> orderShipmentDetails = new ArrayList<>();
		if (shipment != null && shipmentOlqIds != null && !shipmentOlqIds.isEmpty())
		{
			orderShipmentDetails = this.buildOrderShipmentVOFromIdsLocationId(shipmentOlqIds);
		}
		return orderShipmentDetails;
	}

	@Override
	@Transactional
	public PickSlipBinInfo getBinInfoForPickSlipByShipmentId(final String shipmentId) throws EntityNotFoundException
	{
		final PickSlipBinInfo result = new PickSlipBinInfo();
		final Long shipmentIdLong = this.getShipmentIdAsLong(shipmentId);
		final ShipmentData shipment = this.shipmentService.getShipmentById(shipmentIdLong);
		final List<OrderLineShipmentPickSlipBinInfo> orderLineBins = new ArrayList<>();

		final String stockroomLocationId = shipment.getStockroomLocationId();
		for (final OrderLineData orderLineData : orderService.getOrderLinesByShipment(shipment))
		{
			final OrderLineShipmentPickSlipBinInfo orderLineBin = new OrderLineShipmentPickSlipBinInfo();
			orderLineBin.setBins(converters.convertAll(
					inventoryService.findBinsBySkuAndLocationId(orderLineData.getSkuId(), stockroomLocationId), binConverter));
			orderLineBin.setOrderLineSkuId(orderLineData.getSkuId());
			orderLineBin.setOrderLineNote(orderLineData.getNote());
			orderLineBin.setOrderLineQuantity(new Quantity(orderLineData.getQuantityUnitCode(), orderLineData.getQuantityValue()));
			orderLineBins.add(orderLineBin);
		}
		result.setOrderLineBins(orderLineBins);
		result.setShipmentId(shipmentId);
		return result;
	}

	@Override
	@Transactional
	public void removeAllOrderLineQuantitiesFromShipment(final String shipmentId)
	{
		try
		{
			LOGGER.debug("removeAllOLQsFromShipment");
			final Long shipmentIdLong = this.getShipmentIdAsLong(shipmentId);
			final ShipmentData shipmentData = this.shipmentService.getShipmentById(shipmentIdLong);
			this.shipmentService.declineShipment(shipmentData);
		}
		catch (final IllegalStateException e)
		{
			throw new InvalidOperationException(String.format("Cannot remove all olqs from shipment with shipmentId: %s.)",
					shipmentId), e);
		}
	}

	@Override
	public void removeOrderLineQuantityFromShipment(final String shipmentId, final String olqId)
	{
		LOGGER.debug("removeOLQsFromShipment");

		try
		{

			final ShipmentData newShipment = new TransactionTemplate(transactionManager)
					.execute(new TransactionCallback<ShipmentData>()
					{
						@Override
						public ShipmentData doInTransaction(final TransactionStatus status)
						{
							final Long shipmentIdLong = getShipmentIdAsLong(shipmentId);
							final ShipmentData shipmentData = shipmentService.getShipmentById(shipmentIdLong);

							final int numberOfOlqs = ShipmentDataStaticUtils.getShipmentOrderLineQuantities(shipmentData).size();

							if (numberOfOlqs <= 1)
							{
								shipmentService.declineShipment(shipmentData);
							}
							else
							{
								final OrderLineQuantityData orderLineQuantityData = orderService
										.getOrderLineQuantityByOlqId(getOlqIdAsLong(olqId));
								return shipmentService.splitShipmentByOlqs(shipmentData, Arrays.asList(orderLineQuantityData));
							}

							return null;
						}
					});

			if (newShipment != null)
			{
				new TransactionTemplate(transactionManager).execute(new TransactionCallbackWithoutResult()
				{
					@Override
					public void doInTransactionWithoutResult(final TransactionStatus status)
					{
						shipmentService.declineShipment(newShipment);
					}
				});

			}

		}
		catch (final IllegalStateException e)
		{
			throw new InvalidOperationException(String.format("Cannot remove olq '%s' from shipment with shipmentId: %s.)", olqId,
					shipmentId), e);
		}
	}

	protected Long getOlqIdAsLong(final String olqId)
	{
		try
		{
			return Long.valueOf(olqId);
		}
		catch (final NumberFormatException e)
		{
			throw new EntityValidationException("olqId should represent long value, but was: " + olqId, e);
		}
	}

	// TODO: Move conversion to a converter.
	protected ShipmentDetail buildShipmentDetail(final Shipment shipment)
	{
		final ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setShipment(shipment);
		shipmentDetail.setOrder(this.getOrderById(shipment.getOrderId()));
		shipmentDetail.setLocation(this.getLocationById(shipment.getLocation()));
		return shipmentDetail;
	}

	// TODO: Move conversion to a converter.
	protected List<ShipmentDetail> buildShipmentDetails(final Collection<Shipment> shipments)
	{
		final List<ShipmentDetail> shipmentDetails = new ArrayList<>();
		if (shipments != null && !shipments.isEmpty())
		{
			for (final Shipment shipment : shipments)
			{
				shipmentDetails.add(buildShipmentDetail(shipment));
			}
		}
		return shipmentDetails;
	}

	// TODO: Move conversion to a converter.
	protected List<OrderShipmentDetail> buildOrderShipmentVO(final List<OrderLine> orderLines)
	{
		final List<OrderShipmentDetail> orderShipmentDetails = new ArrayList<>();

		if (orderLines != null)
		{
			for (final OrderLine orderLine : orderLines)
			{
				if (orderLine.getOrderLineQuantities() != null)
				{
					for (final OrderLineQuantity orderLineQuantity : orderLine.getOrderLineQuantities())
					{
						orderShipmentDetails.add(this.buildOrderShipmentDetail(orderLineQuantity, orderLine, false));
					}
				}
			}
		}
		return orderShipmentDetails;
	}

	protected Map<AtsLocalQuantities, String> buildATSLocationMap(final String skuId)
	{
		// Find those with value for ATS
		final AtsResult atsResult = this.atsService.getLocalAts(Sets.newHashSet(skuId), null,
				Sets.newHashSet(this.atsService.getDefaultAtsId()));
		final List<AtsLocalQuantities> listOfItemLocationAtsWithAts = this.atsLocalQuantitiesConverter.convert(atsResult);
		final Map<AtsLocalQuantities, String> mapLocationNameATSValue = new HashMap<>();

		for (final AtsLocalQuantities itemLocationAts : listOfItemLocationAtsWithAts)
		{
			final Location location = this.getLocationById(itemLocationAts.getLocationId());
			mapLocationNameATSValue.put(itemLocationAts, location.getStoreName());
		}

		return mapLocationNameATSValue;
	}

	// TODO: Move conversion to a converter.
	protected OrderShipmentDetail buildOrderShipmentDetail(final OrderLineQuantity orderLineQuantity, final OrderLine orderLine,
			final boolean calculateATSLocationMap)
	{
		final OrderShipmentDetail orderShipmentDetail = new OrderShipmentDetail();
		orderShipmentDetail.setOrderLineQuantity(orderLineQuantity);

		if (orderLine != null)
		{
			orderShipmentDetail.setOrderLine(orderLine);
		}

		if (calculateATSLocationMap)
		{
			if (orderLine != null)
			{
				orderShipmentDetail.setLocationATS(this.buildATSLocationMap(orderLine.getSkuId()));
			}
		}
		else
		{
			final Location location = this.getLocationById(orderLineQuantity.getLocation());
			orderShipmentDetail.getOrderLineQuantity().setLocation(location.getLocationId());
		}

		return orderShipmentDetail;
	}

	// TODO: Move conversion to a converter.
	protected List<OrderShipmentDetail> buildOrderShipmentVOFromIdsLocationId(final List<Long> olqIds)
	{
		final List<OrderShipmentDetail> orderShipmentDetails = new ArrayList<>();

		for (final Long olqId : olqIds)
		{
			final OrderLineQuantityData orderLineQuantityData = this.orderService.getOrderLineQuantityByOlqId(olqId);
			final OrderLineQuantity orderLineQuantity = this.orderLineQuantityConverter.convert(orderLineQuantityData);
			final OrderLine orderLine = this.orderLineConverter.convert(orderLineQuantityData.getOrderLine());

			final OrderShipmentDetail orderShipmentDetail = this.buildOrderShipmentDetail(orderLineQuantity, orderLine, true);

			orderShipmentDetails.add(orderShipmentDetail);
		}
		return orderShipmentDetails;
	}

	protected Location getLocationById(final String locationId)
	{
		try
		{
			final StockroomLocationData locationData = this.inventoryService.getLocationByLocationId(locationId);
			return this.locationConverter.convert(locationData);
		}
		catch (final EntityNotFoundException e)
		{
			return null;
		}
	}

	protected Order getOrderById(final String orderId)
	{
		try
		{
			final OrderData orderData = this.orderService.getOrderByOrderId(orderId);
			return this.orderConverter.convert(orderData);
		}
		catch (final EntityNotFoundException e)
		{
			return null;
		}
	}

	protected Long getShipmentIdAsLong(final String shipmentId)
	{
		try
		{
			return Long.valueOf(shipmentId);
		}
		catch (final NumberFormatException e)
		{
			throw new EntityValidationException("shipmentId should represent long value, but was: " + shipmentId, e);
		}
	}

	protected AtsService getAtsService()
	{
		return atsService;
	}

	@Required
	public void setAtsService(final AtsService atsService)
	{
		this.atsService = atsService;
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

	protected Converter<StockroomLocationData, Location> getLocationConverter()
	{
		return locationConverter;
	}

	@Required
	public void setLocationConverter(final Converter<StockroomLocationData, Location> locationConverter)
	{
		this.locationConverter = locationConverter;
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

	protected Validator<QueryObject<?>> getQueryObjectValidator()
	{
		return queryObjectValidator;
	}

	@Required
	public void setQueryObjectValidator(final Validator<QueryObject<?>> queryObjectValidator)
	{
		this.queryObjectValidator = queryObjectValidator;
	}

	protected Converter<OrderLineQuantityData, OrderLineQuantity> getOrderLineQuantityConverter()
	{
		return orderLineQuantityConverter;
	}

	@Required
	public void setOrderLineQuantityConverter(final Converter<OrderLineQuantityData, OrderLineQuantity> orderLineQuantityConverter)
	{
		this.orderLineQuantityConverter = orderLineQuantityConverter;
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

	protected Converter<OrderData, Order> getOrderConverter()
	{
		return orderConverter;
	}

	@Required
	public void setOrderConverter(final Converter<OrderData, Order> orderConverter)
	{
		this.orderConverter = orderConverter;
	}

	public void setUiShipmentConverter(final Converter<ShipmentData, UIShipment> uiShipmentConverter)
	{
		this.uiShipmentConverter = uiShipmentConverter;
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

	protected Converter<AtsResult, List<AtsLocalQuantities>> getAtsLocalQuantitiesConverter()
	{
		return atsLocalQuantitiesConverter;
	}

	@Required
	public void setAtsLocalQuantitiesConverter(final Converter<AtsResult, List<AtsLocalQuantities>> atsLocalQuantitiesConverter)
	{
		this.atsLocalQuantitiesConverter = atsLocalQuantitiesConverter;
	}

	protected Converter<BinData, Bin> getBinConverter()
	{
		return binConverter;
	}

	@Required
	public void setBinConverter(final Converter<BinData, Bin> binConverter)
	{
		this.binConverter = binConverter;
	}

}
