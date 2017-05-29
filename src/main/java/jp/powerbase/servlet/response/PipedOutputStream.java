/*
 * @(#)$Id: PipedOutputStream.java 1087 2011-05-25 05:28:29Z hirai $
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
package jp.powerbase.servlet.response;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

public class PipedOutputStream extends OutputStream {
	private static final int MAX_SIZE = 4096;

	Writer writer;

	ArrayList<Byte> buffer = new ArrayList<Byte>();

	boolean ascii = true;

	private void print() {
		byte[] b = new byte[buffer.size()];
		for (int i = 0; i < buffer.size(); i++) {
			b[i] = (byte) buffer.get(i);
		}

		String s;
		try {
			s = new String(b, "UTF-8");
			writer.write(s);
			buffer.clear();
		} catch (UnsupportedEncodingException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

	@Override
	public void flush() throws IOException {
		print();
	}

	@Override
	public void write(int i) throws IOException {
		byte b = (byte) i;

		if (b < 0) {
			ascii = false;
		} else {
			ascii = true;
		}
		if (ascii) {
			if (buffer.size() > MAX_SIZE) {
				print();
				writer.write(b);
			} else {
				buffer.add(b);
			}
		} else {
			buffer.add(b);
		}
	}

	public PipedOutputStream() {
		super();
	}

	public PipedOutputStream(Writer printWriter) {
		super();
		this.writer = printWriter;
	}

	@Override
	public void close() throws IOException {
		this.flush();
		super.close();
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		super.write(b, off, len);
	}

	@Override
	public void write(byte[] b) throws IOException {
		super.write(b);
	}

}
