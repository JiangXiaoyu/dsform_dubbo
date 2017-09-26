package com.dotoyo.ims.dsform.allin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import com.dotoyo.dsform.log.LogProxy;


/**
 * 替换Excel模版中的特殊字符
 * @author wangl
 *
 */
public class Excel2ReplaceWord {

	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(Excel2ReplaceWord.class));
	
	public void replaceWord(String  tempFilePath ,OutputStream os,Map<Integer,String> data) throws Exception{
		FileInputStream is = new FileInputStream(tempFilePath);
		boolean is2007 = isExcel2007(is);
		Workbook workbook  = null;
		FileInputStream newis = new FileInputStream(tempFilePath);
		if(is2007){
			workbook = new XSSFWorkbook(newis);
		}else{
			workbook = new HSSFWorkbook(newis);
		}
		int sheetNum = workbook.getNumberOfSheets();
		int index = 0;//data的index
		
		try { 
			for(int sheetIndex = 0 ; sheetIndex < sheetNum; sheetIndex++){
			 
	            Sheet sheet = workbook.getSheetAt(sheetIndex);   //读取第一个工作簿  
	            int firstRowNum = sheet.getFirstRowNum();
	    		int lastRowNum = sheet.getLastRowNum();
	    		Row row = null;
	    		if(index >= data.size()){
	    			break;//退出
	    		}
	    		
	    		for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
	    			row = sheet.getRow(rowNum);
	    			int firstCell = row.getFirstCellNum();
	    			if(firstCell < 0){
	    				continue;
	    			}
	    			int lastCellNum = row.getLastCellNum();
	    			Cell cell = null;
	    			for (int cellNum = firstCell; cellNum <= lastCellNum; cellNum++) {
	    				cell = row.getCell(cellNum);
	    				if (cell == null) {
	    					continue;
	    				}
	    				int cellType = cell.getCellType();
	    				String content =  "";
	    				if(cellType == Cell.CELL_TYPE_NUMERIC){
	    					content = cell.getNumericCellValue() + "";
	    				}else if(cellType == Cell.CELL_TYPE_STRING){
	    					content = cell.getStringCellValue();
	    				}
	    				if(!content.trim().equals("")){
	    					String value = data.get(index);
	    					if(content.indexOf("__\n__")!=-1){//处理文本域
	    						if(value!=null){
	    							cell.setCellValue(value);
	    						}else{
	    							cell.setCellValue("");
	    						}
	    						index++;
	    					}else if(content.indexOf("__")!=-1){//处理文本框
	/*    						int startIndex = 0;
	    						for (int i = 0; i < content.length(); i++) {
	    							if(content.charAt(i) != '_') {
	    								startIndex = i;
	    								break;
	    							}
	    						}
	    						
	    						String desc = "";//如果内容为"注意___________（试验/检验表明）______",那么显示的结果就是 "注意" + 数据+ "(试验/检验表明）" + 数据
	    						if(startIndex != 0){
	    							desc = content.substring(startIndex);
	    						}
	    						
	    						if(value!=null){
	    							cell.setCellValue(value + desc);
	    						}else{
	    							cell.setCellValue(desc);
	    						}
	    						index++;*/
	    						
	    						//如果内容为"注意___________（试验/检验表明）______",那么显示的结果就是 "注意" + 数据+ "(试验/检验表明）" + 数据
	    						StringBuilder sb = new StringBuilder("");
	    						
	    						int ind = content.indexOf("__");
	    						while (ind != -1) {
	    							value = data.get(index);
	    							
	    							// 处理元数据
	    							int len = 0;
	    							for (int i = ind; i < content.length(); i++) {
	    								if (content.charAt(i) == '_') {
	    									len++;
	    								} else {
	    									break;
	    								}
	    							}
	    							sb.append(content.substring(0, ind));
	    							
	    							//这里加数据
	    							if(value!=null){
	    								sb.append(value);
	    							}else{
	    								sb.append("");
	    							}
	    							
	    							
	    							content = content.substring(ind + len, content.length());
	    							ind = content.indexOf("__");
	    							if (ind == -1) {
	    								sb.append(content);
	    							}
	    							index++;
	    						}
	    						
								cell.setCellValue(sb.toString());
	    						
	    					}else if(content.indexOf("##")!=-1){//处理ckeditor编辑器
	    						if(value!=null){
	    							value = this.getCkEditorContent(workbook, sheet, rowNum, cellNum, cell, value);
	    							cell.setCellValue(value);
	    						}else{
	    							cell.setCellValue("");
	    						}
	    						index++;
	    					}else if(content.indexOf("${")!=-1){//特殊控件
	    						
	    						int index1 = content.indexOf("${");
	    						int index2 = content.indexOf("}",index);
	    						if(index != -1 && index2!=-1 && index2 > index1) {
	    							String widgeType = content.substring(index1+2, index2);
	    							if(widgeType.indexOf("project")!= -1 || widgeType.indexOf("org")!= -1 ||  widgeType.indexOf("dflt")!= -1 ||
	    									widgeType.indexOf("weather")!= -1 || widgeType.indexOf("username")!= -1 || widgeType.indexOf("date")!= -1 ){
	    								if(value!=null){
	    	    							cell.setCellValue(value);
	    	    						}else{
	    	    							cell.setCellValue("");
	    	    						}
	    							}
	    						}
	    						index ++;
	    					}
	    				}
	    			}
	    		}
			}
			workbook.write(os);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(is != null){
				is.close();
			}
			if(newis != null){
				newis.close();
			}
		}
	}
	
	private boolean isExcel2007(InputStream strem) {
		try {
			new XSSFWorkbook(strem);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/*
	 * 获得ckeditor的内容
	 */
	private String getCkEditorContent(Workbook workbook, Sheet sheet,int rowNum,int cellNum,Cell cell, String value) throws Exception {
		if("".equals(value.trim())){
			return value;
		}
		//暂时不处理图片
/*		value = value.replace("&nbsp;", " ");//处理空格
		value = value.replace("<p>", " ");//处理P
		value = value.replace("</p>", " ");//处理P
		
		//处理<imag> </imag>
		while(this.hasimg(value)){
			value = insertImgCell(workbook, sheet, rowNum, cellNum, cell,value);
		}*/
		return value;
	}

	/*
	 * 插入图片  (暂时没有处理图片大小以及多个图片的问题)
	 */
	private String insertImgCell(Workbook workbook, Sheet sheet, int rowNum, int cellNum, Cell cell ,String value) throws Exception {
		if(value.indexOf("src")==-1){
			return value;
		}
		int imgIndex = value.indexOf("<img");
		int srcIndex = value.indexOf("src=\"", imgIndex)+5;
		int srcEndIndex =  value.indexOf("\"", srcIndex);
		String src = value.substring(srcIndex, srcEndIndex);
		
		
		String baseDir = PreformConfig.getInstance().getConfig(PreformConfig.BASE_DIR) + File.separator + "images" + File.separator;
		String filePath = baseDir + URLDecoder.decode(src.substring(src.lastIndexOf("images/")+7),"UTF-8") ;
		System.out.println("filePath:" + filePath);
		
		int imgEndIndex = value.indexOf("/>", srcEndIndex) +2;
		value = value.substring(0,imgIndex) + value.substring(imgEndIndex);
		
		InputStream is = null;
		try{
			is = new FileInputStream(filePath);  
			byte[] bytes = IOUtils.toByteArray(is);  
			  
			int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);  
			  
			CreationHelper helper = workbook.getCreationHelper();  
			Drawing drawing = sheet.createDrawingPatriarch();  
			ClientAnchor anchor = helper.createClientAnchor();  
			  
			// 图片插入坐标  
			anchor.setRow1(rowNum);  
			anchor.setCol1(cellNum); 
			
			// 插入图片  
			Picture pict = drawing.createPicture(anchor, pictureIdx);  
			pict.resize();  
		}catch(Exception e){
			log.error(e);
		}finally{
			if(is!=null){
				is.close();
			}
		}
		return value;
		
//		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
//		Bufferedimge bufferImg = imgeIO.read(new File("C:/Users/Administrator/Desktop/Photo/a044ad345982b2b731af87d332adcbef76099bf1.jpg"));
//        imgeIO.write(bufferImg, "jpg", byteArrayOut);
	}

	/*
	 * 判断是否有img标签
	 */
	private boolean hasimg(String value) {
		if(value.indexOf("<img")!=-1){
			return true;
		}
		return false;
	}

	/**
	 * 测试2007专用
	 * @throws wanglong
	 */
	public void test2007() throws Exception {
		FileOutputStream fos = null;
		OPCPackage pack = null;
		try {
			pack = POIXMLDocument.openPackage("D:\\1.xls");
			
			XWPFDocument doc = new XWPFDocument(pack);
			Iterator<XWPFParagraph> paragraphIt = doc.getParagraphsIterator();

			while (paragraphIt.hasNext()) {
				XWPFParagraph paragraph = paragraphIt.next();

				if (paragraph.getParagraphText().indexOf("____") != -1) {
					List<XWPFRun> run = paragraph.getRuns();

					for (int i = 0; i < run.size(); i++) {
						if (run.get(i).getText(run.get(i).getTextPosition()) != null
								&& run.get(i).getText(
										run.get(i).getTextPosition()).equals(
										"____")) {
							/**
							 * 参数0表示生成的文字是要从哪一个地方开始放置,一开始这里的代码是
							 * run.get(i).setText("AAAA",run.get(i).getTextPosition());
							 * 结果AAAA总是添加到要被替换的文字之后,经查看API知道,设置文字从位置0开始
							 * 就可以把原来的文字全部替换掉了
							 */
							run.get(i).setText("AAAA", 0);
						}
					}
				}

			}
			fos = new FileOutputStream("D:\\222.xls");
			doc.write(fos);
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				fos.close();
			}
			if(pack != null){
				pack.close();
			}
		}

	}
	
	/**
	 * 测试2003专用
	 * @throws wangl
	 */
	public void test2003() throws Exception{
		FileOutputStream fos = null;
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream("D:\\1.xls"));
		try {  
            HSSFSheet sheet = workbook.getSheetAt(0);   //读取第一个工作簿  
            int firstRowNum = sheet.getFirstRowNum();
    		int lastRowNum = sheet.getLastRowNum();
    		Row row = null;
    		for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
    			row = sheet.getRow(rowNum);
    			int firstCell = row.getFirstCellNum();
    			int lastCellNum = row.getLastCellNum();
    			Cell cell = null;
    			for (int cellNum = firstCell; cellNum <= lastCellNum; cellNum++) {
    				cell = row.getCell(cellNum);
    				if (cell == null) {
    					continue;
    				}
    				String content =  cell.getStringCellValue();
    				if(!content.trim().equals("")){
    					if(content.indexOf("__")!=-1){
    						cell.setCellValue("22222qqq");
    					}
    					System.out.println(content);
    				}
    			}
    		}
    		fos = new FileOutputStream("D:\\222.xls");
    		workbook.write(fos);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if (fos != null) {
				fos.close();
			}
		}
	}
	
	public static void main(String[]args) throws Exception{
		//如果内容为"注意___________（试验/检验表明）______",那么显示的结果就是 "注意" + 数据+ "(试验/检验表明）" + 数据
		String content = "__注意___________（试验/检验表明）______结论_";
		StringBuilder sb = new StringBuilder("");
		
		List<String> data = new ArrayList<String>();
		data.add("sss");
		data.add("ggg");
		data.add("mm");
		int index = 0;
		String value = data.get(index);
		int ind = content.indexOf("__");
		while (ind != -1) {
			// 处理元数据
			int len = 0;
			for (int i = ind; i < content.length(); i++) {
				if (content.charAt(i) == '_') {
					len++;
				} else {
					break;
				}
			}
			sb.append(content.substring(0, ind));
			
			//这里加数据
			sb.append(value);
			value = data.get(index);
			index++;
			
			
			content = content.substring(ind + len, content.length());
			ind = content.indexOf("__");
			if (ind == -1) {
				sb.append(content);
			}
		}
		System.out.println(sb.toString());
	}
}
