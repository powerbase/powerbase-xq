/*
 * @(#)$Id: StopWatch.java 1121 2011-06-13 10:39:57Z hirai $
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

/**
 * StopWatch provides a API for timings.
 *
 * @author Makoto YUI (yuin405+xbird@gmail.com)
 */
public final class StopWatch {

	private final String label;
	private long begin = 0;
	private long end = 0;

	public StopWatch() {
		this(null);
	}

	public StopWatch(String label) {
		this.label = label;
		start();
	}

	public void start() {
		begin = System.currentTimeMillis();
	}

	public long stop() {
		end = System.currentTimeMillis();
		return end - begin;
	}

	public void suspend() {
		end = System.currentTimeMillis();
	}

	public void resume() {
		begin += (System.currentTimeMillis() - end);
	}

	public void reset() {
		begin = 0;
		end = 0;
	}

	public long elapsed() {
		if (end != 0) {
			return end - begin;
		} else {
			return System.currentTimeMillis() - begin;
		}
	}

	@Override
	public String toString() {
		final StringBuilder buf = new StringBuilder();
		if (label != null) {
			buf.append(label + ": ");
		}
		long t = elapsed();
		if (t == 0) {
			buf.append("0ms");
			return buf.toString();
		}
		long hour = t / 3600000;
		if (hour > 0) {
			buf.append(hour + "h ");
			t = t % 3600000;
		}
		long min = t / 60000;
		if (min > 0) {
			buf.append(min + "m ");
			t = t % 60000;
		}
		long sec = t / 1000;
		if (sec > 0) {
			buf.append(sec + "s ");
			t = t % 1000;
		}
		if (t > 0) {
			buf.append(t + "ms");
		}
		return buf.toString();
	}

	public static String elapsedTime(long t) {
		if (t == 0) {
			return "0ms";
		}
		final StringBuilder buf = new StringBuilder();
		long hour = t / 3600000;
		if (hour > 0) {
			buf.append(hour + "h ");
			t = t % 3600000;
		}
		long min = t / 60000;
		if (min > 0) {
			buf.append(min + "m ");
			t = t % 60000;
		}
		long sec = t / 1000;
		if (sec > 0) {
			buf.append(sec + "s ");
			t = t % 1000;
		}
		if (t > 0) {
			buf.append(t + "ms");
		}
		return buf.toString();
	}
}
