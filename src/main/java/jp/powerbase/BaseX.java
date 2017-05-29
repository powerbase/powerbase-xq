/*
 * @(#)$Id: BaseX.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase;

import java.io.OutputStream;

import jp.powerbase.basex.Client;
import jp.powerbase.basex.Connection;
import jp.powerbase.basex.Connection.Mode;
import jp.powerbase.basex.LocalClient;
import jp.powerbase.basex.ServerClient;

/**
 *
 * BaseX connection manager
 *
 * @author Toshio HIRAI <toshio.hirai@gmail.com>
 *
 */
public final class BaseX {
	private Client client;
	private Connection connection;

	/**
	 * Constructor.
	 * @param connection connection instance.
	 */
	public BaseX(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Constructor.
	 */
	public BaseX() {
		Mode mode = Mode.getMode(Settings.get(Settings.Symbol.BASEX_SERVER));
		String host = Settings.get(Settings.Symbol.BASEX_HOST);
		int port = Integer.valueOf(Settings.get(Settings.Symbol.BASEX_PORT));
		String userid = Settings.get(Settings.Symbol.BASEX_USERID);
		String passwd = Settings.get(Settings.Symbol.BASEX_PASSWD);
		this.connection = new Connection(mode, host, port, userid, passwd);
	}

	/**
	 * Get client.
	 *
	 * @param out result printer
	 * @return Client object
	 * @throws PowerBaseException exception
	 */
	public Client getClient(OutputStream out) throws PowerBaseException {
		if (connection.mode == Mode.STANDALONE) {
			client = new LocalClient(out);
		} else {
			client = new ServerClient(out, connection.host, connection.port, connection.userid, connection.password);
		}

		return client;

	}
}
