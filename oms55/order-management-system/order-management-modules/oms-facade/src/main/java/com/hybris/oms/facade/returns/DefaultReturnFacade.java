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
package com.hybris.oms.facade.returns;

import static com.hybris.oms.service.workflow.WorkflowConstants.APPROVE_RETURN_USER_TASK;
import static com.hybris.oms.service.workflow.WorkflowConstants.CAPTURE_ACTION_USER_TASK;
import static com.hybris.oms.service.workflow.WorkflowConstants.CONFIRM_INSTORE_REFUND_USER_TASK;
import static com.hybris.oms.service.workflow.WorkflowConstants.CONFIRM_ONLINE_REFUND_USER_TASK;
import static com.hybris.oms.service.workflow.WorkflowConstants.FIX_TAX_REVERSE_USER_TASK;
import static com.hybris.oms.service.workflow.WorkflowConstants.STATE_RETURN_NEW;
import static com.hybris.oms.service.workflow.WorkflowConstants.STATE_RETURN_TAX_REVERSE_FIXED;
import static com.hybris.oms.service.workflow.WorkflowConstants.STATE_RETURN_UPDATE;
import static com.hybris.oms.service.workflow.WorkflowConstants.WAIT_FOR_GOODS_USER_TASK;

import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.Populator;
import com.hybris.commons.conversion.util.Converters;
import com.hybris.kernel.api.Page;
import com.hybris.kernel.api.exceptions.PrimaryKeyViolationException;
import com.hybris.kernel.api.exceptions.ValidationException;
import com.hybris.oms.api.PageInfo;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.PagedResults;
import com.hybris.oms.api.returns.ReturnFacade;
import com.hybris.oms.domain.QueryObject;
import com.hybris.oms.domain.exception.DuplicateEntityException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.exception.InvalidOperationException;
import com.hybris.oms.domain.returns.Return;
import com.hybris.oms.domain.returns.ReturnLineRejection;
import com.hybris.oms.domain.returns.ReturnOrderLine;
import com.hybris.oms.domain.returns.ReturnQueryObject;
import com.hybris.oms.domain.returns.ReturnReview;
import com.hybris.oms.facade.validation.FailureHandler;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.managedobjects.returns.ReturnLineRejectionData;
import com.hybris.oms.service.managedobjects.returns.ReturnOrderLineData;
import com.hybris.oms.service.returns.ReturnService;
import com.hybris.oms.service.util.ValidationUtils;
import com.hybris.oms.service.workflow.UserTaskForm;
import com.hybris.oms.service.workflow.executor.UserTaskData;
import com.hybris.oms.service.workflow.executor.WorkflowExecutor;
import com.hybris.oms.service.workflow.executor.returns.ReturnAction;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;


/**
 * Default implementation of {@link com.hybris.oms.api.returns.ReturnFacade}.
 */
public class DefaultReturnFacade implements ReturnFacade
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultReturnFacade.class);

	private ReturnService returnService;

	private Validator<QueryObject<?>> queryObjectValidator;

	private Converter<ReturnData, Return> returnConverter;

	private Converter<Return, ReturnData> returnReverseConverter;

	private Populator<Return, ReturnData> returnReversePopulator;

	private Converter<ReturnOrderLineData, ReturnOrderLine> returnOrderLineConverter;

	private Populator<ReturnOrderLine, ReturnOrderLineData> returnOrderLineReversePopulator;

	private Converters converters;

	private Validator<Return> returnValidator;

	private Validator<ReturnOrderLine> returnOrderLineValidator;

	private Validator<ReturnReview> returnReviewValidator;

	private Converter<ReturnLineRejection, ReturnLineRejectionData> returnLineRejectionReverseConverter;

	private Converter<ReturnLineRejectionData, ReturnLineRejection> returnLineRejectionConverter;

	private FailureHandler entityValidationHandler;
	private WorkflowExecutor<ReturnData> returnWorkflowExecutor;

	@Transactional
	@Override
	public Return createReturn(final Return returnDto) throws EntityValidationException, DuplicateEntityException
	{
		LOG.trace("createReturn");

		returnValidator.validate("Return", returnDto, entityValidationHandler);
		final ReturnData returnData = returnReverseConverter.convert(returnDto);

		try
		{
			returnService.flush();
			// Kick Start the workflow for Return
			this.returnWorkflowExecutor.execute(returnData);
			returnDto.setState(STATE_RETURN_NEW);
		}
		catch (final PrimaryKeyViolationException e)
		{
			throw new DuplicateEntityException("Return " + returnDto.getReturnId() + " already exists", e);
		}
		catch (final ValidationException e)
		{
			LOG.debug("Validation Exception during Return creation", e);
			throw new EntityValidationException(ValidationUtils.getValidationMessage(e.getConstraintViolations()), e);
		}

		return returnConverter.convert(returnData);
	}

	@Transactional
	@Override
	public Return updateReturn(final Return returnDto) throws EntityValidationException, EntityNotFoundException
	{
		LOG.trace("updateReturn");
		returnValidator.validate("Return", returnDto, entityValidationHandler);
		final ReturnData returnData = returnService.findReturnById(Long.valueOf(returnDto.getReturnId()));
		if (returnData == null)
		{
			throw new EntityNotFoundException("Return not found, Return Id: " + returnDto.getReturnId());
		}
		returnReversePopulator.populate(returnDto, returnData);
		returnData.setState(STATE_RETURN_UPDATE);
		returnService.flush();
		// Execute the workflow, but the validation and update are already done
		// It ensure that the workflow is in the right state and give a chance to do more stuff in the workflow if
		// necessary
		final UserTaskForm form = new UserTaskForm();
		form.putAction(ReturnAction.UPDATE.name());
		form.addTaskDefinitionKeys(CONFIRM_INSTORE_REFUND_USER_TASK, CONFIRM_ONLINE_REFUND_USER_TASK);
		this.returnWorkflowExecutor.completeUserTask(returnData, form, new UserTaskData(returnDto));
		return returnConverter.convert(returnService.findReturnById(Long.valueOf(returnDto.getReturnId())));
	}

	@Transactional
	@Override
	public Return fixTaxReverseFailure(final String returnId) throws EntityNotFoundException, InvalidOperationException
	{
		LOG.trace("fixTaxReverseFailure");
		final ReturnData returnData = returnService.findReturnById(Long.valueOf(returnId));
		if (returnData == null)
		{
			throw new EntityNotFoundException("Return not found for ID: " + returnId);
		}
		returnData.setState(STATE_RETURN_TAX_REVERSE_FIXED);
		returnService.flush();
		final UserTaskForm form = new UserTaskForm();
		form.putAction(ReturnAction.TAX_FIXED.name());
		form.addTaskDefinitionKey(FIX_TAX_REVERSE_USER_TASK);
		this.returnWorkflowExecutor.completeUserTask(returnData, form);
		return returnConverter.convert(returnService.findReturnById(Long.valueOf(returnId)));
	}

	@Transactional
	@Override
	public Return getReturnById(final String returnId) throws EntityNotFoundException
	{
		LOG.trace("getReturnById {}", returnId);
		final ReturnData returnData = returnService.findReturnById(Long.valueOf(returnId));
		if (returnData == null)
		{
			throw new EntityNotFoundException("Return not found for ID: " + returnId);
		}
		return returnConverter.convert(returnData);
	}

	@Transactional
	@Override
	public Return cancelReturn(final String returnId) throws EntityNotFoundException
	{
		LOG.trace("cancelReturn {}", returnId);
		final ReturnData returnData = returnService.findReturnById(Long.valueOf(returnId));
		if (returnData == null)
		{
			throw new EntityNotFoundException("Return not found for ID: " + returnId);
		}
		final UserTaskForm form = new UserTaskForm();
		form.putAction(ReturnAction.CANCEL.name());
		form.addTaskDefinitionKeys(CONFIRM_INSTORE_REFUND_USER_TASK, CONFIRM_ONLINE_REFUND_USER_TASK, APPROVE_RETURN_USER_TASK,
				WAIT_FOR_GOODS_USER_TASK, CAPTURE_ACTION_USER_TASK);
		this.returnWorkflowExecutor.completeUserTask(returnData, form);
		return returnConverter.convert(returnData);
	}

	@Transactional
	@Override
	public Return autoRefundReturn(final String returnId) throws EntityNotFoundException, InvalidOperationException
	{
		LOG.trace("autoRefundReturn {}", returnId);
		final ReturnData returnData = returnService.findReturnById(Long.valueOf(returnId));
		if (returnData == null)
		{
			throw new EntityNotFoundException("Return not found for ID: " + returnId);
		}
		final UserTaskForm form = new UserTaskForm();
		form.putAction(ReturnAction.AUTO_REFUND.name());
		form.addTaskDefinitionKeys(CONFIRM_INSTORE_REFUND_USER_TASK, CONFIRM_ONLINE_REFUND_USER_TASK, WAIT_FOR_GOODS_USER_TASK);
		this.returnWorkflowExecutor.completeUserTask(returnData, form);
		return returnConverter.convert(returnData);
	}

	@Transactional
	@Override
	public Return manualRefundReturn(final String returnId) throws EntityNotFoundException, InvalidOperationException
	{
		LOG.trace("manualRefundReturn {}", returnId);
		final ReturnData returnData = returnService.findReturnById(Long.valueOf(returnId));
		if (returnData == null)
		{
			throw new EntityNotFoundException("Return not found for ID: " + returnId);
		}
		final UserTaskForm form = new UserTaskForm();
		form.putAction(ReturnAction.MANUAL_REFUND.name());
		form.addTaskDefinitionKeys(CONFIRM_INSTORE_REFUND_USER_TASK, CAPTURE_ACTION_USER_TASK);
		this.returnWorkflowExecutor.completeUserTask(returnData, form);
		return returnConverter.convert(returnData);
	}

	@Override
	public List<String> returnReasonCodes()
	{
		final List<String> codes = new ArrayList<>();
		for (final String reasonCode : returnService.getReturnReasonCodes())
		{
			codes.add(reasonCode);
		}

		return codes;
	}

	@Override
	@Transactional(readOnly = true)
	public Pageable<Return> findReturnsByQuery(final ReturnQueryObject returnQueryObject)
	{
		this.queryObjectValidator.validate("ReturnQueryObject", returnQueryObject, this.entityValidationHandler);
		final Page<ReturnData> pagedReturnData = this.returnService.findPagedReturnsByQuery(returnQueryObject);
		final List<Return> returns = this.converters.convertAll(pagedReturnData.getContent(), this.returnConverter);

		final PageInfo pageInfo = new PageInfo();
		pageInfo.setTotalPages(pagedReturnData.getTotalPages());
		pageInfo.setTotalResults(pagedReturnData.getTotalElements());
		pageInfo.setPageNumber(pagedReturnData.getNumber());
		return new PagedResults<Return>(returns, pageInfo);
	}

	@Transactional
	@Override
	public void createReturnReview(final ReturnReview returnReview) throws EntityValidationException, DuplicateEntityException
	{
		LOG.trace("createReturnReview");

		returnReviewValidator.validate("ReturnReview", returnReview, entityValidationHandler);

		final List<ReturnLineRejectionData> returnLineRejectionDataList = new ArrayList<>();

		for (final ReturnLineRejection returnLineRejection : returnReview.getReturnLineRejections())
		{
			final ReturnLineRejectionData returnLineRejectionData = returnLineRejectionReverseConverter.convert(returnLineRejection);
			returnLineRejectionDataList.add(returnLineRejectionData);
		}
		final ReturnData returnData = returnLineRejectionDataList.get(0).getMyReturnOrderLine().getMyReturn();

		final UserTaskForm form = new UserTaskForm();
		form.putAction(ReturnAction.REVIEW.name());
		form.addTaskDefinitionKeys(APPROVE_RETURN_USER_TASK, WAIT_FOR_GOODS_USER_TASK);
		returnWorkflowExecutor.completeUserTask(returnData, form, new UserTaskData(returnLineRejectionDataList));
	}

	@Required
	public void setReturnService(final ReturnService returnService)
	{
		this.returnService = returnService;
	}

	@Required
	public void setQueryObjectValidator(final Validator<QueryObject<?>> queryObjectValidator)
	{
		this.queryObjectValidator = queryObjectValidator;
	}

	@Required
	public void setReturnConverter(final Converter<ReturnData, Return> returnConverter)
	{
		this.returnConverter = returnConverter;
	}

	@Required
	public void setReturnReverseConverter(final Converter<Return, ReturnData> returnReverseConverter)
	{
		this.returnReverseConverter = returnReverseConverter;
	}

	@Required
	public void setReturnReversePopulator(final Populator<Return, ReturnData> returnReversePopulator)
	{
		this.returnReversePopulator = returnReversePopulator;
	}

	@Required
	public void setConverters(final Converters converters)
	{
		this.converters = converters;
	}

	@Required
	public void setReturnValidator(final Validator<Return> returnValidator)
	{
		this.returnValidator = returnValidator;
	}

	@Required
	public void setReturnOrderLineValidator(final Validator<ReturnOrderLine> returnOrderLineValidator)
	{
		this.returnOrderLineValidator = returnOrderLineValidator;
	}

	@Required
	public void setEntityValidationHandler(final FailureHandler entityValidationHandler)
	{
		this.entityValidationHandler = entityValidationHandler;
	}

	@Required
	public void setReturnOrderLineConverter(final Converter<ReturnOrderLineData, ReturnOrderLine> returnOrderLineConverter)
	{
		this.returnOrderLineConverter = returnOrderLineConverter;
	}

	@Required
	public void setReturnOrderLineReversePopulator(
			final Populator<ReturnOrderLine, ReturnOrderLineData> returnOrderLineReversePopulator)
	{
		this.returnOrderLineReversePopulator = returnOrderLineReversePopulator;
	}

	@Required
	public void setReturnWorkflowExecutor(final WorkflowExecutor<ReturnData> returnWorkflowExecutor)
	{
		this.returnWorkflowExecutor = returnWorkflowExecutor;
	}

	@Required
	public void setReturnReviewValidator(final Validator<ReturnReview> returnReviewValidator)
	{
		this.returnReviewValidator = returnReviewValidator;
	}

	@Required
	public void setReturnLineRejectionReverseConverter(
			final Converter<ReturnLineRejection, ReturnLineRejectionData> returnLineRejectionReverseConverter)
	{
		this.returnLineRejectionReverseConverter = returnLineRejectionReverseConverter;
	}

	@Required
	public void setReturnLineRejectionConverter(
			final Converter<ReturnLineRejectionData, ReturnLineRejection> returnLineRejectionConverter)
	{
		this.returnLineRejectionConverter = returnLineRejectionConverter;
	}

	protected FailureHandler getEntityValidationHandler()
	{
		return entityValidationHandler;
	}

	protected Converters getConverters()
	{
		return converters;
	}

	protected Validator<Return> getReturnValidator()
	{
		return returnValidator;
	}

	protected Validator<ReturnOrderLine> getReturnOrderLineValidator()
	{
		return returnOrderLineValidator;
	}

	protected Populator<Return, ReturnData> getReturnReversePopulator()
	{
		return returnReversePopulator;
	}

	protected Converter<Return, ReturnData> getReturnReverseConverter()
	{
		return returnReverseConverter;
	}

	protected Validator<QueryObject<?>> getQueryObjectValidator()
	{
		return queryObjectValidator;
	}

	protected ReturnService getReturnService()
	{
		return returnService;
	}

	protected Converter<ReturnData, Return> getReturnConverter()
	{
		return returnConverter;
	}

	protected Converter<ReturnOrderLineData, ReturnOrderLine> getReturnOrderLineConverter()
	{
		return returnOrderLineConverter;
	}

	protected Populator<ReturnOrderLine, ReturnOrderLineData> getReturnOrderLineReversePopulator()
	{
		return returnOrderLineReversePopulator;
	}

	protected WorkflowExecutor<ReturnData> getReturnWorkflowExecutor()
	{
		return returnWorkflowExecutor;
	}

	protected Validator<ReturnReview> getReturnReviewValidator()
	{
		return returnReviewValidator;
	}

	protected Converter<ReturnLineRejection, ReturnLineRejectionData> getReturnLineRejectionReverseConverter()
	{
		return returnLineRejectionReverseConverter;
	}

	protected Converter<ReturnLineRejectionData, ReturnLineRejection> getReturnLineRejectionConverter()
	{
		return returnLineRejectionConverter;
	}

}
