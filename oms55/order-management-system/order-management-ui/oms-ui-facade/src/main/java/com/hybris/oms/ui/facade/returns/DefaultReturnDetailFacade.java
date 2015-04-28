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
package com.hybris.oms.ui.facade.returns;

import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.util.Converters;
import com.hybris.kernel.api.Page;
import com.hybris.oms.api.PageInfo;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.PagedResults;
import com.hybris.oms.domain.QueryObject;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.returns.Return;
import com.hybris.oms.domain.returns.ReturnOrderLine;
import com.hybris.oms.domain.returns.ReturnPaymentInfo;
import com.hybris.oms.domain.returns.ReturnQueryObject;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.facade.validation.FailureHandler;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.managedobjects.types.QuantityVT;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.returns.ReturnService;
import com.hybris.oms.ui.api.returns.ReturnDetail;
import com.hybris.oms.ui.api.returns.ReturnDetailFacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;


/**
 * Default implementation of {@link ReturnDetailFacade}.
 */
public class DefaultReturnDetailFacade implements ReturnDetailFacade
{
	private ReturnService returnService;
	private Converters converters;
	private Converter<ReturnData, Return> returnConverter;
	private OrderService orderService;
	private Validator<QueryObject<?>> queryObjectValidator;
	private Converter<OrderData, Order> orderConverter;
	private FailureHandler entityValidationHandler;

	@Override
	@Transactional(readOnly = true)
	public Pageable<ReturnDetail> findReturnDetailsByQuery(final ReturnQueryObject returnQueryObject)
	{
		this.queryObjectValidator.validate("ReturnQueryObject", returnQueryObject, this.entityValidationHandler);

		final Page<ReturnData> pagedReturnData = this.returnService.findPagedReturnsByQuery(returnQueryObject);
		final Collection<Return> returns = this.converters.convertAll(pagedReturnData.getContent(), this.returnConverter);
		final List<ReturnDetail> returnDetails = this.buildReturnDetails(returns);

		final PageInfo pageInfo = new PageInfo();
		pageInfo.setTotalPages(pagedReturnData.getTotalPages());
		pageInfo.setTotalResults(pagedReturnData.getTotalElements());
		pageInfo.setPageNumber(pagedReturnData.getNumber());
		return new PagedResults<ReturnDetail>(returnDetails, pageInfo);
	}

	@Override
	@Transactional(readOnly = true)
	public ReturnDetail getReturnDetailById(final String returnId) throws EntityNotFoundException
	{
		final ReturnData returnData = this.returnService.findReturnById(Long.valueOf(returnId));
		final Return aReturn = this.returnConverter.convert(returnData);
		return this.buildReturnDetail(aReturn);
	}

	@Override
	@Transactional(readOnly = true)
	public ReturnDetail buildReturnDetailByOrderId(final String orderId)
	{
		final ReturnDetail returnDetail = new ReturnDetail();

		final OrderData orderData = orderService.getOrderByOrderId(orderId);
		final Order linkedOrder = this.getOrderById(orderId);
		returnDetail.setOrder(linkedOrder);

		final Return newReturn = new Return();
		newReturn.setOrderId(orderId);
		newReturn.setReturnPaymentInfos(new ReturnPaymentInfo());

		// Populate possible return order lines
		final List<ReturnOrderLine> returnOrderLines = new ArrayList<ReturnOrderLine>();
		for (final OrderLine orderLine : linkedOrder.getOrderLines())
		{
			final ReturnOrderLine rol = new ReturnOrderLine();
			rol.setOrderLine(orderLine);
			final QuantityVT returnableQuantity = returnService.getReturnableQuantity(null, orderData, orderLine.getOrderLineId(),
					orderLine.getSkuId());
			rol.setQuantity(new Quantity(returnableQuantity.getUnitCode(), returnableQuantity.getValue()));
			returnOrderLines.add(rol);
		}
		newReturn.setReturnOrderLines(returnOrderLines);

		final boolean previouslyrefunded = returnService.shippingPreviouslyRefunded(null, orderData);
		returnDetail.setPreviouslyrefundedshippingcost(previouslyrefunded);
		returnDetail.setReturn(newReturn);

		return returnDetail;
	}

	protected ReturnDetail buildReturnDetail(final Return aReturn)
	{
		final ReturnDetail returnDetail = new ReturnDetail();
		returnDetail.setReturn(aReturn);
		returnDetail.setOrder(this.getOrderById(aReturn.getOrderId()));
		return returnDetail;
	}

	protected List<ReturnDetail> buildReturnDetails(final Collection<Return> returns)
	{
		final List<ReturnDetail> returnDetails = new ArrayList<>();
		if (returns != null && !returns.isEmpty())
		{
			for (final Return aReturn : returns)
			{
				returnDetails.add(buildReturnDetail(aReturn));
			}
		}
		return returnDetails;
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

	protected Converters getConverters()
	{
		return converters;
	}

	@Required
	public void setConverters(final Converters converters)
	{
		this.converters = converters;
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

	protected Converter<ReturnData, Return> getReturnConverter()
	{
		return returnConverter;
	}

	@Required
	public void setReturnConverter(final Converter<ReturnData, Return> returnConverter)
	{
		this.returnConverter = returnConverter;
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

	protected Converter<OrderData, Order> getOrderConverter()
	{
		return orderConverter;
	}

	@Required
	public void setOrderConverter(final Converter<OrderData, Order> orderConverter)
	{
		this.orderConverter = orderConverter;
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

	protected ReturnService getReturnService()
	{
		return returnService;
	}

	@Required
	public void setReturnService(final ReturnService returnService)
	{
		this.returnService = returnService;
	}

}
