package jp.powerbase.test.request;


import jp.powerbase.PowerBaseException;
import jp.powerbase.basex.Client;
import jp.powerbase.basex.LocalClient;
import jp.powerbase.request.PathParser;
import jp.powerbase.xmldb.resource.Path;
import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class TestPath {
	public static void main(String[] args) {
		Path p1 = new Path("////a/b/c/d/////");
		System.out.println(p1.toString());
	    Assert.assertEquals(p1.toString().equals("/a/b/c/d"), true);

		Path p2 = new Path("////a//b//c//d/////");
		System.out.println(p2.toString());
	    Assert.assertEquals(p2.toString().equals("/a//b//c//d"), true);

		Client client = new LocalClient(System.out);

		PathParser pp1 = new PathParser(new Path("/test/bib//text()"), client);
		try {
			pp1.parse();
		} catch (PowerBaseException e) {
			e.printStackTrace();
		}
		printPathInfo(pp1);
		Assert.assertEquals(pp1.getDirectory().equals("/test"), true);
		Assert.assertEquals(pp1.getDatabaseName().equals("/test/bib"), true);
		Assert.assertEquals(pp1.getXpath().equals("//text()"), true);

		PathParser pp2 = new PathParser(new Path("/test/bib/index/[1]-[10]"), client);
		try {
			pp2.parse();
		} catch (PowerBaseException e) {
			e.printStackTrace();
		}
		printPathInfo(pp2);
		Assert.assertEquals(pp2.getDirectory().equals("/test"), true);
		Assert.assertEquals(pp2.getDatabaseName().equals("/test/bib"), true);
		Assert.assertEquals(pp2.isIndexViewing(), true);
		Assert.assertEquals(pp2.getRangeMin(), 1);
		Assert.assertEquals(pp2.getRangeMax(), 10);

		PathParser pp3 = new PathParser(new Path("/test/bib/2/title[1]/text()"), client);
		try {
			pp3.parse();
		} catch (PowerBaseException e) {
			e.printStackTrace();
		}
		printPathInfo(pp3);
		Assert.assertEquals(pp3.getDirectory().equals("/test"), true);
		Assert.assertEquals(pp3.getDatabaseName().equals("/test/bib"), true);
		Assert.assertEquals(pp3.getNodeID().equals("2"), true);
		Assert.assertEquals(pp3.getXpath().equals("/title[1]/text()"), true);

	}

	public static void printPathInfo(PathParser pp) {
		System.out.println("directory: " + pp.getDirectory());
		System.out.println("database: " + pp.getDatabaseName());
		System.out.println("nodeID: " + pp.getNodeID());
		System.out.println("position: " + pp.getPosition());
		System.out.println("rangeMode: " + pp.isRangeMode());
		System.out.println("rangeMin: " + pp.getRangeMin());
		System.out.println("rangeMax: " + pp.getRangeMax());
		System.out.println("xpath: " + pp.getXpath());
		System.out.println("indexViewing: " + pp.isIndexViewing());
		System.out.println("recursive: " + pp.isRecursive());
	}


}
