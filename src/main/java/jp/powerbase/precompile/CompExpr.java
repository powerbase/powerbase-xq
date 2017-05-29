/*
 * @(#)$Id: CompExpr.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.util.HashMap;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;

public class CompExpr {

	private String leftOperand;
	private String operator;
	private String rightOperand;

	public CompExpr(String leftOperand, String operator, String rightOperand) {
		this.leftOperand = leftOperand;
		this.operator = operator.toLowerCase();
		this.rightOperand = rightOperand;
	}

	private String convert(String operator) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("eq", "=");
		map.put("ne", "!=");
		map.put("gt", ">");
		map.put("lt", "<");
		map.put("ge", ">=");
		map.put("le", "<=");
		return map.get(operator);
	}

	public String toExpr() throws PowerBaseException {
		StringBuffer sb = new StringBuffer();
		if (operator.equals("eq") || operator.equals("ne") || operator.equals("gt") || operator.equals("lt") || operator.equals("ge") || operator.equals("le")) {
			// 比較式
			sb.append(Parser.PREFIX + "/");
			sb.append(leftOperand);
			sb.append(" ");
			sb.append(convert(operator));
			sb.append(" ");
			sb.append(rightOperand);
		} else if (operator.equals("=") || operator.equals("!=") || operator.equals(">") || operator.equals("<") || operator.equals(">=") || operator.equals("<=")) {
			sb.append(Parser.PREFIX + "/");
			sb.append(leftOperand);
			sb.append(" ");
			sb.append(operator);
			sb.append(" ");
			sb.append(rightOperand);
		} else {
			// 関数
			if (operator.equals("ct")) {
				sb.append("contains");
			} else if (operator.equals("sw")) {
				sb.append("starts-with");
			} else if (operator.equals("ew")) {
				sb.append("ends-with");
			} else {
				throw new PowerBaseException(PowerBaseError.Code.INVALID_REQUEST);
			}
			sb.append("(");
			sb.append(Parser.PREFIX + "/");
			sb.append(leftOperand);
			sb.append(", ");
			sb.append(rightOperand);
			sb.append(")");

		}

		return sb.toString();
	}
}
