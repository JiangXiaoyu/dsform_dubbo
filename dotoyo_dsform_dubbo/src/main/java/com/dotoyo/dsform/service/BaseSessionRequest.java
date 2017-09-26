package com.dotoyo.dsform.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.util.StringsUtils;
import com.dotoyo.ims.dsform.allin.FrameFactory;
import com.dotoyo.ims.dsform.allin.IFrameAuthority;
import com.dotoyo.ims.dsform.allin.IFrameCache;
import com.dotoyo.ims.dsform.allin.IFrameLog;
import com.dotoyo.ims.dsform.allin.IFrameSession;
import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.ims.dsform.allin.SessionData;


/**
 * 当有会话请求时，将调用这个方法处理会话信息
 * 
 * @author wangl
 * 
 */
public class BaseSessionRequest extends AbstractSessionRequest {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(BaseSessionRequest.class));

	private final static long ACCESS_TIME = 3 * 1000;// 访问间隔

	public void doRequest(HttpServletRequest request, HttpSession session)
			throws Exception {
		log.debug("BaseSessionRequestSessionHandleTask.doRequest");
		// 用户code直接为空，不做任何处理
		IFrameAuthority sf = FrameFactory.getAuthorityFactory(null);
		String userCode = sf.getLoginUserCode(null);
		if (StringsUtils.isEmpty(userCode)) {
			return;
		}

		// 在会话中存储用户上次更新的时间
		long lastAccessUpdateTime = (Long) session
				.getAttribute("LAST_ACCESS_UPDATE_TIME");
		long now = System.currentTimeMillis();
		long time = now - lastAccessUpdateTime;
		if (time > ACCESS_TIME) {
			IFrameCache frameCache = FrameFactory.getCacheFactory(null);

			SessionData sessionData = (SessionData) frameCache.getCache(
					IFrameSession.SESSION_DATA, session.getId());
			time = now - sessionData.getLastAccessTime();
			if (null != sessionData && (time > ACCESS_TIME)) {

				// 更新sessionData
				sessionData.setLastAccessTime(now);
				// TODO如果第一次更新失败，则保存一次，如果再失败，不处理
				frameCache.addCache(IFrameSession.SESSION_DATA, session.getId(),
						sessionData, 1 * 60 * 60);
				// 刷新会话的更新时间
				session.setAttribute("LAST_ACCESS_UPDATE_TIME", now);
			}
		}
	}
}
