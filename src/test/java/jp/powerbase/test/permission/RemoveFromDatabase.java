package jp.powerbase.test.permission;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.w3c.dom.Document;

import jp.powerbase.client.CommandInvoker;
import jp.powerbase.client.Connector;
import jp.powerbase.client.User;
import jp.powerbase.client.command.RemovePermissionFromDatabase;
import jp.powerbase.xmldb.resource.Path;

public class RemoveFromDatabase
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

			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			StringWriter sw = new StringWriter();
			XMLStreamWriter writer = factory.createXMLStreamWriter(sw);

			writer.writeStartElement("permission");
			{
				writer.writeStartElement("readable");
				writer.writeCharacters("public");
				writer.writeEndElement();

				writer.writeStartElement("writable");
				writer.writeCharacters("infinite");
				writer.writeEndElement();

			}
			writer.writeEndElement();

			DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docbuilder = dbfactory.newDocumentBuilder();
			Document def = docbuilder.parse(new ByteArrayInputStream(sw.toString().getBytes("UTF-8")));

			CommandInvoker cmd = new RemovePermissionFromDatabase(connector, new Path("/aozora/sakka"), def);

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



