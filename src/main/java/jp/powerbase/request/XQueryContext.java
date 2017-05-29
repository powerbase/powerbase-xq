/*
 * @(#)$Id: XQueryContext.java 1152 2011-07-01 03:11:50Z hirai $
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
package jp.powerbase.request;

import java.util.ArrayList;
import java.util.Iterator;

import org.basex.core.Context;
import org.basex.data.Data;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.QueryResources;

public class XQueryContext {
	private final String xquery;
	private final Context ctx;
	private final QueryContext qctx;
	private boolean syntaxError = false;
	private String errorMessage;
	private QueryResources resource;

	public XQueryContext(String xquery) {
		this.xquery = xquery;
		ctx = new Context();
		qctx = new QueryContext(ctx);
		try {
			qctx.parse(this.xquery, null, null);
		} catch (QueryException e) {
			errorMessage = e.getMessage();
			syntaxError = true;
		}
	}

	public void compile() throws QueryException {
		qctx.compile();
		resource = qctx.resources;
	}

	public ArrayList<String> getDbNames() {
		ArrayList<String> dbNames = new ArrayList<String>();
		ArrayList<Data> datas = resource.getData();
		Iterator i = datas.iterator();
		
		while (i.hasNext()) {
			Data data = (Data) i.next();
			dbNames.add(data.meta.name);
		}

		return dbNames;
	}

	public boolean isUpdating() {
		return qctx.updating;
	}

	public boolean hasSyntaxError() {
		return syntaxError;
	}

	public String toString() {
		return xquery;
	}

	/**
	 * errorMessage
	 *
	 * @return errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

}
