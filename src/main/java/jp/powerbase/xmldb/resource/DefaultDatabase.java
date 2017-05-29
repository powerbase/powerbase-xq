/*
 * @(#)$Id: DefaultDatabase.java 1178 2011-07-22 10:16:56Z hirai $
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
import java.net.URL;
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

public abstract class DefaultDatabase implements Database {
	protected Client client;

	protected DOM dom;

	protected int id = 0;
	protected Path path = null;
	protected String name = "";
	protected String description = "";
	protected Database.Type type = null;

	protected ArrayList<NameSpace> namespaces = new ArrayList<NameSpace>();

	protected String rootTag = "";

	protected String owner = "";

	protected String tuplePath = "";
	protected String tupleTag = "";
	protected ArrayList<Heading> headings = new ArrayList<Heading>();

	protected ArrayList<String> readable = new ArrayList<String>();
	protected ArrayList<String> writable = new ArrayList<String>();

	protected String query = "";

	protected NodeList meta = null;

	protected URL url = null;

	protected Path target = null;

	protected boolean exists;

	DefaultDatabase() {
	}

	DefaultDatabase(Client client, int id, Path path, Database.Type type) throws PowerBaseException {
		this.client = client;
		this.id = id;
		this.path = path;
		this.type = type;
		exists = true;

		try {
			dom = new DOM(this.client.executeXQuery("db:open-id(\"databases\", " + this.id + ")"));
			this.name = dom.getNodeValue("/database/@name");
			this.description = dom.getNodeValue("/database/@description");
			this.owner = dom.getNodeValue("/database/@owner");

			// namespace
			Element root = dom.get().getDocumentElement();
			if (root.hasChildNodes()) {
				Element namespaces = (Element) root.getElementsByTagName("namespaces").item(0);
				if (namespaces != null && namespaces.hasChildNodes()) {
					NodeList nss = namespaces.getElementsByTagName("namespace");
					for (int i = 0; i < nss.getLength(); i++) {
						String ns_name = "";
						String ns_uri = "";
						Element ns = (Element) nss.item(i);
						if (ns.hasAttribute("ns")) {
							ns_name = ns.getAttribute("ns");
						} else {
							throw new PowerBaseException(PowerBaseError.Code.ILLEGAL_DATABASE_DEFINITION_FORMAT);
						}
						if (ns.hasChildNodes()) {
							ns_uri = ns.getFirstChild().getNodeValue();
						} else {
							throw new PowerBaseException(PowerBaseError.Code.ILLEGAL_DATABASE_DEFINITION_FORMAT);
						}
						this.namespaces.add(new NameSpace(ns_name, ns_uri));
					}
				}
				Element permission = (Element) root.getElementsByTagName("permission").item(0);
				if (permission != null && permission.hasChildNodes()) {
					NodeList readable = permission.getElementsByTagName("readable");
					for (int i = 0; i < readable.getLength(); i++) {
						Element r = (Element) readable.item(i);
						this.readable.add(r.getTextContent());
					}

					NodeList writable = permission.getElementsByTagName("writable");
					for (int i = 0; i < writable.getLength(); i++) {
						Element w = (Element) writable.item(i);
						this.writable.add(w.getTextContent());
					}
				}
			} else {
				throw new PowerBaseException(PowerBaseError.Code.ILLEGAL_DATABASE_DEFINITION_FORMAT);
			}

			this.meta = dom.evalXpath("/database/meta");

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

	public boolean exists() {
		return exists;
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
	public Type getType() {
		return type;
	}

	@Override
	public NodeList getMeta() {
		return meta;
	}

	@Override
	public ArrayList<NameSpace> getNamespaces() {
		return namespaces;
	}

	@Override
	public String getOwner() {
		return owner;
	}

	@Override
	public ArrayList<String> getReadable() {
		return readable;
	}

	@Override
	public ArrayList<String> getWritable() {
		return writable;
	}

	@Override
	public abstract ArrayList<Heading> getHeadings();

	@Override
	public abstract String getTuplePath();

	@Override
	public abstract String getRootTag();

	@Override
	public abstract String getTupleTag();

	@Override
	public abstract String getQuery();

	@Override
	public abstract boolean isTuple();

	@Override
	public Directory getParent() throws PowerBaseException {
		return new DefaultDirectory(client, new Path(path.getParentPath()));
	}

	public String toString() {
		return path.getPath();
	}

}
