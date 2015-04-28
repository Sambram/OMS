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
package com.hybris.oms.service.returns.impl;

import com.google.common.collect.ImmutableList;
import com.hybris.kernel.api.Page;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.domain.returns.ReturnQueryObject;
import com.hybris.oms.domain.returns.ReviewReason;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.preference.TenantPreferenceData;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.managedobjects.returns.ReturnLineRejectionData;
import com.hybris.oms.service.managedobjects.returns.ReturnOrderLineData;
import com.hybris.oms.service.managedobjects.types.QuantityVT;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.preference.TenantPreferenceService;
import com.hybris.oms.service.preference.impl.DefaultTenantPreferenceService;
import com.hybris.oms.service.returns.ReturnService;
import com.hybris.oms.service.service.AbstractHybrisService;
import com.hybris.oms.service.workflow.CoreWorkflowConstants;
import com.hybris.oms.service.workflow.WorkflowConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Arrays;
import java.util.List;


public class DefaultReturnService extends AbstractHybrisService implements ReturnService
{

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultReturnService.class);

	private ReturnQueryFactory returnQueryFactory;
	private TenantPreferenceService tenantPreferenceService;
	private OrderService orderService;
	private List<String> returnReasonCodes;

	@Override
	public ReturnData findReturnById(final long returnId)
	{
		LOGGER.trace("findReturnById");
		return this.returnQueryFactory.getReturnById(returnId).optionalUniqueResult();
	}

	@Override
	public ReturnOrderLineData findReturnOrderLineById(final long returnOrderLineId)
	{
		LOGGER.trace("findReturnOrderLineById");
		return this.returnQueryFactory.getReturnOrderLineById(returnOrderLineId).optionalUniqueResult();
	}



	@Override
	public Page<ReturnData> findPagedReturnsByQuery(final ReturnQueryObject returnQueryObject)
	{
		LOGGER.trace("findPagedReturnsByQuery");
		final int[] pageNumberAndSize = this.getPageNumberAndSize(returnQueryObject, 0, this.getQueryPageSizeDefault());

		return this.findPaged(this.returnQueryFactory.findReturnsByQuery(returnQueryObject), pageNumberAndSize[0],
				pageNumberAndSize[1]);
	}

	@Override
	public List<ReturnData> findReturnsByOrderId(final String orderId)
	{
		LOGGER.trace("findReturnsByOrderId");
		final ReturnQueryObject returnQueryObject = new ReturnQueryObject();
		returnQueryObject.setOrderIds(ImmutableList.of(orderId));
		return this.returnQueryFactory.findReturnsByQuery(returnQueryObject).resultList();
	}

	@Override
	public QuantityVT getReturnableQuantity(final ReturnData returnData, final OrderData orderData, final String orderLineId,
			final String skuId)
	{
		if (orderData.getOrderId() == null)
		{
			throw new EntityValidationException("Cannot check the returnable quantity because order ID has not been provided");
		}

		int returnableQuantity = 0;
		returnableQuantity = incrementReturnableQuantity(orderData.getOrderId(), orderLineId, returnableQuantity);
		final Long ReturnId = returnData == null ? null : returnData.getReturnId();
		returnableQuantity = decreaseReturnableQuantity(ReturnId, orderData.getOrderId(), orderLineId, returnableQuantity);

		return new QuantityVT(null, returnableQuantity);
	}

	@Override
	public Boolean shippingPreviouslyRefunded(final ReturnData returnData, final OrderData orderData)
	{
		LOGGER.trace("shippingPreviouslyRefunded");
		final ReturnQueryObject returnQueryObject = new ReturnQueryObject();
		returnQueryObject.setOrderIds(ImmutableList.of(orderData.getOrderId()));
		final List<ReturnData> returns = this.returnQueryFactory.findReturnsByQuery(returnQueryObject).resultList();
		final Long returnId;

		if (returnData == null)
		{
			returnId = null;
		}
		else
		{
			returnId = returnData.getReturnId();
		}

		for (final ReturnData previousReturn : returns)
		{
			if (isShippingRefundedReturnNull(returnId, previousReturn))
			{
				return true;
			}
			else if (isShippingRefundedReturnNotNull(returnId, previousReturn))
			{
				return true;
			}
		}
		return false;
	}

	protected boolean isShippingRefundedReturnNull(final Long returnId, final ReturnData previousReturn)
	{
		return returnId == null && previousReturn.isShippingRefunded()
				&& !previousReturn.getState().equals(WorkflowConstants.STATE_RETURN_CANCELED);
	}

	protected boolean isShippingRefundedReturnNotNull(final Long returnId, final ReturnData previousReturn)
	{
		return returnId != null && previousReturn.getReturnId() != returnId && previousReturn.isShippingRefunded()
				&& !previousReturn.getState().equals(WorkflowConstants.STATE_RETURN_CANCELED);
	}

	protected int incrementReturnableQuantity(final String orderId, final String orderLineId,
			final int currentQuantity)
	{
		int incrementedQuantity = currentQuantity;

		// Get the tenant preference that contains the list of shipped status
		final TenantPreferenceData tenantPreference = tenantPreferenceService
				.getTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_RETURN_SHIPPED_STATUS);
		final List<String> shipmentOlqStatuses = Arrays.asList(tenantPreference.getValue().split("\\|"));

		final OrderLineData olData = orderService.getOrderLineByOrderIdAndOrderLineId(orderId, orderLineId);
		for (final OrderLineQuantityData olq : olData.getOrderLineQuantities())
		{
			if (shipmentOlqStatuses.contains(olq.getStatus().getStatusCode()))
			{
				incrementedQuantity += olq.getQuantityValue();
			}
		}

		return incrementedQuantity;
	}

	protected int decreaseReturnableQuantity(final Long returnId, final String orderId, final String orderLineId,
			final int currentQuantity)
	{
		final List<ReturnData> existingReturns = findReturnsByOrderId(orderId);
		int decreasedQuantity = currentQuantity;

		for (final ReturnData existingReturn : existingReturns)
		{
			final boolean updateCurrentReturn = (returnId != null && existingReturn.getReturnId() == returnId);

			if (!updateCurrentReturn && !existingReturn.getState().equalsIgnoreCase(WorkflowConstants.STATE_RETURN_CANCELED))
			{
				for (final ReturnOrderLineData existingRol : existingReturn.getReturnOrderLines())
				{
					if (orderService
							.getOrderLineByOrderIdAndOrderLineId(existingReturn.getOrder().getOrderId(), existingRol.getOrderLineId())
							.getOrderLineId().equalsIgnoreCase(orderLineId))
					{
						decreasedQuantity -= existingRol.getQuantity().getValue();
					}
				}
			}
		}
		return decreasedQuantity;
	}

	@Override
	public void acceptAllNonRejectedItems(final ReturnData theReturn)
	{
		Integer quantityRejected = null;
		for (final ReturnOrderLineData returnOrderLine : theReturn.getReturnOrderLines())
		{
			quantityRejected = getQuantityRejected(returnOrderLine);

			// Only accept items for lines not all rejected
			if (quantityRejected.intValue() < returnOrderLine.getQuantity().getValue())
			{
				final ReturnLineRejectionData returnLineRejection = getPersistenceManager().create(ReturnLineRejectionData.class);
				returnLineRejection.setMyReturnOrderLine(returnOrderLine);
				returnLineRejection.setQuantity(0);
				returnLineRejection.setResponsible(CoreWorkflowConstants.USER_SYSTEM);
				returnLineRejection.setReason(ReviewReason.APPROVED.name());
				returnOrderLine.getReturnLineRejections().add(returnLineRejection);
			}
		}
	}

	@Override
	public Integer getQuantityRejected(final ReturnOrderLineData returnOrderLine)
	{
		int numberRejected = 0;
		for (final ReturnLineRejectionData returnLineRejection : returnOrderLine.getReturnLineRejections())
		{
			numberRejected += returnLineRejection.getQuantity();
		}
		return Integer.valueOf(numberRejected);
	}

	@Required
	public void setReturnQueryFactory(final ReturnQueryFactory returnQueryFactory)
	{
		this.returnQueryFactory = returnQueryFactory;
	}

	protected ReturnQueryFactory getReturnQueryFactory()
	{
		return returnQueryFactory;
	}

	@Required
	public void setTenantPreferenceService(final DefaultTenantPreferenceService tenantPreferenceService)
	{
		this.tenantPreferenceService = tenantPreferenceService;
	}

	protected TenantPreferenceService getTenantPreferenceService()
	{
		return tenantPreferenceService;
	}

	public OrderService getOrderService()
	{
		return orderService;
	}

	@Required
	public void setOrderService(final OrderService orderService)
	{
		this.orderService = orderService;
	}


	@Override
	public List<String> getReturnReasonCodes()
	{
		return returnReasonCodes;
	}

	public void setReturnReasonCodes(final List<String> returnReasonCodes)
	{
		this.returnReasonCodes = returnReasonCodes;
	}

}
