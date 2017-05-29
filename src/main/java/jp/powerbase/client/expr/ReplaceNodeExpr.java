/*
 * @(#)$Id: ReplaceNodeExpr.java 1087 2011-05-25 05:28:29Z hirai $
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

import jp.powerbase.xmldb.resource.Path;

public class ReplaceNodeExpr {
	protected String targetExpr;
	protected String exprSingle;

	public ReplaceNodeExpr(Path nodePath, String exprSingle) {
		targetExpr = nodePath.toString();
		this.exprSingle = exprSingle;
	}

	public ReplaceNodeExpr(Path databasePath, int nodeId, String exprSingle) {
		databasePath.addPath(Integer.toString(nodeId));
		targetExpr = databasePath.toString();
		this.exprSingle = exprSingle;
	}

	public ReplaceNodeExpr(Path databasePath, int nodeId, String attribute, String exprSingle) {
		databasePath.addPath(Integer.toString(nodeId));
		databasePath.addPath("@" + attribute);
		targetExpr = databasePath.toString();
		this.exprSingle = exprSingle;
	}

	public ReplaceNodeExpr(Path databasePath, String xpath, String exprSingle) {
		databasePath.addPath(xpath);
		targetExpr = databasePath.toString();
		this.exprSingle = exprSingle;
	}

	public String toString() {
		StringBuffer expr = new StringBuffer();
		expr.append("<node>");
		expr.append(targetExpr);
		expr.append("</node>");
		expr.append("<with>");
		expr.append(exprSingle);
		expr.append("</with>");
		return ExprWrapper.Wrap(expr.toString());
	}

}
