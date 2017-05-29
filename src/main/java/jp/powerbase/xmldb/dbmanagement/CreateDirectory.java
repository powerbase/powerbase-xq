/*
 * @(#)$Id: CreateDirectory.java 1178 2011-07-22 10:16:56Z hirai $
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
import jp.powerbase.xmldb.resource.DataNodeUtil;
import jp.powerbase.xmldb.resource.DefaultDirectory;
import jp.powerbase.xmldb.resource.Path;

public class CreateDirectory implements Processor {

	private String parentPath = "";
	private String name = "";
	private String path = "";
	private String value = "";

	private Client client;
	private Response res;

	public CreateDirectory(Request req, Response res) throws PowerBaseException {
		client = req.getClient();
		this.res = res;

		DOM dom = new DOM(req.getRequestBody());
		try {
			DOM def = new DOM(dom.evalXpath("/request/directory"));
			this.parentPath = req.getDirectoryName();

			if (!new DefaultDirectory(client, new Path(parentPath)).exists()) {
				throw new PowerBaseException(PowerBaseError.Code.PARENT_DIRECTORY_NOT_FOUND);
			}

			this.name = def.getNodeValue("/directory/@name");

			if (this.name.equals("")) {
				throw new PowerBaseException(PowerBaseError.Code.DIRECTORY_NAME_NOT_SPECIFIED);
			}

			if (this.parentPath.equals("")) {
				this.parentPath = "/";
				this.path = "/" + this.name;
			} else {
				this.path = this.parentPath + "/" + this.name;
			}

			if (new DefaultDirectory(client, new Path(this.path)).exists()) {
				throw new PowerBaseException(PowerBaseError.Code.DIRECTORY_ALREADY_EXIST);
			}

			Element directory = (Element) def.get().getElementsByTagName("directory").item(0);
			directory.setAttribute("path", this.path);
			directory.setAttribute("owner", req.getUser().getName());
			directory.setAttribute("version", PowerBase.VERSION.toString());

			Element permission = (Element) def.get().createElement("permission");
			{
				Element readable = (Element) def.get().createElement("visible");
				readable.setTextContent(Group.PUBLIC_GROUP);
				permission.appendChild(readable);
			}
			directory.appendChild(permission);

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

	}

	private void create() throws PowerBaseException {
		StringBuilder q = new StringBuilder();
		q.append("insert node ");
		q.append(value);
		q.append(" into ");
		q.append(DataNodeUtil.getDirectoryXPath(parentPath));

		client.execute(Client.Command.OPEN, "databases");
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
