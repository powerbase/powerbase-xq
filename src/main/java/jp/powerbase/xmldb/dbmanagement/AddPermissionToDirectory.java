/*
 * @(#)$Id: AddPermissionToDirectory.java 1178 2011-07-22 10:16:56Z hirai $
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
import jp.powerbase.xmldb.resource.Directory;

public class AddPermissionToDirectory implements Processor {
	protected Directory directory;

	protected Client client;
	protected Response res;

	protected ArrayList<String> visible = new ArrayList<String>();

	public AddPermissionToDirectory() {
	}

	public AddPermissionToDirectory(Request req, Response res) throws PowerBaseException {
		client = req.getClient();
		this.res = res;

		directory = req.getDirectory();
		Database database = req.getDatabase();

		if (directory == null) {
			throw new PowerBaseException(PowerBaseError.Code.DIRECTORY_NOT_SPECIFIED);
		}

		if (database != null) {
			throw new PowerBaseException(PowerBaseError.Code.DIRECTORY_NOT_SPECIFIED);
		}

		if (directory.isRoot()) {
			throw new PowerBaseException(PowerBaseError.Code.ACCESS_DENIED);
		}

		ArrayList<String> currentVisible = directory.getVisible();

		if (!req.getUser().getName().equals(User.ADMINISTRATOR) && !directory.getOwner().equals(req.getUser().getName())) {
			throw new PowerBaseException(PowerBaseError.Code.PERMISSION_DENIED);
		}

		DOM dom = new DOM(req.getRequestBody());
		try {
			DOM def = new DOM(dom.evalXpath("/request/permission"));
			Element permission = def.get().getDocumentElement();
			if (permission != null && permission.hasChildNodes()) {
				NodeList visible = permission.getElementsByTagName("visible");
				for (int i = 0; i < visible.getLength(); i++) {
					Element ve = (Element) visible.item(i);
					String v = ve.getTextContent();
					int count = Integer.valueOf(client.executeXQuery("count(doc('users')/root/node()[@id='" + v + "'])"));
					if (count == 0) {
						throw new PowerBaseException(PowerBaseError.Code.USER_OR_GROUP_NOT_FOUND);
					}
					if (currentVisible.contains(v) ^ getSw()) {
						this.visible.add(v);
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

	protected void addVisible() throws PowerBaseException {
		if (visible.size() == 0) {
			return;
		}
		String[] queries = new String[visible.size()];

		int c = 0;
		Iterator i = visible.iterator();
		while (i.hasNext()) {
			String v = (String) i.next();
			StringBuilder q = new StringBuilder();
			q.append("insert node <visible>");
			q.append(v);
			q.append("</visible> into db:open-id('databases', ");
			q.append(directory.getId());
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
		addVisible();
		new XMLResponse(res).write(res.getPrinter());

	}

	@Override
	public void setStatus() {
		res.setStatus(HttpServletResponse.SC_OK);
	}

}
