package com.lanking.cloud.sdk.util;

import java.io.UnsupportedEncodingException;

import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Longs;
import com.lanking.cloud.ex.core.ServerException;

public final class Codecs {

	private static BaseEncoding BE = BaseEncoding.base64Url().omitPadding();

	public static String md5Hex(byte[] bytes) {
		return Hashing.md5().hashBytes(bytes).toString();
	}

	public static String md5Hex16(byte[] bytes) {
		return Hashing.md5().hashBytes(bytes).toString().substring(8, 24);
	}

	public static String md5Hex(String str) {
		return md5Hex(toBytes(str));
	}

	public static String md5Hex16(String str) {
		return md5Hex16(toBytes(str));
	}

	public static String sha1Hex(byte[] bytes) {
		return Hashing.sha1().hashBytes(bytes).toString();
	}

	public static String sha1Hex(String str) {
		return sha1Hex(toBytes(str));
	}

	public static String encode(byte[] bytes) {
		return BE.encode(bytes);
	}

	public static String encode(long l) {
		return encode(Longs.toByteArray(l));
	}

	public static byte[] decode(String str) {
		return BE.decode(str);
	}

	public static byte[] toBytes(String s) {
		if (s == null) {
			return ArrayUtils.EMPTY_BYTE_ARRAY;
		}
		try {
			return s.getBytes(Charsets.UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new ServerException(e);
		}
	}

	public static String toString(byte[] bytes) {
		try {
			return new String(bytes, Charsets.UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new ServerException(e);
		}
	}

}
