/*
 * @(#)$Id: CreateDatabaseSpace.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.xmldb;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import jp.powerbase.Group;
import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Settings;
import jp.powerbase.User;
import jp.powerbase.basex.Client;
import jp.powerbase.security.Cipher;
import jp.powerbase.security.CipherUtil;
import jp.powerbase.security.DES;
import jp.powerbase.security.RSA;
import jp.powerbase.util.FileUtil;

public class CreateDatabaseSpace {
	private static final String INITIAL_PASSWORD = "admin";

	public final static void create(Client client) throws PowerBaseException {
		create(client, new File(Settings.get(Settings.Symbol.DATA_DIR)));
	}

	public final static void create(Client client, File dataDir) throws PowerBaseException {
		if (!dataDir.exists()) {
			throw new PowerBaseException(PowerBaseError.Code.DATABASE_DIRECTORY_NOT_FOUND);
		}

		if (!dataDir.isDirectory()) {
			throw new PowerBaseException(PowerBaseError.Code.IS_NOT_DIRECTORY);
		}

		if (!FileUtil.isEmptyDirectory(dataDir)) {
			throw new PowerBaseException(PowerBaseError.Code.DATABASE_DIRECTORY_IS_NOT_EMPTY);
		}

		Key desKey;
		KeyPair rsaKey;
		try {
			desKey = (Key) DES.generateKey();
			CipherUtil.saveKey(desKey, new File(dataDir, ".des"));

			rsaKey = (KeyPair) RSA.generateKey();
			CipherUtil.saveKey(rsaKey, new File(dataDir, ".rsa"));
		} catch (NoSuchAlgorithmException e) {
			throw new PowerBaseException(PowerBaseError.Code.DATABASE_CREATE_FAILED, e);
		}

		File files = new File(dataDir, "files");
		if (!files.mkdir()) {
			throw new PowerBaseException(PowerBaseError.Code.UNABLE_MAKE_DIRECTORY);
		}

		client.execute(Client.Command.CREATE_DB, "databases", "<root />");

		try {
			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			StringWriter sw = new StringWriter();
			XMLStreamWriter writer = factory.createXMLStreamWriter(sw);

			writer.writeStartElement("root");
			{
				writer.writeStartElement("user");
				writer.writeAttribute("id", User.ADMINISTRATOR);
				{
					writer.writeStartElement("name");
					writer.writeCharacters("Administrator");
					writer.writeEndElement();

					writer.writeStartElement("password");
					writer.writeAttribute("encryption", "RSA");
					Cipher r = new RSA(rsaKey);
					writer.writeCharacters(r.encrypt(INITIAL_PASSWORD));
					writer.writeEndElement();

				}
				writer.writeEndElement();

				writer.writeStartElement("group");
				writer.writeAttribute("id", Group.PUBLIC_GROUP);
				writer.writeEndElement();

			}
			writer.writeEndElement();
			client.execute(Client.Command.CREATE_DB, "users", sw.toString());

			sw.close();
		} catch (FactoryConfigurationError e) {
			throw new PowerBaseException(PowerBaseError.Code.DATABASE_CREATE_FAILED, e);
		} catch (XMLStreamException e) {
			throw new PowerBaseException(PowerBaseError.Code.DATABASE_CREATE_FAILED, e);
		} catch (IOException e) {
			throw new PowerBaseException(PowerBaseError.Code.DATABASE_CREATE_FAILED, e);
		}

	}
}
