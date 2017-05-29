/*
 * @(#)$Id: WSSE.java 1093 2011-05-25 06:08:28Z hirai $
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
package jp.powerbase.client;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import jp.powerbase.util.Base64;

public class WSSE {
	public static String getHeader(String userid, String passwd) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		byte[] nonceB = new byte[8];
		SecureRandom.getInstance("SHA1PRNG").nextBytes(nonceB);

		SimpleDateFormat zulu = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		zulu.setTimeZone(TimeZone.getTimeZone("GMT"));
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(System.currentTimeMillis());
		String created = zulu.format(now.getTime());
		byte[] createdB = created.getBytes("utf-8");
		byte[] passwordB = passwd.getBytes("utf-8");

		byte[] v = new byte[nonceB.length + createdB.length + passwordB.length];
		System.arraycopy(nonceB, 0, v, 0, nonceB.length);
		System.arraycopy(createdB, 0, v, nonceB.length, createdB.length);
		System.arraycopy(passwordB, 0, v, nonceB.length + createdB.length, passwordB.length);

		MessageDigest md = MessageDigest.getInstance("SHA1");
		md.update(v);
		byte[] digest = md.digest();

		StringBuffer buf = new StringBuffer();
		buf.append("UsernameToken Username=\"");
		buf.append(userid);
		buf.append("\", PasswordDigest=\"");
		buf.append(Base64.encode(digest));
		buf.append("\", Nonce=\"");
		buf.append(Base64.encode(nonceB));
		buf.append("\", Created=\"");
		buf.append(created);
		buf.append('"');
		return buf.toString();

	}

}
