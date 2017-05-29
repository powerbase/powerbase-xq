/*
 * @(#)$Id: XMLUtil.java 1093 2011-05-25 06:08:28Z hirai $
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
package jp.powerbase.util;

import java.io.IOException;
import java.io.StringWriter;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public final class XMLUtil {
	private XMLUtil() {
	}

	public static String escapeXML(String str) {
		if (str == null || str.length() == 0) {
			return str;
		}
		final StringBuilder buf = new StringBuilder(255);
		final int len = str.length();
		for (int i = 0; i < len; ++i) {
			final char c = str.charAt(i);
			switch (c) {
			case '&':
				buf.append("&amp;");
				break;
			case '<':
				buf.append("&lt;");
				break;
			case '>':
				buf.append("&gt;");
				break;
			case '"':
				buf.append("&quot;");
				break;
			case '\'':
				buf.append("&apos;");
				break;
			// case '\r':
			// buf.append("&#xD;");
			default:
				buf.append(c);
				break;
			}
		}
		return buf.toString();
	}

	public static String documentToString(Document doc) throws IOException {
		return documentToString(doc, true);
	}

	public static String documentToString(Document doc, boolean header) throws IOException {
		OutputFormat formatter = new OutputFormat(doc);
		formatter.setPreserveSpace(true);
		// formatter.setIndenting(true);
		// formatter.setLineSeparator("\n");
		formatter.setOmitXMLDeclaration(!header);
		StringWriter swriter = new StringWriter();
		XMLSerializer serializer = new XMLSerializer(swriter, formatter);
		serializer.serialize(doc);
		return (swriter.toString());

	}

	public static void traceList(Document doc, Element root, NodeList list) {
		Node node;
		for (int iNode = 0; iNode < list.getLength(); iNode++) {
			node = list.item(iNode);
			Element el = doc.createElement(node.getNodeName());
			XMLUtil.scanChild(doc, node, el, 0);
			root.appendChild(el);
		}

	}

	public static void scanChild(Document doc, Node n, Element el, int level) {
		if (n.hasAttributes()) {
			NamedNodeMap map = n.getAttributes();
			for (int i = 0; i < map.getLength(); i++) {
				Node an = map.item(i);
				if (an.getNodeType() == Node.ATTRIBUTE_NODE) {
					Attr a = (Attr) an;
					el.setAttribute(a.getName(), a.getValue());
				}
			}
		}

		for (Node ch = n.getFirstChild(); ch != null; ch = ch.getNextSibling()) {
			switch (ch.getNodeType()) {
			case Node.ATTRIBUTE_NODE:
				break;
			case Node.CDATA_SECTION_NODE:
				el.appendChild(doc.createCDATASection(ch.getTextContent()));
				break;
			case Node.COMMENT_NODE:
				el.appendChild(doc.createComment(ch.getTextContent()));
				break;
			case Node.DOCUMENT_FRAGMENT_NODE:
				break;
			case Node.DOCUMENT_NODE:
				break;
			case Node.DOCUMENT_POSITION_CONTAINED_BY:
				break;
			case Node.DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC:
				break;
			case Node.DOCUMENT_TYPE_NODE:
				break;
			case Node.ELEMENT_NODE:
				Element cel = doc.createElement(ch.getNodeName());
				scanChild(doc, ch, cel, level + 1);
				el.appendChild(cel);
				break;
			case Node.ENTITY_NODE:
			case Node.ENTITY_REFERENCE_NODE:
			case Node.NOTATION_NODE:
			case Node.PROCESSING_INSTRUCTION_NODE:
				break;
			case Node.TEXT_NODE:
				el.appendChild(doc.createTextNode(ch.getTextContent()));
				break;
			default:
				break;
			}

		}
		return;
	}
}
