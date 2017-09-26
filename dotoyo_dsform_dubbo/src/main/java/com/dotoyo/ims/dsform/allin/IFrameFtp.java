package com.dotoyo.ims.dsform.allin;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Ftp通迅模块
 * 
 * @author xieshh
 * 
 */
public interface IFrameFtp {
	/**
	 * 上传文件到FTP
	 * 
	 * @param bo
	 * @param file
	 * @throws Exception
	 */
	public void upload(FtpBo bo, InputStream file) throws Exception;
	
	public void upload(FtpBo bo, byte[] file) throws Exception;

	/**
	 * 从FTP上下载
	 * 
	 * @param bo
	 * @return
	 * @throws Exception
	 */
	public void download(FtpBo bo, OutputStream out) throws Exception;

	/**
	 * 从FTP上下载
	 * 
	 * @param bo
	 * @return
	 * @throws Exception
	 */
	public OutputStream download(FtpBo bo) throws Exception;

	/**
	 * 从指定ftp服务器删除文件
	 * 
	 * @param bo
	 */
	public void deleteFtpFile(FtpBo bo) throws Exception;

	public boolean isExist(FtpBo bo) throws Exception;

}
