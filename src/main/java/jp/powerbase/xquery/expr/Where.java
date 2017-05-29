/*
 * @(#)$Id: Where.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.xquery.expr;

import jp.powerbase.PowerBaseException;
import jp.powerbase.precompile.LexicalAnalyzer;
import jp.powerbase.precompile.Parser;
import jp.powerbase.precompile.WhereClauseParser;
import jp.powerbase.request.context.RequestContext;

public class Where implements Expr {
	private StringBuffer expr = new StringBuffer();

	public Where(RequestContext ctx) throws PowerBaseException {
		String where = ctx.getQueryWhere();
		LexicalAnalyzer compiler = new LexicalAnalyzer(where);
		Parser parser = new WhereClauseParser(compiler.getList());
		expr.append(parser.parse());
	}

	public Where(String expr) {
		this.expr.append(expr);
	}

	private void append(Object comp, Logical.Operator lop) {
		if (expr.toString().equals("")) {
			expr.append(comp.toString());
		} else {
			expr.insert(0, "(");
			expr.append(")");
			expr.append(lop.toString());
			expr.append(comp.toString());
		}
	}

	public void append(Comparison comp, Logical.Operator lop) {
		this.append((Object) comp, lop);
	}

	public void append(String comp, Logical.Operator lop) {
		this.append((Object) comp, lop);
	}

	@Override
	public String toString() {

		if (trim(expr.toString()).equals("")) {
			return "";
		} else {
			return "where " + trim(expr.toString());
		}

	}

	private static String trim(String string) {
		return string.replaceAll("^[\\s　]*", "").replaceAll("[\\s　]*$", "");
	}

	@Override
	public String display() {
		return toString();
	}

}
