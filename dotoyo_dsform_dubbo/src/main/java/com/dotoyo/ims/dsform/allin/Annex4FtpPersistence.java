package com.dotoyo.ims.dsform.allin;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.model.AnnexModel;

public class Annex4FtpPersistence implements IAnnexPersistence {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(Annex4FtpPersistence.class));

	protected static Annex4FtpPersistence instance = null;

	public Annex4FtpPersistence() {

	}

	

	

	public void upload(AnnexModel model, InputStream in, Map<String, String> map)
			throws Exception {
		IFrameFtp ftp=FrameFactory.getFtpFactory(null);
		FtpBo ftpBo=new FtpBo();
		ftpBo.setCode(model.getCode());
		ftpBo.setFileName(model.getId());
		ftp.upload(ftpBo, in);
		
	}

	public void upload(AnnexModel model, byte[] in, Map<String, String> map)
			throws Exception {
		IFrameFtp ftp=FrameFactory.getFtpFactory(null);
		FtpBo ftpBo=new FtpBo();
		ftpBo.setCode(model.getCode());
		ftpBo.setFileName(model.getId());
		ftp.upload(ftpBo, in);
		
	}
	
	public void removeAnnex(AnnexModel model) throws Exception{
		IFrameFtp ftp=FrameFactory.getFtpFactory(null);
		FtpBo ftpBo=new FtpBo();
		ftpBo.setCode(model.getCode());
		ftpBo.setFileName(model.getId());
		ftp.deleteFtpFile(ftpBo);
	}

	public void download(AnnexModel model, OutputStream out) throws Exception {
		IFrameFtp ftp=FrameFactory.getFtpFactory(null);
		FtpBo bo=new FtpBo();
		bo.setCode(model.getCode());
		bo.setFileName(model.getId());
		ftp.download(bo,out);
	}

	
}
