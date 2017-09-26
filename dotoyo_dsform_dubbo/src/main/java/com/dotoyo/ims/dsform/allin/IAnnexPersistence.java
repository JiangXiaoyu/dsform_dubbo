package com.dotoyo.ims.dsform.allin;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.dotoyo.dsform.model.AnnexModel;

public interface IAnnexPersistence {
	public void upload(AnnexModel bo, byte[] in, Map<String, String> map) throws Exception;;
	public void upload(AnnexModel bo, InputStream in, Map<String, String> map)
	throws Exception;
	public void download(AnnexModel model, OutputStream out) throws Exception;
	public void removeAnnex(AnnexModel model) throws Exception;
}
