/*
 * @(#)$Id: DeleteUser.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.user;

import javax.servlet.http.HttpServletResponse;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Request;
import jp.powerbase.Response;
import jp.powerbase.User;
import jp.powerbase.basex.Client;
import jp.powerbase.response.XMLResponse;
import jp.powerbase.xmldb.Processor;

public class DeleteUser implements Processor {
	private String userId;

	private Client client;
	private Response res;

	public DeleteUser(Request req, Response res) throws PowerBaseException {
		client = req.getClient();
		this.res = res;
		userId = req.getUserId();
		if (userId.equals(User.ADMINISTRATOR)) {
			throw new PowerBaseException(PowerBaseError.Code.CAN_NOT_DELETE_ADMIN_USER);
		}

		int count = Integer.valueOf(client.executeXQuery("count(doc('users')/root/user[@id='" + userId + "'])"));
		if (count == 0) {
			throw new PowerBaseException(PowerBaseError.Code.USER_NOT_FOUND);
		}

		count = Integer.valueOf(client.executeXQuery("count(doc('databases')//database[@owner='" + userId + "'])"));
		if (count != 0) {
			throw new PowerBaseException(PowerBaseError.Code.USER_STILL_OWNED_DATABASE);
		}

	}

	private void delete() throws PowerBaseException {
		StringBuffer xquery = new StringBuffer();
		xquery.append("delete node /root/user[@id='");
		xquery.append(userId);
		xquery.append("']");

		client.execute(Client.Command.OPEN, "users");
		client.execute(Client.Command.XQUERY, xquery.toString());
		client.execute(Client.Command.OPTIMIZE);

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
