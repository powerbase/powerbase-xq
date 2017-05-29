/*
 * @(#)$Id: Get.java 1178 2011-07-22 10:16:56Z hirai $
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

public class Get extends Builder {
	private Database db;
	private RequestContext ctx;

	public Get(RequestContext ctx) {
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
		_let = "";
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
		query.append(_xpath);

		return query.toString();
	}

	@Override
	public void buildReturn() throws PowerBaseException {
		_return = "";

	}

}
