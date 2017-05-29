/*
 * @(#)$Id: DefaultLoader.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.xmldb.loader;

import java.io.File;

import jp.powerbase.PowerBaseException;
import jp.powerbase.Request;
import jp.powerbase.basex.Client;

public class DefaultLoader {
	Request req;
	File file;

	public DefaultLoader(Request req, File file) {
		this.req = req;
		this.file = file;
	}

	public void load() throws PowerBaseException {
		Client client = req.getClient();
		client.execute(Client.Command.CLOSE);
		client.execute(Client.Command.SET, "textindex", "on");
		client.execute(Client.Command.SET, "attrindex", "on");
		client.execute(Client.Command.SET, "ftindex", "on");
		client.execute(Client.Command.CREATE_DB, Integer.toString(req.getDatabase().getId()), file.toString());
//		client.execute(Client.Command.CREATE_INDEX, "TEXT");
//		client.execute(Client.Command.CREATE_INDEX, "ATTRIBUTE");
//		client.execute(Client.Command.CREATE_INDEX, "FULLTEXT");
//		client.execute(Client.Command.OPTIMIZE);

	}
}
