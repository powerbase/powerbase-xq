/*
 * @(#)$Id: Path.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.powerbase.PowerBaseException;
import jp.powerbase.basex.Client;
import jp.powerbase.util.StringUtil;

public class Path {
	private List<String> pathList = new ArrayList<String>();

	public Path(String database, int nodeId) {
		this(database + "/" + Integer.toString(nodeId));
	}

	public Path(String path) {
		String p = chopSlash(path);
		if (p.equals("")) {
			return;
		}

		String[] ss = p.split("/");

		for (String s : ss) {
			pathList.add(s);
		}
	}

	public static String stripSlash(String path) {
		StringBuffer res = new StringBuffer();
		String[] ss = path.split("/");
		for (String s : ss) {
			if (!StringUtil.isEmpty(s)) {
				res.append("/");
				res.append(s);
			}
		}
		return res.toString();
	}

	public static String chopSlash(String path) {
		String p = path;
		if (p.equals("")) {
			return "";
		}

		while (true) {
			if (p.startsWith("/")) {
				p = p.substring(1);
			} else {
				break;
			}
		}
		while (true) {
			if (p.endsWith("/")) {
				p = p.substring(0, p.length() - 1);
			} else {
				break;
			}
		}
		return p;
	}

	public void addPath(String node) {
		String p = chopSlash(node);
		String[] pp = p.split("/");
		for (int i = 0; i < pp.length; i++) {
			if (!pp[i].equals("")) {
				pathList.add(pp[i]);
			}
		}

	}

	public boolean isRoot() {
		if (pathList.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isDatabase(Client client) throws PowerBaseException {
		Database db = new DatabaseFactory(client, this).getInstance();
		return db.exists();
	}

	public boolean isDirectory(Client client) throws PowerBaseException {
		Directory dir = new DefaultDirectory(client, this);
		return dir.exists();
	}

	public String toString() {
		return getPath();
	}

	public String getParentPath() {
		return getPath(pathList.size() - 1);
	}

	public String getPath() {
		return getPath(pathList.size());
	}

	public String getPath(int depth) {
		StringBuffer path = new StringBuffer();
		for (int i = 0; i < depth; i++) {
			path.append("/");
			path.append(pathList.get(i));
		}
		return path.toString();
	}

	public Iterator<String> iterator() {
		return pathList.iterator();
	}

	/**
	 * pathList
	 *
	 * @return pathList
	 */
	public List<String> getPathList() {
		return pathList;
	}

}
