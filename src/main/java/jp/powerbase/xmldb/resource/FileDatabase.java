/*
 * @(#)$Id: FileDatabase.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.xmldb.resource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import jp.powerbase.PowerBaseException;
import jp.powerbase.Settings;
import jp.powerbase.basex.Client;
import jp.powerbase.file.Patch;
import jp.powerbase.file.PatchFactory;
import jp.powerbase.util.FileUtil;
import jp.powerbase.util.IOUtil;

public class FileDatabase extends DefaultDatabase {

	FileDatabase(Client client, int id, Path path, Type type) throws PowerBaseException {
		super(client, id, path, type);

	}

	private static InputStream restore(File f, int rev, String contentType) throws IOException {
		File dir = f.getParentFile();
		assert (dir != null);
		Patch patcher = new PatchFactory(contentType).getInstance();
		File src = f;
		Random rnd = new Random();
		int r = rnd.nextInt(Integer.MAX_VALUE);
		String workDir = Long.toString(System.currentTimeMillis()) + Integer.toString(r);
		File work = new File(System.getProperty("java.io.tmpdir") + "/" + workDir);
		if (!work.exists()) {
			boolean b = work.mkdir();
			assert (!b);
		} else {
			throw new IOException();
		}

		File patched = null;
		for (int i = 2; i <= rev; i++) {
			patched = new File(work, Integer.toString(i));
			InputStream patch = new FileInputStream(new File(dir, Integer.toString(i)));
			patcher.exec(src, patch, patched);
			src = patched;
		}
		assert (patched != null);
		ByteArrayInputStream res = new ByteArrayInputStream(IOUtil.fileToByteArray(patched));
		FileUtil.delete(work);
		return res;
	}

	public static InputStream load(Database db, int id, int rev, String contentType) throws IOException {
		File original = new File(Settings.get(Settings.Symbol.DATA_DIR) + "/files/" + Integer.toString(db.getId()) + "/" + Integer.toString(id) + "/1");
		if (rev == 1) {
			return new ByteArrayInputStream(IOUtil.fileToByteArray(original));
		} else {
			return restore(original, rev, contentType);
		}
	}

	public static void save(Database db, int id, int rev, InputStream in) throws IOException {
		File dirDB = new File(Settings.get(Settings.Symbol.DATA_DIR) + "/files/" + Integer.toString(db.getId()));
		if (!dirDB.exists()) {
			boolean b = dirDB.mkdir();
			if (!b) {
				throw new IOException();
			}
		}
		File dirID = new File(dirDB, Integer.toString(id));
		if (!dirID.exists()) {
			boolean b = dirID.mkdir();
			if (!b) {
				throw new IOException();
			}
		}
		File output = new File(dirID, Integer.toString(rev));
		IOUtil.copy(in, output);

	}

	public static void delete(Database db, int id, int rev) throws IOException {
		File dirDB = new File(Settings.get(Settings.Symbol.DATA_DIR) + "/files/" + Integer.toString(db.getId()));
		File dirID = new File(dirDB, Integer.toString(id));
		File output = new File(dirID, Integer.toString(rev));
		if (output.exists()) {
			output.delete();
		}
	}

	@Override
	public ArrayList<Heading> getHeadings() {
		ArrayList<Heading> headings = new ArrayList<Heading>();
		headings.add(new Heading(1, "@name"));
		headings.add(new Heading(2, "@type", "&lt;", "&gt;"));
		return headings;
	}

	@Override
	public String getTuplePath() {
		return "/files/file";
	}

	@Override
	public String getRootTag() {
		return "files";
	}

	@Override
	public String getTupleTag() {
		return "file";
	}

	@Override
	public String getQuery() {
		return "";
	}

	@Override
	public boolean isTuple() {
		return true;
	}

	@Override
	public URL getUrl() {
		return null;
	}

	@Override
	public Path getTarget() {
		return null;
	}

}
