/*
 * @(#)$Id: Tokenizer.java 1087 2011-05-25 05:28:29Z hirai $
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

import java.util.Iterator;

public class Tokenizer implements Iterator<Token> {
	private Scanner sline;
	private Token nextToken = null;
	private int prevIndex = -1;

	public Tokenizer(String context) {
		this.sline = new Scanner(context);
	}

	@Override
	public boolean hasNext() {
		sline.cutWhitespace();
		return sline.hasNext();
	}

	@Override
	public Token next() {
		Token token = null;

		int index = sline.getIndex();
		if (prevIndex != -1) {
			nextToken.setIndexNumber(prevIndex);
			prevIndex = -1;
			return nextToken;
		}

		char ch = sline.peek();
		if (sline.isString()) {
			StringBuffer s = new StringBuffer();
			while (sline.hasNext() && sline.isString()) {
				char c = sline.next();
				if (c == sline.getQuotedBy()) {
					prevIndex = sline.getIndex();

					if (c == '\'') {
						nextToken = new Token(TokenUtil.APOS, "'");
					} else {
						nextToken = new Token(TokenUtil.QUOT, "\"");
					}
					break;
				}
				s.append(c);
			}
			token = new Token(TokenUtil.STRING, s.toString());
		} else if (Character.isLetter(ch) || Character.isDigit(ch)) {
			StringBuilder sb = new StringBuilder();
			while (sline.hasNext() && (Character.isLetter(ch) || Character.isDigit(ch))) {
				ch = sline.next();
				sb.append(ch);
				ch = sline.peek();
			}
			String s = sb.toString();
			if (TokenUtil.isKeyword(s.toLowerCase())) {
				token = new Token(TokenUtil.OPERATOR, s);
			} else if (TokenUtil.isLop(s.toLowerCase())) {
				token = new Token(TokenUtil.LOP, s);
			} else if (TokenUtil.isDirection(s.toLowerCase())) {
				token = new Token(TokenUtil.DIRECTION, s);
			} else {
				try {
					Integer.parseInt(s);
					token = new Token(TokenUtil.NUMBER, s);
				} catch (NumberFormatException e) {
					token = new Token(TokenUtil.IDENTIFIER, s);
				}
			}
		} else {
			switch (ch) {
			case '(':
				token = new Token(TokenUtil.LPAREN, sline.next());
				break;
			case ')':
				token = new Token(TokenUtil.RPAREN, sline.next());
				break;
			case '{':
				token = new Token(TokenUtil.LBRACE, sline.next());
				break;
			case '}':
				token = new Token(TokenUtil.RBRACE, sline.next());
				break;
			case '[':
				token = new Token(TokenUtil.LBRACK, sline.next());
				break;
			case ']':
				token = new Token(TokenUtil.RBRACK, sline.next());
				break;
			case ',':
				token = new Token(TokenUtil.COMMMA, sline.next());
				break;
			case ';':
				token = new Token(TokenUtil.SEMICOLON, sline.next());
				break;
			case ':':
				token = new Token(TokenUtil.COLON, sline.next());
				break;
			case '/':
				token = new Token(TokenUtil.SLASH, sline.next());
				break;
			case '"':
				token = new Token(TokenUtil.QUOT, sline.next());
				break;
			case '\'':
				token = new Token(TokenUtil.APOS, sline.next());
				break;

			default:
				token = new Token(TokenUtil.ERROR, sline.next());
			}
		}
		token.setIndexNumber(index);
		return token;
	}

	@Override
	public void remove() {
	}

}
