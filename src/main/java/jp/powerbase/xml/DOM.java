/*
 * @(#)$Id: DOM.java 1093 2011-05-25 06:08:28Z hirai $
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
package jp.powerbase.xml;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.*;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import jp.powerbase.util.XMLUtil;

import org.w3c.dom.*;
import org.xml.sax.*;

public class DOM {
	private Document document;

	public DOM(NodeList list) throws ParserConfigurationException {
		DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		builder = dbfactory.newDocumentBuilder();
		document = builder.newDocument();

		Node node;
		for (int iNode = 0; iNode < list.getLength(); iNode++) {
			node = list.item(iNode);
			Element el = document.createElement(node.getNodeName());
			document.appendChild(el);
			XMLUtil.scanChild(document, node, el, 0);
		}

	}

	public DOM(String xmlBody) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		builder = dbfactory.newDocumentBuilder();
		Reader reader = new StringReader(xmlBody);
		InputSource is = new InputSource(reader);
		document = builder.parse(is);
	}

	public DOM(byte[] xmlBody) throws ParserConfigurationException, SAXException, IOException {
		this(new String(xmlBody));
	}

	public DOM() throws ParserConfigurationException {
		DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docbuilder = null;
		docbuilder = dbfactory.newDocumentBuilder();
		document = docbuilder.newDocument();
	}

	public DOM(File file) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbfactory.newDocumentBuilder();
		document = builder.parse(file);
	}

	public DOM(Document DomDocument) {
		this.document = DomDocument;
	}

	public String getNodeValue(String XPath) throws XPathExpressionException {
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		XPathExpression expr = xpath.compile(XPath);
		return (String) expr.evaluate(document, XPathConstants.STRING);
	}

	public Document get() {
		return document;
	}

	public NodeList evalXpath(String query) throws XPathExpressionException {
		XPath xpath = XPathFactory.newInstance().newXPath();
		NodeList list = (NodeList) xpath.evaluate(query, document, XPathConstants.NODESET);
		return (list);
	}

	public String getXML(boolean header) throws IOException, TransformerException {
		return XMLUtil.documentToString(document, header);
	}

	public String toString() {
		try {
			return this.getXML();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	public String getXML() throws IOException {
		return XMLUtil.documentToString(document);
	}

}
