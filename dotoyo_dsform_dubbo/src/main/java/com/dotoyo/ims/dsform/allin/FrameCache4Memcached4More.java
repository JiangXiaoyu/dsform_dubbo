package com.dotoyo.ims.dsform.allin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.datasource.MybatisMapperBean;
import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.util.StringsUtils;

import net.rubyeye.xmemcached.GetsResponse;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.transcoders.SerializingTranscoder;
import net.rubyeye.xmemcached.utils.AddrUtil;

/**
 * 缓存模块
 * 
 * @author xieshh
 * 
 */
public class FrameCache4Memcached4More extends AbstractFrame implements
		IFrameCache {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameCache4Memcached4More.class));

	protected static FrameCache4Memcached4More instance = null;
	private static Map<String, MemcachedClient> clientMap = new HashMap<String, MemcachedClient>();

	protected FrameCache4Memcached4More() {
		Map<String, Map<String, String>> rows = CacheServerKeyValuesBean
				.getInstance().getKeyValues();
		for (String code : rows.keySet()) {
			Map<String, String> row = rows.get(code);
			if (row == null) {
				continue;
			}
			MemcachedClient client = init(code, row);
			if (client != null) {
				clientMap.put(code, client);
			}
		}
	}

	protected MemcachedClient init(String code, Map<String, String> row) {

		String hosts = row.get("hosts");

		if (StringsUtils.isEmpty(hosts)) {
			return null;
		}
		hosts = hosts.replaceAll(",", " ");
		String poolSize = row.get("poolSize");

		if (StringsUtils.isEmpty(poolSize)) {
			poolSize = "50";
		}
		MemcachedClientBuilder builder = new XMemcachedClientBuilder(

		AddrUtil.getAddresses(hosts));
		// 设置连接池大小，即客户端个数
		builder.setConnectionPoolSize(Integer.parseInt(poolSize));

		// 宕机报警
		builder.setFailureMode(false);
		// 使用二进制文件
		builder.setCommandFactory(new BinaryCommandFactory());
		// 使用一致性哈希算法（Consistent Hash Strategy）
		builder.setSessionLocator(new KetamaMemcachedSessionLocator());
		// 使用序列化传输编码
		builder.setTranscoder(new SerializingTranscoder());
		// 进行数据压缩，大于1KB时进行压缩
		String compressionThreshold = row.get("compressionThreshold");

		if (StringsUtils.isEmpty(compressionThreshold)) {
			poolSize = "2048";
		}
		builder.getTranscoder().setCompressionThreshold(
				Integer.parseInt(compressionThreshold));
		MemcachedClient xmc = null;
		try {
			xmc = builder.build();
			xmc.flushAll();
			return xmc;
		} catch (Throwable e) {
			log.error("", e);
			if (xmc != null) {
				try {
					xmc.shutdown();
				} catch (Throwable e1) {

				}
			}
			return null;
		}

	}

	public static FrameCache4Memcached4More getInstance(
			Map<String, String> param) {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new FrameCache4Memcached4More();
		}
	}

	public void startup() {
		log.debug("startup");
	}

	public void shutdown() {
		for (String code : clientMap.keySet()) {
			MemcachedClient xmc = clientMap.get(code);
			if (xmc != null) {
				try {
					xmc.shutdown();
				} catch (Throwable e1) {

				}
			}
		}
	}

	/**
	 * 清空缓存
	 * 
	 * @param daoClsName
	 *            dao名
	 * @throws Exception
	 */
	public void clearCache(String daoClsName) throws Exception {
		if (getMemcachedClient(daoClsName) == null) {
			return;
		}
		ICache c = FrameCache4Mybatis.getCache(daoClsName);
		if (c == null || c.isUserCache()) {
			throw new RuntimeException("unsupport");
		} else {
			if (StringsUtils.isEmpty(daoClsName)) {
				getMemcachedClient(daoClsName).flushAll(3000);
			} else {
				MapValue4MemcacheBean1.getInstance().clearCache(
						getMemcachedClient(daoClsName), daoClsName);
				InformCacheModify.getInstance(null)
						.infomCacheModify(daoClsName);
			}
		}
	}

	/*
	 * 清空缓存并不通知其他节点
	 */
	public void clearCache4Inform(String daoClsName) throws Exception {
		if (getMemcachedClient(daoClsName) == null) {
			return;
		}
		ICache c = FrameCache4Mybatis.getCache(daoClsName);
		if (c == null || c.isUserCache()) {
			throw new RuntimeException("unsupport");
		} else {
			if (StringsUtils.isEmpty(daoClsName)) {
				getMemcachedClient(daoClsName).flushAll(3000);
			} else {
				MapValue4MemcacheBean1.getInstance().clearCache(
						getMemcachedClient(daoClsName), daoClsName);

			}
		}
	}

	public void addCache(String daoClsName, Object key, Object value, int time)
			throws Exception {
		if (getMemcachedClient(daoClsName) == null) {
			return;
		}
		ICache c = FrameCache4Mybatis.getCache(daoClsName);
		if (c == null || c.isUserCache()) {
			getMemcachedClient(daoClsName).set(fixKey(daoClsName, key), time,
					value, 3000);
		} else {
			MapValue4MemcacheBean1.getInstance().addCache(
					getMemcachedClient(daoClsName), daoClsName, key, value,
					time);
		}

	}

	public Object getCache(String daoClsName, Object key) throws Exception {
		if (getMemcachedClient(daoClsName) == null) {
			return null;
		}
		ICache c = FrameCache4Mybatis.getCache(daoClsName);
		if (c == null || c.isUserCache()) {

			Object ret = getMemcachedClient(daoClsName).get(
					fixKey(daoClsName, key), 3000);
			return ret;
		} else {
			Object ret = MapValue4MemcacheBean1.getInstance().getCache(
					getMemcachedClient(daoClsName), daoClsName, key);
			return ret;
		}

	}

	public int getSize(String daoClsName) throws Exception {
		if (getMemcachedClient(daoClsName) == null) {
			return 0;
		}
		ICache c = FrameCache4Mybatis.getCache(daoClsName);
		if (c == null || c.isUserCache()) {
			throw new RuntimeException("unsupport");
		} else {
			return MapValue4MemcacheBean1.getInstance().getSize(
					getMemcachedClient(daoClsName), daoClsName);
		}
	}

	public Object removeCache(String daoClsName, Object key) throws Exception {
		if (getMemcachedClient(daoClsName) == null) {
			return null;
		}
		ICache c = FrameCache4Mybatis.getCache(daoClsName);
		if (c == null || c.isUserCache()) {
			return getMemcachedClient(daoClsName).delete(
					fixKey(daoClsName, key), (long) 3000);
		} else {
			return MapValue4MemcacheBean1.getInstance().removeCache(
					getMemcachedClient(daoClsName), daoClsName,
					fixKey(daoClsName, key));
		}

	}

	public Object removeCacheBo(String daoClsName, Object key, CacheBo bo)
			throws Exception {
		if (getMemcachedClient(daoClsName) == null) {
			return null;
		}
		ICache c = FrameCache4Mybatis.getCache(daoClsName);
		if (c == null || c.isUserCache()) {
			return getMemcachedClient(daoClsName).delete(
					fixKey(daoClsName, key), bo.getCas(), 3000);
		} else {
			return MapValue4MemcacheBean1.getInstance().removeCacheBo(
					getMemcachedClient(daoClsName), daoClsName, key, bo);
		}

	}

	public static void main(String[] args) throws Exception {
		FrameCache4Memcached4More more=new FrameCache4Memcached4More();
		String daoClsName="com.dotoyo.busi.gov.credit.ent.dao.inter.ITGovCreditEntDao";
		String code=	more.getCacheCode(daoClsName);
		log.debug(code);
	}

	public boolean addCacheBo(String daoClsName, Object key, int time,
			CacheBo bo) throws Exception {
		if (getMemcachedClient(daoClsName) == null) {
			return false;
		}
		ICache c = FrameCache4Mybatis.getCache(daoClsName);
		if (c == null || c.isUserCache()) {
			return getMemcachedClient(daoClsName).cas(fixKey(daoClsName, key),
					time, bo.getValue(), bo.getCas());
		} else {
			return MapValue4MemcacheBean1.getInstance().addCacheBo(
					getMemcachedClient(daoClsName), daoClsName, key, time, bo);
		}

	}

	public CacheBo getCacheBo(String daoClsName, Object key) throws Exception {
		if (getMemcachedClient(daoClsName) == null) {
			return null;
		}
		ICache c = FrameCache4Mybatis.getCache(daoClsName);
		if (c == null || c.isUserCache()) {
			GetsResponse<Object> resp = getMemcachedClient(daoClsName).gets(
					fixKey(daoClsName, key), 3000);
			if (resp == null) {
				return null;
			}
			CacheBo bo = new CacheBo();
			bo.setCas(resp.getCas());
			bo.setValue(resp.getValue());
			return bo;
		} else {
			return MapValue4MemcacheBean1.getInstance().getCacheBo(
					getMemcachedClient(daoClsName), daoClsName, key);
		}

	}

	protected MemcachedClient getMemcachedClient(String daoClsName)
			throws Exception {
		MemcachedClient xmc = clientMap.get(getCacheCode(daoClsName));
		return xmc;
	}

	protected String getCacheCode(String daoClsName) throws Exception {

		if (IFrameSession.SESSION_DATA.equals(daoClsName)
				|| "com.dotoyo.frame.keyvalue.dao.IKeyValueDao"
						.equals(daoClsName)
				|| "com.dotoyo.frame.staticdata.dao.inter.IStaticDataDao"
						.equals(daoClsName)) {
			return "default";
		}
		if (isCenter(daoClsName)) {
			// 是分中心业务
			String centerId = "";
			try {
				IFrameCenter cen = (IFrameCenter) FrameFactory
						.getCenterFactory(null);

				centerId = cen.getCurrentCenter();

			} catch (Throwable e) {
				throw new SQLException(e.getMessage());
			}
			String sub = "";
			if (StringsUtils.isEmpty(centerId)) {
				sub = daoClsName;
			} else {
				sub = String.format("%s_%s", daoClsName, centerId);
			}

			Map<String, String> row = CacheServerKeyValuesBean.getInstance()
					.getValue(sub);
			if (row != null) {
				String ref = row.get("ref");
				if (!StringsUtils.isEmpty(ref)) {
					return ref;
				}
			}
			if (StringsUtils.isEmpty(sub)) {
				sub = String.format("%s_%s", "default", centerId);
			}
			row = CacheServerKeyValuesBean.getInstance()
			.getValue(sub);
			if (row == null) {
				sub="default";
			}
			
			return sub;
		} else {
			Map<String, String> row = CacheServerKeyValuesBean.getInstance()
					.getValue(daoClsName);
			if (row != null) {
				String ref = row.get("ref");
				if (!StringsUtils.isEmpty(ref)) {
					return ref;
				}
			}
			String sub = "";
			if (StringsUtils.isEmpty(sub)) {
				sub = "default";
			}
			return sub;
		}

	}

	protected boolean isCenter(String daoClsName) throws Exception {
		try {
			Class<?> cls = Class.forName(daoClsName);
			MybatisMapperBean bean=MybatisMapperBean.getMapperBean(cls);
			if ( bean!= null) {
				
				return bean.getIsCenter();
			}
		} catch (Throwable e) {

		}
		return false;
	}

	private String fixKey(String daoClsName, Object key) throws Exception {
		IKeyCache keyCache = new KeyCache4Memcache();
		return keyCache.getCacheKey(daoClsName, key);
	}

}
