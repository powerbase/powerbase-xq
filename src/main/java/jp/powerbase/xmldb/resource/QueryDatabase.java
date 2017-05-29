/*
 * @(#)$Id: QueryDatabase.java 1178 2011-07-22 10:16:56Z hirai $
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

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Settings;
import jp.powerbase.basex.Client;
import jp.powerbase.request.XQueryContext;

public class QueryDatabase extends TupleDatabase implements Database {
	private boolean tuple;

	QueryDatabase(Client client, int id, Path path, Type type) throws PowerBaseException {
		super(client, id, path, type);

		Element root = dom.get().getDocumentElement();

		if (root.getElementsByTagName("tuple") != null) {
			tuple = true;
			super.setTupleItem(root);
		} else {
			try {
				String r = dom.getNodeValue("/database/root/text()");
				if (r.equals("")) {
					rootTag = Settings.get(Settings.Symbol.DEFAULT_DOCUMENT_ROOT);
				} else {
					rootTag = r;
				}
			} catch (XPathExpressionException e) {
				throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
			}
		}

		Element q = (Element) root.getElementsByTagName("query").item(0);
		NodeList l = q.getChildNodes();
		for (int i = 0; i < l.getLength(); i++) {
			Node n = l.item(i);
			if (n.getNodeType() == Node.CDATA_SECTION_NODE) {
				this.query = n.getTextContent();
				break;
			}
		}
		if (this.query.equals("")) {
			for (int i = 0; i < l.getLength(); i++) {
				Node n = l.item(i);
				if (n.getNodeType() == Node.TEXT_NODE) {
					this.query = n.getTextContent();
					break;
				}
			}
		}

		XQueryContext xqueryContext = new XQueryContext(this.query);
		if (xqueryContext.hasSyntaxError()) {
			String message = xqueryContext.toString() + "\n";
			message += xqueryContext.getErrorMessage();
			throw new PowerBaseException(PowerBaseError.Code.XQUERY_SYNTAX_ERROR, new Exception(message));
		}

		if (xqueryContext.isUpdating()) {
			throw new PowerBaseException(PowerBaseError.Code.UPDATING_QUERY_IS_INVALID);
		}

	}

	@Override
	public String getQuery() {
		return query;
	}

	@Override
	public boolean isTuple() {
		return tuple;
	}

}
