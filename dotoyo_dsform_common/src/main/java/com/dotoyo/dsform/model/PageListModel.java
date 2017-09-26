package com.dotoyo.dsform.model;

import java.util.List;

import com.dotoyo.dsform.model.IModel;

/**
 * 用于分页查询
 * @author Administrator
 *
 */
final public class PageListModel implements java.io.Serializable{
	protected static final long serialVersionUID = 1288916890858085850L;
	private long count;
	

	private List<IModel> modelList;



	public PageListModel(long count, List<IModel> modelList) {
		super();
		this.count = count;
		this.modelList = modelList;
	}

	public List<IModel> getModelList() {
		return modelList;
	}

	public void setModelList(List<IModel> modelList) {
		this.modelList = modelList;
	}

	protected PageListModel() {

	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	

}
