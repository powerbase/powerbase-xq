/*
 * @(#)$Id: BuilderUtil.java 1104 2011-05-31 05:30:05Z hirai $
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
package jp.powerbase.xquery.builder;

import java.util.ArrayList;
import java.util.Iterator;

import jp.powerbase.precompile.Parser;
import jp.powerbase.xmldb.resource.Database;
import jp.powerbase.xmldb.resource.Heading;

public class BuilderUtil {

	private BuilderUtil(){}

	public static String getHeadings(Database db)
	{
		StringBuffer ret = new StringBuffer();
		ArrayList headings = db.getHeadings();
		if (!headings.isEmpty()) {
			for (Iterator i = headings.iterator(); i.hasNext();) {
				Heading heading = (Heading) i.next();
				ret.append("{fn:concat(\"" + heading.getPrefix() + "\"");
				ret.append(", ");
				ret.append("fn:string-join(");
				String h = heading.getHeading();
				if (h.substring(0, 1).equals("@")) {
					ret.append(Parser.PREFIX + "/fn:string(" + h + ")");
				} else {
					ret.append(Parser.PREFIX + "/" + h + "/text()");
				}
				ret.append(", \", \")");
				ret.append(", \"" + heading.getSuffix() + "\")}");
			}
		}
		return ret.toString();
	}

}
