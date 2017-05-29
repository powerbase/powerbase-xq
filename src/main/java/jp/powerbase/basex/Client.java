/*
 * @(#)$Id: Client.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.basex;

import jp.powerbase.PowerBaseException;

/**
 * BaseX client.
 *
 * @author Infinite Corporation 2005-2011, BSD License
 * @author Toshio HIRAI(Infinite Corporation)
 *
 */
public interface Client {
	/**
	 * BaseX Command enumration.
	 */
	public static enum Command {
		/**
		 * ADD
		 */
		ADD("Add", "ADD"),
		/**
		 * ALTER USER
		 */
		ALTER_USER("AlterUser", "ALTER USER"),
		/**
		 * CHECK
		 */
		CHECK("Check", "CHECK"),
		/**
		 * CLOSE
		 */
		CLOSE("Close", "CLOSE"),
		/**
		 * CREATE COLL
		 */
		CREATE_COLL("CreateColl", "CREATE COLL"),
		/**
		 * CREATE DB
		 */
		CREATE_DB("CreateDB", "CREATE DB"),
		/**
		 * CREATE FS
		 */
		CREATE_FS("CreateFS", "CREATE FS"),
		/**
		 * CREATE INDEX
		 */
		CREATE_INDEX("CreateIndex", "CREATE INDEX"),
		/**
		 * CREATE MAB
		 */
		CREATE_MAB("CreateMAB", "CREATE MAB"),
		/**
		 * CREATE USER
		 */
		CREATE_USER("CreateUser", "CREATE USER"),
		/**
		 * CS
		 */
		CS("Cs", "CS"),
		/**
		 * DELETE
		 */
		DELETE("Delete", "DELETE"),
		/**
		 * DROP DB
		 */
		DROP_DB("DropDB", "DROP DB"),
		/**
		 * DROP INDEX
		 */
		DROP_INDEX("DropIndex", "DROP INDEX"),
		/**
		 * DROP USER
		 */
		DROP_USER("DropUser", "DROP USER"),
		/**
		 * EXPORT
		 */
		EXPORT("Export", "EXPORT"),
		/**
		 * FIND
		 */
		FIND("Find", "FIND"),
		/**
		 * GET
		 */
		GET("Get", "GET"),
		/**
		 * GRANT
		 */
		GRANT("Grant", "GRANT"),
		/**
		 * INFO
		 */
		INFO("Info", "INFO"),
		/**
		 * INFO DB
		 */
		INFO_DB("InfoDB", "INFO DB"),
		/**
		 * INFO INDEX
		 */
		INFO_INDEX("InfoIndex", "INFO INDEX"),
		/**
		 * INFO TABLE
		 */
		INFO_TABLE("InfoTable", "INFO TABLE"),
		/**
		 * KILL
		 */
		KILL("Kill", "KILL"),
		/**
		 * LIST
		 */
		LIST("List", "LIST"),
		/**
		 * MOUNT
		 */
		MOUNT("Mount", "MOUNT"),
		/**
		 * OPEN
		 */
		OPEN("Open", "OPEN"),
		/**
		 * OPTIMIZE
		 */
		OPTIMIZE("Optimize", "OPTIMIZE"),
		/**
		 * PASSWORD
		 */
		PASSWORD("Password", "PASSWORD"),
		/**
		 * RUN
		 */
		RUN("Run", "RUN"),
		/**
		 * SET
		 */
		SET("Set", "SET"),
		/**
		 * SHOW DATABASES
		 */
		SHOW_DATABASES("ShowDatabases", "SHOW DATABASES"),
		/**
		 * SHOW SESSIONS
		 */
		SHOW_SESSIONS("ShowSessions", "SHOW SESSIONS"),
		/**
		 * SHOW USERS
		 */
		SHOW_USERS("ShowUsers", "SHOW USERS"),
		/**
		 * XQUERY
		 */
		XQUERY("XQuery", "XQUERY"),

		;

		private final String value;
		private final String command;

		Command(String value, String command) {
			this.value = value;
			this.command = command;
		}

		/**
		 * @return value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @return command
		 */
		public String getCommand() {
			return command;
		}

	}

	/**
	 * execute command
	 *
	 * @param command
	 * @param args
	 * @throws PowerBaseException
	 */
	public abstract void execute(Command command, String... args) throws PowerBaseException;

	/**
	 * execute XQuery, and return value (as String).
	 * Use it only when a small amount of result is expected.
	 *
	 * @param xquery
	 * @return result of query
	 * @throws PowerBaseException
	 */
	public abstract String executeXQuery(String xquery) throws PowerBaseException;

	/**
	 * close this session.
	 *
	 * @throws PowerBaseException
	 */
	public abstract void close() throws PowerBaseException;

}
