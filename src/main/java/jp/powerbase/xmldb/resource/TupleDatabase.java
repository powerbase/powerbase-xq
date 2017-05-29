/*
 * @(#)$Id: TupleDatabase.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.net.URL;
import java.util.ArrayList;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Settings;
import jp.powerbase.basex.Client;

public class TupleDatabase extends DefaultDatabase implements Database {

	TupleDatabase(Client client, int id, Path path, Type type) throws PowerBaseException {
		super(client, id, path, type);

		Element root = dom.get().getDocumentElement();

		if (type != Type.QUERY) {
			setTupleItem(root);
		}

	}

	protected void setTupleItem(Element root) throws PowerBaseException {
		try {
			String r = dom.getNodeValue("/database/root/text()");
			if (r.equals("")) {
				rootTag = Settings.get(Settings.Symbol.DEFAULT_TUPLE_ROOT);
			} else {
				rootTag = r;
			}
			owner = dom.getNodeValue("/database/@owner");

			String tt = dom.getNodeValue("/database/tuple/tag/text()");
			if (tt.equals("")) {
				tupleTag = Settings.get(Settings.Symbol.DEFAULT_TUPLE_TAG);
			} else {
				tupleTag = tt;
			}

			tuplePath = "/" + rootTag + "/" + tupleTag;

			if (root.hasChildNodes()) {
				Element headings = (Element) root.getElementsByTagName("headings").item(0);
				if (headings != null) {
					NodeList hs = headings.getElementsByTagName("heading");
					for (int i = 0; i < hs.getLength(); i++) {
						String heading = "";
						String prefix = "";
						String suffix = "";
						Element h = (Element) hs.item(i);
						if (h.hasAttribute("prefix")) {
							prefix = h.getAttribute("prefix");
						} else {
							prefix = "";
						}
						if (h.hasAttribute("suffix")) {
							suffix = h.getAttribute("suffix");
						} else {
							suffix = "";
						}
						if (h.hasChildNodes()) {
							heading = h.getFirstChild().getNodeValue();
						} else {
							heading = "";
						}
						if (!heading.equals("")) {
							this.headings.add(new Heading((i + 1), heading, prefix, suffix));
						}
					}
				}
			}
		} catch (XPathExpressionException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		}
	}

	@Override
	public ArrayList<Heading> getHeadings() {
		return headings;
	}

	@Override
	public String getTuplePath() {
		return tuplePath;
	}

	@Override
	public String getRootTag() {
		return rootTag;
	}

	@Override
	public String getTupleTag() {
		return tupleTag;
	}

	@Override
	public String getQuery() {
		return "";
	}

	@Override
	public boolean isTuple() {
		return true;
	}

	@Override
	public URL getUrl() {
		return null;
	}

	@Override
	public Path getTarget() {
		return null;
	}

}
