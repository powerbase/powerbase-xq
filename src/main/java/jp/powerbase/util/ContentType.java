/*
 * @(#)$Id: ContentType.java 1121 2011-06-13 10:39:57Z hirai $
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
package jp.powerbase.util;

import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContentType {
	private static Map<String, String> extendMap = new HashMap<String, String>();
	private static ArrayList<String> textType = new ArrayList<String>();
	private static String defaultType = "application/octet-stream";

	static {
		extendMap.put("323", "text/h323");
		extendMap.put("acx", "application/internet-property-stream");
		extendMap.put("ai", "application/postscript");
		extendMap.put("aif", "audio/x-aiff");
		extendMap.put("aifc", "audio/x-aiff");
		extendMap.put("aiff", "audio/x-aiff");
		extendMap.put("asf", "video/x-ms-asf");
		extendMap.put("asr", "video/x-ms-asf");
		extendMap.put("asx", "video/x-ms-asf");
		extendMap.put("au", "audio/basic");
		extendMap.put("avi", "video/x-msvideo");
		extendMap.put("axs", "application/olescript");
		extendMap.put("bas", "text/plain");
		extendMap.put("bcpio", "application/x-bcpio");
		extendMap.put("bin", "application/octet-stream");
		extendMap.put("bmp", "image/bmp");
		extendMap.put("c", "text/plain");
		extendMap.put("cat", "application/vnd.ms-pkiseccat");
		extendMap.put("cdf", "application/x-cdf");
		extendMap.put("cer", "application/x-x509-ca-cert");
		extendMap.put("class", "application/octet-stream");
		extendMap.put("clp", "application/x-msclip");
		extendMap.put("cmx", "image/x-cmx");
		extendMap.put("cod", "image/cis-cod");
		extendMap.put("cpio", "application/x-cpio");
		extendMap.put("crd", "application/x-mscardfile");
		extendMap.put("crl", "application/pkix-crl");
		extendMap.put("crt", "application/x-x509-ca-cert");
		extendMap.put("csh", "application/x-csh");
		extendMap.put("css", "text/css");
		extendMap.put("dcr", "application/x-director");
		extendMap.put("der", "application/x-x509-ca-cert");
		extendMap.put("dir", "application/x-director");
		extendMap.put("dll", "application/x-msdownload");
		extendMap.put("dms", "application/octet-stream");
		extendMap.put("doc", "application/msword");
		extendMap.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		extendMap.put("dot", "application/msword");
		extendMap.put("dvi", "application/x-dvi");
		extendMap.put("dxr", "application/x-director");
		extendMap.put("eps", "application/postscript");
		extendMap.put("etx", "text/x-setext");
		extendMap.put("evy", "application/envoy");
		extendMap.put("exe", "application/octet-stream");
		extendMap.put("fif", "application/fractals");
		extendMap.put("flr", "x-world/x-vrml");
		extendMap.put("gif", "image/gif");
		extendMap.put("gtar", "application/x-gtar");
		extendMap.put("gz", "application/x-gzip");
		extendMap.put("h", "text/plain");
		extendMap.put("hdf", "application/x-hdf");
		extendMap.put("hlp", "application/winhlp");
		extendMap.put("hqx", "application/mac-binhex40");
		extendMap.put("hta", "application/hta");
		extendMap.put("htc", "text/x-component");
		extendMap.put("htm", "text/html");
		extendMap.put("html", "text/html");
		extendMap.put("htt", "text/webviewhtml");
		extendMap.put("ico", "image/x-icon");
		extendMap.put("ief", "image/ief");
		extendMap.put("iii", "application/x-iphone");
		extendMap.put("ins", "application/x-internet-signup");
		extendMap.put("isp", "application/x-internet-signup");
		extendMap.put("jfif", "image/pipeg");
		extendMap.put("jpe", "image/jpeg");
		extendMap.put("jpeg", "image/jpeg");
		extendMap.put("jpg", "image/jpeg");
		extendMap.put("js", "application/x-javascript");
		extendMap.put("latex", "application/x-latex");
		extendMap.put("lha", "application/octet-stream");
		extendMap.put("lsf", "video/x-la-asf");
		extendMap.put("lsx", "video/x-la-asf");
		extendMap.put("lzh", "application/octet-stream");
		extendMap.put("m13", "application/x-msmediaview");
		extendMap.put("m14", "application/x-msmediaview");
		extendMap.put("m3u", "audio/x-mpegurl");
		extendMap.put("man", "application/x-troff-man");
		extendMap.put("mdb", "application/x-msaccess");
		extendMap.put("me", "application/x-troff-me");
		extendMap.put("mht", "message/rfc822");
		extendMap.put("mhtml", "message/rfc822");
		extendMap.put("mid", "audio/mid");
		extendMap.put("mny", "application/x-msmoney");
		extendMap.put("mov", "video/quicktime");
		extendMap.put("movie", "video/x-sgi-movie");
		extendMap.put("mp2", "video/mpeg");
		extendMap.put("mp3", "audio/mpeg");
		extendMap.put("mpa", "video/mpeg");
		extendMap.put("mpe", "video/mpeg");
		extendMap.put("mpeg", "video/mpeg");
		extendMap.put("mpg", "video/mpeg");
		extendMap.put("mpp", "application/vnd.ms-project");
		extendMap.put("mpv2", "video/mpeg");
		extendMap.put("ms", "application/x-troff-ms");
		extendMap.put("mvb", "application/x-msmediaview");
		extendMap.put("nws", "message/rfc822");
		extendMap.put("oda", "application/oda");
		extendMap.put("p10", "application/pkcs10");
		extendMap.put("p12", "application/x-pkcs12");
		extendMap.put("p7b", "application/x-pkcs7-certificates");
		extendMap.put("p7c", "application/x-pkcs7-mime");
		extendMap.put("p7m", "application/x-pkcs7-mime");
		extendMap.put("p7r", "application/x-pkcs7-certreqresp");
		extendMap.put("p7s", "application/x-pkcs7-signature");
		extendMap.put("pbm", "image/x-portable-bitmap");
		extendMap.put("pdf", "application/pdf");
		extendMap.put("pfx", "application/x-pkcs12");
		extendMap.put("pgm", "image/x-portable-graymap");
		extendMap.put("pko", "application/ynd.ms-pkipko");
		extendMap.put("pma", "application/x-perfmon");
		extendMap.put("pmc", "application/x-perfmon");
		extendMap.put("pml", "application/x-perfmon");
		extendMap.put("pmr", "application/x-perfmon");
		extendMap.put("pmw", "application/x-perfmon");
		extendMap.put("pnm", "image/x-portable-anymap");
		extendMap.put("pot,", "application/vnd.ms-powerpoint");
		extendMap.put("ppm", "image/x-portable-pixmap");
		extendMap.put("pps", "application/vnd.ms-powerpoint");
		extendMap.put("ppt", "application/vnd.ms-powerpoint");
		extendMap.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
		extendMap.put("prf", "application/pics-rules");
		extendMap.put("ps", "application/postscript");
		extendMap.put("pub", "application/x-mspublisher");
		extendMap.put("qt", "video/quicktime");
		extendMap.put("ra", "audio/x-pn-realaudio");
		extendMap.put("ram", "audio/x-pn-realaudio");
		extendMap.put("ras", "image/x-cmu-raster");
		extendMap.put("rgb", "image/x-rgb");
		extendMap.put("rmi", "audio/mid");
		extendMap.put("roff", "application/x-troff");
		extendMap.put("rtf", "application/rtf");
		extendMap.put("rtx", "text/richtext");
		extendMap.put("scd", "application/x-msschedule");
		extendMap.put("sct", "text/scriptlet");
		extendMap.put("setpay", "application/set-payment-initiation");
		extendMap.put("setreg", "application/set-registration-initiation");
		extendMap.put("sh", "application/x-sh");
		extendMap.put("shar", "application/x-shar");
		extendMap.put("sit", "application/x-stuffit");
		extendMap.put("snd", "audio/basic");
		extendMap.put("spc", "application/x-pkcs7-certificates");
		extendMap.put("spl", "application/futuresplash");
		extendMap.put("src", "application/x-wais-source");
		extendMap.put("sst", "application/vnd.ms-pkicertstore");
		extendMap.put("stl", "application/vnd.ms-pkistl");
		extendMap.put("stm", "text/html");
		extendMap.put("svg", "image/svg+xml");
		extendMap.put("sv4cpio", "application/x-sv4cpio");
		extendMap.put("sv4crc", "application/x-sv4crc");
		extendMap.put("swf", "application/x-shockwave-flash");
		extendMap.put("t", "application/x-troff");
		extendMap.put("tar", "application/x-tar");
		extendMap.put("tcl", "application/x-tcl");
		extendMap.put("tex", "application/x-tex");
		extendMap.put("texi", "application/x-texinfo");
		extendMap.put("texinfo", "application/x-texinfo");
		extendMap.put("tgz", "application/x-compressed");
		extendMap.put("tif", "image/tiff");
		extendMap.put("tiff", "image/tiff");
		extendMap.put("tr", "application/x-troff");
		extendMap.put("trm", "application/x-msterminal");
		extendMap.put("tsv", "text/tab-separated-values");
		extendMap.put("txt", "text/plain");
		extendMap.put("uls", "text/iuls");
		extendMap.put("ustar", "application/x-ustar");
		extendMap.put("vcf", "text/x-vcard");
		extendMap.put("vrml", "x-world/x-vrml");
		extendMap.put("wav", "audio/x-wav");
		extendMap.put("wcm", "application/vnd.ms-works");
		extendMap.put("wdb", "application/vnd.ms-works");
		extendMap.put("wks", "application/vnd.ms-works");
		extendMap.put("wmf", "application/x-msmetafile");
		extendMap.put("wps", "application/vnd.ms-works");
		extendMap.put("wri", "application/x-mswrite");
		extendMap.put("wrl", "x-world/x-vrml");
		extendMap.put("wrz", "x-world/x-vrml");
		extendMap.put("xaf", "x-world/x-vrml");
		extendMap.put("xbm", "image/x-xbitmap");
		extendMap.put("xla", "application/vnd.ms-excel");
		extendMap.put("xlc", "application/vnd.ms-excel");
		extendMap.put("xlm", "application/vnd.ms-excel");
		extendMap.put("xls", "application/vnd.ms-excel");
		extendMap.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		extendMap.put("xlt", "application/vnd.ms-excel");
		extendMap.put("xlw", "application/vnd.ms-excel");
		extendMap.put("xof", "x-world/x-vrml");
		extendMap.put("xpm", "image/x-xpixmap");
		extendMap.put("xwd", "image/x-xwindowdump");
		extendMap.put("z", "application/x-compress");
		extendMap.put("zip", "application/zip");

		textType.add("application/xhtml+xml");
		textType.add("application/xml");
	}

	public static String get(String fileName) {
		String t1 = URLConnection.guessContentTypeFromName(fileName);
		if (t1 == null) {
			String suffix = FileUtil.getSuffix(fileName);
			String t2 = extendMap.get(suffix);
			if (t2 != null && !t2.equals("")) {
				return t2;
			} else {
				return defaultType;
			}
		} else {
			return t1;
		}
	}

	public static boolean isText(String contentType) {
		String[] type = contentType.split("/");
		if (type[0].equalsIgnoreCase("text")) {
			return true;
		} else {
			if (textType.indexOf(contentType) != -1) {
				return true;
			} else {
				return false;
			}
		}

	}

}
