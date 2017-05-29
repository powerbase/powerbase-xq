/*
 * @(#)$Id: For.java 1178 2011-07-22 10:16:56Z hirai $
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

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.precompile.Parser;

public class For implements Expr {
	private Var var = new Var(Parser.PREFIX);
	private Doc doc = null;
	private XPath xpath = null;
	private Where where = null;
	private OrderBy order = null;
	private Return ret = null;

	public For() {
	}

	public For(Object... objects) throws PowerBaseException {
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] instanceof Var) {
				this.var = (Var) objects[i];
			} else if (objects[i] instanceof Doc) {
				this.doc = (Doc) objects[i];
			} else if (objects[i] instanceof XPath) {
				this.xpath = (XPath) objects[i];
			} else if (objects[i] instanceof Where) {
				this.where = (Where) objects[i];
			} else if (objects[i] instanceof OrderBy) {
				this.order = (OrderBy) objects[i];
			} else if (objects[i] instanceof Return) {
				this.ret = (Return) objects[i];
			} else {
				throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, new Exception());
			}
		}
	}

	@Override
	public String toString() {
		// for $b in doc("/aozora/sakuhin")//record
		StringBuffer f = new StringBuffer();
		f.append("for ");
		f.append(var);
		f.append(" in ");
		if (doc != null && !doc.toString().equals("")) {
			f.append(doc);
		}
		if (xpath != null && !xpath.toString().equals("")) {
			f.append(xpath);
		}

		if (where != null && !where.toString().equals("")) {
			f.append(" ");
			f.append(where);
		}

		if (order != null && !order.toString().equals("")) {
			f.append(" ");
			f.append(order);
		}

		if (ret != null && !ret.toString().equals("")) {
			f.append(" ");
			f.append(ret);
		}

		return f.toString();
	}

	@Override
	public String display() {
		return toString();
	}

}
