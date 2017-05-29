/*
 * @(#)$Id: RemoveUserFromGroup.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import jp.powerbase.Group;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Request;
import jp.powerbase.Response;
import jp.powerbase.basex.Client;
import jp.powerbase.response.XMLResponse;
import jp.powerbase.xmldb.Processor;

public class RemoveUserFromGroup extends AddUserToGroup implements Processor {

	public RemoveUserFromGroup(Request req, Response res) throws PowerBaseException {
		super(req, res);
	}

	@Override
	protected boolean check(String group, List<Group> currentGroups) {
		Iterator j = currentGroups.iterator();
		boolean notFound = true;
		while (j.hasNext()) {
			Group current = (Group) j.next();
			if (group.equals(current.getName())) {
				notFound = false;
				break;
			}
		}
		return notFound;
	}

	@Override
	protected void create() throws PowerBaseException {
		if (groups.size() == 0) {
			return;
		}
		String[] queries = new String[groups.size()];

		int c = 0;
		Iterator i = groups.iterator();
		while (i.hasNext()) {
			Group group = (Group) i.next();
			StringBuilder q = new StringBuilder();
			q.append("delete node ");
			q.append("/root/user[@id='");
			q.append(user);
			q.append("']/groups/group[text()='");
			q.append(group.getName());
			q.append("']");
			queries[c++] = q.toString();
		}

		String query = StringUtils.join(queries, ",");

		client.execute(Client.Command.OPEN, "users");
		client.execute(Client.Command.XQUERY, query.toString());
		client.execute(Client.Command.OPTIMIZE);

	}

	@Override
	public void execute() throws PowerBaseException {
		create();
		new XMLResponse(res).write(res.getPrinter());

	}

	@Override
	public void setStatus() {
		res.setStatus(HttpServletResponse.SC_OK);
	}

}
