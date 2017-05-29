/*
 * @(#)$Id: ErrorResponse.java 1178 2011-07-22 10:16:56Z hirai $
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
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Response;
import jp.powerbase.Settings;
import jp.powerbase.constant.CommonUseNameSpaces;
import jp.powerbase.constant.HttpResponseCode;
import jp.powerbase.constant.PowerBaseAttribute;
import jp.powerbase.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class ErrorResponse extends XMLResponse {
	public ErrorResponse(Response res, Throwable e) {
		super(res, e);
	}

	/*
	 * (非 Javadoc)
	 *
	 * @see jp.infinite.xmldb.Response#write(java.io.Writer)
	 */
	public void write(OutputStream out) {
		try {
			writeOpen(out, new ResponseTag(Settings.get(Settings.Symbol.WRAPPING_TAG), CommonUseNameSpaces.getNamespaces()));
			out.write(this.write().getBytes("UTF-8"));
			writeClose(out, new ResponseTag(Settings.get(Settings.Symbol.WRAPPING_TAG), CommonUseNameSpaces.getNamespaces()));
		} catch (IOException e) {
			Log.error(printStackTrace(e));
			throw new Error(e);
		} catch (PowerBaseException e) {
			Log.error(printStackTrace(e));
			throw new Error(e);
		}
	}

	/*
	 * (非 Javadoc)
	 *
	 * @see jp.infinite.xmldb.Response#write()
	 */
	public String write() {
		Throwable e = super.e;

		int code = super.code;
		String msg = super.errorMessage;

		DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docbuilder = null;
		try {
			docbuilder = dbfactory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			Log.error(printStackTrace(e1));
			throw new Error(e1);
		}
		Document doc = docbuilder.newDocument();

		Element err = doc.createElement(PowerBaseAttribute.ERROR);
		//err.setAttribute(PowerBaseAttribute.CODE, errorCodeValue);
		doc.appendChild(err);

		Throwable cause = e.getCause();

		if (code == PowerBaseError.Code.XQUERY_SYNTAX_ERROR.hashCode()) {
			Element message = doc.createElement(PowerBaseAttribute.MESSAGE);
			message.appendChild(doc.createCDATASection(cause.getMessage()));
			err.appendChild(message);
		} else if (code == PowerBaseError.Code.BASEX_INVOCATION_FAILED.hashCode()) {
			Element message = doc.createElement(PowerBaseAttribute.MESSAGE);
			message.appendChild(doc.createTextNode(cause.getMessage()));
			err.appendChild(message);
		} else {
			Element jmessage = doc.createElement(PowerBaseAttribute.MESSAGE);
			jmessage.appendChild(doc.createTextNode(msg));
			err.appendChild(jmessage);
		}

		Log.debug(printStackTrace(e));

		if (status == HttpResponseCode.STATUS_500_INTERNAL_SERVER_ERROR) {
			printError(doc, err, e);
		}

		OutputFormat formatter = new OutputFormat();
		formatter.setPreserveSpace(true);
		formatter.setOmitXMLDeclaration(true);
		StringWriter swriter = new StringWriter();
		XMLSerializer serializer = new XMLSerializer(swriter, formatter);
		try {
			serializer.serialize(doc);
		} catch (IOException e2) {
			Log.error(printStackTrace(e2));
			throw new Error(e2);
		}
		Log.debug(swriter.toString());
		return (swriter.toString());

	}

	protected static void printError(Document doc, Element err, Throwable e) {
		Element error = doc.createElement("error");
		error.appendChild(doc.createTextNode(e.toString()));

		Element trace = doc.createElement("stacktrace");
		StackTraceElement[] stElem = e.getStackTrace();
		for (int i = 0; i < stElem.length; i++) {

			Element ste = doc.createElement("stuck");
			ste.setAttribute("level", Integer.toString(stElem.length - i));

			String tmp = "at ";
			tmp += stElem[i].getClassName() + ".";
			tmp += stElem[i].getMethodName() + "(";
			tmp += stElem[i].getFileName() + ": ";
			tmp += stElem[i].getLineNumber() + ")";
			ste.appendChild(doc.createTextNode(tmp));
			trace.appendChild(ste);
		}

		error.appendChild(trace);

		Throwable t = e.getCause();
		if (t != null) {
			Element c = doc.createElement("cause");
			c.appendChild(doc.createTextNode("caused by: " + t.toString()));

			trace = doc.createElement("stacktrace");
			stElem = t.getStackTrace();
			for (int i = 0; i < stElem.length; i++) {

				Element ste = doc.createElement("stuck");
				ste.setAttribute("level", Integer.toString(stElem.length - i));

				String tmp = "at ";
				tmp += stElem[i].getClassName() + ".";
				tmp += stElem[i].getMethodName() + "(";
				tmp += stElem[i].getFileName() + ": ";
				tmp += stElem[i].getLineNumber() + ")";
				ste.appendChild(doc.createTextNode(tmp));
				trace.appendChild(ste);
			}

			c.appendChild(trace);

			error.appendChild(c);
		}

		err.appendChild(error);

	}

	public static String printStackTrace(Throwable e) {
		StringWriter sw = new StringWriter();
		try {
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
		} catch (Exception e1) {
			e1.printStackTrace();
			new Error(e1);
		}
		return sw.toString();

	}
}
