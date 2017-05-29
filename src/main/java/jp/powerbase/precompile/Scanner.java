/*
 * @(#)$Id: Scanner.java 1087 2011-05-25 05:28:29Z hirai $
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

public class Scanner {
	private String line;
	private int current;
	private int maxIndex;

	private boolean string = false;
	char quotedBy;

	public Scanner(String s) {
		line = s;
		current = 0;
		if (line != null) {
			maxIndex = line.length();
		}
	}

	public char next() {
		if (!hasNext()) {
			return (char) -1;
		}
		char c = line.charAt(current++);
		if (c == '\'' || c == '"') {
			if (!string) {
				quotedBy = c;
				string = true;
			} else {
				if (quotedBy == c) {
					string = false;
				}
			}
		}
		return c;
	}

	public char peek() {
		if (!hasNext()) {
			return (char) -1;
		}
		char c = line.charAt(current);
		return c;
	}

	public int getIndex() {
		return current;
	}

	public boolean hasNext() {
		return (line != null) && (maxIndex != current);
	}

	public void cutWhitespace() {
		char ch = peek();
		while (Character.isWhitespace(ch)) {
			next();
			ch = peek();
		}
	}

	/**
	 * string
	 *
	 * @return string
	 */
	public boolean isString() {
		return string;
	}

	/**
	 * quotedBy
	 *
	 * @return quotedBy
	 */
	public char getQuotedBy() {
		return quotedBy;
	}

}
