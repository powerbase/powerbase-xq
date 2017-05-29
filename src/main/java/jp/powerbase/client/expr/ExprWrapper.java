/*
 * @(#)$Id: ExprWrapper.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.client.expr;

import jp.powerbase.Command;

public class ExprWrapper {
	private ExprWrapper() {
	}

	public static String Wrap(String contents) {
		return Wrap(contents, null);
	}

	public static String Wrap(String contents, Command command) {
		StringBuffer s = new StringBuffer();
		if (command != null) {
			s.append("<expr cmd=\"");
			s.append(command.value);
			s.append("\">");
		} else {
			s.append("<expr>");
		}
		s.append(contents);
		s.append("</expr>");
		return s.toString();
	}

}
