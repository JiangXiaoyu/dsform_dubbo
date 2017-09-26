package com.dotoyo.ims.dsform.allin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;


public class FrameFtp extends AbstractFrame implements IFrameFtp {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameFtp.class));

	protected static FrameFtp instance = null;

	protected FrameFtp() {

	}

	public static FrameFtp getInstance(Map<String, String> param) {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new FrameFtp();
		}
	}

	/**
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws Exception {
		FileInputStream fis = new FileInputStream("c:\\Temp\\1.xls");
		FrameFtp ftp = new FrameFtp();

		FtpBo bo = new FtpBo();

		
//		<host code="default">
//		<encrypt note="是否加密">false</encrypt>
//		<user>ftptest</user>
//		<passwd>ftptest</passwd>
//		<path>preform</path>
//		<type>FTP</type>
//		<ip>192.168.1.21</ip>
//		<port>21</port>
		//21ftp测试
		/*bo.setCode("default");
		bo.setServerIp("192.168.1.21");
		bo.setPort(21); 
		bo.setUser("ftptest");
		bo.setPasswd("ftptest");
		bo.setRemoteDir("preform");
		bo.setFileName("bb38fb3a13bc580b9c35e866629311e32");
		bo.setType("FTP");*/
		
		//165ftp测试
		bo.setCode("default");
		bo.setServerIp("14.17.69.165");
		bo.setPort(21);
		bo.setUser("dtyims");
		bo.setPasswd("dtAS^&*56&so55eSOyims113");
		//
		bo.setRemoteDir("preform");
		bo.setFileName("bb38fb3a13bc580b9c35e866629311e3a");
		bo.setType("FTP");
		
		ftp.upload(bo, fis);
		
		
		
/*		FTPClientConfig ftpClientConfig = new FTPClientConfig();
		ftpClientConfig.setServerTimeZoneId(TimeZone.getDefault().getID());
		
		
		FTPClient client = new FTPClient();  
        //client.connect("14.17.69.165");  
		client.connect("14.17.69.165",21);
        client.setControlEncoding("GBK");
        
        client.configure(ftpClientConfig);
        
        int reply = client.getReplyCode();  
        
        client.enterLocalPassiveMode();
        client.setFileType(FTPClient.BINARY_FILE_TYPE);
        
        //client.setFileTransferMode(FTP.BINARY_FILE_TYPE);  
        //client.setConnectTimeout(0);  
        if (!FTPReply.isPositiveCompletion(reply)) {  
            client.disconnect();  
        }  
        System.out.println(client.login("dtyims", "dtAS^&*56&so55eSOyims113"));
        
        client.changeWorkingDirectory("preform");
		boolean isSuccess = client.storeFile("1.xls", new FileInputStream("C:\\Temp\\1.xls"));
		System.out.println("upload isSuccess :" + isSuccess);*/
	}

	public void startup() {

	}

	public void shutdown() {
		
	}

	public void upload(FtpBo bo, InputStream stream) throws Exception {
		fixFtpBo(bo);
		IFtp ftp = null;
		if ("FTP".equals(bo.getType())) {
			ftp = new Ftp4Ftp();
		} else if ("SFTP".equals(bo.getType())) {
			ftp = new Ftp4Sftp();
		}
		if (ftp != null) {
			ftp.upload(bo, stream);
		}

	}
	
	public void upload(FtpBo bo, byte[] stream) throws Exception {
		fixFtpBo(bo);
		IFtp ftp = null;
		if ("FTP".equals(bo.getType())) {
			ftp = new Ftp4Ftp();
		} else if ("SFTP".equals(bo.getType())) {
			ftp = new Ftp4Sftp();
		}
		if (ftp != null) {
			ftp.upload(bo, stream);
		}

	}

	public OutputStream download(FtpBo bo) throws Exception {
		fixFtpBo(bo);
		IFtp ftp = null;
		if ("FTP".equals(bo.getType())) {
			ftp = new Ftp4Ftp();
		} else if ("SFTP".equals(bo.getType())) {
			ftp = new Ftp4Sftp();
		}
		if (ftp != null) {
			return ftp.download(bo);
		}
		return null;
	}

	/**
	 * 从FTP上下载
	 * 
	 * @param bo
	 * @return
	 * @throws Exception
	 */
	public void download(FtpBo bo, OutputStream out) throws Exception {
		fixFtpBo(bo);
		IFtp ftp = null;
		if ("FTP".equals(bo.getType())) {
			ftp = new Ftp4Ftp();
		} else if ("SFTP".equals(bo.getType())) {
			ftp = new Ftp4Sftp();
		}
		if (ftp != null) {
			ftp.download(bo, out);
		}

	}

	@Override
	public void deleteFtpFile(FtpBo bo) throws Exception {
		fixFtpBo(bo);
		IFtp ftp = null;
		if ("FTP".equals(bo.getType())) {
			ftp = new Ftp4Ftp();
		} else if ("SFTP".equals(bo.getType())) {
			ftp = new Ftp4Sftp();
		}
		if (ftp != null) {
			ftp.deleteFtpFile(bo);
		}

	}

	public boolean isExist(FtpBo bo) throws Exception {
		fixFtpBo(bo);
		IFtp ftp = null;
		if ("FTP".equals(bo.getType())) {
			ftp = new Ftp4Ftp();
		} else if ("SFTP".equals(bo.getType())) {
			ftp = new Ftp4Sftp();
		}
		if (ftp != null) {
			return ftp.isExist(bo);
		}
		FrameException fe = new FrameException("", null);
		throw fe;
	}

	private void fixFtpBo(FtpBo bo) throws Exception {
		Map<String, String> param = FtpKeyValuesConfig.getInstance().getValue(
				bo.getCode());
		if (param != null) {
			String user = param.get("user");
			String passwd = param.get("passwd");
			String encrypt = param.get("encrypt");
			if ("true".equals(encrypt)) {
				passwd = new String(Base64Util.decryptBASE64(passwd));
			}
			String remoteDir = param.get("path");
			String type = param.get("type");
			String ip = param.get("ip");
			String port = param.get("port");
			bo.setUser(user);
			bo.setPasswd(passwd);
			bo.setRemoteDir(remoteDir);
			bo.setType(type);
			bo.setServerIp(ip);
			bo.setPort(Integer.parseInt(port));
		}

	}
}
