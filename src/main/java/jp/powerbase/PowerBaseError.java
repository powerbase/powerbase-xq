package jp.powerbase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import jp.powerbase.constant.HttpResponseCode;

public class PowerBaseError {







	public enum Code {
		INTERNAL_PROCESS_ERROR(HttpResponseCode.STATUS_500_INTERNAL_SERVER_ERROR),
		BASEX_INVOCATION_FAILED(HttpResponseCode.STATUS_500_INTERNAL_SERVER_ERROR),
		BASEX_SERVER_STOPPED(HttpResponseCode.STATUS_503_SERVICE_UNAVAILABLE),
		INVALID_ARGUMENT(HttpResponseCode.STATUS_400_BAD_REQUEST),
		INVALID_PARAMETER(HttpResponseCode.STATUS_400_BAD_REQUEST),
		INVALID_COMMAND(HttpResponseCode.STATUS_400_BAD_REQUEST),
		INTERRUPTED(HttpResponseCode.STATUS_417_EXPECTATION_FAILED),
		UNSUPPORTED_FUNCTION(HttpResponseCode.STATUS_501_NOT_IMPLEMENTED),
		UNSUPPORTED_COMMAND(HttpResponseCode.STATUS_501_NOT_IMPLEMENTED),
		DATA_DIRECTORY_IS_NOT_SPECIFIED(HttpResponseCode.STATUS_400_BAD_REQUEST),
		DATABASE_CREATE_FAILED(HttpResponseCode.STATUS_500_INTERNAL_SERVER_ERROR),
		DATABASE_DIRECTORY_NOT_SPECIFIED(HttpResponseCode.STATUS_400_BAD_REQUEST),
		DATABASE_DIRECTORY_NOT_FOUND(HttpResponseCode.STATUS_400_BAD_REQUEST),
		IS_NOT_DIRECTORY(HttpResponseCode.STATUS_400_BAD_REQUEST),
		DATABASE_DIRECTORY_IS_NOT_EMPTY(HttpResponseCode.STATUS_400_BAD_REQUEST),
		UNABLE_MAKE_DIRECTORY(HttpResponseCode.STATUS_500_INTERNAL_SERVER_ERROR),
		INVALID_VERSION_FORMAT(HttpResponseCode.STATUS_400_BAD_REQUEST),
		FILE_NOT_FOUND(HttpResponseCode.STATUS_500_INTERNAL_SERVER_ERROR),
		INVALID_REQUEST(HttpResponseCode.STATUS_400_BAD_REQUEST),
		INVALID_LOCATION_PATH(HttpResponseCode.STATUS_404_NOT_FOUND),
		UNKNOWN_COMMAND(HttpResponseCode.STATUS_400_BAD_REQUEST),
		XQUERY_SYNTAX_ERROR(HttpResponseCode.STATUS_400_BAD_REQUEST),
		ACCESS_DENIED(HttpResponseCode.STATUS_403_FORBIDDEN),
		PERMISSION_DENIED(HttpResponseCode.STATUS_401_UNAUTHORIZED),
		DATABASE_NOT_SPECIFIED(HttpResponseCode.STATUS_400_BAD_REQUEST),
		DIRECTORY_NOT_SPECIFIED(HttpResponseCode.STATUS_400_BAD_REQUEST),
		COMMAND_IS_UNMATCH_TO_METHOD(HttpResponseCode.STATUS_400_BAD_REQUEST),
		ENCRYPTION_PROCESS_ERROR(HttpResponseCode.STATUS_500_INTERNAL_SERVER_ERROR),
		AUTHENTICATION_FAILED(HttpResponseCode.STATUS_401_UNAUTHORIZED),
		UNABLE_DELETE_ROOT_DIRECTORY(HttpResponseCode.STATUS_400_BAD_REQUEST),
		USER_ID_NOT_SPECIFIED(HttpResponseCode.STATUS_400_BAD_REQUEST),
		USER_NOT_FOUND(HttpResponseCode.STATUS_400_BAD_REQUEST),
		GROUP_NOT_FOUND(HttpResponseCode.STATUS_400_BAD_REQUEST),
		PASSWORD_IS_EMPTY(HttpResponseCode.STATUS_400_BAD_REQUEST),
		GROUP_ID_NOT_SPECIFIED(HttpResponseCode.STATUS_400_BAD_REQUEST),
		INVALID_GROUP(HttpResponseCode.STATUS_400_BAD_REQUEST),
		GROUP_ID_IS_ALREADY_EXIST(HttpResponseCode.STATUS_400_BAD_REQUEST),
		INVALID_USER(HttpResponseCode.STATUS_400_BAD_REQUEST),
		USER_ID_IS_ALREADY_EXIST(HttpResponseCode.STATUS_400_BAD_REQUEST),
		CAN_NOT_DELETE_PUBLIC_GROUP(HttpResponseCode.STATUS_400_BAD_REQUEST),
		GROUP_STILL_HAS_RELATION(HttpResponseCode.STATUS_400_BAD_REQUEST),
		CAN_NOT_DELETE_ADMIN_USER(HttpResponseCode.STATUS_400_BAD_REQUEST),
		USER_STILL_OWNED_DATABASE(HttpResponseCode.STATUS_400_BAD_REQUEST),
		USER_OR_GROUP_NOT_FOUND(HttpResponseCode.STATUS_400_BAD_REQUEST),
		PARENT_DIRECTORY_NOT_FOUND(HttpResponseCode.STATUS_400_BAD_REQUEST),
		UNABLE_MAKE_DATABASE_ON_ROOT_DIRECTORY(HttpResponseCode.STATUS_400_BAD_REQUEST),
		ILLEGAL_DATABASE_DEFINITION_FORMAT(HttpResponseCode.STATUS_400_BAD_REQUEST),
		DATABASE_ALREADY_EXIST(HttpResponseCode.STATUS_400_BAD_REQUEST),
		DIRECTORY_NAME_NOT_SPECIFIED(HttpResponseCode.STATUS_400_BAD_REQUEST),
		DIRECTORY_ALREADY_EXIST(HttpResponseCode.STATUS_400_BAD_REQUEST),
		DIRECTORY_OR_DATABASE_EXISTS(HttpResponseCode.STATUS_400_BAD_REQUEST),
		SAME_FILE_ALREADY_EXIST(HttpResponseCode.STATUS_400_BAD_REQUEST),
		UPDATING_QUERY_IS_INVALID(HttpResponseCode.STATUS_400_BAD_REQUEST),
		UNABLE_DELETE_ROOT_ELEMENT(HttpResponseCode.STATUS_400_BAD_REQUEST),

		;
		public final int status;

		Code(int status) {
			this.status = status;
		}
	}

	/** Cached message file. */
	private static final HashMap<String, String> MASSAGES = new HashMap<String, String>();

	static {
		String lang = Settings.get(Settings.Symbol.LANG);
		final String path = "/lang/messages" + "." + lang;
		final InputStream is = PowerBaseError.class.getResourceAsStream(path);
		if (is == null) {
			throw new FatalError(path + " not found.");
		} else {
			try {
				final BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String line;
				while ((line = br.readLine()) != null) {
					final int i = line.indexOf('=');
					if (i == -1 || line.startsWith("#"))
						continue;
					final String key = line.substring(0, i);
					String val = line.substring(i + 1);
					if (MASSAGES.get(key) == null) {
						MASSAGES.put(key, val);
					} else {
						throw new FatalError(path + " " + key + " assigned twice.");
					}
				}
				br.close();
			} catch (UnsupportedEncodingException e) {
				throw new FatalError(e);
			} catch (IOException e) {
				throw new FatalError(e);
			}
		}

	}

	public static String getMessage(Code code) {
		return MASSAGES.get(code.toString());
	}

}
