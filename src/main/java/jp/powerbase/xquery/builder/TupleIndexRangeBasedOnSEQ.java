/*
 * @(#)$Id: TupleIndexRangeBasedOnSEQ.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.util.ArrayList;
import java.util.Iterator;

import jp.powerbase.PowerBaseException;
import jp.powerbase.precompile.Parser;
import jp.powerbase.request.context.RequestContext;
import jp.powerbase.xmldb.resource.Heading;
import jp.powerbase.xquery.expr.Doc;
import jp.powerbase.xquery.expr.For;
import jp.powerbase.xquery.expr.Let;
import jp.powerbase.xquery.expr.OrderBy;
import jp.powerbase.xquery.expr.Return;
import jp.powerbase.xquery.expr.TupleTag;
import jp.powerbase.xquery.expr.Var;
import jp.powerbase.xquery.expr.Where;
import jp.powerbase.xquery.expr.XPath;

public class TupleIndexRangeBasedOnSEQ extends TupleRangeBasedOnSEQ {
	public TupleIndexRangeBasedOnSEQ(RequestContext ctx) {
		super(ctx);
	}

	@Override
	public void buildLet() throws PowerBaseException {
		XPath xpath = new XPath(db.getTuplePath());
		Where where = new Where(ctx);
		OrderBy order = new OrderBy(ctx);

		TupleTag tt = new TupleTag(db.getPath().toString(), db.getTupleTag(), new Var());

		StringBuffer r = new StringBuffer();
		ArrayList headings = db.getHeadings();
		if (!headings.isEmpty()) {
			for (Iterator i = headings.iterator(); i.hasNext();) {
				Heading heading = (Heading) i.next();
				r.append("{fn:concat(\"" + heading.getPrefix() + "\"");
				r.append(", ");
				r.append("fn:string-join(");
				r.append(Parser.PREFIX + "/" + heading.getHeading() + "/text()");
				r.append(", \", \")");
				r.append(", \"" + heading.getSuffix() + "\")}");
			}
		}

		String s = tt.wrap(r.toString());

		Return ret = new Return(s);

		For f = new For((Object) new Doc(db.getPath().toString()), (Object) xpath, (Object) where, (Object) order, (Object) ret);
		Let let = new Let(new Var("nodes"), (Object) f);
		_let = let.toString();
	}

}
