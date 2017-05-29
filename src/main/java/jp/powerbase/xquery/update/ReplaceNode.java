/*
 * @(#)$Id: ReplaceNode.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.xquery.update;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.request.PathParser;
import jp.powerbase.request.context.RequestContext;
import jp.powerbase.xml.DOM;
import jp.powerbase.xmldb.resource.Database;
import jp.powerbase.xmldb.resource.NodeId;
import jp.powerbase.xmldb.resource.Path;
import jp.powerbase.xquery.expr.Doc;
import jp.powerbase.xquery.expr.XPath;

public class ReplaceNode implements XQueryUpdateBuilder {
	RequestContext ctx;
	Database db;

	public ReplaceNode(RequestContext ctx) {
		this.ctx = ctx;
		db = this.ctx.getDatabase();
	}

	public String getExpr(String database, String id, String path, String exprSingle) {
		String targetExpr = "";

		if (id.equals("")) {
			Doc doc = new Doc(database);
			XPath xpath = new XPath(doc.toString() + path);
			targetExpr = xpath.toString();
		} else {
			NodeId nodeId = new NodeId(database, Integer.valueOf(id));
			targetExpr = nodeId.toString();
			if (!path.equals("")) {
				targetExpr += path;
			}
		}
		return getContext(targetExpr, exprSingle);
	}

	protected String getContext(String targetExpr, String exprSingle) {
		StringBuffer q = new StringBuffer();
		q.append("replace node ");
		q.append(targetExpr);
		q.append(" with ");
		q.append(exprSingle);
		return q.toString();
	}

	protected String retrieve(NodeList with) throws XPathExpressionException, ParserConfigurationException, IOException, TransformerException {
		DOM w = new DOM(new DOM(with).evalXpath("/with/*"));
		return w.getXML(false);
	}

	@Override
	public String build() throws PowerBaseException {
		StringBuffer xquery = new StringBuffer();
		DOM query = new DOM(ctx.getRequestBody());
		ArrayList<String> exprs = new ArrayList<String>();
		try {
			NodeList list = query.evalXpath("/request/expr");
			if (list != null && list.getLength() != 0) {
				for (int i = 0; i < list.getLength(); i++) {
					String node = "";
					String value = "";
					Element expr = (Element) list.item(i);
					if (expr.hasChildNodes()) {
						NodeList n = expr.getElementsByTagName("node");
						node = n.item(0).getFirstChild().getNodeValue();
						PathParser parser = new PathParser(new Path(node), ctx.getClient());
						parser.parse();

						String db = parser.getDatabaseName();
						String id = parser.getNodeID();
						String xp = parser.getXpath();
						NodeList with = expr.getElementsByTagName("with");

						value = retrieve(with);

						if (node.equals("")) {
							throw new PowerBaseException(PowerBaseError.Code.INVALID_PARAMETER);
						}

						if (db.equals("")) {
							throw new PowerBaseException(PowerBaseError.Code.DATABASE_NOT_SPECIFIED);
						}
						exprs.add(getExpr(db, id, xp, value));
					} else {
						throw new PowerBaseException(PowerBaseError.Code.INVALID_PARAMETER);
					}
				}
			} else {
				String targetExpr = "";

				if (ctx.getNodeID().equals("")) {
					Doc doc = new Doc(ctx.getDatabaseName());
					XPath xpath = new XPath(doc.toString() + ctx.getXPath());
					targetExpr = xpath.toString();
				} else {
					NodeId nodeId = new NodeId(ctx.getDatabaseName(), Integer.valueOf(ctx.getNodeID()));
					targetExpr = nodeId.toString();
					if (!ctx.getXPath().equals("")) {
						targetExpr += ctx.getXPath();
					}
				}
				NodeList val = query.evalXpath("/request");
				Element expr = (Element) val.item(0);

				NodeList with = expr.getElementsByTagName("with");
				String value = retrieve(with);

				exprs.add(getContext(targetExpr, value));
			}

			xquery.append(exprs.get(0));
			for (int i = 1; i < exprs.size(); i++) {
				xquery.append(", ");
				xquery.append(exprs.get(i));
			}
		} catch (XPathExpressionException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (ParserConfigurationException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (IOException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (TransformerException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		}
		return xquery.toString();
	}

}
