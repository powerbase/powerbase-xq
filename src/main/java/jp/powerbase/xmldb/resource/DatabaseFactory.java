/*
 * @(#)$Id: DatabaseFactory.java 1178 2011-07-22 10:16:56Z hirai $
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

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.basex.Client;

public class DatabaseFactory {
	private Client client;
	private Database.Type type = null;
	private int id;
	private Path path;

	public DatabaseFactory(Client client, Path path) throws PowerBaseException {
		this.client = client;
		this.path = path;

		try {
			this.client.execute(Client.Command.OPEN, "databases");
			String xpath = DataNodeUtil.getDatabaseXPath(path.toString());
			if (xpath.equals("")) {
				return;
			}
			String xquery = "db:node-id(" + xpath + ")";
			String id = this.client.executeXQuery(xquery);
			if (id.equals("")) {
				return;
			}
			String type = this.client.executeXQuery("db:open-id(\"databases\", " + id + ")/fn:string(@type)");
			this.type = Database.Type.getType(type);
			this.id = Integer.valueOf(id);
			this.client.execute(Client.Command.CLOSE);
		} catch (PowerBaseException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		}
	}

	public Database getInstance() throws PowerBaseException {
		Database db = null;
		if (type != null) {
			switch (type) {
			case DOCUMENT:
				db = new DocumentDatabase(client, id, path, type);
				break;
			case TUPLE:
				db = new TupleDatabase(client, id, path, type);
				break;
			case QUERY:
				db = new QueryDatabase(client, id, path, type);
				break;
			case RESOURCE:
				db = new ResourceDatabase(client, id, path, type);
				break;
			case LINK:
				Database link = new LinkDatabase(client, id, path, type);
				db = new DatabaseFactory(client, link.getTarget()).getInstance();
				break;
			case FILE:
				db = new FileDatabase(client, id, path, type);
				break;
			default:
				db = new EmptyDatabase();
				break;
			}
		} else {
			db = new EmptyDatabase();
		}
		client.execute(Client.Command.CLOSE);
		return db;
	}
}
