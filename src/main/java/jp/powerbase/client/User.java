/*
 * @(#)$Id: User.java 1087 2011-05-25 05:28:29Z hirai $
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
package jp.powerbase.client;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.httpclient.HttpException;
import org.xml.sax.SAXException;

public class User implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1843590691523920482L;

	private String userid;
	private String passwd;

	public User(String userid, String passwd) throws NoSuchAlgorithmException, HttpException, IOException, ParserConfigurationException, SAXException, XPathExpressionException {
		this.userid = userid;
		this.passwd = passwd;

	}

	public String getUserid() {
		return userid;
	}

	public String getPasswd() {
		return passwd;
	}

	public String getAuthenticationHeader() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return WSSE.getHeader(userid, passwd);
	}
}
