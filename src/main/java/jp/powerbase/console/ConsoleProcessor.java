/*
 * @(#)$Id: ConsoleProcessor.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import jp.powerbase.Command;
import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.client.Entity;
import jp.powerbase.client.RequestElement;
import jp.powerbase.request.context.ConsoleRequest;
import jp.powerbase.xml.DOM;
import jp.powerbase.xmldb.CreateDatabaseSpace;
import jp.powerbase.xmldb.ProcessManager;
import jp.powerbase.xmldb.Processor;

public class ConsoleProcessor {
	private Command command;
	private ConsoleRequest request;
	private ConsoleResponse response;

	public ConsoleProcessor(ConsoleRequest request, ConsoleResponse response) {
		this.request = request;
		this.response = response;
		command = this.request.getCommand();
	}

	private void setEntity(String entity) throws ParserConfigurationException, SAXException, IOException {
		RequestElement re = new RequestElement(command);
		Document doc = new DOM(re.wrap(entity)).get();
		request.setRequestBody(doc);

	}

	public int execute() throws IOException, XMLStreamException, ParserConfigurationException, SAXException {
		try {
			switch (command) {
			case CREATE_DATABASE_SPACE:
				CreateDatabaseSpace.create(request.getClient(), new File(request.getDataDir()));
				System.out.println("Done.");
				return 0;

			case CHANGE_PASSWORD:
				setEntity(Entity.changePassWord(request.getUserId(), request.getPassword()));
				break;

			case CHANGE_OWN_PASSWORD:
			case CHANGE_ADMIN_PASSWORD:
				setEntity(Entity.changeOwnPassWord(request.getPassword()));
				break;

			case CREATE_USER:
				setEntity(Entity.createUser(request.getUserId(), request.getUserName(), request.getPassword()));
				break;

			case CREATE_GROUP:
				setEntity(Entity.createGroup(request.getGroupId()));
				break;

			default:
				break;

			}
			Processor processor = new ProcessManager(command).getProcessor(request, response);
			processor.execute();

		} catch (PowerBaseException e) {
			response.getPrinter().write(("Error: " + PowerBaseError.getMessage(e.getCode())).getBytes());
			return e.getCode().hashCode();
		}

		return 0;
	}
}
