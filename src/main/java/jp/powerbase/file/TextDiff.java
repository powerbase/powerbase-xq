/*
 * @(#)$Id: TextDiff.java 1121 2011-06-13 10:39:57Z hirai $
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import jp.powerbase.test.diff.diff_match_patch;
import jp.powerbase.util.TextReader;

public class TextDiff implements Diff {

	@Override
	public InputStream exec(File oldFile, File newFile) throws IOException {
		diff_match_patch differ = new diff_match_patch();
		String oldF = TextReader.read(oldFile);
		String newF = TextReader.read(newFile);
		LinkedList<jp.powerbase.test.diff.diff_match_patch.Diff> diffs = differ.diff_main(oldF, newF, true);
		LinkedList<jp.powerbase.test.diff.diff_match_patch.Patch> p = differ.patch_make(diffs);
		return new ByteArrayInputStream(differ.patch_toText(p).getBytes());
	}

}
