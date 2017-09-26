package com.dotoyo.ims.dsform.allin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

/**
 * 与系统相关的一些常用工具方法.
 * 
 * @author lvbogun
 * @version 1.0.0
 */
public class MacUtil {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(MacUtil.class));

	private static String getMac4Window(String ip) {
		String mac = "";
		Pattern macPattern = Pattern
				.compile("([0-9A-Fa-f]{2})(-[0-9A-Fa-f]{2}){5}");
		Matcher macMatcher;
		BufferedReader br = null;
		try {
			Process p = Runtime.getRuntime().exec("ipconfig /all");

			br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String content = null;
			boolean result = false;

			while ((content = br.readLine()) != null) {
				macMatcher = macPattern.matcher(content);

				macMatcher = macPattern.matcher(content);
				result = macMatcher.find();
				if (result) {
					mac = macMatcher.group(0);
					break;
				}

			}

		} catch (IOException e) {
			log.error("", e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {

				}
			}
		}
		return mac;
	}

	private static String getMac4Unix() {
		String mac = "";
		BufferedReader br = null;
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("/sbin/ifconfig -a");
			br = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line = null;
			int index = -1;
			while ((line = br.readLine()) != null) {
				index = line.toLowerCase().indexOf("hwaddr");
				if (index >= 0) {
					mac = line.substring(index + "hwaddr".length() + 1).trim();
					break;
				}
			}
		} catch (Throwable e) {
			log.error("", e);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {

			}
			br = null;
			process = null;
		}
		return mac;

	}

	private static String osName() {
		String osName = System.getProperty("os.name");
		return osName.toLowerCase();
	}

	public static String getMacAddress() {
		String mac = "";
		try {
			if (osName().startsWith("windows")) {
				InetAddress addre = InetAddress.getLocalHost();
				mac = MacUtil.getMac4Window(addre.getHostAddress());
			} else {
				mac = MacUtil.getMac4Unix();
			}
		} catch (UnknownHostException e) {
			log.error("", e);
		}
		return mac.trim();
	}

	/**
	 * 测试用的main方法.
	 * 
	 * @param argc
	 *            运行参数.
	 * @throws Exception
	 */
	public static void main(String[] argc) throws Exception {

		String mac = getMacAddress();
		System.out.println(mac);

	}

}