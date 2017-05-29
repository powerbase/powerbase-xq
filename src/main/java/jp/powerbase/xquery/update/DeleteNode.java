/*
 * @(#)$Id: DeleteNode.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.xquery.update;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.request.context.RequestContext;
import jp.powerbase.xmldb.resource.Database;
import jp.powerbase.xmldb.resource.NodeId;

public class DeleteNode implements XQueryUpdateBuilder {
	RequestContext ctx;
	Database db;

	public DeleteNode(RequestContext ctx) {
		this.ctx = ctx;
		db = this.ctx.getDatabase();
	}

	@Override
	public String build() throws PowerBaseException {
		String id = ctx.getNodeID();
		String path = ctx.getXPath();

		if (id.equals("1") || path.equals("/" + ctx.getDatabase().getRootTag())) {
			throw new PowerBaseException(PowerBaseError.Code.UNABLE_DELETE_ROOT_ELEMENT);
		}

		return getExpr(ctx.getDatabaseName(), id, path);
	}

	public String getExpr(String database, String id, String path) {
		StringBuffer q = new StringBuffer();
		NodeId nodeId = null;
		if (!id.equals("")) {
			nodeId = new NodeId(database, Integer.valueOf(id));
		}

		q.append("delete node ");
		if (nodeId == null) {
			q.append(path);
		} else {
			q.append(nodeId.toString());
		}
		return q.toString();
	}

}
