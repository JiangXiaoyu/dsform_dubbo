package com.dotoyo.dsform.service;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.dao.inter.IAnnexDao;
import com.dotoyo.dsform.model.AnnexModel;
import com.dotoyo.dsform.service.inter.IAnnexSv;
import com.dotoyo.ims.dsform.allin.AbstractService;
import com.dotoyo.ims.dsform.allin.FrameFactory;
import com.dotoyo.ims.dsform.allin.IFrameLog;
import com.dotoyo.ims.dsform.allin.IFrameService;
import com.dotoyo.dsform.log.LogProxy;

public class AnnexSv extends AbstractService implements IAnnexSv {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(AnnexSv.class));
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		IFrameService fsv = FrameFactory.getServiceFactory(null);
		IAnnexSv sv = (IAnnexSv) fsv
				.getService("com.dotoyo.dsform.service.inter.IAnnexSv");
		log.debug("ok");
	}

	@Override
	public String addAnnex(AnnexModel model) throws Exception {
		IFrameService fsv = FrameFactory.getServiceFactory(null);
		IAnnexDao dao = (IAnnexDao) fsv
				.getService("com.dotoyo.dsform.dao.inter.IAnnexDao");
		dao.insert(model);
		return model.getId();

	}

	@Override
	public AnnexModel getAnnexById(String id) throws Exception {
		IFrameService fsv = FrameFactory.getServiceFactory(null);
		IAnnexDao dao = (IAnnexDao) fsv
				.getService("com.dotoyo.dsform.dao.inter.IAnnexDao");
		AnnexModel model=new AnnexModel();
		model.setId(id);
		return dao.selectByPrimaryKey(model);
	}

}
