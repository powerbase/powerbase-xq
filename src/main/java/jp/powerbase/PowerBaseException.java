/*
 * @(#)$Id: PowerBaseException.java 1178 2011-07-22 10:16:56Z hirai $
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
/**
 *
 */
package jp.powerbase;

/**
 * PowerBase Exception.
 *
 * @author Toshio HIRAI <toshio.hirai@gmail.com>
 */
public class PowerBaseException extends Exception {

	private static final long serialVersionUID = 6566442771386609183L;
	PowerBaseError.Code code;

	/**
	 * Construter.
	 */
	public PowerBaseException() {
	}

	/**
	 * Construter.
	 *
	 * @param message error message
	 */
	public PowerBaseException(String message) {
		super(message);
	}

	/**
	 * Construter.
	 *
	 * @param cause some error
	 */
	public PowerBaseException(Throwable cause) {
		super(cause);
	}

	/**
	 * Construter.
	 *
	 * @param message error message
	 * @param cause some error
	 */
	public PowerBaseException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Construter.
	 * @param val error code
	 */
	public PowerBaseException(PowerBaseError.Code code) {
		super(code.toString());
		this.code = code;
	}

	/**
	 * Construter.
	 *
	 * @param val error code
	 * @param cause some error
	 */
	public PowerBaseException(PowerBaseError.Code code, Throwable cause) {
		super(code.toString(), cause);
		this.code = code;
	}

	/**
	 * Get error code.
	 *
	 * @return val error code
	 */
	public PowerBaseError.Code getCode() {
		return code;
	}

}
