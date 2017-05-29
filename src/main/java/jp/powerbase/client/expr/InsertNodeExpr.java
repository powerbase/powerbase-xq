/*
 * @(#)$Id: InsertNodeExpr.java 1087 2011-05-25 05:28:29Z hirai $
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

public class InsertNodeExpr {
	public enum TargetChoice {
		INTO("into"),
		FIRST("first"),
		AFTER("after"),
		BEFORE("before"),
		;

		public final String value;

		TargetChoice(String value) {
			this.value = value;
		}

	}

	private String targetExpr;
	private TargetChoice choice;
	private String sourceExpr;

	public InsertNodeExpr(Path target, TargetChoice choice, String sourceExpr) {
		targetExpr = target.toString();
		this.choice = choice;
		this.sourceExpr = sourceExpr;
	}

	public InsertNodeExpr(Path targetDatabase, int targetNodeId, TargetChoice choice, String sourceExpr) {
		targetDatabase.addPath(Integer.toString(targetNodeId));
		targetExpr = targetDatabase.toString();
		this.choice = choice;
		this.sourceExpr = sourceExpr;
	}

	public InsertNodeExpr(Path targetDatabase, String xpath, TargetChoice choice, String sourceExpr) {
		targetDatabase.addPath(xpath);
		targetExpr = targetDatabase.toString();
		this.choice = choice;
		this.sourceExpr = sourceExpr;
	}

	public String toString() {
		StringBuffer expr = new StringBuffer();
		expr.append("<source>");
		expr.append(sourceExpr);
		expr.append("</source>");
		expr.append("<target choice=\"");
		expr.append(choice.value);
		expr.append("\">");
		expr.append(targetExpr);
		expr.append("</target>");
		return ExprWrapper.Wrap(expr.toString());
	}

}
