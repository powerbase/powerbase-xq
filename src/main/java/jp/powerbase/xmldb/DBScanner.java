/*
 * @(#)$Id: DBScanner.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.xmldb;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.User;
import jp.powerbase.basex.Client;
import jp.powerbase.xmldb.resource.DataNodeUtil;
import jp.powerbase.xmldb.resource.Database;
import jp.powerbase.xmldb.resource.NameSpace;
import jp.powerbase.xmldb.resource.Path;
import jp.powerbase.xmldb.sax.DBScanHandler;

public class DBScanner implements Iterator<Database> {

	private Iterator<Database> list;
	private ArrayList<NameSpace> namespaces;
	private int count = 0;

	public DBScanner(Client client, User user, Path path) throws PowerBaseException {
		String xpath;
		if (path.isDatabase(client)) {
			xpath = DataNodeUtil.getDatabaseXPath(path.getPath());
		} else if (path.isDirectory(client)) {
			xpath = DataNodeUtil.getDirectoryXPath(path.getPath());
		} else {
			throw new PowerBaseException(PowerBaseError.Code.INVALID_LOCATION_PATH);
		}

		String xml = client.executeXQuery("doc('databases')" + xpath);
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			DBScanHandler h = new DBScanHandler();
			h.setClient(client);
			h.setUser(user);
			parser.parse(new ByteArrayInputStream(xml.getBytes()), h);
			list = h.getDatabases().iterator();
			count = h.getDatabases().size();
		} catch (ParserConfigurationException e) {
			throw new PowerBaseException(e);
		} catch (SAXException e) {
			throw new PowerBaseException(e);
		} catch (IOException e) {
			throw new PowerBaseException(e);
		}
	}

	@Override
	public boolean hasNext() {
		return list.hasNext();
	}

	@Override
	public Database next() {
		return list.next();
	}

	@Override
	public void remove() {
		list.remove();
	}

	/**
	 * namespaces
	 * @return namespaces
	 */
	public ArrayList<NameSpace> getNamespaces() {
	    return namespaces;
	}

	/**
	 * count
	 * @return count
	 */
	public int getCount() {
	    return count;
	}

}
