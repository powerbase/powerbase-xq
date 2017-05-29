/*
 * @(#)$Id: ExecuteXQuery.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.xmldb.process;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.basex.query.QueryException;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Request;
import jp.powerbase.Response;
import jp.powerbase.basex.Client;
import jp.powerbase.request.XQueryContext;
import jp.powerbase.response.XMLResponse;
import jp.powerbase.util.Log;

public class ExecuteXQuery extends AbstractProcessor {
	protected String results;
	private XMLResponse xres;

	public ExecuteXQuery() {
	}

	public ExecuteXQuery(Request req, Response res) throws PowerBaseException {
		super(req, res);
		xres = new XMLResponse(res);

	}

	/*
	 * (非 Javadoc)
	 *
	 * @see jp.infinite.xmldb.process.AbstractProcessor#process()
	 */
	@Override
	public void process() throws PowerBaseException {
		String xquery = req.getXqueryContext().toString();
		Log.debug("execute xquery: " + xquery);
		Client session = req.getClient();
		XQueryContext context = req.getXqueryContext();
		ArrayList<String> dbNames = new ArrayList<String>();
		if (context != null && context.isUpdating()) {
			try {
				context.compile();
			} catch (QueryException e) {
				throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
			}
			dbNames = context.getDbNames();
		}

		if (req.getDatabase() != null) {
			String name = Integer.toString(req.getDatabase().getId());
			session.execute(Client.Command.OPEN, name);
		}
		session.execute(Client.Command.XQUERY, xquery);

		Iterator i = dbNames.iterator();
		while (i.hasNext()) {
			session.execute(Client.Command.OPEN, (String) i.next());
			session.execute(Client.Command.OPTIMIZE);
		}

		if (context.isUpdating()) {
			xres.write(res.getPrinter());
		}
	}

	@Override
	public void setStatus() {
		res.setStatus(HttpServletResponse.SC_OK);
	}

}
