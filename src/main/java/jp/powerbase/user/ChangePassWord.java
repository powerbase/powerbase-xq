/*
 * @(#)$Id: ChangePassWord.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.io.File;
import java.security.KeyPair;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Request;
import jp.powerbase.Response;
import jp.powerbase.Settings;
import jp.powerbase.basex.Client;
import jp.powerbase.response.XMLResponse;
import jp.powerbase.security.Cipher;
import jp.powerbase.security.CipherUtil;
import jp.powerbase.security.RSA;
import jp.powerbase.xml.DOM;
import jp.powerbase.xmldb.Processor;

public class ChangePassWord implements Processor {
	protected String userId;
	protected String password;

	protected Client client;
	protected Response res;

	public ChangePassWord() {
	}

	public ChangePassWord(Request req, Response res) throws PowerBaseException {
		client = req.getClient();
		this.res = res;

		if (!req.getUser().isAdmin()) {
			throw new PowerBaseException(PowerBaseError.Code.PERMISSION_DENIED);
		}

		DOM dom = new DOM(req.getRequestBody());
		try {
			DOM def = new DOM(dom.evalXpath("/request/user"));
			userId = def.getNodeValue("/user/@id");
			if (userId.equals("")) {
				throw new PowerBaseException(PowerBaseError.Code.USER_ID_NOT_SPECIFIED);
			}

			String p = def.getNodeValue("/user/password/text()");
			if (p.equals("")) {
				throw new PowerBaseException(PowerBaseError.Code.PASSWORD_IS_EMPTY);
			}

			KeyPair rsaKey = (KeyPair) CipherUtil.getKey(new File(Settings.get(Settings.Symbol.DATA_DIR), ".rsa"));
			Cipher r = new RSA(rsaKey);
			password = r.encrypt(p);

		} catch (XPathExpressionException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (ParserConfigurationException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		}
	}

	protected void update() throws PowerBaseException {
		StringBuilder q = new StringBuilder();
		q.append("replace value of node /root/user[@id='");
		q.append(userId);
		q.append("']/password with '");
		q.append(password);
		q.append("'");

		client.execute(Client.Command.OPEN, "users");
		try {
			client.execute(Client.Command.XQUERY, q.toString());
		} catch (Exception e) {
			throw new PowerBaseException(PowerBaseError.Code.USER_NOT_FOUND);
		}
		client.execute(Client.Command.OPTIMIZE);
	}

	@Override
	public void execute() throws PowerBaseException {
		update();
		new XMLResponse(res).write(res.getPrinter());

	}

	@Override
	public void setStatus() {
		res.setStatus(HttpServletResponse.SC_OK);
	}

}
