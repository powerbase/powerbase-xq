/*
 * @(#)$Id: Dump.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.xquery.builder;

import jp.powerbase.PowerBaseException;
import jp.powerbase.request.context.RequestContext;
import jp.powerbase.xmldb.resource.Database;
import jp.powerbase.xquery.Builder;
import jp.powerbase.xquery.expr.Doc;
import jp.powerbase.xquery.expr.Let;
import jp.powerbase.xquery.expr.Return;
import jp.powerbase.xquery.expr.Var;
import jp.powerbase.xquery.expr.XPath;

public class Dump extends Builder {
	private Database db;
	private RequestContext ctx;

	private static final String function;

	static {
		StringBuffer s = new StringBuffer();
		s.append("declare function local:trace($n as node()) as node()?");
		s.append(_SEPARATOR);
		s.append("{");
		s.append(_SEPARATOR);
		s.append("typeswitch($n)");
		s.append(_SEPARATOR);
		s.append("  case $e as element()");
		s.append(_SEPARATOR);
		s.append("    return element  { fn:local-name($e) } { for $c in ($e/@*, $e/node()) return local:trace($c) }");
		s.append(_SEPARATOR);
		s.append("  default return $n");
		s.append(_SEPARATOR);
		s.append("};");
		s.append(_SEPARATOR);
		function = s.toString();
	}

	public Dump(RequestContext ctx) {
		this.ctx = ctx;
		this.db = this.ctx.getDatabase();

	}

	@Override
	public void buildFor() throws PowerBaseException {
		_for = "";
	}

	@Override
	public void buildXPath() throws PowerBaseException {
		Doc doc = new Doc(db.getPath().toString());
		_xpath = doc.toString();
	}

	@Override
	public void buildLet() throws PowerBaseException {
		Doc doc = new Doc(db.getPath().toString());
		XPath xpath;
		if (ctx.getXPath().equals("")) {
			xpath = new XPath("/" + db.getRootTag());
		} else {
			xpath = new XPath(ctx.getXPath());
		}

		Let let = new Let(new Var("d"), doc, xpath);
		_let = let.toString();
	}

	@Override
	public void buildOrderBy() throws PowerBaseException {
		_order = "";
	}

	@Override
	public void buildWhere() throws PowerBaseException {
		_where = "";
	}

	@Override
	public String getResult() {
		StringBuffer query = new StringBuffer();
		query.append(function);
		query.append(_SEPARATOR);
		query.append(_let);
		query.append(_SEPARATOR);
		query.append(_return);

		return query.toString();
	}

	@Override
	public void buildReturn() throws PowerBaseException {
		Return r = new Return("local:trace($d)");
		_return = r.toString();

	}

}
