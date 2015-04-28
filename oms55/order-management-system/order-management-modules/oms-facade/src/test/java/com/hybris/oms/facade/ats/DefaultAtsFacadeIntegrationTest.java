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
package com.hybris.oms.facade.ats;

import com.hybris.kernel.api.HybrisId;
import com.hybris.kernel.api.ImportService;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.regioncache.CacheController;
import com.hybris.kernel.utils.ClasspathScanningResourceFetcher;
import com.hybris.kernel.utils.ResourceFetcher;
import com.hybris.oms.api.ats.AtsFacade;
import com.hybris.oms.domain.ats.AtsFormula;
import com.hybris.oms.domain.ats.AtsLocalQuantities;
import com.hybris.oms.domain.ats.AtsQuantities;
import com.hybris.oms.domain.ats.AtsQuantity;
import com.hybris.oms.domain.exception.DuplicateEntityException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.service.managedobjects.inventory.CurrentItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.preference.TenantPreferenceData;
import com.hybris.oms.service.preference.TenantPreferenceService;
import com.hybris.oms.service.util.OmsTestUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


/**
 * Integration test for {@link com.hybris.oms.service.ats.impl.DefaultAtsService}.
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class DefaultAtsFacadeIntegrationTest
{
	private static final String ATS_ID = "test";

	@Autowired
	private AtsFacade facade;

	@Autowired
	private ImportService importService;

	@Autowired
	private PersistenceManager persistenceManager;

	@Autowired
	private TenantPreferenceService tenantPreferenceService;

	@Autowired
	private CacheController cacheController;

	@Before
	public void setUp()
	{
		final ResourceFetcher fetcher = new ClasspathScanningResourceFetcher();
		this.importService.loadMcsvResource(fetcher.fetchResources("/inventory/test-inventory-data-import.mcsv")[0]);
		this.importService.loadMcsvResource(fetcher.fetchResources("/tenantPreference/test-tenantPreference-data-import.mcsv")[0]);
	}

	@Test
	@Transactional
	public void shouldMaintainFormula() throws DuplicateEntityException, EntityNotFoundException
	{
		final AtsFormula formula = this.buildFormula();
		this.facade.createFormula(formula);
		this.persistenceManager.flush();
		this.verifyResult(formula, this.facade.getFormulaById(ATS_ID));
		final Collection<AtsFormula> formulas = this.facade.findAllFormulas();
		AtsFormula testResult = null;
		for (final AtsFormula result : formulas)
		{
			if (result.getAtsId().equals(ATS_ID))
			{
				testResult = result;
				break;
			}
		}
		this.verifyResult(formula, testResult);
		formula.setName("name2");
		formula.setDescription("description2");
		formula.setFormula("I[ST]+I[ON_HAND]");
		this.facade.updateFormula(ATS_ID, formula);
		this.verifyResult(formula, this.facade.getFormulaById(ATS_ID));
		this.facade.deleteFormula(ATS_ID);
		this.persistenceManager.flush();
		try
		{
			this.facade.getFormulaById(ATS_ID);
			Assert.fail();
		}
		catch (@SuppressWarnings("unused") final EntityNotFoundException ignored)
		{
			// success
		}
	}

	@Test(expected = DuplicateEntityException.class)
	@Transactional
	public void shouldThrowDuplicateFormula() throws DuplicateEntityException, EntityNotFoundException
	{
		final AtsFormula formula = this.buildFormula();
		this.facade.createFormula(formula);
		this.persistenceManager.flush();
		this.facade.createFormula(formula);
		Assert.fail();
	}

	@Test(expected = EntityValidationException.class)
	@Transactional
	public void shouldValidateFormulaCreate() throws DuplicateEntityException, EntityNotFoundException
	{
		final AtsFormula formula = this.buildFormula();
		formula.setFormula("I[INVALID]");
		this.facade.createFormula(formula);
		Assert.fail();
	}

	@Test(expected = EntityValidationException.class)
	@Transactional
	public void shouldValidateFormulaUpdate() throws DuplicateEntityException, EntityNotFoundException
	{
		final AtsFormula formula = this.buildFormula();
		this.facade.createFormula(formula);
		this.persistenceManager.flush();

		formula.setFormula("I[INVALID]");
		this.facade.updateFormula(ATS_ID, formula);
		Assert.fail();
	}

	@Test
	@Transactional
	public void shouldCalculateGlobalAts() throws EntityNotFoundException, DuplicateEntityException
	{
		this.facade.createFormula(this.buildFormula());
		this.persistenceManager.flush();
		final AtsQuantities result = this.facade.findGlobalAts(Collections.singleton("NPEYBO"), Collections.singleton(ATS_ID));
		assertValidGlobalAtsResult(result, 105);
	}

	@Test(expected = EntityValidationException.class)
	@Transactional
	public void shouldValidateFindGlobalEmptySkus() throws EntityNotFoundException, DuplicateEntityException
	{
		this.facade.findGlobalAts(Collections.<String>emptySet(), Collections.singleton(ATS_ID));
		Assert.fail();
	}

	@Test(expected = EntityValidationException.class)
	@Transactional
	public void shouldValidateFindLocalEmptySkus() throws EntityNotFoundException, DuplicateEntityException
	{
		this.facade.findLocalAts(Collections.<String>emptySet(), null, Collections.singleton(ATS_ID));
		Assert.fail();
	}

	@Test
	@Transactional
	public void shouldSubtractThresholdGlobalAts() throws EntityNotFoundException, DuplicateEntityException
	{
		this.facade.createFormula(this.buildFormula());
		final TenantPreferenceData pref = this.tenantPreferenceService
				.getTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_ATS_GLOBAL_THRHOLD);
		pref.setValue("5");
		this.persistenceManager.flush();
		final AtsQuantities result = this.facade.findGlobalAts(Collections.singleton("NPEYBO"), Collections.singleton(ATS_ID));
		assertValidGlobalAtsResult(result, 100);
	}

	private void assertValidGlobalAtsResult(final AtsQuantities result, final int value, final String sku)
	{
		Assertions.assertThat(result.getAtsQuantities()).hasSize(1);
		final AtsQuantity quantity = result.getAtsQuantities().iterator().next();
		Assert.assertEquals(ATS_ID, quantity.getAtsId());
		Assert.assertEquals(sku, quantity.getSku());
		Assert.assertEquals("units", quantity.getQuantity().getUnitCode());
		Assert.assertEquals(value, quantity.getQuantity().getValue());
	}

	private void assertValidGlobalAtsResult(final AtsQuantities result, final int value)
	{
		assertValidGlobalAtsResult(result, value, "NPEYBO");
	}

	@Test
	@Transactional
	public void shouldCalculateLocalAts() throws EntityNotFoundException, DuplicateEntityException
	{
		this.setupAts();
		final List<AtsLocalQuantities> result = this.facade.findLocalAts(Collections.singleton("OMS97_sku1"),
				Collections.singleton("OMS97_location1"), Collections.singleton(ATS_ID));
		assertValidLocalAtsResult(result, 4);
	}

	@Test
	@Transactional
	public void shouldSubtractAbsoluteThresholdLocalAts() throws EntityNotFoundException, DuplicateEntityException
	{
		final StockroomLocationData loc = this.persistenceManager.getByIndex(
				StockroomLocationData.UX_STOCKROOMLOCATIONS_LOCATIONID, "OMS97_location1");
		loc.setAbsoluteInventoryThreshold(5);
		this.setupAts();
		final List<AtsLocalQuantities> result = this.facade.findLocalAts(Collections.singleton("OMS97_sku1"),
				Collections.singleton("OMS97_location1"), Collections.singleton(ATS_ID));
		assertValidLocalAtsResult(result, -1);
	}

	@Test
	@Transactional
	public void shouldSubtractPercentageThresholdLocalAts() throws EntityNotFoundException, DuplicateEntityException
	{
		final StockroomLocationData loc = this.persistenceManager.getByIndex(
				StockroomLocationData.UX_STOCKROOMLOCATIONS_LOCATIONID, "OMS97_location1");
		loc.setPercentageInventoryThreshold(50);
		loc.setUsePercentageThreshold(true);
		this.setupAts();
		final List<AtsLocalQuantities> result = this.facade.findLocalAts(Collections.singleton("OMS97_sku1"),
				Collections.singleton("OMS97_location1"), Collections.singleton(ATS_ID));
		assertValidLocalAtsResult(result, 2);
	}

	@Test
	@Transactional
	public void shouldMaintainAtsWhenDeletingItemLocation()
	{
		this.setupAts();

		// Verify initial local + global ATS for 2 skus in the same bin.
		List<AtsLocalQuantities> localATS = this.facade.findLocalAts(Collections.singleton("0000000001"),
				Collections.singleton("1"), Collections.singleton(ATS_ID));
		this.assertValidLocalAtsResult(localATS, 6, "0000000001");
		localATS = this.facade.findLocalAts(Collections.singleton("0000000004"), Collections.singleton("1"),
				Collections.singleton(ATS_ID));
		this.assertValidLocalAtsResult(localATS, 5, "0000000004");

		AtsQuantities globalATS = this.facade.findGlobalAts(Collections.singleton("0000000001"), Collections.singleton(ATS_ID));
		this.assertValidGlobalAtsResult(globalATS, 60, "0000000001");
		globalATS = this.facade.findGlobalAts(Collections.singleton("0000000004"), Collections.singleton(ATS_ID));
		this.assertValidGlobalAtsResult(globalATS, 16, "0000000004");

		this.persistenceManager.remove(HybrisId.valueOf("single|ItemLocationData|101"));
		this.persistenceManager.remove(HybrisId.valueOf("single|ItemLocationData|102"));
		this.persistenceManager.remove(HybrisId.valueOf("single|ItemLocationData|104"));
		this.persistenceManager.flush();
		OmsTestUtils.clearCaches(cacheController);

		// Verify post delete local + global ATS for 2 skus with same bin
		localATS = this.facade.findLocalAts(Collections.singleton("0000000001"), Collections.singleton("1"),
				Collections.singleton(ATS_ID));
		this.assertValidLocalAtsResult(localATS, 1, "0000000001");
		localATS = this.facade.findLocalAts(Collections.singleton("0000000004"), Collections.singleton("1"),
				Collections.singleton(ATS_ID));
		this.assertValidLocalAtsResult(localATS, 0, "0000000004");

		globalATS = this.facade.findGlobalAts(Collections.singleton("0000000001"), Collections.singleton(ATS_ID));
		this.assertValidGlobalAtsResult(globalATS, 55, "0000000001");
		globalATS = this.facade.findGlobalAts(Collections.singleton("0000000004"), Collections.singleton(ATS_ID));
		this.assertValidGlobalAtsResult(globalATS, 11, "0000000004");
	}

	@Test
	@Transactional
	public void shouldMaintainAtsWhenDeletingItemQuantity()
	{
		this.setupAts();

		// Verify initial local + global ATS
		List<AtsLocalQuantities> localATS = this.facade.findLocalAts(Collections.singleton("0000000001"),
				Collections.singleton("1"), Collections.singleton(ATS_ID));
		this.assertValidLocalAtsResult(localATS, 6, "0000000001");

		AtsQuantities globalATS = this.facade.findGlobalAts(Collections.singleton("0000000001"), Collections.singleton(ATS_ID));
		this.assertValidGlobalAtsResult(globalATS, 60, "0000000001");

		final CurrentItemQuantityData iqData = this.persistenceManager.get(HybrisId.valueOf("single|CurrentItemQuantityData|101"));
		Assert.assertNotNull(iqData);

		this.persistenceManager.remove(HybrisId.valueOf("single|CurrentItemQuantityData|101"));
		this.persistenceManager.flush();
		OmsTestUtils.clearCaches(cacheController);

		// Verify post delete local + global ATS
		localATS = facade.findLocalAts(Collections.singleton("0000000001"), Collections.singleton("1"),
				Collections.singleton(ATS_ID));
		this.assertValidLocalAtsResult(localATS, 1, "0000000001");

		globalATS = facade.findGlobalAts(Collections.singleton("0000000001"), Collections.singleton(ATS_ID));
		this.assertValidGlobalAtsResult(globalATS, 55, "0000000001");

	}

	private void setupAts()
	{
		this.facade.createFormula(this.buildFormula());
		this.persistenceManager.flush();
	}

	private void assertValidLocalAtsResult(final List<AtsLocalQuantities> result, final int value, final String sku)
	{
		Assertions.assertThat(result).hasSize(1);
		final AtsLocalQuantities localQuantities = result.iterator().next();
		Assertions.assertThat(localQuantities.getAtsQuantities()).hasSize(1);
		final AtsQuantity quantity = localQuantities.getAtsQuantities().iterator().next();
		Assert.assertEquals(ATS_ID, quantity.getAtsId());
		Assert.assertEquals(sku, quantity.getSku());
		Assert.assertEquals("units", quantity.getQuantity().getUnitCode());
		Assert.assertEquals(value, quantity.getQuantity().getValue());
	}

	private void assertValidLocalAtsResult(final List<AtsLocalQuantities> result, final int value)
	{
		assertValidLocalAtsResult(result, value, "OMS97_sku1");
	}

	private AtsFormula buildFormula()
	{
		final AtsFormula formula = new AtsFormula();
		formula.setAtsId(ATS_ID);
		formula.setName("name");
		formula.setDescription("description");
		formula.setFormula("I[ON_HAND]-T");
		return formula;
	}

	private void verifyResult(final AtsFormula formula, final AtsFormula result)
	{
		Assert.assertNotNull(result);
		Assert.assertEquals(formula.getAtsId(), result.getAtsId());
		Assert.assertEquals(formula.getName(), result.getName());
		Assert.assertEquals(formula.getDescription(), result.getDescription());
	}

}
