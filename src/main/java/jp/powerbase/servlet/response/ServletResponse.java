/*
 * @(#)$Id: ServletResponse.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.servlet.response;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Response;
import jp.powerbase.Settings;
import jp.powerbase.constant.CommonUseNameSpaces;
import jp.powerbase.response.ResponseTag;
import jp.powerbase.servlet.request.Accept;

public class ServletResponse implements Response {
	public static final String DEFAULT_CONTENT_TYPE = "text/xml";
	public static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";

	private HttpServletResponse res = null;
	private OutputStream writer;
	private ResponseTag responseTag;
	private URI baseURI;

	public ServletResponse(HttpServletRequest req, HttpServletResponse res) throws PowerBaseException {
		this.res = res;
		this.res.setCharacterEncoding(DEFAULT_CHARACTER_ENCODING);
		setContentType(DEFAULT_CONTENT_TYPE);
		try {
			Accept accept = new Accept(req);
			if (accept.isBinary()) {
				writer = new BufferedOutputStream(res.getOutputStream());
			} else {
				writer = new BufferedOutputStream(new PipedOutputStream(res.getWriter()));
			}
			responseTag = new ResponseTag(Settings.get(Settings.Symbol.WRAPPING_TAG), CommonUseNameSpaces.getNamespaces());
		} catch (IOException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		}
	}

	@Override
	public void setStatus(int code) {
		res.setStatus(code);
	}

	@Override
	public OutputStream getPrinter() {
		return writer;
	}

	public void close() throws PowerBaseException {
		try {
			writer.close();
		} catch (IOException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		}

	}

	public void flush() throws PowerBaseException {
		try {
			writer.flush();
		} catch (IOException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		}

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

	/**
	 * responseTag
	 *
	 * @return responseTag
	 */
	public ResponseTag getResponseTag() {
		return responseTag;
	}

	/**
	 * baseURI
	 *
	 * @return baseURI
	 */
	public URI getBaseURI() {
		return baseURI;
	}

	/**
	 * baseURI
	 *
	 * @param baseURI
	 *            baseURI
	 */
	public void setBaseURI(URI baseURI) {
		this.baseURI = baseURI;
		responseTag.setBaseURI(baseURI);
	}

	@Override
	public void setResponseTag(ResponseTag responseTag) {
		this.responseTag = responseTag;
	}

	@Override
	public void setContentType(String contextType) {
		res.setContentType(contextType);
	}

}
