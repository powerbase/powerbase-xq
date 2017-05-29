/*
 * @(#)$Id: DeclareNameSpaces.java 1087 2011-05-25 05:28:29Z hirai $
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

import java.util.ArrayList;
import java.util.Iterator;

import jp.powerbase.constant.CommonUseNameSpaces;
import jp.powerbase.xmldb.resource.NameSpace;

public class DeclareNameSpaces implements Expr {
	private ArrayList<NameSpace> namespaces = new ArrayList<NameSpace>();

	public DeclareNameSpaces(ArrayList<NameSpace> namespaces) {
		this.namespaces = namespaces;
	}

	public DeclareNameSpaces() {
	}

	public void addNamespace(NameSpace namespace) {
		namespaces.add(namespace);
	}

	@Override
	public String toString() {
		StringBuffer ns = new StringBuffer();
		ns.append("declare namespace");
		ns.append(CommonUseNameSpaces.getNameSpacesAsString(false));
		ns.append(";");
		ns.append(System.getProperty("line.separator"));

		Iterator nss = namespaces.iterator();
		while (nss.hasNext()) {
			ns.append("declare namespace ");
			ns.append(((NameSpace) nss.next()).toXMLString(false));
			ns.append(";");
			ns.append(System.getProperty("line.separator"));
		}

		return ns.toString();

	}

	@Override
	public String display() {
		return toString();
	}

}
