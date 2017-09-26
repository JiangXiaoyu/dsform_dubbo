package com.dotoyo.ims.dsform.allin;

import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;


/**
 * RSA加解密
 */
public class RsaUtil {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(RsaUtil.class));

	public static final String KEY_ALGORTHM = "RSA";//

	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

	public static final String PUBLIC_KEY = "RSAPublicKey";

	public static final String PRIVATE_KEY = "RSAPrivateKey";

	public static void main1(String[] args1) throws Exception {

		byte[] plainText = "A".getBytes("UTF8");// 形成RSA公钥对
		System.out.println("\nStart generating RSA key");
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(1024);
		KeyPair key = keyGen.generateKeyPair();
		System.out.println("Finish generating RSA key");// 使用私鈅签名
		Signature sig = Signature.getInstance("SHA1WithRSA");
		sig.initSign(key.getPrivate());
		sig.update(plainText);
		byte[] signature = sig.sign();
		System.out.println(sig.getProvider().getInfo());
		System.out.println("\nSignature:");
		System.out.println(new String(signature, "UTF8"));// 使用公鈅验证
		System.out.println("\nStart signature verification");
		sig.initVerify(key.getPublic());
		sig.update(plainText);
		try {
			if (sig.verify(signature)) {
				System.out.println("Signature verified");
			} else
				System.out.println("Signature failed");
		} catch (SignatureException e) {
			System.out.println("Signature failed");
		}
	}

	public static void main(String[] args1) throws Exception {
		Map<String, Object> map = initKey();
		String privateKey = getPrivateKey(map);
		String publicKey = getPublicKey(map);
		System.out.println(privateKey);
		System.out.println(publicKey);
		FileOutputStream f=new FileOutputStream("a");
		f.write(publicKey.getBytes());
		f.write("\n".getBytes());
		f.write(privateKey.getBytes());
		byte[] data = "3".getBytes("UTF8");
		String sign = RsaUtil.sign(data, privateKey);
		System.out.println(sign);
		boolean ret = verify(data, publicKey, sign);
		System.out.println(ret);
	}

	/**
	 * 
	 * 生成数字签名
	 * 
	 * @param data
	 *            加密数据
	 * 
	 * @param privateKey
	 *            私钥
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */

	public static String sign(byte[] data, String privateKey) throws Exception {

		// 解密私钥

		byte[] keyBytes = Base64Util.decryptBASE64(privateKey);

		// 构造PKCS8EncodedKeySpec对象

		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
				keyBytes);

		// 指定加密算法

		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);

		// 取私钥匙对象

		PrivateKey privateKey2 = keyFactory
				.generatePrivate(pkcs8EncodedKeySpec);

		// 用私钥对信息生成数字签名

		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);

		signature.initSign(privateKey2);

		signature.update(data);

		return Base64Util.encryptBASE64(signature.sign());

	}

	/**
	 * 
	 * 校验数字签名
	 * 
	 * @param data
	 *            加密数据
	 * 
	 * @param publicKey
	 *            公钥
	 * 
	 * @param sign
	 *            数字签名
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */

	public static boolean verify(byte[] data, String publicKey, String sign)
			throws Exception {

		// 解密公钥

		byte[] keyBytes = Base64Util.decryptBASE64(publicKey);

		// 构造X509EncodedKeySpec对象

		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);

		// 指定加密算法

		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);

		// 取公钥匙对象

		PublicKey publicKey2 = keyFactory.generatePublic(x509EncodedKeySpec);

		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);

		signature.initVerify(publicKey2);

		signature.update(data);

		// 验证签名是否正常

		return signature.verify(Base64Util.decryptBASE64(sign));

	}

	public static String getPublicKey(Map<String, Object> keyMap)
			throws Exception {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return Base64Util.encryptBASE64(key.getEncoded());
	}

	public static String getPrivateKey(Map<String, Object> keyMap)
			throws Exception {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return Base64Util.encryptBASE64(key.getEncoded());
	}

	public static Map<String, Object> initKey() throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator
				.getInstance(KEY_ALGORTHM);
		keyPairGenerator.initialize(1024);
		KeyPair keyPair = keyPairGenerator.generateKeyPair(); // 公钥12
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic(); // 私钥14
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, Object> keyMap = new HashMap<String, Object>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}

	public static byte[] encryptByPublicKey(byte[] data, String key)
			throws Exception {

		// 对公钥解密

		byte[] keyBytes = Base64Util.decryptBASE64(key);

		// 取公钥

		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);

		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);

		Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

		// 对数据解密

		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());

		cipher.init(Cipher.ENCRYPT_MODE, publicKey);

		return cipher.doFinal(data);

	}

	public static byte[] decryptByPublicKey(byte[] data, String key)
			throws Exception {

		// 对私钥解密

		byte[] keyBytes = Base64Util.decryptBASE64(key);

		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);

		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);

		Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

		// 对数据解密

		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());

		cipher.init(Cipher.DECRYPT_MODE, publicKey);

		return cipher.doFinal(data);

	}

	public static byte[] encryptByPrivateKey(byte[] data, String key)
			throws Exception {

		// 解密密钥

		byte[] keyBytes = Base64Util.decryptBASE64(key);

		// 取私钥

		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
				keyBytes);

		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);

		Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

		// 对数据加密

		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());

		cipher.init(Cipher.ENCRYPT_MODE, privateKey);

		return cipher.doFinal(data);

	}

	public static byte[] decryptByPrivateKey(byte[] data, String key)
			throws Exception {

		// 对私钥解密

		byte[] keyBytes = Base64Util.decryptBASE64(key);

		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
				keyBytes);

		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);

		Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

		// 对数据解密

		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());

		cipher.init(Cipher.DECRYPT_MODE, privateKey);

		return cipher.doFinal(data);

	}
}
