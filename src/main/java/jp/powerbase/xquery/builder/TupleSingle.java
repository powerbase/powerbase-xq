/*
 * @(#)$Id: TupleSingle.java 1178 2011-07-22 10:16:56Z hirai $
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
import jp.powerbase.constant.PowerBaseAttribute;
import jp.powerbase.precompile.Parser;
import jp.powerbase.request.context.RequestContext;
import jp.powerbase.xquery.expr.Where;

public class TupleSingle extends Tuple {
	public TupleSingle(RequestContext ctx) {
		super(ctx);
	}

	@Override
	public void buildXPath() throws PowerBaseException {
		_xpath = "";
	}

	@Override
	public void buildOrderBy() throws PowerBaseException {
		_order = "";
	}

	@Override
	public void buildWhere() throws PowerBaseException {
		Where w = new Where(Parser.PREFIX + "/@" + PowerBaseAttribute.ID + " = " + ctx.getNodeID());
		// Where w = new Where(Parser.PREFIX + "/@" + PowerBaseAttribute.ID +
		// " = " + ctx.getTupleID());
		_where = w.toString();

	}

}
