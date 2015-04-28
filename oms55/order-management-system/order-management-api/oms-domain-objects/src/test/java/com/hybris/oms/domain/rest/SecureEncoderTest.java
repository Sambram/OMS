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
package com.hybris.oms.domain.rest;

import static org.junit.Assert.fail;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.IllegalBlockSizeException;

import org.junit.Assert;
import org.junit.Test;


/**
 * The Class SecureEncoderTest.
 */
public class SecureEncoderTest
{

	/**
	 * The Constant A_TEST_STRING.
	 */
	private static final String A_TEST_STRING = "Content-Length: 27" + "\n" + "Content-Type: application/x-www-form-urlencoded"
			+ "\n" + "Date" + "\n" + "userid=andy&password=guessme";

	/**
	 * The Constant A_VALID_ALGORITHM_NAME.
	 */
	private static final String A_VALID_ALGORITHM_NAME = "Blowfish";

	/**
	 * The Constant A_VALID_ENCODED_STRING.
	 */
	private static final String A_VALID_ENCODED_STRING = "-67ab3e5219711e9c5b6d4442e8117f3d22630630544ba3f524b85a33307da2a15"
			+ "a2b532970f23238c802ef2c1f7dd83ecc8728b9bb374b91ad68b9dc641457607716fddd5866712fd5c4cadbff63af574bc59be9215c610529"
			+ "fb6e9930b1a4e14cc74ed45a21b8b9";

	/**
	 * The Constant A_VALID_SECRET_PHRASE.
	 */
	private static final String A_VALID_SECRET_PHRASE = "hybris is my way";

	/**
	 * The Constant AN_INVALID_ALGORITHM_NAME.
	 */
	private static final String AN_INVALID_ALGORITHM_NAME = "nonExistingCipherAlgorithmName";

	/**
	 * The Constant AN_INVALID_ENCODED_STRING.
	 */
	private static final String AN_INVALID_ENCODED_STRING = "-67";

	/**
	 * The Constant AN_INVALID_SECRET_PHRASE.
	 */
	private static final String AN_INVALID_SECRET_PHRASE =
			"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

	/**
	 * Test decode doesnt work with invalid encoded string.
	 */
	@Test
	public void testDecodeDoesntWorkWithInvalidEncodedString()
	{
		SecureEncoder encoder;
		try
		{
			encoder = new SecureEncoder(A_VALID_SECRET_PHRASE, A_VALID_ALGORITHM_NAME);
			encoder.decode(AN_INVALID_ENCODED_STRING);
			fail("Should have thrown an IllegalBlockSizeException");
		}
		catch (final IllegalBlockSizeException e)
		{
			// expected
		}
		catch (final GeneralSecurityException e)
		{
			fail("Should have thrown an IllegalBlockSizeException");
		}
	}

	/**
	 * Test decode works with valid secret and algorithm.
	 * 
	 * @throws GeneralSecurityException the general security exception
	 */
	@Test
	public void testDecodeWorksWithValidSecretAndAlgorithm() throws GeneralSecurityException
	{
		final SecureEncoder encoder = new SecureEncoder(A_VALID_SECRET_PHRASE, A_VALID_ALGORITHM_NAME);
		final String decodedString = encoder.decode(A_VALID_ENCODED_STRING);
		Assert.assertEquals(A_TEST_STRING, decodedString);
	}

	/**
	 * Test encode works with valid secret and algorithm.
	 * 
	 * @throws GeneralSecurityException the general security exception
	 */
	@Test
	public void testEncodeWorksWithValidSecretAndAlgorithm() throws GeneralSecurityException
	{
		final SecureEncoder encoder = new SecureEncoder(A_VALID_SECRET_PHRASE, A_VALID_ALGORITHM_NAME);
		final String encodedString = encoder.encode(A_TEST_STRING);
		Assert.assertEquals(A_VALID_ENCODED_STRING, encodedString);
	}

	/**
	 * Test secure decode doesnt work when secret key is wrong.
	 */
	@Test
	public void testSecureDecodeDoesntWorkWhenSecretKeyIsWrong()
	{
		try
		{
			final SecureEncoder encoder = new SecureEncoder(AN_INVALID_SECRET_PHRASE, A_VALID_ALGORITHM_NAME);
			encoder.decode(A_TEST_STRING);
			fail("Should have thrown an Exception");
		}
		catch (final NumberFormatException | InvalidKeyException e)
		{
			// expected
		}
		catch (final GeneralSecurityException e)
		{
			fail("Should not have thrown a " + e.getClass());
		}
	}

	/**
	 * Test secure encoder throws no such algorithm exception when invalid algorithm specified.
	 */
	@Test
	public void testSecureEncoderThrowsNoSuchAlgorithmExceptionWhenInvalidAlgorithmSpecified()
	{
		try
		{
			new SecureEncoder(A_VALID_SECRET_PHRASE, AN_INVALID_ALGORITHM_NAME);
			fail("Should have thrown a NoSuchAlgorithmException");
		}
		catch (final NoSuchAlgorithmException e)
		{
			// expected
		}
		catch (final GeneralSecurityException e)
		{
			fail("Should have thrown a NoSuchAlgorithmException");
		}
	}

	/**
	 * Test secure encode throws invalid key exception when key length not appropriate.
	 */
	@Test
	public void testSecureEncodeThrowsInvalidKeyExceptionWhenKeyLengthNotAppropriate()
	{
		try
		{
			// Blowfish key length must be between 5 and 56 bytes, but strange behavior on bamboo
			final SecureEncoder encoder = new SecureEncoder(AN_INVALID_SECRET_PHRASE, A_VALID_ALGORITHM_NAME);
			encoder.encode(A_TEST_STRING);

			fail("Should have thrown an InvalidKeyException");
		}
		catch (final InvalidKeyException e)
		{
			// expected
			Assert.assertEquals(InvalidKeyException.class, e.getClass());
		}
		catch (final GeneralSecurityException e)
		{
			fail("Should have thrown a InvalidKeyException but threw a " + e);
		}
	}

}
