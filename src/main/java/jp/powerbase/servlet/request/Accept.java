/*
 * @(#)$Id: Accept.java 1121 2011-06-13 10:39:57Z hirai $
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
package jp.powerbase.servlet.request;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import jp.powerbase.util.ContentType;

public class Accept {

	ArrayList<String> accepts = new ArrayList<String>();
	boolean binary;

	public Accept(HttpServletRequest req) {
		Enumeration e = req.getHeaders("Accept");
		while (e.hasMoreElements()) {
			String elements = (String) e.nextElement();
			String[] element = elements.split(",");
			for (int i = 0; i < element.length; i++) {
				accepts.add(element[i].trim());
			}
		}

		if (accepts.isEmpty()) {
			binary = false;
		} else {
			binary = false;
			Iterator<String> i = accepts.iterator();
			while (i.hasNext()) {
				String type = i.next();
				if (!type.equals("*/*")) {
					if (!ContentType.isText(type)) {
						binary = true;
						break;
					}
				}
			}
		}
	}

	/**
	 * binary
	 * @return binary
	 */
	public boolean isBinary() {
	    return binary;
	}
}
