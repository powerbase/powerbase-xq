/*
 * @(#)$Id: CreateOrUpdateFile.java 1178 2011-07-22 10:16:56Z hirai $
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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import jp.powerbase.PowerBaseError;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Request;
import jp.powerbase.Response;
import jp.powerbase.basex.Client;
import jp.powerbase.file.Diff;
import jp.powerbase.file.DiffFactory;
import jp.powerbase.file.UploadFile;
import jp.powerbase.response.XMLResponse;
import jp.powerbase.util.IOUtil;
import jp.powerbase.util.StringUtil;
import jp.powerbase.xmldb.resource.FileDatabase;
import jp.powerbase.xmldb.resource.NodeId;

public class CreateOrUpdateFile extends AbstractProcessor {

	public CreateOrUpdateFile(Request req, Response res) throws PowerBaseException {
		super(req, res);
	}

	/*
	 * (非 Javadoc)
	 *
	 * @see jp.infinite.xmldb.process.AbstractProcessor#process()
	 */
	@Override
	public void process() throws PowerBaseException {
		UploadFile file = req.getUploadFile();
		Map<String, String> meta = req.getFileMeta();
		Client session = req.getClient();
		String name = Integer.toString(req.getDatabase().getId());
		session.execute(Client.Command.OPEN, name);
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		StringWriter sw = new StringWriter();
		XMLStreamWriter writer = null;
		String currentRev = "";
		String id = req.getNodeID();
		try {
			writer = factory.createXMLStreamWriter(sw);
			currentRev = "1";
			String hash = file.getHash();
			String c = session.executeXQuery("fn:count(/files/file[@hash='" + hash + "'])");
			if (!c.equals("0")) {
				throw new PowerBaseException(PowerBaseError.Code.SAME_FILE_ALREADY_EXIST);
			}

			if (StringUtil.isEmpty(id)) {

				writer.writeStartElement("file");
				writer.writeAttribute("name", file.getOrginalName());
				writer.writeAttribute("type", file.getType());
				writer.writeAttribute("hash", hash);
				writer.writeAttribute("rev", currentRev);
				writer.writeAttribute("size", Long.toString(file.getQuantity()));
				{
					writer.writeStartElement("metainfo");
					for (Iterator it = meta.entrySet().iterator(); it.hasNext();) {
						Map.Entry entry = (Map.Entry) it.next();
						writer.writeStartElement((String) entry.getKey());
						writer.writeCharacters((String) entry.getValue());
						writer.writeEndElement();
					}
					writer.writeEndElement();

					writer.writeStartElement("log");
					{
						writer.writeStartElement("create");
						writer.writeAttribute("time", Long.toString(System.currentTimeMillis()));
						writer.writeAttribute("user", req.getUser().getName());
						writer.writeAttribute("rev", currentRev);
						writer.writeAttribute("hash", hash);
						writer.writeAttribute("size", Long.toString(file.getQuantity()));
						writer.writeEndElement();
					}
					writer.writeEndElement();
				}
				writer.writeEndElement();
				StringBuffer xquery = new StringBuffer();
				xquery.append("insert node ");
				xquery.append(sw.toString());
				xquery.append(" into /files");
				session.execute(Client.Command.XQUERY, xquery.toString());
				id = session.executeXQuery("db:node-id(/files/file[@hash='" + hash + "'])");
				FileDatabase.save(req.getDatabase(), Integer.valueOf(id), 1, new FileInputStream(file.getPhisicalName()));
			} else {
				NodeId nodeId = new NodeId(req.getDatabase().getPath().toString(), Integer.valueOf(id));
				String prevRev = session.executeXQuery(nodeId.toString() + "/fn:string(@rev)");
				String contentType = session.executeXQuery(nodeId.toString() + "/@type");
				currentRev = Integer.toString(Integer.valueOf(prevRev) + 1);
				if (StringUtil.isEmpty(prevRev)) {
					throw new PowerBaseException(PowerBaseError.Code.INVALID_LOCATION_PATH);
				}
				InputStream current = FileDatabase.load(req.getDatabase(), Integer.valueOf(id), Integer.valueOf(prevRev), contentType);
				Random rnd = new Random();
				int r = rnd.nextInt(Integer.MAX_VALUE);
				String prefix = Long.toString(System.currentTimeMillis()) + Integer.toString(r);
				File currentFile = File.createTempFile(prefix, ".tmp");
				IOUtil.copy(current, currentFile);
				Diff differ = new DiffFactory(contentType).getInstance();
				InputStream diff = differ.exec(currentFile, new File(file.getPhisicalName()));
				FileDatabase.save(req.getDatabase(), Integer.valueOf(id), Integer.valueOf(currentRev), diff);
				currentFile.delete();
				StringBuffer xquery = new StringBuffer();
				// size
				xquery.append("replace value of node ");
				xquery.append(nodeId.toString() + "/@size with '");
				xquery.append(file.getQuantity());
				xquery.append("', ");
				// rev
				xquery.append("replace value of node ");
				xquery.append(nodeId.toString() + "/@rev with '");
				xquery.append(currentRev);
				xquery.append("', ");
				// hash
				xquery.append("replace value of node ");
				xquery.append(nodeId.toString() + "/@hash with '");
				xquery.append(file.getHash());
				xquery.append("', ");

				writer.writeStartElement("update");
				writer.writeAttribute("time", Long.toString(System.currentTimeMillis()));
				writer.writeAttribute("user", req.getUser().getName());
				writer.writeAttribute("rev", currentRev);
				writer.writeAttribute("hash", file.getHash());
				writer.writeAttribute("size", Long.toString(file.getQuantity()));
				String comment = meta.get("comment");
				if (!StringUtil.isEmpty(comment)) {
					writer.writeCharacters(comment);
				}
				writer.writeEndElement();

				xquery.append("insert node ");
				xquery.append(sw.toString());
				xquery.append(" as last into ");
				xquery.append(nodeId.toString() + "/log");

				try {
					session.execute(Client.Command.XQUERY, xquery.toString());
					session.execute(Client.Command.OPTIMIZE);
				} catch (Exception e) {
					FileDatabase.delete(req.getDatabase(), Integer.valueOf(id), Integer.valueOf(currentRev));
					throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
				}
			}

		} catch (XMLStreamException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		} catch (IOException e) {
			throw new PowerBaseException(PowerBaseError.Code.INTERNAL_PROCESS_ERROR, e);
		}

		XMLResponse xres = new XMLResponse(res);
		xres.write(res.getPrinter());

	}

	@Override
	public void setStatus() {
		res.setStatus(HttpServletResponse.SC_CREATED);
	}

}
