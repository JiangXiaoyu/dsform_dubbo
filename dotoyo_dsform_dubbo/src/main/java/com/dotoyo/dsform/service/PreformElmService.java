
package com.dotoyo.dsform.service;

import java.util.List;
import java.util.Map;

import com.dotoyo.ims.dsform.allin.AbstractService;
import com.dotoyo.ims.dsform.allin.FrameFactory;
import com.dotoyo.ims.dsform.allin.IFrameService;
import com.dotoyo.ims.dsform.allin.IPreformElmService;
import com.dotoyo.ims.dsform.allin.PreformElm;

public class PreformElmService extends AbstractService implements IPreformElmService {

	@Override
	public int deleteByPrimaryKey(PreformElm record) throws Exception {
		IFrameService fsv = FrameFactory.getServiceFactory(null);
		IPreformElmDao dao = (IPreformElmDao)fsv.getService("com.dotoyo.preform.dao.inter.IPreformElmDao");		
		return dao.deleteByPrimaryKey(record);
	}

	@Override
	public int insert(PreformElm record) throws Exception {
		IFrameService fsv = FrameFactory.getServiceFactory(null);
		IPreformElmDao dao = (IPreformElmDao)fsv.getService("com.dotoyo.preform.dao.inter.IPreformElmDao");	
		return dao.insert(record);
	}

	@Override
	public int insert4Map(Map record) throws Exception {
		IFrameService fsv = FrameFactory.getServiceFactory(null);
		IPreformElmDao dao = (IPreformElmDao)fsv.getService("com.dotoyo.preform.dao.inter.IPreformElmDao");	
		return dao.insert4Map(record);
	}

	@Override
	public List<Map<String, Object>> query(Map<String, String> condition)
			throws Exception {
		IFrameService fsv = FrameFactory.getServiceFactory(null);
		IPreformElmDao dao = (IPreformElmDao)fsv.getService("com.dotoyo.preform.dao.inter.IPreformElmDao");	
		return dao.query(condition);
	}

	@Override
	public long queryCount(Map<String, String> condition) throws Exception {
		IFrameService fsv = FrameFactory.getServiceFactory(null);
		IPreformElmDao dao = (IPreformElmDao)fsv.getService("com.dotoyo.preform.dao.inter.IPreformElmDao");	
		return dao.queryCount(condition);
	}

	@Override
	public PreformElm selectByPrimaryKey(PreformElm record)
			throws Exception {
		IFrameService fsv = FrameFactory.getServiceFactory(null);
		IPreformElmDao dao = (IPreformElmDao)fsv.getService("com.dotoyo.preform.dao.inter.IPreformElmDao");	
		return dao.selectByPrimaryKey(record);
	}

	@Override
	public List<PreformElm> selectList(Map<String, String> condition)
			throws Exception {
		IFrameService fsv = FrameFactory.getServiceFactory(null);
		IPreformElmDao dao = (IPreformElmDao)fsv.getService("com.dotoyo.preform.dao.inter.IPreformElmDao");	
		return dao.selectList(condition);
	}

	@Override
	public List<Map> selectList4Map(Map record) throws Exception {
		IFrameService fsv = FrameFactory.getServiceFactory(null);
		IPreformElmDao dao = (IPreformElmDao)fsv.getService("com.dotoyo.preform.dao.inter.IPreformElmDao");	
		return dao.selectList4Map(record);
	}

	@Override
	public int updateByPrimaryKey(PreformElm record) throws Exception {
		IFrameService fsv = FrameFactory.getServiceFactory(null);
		IPreformElmDao dao = (IPreformElmDao)fsv.getService("com.dotoyo.preform.dao.inter.IPreformElmDao");	
		return dao.updateByPrimaryKey(record);
	}

	@Override
	public int updateByPrimaryKey4Map(Map record) throws Exception {
		IFrameService fsv = FrameFactory.getServiceFactory(null);
		IPreformElmDao dao = (IPreformElmDao)fsv.getService("com.dotoyo.preform.dao.inter.IPreformElmDao");	
		return dao.updateByPrimaryKey4Map(record);
	}

}
