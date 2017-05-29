/*
 * @(#)$Id: Log.java 1093 2011-05-25 06:08:28Z hirai $
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

import org.apache.commons.logging.LogFactory;

public class Log {
	private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(Log.class);

	public static void debug(Object arg0) {
		if (isDebugEnabled()) {
			StackTraceElement e = new Exception().getStackTrace()[1];
			LOG.debug(e + " - " + arg0);
		}
	}

	public static void debug(Object arg0, Throwable arg1) {
		if (isDebugEnabled()) {
			StackTraceElement e = new Exception().getStackTrace()[1];
			LOG.debug(e + " - " + arg0, arg1);
		}
	}

	public static void error(Object arg0) {
		if (LOG.isErrorEnabled()) {
			StackTraceElement e = new Exception().getStackTrace()[1];
			LOG.error(e + " - " + arg0);
		}
	}

	public static void error(Object arg0, Throwable arg1) {
		if (LOG.isErrorEnabled()) {
			StackTraceElement e = new Exception().getStackTrace()[1];
			LOG.error(e + " - " + arg0, arg1);
		}
	}

	public static void fatal(Object arg0) {
		if (LOG.isFatalEnabled()) {
			StackTraceElement e = new Exception().getStackTrace()[1];
			LOG.fatal(e + " - " + arg0);
		}
	}

	public static void fatal(Object arg0, Throwable arg1) {
		if (LOG.isFatalEnabled()) {
			StackTraceElement e = new Exception().getStackTrace()[1];
			LOG.fatal(e + " - " + arg0, arg1);
		}
	}

	public static void info(Object arg0) {
		if (LOG.isInfoEnabled()) {
			StackTraceElement e = new Exception().getStackTrace()[1];
			LOG.info(e + " - " + arg0);
		}
	}

	public static void info(Object arg0, Throwable arg1) {
		if (LOG.isInfoEnabled()) {
			StackTraceElement e = new Exception().getStackTrace()[1];
			LOG.info(e + " - " + arg0, arg1);
		}
	}

	public static boolean isDebugEnabled() {
		return LOG.isDebugEnabled();
	}

	public static boolean isErrorEnabled() {
		return LOG.isErrorEnabled();
	}

	public static boolean isFatalEnabled() {
		return LOG.isFatalEnabled();
	}

	public static boolean isInfoEnabled() {
		return LOG.isInfoEnabled();
	}

	public static boolean isTraceEnabled() {
		return LOG.isTraceEnabled();
	}

	public static boolean isWarnEnabled() {
		return LOG.isWarnEnabled();
	}

	public static void trace(Object arg0) {
		if (LOG.isTraceEnabled()) {
			StackTraceElement e = new Exception().getStackTrace()[1];
			LOG.trace(e + " - " + arg0);
		}
	}

	public static void trace(Object arg0, Throwable arg1) {
		if (LOG.isTraceEnabled()) {
			StackTraceElement e = new Exception().getStackTrace()[1];
			LOG.trace(e + " - " + arg0, arg1);
		}
	}

	public static void warn(Object arg0) {
		if (LOG.isWarnEnabled()) {
			StackTraceElement e = new Exception().getStackTrace()[1];
			LOG.warn(e + " - " + arg0);
		}
	}

	public static void warn(Object arg0, Throwable arg1) {
		if (LOG.isWarnEnabled()) {
			StackTraceElement e = new Exception().getStackTrace()[1];
			LOG.warn(e + " - " + arg0, arg1);
		}
	}

}
