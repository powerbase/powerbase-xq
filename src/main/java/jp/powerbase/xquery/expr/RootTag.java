/*
 * @(#)$Id: RootTag.java 1087 2011-05-25 05:28:29Z hirai $
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
package jp.powerbase.xquery.expr;

public class RootTag implements Wrappable {
	private String rootTag;

	public RootTag(String rootTag) {
		this.rootTag = rootTag;
	}

	private String wrap(String expr, String ob, String cb) {
		StringBuffer xquery = new StringBuffer();
		xquery.append("<");
		xquery.append(rootTag);
		xquery.append(">");
		xquery.append(ob);
		xquery.append(Wrappable.CR);
		xquery.append(expr);
		xquery.append(Wrappable.CR);
		xquery.append(cb);
		xquery.append("</");
		xquery.append(rootTag);
		xquery.append(">");

		return xquery.toString();
	}

	@Override
	public String wrap(String contents) {
		return wrap(contents, "{", "}");
	}

}
