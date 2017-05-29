/*
 * @(#)$Id: Base64.java 1093 2011-05-25 06:08:28Z hirai $
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

import java.io.IOException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import org.apache.commons.lang.StringUtils;

public class Base64 {
	private static BASE64Encoder encoder;
	private static BASE64Decoder decoder;

	static {
		encoder = new BASE64Encoder();
		decoder = new BASE64Decoder();
	}

	public static String encode(String str) {
		String encodedStr = encoder.encodeBuffer(str.getBytes());
		return StringUtils.chop(encodedStr);
	}

	public static String encode(byte[] data) {
		String encodedStr = encoder.encodeBuffer(data);
		return StringUtils.chop(encodedStr);
	}

	public static byte[] decode(String str) throws IOException {
		byte[] decodedStr = null;
		decodedStr = decoder.decodeBuffer(str);
		return decodedStr;

	}
}
