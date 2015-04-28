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

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.Charsets;


/**
 * The Class SecureEncoder.
 */
public class SecureEncoder
{

	/**
	 * The Constant RADIX.
	 */
	private static final int RADIX = 16;

	/**
	 * The cipher.
	 */
	private final transient Cipher cipher;

	/**
	 * The key.
	 */
	private final transient SecretKeySpec key;

	/**
	 * Instantiates a new secure encoder.
	 * 
	 * @param secretKey the secret key
	 * @param algorithm (DES, RSA, ...)
	 * @throws GeneralSecurityException the general security exception
	 */
	public SecureEncoder(final String secretKey, final String algorithm) throws GeneralSecurityException
	{

		this.key = new SecretKeySpec(secretKey.getBytes(Charsets.UTF_8), algorithm);
		this.cipher = Cipher.getInstance(algorithm);
	}

	/**
	 * Decode.
	 * 
	 * @param theStringToDecode the the string to decode
	 * @return the string
	 * @throws InvalidKeyException the invalid key exception
	 * @throws IllegalBlockSizeException the illegal block size exception
	 * @throws BadPaddingException the bad padding exception
	 */
	public String decode(final String theStringToDecode) throws InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException
	{
		this.cipher.init(Cipher.DECRYPT_MODE, this.key);
		final BigInteger secret = new BigInteger(theStringToDecode, RADIX);
		final byte[] encoding = this.cipher.doFinal(secret.toByteArray());
		return new String(encoding, Charsets.UTF_8);
	}

	/**
	 * Encode.
	 * 
	 * @param theStringToEncode the the string to encode
	 * @return the string
	 * @throws InvalidKeyException the invalid key exception
	 * @throws IllegalBlockSizeException the illegal block size exception
	 * @throws BadPaddingException the bad padding exception
	 */
	public String encode(final String theStringToEncode) throws InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException
	{
		this.cipher.init(Cipher.ENCRYPT_MODE, this.key);
		final byte[] encoding = this.cipher.doFinal(theStringToEncode.getBytes(Charsets.UTF_8));
		return new BigInteger(encoding).toString(RADIX);
	}
}
