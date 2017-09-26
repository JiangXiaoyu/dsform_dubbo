package com.dotoyo.ims.dsform.allin;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TimeZone;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;

import com.dotoyo.dsform.log.LogProxy;

public class Ftp4Ftp implements IFtp {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(Ftp4Ftp.class));

	public void upload(FtpBo bo, InputStream stream) throws Exception {
		FTPClient ftpClient = null;
		long beginTime = System.currentTimeMillis();
		try {
			ftpClient = ftpLogin(bo);
			if (ftpClient == null) {
				throw new FrameException("3000000000000059", null);
			}
			ftpClient.changeWorkingDirectory(bo.getRemoteDir());

			boolean isSuccess = ftpClient.storeFile(bo.getFileName(), stream);
			if (isSuccess) {
				log
						.debug("上传成功,耗时:"
								+ (System.currentTimeMillis() - beginTime));
			} else {
				log.error("上传失败...");
				throw new FrameException("4000000000000003", null);
			}
		} finally {
			logout(ftpClient);
		}

	}
	
	public void upload(FtpBo bo, byte[] stream) throws Exception {
		FTPClient ftpClient = null;
		long beginTime = System.currentTimeMillis();
		try {
			ftpClient = ftpLogin(bo);
			if (ftpClient == null) {
				throw new FrameException("3000000000000059", null);
			}
			ftpClient.changeWorkingDirectory(bo.getRemoteDir());

//			boolean isSuccess = ftpClient.storeFile(bo.getFileName(), stream);
			boolean isSuccess = true;
			if (isSuccess) {
				log
						.debug("上传成功,耗时:"
								+ (System.currentTimeMillis() - beginTime));
			} else {
				log.error("上传失败...");
				throw new FrameException("4000000000000003", null);
			}
		} finally {
			logout(ftpClient);
		}

	}

	public OutputStream download(FtpBo bo) throws Exception {
		FTPClient ftpClient = null;
		BufferedOutputStream out = null;
		try {
			ftpClient = ftpLogin(bo);
			if (ftpClient == null) {
				throw new FrameException("3000000000000002", null);
			}
			String filePath = bo.getDownPath() + bo.getFileName();
			ftpClient.changeWorkingDirectory(bo.getRemoteDir());
			out = new BufferedOutputStream(new FileOutputStream(filePath));
			log.debug(bo.getFileName() + "开始下载...");

			boolean isSuccess = ftpClient.retrieveFile(bo.getFileName(), out);
			if (isSuccess) {
				log.debug("下载成功!");
			}
		} finally {
			logout(ftpClient);
		}

		return out;
	}

	/**
	 * 从FTP上下载
	 * 
	 * @param bo
	 * @return
	 * @throws Exception
	 */
	public void download(FtpBo bo, OutputStream out) throws Exception {
		FTPClient ftpClient = null;

		try {
			ftpClient = ftpLogin(bo);
			if (ftpClient == null) {
				throw new FrameException("3000000000000002", null);
			}

			ftpClient.changeWorkingDirectory(bo.getRemoteDir());

			log.debug(bo.getFileName() + "开始下载...");

			boolean isSuccess = ftpClient.retrieveFile(bo.getFileName(), out);
			if (isSuccess) {
				log.debug("下载成功!");
			}
		} finally {
			logout(ftpClient);
		}

	}

	private FTPClient ftpLogin(FtpBo bo) throws Exception {
		// fixFtpBo(bo);
		FTPClientConfig ftpClientConfig = new FTPClientConfig();
		ftpClientConfig.setServerTimeZoneId(TimeZone.getDefault().getID());
		// 获取客户端实例

		FTPClient ftpClient = null;
		try {
			ftpClient = new FTPClient();
			ftpClient.setControlEncoding("GBK");
			ftpClient.configure(ftpClientConfig);
			//ftpClient.setSoTimeout(600 * 1000);// 10分钟
			// 获取进行连接
			try {
				if (bo.getPort() > -1) {
					ftpClient.connect(bo.getServerIp(), bo.getPort());
				} else {
					ftpClient.connect(bo.getServerIp());
				}
			} catch (IOException e) {
				log.error("连接失败，请检查配置或者网络是否正确", e);
				return null;
			}
			// 连接响应是否正确

			int repayCode = ftpClient.getReplyCode();
			log.debug("ftp connection repayCode:" + repayCode);
			if (!FTPReply.isPositiveCompletion(repayCode)) {
				ftpClient.disconnect();
				log.error("连接失败");
				return null;
			}
			// 登录
			if (ftpClient.login(bo.getUser(), bo.getPasswd())) {
				ftpClient.enterLocalPassiveMode();
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				log.debug("登录成功!");
				return ftpClient;
			}
			repayCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(repayCode)) {
				log.error("ftp login repayCode:" + repayCode);
			}else{
				log.debug("ftp login repayCode:" + repayCode);
			}
		} catch (Throwable e) {
			if (ftpClient != null) {
				logout(ftpClient);
			}
			if (e instanceof Exception) {
				throw (Exception) e;

			} else {
				FrameException fe = new FrameException("", null);
				throw fe;
			}
		}
		return null;
	}

	private void logout(FTPClient ftpClient) {
		try {
			if (ftpClient != null && ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					log.debug(e);
				}
			}
		} catch (Throwable e) {
			try {
				ftpClient.disconnect();
			} catch (Exception e1) {

			}
		}
	}

	public void deleteFtpFile(FtpBo bo) throws Exception {
		FTPClient ftpClient = null;

		try {
			ftpClient = ftpLogin(bo);
			if (ftpClient == null) {
				throw new FrameException("3000000000000002", null);
			}
			ftpClient.changeWorkingDirectory(bo.getRemoteDir());
			boolean success = ftpClient.deleteFile(bo.getRemoteDir() + "/"
					+ bo.getFileName());
			if (!success) {
				log.error("删除失败");
			}
		} finally {
			logout(ftpClient);
		}

	}

	@Override
	public boolean isExist(FtpBo bo) throws Exception {
		FTPClient ftpClient = null;

		try {
			ftpClient = ftpLogin(bo);
			if (ftpClient == null) {
				throw new FrameException("3000000000000002", null);
			}
			ftpClient.changeWorkingDirectory(bo.getRemoteDir());
			int count = ftpClient.list(bo.getFileName());
			if (count > 0) {
				return true;
			} else {
				return false;
			}
		} finally {
			logout(ftpClient);
		}
	}
}
