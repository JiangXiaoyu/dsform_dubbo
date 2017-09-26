package com.dotoyo.ims.dsform.allin;


/**
 * 缓存对象
 * @author xieshh
 *
 */
public class CacheBo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1948813961886581028L;
	private Object value = "";
	private long cas = 0;


	

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public long getCas() {
		return cas;
	}

	public void setCas(long cas) {
		this.cas = cas;
	}

}
