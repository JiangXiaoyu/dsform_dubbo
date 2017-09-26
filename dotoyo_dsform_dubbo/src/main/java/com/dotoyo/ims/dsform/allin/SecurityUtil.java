package com.dotoyo.ims.dsform.allin;

import java.util.List;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.util.StringsUtils;


public class SecurityUtil {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(SecurityUtil.class));

	private SecurityUtil() {

	}

	/**
	 * 判断SQL注入
	 * 
	 * @param str
	 * @param isCookie
	 * @return
	 * @throws Exception
	 */
	public static boolean doSqlValidate(String str, boolean isCookie)
			throws Exception {

		str = str.trim().toLowerCase();
		/**
		 *  modify by wangl 2015.03.17
		 *  暂时允许使用常用的符号  ' * % ; - + ,
		 */
		//String validateSqlKey = "and|'|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|; |or|-|+|,|--";
		
		String validateSqlKey = "and|exec|insert|select|delete|update|count|chr|mid|master|truncate|char|declare|or";
		
		String temp = FrameBean
				.getInstance()
				.getConfig(
						"com.dotoyo.frame.j2ee.filter.SecurityValidateFilter.validateSqlKey");
		if (!StringsUtils.isEmpty(temp)) {
			validateSqlKey = temp.trim().toLowerCase();
		}
		List<String> list = StringsUtils.split(validateSqlKey, '|');
		//|| str.indexOf(is + "%") >= 0 || str.indexOf("%" + is) >= 0
		for (int i = 0; i < list.size(); i++) {
			String is = list.get(i);
			if (str.indexOf(" " + is + " ") >= 0 || str.indexOf(is + " ") >= 0
					|| str.indexOf(" " + is) >= 0 || str.indexOf(is + ";") >= 0
					|| str.indexOf(";" + is) >= 0
					|| str.indexOf(is + "/**/") >= 0
					|| str.indexOf("/**/" + is) >= 0
					) {

				return true;
			}
			if ("--".equals(is) && str.indexOf(is) >= 0 && !isCookie) {

				return true;
			}
		}

		return false;
	}
}
