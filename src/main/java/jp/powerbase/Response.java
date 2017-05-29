/*
 * @(#)$Id: Response.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.io.OutputStream;
import java.net.URI;

import jp.powerbase.response.ResponseTag;

public interface Response {

	public abstract OutputStream getPrinter();

	public abstract void write(String s) throws PowerBaseException;

	public abstract void close() throws PowerBaseException;

	public abstract void flush() throws PowerBaseException;

	public abstract void setStatus(int code);

	public abstract ResponseTag getResponseTag();

	public abstract void setResponseTag(ResponseTag responseTag);

	public abstract URI getBaseURI();

	public abstract void setBaseURI(URI baseURI);

	public abstract void setContentType(String contextType);

}
