package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

/**
 * 许可业务对象
 * 
 * @author xieshh
 * 
 */
final public class LicenceBo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String name;

	private String version;

	private String type;

	private String expiry;

	private final String mac;

	public void setVersion(String version) {
		this.version = version;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	private String sign;

	public LicenceBo(String xmlStr) {
		LicenceBean bean = null;
		Map<String, String> param = null;
		try {
			bean = new LicenceBean(xmlStr);
			param = bean.getParam();
		} catch (Throwable e) {
			param = new HashMap<String, String>();
		}

		this.name = param.get("name");
		this.version = param.get("version");
		this.type = param.get("type");
		this.expiry = param.get("expiry");
		this.mac = param.get("mac");
		this.sign = param.get("sign");

	}

	final public String getVersion() {
		return version;
	}

	final public String getType() {
		return type;
	}

	final public String getExpiry() {
		return expiry;
	}

	public void setExpiry(String expiry) {
		this.expiry = expiry;
	}

	final public String getMac() {
		return mac;
	}

	final public String getSign() {
		return sign;
	}

	final public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return String.format("name=%s,version=%s,type=%s,expiry=%s,mac=%s",
				name, version, type, expiry, mac);
	}

}
