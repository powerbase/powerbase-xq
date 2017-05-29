/*
 * @(#)$Id: CommonUseNameSpaces.java 1093 2011-05-25 06:08:28Z hirai $
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
package jp.powerbase.constant;

import java.util.ArrayList;
import java.util.Iterator;

import jp.powerbase.constant.NameSpaces;
import jp.powerbase.xmldb.resource.NameSpace;

public final class CommonUseNameSpaces {
	private static ArrayList<NameSpace> namespaces = new ArrayList<NameSpace>();

	static {
		namespaces.add(new NameSpace("pb", NameSpaces.POWERBASE_URI));
	}

	public static Iterator<NameSpace> iterator() {
		return namespaces.iterator();
	}

	public static String getNameSpacesAsString() {
		return CommonUseNameSpaces.getNameSpacesAsString(true);
	}

	public static String getNameSpacesAsString(boolean ns) {
		Iterator i = namespaces.iterator();
		StringBuffer sb = new StringBuffer();
		while (i.hasNext()) {
			sb.append(" ");
			sb.append(((NameSpace) i.next()).toXMLString(ns));
		}
		return sb.toString();

	}

	/**
	 * namespaces
	 *
	 * @return namespaces
	 */
	public static ArrayList<NameSpace> getNamespaces() {
		return namespaces;
	}

}
