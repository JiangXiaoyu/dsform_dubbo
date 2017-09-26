package com.dotoyo.ims.dsform.allin;

/*
 * 	图片样式对象
 */
public class PictureStyleBo implements Comparable<PictureStyleBo>{
	private int sheetIndex = 0;//sheet index (多页表单要排序)
	
	private String id;//图片附件id
	
	private int top;//上偏移量
	
	private int left;//左偏移量
	
	private double width;//宽度
	
	private double height;//高度

	private String mdfStatus = "0";//1表示已经被使用
	
	private String viewStatus = "0";//1表示已经被使用
	
	public PictureStyleBo() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public String getMdfStatus() {
		return mdfStatus;
	}

	public void setMdfStatus(String status) {
		this.mdfStatus = status;
	}

	public String getViewStatus() {
		return viewStatus;
	}

	public void setViewStatus(String status2) {
		this.viewStatus = status2;
	}

	public int getSheetIndex() {
		return sheetIndex;
	}

	public void setSheetIndex(int sheetIndex) {
		this.sheetIndex = sheetIndex;
	}

	@Override
	public int compareTo(PictureStyleBo obj) {
		if(obj.getSheetIndex() < this.getSheetIndex()){
			return 1;
		}else if(obj.getSheetIndex() > this.getSheetIndex()){
			return -1;
		}else{
			if(obj.getSheetIndex() < this.getSheetIndex()){
				return 1;
			}else if(obj.getSheetIndex() > this.getSheetIndex()){
				return -1;
			}else{
				if(obj.getTop() < this.getTop()){
					return 1;
				}else if(obj.getTop() > this.getTop()){
					return -1;
				}else{
					if(obj.getLeft() < this.getLeft()){
						return 1;
					}else if(obj.getLeft() > this.getLeft()){
						return -1;
					}else{
						return 0;
					}
				}
			}
		}
	}
}
