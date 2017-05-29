/*
 * @(#)$Id: CreateFile.java 1118 2011-06-10 02:21:28Z hirai $
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
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

import jp.powerbase.client.Client;
import jp.powerbase.client.CommandInvoker;
import jp.powerbase.client.Connector;
import jp.powerbase.constant.HttpRequest.Method;
import jp.powerbase.xmldb.resource.Path;

public class CreateFile extends Invoker implements CommandInvoker {
	protected Part[] parts;

	public CreateFile(){}

	public CreateFile(Connector connector, Path database, File file, Map<String, String> meta) throws MalformedURLException, NoSuchAlgorithmException, UnsupportedEncodingException, FileNotFoundException {
		ArrayList<Part> p = new ArrayList<Part>();
		p.add(new FilePart("upfile", URLEncoder.encode(file.getName(), "UTF-8"), file));
		for (Iterator it = meta.entrySet().iterator(); it.hasNext();) {
		    Map.Entry entry = (Map.Entry)it.next();
			p.add(new StringPart(URLEncoder.encode((String)entry.getKey(), "UTF-8"), URLEncoder.encode((String)entry.getValue(), "UTF-8")));
		}
		parts = (Part[])p.toArray(new Part[0]);
		client = new Client(connector.getUser(), Method.POST, connector.getServer().addPath(database).getUrl());
	}

	public int request() throws HttpException, IOException {
		return client.request(parts);
	}

}
