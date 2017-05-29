/*
 * @(#)$Id: Initializer.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.xmldb;

import java.io.IOException;

import org.basex.BaseXServer;
import org.basex.api.client.ClientSession;

import jp.powerbase.Settings;

public final class Initializer {
	@SuppressWarnings("resource")
	public static final void StartBaeX() throws IOException {
		if (Settings.get(Settings.Symbol.BASEX_SERVER).equals("local")) {
			String host = Settings.get(Settings.Symbol.BASEX_HOST);
			int port = Integer.valueOf(Settings.get(Settings.Symbol.BASEX_PORT));
			String userid = Settings.get(Settings.Symbol.BASEX_USERID);
			String passwd = Settings.get(Settings.Symbol.BASEX_PASSWD);
			try {
				new ClientSession(host, port, userid, passwd);
			} catch (IOException e) {
				new BaseXServer();
			}
		}
	}

}
