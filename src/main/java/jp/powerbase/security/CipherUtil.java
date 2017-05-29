/*
 * @(#)$Id: CipherUtil.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.security;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.util.Base64;
import jp.powerbase.util.TextReader;

public class CipherUtil {
	public static final Serializable getKey(File input) throws PowerBaseException {
		try {
			String s = TextReader.read(input);
			ByteArrayInputStream in = new ByteArrayInputStream(Base64.decode(s));
			ObjectInputStream is = new ObjectInputStream(in);
			return (Serializable) is.readObject();
		} catch (UnsupportedEncodingException e) {
			throw new PowerBaseException(PowerBaseError.Code.ENCRYPTION_PROCESS_ERROR, e);
		} catch (FileNotFoundException e) {
			throw new PowerBaseException(PowerBaseError.Code.ENCRYPTION_PROCESS_ERROR, e);
		} catch (IOException e) {
			throw new PowerBaseException(PowerBaseError.Code.ENCRYPTION_PROCESS_ERROR, e);
		} catch (ClassNotFoundException e) {
			throw new PowerBaseException(PowerBaseError.Code.ENCRYPTION_PROCESS_ERROR, e);
		}
	}

	public static final void saveKey(Serializable key, File output) throws PowerBaseException {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(out);
			os.writeObject(key);
			byte[] buff = out.toByteArray();
			String outBuff = Base64.encode(buff);
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(output)));
			writer.print(outBuff);
			writer.close();
			os.close();
			out.close();
		} catch (FileNotFoundException e) {
			throw new PowerBaseException(PowerBaseError.Code.ENCRYPTION_PROCESS_ERROR, e);
		} catch (IOException e) {
			throw new PowerBaseException(PowerBaseError.Code.ENCRYPTION_PROCESS_ERROR, e);
		}

	}
}
