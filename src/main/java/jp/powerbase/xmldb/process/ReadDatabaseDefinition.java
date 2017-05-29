/*
 * @(#)$Id: ReadDatabaseDefinition.java 1178 2011-07-22 10:16:56Z hirai $
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

import javax.servlet.http.HttpServletResponse;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Request;
import jp.powerbase.Response;
import jp.powerbase.basex.Client;
import jp.powerbase.constant.CommonUseNameSpaces;
import jp.powerbase.response.ResponseTag;
import jp.powerbase.xmldb.resource.Database;
import jp.powerbase.xquery.expr.Brace;

public class ReadDatabaseDefinition extends AbstractProcessor {

	private Client client;

	public ReadDatabaseDefinition(Request req, Response res) throws PowerBaseException {
		super(req, res);
		client = req.getClient();
	}

	@Override
	public void process() throws PowerBaseException {
		Database db = req.getDatabase();
		if (db == null) {
			throw new PowerBaseException(PowerBaseError.Code.DATABASE_NOT_SPECIFIED);
		}

		StringBuffer ns = new StringBuffer();
		ns.append("declare namespace");
		ns.append(CommonUseNameSpaces.getNameSpacesAsString(false));
		ns.append(";");

		StringBuilder q = new StringBuilder();
		q.append("db:open-id(\"databases\", " + db.getId() + ")");

		ResponseTag rt = new ResponseTag(req.getRootTag());
		String query = rt.wrap(new Brace().wrap(q.toString()));

		client.execute(Client.Command.XQUERY, ns.toString() + query);

	}

	@Override
	public void setStatus() {
		res.setStatus(HttpServletResponse.SC_OK);
	}

}
