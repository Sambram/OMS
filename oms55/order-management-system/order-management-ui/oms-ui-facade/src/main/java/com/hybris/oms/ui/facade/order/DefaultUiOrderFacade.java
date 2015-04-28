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
package com.hybris.oms.ui.facade.order;

import java.util.List;

import com.hybris.oms.api.PageInfo;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.PagedResults;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.util.Converters;
import com.hybris.kernel.api.Page;
import com.hybris.oms.domain.QueryObject;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.order.OrderQueryObject;
import com.hybris.oms.facade.validation.FailureHandler;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.ui.api.order.UiOrderFacade;
import com.hybris.oms.ui.api.order.UIOrder;
import com.hybris.oms.ui.api.order.UIOrderDetails;


/**
 * Default implementation of {@link UiOrderFacade}.
 */
@Transactional
public class DefaultUiOrderFacade implements UiOrderFacade
{
	private OrderService orderService;
    private Converter<OrderData, UIOrder> uIOrderConverter;
    private Converter<OrderData, UIOrderDetails> uIOrderDetailsConverter;
	private Converters converters;
	private Validator<QueryObject<?>> queryObjectValidator;
	private FailureHandler entityValidationHandler;

	@Override
	public Pageable<UIOrder> findOrdersByQuery(final OrderQueryObject queryObject)
	{
		this.queryObjectValidator.validate("OrderQueryObject", queryObject, this.entityValidationHandler);

		final Page<OrderData> pagedOrderDatas = this.orderService.findPagedOrdersByQuery(queryObject);
		final List<UIOrder> orders = this.converters.convertAll(pagedOrderDatas.getContent(), this.uIOrderConverter);

		final PageInfo pageInfo = new PageInfo();
		pageInfo.setTotalPages(pagedOrderDatas.getTotalPages());
		pageInfo.setTotalResults(pagedOrderDatas.getTotalElements());
		pageInfo.setPageNumber(pagedOrderDatas.getNumber());

		return new PagedResults<UIOrder>(orders, pageInfo);
	}

    @Override
    public UIOrder getUIOrderByOrderId(String orderId) throws EntityNotFoundException
    {
        final OrderData orderData = this.orderService.getOrderByOrderId(orderId);
        return this.uIOrderConverter.convert(orderData);
    }

    @Override
    public UIOrderDetails getUIOrderDetailsByOrderId(String orderId) throws EntityNotFoundException
    {
        final OrderData orderData = this.orderService.getOrderByOrderId(orderId);
        return this.uIOrderDetailsConverter.convert(orderData);
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

    @Required
    public void setuIOrderConverter(final Converter<OrderData, UIOrder> uIOrderConverter) {
        this.uIOrderConverter = uIOrderConverter;
    }

    @Required
    public void setuIOrderDetailsConverter(final Converter<OrderData, UIOrderDetails> uIOrderDetailsConverter) {
        this.uIOrderDetailsConverter = uIOrderDetailsConverter;
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

	protected FailureHandler getEntityValidationHandler()
	{
		return entityValidationHandler;
	}

	@Required
	public void setEntityValidationHandler(final FailureHandler entityValidationHandler)
	{
		this.entityValidationHandler = entityValidationHandler;
	}

}
