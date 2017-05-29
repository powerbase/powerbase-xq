package jp.powerbase.test.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import jp.powerbase.client.CommandInvoker;
import jp.powerbase.client.Connector;
import jp.powerbase.client.User;
import jp.powerbase.client.command.CreateFile;
import jp.powerbase.xmldb.resource.Path;

public class AddFileToDB {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			User user = new User("admin", "admin");
			URL server = new URL("http://localhost:8080/powerbase");
			Connector connector = new Connector(user, server);

			File file = new File("C:/Users/hirai/Documents/テスト.xlsx");

			HashMap<String, String> meta = new HashMap<String, String>();
			meta.put("creator", "ヒライトシオ");
			meta.put("organization", "インフィニット・コーポレーション");
			meta.put("date", "2011.6.3");
			CommandInvoker cmd = new CreateFile(connector, new Path("/test/file"), file, meta);
			int sc = cmd.request();
			System.out.println("HTTP status code: " + sc);
			InputStream res = cmd.getResponse();
			BufferedReader reader = new BufferedReader(new InputStreamReader(res, "UTF-8"));
			String s = "";
			while ((s = reader.readLine()) != null) {
				System.out.println(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
