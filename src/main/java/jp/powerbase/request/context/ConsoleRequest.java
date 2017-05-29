/*
 * @(#)$Id: ConsoleRequest.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.request.context;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import jp.powerbase.Command;
import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.User;
import jp.powerbase.basex.Client;
import jp.powerbase.console.AdminConsole;
import jp.powerbase.console.CommandRetriever;
import jp.powerbase.console.cmd.CreateDB;
import jp.powerbase.request.XQueryContext;
import jp.powerbase.util.StringUtil;
import jp.powerbase.util.TextReader;
import jp.powerbase.xml.DOM;
import jp.powerbase.xmldb.resource.Database;
import jp.powerbase.xmldb.resource.DefaultDirectory;
import jp.powerbase.xmldb.resource.Directory;
import jp.powerbase.xmldb.resource.Path;
import jp.powerbase.xmldb.resource.Database.Type;
import jp.powerbase.xquery.expr.Logical;
import jp.powerbase.xquery.expr.Logical.Operator;

public class ConsoleRequest extends RequestContext {
	private String dataDir;
	private AdminConsole console;
	private String baseURI;

	private String userId;
	private String userName;
	private String newPassword;

	private String groupId;

	private List<String> ftQuery = new ArrayList<String>();
	private Logical.Operator ftLop = null;

	public ConsoleRequest(AdminConsole console, User user) throws PowerBaseException {
		this.console = console;
		this.user = user;

		if (this.console instanceof CreateDB) {
			command = Command.CREATE_DATABASE_SPACE;
			if (StringUtil.isEmpty(this.console.dataDir)) {
				throw new PowerBaseException(PowerBaseError.Code.DATABASE_DIRECTORY_NOT_SPECIFIED);
			}
		} else {
			command = Command.getCommand(this.console.cmd);
			if (command == Command.CREATE_DATABASE_SPACE) {
				throw new PowerBaseException(PowerBaseError.Code.INVALID_COMMAND);
			}
		}

		dataDir = this.console.dataDir;
		if (console.iserver != null) {
			baseURI = console.iserver.getURI().toString();
		} else {
			baseURI = "";
		}

		groupId = this.console.groupId;
		userId = this.console.userId;
		userName = this.console.userName;
		newPassword = this.console.newPassword;

	}

	public void setRequestBody(Document requestBody) {
		this.requestBody = requestBody;
	}

	@Override
	public Command getCommand() {
		return command;
	}

	@Override
	public Client getClient() {
		return client;
	}

	@Override
	public Directory getDirectory() {
		return dirObject;
	}

	@Override
	public Database getDatabase() {
		return dbObject;
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public XQueryContext getXqueryContext() {
		return xqueryContext;
	}

	@Override
	public String getDirectoryName() {
		return directory;
	}

	@Override
	public String getDatabaseName() {
		return database;
	}

	@Override
	public String getNodeID() {
		return nodeID;
	}

	@Override
	public String getXPath() {
		return xpath;
	}

	@Override
	public Document getRequestBody() {
		return requestBody;
	}

	@Override
	public String getBaseURI() {
		return baseURI;
	}

	@Override
	public String getQueryWhere() {
		return console.where;
	}

	@Override
	public String getQueryOrder() {
		return console.orderBy;
	}

	@Override
	public String getQuerySelect() {
		return console.select;
	}

	@Override
	public String getRootTag() {
		return "";
	}

	@Override
	public boolean isWrapingResponse() {
		return false;
	}

	@Override
	public String getUserId() {
		return userId;
	}

	@Override
	public String getGroupId() {
		return groupId;
	}

	@Override
	public List<String> getFTQuery() {
		return ftQuery;
	}

	@Override
	public Logical.Operator getFTLop() {
		return ftLop;
	}

	public String getDataDir() {
		return dataDir;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return newPassword;
	}

	@Override
	protected Path getContextPath() {
		return new Path(console.res);
	}

	@Override
	protected void override() throws PowerBaseException {
		if (!StringUtil.isEmpty(console.from) && !StringUtil.isEmpty(console.to)) {
			rangeMode = true;
			this.rangeMin = Integer.valueOf(console.from);
			this.rangeMax = Integer.valueOf(console.to);
		}

		this.dirObject = null;
		if (!directory.equals("")) {
			this.dirObject = new DefaultDirectory(this.client, new Path(directory));
			if (!this.dirObject.exists()) {
				throw new PowerBaseException(PowerBaseError.Code.INVALID_LOCATION_PATH);
			}
		} else {
			// root access
			this.dirObject = new DefaultDirectory(this.client, new Path("/"));
		}

		if (position <= 0) {
			if (!StringUtil.isEmpty(console.id)) {
				this.nodeID = console.id;
			}

		}

		if (!indexViewing) {
			if (!StringUtil.isEmpty(console.xpath)) {
				this.xpath = console.xpath;
			}
		} else {
			this.xpath = "";
			this.nodeID = "";
		}

		if (command == null) {
			if (!StringUtil.isEmpty(console.xquery)) {
				command = Command.EXECUTE_XQUERY;
			} else if (!StringUtil.isEmpty(console.xqueryFile)) {
				command = Command.EXECUTE_XQUERY;
			} else if (!StringUtil.isEmpty(console.entity)) // POST, PUT系
			{
				try {
					String res = TextReader.read(new File(console.entity));
					DOM dom = null;

					if (!res.toString().equals("")) {
						dom = new DOM(res.toString());
						requestBody = dom.get();
						command = new CommandRetriever(requestBody).getCommand();
					}
				} catch (ParserConfigurationException e) {
					throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
				} catch (SAXException e) {
					throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
				} catch (IOException e) {
					throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
				}

			} else if (!StringUtil.isEmpty(console.source)) // LOAD_XML
			{
				try {
					if (dbObject.getType() == Type.FILE) {
						//this.command = Command.CREATE_OR_UPDATE_FILE;
						throw new PowerBaseException(PowerBaseError.Code.UNSUPPORTED_FUNCTION);
					} else {
						this.command = Command.LOAD_XML;
						DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
						DocumentBuilder docbuilder = null;
						docbuilder = dbfactory.newDocumentBuilder();
						requestBody = docbuilder.newDocument();

						Element root = requestBody.createElement("request");
						root.setAttribute("cmd", this.command.getValue());

						Element fnm = requestBody.createElement("file");
						fnm.appendChild(requestBody.createTextNode(console.source));
						root.appendChild(fnm);

						requestBody.appendChild(root);
					}
				} catch (DOMException e) {
					throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
				} catch (ParserConfigurationException e) {
					throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
				}

			} else {
				if (ftLop != null) {
					this.command = Command.FULL_TEXT_SEARCH;
				} else {
					if (dbObject == null) {
						this.command = Command.GET_INDEX;
					} else {
						if (getDatabase().isTuple()) {
							if (getNodeID().equals("") && getXPath().equals("")) {
								if (isIndexViewing()) {
									this.command = Command.GET_LIST;
								} else {
									this.command = Command.GET;
								}

							} else if (!getNodeID().equals("")) {
								this.command = Command.GET_BY_ID;
							} else if (!getXPath().equals("")) {
								this.command = Command.GET_BY_XPATH;
							} else {
								throw new PowerBaseException(PowerBaseError.Code.INVALID_COMMAND);
							}
						} else {
							if (!getXPath().equals("") && getNodeID().equals("")) {
								this.command = Command.GET_BY_XPATH;
							} else if (!getNodeID().equals("")) {
								this.command = Command.GET_BY_ID;
							} else {
								this.command = Command.GET;
							}
						}
					}

				}

			}
		}

	}

	@Override
	String getContextRevision() {
		return console.rev;
	}

	@Override
	Command getContextCommand() throws PowerBaseException {
		return command;
	}

	@Override
	String getContextXQuery() {
		return console.xquery;
	}

	@Override
	void setFtParam() {
		String ftAnd = this.console.ftAnd;
		String ftOr = this.console.ftOr;

		String separator = ",";

		String[] andArray = null;
		String[] orArray = null;

		if (!StringUtil.isEmpty(ftAnd)) {
			andArray = ftAnd.split(separator);
		}

		if (!StringUtil.isEmpty(ftOr)) {
			orArray = ftAnd.split(separator);
		}

		if (andArray != null && andArray.length != 0) {
			ftLop = Operator.AND;
			ftQuery.addAll(Arrays.asList(andArray));
		} else if (orArray != null && orArray.length != 0) {
			ftLop = Operator.OR;
			ftQuery.addAll(Arrays.asList(orArray));
		}
	}

}
