/*
 * @(#)$Id: ReplaceValueOfNode.java 1124 2011-06-14 07:34:49Z hirai $
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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.NodeList;

import jp.powerbase.request.context.RequestContext;
import jp.powerbase.xmldb.resource.Database;

public class ReplaceValueOfNode extends ReplaceNode implements XQueryUpdateBuilder {
	RequestContext ctx;
	Database db;

	public ReplaceValueOfNode(RequestContext ctx) {
		super(ctx);
	}

	protected String getContext(String targetExpr, String value) {
		StringBuffer q = new StringBuffer();
		q.append("replace value of node ");
		q.append(targetExpr);
		q.append(" with ");
		q.append("'");
		q.append(value);
		q.append("'");
		return q.toString();
	}

	protected String retrieve(NodeList with) throws XPathExpressionException, ParserConfigurationException, IOException, TransformerException {
		return with.item(0).getFirstChild().getNodeValue();
	}

}
