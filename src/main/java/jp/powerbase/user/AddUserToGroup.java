/*
 * @(#)$Id: AddUserToGroup.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.user;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import jp.powerbase.Group;
import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Request;
import jp.powerbase.Response;
import jp.powerbase.User;
import jp.powerbase.basex.Client;
import jp.powerbase.response.XMLResponse;
import jp.powerbase.xml.DOM;
import jp.powerbase.xmldb.Processor;

public class AddUserToGroup implements Processor {
	protected String user;
	protected List<Group> groups = new ArrayList<Group>();

	protected Client client;
	protected Response res;

	public AddUserToGroup() {
	}

	public AddUserToGroup(Request req, Response res) throws PowerBaseException {
		client = req.getClient();
		this.res = res;

		DOM dom = new DOM(req.getRequestBody());
		try {
			DOM def = new DOM(dom.evalXpath("/request/user"));
			user = def.getNodeValue("/user/@id");
			if (user.equals("")) {
				throw new PowerBaseException(PowerBaseError.Code.USER_ID_NOT_SPECIFIED);
			}
			if (user.equals(User.ADMINISTRATOR)) {
				throw new PowerBaseException(PowerBaseError.Code.PERMISSION_DENIED);
			}
			int count = Integer.valueOf(client.executeXQuery("count(doc('users')/root/user[@id='" + user + "'])"));
			if (count == 0) {
				throw new PowerBaseException(PowerBaseError.Code.USER_NOT_FOUND);
			}

			Element root;
			Document userDoc = def.get();
			root = userDoc.getDocumentElement();

			List<Group> currentGroups = new PowerBaseUser(client, user).getGroups();

			Element groups = (Element) root.getElementsByTagName("groups").item(0);
			if (groups != null && groups.hasChildNodes()) {
				NodeList groupNodes = groups.getElementsByTagName("group");

				for (int i = 0; i < groupNodes.getLength(); i++) {
					Element groupTextNode = (Element) groupNodes.item(i);
					String g = groupTextNode.getTextContent();
					if (g != null) {
						Group group = new PowerBaseGroup(client, g);
						if (!group.isValid()) {
							throw new PowerBaseException(PowerBaseError.Code.GROUP_NOT_FOUND);
						}

						if (!check(g, currentGroups)) {
							this.groups.add(group);
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

	protected boolean check(String group, List<Group> currentGroups) {
		Iterator j = currentGroups.iterator();
		boolean found = false;
		while (j.hasNext()) {
			Group current = (Group) j.next();
			if (group.equals(current.getName())) {
				found = true;
				break;
			}
		}
		return found;
	}

	protected void create() throws PowerBaseException {
		if (groups.size() == 0) {
			return;
		}
		String[] queries = new String[groups.size()];

		int c = 0;
		Iterator i = groups.iterator();
		while (i.hasNext()) {
			Group group = (Group) i.next();
			StringBuilder q = new StringBuilder();
			q.append("insert node <group>");
			q.append(group.getName());
			q.append("</group> into /root/user[@id='");
			q.append(user);
			q.append("']/groups");
			queries[c++] = q.toString();
		}

		String query = StringUtils.join(queries, ",");

		client.execute(Client.Command.OPEN, "users");
		client.execute(Client.Command.XQUERY, query.toString());
		client.execute(Client.Command.OPTIMIZE);

	}

	@Override
	public void execute() throws PowerBaseException {
		create();
		new XMLResponse(res).write(res.getPrinter());

	}

	@Override
	public void setStatus() {
		res.setStatus(HttpServletResponse.SC_OK);
	}

}
