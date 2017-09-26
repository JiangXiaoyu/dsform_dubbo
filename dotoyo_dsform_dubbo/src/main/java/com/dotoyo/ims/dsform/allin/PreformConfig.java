package com.dotoyo.ims.dsform.allin;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

/**
 * 文件模版引入的js css 文件的前缀
 * 
 * @author wangl
 * 
 */
public class PreformConfig {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(PreformConfig.class));
	private Properties param = new Properties();
	protected Map<IFrame, String> frameInstance = new Hashtable<IFrame, String>();
	protected static PreformConfig instance = null;
	protected String filePath = "preform/preform.properties";
	
	public final static String DS_PATH = "prefixPath";//资源文件中的key
	public final static String ORG_PATH = "orgPath";//机构项目地址
	public static final String SYS_PATH = "sysPath";//后台项目地址
	public final static String DOMAIN = "domain";//domain
	public final static String PIC_FTP_PATH = "picFtpPath"; //模版图片的ftp服务的前缀
	public final static String ZIP_PATH = "zipPath";//zip打包目录
	public final static String BASE_DIR = "baseDir";//org地址  （暂时没有使用了）
	public final static String VERSION = "version";//version
	
	//开放api安全加密
	public final static String OPEN_API_SECURY_STATUS = "openApiSecury.status";//开放api接口是否启用(1表示启用)
	public final static String OPEN_API_SECURY_MD5SALT = "openApiSecury.md5Salt";//md5加密salt
	public final static String OPEN_API_SECURY_MAXINACTIVEINTERVAL = "openApiSecury.maxInactiveInterval";//请求失效时间,单位分钟,默认120分钟
	
	
	@SuppressWarnings("serial")
	public final static Map<String,String> allowAttr = new HashMap<String,String>(){
		{
			/**
			 *     	map.checkpart = 1;//动火部位
    	
    	map.firestart = 1;//动火作业开始日期
    	map.firestarttime = 1;//开始时
    	map.firestartminute = 1;//开始分
    	
    	map.fireend = 1;//动火作业结束日期
    	map.firesendtime = 1;//结束时
    	map.firesendminute = 1;//结束分
    	
    	map.firepart = 1;//动火部位
    	map.firestartdate = 1;//动火开始时间
    	map.fireenddate = 1;//动火结束时间
    	
    	//
    	map.retreatdate = 1;//机械进退场日期
    	map.safetyaccep = 1;//验收日期
    	map.part = 1;//施工部位
			 * 
			 */
			
			put("checkpart","");//动火部位
			put("firestart","");//动火开始时间
			put("firestarttime","");//开始时
			put("firestartminute","");//开始分
			
			put("fireend","");//动火结束时间
			put("firesendtime","");//结束时
			put("firesendminute","");//结束分
			
			//put("fireendleve","");//动火级别
			put("retreatdate","");//机械进退场日期
			put("safetyaccep","");//验收日期
			put("part","");//施工部位
			
			put("comsel","");//下拉选择文本值
			put("comselval","");//下拉选择值
			put("comsel1","");//下拉选择文本值
			put("comsel1val","");//下拉选择值
			put("rt","");//复选框文本值
			put("rtval","");//复选框选择attr值
			put("rdate1","");//时间控件返回值
			put("rdate2","");//
			put("rdate3","");//
			

		}
	};

	protected PreformConfig() throws Exception {
		InputStream is = null;
		try {
			is = this.getClass().getClassLoader().getResourceAsStream(
			filePath);
			if (is == null) {
				File file = new File(filePath);
				if (file.exists()) {
					is = new FileInputStream(file);
				} else {
					Map<String, String> m = new HashMap<String, String>();
					m.put("path", filePath);
					FrameException e = new FrameException(
							"3000000000000025", m);
					throw e;
				}
			}
			param.load(is);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Throwable e) {

				}
			}
		}
	}

	public static PreformConfig getInstance() throws Exception {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() throws Exception {
		if (instance == null) {
			instance = new PreformConfig();
		}
	}
	
	public String getConfig(String key) {
		return param.getProperty(key);
	}
}
