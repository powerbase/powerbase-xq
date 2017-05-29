/*
 * @(#)$Id: TokenUtil.java 1087 2011-05-25 05:28:29Z hirai $
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

import java.util.Arrays;
import java.util.List;

public class TokenUtil {

	public static final int ERROR = 0;
	public static final int NUMBER = 1;
	public static final int STRING = 2;

	// 記号
	public static final int LPAREN = 3;
	public static final int RPAREN = 4;
	public static final int LBRACE = 5;
	public static final int RBRACE = 6;
	public static final int LBRACK = 7;
	public static final int RBRACK = 8;
	public static final int COMMMA = 9;
	public static final int SEMICOLON = 10;
	public static final int COLON = 11;
	public static final int SLASH = 12;
	public static final int QUOT = 13;
	public static final int APOS = 14;

	// キーワード
	public static final int LOP = 15;
	public static final int DIRECTION = 16;
	public static final int OPERATOR = 17;

	public static final int IDENTIFIER = 18; // 識別子

	private static final String[] logicalOperatorArray = { "and", "or" };
	private static final String[] directionArray = { "asc", "desc" };
	private static final String[] symbolsArray = { "(", ")", "{", "}", "[", "]", ",", ";", ":", "/", "\"", "'" };
	private static final String[] operatorArray = { "eq", "ne", "gt", "lt", "ge", "le", "ct", "sw", "ew" };

	public static final List<String> lops = Arrays.asList(logicalOperatorArray);
	public static final List<String> directions = Arrays.asList(directionArray);
	public static final List<String> symbols = Arrays.asList(symbolsArray);
	public static final List<String> operators = Arrays.asList(operatorArray);

	public static boolean isLop(String s) {
		return lops.contains(s);
	}

	public static boolean isDirection(String s) {
		return directions.contains(s);
	}

	public static boolean isKeyword(String s) {
		return operators.contains(s);
	}

	public static boolean isSymbol(String s) {
		return symbols.contains(s);
	}

	public static String toPrintFormat(Token t) {
		StringBuilder sb = new StringBuilder();
		sb.append("\tindex:" + t.getIndexNumber() + "\n");
		int type = t.getType();
		sb.append("\ttype :" + type + "\n");
		sb.append("\tvalue:");
		switch (type) {
		// case TokenUtil.NUMBER:
		// sb.append(t.getN());
		// break;
		default:
			sb.append(t.getS());
		}
		return sb.toString();
	}

}
