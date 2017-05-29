/*
 * @(#)$Id: ReadByXQueryViaXBird.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import xbird.xquery.XQueryException;
import xbird.xquery.XQueryModule;
import xbird.xquery.XQueryProcessor;
import xbird.xquery.dm.XQEventReceiver;
import xbird.xquery.dm.ser.StAXSerializer;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Request;
import jp.powerbase.Response;
import jp.powerbase.util.Log;

public class ReadByXQueryViaXBird extends AbstractProcessor {
	protected String results;
	private String xquery;

	public ReadByXQueryViaXBird() {
	}

	public ReadByXQueryViaXBird(Request req, Response res) throws PowerBaseException {
		super(req, res);
		xquery = req.getXqueryContext().toString();
	}

	public ReadByXQueryViaXBird(Request req, Response res, String xquery) throws PowerBaseException {
		super(req, res);
		this.xquery = xquery;
	}

	/*
	 * (非 Javadoc)
	 *
	 * @see jp.infinite.xmldb.process.AbstractProcessor#process()
	 */
	@Override
	public void process() throws PowerBaseException {
		/*
		 * StringBuffer xq = new StringBuffer(); xq.append(
		 * "declare namespace pb=\"http://pb.infinite.jp/dtd/powerbase/\"; declare namespace xlink=\"http://www.w3.org/1999/xlink/\";"
		 * ); xq.append("<records> "); xq.append("{ "); xq.append(
		 * "for $a in distinct-values(doc(\"/aozora/sakuhin\")/records/record/author) "
		 * ); xq.append("return "); xq.append("<author value=\"{ $a }\"> ");
		 * xq.append("{ ");
		 * xq.append("for $b in doc(\"/aozora/sakuhin\")/records/record ");
		 * xq.append("where some $ba in $b/author satisfies $ba = $a ");
		 * xq.append("return $b/title "); xq.append("} ");
		 * xq.append("</author> "); xq.append("} "); xq.append("</records> ");
		 * String xquery = xq.toString();
		 */

		/*
		 * declare namespace pb="http://pb.infinite.jp/dtd/powerbase/"; declare
		 * namespace xlink="http://www.w3.org/1999/xlink/"; <records> { for $a
		 * in distinct-values(doc("/aozora/sakuhin")/records/record/author)
		 * return <author value="{ $a }"> { for $b in
		 * doc("/aozora/sakuhin")/records/record where some $ba in $b/author
		 * satisfies $ba = $a return $b/title } </author> } </records>
		 */

		Log.debug("execute xquery via XBird: " + xquery);
		OutputStream out = res.getPrinter();
		InputStream is = null;
		try {
			is = new ByteArrayInputStream(xquery.getBytes("UTF-8"));
			Writer writer = new PrintWriter(out);
			URI uri = new URI(req.getBaseURI() + "/resource");
			invokeQueryPushModeByStAX(is, uri, writer);

		} catch (UnsupportedEncodingException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (XQueryException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (XMLStreamException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (URISyntaxException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		}

	}

	private void invokeQueryPushModeByStAX(InputStream input, URI baseURI, Writer writer) throws XQueryException, XMLStreamException {
		XQueryProcessor proc = new XQueryProcessor();
		XQueryModule module = proc.parse(input, baseURI);
		System.out.println(baseURI.toString());

		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		XMLStreamWriter streamWriter = factory.createXMLStreamWriter(writer);
		XQEventReceiver handler = new StAXSerializer(streamWriter);

		proc.execute(module, handler);
		streamWriter.flush();
	}

	@Override
	public void setStatus() {
		res.setStatus(HttpServletResponse.SC_OK);
	}

}
