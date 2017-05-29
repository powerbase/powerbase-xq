/*
 * @(#)$Id: CommandDispatcher.java 1178 2011-07-22 10:16:56Z hirai $
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

import org.w3c.dom.Element;

import jp.powerbase.Command;
import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.constant.HttpRequest;
import jp.powerbase.request.context.ServletRequest;
import jp.powerbase.request.context.ServletRequest.Parameters;
import jp.powerbase.util.StringUtil;
import jp.powerbase.xml.DOM;
import jp.powerbase.xmldb.resource.Database;

public class CommandDispatcher {
	private Command command;

	public CommandDispatcher(HttpRequest.Method method, ServletRequest ctx) throws PowerBaseException {
		String command;
		switch (method) {
		case GET:
			try {
				command = ctx.getHttpRequestParameter(ServletRequest.Parameters.QUERY_NAME_COMMAND).toLowerCase();
			} catch (Exception e) {
				command = "";
			}
			if (!command.equals("")) {
				for (Command val : Command.values()) {
					if (val.value.equalsIgnoreCase(command)) {
						this.command = val;
					}
				}
			} else {
				this.command = null;
			}

			if (this.command == null) {
				if (!StringUtil.isEmpty(ctx.getRevision())) {
					this.command = Command.GET_FILE;
				} else if (ctx.getFTLop() != null) {
					this.command = Command.FULL_TEXT_SEARCH;
				} else {
					Database dbObject = ctx.getDatabase();
					if (dbObject == null) {
						if (ctx.isRecursive()) {
							this.command = Command.GET_BY_XPATH_RECURSIVE;
						} else {
							if (ctx.getHttpRequestParameter(Parameters.QUERY_NAME_XQUERY) != null && !ctx.getHttpRequestParameter(Parameters.QUERY_NAME_XQUERY).equals("")) {
								this.command = Command.GET_BY_XQUERY;
							} else {
								this.command = Command.GET_INDEX;
							}
						}
					} else {
						if (ctx.getDatabase().isTuple()) {
							if (ctx.getNodeID().equals("") && ctx.getXPath().equals("")) {
								if (ctx.isIndexViewing()) {
									this.command = Command.GET_LIST;
								} else {
									this.command = Command.GET;
								}

							} else if (!ctx.getNodeID().equals("")) {
								this.command = Command.GET_BY_ID;
							} else if (!ctx.getXPath().equals("")) {
								this.command = Command.GET_BY_XPATH;
							} else {
								throw new PowerBaseException(PowerBaseError.Code.INVALID_COMMAND);
							}
						} else {
							if (!ctx.getXPath().equals("") && ctx.getNodeID().equals("")) {
								this.command = Command.GET_BY_XPATH;
							} else if (!ctx.getNodeID().equals("")) {
								this.command = Command.GET_BY_ID;
							} else {
								this.command = Command.GET;
							}
						}
					}
				}
			}
			break;
		case POST:
		case PUT:
			try {
				command = ctx.getHttpRequestParameter(ServletRequest.Parameters.QUERY_NAME_COMMAND).toLowerCase();
			} catch (Exception e) {
				DOM dom = new DOM(ctx.getRequestBody());
				Element root = dom.get().getDocumentElement();
				command = root.getAttribute("cmd");
			}
			if (!command.equals("")) {
				for (Command val : Command.values()) {
					if (val.value.equalsIgnoreCase(command)) {
						this.command = val;
					}
				}
			} else {
				this.command = null;
			}

			break;

		case DELETE:
			try {
				command = ctx.getHttpRequestParameter(ServletRequest.Parameters.QUERY_NAME_COMMAND).toLowerCase();
			} catch (Exception e) {
				command = "";
			}
			if (!command.equals("")) {
				for (Command val : Command.values()) {
					if (val.value.equalsIgnoreCase(command)) {
						this.command = val;
					}
				}
			} else {
				this.command = null;
			}

			if (this.command == null) {
				if (ctx.getDirectory().equals("")) {
					throw new PowerBaseException(PowerBaseError.Code.UNABLE_DELETE_ROOT_DIRECTORY);
				} else if (ctx.getDatabaseName().equals("")) {
					this.command = Command.DELETE_DIRECTORY;
				} else if (ctx.getNodeID().equals("") && ctx.getXPath().equals("")) {
					this.command = Command.DELETE_DATABASE;
				} else if (ctx.getXPath().equals("") && !ctx.getNodeID().equals("")) {
					this.command = Command.DELETE_NODE;
				} else if (!ctx.getXPath().equals("") && ctx.getNodeID().equals("")) {
					this.command = Command.DELETE_NODE;
				} else {
					throw new PowerBaseException(PowerBaseError.Code.INVALID_COMMAND);
				}
			}
			break;

		default:
			break;
		}

	}

	public Command getCommand() {
		return command;
	}

}
