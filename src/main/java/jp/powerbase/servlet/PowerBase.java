/*
 * @(#)$Id: PowerBase.java 1183 2011-07-27 01:36:26Z hirai $
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
package jp.powerbase.servlet;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.powerbase.BaseX;
import jp.powerbase.Command;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Request;
import jp.powerbase.Response;
import jp.powerbase.Settings;
import jp.powerbase.Version;
import jp.powerbase.basex.Client;
import jp.powerbase.constant.HttpRequest;
import jp.powerbase.constant.HttpResponseCode;
import jp.powerbase.request.RequestImpl;
import jp.powerbase.request.context.RequestContext;
import jp.powerbase.request.context.ServletRequest;
import jp.powerbase.servlet.request.Authentication;
import jp.powerbase.servlet.response.ServletResponse;
import jp.powerbase.util.Log;
import jp.powerbase.util.StopWatch;
import jp.powerbase.xmldb.Initializer;
import jp.powerbase.xmldb.ProcessManager;
import jp.powerbase.xmldb.Processor;
import jp.powerbase.response.ErrorResponse;
import jp.powerbase.response.InternalErrorResponse;

/**
 * Servlet implementation class
 *
 * @author Toshio HIRAI <toshio.hirai@gmail.com>
 *
 */
public class PowerBase extends HttpServlet {
	/**
	 * UID
	 */
	private static final long serialVersionUID = 7966448087328954469L;

	/**
	 * PowerBase Version.
	 */
	public static final Version VERSION = new Version();

	/**
	 * debug switch.
	 */
	public static final boolean DEBUG = true;

	/**
	 * system name.
	 */
	public static String SYSTEM_NAME;
	/**
	 * printable current version.
	 */
	public static final String SYSTEM_VERSION = VERSION.toString();

	/**
	 * Servlet context.
	 */
	public static ServletContext context;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PowerBase() {
		super();
	}

	@Override
	public void init() throws ServletException {
		super.init();

		context = this.getServletContext();
		SYSTEM_NAME = Settings.get(Settings.Symbol.SYSTEM_NAME);
		printServerEnvironment(context);
		try {
			Initializer.StartBaeX();
		} catch (IOException e) {
			throw new ServletException(e);
		}

		Log.info(SYSTEM_NAME + " Ver." + SYSTEM_VERSION + " system initialized.");
	}

	/**
	 * @param method
	 * @param request
	 * @param response
	 */
	private void process(HttpRequest.Method method, HttpServletRequest request, HttpServletResponse response) {
		Response res = null;
		Client client = null;
		try {
			res = new ServletResponse(request, response);
			client = new BaseX().getClient(res.getPrinter());
			Authentication auth = new Authentication(request, client);
			RequestContext ctx = new ServletRequest(method, request, auth.getUser());
			ctx.parse(client);
			Request req = new RequestImpl(ctx);
			Command command = req.getCommand();
			Processor processor = new ProcessManager(command).getProcessor(req, res);
			processor.execute();
			processor.setStatus();
			Log.debug("process complete (Command: " + command + ")");
		} catch (PowerBaseException e) {
			ErrorResponse error = new ErrorResponse(res, e);
			error.write(res.getPrinter());
			res.setStatus(error.getStatus());
			return;
		} catch (Throwable e) {
			ErrorResponse error = new InternalErrorResponse(res, e);
			error.write(res.getPrinter());
			res.setStatus(HttpResponseCode.STATUS_500_INTERNAL_SERVER_ERROR);
			return;
		} finally {
			try {
				if (client != null) {
					client.close();
				}
				if (res != null) {
					res.close();
				}
			} catch (Throwable e) {
				ErrorResponse error = new InternalErrorResponse(res, e);
				error.write(res.getPrinter());
				res.setStatus(HttpResponseCode.STATUS_500_INTERNAL_SERVER_ERROR);
				return;
			}
		}

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StopWatch sw = new StopWatch();
		Log.debug("-----------------" + SYSTEM_NAME + " Ver." + SYSTEM_VERSION + " request process start. (method=GET)-------------------");
		process(HttpRequest.Method.GET, request, response);
		Log.debug("-----------------" + SYSTEM_NAME + " Ver." + SYSTEM_VERSION + " request process end. (total elasped: " + sw + ")-------------------");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StopWatch sw = new StopWatch();
		Log.debug("-----------------" + SYSTEM_NAME + " Ver." + SYSTEM_VERSION + " request process start. (method=POST)-------------------");
		process(HttpRequest.Method.POST, request, response);
		Log.debug("-----------------" + SYSTEM_NAME + " Ver." + SYSTEM_VERSION + " request process end. (total elasped: " + sw + ")-------------------");
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StopWatch sw = new StopWatch();
		Log.debug("-----------------" + SYSTEM_NAME + " Ver." + SYSTEM_VERSION + " request process start. (method=PUT)-------------------");
		process(HttpRequest.Method.PUT, request, response);
		Log.debug("-----------------" + SYSTEM_NAME + " Ver." + SYSTEM_VERSION + " request process end. (total elasped: " + sw + ")-------------------");
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StopWatch sw = new StopWatch();
		Log.debug("-----------------" + SYSTEM_NAME + " Ver." + SYSTEM_VERSION + " request process start. (method=DELETE)-------------------");
		process(HttpRequest.Method.DELETE, request, response);
		Log.debug("-----------------" + SYSTEM_NAME + " Ver." + SYSTEM_VERSION + " request process end. (total elasped: " + sw + ")-------------------");
	}

	private static final void printServerEnvironment(ServletContext serverContext) {
		Properties sys = System.getProperties();
		Log.info("Executing Platform: " + sys.getProperty("os.name") + " " + sys.getProperty("os.version"));
		StringBuilder s = new StringBuilder();
		s.append(sys.getProperty("java.runtime.name"));
		s.append(" ");
		s.append(sys.getProperty("java.runtime.version"));
		s.append("[");
		s.append(sys.getProperty("java.vm.vendor"));
		s.append(" ");
		s.append(sys.getProperty("java.vm.name"));
		s.append(" ");
		s.append(sys.getProperty("java.vm.version"));
		s.append("]");
		Log.info("Java VM: " + s.toString());
		Log.info("Servlet Container: " + serverContext.getServerInfo());

	}

}
