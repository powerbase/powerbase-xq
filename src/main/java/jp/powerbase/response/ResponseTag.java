/*
 * @(#)$Id: ResponseTag.java 1087 2011-05-25 05:28:29Z hirai $
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
package jp.powerbase.response;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;

import jp.powerbase.xmldb.resource.NameSpace;
import jp.powerbase.xquery.expr.Wrappable;

public class ResponseTag implements Wrappable {
	private String name;
	private ArrayList<NameSpace> namespaces;
	private URI baseURI = null;

	public ResponseTag(String name) {
		this(name, null);
	}

	public ResponseTag(String name, ArrayList<NameSpace> namespaces) {
		this.name = name;
		this.namespaces = namespaces;
	}

	public void addNamespace(NameSpace namespace) {
		namespaces.add(namespace);
	}

	public String getOpen() {
		StringBuffer s = new StringBuffer();
		if (name != null && !name.equals("")) {
			s.append("<");
			s.append(name);

			if (baseURI != null) {
				s.append(" ");
				s.append("xml:base=\"");
				s.append(baseURI.toString());
				s.append("\"");
			}
			if (namespaces != null) {
				Iterator nss = namespaces.iterator();
				while (nss.hasNext()) {
					s.append(" ");
					s.append(((NameSpace) nss.next()).toXMLString());
				}
			}
			s.append(">");

		}
		return s.toString();
	}

	public String getClose() {
		StringBuffer s = new StringBuffer();
		if (name != null && !name.equals("")) {
			s.append("</");
			s.append(name);
			s.append(">");
		}
		return s.toString();
	}

	@Override
	public String wrap(String contents) {
		return getOpen() + contents + getClose();
	}

	/**
	 * baseURI
	 *
	 * @param baseURI
	 *            baseURI
	 */
	public void setBaseURI(URI baseURI) {
		this.baseURI = baseURI;
	}

}
