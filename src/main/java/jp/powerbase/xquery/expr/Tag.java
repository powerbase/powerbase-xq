/*
 * @(#)$Id: Tag.java 1087 2011-05-25 05:28:29Z hirai $
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

public class Tag implements Wrappable {
	private String tag;
	private Attributes atts;

	public Tag(String tag) {
		this(tag, null);
	}

	public Tag(String tag, Attributes atts) {
		this.tag = tag;
		this.atts = atts;
	}

	@Override
	public String wrap(String contents) {
		return wrap(contents, false);
	}

	public String wrap(String contents, boolean brace) {
		StringBuffer s = new StringBuffer();
		s.append("<");
		s.append(tag);
		if (atts != null) {
			s.append(atts.toString());
		}
		s.append(">");
		if (brace) {
			s.append("{");
		}
		s.append(contents);
		if (brace) {
			s.append("}");
		}
		s.append("</");
		s.append(tag);
		s.append(">");

		return s.toString();
	}

}
