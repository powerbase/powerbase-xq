/*
 * @(#)$Id: DefaultDirectory.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.basex.Client;
import jp.powerbase.xml.DOM;

public class DefaultDirectory implements Directory {
	private Client client;
	protected final int id;
	protected final Path path;
	protected final String name;
	protected final String description;
	protected final boolean exists;
	protected final String owner;

	protected ArrayList<String> visible = new ArrayList<String>();

	public DefaultDirectory(Client client, Path path) throws PowerBaseException {
		this.client = client;
		this.path = path;

		try {
			client.execute(Client.Command.OPEN, "databases");
			String xpath = DataNodeUtil.getDirectoryXPath(this.path.toString());
			String xquery = "db:node-id(" + xpath + ")";
			String id = client.executeXQuery(xquery);
			if (id.equals("")) {
				this.exists = false;
				this.id = 0;
				this.name = "";
				this.description = "";
				this.owner = "";
			} else {
				this.exists = true;
				this.id = Integer.valueOf(id);
				String openNode = "db:open-id(\"databases\", " + id + ")";
				DOM dom = new DOM(client.executeXQuery(openNode));

				this.name = dom.getNodeValue("/directory/@name");
				this.description = dom.getNodeValue("/directory/@description");
				this.owner = dom.getNodeValue("/directory/@owner");

				Element root = dom.get().getDocumentElement();

				Element permission = (Element) root.getElementsByTagName("permission").item(0);
				if (permission != null && permission.hasChildNodes()) {
					NodeList readable = permission.getElementsByTagName("visible");
					for (int i = 0; i < readable.getLength(); i++) {
						Element r = (Element) readable.item(i);
						this.visible.add(r.getTextContent());
					}
				}
			}
			client.execute(Client.Command.CLOSE);
		} catch (ParserConfigurationException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (SAXException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (IOException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (XPathExpressionException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		}

	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public Path getPath() {
		return path;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public boolean exists() {
		return exists;
	}

	@Override
	public ArrayList<String> getVisible() {
		return visible;
	}

	@Override
	public String getOwner() {
		return owner;
	}

	@Override
	public Directory getParent() throws PowerBaseException {
		return new DefaultDirectory(client, new Path(path.getParentPath()));
	}

	@Override
	public boolean isRoot() {
		return path.isRoot();
	}

}
