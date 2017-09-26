package com.dotoyo.ims.dsform.allin;

import com.dotoyo.dsform.util.StringsUtils;

public class SessionUtil {

	private SessionUtil() {

	}

	public static IFrameSessionDataSave getFrameSessionDataSave()
			throws Exception {
		IFrameSessionDataSave save = null;
		String clsName = FrameBean.getInstance().getConfig("sessionSave");
		if (!StringsUtils.isEmpty(clsName)) {
			clsName = clsName.trim();
			Class<?> newInstance = SessionUtil.class.getClassLoader()
					.loadClass(clsName);

			save = (IFrameSessionDataSave) newInstance.newInstance();

		} else {
			save = new SessionDataSaveBean();
		}
		return save;
	}
}