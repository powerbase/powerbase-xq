/*
 * @(#)$Id: Attributes.java 1087 2011-05-25 05:28:29Z hirai $
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

public class Attributes implements Expr {
	private ArrayList<Attribute> atts = new ArrayList<Attribute>();

	public Attributes() {

	}

	public Attributes(Attribute att) {
		add(att);
	}

	public void add(Attribute att) {
		atts.add(att);
	}

	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		Iterator i = atts.iterator();
		while (i.hasNext()) {
			s.append(" ");
			s.append(i.next().toString());
		}
		return s.toString();
	}

	@Override
	public String display() {
		return toString();
	}

}
