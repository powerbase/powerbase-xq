/*
 * @(#)$Id: ByteBufferStream.java 1093 2011-05-25 06:08:28Z hirai $
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

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteBufferStream extends InputStream {
	ByteBuffer buffer;
	int i;

	public ByteBufferStream(ByteBuffer buffer) {
		super();
		this.buffer = buffer;
		this.i = 0;

	}

	@Override
	public int read() throws IOException {
		byte c = buffer.get(i++);
		int r = c & 0xFF;
		if (r == 0) {
			return -1;
		} else {
			return c;
		}
	}

}
