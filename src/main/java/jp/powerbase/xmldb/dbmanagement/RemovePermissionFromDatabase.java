/*
 * @(#)$Id: RemovePermissionFromDatabase.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.xmldb.dbmanagement;

import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import jp.powerbase.PowerBaseException;
import jp.powerbase.Request;
import jp.powerbase.Response;
import jp.powerbase.basex.Client;
import jp.powerbase.response.XMLResponse;
import jp.powerbase.xmldb.Processor;

public class RemovePermissionFromDatabase extends AddPermissionToDatabase implements Processor {
	public RemovePermissionFromDatabase() {
	}

	public RemovePermissionFromDatabase(Request req, Response res) throws PowerBaseException {
		super(req, res);
	}

	protected boolean getSw() {
		return false;
	}

	protected void removeReadable() throws PowerBaseException {
		if (readable.size() == 0) {
			return;
		}
		String[] queries = new String[readable.size()];

		int c = 0;
		Iterator i = readable.iterator();
		while (i.hasNext()) {
			String r = (String) i.next();
			StringBuilder q = new StringBuilder();
			q.append("delete node db:open-id('databases', ");
			q.append(database.getId());
			q.append(")/permission/readable[text() = '");
			q.append(r);
			q.append("']");
			queries[c++] = q.toString();
		}

		String query = StringUtils.join(queries, ",");

		client.execute(Client.Command.OPEN, "databases");
		client.execute(Client.Command.XQUERY, query.toString());
		client.execute(Client.Command.OPTIMIZE);

	}

	protected void removeWritable() throws PowerBaseException {
		if (writable.size() == 0) {
			return;
		}
		String[] queries = new String[writable.size()];

		int c = 0;
		Iterator i = writable.iterator();
		while (i.hasNext()) {
			String w = (String) i.next();
			StringBuilder q = new StringBuilder();
			q.append("delete node db:open-id('databases', ");
			q.append(database.getId());
			q.append(")/permission/writable[text() = '");
			q.append(w);
			q.append("']");
			queries[c++] = q.toString();
		}

		String query = StringUtils.join(queries, ",");

		client.execute(Client.Command.OPEN, "databases");
		client.execute(Client.Command.XQUERY, query.toString());
		client.execute(Client.Command.OPTIMIZE);

	}

	@Override
	public void execute() throws PowerBaseException {
		removeReadable();
		removeWritable();
		new XMLResponse(res).write(res.getPrinter());

	}

	@Override
	public void setStatus() {
		res.setStatus(HttpServletResponse.SC_OK);
	}

}
