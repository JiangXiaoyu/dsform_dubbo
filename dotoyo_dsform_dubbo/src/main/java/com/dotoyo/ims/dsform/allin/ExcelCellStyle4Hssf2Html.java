package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.util.StringsUtils;

public class ExcelCellStyle4Hssf2Html implements IExcelCellStyle2Html {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(ExcelCellStyle4Hssf2Html.class));

	@Override
	public String toHtml(RowColumnSpanBo bo, Workbook wb, Sheet s, Row r,
			Cell c, int br, int bc, CellStyle cs) throws Exception {
		HSSFWorkbook workbook = (HSSFWorkbook) wb;
		HSSFSheet sheet = (HSSFSheet) s;
		HSSFRow row = (HSSFRow) r;
		HSSFCell cell = (HSSFCell) c;
		HSSFRow bottomeRow = br == -1 ? null : (HSSFRow) sheet.getRow(br);
		HSSFCell bottomeCol = bottomeRow == null ? null : bottomeRow
				.getCell(bc);
		HSSFCellStyle cellStyle = (HSSFCellStyle) cs;
		StringBuilder sb = new StringBuilder();
		sb.append("style='");

		HSSFPalette palette = workbook.getCustomPalette(); // 类HSSFPalette用于求的颜色的国际标准形式

		String font = toStyle4FontHtml(workbook, sheet, row, cell, cellStyle,
				palette);
		sb.append(font);
		String border = toStyle4BorderHtml(bo, workbook, sheet, row, cell,
				bottomeRow, bottomeCol, cellStyle, palette);
		sb.append(border);
		sb.append("' ");
		return sb.toString();
	}

	private String toStyle4FontHtml(HSSFWorkbook workbook, HSSFSheet s,
			HSSFRow row, HSSFCell c, HSSFCellStyle cellStyle,
			HSSFPalette palette) throws Exception {
		StringBuilder sb = new StringBuilder();
		HSSFFont hf = cellStyle.getFont(workbook);
		short fontColor = hf.getColor();
		HSSFColor hc = palette.getColor(fontColor);
		// 字体大小
		if (hf.getFontHeightInPoints() > 0) {
			sb.append("font-size: " + hf.getFontHeightInPoints() + "pt;");
		}
		if (!StringsUtils.isEmpty(hf.getFontName())) {
			sb.append("font-family:" + hf.getFontName() + ";");
		}
		if (hf.getBoldweight() > 0) {
			sb.append("font-weight:" + hf.getBoldweight() + ";");
		}
		if (hf.getItalic()) {
			sb.append("font-style:italic;");
		}

		String fontColorStr = convertToStardColor(hc);

		if (fontColorStr != null && !"".equals(fontColorStr.trim())) {

			sb.append("color:" + fontColorStr + ";"); // 字体颜色
		}
		return sb.toString();
	}
	private String toStyle4FontHtml(HSSFWorkbook workbook, HSSFSheet s,
			HSSFRow row, HSSFCell c, HSSFCellStyle cellStyle,
			HSSFPalette palette,ExcelTableTdBo td) throws Exception {
		StringBuilder sb = new StringBuilder();
		HSSFFont hf = cellStyle.getFont(workbook);
		short fontColor = hf.getColor();
		HSSFColor hc = palette.getColor(fontColor);
		// 字体大小
		if (hf.getFontHeightInPoints() > 0) {
			sb.append("font-size: " + hf.getFontHeightInPoints() + "pt;");
			td.setFs(hf.getFontHeightInPoints());
		}
		if (!StringsUtils.isEmpty(hf.getFontName())) {
			sb.append("font-family:" + hf.getFontName() + ";");
		}
		if (hf.getBoldweight() > 0) {
			sb.append("font-weight:" + hf.getBoldweight() + ";");
		}
		if (hf.getItalic()) {
			sb.append("font-style:italic;");
		}

		String fontColorStr = convertToStardColor(hc);

		if (fontColorStr != null && !"".equals(fontColorStr.trim())) {

			sb.append("color:" + fontColorStr + ";"); // 字体颜色
		}
		return sb.toString();
	}
	private String toStyle4BorderHtml(RowColumnSpanBo bo, HSSFWorkbook wb,
			HSSFSheet s, HSSFRow row, HSSFCell c, HSSFRow bottomeRow,
			HSSFCell bottome, HSSFCellStyle cellStyle, HSSFPalette palette)
			throws Exception {

/*		if (c.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			log.debug(c.getNumericCellValue());
		}
		if (c.getCellType() == Cell.CELL_TYPE_STRING) {
			if (c.getStringCellValue().indexOf("单位(子单位)工程名称") != -1) {
				log.debug("");
			}
		}*/

		StringBuilder sb = new StringBuilder();
		short bgColor = cellStyle.getFillForegroundColor();

		HSSFColor hc = palette.getColor(bgColor);

		String bgColorStr = convertToStardColor(hc);

		if (!StringsUtils.isEmpty(bgColorStr)) {

			sb.append("background-color:" + bgColorStr + ";"); // 背景颜色
		}
		//
		String lKey = (bottomeRow == null ? row.getRowNum() : bottomeRow
				.getRowNum())
				+ ","
				+ c.getColumnIndex()
				+ ","
				+ (bottome == null ? c.getColumnIndex() : bottome
						.getColumnIndex());

		if (!bo.getBorderBottom().containsKey(lKey)) {
			String top = toStyle4BorderTopHtml(wb, s, row, c, bottomeRow,
					bottome, cellStyle, palette);
			sb.append(top);
		}
		//
		String bottom = toStyle4BorderBottomHtml(wb, s, row, c, bottomeRow,
				bottome, cellStyle, palette);
		if (!StringsUtils.isEmpty(bottom)) {
			String lKey1 = (((bottomeRow == null ? row.getRowNum() : bottomeRow
					.getRowNum())) + 1)
					+ ","
					+ c.getColumnIndex()
					+ ","
					+ (bottome == null ? c.getColumnIndex() : bottome
							.getColumnIndex());
			bo.getBorderBottom().put(lKey1, "");
			sb.append(bottom);
		}
		//
		String hKey = row.getRowNum()
				+ ","
				+ c.getColumnIndex()
				+ ","
				+ (bottomeRow == null ? row.getRowNum() : bottomeRow
						.getRowNum());
		if (!bo.getBorderRigth().containsKey(hKey)) {
			String left = toStyle4BorderLeftHtml(wb, s, row, c, bottomeRow,
					bottome, cellStyle, palette);
			sb.append(left);
		}
		//
		String right = toStyle4BorderRightHtml(wb, s, row, c, bottomeRow,
				bottome, cellStyle, palette);
		if (!StringsUtils.isEmpty(right)) {
			String hKey1 = row.getRowNum()
					+ ","
					+ ((bottome == null ? c.getColumnIndex() : bottome
							.getColumnIndex()) + 1)
					+ ","
					+ (bottomeRow == null ? row.getRowNum() : bottomeRow
							.getRowNum());
			bo.getBorderRigth().put(hKey1, "");
			sb.append(right);
		}
		//
		return sb.toString();
	}

	private String toStyle4BorderTopHtml(HSSFWorkbook wb, HSSFSheet s,
			HSSFRow row, HSSFCell c, HSSFRow bottomeRow, HSSFCell bottome,
			HSSFCellStyle cellStyle, HSSFPalette palette) throws Exception {
		StringBuilder sb = new StringBuilder();
		//
		short bordertop = cellStyle.getBorderTop();
		if (bottome != null) {

			for (int i = c.getColumnIndex(); i < bottome.getColumnIndex(); i++) {
				CellStyle cs = row.getCell(i).getCellStyle();
				if (cs != null) {
					if (cs.getBorderTop() != bordertop) {
						return "";
					}
				}
			}

		}

		if (bordertop > 0) {
			short colortop = cellStyle.getTopBorderColor();

			String boderstyler = this.convertBorderStyle2Html(bordertop,
					colortop);
			if (!StringsUtils.isEmpty(boderstyler)) {
				sb.append("border-top:" + boderstyler + ";");
			}
		}

		return sb.toString();
	}

	private String toStyle4BorderLeftHtml(HSSFWorkbook wb, HSSFSheet s,
			HSSFRow row, HSSFCell c, HSSFRow bottomeRow, HSSFCell bottome,
			HSSFCellStyle cellStyle, HSSFPalette palette) throws Exception {
		StringBuilder sb = new StringBuilder();
		short borderleft = cellStyle.getBorderLeft();
		if (bottome != null) {

			for (int i = row.getRowNum(); i < bottomeRow.getRowNum(); i++) {
				HSSFRow row1 = s.getRow(i);
				CellStyle cs = row1.getCell(c.getColumnIndex()).getCellStyle();
				if (cs != null) {
					if (cs.getBorderLeft() != borderleft) {
						return "";
					}
				}
			}

		}

		if (borderleft > 0) {

			short colorleft = cellStyle.getLeftBorderColor();
			String boderstyler = this.convertBorderStyle2Html(borderleft,
					colorleft);
			if (!StringsUtils.isEmpty(boderstyler)) {
				sb.append("border-left:" + boderstyler + ";");
			}
		}
		return sb.toString();
	}

	private String toStyle4BorderRightHtml(HSSFWorkbook wb, HSSFSheet s,
			HSSFRow row, HSSFCell c, HSSFRow bottomeRow, Cell bottome,
			HSSFCellStyle cellStyle, HSSFPalette palette) throws Exception {
		StringBuilder sb = new StringBuilder();
		if (bottome != null) {

			CellStyle cs = bottome.getCellStyle();
			if (cs != null) {
				short borderright = cs.getBorderRight();
				if (bottome != null) {

					for (int i = row.getRowNum(); i < bottomeRow.getRowNum(); i++) {
						HSSFRow row1 = s.getRow(i);
						CellStyle cs1 = row1.getCell(bottome.getColumnIndex())
								.getCellStyle();
						if (cs1 != null) {
							if (cs1.getBorderRight() != borderright) {
								return "";
							}
						}
					}

				}

				short colorright = cs.getRightBorderColor();

				String boderstyler = this.convertBorderStyle2Html(borderright,
						colorright);
				if (!StringsUtils.isEmpty(boderstyler)) {
					sb.append("border-right:" + boderstyler + ";");
				}

			}

		} else {
			short borderRight = cellStyle.getBorderRight();
			if (borderRight > 0) {

				short colorright = cellStyle.getRightBorderColor();
				String boderstyler = this.convertBorderStyle2Html(borderRight,
						colorright);
				if (!StringsUtils.isEmpty(boderstyler)) {
					sb.append("border-right:" + boderstyler + ";");
				}
			}
		}
		return sb.toString();
	}

	private String toStyle4BorderBottomHtml(HSSFWorkbook wb, HSSFSheet s,
			HSSFRow row, HSSFCell c, HSSFRow bottomeRow, HSSFCell bottome,
			HSSFCellStyle cellStyle, HSSFPalette palette) throws Exception {
		StringBuilder sb = new StringBuilder();

		if (bottome != null) {
			CellStyle cs = bottome.getCellStyle();
			if (cs != null) {
				short borderbottom = cs.getBorderBottom();

				for (int i = c.getColumnIndex(); i < bottome.getColumnIndex(); i++) {
					CellStyle cs1 = bottomeRow.getCell(i).getCellStyle();
					if (cs1 != null) {
						if (cs.getBorderBottom() != borderbottom) {
							return "";
						}
					}
				}

				short colorbottom = cs.getBottomBorderColor();
				String boderstyler = this.convertBorderStyle2Html(borderbottom,
						colorbottom);
				if (!StringsUtils.isEmpty(boderstyler)) {
					sb.append("border-bottom:" + boderstyler + ";");
				}
			}
		} else {

			short borderbottom = cellStyle.getBorderBottom();
			if (borderbottom > 0) {
				short colorbottom = cellStyle.getBottomBorderColor();

				String boderstyler = this.convertBorderStyle2Html(borderbottom,
						colorbottom);
				if (!StringsUtils.isEmpty(boderstyler)) {
					sb.append("border-bottom:" + boderstyler + ";");
				}

			}
		}
		return sb.toString();
	}

	private boolean isTheSameTop(HSSFWorkbook wb, HSSFSheet s, HSSFRow row,
			HSSFCell c, HSSFCell bottome, HSSFCellStyle cellStyle,
			HSSFPalette palette) throws Exception {
		if (bottome == null) {
			return true;
		}
		short borderbottom = cellStyle.getBorderTop();
		int startIndex = c.getColumnIndex();
		int lastIndex = bottome.getColumnIndex();
		for (int i = startIndex; i < lastIndex; i++) {
			if (borderbottom != row.getCell(startIndex).getCellStyle()
					.getBorderTop()) {
				return false;
			}
		}
		return true;
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

	private String convertBorderStyle2Html(short borderType, short colorType) {
		String html = "none";

		switch (borderType) {
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_NONE:
			html = "none";
			break;
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_THIN:
			html = "1px solid " + convertBorderColor2Html(colorType);
			break;
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_MEDIUM:
			html = "2px solid " + convertBorderColor2Html(colorType);//黑边框稍微颜色变淡点
			break;
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_DASHED:
			html = "1px dashed " + convertBorderColor2Html(colorType);
			break;
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_HAIR:
			html = "1px solid " + convertBorderColor2Html(colorType);
			break;
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_THICK:
			html = "5px solid " + convertBorderColor2Html(colorType);
			break;
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_DOUBLE:
			html = "double solid " + convertBorderColor2Html(colorType);
			break;
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_DOTTED:
			html = "1px dotted " + convertBorderColor2Html(colorType);
			break;
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_MEDIUM_DASHED:
			html = "3px dashed " + convertBorderColor2Html(colorType);
			break;
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_DASH_DOT:
			html = "1px solid " + convertBorderColor2Html(colorType);
			break;
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_MEDIUM_DASH_DOT:
			html = "3px solid " + convertBorderColor2Html(colorType);
			break;
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_DASH_DOT_DOT:
			html = "1px solid " + convertBorderColor2Html(colorType);
			break;
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_MEDIUM_DASH_DOT_DOT:
			html = "3px solid " + convertBorderColor2Html(colorType);
			break;
		case org.apache.poi.ss.usermodel.CellStyle.BORDER_SLANTED_DASH_DOT:
			html = "1px solid " + convertBorderColor2Html(colorType);
			break;
		default:
			break;
		}

		return html;
	}

	private String convertBorderColor2Html(short bordercolor) {
		String type = "black";

		switch (bordercolor) {
		case org.apache.poi.hssf.util.HSSFColor.AUTOMATIC.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.LIGHT_CORNFLOWER_BLUE.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.ROYAL_BLUE.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.CORAL.index:
			type = "coral";
			break;
		case org.apache.poi.hssf.util.HSSFColor.ORCHID.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.MAROON.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.LEMON_CHIFFON.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.CORNFLOWER_BLUE.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.WHITE.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.LAVENDER.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.PALE_BLUE.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.LIGHT_TURQUOISE.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.LIGHT_GREEN.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.LIGHT_YELLOW.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.TAN.index:
			type = "tan";
			break;
		case org.apache.poi.hssf.util.HSSFColor.ROSE.index:
			type = "rose";
			break;
		case org.apache.poi.hssf.util.HSSFColor.GREY_25_PERCENT.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.PLUM.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.SKY_BLUE.index:
			type = "blue";
			break;
		case org.apache.poi.hssf.util.HSSFColor.TURQUOISE.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.BRIGHT_GREEN.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.YELLOW.index:
			type = "yellow";
			break;
		case org.apache.poi.hssf.util.HSSFColor.GOLD.index:
			type = "gold";
			break;
		case org.apache.poi.hssf.util.HSSFColor.PINK.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.GREY_40_PERCENT.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.VIOLET.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.LIGHT_BLUE.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.AQUA.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.SEA_GREEN.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.LIME.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.LIGHT_ORANGE.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.RED.index:
			type = "red";
			break;
		case org.apache.poi.hssf.util.HSSFColor.GREY_50_PERCENT.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.BLUE_GREY.index:
			type = "grey";
			break;
		case org.apache.poi.hssf.util.HSSFColor.BLUE.index:
			type = "blue";
			break;
		case org.apache.poi.hssf.util.HSSFColor.TEAL.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.GREEN.index:
			type = "green";
			break;
		case org.apache.poi.hssf.util.HSSFColor.DARK_YELLOW.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.ORANGE.index:
			type = "orange";
			break;
		case org.apache.poi.hssf.util.HSSFColor.DARK_RED.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.GREY_80_PERCENT.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.INDIGO.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.DARK_BLUE.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.DARK_TEAL.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.DARK_GREEN.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.OLIVE_GREEN.index:
			type = "black";
			break;
		case org.apache.poi.hssf.util.HSSFColor.BROWN.index:
			type = "brown";
			break;
		case org.apache.poi.hssf.util.HSSFColor.BLACK.index:
			type = "black";
			break;
		default:
			break;
		}

		return type;
	}

	@Override
	public void toTable(RowColumnSpanBo bo, Workbook wb, Sheet s, Row r,
			Cell c, int br, int bc, CellStyle cs, ExcelTableBo table,
			ExcelTableTrBo tr, ExcelTableTdBo td,
			ExcelTableTdStyleBorderBo styleBo) throws Exception {
		HSSFWorkbook workbook = (HSSFWorkbook) wb;
		HSSFSheet sheet = (HSSFSheet) s;
		HSSFRow row = (HSSFRow) r;
		HSSFCell cell = (HSSFCell) c;
		HSSFRow bottomeRow = br == -1 ? null : (HSSFRow) sheet.getRow(br);
		HSSFCell bottomeCol = bottomeRow == null ? null : bottomeRow
				.getCell(bc);
		HSSFCellStyle cellStyle = (HSSFCellStyle) cs;

		HSSFPalette palette = workbook.getCustomPalette(); // 类HSSFPalette用于求的颜色的国际标准形式

		String font = toStyle4FontHtml(workbook, sheet, row, cell, cellStyle,
				palette,td);
		styleBo.setFont(font);
	
		//
		short bgColor = cellStyle.getFillForegroundColor();

		HSSFColor hc = palette.getColor(bgColor);

		String bgColorStr = convertToStardColor(hc);

		if (!StringsUtils.isEmpty(bgColorStr)) {

			styleBo.setColor("background-color:" + bgColorStr + ";"); // 背景颜色
		}

		toStyle4Border(bo, workbook, sheet, row, cell, bottomeRow, bottomeCol,
				cellStyle, palette, table, tr, td, styleBo);

	}

	private void toStyle4Border(RowColumnSpanBo bo, HSSFWorkbook wb,
			HSSFSheet s, HSSFRow row, HSSFCell c, HSSFRow bottomeRow,
			HSSFCell bottome, HSSFCellStyle cellStyle, HSSFPalette palette,
			ExcelTableBo table, ExcelTableTrBo tr, ExcelTableTdBo td,
			ExcelTableTdStyleBorderBo styleBo) throws Exception {

/*		if (c.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			log.debug(c.getNumericCellValue());
		}
		if (c.getCellType() == Cell.CELL_TYPE_STRING) {
			if (c.getStringCellValue().indexOf("本系统设计总") != -1) {
				log.debug("");
			}
		}*/
		toStyle4BorderTop(wb, s, row, c, bottomeRow, bottome, cellStyle,
				palette, table, tr, td, styleBo);
		toStyle4BorderLeft(wb, s, row, c, bottomeRow, bottome, cellStyle,
				palette, table, tr, td, styleBo);
		toStyle4BorderBottom(wb, s, row, c, bottomeRow, bottome, cellStyle,
				palette, table, tr, td, styleBo);
		toStyle4BorderRight(wb, s, row, c, bottomeRow, bottome, cellStyle,
				palette, table, tr, td, styleBo);
	}

	private void toStyle4BorderTop(HSSFWorkbook wb, HSSFSheet s, HSSFRow row,
			HSSFCell c, HSSFRow bottomeRow, HSSFCell bottome,
			HSSFCellStyle cellStyle, HSSFPalette palette, ExcelTableBo table,
			ExcelTableTrBo tr, ExcelTableTdBo td,
			ExcelTableTdStyleBorderBo styleBo) throws Exception {

		//
		short bordertop = cellStyle.getBorderTop();
		if (bordertop > 0) {
			short colortop = cellStyle.getTopBorderColor();

			String boderstyler = this.convertBorderStyle2Html(bordertop,
					colortop);
			if (!StringsUtils.isEmpty(boderstyler)) {
				styleBo.setBorderTop("border-top:" + boderstyler + ";");
//				int count = (bottome == null ? 1 : bottome.getColumnIndex()
//						-  c.getColumnIndex()+1);
//				for (int i = 0; i < count; i++) {
//					ExcelTableTdBo bTd = tr.getTdMap().get(
//							tr.getTdMap().size()-1 - i);
//					if (bTd != null) {
//						ExcelTableTdStyleBorderBo bStyle = bTd.getBo();
//						if (bStyle != null) {
//							bStyle.setBorderBottom("");
//						}
//					}
//				}
			}

		}

	}

	private void toStyle4BorderLeft(HSSFWorkbook wb, HSSFSheet s, HSSFRow row,
			HSSFCell c, HSSFRow bottomeRow, HSSFCell bottome,
			HSSFCellStyle cellStyle, HSSFPalette palette, ExcelTableBo table,
			ExcelTableTrBo tr, ExcelTableTdBo td,
			ExcelTableTdStyleBorderBo styleBo) throws Exception {

		//
		short borderleft = cellStyle.getBorderLeft();

		if (borderleft > 0) {

			short colorleft = cellStyle.getLeftBorderColor();
			String boderstyler = this.convertBorderStyle2Html(borderleft,
					colorleft);
			if (!StringsUtils.isEmpty(boderstyler)) {

				styleBo.setBorderLeft("border-left:" + boderstyler + ";");
//				int count = (bottomeRow == null ? 1 : bottomeRow.getRowNum()
//						-  row.getRowNum()+1);
//				for (int i = 0; i < count; i++) {
//					ExcelTableTdBo bTd = tr.getTdMap().get(
//							tr.getTdMap().size()-1 - i);
//					if (bTd != null) {
//						ExcelTableTdStyleBorderBo bStyle = bTd.getBo();
//						if (bStyle != null) {
//							bStyle.setBorderRight("");
//						}
//					}
//				}

			}
		}

	}

	private void toStyle4BorderRight(HSSFWorkbook wb, HSSFSheet s, HSSFRow row,
			HSSFCell c, HSSFRow bottomeRow, Cell bottome,
			HSSFCellStyle cellStyle, HSSFPalette palette, ExcelTableBo table,
			ExcelTableTrBo tr, ExcelTableTdBo td,
			ExcelTableTdStyleBorderBo styleBo) throws Exception {
		if (bottome != null) {

			CellStyle cs = bottome.getCellStyle();
			if (cs != null) {
				short borderright = cs.getBorderRight();
				if (bottome != null) {

					for (int i = row.getRowNum(); i < bottomeRow.getRowNum(); i++) {
						HSSFRow row1 = s.getRow(i);
						if(row1.getCell(bottome.getColumnIndex()) == null){
							Map<String,String> map = new HashMap<String,String>();
							map.put("row", i+1+"");
							map.put("cell", bottome.getColumnIndex() + 1+"");
							throw new FrameException("3000000000000050",map);
						}
						CellStyle cs1 = row1.getCell(bottome.getColumnIndex())
								.getCellStyle();
						if (cs1 != null) {
							if (cs1.getBorderRight() != borderright) {
								styleBo.setBorderRight("");
							}
						}
					}

				}

				short colorright = cs.getRightBorderColor();

				String boderstyler = this.convertBorderStyle2Html(borderright,
						colorright);
				if (!StringsUtils.isEmpty(boderstyler)) {
					styleBo.setBorderRight("border-right:" + boderstyler + ";");
				}

			}

		} else {
			//

			short borderRight = cellStyle.getBorderRight();
			if (borderRight > 0) {

				short colorright = cellStyle.getRightBorderColor();
				String boderstyler = this.convertBorderStyle2Html(borderRight,
						colorright);
				if (!StringsUtils.isEmpty(boderstyler)) {
					styleBo.setBorderRight("border-right:" + boderstyler + ";");
				}
			}
		}
		//

	}

	private void toStyle4BorderBottom(HSSFWorkbook wb, HSSFSheet s,
			HSSFRow row, HSSFCell c, HSSFRow bottomeRow, HSSFCell bottome,
			HSSFCellStyle cellStyle, HSSFPalette palette, ExcelTableBo table,
			ExcelTableTrBo tr, ExcelTableTdBo td,
			ExcelTableTdStyleBorderBo styleBo) throws Exception {
		if (bottome != null) {
			CellStyle cs = bottome.getCellStyle();
			if (cs != null) {
				short borderbottom = cs.getBorderBottom();

				for (int i = c.getColumnIndex(); i < bottome.getColumnIndex(); i++) {
					CellStyle cs1 = bottomeRow.getCell(i).getCellStyle();
					if (cs1 != null) {
						if (cs.getBorderBottom() != borderbottom) {
							styleBo.setBorderBottom("");
						}
					}
				}

				short colorbottom = cs.getBottomBorderColor();
				String boderstyler = this.convertBorderStyle2Html(borderbottom,
						colorbottom);
				if (!StringsUtils.isEmpty(boderstyler)) {
					styleBo.setBorderBottom("border-bottom:" + boderstyler
							+ ";");
				}
			}
		} else {
			short borderbottom = cellStyle.getBorderBottom();
			if (borderbottom > 0) {
				short colorbottom = cellStyle.getBottomBorderColor();

				String boderstyler = this.convertBorderStyle2Html(borderbottom,
						colorbottom);
				if (!StringsUtils.isEmpty(boderstyler)) {
					styleBo.setBorderBottom("border-bottom:" + boderstyler
							+ ";");
				}

			}
		}

	}
}
