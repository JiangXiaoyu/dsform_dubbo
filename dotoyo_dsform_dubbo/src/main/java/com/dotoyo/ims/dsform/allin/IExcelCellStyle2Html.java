package com.dotoyo.ims.dsform.allin;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

public interface IExcelCellStyle2Html {
	public String toHtml(RowColumnSpanBo bo, Workbook workbook,
			org.apache.poi.ss.usermodel.Sheet sheet, Row row, Cell cell,
			int bottomeRow, int bottomeCol, CellStyle cellStyle)
			throws Exception;

	public void toTable(RowColumnSpanBo bo, Workbook workbook,
			org.apache.poi.ss.usermodel.Sheet sheet, Row row, Cell cell,
			int bottomeRow, int bottomeCol, CellStyle cellStyle,
			ExcelTableBo table, ExcelTableTrBo tr, ExcelTableTdBo td,ExcelTableTdStyleBorderBo styleBo)
			throws Exception;
}
