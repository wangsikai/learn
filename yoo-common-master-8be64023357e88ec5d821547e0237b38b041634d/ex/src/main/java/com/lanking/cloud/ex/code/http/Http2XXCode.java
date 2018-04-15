package com.lanking.cloud.ex.code.http;

public interface Http2XXCode extends HttpStatusCode {

	static int OK = 200;
	static int Created = 201;
	static int Accepted = 202;
	static int NON_AUTHORITATIVE_INFORMATION = 203;
	static int NO_CONTENT = 204;
	static int RESET_CONTENT = 205;
	static int PARTIAL_CONTENT = 206;
	static int MULTI_STATUS = 207;
}
