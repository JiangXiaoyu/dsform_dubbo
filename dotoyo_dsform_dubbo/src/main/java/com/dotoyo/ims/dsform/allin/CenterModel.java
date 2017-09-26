package com.dotoyo.ims.dsform.allin;

import com.dotoyo.dsform.model.AbstractModel;

public class CenterModel extends AbstractModel{
//center{{{{
//center}}}}
//center[[[[
	private String id;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	private String center;

	public String getCenter() {
		return this.center;
	}

	public void setCenter(String center) {
		this.center = center == null ? null : center.trim();
	}

//center]]]]
}
