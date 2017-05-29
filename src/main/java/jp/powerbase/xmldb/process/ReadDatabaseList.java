/*
 * @(#)$Id: ReadDatabaseList.java 1178 2011-07-22 10:16:56Z hirai $
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

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import jp.powerbase.Group;
import jp.powerbase.PowerBaseException;
import jp.powerbase.Request;
import jp.powerbase.Response;
import jp.powerbase.User;
import jp.powerbase.basex.Client;
import jp.powerbase.constant.CommonUseNameSpaces;
import jp.powerbase.response.ResponseTag;
import jp.powerbase.xmldb.resource.Directory;
import jp.powerbase.xquery.expr.Brace;

public class ReadDatabaseList extends AbstractProcessor {
	private Client client;
	private Directory directory;

	private ArrayList<String> role = new ArrayList<String>();

	public ReadDatabaseList(Request req, Response res) throws PowerBaseException {
		super(req, res);
		client = req.getClient();
		directory = req.getDirectory();

		User user = req.getUser();
		role.add(user.getName());
		Iterator g = user.getGroups().iterator();
		while (g.hasNext()) {
			Group group = (Group) g.next();
			role.add(group.getName());
		}
	}

	private String getExpr() {
		StringBuffer s = new StringBuffer();

		Iterator r = role.iterator();
		while (r.hasNext()) {
			String u = (String) r.next();
			if (!u.equals(Group.PUBLIC_GROUP)) {
				s.append(" or ");
				s.append("$d/text() = '");
				s.append(u);
				s.append("' \n");
			}
		}

		return s.toString();
	}

	/*
	 * (非 Javadoc)
	 *
	 * @see jp.infinite.xmldb.process.AbstractProcessor#process()
	 */
	@Override
	public void process() throws PowerBaseException {
		int id = 1;
		if (directory != null) {
			id = directory.getId();
		}

		StringBuffer ns = new StringBuffer();
		ns.append("declare namespace");
		ns.append(CommonUseNameSpaces.getNameSpacesAsString(false));
		ns.append("; \n");

		StringBuffer decl = new StringBuffer();
		decl.append("declare function local:isReadable($n as node()) as xs:boolean \n");
		decl.append("{ \n");
		decl.append("  some $d in $n/permission/readable satisfies \n");
		decl.append("  $d/text() = 'public' \n");
		// or more user and groups
		decl.append(getExpr());
		decl.append("}; \n");

		decl.append("declare function local:isWritable($n as node()) as xs:boolean \n");
		decl.append("{ \n");
		decl.append("  some $d in $n/permission/writable satisfies \n");
		decl.append("  $d/text() = 'public' \n");
		// or more user and groups
		decl.append(getExpr());
		decl.append("}; \n");

		decl.append("declare function local:getPermission($n as node()) as xs:string \n");
		decl.append("{ \n");
		decl.append("if ($n/@owner = '" + req.getUser().getName() + "') then 'r,w' \n");
		decl.append("else \n");
		decl.append("  if (local:isReadable($n) and local:isWritable($n)) then 'r,w' \n");
		decl.append("  else \n");
		decl.append("    if (local:isReadable($n)) then 'r' \n");
		decl.append("    else \n");
		decl.append("      if (local:isWritable($n)) then 'w' \n");
		decl.append("      else '' \n");
		decl.append("}; \n");

		decl.append("declare function local:isVisible($n as node()) as xs:boolean \n");
		decl.append("{\n");
		decl.append("$n/@owner = '" + req.getUser().getName() + "'\n");
		decl.append("  or \n");
		decl.append("(some $d in $n/permission/visible\n");
		decl.append("satisfies \n");
		decl.append("  $d/text() = 'public'\n");
		// or more user and groups
		decl.append(getExpr());
		decl.append(")}; \n");
		decl.append("declare function local:scan($n as node()) as node()? \n");
		decl.append("{ \n");
		decl.append("typeswitch($n) \n");
		decl.append("  case $dir as element(directory) \n");
		decl.append("    return \n");
		decl.append("    if (local:isVisible($dir)) then \n");
		decl.append("      element directory { for $c in ($dir/@name, $dir/@path, $dir/@description, $dir/@owner, $dir/*) order by $c/@name return local:scan($c) } \n");
		decl.append("    else () \n");
		decl.append("  case $db as element(database) \n");
		decl.append("    return element database { $db/@name , $db/@type , $db/@path, $db/@description, $db/@owner, attribute permission {local:getPermission($db)}} \n");
		decl.append("  case $p as element(permission) \n");
		decl.append("    return () \n");
		decl.append("  default return $n \n");
		decl.append("}; \n");

		StringBuffer q = new StringBuffer();
		if (id == 1) {
			q.append("<root path=\"/\"> \n");
			q.append("{ \n");
		}
		q.append("  for $d in db:open-id(\"databases\", " + id + ")");
		if (id == 1) {
			q.append("/directory\n");
		}

		q.append(" order by $d/@name \n");
		q.append(" return local:scan($d) \n");
		if (id == 1) {
			q.append("} \n");
			q.append("</root> \n");
		}

		String query = "";
		if (req.isWrapingResponse()) {
			ResponseTag rt = new ResponseTag(req.getRootTag());
			query = rt.wrap(new Brace().wrap(q.toString()));
		} else {
			query = q.toString();
		}

		System.out.println(ns.toString() + decl.toString() + query);
		client.execute(Client.Command.XQUERY, ns.toString() + decl.toString() + query);

	}

	@Override
	public void setStatus() {
		res.setStatus(HttpServletResponse.SC_OK);
	}

}
