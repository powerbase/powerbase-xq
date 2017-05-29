/*
 * @(#)$Id: Digest.java 1093 2011-05-25 06:08:28Z hirai $
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
package jp.powerbase.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Digest {

	public static byte[] get(String algorithm, byte[] message) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(algorithm.toUpperCase());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		md.update(message);
		return md.digest();

	}

	@SuppressWarnings("unused")
	public static String get(String algorithm, String message) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm.toUpperCase());
			byte[] dat = message.getBytes();
			md.update(dat);

			if (md != null) {
				StringBuffer s = new StringBuffer();
				byte[] digest = md.digest();
				for (int i = 0; i < digest.length; i++) {
					int d = digest[i];
					if (d < 0) { // byte 128-255
						d += 256;
					}
					if (d < 16) { // 0-15 16
						s.append("0");
					}
					s.append(Integer.toString(d, 16));
				}
				return s.toString();
			} else {
				return "";
			}

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String md5(String message) {
		return get("md5", message);
	}

	public static String sha1(String message) {
		return get("sha1", message);
	}

	public static byte[] md5(byte[] message) {
		return get("md5", message);
	}

	public static byte[] sha1(byte[] message) {
		return get("sha1", message);
	}

}
