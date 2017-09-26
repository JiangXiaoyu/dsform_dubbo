package com.dotoyo.ims.dsform.allin;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5JAVA {
	private static String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8","9", "A", "B", "C", "d", "E", "F" };
	/**
	 * 加密方法
	 * @param origin
	 * @param encodingType
	 * @return
	 */
	public static String MD5Encode(String origin, String encodingType) {
		byte[] md5Bytes = toMd5(origin, encodingType);
		return byteArrayToHexString(md5Bytes);
	}
	
	
	public synchronized static final byte[] toMd5(String data,String encodingType) {
		MessageDigest digest = null;
		if (digest == null) {
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException nsae) {
				System.err.println("Failed to load the MD5 MessageDigest. ");
				nsae.printStackTrace();
			}
		}
		if (data == null) {
			return null;
		}
		try {
			//最重要的是这句,需要加上编码类型
			digest.update(data.getBytes(encodingType));
		} catch (UnsupportedEncodingException e) {
			digest.update(data.getBytes());
		}
		return digest.digest();
	}

	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}
	public static void main(String[] args) {
	    System.out.println(MD5JAVA.toMd5("888888","UTF-8"));
	    System.out.println(MD5JAVA.MD5Encode("888888","UTF-8"));
    }
	public static String passwd4md5(String text) throws Exception {
		

		StringBuffer buf = new StringBuffer();
		buf.append(MD5JAVA.MD5Encode(text,"UTF-8"));

		return buf.toString();

	}
}