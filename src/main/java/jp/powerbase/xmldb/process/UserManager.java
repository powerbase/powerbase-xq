/*
 * @(#)$Id: UserManager.java 1178 2011-07-22 10:16:56Z hirai $
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
import jp.powerbase.user.AddUserToGroup;
import jp.powerbase.user.ChangeOwnPassWord;
import jp.powerbase.user.ChangePassWord;
import jp.powerbase.user.CreateGroup;
import jp.powerbase.user.CreateUser;
import jp.powerbase.user.DeleteGroup;
import jp.powerbase.user.DeleteUser;
import jp.powerbase.user.RemoveUserFromGroup;
import jp.powerbase.xmldb.Processor;

public class UserManager extends AbstractProcessor implements Processor {
	private Processor processor;

	public UserManager(Request req, Response res) throws PowerBaseException {
		super(req, res);
	}

	@Override
	public void process() throws PowerBaseException {
		// evaluate command
		switch (req.getCommand()) {
		case CREATE_USER:
			processor = new CreateUser(req, res);
			break;
		case DELETE_USER:
			processor = new DeleteUser(req, res);
			break;
		case CREATE_GROUP:
			processor = new CreateGroup(req, res);
			break;
		case DELETE_GROUP:
			processor = new DeleteGroup(req, res);
			break;
		case CHANGE_PASSWORD:
			processor = new ChangePassWord(req, res);
			break;
		case CHANGE_OWN_PASSWORD:
		case CHANGE_ADMIN_PASSWORD:
			processor = new ChangeOwnPassWord(req, res);
			break;
		case ADD_USER_TO_GROUP:
			processor = new AddUserToGroup(req, res);
			break;
		case REMOVE_USER_FROM_GROUP:
			processor = new RemoveUserFromGroup(req, res);
			break;

		default:
			throw new PowerBaseException(PowerBaseError.Code.INVALID_COMMAND);
		}

		processor.execute();
	}

	@Override
	public void setStatus() {
		processor.setStatus();
	}

}
