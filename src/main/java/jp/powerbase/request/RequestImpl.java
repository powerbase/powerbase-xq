/*
 * @(#)$Id: RequestImpl.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.request;

import java.util.List;
import java.util.Map;

import jp.powerbase.Command;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Request;
import jp.powerbase.User;
import jp.powerbase.basex.Client;
import jp.powerbase.file.UploadFile;

import org.w3c.dom.Document;

import jp.powerbase.request.context.RequestContext;
import jp.powerbase.xmldb.resource.Database;
import jp.powerbase.xmldb.resource.Directory;
import jp.powerbase.xmldb.resource.Path;
import jp.powerbase.xquery.expr.Logical.Operator;

/**
 * The Request instance.
 *
 * @author Toshio HIRAI <toshio.hirai@gmail.com>
 */
public class RequestImpl implements Request {
	private RequestContext ctx;

	/**
	 * Constructor.
	 *
	 * @param ctx Request Context
	 * @throws PowerBaseException exception
	 */
	public RequestImpl(RequestContext ctx) throws PowerBaseException {
		this.ctx = ctx;
	}

	@Override
	public Command getCommand() {
		return ctx.getCommand();
	}

	@Override
	public Client getClient() {
		return ctx.getClient();
	}

	@Override
	public Directory getDirectory() {
		return ctx.getDirectory();
	}

	@Override
	public Database getDatabase() {
		return ctx.getDatabase();
	}

	@Override
	public User getUser() {
		return ctx.getUser();
	}

	@Override
	public XQueryContext getXqueryContext() {
		return ctx.getXqueryContext();
	}

	@Override
	public String getDirectoryName() {
		return ctx.getDirectoryName();
	}

	@Override
	public String getDatabaseName() {
		return ctx.getDatabaseName();
	}

	@Override
	public String getNodeID() {
		return ctx.getNodeID();
	}

	@Override
	public String getXPath() {
		return ctx.getXPath();
	}

	@Override
	public Document getRequestBody() {
		return ctx.getRequestBody();
	}

	@Override
	public String getBaseURI() {
		return ctx.getBaseURI();
	}

	@Override
	public String getQueryWhere() {
		return ctx.getQueryWhere();
	}

	@Override
	public String getQueryOrder() {
		return ctx.getQueryOrder();
	}

	@Override
	public String getQuerySelect() {
		return ctx.getQuerySelect();
	}

	@Override
	public String getRootTag() {
		return ctx.getRootTag();
	}

	@Override
	public boolean isWrapingResponse() {
		return ctx.isWrapingResponse();
	}

	@Override
	public String getUserId() {
		return ctx.getUserId();
	}

	@Override
	public String getGroupId() {
		return ctx.getGroupId();
	}

	@Override
	public List<String> getFTQuery() {
		return ctx.getFTQuery();
	}

	@Override
	public Operator getFTLop() {
		return ctx.getFTLop();
	}

	@Override
	public Path getPath() {
		return ctx.getPath();
	}

	@Override
	public UploadFile getUploadFile() {
		return ctx.getUploadFile();
	}

	@Override
	public Map<String, String> getFileMeta() {
		return ctx.getFileMeta();
	}

	@Override
	public String getRevision() {
		return ctx.getRevision();
	}

}
