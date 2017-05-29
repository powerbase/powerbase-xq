/*
 * @(#)$Id: CreateGroup.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.xpath.XPathExpressionException;

import jp.powerbase.Group;
import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Request;
import jp.powerbase.Response;
import jp.powerbase.basex.Client;
import jp.powerbase.response.XMLResponse;
import jp.powerbase.xml.DOM;
import jp.powerbase.xmldb.Processor;

public class CreateGroup implements Processor {
	private String group;

	private Client client;
	private Response res;

	public CreateGroup(Request req, Response res) throws PowerBaseException {
		client = req.getClient();
		this.res = res;

		DOM dom = new DOM(req.getRequestBody());
		try {
			DOM def = new DOM(dom.evalXpath("/request/group"));
			String id = def.getNodeValue("/group/@id");
			if (id.equals("")) {
				throw new PowerBaseException(PowerBaseError.Code.GROUP_ID_NOT_SPECIFIED);
			}

			if (id.equals(Group.PUBLIC_GROUP)) {
				throw new PowerBaseException(PowerBaseError.Code.INVALID_GROUP);
			}

			int count = Integer.valueOf(client.executeXQuery("count(doc('users')/root/group[@id='" + id + "'])"));

			if (count != 0) {
				throw new PowerBaseException(PowerBaseError.Code.GROUP_ID_IS_ALREADY_EXIST);
			}

			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			StringWriter sw = new StringWriter();
			XMLStreamWriter writer = factory.createXMLStreamWriter(sw);

			writer.writeStartElement("group");
			writer.writeAttribute("id", id);
			writer.writeEndElement();

			group = sw.toString();

			sw.close();

		} catch (XPathExpressionException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (ParserConfigurationException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (XMLStreamException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (IOException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		}

	}

	private void create() throws PowerBaseException {
		StringBuilder q = new StringBuilder();
		q.append("insert node ");
		q.append(group);
		q.append(" into /root");

		client.execute(Client.Command.OPEN, "users");
		client.execute(Client.Command.XQUERY, q.toString());
		client.execute(Client.Command.OPTIMIZE);
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
