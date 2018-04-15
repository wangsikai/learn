package com.lanking.cloud.ex.code.http;

public interface Http1XXCode extends HttpStatusCode {

	static int Continue = 100;
	static int SWITCHING_PROTOCOLS = 101;
	static int PROCESSING = 102;
}
