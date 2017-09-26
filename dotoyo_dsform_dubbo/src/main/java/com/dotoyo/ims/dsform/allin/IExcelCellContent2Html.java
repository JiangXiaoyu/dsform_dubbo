package com.dotoyo.ims.dsform.allin;

import java.util.List;

public interface IExcelCellContent2Html {
	/*
	 * 只有可修改html的getPreformElm()有值
	 */
	public PreformElm getPreformElm() ;
	
	public List<RightMenuTag> getRightMenuList();
	
	public String toHtml(org.apache.poi.ss.usermodel.Cell cell) throws Exception;
	
	public String toHtml(ExcelTableBo table, ExcelTableTrBo tr,ExcelTableTdBo td) throws Exception;
}
