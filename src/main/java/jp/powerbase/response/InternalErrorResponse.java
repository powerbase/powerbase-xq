/*
 * @(#)$Id: InternalErrorResponse.java 1178 2011-07-22 10:16:56Z hirai $
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
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jp.powerbase.PowerBaseError;
import jp.powerbase.Response;
import jp.powerbase.constant.PowerBaseAttribute;
import jp.powerbase.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class InternalErrorResponse extends ErrorResponse {
	public InternalErrorResponse(Response res, Throwable e) {
		super(res, e);
	}

	/*
	 * (非 Javadoc)
	 *
	 * @see jp.infinite.xmldb.Response#write()
	 */
	public String write() {
		Throwable e = super.e;

		@SuppressWarnings("unused")
		String errorCodeValue = PowerBaseError.Code.INTERNAL_PROCESS_ERROR.toString();
		String msg = PowerBaseError.getMessage(PowerBaseError.Code.INTERNAL_PROCESS_ERROR);

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

		Element jmessage = doc.createElement(PowerBaseAttribute.MESSAGE);
		jmessage.appendChild(doc.createTextNode(msg));
		err.appendChild(jmessage);

		Log.error(printStackTrace(e));

		printError(doc, err, e);

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

}
