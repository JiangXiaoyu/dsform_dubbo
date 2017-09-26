package com.dotoyo.dsform.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.util.StringsUtils;
import com.dotoyo.ims.dsform.allin.FrameBean;
import com.dotoyo.ims.dsform.allin.FrameException;
import com.dotoyo.ims.dsform.allin.FrameFactory;
import com.dotoyo.ims.dsform.allin.FrameRequestMonitor;
import com.dotoyo.ims.dsform.allin.IFrameLog;
import com.dotoyo.ims.dsform.allin.IFrameSessionRequest;
import com.dotoyo.ims.dsform.allin.IFrameSystemMonitor;
import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.ims.dsform.allin.WebUtils;

public class BaseRequestListener implements ServletRequestListener {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(BaseRequestListener.class));
	protected static final Map<Long, HttpServletRequest> requests = new ConcurrentHashMap<Long, HttpServletRequest>(
			1024);

	public void requestInitialized(ServletRequestEvent event) {
		HttpServletRequest request = (HttpServletRequest) event
				.getServletRequest();
		try {
			if (WebUtils.isResource(request)) {
				return;
			}
			requests.put(Thread.currentThread().getId(), request);
			try {
				IFrameSystemMonitor moF;
				moF = FrameFactory.getSystemMonitorFactory(null);
				FrameRequestMonitor m = new FrameRequestMonitor(request);
				request.setAttribute("REQUEST_MONITOR", m);
				moF.addMonitor(m);
			} catch (Throwable e) {

			}

			//
			char pType;

			pType = FrameFactory.getApplyProductType();

			switch (pType) {
			case '0': {
				request4Session(event);
				return;
			}
			case '1': {
				request4Session(event);
				return;
			}
			case '2': {
				return;
			}
			case '4': {
				FrameException e = new FrameException("3000000000000006",
						new HashMap<String, String>());
				throw e;

			}
			default: {

				return;
			}
			}

		} catch (Throwable e) {
			log.error("", e);
		}
	}

	private void request4Session(ServletRequestEvent event) {
		HttpServletRequest request = (HttpServletRequest) event
				.getServletRequest();

		try {

			//
			String curUrl = request.getRequestURI();
			HttpSession session = request.getSession(false);
			if (session == null) {
				return;
			}
			if (curUrl.indexOf("/msgpush") != -1) {
				// 消息推送请求不处理
				return;
			} else if (curUrl.indexOf("/heartbeat") != -1) {
				// 心跳请求不处理
				return;

			} else {
				// 处理会话模块
				String clsName = FrameBean.getInstance().getConfig(
						"sessionRequest");
				if (!StringsUtils.isEmpty(clsName)) {
					clsName = clsName.trim();
					Class<?> newInstance = this.getClass().getClassLoader()
							.loadClass(clsName);

					IFrameSessionRequest req = null;
					req = (IFrameSessionRequest) newInstance.newInstance();
					if (req != null) {
						req.doRequest(request, session);
					}
				}
			}

		} catch (Throwable e) {
			log.error("", e);
		}
	}

	public void requestDestroyed(ServletRequestEvent event) {
		HttpServletRequest request = (HttpServletRequest) event
				.getServletRequest();
		try {
			IFrameSystemMonitor moF;
			moF = FrameFactory.getSystemMonitorFactory(null);
			FrameRequestMonitor m = (FrameRequestMonitor) request
					.getAttribute("REQUEST_MONITOR");
			moF.remoteMonitor(m);
		} catch (Throwable e) {

		}
		requests.remove(Thread.currentThread().getId());
	}

	public static HttpServletRequest getServletRequest() {
		HttpServletRequest request = (HttpServletRequest) requests.get(Thread
				.currentThread().getId());
		return request;
	}
}
