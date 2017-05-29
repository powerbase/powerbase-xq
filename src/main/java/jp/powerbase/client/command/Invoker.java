/*
 * @(#)$Id: Invoker.java 1087 2011-05-25 05:28:29Z hirai $
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
import java.io.InputStream;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.RequestEntity;

import jp.powerbase.client.Client;
import jp.powerbase.client.CommandInvoker;

public class Invoker implements CommandInvoker {
	Client client;
	RequestEntity req;

	@Override
	public int request() throws HttpException, IOException {
		return client.request(req);
	}

	@Override
	public InputStream getResponse() throws IOException {
		return client.getResponse();
	}

	@Override
	@Deprecated
	public String getResponseAsString() throws IOException {
		return client.getResponseAsString();
	}

}
