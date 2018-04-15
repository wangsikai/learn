package com.lanking.cloud.ex.code.http;

public interface Http4XXCode extends HttpStatusCode {

	static int BAD_REQUEST = 400;
	static int UNAUTHORIZED = 401;
	static int PAYMENT_REQUIRED = 402;
	static int FORBIDDEN = 403;
	static int NOT_FOUND = 404;
	static int METHOD_NOT_ALLOWED = 405;
	static int NOT_ACCEPTABLE = 406;
	static int PROXY_AUTHENTICATION_REQUIRED = 407;
	static int REQUEST_TIMEOUT = 408;
	static int CONFLICT = 409;
	static int GONE = 410;
	static int LENGTH_REQUIRED = 411;
	static int PRECONDITION_FAILED = 412;
	static int REQUEST_ENTITY_TOO_LARGE = 413;
	static int REQUEST_URI_TOO_LONG = 414;
	static int UNSUPPORTED_MEDIA_TYPE = 415;
	static int REQUESTED_RANGE_NOT_SATISFIABLE = 416;
	static int EXPECTATION_FAILED = 417;
	static int I_AM_A_TEAPOT = 418;
	static int THERE_ARE_TOO_MANY_CONNECTIONS_FROM_YOUR_INTERNET_ADDRESS = 421;
	static int UNPROCESSABLE_ENTITY = 422;
	static int LOCKED = 423;
	static int FAILED_DEPENDENCY = 424;
	static int UNORDERED_COLLECTION = 425;
	static int UPGRADE_REQUIRED = 426;
	static int RETRY_WITH = 449;
	static int UNAVAILABLE_FOR_LEGAL_REASONS = 451;
}
