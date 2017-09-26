package com.dotoyo.ims.dsform.allin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.util.StringsUtils;

public class Excel2ViewHtml implements IExcel2Html {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(Excel2ViewHtml.class));
	private List<PictureStyleBo> picList = null;
	
	public Excel2ViewHtml(){
	}
	
	public Excel2ViewHtml(List<PictureStyleBo> picList){
		this.picList = picList;
	}
	
	@Override
	public List<PreformElm> getPreformElmList() {
		return null;
	}

	@Override
	public Set<RightMenuTag> getRightMenuTagSet() {
		return null;
	}

	public String toHtml(String fileName, int sheetIndex) throws Exception {
		StringBuffer htmlbuf = new StringBuffer("");
		String filepath = fileName;
		Workbook workbook = null;
		try {
			workbook = getExcelWorkBook(filepath);
			String exceltitle = getFirstRowContent(workbook, sheetIndex);
			StringBuffer htmlsource = excelToHtml(workbook, sheetIndex);
			htmlbuf.append(headerHtmlStart(exceltitle));
			htmlbuf.append(headerHtmlEnd());
			htmlbuf.append(bodyHtml());
			htmlbuf.append(htmlsource);
			htmlbuf.append(bodyHtmlEnd());
		} finally {
			if (workbook != null) {
				try {
					// workbook.cloneSheet(sheetIndex);
				} catch (Exception e) {
				}
			}
		}
		return htmlbuf.toString();
	}

	@Override
	public String toHtml(String fileName) throws Exception {
		int sheetIndex = 0;
		return toHtml(fileName, sheetIndex);
	}

	private StringBuffer headerHtmlStart(String title) throws Exception {
		StringBuffer sb = new StringBuffer("");
		sb.append("<!DOCTYPE HTML>");
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=EmulateIE8\"/>");
		sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
		sb.append("<META HTTP-EQUIV=\"pragma\" CONTENT=\"no-cache\">");
		sb.append("<META HTTP-EQUIV=\"Cache-Control\" CONTENT=\"no-cache, must-revalidate\">");
		sb.append("<META HTTP-EQUIV=\"expires\" CONTENT=\"0\">");
		sb.append("<style type='text/css'>		");
		sb.append("	.new-btn-yellow {		");
		sb.append("		background: none repeat scroll 0 0 #ff9a00;	");
		sb.append("		border: 0 none;		");
		sb.append("		color: #fff;	");
		sb.append("		border-radius: 4px;	");
		sb.append("		cursor: pointer;	");
		sb.append("		display: inline-block;	");
		sb.append("		font-size: 12px;	");
		sb.append("		height: 32px;	");
		sb.append("		line-height: 30px;	");
		sb.append("		padding: 0 16px;	");
		sb.append("		text-decoration: none;	}");
		sb.append(".area-edit p{margin: 0;padding: 0;}");
		sb.append("</style>	");
		
		String prefixPath = PreformConfig.getInstance().getConfig(PreformConfig.DS_PATH);//http://dsframe.10333.com
		String version = PreformConfig.getInstance().getConfig(PreformConfig.VERSION);//10333.com
		String domain = PreformConfig.getInstance().getConfig(PreformConfig.DOMAIN);//10333.com
		sb.append(String.format("<link rel=\"stylesheet\" type=\"text/css\" href=\"%s/preformCss.css?version=%s\">", prefixPath, version));
		//sb.append("<title>" + title + "</title>");

		//本地测试时会自动去掉domain信息
		sb.append(String.format("<SCRIPT type='text/javascript'>document.domain='%s';</SCRIPT>",domain));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/jquery/jquery.js?version=%s\"></SCRIPT>", prefixPath, version));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/easyui/jquery.easyui.min.js?version=%s\"></SCRIPT>", prefixPath, version));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/easyui/locale/easyui-lang-zh_CN.js?version=%s\"></SCRIPT>", prefixPath, version));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/jquery/json2.js?version=%s\"></SCRIPT>", prefixPath, version));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/frame/js/zh_CN/frame.js?version=%s\"></SCRIPT>", prefixPath, version));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/ckeditor/ckeditor.js?version=%s\"></SCRIPT>", prefixPath, version));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/preform/excel2MdfHtml.js?version=%s\"></SCRIPT>", prefixPath, version));	
		//sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/layer/layer.js\"></SCRIPT>",prefixPath));
		sb.append(getScript());
		return sb;
	}

	private String headerHtmlEnd() {
		StringBuffer sb = new StringBuffer("");
		sb.append("</head>");
		sb.append("<body>");// 进入页面就刷新下
		return sb.toString();
	}

	private String getScript() throws Exception {
		StringBuffer sb = new StringBuffer("");
		sb.append(" <SCRIPT type='text/javascript'> ");
		sb.append("     var param = {}; ");
		sb.append("		var editorList = {}; ");
		sb.append("     var dataBody = {}; ");
		sb.append("     var iframeId = ''; ");
		
		sb.append(" $(function(){ ");
		sb.append("		getParam();	");
		sb.append(" 	loadPrintDirect(); "); //加载打印设置
		sb.append("		var _id = param._id; ");
		sb.append("		var isView = param.isView; ");
		sb.append("		iframeId = getFormContentId(); ");
		sb.append("		setHeight(); ");
		sb.append("     if(_id!='null' && _id!='' && _id != undefined && _id != 'undefined' ){ ");
		String prefixPath = PreformConfig.getInstance().getConfig(PreformConfig.DS_PATH);//http://dsframe.10333.com
		sb.append(String.format(" 		var url='http://org.10333.com/formInstance/getPreformData?_id=' + _id; ",prefixPath));
		sb.append("			var json=''; ");
		sb.append(" 		$.ajax({ ");
		sb.append(" 			type: 'POST', ");
		sb.append(" 			contentType: 'application/json;charset=utf-8', ");
		sb.append(" 			url:  url, ");
		sb.append(" 			data: json, ");
		sb.append(" 			async: true, ");
		sb.append(" 			dataType : 'json', ");
		sb.append(" 			success : function(data) { ");
		sb.append("         		if(data != null){ ");
		sb.append("						dataBody = data.body; ");
		sb.append("						loadStyleData(dataBody); ");
		sb.append("						loadSpanData(dataBody); ");
		sb.append("						loadDataReady();"); 
		sb.append("					} "); 
		sb.append(" 			}, ");
		sb.append(" 			error : function(){  ");
		sb.append(" 			}	 ");
		sb.append(" 		}); ");
		sb.append(" 	} ");
		sb.append("		if(isView != ''){ ");
		sb.append("			loadSpanData(param); ");
		sb.append("			loadStyleData(param); ");
		sb.append("			loadDataReady();	"); 
		sb.append(" 	}else { ");
		sb.append("			loadDataReady();	"); 
		sb.append(" 	}	");
		sb.append(" }); ");
		
		//在数据加载完后，设置页面高度
		sb.append("     function setHeight(){ ");
		sb.append("     	var height = calcPageHeight(document); ");
		sb.append("     	if(parent.document && parent.document.getElementById(iframeId)!=null){ ");
		sb.append("				var formContent = parent.document.getElementById(iframeId); ");
		sb.append("				var flag = formContent.flag; ");
		sb.append("     		if(flag=='true'){ ");
		sb.append("     			formContent.style.height = height + 'px' ; ");
		sb.append("     		}else{ ");
		sb.append("     			formContent.style.height = height + 100 + 'px' ; ");
		sb.append("					formContent.flag = 'true';	");
		sb.append("     		}");
		sb.append("     		formContent.style.width = '1198px' ; ");
		sb.append(" 		}	");
		sb.append("     } ");
		
		//加载数据完毕
		sb.append("     function loadDataReady(){ 	");
		sb.append("     	$('#loadReady').val('1');	");
		sb.append("     } ");

		sb.append("		function getParam(){ ");
		sb.append(" 		var param1 = $('#paramValue').val(); ");
		sb.append(" 	    param1 = JSON.parse(param1); ");
		sb.append("         if(param1.paramValue){	");
		sb.append(" 			param1 = decodeURIComponent(decodeURIComponent(param1.paramValue)); ");
		sb.append(" 	   		param = JSON.parse(param1); ");
		sb.append("         }else{	");
		sb.append("         	param = param1; ");//没有汉字
		sb.append("         } ");
		sb.append(" 	}  ");
		
		//加载样式(fontSize size align)
		sb.append("		function loadStyleData(data){ ");
		sb.append(" 	  	var tempName = '';");
		sb.append(" 	  	var styData = data.intSty;");
		sb.append(" 	  	var tempMap = initStyAttr();");
		sb.append(" 		if(styData != null){");
		sb.append(" 	  		for(var i in styData){");
		sb.append(" 	 			if($('#'+i).size()==1){");
		sb.append(" 	 				if(styData[i].fontSize!=null){ ");
		sb.append(" 	 					$('#'+i).css('font-size',styData[i].fontSize);");
		sb.append(" 	  				}");
		sb.append(" 	 				if(styData[i].align!=null){ ");
		sb.append(" 	 					$('#'+i)[0].style.textAlign=styData[i].align;");
		sb.append(" 	  				}");
		sb.append(" 	 				if(styData[i].size!=null){ ");
		sb.append("							$('#'+i).attr('size',styData[i].size); ");
		sb.append(" 	  				}");
		sb.append(" 	 				if(styData[i].valign!=null){ ");
		sb.append(" 	 					$('#'+i).css('vertical-align',styData[i].valign);");
		sb.append(" 	 					$('#'+i).parent().parent().attr('valign',styData[i].valign);");
		sb.append(" 	  				}");
		sb.append(" 	  				for(var attrName in styData[i]){	");
		sb.append(" 						if(tempMap[attrName]==1){	");
		sb.append(" 							$('#'+i).css(attrName,styData[i][attrName]);");
		sb.append(" 						}");
		sb.append(" 					}");
		sb.append(" 	  			}");
		sb.append(" 	 		}");
		sb.append(" 		}");
		sb.append(" 	}  ");
		
		//加载数据
		sb.append("		function loadSpanData(data){ ");
		sb.append("			for(var i in data){ ");
		sb.append("				var jqObj = $('#'+i); ");
		sb.append("		 		if(jqObj.size()==1){ ");
		sb.append("		 			var len = jqObj.attr('size'); ");
		sb.append("					var iseditor =jqObj.attr('iseditor'); ");
		sb.append("					var tagName = jqObj[0].tagName; ");
		sb.append("					var inpType = jqObj.attr('type'); ");
		sb.append("					if(iseditor == 'true'){ ");
		sb.append("    					 for(var o in editorList){ ");
		sb.append("     					if(editorList[o]['name'] == i){ ");
		sb.append("     						editorList[o].setData(data[i],{ callback: function() { setHeight(); }});   ");
		sb.append("     					} ");
		sb.append("    					 } ");
		sb.append("					}else if(tagName.toUpperCase() == 'INPUT' && inpType.toUpperCase() == 'CHECKBOX'){ ");
		sb.append("						jqObj.attr('checked','checked'); ");//单选框
		sb.append("					}else if(tagName.toUpperCase() == 'INPUT' && inpType.toUpperCase() == 'HIDDEN'){ ");
		sb.append("						jqObj.val(data[i]); ");//隐藏域
		sb.append("					}else{ ");
		sb.append("		 				if(isNaN(len)){	");
		sb.append("		 					jqObj.html(data[i]); ");
		sb.append("		 				}else{	");
		//sb.append("		 					$('#'+i).html(data[i].substr(0,parseInt(len))); ");
		sb.append("		 					jqObj.html(data[i]); ");
		sb.append("		 				}	");
		sb.append("					} ");
		sb.append("		 		 } ");
		sb.append("		 	} ");
		sb.append("		} ");
		
		sb.append("		 function preview(oper) { ");
		sb.append("			if (oper < 10) { ");
		sb.append("		 		var bdhtml = window.document.body.innerHTML; ");
		sb.append("		 		var sprnstr = '<!--startprint' + oper + '-->';	 ");
		sb.append("		 		var eprnstr = '<!--endprint' + oper + '-->';	 ");
		sb.append("		 		var prnhtml = bdhtml.substring(bdhtml.indexOf(sprnstr) + 18);  ");
		sb.append("		 	    prnhtml = prnhtml.substring(0, prnhtml.indexOf(eprnstr)); ");
		sb.append("				window.document.body.innerHTML = prnhtml;");
		sb.append("				window.print();");
		sb.append("				window.document.body.innerHTML = bdhtml;");
		sb.append("			} else {	");
		sb.append("				window.print();	");
		sb.append("			}	");
		sb.append("		}		");
		
		sb.append("		function loadPrintDirect(){ 	");
		sb.append("			var id = param.id; ");
		sb.append("			var _id = param._id; ");
		sb.append(String.format(" 		var url='http://org.10333.com/formInstance/loadPrintDirect?id=' + id + '&_id=' + _id; ", prefixPath));
		sb.append(" 		$.ajax({ ");
		sb.append(" 			type: 'POST', ");
		sb.append(" 			contentType: 'application/json;charset=utf-8', ");
		sb.append(" 			url:  url, ");
		sb.append(" 			async: true, ");//这里必须为false，解决编辑器加载数据有几率失败的问题
		sb.append(" 			dataType : 'json', ");
		sb.append(" 			success : function(data) { ");
		sb.append("         		if(data != null && data.body != null){ ");
		sb.append("						document.getElementById('printDirec').value= data.body.printDirec; ");
		sb.append("						document.getElementById('note').value= data.body.note; ");
		sb.append("						document.getElementById('leftMargin').value= data.body.leftMargin; ");
		sb.append("						document.getElementById('topMargin').value= data.body.topMargin; ");
		sb.append("					} "); 
		sb.append("		 		}");
		sb.append("		 	});	");
		sb.append("		 }	");
		
		//获得此页面上的数据
		sb.append("		function getDataBody(){ 	 ");
		sb.append("			return dataBody; 	 ");
		sb.append("		} 	 ");
		
		
		sb.append(" </SCRIPT> ");
		return sb.toString();
	}

	private StringBuffer bodyHtml() {
		StringBuffer sb = new StringBuffer("");
		return sb;
	}

	private StringBuffer bodyHtmlEnd() {
		StringBuffer sb = new StringBuffer("");
		sb.append("</body>");
		sb.append("</html>");
		return sb;
	}

	@Override
	public String toString() {
		return "ExcelUtils []";
	}

	private StringBuffer excelToHtml(Workbook workbook, int sheetindex)
			throws Exception {

		Sheet sheet = workbook.getSheetAt(sheetindex);
		int firstRow = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
		//
		ExcelTableBo table = new ExcelTableBo();
		//
		table.setFirstRow(firstRow);
		table.setLastRow(lastRowNum);
		//
		if (lastRowNum > Excel2MdfHtml.MAX_ROW_SUM) {
			// 行数不允许多于10000
			FrameException e = new FrameException("3000000000000045", null);
			throw e;
		}

		RowColumnSpanBo bo = getRowColumnSpanBo(sheet);

		Row row = null;
		// 处理表格
		for (int rowNum = firstRow; rowNum <= lastRowNum; rowNum++) {
			row = sheet.getRow(rowNum);
			ExcelTableTrBo tr = new ExcelTableTrBo();

			if (row == null) {

				tr.setRowNum(rowNum);
				tr.setHeight("0pt");
				tr.setLastColNum(-1);
				tr.getTdMap().put(0, new ExcelTableTdBo());
				table.getTrMap().put(rowNum, tr);
				continue;
			}
			tr.setHeight(row.getHeightInPoints() + "pt");
			row2TableTr(workbook, sheet, row, table, tr, bo);
			table.getTrMap().put(rowNum, tr);
		}
		//
		StringBuffer sb = new StringBuffer();
		
		sb.append("<form id='preformForm'>");
		sb.append("<table id=\"preformTable\" border=\"0\" align='center' cellspacing=\"0\" cellpadding=\"0\">");
		for (int rowNum = sheet.getFirstRowNum(); rowNum <= lastRowNum; rowNum++) {
			ExcelTableTrBo tr = table.getTrMap().get(rowNum);
			if (tr != null) {
				String trHtml = row4Tr2Html(table, tr);
				sb.append(trHtml);
			}

		}
		sb.append("</table>");
		sb.append("</form>");
		// HTML
		return sb;
	}

	//tr对象转为为html
	private String row4Tr2Html(ExcelTableBo table, ExcelTableTrBo tr)
			throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("<tr ");
		sb.append(String.format("height='%s'>", tr.getHeight()));
		int lastColNum = tr.getLastColNum();

		for (int colNum = 0; colNum < lastColNum; colNum++) {
			ExcelTableTdBo td = tr.getTdMap().get(colNum);
			if (td != null) {
				String tdHtml = row4Td2Html(table, tr, td);
				sb.append(tdHtml);
			}
		}
		sb.append("</tr>");
		return sb.toString();
	}

	//获得td样式
	private String row4Td2Html(ExcelTableBo table, ExcelTableTrBo tr,
			ExcelTableTdBo td) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("<td ");
		
		if (!StringsUtils.isEmpty(td.getColspan())) {
			sb.append(String.format(" colspan= '%s'", td.getColspan()));
		}
		if (!StringsUtils.isEmpty(td.getRowspan())) {
			sb.append(String.format(" rowspan= '%s'", td.getRowspan()));
		}
		
		String str = "";
		if (!StringsUtils.isEmpty(td.getContent())) {
			/*
			  modify by wangl viewHtml添加span
			//sb.append(td.getContent());
			*/
			IExcelCellContent2Html html = new ExcelCellContent4String2Html();
			str = html.toHtml(table, tr, td);
		}
		
		if (td.getBo() != null) {
			ExcelTableTdStyleBorderBo bo = td.getBo();
			if (bo != null) {
				sb.append(row4TdStyle2Html(table, tr, td, bo));
			}
		}
		
		//如果td包含编辑器，则不需要align
		if (!StringsUtils.isEmpty(td.getAlign()) && !td.getEditor()) {
			sb.append(td.getAlign());
		}
		
		sb.append(">");
		sb.append(str);
		sb.append("</td>");
		return sb.toString();
	}

	private String row4TdStyle2Html(ExcelTableBo table, ExcelTableTrBo tr,
			ExcelTableTdBo td, ExcelTableTdStyleBorderBo bo) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(String.format(" style= '"));
		
		if (!StringsUtils.isEmpty(td.getWidth())) {
			if(table.isOldPreform()){//旧模版所有行都有宽度
				sb.append(String.format(" width:%s;min-width:%s;max-width:%s;", td.getWidth(),td.getWidth(),td.getWidth()));
			}else if(!table.isOldPreform() && tr.isFirstRowFlag()){//新模版只有首行有宽度
				sb.append(String.format(" width:%s;min-width:%s;max-width:%s;", td.getWidth(),td.getWidth(),td.getWidth()));
			}
		}
		
		sb.append(bo.getFont());
		sb.append(bo.getColor());
		sb.append(bo.getBorderBottom());
		sb.append(bo.getBorderLeft());
		sb.append(bo.getBorderRight());
		sb.append(bo.getBorderTop());
		sb.append("'");
		return sb.toString();
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
			if (bo.getRow().containsKey(rowNum + "," + colNum)) {
				String pointString = bo.getRow().get(rowNum + "," + colNum);
				bo.getRow().remove(rowNum + "," + colNum);
				bottomeRow = Integer.valueOf(pointString.split(",")[0]);
				bottomeCol = Integer.valueOf(pointString.split(",")[1]);

				int colSpan = bottomeCol - colNum + 1;
				double tdWidth = PreformUtils.getTdSpanWidth(sheet, colNum, bottomeCol);
				td.setWidth(tdWidth + "pt");
				
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
				int tdwidth = sheet.getColumnWidth(colNum) / 32;
				td.setWidth(tdwidth + "pt");
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

		if (cellStyle != null) {
			String align = alignment2Html(bo, workbook, sheet, row, cell,
					cellStyle);
			td.setAlign(align);
			if (workbook instanceof HSSFWorkbook) {
				IExcelCellStyle2Html html = new ExcelCellStyle4Hssf2Html();
				html.toTable(bo, workbook, sheet, row, cell, bottomeRow,
						bottomeCol, cellStyle, table, tr, td, styleBo);

			} else if (workbook instanceof XSSFWorkbook) {

			}

		}
		td.setBo(styleBo);
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
		if (verticalAlignment > 0) {
			sb.append(" valign='"
					+ convertVerticalAlignToHtml(verticalAlignment) + "' ");
		}
		return sb.toString();
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

	/**
	 * 根据文件的路径创建Workbook对象
	 * 
	 * @param filePath
	 * @throws Exception
	 */
	private Workbook getExcelWorkBook(String filePath) throws Exception {
		InputStream ins = null;
		Workbook book = null;
		try {
			ins = new FileInputStream(new File(filePath));
			book = WorkbookFactory.create(ins);

			return book;
		} finally {
			if (ins != null) {
				try {
					ins.close();
				} catch (Exception e) {
				}
			}
		}

	}

	/**
	 * 获取某Sheet，第一行的内容
	 * 
	 * @param workbook
	 * @param sheetindex
	 * @return
	 */
	private String getFirstRowContent(Workbook workbook, int sheetindex) {
		String exceltitle = "";
		if (workbook != null) {
			Sheet sheet = workbook.getSheetAt(sheetindex);
			int firstrownum = sheet.getFirstRowNum();
			Row row = sheet.getRow(firstrownum);
			short fcellnum = row.getFirstCellNum();
			short lcellnum = row.getLastCellNum();
			for (int j = fcellnum; j < lcellnum; j++) {
				Cell cell = row.getCell(j);
				exceltitle += getStringCellValue(cell);
			}
		}
		return exceltitle;
	}

	public String toHtml(ExcelTableBo table) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(headerHtmlStart(table.getExcelTitle()));
		sb.append(headerHtmlEnd());
		sb.append(bodyHtml());

		sb.append("<!--startprint1-->");
		
		//white-space:nowrap;  样式会影响div中文字不换行
		//sb.append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style='border-collapse:collapse;white-space:nowrap;'>");
		sb.append("<form id='preformForm'>");
		sb.append("<div style='display:none;'>  ");
		sb.append("		<input id='printDirec' name='printDirec' type='hidden'/>  ");		//打印方向	
		sb.append("		<input id='note' name='note' type='hidden'/> ");					//
		sb.append("		<input id='leftMargin' name='leftMargin' type='hidden'/>  ");		//左留白
		sb.append("		<input id='topMargin' name='topMargin' type='hidden'/>  ");			//上留白
		sb.append("		<input id='loadReady' name='loadReady' type='hidden'/>  "); 		//数据加载完毕标示
		sb.append(" 	<input id='paramValue' name='paramValue' type='hidden'>  ");		//参数隐藏域
		sb.append(" 	<input id='_id' name='_id' type='hidden'>  ");						//文件实例id
		sb.append("</div>  ");
		
		sb.append("<table id=\"preformTable\" border=\"0\" align='center' cellspacing=\"0\" cellpadding=\"0\" style='border-collapse:collapse;'>");
		table.setPicStyleList(picList);
		header2Html(table, sb);
		footer2Html(table, sb);
		sb.append("<tbody>");
		body2Html(table, sb);
		sb.append("</tbody>");
		sb.append("</table>");
		
		sb.append(String.format("<input id='keyValue' name='keyValue' type='hidden' value='%s'>", table.getKeyValue().toString()));//id与行列的对应关系,计算时会使用
		sb.append("</form>");
		sb.append("<!--endprint1-->");

		sb.append(bodyHtmlEnd());
		// HTML
		return sb.toString();
	}

	/**
	 * 表格内容 转化为tbody
	 */
	private void body2Html(ExcelTableBo table, StringBuffer sb)
			throws Exception {
		boolean firstRowFlag = true;//第一行标示
		for (int rowNum = table.getFirstRow(); rowNum <= table.getLastRow(); rowNum++) {
			ExcelTableTrBo tr = table.getTrMap().get(rowNum);
			tr.setFirstRowFlag(firstRowFlag);
			tr.setRowNum(rowNum);//设置rownum 计算合并单元格高度时用到
			if (tr != null) {
				String trHtml = row4Tr2Html(table, tr);
				sb.append(trHtml);
			}
			firstRowFlag = false;
		}
	}
	
	/**
	 * 页眉 页脚 转化为tfooter
	 */
	private void header2Html(ExcelTableBo table, StringBuffer sb) {
		ExcelHeader header =  table.getHeader();
		if(header != null){
			String align = "";
			String content = "";
			if(!"".equals(header.getLeftContent())){
				align = "left";
				content = header.getLeftContent();
			}else if(!"".equals(header.getCenterContent())){
				align = "center";
				content = header.getCenterContent();
			}else if(!"".equals(header.getRightContent())){
				align = "right";
				content = header.getRightContent();
			}
			
			if(!"".equals(content)){
				int cellNum = table.getCellNum();
				sb.append("<thead>");
				sb.append("<tr>");
				sb.append(String.format("<td align='%s' colspan='%s' style='font-family:宋体;font-size: 10pt;'>%s", align, cellNum, content));
				
				sb.append("</td>");
				sb.append("</tr>");
				sb.append("</thead>");
			}
		}
	}
	
	/**
	 * 页脚转化为theader
	 */
	private void footer2Html(ExcelTableBo table, StringBuffer sb) {
		ExcelHeader footer =  table.getFooter();
		if(footer != null){
			String align = "";
			String content = "";
			if(!"".equals(footer.getLeftContent())){
				align = "left";
				content = footer.getLeftContent();
			}else if(!"".equals(footer.getCenterContent())){
				align = "center";
				content = footer.getCenterContent();
			}else if(!"".equals(footer.getRightContent())){
				align = "right";
				content = footer.getRightContent();
			}
			
			if(!"".equals(content)){
				int cellNum = table.getCellNum();
				sb.append("<tfoot>");
				sb.append("<tr>");
				sb.append(String.format("<td align='%s' colspan='%s' style='font-family:宋体;font-size: 10pt;'>%s", align, cellNum, content));
				
				sb.append("</td>");
				sb.append("</tr>");
				sb.append("</tfoot>");
			}
		}
	}

	public static void main(String args[]) throws Exception {
		IExcel2Html html = new Excel2ViewHtml();
		Excel2Table eu = new Excel2Table();
		ExcelTableBo table = eu.toTable("D:/wangl/预定义表单文档/02 监理单位用表/10施工测量放线报验单GD220210.xls", 0);
		String htmlbuf = html.toHtml(table);
		FileOutputStream fos = null;
		try {

			File file = new File(
					"D:/wangl/05监理规划GD220205.html");
			fos = new FileOutputStream(file);
			fos.write(htmlbuf.getBytes("utf-8"));

			fos.flush();
			fos.close();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {

				}
			}
		}
	}
}
