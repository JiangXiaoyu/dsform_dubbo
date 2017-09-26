package com.dotoyo.ims.dsform.allin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.util.StringsUtils;


/**
 * 节点编码+时间+变量
 * 
 * @author xieshh
 * 
 */
public class BaseSequence extends AbstractFrame implements IFrameSequence {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(BaseSequence.class));

	protected static BaseSequence instance = null;

	/*
	 * 全局变量
	 */
	private long val = 0;

	protected BaseSequence() {

	}

	public static IFrameSequence getInstance(Map<String, String> param) {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new BaseSequence();

		}
	}

	public void startup() {

	}

	public void shutdown() {

	}

	/*
	 * 通过此方法获取id
	 */
	public String getSequence() throws Exception {

		String clusterId = FrameConfig.getInstance().getConfig("clusterId");
		if (StringsUtils.isNull(clusterId)) {
			FrameException e = new FrameException("3000000000000007", null);
			throw e;
		}
		String str = "";
		synchronized (this) {
			str = clusterId + System.currentTimeMillis() + val;
			val++;
		}

		String result = this.toMD5(str);
		return result;
	}

	private String toMD5(String text) throws Exception {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("MD5 error:", e);
		}
		messageDigest.update(text.getBytes());
		byte by[] = messageDigest.digest();

		return bytesToHexString(by);
	}

	/*
	 * byte数组转为十六进制字符
	 */
	private String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/*
	 * 通过此方法获取id
	 */
	public String getSequence4Number() throws Exception {

		String clusterId = FrameConfig.getInstance().getConfig("clusterId");
		if (StringsUtils.isNull(clusterId)) {
			FrameException e = new FrameException("3000000000000007", null);
			throw e;
		}

		String str = clusterId.substring(2, clusterId.length());
		if (!StringsUtils.isNumber(str)) {
			FrameException e = new FrameException("3000000000000007", null);
			throw e;
		}
		long now = System.currentTimeMillis();
		synchronized (this) {
			str = String.format("%s%d%d", str, now, val);
			val++;
		}

		return str;
	}

	public static void main(String[] args) throws Exception {
		// Map<String, String> param = new HashMap<String, String>();
		// IFrameSequence sf = FrameFactory.getSequenceFactory(param);
		// String id = sf.getSequence();
		// System.out.println(id);

		Thread t1 = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						String str = BaseSequence.getInstance(null)
								.getSequence();
						System.out.println(Thread.currentThread().getName()
								+ ":" + str.length());
						Thread.sleep(200);
					} catch (Exception e) {
						log.error("", e);
					}
				}

			}
		});

		Thread t2 = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						String str = BaseSequence.getInstance(null)
								.getSequence();
						System.out.println(Thread.currentThread().getName()
								+ ":" + str.length());
						Thread.sleep(200);
					} catch (Exception e) {
						log.error("", e);
					}
				}

			}
		});

		Thread t3 = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						String str = BaseSequence.getInstance(null)
								.getSequence();
						System.out.println(Thread.currentThread().getName()
								+ ":" + str.length());
						Thread.sleep(200);
					} catch (Exception e) {
						log.error("", e);
					}
				}

			}
		});

		Thread t4 = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						String str = BaseSequence.getInstance(null)
								.getSequence();
						System.out.println(Thread.currentThread().getName()
								+ ":" + str.length());
						Thread.sleep(200);
					} catch (Exception e) {
						log.error("", e);
					}
				}

			}
		});
		t1.start();
		t2.start();
		t3.start();
		t4.start();

	}
}
