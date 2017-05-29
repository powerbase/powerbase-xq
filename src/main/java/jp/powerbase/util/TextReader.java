/*
 * @(#)$Id: TextReader.java 1116 2011-06-08 08:36:01Z hirai $
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
package jp.powerbase.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class TextReader {
	public static String read(Reader reader) throws IOException {
		int ch;
		StringBuffer contents = new StringBuffer();
		while ((ch = reader.read()) != -1) {
			contents.append((char) ch);
		}
		reader.close();
		return contents.toString();
	}

	public static String read(InputStream input) throws IOException {
		Reader reader = new InputStreamReader(input);
		return read(reader);
	}

	public static String read(File input) throws IOException {
		FileReader reader = new FileReader(input);
		return read(reader);
	}

	public static String read(InputStream in, String encode) throws IOException {
		StringBuffer contents = new StringBuffer();

		InputStreamReader reader = new InputStreamReader(in, encode);
		BufferedReader br = new BufferedReader(reader);
		String line;
		while ((line = br.readLine()) != null) {
			contents.append(line);
			contents.append(System.getProperty("line.separator"));
		}
		br.close();
		reader.close();

		return contents.toString();
	}

	public static String read(String filename, String encode) throws IOException {
		File inputFile = new File(filename);
		FileInputStream infile = new FileInputStream(inputFile);
		return read(infile, encode);
	}

}
