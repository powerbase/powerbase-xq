/*
 * @(#)$Id: ResourceManager.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.xmldb.process;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Request;
import jp.powerbase.Response;
import jp.powerbase.basex.Client;
import jp.powerbase.xmldb.Processor;
import jp.powerbase.xmldb.dbmanagement.AddPermissionToDatabase;
import jp.powerbase.xmldb.dbmanagement.AddPermissionToDirectory;
import jp.powerbase.xmldb.dbmanagement.CreateDatabase;
import jp.powerbase.xmldb.dbmanagement.CreateDirectory;
import jp.powerbase.xmldb.dbmanagement.DeleteDatabase;
import jp.powerbase.xmldb.dbmanagement.DeleteDirectory;
import jp.powerbase.xmldb.dbmanagement.RemovePermissionFromDatabase;
import jp.powerbase.xmldb.dbmanagement.RemovePermissionFromDirectory;

public class ResourceManager extends AbstractProcessor implements Processor {
	private Processor processor;

	public ResourceManager(Request req, Response res) throws PowerBaseException {
		super(req, res);
	}

	@Override
	public void process() throws PowerBaseException {
		// evaluate command
		switch (req.getCommand()) {
		case CREATE_DIRECTORY:
			processor = new CreateDirectory(req, res);
			break;
		case CREATE_DATABASE:
			processor = new CreateDatabase(req, res);
			break;
		case DELETE_DATABASE:
			processor = new DeleteDatabase(req, res);
			break;
		case DELETE_DIRECTORY:
			processor = new DeleteDirectory(req, res);
			break;
		case ADD_PERMISSION_TO_DATABASE:
			processor = new AddPermissionToDatabase(req, res);
			break;
		case ADD_PERMISSION_TO_DIRECTORY:
			processor = new AddPermissionToDirectory(req, res);
			break;
		case REMOVE_PERMISSION_FROM_DATABASE:
			processor = new RemovePermissionFromDatabase(req, res);
			break;
		case REMOVE_PERMISSION_FROM_DIRECTORY:
			processor = new RemovePermissionFromDirectory(req, res);
			break;

		default:
			throw new PowerBaseException(PowerBaseError.Code.INVALID_COMMAND);
		}

		processor.execute();

		Client client = req.getClient();
		client.execute(Client.Command.OPEN, "databases");
		client.execute(Client.Command.OPTIMIZE);
	}

	@Override
	public void setStatus() {
		processor.setStatus();
	}

}
