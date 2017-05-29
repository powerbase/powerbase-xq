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
import jp.powerbase.client.command.ReplaceValueOfNode;
import jp.powerbase.client.expr.ReplaceValueOfNodeExpr;
import jp.powerbase.xmldb.resource.Path;

public class ReplaceValueOfNodeMulti
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

			ReplaceValueOfNodeExpr expr = new ReplaceValueOfNodeExpr(new Path("/test/bib"), 2, "year", "2888");
			ReplaceValueOfNodeExpr expr2 = new ReplaceValueOfNodeExpr(new Path("/test/bib"), 15, "year", "3888");
			List<ReplaceValueOfNodeExpr> exprs = new ArrayList<ReplaceValueOfNodeExpr>();
			exprs.add(expr);
			exprs.add(expr2);
			CommandInvoker cmd = new ReplaceValueOfNode(connector, exprs);
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
