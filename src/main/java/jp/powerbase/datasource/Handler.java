/*
 * @(#)$Id: Handler.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.datasource;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;

import jp.powerbase.PowerBaseException;
import jp.powerbase.basex.Client;
import jp.powerbase.basex.LocalClient;
import jp.powerbase.constant.CommonUseNameSpaces;
import jp.powerbase.response.ResponseTag;
import jp.powerbase.xmldb.resource.Database;
import jp.powerbase.xmldb.resource.DatabaseFactory;
import jp.powerbase.xmldb.resource.NameSpace;
import jp.powerbase.xmldb.resource.Path;
import jp.powerbase.xquery.expr.Brace;
import jp.powerbase.xquery.expr.DeclareNameSpaces;

import com.sun.net.httpserver.Headers;

public class Handler implements HttpHandler {

	/*
	 * (非 Javadoc)
	 *
	 * @see
	 * jp.infinite.xmldb.resource.InternalHttpHandler#handle(com.sun.net.httpserver
	 * .HttpExchange)
	 */
	@Override
	public void handle(HttpExchange ex) throws IOException {

		URI uri = ex.getRequestURI();

		Headers responseHeaders = ex.getResponseHeaders();
		responseHeaders.set("Content-Type", "text/xml; charset=UTF-8");
		OutputStream out = ex.getResponseBody();
		ex.sendResponseHeaders(200, 0);

		String resId = uri.toString();

		Client client = new LocalClient(out);

		try {
			Database db = new DatabaseFactory(client, new Path(uri.toString())).getInstance();
			String xquery = "";
			switch (db.getType()) {
			case QUERY:
				xquery = db.getQuery();
				DeclareNameSpaces namespace = new DeclareNameSpaces(db.getNamespaces());
				xquery = namespace.toString() + xquery;
				break;
			case TUPLE:
			case DOCUMENT:
				StringBuffer q = new StringBuffer();
				q.append("for $d in doc(\"");
				q.append(resId);
				q.append("\")/");
				q.append(db.getRootTag());
				q.append("/node()");
				q.append(" return $d");
				xquery = q.toString();
				ResponseTag rt = new ResponseTag(db.getRootTag(), db.getNamespaces());
				ArrayList nss = CommonUseNameSpaces.getNamespaces();
				Iterator i = nss.iterator();
				while (i.hasNext()) {
					rt.addNamespace((NameSpace) i.next());
				}
				xquery = rt.wrap(new Brace().wrap(xquery));
				break;

			default:
				break;
			}

			client.execute(Client.Command.XQUERY, xquery.toString());
			client.close();
		} catch (PowerBaseException e) {
			throw new IOException(e);
		}

		out.close();

	}
}
