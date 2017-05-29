/*
 * @(#)$Id: Authentication.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.servlet.request;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Settings;
import jp.powerbase.User;
import jp.powerbase.basex.Client;
import jp.powerbase.user.UserAuth;
import jp.powerbase.user.PowerBaseUser;
import jp.powerbase.user.WSSEParser;
import jp.powerbase.util.Base64;
import jp.powerbase.util.Digest;

public class Authentication implements UserAuth {
	private String method;
	private WSSEParser wsse;
	private User user;

	public Authentication(HttpServletRequest req, Client client) throws PowerBaseException {
		this.method = Settings.get(Settings.Symbol.USER_AUTH_METHOD);
		wsse = null;
		if (this.method.toUpperCase().equals("WSSE")) {
			wsse = new WSSEParser(req.getHeader("x-wsse"));
		}

		// user confirmation process
		String user = "";
		if (Settings.get(Settings.Symbol.USER_AUTH_METHOD).toUpperCase().equals("WSSE")) {
			user = wsse.getUsername();
		} else if (Settings.get(Settings.Symbol.USER_AUTH_METHOD).toUpperCase().equals("BASIC") || Settings.get(Settings.Symbol.USER_AUTH_METHOD).toUpperCase().equals("DIGEST")) {
			user = req.getRemoteUser();
		}

		if (user != null && !user.equals("")) {
			this.user = new PowerBaseUser(client, user);
		} else {
			this.user = new PowerBaseUser();
		}

		if (Settings.get(Settings.Symbol.USER_AUTH).equals("true")) {
			// check userid
			if (!this.user.isValid()) {
				throw new PowerBaseException(PowerBaseError.Code.AUTHENTICATION_FAILED);
			}
			// check password
			try {
				if (!isValid(this.user)) {
					throw new PowerBaseException(PowerBaseError.Code.AUTHENTICATION_FAILED);
				}
			} catch (IOException e) {
				throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
			}
		}

	}

	public boolean isValid(User user) throws IOException {
		if (this.method.toUpperCase().equals("WSSE")) {
			byte[] nonce = Base64.decode(wsse.getNonce());
			byte[] created = wsse.getCreated().getBytes();
			byte[] password = user.getPassWord().getBytes();
			byte[] v = new byte[nonce.length + created.length + password.length];
			System.arraycopy(nonce, 0, v, 0, nonce.length);
			System.arraycopy(created, 0, v, nonce.length, created.length);
			System.arraycopy(password, 0, v, nonce.length + created.length, password.length);
			byte[] d = Digest.sha1(v);
			String digest = Base64.encode(d);

			if (digest.trim().equals(wsse.getPasswordDigest().trim())) {
				return true;
			} else {
				return false;
			}
		} else if (this.method.toUpperCase().equals("BASIC") || this.method.toUpperCase().equals("DIGEST")) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public User getUser() {
		return user;
	}

}
