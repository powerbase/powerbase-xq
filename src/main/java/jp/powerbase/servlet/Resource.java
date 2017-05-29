/*
 * @(#)$Id: Resource.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.powerbase.BaseX;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Response;
import jp.powerbase.basex.Client;
import jp.powerbase.constant.CommonUseNameSpaces;
import jp.powerbase.response.ResponseTag;
import jp.powerbase.servlet.response.ServletResponse;
import jp.powerbase.xmldb.resource.Database;
import jp.powerbase.xmldb.resource.DatabaseFactory;
import jp.powerbase.xmldb.resource.NameSpace;
import jp.powerbase.xmldb.resource.Path;
import jp.powerbase.xquery.expr.Brace;
import jp.powerbase.xquery.expr.DeclareNameSpaces;

/**
 * Servlet implementation class Server
 */
public class Resource extends HttpServlet {
	/**
	 *
	 */
	private static final long serialVersionUID = 7966448087328954469L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Resource() {
		super();
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void init() throws ServletException {
		super.init();
	}

	/**
	 * @param method
	 * @param request
	 * @param response
	 */
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Response res = null;
		Client client = null;
		try {
			res = new ServletResponse(request, response);
			client = new BaseX().getClient(res.getPrinter());

			String path = request.getPathInfo();

			Database db = new DatabaseFactory(client, new Path(path)).getInstance();
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
				q.append(path);
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
			response.getWriter().print("<empty />");
		} finally {
			try {
				if (res != null) {
					res.close();
				}
			} catch (PowerBaseException e) {
				response.getWriter().print("<empty />");
			}
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		throw new ServletException("unsupported.");
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		throw new ServletException("unsupported.");
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		throw new ServletException("unsupported.");
	}

}
