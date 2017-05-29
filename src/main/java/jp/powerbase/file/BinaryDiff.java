/*
 * @(#)$Id: BinaryDiff.java 1121 2011-06-13 10:39:57Z hirai $
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
package jp.powerbase.file;

import ie.wombat.jbdiff.JBDiff;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import jp.powerbase.util.IOUtil;

public class BinaryDiff implements Diff {

	@Override
	public InputStream exec(File oldFile, File newFile) throws IOException {
		Random rnd = new Random();
		int r = rnd.nextInt(Integer.MAX_VALUE);
		String prefix = Long.toString(System.currentTimeMillis()) + Integer.toString(r);
		File diffFile = File.createTempFile(prefix, ".tmp");
		JBDiff.bsdiff(oldFile, newFile, diffFile);
		byte[] diff = IOUtil.fileToByteArray(diffFile);
		diffFile.delete();
		return new ByteArrayInputStream(diff);
	}

}
