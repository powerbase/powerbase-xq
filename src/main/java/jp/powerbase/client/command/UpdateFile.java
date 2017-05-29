/*
 * @(#)$Id: UpdateFile.java 1118 2011-06-10 02:21:28Z hirai $
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
import java.util.Map;

import org.apache.commons.httpclient.HttpException;

import jp.powerbase.client.CommandInvoker;
import jp.powerbase.client.Connector;
import jp.powerbase.xmldb.resource.Path;

public class UpdateFile extends CreateFile implements CommandInvoker {

	public UpdateFile(Connector connector, Path resource, File file, Map<String, String> meta) throws MalformedURLException, NoSuchAlgorithmException, UnsupportedEncodingException, FileNotFoundException {
		super(connector, resource, file, meta);
	}

	public int request() throws HttpException, IOException {
		return client.request(parts);
	}

}
