package com.dotoyo.ims.dsform.allin;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.util.StringsUtils;

public class BaseFrameAction extends AbstractFrame implements IFrameAction {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(BaseFrameAction.class));

	protected static BaseFrameAction instance = null;

	protected BaseFrameAction() {

	}

	public static BaseFrameAction getInstance(Map<String, String> param) {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new BaseFrameAction();
		}
	}

	@Override
	public void startup() {
		

	}

	@Override
	public void shutdown() {
		

	}

	public String getActionResultByCode(String action, String method,
			String result) throws Exception {
		ActionConfig cfg = ActionConfig.getInstance();
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) cfg.getActions().get(
				action);
		if (map != null) {
			@SuppressWarnings("unchecked")
			Map<String, String> resultMap = (Map<String, String>) map
					.get(method);
			if (resultMap != null) {
				// 是正确配置，处理配置业务
				resultMap.get(result);
			}
		}
		return "";
	}

	/**
	 * 判断URL是否是业务URL
	 * 
	 * @param curUrl
	 * @return
	 * @throws Exception
	 */
	public boolean isBusiAction(String curUrl) throws Exception {
		if (StringsUtils.isEmpty(curUrl)) {
			return false;
		}
		if (curUrl.startsWith("/")) {
			curUrl = curUrl.substring(1, curUrl.length());
		}
		// user_login
		if (curUrl.indexOf(".") == -1) {
			if (curUrl.indexOf("_") != -1) {
				List<String> list = StringsUtils.split(curUrl, '_');
				if (list.size() == 2) {
					String action = list.get(0);
					//String method = list.get(1);
					// 处理ACTION
					ActionConfig cfg = ActionConfig.getInstance();
					@SuppressWarnings("unchecked")
					Map<String, Object> map = (Map<String, Object>) cfg
							.getActions().get(action);
					log.debug(map);
					if (map != null) {
						if ("busi".equals(map.get("type"))) {
							return true;
						}
					}

				}

			}

		}
		return false;
	}
}
