/*
 * @(#)$Id: Database.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.net.URL;
import java.util.ArrayList;

import jp.powerbase.PowerBaseException;

import org.w3c.dom.NodeList;

public interface Database {
	public enum Type {
		DOCUMENT("document"),
		TUPLE("tuple"),
		QUERY("query"),
		RESOURCE("resource"),
		LINK("link"),
		FILE("file"),
		;

		public final String value;

		Type(String value) {
			this.value = value;
		}

		public static Type getType(String key) {
			for (Type val : Type.values()) {
				if (val.value.equalsIgnoreCase(key)) {
					return val;
				}
			}
			return null;
		}
		public String toString() {
			return value;
		}

	}

	public enum Label {
		ID("id"),

		;

		public final String label;

		Label(String label) {
			this.label = label;
		}

	}

	public abstract int getId();

	public abstract Path getPath();

	public abstract String getName();

	public abstract String getOwner();

	public abstract String getDescription();

	public abstract Type getType();

	public abstract ArrayList<NameSpace> getNamespaces();

	public abstract ArrayList<Heading> getHeadings();

	public abstract String getTuplePath();

	public abstract String getRootTag();

	public abstract String getTupleTag();

	public abstract String getQuery();

	public abstract boolean isTuple();

	public abstract NodeList getMeta();

	public abstract URL getUrl();

	public abstract Path getTarget();

	public abstract ArrayList<String> getReadable();

	public abstract ArrayList<String> getWritable();

	public abstract Directory getParent() throws PowerBaseException;

	public abstract boolean exists();

}
