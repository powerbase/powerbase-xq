/*
 * @(#)$Id: InternalHttpServer.java 1087 2011-05-25 05:28:29Z hirai $
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
package jp.powerbase.datasource;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import com.sun.net.httpserver.HttpServer;

public class InternalHttpServer {

	HttpServer server;
	private int portno;
	private URI uri;

	public InternalHttpServer(int portno) throws IOException {

		boolean open = true;
		Socket sock = null;
		while (open) {
			try {
				sock = new Socket("localhost", portno);
				portno++;
			} catch (Exception e) {
				open = false;
			}
		}
		this.portno = portno;
		try {
			sock.close();
		} catch (Exception e) {
		}
		sock = null;

		server = HttpServer.create(new InetSocketAddress(this.portno), 0);
		Handler handler = new Handler();

		server.createContext("/", handler);
		server.start();
		try {
			uri = new URL("http", "localhost", portno, "").toURI();
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}

	}

	public void stop() {
		server.stop(0);
	}

	public int getPortNo() {
		return portno;
	}

	public URI getURI() {
		return uri;
	}

}
