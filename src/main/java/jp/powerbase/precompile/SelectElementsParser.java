/*
 * @(#)$Id: SelectElementsParser.java 1087 2011-05-25 05:28:29Z hirai $
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
package jp.powerbase.precompile;

import java.util.List;
import java.util.ListIterator;

public class SelectElementsParser extends Parser {
	private ListIterator<Token> tokens;

	public SelectElementsParser(List<Token> tokens) {
		this.tokens = (ListIterator<Token>) tokens.listIterator();
	}

	@Override
	// TODO REVIEWME
	public String parse() {
		StringBuffer sb = new StringBuffer();
		sb.append("{" + Parser.PREFIX + "/");
		while (tokens.hasNext()) {
			if (check(tokens, TokenUtil.COMMMA)) {
				sb.append("}");
				tokens.next();
				if (tokens.hasNext()) {
					sb.append("{" + Parser.PREFIX + "/");
				}
			} else {
				Token el = tokens.next();
				sb.append(el.getS());
			}
		}

		sb.append("}");
		return sb.toString();
	}

}
