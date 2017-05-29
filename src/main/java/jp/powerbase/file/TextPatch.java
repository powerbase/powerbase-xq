/*
 * @(#)$Id: TextPatch.java 1121 2011-06-13 10:39:57Z hirai $
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import jp.powerbase.test.diff.diff_match_patch;
import jp.powerbase.util.IOUtil;
import jp.powerbase.util.TextReader;

public class TextPatch implements Patch {

	@Override
	public void exec(File oldFile, InputStream patch, File newFile) throws IOException {
		diff_match_patch patcher = new diff_match_patch();
		String p = TextReader.read(patch);
		List<jp.powerbase.test.diff.diff_match_patch.Patch> pl = patcher.patch_fromText(p);
		String oldF = TextReader.read(oldFile);
		Object[] o = patcher.patch_apply((LinkedList<jp.powerbase.test.diff.diff_match_patch.Patch>) pl, oldF);
		String res = (String) o[0];
		IOUtil.copy(res.getBytes(), newFile);
	}

}
