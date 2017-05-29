/*
 * @(#)$Id: Parser.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.util.ListIterator;

import jp.powerbase.PowerBaseException;

public abstract class Parser {
	public static final String PREFIX = "$t";

	public abstract String parse() throws PowerBaseException;

	public boolean check(ListIterator<Token> tokens, int type) {
		if (tokens.hasNext()) {
			Token t = tokens.next();
			if (type == TokenUtil.APOS && t.getType() == TokenUtil.APOS) {
				Token nnt = tokens.next();
				if (nnt.getType() == TokenUtil.APOS) {
					tokens.previous();
					tokens.previous();
					return false;
				}
				tokens.previous();
			}

			if (type == TokenUtil.QUOT && t.getType() == TokenUtil.QUOT) {
				Token nnt = tokens.next();
				if (nnt.getType() == TokenUtil.QUOT) {
					tokens.previous();
					tokens.previous();
					return false;
				}
				tokens.previous();
			}
			tokens.previous();
			return (t.getType() == type);
		} else {
			return false;
		}
	}

}
