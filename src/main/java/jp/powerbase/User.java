/*
 * @(#)$Id: User.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase;

import java.util.List;

import jp.powerbase.xmldb.resource.Database;
import jp.powerbase.xmldb.resource.Directory;

/**
 * PowerBase User.
 *
 * @author Toshio HIRAI <toshio.hirai@gmail.com>
 */
public interface User {
	/**
	 * The name of ADMINISTRATOR.
	 */
	public static final String ADMINISTRATOR = "admin";

	/**
	 * Get real name.
	 * @return User's real name
	 */
	public String getRealName();

	/**
	 * Get user ID.
	 * @return name(user ID)
	 */
	public String getName();

	/**
	 * Get pass word.
	 * @return pass word.
	 */
	public String getPassWord();

	/**
	 * Checks if the user is valid.
	 * @return Whether it is valid user or not?
	 */
	public boolean isValid();

	/**
	 * Checks if the user is Administrator.
	 * @return true if the user is Administrator.
	 */
	public boolean isAdmin();

	/**
	 * Get group list.
	 * @return List of group to which this user belongs.
	 */
	public List<Group> getGroups();

	/**
	 *Checks if the target directory is visible.
	 *
	 * @param directory Target directory.
	 * @return true if target directory is visible.
	 * @throws PowerBaseException exception.
	 */
	public boolean canView(Directory directory) throws PowerBaseException;

	/**
	 * Checks if the target database is visible.
	 *
	 * @param database Target database.
	 * @return true if target database is visible.
	 * @throws PowerBaseException exception
	 */
	public boolean canView(Database database) throws PowerBaseException;

	/**
	 * Checks if the target database is readable.
	 *
	 * @param database Target database.
	 * @return true if target database is readable.
	 * @throws PowerBaseException exception
	 */
	public boolean canRead(Database database) throws PowerBaseException;

	/**
	 * Checks if the target database is writable.
	 *
	 * @param database Target database.
	 * @return true if target database is writable.
	 * @throws PowerBaseException exception
	 */
	public boolean canWrite(Database database) throws PowerBaseException;
}
