package com.dotoyo.ims.dsform.allin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.annex.FrameAnnex;
import com.dotoyo.dsform.frame.FrameKeyvalue;
import com.dotoyo.dsform.frame.FrameMessage;
import com.dotoyo.dsform.log.FrameServiceLog;
import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.util.StringsUtils;

/**
 * 框架工厂静态类:最核心的类，绝对不允许重载，重写，或是反编译 框架核心工具类：最核心的类，绝对不允许重载，重写，或是反编译
 * 
 * @author xieshh
 * 
 */
final public class FrameFactory {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameFactory.class));
	private final static LicenceBo bo;
	private static boolean isLogin = false;
	static {
		String fileName = "";
		String xmlStr = "";
		LicenceBo lBo = null;
		try {
			fileName = FrameBean.getInstance().getConfig(
					String.format("%s.fileName", FrameFactory.class.getName()));
			xmlStr = getStrFromFile(fileName);
			lBo = new LicenceBo(xmlStr);
			sign(lBo);
		} catch (Throwable e) {
			if (lBo == null) {
				lBo = new LicenceBo("");
			}
			lBo.setExpiry("");
			// log.error("", e);
		} finally {

			bo = lBo;
		}

	}

	private FrameFactory() {

	}

	/**
	 * 取会话管理模块
	 * 
	 * @return
	 */
	public static IFrameSession getSessionFactory(Map<String, String> param)
			throws Exception {
		if (isLogin) {
			checkLicense();
		}
		try {
			String className = FrameBean.getInstance().getConfig("session");
			IFrameSession ret = (IFrameSession) getInstance(className, param);
			return ret;
		} catch (Throwable e) {
			log.error("", e);
		}
		return BaseSession.getInstance(param);
	}

	/**
	 * 取区域管理模块
	 * 
	 * @return
	 */
	public static IFrameArea getAreaFactory(Map<String, String> param)
			throws Exception {
		if (isLogin) {
			checkLicense();
		}
		try {
			String className = FrameBean.getInstance().getConfig("area");
			IFrameArea ret = (IFrameArea) getInstance(className, param);
			return ret;
		} catch (Throwable e) {
			log.error("", e);
		}
		return FrameArea.getInstance(param);
	}

	/**
	 * 取控制层
	 * 
	 * @return
	 */
	public static IFrameAction getActionFactory(Map<String, String> param)
			throws Exception {
		if (isLogin) {
			checkLicense();
		}
		try {
			String className = FrameBean.getInstance().getConfig("action");
			IFrameAction ret = (IFrameAction) getInstance(className, param);
			return ret;
		} catch (Throwable e) {
			log.error("", e);
		}
		return BaseFrameAction.getInstance(param);
	}

	/**
	 * 取业务管理模块
	 * 
	 * @return
	 */
	public static IFrameService getServiceFactory(Map<String, String> param)
			throws Exception {
//		if (isLogin) {
//			checkLicense();
//		}
		try {
			String className = FrameBean.getInstance().getConfig("service");
			IFrameService ret = (IFrameService) getInstance(className, param);
			return ret;
		} catch (Throwable e) {
			log.error("", e);
		}
		return FrameService.getInstance(param);
	}

	/**
	 * 取业务日志模块
	 * 
	 * @return
	 */
	public static IFrameServiceLog getServiceLogFactory(
			Map<String, String> param) throws Exception {
		if (isLogin) {
			checkLicense();
		}
		try {
			String className = FrameBean.getInstance().getConfig("log");
			IFrameServiceLog ret = (IFrameServiceLog) getInstance(className,
					param);
			return ret;
		} catch (Throwable e) {
			log.error("", e);
		}
		return FrameServiceLog.getInstance(param);
	}

	/**
	 * 取数据源模块
	 * 
	 * @return
	 */
	public static IFrameDatasource getDataSourceFactory(
			Map<String, String> param) throws Exception {
		if (isLogin) {
			checkLicense();
		}
		try {
			String className = FrameBean.getInstance().getConfig("datasource");
			IFrameDatasource ret = (IFrameDatasource) getInstance(className,
					param);
			return ret;
		} catch (Throwable e) {
			log.error("", e);
		}
		return DataSourceForDhcp.getInstance(param);
	}

	/**
	 * 取权限模块
	 * 
	 * @return
	 */
	public static IFrameAuthority getAuthorityFactory(Map<String, String> param)
			throws Exception {
		if (isLogin) {
			checkLicense();
		}
		try {
			String className = FrameBean.getInstance().getConfig("authority");
			IFrameAuthority ret = (IFrameAuthority) getInstance(className,
					param);
			return ret;
		} catch (Throwable e) {
			log.error("", e);
		}
		return FrameAuthority.getInstance(param);
	}

	/**
	 * 取国际化模块
	 * 
	 * @return
	 */
	public static IFrameLanguage getLanguageFactory(Map<String, String> param)
			throws Exception {
		if (isLogin) {
			checkLicense();
		}
		if (param == null) {
			param = new HashMap<String, String>();
			String lang = ChineseLanguage.getInstance().getCode();
			param.put("language", lang);

		}
		return LanguageManager.getInstance().getFrameLanguage(param);
	}

	/**
	 * 取报表原数据模块
	 * 
	 * @return
	 * @throws Exception
	 */
	public static IFrameReport getReportFactory(Map<String, String> param)
			throws Exception {
		if (isLogin) {
			checkLicense();
		}
		try {
			String className = FrameBean.getInstance().getConfig("report");
			IFrameReport ret = (IFrameReport) getInstance(className, param);
			return ret;
		} catch (Throwable e) {
			log.error("", e);
		}
		return BaseReport.getInstance(param);
	}

	/**
	 * 取表单原数据模块
	 * 
	 * @return
	 */
	public static IFrameForm getFormFactory(Map<String, String> param)
			throws Exception {
		if (isLogin) {
			checkLicense();
		}
		try {
			String className = FrameBean.getInstance().getConfig("form");
			IFrameForm ret = (IFrameForm) getInstance(className, param);
			return ret;
		} catch (Throwable e) {
			log.error("", e);
		}
		return BaseForm.getInstance(param);
	}

	/**
	 * 取换肤模块
	 * 
	 * @return
	 */
	public static IFrameSkin getSkinFactory(Map<String, String> param)
			throws Exception {
		if (isLogin) {
			checkLicense();
		}
		if (param == null) {
			param = new HashMap<String, String>();
		}
		try {
			String className = FrameBean.getInstance().getConfig("skin");
			IFrameSkin ret = (IFrameSkin) getInstance(className, param);
			return ret;
		} catch (Throwable e) {
			log.error("", e);
		}
		return FrameHttp5Skin.getInstance(param);
	}

	/**
	 * 取序列号模块工厂
	 * 
	 * @return
	 * @throws FrameException
	 */
	public static IFrameSequence getSequenceFactory(Map<String, String> param)
			throws Exception {
		if (isLogin) {
			checkLicense();
		}
		if (param == null) {
			param = new HashMap<String, String>();
		}
		if ("1".equals(param.get("type"))) {
			FrameException e = new FrameException("3000000000000006", null);
			throw e;
		} else {

			try {
				String className = FrameBean.getInstance().getConfig("seq");
				IFrameSequence ret = (IFrameSequence) getInstance(className,
						param);
				return ret;
			} catch (Throwable e) {
				log.error("", e);
			}
			return BaseSequence.getInstance(param);
		}

	}

	/**
	 * 取HTTP请求模块
	 * 
	 * @return
	 * @throws FrameException
	 */
	public static IFrameHttp getHttpFactory(Map<String, String> param)
			throws Exception {
		if (isLogin) {
			checkLicense();
		}

		if (param == null) {
			param = new HashMap<String, String>();
		}
		try {
			String className = FrameBean.getInstance().getConfig("http");
			IFrameHttp ret = (IFrameHttp) getInstance(className, param);
			return ret;
		} catch (Throwable e) {
			log.error("", e);
		}
		return FrameHttp.getInstance(param);

	}

	/**
	 * 取HTTP请求模块
	 * 
	 * @return
	 * @throws FrameException
	 */
	public static IFrameFtp getFtpFactory(Map<String, String> param)
			throws Exception {
		if (isLogin) {
			checkLicense();
		}
		if (param == null) {
			param = new HashMap<String, String>();
		}
		try {
			String className = FrameBean.getInstance().getConfig("ftp");
			IFrameFtp ret = (IFrameFtp) getInstance(className, param);
			return ret;
		} catch (Throwable e) {
			log.error("", e);
		}
		return FrameFtp.getInstance(param);

	}

	public static IFrameKeyvalue getKeyValueFactory(Map<String, String> param)
			throws Exception {
		if (isLogin) {
			checkLicense();
		}
		if (param == null) {
			param = new HashMap<String, String>();
		}
		try {
			String className = FrameBean.getInstance().getConfig("keyvalue");
			IFrameKeyvalue ret = (IFrameKeyvalue) getInstance(className, param);
			return ret;
		} catch (Throwable e) {
			log.error("", e);
		}
		return FrameKeyvalue.getInstance(param);
	}

	public static IFrameStaticData getStaticDataFactory(
			Map<String, String> param) throws Exception {
		if (isLogin) {
			checkLicense();
		}
		if (param == null) {
			param = new HashMap<String, String>();
		}
		try {
			String className = FrameBean.getInstance().getConfig("staticdata");
			IFrameStaticData ret = (IFrameStaticData) getInstance(className,
					param);
			return ret;
		} catch (Throwable e) {
			log.error("", e);
		}
		return FrameStaticData.getInstance(param);
	}

	/**
	 * 取得附件管理模块工厂
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static IFrameAnnex getAnnexFactory(Map<String, String> param)
			throws Exception {
		if (isLogin) {
			checkLicense();
		}
		if (param == null) {
			param = new HashMap<String, String>();
		}
		try {
			String className = FrameBean.getInstance().getConfig("annex");
			IFrameAnnex ret = (IFrameAnnex) getInstance(className, param);
			return ret;
		} catch (Throwable e) {
			log.error("", e);
		}
		return FrameAnnex.getInstance(param);
	}

	/**
	 * 取得批量导入导出模块工厂
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static IFrameBatchInOut getBatchInOutFactory(
			Map<String, String> param) throws Exception {
		if (isLogin) {
			checkLicense();
		}
		if (param == null) {
			param = new HashMap<String, String>();
		}
		try {
			String className = FrameBean.getInstance().getConfig("batchInOut");
			IFrameBatchInOut ret = (IFrameBatchInOut) getInstance(className,
					param);
			return ret;
		} catch (Throwable e) {
			log.error("", e);
		}
		return FrameBatchInOut.getInstance(param);
	}

	/**
	 * 取得邮件模块工厂
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static IFrameMail getMailFactory(Map<String, String> param)
			throws Exception {
		if (isLogin) {
			checkLicense();
		}
		if (param == null) {
			param = new HashMap<String, String>();
		}
		try {
			String className = FrameBean.getInstance().getConfig("mail");
			IFrameMail ret = (IFrameMail) getInstance(className, param);
			return ret;
		} catch (Throwable e) {
			log.error("", e);
		}
		return FrameMail.getInstance(param);
	}

	/**
	 * 取得报文模块工厂
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static IFrameMessage getMessageFactory(Map<String, String> param)
			throws Exception {
		if (isLogin) {
			checkLicense();
		}
		if (param == null) {
			param = new HashMap<String, String>();
		}
		try {
			String className = FrameBean.getInstance().getConfig("msg");
			IFrameMessage ret = (IFrameMessage) getInstance(className, param);
			return ret;
		} catch (Throwable e) {
			log.error("", e);
		}
		return FrameMessage.getInstance(param);
	}

	/**
	 * 取得消息推送模块工厂
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static IFrameMsgpush getMsgPushFactory(Map<String, String> param)
			throws Exception {
		if (isLogin) {
			checkLicense();
		}
		if (param == null) {
			param = new HashMap<String, String>();
		}
		try {
			String className = FrameBean.getInstance().getConfig("msgpush");
			IFrameMsgpush ret = (IFrameMsgpush) getInstance(className, param);
			return ret;
		} catch (Throwable e) {
			log.error("", e);
		}
		return FrameMsgpush.getInstance(param);
	}

	/**
	 * 取得缓存模块工厂
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static IFrameCache getCacheFactory(Map<String, String> param)
			throws Exception {
		if (isLogin) {
			checkLicense();
		}
		if (param == null) {
			param = new HashMap<String, String>();
		}
		try {
			String className = FrameBean.getInstance().getConfig("cache");
			IFrameCache ret = (IFrameCache) getInstance(className, param);

			return ret;
		} catch (Throwable e) {
			log.error("", e);
		}
		IFrameCache ret = FrameCache.getInstance(param);

		return ret;

	}

	/**
	 * 取得缓存模块工厂
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static IFrameTask getTaskFactory(Map<String, String> param)
			throws Exception {
		if (isLogin) {
			checkLicense();
		}
		if (param == null) {
			param = new HashMap<String, String>();
		}
		try {
			String className = FrameBean.getInstance().getConfig("task");
			IFrameTask ret = (IFrameTask) getInstance(className, param);

			return ret;
		} catch (Throwable e) {
			log.error("", e);
		}
		IFrameTask ret = FrameTask.getInstance(param);

		return ret;

	}

	/**
	 * 取得安全模块的工厂
	 * 
	 * @param param
	 * @return
	 */
	public static IFrameSecurity getSecurityFactory(Map<String, String> param)
			throws Exception {
		if (isLogin) {
			checkLicense();
		}
		if (param == null) {
			param = new HashMap<String, String>();
		}
		try {
			String className = FrameBean.getInstance().getConfig("security");
			IFrameSecurity ret = (IFrameSecurity) getInstance(className, param);
			return ret;
		} catch (Throwable e) {
			log.error("", e);
		}
		return FrameSecurity.getInstance(param);
	}

	/**
	 * 取得系统监控模块的工厂
	 * 
	 * @param param
	 * @return
	 */
	public static IFrameSystemMonitor getSystemMonitorFactory(
			Map<String, String> param) throws Exception {
		if (isLogin) {
			checkLicense();
		}
		if (param == null) {
			param = new HashMap<String, String>();
		}
		try {
			String className = FrameBean.getInstance().getConfig(
					"systemMonitor");
			IFrameSystemMonitor ret = (IFrameSystemMonitor) getInstance(
					className, param);
			return ret;
		} catch (Throwable e) {
			log.error("", e);
			return null;
		}
	}

	/**
	 * 取得公共组件模块的工厂
	 * 
	 * @param param
	 * @return
	 */
	public static IFramePublishComponent getPublishComponentFactory(
			Map<String, String> param) throws Exception {
		if (isLogin) {
			checkLicense();
		}
		if (param == null) {
			param = new HashMap<String, String>();
		}
		try {
			String className = FrameBean.getInstance().getConfig(
					"publishComponent");
			IFramePublishComponent ret = (IFramePublishComponent) getInstance(
					className, param);
			return ret;
		} catch (Throwable e) {
			log.error("", e);
		}
		return FramePublishComponent.getInstance(param);
	}

	/**
	 * 取得心跳模块的工厂
	 * 
	 * @param param
	 * @return
	 */
	public static IFrameHeartbeat getHeartbeatFactory(Map<String, String> param)
			throws Exception {
		if (isLogin) {
			checkLicense();
		}
		if (param == null) {
			param = new HashMap<String, String>();
		}
		try {
			String className = FrameBean.getInstance().getConfig("heartbeat");
			IFrameHeartbeat ret = (IFrameHeartbeat) getInstance(className,
					param);
			return ret;
		} catch (Throwable e) {
			log.error("", e);
		}
		return FrameHeartbeat.getInstance(param);
	}

	/**
	 * 取得中心模块的工厂
	 * 
	 * @param param
	 * @return
	 */
	public static IFrameCenter getCenterFactory(Map<String, String> param)
			throws Exception {
		if (isLogin) {
			checkLicense();
		}
		if (param == null) {
			param = new HashMap<String, String>();
		}
		try {
			String className = FrameBean.getInstance().getConfig("center");
			IFrameCenter ret = (IFrameCenter) getInstance(className, param);
			return ret;
		} catch (Throwable e) {
			log.error("", e);
		}
		return FrameCenter.getInstance(param);
	}

	/**
	 * 取得MONGODB模块的工厂
	 * 
	 * @param param
	 * @return
	 */
	public static IFrameMongodb getMongodbFactory(Map<String, String> param)
			throws Exception {
		if (isLogin) {
			checkLicense();
		}
		if (param == null) {
			param = new HashMap<String, String>();
		}
		
		String className = FrameBean.getInstance().getConfig("mongodb");
		IFrameMongodb ret = (IFrameMongodb) getInstance(className, param);
		return ret;
		
	}

	/**
	 * 取线程池服务模块
	 * 
	 * @return
	 */
	public static IFrameThread getThreadFactory(Map<String, String> param)
			throws Exception {
		if (isLogin) {
			checkLicense();
		}
	
		String className = FrameBean.getInstance().getConfig("thread");
		IFrameThread ret = (IFrameThread) getInstance(className, param);
		return ret;
		
	}
	
	/**
	 * 在决定使用框架了，调用这个方法[系统初始化]
	 * 
	 * @throws Exception
	 */
	public static void startup() throws Exception {
		Method[] methods = FrameFactory.class.getDeclaredMethods();

		for (Method method : methods) {
			String name = method.getName();
			if (name.endsWith("Factory")) {
				// 资源启动
				try {
					IFrame frame = (IFrame) ClassUtils.invokeStaticMethod(
							method, new Class[] { Map.class },
							new Object[] { new HashMap<String, String>() });
					if (frame != null) {
						frame.startup();
					}
				} catch (Throwable e) {
					// log.error("", e);
				}
			}
		}

	}

	/**
	 * 在决定不再使用框架了，调用这个方法[热部署]
	 * 
	 * @throws Exception
	 */
	public static void shutdown() throws Exception {
		Method[] methods = FrameFactory.class.getDeclaredMethods();

		for (Method method : methods) {
			if (method.getName().endsWith("Factory")) {
				// 资源回收处理
				try {
					IFrame frame = (IFrame) ClassUtils.invokeStaticMethod(
							method, new Class[] { Map.class },
							new Object[] { new HashMap<String, String>() });
					frame.shutdown();
				} catch (Throwable e) {
					// log.error("", e);
				}
			}
		}
		IndexManager.getInstance().closeResource();
	}

	private static Object getInstance(String className,
			Map<String, String> param) throws Exception {
		if (StringsUtils.isEmpty(className)) {
			log.error("模块实现类配置为空");
			return null;
		}
		if (param == null) {
			param = new HashMap<String, String>();
		}
		try {
			Class<?> ownerClass = Class.forName(className);

			Method method = ownerClass.getDeclaredMethod("getInstance",
					new Class[] { Map.class });
			return method.invoke(null, new Object[] { param });
		} catch (InvocationTargetException e) {

			throw new Exception(e.getTargetException());

		}

	}

	/** *************************************************** */
	/**
	 * 检查许可
	 * 
	 * @throws Exception
	 */
	public static void checkLicense() throws Exception {
		if (FrameFactory.class.getName()
				.equals("com.dotoyo.frame.FrameFactory")) {
			return;
		}
		String expiry = bo.getExpiry();
		if (StringsUtils.isEmpty(expiry)) {
			// 3000000000000021=您的软件使用许可不合法
			FrameException e = new FrameException("3000000000000021",
					new HashMap<String, String>());
			throw e;
		}
		check();

		java.util.Date expiryDate = strToDate(expiry);

		if (System.currentTimeMillis() - expiryDate.getTime() >= 0) {
			// 3000000000000018=您的软件使用许可已过期
			FrameException e = new FrameException("3000000000000018",
					new HashMap<String, String>());
			throw e;
		}
	}

	/**
	 * 检查许可
	 * 
	 * @throws Exception
	 */
	private static void check() throws Exception {

		IFrameLicenseCheck check = null;
		if ("1".equals(bo.getType())) {
			check = new FrameLicenseCheckHost();
		} else if ("2".equals(bo.getType())) {
			check = new FrameLicenseCheckIp();
		} else if ("3".equals(bo.getType())) {
			check = new FrameLicenseCheckMac();
		} else {
			// 3000000000000022=检查类型不合法
			FrameException e = new FrameException("3000000000000022",
					new HashMap<String, String>());
			throw e;
		}
		if (check != null) {
			check.check(bo);
		}

	}

	/**
	 * 更新许可
	 * 
	 * @param xmlStr
	 * @throws Exception
	 */
	public static void updateLicense(String xmlStr) throws Exception {

		LicenceBo lBo = new LicenceBo(xmlStr);
		if (lBo.getName().equals(bo.getName())) {
			if (lBo.getMac().equals(bo.getMac())) {
				sign(lBo);
				bo.setExpiry(lBo.getExpiry());
				bo.setSign(lBo.getSign());
				bo.setType(lBo.getType());
				bo.setVersion(lBo.getVersion());
			}
		}
	}

	private static void sign(LicenceBo lBo) throws Exception {
		String pubKey = FrameBean.getInstance().getConfig("pubKey");
		byte[] data = lBo.toString().getBytes();
		String sign = lBo.getSign();
		boolean ret = RsaUtil.verify(data, pubKey, sign);
		if (!ret) {
			// 3000000000000019=您的软件使用许可签名失败
			FrameException e = new FrameException("3000000000000019",
					new HashMap<String, String>());
			throw e;
		}
	}

	public static java.util.Date strToDate(String timeStr) throws Exception {
		if (StringsUtils.isEmpty(timeStr)) {
			return new java.util.Date(System.currentTimeMillis());
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		java.util.Date date = formatter.parse(timeStr);
		return date;
	}

	/** *************************** 工具代码 ************* */
	/*
	 * 获得xml编辑器的内容
	 */
	public static String getStrFromFile(String fileName) throws Exception {
		StringBuilder result = new StringBuilder();
		InputStream is = null;
		BufferedReader fr = null;
		try {
			is = getSecurityFactory(null).getClassLoader().getResourceAsStream(
					fileName);

			if (is == null) {
				File file = new File(fileName);
				if (file.exists()) {
					is = new FileInputStream(file);
				} else {
					// 3000000000000025=根据路径${path}找不到配置文件
					Map<String, String> m = new HashMap<String, String>();
					m.put("path", fileName);
					FrameException e = new FrameException("3000000000000025", m);
					throw e;
				}
			}

			fr = new BufferedReader(new InputStreamReader(is));
			String s = "";
			while ((s = fr.readLine()) != null) {
				result.append(s);
			}
		} finally {
			if (fr != null) {
				try {
					fr.close();
				} catch (Exception e) {

				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {

				}
			}
		}

		return result.toString();
	}

	/*
	 * 获得xml编辑器的内容
	 */
	public static String getCurrentSecond() throws Exception {
		Date nowTime = new Date();
		SimpleDateFormat matter = new SimpleDateFormat("ss");
		String time = matter.format(nowTime);
		return time;
	}

	/**
	 * 取主机名称
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getHostName() throws Exception {
		InetAddress a;

		a = InetAddress.getLocalHost();
		String hostName = a.getHostName();

		return hostName;
	}

	/**
	 * 取主机IP
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getIp() throws Exception {
		InetAddress a;

		a = InetAddress.getLocalHost();
		String ip = a.getHostAddress();

		return ip;
	}

	/**
	 * 取应用产品类型
	 * 
	 * @return
	 * @throws Exception
	 */
	public static char getApplyProductType() throws Exception {
		String clusterId = FrameConfig.getInstance().getConfig("clusterId");
		if (StringsUtils.isEmpty(clusterId)) {
			return '0';
		}
		if (clusterId.length() != 8) {
			return '0';
		}
		char c = clusterId.charAt(4);
		return c;
	}

	public static String getBackupUrl() {
		try {
			String backupUrl = FrameConfig.getInstance().getConfig("backupUrl");
			if (StringsUtils.isEmpty(backupUrl)) {
				return "";
			}

			return backupUrl;
		} catch (Throwable e) {
			log.error("", e);

		}
		return "";
	}

	public static String getTaskWorking() {
		try {
			String taskWorking = FrameConfig.getInstance().getConfig(
					"taskWorking");
			if (StringsUtils.isEmpty(taskWorking)) {
				return "";
			}

			return taskWorking;
		} catch (Throwable e) {
			log.error("", e);

		}
		return "";
	}

	public static void responseJsonSuccess4Proxy(HttpServletResponse response,
			String str) throws Exception {
		// response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			pw.print(str);
			pw.flush();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		try {
			FrameFactory.startup();

			// MqBo bo = new MqBo();
			// bo.setQueueName("hello");
			// factory.send(bytes, bo);

		} finally {
			FrameFactory.shutdown();
		}

	}

	public static void test(String[] args) throws Exception {

		try {
			FrameFactory.startup();
			IFrameStaticData factory = getStaticDataFactory(null);
			String module = "frame";
			String code = "busAddress";
			String busAddress = factory.getStaticData(module, code);
			log.debug(busAddress);
			// factory.postJson(url, head, json);
			// log.error(request);
		} finally {
			FrameFactory.shutdown();
		}

	}

	/**
	 * @deprecated
	 * @param args
	 * @throws Exception
	 */
	public static void testSession(String[] args) throws Exception {

		try {
			FrameFactory.startup();
			IFrameSession factory = getSessionFactory(null);
			String sessionId = "ASDF7AS7DF8A7SDF87AS8DF7A8SDF7";
			HttpSession session = factory.getHttpSession(sessionId);
			if (session != null) {
				session.invalidate();
			}
		} finally {
			FrameFactory.shutdown();
		}

	}
}
