package com.dotoyo.dsform.annex;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.dao.inter.IAnnexDao;
import com.dotoyo.dsform.dao.inter.IFormModelDao;
import com.dotoyo.dsform.model.AnnexModel;
import com.dotoyo.dsform.util.StringsUtils;
import com.dotoyo.ims.dsform.allin.AbstractFrame;
import com.dotoyo.ims.dsform.allin.Annex4FilePersistence;
import com.dotoyo.ims.dsform.allin.Annex4FtpPersistence;
import com.dotoyo.ims.dsform.allin.AnnexKeyValuesConfig;
import com.dotoyo.ims.dsform.allin.FormElmParModel;
import com.dotoyo.ims.dsform.allin.FrameException;
import com.dotoyo.ims.dsform.allin.FrameFactory;
import com.dotoyo.ims.dsform.allin.IAnnexPersistence;
import com.dotoyo.ims.dsform.allin.IFrameAnnex;
import com.dotoyo.ims.dsform.allin.IFrameCache;
import com.dotoyo.ims.dsform.allin.IFrameLog;
import com.dotoyo.ims.dsform.allin.IFrameSequence;
import com.dotoyo.ims.dsform.allin.IFrameService;
import com.dotoyo.dsform.log.LogProxy;


public class FrameAnnex extends AbstractFrame implements IFrameAnnex {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameAnnex.class));

	protected static FrameAnnex instance = null;

	protected FrameAnnex() {

	}

	public static FrameAnnex getInstance(Map<String, String> param) {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new FrameAnnex();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void startup() {
		// TODO Auto-generated method stub

	}

	public void shutdown() {
		// TODO Auto-generated method stub

	}

	public void upload(AnnexModel model, File file) throws Exception {
		IFrameSequence sF = FrameFactory.getSequenceFactory(null);
		model.setId(sF.getSequence());
		
		String code = model.getCode();
		Map<String, String> row = AnnexKeyValuesConfig.getInstance().getValue(
				code);
		if (row == null) {
			row = AnnexKeyValuesConfig.getInstance().getValue("default");
		}
		if (row == null) {
			throw new FrameException("", null);
		}
		//
		IAnnexPersistence p = null;
		String thisType = row.get("type");
		if ("FTP".equals(thisType)) {
			model.setCode(row.get("ftp"));
			p = new Annex4FtpPersistence();
		} else {
			model.setPath(row.get("path"));
			p = new Annex4FilePersistence();
		}
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			p.upload(model, fis, null);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Throwable e) {

				}
			}
		}
		submit(model);

	}

	public File download(AnnexModel bo) throws Exception {
		String code = bo.getCode();
		Map<String, String> row = AnnexKeyValuesConfig.getInstance().getValue(
				code);

		return null;
	}

	
	
	
	public void upload(AnnexModel model, InputStream in, Map<String, String> map)
			throws Exception {
		IFrameSequence sF = FrameFactory.getSequenceFactory(null);
		if(StringsUtils.isEmpty(model.getId())){
			model.setId(sF.getSequence());
		}

		IFrameService svf = FrameFactory.getServiceFactory(null);
		IFormModelDao formModelDao = (IFormModelDao) svf
				.getService(IFormModelDao.class);
		List<FormElmParModel> list = formModelDao.queryFormElePar(map);
		if (list == null || list.size() < 1) {
			// 配置不正确
			FrameException fe = new FrameException("", null);
			throw fe;
		}
		FormElmParModel parModel = list.get(0);
		model.setThisType(parModel.getText());
		IAnnexPersistence p = null;
		if ("FTP".equals(model.getThisType())) {
			model.setCode(parModel.getThisValue());
			p = new Annex4FtpPersistence();
		} else {
			model.setPath(parModel.getThisValue());
			p = new Annex4FilePersistence();
		}

		p.upload(model, in, map);
		submit(model);
	}
	
	@Override
	public void update(AnnexModel model, InputStream in, Map<String, String> map)
			throws Exception {
		//IFrameSequence sF = FrameFactory.getSequenceFactory(null);
		//model.setId(sF.getSequence());
		IFrameService svf = FrameFactory.getServiceFactory(null);
		IFormModelDao formModelDao = (IFormModelDao) svf
				.getService(IFormModelDao.class);
		List<FormElmParModel> list = formModelDao.queryFormElePar(map);
		if (list == null || list.size() < 1) {
			// 配置不正确
			FrameException fe = new FrameException("", null);
			throw fe;
		}
		FormElmParModel parModel = list.get(0);
		model.setThisType(parModel.getText());
		IAnnexPersistence p = null;
		if ("FTP".equals(model.getThisType())) {
			model.setCode(parModel.getThisValue());
			p = new Annex4FtpPersistence();
		} else {
			model.setPath(parModel.getThisValue());
			p = new Annex4FilePersistence();
		}

		p.upload(model, in, map);
	}
	
	public AnnexModel upload(AnnexModel model, byte[] in, Map<String, String> map)
			throws Exception {
		IFrameSequence sF = FrameFactory.getSequenceFactory(null);
		if(StringsUtils.isEmpty(model.getId())){
			model.setId(sF.getSequence());
		}

		IFrameService svf = FrameFactory.getServiceFactory(null);
		IFormModelDao formModelDao = (IFormModelDao) svf
				.getService(IFormModelDao.class);
		List<FormElmParModel> list = formModelDao.queryFormElePar(map);
		if (list == null || list.size() < 1) {
			// 配置不正确
			FrameException fe = new FrameException("", null);
			throw fe;
		}
		FormElmParModel parModel = list.get(0);
		model.setThisType(parModel.getText());
		IAnnexPersistence p = null;
		if ("FTP".equals(model.getThisType())) {
			model.setCode(parModel.getThisValue());
			p = new Annex4FtpPersistence();
		} else {
			model.setPath(parModel.getThisValue());
			p = new Annex4FilePersistence();
		}

//		p.upload(model, in, map);
		submit(model);
		
		return model;
	}
	
	@Override
	public void update(AnnexModel model, byte[] in, Map<String, String> map)
			throws Exception {
		//IFrameSequence sF = FrameFactory.getSequenceFactory(null);
		//model.setId(sF.getSequence());
		IFrameService svf = FrameFactory.getServiceFactory(null);
		IFormModelDao formModelDao = (IFormModelDao) svf
				.getService(IFormModelDao.class);
		List<FormElmParModel> list = formModelDao.queryFormElePar(map);
		if (list == null || list.size() < 1) {
			// 配置不正确
			FrameException fe = new FrameException("", null);
			throw fe;
		}
		FormElmParModel parModel = list.get(0);
		model.setThisType(parModel.getText());
		IAnnexPersistence p = null;
		if ("FTP".equals(model.getThisType())) {
			model.setCode(parModel.getThisValue());
			p = new Annex4FtpPersistence();
		} else {
			model.setPath(parModel.getThisValue());
			p = new Annex4FilePersistence();
		}

		p.upload(model, in, map);
	}
	
	
	
	
	
	
	
	
	

	public void cleanAnnex() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		map.put("time", sdf.format(calendar.getTime()));
		// 删除一天之前无效的附件
		cleanInvalidAnnex(map);
		// 直接删除附件
		cleanDelAnnex();
		IFrameCache c = FrameFactory.getCacheFactory(null);
		c.clearCache(IAnnexDao.class.getName());
	}

	private boolean cleanDelAnnex() throws Exception {
		boolean flag = false;
		IFrameService svf = FrameFactory.getServiceFactory(null);
		IAnnexDao dao = (IAnnexDao) svf.getService(IAnnexDao.class.getName());
		List<Map<String, Object>> annexs = dao.queryDelAnnex();
		if (annexs != null && annexs.size() > 0) {
			for (Map<String, Object> annex : annexs) {
				deleteFolder(annex.get("path") + File.separator
						+ annex.get("id"));
			}
			cleanAnnex4Jdbc(annexs);
			flag = true;
		}
		return flag;
	}

	private boolean cleanInvalidAnnex(Map<String, Object> map) throws Exception {
		boolean flag = false;
		IFrameService svf = FrameFactory.getServiceFactory(null);
		IAnnexDao annexDao = (IAnnexDao) svf.getService(IAnnexDao.class
				.getName());
		List<Map<String, Object>> annexs = annexDao.queryInvalidAnnex(map);
		if (annexs != null && annexs.size() > 0) {
			for (Map<String, Object> annex : annexs) {
				deleteFolder(annex.get("path") + File.separator
						+ annex.get("id"));
			}
			cleanAnnex4Jdbc(annexs);
			flag = true;
		}
		return flag;
	}

	private void cleanAnnex4Jdbc(List<Map<String, Object>> annexs) {
		PreparedStatement pst = null;
		Connection conn = null;
		try {
			DataSource dataSource = (DataSource) FrameFactory
					.getServiceFactory(null).getService("dataSource1");
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			pst = conn.prepareStatement("delete from t_f_annex where id = ?");
			for (int i = 0; i < annexs.size(); i++) {
				pst.setString(1, (String) annexs.get(i).get("id"));
				pst.addBatch();
			}
			pst.executeBatch();
			conn.commit();
			pst.clearBatch();
		} catch (Exception e) {
			log.error("", e);
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}

			} catch (Exception e) {
				log.error("", e);
			}
			try {

				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}

	private boolean deleteFolder(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		} else {
			if (file.isFile()) {
				return deleteFile(path);
			} else {
				return deleteDirectory(path);
			}
		}
	}

	private boolean deleteFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	private boolean deleteDirectory(String path) {
		if (!path.endsWith(File.separator)) {
			path = path + File.separator;
		}
		File dirFile = new File(path);
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	public void download(AnnexModel model, OutputStream out) throws Exception {
		IAnnexPersistence p = null;
		if ("FTP".equals(model.getThisType())) {
			p = new Annex4FtpPersistence();
		} else {
			p = new Annex4FilePersistence();
		}
		p.download(model, out);

	}

	/**
	 * 事务提交
	 * 
	 * @param bo
	 * @throws Exception
	 */
	public void commit(AnnexModel bo) throws Exception {
		IFrameService svf = FrameFactory.getServiceFactory(null);
		IAnnexDao annexDao = (IAnnexDao) svf.getService(IAnnexDao.class
				.getName());
		bo.setState("3");
		annexDao.updateByPrimaryKey(bo);
	}

	/**
	 * 附件新增
	 * 
	 * @param bo
	 * @throws Exception
	 */
	public void submit(AnnexModel bo) throws Exception {
		IFrameService svf = FrameFactory.getServiceFactory(null);
		IAnnexDao dao = (IAnnexDao) svf.getService(IAnnexDao.class.getName());
		bo.setState("1");
		bo.setCreateTime(new Timestamp(System.currentTimeMillis()));

		dao.insert(bo);

	}
	
	/**
	 * 附件删除
	 * 
	 * @param bo
	 * @throws Exception
	 */
	public void remote(AnnexModel bo) throws Exception {
		IFrameService svf = FrameFactory.getServiceFactory(null);
		IAnnexDao annexDao = (IAnnexDao) svf.getService(IAnnexDao.class
				.getName());
		annexDao.deleteByPrimaryKey(bo);
	}
}
