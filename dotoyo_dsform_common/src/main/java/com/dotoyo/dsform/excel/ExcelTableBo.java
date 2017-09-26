package com.dotoyo.dsform.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class ExcelTableBo {
	private Map<Integer, ExcelTableTrBo> trMap = new HashMap<Integer, ExcelTableTrBo>();
	private String excelTitle = "";
	private int firstRow = 0;
	private int lastRow = 0;
	private Map<String, ExcelColumnBo> param = new HashMap<String, ExcelColumnBo>();
	private List<ExcelPictureBo> picList = new ArrayList<ExcelPictureBo>();
	private List<PictureStyleBo> picStyleList = new ArrayList<PictureStyleBo>();
	private boolean isOldPreform = false;//旧表单所有列都有宽度，新表单只有第一行才有宽度
	private String printDirc = "V";//打印方向   v竖 H横
	
	private boolean hasEditor = false;//是否有编辑器  （只用于判断是否加载系统图片脚本代码）
	
	private int startIdValue = -1;//控件id的初始值(多页模版时用户计算控件id)
	
	private String thisType = "fixed";//模版类型  (fixed) (auto) (mobile) 
	
	private String snType="";//流水号类型
	//20_年,第_号   snInputList代表__的tagId,用于在save时重新设置控件值
	private List<String> snInputList = new ArrayList<String>();
	//20%s年,第%s号  snStrFormat用于生成文件的文件编号
	private String snStrFormat = "";//流水号格式化
	
	private ExcelHeader header;//页眉
	private ExcelHeader footer;//页脚
	private int cellNum;//列数
	
	private String totalOfSumTitle = "0";//sumTitle的总数
	private String totalOfSumSubSec = "0";//sumSubSec的总数
	private String itemNo;//年份单位工程分部子分部分项
	
	private JSONObject keyValue = new JSONObject();//Excel行列 与 id的关联关系

	public Map<String, ExcelColumnBo> getParam() {
		return param;
	}

	public void setParam(Map<String, ExcelColumnBo> param) {
		this.param = param;
	}

	public Map<Integer, ExcelTableTrBo> getTrMap() {
		return trMap;
	}

	public void setTrMap(Map<Integer, ExcelTableTrBo> trMap) {
		this.trMap = trMap;
	}

	public String getExcelTitle() {
		return excelTitle;
	}

	public void setExcelTitle(String excelTitle) {
		this.excelTitle = excelTitle;
	}

	public int getFirstRow() {
		return firstRow;
	}

	public void setFirstRow(int firstRow) {
		this.firstRow = firstRow;
	}

	public int getLastRow() {
		return lastRow;
	}

	public void setLastRow(int lastRow) {
		this.lastRow = lastRow;
	}

	public List<ExcelPictureBo> getPicList() {
		return picList;
	}

	public void setPicList(List<ExcelPictureBo> picList) {
		this.picList = picList;
	}

	public List<PictureStyleBo> getPicStyleList() {
		return picStyleList;
	}

	public void setPicStyleList(List<PictureStyleBo> picStyleList) {
		this.picStyleList = picStyleList;
	}

	public boolean isHasEditor() {
		return hasEditor;
	}

	public void setHasEditor(boolean hasEditor) {
		this.hasEditor = hasEditor;
	}

	public int getStartIdValue() {
		return startIdValue;
	}

	public void setStartIdValue(int startIdValue) {
		this.startIdValue = startIdValue;
	}

	public String getThisType() {
		return thisType;
	}

	public void setThisType(String thisType) {
		this.thisType = thisType;
	}

	public boolean isOldPreform() {
		return isOldPreform;
	}

	public void setOldPreform(boolean isOldPreform) {
		this.isOldPreform = isOldPreform;
	}

	public String getPrintDirc() {
		return printDirc;
	}

	public void setPrintDirc(String printDirc) {
		this.printDirc = printDirc;
	}

	public String getSnType() {
		return snType;
	}

	public void setSnType(String snType) {
		this.snType = snType;
	}

	public List<String> getSnInputList() {
		return snInputList;
	}

	public void setSnInputList(List<String> snInputList) {
		this.snInputList = snInputList;
	}

	public String getSnStrFormat() {
		return snStrFormat;
	}

	public void setSnStrFormat(String snStrFormat) {
		this.snStrFormat = snStrFormat;
	}

	public ExcelHeader getHeader() {
		return header;
	}

	public void setHeader(ExcelHeader header) {
		this.header = header;
	}

	public ExcelHeader getFooter() {
		return footer;
	}

	public void setFooter(ExcelHeader footer) {
		this.footer = footer;
	}

	public int getCellNum() {
		return cellNum;
	}

	public void setCellNum(int cellNum) {
		this.cellNum = cellNum;
	}

	public JSONObject getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(JSONObject keyValue) {
		this.keyValue = keyValue;
	}

	public String getTotalOfSumTitle() {
		return totalOfSumTitle;
	}

	public void setTotalOfSumTitle(String totalOfSumTitle) {
		this.totalOfSumTitle = totalOfSumTitle;
	}

	public String getTotalOfSumSubSec() {
		return totalOfSumSubSec;
	}

	public void setTotalOfSumSubSec(String totalOfSumSubSec) {
		this.totalOfSumSubSec = totalOfSumSubSec;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
}
