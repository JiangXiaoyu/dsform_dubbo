
package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

import com.dotoyo.dsform.util.StringsUtils;

public class FrameException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code = "";
	private Map<String, String> map = new HashMap<String, String>();

	public FrameException(String code, Map<String, String> map) {
		super(toMsg(code, map));
		this.code = code;
		this.map = map;
	}

	public String getCode() {
		return code;
	}

	public Map<String, String> getMap() {
		return map;
	}

	private static String toMsg(String code, Map<String, String> map) {
		String ret = "";
		if (StringsUtils.isEmpty(code)) {
			return ret;
		}
		IFrameLanguage langF = null;
		try {
			langF=FrameFactory.getLanguageFactory(null);
			if (map == null) {
				map = new HashMap<String, String>();
			}
			String msg = langF.getWords(code, map);
			if (!StringsUtils.isEmpty(msg)) {
				return msg;
			}
		} catch (Throwable e) {
			ret = e.getMessage();
		}
		return ret;
	}
}
