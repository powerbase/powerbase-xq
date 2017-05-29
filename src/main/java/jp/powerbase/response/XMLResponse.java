/*
 * @(#)$Id: XMLResponse.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.response;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.w3c.dom.Document;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Response;
import jp.powerbase.Settings;
import jp.powerbase.constant.Response.Lang;
import jp.powerbase.constant.XMLDeclaration;
import jp.powerbase.xmldb.resource.NameSpace;

public class XMLResponse {

	public static final String header = XMLDeclaration.get();

	private String content = "";

	protected Lang lang;

	protected int status;

	protected int code;
	protected String errorCodeValue;
	protected Throwable e;
	protected String errorMessage;
	private ArrayList<NameSpace> nss = new ArrayList<NameSpace>();
	private Response res;

	public XMLResponse(Response res) {
		this.res = res;
	}

	public XMLResponse(Response res, Throwable e) {
		this(res);

		lang = Lang.get(Settings.get(Settings.Symbol.LANG));
		this.e = e;
		String msg = e.getMessage();

		for (PowerBaseError.Code val : PowerBaseError.Code.values()) {
			if (val.toString().equals(msg)) {
				this.code = val.hashCode();
				this.errorCodeValue = val.toString();
				this.status = val.status;
				this.errorMessage = PowerBaseError.getMessage(val);
				break;
			}
		}
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setContent(Document content) throws PowerBaseException {
		try {
			OutputFormat formatter = new OutputFormat();
			formatter.setPreserveSpace(true);
			StringWriter swriter = new StringWriter();
			XMLSerializer serializer = new XMLSerializer(swriter, formatter);
			serializer.serialize(content);
			this.content = swriter.toString();
		} catch (IOException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		}
	}

	public void addNameSpace(NameSpace ns) {
		nss.add(ns);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String write() {
		StringBuffer sb = new StringBuffer();
		sb.append(writeOpen());
		sb.append(content);
		sb.append(writeClose());
		return sb.toString();

	}

	public void write(OutputStream out) {
		try {
			out.write(this.write().getBytes("UTF-8"));
		} catch (IOException e) {
			throw new Error(e);
		}
	}

	public void write(OutputStream out, String body) {
		try {
			out.write(body.getBytes("UTF-8"));
		} catch (IOException e) {
			throw new Error(e);
		}
	}

	public String writeOpen() {
		return res.getResponseTag().getOpen();
	}

	public String writeClose() {
		return res.getResponseTag().getClose();
	}

	public String writeOpen(ResponseTag rt) {
		return rt.getOpen();
	}

	public String writeClose(ResponseTag rt) {
		return rt.getClose();
	}

	public void writeOpen(OutputStream out) throws PowerBaseException {
		writeOpen(out, null);
	}

	public void writeClose(OutputStream out) throws PowerBaseException {
		writeClose(out, null);
	}

	public void writeOpen(OutputStream out, ResponseTag rt) throws PowerBaseException {
		try {
			if (rt == null) {
				out.write(this.writeOpen().getBytes("UTF-8"));
			} else {
				out.write(this.writeOpen(rt).getBytes("UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (IOException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		}
	}

	public void writeClose(OutputStream out, ResponseTag rt) throws PowerBaseException {
		try {
			if (rt == null) {
				out.write(this.writeClose().getBytes("UTF-8"));
			} else {
				out.write(this.writeClose(rt).getBytes("UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (IOException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		}
	}
}
