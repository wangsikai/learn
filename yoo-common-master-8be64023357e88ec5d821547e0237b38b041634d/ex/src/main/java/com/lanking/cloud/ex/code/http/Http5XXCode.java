package com.lanking.cloud.ex.code.http;

public interface Http5XXCode extends HttpStatusCode {

	static int INTERNAL_SERVER_ERROR = 500;
	static int NOT_IMPLEMENTED = 501;
	static int BAD_GATEWAY = 502;
	static int SERVICE_UNAVAILABLE = 503;
	static int GATEWAY_TIMEOUT = 504;
	static int HTTP_VERSION_NOT_SUPPORTED = 505;
}
