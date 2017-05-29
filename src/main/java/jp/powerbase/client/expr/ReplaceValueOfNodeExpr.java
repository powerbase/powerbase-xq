/*
 * @(#)$Id: ReplaceValueOfNodeExpr.java 1093 2011-05-25 06:08:28Z hirai $
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
package jp.powerbase.client.expr;

import jp.powerbase.util.XMLUtil;
import jp.powerbase.xmldb.resource.Path;

public class ReplaceValueOfNodeExpr extends ReplaceNodeExpr {
	public ReplaceValueOfNodeExpr(Path nodePath, String exprSingle) {
		super(nodePath, exprSingle);
	}

	public ReplaceValueOfNodeExpr(Path databasePath, int nodeId, String exprSingle) {
		super(databasePath, nodeId, exprSingle);
	}

	public ReplaceValueOfNodeExpr(Path databasePath, int nodeId, String attribute, String exprSingle) {
		super(databasePath, nodeId, attribute, exprSingle);
	}

	public ReplaceValueOfNodeExpr(Path databasePath, String xpath, String exprSingle) {
		super(databasePath, xpath, exprSingle);
	}

	public String toString() {
		return toString(true);
	}

	public String toString(boolean escape) {
		StringBuffer expr = new StringBuffer();
		expr.append("<node>");
		expr.append(targetExpr);
		expr.append("</node>");
		expr.append("<with>");
		expr.append(escape ? XMLUtil.escapeXML(exprSingle) : exprSingle);
		expr.append("</with>");
		return ExprWrapper.Wrap(expr.toString());
	}
}
