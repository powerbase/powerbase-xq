/*
 * @(#)$Id: HttpResponseCode.java 1093 2011-05-25 06:08:28Z hirai $
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
package jp.powerbase.constant;

/**
 *
 * @author Infinite Corporation
 *
 * RFC2616-Hypertext Transfer Protocol -- HTTP/1.1
 *
	Informational-1xx
	100	Continue
	101	SwitchingProtocols

	Successful-2xx
	200	OK
	201	Created
	202	Accepted
	203	Non-AuthoritativeInformation
	204	NoContent
	205	ResetContent
	206	PartialContent

	Redirection-3xx
	300	MultipleChoices
	301	MovedPermanently
	302	Found
	303	SeeOther
	304	NotModified
	305	UseProxy
	306	(Unused)
	307	TemporaryRedirect

	ClientError-4xx
	400	BadRequest
	401	Unauthorized
	402	PaymentRequired
	403	Forbidden
	404	NotFound
	405	MethodNotAllowed
	406	NotAcceptable
	407	ProxyAuthenticationRequired
	408	RequestTimeout
	409	Conflict
	410	Gone
	411	LengthRequired
	412	PreconditionFailed
	413	RequestEntityTooLarge
	414	Request-URITooLong
	415	UnsupportedMediaType
	416	RequestedRangeNotSatisfiable
	417	ExpectationFailed

	ServerError-5xx
	500	InternalServerError
	501	NotImplemented
	502	BadGateway
	503	ServiceUnavailable
	504	GatewayTimeout
	505	HTTPVersionNotSupported

 *
 */
public final class HttpResponseCode {

	//Informational-1xx
	public static final int STATUS_100_CONTINUE				 		 	= 100;			//100	Continue
	public static final int STATUS_101_SWITCHING_PROTOCOLS		 	 	= 101;			//101	SwitchingProtocols

	//Successful-2xx
	public static final int STATUS_200_OK					 		 	= 200;			//200	OK
	public static final int STATUS_201_CREATED				 		 	= 201;			//201	Created
	public static final int STATUS_202_ACCEPTED				 		 	= 202;			//202	Accepted
	public static final int STATUS_203_NON_AUTHORITATIVE_INFORMATION 	= 203;			//203	Non-AuthoritativeInformation
	public static final int STATUS_204_NO_CONTENT				 	 	= 204;			//204	NoContent
	public static final int STATUS_205_RESET_CONTENT			 	 	= 205;			//205	ResetContent
	public static final int STATUS_206_PARTIAL_CONTENT			 	 	= 206;			//206	PartialContent

	//Redirection-3xx
	public static final int STATUS_300_MULTIPLE_CHOICES			 	 	= 300;			//300	MultipleChoices
	public static final int STATUS_301_MOVED_PERMANENTLY			 	= 301;			//301	MovedPermanently
	public static final int STATUS_302_FOUND				 		 	= 302;			//302	Found
	public static final int STATUS_303_SEEOTHER				 		 	= 303;			//303	SeeOther
	public static final int STATUS_304_NOT_MODIFIED			 		 	= 304;			//304	NotModified
	public static final int STATUS_305_USE_PROXY				 	 	= 305;			//305	UseProxy
																						//306	(Unused)
	public static final int STATUS_307_TEMPORARY_REDIRECT			 	= 307;			//307	TemporaryRedirect

	//ClientError-4xx
	public static final int STATUS_400_BAD_REQUEST			 		 	= 400;			//400	BadRequest
	public static final int STATUS_401_UNAUTHORIZED			 		 	= 401;			//401	Unauthorized
	public static final int STATUS_402_PAYMENT_REQUIRED			 	 	= 402;			//402	PaymentRequired
	public static final int STATUS_403_FORBIDDEN				 	 	= 403;			//403	Forbidden
	public static final int STATUS_404_NOT_FOUND				 	 	= 404;			//404	NotFound
	public static final int STATUS_405_METHOD_NOT_ALLOWED			 	= 405;			//405	MethodNotAllowed
	public static final int STATUS_406_NOT_ACCEPTABLE			 	 	= 406;			//406	NotAcceptable
	public static final int STATUS_407_PROXY_AUTHENTICATION_REQUIRED 	= 407;			//407	ProxyAuthenticationRequired
	public static final int STATUS_408_REQUEST_TIMEOUT			 	 	= 408;			//408	RequestTimeout
	public static final int STATUS_409_CONFLICT				 		 	= 409;			//409	Conflict
	public static final int STATUS_410_GONE				 			 	= 410;			//410	Gone
	public static final int STATUS_411_LENGTH_REQUIRED			 	 	= 411;			//411	LengthRequired
	public static final int STATUS_412_PRECONDITION_FAILED		 	 	= 412;			//412	PreconditionFailed
	public static final int STATUS_413_REQUEST_ENTITY_TOOLARGE		 	= 413;			//413	RequestEntityTooLarge
	public static final int STATUS_414_REQUEST_URI_TOO_LONG		 	 	= 414;			//414	Request-URITooLong
	public static final int STATUS_415_UNSUPPORTED_MEDIA_TYPE		 	= 415;			//415	UnsupportedMediaType
	public static final int STATUS_416_REQUESTED_RANGE_NOT_SATISFIABLE 	= 416;			//416	RequestedRangeNotSatisfiable
	public static final int STATUS_417_EXPECTATION_FAILED			 	= 417;			//417	ExpectationFailed

	//ServerError-5xx
	public static final int STATUS_500_INTERNAL_SERVER_ERROR		 	= 500;			//500	InternalServerError
	public static final int STATUS_501_NOT_IMPLEMENTED			 	 	= 501;			//501	NotImplemented
	public static final int STATUS_502_BAD_GATEWAY			 		 	= 502;			//502	BadGateway
	public static final int STATUS_503_SERVICE_UNAVAILABLE		 	 	= 503;			//503	ServiceUnavailable
	public static final int STATUS_504_GATEWAY_TIMEOUT			 	 	= 504;			//504	GatewayTimeout
	public static final int STATUS_505_HTTP_VERSION_NOT_SUPPORTED	 	= 505;			//505	HTTPVersionNotSupported

}
