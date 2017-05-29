/*
 * @(#)$Id: RemovePermissionFromDirectory.java 1178 2011-07-22 10:16:56Z hirai $
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

public class RemovePermissionFromDirectory extends AddPermissionToDirectory implements Processor {
	public RemovePermissionFromDirectory() {
	}

	public RemovePermissionFromDirectory(Request req, Response res) throws PowerBaseException {
		super(req, res);
	}

	protected boolean getSw() {
		return false;
	}

	protected void removeVisible() throws PowerBaseException {
		if (visible.size() == 0) {
			return;
		}
		String[] queries = new String[visible.size()];

		int c = 0;
		Iterator i = visible.iterator();
		while (i.hasNext()) {
			String v = (String) i.next();
			StringBuilder q = new StringBuilder();
			q.append("delete node db:open-id('databases', ");
			q.append(directory.getId());
			q.append(")/permission/visible[text() = '");
			q.append(v);
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
		removeVisible();
		new XMLResponse(res).write(res.getPrinter());

	}

	@Override
	public void setStatus() {
		res.setStatus(HttpServletResponse.SC_OK);
	}

}
