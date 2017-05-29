/*
 * @(#)$Id: Settings.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import jp.powerbase.servlet.PowerBase;
import jp.powerbase.util.Log;
import jp.powerbase.util.StringUtil;

public class Settings {
	private static final String PROPERTY_FILE_NAME = "powerbase.properties";

	// element symbols
	public static enum Symbol {
		SYSTEM_NAME("jp.powerbase.system.name", "PowerBase"),
		LANG("jp.powerbase.response.language", "ja"),
		LOGS("jp.powerbase.logs", System.getProperty("java.io.tmpdir")),
		DATA_DIR("jp.powerbase.datadir", System.getProperty("user.dir")),
		WRAPPING_RESPONSE("jp.powerbase.response.wrap", "true"),
		WRAPPING_TAG("jp.powerbase.response.tag", "pb:response"),
		USER_AUTH("jp.powerbase.user.auth", "true"),
		USER_AUTH_METHOD("jp.powerbase.user.auth.method", "WSSE"),
		ENCRYPTION_TYPE("jp.powerbase.security.crypt.algorithm", "RSA"),
		BASEX_SERVER("jp.powerbase.basex.server", "local"),
		BASEX_HOST("jp.powerbase.basex.host", "localhost"),
		BASEX_PORT("jp.powerbase.basex.port", "1984"),
		BASEX_USERID("jp.powerbase.basex.userid", "admin"),
		BASEX_PASSWD("jp.powerbase.basex.passwd", "admin"),
		DEFAULT_DOCUMENT_ROOT("jp.powerbase.document.root", "root"),
		DEFAULT_TUPLE_ROOT("jp.powerbase.tuple.root", "records"),
		DEFAULT_TUPLE_TAG("jp.powerbase.tuple.tag", "record"),
		XQUERY_PROCESSOR("jp.powerbase.xquery.processor", "basex"),

		;
		private final String value;
		private final String initialValue;

		Symbol(String value, String initialValue) {
			this.value = value;
			this.initialValue = initialValue;
		}

		public static Symbol getSymbol(String value) {
			for (Symbol s : values()) {
				if (s.equals(value)) {
					return s;
				}
			}
			return null;
		}

		@Override
		public String toString() {
			return value;
		}

		public String getInitialValue() {
			return initialValue;
		}

	}

	private static final Properties properties;
	static {
		properties = new Properties();
		final String userDir = System.getProperty("user.dir");
		String notify = "";
		try {
			ClassLoader clazz = Settings.class.getClassLoader();
			InputStream is = clazz.getResourceAsStream(PROPERTY_FILE_NAME);
			if (is != null) {
				properties.load(is);
				URL url = clazz.getResource(PROPERTY_FILE_NAME);
				notify = "Loaded powerbase.properties in: " + url.toString();
			} else {
				File propFile = new File(userDir, PROPERTY_FILE_NAME);
				if (propFile.exists()) {
					properties.load(new FileInputStream(propFile));
					notify = "Loaded powerbase.properties in: " + propFile.getAbsolutePath();
				} else {
					notify = "powerbase.properties default value loaded.";
				}
			}
			for (Symbol symbol : Symbol.values()) {
				switch (symbol) {
				case LOGS:
					String logsDir = properties.getProperty(symbol.toString(), symbol.getInitialValue()).trim();
					properties.setProperty(symbol.toString(), logsDir);
					System.setProperty(symbol.toString(), Settings.get(symbol).trim());
					break;
				case DATA_DIR:
					String dataDir = properties.getProperty(symbol.toString(), symbol.getInitialValue()).trim();
					String env = System.getenv("POWERBASE_DATA_DIR");
					if (!StringUtil.isEmpty(env)) {
						dataDir = env;
					}
					properties.setProperty(symbol.toString(), dataDir);
					System.setProperty(symbol.toString(), Settings.get(symbol).trim());
					break;

				case USER_AUTH:
				case ENCRYPTION_TYPE:
				case BASEX_SERVER:
				case XQUERY_PROCESSOR:
				case WRAPPING_RESPONSE:
					properties.setProperty(symbol.toString(), properties.getProperty(symbol.toString(), symbol.getInitialValue()).toLowerCase().trim());
					break;

				case USER_AUTH_METHOD:
					properties.setProperty(symbol.toString(), properties.getProperty(symbol.toString(), symbol.getInitialValue()).toUpperCase().trim());
					break;

				default:
					properties.setProperty(symbol.toString(), properties.getProperty(symbol.toString(), symbol.getInitialValue()).trim());
					break;
				}

			}

			if (PowerBase.context != null) {
				Log.info(notify);
				for (Enumeration e = properties.keys(); e.hasMoreElements();) {
					String key = (String) e.nextElement();
					String value = properties.getProperty(key);
					Log.info(key + " = " + value);
				}
			}
		} catch (Exception e) {
			throw new java.lang.Error("Exception caused while loading user provided properties file.", e);
		}
	}

	private Settings() {
	}

	public static String get(Symbol key) {
		return properties.getProperty(key.value);
	}

	public static String get(Symbol key, String defaultValue) {
		return properties.getProperty(key.value, defaultValue);
	}

}
