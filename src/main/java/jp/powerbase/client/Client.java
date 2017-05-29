/*
 * @(#)$Id: Client.java 1119 2011-06-10 09:15:58Z hirai $
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
package jp.powerbase.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;

import jp.powerbase.constant.HttpRequest.Method;

public class Client {
	private static final int DEFAULT_TIMEOUT = 30;

	private User user;
	private Method method;
	private int timeout;

	private HttpClient client;
	private HttpMethod request;
	private HashMap<String, String> parameters = null;

	public Client(User user, Method method, URL url) throws MalformedURLException, NoSuchAlgorithmException, UnsupportedEncodingException {
		this(user, method, url.toString(), DEFAULT_TIMEOUT);
	}

	public Client(User user, Method method, URL url, int timeout) throws MalformedURLException, NoSuchAlgorithmException, UnsupportedEncodingException {
		this(user, method, url.toString(), timeout);
	}

	public Client(User user, Method method, String urlString) throws MalformedURLException, NoSuchAlgorithmException, UnsupportedEncodingException {
		this(user, method, urlString, DEFAULT_TIMEOUT);
	}

	public Client(User user, Method method, String urlString, int timeout) throws MalformedURLException, NoSuchAlgorithmException, UnsupportedEncodingException {
		this.timeout = timeout;

		URL u = new URL(urlString);
		String path = u.getPath();
		StringBuffer encoded = new StringBuffer();
		String[] paths = path.split("/");
		for (int i = 1; i < paths.length; i++) {
			encoded.append("/");
			encoded.append(URLEncoder.encode(paths[i], "UTF-8"));
		}

		String scheme = u.getProtocol();
		String server = u.getHost();
		String port = Integer.toString(u.getPort());
		String query = u.getQuery();
		String requestURL = scheme + "://" + server + ":" + port + "" + encoded.toString() + "?" + query;

		this.user = user;
		this.method = method;

		client = new HttpClient(new MultiThreadedHttpConnectionManager());
		client.getHttpConnectionManager().getParams().setConnectionTimeout(this.timeout);
		switch (method) {
		case GET:
			request = new GetMethod(requestURL);
			break;

		case POST:
			request = new PostMethod(requestURL);
			break;

		case PUT:
			request = new PutMethod(requestURL);
			break;

		case DELETE:
			request = new DeleteMethod(requestURL);
			break;
		}
		request.addRequestHeader("X-WSSE", this.user.getAuthenticationHeader());
	}

	public void addRequestHeader(String key, String value) {
		request.addRequestHeader(key, value);
	}

	public int request() throws HttpException, IOException {
		request.addRequestHeader("Content-Type", "application/xml");
		// addParameters((PostMethod)request);

		return client.executeMethod(request);
	}

	public int request(Part[] parts) throws HttpException, IOException {
		((PostMethod) request).setRequestEntity(new MultipartRequestEntity(parts, ((PostMethod) request).getParams()));
		// request.setRequestHeader("Content-Type", "multipart/form-data");
		// addParameters((PostMethod)request);
		return client.executeMethod(request);
	}

	public int request(RequestEntity entity) throws HttpException, IOException {
		switch (method) {
		case POST:
			if (parameters == null) {
				request.addRequestHeader("Content-Type", "application/xml");
				((PostMethod) request).setRequestEntity(entity);
			} else {
				request.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
				addParameters((PostMethod) request);
			}

			break;
		case PUT:
			request.addRequestHeader("Content-Type", "application/xml");
			((PutMethod) request).setRequestEntity(entity);
			break;
		default:
			throw new HttpException("unmatch request method.");
		}
		return client.executeMethod(request);
	}

	public InputStream getResponse() throws IOException {
		return request.getResponseBodyAsStream();
	}

	private void addParameters(PostMethod request) {
		if (parameters != null) {
			for (Iterator<Map.Entry<String, String>> params = parameters.entrySet().iterator(); params.hasNext();) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) params.next();
				String key = entry.getKey();
				String value = entry.getValue();
				request.addParameter(key, value);
			}
		}
	}

	@Deprecated
	public String getResponseAsString() throws IOException {
		return request.getResponseBodyAsString();
	}

	/**
	 * parameters
	 *
	 * @param parameters
	 *            parameters
	 */
	public void setParameters(HashMap<String, String> parameters) {
		this.parameters = parameters;
	}

}
