package com.dotoyo.ims.dsform.allin;

import java.text.DecimalFormat;
import java.util.List;

public class ExcelCellContent4Number2Html implements IExcelCellContent2Html {

	@Override
	public PreformElm getPreformElm() {
		return null;
	}
	
	@Override
	public List<RightMenuTag> getRightMenuList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toHtml(org.apache.poi.ss.usermodel.Cell cell)
			throws Exception {
		StringBuilder sb = new StringBuilder();
		DecimalFormat format = new DecimalFormat("#0.##");

		String ret = format.format(cell.getNumericCellValue());
		sb.append(ret);
		return sb.toString();
	}
	
	public String toHtml(String content) throws Exception{
		return "";
	}
	
	@Override
	public String toHtml(ExcelTableBo table, ExcelTableTrBo tr,
			ExcelTableTdBo td) throws Exception {
		return null;
	}
}
