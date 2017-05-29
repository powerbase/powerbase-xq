/*
 * @(#)$Id: Request.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;

import jp.powerbase.basex.Client;
import jp.powerbase.file.UploadFile;
import jp.powerbase.request.XQueryContext;
import jp.powerbase.xmldb.resource.Database;
import jp.powerbase.xmldb.resource.Directory;
import jp.powerbase.xmldb.resource.Path;
import jp.powerbase.xquery.expr.Logical;

/**
 * PowerBase Request object.
 *
 * @author Toshio HIRAI <toshio.hirai@gmail.com>
 */
public interface Request {
	/**
	 * Command demanded by this session
	 *
	 * @return command
	 */
	public abstract Command getCommand();

	/**
	 * BaseX client.
	 *
	 * @return client
	 */
	public abstract Client getClient();

	/**
	 * Path to resource.
	 *
	 * @return path
	 */
	public abstract Path getPath();

	/**
	 * Request Directory.
	 *
	 * @return directory
	 */
	public abstract Directory getDirectory();

	/**
	 * Request Database.
	 *
	 * @return databse
	 */
	public abstract Database getDatabase();

	/**
	 * User in the session.
	 *
	 * @return user
	 */
	public abstract User getUser();

	/**
	 * Compiled XQuery.
	 *
	 * @return XQuery context
	 */
	public abstract XQueryContext getXqueryContext();

	/**
	 * Directory name.
	 *
	 * @return name
	 */
	public abstract String getDirectoryName();

	/**
	 * Database name (as resource path).
	 *
	 * @return name
	 */
	public abstract String getDatabaseName();

	/**
	 * NODE ID of xml context.
	 *
	 * @return id
	 */
	public abstract String getNodeID();

	/**
	 * File revision that tries to be taken out.
	 *
	 * @return file revision.
	 */
	public abstract String getRevision();

	/**
	 * XPath.
	 *
	 * @return xpath
	 */
	public abstract String getXPath();

	/**
	 * Request entity in update system command.
	 *
	 * @return request body.
	 */
	public abstract Document getRequestBody();

	/**
	 * Place of data source obtained from disk or resource.
	 *
	 * @return URI
	 */
	public abstract String getBaseURI();

	/**
	 * Where Clause in XQuery
	 *
	 * @return where clause
	 */
	public abstract String getQueryWhere();

	/**
	 * Order by Clause in XQuery
	 *
	 * @return order by clause
	 */
	public abstract String getQueryOrder();

	/**
	 * List of element that tries to be taken out.
	 *
	 * @return element list
	 */
	public abstract String getQuerySelect();

	/**
	 * Top-level route element name of response data.
	 *
	 * @return root element name
	 */
	public abstract String getRootTag();

	/**
	 * Whether the response data is enclosed or not?
	 *
	 * @return true when enclosing it
	 */
	public abstract boolean isWrapingResponse();

	/**
	 * User ID when deleting it.
	 *
	 * @return user id
	 */
	public abstract String getUserId();

	/**
	 * Group ID when deleting it.
	 *
	 * @return group id
	 */
	public abstract String getGroupId();

	/**
	 * Key word list for full-text search.
	 *
	 * @return list of keyword
	 */
	public abstract List<String> getFTQuery();

	/**
	 * Logical operator when two or more key words are treated(default ftand).
	 *
	 * @return ftand or ftor
	 */
	public abstract Logical.Operator getFTLop();

	/**
	 * Information on up-loaded file.
	 *
	 * @return file infomation
	 */
	public abstract UploadFile getUploadFile();

	/**
	 * Meta information attached to file.
	 *
	 * @return mata-inf map.
	 */
	public abstract Map<String, String> getFileMeta();
}
