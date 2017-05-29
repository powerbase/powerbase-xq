/*
 * @(#)$Id: LocalClient.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.io.OutputStream;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;

import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.Close;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.CreateIndex;
import org.basex.core.cmd.DropDB;
import org.basex.core.cmd.Open;
import org.basex.core.cmd.Optimize;
import org.basex.core.cmd.Set;
import org.basex.core.cmd.XQuery;

/**
 * standalone mode.
 *
 * @author Toshio HIRAI(Infinite Corporation)
 *
 */
public class LocalClient implements Client {
	private OutputStream out;
	private Context context;

	/**
	 * @param out OutputStream to write result.
	 */
	public LocalClient(OutputStream out) {
		this.out = out;
		context = new Context();
	}

	@Override
	public void execute(Command command, String... args) throws PowerBaseException {
		try {
			switch (command) {
			case ADD:
				throw new PowerBaseException(PowerBaseError.Code.UNSUPPORTED_COMMAND);
			case ALTER_USER:
				throw new PowerBaseException(PowerBaseError.Code.UNSUPPORTED_COMMAND);
			case CHECK:
				throw new PowerBaseException(PowerBaseError.Code.UNSUPPORTED_COMMAND);
			case CLOSE:
				new Close().execute(context, out);
				break;
			case CREATE_COLL:
				throw new PowerBaseException(PowerBaseError.Code.UNSUPPORTED_COMMAND);
			case CREATE_DB:
				new CreateDB(args[0], args[1]).execute(context, out);
				break;
			case CREATE_FS:
				throw new PowerBaseException(PowerBaseError.Code.UNSUPPORTED_COMMAND);
			case CREATE_INDEX:
				new CreateIndex(args[0]).execute(context, out);
				break;
			case CREATE_MAB:
				throw new PowerBaseException(PowerBaseError.Code.UNSUPPORTED_COMMAND);
			case CREATE_USER:
				throw new PowerBaseException(PowerBaseError.Code.UNSUPPORTED_COMMAND);
			case CS:
				throw new PowerBaseException(PowerBaseError.Code.UNSUPPORTED_COMMAND);
			case DELETE:
				throw new PowerBaseException(PowerBaseError.Code.UNSUPPORTED_COMMAND);
			case DROP_DB:
				new DropDB(args[0]).execute(context, out);
				break;
			case DROP_INDEX:
				throw new PowerBaseException(PowerBaseError.Code.UNSUPPORTED_COMMAND);
			case DROP_USER:
				throw new PowerBaseException(PowerBaseError.Code.UNSUPPORTED_COMMAND);
			case EXPORT:
				throw new PowerBaseException(PowerBaseError.Code.UNSUPPORTED_COMMAND);
			case FIND:
				throw new PowerBaseException(PowerBaseError.Code.UNSUPPORTED_COMMAND);
			case GET:
				throw new PowerBaseException(PowerBaseError.Code.UNSUPPORTED_COMMAND);
			case GRANT:
				throw new PowerBaseException(PowerBaseError.Code.UNSUPPORTED_COMMAND);
			case INFO:
				throw new PowerBaseException(PowerBaseError.Code.UNSUPPORTED_COMMAND);
			case INFO_DB:
				throw new PowerBaseException(PowerBaseError.Code.UNSUPPORTED_COMMAND);
			case INFO_INDEX:
				throw new PowerBaseException(PowerBaseError.Code.UNSUPPORTED_COMMAND);
			case INFO_TABLE:
				throw new PowerBaseException(PowerBaseError.Code.UNSUPPORTED_COMMAND);
			case KILL:
				throw new PowerBaseException(PowerBaseError.Code.UNSUPPORTED_COMMAND);
			case LIST:
				throw new PowerBaseException(PowerBaseError.Code.UNSUPPORTED_COMMAND);
			case MOUNT:
				throw new PowerBaseException(PowerBaseError.Code.UNSUPPORTED_COMMAND);
			case OPEN:
				new Open(args[0]).execute(context, out);
				break;
			case OPTIMIZE:
				new Optimize().execute(context);
				break;
			case PASSWORD:
				throw new PowerBaseException(PowerBaseError.Code.UNSUPPORTED_COMMAND);
			case RUN:
				throw new PowerBaseException(PowerBaseError.Code.UNSUPPORTED_COMMAND);
			case SET:
				new Set(args[0], args[1]).execute(context, out);
				break;
			case SHOW_DATABASES:
				throw new PowerBaseException(PowerBaseError.Code.UNSUPPORTED_COMMAND);
			case SHOW_SESSIONS:
				throw new PowerBaseException(PowerBaseError.Code.UNSUPPORTED_COMMAND);
			case SHOW_USERS:
				throw new PowerBaseException(PowerBaseError.Code.UNSUPPORTED_COMMAND);
			case XQUERY:
				new XQuery(args[0]).execute(context, out);
				break;
			default:
				throw new PowerBaseException(PowerBaseError.Code.INVALID_COMMAND);
			}
		} catch (BaseXException e) {
			throw new PowerBaseException(PowerBaseError.Code.BASEX_INVOCATION_FAILED, e);
		}

	}

	@Override
	public void close() throws PowerBaseException {
		execute(Client.Command.CLOSE);
		context.close();
	}

	@Override
	public String executeXQuery(String xquery) throws PowerBaseException {
		try {
			return new XQuery(xquery).execute(context);
		} catch (BaseXException e) {
			throw new PowerBaseException(PowerBaseError.Code.BASEX_INVOCATION_FAILED, e);
		}
	}

}
