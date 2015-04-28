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

import static org.junit.Assert.assertEquals;

import com.hybris.commons.conversion.Converter;
import com.hybris.kernel.api.ImportService;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.engine.jdbc.impl.JdbcPersistenceEngine;
import com.hybris.kernel.utils.ClasspathScanningResourceFetcher;
import com.hybris.kernel.utils.ResourceFetcher;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.api.preference.PreferenceFacade;
import com.hybris.oms.api.shipment.ShipmentFacade;
import com.hybris.oms.domain.SortDirection;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.inventory.Bin;
import com.hybris.oms.domain.inventory.BinQuerySupport;
import com.hybris.oms.domain.inventory.ItemStatus;
import com.hybris.oms.domain.order.OrderLineQuantity;
import com.hybris.oms.domain.preference.TenantPreference;
import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.domain.shipping.ShipmentQueryObject;
import com.hybris.oms.domain.shipping.ShipmentQuerySupport;
import com.hybris.oms.facade.inventory.InventoryHelper;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.shipment.ShipmentService;
import com.hybris.oms.service.util.OmsTestUtils;
import com.hybris.oms.ui.api.shipment.PickSlipBinInfo;
import com.hybris.oms.ui.api.shipment.UIShipment;
import com.hybris.oms.ui.api.shipment.UiShipmentFacade;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/oms-ui-facade-spring-test.xml")
public class DefaultUIShipmentFacadeIntegrationTest
{
	private static final String OMS141 = "OMS141";
	private static final String SHIPMENT_ID = "998";
	private static final String SHIPMENT_ID_NO_BINS = "1102";
	private static final String SHIPMENT_ID_INVALID = "2222";
	private static final String STATUS_CODE_ON_HAND = "ON_HAND";
	private static final String LOCATION_1 = "1";
	private static final String LOCATION_2 = "2";
	private static final String SKU_ID_2 = "2";
	private static final String BIN_CODE_1 = "bincode1";
	private static final String BIN_DESC_1 = "bin 1";
	private static final String BIN_CODE_2 = "bincode2";
	private static final String BIN_DESC_2 = "bin 2";
	private static final String BIN_CODE_3 = "bincode3";
	private static final String BIN_DESC_3 = "bin 3";
	private static final String BIN_CODE_4 = "bincode4";
	private static final String BIN_DESC_4 = "bin 4";
	private static final int BIN_PRIORITY_1 = 1;
	private static final int BIN_PRIORITY_2 = 2;
	private static final int BIN_PRIORITY_4 = 4;
	private static final int BIN_PRIORITY_6 = 6;
	private static final int BIN_PRIORITY_8 = 8;
	private static final Bin bin1_1 = InventoryHelper.buildBin(BIN_CODE_1, LOCATION_1, BIN_DESC_1, BIN_PRIORITY_4);
	private static final Bin bin2_1 = InventoryHelper.buildBin(BIN_CODE_2, LOCATION_1, BIN_DESC_2, BIN_PRIORITY_2);
	private static final Bin bin3_1 = InventoryHelper.buildBin(BIN_CODE_3, LOCATION_1, BIN_DESC_3, BIN_PRIORITY_6);
	private static final Bin bin4_1 = InventoryHelper.buildBin(BIN_CODE_4, LOCATION_1, BIN_DESC_4, BIN_PRIORITY_8);
	private static final Bin bin1_2 = InventoryHelper.buildBin(BIN_CODE_1, LOCATION_2, BIN_DESC_1, BIN_PRIORITY_1);
	private final ResourceFetcher fetcher = new ClasspathScanningResourceFetcher();
	@Resource
	private JdbcPersistenceEngine persistenceEngine;
	@Autowired
	private PersistenceManager persistenceManager;
	@Autowired
	private ImportService importService;
	@Resource
	private UiShipmentFacade uiShipmentFacade;
	@Resource
	private ShipmentFacade shipmentFacade;
	@Resource
	private PreferenceFacade preferenceFacade;
	@Resource
	private InventoryFacade inventoryFacade;
	@Autowired
	private ShipmentService shipmentService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private Converter<OrderLineQuantityData, OrderLineQuantity> orderLineQuantityConverter;

	@Before
	public void setUp()
	{
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/order/test-order-data-import.mcsv")[0]);
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/shipment/test-shipment-data-import-standalone.mcsv")[0]);
		this.importService
		.loadMcsvResource(this.fetcher.fetchResources("/tenantPreference/test-tenantPreference-data-import.mcsv")[0]);
	}

	@After
	public void tearDown()
	{
		OmsTestUtils.cleanUp(persistenceEngine);
	}

	@Test
	public void findOrderShipmentsByQuery()
	{
		final ShipmentQueryObject queryObject = new ShipmentQueryObject();
		queryObject.setPageNumber(0);
		queryObject.setPageSize(3);
		queryObject.setPreOrder(true);
		queryObject.setSorting(ShipmentQuerySupport.SHIPMENT_ID, SortDirection.ASC);
		queryObject.setOrderIds(Lists.newArrayList(OMS141));

		final Pageable<UIShipment> pageable1 = this.uiShipmentFacade.findUIShipmentsByQuery(queryObject);
		assertEquals(Integer.valueOf(2), pageable1.getTotalPages());
		final List<UIShipment> result1 = pageable1.getResults();
		assertEquals(3, result1.size());

		assertEquals("1004", result1.get(0).getShipmentId().toString());
		assertEquals("1005", result1.get(1).getShipmentId().toString());
		assertEquals("1006", result1.get(2).getShipmentId().toString());

		queryObject.setPageNumber(1);
		final Pageable<UIShipment> pageable2 = this.uiShipmentFacade.findUIShipmentsByQuery(queryObject);
		assertEquals(Integer.valueOf(2), pageable2.getTotalPages());
		final List<UIShipment> result2 = pageable2.getResults();
		assertEquals(1, result2.size());

		assertEquals("1007", result2.get(0).getShipmentId().toString());
	}

	@Test
	public void getOrderShipmentById()
	{
		final UIShipment UIShipment = this.uiShipmentFacade.getUIShipmentById("1004");
		Assert.assertEquals("1004", UIShipment.getShipmentId().toString());
	}

	@Test(expected = EntityNotFoundException.class)
	public void getOrderShipmentByIdThatDoesNotExist()
	{
		this.uiShipmentFacade.getUIShipmentById("00");
	}

	@Test
	@Transactional
	public void testGetBinInfoForPickSlipByShipmentIdSingleBin()
	{
		createInventoryWithBins(SKU_ID_2, Arrays.asList(bin1_1));

		final PickSlipBinInfo shipmentBins = this.uiShipmentFacade.getBinInfoForPickSlipByShipmentId(SHIPMENT_ID);

		Assert.assertNotNull(shipmentBins);
		Assert.assertEquals(1, shipmentBins.getOrderLineBins().size());

		Assert.assertEquals(1, shipmentBins.getOrderLineBins().get(0).getBins().size());
		assertEqualsBin(bin1_1, shipmentBins.getOrderLineBins().get(0).getBins().get(0));
	}

	@Test
	@Transactional
	public void testGetBinInfoForPickSlipByShipmentIdMultipleBinsSortedByBinCodeASC()
	{
		createInventoryWithBins(SKU_ID_2, Arrays.asList(bin1_1, bin2_1, bin3_1, bin1_2));

		final PickSlipBinInfo shipmentBins = this.uiShipmentFacade.getBinInfoForPickSlipByShipmentId(SHIPMENT_ID);

		Assert.assertNotNull(shipmentBins);
		Assert.assertEquals(1, shipmentBins.getOrderLineBins().size());

		Assert.assertEquals(3, shipmentBins.getOrderLineBins().get(0).getBins().size());
		assertEqualsBin(bin1_1, shipmentBins.getOrderLineBins().get(0).getBins().get(0));

		assertEqualsBin(bin2_1, shipmentBins.getOrderLineBins().get(0).getBins().get(1));

		assertEqualsBin(bin3_1, shipmentBins.getOrderLineBins().get(0).getBins().get(2));
	}

	@Test
	@Transactional
	public void testGetBinInfoForPickSlipByShipmentIdMultipleBinsSortedByPriorityASC()
	{
		createInventoryWithBins(SKU_ID_2, Arrays.asList(bin1_1, bin2_1, bin3_1, bin1_2));

		TenantPreference tenantPreference = this.preferenceFacade
				.getTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_BIN_DIRECTION);
		tenantPreference.setValue(SortDirection.ASC.toString());
		this.preferenceFacade.updateTenantPreference(tenantPreference);

		tenantPreference = this.preferenceFacade.getTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_BIN_SEQUENCING);
		tenantPreference.setValue(BinQuerySupport.BIN_PRIORITY.toString());
		this.preferenceFacade.updateTenantPreference(tenantPreference);

		final PickSlipBinInfo shipmentBins = this.uiShipmentFacade.getBinInfoForPickSlipByShipmentId(SHIPMENT_ID);

		Assert.assertNotNull(shipmentBins);
		Assert.assertEquals(1, shipmentBins.getOrderLineBins().size());

		Assert.assertEquals(3, shipmentBins.getOrderLineBins().get(0).getBins().size());
		assertEqualsBin(bin2_1, shipmentBins.getOrderLineBins().get(0).getBins().get(0));

		assertEqualsBin(bin1_1, shipmentBins.getOrderLineBins().get(0).getBins().get(1));

		assertEqualsBin(bin3_1, shipmentBins.getOrderLineBins().get(0).getBins().get(2));
	}

	@Test
	@Transactional
	public void testGetBinInfoForPickSlipByShipmentIdMultipleBinsSortedByPriorityDESC()
	{
		createInventoryWithBins(SKU_ID_2, Arrays.asList(bin1_1, bin2_1, bin3_1, bin1_2));

		TenantPreference tenantPreference = this.preferenceFacade
				.getTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_BIN_DIRECTION);
		tenantPreference.setValue(SortDirection.DESC.toString());
		this.preferenceFacade.updateTenantPreference(tenantPreference);

		tenantPreference = this.preferenceFacade.getTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_BIN_SEQUENCING);
		tenantPreference.setValue(BinQuerySupport.BIN_PRIORITY.toString());
		this.preferenceFacade.updateTenantPreference(tenantPreference);

		final PickSlipBinInfo shipmentBins = this.uiShipmentFacade.getBinInfoForPickSlipByShipmentId(SHIPMENT_ID);

		Assert.assertNotNull(shipmentBins);
		Assert.assertEquals(1, shipmentBins.getOrderLineBins().size());

		Assert.assertEquals(3, shipmentBins.getOrderLineBins().get(0).getBins().size());
		assertEqualsBin(bin3_1, shipmentBins.getOrderLineBins().get(0).getBins().get(0));

		assertEqualsBin(bin1_1, shipmentBins.getOrderLineBins().get(0).getBins().get(1));

		assertEqualsBin(bin2_1, shipmentBins.getOrderLineBins().get(0).getBins().get(2));
	}

	@Test
	@Transactional
	public void testGetBinInfoForPickSlipByShipmentIdMultipleBinsSortedByDescriptionASC()
	{
		createInventoryWithBins(SKU_ID_2, Arrays.asList(bin1_1, bin2_1, bin3_1, bin1_2));

		final TenantPreference tenantPreference = this.preferenceFacade
				.getTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_BIN_SEQUENCING);
		tenantPreference.setValue(BinQuerySupport.BIN_DESCRIPTION.toString());
		this.preferenceFacade.updateTenantPreference(tenantPreference);

		final PickSlipBinInfo shipmentBins = this.uiShipmentFacade.getBinInfoForPickSlipByShipmentId(SHIPMENT_ID);

		Assert.assertNotNull(shipmentBins);
		Assert.assertEquals(1, shipmentBins.getOrderLineBins().size());

		Assert.assertEquals(3, shipmentBins.getOrderLineBins().get(0).getBins().size());
		assertEqualsBin(bin1_1, shipmentBins.getOrderLineBins().get(0).getBins().get(0));

		assertEqualsBin(bin2_1, shipmentBins.getOrderLineBins().get(0).getBins().get(1));

		assertEqualsBin(bin3_1, shipmentBins.getOrderLineBins().get(0).getBins().get(2));
	}

	@Test
	@Transactional
	public void testGetBinInfoForPickSlipByShipmentIdMultipleBinsLimitToTwo()
	{
		createInventoryWithBins(SKU_ID_2, Arrays.asList(bin1_1, bin2_1, bin3_1, bin4_1));

		TenantPreference tenantPreference = this.preferenceFacade
				.getTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_BIN_DIRECTION);
		tenantPreference.setValue(SortDirection.DESC.toString());
		this.preferenceFacade.updateTenantPreference(tenantPreference);

		tenantPreference = this.preferenceFacade.getTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_BIN_SEQUENCING);
		tenantPreference.setValue(BinQuerySupport.BIN_PRIORITY.toString());
		this.preferenceFacade.updateTenantPreference(tenantPreference);

		tenantPreference = this.preferenceFacade.getTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_BIN_NUMBER);
		tenantPreference.setValue("2");
		this.preferenceFacade.updateTenantPreference(tenantPreference);

		final PickSlipBinInfo shipmentBins = this.uiShipmentFacade.getBinInfoForPickSlipByShipmentId(SHIPMENT_ID);

		Assert.assertNotNull(shipmentBins);
		Assert.assertEquals(1, shipmentBins.getOrderLineBins().size());

		Assert.assertEquals(2, shipmentBins.getOrderLineBins().get(0).getBins().size());
		assertEqualsBin(bin4_1, shipmentBins.getOrderLineBins().get(0).getBins().get(0));

		assertEqualsBin(bin3_1, shipmentBins.getOrderLineBins().get(0).getBins().get(1));
	}

	@Test(expected = EntityNotFoundException.class)
	@Transactional
	public void testGetBinInfoForPickSlipByShipmentIdInvalid()
	{
		this.uiShipmentFacade.getBinInfoForPickSlipByShipmentId(SHIPMENT_ID_INVALID);
		Assert.fail("EntityNotFoundException was expected");
	}

	@Test
	@Transactional
	public void testGetBinInfoForPickSlipByShipmentIdNoBins()
	{
		final PickSlipBinInfo shipmentBins = this.uiShipmentFacade.getBinInfoForPickSlipByShipmentId(SHIPMENT_ID_NO_BINS);

		Assert.assertNotNull(shipmentBins);
		Assert.assertEquals(1, shipmentBins.getOrderLineBins().size());
		Assert.assertTrue(shipmentBins.getOrderLineBins().get(0).getBins().isEmpty());
	}

	private void createInventoryWithBins(final String skuId, final List<Bin> bins)
	{
		final ItemStatus itemStatus = new ItemStatus();
		itemStatus.setStatusCode(STATUS_CODE_ON_HAND);
		itemStatus.setDescription("On Hand");
		inventoryFacade.createItemStatus(itemStatus);

		for (final Bin bin : bins)
		{
			inventoryFacade.createBin(bin);
			final OmsInventory inventory = InventoryHelper.buildInventory(skuId, bin.getLocationId(), bin.getBinCode(),
					STATUS_CODE_ON_HAND, null, 0);
			inventoryFacade.createInventory(inventory);
		}
	}

	private void assertEqualsBin(final Bin expected, final Bin result)
	{
		Assert.assertEquals(expected.getBinCode(), result.getBinCode());
		Assert.assertEquals(expected.getLocationId(), result.getLocationId());
		Assert.assertEquals(expected.getDescription(), result.getDescription());
		Assert.assertEquals(expected.getPriority(), result.getPriority());
	}
}
