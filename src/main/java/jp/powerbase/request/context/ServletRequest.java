/*
 * @(#)$Id: ServletRequest.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import jp.powerbase.Command;
import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Settings;
import jp.powerbase.User;
import jp.powerbase.servlet.PowerBase;
import jp.powerbase.servlet.request.CommandDispatcher;
import jp.powerbase.servlet.request.RequestURL;
import jp.powerbase.util.ContentType;
import jp.powerbase.util.Log;
import jp.powerbase.util.StringUtil;
import jp.powerbase.xml.DOM;
import jp.powerbase.xmldb.resource.Database.Type;
import jp.powerbase.xmldb.resource.DefaultDirectory;
import jp.powerbase.xmldb.resource.Path;
import jp.powerbase.xquery.expr.Logical.Operator;
import jp.powerbase.constant.HttpRequest;
import jp.powerbase.debug.ServletRequestContext;
import jp.powerbase.file.UploadFile;

public class ServletRequest extends RequestContext {
	private static final String DEFAULT_CHARACTOR_ENCODING = "UTF-8";

	private HttpRequest.Method method;
	private HttpServletRequest req;
	private RequestURL url;

	private final Map<String, String> httpRequestParams = new HashMap<String, String>();
	private final Map<String, String> httpHeaders = new HashMap<String, String>();

	public ServletRequest(HttpRequest.Method method, HttpServletRequest req, User user) throws PowerBaseException {
		this.method = method;
		this.req = req;
		this.user = user;
		url = new RequestURL(this.req);
		try {
			this.req.setCharacterEncoding(DEFAULT_CHARACTOR_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		}
		if (PowerBase.DEBUG) {
			ServletRequestContext.print(this.req);
		}
		// Request Header enumeration
		Enumeration enumHead = this.req.getHeaderNames();
		while (enumHead.hasMoreElements()) {
			String name = (String) (enumHead.nextElement());
			Enumeration<?> vals = this.req.getHeaders(name);
			while (vals.hasMoreElements()) {
				httpHeaders.put(name, (String) vals.nextElement());
			}
		}
		// URL Parameter enumeration
		Enumeration enumParm = this.req.getParameterNames();
		while (enumParm.hasMoreElements()) {
			String name = (String) (enumParm.nextElement());
			String[] ary = this.req.getParameterValues(name);
			for (int i = 0; i < ary.length; i++) {
				httpRequestParams.put(name, (String) ary[i]);
			}
		}
		command = Command.getCommand(this.httpRequestParams.get(Parameters.QUERY_NAME_COMMAND.toString()));
	}

	@Override
	final Path getContextPath() {
		return url.getPath();
	}

	@Override
	final String getContextRevision() {
		return getHttpRequestParameter(Parameters.QUERY_NAME_REVISION);
	}

	@Override
	final void setFtParam() {
		String ftAnd = this.httpRequestParams.get(Parameters.QUERY_FULL_TEXT_AND.toString());
		String ftOr = this.httpRequestParams.get(Parameters.QUERY_FULL_TEXT_OR.toString());

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

	@Override
	final Command getContextCommand() throws PowerBaseException {
		// command assign
		if (command == null) {
			CommandDispatcher dispatcer = new CommandDispatcher(method, this);
			command = dispatcer.getCommand();
		}

		if (command.getMethod() != this.method) {
			throw new PowerBaseException(PowerBaseError.Code.COMMAND_IS_UNMATCH_TO_METHOD);
		}

		return command;
	}

	@Override
	final String getContextXQuery() {
		return getHttpRequestParameter(Parameters.QUERY_NAME_XQUERY);
	}

	@Override
	public final String getQuerySelect() {
		return this.httpRequestParams.get(Parameters.QUERY_NAME_SELECT.toString());
	}

	@Override
	public final String getQueryWhere() {
		return this.httpRequestParams.get(Parameters.QUERY_NAME_WHERE.toString());
	}

	@Override
	public final String getQueryOrder() {
		return this.httpRequestParams.get(Parameters.QUERY_NAME_ORDER.toString());
	}

	@Override
	public final String getUserId() {
		return this.httpRequestParams.get(Parameters.QUERY_NAME_USER_ID.toString());
	}

	@Override
	public String getGroupId() {
		return this.httpRequestParams.get(Parameters.QUERY_NAME_GROUP_ID.toString());
	}

	@Override
	public String getBaseURI() {
		String scheme = req.getScheme();
		String server = req.getServerName();
		int port = req.getServerPort();
		String path = req.getContextPath() + req.getServletPath();
		String baseurl = scheme + "://" + server + ":" + port + "" + path + "";
		return baseurl;
	}

	@Override
	final void override() throws PowerBaseException {
		dirObject = null;
		if (!directory.equals("")) {
			this.dirObject = new DefaultDirectory(this.client, new Path(directory));
			if (!dirObject.exists()) {
				throw new PowerBaseException(PowerBaseError.Code.INVALID_LOCATION_PATH);
			}
		} else {
			// root access
			dirObject = new DefaultDirectory(this.client, new Path("/"));
		}

		if (position <= 0) {
			if (getHttpRequestParameter(Parameters.QUERY_NAME_TUPLE_ID) != null) {
				nodeID = this.getHttpRequestParameter(Parameters.QUERY_NAME_TUPLE_ID);
			}
		}

		if (!indexViewing) {
			if (getHttpRequestParameter(Parameters.QUERY_NAME_XPATH) != null) {
				xpath = this.getHttpRequestParameter(Parameters.QUERY_NAME_XPATH);
			}
		} else {
			xpath = "";
			nodeID = "";
		}

		String tmpRoot = this.getHttpRequestParameter(Parameters.QUERY_NAME_RESPONSE_ROOT_TAG);
		if (tmpRoot != null) {
			this.rootTag = tmpRoot;
			if (this.rootTag.equals("")) {
				this.wrapingResponse = false;

			} else {
				this.wrapingResponse = true;
			}
		} else {
			this.wrapingResponse = Settings.get(Settings.Symbol.WRAPPING_RESPONSE).equalsIgnoreCase("true");
			if (this.wrapingResponse) {
				this.rootTag = Settings.get(Settings.Symbol.WRAPPING_TAG);
			} else {
				this.rootTag = "";
			}
		}

		switch (method) {
		case GET:
		case DELETE:
			break;

		case POST:
		case PUT:
			if (ServletFileUpload.isMultipartContent(this.req)) {
				try {
					Random rnd = new Random();
					int r = rnd.nextInt(Integer.MAX_VALUE);
					String tmp = System.getProperty("java.io.tmpdir");
					HttpSession session = this.req.getSession();
					DiskFileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					System.out.println(this.req.getCharacterEncoding());
					upload.setHeaderEncoding(this.req.getCharacterEncoding());
					List items = upload.parseRequest(this.req);

					if (this.dbObject.getType() == Type.FILE) {
						this.command = Command.CREATE_OR_UPDATE_FILE;
						fileMeta = new HashMap<String, String>();

						String fileName = (tmp + session.getId() + "_" + Integer.toString(r)).replace('\\', '/');
						for (int i = 0; i < items.size(); i++) {
							FileItem item = (FileItem) items.get(i);
							if (!(item.isFormField())) {
								assert (uploadFile == null);
								File f = new File(fileName);
								item.write(f);
								String orgFileName = URLDecoder.decode(item.getName(), "UTF-8");
								uploadFile = new UploadFile(orgFileName, fileName, ContentType.get(orgFileName), f.length());
							} else {
								if (this.command == Command.CREATE_OR_UPDATE_FILE) {
									fileMeta.put(URLDecoder.decode(item.getFieldName(), "UTF-8"), URLDecoder.decode(new String(item.get(), "UTF-8"), "UTF-8"));
								}
							}
						}
					} else {
						this.command = Command.LOAD_XML;
						String upFileName = (tmp + session.getId() + "_" + Integer.toString(r)) + ".xml".replace('\\', '/');
						for (int i = 0; i < items.size(); i++) {
							FileItem item = (FileItem) items.get(i);
							if (!(item.isFormField())) {
								// System.out.println(ContentType.get(item.getName()));
								// System.out.println(item.getFieldName());
								item.write(new File(upFileName));
							} else {
								if (item.getFieldName().equals("cmd")) {
									this.command = Command.getCommand(new String(item.get()));
								}
							}
						}

						DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
						DocumentBuilder docbuilder = null;
						docbuilder = dbfactory.newDocumentBuilder();
						requestBody = docbuilder.newDocument();

						Element root = requestBody.createElement("request");
						root.setAttribute("cmd", this.req.getParameter("cmd"));

						Element fnm = requestBody.createElement("file");
						fnm.appendChild(requestBody.createTextNode(upFileName));
						root.appendChild(fnm);

						requestBody.appendChild(root);

					}
				} catch (DOMException e) {
					throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
				} catch (FileUploadException e) {
					throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
				} catch (ParserConfigurationException e) {
					throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
				} catch (Exception e) {
					throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
				}

			} else {
				try {
					InputStream in = this.req.getInputStream();
					BufferedReader r = new BufferedReader(new InputStreamReader(in, "utf-8"));
					String line = null;
					StringBuffer res = new StringBuffer();
					while ((line = r.readLine()) != null) {
						res.append(line);
						res.append("\n");
					}
					DOM dom = null;
					Log.debug("request body:" + res.toString());

					if (!res.toString().equals("")) {
						try {
							dom = new DOM(res.toString());
						} catch (ParserConfigurationException e) {
							throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
						} catch (SAXException e) {
							throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
						}
						requestBody = dom.get();
					}
				} catch (IOException e) {
					throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
				}
			}
			break;

		default:
			break;
		}

	}

	public String getHttpRequestParameter(Parameters key) {
		return ((String) this.httpRequestParams.get(key.toString()));
	}

	public String getRequestHeader(String key) {
		return ((String) this.httpHeaders.get(key));
	}

	public HttpServletRequest getRequest() {
		return req;
	}

	public HttpRequest.Method getMethod() {
		return method;
	}

	public static enum Parameters {
		QUERY_NAME_COMMAND("cmd"),
		QUERY_NAME_TUPLE_ID("id"),
		QUERY_NAME_REVISION("rev"),
		QUERY_NAME_XPATH("p"),
		QUERY_NAME_XQUERY("q"),
		QUERY_NAME_SELECT("s"),
		QUERY_NAME_ORDER("o"),
		QUERY_NAME_WHERE("w"),
		QUERY_NAME_RESPONSE_ROOT_TAG("r"),
		QUERY_NAME_USER_ID("uid"),
		QUERY_NAME_GROUP_ID("gid"),
		QUERY_FULL_TEXT_AND("ftand"),
		QUERY_FULL_TEXT_OR("ftor"), ;

		private final String value;

		Parameters(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}
}
