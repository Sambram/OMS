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
package com.hybris.oms.service.cis.impl;

import com.hybris.cis.api.model.CisOrder;
import com.hybris.cis.client.rest.tax.TaxClient;
import com.hybris.commons.client.RestResponse;
import com.hybris.oms.service.cis.CisConverter;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.shipment.ShipmentService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.net.URI;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.*;



public class TaxServiceUnitTest
{

	@Mock
	private TaxClient taxClient;

	@Mock
	private CisConverter cisConverter;

	@Mock
	private ShipmentService shipmentService;

	@Mock
	private ReturnData aReturnData;

	@Mock
	private ShipmentData aShipmentData;

	@InjectMocks
	@Spy
	private TaxService taxService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}




	@Test
	public void test_revertTax_saveTaxAmountInReturn()
	{
		// given
		doReturn(null).when(taxService).callCisToRevertTax(any(ReturnData.class));

		Double expectedTax = Double.parseDouble("1.1");
		Double unusedTax = Double.parseDouble("2.2");
		Double[] merchandiseTax = { expectedTax, unusedTax };
		doReturn(merchandiseTax).when(taxService).calculateMerchandiseTax(any(RestResponse.class));

		doNothing().when(taxService).setTotalTaxToFirstPaymentInfoOfReturn(any(ReturnData.class), anyDouble());

		// when
		this.taxService.revertTax(aReturnData);

		// then
		verify(this.taxService, Mockito.times(1)).setTotalTaxToFirstPaymentInfoOfReturn(aReturnData, expectedTax);
	}

	@Test
	public void test_callCisToRevertTax_adjustTax()
	{

		// given
		final String returnClientRef = "orderId.returnId";
		doReturn(returnClientRef).when(taxService).buildReturnRefForTaxClient(aReturnData);

		CisOrder aCisOrder = Mockito.mock(CisOrder.class);
		doReturn(aCisOrder).when(taxService).convertReturnToCisParamFormat(aReturnData);

		URI aCisUri = URI.create("http://fakeUri");
		doReturn(aCisUri).when(taxService).fetchCisURILocation(aReturnData);

		doReturn(null).when(taxClient).adjust(returnClientRef, aCisUri, aCisOrder);

		// when
		this.taxService.callCisToRevertTax(aReturnData);

		// then
		verify(this.taxClient, Mockito.times(1)).adjust(returnClientRef, aCisUri, aCisOrder);


	}

}
