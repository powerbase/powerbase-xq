/*
 * @(#)$Id: Tuple.java 1178 2011-07-22 10:16:56Z hirai $
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
import jp.powerbase.precompile.LexicalAnalyzer;
import jp.powerbase.precompile.Parser;
import jp.powerbase.precompile.SelectElementsParser;
import jp.powerbase.request.context.RequestContext;
import jp.powerbase.xmldb.resource.Database;
import jp.powerbase.xquery.Builder;
import jp.powerbase.xquery.expr.Doc;
import jp.powerbase.xquery.expr.For;
import jp.powerbase.xquery.expr.OrderBy;
import jp.powerbase.xquery.expr.TupleTag;
import jp.powerbase.xquery.expr.Var;
import jp.powerbase.xquery.expr.Where;
import jp.powerbase.xquery.expr.XPath;

public class Tuple extends Builder {
	protected Database db;
	protected RequestContext ctx;

	public Tuple() {
		super();
	}

	public Tuple(RequestContext ctx) {
		this.ctx = ctx;
		this.db = this.ctx.getDatabase();
	}

	@Override
	public void buildFor() throws PowerBaseException {
		For f = new For((Object) new Doc(db.getPath().toString()), (Object) new XPath(db.getTuplePath()));
		_for = f.toString();
	}

	@Override
	public void buildXPath() throws PowerBaseException {
		_xpath = "";

	}

	@Override
	public void buildLet() throws PowerBaseException {
		_let = "";
	}

	@Override
	public void buildOrderBy() throws PowerBaseException {
		OrderBy o = new OrderBy(ctx);
		_order = o.toString();
	}

	@Override
	public void buildWhere() throws PowerBaseException {
		Where w = new Where(ctx);
		_where = w.toString();
	}

	@Override
	public String getResult() {
		StringBuffer query = new StringBuffer();
		query.append(_SEPARATOR);
		query.append(_for);
		query.append(_xpath);
		if (!_where.equals("")) {
			query.append(_SEPARATOR);
			query.append(_where);
		}
		if (!_order.equals("")) {
			query.append(_SEPARATOR);
			query.append(_order);

		}
		query.append(_SEPARATOR);
		query.append(_return);
		query.append(_SEPARATOR);

		return query.toString();
	}

	@Override
	public void buildReturn() throws PowerBaseException {
		TupleTag tt = new TupleTag(db.getPath().toString(), db.getTupleTag(), new Var());
		StringBuffer ret = new StringBuffer();

		String select = ctx.getQuerySelect();
		if (select != null && !select.equals("")) {
			LexicalAnalyzer compiler = new LexicalAnalyzer(select);
			Parser parser = new SelectElementsParser(compiler.getList());
			ret.append(parser.parse());
		} else {
			ret.append("{");
			ret.append(Parser.PREFIX);
			ret.append("/*");
			ret.append("}");
		}

		String s = tt.wrap(ret.toString());

		_return = "return " + s;

	}

}
