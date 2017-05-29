/*
 * @(#)$Id: FatalError.java 1178 2011-07-22 10:16:56Z hirai $
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

/**
 * PowerBase Error.
 *
 * @author Toshio HIRAI <toshio.hirai@gmail.com>
 */
public class FatalError extends Error {
	private static final long serialVersionUID = -1260509147002749628L;

	/**
	 * Constructor.
	 */
	public FatalError() {
		super();
	}

	/**
	 * Constructor.
	 *
	 * @param message error message
	 */
	public FatalError(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 *
	 * @param cause some error
	 */
	public FatalError(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 *
	 * @param message error message
	 * @param cause some error
	 */
	public FatalError(String message, Throwable cause) {
		super(message, cause);
	}

}
