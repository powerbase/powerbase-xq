/*
 * @(#)$Id: AdminConsole.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.User;
import jp.powerbase.Version;
import jp.powerbase.basex.Client;
import jp.powerbase.basex.LocalClient;
import jp.powerbase.datasource.InternalHttpServer;
import jp.powerbase.request.context.ConsoleRequest;
import jp.powerbase.user.PowerBaseUser;
import jp.powerbase.util.PrintUtils;
import jp.powerbase.util.StringUtil;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ExampleMode;
import org.kohsuke.args4j.Option;
import org.xml.sax.SAXException;

public class AdminConsole {
	public static final Version VERSION = new Version();

	@Argument(index = 0, metaVar = "res")
	public String res = "";

	@Option(name = "-cmd", usage = "Command")
	public String cmd = "";

	@Option(name = "-p", usage = "password")
	public String password = "";

	@Option(name = "-d", usage = "Database data directory")
	public String dataDir = "";

	@Option(name = "-uid", usage = "User ID")
	public String userId = "";

	@Option(name = "-uname", usage = "User Name")
	public String userName = "";

	@Option(name = "-np", usage = "User (new) Pass Word")
	public String newPassword = "";

	@Option(name = "-gid", usage = "Group ID")
	public String groupId = "";

	@Option(name = "-ftand", usage = "Full text search using AND operator")
	public String ftAnd = "";

	@Option(name = "-ftor", usage = "Full text search using OR operator")
	public String ftOr = "";

	@Option(name = "-id", usage = "node id")
	public String id = "";

	@Option(name = "-rev", usage = "file revision")
	public String rev = "";

	@Option(name = "-s", usage = "select element")
	public String select = "";

	@Option(name = "-w", usage = "where clause")
	public String where = "";

	@Option(name = "-o", usage = "order by clause")
	public String orderBy = "";

	@Option(name = "-pos", usage = "tuple sequence position")
	public String pos = "";

	@Option(name = "-from", usage = "based on tuple sequence from")
	public String from = "";

	@Option(name = "-to", usage = "based on tuple sequence to")
	public String to = "";

	@Option(name = "-xp", usage = "xpath")
	public String xpath = "";

	@Option(name = "-q", usage = "xquery")
	public String xquery = "";

	@Option(name = "-f", usage = "xquery", metaVar = "FILE")
	public String xqueryFile = "";

	@Option(name = "-req", usage = "request entity", metaVar = "FILE")
	public String entity = "";

	@Option(name = "-source", usage = "loading input source", metaVar = "FILE")
	public String source = "";

	@Option(name = "-help", usage = "Show help")
	public boolean showHelp = false;

	@Option(name = "-version", usage = "Show version")
	public boolean showVersion = false;

	private CmdLineParser parser;

	public InternalHttpServer iserver;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			if (args.length == 0) {
				new AdminConsole().run();
			} else {
				new AdminConsole().run(args);
			}

		} catch (Throwable e) {
			PrintUtils.prettyPrintStackTrace(e, System.err);
			System.exit(1);
		}
		System.exit(0);
	}

	public void run() {

	}

	public void run(String[] args) throws Exception {
		int rc = 0;
		CmdLineParser parser = new CmdLineParser(this);
		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			return;
		}
		this.parser = parser;

		ConsoleResponse response = new ConsoleResponse();

		if (showHelp) {
			showHelp();
			System.exit(rc);
		}

		if (showVersion) {
			response.getPrinter().write(("PowerBase version: " + VERSION.toString()).getBytes());
			System.exit(rc);
		}

		iserver = new InternalHttpServer(8000);
		try {
			String env = System.getenv("POWERBASE_DATA_DIR");
			if (!StringUtil.isEmpty(env)) {
				dataDir = env;
			} else {
				if (StringUtil.isEmpty(dataDir)) {
					throw new PowerBaseException(PowerBaseError.Code.DATA_DIRECTORY_IS_NOT_SPECIFIED);
				}
			}
			process(response, rc);
		} catch (PowerBaseException e) {
			response.getPrinter().write(("Error: " + PowerBaseError.getMessage(e.getCode())).getBytes());
			rc = e.getCode().hashCode();
		} finally {
			if (iserver != null) {
				iserver.stop();
			}
		}
		System.exit(rc);

	}

	protected void process(ConsoleResponse response, int rc) throws PowerBaseException, IOException, XMLStreamException, ParserConfigurationException, SAXException {
		Client client = new LocalClient(response.getPrinter());
		User user = confirm(client);

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

	protected String getReply(String prompt, boolean required) throws PowerBaseException, IOException {
		String rep = "";
		InputStreamReader isr = new InputStreamReader(System.in);
		System.err.print(prompt + (required?"(required)":"") + ": ");
		BufferedReader br = new BufferedReader(isr);
		rep = br.readLine();
		if (rep == null) {
			throw new PowerBaseException(PowerBaseError.Code.INTERRUPTED);
		}
		if (required && rep.equals("")) {
			rep = getReply(prompt, required);
		}
		return rep;
	}

	protected final User confirm(Client client) throws PowerBaseException, IOException {
		if (StringUtil.isEmpty(password)) {
			InputStreamReader isr = new InputStreamReader(System.in);
			System.err.print("password?: ");
			BufferedReader br = new BufferedReader(isr);
			password = br.readLine();
		}

		User user = new PowerBaseUser(client, User.ADMINISTRATOR);

		if (!password.equals(user.getPassWord())) {
			throw new PowerBaseException(PowerBaseError.Code.AUTHENTICATION_FAILED);
		}

		return user;
	}

	private void showHelp() {
		assert (parser != null);
		System.err.println("[Usage] \n $ java " + getClass().getSimpleName() + parser.printExample(ExampleMode.ALL));
		parser.printUsage(System.err);
	}

}
