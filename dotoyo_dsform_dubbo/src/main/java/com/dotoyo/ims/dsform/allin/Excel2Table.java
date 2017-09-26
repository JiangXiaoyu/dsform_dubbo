package com.dotoyo.ims.dsform.allin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.LogFactory;
import org.apache.poi.ddf.EscherClientAnchorRecord;
import org.apache.poi.ddf.EscherRecord;
import org.apache.poi.hssf.record.EscherAggregate;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.dotoyo.dsform.excel.ExcelPictureBo;
import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.util.StringsUtils;


public class Excel2Table implements IExcel2Table {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(Excel2Table.class));

	private ExcelTableBo excelToTable(Workbook workbook, int sheetindex,
			ExcelTableBo table) throws Exception {
		Sheet sheet = workbook.getSheetAt(sheetindex);
		int firstRow = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
		if (lastRowNum > Excel2MdfHtml.MAX_ROW_SUM) {
			// 行数不允许多于10000
			FrameException e = new FrameException("3000000000000045", null);
			throw e;
		}
		table.setFirstRow(firstRow);
		table.setLastRow(lastRowNum);
		
		setPrintRect(workbook,sheetindex,table);
		setTrAndTd(workbook, table, sheet, firstRow, lastRowNum);
		setPic(workbook, table);
        setHeaderAndFooter(workbook, sheetindex,table);
        
		return table;
	}

	//设置tr和td
	private void setTrAndTd(Workbook workbook, ExcelTableBo table, Sheet sheet,
			int firstRow, int lastRowNum) throws Exception {
		RowColumnSpanBo bo = getRowColumnSpanBo(sheet);
		Row row = null;
		// 处理表格
		for (int rowNum = firstRow; rowNum <= lastRowNum; rowNum++) {
			row = sheet.getRow(rowNum);
			ExcelTableTrBo tr = new ExcelTableTrBo();
			
			if (row == null) {
				tr.setRowNum(rowNum);
				tr.setHeight("0px");
				tr.setLastColNum(-1);
				tr.getTdMap().put(0, new ExcelTableTdBo());
				table.getTrMap().put(rowNum, tr);
				continue;
			}
			tr.setHeight((int) (row.getHeightInPoints() * 1.35) + "px");
			row2TableTr(workbook, sheet, row, table, tr, bo);
			table.getTrMap().put(rowNum, tr);
		}
	}

	//设置图片信息
	private void setPic(Workbook workbook, ExcelTableBo table)
			throws FrameException {
		//处理图片
		List<ExcelPictureBo> list = new ArrayList<ExcelPictureBo>();

		HSSFWorkbook hssfWorkbook = (HSSFWorkbook)workbook;
        List<HSSFPictureData> pictureList = hssfWorkbook.getAllPictures();
        List<ClientAnchorInfo> clientAnchorRecords = getClientAnchorRecords((HSSFWorkbook)workbook);
        
        if (pictureList.size() != clientAnchorRecords.size()) {
        	/*for(ClientAnchorInfo cInfo : clientAnchorRecords){
        		System.out.println(cInfo.clientAnchorRecord.getRow1());
        		System.out.println(cInfo.clientAnchorRecord.getCol1());
        	}*/
//            throw new FrameException("3000000000000043",null);
        }
        
        for (int i = 0; i < pictureList.size(); i++) {
            HSSFPictureData pictureData = pictureList.get(i);
           
            ClientAnchorInfo anchor = clientAnchorRecords.get(i);
            HSSFSheet she = anchor.sheet;
            //获得图片sheet号,以便图片排序
            int curSheetIndex = workbook.getSheetIndex(she);
            EscherClientAnchorRecord clientAnchorRecord = anchor.clientAnchorRecord;
            list.add(new ExcelPictureBo((HSSFWorkbook)workbook, she, pictureData, clientAnchorRecord,curSheetIndex));
        }
        if(list.size()!=0){
        	table.setPicList(list);
        }
	}
	
	//设置页眉和页脚
	private void setHeaderAndFooter(Workbook workbook, int sheetindex,
			ExcelTableBo table) {
		//设置列数
		table.setCellNum(getCellNumBySheet(workbook,sheetindex));
		
		Sheet sheet = workbook.getSheetAt(sheetindex);
		Header header = sheet.getHeader();
		if(header != null){
			ExcelHeader excelHeader = new ExcelHeader();
			excelHeader.setHeaderType("header");
			excelHeader.setLeftContent(header.getLeft());
			excelHeader.setCenterContent(header.getCenter());
			excelHeader.setRightContent(header.getRight());
			table.setHeader(excelHeader);
		}
		
		Footer footer = sheet.getFooter();
		if(footer != null){
			ExcelHeader excelFooter = new ExcelHeader();
			excelFooter.setHeaderType("footer");
			excelFooter.setLeftContent(footer.getLeft());
			excelFooter.setCenterContent(footer.getCenter());
			excelFooter.setRightContent(footer.getRight());
			table.setFooter(excelFooter);
		}
		
	}

	//获得指定sheet的列数
	private int getCellNumBySheet(Workbook workbook, int sheetindex){
		Sheet sheet = workbook.getSheetAt(sheetindex);
		int firstRow = sheet.getFirstRowNum();
		Row row = sheet.getRow(firstRow);
		
		if(row != null){
			return row.getLastCellNum() - row.getFirstCellNum();
		}
		return 0;
	}
	
	//设置打印方向
    private void setPrintRect(Workbook workbook, int sheetindex,
			ExcelTableBo table) {
		String name = workbook.getSheetName(sheetindex);
		String printHorz = Excel2MdfHtml.PRINT_HORZ;
		if(name.toLowerCase().indexOf(printHorz) != -1){
			table.setPrintDirc("H");
		}

		String[] array = name.split(",");
		if( array.length == 2 ){
			String snDesc = array[1];
			if(snDesc.indexOf("sn=") != -1){
				String snType = snDesc.substring(3).toLowerCase();
			
				String allSnType = "|sn|yearsn|prefsn|cmsn|mulsn|brsn|brdatasn|brprojsn|secsn|";
				if( snType.indexOf("|") == -1 && allSnType.indexOf(("|" + snType + "|")) != -1 ){
					table.setSnType(snType);//设置编号类型
				}
			}
		}
	}

	private static List<ClientAnchorInfo> getClientAnchorRecords(HSSFWorkbook workbook) {
        List<ClientAnchorInfo> list = new ArrayList<ClientAnchorInfo>();
        
        EscherAggregate drawingAggregate = null;
        HSSFSheet sheet = null;
        List<EscherRecord> recordList = null;
        Iterator<EscherRecord> recordIter = null;
        int numSheets = workbook.getNumberOfSheets();
        for(int i = 0; i < numSheets; i++) {
            sheet = workbook.getSheetAt(i);
            drawingAggregate = sheet.getDrawingEscherAggregate();
            if(drawingAggregate != null) {
                recordList = drawingAggregate.getEscherRecords();
                recordIter = recordList.iterator();
                while(recordIter.hasNext()) {
                    getClientAnchorRecords(sheet, recordIter.next(), 1, list);
                }
            }
        }
        
        return list;
    }
    
    private static void getClientAnchorRecords(HSSFSheet sheet, EscherRecord escherRecord, int level, List<ClientAnchorInfo> list) {
        List<EscherRecord> recordList = null;
        Iterator<EscherRecord> recordIter = null;
        EscherRecord childRecord = null;
        recordList = escherRecord.getChildRecords();
        recordIter = recordList.iterator();
        while(recordIter.hasNext()) {
            childRecord = recordIter.next();
            if(childRecord instanceof EscherClientAnchorRecord) {
                ClientAnchorInfo e = new ClientAnchorInfo(sheet, (EscherClientAnchorRecord) childRecord);
                list.add(e);
            }
            if(childRecord.getChildRecords().size() > 0) {
                getClientAnchorRecords(sheet, childRecord, level+1, list);
            }
        }
    }
	
    private static class ClientAnchorInfo {
        public HSSFSheet sheet;
        public EscherClientAnchorRecord clientAnchorRecord;
	        
        public ClientAnchorInfo(HSSFSheet sheet, EscherClientAnchorRecord clientAnchorRecord) {
            super();
            this.sheet = sheet;
            this.clientAnchorRecord = clientAnchorRecord;
        }
    }

	private void row2TableTr(Workbook workbook, Sheet sheet, Row row,
			ExcelTableBo table, ExcelTableTrBo tr, RowColumnSpanBo bo)
			throws Exception {
		int rowNum = row.getRowNum();
		int lastColNum = row.getLastCellNum();
		if (lastColNum > Excel2MdfHtml.MAX_CELL_SUM) {
			// 列数不允许多于10000
			FrameException e = new FrameException("3000000000000044", null);
			throw e;
		}
		Cell cell = null;
		int bottomeCol = -1;
		int bottomeRow = -1;
		for (int colNum = 0; colNum < lastColNum; colNum++) {
			ExcelTableTdBo td = new ExcelTableTdBo();
			td.setColNum(colNum);
			tr.setLastColNum(lastColNum);
			cell = row.getCell(colNum);
			if (cell == null) {
				td.setContent("&nbsp;");
				tr.getTdMap().put(colNum, td);
				continue;
			}
			//double fix = 0.89;
			if (bo.getRow().containsKey(rowNum + "," + colNum)) {
				String pointString = bo.getRow().get(rowNum + "," + colNum);
				bo.getRow().remove(rowNum + "," + colNum);
				bottomeRow = Integer.valueOf(pointString.split(",")[0]);
				bottomeCol = Integer.valueOf(pointString.split(",")[1]);

				int colSpan = bottomeCol - colNum + 1;
				double tdWidth = PreformUtils.getTdSpanWidth(sheet, colNum, bottomeCol);
				td.setWidth( tdWidth + "px");
				
				int rowSpan = bottomeRow - rowNum + 1;
				if (rowSpan != 1) {
					td.setRowspan(rowSpan + "");
				}
				if (colSpan != 1) {
					td.setColspan(colSpan + "");
				}
			} else if (bo.getCol().containsKey(rowNum + "," + colNum)) {

				bo.getCol().remove(rowNum + "," + colNum);
				bottomeCol = -1;
				bottomeRow = -1;
				continue;

			} else {
				//这里取第一行单元格的宽度  modify by wangl
				//getColumnWidthInPixels : Please note, that this method works correctly only for workbooks with the default font size 
				//(Arial 10pt for .xls and Calibri 11pt for .xlsx). If the default font is changed the column width can be streched
				//如果输出的宽度有问题,那么肯定是设置的默认字体大小的问题
				double tdwidth = sheet.getColumnWidthInPixels(colNum);
				td.setWidth( tdwidth + "px");
				//System.out.println("第一行第" + colNum + "列,宽度为" + tdwidth + "px.");
			}

			row4TdStyle(bo, workbook, sheet, row, cell, bottomeRow, bottomeCol,
					table, tr, td);

			// 处理内容
			String content = content2Html(workbook, sheet, row, cell);
			td.setContent(content);
			bottomeCol = -1;
			bottomeRow = -1;
			tr.getTdMap().put(colNum, td);
		}
	}

	private void row4TdStyle(RowColumnSpanBo bo, Workbook workbook,
			Sheet sheet, Row row, Cell cell, int bottomeRow, int bottomeCol,
			ExcelTableBo table, ExcelTableTrBo tr, ExcelTableTdBo td)
			throws Exception {
		ExcelTableTdStyleBorderBo styleBo = new ExcelTableTdStyleBorderBo();

		CellStyle cellStyle = cell.getCellStyle();
		cellStyle.setWrapText(true);
		
		/*
		 * add by wangl 解析cell 字体的 颜色,大小,黑体,斜体,宋体
		 * 
		 */
		short fontIndex = cellStyle.getFontIndex();
		Font font = workbook.getFontAt(fontIndex);
		
		HSSFPalette palette = ((HSSFWorkbook)workbook).getCustomPalette();
		HSSFColor hc = palette.getColor(font.getColor());

		String fontColorStr = convertToStardColor(hc);
		styleBo.setFontColor(fontColorStr) ;//字体颜色
		
		styleBo.setFontHeight(String.valueOf(font.getFontHeightInPoints()+"pt;"));//字体大小
		//font.getFontHeightInPoints();//字体大小   
		styleBo.setFontName(font.getFontName());//字体 （宋体,黑体）
		styleBo.setFontItalic(String.valueOf(font.getItalic()));//是否斜体

		if (cellStyle != null) {
			String align = alignment2Html(bo, workbook, sheet, row, cell,
					cellStyle);
			td.setAlign(align);
			
			
			String talign = getTextAlign2Html(bo, workbook, sheet, row, cell,
					cellStyle);
			td.setTextAlign(talign);
			
			String vAlign = getVerAlign2Html(bo, workbook, sheet, row, cell,
					cellStyle);
			td.setVerAlign(vAlign);
			
			if (workbook instanceof HSSFWorkbook) {
				IExcelCellStyle2Html html = new ExcelCellStyle4Hssf2Html();
				html.toTable(bo, workbook, sheet, row, cell, bottomeRow,
						bottomeCol, cellStyle, table, tr, td, styleBo);

			} else if (workbook instanceof XSSFWorkbook) {

			}
		}
		td.setBo(styleBo);
	}
	
	private String convertToStardColor(HSSFColor hc) {
		StringBuffer sb = new StringBuffer("");
		if (hc != null) {
			if (HSSFColor.AUTOMATIC.index == hc.getIndex()) {
				return null;
			}
			sb.append("#");
			for (int i = 0; i < hc.getTriplet().length; i++) {
				sb
						.append(fillWithZero(Integer.toHexString(hc
								.getTriplet()[i])));
			}
		}
		return sb.toString();
	}

	private String fillWithZero(String str) {
		if (str != null && str.length() < 2) {
			return "0" + str;
		}
		return str;
	}

	private String alignment2Html(RowColumnSpanBo bo, Workbook workbook,
			Sheet sheet, Row row, Cell cell, CellStyle cellStyle)
			throws Exception {

		StringBuffer sb = new StringBuffer();
		// 处理排序
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {

			short alignment = cellStyle.getAlignment();
			if (cellStyle.getAlignment() > 0) {
				sb.append(" align='" + convertAlignToHtml(alignment) + "'");
			} else {
				sb.append(" align='right'");
			}
		} else {
			short alignment = cellStyle.getAlignment();
			if (cellStyle.getAlignment() > 0) {
				sb.append(" align='" + convertAlignToHtml(alignment) + "'");
			}

		}
		short verticalAlignment = cellStyle.getVerticalAlignment();
		//if (verticalAlignment > 0) {
		//垂直居中居上时,verticalAlignment为0
		if (verticalAlignment > -1) {
			sb.append(" valign='"
					+ convertVerticalAlignToHtml(verticalAlignment) + "' ");
		}
		return sb.toString();
	}
	
	private String getTextAlign2Html(RowColumnSpanBo bo, Workbook workbook,
			Sheet sheet, Row row, Cell cell, CellStyle cellStyle)
			throws Exception {
		String textAlign = "right";
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			short alignment = cellStyle.getAlignment();
			if (cellStyle.getAlignment() > 0) {
				textAlign = convertAlignToHtml(alignment);
			} 
		} else {
			short alignment = cellStyle.getAlignment();
			if (cellStyle.getAlignment() > 0) {
				textAlign = convertAlignToHtml(alignment);
			}

		}
		return textAlign;
	}
	
	private String getVerAlign2Html(RowColumnSpanBo bo, Workbook workbook,
			Sheet sheet, Row row, Cell cell, CellStyle cellStyle)
			throws Exception {
		String vAlign = "right";
		short verticalAlignment = cellStyle.getVerticalAlignment();
		if (verticalAlignment > 0) {
			vAlign = convertVerticalAlignToHtml(verticalAlignment);
		}
		return vAlign;
	}

	private String content2Html(Workbook workbook, Sheet sheet, Row row,
			Cell cell) throws Exception {
		StringBuffer sb = new StringBuffer();
		IExcelCellContent2Html html = null;
		String ret = "";
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			html = new ExcelCellContent4String2Html();

			break;
		case Cell.CELL_TYPE_NUMERIC:
			html = new ExcelCellContent4Number2Html();

			break;
		case Cell.CELL_TYPE_BOOLEAN:
			ret = String.valueOf(cell.getBooleanCellValue());
			sb.append(ret);
			break;
		case Cell.CELL_TYPE_BLANK:
			ret = "";
			sb.append(ret);
			break;
		default:
			break;
		}
		if (html != null) {
			String stringValue = html.toHtml(cell);
			sb.append(stringValue);
		}
		return sb.toString();
	}

	/**
	 * 取得行与列合并Map
	 * 
	 * @param sheet
	 * @return
	 */
	private RowColumnSpanBo getRowColumnSpanBo(Sheet sheet) {

		RowColumnSpanBo bo = new RowColumnSpanBo();

		int mergedNum = sheet.getNumMergedRegions();

		CellRangeAddress range = null;

		for (int i = 0; i < mergedNum; i++) {

			range = sheet.getMergedRegion(i);

			int topRow = range.getFirstRow();

			int topCol = range.getFirstColumn();

			int bottomRow = range.getLastRow();

			int bottomCol = range.getLastColumn();

			bo.getRow().put(topRow + "," + topCol, bottomRow + "," + bottomCol);

			int tempRow = topRow;

			while (tempRow <= bottomRow) {

				int tempCol = topCol;

				while (tempCol <= bottomCol) {

					bo.getCol().put(tempRow + "," + tempCol, "");

					tempCol++;
				}

				tempRow++;
			}

			bo.getCol().remove(topRow + "," + topCol);

		}

		return bo;
	}

	private String convertAlignToHtml(short alignment) {

		String align = "";

		switch (alignment) {
	
			case CellStyle.ALIGN_LEFT:
				align = "left";
				break;
			case CellStyle.ALIGN_CENTER:
				align = "center";
				break;
			case CellStyle.ALIGN_RIGHT:
				align = "right";
				break;
	
			default:
				break;
		}
		return align;
	}

	private String convertVerticalAlignToHtml(short verticalAlignment) {

		String valign = "";

		switch (verticalAlignment) {
			case CellStyle.VERTICAL_BOTTOM:
				valign = "bottom";
				break;
			case CellStyle.VERTICAL_CENTER:
				valign = "middle";
				break;
			case CellStyle.VERTICAL_TOP:
				valign = "top";
				break;
			case CellStyle.VERTICAL_JUSTIFY:
				valign = "baseline";
				break;
			default:
				break;
		}
		return valign;
	}

	/**
	 * 获取Cell的内容
	 * 
	 * @param cell
	 * @return
	 */
	private String getStringCellValue(Cell cell) {
		String cellvalue = "";
		if (cell == null) {
			return "";
		}
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				cellvalue = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_NUMERIC:
				cellvalue = String.valueOf(cell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				cellvalue = String.valueOf(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_BLANK:
				cellvalue = "";
				break;
			default:
				cellvalue = "";
				break;
		}
		if (StringsUtils.isEmpty(cellvalue)) {
			return "";
		}
		return cellvalue;
	}
	
	private Workbook getExcelWorkBook(InputStream is) throws Exception {
		Workbook book = null;
		try {
			book = WorkbookFactory.create(is);
		}catch(IllegalArgumentException e){
			throw new FrameException("3000000000000038",null);
		}finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
				}
			}
		}
		return book;
	}

	/**
	 * 获取某Sheet，第一行的内容
	 * 
	 * @param workbook
	 * @param sheetindex
	 * @return
	 * @throws FrameException 
	 */
	private String getFirstRowContent(Workbook workbook, int sheetindex) throws FrameException {
		String exceltitle = "";
		if (workbook != null) {
			Sheet sheet = workbook.getSheetAt(sheetindex);
			int firstrownum = sheet.getFirstRowNum();
			Row row = sheet.getRow(firstrownum);
			if(row == null){
				throw new FrameException("3000000000000039",null);
			}
			short fcellnum = row.getFirstCellNum();
			short lcellnum = row.getLastCellNum();
			for (int j = fcellnum; j < lcellnum; j++) {
				Cell cell = row.getCell(j);
				exceltitle += getStringCellValue(cell);
			}
		}
		return exceltitle;
	}

	public ExcelTableBo toTable(String fileName, int sheetIndex)
			throws Exception {

		String filepath = fileName;
		Workbook workbook = null;

		workbook = PreformUtils.getExcelWorkBook(filepath);
		String excelTitle = getFirstRowContent(workbook, sheetIndex);
		ExcelTableBo table = new ExcelTableBo();
		table.setExcelTitle(excelTitle);
		return excelToTable(workbook, sheetIndex, table);
	}
	
	public ExcelTableBo toTable(InputStream is, int sheetIndex)
			throws Exception {
		Workbook workbook = getExcelWorkBook(is);
		
		String excelTitle = getFirstRowContent(workbook, sheetIndex);
		ExcelTableBo table = new ExcelTableBo();
		table.setExcelTitle(excelTitle);
		return excelToTable(workbook, sheetIndex, table);
	}

	public static void main2(String args[]) throws Exception {
		IExcel2Html html = new Excel2ViewHtml();
		Excel2Table eu = new Excel2Table();
		ExcelTableBo table = eu.toTable(
				"D:/wangl/预定义表单文档/02 监理单位用表/09工程材料、构配件、设备报审表GD220209.xls", 0);
		String htmlbuf = html.toHtml(table);
		FileOutputStream fos = null;
		try {
			File file = new File("D:/wangl/09工程材料、构配件、设备报审表GD220209.html");
			fos = new FileOutputStream(file);
			fos.write(htmlbuf.getBytes("utf-8"));

			fos.flush();
			fos.close();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
