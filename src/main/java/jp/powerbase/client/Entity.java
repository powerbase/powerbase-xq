/*
 * @(#)$Id: Entity.java 1087 2011-05-25 05:28:29Z hirai $
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
package jp.powerbase.client;

import java.io.StringWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class Entity {
	public static String changePassWord(String id, String passwd) throws XMLStreamException {
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		StringWriter sw = new StringWriter();
		XMLStreamWriter writer = factory.createXMLStreamWriter(sw);

		writer.writeStartElement("user");
		writer.writeAttribute("id", id);
		{
			writer.writeStartElement("password");
			writer.writeCharacters(passwd);
			writer.writeEndElement();
		}
		writer.writeEndElement();

		return sw.toString();
	}

	public static String changeOwnPassWord(String passwd) throws XMLStreamException {
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		StringWriter sw = new StringWriter();
		XMLStreamWriter writer = factory.createXMLStreamWriter(sw);

		writer.writeStartElement("password");
		writer.writeCharacters(passwd);
		writer.writeEndElement();

		return sw.toString();
	}

	public static String createUser(String id, String name, String passwd) throws XMLStreamException {
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		StringWriter sw = new StringWriter();
		XMLStreamWriter writer = factory.createXMLStreamWriter(sw);

		writer.writeStartElement("user");
		writer.writeAttribute("id", id);
		{
			writer.writeStartElement("name");
			writer.writeCharacters(name);
			writer.writeEndElement();

			writer.writeStartElement("password");
			writer.writeCharacters(passwd);
			writer.writeEndElement();
		}
		writer.writeEndElement();

		return sw.toString();

	}

	public static String createGroup(String id) throws XMLStreamException {
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		StringWriter sw = new StringWriter();
		XMLStreamWriter writer = factory.createXMLStreamWriter(sw);

		writer.writeStartElement("group");
		writer.writeAttribute("id", id);
		writer.writeEndElement();

		return sw.toString();

	}

}
