package com.lanking.cloud.sdk.util;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
public class RSACoder {
	public static final String KEY_ALGORTHM = "RSA";

	/**
	 * BASE64解密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptBASE64(String key) throws Exception {
		return (new BASE64Decoder()).decodeBuffer(key);
	}

	/**
	 * BASE64加密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptBASE64(byte[] key) throws Exception {
		return (new BASE64Encoder()).encodeBuffer(key);
	}

	/**
	 * 初始化密钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> initKey(int keyLength) throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORTHM);
		keyPairGenerator.initialize(keyLength < 512 ? 512 : keyLength);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();

		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

		Map<String, String> keyMap = new HashMap<String, String>(2);
		keyMap.put("publicKey", RSACoder.encryptBASE64(publicKey.getEncoded()));
		keyMap.put("privateKey", RSACoder.encryptBASE64(privateKey.getEncoded()));
		return keyMap;
	}

	/**
	 * 用私钥加密
	 * 
	 * @param data
	 *            加密数据
	 * @param key
	 *            密钥
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception {
		byte[] keyBytes = decryptBASE64(key);
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
		Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);

		return cipher.doFinal(data);
	}

	/**
	 * 用私钥解密 * @param data 加密数据
	 * 
	 * @param key
	 *            密钥
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
		byte[] keyBytes = decryptBASE64(key);
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
		Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

		// 对数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);

		return cipher.doFinal(data);
	}

	/**
	 * 用公钥加密
	 * 
	 * @param data
	 *            加密数据
	 * @param key
	 *            密钥
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {
		byte[] keyBytes = decryptBASE64(key);
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
		Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

		// 对数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);

		return cipher.doFinal(data);
	}

	/**
	 * 用公钥解密
	 * 
	 * @param data
	 *            加密数据
	 * @param key
	 *            密钥
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] data, String key) throws Exception {
		byte[] keyBytes = decryptBASE64(key);
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
		Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

		// 对数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);

		return cipher.doFinal(data);
	}

	/**
	 * 将二进制转换成16进值制 ，防止byte[]数字转换成string类型时造成的数据损失
	 * 
	 * @param buf
	 * @return 返回16进制转换成的string
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();

	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 *            16进制的数组转换成String类型再传过来的参数
	 * @return 转换回来的二进制数组
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}
}
