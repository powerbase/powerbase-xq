/*
 * @(#)$Id: DES.java 1178 2011-07-22 10:16:56Z hirai $
 *
 * Copyright 2005-2011 Infinite Corporation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Toshio HIRAI - initial implementation
 */
package jp.powerbase.security;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.util.Base64;

public class DES implements jp.powerbase.security.Cipher {
	private Key key;

	public DES(File key) throws PowerBaseException {
		this.key = (Key) CipherUtil.getKey(key);
	}

	public DES(Key key) {
		this.key = key;
	}

	public final static Serializable generateKey() throws NoSuchAlgorithmException {
		KeyGenerator kg = KeyGenerator.getInstance("DES");
		return kg.generateKey();

	}

	@Override
	public String encrypt(String value) throws PowerBaseException {
		byte encrypted[];
		try {
			Cipher c = Cipher.getInstance("DES");
			c.init(Cipher.ENCRYPT_MODE, key);
			byte input[] = value.getBytes();
			encrypted = c.doFinal(input);
		} catch (InvalidKeyException e) {
			throw new PowerBaseException(PowerBaseError.Code.ENCRYPTION_PROCESS_ERROR, e);
		} catch (NoSuchAlgorithmException e) {
			throw new PowerBaseException(PowerBaseError.Code.ENCRYPTION_PROCESS_ERROR, e);
		} catch (NoSuchPaddingException e) {
			throw new PowerBaseException(PowerBaseError.Code.ENCRYPTION_PROCESS_ERROR, e);
		} catch (IllegalBlockSizeException e) {
			throw new PowerBaseException(PowerBaseError.Code.ENCRYPTION_PROCESS_ERROR, e);
		} catch (BadPaddingException e) {
			throw new PowerBaseException(PowerBaseError.Code.ENCRYPTION_PROCESS_ERROR, e);
		}
		return Base64.encode(encrypted);
	}

	@Override
	public String decrypt(String value) throws PowerBaseException {
		byte output[];
		try {
			Cipher c = Cipher.getInstance("DES");
			c.init(Cipher.DECRYPT_MODE, key);
			output = c.doFinal(Base64.decode(value));
		} catch (InvalidKeyException e) {
			throw new PowerBaseException(PowerBaseError.Code.ENCRYPTION_PROCESS_ERROR, e);
		} catch (NoSuchAlgorithmException e) {
			throw new PowerBaseException(PowerBaseError.Code.ENCRYPTION_PROCESS_ERROR, e);
		} catch (NoSuchPaddingException e) {
			throw new PowerBaseException(PowerBaseError.Code.ENCRYPTION_PROCESS_ERROR, e);
		} catch (IllegalBlockSizeException e) {
			throw new PowerBaseException(PowerBaseError.Code.ENCRYPTION_PROCESS_ERROR, e);
		} catch (BadPaddingException e) {
			throw new PowerBaseException(PowerBaseError.Code.ENCRYPTION_PROCESS_ERROR, e);
		} catch (IOException e) {
			throw new PowerBaseException(PowerBaseError.Code.ENCRYPTION_PROCESS_ERROR, e);
		}

		return new String(output);
	}
}
