package com.dotoyo.ims.dsform.allin;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class Ftp4Sftp implements IFtp {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(Ftp4Sftp.class));

	public void upload(FtpBo bo, InputStream stream) throws Exception {
		ChannelSftp ftpClient = null;
		long beginTime = System.currentTimeMillis();
		try {
			ftpClient = ftpLogin(bo);
			if (ftpClient == null) {
				throw new FrameException("3000000000000002", null);
			}
			ftpClient.cd(bo.getRemoteDir());

			boolean isSuccess = false;
			try {
				ftpClient.put(stream, bo.getFileName());
				isSuccess = true;
			} catch (Exception e) {
				log.error(e);
			}
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
		ChannelSftp ftpClient = null;
		long beginTime = System.currentTimeMillis();
		try {
			ftpClient = ftpLogin(bo);
			if (ftpClient == null) {
				throw new FrameException("3000000000000002", null);
			}
			ftpClient.cd(bo.getRemoteDir());

			boolean isSuccess = false;
			try {
//				ftpClient.put(stream, bo.getFileName());
				isSuccess = true;
			} catch (Exception e) {
				log.error(e);
			}
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
		ChannelSftp ftpClient = null;
		BufferedOutputStream out = null;
		try {
			ftpClient = ftpLogin(bo);
			if (ftpClient == null) {
				throw new FrameException("3000000000000002", null);
			}
			String filePath = bo.getDownPath() + bo.getFileName();
			ftpClient.cd(bo.getRemoteDir());
			out = new BufferedOutputStream(new FileOutputStream(filePath));
			log.debug(bo.getFileName() + "开始下载...");

			boolean isSuccess = false;
			try {
				ftpClient.get(bo.getFileName(), out);
				isSuccess = true;
			} catch (Exception e) {

			}
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
		ChannelSftp ftpClient = null;

		try {
			ftpClient = ftpLogin(bo);
			if (ftpClient == null) {
				throw new FrameException("3000000000000002", null);
			}

			ftpClient.cd(bo.getRemoteDir());

			log.debug(bo.getFileName() + "开始下载...");

			boolean isSuccess = false;
			try {
				ftpClient.get(bo.getFileName(), out);
				isSuccess = true;
			} catch (Exception e) {

			}
			if (isSuccess) {
				log.debug("下载成功!");
			}
		} finally {
			logout(ftpClient);
		}

	}

	private ChannelSftp ftpLogin(FtpBo bo) throws Exception {
		// fixFtpBo(bo);
		ChannelSftp sftp = null;
		try {
			JSch jsch = new JSch();
			jsch.getSession(bo.getUser(), bo.getServerIp(), bo.getPort());
			Session sshSession = jsch.getSession(bo.getUser(),
					bo.getServerIp(), bo.getPort());
			log.debug("Session created.");
			sshSession.setPassword(bo.getPasswd());
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.setServerAliveInterval(600000);// 10分钟超时
			sshSession.setTimeout(600000);
			sshSession.connect();
			log.debug("Session connected.");
			log.debug("Opening Channel.");
			Channel channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			log.debug("Connected to " + bo.getServerIp() + ".");

			return sftp;
		} catch (Throwable e) {

			if (sftp != null) {
				logout(sftp);
			}
			if (e instanceof Exception) {
				throw (Exception) e;

			} else {
				Exception fe = new Exception(e.getMessage());
				throw fe;
			}
		}

	}

	// private void fixFtpBo(FtpBo bo) throws Exception {
	// Map<String, String> param = FtpKeyValuesConfig.getInstance().getValue(
	// bo.getCode());
	// if (param != null) {
	// String user = param.get("user");
	// String passwd = param.get("passwd");
	// String encrypt = param.get("encrypt");
	// if ("true".equals(encrypt)) {
	// passwd = new String(Base64Util.decryptBASE64(passwd));
	// }
	// String remoteDir = param.get("path");
	// String type = param.get("type");
	// String ip = param.get("ip");
	// String port = param.get("port");
	// bo.setUser(user);
	// bo.setPasswd(passwd);
	// bo.setRemoteDir(remoteDir);
	// bo.setType(type);
	// bo.setServerIp(ip);
	// bo.setPort(Integer.parseInt(port));
	// }
	//
	// }

	private void logout(ChannelSftp sftp) {
		if (sftp != null) {
			try {
				Session sshSession = sftp.getSession();
				sshSession.disconnect();
			} catch (Exception e) {

			}
			try {
				if (sftp.isConnected()) {

					sftp.disconnect();

				} else if (sftp.isClosed()) {
					log.error("sftp is closed already");
				}
			} catch (Exception e) {
				try {
					sftp.disconnect();
				} catch (Exception e1) {

				}
			}
		}
	}

	public void deleteFtpFile(FtpBo bo) throws Exception {
		ChannelSftp ftpClient = null;

		try {
			ftpClient = ftpLogin(bo);
			if (ftpClient == null) {
				throw new FrameException("3000000000000002", null);
			}
			ftpClient.cd(bo.getRemoteDir());

			boolean isSuccess = false;
			try {
				ftpClient.rm(bo.getFileName());
				isSuccess = true;
			} catch (Exception e) {

			}
			if (!isSuccess) {
				log.error("删除失败");
			}
		} finally {
			logout(ftpClient);
		}

	}

	@Override
	public boolean isExist(FtpBo bo) throws Exception {
		ChannelSftp ftpClient = null;

		try {
			ftpClient = ftpLogin(bo);
			if (ftpClient == null) {
				throw new FrameException("3000000000000002", null);
			}
			ftpClient.cd(bo.getRemoteDir());
			Vector list = ftpClient.ls(bo.getFileName());
			if (list.size() == 0) {
				return false;
			} else {
				return true;
			}
		} finally {
			logout(ftpClient);
		}
	}
}
