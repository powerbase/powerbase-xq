/*
 * @(#)$Id: DBScanHandler.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.xmldb.sax;

import java.io.IOException;
import java.util.ArrayList;

import jp.powerbase.PowerBaseException;
import jp.powerbase.User;
import jp.powerbase.basex.Client;
import jp.powerbase.xmldb.resource.Database;
import jp.powerbase.xmldb.resource.DatabaseFactory;
import jp.powerbase.xmldb.resource.NameSpace;
import jp.powerbase.xmldb.resource.Path;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class DBScanHandler extends DefaultHandler {
	private ArrayList<Database> databases = new ArrayList<Database>();
	private ArrayList<NameSpace> namespaces = new ArrayList<NameSpace>();
	private Client client;
	private User user;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		String name;
		if (uri == null) {
			name = localName;
		} else {
			name = qName;
		}

		if (name.indexOf("database") != -1) {
			try {
				Database db = new DatabaseFactory(client, new Path(attributes.getValue("path"))).getInstance();
				if (user.canView(db) && user.canRead(db)) {
					databases.add(db);
					namespaces.addAll(db.getNamespaces());
				}
			} catch (PowerBaseException e) {
				throw new SAXException(e);
			}
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		System.out.println(databases);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		super.endPrefixMapping(prefix);
	}

	@Override
	public void error(SAXParseException e) throws SAXException {
		super.error(e);
	}

	@Override
	public void fatalError(SAXParseException e) throws SAXException {
		super.fatalError(e);
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		super.ignorableWhitespace(ch, start, length);
	}

	@Override
	public void notationDecl(String name, String publicId, String systemId) throws SAXException {
		super.notationDecl(name, publicId, systemId);
	}

	@Override
	public void processingInstruction(String target, String data) throws SAXException {
		super.processingInstruction(target, data);
	}

	@Override
	public InputSource resolveEntity(String publicId, String systemId) throws IOException, SAXException {
		return super.resolveEntity(publicId, systemId);
	}

	@Override
	public void setDocumentLocator(Locator locator) {
		super.setDocumentLocator(locator);
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		super.skippedEntity(name);
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
	}

	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		super.startPrefixMapping(prefix, uri);
	}

	@Override
	public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException {
		super.unparsedEntityDecl(name, publicId, systemId, notationName);
	}

	@Override
	public void warning(SAXParseException e) throws SAXException {
		super.warning(e);
	}

	/**
	 * databases
	 *
	 * @return databases
	 */
	public ArrayList<Database> getDatabases() {
		return databases;
	}

	/**
	 * namespaces
	 * @return namespaces
	 */
	public ArrayList<NameSpace> getNamespaces() {
	    return namespaces;
	}

	/**
	 * client
	 *
	 * @param client
	 *            client
	 */
	public void setClient(Client client) {
		this.client = client;
	}

	/**
	 * user
	 *
	 * @param user
	 *            user
	 */
	public void setUser(User user) {
		this.user = user;
	}

}
