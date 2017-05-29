/*
 * @(#)$Id: CreateUser.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.security.KeyPair;

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
import jp.powerbase.Settings;
import jp.powerbase.User;
import jp.powerbase.basex.Client;
import jp.powerbase.response.XMLResponse;
import jp.powerbase.security.Cipher;
import jp.powerbase.security.CipherUtil;
import jp.powerbase.security.RSA;
import jp.powerbase.xml.DOM;
import jp.powerbase.xmldb.Processor;

public class CreateUser implements Processor {
	private String user;

	private Client client;
	private Response res;

	public CreateUser(Request req, Response res) throws PowerBaseException {
		client = req.getClient();
		this.res = res;

		DOM dom = new DOM(req.getRequestBody());
		try {
			DOM def = new DOM(dom.evalXpath("/request/user"));
			String id = def.getNodeValue("/user/@id");
			if (id.equals("")) {
				throw new PowerBaseException(PowerBaseError.Code.USER_ID_NOT_SPECIFIED);
			}

			if (id.equals(User.ADMINISTRATOR)) {
				throw new PowerBaseException(PowerBaseError.Code.INVALID_USER);
			}

			int count = Integer.valueOf(client.executeXQuery("count(doc('users')/root/user[@id='" + id + "'])"));

			if (count != 0) {
				throw new PowerBaseException(PowerBaseError.Code.USER_ID_IS_ALREADY_EXIST);
			}

			String name = def.getNodeValue("/user/name/text()");
			String password = def.getNodeValue("/user/password/text()");
			if (password.equals("")) {
				throw new PowerBaseException(PowerBaseError.Code.PASSWORD_IS_EMPTY);
			}

			KeyPair rsaKey = (KeyPair) CipherUtil.getKey(new File(Settings.get(Settings.Symbol.DATA_DIR), ".rsa"));

			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			StringWriter sw = new StringWriter();
			XMLStreamWriter writer = factory.createXMLStreamWriter(sw);

			writer.writeStartElement("user");
			writer.writeAttribute("id", id);
			{
				writer.writeStartElement("name");
				writer.writeCharacters(name);
				writer.writeEndElement();

				writer.writeStartElement("password");
				writer.writeAttribute("encryption", "RSA");
				Cipher r = new RSA(rsaKey);
				writer.writeCharacters(r.encrypt(password));
				writer.writeEndElement();

				writer.writeStartElement("groups");
				{
					writer.writeStartElement("group");
					writer.writeCharacters(Group.PUBLIC_GROUP);
					writer.writeEndElement();
				}
				writer.writeEndElement();

			}
			writer.writeEndElement();

			user = sw.toString();

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
		q.append(user);
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
