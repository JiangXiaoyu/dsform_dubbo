
package com.dotoyo.ims.dsform.allin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

public class ReportDataRequestBo implements java.io.Serializable {


	private String code = "";

	/**
	 * 业务名称
	 */
	private String name = "";
	private String page = "";
	/**
	 * 页大小
	 */
	private String szie = "";

	private String time = "";
	/**
	 * 记录总数
	 */
	private String count = "";
	private String sort = "";
	private String order = "";
	private JSONObject params = new JSONObject();
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}

	private String pkg = "";
	private String countMethod = "";
	private String rowMethod = "";

	public String getPkg() {
		return pkg;
	}

	public void setPkg(String pkg) {
		this.pkg = pkg;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountMethod() {
		return countMethod;
	}

	public void setCountMethod(String countMethod) {
		this.countMethod = countMethod;
	}

	public String getRowMethod() {
		return rowMethod;
	}

	public void setRowMethod(String rowMethod) {
		this.rowMethod = rowMethod;
	}

	public List<Map<String, String>> getList() {
		return list;
	}

	public void setList(List<Map<String, String>> list) {
		this.list = list;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getSzie() {
		return szie;
	}

	public void setSzie(String szie) {
		this.szie = szie;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
