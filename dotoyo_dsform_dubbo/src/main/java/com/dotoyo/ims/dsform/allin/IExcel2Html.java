package com.dotoyo.ims.dsform.allin;

import java.util.List;
import java.util.Set;

public interface IExcel2Html {
	
	public List<PreformElm> getPreformElmList();
	
	public Set<RightMenuTag> getRightMenuTagSet();

	public String toHtml(String fileName) throws Exception;

	public String toHtml(String fileName, int sheetIndex) throws Exception;
	
	public String toHtml(ExcelTableBo table) throws Exception;
}
