/*
 * @(#)$Id: ExecuteXQuery.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import jp.powerbase.Command;
import jp.powerbase.client.Client;
import jp.powerbase.client.CommandInvoker;
import jp.powerbase.client.Connector;
import jp.powerbase.constant.HttpRequest.Method;

public class ExecuteXQuery extends Invoker implements CommandInvoker {
	private final Command command = Command.EXECUTE_XQUERY;

	public ExecuteXQuery(Connector connector, String xquery) throws MalformedURLException, NoSuchAlgorithmException, UnsupportedEncodingException {
		client = new Client(connector.getUser(), Method.POST, connector.getServer().getServerLocation());
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("cmd", command.value);
		parameters.put("q", xquery);
		client.setParameters(parameters);
	}
}
