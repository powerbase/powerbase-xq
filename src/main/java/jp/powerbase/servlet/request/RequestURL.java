/*
 * @(#)$Id: RequestURL.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.xmldb.resource.Path;

public class RequestURL {

	private String pathInfo;
	private URL url;

	private Path path;

	public RequestURL(HttpServletRequest request) throws PowerBaseException {
		try {
			this.url = new URL(request.getRequestURL().toString());
		} catch (MalformedURLException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		}
//TODO review workaround code.
//		this.pathInfo = request.getPathInfo();
		String contextPath = request.getContextPath();
		this.pathInfo = this.url.getPath().substring(contextPath.length());
		if (this.pathInfo == null) {
			this.pathInfo = "";
		}

		path = new Path(this.pathInfo);
	}

	public Path getPath() {
		return path;
	}

	public String getQuery() {
		return this.url.getQuery();
	}

	public List<String> getPathList() {
		return path.getPathList();
	}

}
