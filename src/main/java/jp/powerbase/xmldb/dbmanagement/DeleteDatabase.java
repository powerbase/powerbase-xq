/*
 * @(#)$Id: DeleteDatabase.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.io.File;

import javax.servlet.http.HttpServletResponse;

import jp.powerbase.PowerBaseException;
import jp.powerbase.Request;
import jp.powerbase.Response;
import jp.powerbase.Settings;
import jp.powerbase.basex.Client;
import jp.powerbase.response.XMLResponse;
import jp.powerbase.util.FileUtil;
import jp.powerbase.xmldb.Processor;
import jp.powerbase.xmldb.resource.Database;
import jp.powerbase.xmldb.resource.Database.Type;

public class DeleteDatabase implements Processor {
	private Database db;
	private String id;

	private Client client;
	private Response res;

	public DeleteDatabase(Request req, Response res) throws PowerBaseException {
		db = req.getDatabase();
		id = Integer.toString(db.getId());

		client = req.getClient();
		this.res = res;
	}

	private void delete() throws PowerBaseException {
		StringBuffer xquery = new StringBuffer();
		xquery.append("delete node ");
		xquery.append("db:open-id(\"databases\", ");
		xquery.append(id);
		xquery.append(")");

		client.execute(Client.Command.XQUERY, xquery.toString());
		client.execute(Client.Command.DROP_DB, id);

		if (db.getType() == Type.FILE) {
			File dir = new File(Settings.get(Settings.Symbol.DATA_DIR) + "/files/" + Integer.toString(db.getId()));
			FileUtil.delete(dir);
		}

	}

	@Override
	public void execute() throws PowerBaseException {
		delete();
		new XMLResponse(res).write(res.getPrinter());
	}

	@Override
	public void setStatus() {
		res.setStatus(HttpServletResponse.SC_OK);
	}

}
