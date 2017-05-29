/*
 * @(#)$Id: Connection.java 1121 2011-06-13 10:39:57Z hirai $
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
package jp.powerbase.basex;

public class Connection {
	public enum Mode {
		STANDALONE("standalone"),
		LOCAL("local"),
		REMOTE("remote"),
		;
		public final String value;

		Mode(String value) {
			this.value = value;
		}

		public static Mode getMode(String key) {
			for (Mode val : Mode.values()) {
				if (val.value.equalsIgnoreCase(key)) {
					return val;
				}
			}
			return null;
		}
	}

	public final Mode mode;
	public final String host;
	public final int port;
	public final String userid;
	public final String password;

	public Connection(Mode mode, String host, int port, String userid, String password) {
		assert (mode != null);
		this.mode = mode;
		this.host = host;
		this.port = port;
		this.userid = userid;
		this.password = password;
	}

}
