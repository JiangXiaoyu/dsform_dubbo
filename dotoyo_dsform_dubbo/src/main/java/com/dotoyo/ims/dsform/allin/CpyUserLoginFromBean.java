package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

public class CpyUserLoginFromBean implements IUserLogin{

	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(CpyUserLoginFromBean.class));
	
	@Override
	public Map<String, Object> userLogin(String userCode, String userPasswd,
			Map<String, Object> param) throws Exception {
		TpAccountModel model = new TpAccountModel();
		model.setCode(userCode);
		IFrameService svF = FrameFactory.getServiceFactory(null);
		ITpUserSv sv = (ITpUserSv) svF
				.getService("com.dotoyo.admin.service.inter.ITpUserSv");
		TpUserModel userModel = sv.loginUser(userCode, userPasswd, param);
		if (userModel==null) {
			return null;
		}
		//
		UserLoginBo bo = new UserLoginBo();
		bo.setIp((String)param.get("ip"));
		bo.setUserCode(userCode);
		bo.setUserId(userModel.getId());
		bo.setUserName(userModel.getName());
		bo.setCenter("1");

		//
		IFrameSession sf = FrameFactory.getSessionFactory(null);
		sf.saveUserLoginBo("", bo);
		//查询中间表
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("passwd",userPasswd);
        map.put("code",userCode);
        Map obj=null;
        CompanyModel cymodel=null;
        if(sv.selectAMList(map).size()>0){
		   obj=sv.selectAMList(map).get(0);
		   
		   IUserCpyService ucsv=(IUserCpyService)svF.getService("com.dotoyo.busi.association.userCpy.service.inter.IUserCpyService");
		   UserCpyModel ucmodel=new UserCpyModel();
		   ucmodel.setUserid(obj.get("id").toString());
		   ucmodel=ucsv.selectModelList(ucmodel).get(0);
		   //通过cpyid查询企业信息存入session
		   ICompanyService cysv=(ICompanyService)svF.getService("com.dotoyo.busi.association.company.service.inter.ICompanyService");
		   cymodel=new CompanyModel();
		   cymodel.setId(ucmodel.getCpyid());
		   cymodel=cysv.selectByPrimaryKey(cymodel);
        }
        sf.getHttpSession().setAttribute("cpyinfo", cymodel);
		Map<String, Object> user = new HashMap<String, Object>();
		user.put("code", bo.getUserCode());
		user.put("name", bo.getUserName());
		//
		user.put("mainFrameUrl", "/edu_peopleinfo");//forwardEtpUser_index
		return user;
	}
    
}
