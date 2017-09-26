package com.dotoyo.ims.dsform.allin;

import javax.servlet.ServletContextEvent;

import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.dotoyo.dsform.log.LogProxy;

/**
 * 
 * @author Administrator
 * 
 */
public class SpringServiceListener extends
		org.springframework.web.context.ContextLoaderListener {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(SpringServiceListener.class));

	public SpringServiceListener() {
		super();
	}

	public void contextInitialized(ServletContextEvent event) {

		super.contextInitialized(event);
		SpringFrameService.getInstance(null).springContext = WebApplicationContextUtils
				.getWebApplicationContext(event.getServletContext());

		log.debug("contextInitialized--------------------------");
	}

	public void contextDestroyed(ServletContextEvent event) {
		SpringFrameService.getInstance(null).springContext = null;
		super.contextDestroyed(event);

		log.debug("contextDestroyed++++++++++++++++++++++++++");
	}

}
