
package com.dotoyo.ims.dsform.allin;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

/**
 * 用于加载最新的包
 * @author xieshh
 * 
 */
public class BtClassLoader extends URLClassLoader {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(BtClassLoader.class));

	private static BtClassLoader instance = null;

	private static final String defaultPaths[] = new String[] {
			"httpclient-4.2.1.jar", "httpcore-4.2.1.jar", "httpmime-4.2.1.jar" };

	private BtClassLoader(URL urls[]) {
		super(urls, Thread.currentThread().getContextClassLoader());
	}

	public static BtClassLoader getInstance() {
		if (instance == null) {
			String paths[] = defaultPaths;
			newInstance(paths);
		}
		return instance;
	}

	synchronized private static void newInstance(String paths[]) {
		if (instance == null) {

			List<URL> pathList = new ArrayList<URL>();
			for (int i = 0; i < paths.length; i++) {
				try {
					String path = paths[i];
					File file = new File(path);
					if (file.exists()) {
						URI uri = file.toURI();
						if (uri != null) {
							URL url = uri.toURL();
							if (url != null) {
								pathList.add(url);
							}
						}

					}
				} catch (Exception e) {
					log.debug("加载目录失败", e);
				}
			}
			URL urls[] = new URL[pathList.size()];
			for (int i = 0; i < urls.length; i++) {
				urls[i] = (URL) pathList.get(i);
			}
			instance = new BtClassLoader(urls);
		}

	}

	public static void main(String[] args) {
		ClassLoader loader=Thread.currentThread().getContextClassLoader();
		try{
		Thread.currentThread().setContextClassLoader(
				BtClassLoader.getInstance());
		//处理业务结束
		
		
		//处理业务结束
		}finally{
			Thread.currentThread().setContextClassLoader(
					loader);
		}
	}
}
