package com.dotoyo.ims.dsform.allin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.model.AnnexModel;
import com.dotoyo.dsform.util.StringsUtils;

public class Annex4FilePersistence implements IAnnexPersistence {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(Annex4FilePersistence.class));

	protected static Annex4FilePersistence instance = null;

	public Annex4FilePersistence() {

	}
	
	public void upload(AnnexModel bo, byte[] in, Map<String, String> map)
			throws Exception {
		OutputStream out = null;
		try {
			String path = bo.getPath();
			
			String virtualPath = map.get("virtualPath");
			String excelId = map.get("excelId");
			//文件模版特殊处理
			if(!StringsUtils.isEmpty(virtualPath) && !StringsUtils.isEmpty(excelId) ){
				path = path + virtualPath + excelId;
				createDir(path);
				out = new FileOutputStream(new File(path + File.separator
						+ bo.getId()));
				bo.setPath(path);
			}else{
				createDir(path);
				out = new FileOutputStream(new File(path + File.separator
						+ bo.getId()));
			}
			int length = 0;
			byte[] buf = new byte[1024];
//			while ((length = in.read(buf)) != -1) {
//				out.write(buf, 0, length);
//			}
			out.write(in, 0, length);
			out.flush();
		} finally {
//			if (in != null) {
//				in.close();
//			}
			if (out != null) {
				out.close();
			}
		}

	}

	public void upload(AnnexModel bo, InputStream in, Map<String, String> map)
			throws Exception {
		OutputStream out = null;
		try {
			String path = bo.getPath();
			
			String virtualPath = map.get("virtualPath");
			String excelId = map.get("excelId");
			//文件模版特殊处理
			if(!StringsUtils.isEmpty(virtualPath) && !StringsUtils.isEmpty(excelId) ){
				path = path + virtualPath + excelId;
				createDir(path);
				out = new FileOutputStream(new File(path + File.separator
						+ bo.getId()));
				bo.setPath(path);
			}else{
				createDir(path);
				out = new FileOutputStream(new File(path + File.separator
						+ bo.getId()));
			}
			int length = 0;
			byte[] buf = new byte[1024];
			while ((length = in.read(buf)) != -1) {
				out.write(buf, 0, length);
			}
			out.flush();
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}

	}

	private void createDir(String destDirName) {

		String tDestDirName = destDirName;
		if (!tDestDirName.endsWith(File.separator)) {
			tDestDirName = String.format("%s%s", destDirName, File.separator);
		}
		File dir = new File(tDestDirName);
		if (!dir.exists()) {
			dir.mkdirs();
		}

	}

	public void removeAnnex(AnnexModel model) throws Exception {

	}

	public void download(AnnexModel model, OutputStream out) throws Exception {
		String file = (String) model.getPath() + File.separator + model.getId();
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Throwable e) {
				}
			}
		}
	}

}
