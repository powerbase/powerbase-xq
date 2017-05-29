/*
 * @(#)$Id: Token.java 1087 2011-05-25 05:28:29Z hirai $
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

public class Token {
	private int type;
	private String s;
	private double n;
	private int indexNumber;

	public Token(int t, char c) {
		type = t;
		this.s = Character.toString(c);
	}

	public Token(int t, String s) {
		type = t;
		this.s = s;
	}

	public Token(int t, double n) {
		type = t;
		this.n = n;
	}

	public int getType() {
		return type;
	}

	public String getS() {
		return s;
	}

	public double getN() {
		return n;
	}

	public void setIndexNumber(int n) {
		indexNumber = n;
	}

	public int getIndexNumber() {
		return indexNumber;
	}

}
