/*
 * @(#)$Id: CreateDatabase.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.xmldb.dbmanagement;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;

import jp.powerbase.Group;
import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Request;
import jp.powerbase.Response;
import jp.powerbase.basex.Client;
import jp.powerbase.response.XMLResponse;
import jp.powerbase.servlet.PowerBase;
import jp.powerbase.util.Log;
import jp.powerbase.xml.DOM;
import jp.powerbase.xmldb.Processor;
import jp.powerbase.xmldb.resource.Database;
import jp.powerbase.xmldb.resource.Database.Type;
import jp.powerbase.xmldb.resource.DataNodeUtil;
import jp.powerbase.xmldb.resource.DatabaseFactory;
import jp.powerbase.xmldb.resource.DefaultDirectory;
import jp.powerbase.xmldb.resource.Path;

public class CreateDatabase implements Processor {

	private String parentPath = "";
	private String name = "";
	private String path = "";
	private String root = "";
	private String value = "";
	private Database.Type type = null;

	private Client client;
	private Response res;

	public CreateDatabase(Request req, Response res) throws PowerBaseException {
		client = req.getClient();
		this.res = res;

		DOM dom = new DOM(req.getRequestBody());
		try {
			DOM def = new DOM(dom.evalXpath("/request/database"));
			this.parentPath = req.getDirectoryName();

			if (!new DefaultDirectory(client, new Path(parentPath)).exists()) {
				throw new PowerBaseException(PowerBaseError.Code.PARENT_DIRECTORY_NOT_FOUND);
			}

			if (this.parentPath.equals("")) {
				// unable make database on root directory
				throw new PowerBaseException(PowerBaseError.Code.UNABLE_MAKE_DATABASE_ON_ROOT_DIRECTORY);
			}

			this.name = def.getNodeValue("/database/@name");
			this.root = def.getNodeValue("/database/root/text()");
			type = Database.Type.getType(def.getNodeValue("/database/@type"));
			if (type == Type.FILE) {
				this.root = "files";
			}

			if (this.name.equals("")) {
				throw new PowerBaseException(PowerBaseError.Code.DATABASE_NOT_SPECIFIED);
			}

			if (type == null) {
				throw new PowerBaseException(PowerBaseError.Code.ILLEGAL_DATABASE_DEFINITION_FORMAT);
			}

			path = this.parentPath + "/" + this.name;

			Database db = new DatabaseFactory(client, new Path(path)).getInstance();
			if (db != null && db.exists()) {
				throw new PowerBaseException(PowerBaseError.Code.DATABASE_ALREADY_EXIST);
			}

			Element database = (Element) def.get().getElementsByTagName("database").item(0);
			database.setAttribute("path", this.path);
			database.setAttribute("owner", req.getUser().getName());
			database.setAttribute("version", PowerBase.VERSION.toString());

			Element permission = (Element) def.get().createElement("permission");
			{
				Element readable = (Element) def.get().createElement("readable");
				readable.setTextContent(Group.PUBLIC_GROUP);
				permission.appendChild(readable);

				// Element writable =
				// (Element)def.get().createElement("writable");
				// writable.setTextContent(req.getUser().getUser());
				// permission.appendChild(writable);
			}
			database.appendChild(permission);

			this.value = def.getXML(false);
			Log.debug(this.value);
		} catch (XPathExpressionException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (IOException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (TransformerException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (ParserConfigurationException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		}

		/*
		 * parentKey = req.getDirectories().get(this.parent).key; Databases dbs
		 * = req.getDatabases(); Directories dirs = req.getDirectories(); String
		 * path = dirs.getPath(parentKey);
		 *
		 * if (path.equals("")) { throw new
		 * PowerBaseException(PowerBaseError.Value.PARENT_DIRECTORY_NOT_FOUND);
		 * }
		 *
		 * Database db = dbs.get(path + "/" + this.id); if (db != null) { throw
		 * new PowerBaseException(PowerBaseError.Value.DATABASE_ALREADY_EXIST);
		 * }
		 */
	}

	private void create() throws PowerBaseException {
		// TODO
		// validation
		client.execute(Client.Command.OPEN, "databases");

		StringBuilder q = new StringBuilder();
		q.append("insert node ");
		q.append(value);
		q.append(" into ");
		q.append(DataNodeUtil.getDirectoryXPath(parentPath));
		client.execute(Client.Command.XQUERY, q.toString());

		if (type != Database.Type.DOCUMENT && type != Database.Type.TUPLE && type != Database.Type.FILE) {
			return;
		}

		String xpath = "";
		String id = "";
		try {
			xpath = DataNodeUtil.getDatabaseXPath(path);
			assert (xpath != null && !xpath.equals(""));
			String xquery = "db:node-id(" + xpath + ")";
			id = client.executeXQuery(xquery);
			assert (id != null && !id.equals(""));
			StringBuffer body = new StringBuffer();
			body.append("<");
			body.append(root);
			body.append(" />");

			client.execute(Client.Command.SET, "textindex", "on");
			client.execute(Client.Command.SET, "attrindex", "on");
			client.execute(Client.Command.SET, "ftindex", "on");
			client.execute(Client.Command.CREATE_DB, id, body.toString());
			// client.execute(Client.Command.CREATE_INDEX, "TEXT");
			// client.execute(Client.Command.CREATE_INDEX, "ATTRIBUTE");
			// client.execute(Client.Command.CREATE_INDEX, "FULLTEXT");
			// client.execute(Client.Command.OPTIMIZE);
		} catch (Exception e) {
			client.execute(Client.Command.XQUERY, "delete node db:open-id('databases', " + id + ")");
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		}

	}

	@Override
	public void execute() throws PowerBaseException {
		create();
		new XMLResponse(res).write(res.getPrinter());

	}

	@Override
	public void setStatus() {
		res.setStatus(HttpServletResponse.SC_CREATED);
	}
}
