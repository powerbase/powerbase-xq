/*
 * @(#)$Id: Command.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase;

import jp.powerbase.constant.HttpRequest;
import jp.powerbase.constant.HttpRequest.Method;

/**
 * Process Command enumration.
 *
 * @author Toshio HIRAI <toshio.hirai@gmail.com>
 *
 */
public enum Command {
	// Special
	/**
	 * Create Database.
	 */
	CREATE_DATABASE_SPACE("CreateDB", null),

	// Database manipuration
	CHANGE_OWNER("ChangeOwner", Method.PUT),
	CREATE_DIRECTORY("CreateDirectory", Method.POST),
	CREATE_DATABASE("CreateDatabase", Method.POST),
	DELETE_DIRECTORY("DeleteDirectory", Method.DELETE),
	DELETE_DATABASE("DeleteDatabase", Method.DELETE),
	ALTER_DIRECTORY("AlterDirectory", Method.PUT),
	ALTER_DATABASE("AlterDatabase", Method.PUT),
	ADD_PERMISSION_TO_DATABASE("AddPermissionToDatabase", Method.PUT),
	ADD_PERMISSION_TO_DIRECTORY("AddPermissionToDirectory", Method.PUT),
	REMOVE_PERMISSION_FROM_DATABASE("RemovePermissionFromDatabase", Method.PUT),
	REMOVE_PERMISSION_FROM_DIRECTORY("RemovePermissionFromDirectory",Method.PUT),

	// User manipuration
	CHANGE_PASSWORD("ChangePassWord", Method.PUT),
	CHANGE_ADMIN_PASSWORD("ChangeAdminPassWord", Method.PUT),
	CHANGE_OWN_PASSWORD("ChangeOwnPassWord", Method.PUT),
	CREATE_USER("CreateUser", Method.POST),
	DELETE_USER("DeleteUser", Method.DELETE),
	CREATE_GROUP("CreateGroup", Method.POST),
	DELETE_GROUP("DeleteGroup", Method.DELETE),
	ALTER_USER("AlterUser", Method.POST),
	ADD_USER_TO_GROUP("AddUserToGroup", Method.PUT),
	REMOVE_USER_FROM_GROUP("RemoveUserFromGroup", Method.PUT),

	// GET
	GET_SERVER_INFO("GetServerInfo", Method.GET),
	// GET_USER_LIST("GetUserList", Method.GET), // not implement, yet.
	GET_USER_INFO("GetUserInfo", Method.GET),

	GET_INDEX("GetIndex", Method.GET),
	GET_DATABASE_DEF("GetDatabaseDef", Method.GET),

	GET_DUMP("GetDump", Method.GET),
	GET_LIST("GetList", Method.GET),

	GET("Get", Method.GET),
	GET_BY_ID("GetByID", Method.GET),
	GET_BY_XPATH("GetByXPath", Method.GET),
	GET_BY_XPATH_RECURSIVE("GetByXPathRecursive", Method.GET),
	GET_BY_XQUERY("GetByXQuery", Method.GET),
	GET_BY_XQUERY_VIA_XBIRD("GetByXQueryViaXBird", Method.GET),
	FULL_TEXT_SEARCH("FullTextSearch", Method.GET),
	GET_FILE("GetFile", Method.GET),
	GET_FILE_EXCLUSIVE("GetFileExclusive ", Method.GET),

	// POST
	EXECUTE_XQUERY("ExecuteXQuery", Method.POST),
	INSERT_NODE("InsertNode", Method.POST),
	REPLACE_NODE("ReplaceNode", Method.POST), // caution!
	LOAD_XML("LoadXML", Method.POST),
	CREATE_OR_UPDATE_FILE("CreateOrUpdateFile", Method.POST),

	// PUT
	REPLACE_VALUE_OF_NODE("ReplaceValueOfNode", Method.PUT),
	UNLOCK_FILE("UnlockFile", Method.PUT),

	// DELETE
	DELETE_NODE("DeleteNode", Method.DELETE),

	;

	public final String value;
	public final HttpRequest.Method method;

	Command(String value, HttpRequest.Method method) {
		this.value = value;
		this.method = method;
	}

	public HttpRequest.Method getMethod() {
		return method;
	}

	public String getValue() {
		return value;
	}

	public static Command getCommand(String key) {
		for (Command val : Command.values()) {
			if (val.value.equalsIgnoreCase(key)) {
				return val;
			}
		}
		return null;
	}

}
