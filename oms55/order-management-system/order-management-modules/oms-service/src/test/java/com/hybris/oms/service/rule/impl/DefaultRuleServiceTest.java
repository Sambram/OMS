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
package com.hybris.oms.service.rule.impl;

import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.inventory.InventoryServiceConstants;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.rule.Operator;
import com.hybris.oms.service.managedobjects.rule.RuleActionData;
import com.hybris.oms.service.managedobjects.rule.RuleConditionsData;
import com.hybris.oms.service.managedobjects.rule.RuleData;
import com.hybris.oms.service.managedobjects.rule.RuleParameterData;
import com.hybris.oms.service.managedobjects.rule.RuleParameterKey;
import com.hybris.oms.service.rule.strategy.InventoryUpdateDto;
import com.hybris.oms.service.rule.strategy.InventoryUpdateStrategy;
import com.hybris.oms.service.rule.strategy.InventoryUpdateStrategyFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultRuleServiceTest
{

	private static final Integer FIVE = 5;
	private static final String FROM_STATUS = "FROM_STATUS";
	private static final String TO_STATUS = "TO_STATUS";
	private static final String LOCATION_ID = "LOCATION_ID";
	private static final String SKU = "SKU";
	private static final String POSITIVE = "TRUE";
	private static final String STRATEGY = "OLQ_QUANTITY";
	private static final String STATUS = "ON_HAND";

	@Spy
	@InjectMocks
	private final DefaultRuleService ruleService = new DefaultRuleService();

	@Mock
	private InventoryService mockInventoryService;
	@Mock
	private InventoryUpdateStrategyFactory mockInventoryUpdateStrategyFactory;
	@Mock
	private InventoryUpdateStrategy mockInventoryUpdateStrategy;
	@Mock
	private PersistenceManager mockPersistenceManager;

	@Mock
	private RuleData mockRule;
	@Mock
	private RuleConditionsData mockRuleCondition;
	@Mock
	private RuleActionData mockRuleAction;
	@Mock
	private RuleParameterData mockStatus;
	@Mock
	private RuleParameterData mockStrategy;
	@Mock
	private RuleParameterData mockPositive;
	@Mock
	private RuleParameterData mockFromStatus;
	@Mock
	private RuleParameterData mockToStatus;
	@Mock
	private OrderLineQuantityData mockOLQ;
	@Mock
	private OrderLineData mockOrderLine;

	private List<RuleParameterData> actionParams;
	private List<RuleParameterData> conditionParams;

	@Before
	public void setUp()
	{
		actionParams = new ArrayList<>();
		actionParams.add(mockStatus);
		actionParams.add(mockStrategy);
		actionParams.add(mockPositive);

		conditionParams = new ArrayList<>();
		conditionParams.add(mockFromStatus);
		conditionParams.add(mockToStatus);

		Mockito.doReturn(Collections.singletonList(mockRule)).when(ruleService).findAllRules();

		Mockito.when(mockInventoryUpdateStrategyFactory.getStrategy(Mockito.anyString())).thenReturn(mockInventoryUpdateStrategy);
		Mockito.when(mockInventoryUpdateStrategy.calculateInventoryUpdateQuantity(Mockito.any(InventoryUpdateDto.class)))
				.thenReturn(FIVE);

		Mockito.when(mockRule.getConditionOperator()).thenReturn(Operator.OR);
		Mockito.when(mockRule.getRuleConditions()).thenReturn(Collections.singletonList(mockRuleCondition));
		Mockito.when(mockRule.getRuleActions()).thenReturn(Collections.singletonList(mockRuleAction));

		Mockito.when(mockRuleAction.getParameters()).thenReturn(actionParams);
		Mockito.when(mockRuleCondition.getParameters()).thenReturn(conditionParams);
		Mockito.when(mockStatus.getKey()).thenReturn(RuleParameterKey.ACTION_INVENTORY_STATUS);
		Mockito.when(mockStrategy.getKey()).thenReturn(RuleParameterKey.ACTION_INVENTORY_STRATEGY);
		Mockito.when(mockPositive.getKey()).thenReturn(RuleParameterKey.ACTION_INVENTORY_POSITIVE);
		Mockito.when(mockStatus.getValue()).thenReturn(STATUS);
		Mockito.when(mockStrategy.getValue()).thenReturn(STRATEGY);
		Mockito.when(mockPositive.getValue()).thenReturn(POSITIVE);
		Mockito.when(mockFromStatus.getKey()).thenReturn(RuleParameterKey.CONDITION_ORDERLINE_PREVIOUS_STATUS);
		Mockito.when(mockToStatus.getKey()).thenReturn(RuleParameterKey.CONDITION_ORDERLINE_CURRENT_STATUS);
		Mockito.when(mockFromStatus.getValue()).thenReturn(FROM_STATUS);
		Mockito.when(mockToStatus.getValue()).thenReturn(TO_STATUS);

		Mockito.when(mockOLQ.getStockroomLocationId()).thenReturn(LOCATION_ID);
		Mockito.when(mockOLQ.getOrderLine()).thenReturn(mockOrderLine);
		Mockito.when(mockOrderLine.getSkuId()).thenReturn(SKU);
	}

	@Test
	public void shouldExecuteOLQStatusChange()
	{
		ruleService.executeOLQStatusChange(FROM_STATUS, TO_STATUS, mockOLQ);
		Mockito.verify(mockInventoryUpdateStrategyFactory).getStrategy(Mockito.anyString());
		Mockito.verify(mockInventoryUpdateStrategy).calculateInventoryUpdateQuantity(Mockito.any(InventoryUpdateDto.class));
		Mockito.verify(mockInventoryService).updateCurrentItemQuantityIncremental(LOCATION_ID,
				InventoryServiceConstants.DEFAULT_BIN, SKU, STATUS, FIVE);
	}

	@Test
	public void shouldSkipExecuteOLQStatusChange()
	{
		ruleService.executeOLQStatusChange("OTHER_STATUS", TO_STATUS, mockOLQ);
		Mockito.verify(mockInventoryService, Mockito.never()).updateCurrentItemQuantityIncremental(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt());
	}
}
