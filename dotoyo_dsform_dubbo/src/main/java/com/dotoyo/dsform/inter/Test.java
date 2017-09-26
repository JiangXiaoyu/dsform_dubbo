package com.dotoyo.dsform.inter;

import org.apache.ibatis.session.SqlSession;

import com.dotoyo.dsform.dao.inter.ITfPreformDao;
import com.dotoyo.dsform.interf.ITfPreformService;
import com.dotoyo.dsform.model.TfPreformModel;
import com.dotoyo.dsform.service.TfPreformService;
import com.dotoyo.ims.dsform.allin.FrameFactory;
import com.dotoyo.ims.dsform.allin.IFrameService;

public class Test {

	    public static void main(String[] args) {
	        
	        try{
	            SqlSession sqlSession = SqlSessionHelper.getSessionFactory().openSession();            
	            ITfPreformDao agentMapper = sqlSession.getMapper(ITfPreformDao.class);	     
	            TfPreformModel preform = new TfPreformModel();
	            preform.setId("9f2d3c0ae7e95841e38fdcf47582fe61");
	            
	            preform = agentMapper.selectByPrimaryKey(preform);
	            System.out.println(preform.getName());
	        }catch(Exception ex){
	            System.out.println(ex.getMessage());
	        }

	    }
	    
	    public void test() throws Exception{
	    	
	    	IFrameService svF = FrameFactory.getServiceFactory(null);
    		ITfPreformService sv = (ITfPreformService) svF
    				.getService(TfPreformService.class.getName());
	    	  TfPreformModel preform = new TfPreformModel();
	            preform.setId("9f2d3c0ae7e95841e38fdcf47582fe61");
	            
	            preform = sv.selectByPrimaryKey(preform);
	            System.out.println(preform.getName());
	    }
	
}
