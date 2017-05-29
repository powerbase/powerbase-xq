/*
 * @(#)$Id: QueryIndex.java 1178 2011-07-22 10:16:56Z hirai $
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
import jp.powerbase.constant.PowerBaseAttribute;
import jp.powerbase.precompile.Parser;
import jp.powerbase.request.context.RequestContext;
import jp.powerbase.xmldb.resource.Heading;
import jp.powerbase.xquery.expr.Attribute;
import jp.powerbase.xquery.expr.Attributes;
import jp.powerbase.xquery.expr.Tag;
import jp.powerbase.xquery.expr.Var;
import jp.powerbase.xquery.expr.Where;

public class QueryIndex extends Query {
	public QueryIndex(RequestContext ctx) {
		super(ctx);
	}

	@Override
	public void buildWhere() throws PowerBaseException {
		Where w = new Where(ctx);
		if (!ctx.getNodeID().equals("")) {
			w = new Where(new Var(Parser.PREFIX).toString() + "/@" + PowerBaseAttribute.ID + " = " + ctx.getNodeID());
		} else {
			w = new Where(ctx);
		}
		_where = w.toString();
	}

	@Override
	public void buildReturn() throws PowerBaseException {
		Tag tt = new Tag(db.getTupleTag(), new Attributes(new Attribute("pb:db", db.getPath().toString())));
		StringBuffer ret = new StringBuffer();
		ret.append("{");
		ret.append(new Var(Parser.PREFIX).toString());
		ret.append("/@pb:id}");
		// ret.append("/@*}"); //20110202_error.xml
		ArrayList headings = db.getHeadings();
		if (!headings.isEmpty()) {
			for (Iterator i = headings.iterator(); i.hasNext();) {
				Heading heading = (Heading) i.next();
				ret.append("{fn:concat(\"" + heading.getPrefix() + "\"");
				ret.append(", ");
				ret.append("fn:string-join(");
				ret.append(Parser.PREFIX + "/" + heading.getHeading() + "/text()");
				ret.append(", \", \")");
				ret.append(", \"" + heading.getSuffix() + "\")}");
			}
		}

		String s = tt.wrap(ret.toString());

		_return = "return " + s;

	}

}
