/*
 * @(#)$Id: Builder.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.xquery;

import jp.powerbase.PowerBaseException;

public abstract class Builder {
	public enum ClassName {
		Dump("jp.powerbase.xquery.builder.Dump"),
		Get("jp.powerbase.xquery.builder.Get"),
		Tuple("jp.powerbase.xquery.builder.Tuple"),
		TupleIndex("jp.powerbase.xquery.builder.TupleIndex"),
		TupleSingle("jp.powerbase.xquery.builder.TupleSingle"),
		TupleRangeBasedOnSEQ("jp.powerbase.xquery.builder.TupleRangeBasedOnSEQ"),
		TupleIndexRangeBasedOnSEQ(
				"jp.powerbase.xquery.builder.TupleIndexRangeBasedOnSEQ"),
		TupleSingleBasedOnSEQ(
				"jp.powerbase.xquery.builder.TupleSingleBasedOnSEQ"),
		TupleIndexBasedOnSEQ(
				"jp.powerbase.xquery.builder.TupleSimpleIndexBasedOnSEQ"),
		TupleSimpleRangeBasedOnSEQ(
				"jp.powerbase.xquery.builder.TupleSimpleRangeBasedOnSEQ"),
		TupleSimpleIndexRangeBasedOnSEQ(
				"jp.powerbase.xquery.builder.TupleSimpleIndexRangeBasedOnSEQ"),
		TupleSimpleSingleBasedOnSEQ(
				"jp.powerbase.xquery.builder.TupleSimpleSingleBasedOnSEQ"),
		TupleSimpleIndexBasedOnSEQ(
				"jp.powerbase.xquery.builder.TupleSimpleIndexBasedOnSEQ"),
		QueryGet("jp.powerbase.xquery.builder.QueryGet"),
		Query("jp.powerbase.xquery.builder.Query"),
		QueryIndex("jp.powerbase.xquery.builder.QueryIndex"),
		QuerySingle("jp.powerbase.xquery.builder.QuerySingle"),
		QueryRangeBasedOnSEQ("jp.powerbase.xquery.builder.QueryRangeBasedOnSEQ"),
		QueryIndexRangeBasedOnSEQ(
				"jp.powerbase.xquery.builder.QueryIndexRangeBasedOnSEQ"),
		QuerySingleBasedOnSEQ(
				"jp.powerbase.xquery.builder.QuerySingleBasedOnSEQ"),
		QueryIndexBasedOnSEQ("jp.powerbase.xquery.builder.QueryIndexBasedOnSEQ"), ;

		private String fqn;

		private ClassName(String fqn) {
			this.fqn = fqn;
		}

		public String toString() {
			return fqn;
		}
	}

	static protected final String _SEPARATOR = System.getProperty("line.separator");
	protected String _for = "";
	protected String _xpath = "";
	protected String _let = "";
	protected String _where = "";
	protected String _order = "";
	protected String _return = "";

	public abstract void buildFor() throws PowerBaseException;

	public abstract void buildXPath() throws PowerBaseException;

	public abstract void buildLet() throws PowerBaseException;

	public abstract void buildWhere() throws PowerBaseException;

	public abstract void buildOrderBy() throws PowerBaseException;

	public abstract void buildReturn() throws PowerBaseException;

	public abstract String getResult();

}
