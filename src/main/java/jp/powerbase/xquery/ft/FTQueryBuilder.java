/*
 * @(#)$Id: FTQueryBuilder.java 1125 2011-06-14 08:17:12Z hirai $
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
package jp.powerbase.xquery.ft;

import jp.powerbase.request.context.RequestContext;
import jp.powerbase.response.ResponseTag;
import jp.powerbase.xmldb.DBScanner;
import jp.powerbase.xmldb.resource.Database;

public class FTQueryBuilder {
	private DBScanner iterator;

	public FTQueryBuilder(DBScanner iterator) {
		this.iterator = iterator;
		assert (this.iterator.getCount() != 0);
	}

	public String build(RequestContext req) {
		StringBuffer function = new StringBuffer();
		StringBuffer query = new StringBuffer();
		while (iterator.hasNext()) {
			FTType ft = null;
			Database db = iterator.next();
			switch (db.getType()) {
			case DOCUMENT:
				ft = new TypeDocument(db, req.getFTLop(), req.getFTQuery());
				break;
			case TUPLE:
			case FILE:
				ft = new TypeTuple(db, req.getFTLop(), req.getFTQuery());
				break;
			}
			if (ft != null) {
				function.append(ft.buildFunction());
				query.append(ft.build());
			}
		}

		ResponseTag res = new ResponseTag(req.getRootTag());

		return function + res.wrap("" + query + "");
	}
}
