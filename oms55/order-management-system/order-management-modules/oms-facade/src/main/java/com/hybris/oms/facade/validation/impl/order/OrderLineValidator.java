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
package com.hybris.oms.facade.validation.impl.order;

import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.order.OrderLineQuantity;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.facade.validation.Failure;
import com.hybris.oms.facade.validation.FieldValidationType;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.facade.validation.field.FieldValidatorFactory;
import com.hybris.oms.facade.validation.impl.AbstractValidator;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default order line validator.
 */
public class OrderLineValidator extends AbstractValidator<OrderLine>
{

	private static final Logger LOG = LoggerFactory.getLogger(OrderLineValidator.class);

	private Validator<Amount> amountWithPositiveValueValidator;

	private Validator<Quantity> quantityWithPositiveValueValidator;

	private Validator<OrderLineQuantity> orderLineQuantityValidator;

	@Override
	public void validateInternal(final ValidationContext context, final OrderLine orderLine)
	{
		LOG.debug("Validating order line with order line id: {}", orderLine.getOrderLineId());

		FieldValidatorFactory.getStringFieldValidator(context).notBlank("OrderLine.orderLineId", orderLine.getOrderLineId())
				.notBlank("OrderLine.skuId", orderLine.getSkuId());

		// if orderLine does not have orderlinequantities it means the orderline is not sourced yet and therefore
		// orderline quantity should be equal to orderline qunatity unassigned
		if (CollectionUtils.isEmpty(orderLine.getOrderLineQuantities()))
		{
			FieldValidatorFactory.getGenericFieldValidator(context).equalsValueType("OrderLine.quantity", orderLine.getQuantity(),
					orderLine.getQuantityUnassigned());
		}

		this.quantityWithPositiveValueValidator.validate("OrderLine.quantity", context, orderLine.getQuantity());
		this.quantityWithPositiveValueValidator
				.validate("OrderLine.quantityUnassigned", context, orderLine.getQuantityUnassigned());
		this.amountWithPositiveValueValidator.validate("OrderLine.unitPrice", context, orderLine.getUnitPrice());
		this.amountWithPositiveValueValidator.validate("OrderLine.unitTax", context, orderLine.getUnitTax());

		validateTaxCategory(context, orderLine);
		validateOrderLineQuantity(context, orderLine);
		validateOrderLineLocationRoles(context, orderLine);
	}

	protected void validateOrderLineLocationRoles(final ValidationContext context, final OrderLine orderLine)
	{
		// if it is not empty -> valid
		if (CollectionUtils.isEmpty(orderLine.getLocationRoles()))
		{
			context.reportFailure(this.getClass().getName(), new Failure(FieldValidationType.IS_BLANK, "OrderLine.locationRoles",
					null, "LocationRoles for orderLine is blank!"));
		}
	}


	protected void validateTaxCategory(final ValidationContext context, final OrderLine orderLine)
	{
		FieldValidatorFactory.getStringFieldValidator(context).notBlank("OrderLine.taxCategory", orderLine.getTaxCategory());
	}

	protected void validateOrderLineQuantity(final ValidationContext context, final OrderLine orderLine)
	{
		// order line quantities are not mandatory and therefore will only be validated when they exist
		if (CollectionUtils.isNotEmpty(orderLine.getOrderLineQuantities()))
		{
			FieldValidatorFactory.getGenericFieldValidator(context).validateCollection("OrderLine.orderLineQuantities",
					orderLine.getOrderLineQuantities(), this.orderLineQuantityValidator);
		}
	}

	@Required
	public void setAmountWithPositiveValueValidator(final Validator<Amount> amountWithPositiveValueValidator)
	{
		this.amountWithPositiveValueValidator = amountWithPositiveValueValidator;
	}

	@Required
	public void setOrderLineQuantityValidator(final Validator<OrderLineQuantity> orderLineQuantityValidator)
	{
		this.orderLineQuantityValidator = orderLineQuantityValidator;
	}

	@Required
	public void setQuantityWithPositiveValueValidator(final Validator<Quantity> quantityWithPositiveValueValidator)
	{
		this.quantityWithPositiveValueValidator = quantityWithPositiveValueValidator;
	}

	protected Validator<Amount> getAmountWithPositiveValueValidator()
	{
		return amountWithPositiveValueValidator;
	}

	protected Validator<OrderLineQuantity> getOrderLineQuantityValidator()
	{
		return orderLineQuantityValidator;
	}

	protected Validator<Quantity> getQuantityWithPositiveValueValidator()
	{
		return quantityWithPositiveValueValidator;
	}

}
