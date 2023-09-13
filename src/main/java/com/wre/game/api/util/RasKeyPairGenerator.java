package com.wre.game.api.util;

import cn.hutool.core.io.FileUtil;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.File;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RasKeyPairGenerator {

	/***
	 * 生成密钥对
	 *
	 * @param keyLength
	 * @return
	 * @throws Exception
	 */
	public static KeyPair genKeyPair(int keyLength) throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(keyLength);
		return keyPairGenerator.generateKeyPair();
	}

	/***
	 * 公钥加密
	 *
	 * @param content
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] content, PublicKey publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(content);
	}

	/***
	 * 私钥解密
	 *
	 * @param content
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] content, PrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(content);
	}

	/***
	 * 将base64编码后的公钥字符串转成PublicKey实例
	 *
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String publicKey) throws Exception {
		byte[] keyBytes = Base64.getDecoder().decode(publicKey.getBytes());
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePublic(keySpec);
	}

	/***
	 * 将base64编码后的私钥字符串转成PrivateKey实例
	 *
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String privateKey) throws Exception {
		byte[] keyBytes = Base64.getDecoder().decode(privateKey.getBytes());
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePrivate(keySpec);
	}

	/***
	 * 将base64编码后的公钥字符串转成PublicKey实例 由n和e获取公钥
	 *
	 * @param modulusStr
	 * @param exponentStr
	 * @return
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String modulusStr, String exponentStr) throws Exception {
		BigInteger modulus = new BigInteger(modulusStr);
		BigInteger exponent = new BigInteger(exponentStr);
		RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, exponent);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePublic(publicKeySpec);
	}

	/***
	 * 将base64编码后的私钥字符串转成PrivateKey实例 由n和d获取私钥
	 *
	 * @param modulusStr
	 * @param exponentStr
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String modulusStr, String exponentStr) throws Exception {
		BigInteger modulus = new BigInteger(modulusStr);
		BigInteger exponent = new BigInteger(exponentStr);
		RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(modulus, exponent);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePrivate(privateKeySpec);
	}

	/***
	 * 从文件中读取公钥私钥
	 *
	 * @param filePath
	 * @return
	 */
	public static String readKey(String filePath) {
		File file = FileUtil.file(filePath);
		return file.getName();
	}

	/***
	 * encode:编码
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptBASE64(byte[] key) throws Exception {
		return (new BASE64Encoder()).encodeBuffer(key);
	}

	/***
	 * decode:解码
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptBASE64(String key) throws Exception {
		return (new BASE64Decoder()).decodeBuffer(key);
	}
}