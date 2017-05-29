/*
 * @(#)$Id: XMLLoader.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.io.File;

import javax.servlet.http.HttpServletResponse;
import javax.xml.xpath.XPathExpressionException;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Request;
import jp.powerbase.Response;
import jp.powerbase.basex.Client;
import jp.powerbase.response.XMLResponse;
import jp.powerbase.xml.DOM;
import jp.powerbase.xmldb.loader.DefaultLoader;

public class XMLLoader extends AbstractProcessor {

    public XMLLoader(Request req, Response res) throws PowerBaseException {
	super(req, res);
    }

    /*
     * (非 Javadoc)
     *
     * @see jp.infinite.xmldb.process.AbstractProcessor#process()
     */
    @Override
    public void process() throws PowerBaseException {
	DOM dom = new DOM(req.getRequestBody());
	String fileName;
	try {
	    fileName = dom.getNodeValue("/request/file/text()");
	    File up = new File(fileName);

	    DefaultLoader loader = new DefaultLoader(req, up);
	    loader.load();

	    Client session = req.getClient();
	    session.execute(Client.Command.OPEN, Integer.toString(req.getDatabase().getId()));
	    session.execute(Client.Command.OPTIMIZE);

	} catch (XPathExpressionException e) {
	    throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
	}

	XMLResponse xres = new XMLResponse(res);
	xres.write(res.getPrinter());

    }

    @Override
    public void setStatus() {
	res.setStatus(HttpServletResponse.SC_OK);
    }

}
