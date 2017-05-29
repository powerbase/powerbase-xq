/*
 * @(#)$Id: WhereClauseParser.java 1178 2011-07-22 10:16:56Z hirai $
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

import jp.powerbase.PowerBaseException;

public class WhereClauseParser extends Parser {
	private ListIterator<Token> tokens;

	public WhereClauseParser(List<Token> tokens) {
		this.tokens = (ListIterator<Token>) tokens.listIterator();
	}

	@Override
	public String parse() throws PowerBaseException {
		StringBuffer sb = new StringBuffer();
		while (tokens.hasNext()) {
			if (check(tokens, TokenUtil.LPAREN)) {
				sb.append("(");
				tokens.next();
			} else if (check(tokens, TokenUtil.RPAREN)) {
				sb.append(")");
				tokens.next();
			} else if (check(tokens, TokenUtil.LOP)) {
				Token lop = tokens.next();
				sb.append(" ");
				sb.append(lop.getS());
				sb.append(" ");
			} else {
				sb.append(retrieve(tokens));
			}
		}

		return sb.toString();
	}

	private String retrieve(ListIterator<Token> tokens) throws PowerBaseException {
		final int LEFT = 1;
		final int RIGHT = 2;

		int pos = LEFT;

		StringBuffer rightOperand = new StringBuffer();
		StringBuffer leftOperand = new StringBuffer();
		String operator = "";

		while (tokens.hasNext()) {
			if (check(tokens, TokenUtil.RPAREN)) {
				break;
			}
			if (check(tokens, TokenUtil.LOP)) {
				break;
			}

			switch (pos) {
			case LEFT:
				if (check(tokens, TokenUtil.OPERATOR)) {
					Token op = tokens.next();
					operator = op.getS();
					pos = RIGHT;
					// if (check(tokens, TokenUtil.QUOT))
					// {
					// enquoteBy = TokenUtil.QUOT;
					// rightOperand.append("\"");
					// tokens.next();
					// }
					// if (check(tokens, TokenUtil.APOS))
					// {
					// enquoteBy = TokenUtil.APOS;
					// rightOperand.append("'");
					// tokens.next();
					// }
				} else {
					Token lope = tokens.next();
					leftOperand.append(lope.getS());
				}
				break;

			case RIGHT:
				Token rope = tokens.next();
				rightOperand.append(rope.getS());
				break;

			}

		}
		CompExpr comparison = new CompExpr(leftOperand.toString(), operator, rightOperand.toString());
		return comparison.toExpr();
	}

}
