/*
 * @(#)$Id: AddPermissionToDatabase.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Request;
import jp.powerbase.Response;
import jp.powerbase.User;
import jp.powerbase.basex.Client;
import jp.powerbase.response.XMLResponse;
import jp.powerbase.xml.DOM;
import jp.powerbase.xmldb.Processor;
import jp.powerbase.xmldb.resource.Database;

public class AddPermissionToDatabase implements Processor {
	protected Database database;

	protected Client client;
	protected Response res;

	protected ArrayList<String> readable = new ArrayList<String>();
	protected ArrayList<String> writable = new ArrayList<String>();

	public AddPermissionToDatabase() {
	}

	public AddPermissionToDatabase(Request req, Response res) throws PowerBaseException {
		client = req.getClient();
		this.res = res;

		database = req.getDatabase();

		if (database == null) {
			throw new PowerBaseException(PowerBaseError.Code.DATABASE_NOT_SPECIFIED);
		}

		ArrayList<String> currentReadable = database.getReadable();
		ArrayList<String> currentWritable = database.getWritable();

		if (!req.getUser().getName().equals(User.ADMINISTRATOR) && !database.getOwner().equals(req.getUser().getName())) {
			throw new PowerBaseException(PowerBaseError.Code.PERMISSION_DENIED);
		}

		DOM dom = new DOM(req.getRequestBody());
		try {

			DOM def = new DOM(dom.evalXpath("/request/permission"));
			Element permission = def.get().getDocumentElement();
			if (permission != null && permission.hasChildNodes()) {
				NodeList readable = permission.getElementsByTagName("readable");
				for (int i = 0; i < readable.getLength(); i++) {
					Element re = (Element) readable.item(i);
					String r = re.getTextContent();
					int count = Integer.valueOf(client.executeXQuery("count(doc('users')/root/node()[@id='" + r + "'])"));
					if (count == 0) {
						throw new PowerBaseException(PowerBaseError.Code.USER_OR_GROUP_NOT_FOUND);
					}
					if (currentReadable.contains(r) ^ getSw()) {
						this.readable.add(r);
					}
				}

				if (database.getType() != Database.Type.QUERY) {
					NodeList writable = permission.getElementsByTagName("writable");
					for (int i = 0; i < writable.getLength(); i++) {
						Element we = (Element) writable.item(i);
						String w = we.getTextContent();
						int count = Integer.valueOf(client.executeXQuery("count(doc('users')/root/node()[@id='" + w + "'])"));
						if (count == 0) {
							throw new PowerBaseException(PowerBaseError.Code.USER_OR_GROUP_NOT_FOUND);
						}

						if (currentWritable.contains(w) ^ getSw()) {
							this.writable.add(w);
						}
					}
				}

			}

		} catch (XPathExpressionException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (ParserConfigurationException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		}
	}

	protected boolean getSw() {
		return true;
	}

	protected void addReadable() throws PowerBaseException {
		if (readable.size() == 0) {
			return;
		}
		String[] queries = new String[readable.size()];

		int c = 0;
		Iterator i = readable.iterator();
		while (i.hasNext()) {
			String r = (String) i.next();
			StringBuilder q = new StringBuilder();
			q.append("insert node <readable>");
			q.append(r);
			q.append("</readable> into db:open-id('databases', ");
			q.append(database.getId());
			q.append(")/permission");
			queries[c++] = q.toString();
		}

		String query = StringUtils.join(queries, ",");

		client.execute(Client.Command.OPEN, "databases");
		client.execute(Client.Command.XQUERY, query.toString());
		client.execute(Client.Command.OPTIMIZE);

	}

	protected void addWritable() throws PowerBaseException {
		if (writable.size() == 0) {
			return;
		}
		String[] queries = new String[writable.size()];

		int c = 0;
		Iterator i = writable.iterator();
		while (i.hasNext()) {
			String w = (String) i.next();
			StringBuilder q = new StringBuilder();
			q.append("insert node <writable>");
			q.append(w);
			q.append("</writable> into db:open-id('databases', ");
			q.append(database.getId());
			q.append(")/permission");
			queries[c++] = q.toString();
		}

		String query = StringUtils.join(queries, ",");

		client.execute(Client.Command.OPEN, "databases");
		client.execute(Client.Command.XQUERY, query.toString());
		client.execute(Client.Command.OPTIMIZE);

	}

	@Override
	public void execute() throws PowerBaseException {
		addReadable();
		addWritable();
		new XMLResponse(res).write(res.getPrinter());

	}

	@Override
	public void setStatus() {
		res.setStatus(HttpServletResponse.SC_OK);
	}

}
