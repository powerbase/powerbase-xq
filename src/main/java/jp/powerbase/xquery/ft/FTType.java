/*
 * @(#)$Id: FTType.java 1087 2011-05-25 05:28:29Z hirai $
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
import jp.powerbase.xquery.expr.Quote;
import jp.powerbase.xquery.expr.Logical.Operator;

import org.apache.commons.lang.StringUtils;

public abstract class FTType {

	protected Database db;
	protected Operator ftOp;
	protected List<String> words;

	protected String words() {
		String[] w = new String[words.size()];

		Quote q = new Quote();
		for (int i = 0; i < words.size(); i++) {
			w[i] = q.wrap(words.get(i));
		}
		return StringUtils.join(w, ftOp == Operator.AND ? "ftand" : "ftor");
	}

	public abstract String buildFunction();

	public abstract String build();

}
