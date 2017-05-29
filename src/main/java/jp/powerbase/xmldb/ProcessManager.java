/*
 * @(#)$Id: ProcessManager.java 1178 2011-07-22 10:16:56Z hirai $
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

import jp.powerbase.Command;
import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Request;
import jp.powerbase.Response;
import jp.powerbase.xmldb.process.CreateOrUpdateFile;
import jp.powerbase.xmldb.process.GetFile;
import jp.powerbase.xmldb.process.ResourceManager;
import jp.powerbase.xmldb.process.GetServerInfo;
import jp.powerbase.xmldb.process.GetUserInfo;
import jp.powerbase.xmldb.process.ReadByXQuery;
import jp.powerbase.xmldb.process.ReadByXQueryViaXBird;
import jp.powerbase.xmldb.process.ReadDatabaseDefinition;
import jp.powerbase.xmldb.process.ReadDatabaseList;
import jp.powerbase.xmldb.process.UserManager;
import jp.powerbase.xmldb.process.XMLLoader;
import jp.powerbase.xmldb.process.ExecuteXQuery;

public class ProcessManager {
	private Command command;

	public ProcessManager(Command command) {
		this.command = command;
	}

	public final Processor getProcessor(Request req, Response res) throws PowerBaseException {
		Processor proc;

		switch (command) {
		case GET_SERVER_INFO:
			proc = new GetServerInfo(req, res);
			break;
		case GET_USER_INFO:
			proc = new GetUserInfo(req, res);
			break;

		case CREATE_USER:
		case CREATE_GROUP:
		case CHANGE_PASSWORD:
		case CHANGE_OWN_PASSWORD:
		case CHANGE_ADMIN_PASSWORD:
		case DELETE_USER:
		case DELETE_GROUP:
		case ADD_USER_TO_GROUP:
		case REMOVE_USER_FROM_GROUP:
			proc = new UserManager(req, res);
			break;

		case CREATE_DIRECTORY:
		case CREATE_DATABASE:
		case DELETE_DIRECTORY:
		case DELETE_DATABASE:
		case ALTER_DIRECTORY:
		case ALTER_DATABASE:
		case ADD_PERMISSION_TO_DATABASE:
		case ADD_PERMISSION_TO_DIRECTORY:
		case REMOVE_PERMISSION_FROM_DATABASE:
		case REMOVE_PERMISSION_FROM_DIRECTORY:
			proc = new ResourceManager(req, res);
			break;

		case LOAD_XML:
			proc = new XMLLoader(req, res);
			break;

		case CREATE_OR_UPDATE_FILE:
			proc = new CreateOrUpdateFile(req, res);
			break;

		case GET:
		case GET_BY_XPATH:
		case GET_BY_XPATH_RECURSIVE:
		case GET_DUMP:
		case GET_BY_ID:
		case GET_LIST:
		case GET_BY_XQUERY:
		case FULL_TEXT_SEARCH:
			proc = new ReadByXQuery(req, res);
			break;

		case GET_BY_XQUERY_VIA_XBIRD:
			proc = new ReadByXQueryViaXBird(req, res);
			break;

		case GET_INDEX:
			proc = new ReadDatabaseList(req, res);
			break;

		case GET_DATABASE_DEF:
			proc = new ReadDatabaseDefinition(req, res);
			break;

		case GET_FILE:
			proc = new GetFile(req, res);
			break;

		case EXECUTE_XQUERY:
			proc = new ExecuteXQuery(req, res);
			break;

		case INSERT_NODE:
		case REPLACE_NODE:
		case REPLACE_VALUE_OF_NODE:
		case DELETE_NODE:
			proc = new ExecuteXQuery(req, res);
			break;

		default:
			throw new PowerBaseException(PowerBaseError.Code.INVALID_COMMAND);
		}

		return proc;
	}

}
