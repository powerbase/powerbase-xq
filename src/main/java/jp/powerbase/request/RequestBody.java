/*
 * @(#)$Id: RequestBody.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.request;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import jp.powerbase.Command;
import jp.powerbase.util.XMLUtil;
import jp.powerbase.xml.DOM;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class RequestBody {
	private final String command;
	HashMap elements = new HashMap();

	public RequestBody(Command command) {
		super();

		String c = "";
		for (Command name : Command.values()) {
			if (name.equals(command)) {
				c = name.value;
			}
		}
		this.command = c;

	}

	@SuppressWarnings("unchecked")
	public void addElements(Object key, Object elements) {
		this.elements.put(key, elements);
	}

	public Document create() throws ParserConfigurationException, XPathExpressionException {
		DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docbuilder = null;
		docbuilder = dbfactory.newDocumentBuilder();
		Document doc = docbuilder.newDocument();

		Element root = doc.createElement("request");
		root.setAttribute("cmd", command);
		doc.appendChild(root);

		Set set = elements.keySet();
		Iterator iterator = set.iterator();
		Object key;
		while (iterator.hasNext()) {
			key = iterator.next();
			if (elements.get(key) instanceof String) {
				Element param = doc.createElement((String) key);
				param.appendChild(doc.createTextNode((String) elements.get(key)));
				root.appendChild(param);
			} else if (elements.get(key) instanceof Document) {
				DOM dom = new DOM((Document) elements.get(key));
				NodeList list = null;
				list = dom.evalXpath("/*");
				XMLUtil.traceList(doc, root, list);
			}
		}

		OutputFormat formatter = new OutputFormat();
		formatter.setPreserveSpace(true);
		StringWriter swriter = new StringWriter();
		XMLSerializer serializer = new XMLSerializer(swriter, formatter);
		try {
			serializer.serialize(doc);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return doc;
	}

}
