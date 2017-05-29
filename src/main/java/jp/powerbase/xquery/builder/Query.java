/*
 * @(#)$Id: Query.java 1178 2011-07-22 10:16:56Z hirai $
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
import jp.powerbase.precompile.Parser;
import jp.powerbase.request.context.RequestContext;
import jp.powerbase.xmldb.resource.Database;
import jp.powerbase.xquery.Builder;
import jp.powerbase.xquery.expr.For;
import jp.powerbase.xquery.expr.Let;
import jp.powerbase.xquery.expr.OrderBy;
import jp.powerbase.xquery.expr.Return;
import jp.powerbase.xquery.expr.Var;
import jp.powerbase.xquery.expr.Where;
import jp.powerbase.xquery.expr.XPath;

public class Query extends Builder {
	protected Database db;
	protected RequestContext ctx;

	public Query(RequestContext ctx) {
		this.ctx = ctx;
		this.db = this.ctx.getDatabase();
	}

	@Override
	public void buildFor() throws PowerBaseException {
		For f = new For(new Var(Parser.PREFIX), new XPath(new Var("node").toString() + "/" + db.getTupleTag()));
		_for = f.toString();
	}

	@Override
	public void buildXPath() throws PowerBaseException {
		_xpath = "";
	}

	@Override
	public void buildLet() throws PowerBaseException {
		Let let = new Let(new Var("node"), db.getQuery());
		_let = let.toString();
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
		query.append(_let);
		query.append(_for);
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

		return query.toString();
	}

	@Override
	public void buildReturn() throws PowerBaseException {
		Return ret = new Return(Parser.PREFIX);
		_return = ret.toString();

	}

}

/*
 *
 * declare namespace pb="http://www.powerbase.jp/dtd"; <records> { let $node :=
 * <records> { for $b in doc("/aozora/sakuhin")//record let $u :=
 * doc("/aozora/sakka")//record[id = $b/personid] return <record>{$b/@*}{$b/*}
 * <sakka>{$u/*}</sakka> </record> } </records> for $t in $node/record where
 * $t/@pb:id = '2' return $t } </records>
 */

/*
 * declare namespace pb="http://www.powerbase.jp/dtd"; declare function
 * local:toc() as node() { <records> { for $b in doc("/aozora/sakuhin")//record
 * let $u := doc("/aozora/sakka")//record[id = $b/personid] return
 * <record>{$b/@*}{$b/*} <sakka>{$u/*}</sakka> </record> } </records> }; for $t
 * in local:toc() where $t/records/record/@pb:id = '2' return
 * <record>{$t/@*}{$t/*}</record>
 */

/*
 * <a> { let $t := <a><b>1</b><b>2</b></a> for $tt in $t/b where $tt/text() =
 * '2' return $tt } </a>
 */

