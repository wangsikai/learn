package com.lanking.uxb.service.user.util;

import com.lanking.cloud.sdk.util.Codecs;

public class SecurityUtils {

	private static final String ACCOUNT_KEY = "zcvWuMxLtx48Ikm?g_GurPm8Z%!B^'N+[hyD#[tQ{$oBGEDPFfqkkTkYvDUfY";

	public static String accountActiveSecret(String email, long timestamp) {
		return Codecs.md5Hex((Codecs.md5Hex(ACCOUNT_KEY.getBytes()) + Codecs.md5Hex(email.getBytes()) + Codecs
				.md5Hex(String.valueOf(timestamp))).getBytes());
	}

	public static String emailFindPwdSecret(String email, long timestamp) {
		return Codecs.md5Hex((Codecs.md5Hex(ACCOUNT_KEY.getBytes()) + Codecs.md5Hex(email.getBytes()) + Codecs
				.md5Hex(String.valueOf(timestamp))).getBytes());
	}

}
