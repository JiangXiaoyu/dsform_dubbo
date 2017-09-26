package com.dotoyo.ims.dsform.allin;

/**
 * <br>
 * <b>功能：</b>单点登录用户数据<br>
 * <b>作者：</b>胡双<br>
 * <b>日期：</b>2014-6-9<br>
 * <b>版权所有：</b>版权所有(C) 2014，拜特科技<br>
 */
public class UserLoginBo implements java.io.Serializable {

	/**
	 * 每个字段的定义参见规范
	 */
	private static final long serialVersionUID = -8850693103453252873L;


	private String userCode;// 对应user_code
	private String userName;// 对应user_name
	private String corpCode;// 对应corp_code
	private String roleCode;// 对应role_code
	private String loginType;// 登陆类型（邮箱 手机 账号）
	private String ip;// 对应用户IP
	private String userId;//
	private String center;//

	//

	//public String getDbCode() {
	//	return dbCode;
	//}



	//public void setDbCode(String dbCode) {
	//	this.dbCode = dbCode;
	//}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
}
