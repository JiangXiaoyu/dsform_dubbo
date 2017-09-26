package com.dotoyo.ims.dsform.allin;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

import com.dotoyo.dsform.excel.ExcelPictureBo;
import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.model.AnnexModel;
import com.dotoyo.dsform.util.StringsUtils;

public class Excel2MdfHtml implements IExcel2Html {
	protected static final transient IFrameLog log = new LogProxy(LogFactory.getLog(Excel2MdfHtml.class));
	private List<PreformElm> elmList = new ArrayList<PreformElm>();
	private Set<RightMenuTag> rightMenuTagSet = new HashSet<RightMenuTag>();
	
	public static final int MAX_CELL_SUM = 10000;
	public static final int MAX_ROW_SUM = 10000;
	
	//控件的背景颜色
	private final static String WIDGE_BACKGROUND_COLOR = "#FFDEAD";
	
	public static final String PRINT_HORZ = "horz";//横向打印标示
	
	@Override
	public List<PreformElm> getPreformElmList() {
		return elmList;
	}

	@Override
	public Set<RightMenuTag> getRightMenuTagSet() {
		return rightMenuTagSet;
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
					workbook.close();
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
		sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>");
		sb.append("<meta http-equiv=\"pragma\" CONTENT=\"no-cache\">");
		sb.append("<meta http-equiv=\"Cache-Control\" CONTENT=\"no-cache, must-revalidate\">");
		sb.append("<meta http-equiv=\"expires\" CONTENT=\"0\">");
		
		String domain = PreformConfig.getInstance().getConfig(PreformConfig.DOMAIN);//10333.com
		String version = PreformConfig.getInstance().getConfig(PreformConfig.VERSION);//10333.com
//		String prefixPath = PreformConfig.getInstance().getConfig(PreformConfig.ORG_PATH);//http://dsframe.10333.com
		String prefixPath = "http://org.10333.com";//http://dsframe.10333.com
		
		sb.append(String.format("<link rel=\"stylesheet\" type=\"text/css\" href=\"%s/preformCss.css?version=%s\">", prefixPath, version));
		sb.append(String.format("<link rel=\"stylesheet\" type=\"text/css\" href=\"%s/js/My97DatePicker/skin/WdatePicker.css?version=%s\">", prefixPath, version));
		//sb.append("<title>" + title + "</title>");
		
		//############ 本地测试时会自动去掉domain信息
		sb.append(String.format("<SCRIPT type='text/javascript'>document.domain='%s';</SCRIPT>",domain));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/jquery/jquery.js?version=%s\"></SCRIPT>", prefixPath, version));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/easyui/jquery.easyui.min.js?version=%s\"></SCRIPT>", prefixPath, version));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/easyui/locale/easyui-lang-zh_CN.js?version=%s\"></SCRIPT>", prefixPath, version));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/frame/js/zh_CN/frame.js?version=%s\"></SCRIPT>", prefixPath, version));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/jquery/json2.js?version=%s\"></SCRIPT>", prefixPath, version));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/layer/layer.js?version=%s\"></SCRIPT>", prefixPath, version));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/JSExpressionEval/Evaluator.js?version=%s\"></SCRIPT>", prefixPath, version));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/JSExpressionEval/Tokanizer.js?version=%s\"></SCRIPT>", prefixPath, version));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/JSExpressionEval/Stack.js?version=%s\"></SCRIPT>", prefixPath, version));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/JSExpressionEval/Date.js?version=%s\"></SCRIPT>", prefixPath, version));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/My97DatePicker/WdatePicker.js?version=%s\"></SCRIPT>", prefixPath, version));		
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/preform/excel2MdfHtml.js?version=%s\"></SCRIPT>", prefixPath, version));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/underscore/underscore.js?version=%s\"></SCRIPT>", prefixPath, version));
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/highcharts/js/highcharts.js\"></SCRIPT>", prefixPath));	
		sb.append(String.format("<SCRIPT type=\"text/javascript\" src=\"%s/js/highcharts/js/highcharts-3d.js\"></SCRIPT>", prefixPath));	
		getScript(sb);
		return sb;
	}

	private String getScript(StringBuffer s) throws Exception {
		PreformStringBuffer sb = new PreformStringBuffer(s);
//		String prefixPath = PreformConfig.getInstance().getConfig(PreformConfig.DS_PATH);//http://dsframe.10333.com
		String prefixPath = "http://org.10333.com";//http://dsframe.10333.com
		String orgPath = PreformConfig.getInstance().getConfig(PreformConfig.ORG_PATH);//http://org.10333.com
		String sysPath = PreformConfig.getInstance().getConfig(PreformConfig.SYS_PATH);//http://org.10333.com
		
 		sb.append(" <SCRIPT type='text/javascript'> ");
		sb.append("     var param = {}; ");//调用时传递的参数
		sb.append("     var map = {}; ");//excel id 与 tagId
		sb.append("		var editorList = {}; ");
		sb.append(" 	var textData = null;	");
		sb.append(" 	var selectData = null;	");
		sb.append("     var iframeId = ''; ");
		
		//设置prefromCode的值
		sb.append(" $(function(){ ");
		sb.append("		resizeParantFrame();	");							//设置父页面大小
		sb.append(" 	resizeToolBar();	");								//设置工具栏
		sb.append(" 	parseRequestParam(); ");							//获得请求的参数对象
		sb.append(" 	parseTagId(); ");									//控件id与Excel的标示对应关系
		sb.append("		layer.config({extend: 'extend/layer.ext.js'}); ");	//加载layer.js
		sb.append(" 	colourOnWidge(); ");								//特殊控件着色处理
		sb.append(" 	loadToolbar(); ");									//工具栏js
		sb.append(" 	loadPrintDirect(); ");								//加载打印方向
		sb.append(" 	loadShowSn(); ");									//加载流水号
		sb.append(" 	loadTextData(); ");									//加载文本数据
		sb.append(" 	loadSelData(); ");									//加载下拉数据
		sb.append(" 	loadCopyData(); ");									//加载复制数据
		sb.append(" 	loadStrGrd(); ");									//加载强度等级
		sb.append(" 	initToolbarStat(); ");								//初始化工具栏的状态
		sb.append(" 	loadSpeChars(); ");									//加载特殊字符
		sb.append(" 	initTestRec(); ");									//初始化检验批的检查记录
		sb.append(" 	initBrList(); ");									//初始编号的分部列表
		sb.append(" 	initSecData(); ");									//初始分部子分部分项数据
		sb.append(" 	handleSumData(); ");								//处理汇总数据
		sb.append(" 	loadSecData(); ");									//加载单位工程分部子分部分项部位等数据
		sb.append(" 	handleBatchData(); ");								//处理批量传值数据（子分部工程名称/分项工程名称/检验批数量）
		sb.append(" 	loadTitleData(); ");								//加载自义定标题数据
		sb.append("     iframeId = getFormContentId(); ");
		
		//编辑器加载完毕后，重新设置父页面大小
		sb.append("		if('undefined' != typeof CKEDITOR ){ ");
		sb.append("		CKEDITOR.on('instanceReady', function (e) { ");
		sb.append("			resizeParantFrame();	");
		sb.append("		});}");
		sb.append("		var _id = param._id; ");
		sb.append("     if(_id!='null' && _id!=''){ ");
		sb.append(String.format(" 		var url='http://org.10333.com/formInstance/getPreformData?_id=' + _id; ", orgPath));
		sb.append("			var json=''; ");
		sb.append(" 		var index = layer.load(2); ");//遮罩开始
		sb.append(" 		$.ajax({ ");
		sb.append(" 			type: 'POST', ");
		sb.append(" 			contentType: 'application/json;charset=utf-8', ");
		sb.append(" 			url:  url, ");
		sb.append(" 			data: json, ");
		sb.append(" 			async: false, ");//这里必须为false，解决编辑器加载数据有几率失败的问题
		sb.append(" 			dataType : 'json', ");
		sb.append(" 			success : function(data) { ");
		sb.append(" 				layer.close(index); ");//遮罩关闭
		sb.append("         		if(data && data.body){ ");
		sb.append("						setDataBody(data.body); "); 
		sb.append("					} "); 
		sb.append("     			var height = calcPageHeight(); ");//在数据加载完后，重新设置iframe高度
		sb.append("     			if(parent.document != null && parent.document.getElementById(iframeId)!=null){ ");
		sb.append("     				parent.document.getElementById(iframeId).style.height = height + 100 + 'px' ; ");
		sb.append("     				parent.document.getElementById(iframeId).style.width = '1198px' ; ");
		sb.append(" 				}	");
		sb.append(" 			}, ");
		sb.append(" 			error : function(){  ");
		sb.append(" 				layer.close(index); ");//遮罩关闭
		sb.append(" 			}	 ");
		sb.append(" 		}); ");
		sb.append(" 	}else{ ");
		sb.append(" 		initDspl(); ");//初始化沉降位移
		sb.append(" 	}	");
		sb.append(" }); ");
		
		sb.append(" function setDataBody(body){ ");
		sb.append("		loadStyleByJson(body); ");
		sb.append("		loadSpanByJson(body); ");
		sb.append("		setWidgeValue(body); ");
		sb.append("		initDspl(body); ");
		sb.append(" }; ");

		//加载样式(fontSize size align)
		sb.append("		function loadStyleByJson(data){ ");
		sb.append(" 	  	var styData = data.intSty;");
		sb.append(" 	  	var tempMap = preformConfig.styAttr;");
		sb.append(" 		if(styData != null){");
		sb.append(" 	  		for(var i in styData){");
		sb.append(" 	 			if($('#'+i).size()==1 ){");
		sb.append(" 	 				if(styData[i].fontSize!=null){ ");
		sb.append(" 	 					$('#'+i).parent().css('font-size',styData[i].fontSize);");
		sb.append(" 	 					$('#'+i).css('font-size',styData[i].fontSize);");//view时因为是span所以不需要
		sb.append(" 	  				}");
		sb.append(" 	 				if(styData[i].align!=null){ ");
		sb.append(" 	 					$('#'+i)[0].style.textAlign=styData[i].align;");
		sb.append(" 	  				}");
		sb.append(" 	 				if(styData[i].size!=null){ ");
		sb.append("							$('#'+i).attr('size',styData[i].size);");
		sb.append(" 	  				}");
		sb.append(" 	 				if(styData[i].valign!=null){ ");
		sb.append(" 	 					$('#'+i).css('vertical-align',styData[i].valign);");
		sb.append(" 	 					$('#'+i).parent().parent().attr('valign',styData[i].valign);");
		sb.append(" 	  				}");
		sb.append(" 	  				for(var attrName in styData[i]){	");//自动设置css属性
		sb.append(" 						if(tempMap[attrName]==1){	");
		sb.append(" 							$('#'+i).css(attrName,styData[i][attrName]);");
		sb.append(" 						}");
		sb.append(" 					}");
		sb.append(" 	  			}");
		sb.append(" 	 		}");
		sb.append(" 		}");
		sb.append(" 	}  ");
		 
		sb.append("		function loadSpanByJson(data){ ");
		sb.append("			for(var i in data){ ");
		sb.append("				var jqObj = $('#'+i); ");
		sb.append("		 		if(jqObj.size()==1 && jqObj.attr('widgetype') != 'snInput'){ "); //jqObj.attr('widgetype') != 'snInput' 编号不能修改
		sb.append("		 			var len = jqObj.attr('size'); ");
		sb.append("					var iseditor = jqObj.attr('iseditor'); ");
		sb.append("					var tagName = jqObj[0].tagName; ");
		sb.append("					var inpType = jqObj.attr('type'); ");
		sb.append("					if(tagName.toUpperCase() == 'SPAN' || (tagName.toUpperCase() == 'DIV' && iseditor == undefined)){ ");// 是span
		sb.append("						$('#'+i).html(data[i]); ");
		sb.append("					}else if(iseditor == 'true'){ ");//是ckeditor
		sb.append("    					 for(var o in editorList){ ");
		sb.append("     					if(editorList[o]['name'] == i){ ");
		sb.append("     						editorList[o].setData(data[i]); ");
		sb.append("     					} ");
		sb.append("    					 } ");
		sb.append("					}else if(tagName.toUpperCase() == 'INPUT' && inpType.toUpperCase() == 'CHECKBOX'){ ");
		sb.append("						jqObj.attr('checked','checked'); ");
		sb.append("					}else{ ");
		sb.append("		 				jqObj.val(data[i]); ");//是input 或  textarea
		sb.append("					} ");
		sb.append("		 		 } ");
		sb.append("		 	} ");
		sb.append("		} ");
		
		sb.append("     function setWidgeValue(data){ ");
		sb.append("      	var array = $('input,select,div');	");
		sb.append("			array.each(function(){	");
		sb.append("      		var widge = $(this).attr('widgetype');	");
		sb.append("      				console.info(widge)	");
		sb.append("      		var name = $(this).attr('name');	");//input控件name
		sb.append("				if(data[name] == undefined){	");
		sb.append("      			if(widge == 'date'){	");
		//sb.append("     		 		$(this).val(getCurDate());	");
		sb.append("      			}else if(widge == 'org'  && param.org){	");
		//sb.append("      				param.org= param.org.replace(/%2B/g, ' ');		");//将%2b替换为空格，因为 encodeURIComponent不会处理空格（空格变为+号）
		sb.append("      				$(this).text(param.org);	");
		sb.append("      				triggerChange($(this));");
		sb.append("      			}else if(widge == 'project' && param.project){	");
		//sb.append("      				param.project= param.project.replace(/%2B/g, ' ');		");//将%2b替换为空格，因为 encodeURIComponent不会处理空格（空格变为+号）
		sb.append("      				$(this).text(param.project);		");
		sb.append("      				triggerChange($(this));");
		sb.append("      			}else if(widge == 'weather'){	");
		sb.append("      				$(this).val(getWeaData());");
		sb.append("      				triggerChange($(this));");
		sb.append("      			}else if(widge == 'username'){	");
		//sb.append("      				$(this).val(decodeURIComponent(decodeURIComponent(param.username)));");
		sb.append("      			}else if(widge == 'dflt'){	");
		//sb.append("      				$(this).val($(this).attr('dflt'));");
		sb.append("      			}else if(widge == 'text3' || widge == 'unit'){	");
		sb.append(" 					$(\"div[widgetype='text3']\").html(param.orgItem);");
		sb.append(" 					$(\"div[widgetype='unit']\").html(param.orgItem);");
		sb.append("      			}else if(widge == 'itemprojectname' ){	");
		sb.append("      				$(this).text(param.itemprojectname);	");
		sb.append(" 					triggerChange($(this));");
		sb.append("      			}else if(widge == 'postion' ){	");
		sb.append("      				$(this).text(param.postion);	");
		sb.append(" 					triggerChange($(this));");
		sb.append("      			}else if(widge && widge.indexOf('company') > -1 ){	");
		sb.append("      				$(this).text(param[widge]);	");
		sb.append(" 					triggerChange($(this));");
		sb.append("      			}else if(widge == 'sgzbcompany' ){	");
		sb.append("      				$(this).text(param.sgzbcompany);	");
		sb.append(" 					triggerChange($(this));");
		sb.append("      			}else if(widge && widge.indexOf('leader') > -1 ){	");
		sb.append("      				$(this).text(param[widge]);	");
		sb.append(" 					triggerChange($(this));");
		sb.append("      			}else if(widge && widge.indexOf('str') > -1 ){	");
		sb.append("      				$(this).text(param[widge]);	");
		sb.append(" 					triggerChange($(this));");
		sb.append("      			}else if(widge =='unitn' ){	");
		sb.append("      				$(this).text(param.unitn);	");
		sb.append(" 					triggerChange($(this));");
		sb.append("      			}else if(widge =='secn' ){	");
		sb.append(" 					$(this).text(param.unitn);");
		sb.append(" 					triggerChange($(this));");
		sb.append("      			}else if(widge =='inspnum' ){	");
		sb.append(" 					$(this).text(param.inspnum);");
		sb.append(" 					triggerChange($(this));");
		sb.append("      			}else if(widge =='technicalleader' ){	");
		sb.append(" 					$(this).text(param.technicalleader);");
		sb.append(" 					triggerChange($(this));");
		sb.append("      			}else if(widge =='quantity' ){	");
		sb.append(" 					$(this).text(param.quantity);");
		sb.append(" 					triggerChange($(this));");
		sb.append("      			}else if(widge =='inspection' ){	");
		sb.append(" 					$(this).text(param.inspection);");
		sb.append(" 					triggerChange($(this));");
		sb.append("      			}else if(widge =='itemnum' ){	");
		sb.append(" 					$(this).text(param.itemnum);");
		sb.append(" 					triggerChange($(this));");
		sb.append("      			}else if(widge =='subsecn' ){	");
		sb.append(" 					$(this).text(param.subsecn);");
		sb.append(" 					triggerChange($(this));");
		sb.append("      			}else if(widge =='itemn' ){	");
		sb.append(" 					$(this).text(param.itemn);");
		sb.append(" 					triggerChange($(this));");
		sb.append("      			}else if(widge =='partn' ){	");
		sb.append(" 					$(this).text(param.partn);");
		sb.append(" 					triggerChange($(this));");
		sb.append(" 				}else if(widge =='subsecnum' ){	");
		sb.append(" 					$(this).text(param.subsecnum);");
		sb.append(" 					triggerChange($(this));");
		sb.append(" 				}else if(widge =='secsub' ){	");
		sb.append(" 					$(this).text(param.secsub);");
		sb.append(" 					triggerChange($(this));");
		sb.append(" 				}else if(widge =='date1' ){	");
		sb.append(" 					$(this).text(param.date1);");
		sb.append(" 					triggerChange($(this));");
		sb.append(" 				}else if(widge =='date2' ){	");
		sb.append(" 					$(this).text(param.date2);");
		sb.append(" 					triggerChange($(this));");
		sb.append(" 				}else if(widge =='date3' ){	");
		sb.append(" 					$(this).text(param.date3);");
		sb.append(" 					triggerChange($(this));");
//		sb.append("      			}else if(widge && widge.indexOf('select') > -1){	");
//		sb.append("      				$(this).append('<option value=\"\">请选择</option>');");
//		sb.append("      				var obj = eval(param[widge]);	");
//		sb.append("      				var comsel = '${comsel}'	");
//		sb.append("      				if(obj){	");
//		sb.append("      					for(var i =0; i < obj.length;i++ ){	");  					
//		sb.append("      						$(this).append('<option attrval='+obj[i].id+' value='+obj[i].name+'>'+obj[i].name+'</option>')");
//		sb.append("      					}	");
//		sb.append("      				}	");
//		sb.append(" 					triggerChange($(this));");
		sb.append("      			}");
		sb.append("      		}	");
		sb.append("      		if(widge == 'notice' && param.notice_Ids!=undefined){	");
		sb.append("      			var curObj = $(this); 		");
		sb.append(String.format("url='%s/formInstance/getFullSn?_ids=' + param.notice_Ids; ", prefixPath));
		sb.append(" 				$.ajax({ ");
		sb.append(" 					type: 'POST', ");
		sb.append(" 					contentType: 'application/json;charset=utf-8', ");
		sb.append(" 					url:  url, ");
		sb.append(" 					async: false, ");
		sb.append(" 					dataType : 'json', ");
		sb.append(" 					success : function(data) { ");
		sb.append(" 						var snArray = data.body;");
		sb.append("      					for(var i=0; i < snArray.length; i++){		");
		sb.append(" 							var snObj = snArray[i]; ");
		sb.append("      						curObj.append('<option id=' + snObj._id + '>' + snObj.fullSn + '</option>');	");
		sb.append("      					}	");
		sb.append("      					if(getPtcpInput(curObj).val()==''){		");
		sb.append("      						curObj[0].selectedIndex = 0;	");
		sb.append("      						selectChangeEvent(curObj);	");//下拉框出发文本框改变
		sb.append("      						triggerChange(curObj);	");//触发文本框改变事件	
		sb.append("      					}else{		");
		sb.append("      						curObj[0].selectedIndex = -1;	");
		sb.append("      					}		");
		sb.append(" 					}, ");
		sb.append(" 					error : function(){  ");
		sb.append(" 					}	 ");
		sb.append(" 				}); ");
		sb.append("      		}	");
		sb.append("      	});	");
		sb.append("      }	");
		
		//显示流水号		
		sb.append("		function loadShowSn(){ 	");
		sb.append("			var paramObj = {}; ");
		sb.append("			paramObj.preformId = param.id; ");
		sb.append("			paramObj._id = param._id; ");
		sb.append("			paramObj.companyId = param.companyId; ");
		sb.append("			paramObj.projectId = param.projectId; ");
		sb.append("			paramObj.orgItemId = param.orgItemId; ");
		sb.append(" 		var json = JSON.stringify(paramObj); ");//单位工程
		sb.append(String.format(" 		var url='http://org.10333.com/formInstance/loadShowSn'; ", orgPath));
		sb.append(" 		$.ajax({ ");
		sb.append(" 			type: 'POST', ");
		sb.append(" 			contentType: 'application/json;charset=utf-8', ");
		sb.append(" 			url:  url, ");
		sb.append(" 			data:  json, ");
		sb.append(" 			async: false, ");
		sb.append(" 			dataType : 'json', ");
		sb.append(" 			success : function(data) { ");
		sb.append("         		if(data != null && data.body != null ){ ");
		sb.append("							setShowSn(data.body);"); 
		sb.append("					} "); 
		sb.append("		 		} ");
		sb.append("		 	});	");
		sb.append("		 }	");
		
		//加载打印方向
		sb.append("		function loadPrintDirect(){ 	");
		sb.append("			var id = param.id; ");
		sb.append(String.format(" 		var url='http://org.10333.com/formInstance/loadPrintDirect?id=' + id; ", orgPath));
		sb.append(" 		$.ajax({ ");
		sb.append(" 			type: 'POST', ");
		sb.append(" 			contentType: 'application/json;charset=utf-8', ");
		sb.append(" 			url:  url, ");
		sb.append(" 			async: true, ");//这里必须为false，解决编辑器加载数据有几率失败的问题
		sb.append(" 			dataType : 'json', ");
		sb.append(" 			success : function(data) { ");
		sb.append("         		if(data != null && data.body != null ){ ");
		sb.append("						$('#printDirec').val(data.body.printDirec); ");
		sb.append("						$('#note').val(data.body.note); ");
		sb.append("					} "); 
		sb.append("		 		}");
		sb.append("		 	});	");
		sb.append("		 }	");
		
		//获取天气信息
		sb.append("		function getWeaData(){ 	");
		sb.append("			var ip = param.ip; ");
		sb.append(String.format(" 		var url='%s/formInstance/getWeaDataByIp?ip=' + ip; ", prefixPath));
		sb.append(" 		var result = ''; ");
		sb.append(" 		$.ajax({ ");
		sb.append(" 			type: 'POST', ");
		sb.append(" 			contentType: 'application/json;charset=utf-8', ");
		sb.append(" 			url:  url, ");
		sb.append(" 			data: '', ");
		sb.append(" 			async: false, ");
		sb.append(" 			dataType : 'json', ");
		sb.append(" 			success : function(data) { ");
		sb.append("         		if(data != null){ ");
		sb.append("						result = data.body.result; ");
		sb.append("					} "); 
		sb.append(" 			}, ");
		sb.append(" 			error : function(){  ");
		sb.append(" 			}	 ");
		sb.append(" 		}); ");
		sb.append("		 	return result;	");
		sb.append("		 }	");
		
		sb.append("		function dblclickEvent(arg,obj){ ");
		sb.append("     	if(arg=='username' && param.username!=undefined){		");
		//sb.append("      		param.username= param.username.replace(/%2B/g, ' ');		");//将%2b替换为空格，因为 encodeURIComponent不会处理空格（空格变为+号）
		sb.append("     		$(obj).val(param.username);		");
		sb.append("      		triggerChange($(obj));	");
		sb.append("     	}else if(arg == 'dflt'){	");
		sb.append("      		$(obj).val($(obj).attr('dflt'));	");
		sb.append("      		triggerChange($(obj));");
		sb.append("     	}	");
		sb.append(" 	}  ");
		
		//预览时需要调用此方法
		sb.append("		function getAllData(){ 	 ");
		sb.append("			var obj = {}; 	 ");
		sb.append("			var a = $('#preformForm').serializeArray();");
		sb.append("			$.each(a, function(){  	 ");
		sb.append("				if(this.value == '') return true;	 ");
		sb.append("				if(this.name == 'paramValue'){return true;}	 ");//预览时不需要此属性
		sb.append("				if(obj[this.name]){  	 ");
		sb.append("					if (!obj[this.name].push) { ");
		sb.append(" 			 		obj[this.name] = [obj[this.name]]; ");
		sb.append(" 				} ");
		sb.append("					obj[this.name].push(this.value || ''); ");
		sb.append(" 			}else {   ");
		sb.append(" 				obj[this.name] = this.value || ''; ");
		sb.append(" 	  		}");
		sb.append(" 		 });  ");
		sb.append("			$.each($('span[id][name]'), function(){  	 ");
		sb.append("		 		if($(this).text()=='' ) return true; ");
		sb.append("		 		obj[$(this).attr('id')] = $(this).text(); ");
		sb.append("			});  ");
		sb.append(" 		obj.id=param.id; ");
		sb.append(" 		obj._id=param._id; ");
		sb.append(" 		obj.orgId=param.orgId; ");
		sb.append(" 		obj.orgItemId=param.orgItemId; ");//单位工程
		sb.append(" 		obj.preformName=param.preformName; ");
		sb.append(" 		obj.intSty=intSty; ");//输入框样式
		sb.append(" 		obj.isView=true; 	");//编辑时可预览标示
		sb.append(" 		geneSpecData(obj); 	");//处理特殊数据
		sb.append("      	removeNullAttr(obj);	");//删除无用属性
		sb.append(" 		json = JSON.stringify(obj); ");
		sb.append(" 		json = encodeURIComponent(encodeURIComponent(json)); ");
		sb.append("			return json; 	 ");
		sb.append("		} 	 ");
		
		//保存时调用此方法
		sb.append("		function getData(){ 	 ");
		sb.append("			var obj = {}; 	 ");
		sb.append("			var a = $('#preformForm').serializeArray();");
		sb.append("			$.each(a, function(){  	 ");
		sb.append("				if(this.value == '') return true;	 ");
		sb.append("				if(obj[this.name]){  	 ");
		sb.append("					if (!obj[this.name].push) { ");
		sb.append(" 			 		obj[this.name] = [obj[this.name]]; ");
		sb.append(" 				} ");
		sb.append("					obj[this.name].push(this.value || ''); ");
		sb.append(" 			}else {   ");
		sb.append(" 				obj[this.name] = this.value || ''; ");
		sb.append(" 	  		}");
		sb.append(" 		});  ");
		sb.append(" 		obj.id=param.id; ");
		sb.append(" 		obj._id=param._id; ");
		sb.append(" 		obj.orgId=param.orgId; ");
		sb.append(" 		obj.orgItemId=param.orgItemId; ");//单位工程
		sb.append(" 		obj.companyId=param.companyId; ");
		sb.append(" 		obj.projectId=param.projectId; ");
		sb.append(" 		obj.preformId=param.id; ");
		sb.append(" 		obj.intSty=intSty; ");//输入框样式
		sb.append(" 		obj.draft =$('#draft').val(); ");//草稿
		sb.append(" 		geneSpecData(obj); 	");//处理特殊数据
		sb.append("      	removeNullAttr(obj);	");//删除无用属性
		sb.append(" 		json = JSON.stringify(obj); ");
		sb.append(" 		json = encodeURIComponent(encodeURIComponent(json)); ");
		sb.append("			return json; 	 ");
		sb.append("		} 	 ");
		
		//保存数据
		sb.append(" function saveData(){ ");  
		sb.append("     var result = {}; ");
		sb.append("     var json = getData(); ");
		sb.append(String.format(" 	var  url='%s/formInstance/savePreformData'; ", orgPath));
		sb.append(" 	$.ajax({ ");
		sb.append(" 		type: 'POST', ");
		sb.append(" 		contentType: 'application/json;charset=utf-8', ");
		sb.append(" 		url:  url, ");
		sb.append(" 		data: json, ");
		sb.append(" 		async: false, ");
		sb.append(" 		dataType : 'json', ");
		sb.append(" 		success : function(data) { ");
		sb.append("         	if(data != null){ ");
		sb.append("         		result=data; ");
		sb.append("				} "); 
		sb.append(" 		}, ");
		sb.append(" 		error : function(){  ");
		sb.append(" 		  layer.alert('保存失败!'); ");
		sb.append(" 		}	 ");
		sb.append(" 	}); ");
		sb.append(" 	return result; ");
		sb.append(" } ");
		
		sb.append(" function viewPreform(){ ");
		sb.append("     var param1 = getAllData(); ");
		sb.append(" 	var temp_form = document.createElement('form');  ");
		sb.append(String.format(" 	temp_form.action = '%s/preform/viewPreform';   ",prefixPath));  
		sb.append(" 	temp_form.target = '_blank';	");
		sb.append(" 	temp_form.method = 'post';      ");
		sb.append(" 	temp_form.style.display = 'none'; ");
		sb.append("		var opt = document.createElement('textarea');	");  
		sb.append("		opt.name = 'paramValue';  ");
		sb.append("		opt.value = param1;  ");    
		sb.append("		temp_form.appendChild(opt);  ");  
		sb.append("		 ");
		sb.append("		opt = document.createElement('textarea');	");  //id
		sb.append("		opt.name = 'id';  ");
		sb.append("		opt.value = param.id;  ");    
		sb.append("		temp_form.appendChild(opt);  ");  
		sb.append("		 ");
		sb.append("		opt = document.createElement('textarea');	");  //_id
		sb.append("		opt.name = '_id';  ");
		sb.append("		opt.value = param._id;  ");    
		sb.append("		temp_form.appendChild(opt);  ");
		sb.append(" 	document.body.appendChild(temp_form);   ");   
		sb.append(" 	temp_form.submit();  ");
		sb.append(" } ");
		
		sb.append(" function viewPreform2(){ ");
		sb.append(" 	viewPreform(); ");
		sb.append(" } ");
		
		//控件超过字数限制 提示用户
		sb.append(" function changeEvent(obj,str){ ");
	/*	sb.append("			var len = parseInt($(obj).attr('size'));	");
		sb.append("			var value = $(obj).val();");
		sb.append("			var size = value.length;");
		sb.append("			if(size > len){");
		sb.append("				$(obj).val(value.substr(0,len));");
		sb.append("				layer.alert('内容超出字数限制!');");
		sb.append("			}");*/
		sb.append(" } ");
		
		//参建方input控件改变事件(当文本框改变时,下拉框的选中状态被清空)
		sb.append(" function ptcpInputChange(obj){ ");
		sb.append("			var id = $(obj).attr('id');	");
		sb.append("			var selectObj = $('#' + id + 's'); ");
		sb.append("			selectObj[0].selectedIndex = -1;	");
		sb.append(" } ");
		
		//参建方控件
		sb.append(" function selectChangeEvent(sObj){ ");
		sb.append("			var inputObj = getPtcpInput(sObj); ");
		sb.append("			var text = $(sObj).find('option:selected').text(); ");
		sb.append("			inputObj.val(text); ");
		sb.append(" } ");
		
		sb.append(" function getPtcpInput(sObj){ ");
		sb.append("			var id = $(sObj).attr('input');	");
		sb.append("			var inputObj = $('#' + id); ");
		sb.append("			return inputObj; ");
		sb.append(" } ");
		
		//控件着色
		sb.append(" function colourOnWidge(obj){ ");
		sb.append("		$(\"input[type'='text'][ondblclick]\").each(function(){		");
		sb.append("			var value = $(this).attr('style');");
		sb.append(String.format("			$(this).attr('style',value + 'background-color:%s;');",WIDGE_BACKGROUND_COLOR));
		sb.append("			");
		sb.append("		});	");
		sb.append(" } ");
		
		//按钮触发清除随机数事件
		sb.append(" function cleanRandom(){ ");
		sb.append("		var rdRng = $(\"input[name='rdRng']:checked\").val(); ");
		sb.append("		if(rdRng == 0){	");//整个表单
		sb.append("			$('table div[widgeType=rnd][contenteditable=true]').each(function(){	");
		sb.append("  			$(this).html('');	");
		sb.append("			 	triggerChange($(this)); ");//触发改变事件
		sb.append(" 		}); ");
		sb.append("  	}else if(rdRng == 1){	");
		sb.append("			if(curInput == null){ ");
		sb.append("				return;");
		sb.append("			}");
		sb.append("			var dInx = $(curInput).parent().parent().parent().index()+1; ");
		sb.append("			$('table tr:nth-child('+ dInx +') div[widgeType=rnd][contenteditable=true]').each(function(){	");
		sb.append("  			$(this).html('');	");
		sb.append("			 	triggerChange($(this)); ");//触发改变事件
		sb.append(" 		}); ");
		sb.append(" 	}else if(rdRng == 2){	");
		sb.append("			if(curInput == null){ ");
		sb.append("				return;");
		sb.append("			}");
		sb.append("			var dInx = $(curInput).parent().parent().index()+1; ");
		sb.append("			var trInx = $(curInput).parent().parent().parent().index()+1; ");
		sb.append("			var tdObj = $(curInput).parent().parent(); ");
		sb.append("			var trSize = $('#preformTable tr').length;	");
		sb.append("			var offsetIndex = -1; ");
		sb.append("			$('table tr:nth-child('+ trInx +') div[widgeType=rnd][contenteditable=true]').each(function(index){	");
		sb.append("				if(this == curInput){ ");
		sb.append("					offsetIndex = index;	");
		sb.append("			 	}	");
		sb.append("			 });	");
		sb.append("			for(var i = 0; i < trSize; i++ ){	");
		sb.append("			 	$('table tr:nth-child('+ i +') td div[widgeType=rnd][contenteditable=true]').each(function(index){	");
		sb.append("			 		if(index == offsetIndex){	");
		sb.append("						$(this).html('');");
		sb.append("			 			triggerChange($(this)); ");//触发改变事件
		sb.append("			 		}	");
		sb.append("				 });");
		sb.append("			}");
		sb.append("  	}	");
		sb.append("		layer.close(divId);");
		sb.append(" } ");
		
		//随机数改变事件
		sb.append(" function rdFocusEvent(obj,event){ ");
		sb.append(" 	event = window.event?window.event:event;	");
		sb.append(" 	if(isIE()){	");
/*		sb.append(" 	if(window.event && window.event.srcElement!=null && window.event.srcElement.className=='new-btn-yellow'){	");
		sb.append(" 		return;");//解决ie下设置背景色引起的循环调用
		sb.append(" 	}	");*/
		sb.append(" 	if(window.event){	");
		sb.append(" 		if( window.event.propertyName=='style.backgroundImage' || window.event.propertyName=='style.backgroundRepeat' 	");
		sb.append(" 			|| window.event.propertyName=='style.backgroundPositionX' || window.event.propertyName=='style.backgroundPositionY'  ");
		sb.append(" 			|| window.event.propertyName=='style.backgroundPosition' ){	");
		sb.append(" 			return;");//解决ie下设置背景色引起的循环调用
		sb.append(" 		}	");
		sb.append(" 	}	");
		sb.append(" 	}	");
		sb.append(" 	if(!(getEventTargetId(event)=='creRndBtn' || getEventTargetId(event)=='cleanRndBtn') ){	");  //trigger不能设置当前控件
		sb.append(" 		curInput = obj;	");//设置当前控件
		sb.append(" 	}	");
		sb.append(" 	var value = getDivValue($(obj));	");
		sb.append(" 	if(value==''){	");
		sb.append("  		resetFailFlag(obj);");
		sb.append("  		return;");
		sb.append("  	}");
		sb.append("  	if(isNaN(value)){	");
		sb.append("  		layer.alert('必须是数字!');	");
		sb.append("  		return;	");
		sb.append(" 	}");
		sb.append("		var curObj = $(obj);	");
		sb.append("		var rndtype = curObj.attr('rndtype');	");
		sb.append("		var minMaxArray = new Array();	");
		sb.append(" 	if(getRdRange(curObj,minMaxArray,false)){ ");
		sb.append("  		var minMax = minMaxArray.pop();	");
		sb.append(" 		var min = parseFloat(minMax.min);	");
		sb.append("  		var max = parseFloat(minMax.max);	");
		sb.append("  		value = parseFloat(value);	");
		sb.append("  		if(value >= min && value <= max){	");
		sb.append("  			resetFailFlag(obj);");
		sb.append(" 		}else{	");
		sb.append("  			setFailFlag(obj);");
		sb.append("  		}	");
		sb.append("  	}	");
		sb.append(" } ");
		
		//设置不合格符号
		sb.append("  function setFailFlag(obj){	");
		sb.append("  	$(obj).css({'background-image':'url(../images/triangle.png)','background-repeat': 'no-repeat','background-position':'center'});	");
		sb.append("  	setBackgroundImage(obj);	");
		sb.append("  }	");
		
		//重置不合格符号
		sb.append("  function resetFailFlag(obj){	");
		sb.append("  	$(obj).css({'background-image':'','background-repeat': '','background-position':''});	");
		sb.append("  	setBackgroundImage(obj);	");
		sb.append("  }	");
		
    	//设置背景图片 {'background-image':'url(../images/triangle.png)','background-repeat': 'no-repeat','background-position':'center'}
	    sb.append(" function setBackgroundImage(obj){	");
    	sb.append("		var tagId = $(obj).attr('id');");
    	sb.append("		if(intSty[tagId] == undefined){ intSty[tagId]={}; }; ");
    	sb.append("		var attr = 'background-image'; ");
    	sb.append(" 	if($(obj).css(attr)!='none'){	");
    	sb.append("			intSty[tagId][attr] ='url(../images/triangle.png)'; ");
    	sb.append(" 	}else{	");
    	sb.append("			delete intSty[tagId][attr]; ");
    	sb.append(" 	}	");
    	sb.append("		attr = 'background-repeat'; ");
    	sb.append(" 	if($(obj).css(attr)=='no-repeat'){	");
    	sb.append("			intSty[tagId][attr] ='no-repeat'; ");
    	sb.append(" 	}else{	");
    	sb.append("			delete intSty[tagId][attr]; ");
    	sb.append(" 	}	");
    	sb.append("		attr = 'background-position'; ");
    	sb.append(" 	if($(obj).css(attr)=='center' || $(obj).css(attr)=='50% 50%' ){	");
    	sb.append("			intSty[tagId][attr] ='center'; ");
    	sb.append(" 	}else{	");
    	sb.append("			delete intSty[tagId][attr]; ");
    	sb.append(" 	}	");
    	sb.append("		if(isNullObject(intSty[tagId])){  ");
    	sb.append("		  delete intSty[tagId];");
    	sb.append("		}  ");
    	sb.append(" }	");
		
		//按钮触发生成随机数事件
		sb.append(" function setRandom(){ ");
		sb.append(" 	if(!validRdParam()){ ");
		sb.append(" 		return;	");
		sb.append(" 	} ");
		sb.append("		var rdRng = $(\"input[name='rdRng']:checked\").val(); ");
		sb.append("		var minMaxArray = new Array();	");
		sb.append("		var rowObj = {};");
		//生成随机数时,必须以每一行生成,因为不同行随机数的范围是不一样的,打扰顺序会导致错误
		//1.获得行号
		//2.遍历每行,生成合格和不合格的随机数,然后打乱顺序.
		//3.将值设置到input上
		sb.append("		if(rdRng == 0){	");//整个表单
		sb.append("			$(\"div[widgeType='rnd']\").each(function(){	");
		sb.append("				var dInx = $(this).parent().parent().parent().index()+1; ");
		sb.append("				if(rowObj[dInx] == undefined){	");
		sb.append("					rowObj[dInx] = 1;");
		sb.append("				}	");
		sb.append("			});	");
		sb.append("			for(var trIndex in rowObj){	");
		sb.append("				var flag = false;");
		sb.append("				$('table tr:nth-child('+ trIndex +') div[widgeType=rnd][contenteditable=true]').each(function(){");
		sb.append("					if(!getRdRange($(this),minMaxArray,true)){");
		sb.append("						flag = true;");
		sb.append("						return false;");//跳出循环
		sb.append("					}");
		sb.append("				});");
		sb.append("				if(flag){");
		sb.append("					continue;");
		sb.append("				}");
		sb.append("				var resultArray = getRandomNum(minMaxArray); ");//获得随机数
		sb.append("				minMaxArray.length = 0;	");
		sb.append("				$('table tr:nth-child('+ trIndex +') div[widgeType=rnd][contenteditable=true]').each(function(index){");
		sb.append("					if( !isNaN(resultArray[index])){");
		sb.append("						$(this).html(resultArray[index]);");
		sb.append("			 			triggerChange($(this)); ");//触发改变事件
		sb.append("					}else{");
		sb.append("						//$(this).html('');");
		sb.append("					}");
		sb.append("				});");
		sb.append("			}	");
		sb.append("		}else if(rdRng == 1){	");//整行
		sb.append("			if(curInput == null){ ");
		sb.append("				layer.alert('请选择一项!');");
		sb.append("			}");
		sb.append("			var dInx = $(curInput).parent().parent().parent().index()+1; ");
		sb.append("			var flag = false;");
		sb.append("			$('table tr:nth-child('+ dInx +') div[widgeType=rnd][contenteditable=true]').each(function(index){	");
		sb.append("				if(!getRdRange($(this),minMaxArray,false)){	");
		sb.append("					flag = true;");
		sb.append("					return false;	");//当没有选择范围时,//跳出循环
		sb.append("				}");
		sb.append("			});");
		sb.append("			if(flag){");
		sb.append("				layer.close(divId);");
		sb.append("				return;");
		sb.append("			}");
		sb.append("			var resultArray = getRandomNum(minMaxArray); ");//获得随机数
		sb.append("			$('table tr:nth-child('+ dInx +') div[widgeType=rnd][contenteditable=true]').each(function(index){	");
		sb.append("				if( !isNaN(resultArray[index])){	");
		sb.append("					$(this).html(resultArray[index]);	");
		sb.append("			 		triggerChange($(this)); ");//触发改变事件
		sb.append("				}else{");
		sb.append("					//$(this).html('');");
		sb.append("				}");
		sb.append("			});");
		sb.append("		}else if(rdRng == 2){	");//整列
		sb.append("			if(curInput == null){ ");
		sb.append("				layer.alert('请选择一项!');");
		sb.append("			}");
		sb.append("			var dInx = $(curInput).parent().parent().index()+1; ");
		sb.append("			var trInx = $(curInput).parent().parent().parent().index()+1; ");
		sb.append("			var tdObj = $(curInput).parent().parent(); ");
		sb.append("			var offsetIndex = -1; ");
		sb.append("			$('table tr:nth-child('+ trInx +') div[widgeType=rnd][contenteditable=true]').each(function(index){	");
		sb.append("				if(this == curInput){ ");
		sb.append("					offsetIndex = index;	");
		sb.append("			 	}	");
		sb.append("			 });	");
		sb.append("			var flag = false;");
		sb.append("			var trSize = $('#preformTable tr').length;	");
		sb.append("			for(var i = 0; i < trSize; i++ ){	");
		sb.append("			 	$('table tr:nth-child('+ i +') td div[widgeType=rnd][contenteditable=true]').each(function(index){	");
		sb.append("			 		if(index == offsetIndex){	");
		sb.append("						if(!getRdRange($(this),minMaxArray,false)){	");
		sb.append("							flag = true;");
		sb.append("							return false;	");
		sb.append("						}	");
		sb.append("			 		}	");
		sb.append("				 });	");
		sb.append("			}	");
		sb.append("			if(flag){");
		sb.append("				layer.close(divId);");
		sb.append("				return;");
		sb.append("			}");
		sb.append("			var resultArray = getRandomNum(minMaxArray); ");//整列获得随机数
		sb.append("			var temIndex = 0;	");
		sb.append("			for(var i = 0; i < trSize; i++ ){	");
		sb.append("			 	$('table tr:nth-child('+ i +') td div[widgeType=rnd][contenteditable=true]').each(function(index){	");
		sb.append("			 		if(index == offsetIndex){	");
		sb.append("						if(!isNaN(resultArray[index])){	");
		sb.append("							$(this).html(resultArray[temIndex]);	");
		sb.append("			 				triggerChange($(this)); ");//触发改变事件
		sb.append("			 				temIndex++; ");
		sb.append("						}else{");
		sb.append("							//$(this).html('');");
		sb.append("						}	");
		sb.append("			 		}	");
		sb.append("				 });");
		sb.append("			}");
		sb.append("			 ");
		sb.append("		}");
		sb.append("		layer.close(divId);");
		sb.append(" } ");
		
		//取得随机数的范围
		sb.append(" function getRdRange(curObj,minMaxArray,isPrompt){ ");
		sb.append("		var rndtype = curObj.attr('rndtype');	");
		//随机数列类型
		sb.append("		if(rndtype == 'col'){	");
		sb.append("			var rndSrcMap = curObj.data('rndSrcMap'); ");
		sb.append("			if(rndSrcMap == null){	");
		sb.append("				var src = curObj.attr('src');	");
		sb.append("				var srcArray = src.split(',');	");
		sb.append("				var data = {}; ");
		sb.append("				for(var i=0; i < srcArray.length; i++){ ");
		sb.append("					data[map[srcArray[i]]]= 1; ");
		sb.append("				}");
		sb.append("				curObj.data('rndSrcMap',data); ");
		sb.append("				rndSrcMap = data;");
		sb.append("			}	");
		sb.append("			var rndRngMap = curObj.data('rndRngMap'); ");
		sb.append("			if(rndRngMap == null){	");
		sb.append("				var rng = curObj.attr('rng');	");
		sb.append("				var rngArray = rng.split(',');	");
		sb.append("				var data = {}; ");
		sb.append("				for(var i=0; i < rngArray.length; i++){ ");
		sb.append("					data[map[rngArray[i]]]= 1; ");
		sb.append("				}");
		sb.append("				curObj.data('rndRngMap',data); ");
		sb.append("				rndRngMap = data;	");
		sb.append("			}	");
		sb.append("			var tagId = getChecked(rndSrcMap);	");
		sb.append("			if(tagId == undefined){	");
		sb.append("				if(!isPrompt){	");
		sb.append("					layer.alert('没有选择标准!');	");
		sb.append("				};	");
		sb.append("				return false;	");
		sb.append("			}	");
		sb.append("			");
		sb.append("			var chkRngMap = $('#'+tagId).data('chkRngMap'); ");
		sb.append("			if(chkRngMap == null){");
		sb.append("				var rng = $('#'+tagId).attr('rng');	");
		sb.append("				var rngArray = rng.split(',');	");
		sb.append("				var data = {}; ");
		sb.append("				for(var i=0; i < rngArray.length; i++){ ");
		sb.append("					data[map[rngArray[i]]] = 1; ");
		sb.append("				}");
		sb.append("				$('#'+tagId).data('chkRngMap',data); ");
		sb.append("				chkRngMap = data;	");
		sb.append("				");
		sb.append("			}");
		sb.append("			for(var v in chkRngMap){");
		sb.append("				if(rndRngMap[v] == 1){");
		sb.append("					var mm = {}; ");
		sb.append("					var min = $('#'+v).attr('min'); ");
		sb.append("					var max = $('#'+v).attr('max');");
		sb.append("					if(min != undefined || max != undefined){	");
		sb.append("						mm.min = min;	");
		sb.append("						mm.max = max;	");
		sb.append("						if(curObj.attr('prec')!=''){	");
		sb.append("							mm.prec = curObj.attr('prec'); ");
		sb.append("						}	");
		sb.append("						if(curObj.attr('main')!=''){	");
		sb.append("							mm.main = curObj.attr('main'); ");
		sb.append("						}	");
		sb.append("						if(curObj.attr('uqrange')!=''){	");
		sb.append("							mm.uqrange = curObj.attr('uqrange'); ");
		sb.append("						}	");
		sb.append("						minMaxArray.push(mm); ");
		sb.append("					}	");
		sb.append("					return true;	");//返回true表示成功
		sb.append("				}");
		sb.append("			}");
		//随机数固定类型
		sb.append("		}else if(rndtype == 'fix'){	");
		sb.append(" 		var mm = {};	");
		sb.append(" 		var min =  curObj.attr('min');	");
		sb.append(" 		var max =  curObj.attr('max');	");
		sb.append(" 		mm.min = min;	");
		sb.append(" 		mm.max = max;	");
		sb.append("			if(curObj.attr('prec')!=''){	");
		sb.append("				mm.prec = curObj.attr('prec'); ");
		sb.append("			}	");
		sb.append("			if(curObj.attr('main')!=''){	");
		sb.append("				mm.main = curObj.attr('main'); ");
		sb.append("			}	");
		sb.append("			if(curObj.attr('uqrange')!=''){	");
		sb.append("				mm.uqrange = curObj.attr('uqrange'); ");
		sb.append("			}	");
		sb.append(" 		minMaxArray.push(mm);	");
		sb.append("			return true;");//返回true表示成功
		//随机数单选类型
		sb.append(" 	}else if(rndtype == 'chk'){	");
		sb.append("			var src = curObj.attr('src');	");
		sb.append("			var srcArray = src.split(',');	");
		sb.append("			var rndSrcMap = {};	");
		sb.append("			for(var i = 0; i < srcArray.length; i ++){	");
		sb.append("				rndSrcMap[map[srcArray[i]]] = 1;");
		sb.append("			}	");
		sb.append("			var tagId = getChecked(rndSrcMap);	");
		sb.append("			if(tagId == undefined){	");
		sb.append("				if(!isPrompt){	");
		sb.append("					layer.alert('没有选择标准!');	");
		sb.append("				}	");
		sb.append("				return false;	");
		sb.append("			}	");
		sb.append(" 		var tagObj =  $('#'+tagId); ");
		sb.append(" 		var mm = {};	");
		sb.append(" 		var min =  tagObj.attr('min');	");
		sb.append(" 		var max =  tagObj.attr('max');	");
		sb.append(" 		mm.min = min;	");
		sb.append(" 		mm.max = max;	");
		sb.append("			if(curObj.attr('prec')!=''){	");
		sb.append("				mm.prec = curObj.attr('prec'); ");
		sb.append("			}	");
		sb.append("			if(curObj.attr('main')!=''){	");
		sb.append("				mm.main = curObj.attr('main'); ");
		sb.append("			}	");
		sb.append("			if(curObj.attr('uqrange')!=''){	");
		sb.append("				mm.uqrange = curObj.attr('uqrange'); ");
		sb.append("			}	");
		sb.append(" 		minMaxArray.push(mm);	");
		sb.append("			return true;	");//返回true表示成功
		//随机数计算类型
 		sb.append(" 	}else if(rndtype == 'cal'){	");
 		sb.append("			var src = curObj.attr('src');	");
 		sb.append(" 		var tagId = map[src];	");
 		sb.append(" 		if(!tagId){	");
 		sb.append(" 			return false;");
 		sb.append(" 		}	");
 		sb.append(" 		var tagObj = $('#'+tagId);	");
 		sb.append(" 		if(tagObj == undefined || tagObj.val()==''){	");
 		sb.append("				if(!isPrompt){	");
 		sb.append("					layer.alert('计算变量不能为空!');	");
 		sb.append("				}	");
		sb.append("				return false;	");
 		sb.append(" 		}");
 		sb.append(" 		var exp = new Expression('');	");
 		sb.append(" 		var varVal = parseFloat(tagObj.val()); 	");
 		sb.append(" 		exp.AddVariable('v', varVal);	");//设置变量
 		sb.append(" 		var strExp = tagObj.attr('cal');	");
 		sb.append(" 		exp.Expression(strExp);	");//设置表达式
 		sb.append(" 		var result = exp.Evaluate();	");//得到结果
 		sb.append(" 		var min = tagObj.attr('min');	");
 		sb.append(" 		var max = tagObj.attr('max');	");
 		sb.append(" 			");
 		//求最大值
 		sb.append(" 		var array = min.split(',');");
 		sb.append(" 		for(var i=0; i < array.length; i++){		");
 		sb.append(" 			if(array[i] == 'var'){		");
 		sb.append(" 				array[i] = result; ");
 		sb.append(" 			}else if(array[i].indexOf('var') != -1){	");
 		sb.append(" 				exp = new Expression(''); ");
 		sb.append(" 				exp.Expression(array[i]); ");
 		sb.append(" 				exp.AddVariable('var', result);	");
 		sb.append(" 				array[i] = exp.Evaluate();");
 		sb.append(" 			}	");
 		sb.append(" 		}		");
 		sb.append(" 		min = Math.max.apply(Math,array);  ");//多个最小值那么取其中最大值
 		//求最小值
 		sb.append(" 		array = max.split(',');");
 		sb.append(" 		for(var i=0; i < array.length; i++){		");
 		sb.append(" 			if(array[i] == 'var'){		");
 		sb.append(" 				array[i] = result; ");
 		sb.append(" 			}else if(array[i].indexOf('var') != -1){	");
 		sb.append(" 				exp = new Expression(''); ");
 		sb.append(" 				exp.Expression(array[i]); ");
 		sb.append(" 				exp.AddVariable('var', result);	");
 		sb.append(" 				array[i] = exp.Evaluate();");
 		sb.append(" 			}	");
 		sb.append(" 		}		");
 		sb.append(" 		max = Math.min.apply(Math,array);  ");//多个最大值那么取其中最小值
 		sb.append(" 		var mm = {};	");
 		sb.append(" 		mm.min = min;	");
		sb.append(" 		mm.max = max;	");
		sb.append("			if(curObj.attr('prec')!=''){	");
		sb.append("				mm.prec = curObj.attr('prec'); ");
		sb.append("			}	");
		sb.append("			if(curObj.attr('main')!=''){	");
		sb.append("				mm.main = curObj.attr('main'); ");
		sb.append("			}	");
		sb.append("			if(curObj.attr('uqrange')!=''){	");
		sb.append("				mm.uqrange = curObj.attr('uqrange'); ");
		sb.append("			}	");
		sb.append(" 		minMaxArray.push(mm);	");
		sb.append("			return true;	");//返回true表示成功
 		sb.append(" 	}		");
		sb.append(" }	");
		
		//随机数参数验证
		sb.append(" function validRdParam(){ ");
		sb.append("		var qrVal = document.getElementById('qr').value;");
		sb.append("		var ddVal = document.getElementById('dd').value;");
		sb.append("		if(qrVal==''){");
		sb.append("			layer.tips('不能为空!', document.getElementById('qr'));");
		sb.append("			return false;	");
		sb.append("		}");
		sb.append("		if(ddVal==''){");
		sb.append("			layer.tips('不能为空!', document.getElementById('dd'));");
		sb.append("			return false;	");
		sb.append("		}");
		sb.append("		if(isNaN(qrVal)){");
		sb.append("			layer.tips('必须为数字!', document.getElementById('qr'));");
		sb.append("			return false;	");
		sb.append("		}");
		sb.append("		if(isNaN(ddVal)){");
		sb.append("			layer.tips('必须为数字!', document.getElementById('dd'));");
		sb.append("			return false;	");
		sb.append("		}");
		sb.append("		return true;	");
		sb.append("	}	");
		
		//获得被选中的单选框
		sb.append(" function getChecked(rndSrcMap){ ");
		sb.append("		for(var tagId in rndSrcMap){	");
		sb.append("			if($('#'+tagId).is(':checked')){");
		sb.append("				return tagId;");
		sb.append("			}");
		sb.append("		}");
		sb.append(" } ");
		
		sb.append(" 	function fixDecLen(val){	");
		sb.append(" 		if(val.indexOf('.') != -1){	");
		sb.append(" 			var l = val.split('.')[1].length;	");
		sb.append(" 			return l;	");
		sb.append(" 		}	");
		sb.append(" 		return 0;	");
		sb.append(" 	}	");
		
		sb.append(" 	function loadTextData(){	");//企业管理不会执行到下面
		sb.append(" 		if(!param.orgId){  ");
		sb.append(" 			return;  ");
		sb.append(" 		}  ");
		sb.append(String.format(" var src = '%s/setupProject/setup4Jsonp?orgId=' + param.orgId; ",orgPath));
		sb.append(" 		var JSONP=document.createElement('script'); ");
		sb.append(" 		JSONP.type='text/javascript'; ");
		sb.append(" 		JSONP.src=src;  ");
		sb.append(" 		document.getElementsByTagName('head')[0].appendChild(JSONP);	");
		sb.append(" 	}	");
		
		sb.append(" 	function setTextData(data){	");
		sb.append(" 		data = decodeURIComponent(decodeURIComponent(data));	");
		sb.append(" 		data = JSON.parse(data);	");
		sb.append(" 		for(var v in data){	");
		sb.append(" 			if(v == 'text3'){	");//单位工程
		sb.append(" 			}else if(data[v]){	");
		sb.append(" 				$('div[widgetype=' + v + ']').html(data[v]);");
		sb.append(" 			}	");
		sb.append(" 		}	");
		sb.append(" 	}	");
		
		sb.append(" 	function loadSelData(){	");
		sb.append(" 		if($('div[type=autoSelect]').size() == 0){");
		sb.append(" 			return;	");
		sb.append(" 		}	");
		sb.append(" 		var map = {};	");
		sb.append(" 		$.each($('div[type=autoSelect]'),function(){	");
		sb.append(" 			var widgeType = $(this).attr('widgeType'); ");
		sb.append(" 			if(map[widgeType] != undefined){	");
		sb.append(" 				return;");
		sb.append(" 			}	");
		sb.append(" 			map[widgeType]=1;	");
		sb.append(String.format(" var src = '%s/commentLibrary/view4Jsonp?id=' + widgeType;",sysPath));     
		sb.append(" 			var JSONP=document.createElement('script'); ");
		sb.append(" 			JSONP.type='text/javascript'; ");
		sb.append(" 			JSONP.src=src;  ");
		sb.append(" 			document.getElementsByTagName('head')[0].appendChild(JSONP);	");
		sb.append(" 		});	");
		sb.append(" 	}	");
		
	    //初始化分部列表数据
		sb.append(" 	function loadBrList4Jsonp(){		  ");
		sb.append(" 		if(brArray.length && brArray.length > 0 ){	");
		sb.append(" 			return; 	");//使用缓存数据
		sb.append(" 		}	");
		sb.append(String.format(" var src = '%s/sys/subsegment/list4Jsonp'; ",sysPath));
		sb.append(" 		var JSONP=document.createElement('script'); ");
		sb.append(" 		JSONP.type='text/javascript'; ");
		sb.append(" 		JSONP.src=src;  ");
		sb.append(" 		document.getElementsByTagName('head')[0].appendChild(JSONP);	");
		sb.append(" 	}		  ");
		//设置分部列表数据
		sb.append(" 	function setSecListData(data){	");
		sb.append(" 		data = decodeURIComponent(decodeURIComponent(data));	");
		sb.append(" 		data = JSON.parse(data);	");
		sb.append(" 		brArray = data;	");
		sb.append(" 	}	");
		
	    //初始化分部分项数据
		sb.append(" 	function loadBrData4Jsonp(id){		  ");
		sb.append(" 		if(brData[id]){	");
		sb.append(" 			setSecTreeData(); 	");//使用缓存数据
		sb.append(" 			return;	");
		sb.append(" 		}	");
		sb.append(String.format(" var src = '%s/sys/subsegment/getTreeData4Jsonp?id=' + id; ",sysPath));
		sb.append(" 		var JSONP=document.createElement('script'); ");
		sb.append(" 		JSONP.type='text/javascript'; ");
		sb.append(" 		JSONP.src=src;  ");
		sb.append(" 		document.getElementsByTagName('head')[0].appendChild(JSONP);	");
		sb.append(" 	}		  ");
		//设置分部分项数据
		sb.append(" 	function setSecTreeData(data){	");
		sb.append(" 		var gbId = $('#gbSlt').val();;	");
		sb.append(" 		if(data){	");
		sb.append(" 			data = decodeURIComponent(decodeURIComponent(data));	");
		sb.append(" 			data = JSON.parse(data);	");
		sb.append(" 			brData[gbId] = data[gbId];");
		sb.append(" 		}	");
		sb.append(" 		var brSlt = $('#brSlt');	");
		sb.append(" 		var firstBr = null;	");
		sb.append(" 		for(var br in brData[gbId]){	");
		sb.append(" 			if(!firstBr){");
		sb.append(" 				firstBr = br; ");
		sb.append(" 			}");
		sb.append(" 			brSlt.append(\"<option value='\" + br + \"'>\" + br + \"</option>\");	");
		sb.append(" 		}	");
		sb.append(" 		var brItemSlt = $('#brItemSlt');	");
		sb.append(" 		if(brData[gbId][firstBr]){	");
		sb.append(" 			for(var i = 0; i < brData[gbId][firstBr].length; i++){	");
		sb.append(" 				brItemSlt.append(\"<option value='\" + brData[gbId][firstBr][i] + \"'>\" + brData[gbId][firstBr][i] + \"</option>\");	");
		sb.append(" 			}");		
		sb.append(" 		}	");
		sb.append(" 	}	");
		
/*		//加载特殊字符
		sb.append(" 	function loadSpeChars(){	");
		sb.append(String.format(" var src = '%s/logsymbol/findAll4Jsonp'; ",sysPath));
		sb.append(" 		var JSONP=document.createElement('script'); ");
		sb.append(" 		JSONP.type='text/javascript'; ");
		sb.append(" 		JSONP.src=src;  ");
		sb.append(" 		document.getElementsByTagName('head')[0].appendChild(JSONP);	");
		sb.append(" 	}	");
		
		//初始化特殊字符
		sb.append(" 	var speArray = {};	");		
		sb.append(" 	function setSpeChars(data){	");
		sb.append(" 		speArray = data.split(',');	");
		sb.append(" 	}	");*/		
		
		sb.append(" 	function loadCopyData(){	");
		sb.append("  		var pattern = /A[0-9]+/;	");
		sb.append(" 		for(var name in param){	");
		sb.append(" 			if(pattern.test(name)){	");
		sb.append(" 				if($('#'+name).is('input')){	");
		sb.append(" 					$('#'+name).val(param[name]);");
		sb.append(" 				}else if($('#'+name).is('span'))");
		sb.append(" 					$('#'+name).html(param[name]);");
		sb.append(" 				}	");
		sb.append(" 		}	");
		sb.append(" 	}	");
		
		//
		sb.append(" 	function resizeToolBar(obj){	");
		sb.append(" 		$('#toolbarDiv1').show();	");
		sb.append(" 	}	");
		
		//获得指定对象与左屏幕距离
		sb.append(" 	function getAbsLeft(obj){	");
		sb.append(" 		var l=obj.offsetLeft;");
		sb.append(" 		while(obj.offsetParent != null){");
		sb.append(" 			obj = obj.offsetParent;");
		sb.append(" 			l += obj.offsetLeft;");
		sb.append(" 		}");
		sb.append(" 		return l; ");
		sb.append(" 	}	");
		
		sb.append(" 	function radioOnlyEvent(obj){	");
		sb.append(" 		var alias = $(obj).attr('alias');	");
		sb.append(" 		$('input[type=checkbox][alias=' + alias +']').each(function(){	");
		sb.append(" 			if(this!=obj){");
		sb.append(" 				$(this).prop('checked',false);	");
		sb.append(" 				cancelRndChkEvent(this);	");
		sb.append(" 			}	");
		sb.append(" 		});");
		sb.append(" 		cancelRndChkEvent(obj);	");
		sb.append(" 	}	");
		
		sb.append(" </SCRIPT> ");
		return sb.toString();
	}

	private String picHtml(ExcelTableBo table) throws Exception {
		StringBuffer sb = new StringBuffer("");
		List<ExcelPictureBo> listPic = table.getPicList();
		for(ExcelPictureBo picBo : listPic ){
			int top = picBo.getTop(picBo.getRow1()) + 10;//10px是html中顶端的空白距离
			int left = picBo.getLeft(picBo.getCol1()) + 66;//66px是html中左边的空白距离
			double width = picBo.getWidth(picBo.getCol1(), picBo.getCol2());
			double height = picBo.getHeight(picBo.getRow1(), picBo.getRow2());
			
			String id = uploadPic(picBo);
			picBo.setId(id);
			String imgStr = String.format("<img src='http://f.10333.com/preform/%s' style='position:absolute;top:%spx;left:%spx;width:%spx;height:%spx'/>",id,top,left,width,height);
			sb.append(imgStr);
		}
		return sb.toString();
	}

	private StringBuffer headerHtmlEnd() {
		StringBuffer sb = new StringBuffer("");
		sb.append("</head>");
		sb.append("<body>");// 进入页面就刷新下
		return sb;
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

	//这个没有被使用
	private StringBuffer excelToHtml(Workbook workbook, int sheetindex)
			throws Exception {

		Sheet sheet = workbook.getSheetAt(sheetindex);
		int firstRow = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
		
		ExcelTableBo table = new ExcelTableBo();
		
		table.setFirstRow(firstRow);
		table.setLastRow(lastRowNum);
		
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
		
		//white-space:nowrap;  样式会影响div中文字不换行
		//sb.append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style='border-collapse:collapse;white-space:nowrap;'>");
		
		sb.append("<table id=\"preformTable\" border=\"0\" cellspacing=\"0\" align='center'  cellpadding=\"0\" style='border-collapse:collapse;white-space:nowrap;margin-top:100px'>");
		sb.append("<input id='id' name='id' type='hidden'>");
		sb.append("<input id='_id' name='_id' type='hidden'>");
		sb.append("<input id='orgId' name='orgId' type='hidden'>");
		sb.append("<input id='draft' name='draft' type='hidden'>");//draft = 1为草稿
		String prefixPath = PreformConfig.getInstance().getConfig(PreformConfig.DS_PATH);//http://dsframe.10333.com
		sb.append(String.format("<input id='prefixPath' name='prefixPath' type='hidden' value='%s'>", prefixPath));
		//sb.append("<input id='noCheckSn' name='noCheckSn' type='hidden'>");//noCheckSn = 1 时不检查sn
		for (int rowNum = sheet.getFirstRowNum(); rowNum <= lastRowNum; rowNum++) {
			ExcelTableTrBo tr = table.getTrMap().get(rowNum);
			if (tr != null) {
				String trHtml = row4Tr2Html(table, tr);
				sb.append(trHtml);
			}
		}
		
		sb.append("<tr align='center'><td><input type='submit' onclick='saveData();'/></td></tr>");
		
		sb.append("</table>");
		sb.append("</form>");
		
		sb.append("<div id='dlg-buttons' style='margin-bottom: 5px;margin-left: 5cm;margin-top: 20px;'>"
				+	"<input type='submit' value='保存' onclick='saveData()'/> " 
				+   "</div>");
		
		sb.append("<div id='dialog'></div>");
		return sb;
	}
	
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
			IExcelCellContent2Html html = new ExcelCellContent4String2MdfHtml();
			str = html.toHtml(table, tr, td);
			
			if(html.getRightMenuList().size() != 0){
				rightMenuTagSet.addAll(html.getRightMenuList());
			}
			/**
			 * modify by wangl 控件持久化到数据库
			 */
			if(html.getPreformElm()!=null){
				elmList.add(html.getPreformElm());
			}
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

	//获得td样式
	private String row4TdStyle2Html(ExcelTableBo table, ExcelTableTrBo tr,
			ExcelTableTdBo td, ExcelTableTdStyleBorderBo bo) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(String.format(" style= '"));
		if (!StringsUtils.isEmpty(td.getWidth()) ) {
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
			html = new ExcelCellContent4String2MdfHtml();
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

	/**
	 * 保存时会调用此处的toHtml方法
	 */
	public String toHtml(ExcelTableBo table) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(headerHtmlStart(table.getExcelTitle()));
		sb.append(headerHtmlEnd());
		sb.append(bodyHtml());
		
		//addRightMenuJS(sb);
		addToolBarJs(sb);
		addToolBar(sb);
		
		sb.append("<!--startprint1-->");
		sb.append("<form id='preformForm'>");
		//style='border-collapse:collapse;'  使用这个边框合并样式，会导致某些表单的边框变长很多,以后处理。
		sb.append("<table id=\"preformTable\" border=\"0\" cellspacing=\"0\" align='center' cellpadding=\"0\" style='border-collapse:collapse;margin-top:70px'>");
		sb.append("<input id='id' name='id' type='hidden'>");
		sb.append("<input id='_id' name='_id' type='hidden'>");
		sb.append("<input id='orgId' name='orgId' type='hidden'>");
		sb.append("<input id='preformName' name='preformName' type='hidden'>  ");
		sb.append("<input id='printDirec' name='printDirec' type='hidden'>  ");
		sb.append("<input id='note' name='note' type='hidden'>  ");
		sb.append("<input id='draft' name='draft' type='hidden'>");//draft = 1为草稿
		sb.append("<input id='snType' name='snType' type='hidden'>");//编号类型
		sb.append("<input id='paramValue' name='paramValue' type='hidden'>");//编号类型
		
		header2Html(table,sb);
		footer2Html(table,sb);
		//多页模板时,第二页不会重新解析
		if(table.getPicStyleList().size() ==0){
			List<PictureStyleBo> parsePic = parsePic(table);
			table.setPicStyleList(parsePic);
		}
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
	
	//添加工具栏
	private void addToolBar(StringBuffer sb) {
		sb.append("<div class='box-top-list'>");
		sb.append("<div class='box-top-list-ul'>");
		sb.append("<div class='toolbar-box-big-all'>");
		sb.append("<div id='toolbarDiv1'> ");
		sb.append("	<div class='toolbar-box le'>");
		sb.append("		<ul class='toolbar-box-b'>");
		sb.append("			<li id='font-familyLi' class='font-family'>");
		sb.append("				<ul>");
		sb.append("					<li style='font-family:SimSun;'          onclick=\"setFontFamily('SimSun');\">宋体&nbsp;</li>");
		sb.append("					<li style='font-family:SimHei;'          onclick=\"setFontFamily('SimHei');\">黑体&nbsp;</li>");
		sb.append("					<li style='font-family:KaiTi;'           onclick=\"setFontFamily('KaiTi');\">楷体&nbsp;</li>");
		sb.append("					<li style='font-family:YouYuan;'         onclick=\"setFontFamily('YouYuan');\">幼圆&nbsp;</li>");
		sb.append("					<li style='font-family:Arial;'           onclick=\"setFontFamily('Arial');\">Arial&nbsp;</li>");
		sb.append("					<li style='font-family:Arial Black;'     onclick=\"setFontFamily('Arial Black');\">Arial Black&nbsp;</li>");
		sb.append("					<li style='font-family:Times New Roman;' onclick=\"setFontFamily('Times New Roman');\">Times New Roman&nbsp;</li>");
		sb.append("					<li style='font-family:Verdana;'         onclick=\"setFontFamily('Verdana');\">Verdana&nbsp;</li>");
		sb.append("				</ul>");
		sb.append("			</li>");
		sb.append("			<li id='font-sizeLi' class='font-size'>");
		sb.append("				<ul>");
		sb.append("					<li style='font-size: 9px;' onclick=\"setFontSize('9px');\" >9px</li>");
		sb.append("					<li style='font-size:10px;' onclick=\"setFontSize('10px');\">10px</li>");
		sb.append("					<li style='font-size:11px;' onclick=\"setFontSize('11px');\">11px</li>");
		sb.append("					<li style='font-size:12px;' onclick=\"setFontSize('12px');\">12px</li>");
		sb.append("					<li style='font-size:15px;' onclick=\"setFontSize('15px');\">15px</li>");
		sb.append("					<li style='font-size:18px;' onclick=\"setFontSize('18px');\">18px</li>");
		sb.append("					<li style='font-size:20px;' onclick=\"setFontSize('20px');\">20px</li>");
		sb.append("					<li style='font-size:30px;' onclick=\"setFontSize('30px');\">30px</li>");
		sb.append("					");
		sb.append("				</ul>");
		sb.append("			</li>");
		sb.append("			<li id='boldLi' class='bold' title='黑体' onclick=\"setFontStyle(this,'bold');\"></li>");
		sb.append("			<li id='italicLi' class='italic' title='斜体' onclick=\"setFontStyle(this,'italic');\"></li>");
		sb.append("			<li id='underlineLi' class='underline' title='下划线' onclick=\"setFontStyle(this,'underline');\"></li>");
		//删除线功能暂时屏蔽
		//sb.append("			<li class='line-through' title='删除线' onclick=\"setFontStyle(this,'line-through');\"></li>");
		sb.append("			<li id='fontColorLi' class='fontColor' title='字体颜色'>");
		addColorBtn(sb);
		sb.append("			</li>");
		sb.append("			<li class='split'></li>");
		sb.append("			<li id='left' class='hor left' title='左对齐' onclick=\"setAlign(this,'left');\"></li>");
		sb.append("			<li id='center' class='hor lrcenter' title='水平居中'  onclick=\"setAlign(this,'center');\"></li>");
		sb.append("			<li id='right' class='hor right' title='右对齐' onclick=\"setAlign(this,'right');\"></li>");
		sb.append("			<li id='top' class='ver top' title='顶端对齐' onclick=\"setvAlign(this,'top');\"></li>");
		sb.append("			<li id='vcenter' class='ver udcenter' title='垂直居中' onclick=\"setvAlign(this,'middle');\"></li>");
		sb.append("			<li id='bottom' class='ver bottom' title='底端对齐' onclick=\"setvAlign(this,'bottom');\"></li>");
		sb.append("			<p class='link-1'></p>");
		
		sb.append("		</ul>");
		sb.append("	<div class='toolbar-fun le'> ");
		sb.append("		<ul class='toolbar-fun-le list-left-text'>");
		sb.append("			<li><input id='toolbar1' type='button' value='特殊符号' onclick='showSpeCharDlg();' class='btn-none' /></li>");
		sb.append("			<li><input id='toolbar2' type='button' value='生成参考数据' onclick='openRndDiv();' class='btn-none' /></li>");
		sb.append("			<li><input id='toolbar3' type='button' value='预警设置' onclick='openDsplCfgDlg();' class='btn-none' /></li>");
		sb.append("			<li><input id='toolbar4' type='button' value='生成曲线图' onclick='openDsplGraphDlg();' class='btn-none' /></li>");
		sb.append("			<li><input id='toolbar5' type='button' value='初始编号' onclick='openInitNoDiv();' class='btn-none' /></li>");
		sb.append("			<li><input id='toolbar6' type='button' value='道路' onclick='showRoadDlg();' class='btn-none' /></li>");
		/*sb.append("			<li><input id='toolbar3' type='button' value='汇总分部分项' onclick='' class='btn-none' /></li>");
		sb.append("			<li><input id='toolbar4' type='button' value='生成报验单' onclick='' class='btn-none' /></li>");
		sb.append("			<li>重新编号</li>");
		sb.append("			<li>汇总分部分项</li>");
		sb.append("			<li>生成报验单</li>");
		sb.append("			<li>复制</li>");*/
		sb.append("		</ul>");
		/*sb.append("		<ul class='toolbar-fun-ri'>");
		sb.append("			<li>填写表单</li>");
		sb.append("			<li>填写说明</li>");
		sb.append("			<li class='choose' >范例 ▽");
		sb.append("				<ul>");
		sb.append("					<li>范例1</li>");
		sb.append("					<li>范例2</li>");
		sb.append("				</ul>");
		sb.append("			</li>");
		sb.append("		</ul>");*/
		sb.append("	</div>");
		
		sb.append("	</div>");

		sb.append("</div>");
		sb.append("</div>");
		sb.append("</div>");
		sb.append("</div>");
	}
	
	/*
	 * 添加颜色按钮
	 */
	 private void addColorBtn(StringBuffer sb) {
		 sb.append("<ul>");
		 sb.append("<li title='#000000'><span style='background-color:#000000 ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#993300'><span style='background-color:#993300 ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#333300'><span style='background-color:#333300 ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#003300'><span style='background-color:#003300 ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#003366'><span style='background-color:#003366 ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#000080'><span style='background-color:#000080 ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#333399'><span style='background-color:#333399 ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#333333'><span style='background-color:#333333 ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#800000'><span style='background-color:#800000 ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#FF6600'><span style='background-color:#FF6600 ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#808000'><span style='background-color:#808000 ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#008000'><span style='background-color:#008000 ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#008080'><span style='background-color:#008080 ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#0000FF'><span style='background-color:#0000FF ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#666699'><span style='background-color:#666699 ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#808080'><span style='background-color:#808080 ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#FF0000'><span style='background-color:#FF0000 ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#FF9900'><span style='background-color:#FF9900 ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#99CC00'><span style='background-color:#99CC00 ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#339966'><span style='background-color:#339966 ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#33CCCC'><span style='background-color:#33CCCC ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#3366FF'><span style='background-color:#3366FF ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#800080'><span style='background-color:#800080 ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#999999'><span style='background-color:#999999 ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#FF00FF'><span style='background-color:#FF00FF ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#FFCC00'><span style='background-color:#FFCC00 ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#FFFF00'><span style='background-color:#FFFF00 ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#00FF00'><span style='background-color:#00FF00 ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#00FFFF'><span style='background-color:#00FFFF ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#00CCFF'><span style='background-color:#00CCFF ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#993366'><span style='background-color:#993366 ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#C0C0C0'><span style='background-color:#C0C0C0 ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#FF99CC'><span style='background-color:#FF99CC ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#FFCC99'><span style='background-color:#FFCC99 ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#FFFF99'><span style='background-color:#FFFF99 ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#CCFFCC'><span style='background-color:#CCFFCC ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#CCFFFF'><span style='background-color:#CCFFFF ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#99CCFF'><span style='background-color:#99CCFF ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#CC99FF'><span style='background-color:#CC99FF ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("<li title='#FFFFFF'><span style='background-color:#FFFFFF ;' onclick='setFontColor(this);'></span></li>");
		 sb.append("</ul>");
	}

	private void addToolBarJs(StringBuffer s) {
		PreformStringBuffer sb = new PreformStringBuffer(s);
		sb.append("<script type='text/javascript'> ");
		
		sb.append(" var curInput = null;");
		sb.append(" var intSty = {};");//input sty
		
		//加载工具栏样式
		sb.append(" function loadToolbar(){ ");
		//工具栏固定
		sb.append("		try{	");
		sb.append("		$(top.window).scroll(function(){	");
		sb.append("			var scrollTop = $(top.document).scrollTop();	");
		sb.append("			if($('#'+iframeId,top.document).length!=1){	");
		sb.append("				return;	");
		sb.append("			}	");
		sb.append("			var iframeToTop = $('#'+iframeId,top.document).offset().top;	");
		sb.append("			if(scrollTop>iframeToTop){	");
		sb.append("				var changeT = scrollTop - iframeToTop + 60 + 'px';	");
		sb.append("				$('.box-top-list').css('top',changeT);	");
		sb.append("			}else{	");
		sb.append("				$('.box-top-list').css('top','0px');	");
		sb.append("			}	");
		sb.append("		});	");
		sb.append("		}catch(e){	");
		sb.append("		}	");
		
		sb.append("		$('.toolbar-box-b>li').click(function(){	");
		sb.append("	 		if($(this).hasClass('active')){	");
		sb.append("	 			if($(this).hasClass('ver')||$(this).hasClass('hor')){	");
		sb.append("	 			}else{	");
		sb.append("	 				$(this).removeClass('active');	");
		sb.append("	 			}	");
		sb.append("	 			if($(this).children().length>0){	");
		sb.append("	 				$(this).children().hide();	");
		sb.append("	 			}	");
		sb.append("	 		}else{	");
		sb.append("	 			if($('.ver.active').length>0 && $(this).hasClass('ver')){	");
		sb.append("					$('.ver').removeClass('active'); ");
		sb.append("	 			}	");
		sb.append("				if($('.hor.active').length>0 && $(this).hasClass('hor')){	 ");
		sb.append("				 	$('.hor').removeClass('active');");
		sb.append("				} ");
		sb.append("				$(this).addClass('active');");
		sb.append("				if($(this).children().length>0){");
		sb.append("					$(this).children().show();");
		sb.append("				} ");
		sb.append("			} ");
		sb.append("	 	});");
		sb.append("	 	$('.toolbar-box-b>li').mouseleave(function(){");
		sb.append("	 		if($(this).children().length>0){");
		sb.append("	 			$(this).removeClass('active');");
		sb.append("	 			$(this).children().hide();");
		sb.append("	 		}");
		sb.append("	 	});");
		
		sb.append("		$('.fontColor li').click(function(){	");	
		sb.append("	 		$('.fontColor li').removeClass('active');	");	
		sb.append("			$(this).addClass('active');	");	
		sb.append("	 	});	");	
		sb.append("	 }	");	
		
		//input按钮可用
		sb.append(" function enableInpBtn(){ ");
		sb.append("  $('#left').addClass('').removeClass(''); ");	
		sb.append("  $('#center').addClass('').removeClass('');");	
		sb.append("  $('#right').addClass('').removeClass('');");	
		sb.append(" } ");	
		
		//testarea按钮可用
		sb.append(" function enableAreaBtn(){ ");
		sb.append("  $('#top').addClass('').removeClass(''); ");	
		sb.append("  $('#vcenter').addClass('').removeClass('');");	
		sb.append("  $('#bottom').addClass('').removeClass('');");	
		sb.append(" } ");	
		
		//全部按钮失效
		sb.append(" function disableAllBtn(){ ");
		sb.append("  $('#left').addClass('').removeClass(''); ");	
		sb.append("  $('#center').addClass('').removeClass('');");	
		sb.append("  $('#right').addClass('').removeClass('');");	
		sb.append("  $('#top').addClass('').removeClass(''); ");	
		sb.append("  $('#vcenter').addClass('').removeClass('');");	
		sb.append("  $('#bottom').addClass('').removeClass('');");	
		sb.append(" } ");	
		
		//获得焦点事件
		sb.append(" function toolBarFocusEvent(obj){ ");
		sb.append("		enableAllBtn();	");
		sb.append("  	if($(obj).is('input')){	");
		sb.append("			inactiveVAlignBtn();");
		sb.append("			activeAlignBtn('h',obj.style.textAlign);");
		sb.append("  	}else{	");
		sb.append("			activeAlignBtn('h',obj.style.textAlign);");
		sb.append("			activeAlignBtn('v',$(obj).css('vertical-align'));");
		sb.append("  	}	");
		sb.append("  	activeFontBtn(obj);	");
		sb.append(" 	if(obj!=null){	");
		sb.append(" 		curInput = obj;	");
		sb.append(" 	}	");
		sb.append(" } ");
		
		//失去焦点事件
		sb.append(" function toolBarBlurEvent(obj){ ");
		sb.append(" 	if(obj!=null){	");
		sb.append(" 		curInput = obj;	");
		sb.append(" 	}	");
		sb.append("		//disableAllBtn();");
		sb.append(" } ");
		
		//水平对齐方式
	    sb.append(" function setAlign(obj,value){");
	    sb.append(" 	if(curInput==null){	");
		sb.append(" 		layer.alert('请选择需要设置的输入框!');	");
		sb.append(" 		return;	");
	    sb.append(" 	}	");
	    sb.append("		curInput.style.textAlign = value;");
    	sb.append("		layer.tips('设置成功!', curInput);");
	  	sb.append("		var tagId = $(curInput).attr('id');");
    	sb.append("		if(intSty[tagId] == undefined){ intSty[tagId]={}; } ");
    	sb.append("		intSty[tagId].align= value; ");
    	sb.append(" }");
    	
	    //垂直对齐方式
	    sb.append(" function setvAlign(obj,value){");
	    sb.append("		if($(obj).attr('class').indexOf('unclick')!=-1){	");
	    sb.append(" 		return;	");
	    sb.append("		}	");
	    sb.append(" 	if(curInput==null){	");
		sb.append(" 		layer.alert('请选择需要设置的输入框!');	");
		sb.append(" 		return;	");
	    sb.append(" 	}	");
      	sb.append("		$(curInput).css('vertical-align',value);");
    	sb.append("		layer.tips('设置成功!', curInput);");
    	sb.append("		var tagId = $(curInput).attr('id');");
    	sb.append("		if(intSty[tagId] == undefined){ intSty[tagId]={}; } ");
    	sb.append("		intSty[tagId].valign= value; ");
    	sb.append(" }");
    	
    	//字体样式
    	sb.append(" function setFontStyle(obj,value){	");
	    sb.append(" 	if(curInput==null){	");
		sb.append(" 		layer.alert('请选择需要设置的输入框!');	");
		sb.append(" 		return;	");
	    sb.append(" 	}	");
    	sb.append("		var tagId = $(curInput).attr('id');");
    	sb.append("		if(intSty[tagId] == undefined){ intSty[tagId]={}; } ");//初始化
    	sb.append("		if(value=='bold'){	");
    	sb.append("			addOrRemoveFontCss(tagId,'font-weight','bold');	");
    	sb.append("		}else if(value=='italic'){	");
    	sb.append("			addOrRemoveFontCss(tagId,'font-style','italic');	");
    	sb.append("		}else if(value=='underline'){	");
    	sb.append("			addOrRemoveFontCss(tagId,'text-decoration','underline');	");
    	sb.append("		}else{	");
    	sb.append("			return;	");
    	sb.append("		}	");
     	sb.append("		layer.tips('设置成功!', curInput);");
     	sb.append(" }	");
     	
     	sb.append(" function addOrRemoveFontCss(tagId,fontAttr,value){	");
    	sb.append("		var fontCss = $('#'+tagId).css(fontAttr);	");
    	sb.append("		if(intSty[tagId] == undefined){ intSty[tagId]={}; } ");
    	sb.append("		if(fontCss==value){	");
    	sb.append("			$('#'+tagId).css(fontAttr,'');");
    	sb.append("			delete intSty[tagId][fontAttr];");
    	sb.append("		}else{	");
    	sb.append("			$('#'+tagId).css(fontAttr,value);	");
    	sb.append("			intSty[tagId][fontAttr]= value; ");
    	sb.append("		}	");
     	sb.append(" }	");
     	
     	//设置字体颜色
     	sb.append(" function setFontColor(obj){	");
	    sb.append(" 	if(curInput==null){	");
		sb.append(" 		layer.alert('请选择需要设置的输入框!');	");
		sb.append(" 		return;	");
	    sb.append(" 	}	");
     	sb.append("		var value = $(obj).css('background-color');	");
    	sb.append("		$(curInput).css('color',value);	");
     	sb.append("		layer.tips('设置成功!', curInput);");
     	sb.append("		var tagId = $(curInput).attr('id');");
     	sb.append("		if(intSty[tagId] == undefined){ intSty[tagId]={}; } ");
     	sb.append("		intSty[tagId].color= value; ");
     	sb.append(" }	");
    	
     	//设置字体大小
	    sb.append(" function setFontSize(value){");
	    sb.append(" 	if(curInput==null){	");
		sb.append(" 		layer.alert('请选择需要设置的输入框!');	");
		sb.append(" 		return;	");
	    sb.append(" 	}	");
	    sb.append("		$(curInput).css('font-size', value);");
	   // sb.append("		$(curInput).parent().parent().css('font-size', value);");
    	sb.append("		layer.tips('设置成功!', curInput);");
    	sb.append("		var tagId = $(curInput).attr('id');");
    	sb.append("		if(intSty[tagId] == undefined){ intSty[tagId]={}; }; ");
    	sb.append("		intSty[tagId].fontSize =value; ");
    	sb.append(" }");
    	
    	//设置字体
	    sb.append(" function setFontFamily(value){	");
	    sb.append(" 	if(curInput==null){	");
		sb.append(" 		layer.alert('请选择需要设置的输入框!');	");
		sb.append(" 		return;	");
	    sb.append(" 	}	");
	    sb.append("		var attr = 'font-family'; ");
	    sb.append("		$(curInput).css('font-family', value);");
    	sb.append("		layer.tips('设置成功!', curInput);");
    	sb.append("		var tagId = $(curInput).attr('id');");
    	sb.append("		if(intSty[tagId] == undefined){ intSty[tagId]={}; }; ");
    	sb.append("		intSty[tagId][attr] =value; ");
    	sb.append(" }	");
    	
    	//激活对齐方式按钮
    	sb.append(" function activeAlignBtn(value,align){	");
    	sb.append(" 	if(value == 'h'){");
    	sb.append(" 		inactiveBtn4Input();");
    	sb.append(" 		if(align == 'left'){");
    	sb.append(" 			$('#left').addClass('active'); ");
    	sb.append("	 		}else if(align == 'center'){");
    	sb.append("	 			$('#center').addClass('active'); ");
    	sb.append("	 		}else if(align == 'right'){");
    	sb.append("	 			$('#right').addClass('active'); ");
    	sb.append("	 		}");
    	sb.append(" 	}else if(value == 'v'){");
    	sb.append(" 		inactiveBtn4Area();");
    	sb.append(" 		if(align == 'top'){");
    	sb.append(" 			$('#top').addClass('active'); ");
    	sb.append("	 		}else if(align == 'middle'){");
    	sb.append(" 			$('#vcenter').addClass('active'); ");
    	sb.append("	 		}else if(align == 'bottom'){");
    	sb.append(" 			$('#bottom').addClass('active'); ");
    	sb.append("	 		}");
    	sb.append(" 	}");
    	sb.append(" }	");
    	
    	//激活字体样式按钮
    	sb.append(" function activeFontBtn(obj){	");
    	sb.append(" 	inactiveBtn4Font();");
    	sb.append(" 	if($(obj).css('font-weight')=='bold'){	");
    	sb.append(" 		$('#boldLi').addClass('active');	");
    	sb.append(" 	}	");
    	sb.append(" 	if($(obj).css('font-style')=='italic'){	");
    	sb.append(" 		$('#italicLi').addClass('active');	");
    	sb.append(" 	}	");
    	sb.append(" 	if($(obj).css('text-decoration')=='underline'){	");
    	sb.append(" 		$('#underlineLi').addClass('active');	");
    	sb.append(" 	}	");
    	sb.append(" }	");
    	
    	sb.append(" function inactiveAllBtn(){");
    	sb.append(" 	$('#left').removeClass('active');");
    	sb.append(" 	$('#center').removeClass('active');");
    	sb.append(" 	$('#right').removeClass('active');");
    	sb.append(" 	$('#top').removeClass('active');");
    	sb.append(" 	$('#vcenter').removeClass('active');");
    	sb.append(" 	$('#bottom').removeClass('active');");
    	sb.append(" 	$('#fontSizeLi').removeClass('active');");
    	sb.append(" }");
    	
    	sb.append(" function inactiveBtn4Input(){");
    	sb.append(" 	$('#left').removeClass('active');");
    	sb.append(" 	$('#center').removeClass('active');");
    	sb.append(" 	$('#right').removeClass('active');");
    	sb.append(" }");
    	
       	sb.append(" function inactiveBtn4Font(){");
    	sb.append(" 	$('#boldLi').removeClass('active');");
    	sb.append(" 	$('#italicLi').removeClass('active');");
    	sb.append(" 	$('#underlineLi').removeClass('active');");
    	sb.append(" }");
    	
    	sb.append(" function inactiveBtn4Area(){");
    	sb.append(" 	$('#top').removeClass('active');");
    	sb.append(" 	$('#vcenter').removeClass('active');");
    	sb.append(" 	$('#bottom').removeClass('active');");
    	sb.append(" }");
    	
    	sb.append(" function enableAllBtn(){");
    	sb.append(" 	$('.toolbar-box-b>li').removeClass('unclick');");
    	sb.append(" }");
    	
    	sb.append(" function disableAllBtn(){");
    	sb.append(" 	$('.toolbar-box-b>li').addClass('unclick');");
    	sb.append(" }");
    	
    	sb.append(" function inactiveVAlignBtn(){");
    	sb.append(" 	$('#top').addClass('unclick');");
    	sb.append(" 	$('#vcenter').addClass('unclick');");
    	sb.append(" 	$('#bottom').addClass('unclick');");
    	sb.append(" }");
    	sb.append("</script> ");
	}

	/*
	 ** 解析图片
	 */
	private List<PictureStyleBo> parsePic(ExcelTableBo table) throws Exception {
		List<PictureStyleBo> pbList = new ArrayList<PictureStyleBo>();
		List<ExcelPictureBo> listPic = table.getPicList();
		for(ExcelPictureBo picBo : listPic ){
			int top = picBo.getTop(picBo.getRow1());
			int left = picBo.getLeft(picBo.getCol1());
			double width = picBo.getWidth(picBo.getCol1(), picBo.getCol2());
			double height = picBo.getHeight(picBo.getRow1(), picBo.getRow2());
			PictureStyleBo pb = new PictureStyleBo();
			pb.setTop(top);
			pb.setLeft(left);
			pb.setWidth(width);
			pb.setHeight(height);
			
			if(picBo.getSheet() != null){
				pb.setSheetIndex(picBo.getSheetIndex());
			}
			String id = uploadPic(picBo);
			picBo.setId(id);
			pb.setId(id);
			pbList.add(pb);
		}
		Collections.sort(pbList);
		return pbList;
	}
	
	//上传Excel图片
	private String uploadPic(ExcelPictureBo picBo) throws Exception {
		IFrameAnnex iAnnex = FrameFactory.getAnnexFactory(null);
		// 查找html的param参数
		Map<String, String> htmlParMap = new HashMap<String, String>();
		htmlParMap.put("formcode", "TfPreform");
		htmlParMap.put("curcode", "excelPic");
		
		AnnexModel picAnnex = new AnnexModel();
		ByteArrayInputStream picByteInputStream = null;
		try{
			picByteInputStream = new ByteArrayInputStream(picBo.getData());
			iAnnex.upload(picAnnex, picByteInputStream, htmlParMap);
		}catch(Exception e){
			log.error(e.toString());
		}finally{
			if(picByteInputStream != null){
				picByteInputStream.close();
			}
		}
		return picAnnex.getId();
	}
	
	public static void main(String args[]) throws Exception {
		IExcel2Html html = new Excel2MdfHtml();
		Excel2Table eu = new Excel2Table();
		/*ExcelTableBo table = eu.toTable("E:\\02 监理单位用表\\96ED5000.xls", 1);*/
		ExcelTableBo table = eu.toTable("D:/wangl/01总监理工程师任命通知书GD220201.xls", 0);
		String htmlbuf = html.toHtml(table);
		
		FileOutputStream fos = null;
		try {

			File file = new File(
					"D:/wangl/01总监理工程师任命通知书GD220201.html");
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
