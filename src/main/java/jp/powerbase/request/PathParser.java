/*
 * @(#)$Id: PathParser.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.powerbase.PowerBaseException;
import jp.powerbase.basex.Client;
import jp.powerbase.xmldb.resource.Database;
import jp.powerbase.xmldb.resource.DatabaseFactory;
import jp.powerbase.xmldb.resource.Path;

public class PathParser {
	private Path path;
	private Client client;

	private String directory = "";
	private String databaseName = "";
	private String nodeID = "";
	private String xpath = "";

	private boolean rangeMode = false;
	private int rangeMin = -1;
	private int rangeMax = -1;
	private int position = -1;
	private boolean indexViewing = false;
	private boolean recursive = false;

	private Database database = null;

	public PathParser(Path path, Client client) {
		this.path = path;
		this.client = client;
	}

	public void parse() throws PowerBaseException {
		List<String> paths = path.getPathList();
		StringBuffer p = new StringBuffer();
		StringBuffer xpath = new StringBuffer();
		int i = 0;
		for (; i < paths.size(); i++) {
			String element = paths.get(i);
			p.append("/");
			p.append(element);

			if (new Path(p.toString()).isDatabase(client)) {
				databaseName = Path.stripSlash(p.toString());
				database = new DatabaseFactory(client, new Path(databaseName)).getInstance();
				i++;
				break;
			} else if (element.equals("*")) {
				recursive = true;
				i++;
				break;
			} else {
				directory = Path.stripSlash(p.toString());
			}
		}

		if (i < paths.size() && database != null && database.exists()) {
			for (; i < paths.size(); i++) {
				if (paths.get(i).equalsIgnoreCase("index")) {
					indexViewing = true;
				} else {
					try {
						Integer.parseInt(paths.get(i));
						nodeID = paths.get(i);
						i++;
						break;
					} catch (NumberFormatException e) {
						Pattern range = Pattern.compile("^\\[([0-9]+)\\]\\-\\[([0-9]+)\\]$");
						Pattern pos = Pattern.compile("^\\[([0-9]+)\\]$");
						Matcher mRange = range.matcher(paths.get(i));
						if (mRange.find()) {
							rangeMode = true;
							rangeMin = Integer.valueOf(mRange.group(1));
							rangeMax = Integer.valueOf(mRange.group(2));
							i++;
							break;
						} else {
							Matcher mPos = pos.matcher(paths.get(i));
							if (mPos.find()) {
								position = Integer.valueOf(mPos.group(1));
								i++;
								break;
							} else {
								xpath.append("/");
								xpath.append(paths.get(i));
							}
						}
					}
				}
			}
		}
		for (; i < paths.size(); i++) {
			xpath.append("/");
			xpath.append(paths.get(i));
		}
		this.xpath = xpath.toString();
	}

	/**
	 * directory
	 *
	 * @return directory
	 */
	public String getDirectory() {
		return directory;
	}

	/**
	 * database
	 *
	 * @return database
	 */
	public String getDatabaseName() {
		return databaseName;
	}

	/**
	 * nodeID
	 *
	 * @return nodeID
	 */
	public String getNodeID() {
		return nodeID;
	}

	/**
	 * xpath
	 *
	 * @return xpath
	 */
	public String getXpath() {
		return xpath;
	}

	/**
	 * rangeMode
	 *
	 * @return rangeMode
	 */
	public boolean isRangeMode() {
		return rangeMode;
	}

	/**
	 * rangeMin
	 *
	 * @return rangeMin
	 */
	public int getRangeMin() {
		return rangeMin;
	}

	/**
	 * rangeMax
	 *
	 * @return rangeMax
	 */
	public int getRangeMax() {
		return rangeMax;
	}

	/**
	 * position
	 *
	 * @return position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * indexViewing
	 *
	 * @return indexViewing
	 */
	public boolean isIndexViewing() {
		return indexViewing;
	}

	/**
	 * recursive
	 *
	 * @return recursive
	 */
	public boolean isRecursive() {
		return recursive;
	}

	public Database getDatabase() {
		return database;
	}

}
