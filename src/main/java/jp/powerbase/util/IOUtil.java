/*
 * @(#)$Id: IOUtil.java 1115 2011-06-07 08:30:34Z hirai $
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtil {
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    public static int copy(InputStream input, OutputStream output) throws IOException {
		final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
		    output.write(buffer, 0, n);
		    count += n;
		}
		input.close();
		output.close();
		return count;
    }

    public static int copy(InputStream input, File output) throws IOException {
    	OutputStream out = new FileOutputStream(output);
    	return copy(input, out);
    }

    public static int copy(byte[] input, File output) throws IOException {
    	FileOutputStream fout = new FileOutputStream(output);
    	fout.write(input);
    	fout.close();
    	return input.length;
    }

    public static byte[] fileToByteArray(File input) throws IOException {
        FileInputStream fin = new FileInputStream(input);
        int length = (int)input.length();
        byte[] output = new byte[length];
        fin.read(output);
        fin.close();
        return output;
    }
}
