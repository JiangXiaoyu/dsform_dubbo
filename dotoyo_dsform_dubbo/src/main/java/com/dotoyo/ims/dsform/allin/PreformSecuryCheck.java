package com.dotoyo.ims.dsform.allin;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dotoyo.dsform.util.StringsUtils;

import net.sf.json.JSONObject;

/**
 * 表单接口安全检查
 * @author wanglong
 *
 */
public class PreformSecuryCheck {
	/**
	 * 参数的安全检查
	 * @param httpRequest
	 * @param httpResponse
	 * @return
	 */
	public boolean securyCheck(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) throws Exception {
		String status = PreformConfig.getInstance().getConfig(PreformConfig.OPEN_API_SECURY_STATUS);//0表示宽容验证,1表示强制验证
		String sign1 = httpRequest.getParameter("sign");
		String param = PreformUtils.converURLParam(httpRequest);
		JSONObject paramJson = JSONObject.fromObject(param);
		if("1".equals(status)){
			checkRequestInterval(paramJson, httpResponse);//检查客户端与服务端时间间隔
			if(StringsUtils.isEmpty(sign1)){//强制验证,sign为空则返回false
				PreformUtils.toErrorPage(httpResponse,"对不起,您的请求带有不合法的参数,谢谢合作!","请求中sign为空!");
				return false;
			}
			String sign2 = generateParamSign(paramJson);
			if(sign1.equals(sign2)){
				return true;
			}
		}else{
			if(StringsUtils.isEmpty(sign1)){//宽容验证,sign为空则返回true
				return true;
			}
			checkRequestInterval(paramJson, httpResponse);//检查客户端与服务端时间间隔
			String sign2 = generateParamSign(paramJson);
			if(sign1.equals(sign2)){
				return true;
			}
		}
		PreformUtils.toErrorPage(httpResponse,"对不起,您的请求带有不合法的参数,谢谢合作!","请求中参数不匹配为空!");
		return false;
	}

	/**
	 * 检查客户端与服务端时间间隔
	 * @param paramJson
	 * @return
	 */
	private void checkRequestInterval(JSONObject paramJson, HttpServletResponse httpResponse) throws Exception{
		long urlTimeStamp = 0; 													//客户端时间
		try{
			urlTimeStamp = Long.parseLong(paramJson.optString("timeStamp"));    
		}catch(NumberFormatException e){
			PreformUtils.toErrorPage(httpResponse,"对不起,您的请求带有不合法的参数,谢谢合作!","请求中参数timeStamp不正确!");
			return;
		}
		long serveDate = new Date().getTime();									//服务端时间
		long maxInactiveInterval = 0;											//允许的最大间隔时间
		long actualInterval = Math.abs((serveDate - urlTimeStamp)/(1000*60)) ;	//实际间隔时间
		try{
			maxInactiveInterval = Long.parseLong(PreformConfig.getInstance().getConfig(PreformConfig.OPEN_API_SECURY_MAXINACTIVEINTERVAL)) ;
		}catch(NumberFormatException e){
			maxInactiveInterval = 120;
		}
		if(maxInactiveInterval < 30){
			maxInactiveInterval = 30;//允许的最大间隔时间不能小于30分钟
		}
		if(actualInterval >  maxInactiveInterval){
			PreformUtils.toErrorPage(httpResponse,"对不起,您的请求已失效,谢谢合作!",String.format("实际间隔时间:%s分钟,允许的最大间隔时间:%s分钟!", actualInterval, maxInactiveInterval));
		}
	}

	/**
	 * 服务端生成签名信息
	 * @param paramJson 参数json,包含以下key: id,_id,type,username,project,timeStamp,md5Salt
	 * @return
	 */
	public static String generateParamSign(JSONObject paramJson) throws Exception{
		Map<String,String> treeMap = new TreeMap<String,String>();//按照key排序
		treeMap.put("id", paramJson.optString("id"));
		treeMap.put("_id", paramJson.optString("_id"));
		treeMap.put("orgId", paramJson.optString("orgId"));
		treeMap.put("type", paramJson.optString("type"));
		treeMap.put("username", paramJson.optString("username"));
		treeMap.put("project", paramJson.optString("project"));
		treeMap.put("timeStamp", paramJson.optString("timeStamp"));//时间戳
		treeMap.put("md5Salt", PreformConfig.getInstance().getConfig(PreformConfig.OPEN_API_SECURY_MD5SALT));//md5盐
		
		JSONObject result = JSONObject.fromObject(treeMap);
		return MD5JAVA.MD5Encode(result.toString(), "utf-8");
	}
}
