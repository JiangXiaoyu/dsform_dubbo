package com.dotoyo.ims.dsform.allin;

import java.util.List;
import java.util.regex.Pattern;

import com.dotoyo.dsform.excel.ExcelColumnBo;
import com.dotoyo.dsform.util.StringsUtils;

public class ExcelCellContent4String2Html implements IExcelCellContent2Html {

	@Override
	public PreformElm getPreformElm() {
		return null;
	}
	
	@Override
	public List<RightMenuTag> getRightMenuList() {
		return null;
	}

	@Override
	public String toHtml(org.apache.poi.ss.usermodel.Cell cell)
			throws Exception {
		StringBuilder sb = new StringBuilder();
		String stringValue = cell.getStringCellValue();
		if (StringsUtils.isEmpty(stringValue)) {
			sb.append("&nbsp;");
		} else {
			String value = stringValue.replaceAll(String.valueOf(' '),"&nbsp;");
			value = value.replaceAll(String.valueOf('<'), "&lt;");
			value = value.replaceAll(String.valueOf('>'), "&gt;");
			value = value.replaceAll(String.valueOf('\n'), "</br>");
			
			//支持平方 立方
			//value = value.replaceAll(String.valueOf('²'), "&sup2;");
			//value = value.replaceAll(String.valueOf('³'), "&sup3;");
			
			sb.append(value);
		}
		return sb.toString();
	}
	
	public String toHtml(String content)throws Exception{
		return "";
	}
	
	@Override
	public String toHtml(ExcelTableBo table, ExcelTableTrBo tr,
			ExcelTableTdBo td) throws Exception {
		//设置高度
		td.setHeight(PreformUtils.getTdSpanHeight(table, tr, td));
		
		StringBuilder sb = new StringBuilder();
		String tc = td.getContent().replaceAll(" ", "&nbsp;");
		int id = tc.indexOf("__");
		int idWidge = tc.indexOf("${");
		int idPic = tc.indexOf("$$");//图片
		if (id == -1 && idWidge == -1 && idPic== -1) {
			if((id=tc.indexOf("##"))!=-1){
				editorToHtml4Span(table, tr, td, tc, id, sb);
				return sb.toString();
			}else{
				sb.append(tc);
				return sb.toString();
			}
		}
		
		//处理textarea
		if (tc.startsWith("__")) {
			int id1 = tc.indexOf("__</br>__");
			if (id1 != -1) {
				String fontStyle = getFonStyle(td);
				//String tagId = String.format("A%s", table.getParam().size());
				String tagId = getTagId(table);
				int length = tc.replace("</br>", "").length();
				
				//宽度减去margin
				float width = Float.parseFloat(td.getWidth().substring(0, td.getWidth().indexOf("px")));
				String widthStr = ( width -2) + "px";//减去2px表示border
				
				float height = Float.parseFloat(td.getHeight().substring(0, td.getHeight().indexOf("px")));
				String heigthStr = ( height - 2) + "px";//高度不用减太多
				
				String textAlign = td.getTextAlign();
				String verAlign = td.getVerAlign();
				
				//resize:none;不能拖拽大小 outline:none;没有边框
				if("auto".equals(table.getThisType())){
					//自适应高度
					sb.append(String
							.format(
									"<div style='width: %s; heigth:%s; border: 0px solid #ccc;'>"+
									"<div id='%s' name='%s' class='area-edit' isTextarea='true' contenteditable='false' showmenu='false' style='width: %s;  heigth:%s; %s; display: table-cell;"+
									"  text-align:%s; vertical-align: %s; word-wrap: break-word; word-break: break-all;'></div></div>",
									 widthStr,heigthStr,tagId, tagId, widthStr,heigthStr,fontStyle,textAlign,verAlign));
				}else{
					//固定高度限制
					sb.append(String
							.format(
									"<div style='width: %s; height: %s; overflow: hidden; border: 0px solid #ccc;'>"+
									"<div id='%s' name='%s' class='area-edit' isTextarea='true' contenteditable='false' showmenu='false' style='width: %s; height: %s; %s; display: table-cell;"+
									"  text-align:%s; vertical-align: %s; word-wrap: break-word; word-break: break-all;'  size='%s'></div></div>",
									widthStr,heigthStr,tagId, tagId, widthStr,heigthStr,fontStyle,textAlign,verAlign,length/2));//这里的高度是必须有的  
				}
				
				doExcelColumnBo(tagId, table, tr, td,"TEXTAREA","");
				return sb.toString();
			}
		}
		toHtml4Span(table, tr, td, tc, id, sb);
		widgeToHtml4Span(table, tr, td, tc, sb);
		toHtml4Pic(table, tr, td, tc, idPic, sb);
		return sb.toString();
	}
	
	/*
	 * 处理图片
	 */
	private void toHtml4Pic(ExcelTableBo table, ExcelTableTrBo tr,
			ExcelTableTdBo td, String tc, int idPic, StringBuilder sb) throws Exception {
		List<PictureStyleBo> picStyleList = table.getPicStyleList();
		if(picStyleList == null || picStyleList.size() == 0){
			return;
		}
		while (idPic != -1) {
			PictureStyleBo picStyle = null;
			for(PictureStyleBo ps : picStyleList){
				if("0".equals(ps.getViewStatus())){//状态为0表示未被占用
					picStyle = ps;
					ps.setViewStatus("1");
					break;
				}
			}
			if(picStyle == null){
				return;
			}
			
			double width = picStyle.getWidth();
			double height = picStyle.getHeight();
			sb.append(tc.substring(0, idPic));
			String picPath = PreformConfig.getInstance().getConfig(PreformConfig.PIC_FTP_PATH);//http://f.10333.com/preform
			sb
					.append(String
							.format(
									"<img src='%s/%s' style='padding:2px;width:%spx;height:%spx;' />",picPath,picStyle.getId(), 
									width-4,height));//减去4 表示左右两边2px的留白
			tc = tc.substring(idPic + 2, tc.length());
			idPic = tc.indexOf("$$");
			if (idPic == -1) {
				sb.append(tc);
			}
		}
	}

	private void widgeToHtml4Span(ExcelTableBo table, ExcelTableTrBo tr,
			ExcelTableTdBo td, String tc, StringBuilder sb) throws Exception {
		String content = td.getContent();
		int index = content.indexOf("${");
		int index2 = content.indexOf("}",index);
		
		//宽度
		float width = Float.parseFloat(td.getWidth().substring(0, td.getWidth().indexOf("px")));
		//高度
		int height = Integer.parseInt(td.getHeight().substring(0, td.getHeight().indexOf("px")));
		
		String textAlign = td.getTextAlign();
		if(index != -1 && index2!=-1) {
			String tagId = getTagId(table);
			String widgeType = content.substring(index+2,index2);
			String fontStyle = getFonStyle(td);
			//通知单下拉
			IWidgeType notice = new NoticeWidge(null);
			//表单属性
			IWidgeType itemNo = new ItemNoWidge(notice,table);
			//汇总控件
			IWidgeType numOfTest = new NumOfTestWidge(itemNo);
			IWidgeType numOfSubSec = new NumOfSubSecWidge(numOfTest);
			IWidgeType numOfItem = new NumOfItemWidge(numOfSubSec);
			IWidgeType numOfPart = new NumOfPartWidge(numOfItem);
			IWidgeType sumTitle = new SumTitleWidge(numOfPart,table);
			IWidgeType sumUnitNum = new SumUnitNumWidge(sumTitle);
			IWidgeType sumUnit = new SumUnitWidge(sumUnitNum);
			IWidgeType sumSec = new SumSecWidge(sumUnit);
			IWidgeType sumSubSec = new SumSubSecWidge(sumSec,table);
			IWidgeType sumItem = new SumItemWidge(sumSubSec);
			IWidgeType sumPart = new SumPartWidge(sumItem);
			IWidgeType timesOfTest = new TimesOfTestWidge(sumPart);
			//单位工程 分部 子分部 分项 部位
			IWidgeType unit = new UnitWidge(timesOfTest);
			IWidgeType sec = new SecWidge(unit);
			IWidgeType subsec = new SubSecWidge(sec);
			IWidgeType item = new ItemWidge(subsec);
			IWidgeType  part = new PartWidge(item);
			//标题
			IWidgeType title = new TitleWidge(part);
			//沉降位移
			IWidgeType accumOfs = new DsplAccumOfsWidge(title);
			IWidgeType no = new DsplNoWidge(accumOfs);
			IWidgeType numTime = new DsplNumTimeWidge(no);
			IWidgeType thisOfs = new DsplThisOfsWidge(numTime);
			IWidgeType time = new DsplTimeWidge(thisOfs);
			IWidgeType value = new DsplValueWidge(time);
			//检验批
			IWidgeType testCap = new TestCapWidge(value);
			IWidgeType minSamp = new TestMinSampWidge(testCap,"最小抽样数");
			IWidgeType actSamp = new TestActSampWidge(minSamp,"实际抽样数");
			IWidgeType rec = new TestRecWidge(actSamp,"检查记录");
			IWidgeType rlt = new TestRltWidge(rec,"检查结果");
			//强度统计
			IWidgeType countMpa = new StatsMpaWidge(rlt,"countmpa","试块组数");
			IWidgeType stdMpa = new StatsMpaWidge(countMpa,"stdmpa","强度标准值");
			IWidgeType avgMpa = new StatsMpaWidge(stdMpa,"avgmpa","强度平均值");
			IWidgeType minMpa = new StatsMpaWidge(avgMpa,"minmpa","强度最小值");
			IWidgeType maxMpa = new StatsMpaWidge(minMpa,"maxmpa","强度最大值");
			IWidgeType stDevMpa = new StatsMpaWidge(maxMpa,"stdevmpa","强度标准差");
			IWidgeType coef1 = new StatsMpaWidge(stDevMpa,"coef1","λ1");
			IWidgeType coef2 = new StatsMpaWidge(coef1,"coef2","λ2");
			IWidgeType coef3 = new StatsMpaWidge(coef2,"coef3","λ3");
			IWidgeType coef4 = new StatsMpaWidge(coef3,"coef4","λ4");
			IWidgeType mpa = new MpaWidge(coef4);
			IWidgeType c = new StrGrdWidge(mpa);
			IWidgeType bool = new BoolWidge(c);
			IWidgeType rltMpa = new StatsMpaWidge(bool,"rltmpa","结论");
			//可输入下拉框 自动序号
			IWidgeType autoSnSrcWidge = new AutoSnWidge(rltMpa);
			IWidgeType autoSnWidge = new AutoSnSrcWidge(autoSnSrcWidge);
			IWidgeType upperWidge = new UppercaseWidge(autoSnWidge);
			IWidgeType lowerWidge = new LowercaseWidge(upperWidge);
			IWidgeType text = new AutoTextWidge(lowerWidge);
			IWidgeType slt = new AutoSelectWidge(text);
			//随机数
			IWidgeType numText = new NumTextWidge(slt);
			IWidgeType numSlt = new NumSltWidge(numText);
			IWidgeType calc = new CalcWidge(numSlt);
			IWidgeType rndWid = new RndWidge(calc); 
			IWidgeType rndValWid = new RndValWidge(rndWid);
			IWidgeType rndChkWid = new RndChkWidge(rndValWid);
			IWidgeType rndCalkWid = new RndCalWidge(rndChkWid);
		
			//当前页 总页数 
			IWidgeType pnWid = new PageNoWidge(rndCalkWid, content);
			IWidgeType pcWid = new PageCountWidge(pnWid, content);
			
			//常用控件
			IWidgeType weaWid = new WeatherWidge(pcWid);
			IWidgeType supWid = new SubWidge(weaWid,"sup", content);
			IWidgeType subWid = new SubWidge(supWid ,"sub", content);
			IWidgeType snWid = new SnWidge(subWid,table);
			IWidgeType dftWid = new DefaultWidge(snWid);
			IWidgeType orgWid = new OrgWidge(dftWid);
			IWidgeType chkWid = new CheckWidge(orgWid,table,tr,td);
			IWidgeType dateWid = new DateWidge(chkWid);
			IWidgeType usernameWid = new UsernameWidge(dateWid);
			IWidgeType proWid = new ProjectWidge(usernameWid);
			IWidgeType pseudo = new PseudoWidge(proWid);
			
			//2.0新增控件
			IWidgeType textarea = new TextareaWidge(pseudo, table, tr, td);//textarea
			IWidgeType curPositionWidge = new CompanyWidge(textarea);//单位名称
			IWidgeType currencyWidge = new CurrencyWidge(curPositionWidge);//通用
			IWidgeType postionWidge = new PostionWidge(currencyWidge);//检验批部位
			IWidgeType leaderWidge = new LeaderWidge(postionWidge);//项目负责人
			IWidgeType inspnumWidge = new InspnumWidge(leaderWidge);//检验批数量
			IWidgeType capacityWidge = new CapacityWidge(inspnumWidge);//检验批容量
			IWidgeType inspectionWidge = new InspectionWidge(capacityWidge);//检验批名称
			IWidgeType quantityWidge = new QuantityWidge(inspectionWidge);//分项工程工程量
			IWidgeType subsecnumWidge = new SubsecnumWidge(quantityWidge);//分项工程工程量
			IWidgeType technicalleaderWidge = new TechnicalleaderWidge(subsecnumWidge);//项目技术负责人
			//IWidgeType secsubWidge = new SecsubWidge(technicalleaderWidge);//分部+子分部
			//IWidgeType subsecnsWidge = new SubsecnsWidge(technicalleaderWidge);//子分部工程名称(多个)
			
			
			//下拉控件
			IWidgeType selectWidge= new SelectWidge(technicalleaderWidge);
			
			//复选框
			IWidgeType checkBoxWidge= new CheckBoxWidge(selectWidge);
			
			
			//各方企业名称
			IWidgeType curCompanyWidge= new CurCompanyWidge(checkBoxWidge);
			//分项工程数量
			IWidgeType itemnumWidge= new ItemnumWidge(curCompanyWidge);
			
			//分项工程名称
			IWidgeType itemnWidge= new ItemnWidge(itemnumWidge);
			
			//检查结果
			IWidgeType resultWidge= new ResultWidge(itemnWidge);
			//检验批所在的施工部位
			IWidgeType partnWidge= new PartnWidge(resultWidge);
			
			IWidgeType hidden = new HiddenWidge(partnWidge);
			
			
			//解析
			String wtype = hidden.append2ViewHtml(widgeType, sb, width, height, tagId, textAlign, fontStyle);
			table.getKeyValue().put(WidgeUtils.getColumnName(td.getColNum() + 1) + (tr.getRowNum()+1),  tagId);
			
			if(wtype.equals(CheckWidge.WIDGE_NAME)){
				//单独处理
			}else{
				doExcelColumnBo(tagId, table, tr, td,"TEXT",wtype);
			}	
		}
	}
	
	private void editorToHtml4Span(ExcelTableBo table, ExcelTableTrBo tr,
			ExcelTableTdBo td, String tc, int id, StringBuilder sb) throws Exception {
		int id1 = tc.indexOf("##</br>##");
		if (id1 != -1) {

			for (int i = 0; i < tc.length(); i++) {
				if (tc.charAt(i) != '#') {
					if (tc.startsWith("##</br>##", i - 2)) {
						i += "</br>##".length() - 1;
					} else {
						//toHtml4Input(table, tr, td, tc, id, sb);
					}
				}
			}
			//String tagId = String.format("A%s", table.getParam().size());
			String tagId = getTagId(table);
			//int length = tc.replace("</br>", "").length();
			
			//计算合高度
			int height = Integer.parseInt(td.getHeight().substring(0, td.getHeight().indexOf("px")));
			String heightStr = (height -4) + "px";//-留白
			
			//计算宽度减去padding的
			float width = Float.parseFloat(td.getWidth().substring(0, td.getWidth().indexOf("px")));
			String widthStr = (width -20) + "px";//-留白
			
			if("auto".equals(table.getThisType())){
				//自适应高度
				sb.append(String.format(
				//"<div id='%s' name='%s' isEditor='true' style='width:%s;height:%s;overflow:hidden;text-align:left;margin-left:21px;' size='%s'></div>",
						"<div id='%s' name='%s' isEditor='true' style='font-family:ds;padding:2px 5px;margin:2px 5px;line-height:1.3;width:%s;overflow-x:hidden;overflow-y:%s;'></div>",
						tagId, tagId, widthStr,""));  //这里的宽度必须设置
			}else if("mobile".equals(table.getThisType())){
				//手机自适应高度
				sb.append(String.format(
						"<div id='%s' name='%s' style='font-family:ds;padding:2px 5px;margin:2px 5px;line-height:1.3;width:%s;overflow-x:hidden;overflow-y:%s;' ></div>",
						tagId, tagId, widthStr,""));  //这里的宽度必须设置
			}else{
				//固定高度限制
				sb.append(String.format(
						//"<div id='%s' name='%s' isEditor='true' style='width:%s;height:%s;overflow:hidden;text-align:left;margin-left:21px;' size='%s'></div>",
						"<div id='%s' name='%s' isEditor='true' style='font-family:ds;padding:2px 5px;margin:2px 5px;line-height:1.3;width:%s;height:%s;overflow-x:hidden;overflow-y:%s;'></div>",
						tagId, tagId, widthStr,heightStr,"hidden"));//这里的宽度 高度必须设置
			}
			
			if(!"mobile".equals(table.getThisType())){
				sb.append("<script type='text/javascript'>/*ckeditor*/");
				sb.append(" 	CKEDITOR.disableAutoInline = true;");
				
				//sb.append(String.format("	var %s = CKEDITOR.inline('%s',{removePlugins:'resize,image2',toolbar:[['about']]});",tagId,tagId)); // ID of the element to edit
				sb.append(String.format("	var %s = CKEDITOR.inline('%s');",tagId,tagId)); // ID of the element to edit
				//sb.append(String.format("	%s.setReadOnly(true);",tagId));
				//sb.append(String.format("	%s.execCommand( 'toolbarCollapse');",tagId));
				
				
				/*			sb.append("		 CKEDITOR.on('instanceReady',function(event){ 	");
				sb.append("			var editor=event.editor; 	");
				sb.append("			editor.execCommand('toolbarCollapse'); 	");
				sb.append("			editor.setReadOnly(true); 	");
				sb.append(" 		setTimeout( function(){");
				
				//防止延迟
				sb.append("			if ( !editor.element ){ 	");
				sb.append("				 setTimeout( arguments.callee, 100 ); 	");
				sb.append("				 return; 	");
				sb.append("			}	 	");
				sb.append("			}); 	");
				sb.append("		 }); 	");*/
				
				sb.append(String.format("editorList.%s=%s;",tagId,tagId));
				sb.append("</script>");
			}
			
			doExcelColumnBo(tagId, table, tr, td,"EDITOR","");
			td.setEditor(true);
		}
	}

	/*
	 * 处理文本框
	 */
	private void toHtml4Span(ExcelTableBo table, ExcelTableTrBo tr,
			ExcelTableTdBo td, String tc, int id, StringBuilder sb)
			throws Exception {
		String content = td.getContent();
		Pattern pattern = Pattern.compile("^_*$");// 判断是否是 A0 A1 A2 A3
		boolean isAllInput = pattern.matcher(content).matches();
		while (id != -1) {
			// 处理元数据
			int len = 0;
			for (int i = id; i < tc.length(); i++) {
				if (tc.charAt(i) == '_') {
					len++;
				} else {
					break;
				}
			}
			String fontStyle = getFonStyle(td);
			// 两个_代表一个汉字，input中字体大小默认为14*PT(十号字体)
			float width = 0;
			if(isAllInput){
				width = Float.parseFloat(td.getWidth().substring(0, td.getWidth().indexOf("px")));
			}else{
				width = len * 8;
			}
			
			sb.append(tc.substring(0, id));
			//String tagId = String.format("A%s", table.getParam().size());
			String tagId = getTagId(table);
			String textAlign = td.getTextAlign();
			
			if("auto".equals(table.getThisType())){
				//自适应宽度
				sb.append(String
						.format(			
								"<span id='%s' name='%s' type='text' style='padding:2px;width:%spx;text-align:%s;%s' size='%s'>&nbsp;</span>",
									tagId, tagId, width -4 ,textAlign,fontStyle, len/2));//减去左右padding
			}else{
				//固定宽度
				if(width > 40){
					sb.append(String
							.format(		//text-overflow:ellipsis;  省略号属性
									"<span id='%s' name='%s' type='text' style='padding:1px;width:%spx;text-align:%s;%s" +  
									"overflow:hidden;display:inline-block;white-space:nowrap;'  size='%s'>&nbsp;</span>",
										tagId, tagId, width -2 -2,textAlign ,fontStyle, len/2));
				}else{
					sb.append(String
							.format(		//text-overflow:ellipsis;  省略号属性
									"<span id='%s' name='%s' type='text' style='width:%spx;text-align:%s;%s" +   
									"overflow:hidden;display:inline-block;" +
									"white-space:nowrap;' size='%s'>&nbsp;</span>",
										tagId, tagId, width-2,textAlign ,fontStyle, len/2));
				}	
			}
			
			tc = tc.substring(id + len, tc.length());
			id = tc.indexOf("__");
			if (id == -1) {
				sb.append(tc);
			}
			
			doExcelColumnBo(tagId, table, tr, td,"TEXT","");
		}
	}

	/*
	 * 获得td字体样式 
	 */
	private String getFonStyle(ExcelTableTdBo td) {
		ExcelTableTdStyleBorderBo bo = td.getBo();
		String height = bo.getFontHeight();//字体大小
		String color = bo.getFontColor();//字体颜色
		String fontName = bo.getFontName();//字体样式
		String italic = bo.getFontItalic();//是否斜体
		
		String fontStyle = "";
		if(!StringsUtils.isEmpty(height)){
			fontStyle += String.format("font-size:%s;", height);
		}
		if(!StringsUtils.isEmpty(color)){
			fontStyle += String.format("color:%s;", color);
		}
		if(!StringsUtils.isEmpty(fontName)){
			fontStyle += String.format("font-family:%s;", fontName);
		}
		if(!StringsUtils.isEmpty(italic) && italic.equals("true")){
			fontStyle += "font-style:italic;";
		}
		return fontStyle;
	}
	
	private void doExcelColumnBo(String tagId, ExcelTableBo table,
			ExcelTableTrBo tr, ExcelTableTdBo td,String type,String widgeType) {
		ExcelColumnBo bo = new ExcelColumnBo();
		bo.setCode(tagId);
		bo.setCol(String.format("C%s", table.getParam().size()));
		bo.setTabId(table.getParam().size()+"");
		bo.setThisType(type);
		bo.setValidType(widgeType);
		table.getParam().put(tagId, bo);
	}
	
	private String getTagId(ExcelTableBo table){
		int startIdValue = table.getStartIdValue();
		
		if(startIdValue == -1){
			return String.format("A%s", table.getParam().size());
		}else{
			return String.format("A%s", table.getParam().size() + startIdValue);
		}
	}
}
