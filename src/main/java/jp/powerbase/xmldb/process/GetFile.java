/*
 * @(#)$Id: GetFile.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Request;
import jp.powerbase.Response;
import jp.powerbase.basex.Client;
import jp.powerbase.util.IOUtil;
import jp.powerbase.util.StringUtil;
import jp.powerbase.xmldb.resource.Database;
import jp.powerbase.xmldb.resource.FileDatabase;
import jp.powerbase.xmldb.resource.NodeId;

public class GetFile extends AbstractProcessor {
	protected String results;

	public GetFile() {
	}

	public GetFile(Request req, Response res) throws PowerBaseException {
		super(req, res);
	}

	/*
	 * (非 Javadoc)
	 *
	 * @see jp.infinite.xmldb.process.AbstractProcessor#process()
	 */
	@Override
	public void process() throws PowerBaseException {
		Client session = req.getClient();

		Database db = req.getDatabase();
		String rev = req.getRevision();
		String id = req.getNodeID();
		NodeId nodeId = new NodeId(req.getDatabase().getPath().toString(), Integer.valueOf(id));
		// String currentRev = session.executeXQuery(nodeId.toString() +
		// "/fn:string(@rev)");
		String contentType = session.executeXQuery(nodeId.toString() + "/@type");
		if (!db.exists() || StringUtil.isEmpty(id) || StringUtil.isEmpty(contentType)) {
			throw new PowerBaseException(PowerBaseError.Code.INVALID_LOCATION_PATH);
		}
		try {
			InputStream file = FileDatabase.load(db, Integer.valueOf(id), Integer.valueOf(rev), contentType);
			res.setContentType(contentType);
			IOUtil.copy(file, res.getPrinter());
		} catch (NumberFormatException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (IOException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		}

	}

	@Override
	public void setStatus() {
		res.setStatus(HttpServletResponse.SC_OK);
	}

}
