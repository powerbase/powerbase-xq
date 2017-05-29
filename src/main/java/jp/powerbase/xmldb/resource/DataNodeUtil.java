/*
 * @(#)$Id: DataNodeUtil.java 1178 2011-07-22 10:16:56Z hirai $
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
import java.util.Collections;
import java.util.Iterator;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.basex.Client;

public class DataNodeUtil {
	private static ArrayList<String> scan(Client client, ArrayList<String> path, int id) throws PowerBaseException {
		String element = client.executeXQuery("name(db:open-id('databases'," + id + ")/parent::node())");
		if (!element.equals("root")) {
			String name = client.executeXQuery("db:open-id('databases'," + id + ")/parent::node()/string(@name)");
			path.add(name);
			String parentId = client.executeXQuery("db:node-id(db:open-id('databases'," + id + ")/parent::node())");
			path = scan(client, path, Integer.valueOf(parentId));
		}
		return path;
	}

	public static String getLocationPath(Client client, int id) throws PowerBaseException {
		if (id == 1) {
			return "";
		}
		ArrayList<String> path = new ArrayList<String>();
		String dbName = client.executeXQuery("db:open-id('databases'," + id + ")/string(@name)");
		path.add(dbName);
		path = scan(client, path, id);
		Collections.reverse(path);
		StringBuffer p = new StringBuffer();

		Iterator i = path.iterator();
		while (i.hasNext()) {
			p.append("/");
			p.append(i.next());
		}

		return p.toString();
	}

	// db:open-id('databases',11)/parent::node()/string(@name)
	// name(db:open-id('databases',11)/parent::node())

	public static String getDirectoryXPath(String path) throws PowerBaseException {
		StringBuffer xpath = new StringBuffer();
		xpath.append("/root");

		if (!path.equals("") && !path.equals("/")) {
			String paths[] = Path.stripSlash(path).split("/");
			if (!paths[0].equals("")) {
				throw new PowerBaseException(PowerBaseError.Code.INVALID_LOCATION_PATH);
			}
			for (int i = 1; i < paths.length; i++) {
				xpath.append("/directory[@name=\"");
				xpath.append(paths[i]);
				xpath.append("\"]");
			}
		}

		return xpath.toString();
	}

	public static String getDatabaseXPath(String path) throws PowerBaseException {
		StringBuffer xpath = new StringBuffer();
		xpath.append("/root");

		if (path.equals("")) {
			return "";
		}
		String paths[] = Path.stripSlash(path).split("/");

		if (!paths[0].equals("")) {
			throw new PowerBaseException(PowerBaseError.Code.INVALID_LOCATION_PATH);
		}

		for (int i = 1; i < paths.length; i++) {
			if ((i + 1) != paths.length) {
				xpath.append("/directory[@name=\"");
			} else {
				xpath.append("/database[@name=\"");
			}
			xpath.append(paths[i]);
			xpath.append("\"]");
		}

		return xpath.toString();

	}
}
