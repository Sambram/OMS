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
package com.hybris.oms.facade.basestore;

import java.util.List;

import com.hybris.oms.api.PageInfo;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.PagedResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.Populator;
import com.hybris.commons.conversion.util.Converters;
import com.hybris.kernel.api.Page;
import com.hybris.kernel.api.exceptions.PrimaryKeyViolationException;
import com.hybris.oms.api.basestore.BaseStoreFacade;
import com.hybris.oms.domain.QueryObject;
import com.hybris.oms.domain.basestore.BaseStore;
import com.hybris.oms.domain.basestore.BaseStoreQueryObject;
import com.hybris.oms.domain.exception.DuplicateEntityException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.facade.validation.FailureHandler;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.service.basestore.BaseStoreService;
import com.hybris.oms.service.managedobjects.basestore.BaseStoreData;


/**
 * Default implementation of {@link BaseStoreFacade}.
 */
public class DefaultBaseStoreFacade implements BaseStoreFacade
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultBaseStoreFacade.class);

	private BaseStoreService baseStoreService;

	private Validator<QueryObject<?>> queryObjectValidator;

	private Converter<BaseStoreData, BaseStore> baseStoreConverter;

	private Converter<BaseStore, BaseStoreData> baseStoreReverseConverter;

	private Populator<BaseStore, BaseStoreData> baseStoreReversePopulator;

	private Converters converters;

	private Validator<BaseStore> baseStoreValidator;

	private FailureHandler entityValidationHandler;

	@Transactional
	@Override
	public BaseStore createBaseStore(final BaseStore baseStore)
	{
		LOG.trace("createBaseStore");
		baseStoreValidator.validate("BaseStore", baseStore, entityValidationHandler);
		final BaseStoreData baseStoreData = baseStoreReverseConverter.convert(baseStore);

		try
		{
			baseStoreService.flush();
		}
		catch (final PrimaryKeyViolationException e)
		{
			throw new DuplicateEntityException("Base store " + baseStore.getName() + " already exists", e);
		}
		return baseStoreConverter.convert(baseStoreData);
	}

	@Transactional
	@Override
	public BaseStore updateBaseStore(final BaseStore baseStore)
	{
		LOG.trace("updateBaseStore");
		baseStoreValidator.validate("BaseStore", baseStore, entityValidationHandler);
		final BaseStoreData baseStoreData = baseStoreService.findBaseStoreByName(baseStore.getName());
		if (baseStoreData == null)
		{
			throw new EntityNotFoundException("BaseStore not found for: name=" + baseStore.getName());
		}
		baseStoreReversePopulator.populate(baseStore, baseStoreData);
		baseStoreService.flush();
		return baseStore;
	}

	@Transactional(readOnly = true)
	@Override
	public BaseStore getBaseStoreByName(final String baseStoreName)
	{
		LOG.trace("getBaseStoreByName {}", baseStoreName);
		final BaseStoreData baseStoreData = baseStoreService.findBaseStoreByName(baseStoreName);
		if (baseStoreData == null)
		{
			throw new EntityNotFoundException("BaseStore not found for: name=" + baseStoreName);
		}
		return baseStoreConverter.convert(baseStoreData);
	}


	@Transactional(readOnly = true)
	@Override
	public Pageable<BaseStore> findAllBaseStoresByQuery(final BaseStoreQueryObject queryObject)
	{
		LOG.trace("findAllBaseStoresByQuery");
		this.queryObjectValidator.validate("BaseStoreQueryObject", queryObject, this.entityValidationHandler);

		final Page<BaseStoreData> pagedbaseStoreDatas = this.baseStoreService.findPagedBaseStoresByQuery(queryObject);
		final List<BaseStore> baseStores = this.converters.convertAll(pagedbaseStoreDatas.getContent(), this.baseStoreConverter);


		final PageInfo pageInfo = new PageInfo();
		pageInfo.setTotalPages(pagedbaseStoreDatas.getTotalPages());
		pageInfo.setTotalResults(pagedbaseStoreDatas.getTotalElements());
		pageInfo.setPageNumber(pagedbaseStoreDatas.getNumber());

		return new PagedResults<BaseStore>(baseStores, pageInfo);
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	@Required
	public void setQueryObjectValidator(final Validator<QueryObject<?>> queryObjectValidator)
	{
		this.queryObjectValidator = queryObjectValidator;
	}

	@Required
	public void setBaseStoreConverter(final Converter<BaseStoreData, BaseStore> baseStoreConverter)
	{
		this.baseStoreConverter = baseStoreConverter;
	}

	@Required
	public void setBaseStoreReverseConverter(final Converter<BaseStore, BaseStoreData> baseStoreReverseConverter)
	{
		this.baseStoreReverseConverter = baseStoreReverseConverter;
	}

	@Required
	public void setBaseStoreReversePopulator(final Populator<BaseStore, BaseStoreData> baseStoreReversePopulator)
	{
		this.baseStoreReversePopulator = baseStoreReversePopulator;
	}

	@Required
	public void setConverters(final Converters converters)
	{
		this.converters = converters;
	}

	@Required
	public void setBaseStoreValidator(final Validator<BaseStore> baseStoreValidator)
	{
		this.baseStoreValidator = baseStoreValidator;
	}

	@Required
	public void setEntityValidationHandler(final FailureHandler entityValidationHandler)
	{
		this.entityValidationHandler = entityValidationHandler;
	}

	protected FailureHandler getEntityValidationHandler()
	{
		return entityValidationHandler;
	}

	protected Converters getConverters()
	{
		return converters;
	}

	protected Validator<BaseStore> getBaseStoreValidator()
	{
		return baseStoreValidator;
	}

	protected Populator<BaseStore, BaseStoreData> getBaseStoreReversePopulator()
	{
		return baseStoreReversePopulator;
	}

	protected Converter<BaseStore, BaseStoreData> getBaseStoreReverseConverter()
	{
		return baseStoreReverseConverter;
	}

	protected Validator<QueryObject<?>> getQueryObjectValidator()
	{
		return queryObjectValidator;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	protected Converter<BaseStoreData, BaseStore> getBaseStoreConverter()
	{
		return baseStoreConverter;
	}
}
