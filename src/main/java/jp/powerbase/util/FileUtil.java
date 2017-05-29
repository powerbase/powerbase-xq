/*
 * @(#)$Id: FileUtil.java 1120 2011-06-13 06:52:24Z hirai $
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUtil {

	public static void delete(File f) {
		if (f.exists() == false) {
			return;
		}

		if (f.isFile()) {
			f.delete();
		}

		if (f.isDirectory()) {
			File[] files = f.listFiles();
			for (int i = 0; i < files.length; i++) {
				delete(files[i]);
			}
			f.delete();
		}
	}

	public static boolean isEmptyDirectory(File dir) {
		if (!dir.isDirectory()) {
			return false;
		}
		if (dir.list().length == 0) {
			return true;
		}
		return false;
	}

	public static String getSuffix(String fileName) {
		if (fileName == null) {
			return "";
		}
		int p = fileName.lastIndexOf(".");
		if (p != -1) {
			return fileName.substring(p + 1);
		}
		return "";
	}

	public static String getHash(InputStream in) throws NoSuchAlgorithmException, IOException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		DigestInputStream input = new DigestInputStream(in, md);

		while (input.read() != -1) {
		}
		byte[] digest = md.digest();
		input.close();

		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < digest.length; i++) {
			String tmp = Integer.toHexString(digest[i] & 0xff);
			if (tmp.length() == 1) {
				buffer.append('0').append(tmp);
			} else {
				buffer.append(tmp);
			}
		}
		return buffer.toString();
	}

	public static String getHash(File f) throws NoSuchAlgorithmException, IOException {
		return getHash(new FileInputStream(f));
	}
}
