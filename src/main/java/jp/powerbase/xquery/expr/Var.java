/*
 * @(#)$Id: Var.java 1087 2011-05-25 05:28:29Z hirai $
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

import jp.powerbase.precompile.Parser;

public class Var implements Expr {
	private String var;

	public Var() {
		var = Parser.PREFIX;
	}

	public Var(String var) {
		this(var, true);
	}

	public Var(String var, boolean prefix) {
		this.var = var;
		if (prefix && !this.var.substring(0, 1).equals("$")) {
			this.var = "$" + var;
		}
	}

	@Override
	public String toString() {
		return var;
	}

	@Override
	public String display() {
		return toString();
	}

}
