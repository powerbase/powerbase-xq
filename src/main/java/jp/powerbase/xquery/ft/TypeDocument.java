/*
 * @(#)$Id: TypeDocument.java 1101 2011-05-27 10:20:31Z hirai $
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
package jp.powerbase.xquery.ft;

import java.util.List;

import jp.powerbase.xmldb.resource.Database;
import jp.powerbase.xquery.expr.Attribute;
import jp.powerbase.xquery.expr.Attributes;
import jp.powerbase.xquery.expr.Tag;
import jp.powerbase.xquery.expr.Logical.Operator;

public class TypeDocument extends FTType {
	private String unique;

	TypeDocument(Database db, Operator ftOp, List<String> words) {
		this.db = db;
		this.ftOp = ftOp;
		this.words = words;
		int r = (int) (Math.random() * 100000);
		unique = Integer.toString(r);
	}

	@Override
	public String build() {
		StringBuffer q = new StringBuffer();

		q.append("{let $res := local:getNode" + unique + "() return \n");
		q.append("if (count($res) != 0) then \n");

		Attributes atts = new Attributes();
		atts.add(new Attribute("path", db.getPath().toString()));
		atts.add(new Attribute("description", db.getDescription()));
		atts.add(new Attribute("type", db.getType().toString()));
		Tag rt = new Tag("database", atts);
		q.append(rt.wrap("<snippet>{$res}</snippet>", true));
		q.append(" else ()}\n");

		return q.toString();
	}

	@Override
	public String buildFunction() {
		StringBuffer q = new StringBuffer();

		q.append("declare function local:getNode" + unique + "() as node()* \n");
		q.append("{ \n");

		q.append("for $n in ft:mark(doc('" + db.getPath() + "')//* [text() contains text \n");
		q.append(words());
		q.append("]) \n");
		q.append("return $n \n");

		// q.append("	for $n in doc('" + db.getPath() + "')//* \n");
		// q.append("	for $h in ft:extract($n[text() contains text \n");
		// q.append(words());
		// q.append("]) \n");
		// q.append("	return element {fn:local-name($h)}{attribute {'id'}{ db:node-id($n) }, $h/node()} \n");

		q.append("} \n");
		q.append("; \n");

		return q.toString();
	}

}
