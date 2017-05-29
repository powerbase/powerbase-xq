/*
 * @(#)$Id: OrderBy.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.util.ArrayList;

import jp.powerbase.PowerBaseException;
import jp.powerbase.precompile.LexicalAnalyzer;
import jp.powerbase.precompile.OrderClauseParser;
import jp.powerbase.precompile.Parser;
import jp.powerbase.request.context.RequestContext;

public class OrderBy implements Expr {
	public StringBuffer clause = new StringBuffer();

	public OrderBy(RequestContext ctx) throws PowerBaseException {
		String order = ctx.getQueryOrder();
		LexicalAnalyzer compiler = new LexicalAnalyzer(order);
		Parser parser = new OrderClauseParser(compiler.getList());
		clause.append(parser.parse());
	}

	public OrderBy(String expr) throws PowerBaseException {
		clause.append(expr);
	}

	private void append(Object expr) {
		String s = "";
		if (expr instanceof ArrayList) {
			ArrayList a = (ArrayList) expr;
			if (a.size() == 0) {
				return;
			}
			s += a.get(0).toString();
			for (int i = 0; i < a.size(); i++) {
				s += ", ";
				s += a.get(i).toString();
			}
		} else {
			s = expr.toString();
		}

		if (clause.toString().equals("")) {
			clause.append(s.toString());
		} else {
			clause.append(", ");
			clause.append(s);
		}
	}

	public void append(String expr) {
		this.append((Object) expr);
	}

	public void append(OrderSpec expr) {
		this.append((Object) expr);
	}

	public void append(ArrayList expr) {
		this.append((Object) expr);
	}

	@Override
	public String toString() {
		if (trim(clause.toString()).equals("")) {
			return "";
		} else {
			return "order by " + trim(clause.toString());
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
