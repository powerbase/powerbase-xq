/*
 * @(#)$Id: ServletRequestContext.java 1087 2011-05-25 05:28:29Z hirai $
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
package jp.powerbase.debug;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import jp.powerbase.util.Log;

public class ServletRequestContext {
	public static final void print(HttpServletRequest req) {
		Log.debug("AuthType: " + req.getAuthType());
		Log.debug("ContextPath: " + req.getContextPath());
		// getCookies()
		// getDateHeader(java.lang.String name)
		// getHeader(java.lang.String name)
		// getHeaderNames()
		// getHeaders(java.lang.String name)

		Enumeration<?> headers = req.getHeaderNames();
		while (headers.hasMoreElements()) {
			String name = (String) (headers.nextElement());
			Enumeration<?> vals = req.getHeaders(name);
			while (vals.hasMoreElements()) {
				Log.debug("<Http Header>" + name + ": " + (String) vals.nextElement());
			}
		}

		// getIntHeader(java.lang.String name)
		Log.debug("Method: " + req.getMethod());
		Log.debug("PathInfo: " + req.getPathInfo());
		Log.debug("PathTranslated: " + req.getPathTranslated());
		Log.debug("QueryString: " + req.getQueryString());

		Enumeration<?> params = req.getParameterNames();
		while (params.hasMoreElements()) {
			String name = (String) (params.nextElement());
			String[] ary = req.getParameterValues(name);
			for (int i = 0; i < ary.length; i++) {
				Log.debug("<Parameters>" + name + ": " + (String) ary[i]);
			}
		}

		Log.debug("RemoteUser: " + req.getRemoteUser());
		Log.debug("RequestedSessionId: " + req.getRequestedSessionId());
		Log.debug("RequestURI: " + req.getRequestURI());
		Log.debug("RequestURL: " + req.getRequestURL());
		Log.debug("ServletPath: " + req.getServletPath());

		// getSession()
		// getSession(boolean create)
		// getUserPrincipal()
		// isRequestedSessionIdFromCookie()
		// isRequestedSessionIdFromUrl()
		// isRequestedSessionIdFromURL()
		// isRequestedSessionIdValid()
		// isUserInRole(java.lang.String role)

	}
}
