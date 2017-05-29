/*
 * @(#)$Id: AbstractProcessor.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase.xmldb.process;

import java.net.URI;
import java.net.URISyntaxException;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Request;
import jp.powerbase.Response;
import jp.powerbase.constant.CommonUseNameSpaces;
import jp.powerbase.response.ResponseTag;
import jp.powerbase.util.Log;
import jp.powerbase.util.StopWatch;
import jp.powerbase.xmldb.Processor;

public abstract class AbstractProcessor implements Processor {
	Request req;
	Response res;

	public AbstractProcessor() {
	}

	public abstract void setStatus();

	public AbstractProcessor(Request req, Response res) throws PowerBaseException {
		this.req = req;
		this.res = res;
		try {
			this.res.setBaseURI(new URI(this.req.getBaseURI()));
			System.setProperty("jp.powerbase.server.location", this.req.getBaseURI());
			this.res.setResponseTag(new ResponseTag(req.getRootTag(), CommonUseNameSpaces.getNamespaces()));
		} catch (URISyntaxException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		}

	}

	@Override
	public void execute() throws PowerBaseException {
		StopWatch sw = new StopWatch();
		try {
			process();
		} finally {
			try {
			} catch (Exception e) {
				throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
			}
		}
		Log.debug("----- core process end. (elasped: " + sw + ") -----");
	}

	public abstract void process() throws PowerBaseException;

}
