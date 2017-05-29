/*
 * @(#)$Id: PowerBaseUser.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jp.powerbase.Group;
import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Settings;
import jp.powerbase.User;
import jp.powerbase.basex.Client;
import jp.powerbase.security.Cipher;
import jp.powerbase.security.DES;
import jp.powerbase.security.RSA;
import jp.powerbase.servlet.PowerBase;
import jp.powerbase.util.StringUtil;
import jp.powerbase.xml.DOM;
import jp.powerbase.xmldb.resource.Database;
import jp.powerbase.xmldb.resource.Directory;

public class PowerBaseUser implements User {
	private String uid = "";
	private String username = "";
	private String user = "";
	private Cipher.Type encryption;
	private String password = "";

	private String dataDir = "";

	private boolean valid = false;

	public List<Group> groups = new ArrayList<Group>();

	public PowerBaseUser() throws PowerBaseException {
		super();
		if (PowerBase.DEBUG) {
			this.user = User.ADMINISTRATOR;
		}
	}

	public PowerBaseUser(Client client, String user) throws PowerBaseException {
		Element root;
		if (user == null || user.equals("")) {
			return;
		}

		dataDir = Settings.get(Settings.Symbol.DATA_DIR);

		uid = client.executeXQuery("db:node-id(doc(\"users\")/root/user[@id=\"" + user + "\"])");
		if (StringUtil.isEmpty(uid)) {
			throw new PowerBaseException(PowerBaseError.Code.USER_NOT_FOUND);
		}

		try {
			DOM dom = new DOM(client.executeXQuery("doc(\"users\")/root/user[@id=\"" + user + "\"]"));
			Document userDoc = dom.get();
			root = userDoc.getDocumentElement();
		} catch (DOMException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (ParserConfigurationException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (SAXException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (IOException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		}
		if (root != null && root.hasChildNodes()) {
			valid = true;
			this.user = user;
			Element name = (Element) root.getElementsByTagName("name").item(0);
			if (name != null && name.hasChildNodes()) {
				username = name.getFirstChild().getNodeValue();
			} else {
				username = "";
			}
			Element passwd = (Element) root.getElementsByTagName("password").item(0);
			if (passwd != null && passwd.hasChildNodes()) {
				password = passwd.getFirstChild().getNodeValue();
				if (passwd.hasAttribute("encryption")) {
					encryption = Cipher.Type.getType(passwd.getAttribute("encryption"));
					switch (encryption) {
					case RSA:
						RSA rsa = new RSA(new File(dataDir, ".rsa"));
						password = rsa.decrypt(password);
						break;
					case DES:
						DES des = new DES(new File(dataDir, ".des"));
						password = des.decrypt(password);
						break;
					case NONE:
						break;
					default:
						throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, new Exception());
					}
				}
			} else {
				password = "";
			}

			Element groups = (Element) root.getElementsByTagName("groups").item(0);
			if (groups != null && groups.hasChildNodes()) {
				NodeList groupNodes = groups.getElementsByTagName("group");

				for (int i = 0; i < groupNodes.getLength(); i++) {
					Element groupTextNode = (Element) groupNodes.item(i);
					String g = groupTextNode.getTextContent();
					if (g != null) {
						Group group = new PowerBaseGroup(client, g);
						if (!group.isValid()) {
							throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, new Exception());
						}
						this.groups.add(group);
					}
				}
			}
		} else {
			valid = false;
			this.user = "";
			username = "Anonymous";
			password = "";
		}

	}

	private boolean scan(Directory directory) throws PowerBaseException {
		if (directory.isRoot()) {
			return true;
		}
		boolean u = false;
		ArrayList<String> visible = directory.getVisible();
		if (visible.contains(user)) {
			u = true;
		}

		boolean g = false;
		Iterator i = groups.iterator();
		while (i.hasNext() && !u && !g) {
			if (visible.contains(((Group) i.next()).getName())) {
				g = true;
			}
		}
		if (!u && !g) {
			return false;
		}

		if (!directory.isRoot()) {
			return scan(directory.getParent());
		}

		return true;
	}

	@Override
	public boolean canView(Directory directory) throws PowerBaseException {
		if (directory.isRoot()) {
			return true;
		}

		if (isAdmin()) {
			return true;
		}

		if (directory.getOwner().equalsIgnoreCase(user)) {
			return true;
		}

		return scan(directory);
	}

	@Override
	public boolean canView(Database database) throws PowerBaseException {
		return canView(database.getParent());
	}

	@Override
	public boolean canRead(Database database) throws PowerBaseException {
		if (isAdmin()) {
			return true;
		}

		if (database.getOwner().equalsIgnoreCase(user)) {
			return true;
		}

		if (!canView(database)) {
			throw new PowerBaseException(PowerBaseError.Code.INVALID_LOCATION_PATH);
		}

		ArrayList<String> readable = database.getReadable();
		if (readable.contains(user)) {
			return true;
		}

		Iterator i = groups.iterator();
		while (i.hasNext()) {
			if (readable.contains(((Group) i.next()).getName())) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean canWrite(Database database) throws PowerBaseException {
		if (!canRead(database)) {
			return false;
		}

		if (isAdmin()) {
			return true;
		}

		if (database.getOwner().equalsIgnoreCase(user)) {
			return true;
		}

		if (!canView(database)) {
			throw new PowerBaseException(PowerBaseError.Code.INVALID_LOCATION_PATH);
		}

		ArrayList<String> writable = database.getWritable();
		if (writable.contains(user)) {
			return true;
		}

		Iterator i = groups.iterator();
		while (i.hasNext()) {
			if (writable.contains(((Group) i.next()).getName())) {
				return true;
			}
		}

		return false;
	}

	public String getRealName() {
		return username;
	}

	public String getUserID() {
		return uid;
	}

	public String getName() {
		return user;
	}

	public String getPassWord() {
		return password;
	}

	public boolean isValid() {
		return valid;
	}

	@Override
	public boolean isAdmin() {
		if (user.equalsIgnoreCase(User.ADMINISTRATOR)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<Group> getGroups() {
		return groups;
	}

}
