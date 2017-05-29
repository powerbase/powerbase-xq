/*
 * @(#)$Id: NameSpace.java 1093 2011-05-25 06:08:28Z hirai $
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

public class NameSpace implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1960394124694804724L;
	private String _ns;
	private String _uri;

	public NameSpace(String uri) {
		this("", uri);
	}

	public NameSpace(String ns, String uri) {
		this._ns = ns;
		this._uri = uri;
	}

	public String getNs() {
		return _ns;
	}

	public String getUri() {
		return _uri;
	}

	public String toXMLString() {
		return this.toXMLString(true);
	}

	public String toXMLString(boolean ns) {
		StringBuffer s = new StringBuffer();
		if (_ns.equals("")) {
			if (ns) {
				s.append("xmlns");
			}
		} else {
			if (ns) {
				s.append("xmlns:");
			}
			s.append(_ns);
		}
		s.append("=");
		s.append("\"");
		s.append(_uri);
		s.append("\"");

		return s.toString();
	}

	public String toString() {
		return this.toXMLString();
	}

	public void clear() {
		this._ns = "";
		this._uri = "";
	}

}
