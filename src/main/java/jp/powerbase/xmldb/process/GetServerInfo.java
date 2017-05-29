/*
 * @(#)$Id: GetServerInfo.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.xmldb.process;

import java.io.OutputStream;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Request;
import jp.powerbase.Response;
import jp.powerbase.response.XMLResponse;
import jp.powerbase.servlet.PowerBase;
import jp.powerbase.xml.DOM;
import jp.powerbase.constant.PowerBaseAttribute;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class GetServerInfo extends AbstractProcessor {
	private XMLResponse xres;
	private OutputStream out;
	String body;

	public GetServerInfo(Request req, Response res) throws PowerBaseException {
		super(req, res);
		out = res.getPrinter();
		xres = new XMLResponse(res);
	}

	@Override
	public void process() throws PowerBaseException {
		try {
			ServletContext sc = PowerBase.context;
			Properties pps = System.getProperties();

			xres.writeOpen(out);

			DOM dom = new DOM();
			Document doc = dom.get();
			Element root = doc.createElement(PowerBaseAttribute.SERVER);

			doc.appendChild(root);

			Element platform = doc.createElement("platform");
			platform.appendChild(doc.createTextNode(pps.getProperty("os.name") + " " + pps.getProperty("os.version")));
			root.appendChild(platform);

			Element vm = doc.createElement("vm");
			StringBuilder s = new StringBuilder();
			s.append(pps.getProperty("java.runtime.name"));
			s.append(" ");
			s.append(pps.getProperty("java.runtime.version"));
			s.append("[");
			s.append(pps.getProperty("java.vm.vendor"));
			s.append(" ");
			s.append(pps.getProperty("java.vm.name"));
			s.append(" ");
			s.append(pps.getProperty("java.vm.version"));
			s.append("]");
			vm.appendChild(doc.createTextNode(s.toString()));
			root.appendChild(vm);

			Element container = doc.createElement("container");
			container.appendChild(doc.createTextNode(sc.getServerInfo()));
			root.appendChild(container);

			TransformerFactory tfactory = TransformerFactory.newInstance();
			Transformer transformer = tfactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.transform(new DOMSource(doc), new StreamResult(out));

			xres.writeClose(out);

		} catch (TransformerConfigurationException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (ParserConfigurationException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (TransformerException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		}

	}

	@Override
	public void setStatus() {
		res.setStatus(HttpServletResponse.SC_OK);
	}
}
