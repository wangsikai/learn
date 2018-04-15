package com.lanking.cloud.ex.code.http;

public interface Http3XXCode extends HttpStatusCode {
	static int MULTIPLE_CHOICES = 300;
	static int MOVED_PERMANENTLY = 301;
	static int FOUND = 302;
	static int SEE_OTHER = 303;
	static int NOT_MODIFIED = 304;
	static int USE_PROXY = 305;
	static int SWITCH_PROXY = 306;
	static int TEMPORARY_REDIRECT = 307;
}
