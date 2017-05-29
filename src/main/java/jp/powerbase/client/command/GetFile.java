/*
 * @(#)$Id: GetFile.java 1119 2011-06-10 09:15:58Z hirai $
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
import java.security.NoSuchAlgorithmException;

import org.apache.commons.httpclient.HttpException;

import jp.powerbase.client.Client;
import jp.powerbase.client.CommandInvoker;
import jp.powerbase.client.Connector;
import jp.powerbase.constant.HttpRequest.Method;
import jp.powerbase.xmldb.resource.Path;

public class GetFile extends Invoker implements CommandInvoker {
	public GetFile(Connector connector, Path database, int rev) throws MalformedURLException, NoSuchAlgorithmException, UnsupportedEncodingException {
		client = new Client(connector.getUser(), Method.GET, connector.getServer().addPath(database).toString() + "?rev=" + Integer.toString(rev));
		client.addRequestHeader("accept", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		client.addRequestHeader("accept", "application/xml");
	}

	public int request() throws HttpException, IOException {
		return client.request();
	}

}
