package jp.powerbase.test.ft;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import jp.powerbase.client.CommandInvoker;
import jp.powerbase.client.Connector;
import jp.powerbase.client.User;
import jp.powerbase.client.command.FullTextSearch;
import jp.powerbase.xmldb.resource.Path;

public class FtAnd
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Path path = new Path("/");
		System.out.println(path.toString());

		try
		{
			User user = new User("admin", "admin");
			URL server = new URL("http://localhost:8080/powerbase");
			Connector connector = new Connector(user, server);

			HashMap<String, String> query = new HashMap<String, String>();
			query.put("ftand", "芥川");

			CommandInvoker cmd = new FullTextSearch(connector, new Path("/aozora"), query);
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
