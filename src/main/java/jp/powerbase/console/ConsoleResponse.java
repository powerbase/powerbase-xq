/*
 * @(#)$Id: ConsoleResponse.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.console;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Response;
import jp.powerbase.response.ResponseTag;

public class ConsoleResponse implements Response {
	public static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";

	private OutputStream writer;
	private ResponseTag responseTag = null;
	private int status;
	private URI baseURI = null;

	public ConsoleResponse() {
		writer = System.out;
	}

	@Override
	public OutputStream getPrinter() {
		return writer;
	}

	@Override
	public void write(String s) throws PowerBaseException {
		try {
			writer.write(s.getBytes(DEFAULT_CHARACTER_ENCODING));
		} catch (UnsupportedEncodingException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (IOException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		}

	}

	@Override
	public void close() throws PowerBaseException {
		try {
			writer.close();
		} catch (IOException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		}

	}

	@Override
	public void flush() throws PowerBaseException {
		try {
			writer.flush();
		} catch (IOException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		}

	}

	@Override
	public void setStatus(int code) {
		status = code;
	}

	@Override
	public ResponseTag getResponseTag() {
		return responseTag;
	}

	@Override
	public void setResponseTag(ResponseTag responseTag) {
		this.responseTag = responseTag;
	}

	public int getStatus() {
		return status;
	}

	@Override
	public URI getBaseURI() {
		return baseURI;
	}

	@Override
	public void setBaseURI(URI baseURI) {
		this.baseURI = baseURI;

	}

	@Override
	public void setContentType(String contextType) {

	}

}
