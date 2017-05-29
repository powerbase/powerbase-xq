package jp.powerbase.test.loadxml;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import jp.powerbase.client.CommandInvoker;
import jp.powerbase.client.Connector;
import jp.powerbase.client.User;
import jp.powerbase.client.command.LoadXML;
import jp.powerbase.xmldb.resource.Path;

public class LoadBibXML
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

			File file = new File("C:/workspace/powerbase/data/bib.xml");

			CommandInvoker cmd = new LoadXML(connector, new Path("/test/bib"), file);
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
