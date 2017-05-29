/*
 * @(#)$Id: ReplaceValueOfNode.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.httpclient.methods.InputStreamRequestEntity;

import jp.powerbase.Command;
import jp.powerbase.client.Client;
import jp.powerbase.client.CommandInvoker;
import jp.powerbase.client.Connector;
import jp.powerbase.client.RequestElement;
import jp.powerbase.client.expr.ReplaceValueOfNodeExpr;
import jp.powerbase.constant.HttpRequest.Method;
import jp.powerbase.xmldb.resource.Path;
import jp.powerbase.xquery.expr.Tag;

public class ReplaceValueOfNode extends Invoker implements CommandInvoker {
	private final Command command = Command.REPLACE_VALUE_OF_NODE;

	public ReplaceValueOfNode(Connector connector, List<ReplaceValueOfNodeExpr> exprs) throws MalformedURLException, NoSuchAlgorithmException, UnsupportedEncodingException, XMLStreamException {
		RequestElement rt = new RequestElement(command);
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < exprs.size(); i++) {
			s.append(exprs.get(i).toString());
		}
		client = new Client(connector.getUser(), Method.PUT, connector.getServer().getServerLocation());
		req = new InputStreamRequestEntity(new ByteArrayInputStream(rt.wrap(s.toString()).getBytes()));
	}

	public ReplaceValueOfNode(Connector connector, Path resource, String value) throws MalformedURLException, NoSuchAlgorithmException, UnsupportedEncodingException {
		RequestElement rt = new RequestElement(command);
		client = new Client(connector.getUser(), Method.PUT, connector.getServer().addPath(resource).getUrl());
		req = new InputStreamRequestEntity(new ByteArrayInputStream(rt.wrap(new Tag("with").wrap(value)).getBytes()));
	}

}
