/*
 * @(#)$Id: LinkDatabase.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.xmldb.resource;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.xpath.XPathExpressionException;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.basex.Client;

public class LinkDatabase extends DefaultDatabase {

	LinkDatabase(Client client, int id, Path path, Type type) throws PowerBaseException {
		super(client, id, path, type);

		try {
			target = new Path(dom.getNodeValue("/database/target/text()"));

		} catch (XPathExpressionException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		}

	}

	@Override
	public ArrayList<Heading> getHeadings() {
		return null;
	}

	@Override
	public String getTuplePath() {
		return "";
	}

	@Override
	public String getRootTag() {
		return rootTag;
	}

	@Override
	public String getTupleTag() {
		return "";
	}

	@Override
	public String getQuery() {
		return "";
	}

	@Override
	public boolean isTuple() {
		return false;
	}

	@Override
	public URL getUrl() {
		return null;
	}

	@Override
	public Path getTarget() {
		return target;
	}

}
