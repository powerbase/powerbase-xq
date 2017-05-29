/*
 * @(#)$Id: WSSEParser.java 1087 2011-05-25 05:28:29Z hirai $
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

public class WSSEParser {
	private String Username;
	private String PasswordDigest;
	private String Created;
	private String Nonce;

	public WSSEParser(String wsse) {
		if (wsse == null) {
			return;
		}
		String w = wsse.replaceAll("UsernameToken", "");

		String[] elem = w.split(",");
		for (int i = 0; i < elem.length; i++) {
			String el = elem[i].trim();
			if (el.indexOf("Username") != -1) {
				Username = el.substring(el.indexOf('=') + 1).replaceAll("\"", "");
			}
			if (el.indexOf("PasswordDigest") != -1) {
				PasswordDigest = el.substring(el.indexOf('=') + 1).replaceAll("\"", "");
			}
			if (el.indexOf("Created") != -1) {
				Created = el.substring(el.indexOf('=') + 1).replaceAll("\"", "");
			}
			if (el.indexOf("Nonce") != -1) {
				Nonce = el.substring(el.indexOf('=') + 1).replaceAll("\"", "");
			}
		}
	}

	public String getUsername() {
		return Username;
	}

	public String getPasswordDigest() {
		return PasswordDigest;
	}

	public String getCreated() {
		return Created;
	}

	public String getNonce() {
		return Nonce;
	}

}
