/*
 * @(#)$Id: GetList.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.client.command;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.lang.StringUtils;

import jp.powerbase.Command;
import jp.powerbase.client.Client;
import jp.powerbase.client.CommandInvoker;
import jp.powerbase.client.Connector;
import jp.powerbase.constant.HttpRequest.Method;
import jp.powerbase.xmldb.resource.Path;

public class GetList extends Invoker implements CommandInvoker {
	private final Command command = Command.GET_LIST;

	public GetList(Connector connector, Path database) throws MalformedURLException, NoSuchAlgorithmException, UnsupportedEncodingException {
		this(connector, database, null);
	}
	public GetList(Connector connector, Path database, Map<String, String> params) throws MalformedURLException, NoSuchAlgorithmException, UnsupportedEncodingException {
		String query = "?cmd=" + command.value;
		if (params != null) {
			if (params.size() != 0) {
				ArrayList<String> p = new ArrayList<String>();
				for (Iterator<Map.Entry<String, String>> i = params.entrySet().iterator(); i.hasNext();) {
					Map.Entry<String, String> entry = (Map.Entry<String, String>) i.next();
					String key = entry.getKey();
					String value = URLEncoder.encode(entry.getValue(), "UTF-8");
					p.add(key + "=" + value);
				}
				query += "&" + StringUtils.join(p.toArray(), "&");
			}
		}

		client = new Client(connector.getUser(), Method.GET, connector.getServer().addPath(database).toString() + query);
	}

	public int request() throws HttpException, IOException {
		return client.request();
	}

}
