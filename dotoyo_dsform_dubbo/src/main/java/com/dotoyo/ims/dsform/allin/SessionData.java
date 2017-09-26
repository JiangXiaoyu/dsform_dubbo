package com.dotoyo.ims.dsform.allin;

import java.io.Serializable;

public class SessionData implements Serializable {
	private static final long serialVersionUID = -5352644922759856626L;
	private String parentSessionId;// 单点登录其它应用的会话编号，缓存这个会话编号与本地会话编号的关联关系
	private String sessionId;
	private UserLoginBo userLogingBo;//用户
	private long lastSignTime;//最后签到时间(心跳)
	private long lastAccessTime;//最后访问时间(会话)
	private long lastSave2CacheTime;//最后能保存到cache的时间
	
	public SessionData(String sessionId, UserLoginBo userLogingBo) {
		super();
		this.sessionId = sessionId;
		this.userLogingBo = userLogingBo;
		this.lastAccessTime = 0;
		this.lastSignTime = 0;
		this.lastSave2CacheTime = 0;
	}
	public String getParentSessionId() {
		return parentSessionId;
	}

	public void setParentSessionId(String parentSessionId) {
		this.parentSessionId = parentSessionId;
	}
	public String getSessionId() {
		return sessionId;
	}
	
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public long getLastSignTime() {
		return lastSignTime;
	}

	public void setLastSignTime(long lastSignTime) {
		this.lastSignTime = lastSignTime;
	}

	public long getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(long lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public long getLastSave2CacheTime() {
		return lastSave2CacheTime;
	}

	public void setLastSave2CacheTime(long lastSave2CacheTime) {
		this.lastSave2CacheTime = lastSave2CacheTime;
	}

	public UserLoginBo getUserLogingBo() {
		return userLogingBo;
	}

	public void setUserLogingBo(UserLoginBo userLogingBo) {
		this.userLogingBo = userLogingBo;
	}
}
