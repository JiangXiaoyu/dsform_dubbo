package com.dotoyo.ims.dsform.allin;

import java.io.InputStream;

public interface IExcel2Table {


	public ExcelTableBo toTable(String fileName, int sheetIndex)
			throws Exception;
	
	public ExcelTableBo toTable(InputStream is, int sheetIndex)
			throws Exception;

}
