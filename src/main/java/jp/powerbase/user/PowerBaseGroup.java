/*
 * @(#)$Id: PowerBaseGroup.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.user;

import jp.powerbase.Group;
import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.basex.Client;
import jp.powerbase.util.StringUtil;

public class PowerBaseGroup implements Group {
	private String id = "";
	private String name = "";
	private boolean valid = false;

	public PowerBaseGroup(Client client, String group) throws PowerBaseException {
		if (group == null || group.equals("")) {
			valid = false;
			return;
		}

		id = client.executeXQuery("db:node-id(doc(\"users\")/root/group[@id=\"" + group + "\"])");
		if (StringUtil.isEmpty(id)) {
			throw new PowerBaseException(PowerBaseError.Code.GROUP_NOT_FOUND);
		}

		int c = Integer.valueOf(client.executeXQuery("count(doc(\"users\")/root/group[@id=\"" + group + "\"])"));
		if (c != 0) {
			valid = true;
			name = group;
		} else {
			valid = false;
		}

	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getGroupId() {
		return id;
	}

	@Override
	public boolean isValid() {
		return valid;
	}

}
