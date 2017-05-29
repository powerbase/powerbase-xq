/*
 * @(#)$Id: LoadXML.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

import jp.powerbase.Command;
import jp.powerbase.client.Client;
import jp.powerbase.client.CommandInvoker;
import jp.powerbase.client.Connector;
import jp.powerbase.constant.HttpRequest.Method;
import jp.powerbase.xmldb.resource.Path;

public class LoadXML extends Invoker implements CommandInvoker {
	private final Command command = Command.LOAD_XML;

	private Part[] parts;

	public LoadXML(Connector connector, Path database, File file) throws MalformedURLException, NoSuchAlgorithmException, UnsupportedEncodingException, FileNotFoundException {
		parts = new Part[] { new FilePart(file.getName(), file), new StringPart("cmd", command.value) };
		client = new Client(connector.getUser(), Method.POST, connector.getServer().addPath(database).getUrl());
	}

	public int request() throws HttpException, IOException {
		return client.request(parts);
	}

}
