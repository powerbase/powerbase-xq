/*
 * @(#)$Id: Cipher.java 1178 2011-07-22 10:16:56Z hirai $
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

import jp.powerbase.PowerBaseException;

public interface Cipher {
	public enum Type {
		RSA("rsa"),
		DES("des"),
		NONE("none"),
		;
		public final String value;

		Type(String type) {
			this.value = type;
		}

		public static Type getType(String value) {
			for (Type val : Type.values()) {
				if (val.value.equalsIgnoreCase(value)) {
					return val;
				}
			}
			return null;
		}
	}

	public abstract String encrypt(String value) throws PowerBaseException;

	public abstract String decrypt(String value) throws PowerBaseException;

}
