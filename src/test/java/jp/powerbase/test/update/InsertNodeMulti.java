package jp.powerbase.test.update;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jp.powerbase.client.CommandInvoker;
import jp.powerbase.client.Connector;
import jp.powerbase.client.User;
import jp.powerbase.client.command.InsertNode;
import jp.powerbase.client.expr.InsertNodeExpr;
import jp.powerbase.client.expr.InsertNodeExpr.TargetChoice;
import jp.powerbase.xmldb.resource.Path;

public class InsertNodeMulti
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			User user = new User("admin", "admin");
			URL server = new URL("http://localhost:8080/powerbase");
			Connector connector = new Connector(user, server);

			InsertNodeExpr expr = new InsertNodeExpr(new Path("/test/bib"), 1, TargetChoice.INTO, "<book ins=\"88\" />");
			InsertNodeExpr expr2 = new InsertNodeExpr(new Path("/test/bib"), 1, TargetChoice.INTO, "<book ins=\"99\" />");
			List<InsertNodeExpr> exprs = new ArrayList<InsertNodeExpr>();
			exprs.add(expr);
			exprs.add(expr2);
			CommandInvoker cmd = new InsertNode(connector, exprs);
			int sc = cmd.request();
			System.out.println("HTTP status code: " + sc);
			InputStream res = cmd.getResponse();
			BufferedReader reader = new BufferedReader(new InputStreamReader(res, "UTF-8"));
			String s = "";
			while ((s = reader.readLine()) != null)
			{
				System.out.println(s);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
