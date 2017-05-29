/*
 * @(#)$Id: CreateDB.java 1176 2011-07-22 08:20:05Z hirai $
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
package jp.powerbase.console.cmd;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.xml.sax.SAXException;

import jp.powerbase.Command;
import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.User;
import jp.powerbase.basex.Client;
import jp.powerbase.basex.LocalClient;
import jp.powerbase.console.AdminConsole;
import jp.powerbase.console.ConsoleProcessor;
import jp.powerbase.console.ConsoleResponse;
import jp.powerbase.request.context.ConsoleRequest;
import jp.powerbase.util.PrintUtils;
import jp.powerbase.util.StringUtil;

public class ChangeAdminPassword extends AdminConsole {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new ChangeAdminPassword().run(args);
		} catch (Throwable e) {
			PrintUtils.prettyPrintStackTrace(e, System.err);
			System.exit(1);
		}
	}
	protected void process(ConsoleResponse response, int rc) throws PowerBaseException, IOException, XMLStreamException, ParserConfigurationException, SAXException {
		Client client = new LocalClient(response.getPrinter());
		User user = confirm(client);

		cmd = Command.CHANGE_ADMIN_PASSWORD.getValue();

		if (StringUtil.isEmpty(newPassword)) {
			newPassword = getReply("User Password", true);
		}
		ConsoleRequest request = new ConsoleRequest(this, user);
		request.parse(client);
		if (request.getCommand() != null) {
			ConsoleProcessor processor = new ConsoleProcessor(request, response);
			rc = processor.execute();
			response.setStatus(rc);
		} else {
			throw new PowerBaseException(PowerBaseError.Code.INVALID_ARGUMENT);
		}
		client.close();
	}

}
