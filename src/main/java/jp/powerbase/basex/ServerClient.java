/*
 * @(#)$Id: ServerClient.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.basex.api.client.ClientSession;
import org.basex.core.BaseXException;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;

/**
 * Client for BaseX Server.
 *
 * @author Toshio HIRAI(Infinite Corporation)
 *
 */
public class ServerClient implements Client {
	private OutputStream out;
	private ClientSession session;

	/**
	 * @param out OutputStream to write result.
	 * @throws PowerBaseException
	 */
	public ServerClient(OutputStream out, String host, int port, String userid, String passwd) throws PowerBaseException {
		this.out = out;
		try {
			session = new ClientSession(host, port, userid, passwd);
		} catch (IOException e) {
			throw new PowerBaseException(PowerBaseError.Code.BASEX_SERVER_STOPPED, e);
		}
	}

	@Override
	public void execute(Command command, String... args) throws PowerBaseException {
		StringBuilder parameters = new StringBuilder();

		for (String s : args) {
			parameters.append(" ");
			parameters.append(s);
		}

		session.setOutputStream(out);
		try {
			session.execute(command.getCommand() + parameters.toString());
		} catch (BaseXException e) {
			throw new PowerBaseException(PowerBaseError.Code.BASEX_INVOCATION_FAILED, e);
		} catch (IOException e) {
			throw new PowerBaseException(PowerBaseError.Code.BASEX_INVOCATION_FAILED, e);
		}

	}

	@Override
	public void close() throws PowerBaseException {
		try {
			execute(Client.Command.CLOSE);
			session.close();
		} catch (IOException e) {
			throw new PowerBaseException(PowerBaseError.Code.BASEX_INVOCATION_FAILED, e);
		}

	}

	@Override
	public String executeXQuery(String xquery) throws PowerBaseException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		session.setOutputStream(out);
		try {
			session.execute("XQUERY " + xquery);
			return out.toString("UTF-8");
		} catch (BaseXException e) {
			throw new PowerBaseException(PowerBaseError.Code.BASEX_INVOCATION_FAILED, e);
		} catch (UnsupportedEncodingException e) {
			throw new PowerBaseException(PowerBaseError.Code.BASEX_INVOCATION_FAILED, e);
		} catch (IOException e) {
			throw new PowerBaseException(PowerBaseError.Code.BASEX_INVOCATION_FAILED, e);
		}

	}

}
