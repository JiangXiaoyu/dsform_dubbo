
package com.dotoyo.dsform.service;

import java.util.List;
import java.util.Map;

import com.dotoyo.ims.dsform.allin.AbstractService;
import com.dotoyo.ims.dsform.allin.FrameFactory;
import com.dotoyo.ims.dsform.allin.IFrameService;

public class PreformElmParService extends AbstractService implements IPreformElmParService {

	@Override
	public int deleteByPrimaryKey(PreformElmParModel record) throws Exception {
		IFrameService fsv = FrameFactory.getServiceFactory(null);
		IPreformElmParDao dao = (IPreformElmParDao)fsv.getService("com.dotoyo.busi.preform.dao.inter.IPreformElmParDao");		
		
		return dao.deleteByPrimaryKey(record);
	}

	@Override
	public int insert(PreformElmParModel record) throws Exception {
		IFrameService fsv = FrameFactory.getServiceFactory(null);
		IPreformElmParDao dao = (IPreformElmParDao)fsv.getService("com.dotoyo.busi.preform.dao.inter.IPreformElmParDao");	
		return dao.insert(record);
	}

	@Override
	public int insert4Map(Map record) throws Exception {
		IFrameService fsv = FrameFactory.getServiceFactory(null);
		IPreformElmParDao dao = (IPreformElmParDao)fsv.getService("com.dotoyo.busi.preform.dao.inter.IPreformElmParDao");	
		return dao.insert4Map(record);
	}

	@Override
	public List<Map<String, Object>> query(Map<String, String> condition)
			throws Exception {
		IFrameService fsv = FrameFactory.getServiceFactory(null);
		IPreformElmParDao dao = (IPreformElmParDao)fsv.getService("com.dotoyo.busi.preform.dao.inter.IPreformElmParDao");	
		return dao.query(condition);
	}

	@Override
	public long queryCount(Map<String, String> condition) throws Exception {
		IFrameService fsv = FrameFactory.getServiceFactory(null);
		IPreformElmParDao dao = (IPreformElmParDao)fsv.getService("com.dotoyo.busi.preform.dao.inter.IPreformElmParDao");	
		return dao.queryCount(condition);
	}

	@Override
	public PreformElmParModel selectByPrimaryKey(PreformElmParModel record)
			throws Exception {
		IFrameService fsv = FrameFactory.getServiceFactory(null);
		IPreformElmParDao dao = (IPreformElmParDao)fsv.getService("com.dotoyo.busi.preform.dao.inter.IPreformElmParDao");	
		return dao.selectByPrimaryKey(record);
	}

	@Override
	public List<PreformElmParModel> selectList(Map<String, String> condition)
			throws Exception {
		IFrameService fsv = FrameFactory.getServiceFactory(null);
		IPreformElmParDao dao = (IPreformElmParDao)fsv.getService("com.dotoyo.busi.preform.dao.inter.IPreformElmParDao");	
		return dao.selectList(condition);
	}

	@Override
	public List<Map> selectList4Map(Map record) throws Exception {
		IFrameService fsv = FrameFactory.getServiceFactory(null);
		IPreformElmParDao dao = (IPreformElmParDao)fsv.getService("com.dotoyo.busi.preform.dao.inter.IPreformElmParDao");	
		return dao.selectList4Map(record);
	}

	@Override
	public int updateByPrimaryKey(PreformElmParModel record) throws Exception {
		IFrameService fsv = FrameFactory.getServiceFactory(null);
		IPreformElmParDao dao = (IPreformElmParDao)fsv.getService("com.dotoyo.busi.preform.dao.inter.IPreformElmParDao");	
		return dao.updateByPrimaryKey(record);
	}

	@Override
	public int updateByPrimaryKey4Map(Map record) throws Exception {
		IFrameService fsv = FrameFactory.getServiceFactory(null);
		IPreformElmParDao dao = (IPreformElmParDao)fsv.getService("com.dotoyo.busi.preform.dao.inter.IPreformElmParDao");	
		return dao.updateByPrimaryKey4Map(record);
	}
	
}
