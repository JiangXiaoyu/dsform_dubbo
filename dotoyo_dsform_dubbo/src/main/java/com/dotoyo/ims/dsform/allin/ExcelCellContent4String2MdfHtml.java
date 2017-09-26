package com.dotoyo.ims.dsform.allin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.excel.ExcelColumnBo;
import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.util.StringsUtils;

/**
 * 
 * @author wangl
 * 
 */
public class ExcelCellContent4String2MdfHtml implements IExcelCellContent2Html {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(ExcelCellContent4String2MdfHtml.class));
	private PreformElm preformElm = null;
	
	private List<RightMenuTag> rightMenuList=new ArrayList<RightMenuTag>();
	
	@Override
	public PreformElm getPreformElm() {
		return preformElm;
	}

	@Override
	public String toHtml(org.apache.poi.ss.usermodel.Cell cell)
			throws Exception {
		return "";
	}

	public String toHtml(ExcelTableBo table, ExcelTableTrBo tr,
			ExcelTableTdBo td) throws Exception {
		//设置高度
		td.setHeight(PreformUtils.getTdSpanHeight(table, tr, td));
		StringBuilder sb = new StringBuilder();

		String tc = td.getContent().replaceAll(" ", "&nbsp;");
		int id = tc.indexOf("__");//文本框 文本域
		int idWidge = tc.indexOf("${");//特殊控件
		int idPic = tc.indexOf("$$");//图片
		if (id == -1 && idWidge == -1 && idPic== -1) {
			if((id=tc.indexOf("##"))!=-1){//编辑器
				toHtml4Editor(table, tr, td, tc, id, sb);
				return sb.toString();
			}else{//普通文本
				sb.append(tc);
				return sb.toString();
			}
		}
		
		// 处理textarea
		if (tc.startsWith("__")) {
			int id1 = tc.indexOf("__</br>__");
			if (id1 != -1) {
				//String tagId = String.format("A%s", table.getParam().size());
				String tagId = getTagId(table);
				int length = tc.replace("</br>", "").length();
				
				//宽度减去margin
				float width = Float.parseFloat(td.getWidth().substring(0, td.getWidth().indexOf("px")));
				String widthStr = (width -2) + "px";// 减去2px表示border
				
				float height = Float.parseFloat(td.getHeight().substring(0, td.getHeight().indexOf("px")));
				String heigthStr = ( height -2) + "px";//高度不用减太多
				
				String textAlign = td.getTextAlign();
				String verAlign = td.getVerAlign();
				//自适应高度不控制高度 与 字数
				if("auto".equals(table.getThisType())){
					sb.append(String
							.format(
									"<div style='width: %s; border: 1px solid #ccc;'>"+
									"<div id='%s' name='%s' class='area-edit' isTextarea='true' contenteditable='true' showmenu='true' style='width: %s; display: table-cell;"+
									" text-align:%s; vertical-align: %s; word-wrap: break-word; word-break: break-all;' ></div></div>",
									widthStr,tagId, tagId, widthStr,textAlign,verAlign));
				}else{
					sb.append(String
							.format(
									"<div style='width: %s; height: %s; border: 1px solid #ccc;'>"+
									"<div id='%s' name='%s' class='area-edit' isTextarea='true' contenteditable='true' showmenu='true' style='width: %s; height: %s;display: table-cell;"+
									" text-align:%s; vertical-align: %s; word-wrap: break-word; word-break: break-all;' size='%s' onfocus='toolBarFocusEvent(this);' onblur='toolBarBlurEvent(this);' oninput='changeEvent(this);' onpropertychange='changeEvent(this);'></div></div>",
									widthStr,heigthStr,tagId, tagId, widthStr, heigthStr,textAlign,verAlign, length/2));
				}
				// 添加右键
				RightMenuTag rightMenu=new RightMenuTag();
				rightMenu.setMenutype("menu4area");
				rightMenu.setRightMenuTagId(tagId);
				setRightMenuTag(rightMenu);
				
				preformElm = new PreformElm();
				preformElm.setWidgeName(tagId);
				preformElm.setWidgeId(tagId);
				preformElm.setWidgeType("textarea");
				preformElm.setStyle("border:1px solid #ccc;");
				preformElm.setWidth(td.getWidth());
				preformElm.setHeight(td.getHeight());
				preformElm.setStatus("1");
				preformElm.setCreateTime(new Date());
				preformElm.setLength((tc.replace("</br>", "")).length() +"");
				doExcelColumnBo(tagId, table, tr, td,"TEXTAREA","");
				//控件id与excel行列关系
				table.getKeyValue().put(WidgeUtils.getColumnName(td.getColNum() + 1) + (tr.getRowNum()+1),  tagId);
				return sb.toString();
			}
		}
		toHtml4Input(table, tr, td, tc, id, sb);
		toHtml4Widget(table, tr, td, tc, sb);
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
				if("0".equals(ps.getMdfStatus())){//状态为0表示未被占用
					picStyle = ps;
					ps.setMdfStatus("1");
					break;
				}
			}
			if(picStyle == null){
				return;
			}
			
			double picWidth = picStyle.getWidth();
			double picHeight = picStyle.getHeight();
			
			sb.append(tc.substring(0, idPic));
			
			String picPath = PreformConfig.getInstance().getConfig(PreformConfig.PIC_FTP_PATH);//http://f.10333.com/preform
			sb.append(String.format(
									"<img src='%s/%s' style='padding:2px;width:%spx;height:%spx;' />",picPath,picStyle.getId(), 
									picWidth-4,picHeight)); //减去4 表示左右两边2px的留白
			tc = tc.substring(idPic + 2, tc.length());
			idPic = tc.indexOf("$$");
			if (idPic == -1) {
				sb.append(tc);
			}
			
			preformElm = new PreformElm();
			preformElm.setWidgeName("pic");
			preformElm.setWidgeId("pic");
			preformElm.setWidgeType("pic");
			preformElm.setWidth(picWidth+"px");
			preformElm.setHeight(td.getHeight());
			preformElm.setLength("0");
			preformElm.setStatus("1");
			preformElm.setCreateTime(new Date());
		}
	}
	
	private void toHtml4Widget(ExcelTableBo table, ExcelTableTrBo tr,
			ExcelTableTdBo td, String tc, StringBuilder sb) throws Exception {
		String content = td.getContent();
		int index = content.indexOf("${");
		int index2 = content.indexOf("}",index);
		//宽度
		float width = Float.parseFloat(td.getWidth().substring(0, td.getWidth().indexOf("px")));
		//高度
		int height = Integer.parseInt(td.getHeight().substring(0, td.getHeight().indexOf("px")));
		
		String fontStyle = getFontStyle(td);
		String textAlign = td.getTextAlign();
		
		if(index != -1 && index2 != -1) {
			String tagId = getTagId(table);
			String widgeType = content.substring(index+2,index2);
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
			
			//当前页和总页数
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
			IWidgeType projectNameWidge = new ItemProjectNameWidge(textarea);//单位(子单位)工程名称
			IWidgeType curPositionWidge = new CompanyWidge(projectNameWidge);//养护部位
			IWidgeType currencyWidge = new CurrencyWidge(curPositionWidge);//企业名称
			IWidgeType postionWidge = new PostionWidge(currencyWidge);//检验批部位
			IWidgeType leaderWidge = new LeaderWidge(postionWidge);//项目负责人
			IWidgeType inspnumWidge = new InspnumWidge(leaderWidge);//检验批数量
			IWidgeType capacityWidge = new CapacityWidge(inspnumWidge);//检验批容量
			IWidgeType inspectionWidge = new InspectionWidge(capacityWidge);//检验批名称
			IWidgeType quantityWidge = new QuantityWidge(inspectionWidge);//分项工程工程量
			IWidgeType subsecnumWidge = new SubsecnumWidge(quantityWidge);//分项工程工程量
			IWidgeType technicalleaderWidge = new TechnicalleaderWidge(subsecnumWidge);//项目技术负责人
			
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
			String wtype = hidden.append2EditHtml(widgeType, sb, width, height, tagId, textAlign, fontStyle);
			table.getKeyValue().put(WidgeUtils.getColumnName(td.getColNum() + 1) + (tr.getRowNum()+1),  tagId);
			
			preformElm = new PreformElm();
			preformElm.setWidgeName(tagId);
			preformElm.setWidgeId(tagId);
			preformElm.setWidgeType("text");
			preformElm.setAlign(wtype);
			preformElm.setStyle("border:1px solid #ccc;");
			preformElm.setWidth(td.getWidth());
			preformElm.setHeight(td.getHeight());
			preformElm.setStatus("1");
			preformElm.setCreateTime(new Date());
			preformElm.setLength((tc.replace("</br>", "")).length() +"");
			
			doExcelColumnBo(tagId, table, tr, td,"TEXT",wtype);
		}
	}

	private void toHtml4Editor(ExcelTableBo table, ExcelTableTrBo tr,
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
			//计算合并单元格的高度
			int height = Integer.parseInt(td.getHeight().substring(0, td.getHeight().indexOf("px")));
			String heightStr = (height - 4) + "px";
			
			//计算宽度减去 padding的
			float width = Float.parseFloat(td.getWidth().substring(0, td.getWidth().indexOf("px")));
			String widthStr = (width - 20) + "px";//减去左右两边的padding - bording
			
			//常用编辑器	
			//sb.append(String.format("<textarea id='%s' name='%s' isEditor='true' style='margin:0px 20px 0px 20px;line-height:1.3;height:%spx;border:1px solid #ccc;'></textarea>",
			//		tagId, tagId, height));
			
			//div内联需要 contenteditable='true' 
			sb.append(String.format("<div id='%s' name='%s' isEditor='true' contenteditable='true' style='font-family:ds;padding:2px 5px;margin:2px 5px;line-height:1.3;overflow:scroll;height:%s;width:%s;border:1px solid #ccc;'></div>",
					tagId, tagId, heightStr, widthStr));
			
			//textarea内联式编辑器
			/*sb.append(String.format("<textarea id='%s' name='%s' isEditor='true' style='margin:0px 20px 0px 20px;line-height:1.3;overflow:hidden;height:%spx;border:1px solid #ccc;'></textarea>",
					tagId, tagId, height));*/
			String prefixPath = PreformConfig.getInstance().getConfig(PreformConfig.DS_PATH);//http://dsframe.10333.com

			
			//sb.append(String.format("<script type='text/javascript'>/*ckeditor*/ var %s = CKEDITOR.replace('%s',{height:%s});",tagId,tagId,height)); //n是推算出来的

			//常用编辑器	
			//sb.append(String.format("<script type='text/javascript'>/*ckeditor*/var %s = CKEDITOR.replace('%s',{customConfig:'%s/js/ckeditor/config.js',height:%s});",tagId,tagId,prefixPath,height));
			
			if(!table.isHasEditor()){
				//引入ckeditor js
				sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/ckeditor/ckeditor.js\"></SCRIPT>\n",prefixPath));
				sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/ckfinder/ckfinder.js\"></SCRIPT>\n",prefixPath));
				//添加图片按钮
				sb.append("<script> \r\n");
				sb.append("	$(function(){ \r\n");
				sb.append(String.format("		addUploadButton();\r\n",tagId));
				sb.append("	});\r\n");
					
				sb.append("		var theURLElementId = ''; \r\n");//url输入框的id
				
				sb.append("	function addUploadButton(){\r\n");
				
				//工程图片   本地图片
				sb.append("		CKEDITOR.on('dialogDefinition', function( ev ){\r\n");
				sb.append("			var dialogName = ev.data.name;\r\n");
				sb.append("			var dialogDefinition = ev.data.definition;\r\n");
				sb.append("			if ( param != null && param.fromtype!= undefined && param.fromtype =='ent'  ){	\r\n");
				sb.append("					return;	\r\n");
				sb.append("			}	\r\n");
				sb.append("			if ( dialogName == 'image2' ){\r\n");
				sb.append("			var infoTab = dialogDefinition.getContents( 'info' );\r\n");
				
				sb.append("			infoTab.add({\r\n");
				sb.append("				type : 'button',\r\n");
				sb.append("				id : 'upload_image',\r\n");
				sb.append("				align : 'center',\r\n");
				sb.append("				label : '工程照片',\r\n");
				sb.append("				onClick : function( evt ){\r\n");
				sb.append("							var thisDialog = this.getDialog();\r\n");
				sb.append("							var txtUrlObj = thisDialog.getContentElement('info', 'src');\r\n");
				sb.append("							theURLElementId = txtUrlObj.getInputElement().$.id;\r\n");
				sb.append("							addUploadImage();\r\n");
				sb.append("				}\r\n");
				sb.append("				}, 'browse');\r\n");
				sb.append("			}\r\n");
				sb.append("		});\r\n");
				sb.append("  }\r\n");
				sb.append("			function addUploadImage(){\r\n");
				//http://org.10333.com
				String orgUrl = PreformConfig.getInstance().getConfig(PreformConfig.ORG_PATH);
				//工程图片上传的页面URL，http://org.10333.com/picture/picturelist?showType=selectList
				sb.append(String.format("var uploadUrl = '%s/picture/picturelist?showType=selectList'; \r\n", orgUrl));
				sb.append("				window.open(uploadUrl);\r\n");
				sb.append("			} \r\n");
				
				sb.append("		function setUrl(url){\r\n");
				sb.append("			var urlObj = document.getElementById(theURLElementId);\r\n");
				sb.append("			urlObj.value = url;\r\n");
				sb.append("			if (urlObj.fireEvent){\r\n");
				sb.append("				urlObj.fireEvent('onchange');\r\n");
				sb.append("			}\r\n");
				sb.append("			else{\r\n");
				sb.append("				var evt = document.createEvent('HTMLEvents');\r\n");
				sb.append("				evt.initEvent('change', true, false); \r\n");
				sb.append("				urlObj.dispatchEvent(evt); \r\n");
				sb.append("			}\r\n");
				sb.append("		}\r\n");
				sb.append("</script>\r\n");
				
				table.setHasEditor(true);//第二次不重复加载  系统图片按钮
			}
			
			//内联式编辑器
			sb.append("<script type='text/javascript'>/*ckeditor*/");
			sb.append(" 	CKEDITOR.disableAutoInline = true;");
			sb.append(String.format("	var %s = CKEDITOR.inline('%s');",tagId,tagId)); // ID of the element to edit
			
			//常用编辑 ckfinder
			sb.append(String.format("CKFinder.setupCKEditor( %s, '%s/js/ckfinder/' );",tagId,prefixPath));
			sb.append(String.format("editorList.%s=%s;</script>",tagId,tagId));
			
			preformElm = new PreformElm();
			preformElm.setWidgeName(tagId);
			preformElm.setWidgeId(tagId);
			preformElm.setWidgeType("editor");
			preformElm.setStyle("border:1px solid #ccc;");
			preformElm.setWidth(td.getWidth());
			preformElm.setHeight(td.getHeight());
			preformElm.setStatus("1");
			preformElm.setCreateTime(new Date());
			preformElm.setLength((tc.replace("</br>", "")).length() +"");

			sb.append(" <input type='hidden' id='urlInput'> ");
			doExcelColumnBo(tagId, table, tr, td,"EDITOR","");
			td.setEditor(true);
		}
	}

	private void toHtml4Input(ExcelTableBo table, ExcelTableTrBo tr,
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
			String fontStyle = getFontStyle(td);
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
			
			//如果文本框过小，则不必留间距
			if(width > 40){
				sb.append(String
					.format(
							"<input id='%s' name='%s' type='text' style='padding:1px;margin-bottom:3px;width:%spx;text-align:%s;border:1px solid #ccc;%s'  size='%s' onfocus='toolBarFocusEvent(this);' onblur='toolBarBlurEvent(this);' oninput='changeEvent(this);' onpropertychange='changeEvent(this);' />",
									tagId, tagId, width-2-2, textAlign ,fontStyle, len/2));//左右边的padding 减去2px表示border
			}else{
				sb.append(String
						.format(
								"<input id='%s' name='%s' type='text' style='margin-bottom:3px;width:%spx;text-align:%s;border:1px solid #ccc;'  size='%s' onfocus='toolBarFocusEvent(this);' onblur='toolBarBlurEvent(this);' oninput='changeEvent(this);' onpropertychange='changeEvent(this);' />",
										tagId, tagId, width-2, textAlign , fontStyle, len/2));
			}
			//文本框添加右键菜单。
			RightMenuTag rightMenu=new RightMenuTag();
			rightMenu.setMenutype("menu4input");
			rightMenu.setRightMenuTagId(tagId);
			setRightMenuTag(rightMenu);
			//
			tc = tc.substring(id + len, tc.length());
			id = tc.indexOf("__");
			if (id == -1) {
				sb.append(tc);
			}
			
			preformElm = new PreformElm();
			preformElm.setWidgeName(tagId);
			preformElm.setWidgeId(tagId);
			preformElm.setWidgeType("text");
			preformElm.setStyle("border:1px solid #ccc;");
			preformElm.setWidth(td.getWidth());
			preformElm.setHeight(td.getHeight());
			preformElm.setStatus("1");
			preformElm.setCreateTime(new Date());
			preformElm.setLength(tc.length() +"");
			//控件id与Excel行列关系
			table.getKeyValue().put(WidgeUtils.getColumnName(td.getColNum() + 1) + (tr.getRowNum()+1),  tagId);
			doExcelColumnBo(tagId, table, tr, td,"TEXT","");
		}

	}
	
	/*
	 * 获得td字体样式 
	 */
	private String getFontStyle(ExcelTableTdBo td) {
		ExcelTableTdStyleBorderBo bo = td.getBo();
		String fontSize = bo.getFontHeight();//字体大小
		String fontFamily = bo.getFontName();
		StringBuffer sb = new StringBuffer("");
		
		if(!StringsUtils.isEmpty(fontSize)){
			sb.append(String.format("font-size:%s;", fontSize));
		}
		if(!StringsUtils.isEmpty(fontFamily)){
			sb.append(String.format("font-family:%s;", fontFamily));
		}
		return sb.toString();
	}

	@Override
	public List<RightMenuTag> getRightMenuList() {
		return rightMenuList;
	}
	
	public void setRightMenuTag(RightMenuTag tag) {
		rightMenuList.add(tag);
	}

	private void doExcelColumnBo(String tagId, ExcelTableBo table,
			ExcelTableTrBo tr, ExcelTableTdBo td,String type,String widge) {
		ExcelColumnBo bo = new ExcelColumnBo();
		bo.setCode(tagId);
		bo.setCol(String.format("C%s", table.getParam().size()));
		bo.setTabId(table.getParam().size()+"");
		bo.setThisType(type);
		bo.setValidType(widge);
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
