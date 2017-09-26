package com.dotoyo.ims.dsform.allin;
import java.util.Date;

import com.dotoyo.dsform.model.AbstractModel;
public class SysFormModelModel extends AbstractModel{
	private String id;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	private String model;

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model == null ? null : model.trim();
	}

	private String name;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	private String sort;

	public String getSort() {
		return this.sort;
	}

	public void setSort(String sort) {
		this.sort = sort == null ? null : sort.trim();
	}

	private String style;

	public String getStyle() {
		return this.style;
	}

	public void setStyle(String style) {
		this.style = style == null ? null : style.trim();
	}

	private String type;

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type == null ? null : type.trim();
	}

	private String verno;

	public String getVerno() {
		return this.verno;
	}

	public void setVerno(String verno) {
		this.verno = verno == null ? null : verno.trim();
	}

	private String attachedId;

	public String getAttachedId() {
		return this.attachedId;
	}

	public void setAttachedId(String attachedId) {
		this.attachedId = attachedId == null ? null : attachedId.trim();
	}

	private String formtypeId;

	public String getFormtypeId() {
		return this.formtypeId;
	}

	public void setFormtypeId(String formtypeId) {
		this.formtypeId = formtypeId == null ? null : formtypeId.trim();
	}

	private String createdtype;

	public String getCreatedtype() {
		return this.createdtype;
	}

	public void setCreatedtype(String createdtype) {
		this.createdtype = createdtype == null ? null : createdtype.trim();
	}

	private String menusort;

	public String getMenusort() {
		return this.menusort;
	}

	public void setMenusort(String menusort) {
		this.menusort = menusort == null ? null : menusort.trim();
	}

	private String menutype;

	public String getMenutype() {
		return this.menutype;
	}

	public void setMenutype(String menutype) {
		this.menutype = menutype == null ? null : menutype.trim();
	}

	private String rootdirectoryId;

	public String getRootdirectoryId() {
		return this.rootdirectoryId;
	}

	public void setRootdirectoryId(String rootdirectoryId) {
		this.rootdirectoryId = rootdirectoryId == null ? null : rootdirectoryId.trim();
	}

	private Date createDate;

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	private String number;

	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number == null ? null : number.trim();
	}

}
