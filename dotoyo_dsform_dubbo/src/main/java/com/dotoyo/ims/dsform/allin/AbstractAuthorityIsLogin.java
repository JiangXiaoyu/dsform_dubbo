package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.util.StringsUtils;

/**
 * 
 * @author xieshh
 * 
 */
abstract public class AbstractAuthorityIsLogin implements IAuthorityIsLogin {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(AbstractAuthorityIsLogin.class));
	protected Map<String, String> param = null;

	public AbstractAuthorityIsLogin(Map<String, String> param) {
		if (param == null) {
			this.param = new HashMap<String, String>();
		} else {
			this.param = param;
		}
	}

	public boolean isLogin(String userCode) throws Exception {

		if (!StringsUtils.isEmpty(userCode)) {
			return true;
		} else {
			IFrameSession sf = FrameFactory.getSessionFactory(param);
			HttpSession s = sf.getHttpSession();
			if (s == null) {
				return false;
			} else {

				UserLoginBo uBo = sf.loginUser4back(s.getId());
				if (uBo != null) {

					return true;
				} else {
					return false;
				}

			}
		}

	}
}
