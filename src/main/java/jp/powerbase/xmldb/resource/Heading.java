/*
 * @(#)$Id: Heading.java 1093 2011-05-25 06:08:28Z hirai $
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
package jp.powerbase.xmldb.resource;

import java.io.Serializable;

public class Heading implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -4647572103002365048L;
	private int seq;
	private String prefix;
	private String heading;
	private String suffix;

	public Heading(int seq, String heading, String prefix, String suffix) {
		super();
		this.seq = seq;
		this.prefix = prefix;
		this.heading = heading;
		this.suffix = suffix;
	}

	public Heading(int seq, String heading) {
		this(seq, heading, "", "");
	}

	/**
	 * seq
	 *
	 * @return seq
	 */
	public int getSeq() {
		return seq;
	}

	/**
	 * prefix
	 *
	 * @return prefix
	 */
	public String getPrefix() {
		return prefix.replaceAll("\"", "&quot;");
	}

	/**
	 * heading
	 *
	 * @return heading
	 */
	public String getHeading() {
		return heading;
	}

	/**
	 * suffix
	 *
	 * @return suffix
	 */
	public String getSuffix() {
		return suffix.replaceAll("\"", "&quot;");
	}

}
