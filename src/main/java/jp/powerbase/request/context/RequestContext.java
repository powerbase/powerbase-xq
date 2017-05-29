/*
 * @(#)$Id: RequestContext.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.basex.query.QueryException;
import org.w3c.dom.Document;

import jp.powerbase.Command;
import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Request;
import jp.powerbase.User;
import jp.powerbase.basex.Client;
import jp.powerbase.file.UploadFile;
import jp.powerbase.request.PathParser;
import jp.powerbase.request.XQueryContext;
import jp.powerbase.response.ResponseTag;
import jp.powerbase.util.Log;
import jp.powerbase.util.StopWatch;
import jp.powerbase.util.StringUtil;
import jp.powerbase.xmldb.DBScanner;
import jp.powerbase.xmldb.resource.DataNodeUtil;
import jp.powerbase.xmldb.resource.Database;
import jp.powerbase.xmldb.resource.DatabaseFactory;
import jp.powerbase.xmldb.resource.Directory;
import jp.powerbase.xmldb.resource.Path;
import jp.powerbase.xquery.Builder;
import jp.powerbase.xquery.Director;
import jp.powerbase.xquery.expr.Brace;
import jp.powerbase.xquery.expr.DeclareNameSpaces;
import jp.powerbase.xquery.expr.Logical;
import jp.powerbase.xquery.ft.FTQueryBuilder;
import jp.powerbase.xquery.update.DeleteNode;
import jp.powerbase.xquery.update.InsertNode;
import jp.powerbase.xquery.update.ReplaceNode;
import jp.powerbase.xquery.update.ReplaceValueOfNode;
import jp.powerbase.xquery.update.XQueryUpdateBuilder;

/**
 * Request context parser.
 *
 * @author Toshio HIRAI <toshio.hirai@gmail.com>
 */
public abstract class RequestContext implements Request {
	/**
	 * BaseX client
	 */
	Client client;
	/**
	 * PowerBase user
	 */
	User user;
	/**
	 * Command
	 */
	Command command = null;
	/**
	 * Request Path
	 */
	Path path = null;
	/**
	 * Request Directory
	 */
	Directory dirObject = null;
	/**
	 * Request Database
	 */
	Database dbObject = null;
	/**
	 * Directory name
	 */
	String directory = "";
	/**
	 * Database name
	 */
	String database = "";
	/**
	 * Node ID
	 */
	String nodeID = "";
	/**
	 * File revision
	 */
	String revision = "";
	/**
	 * XPath
	 */
	String xpath = "";
	/**
	 * Root element name of response body
	 */
	String rootTag = "";
	/**
	 * Wraps the response
	 */
	boolean wrapingResponse = false;
	/**
	 * Response as data index
	 */
	boolean indexViewing = false;
	/**
	 * XQuery context
	 */
	XQueryContext xqueryContext = null;
	/**
	 * Returns by range specification
	 */
	boolean rangeMode = false;
	/**
	 * Min of range
	 */
	int rangeMin = -1;
	/**
	 * Max of range
	 */
	int rangeMax = -1;
	/**
	 * Returns by data position
	 */
	int position = -1;
	/**
	 * Get database contents recursive
	 */
	boolean recursive = false;
	/**
	 * Request XQuery
	 */
	String xquery = "";
	/**
	 * When deleting it, the user is specified.
	 */
	String userId = "";
	/**
	 * When deleting it, the group is specified.
	 */
	String groupId = "";
	/**
	 * Full Text Search keywords
	 */
	List<String> ftQuery = new ArrayList<String>();
	/**
	 * Full Text Search operator
	 */
	Logical.Operator ftLop = null;

	/**
	 * Request context(when updating)
	 */
	Document requestBody = null;
	/**
	 * Uploaded file infomation
	 */
	UploadFile uploadFile = null;
	/**
	 * File meta inf.
	 */
	Map<String, String> fileMeta = null;

	abstract Path getContextPath();

	abstract String getContextRevision();

	abstract Command getContextCommand() throws PowerBaseException;

	abstract String getContextXQuery();

	abstract void override() throws PowerBaseException;

	abstract void setFtParam();

	public abstract String getQuerySelect();

	public abstract String getQueryWhere();

	public abstract String getQueryOrder();

	public abstract String getBaseURI();

	/**
	 * Parse the request context.
	 *
	 * @param client BaseX client
	 * @throws PowerBaseException exception
	 */
	public void parse(Client client) throws PowerBaseException
	{
		assert(user != null);
		this.client = client;

		if (command == Command.CREATE_DATABASE_SPACE) {
			return;
		}

		StopWatch sw = new StopWatch();

		// set request path.
		path = getContextPath();
		// parse pathinfo.
		PathParser pathParser = new PathParser(path, client);
		pathParser.parse();

		//set fields.
		directory = pathParser.getDirectory();
		database = pathParser.getDatabaseName();
		dbObject = pathParser.getDatabase();
		if (dbObject != null && !dbObject.exists()) {
			dbObject = null;
		}
		nodeID = pathParser.getNodeID();
		xpath = pathParser.getXpath();
		rangeMode = pathParser.isRangeMode();
		rangeMin = pathParser.getRangeMin();
		rangeMax = pathParser.getRangeMax();
		position = pathParser.getPosition();
		indexViewing = pathParser.isIndexViewing();
		recursive = pathParser.isRecursive();

		//Checks visibility.
		if (dirObject != null) {
			if (!user.canView(dirObject)) {
				throw new PowerBaseException(PowerBaseError.Code.INVALID_LOCATION_PATH);
			}
		}
		if (dbObject != null) {
			if (!user.canView(dbObject)) {
				throw new PowerBaseException(PowerBaseError.Code.INVALID_LOCATION_PATH);
			}
		}

		//set file revision.
		revision = getContextRevision();

		//set Full Text Search parameters.
		setFtParam();

		//fields override from context.
		override();

		command = getContextCommand();
		if (command == null) {
			throw new PowerBaseException(PowerBaseError.Code.UNKNOWN_COMMAND);
		}

		// invoke xquery builder
		if (command == Command.EXECUTE_XQUERY || command == Command.GET_BY_XQUERY) {
			xquery = getContextXQuery();
		} else if (command == Command.FULL_TEXT_SEARCH) {
			DBScanner scanner = new DBScanner(client, user, path);
			FTQueryBuilder builder = new FTQueryBuilder(scanner);
			xquery = builder.build(this);
		} else {
			buildQuery();
		}

		//Checks Xquery
		if (!xquery.equals("")) {
			Log.debug(xquery);
			XQueryContext xqct = new XQueryContext(this.xquery);

			if (this.dirObject != null && this.dbObject != null && !xqct.isUpdating() && this.command != Command.GET_BY_XQUERY && this.command != Command.EXECUTE_XQUERY && this.command != Command.GET_DUMP) {
				if (this.wrapingResponse) {
					ResponseTag rt = new ResponseTag(this.rootTag, this.dbObject.getNamespaces());
					this.xquery = rt.wrap(new Brace().wrap(this.xquery));
				}
				DeclareNameSpaces namespace = new DeclareNameSpaces(this.dbObject.getNamespaces());
				this.xquery = namespace.toString() + this.xquery;
			}

			this.xqueryContext = new XQueryContext(this.xquery);
			if (this.xqueryContext.hasSyntaxError()) {
				String message = this.xqueryContext.toString() + "\n";
				message += this.xqueryContext.getErrorMessage();
				throw new PowerBaseException(PowerBaseError.Code.XQUERY_SYNTAX_ERROR, new Exception(message));
			}

			if (this.command == Command.GET_BY_XQUERY || this.command == Command.EXECUTE_XQUERY) {
				try {
					this.xqueryContext.compile();
				} catch (QueryException e) {
					String message = this.xqueryContext.toString() + "\n";
					message += e.getMessage();
					throw new PowerBaseException(PowerBaseError.Code.XQUERY_SYNTAX_ERROR, new Exception(message));
				} catch (Exception e) {
					throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
				}
				ArrayList dbs = this.xqueryContext.getDbNames();
				// Log.debug(dbs);
				if (dbs.contains("users") || dbs.contains("databases")) {
					throw new PowerBaseException(PowerBaseError.Code.ACCESS_DENIED);
				}
				Iterator i = dbs.iterator();
				while (i.hasNext()) {
					String dbId = (String) i.next();
					String dbPath = DataNodeUtil.getLocationPath(client, Integer.valueOf(dbId));
					Database db = new DatabaseFactory(client, new Path(dbPath)).getInstance();
					if (xqueryContext.isUpdating()) {
						if (!user.canWrite(db)) {
							throw new PowerBaseException(PowerBaseError.Code.PERMISSION_DENIED);
						}
					} else {
						if (!user.canRead(db)) {
							throw new PowerBaseException(PowerBaseError.Code.ACCESS_DENIED);
						}
					}
				}
			} else {
				assert(xqueryContext != null);
				if (xqueryContext.isUpdating()) {
					if (!user.canWrite(dbObject)) {
						throw new PowerBaseException(PowerBaseError.Code.PERMISSION_DENIED);
					}
				} else {
					if (!user.canRead(dbObject)) {
						throw new PowerBaseException(PowerBaseError.Code.ACCESS_DENIED);
					}
				}
			}
		}

		Log.debug("----- request context parse. (elasped: " + sw + ") -----");
	}

	protected void buildQuery() throws PowerBaseException {
		XQueryUpdateBuilder xqu = null;

		switch (this.command) {
		case GET:
			if (dirObject == null || dbObject == null) {
				throw new PowerBaseException(PowerBaseError.Code.DATABASE_NOT_SPECIFIED);
			}

			if (dbObject.isTuple()) {
				if (rangeMode) {
					if (dbObject.getType() == Database.Type.QUERY) {
						this.xquery = build(Builder.ClassName.QueryRangeBasedOnSEQ, this);
					} else {
						if (StringUtil.isEmpty(this.getQueryWhere()) && StringUtil.isEmpty(this.getQueryOrder())) {
							this.xquery = build(Builder.ClassName.TupleSimpleRangeBasedOnSEQ, this);
						} else {
							this.xquery = build(Builder.ClassName.TupleRangeBasedOnSEQ, this);
						}
					}
				} else {
					if (position > 0) {
						if (dbObject.getType() == Database.Type.QUERY) {
							this.xquery = build(Builder.ClassName.QuerySingleBasedOnSEQ, this);
						} else {
							if (StringUtil.isEmpty(this.getQueryWhere()) && StringUtil.isEmpty(this.getQueryOrder())) {
								this.xquery = build(Builder.ClassName.TupleSimpleSingleBasedOnSEQ, this);
							} else {
								this.xquery = build(Builder.ClassName.TupleSingleBasedOnSEQ, this);
							}
						}
					} else {
						if (dbObject.getType() == Database.Type.QUERY) {
							this.xquery = build(Builder.ClassName.Query, this);
						} else {
							this.xquery = build(Builder.ClassName.Tuple, this);
						}
					}
				}
			} else {
				this.xquery = build(Builder.ClassName.Get, this);
			}
			break;

		case GET_BY_XPATH:
			if (dirObject == null || dbObject == null) {
				throw new PowerBaseException(PowerBaseError.Code.DATABASE_NOT_SPECIFIED);
			}
			StringBuffer xp = new StringBuffer();
			xp.append("doc('");
			xp.append(dbObject.getPath());
			xp.append("')");
			xp.append(this.xpath);
			this.xquery = xp.toString();
			break;

		case GET_BY_XPATH_RECURSIVE:
			if (dirObject == null || dbObject != null) {
				throw new PowerBaseException(PowerBaseError.Code.DIRECTORY_NOT_SPECIFIED);
			}
			StringBuffer xpr = new StringBuffer();
			DBScanner scanner = new DBScanner(getClient(), getUser(), new Path(directory));
			xpr.append("let $docs := (");
			ArrayList<String> d = new ArrayList<String>();
			while (scanner.hasNext()) {
				String db = scanner.next().getPath().toString();
				d.add("fn:doc('" + db + "')");
			}
			xpr.append(StringUtils.join(d.toArray(), ", "));
			xpr.append(") return $docs");
			xpr.append(this.xpath);
			ResponseTag rt = new ResponseTag(this.rootTag, scanner.getNamespaces());
			this.xquery = rt.wrap(new Brace().wrap(xpr.toString()));
			break;

		case GET_DUMP:
			if (dirObject == null || dbObject == null) {
				throw new PowerBaseException(PowerBaseError.Code.DATABASE_NOT_SPECIFIED);
			}
			if (dbObject.getType() == Database.Type.QUERY) {
				this.xquery = build(Builder.ClassName.QueryGet, this);
			} else {
				this.xquery = build(Builder.ClassName.Dump, this);
			}
			break;

		case GET_BY_ID:
			if (dirObject == null || dbObject == null) {
				throw new PowerBaseException(PowerBaseError.Code.DATABASE_NOT_SPECIFIED);
			}

			if (dbObject.getType() == Database.Type.QUERY && dbObject.isTuple()) {
				this.xquery = build(Builder.ClassName.QuerySingle, this);
			} else {
				StringBuilder q = new StringBuilder();
				q.append("db:open-id(\"");
				q.append(dbObject.getPath());
				q.append("\", ");
				q.append(this.nodeID);
				q.append(")");
				q.append(this.xpath);
				this.xquery = q.toString();

			}

			break;

		case GET_LIST:
			if (dirObject == null || dbObject == null) {
				throw new PowerBaseException(PowerBaseError.Code.DATABASE_NOT_SPECIFIED);
			}
			if (dbObject.isTuple()) {
				ArrayList headings = dbObject.getHeadings();
				if (headings != null && !headings.isEmpty()) {
					if (rangeMode) {
						if (dbObject.getType() == Database.Type.QUERY) {
							this.xquery = build(Builder.ClassName.QueryIndexRangeBasedOnSEQ, this);
						} else {
							if (StringUtil.isEmpty(this.getQueryWhere()) && StringUtil.isEmpty(this.getQueryOrder())) {
								this.xquery = build(Builder.ClassName.TupleSimpleIndexRangeBasedOnSEQ, this);
							} else {
								this.xquery = build(Builder.ClassName.TupleIndexRangeBasedOnSEQ, this);
							}
						}
					} else {
						if (position > 0) {
							if (dbObject.getType() == Database.Type.QUERY) {
								this.xquery = build(Builder.ClassName.QueryIndexBasedOnSEQ, this);
							} else {
								if (StringUtil.isEmpty(this.getQueryWhere()) && StringUtil.isEmpty(this.getQueryOrder())) {
									this.xquery = build(Builder.ClassName.TupleSimpleIndexBasedOnSEQ, this);
								} else {
									this.xquery = build(Builder.ClassName.TupleIndexBasedOnSEQ, this);
								}
							}
						} else {
							if (dbObject.getType() == Database.Type.QUERY) {
								this.xquery = build(Builder.ClassName.QueryIndex, this);
							} else {
								this.xquery = build(Builder.ClassName.TupleIndex, this);
							}
						}
					}
				} else {
					this.xquery = build(Builder.ClassName.Get, this);
				}
			} else {
				if (dbObject.getType() == Database.Type.QUERY) {
					this.xquery = build(Builder.ClassName.QueryGet, this);
				} else {
					this.xquery = build(Builder.ClassName.Get, this);
				}
			}
			break;

		case INSERT_NODE:
			xqu = new InsertNode(this);
			this.xquery = xqu.build();
			break;

		case REPLACE_NODE:
			xqu = new ReplaceNode(this);
			this.xquery = xqu.build();
			break;

		case REPLACE_VALUE_OF_NODE:
			xqu = new ReplaceValueOfNode(this);
			this.xquery = xqu.build();
			break;

		case DELETE_NODE:
			if (dirObject == null || dbObject == null) {
				throw new PowerBaseException(PowerBaseError.Code.DATABASE_NOT_SPECIFIED);
			}
			xqu = new DeleteNode(this);
			this.xquery = xqu.build();
			break;
		}
	}

	private String build(Builder.ClassName name, RequestContext ctx) throws PowerBaseException {
		Builder builder;
		Director director;

		String clazz = name.toString();
		Class[] classes = { RequestContext.class };
		Object[] param = { ctx };
		try {
			builder = (Builder) Class.forName(clazz).getConstructor(classes).newInstance(param);
		} catch (SecurityException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (NoSuchMethodException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (ClassNotFoundException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (IllegalArgumentException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (InstantiationException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (IllegalAccessException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (InvocationTargetException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		}

		director = new Director(builder);
		director.construct();
		return builder.getResult();

	}


	/**
	 * rangeMode
	 *
	 * @return rangeMode
	 */
	public boolean isRangeMode() {
		return rangeMode;
	}

	/**
	 * recursive
	 *
	 * @return recursive
	 */
	public boolean isRecursive() {
		return recursive;
	}

	/**
	 * rangeMin
	 *
	 * @return rangeMin
	 */
	public int getRangeMin() {
		return rangeMin;
	}

	/**
	 * rangeMax
	 *
	 * @return rangeMax
	 */
	public int getRangeMax() {
		return rangeMax;
	}

	/**
	 * position
	 *
	 * @return position
	 */
	public int getPosition() {
		return position;
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
	public User getUser() {
		return (user);
	}

	/**
	 * revision
	 * @return revision
	 */
	@Override
	public String getRevision() {
	    return revision;
	}

	/**
	 * rootTag
	 *
	 * @return rootTag
	 */
	public String getRootTag() {
		return rootTag;
	}

	/**
	 * wrapingResponse
	 *
	 * @return wrapingResponse
	 */
	public boolean isWrapingResponse() {
		return wrapingResponse;
	}

	/**
	 * indexViewing
	 *
	 * @return indexViewing
	 */
	public boolean isIndexViewing() {
		return indexViewing;
	}

	@Override
	public XQueryContext getXqueryContext() {
		return xqueryContext;
	}

	@Override
	public Database getDatabase() {
		return dbObject;
	}

	/**
	 * path
	 *
	 * @return path
	 */
	public Path getPath() {
		return path;
	}

	@Override
	public Directory getDirectory() {
		return dirObject;
	}

	@Override
	public Command getCommand() {
		return command;
	}

	@Override
	public Client getClient() {
		return this.client;
	}

	@Override
	public String getUserId() {
		return userId;
	}

	@Override
	public String getGroupId() {
		return groupId;
	}

	/**
	 * ftQuery
	 *
	 * @return ftQuery
	 */
	@Override
	public List<String> getFTQuery() {
		return ftQuery;
	}

	/**
	 * ftLop
	 *
	 * @return ftLop
	 */
	@Override
	public Logical.Operator getFTLop() {
		return ftLop;
	}

	/**
	 * uploadFile
	 * @return uploadFile
	 */
	@Override
	public UploadFile getUploadFile() {
	    return uploadFile;
	}

	/**
	 * fileMeta
	 * @return fileMeta
	 */
	@Override
	public Map<String,String> getFileMeta() {
	    return fileMeta;
	}

}
