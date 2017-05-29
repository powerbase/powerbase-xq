/*
 * @(#)$Id: Version.java 1178 2011-07-22 10:16:56Z hirai $
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
package jp.powerbase;

public final class Version {
	private int majorVersion = 1;
	private int minorVersion = 0;
	private int revision = 0;
	private int build = 0;

	public final String PERIOD = ".";

	public Version() {

	}

	public Version(int majorVersion, int minorVersion, int revision, int build) {
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.revision = revision;
		this.build = build;
	}

	public Version(Version version) {
		this(version.getMajorVersion(), version.getMinorVersion(), version.getRevision(), version.getBuild());
	}

	public Version(String version) throws PowerBaseException {
		if (!version.matches("^[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+$")) {
			throw new PowerBaseException(PowerBaseError.Code.INVALID_VERSION_FORMAT);
		}

		String[] v = version.split(PERIOD);
		this.majorVersion = Integer.valueOf(v[0]);
		this.minorVersion = Integer.valueOf(v[1]);
		this.revision = Integer.valueOf(v[2]);
		this.build = Integer.valueOf(v[3]);
	}

	/**
	 * majorVersion
	 *
	 * @return majorVersion
	 */
	public int getMajorVersion() {
		return majorVersion;
	}

	/**
	 * minorVersion
	 *
	 * @return minorVersion
	 */
	public int getMinorVersion() {
		return minorVersion;
	}

	/**
	 * revision
	 *
	 * @return revision
	 */
	public int getRevision() {
		return revision;
	}

	/**
	 * build
	 *
	 * @return build
	 */
	public int getBuild() {
		return build;
	}

	public String toString() {
		StringBuffer version = new StringBuffer();
		version.append(majorVersion);
		version.append(PERIOD);
		version.append(minorVersion);
		version.append(PERIOD);
		version.append(revision);
		version.append(PERIOD);
		version.append(build);
		return version.toString();
	}

}
