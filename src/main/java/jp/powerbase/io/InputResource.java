/*
 * @(#)$Id: InputResource.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.io;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;

public class InputResource {
	private final InputStream stream;
	private File file;

	public InputResource(File f) throws PowerBaseException {
		this.file = f;
		try {
			this.stream = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			throw new PowerBaseException(PowerBaseError.Code.FILE_NOT_FOUND, e);
		}
	}

	public InputResource(String s, String Encoding) throws PowerBaseException {
		try {
			this.stream = new ByteArrayInputStream(s.getBytes(Encoding));
		} catch (UnsupportedEncodingException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		}
	}

	public InputResource(String s) throws PowerBaseException {
		this(s, "UTF-8");
	}

	public InputStream get() {
		return stream;
	}

	public void delete() {
		if (stream instanceof FileInputStream) {
			if (file.exists()) {
				file.delete();
			}
		}
	}

}
