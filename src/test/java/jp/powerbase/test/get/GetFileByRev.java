package jp.powerbase.test.get;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import jp.powerbase.client.CommandInvoker;
import jp.powerbase.client.Connector;
import jp.powerbase.client.User;
import jp.powerbase.client.command.GetFile;
import jp.powerbase.util.IOUtil;
import jp.powerbase.xmldb.resource.Path;

public class GetFileByRev {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Path path = new Path("/");
		System.out.println(path.toString());

		try {
			User user = new User("admin", "admin");
			URL server = new URL("http://localhost:8080/powerbase");
			Connector connector = new Connector(user, server);
			int rev = 5;
			CommandInvoker cmd = new GetFile(connector, new Path("/test/file/2"), rev);
			int sc = cmd.request();
			System.out.println("HTTP status code: " + sc);
			InputStream res = cmd.getResponse();
			File out = new File("C:/Users/hirai/result" + Integer.toString(rev) + ".xlsx");
			IOUtil.copy(res, out);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
