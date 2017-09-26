package com.dotoyo.ims.dsform.allin;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.dotoyo.dsform.model.AnnexModel;

/**
 * 附件模块
 * 
 * @author xieshh
 * 
 */
public interface IFrameAnnex {
	
	/**
	 * 根据附件编码上传附件
	 * @param bo
	 * @param file
	 * @throws Exception
	 */
	public void upload(AnnexModel bo, File file) throws Exception;

	

	public void download(AnnexModel bo, OutputStream out) throws Exception;

	public AnnexModel upload(AnnexModel bo, byte[] in, Map<String, String> map)
			throws Exception;
	
	public void upload(AnnexModel bo, InputStream in, Map<String, String> map) throws Exception;
	
	public void update(AnnexModel bo, InputStream in, Map<String, String> map)
			throws Exception;
	
	public void update(AnnexModel bo, byte[] in, Map<String, String> map)
			throws Exception;

	public void cleanAnnex() throws Exception;
	
	/**
	 * 事务提交
	 * @param bo
	 * @throws Exception
	 */
	public void commit(AnnexModel bo)
	throws Exception;
	/**
	 * 附件新增
	 * @param bo
	 * @throws Exception
	 */
	public void submit(AnnexModel bo)
	throws Exception;
	/**
	 * 附件删除
	 * @param bo
	 * @throws Exception
	 */
	public void remote(AnnexModel bo)
	throws Exception;
	
}
