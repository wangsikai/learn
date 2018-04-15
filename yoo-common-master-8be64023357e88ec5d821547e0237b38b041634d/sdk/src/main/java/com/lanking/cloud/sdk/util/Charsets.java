package com.lanking.cloud.sdk.util;

import java.nio.charset.Charset;

public final class Charsets {

	public static final String UTF8 = "UTF-8".intern();
	public static final String GBK = "GBK".intern();
	public static final String ISO88591 = "ISO-8859-1".intern();
	public static final Charset CHARSET_UTF8 = Charset.forName(UTF8);
	public static final Charset CHARSET_GBK = Charset.forName(GBK);
	public static final Charset CHARSET_ISO88591 = Charset.forName(ISO88591);

	private Charsets() {
	}
}
