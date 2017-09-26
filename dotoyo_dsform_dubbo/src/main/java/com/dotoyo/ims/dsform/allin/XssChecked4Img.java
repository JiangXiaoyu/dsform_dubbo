package com.dotoyo.ims.dsform.allin;

/*
 * <img alt=\"\" src=\"http://dsframe.10333.com/ckfinder/images/u=3603402794,2797358574&fm=116&gp=0.jpg\" style=\"height:143px; width:220px\" /> 
 * ' 和 " 必须是双数
 * < 和 > 必须有头有尾
 * < 和 > 不能在引号内
 * url必须以http://dsframe.10333.com:8080/ 开头
 * url必须不包含<script> < script > <(Tab)script(Tab)>  </script> < /script > <(Tab)/script(Tab)>
 */
public class XssChecked4Img implements IXssChecked{

	@Override
	public String check(String key) throws Exception {
		int length = key.length();
		int m = 0;//"的次数
		int n = 0;//'的次数
		int j = 0;//遇到<自增 >自减
		for(int i = 0; i < length; i++){
			if(key.charAt(i) == '\''){
				n++;
			}else if(key.charAt(i) == '"'){
				m++;
			}else if(key.charAt(i) == '<'){
				//<在引号之内
				if(n%2==1 || m%2==1){
					return "";
				}
				j++;
			}else if(key.charAt(i) == '>'){
				//>在引号之内
				if(n%2==1 || m%2==1){
					return "";
				}
				j--;
			}
		}
		int index = key.indexOf("src");
		int startIndex = -1;
		int lastIndex = -1;
		char c = ' ';//单引号或双引号
		if(index != -1){
			for(int i = index; i < length; i++){
				if(c == ' ' && key.charAt(i)=='\'' ){
					startIndex = i;
					c = '\'';
				}else if(c == ' ' && key.charAt(i)=='"' ){
					startIndex = i;
					c = '"';
				}else if(c == key.charAt(i)){
					lastIndex = i;
				}
			}
		}
		if(m%2==1 || n%2==1 || j!=0){
			//' " < >次数异常
			return "";
		}
		//检验url是否为http://dsframe.10333.com:8080/
/*		String url = key.substring(startIndex+1,lastIndex);
		String prefixPath = PreformConfig.getInstance().getConfig(PreformConfig.FILE_PATH);//http://dsframe.10333.com
			
		if(!url.startsWith(prefixPath)){
			//url异常
			return "";
		}*/
/*		if((index = url.indexOf("script"))!=-1){
			while((index = url.indexOf("script",index))!=-1){
				for(int i = index-1; i >-1; i--){
					char ch = url.charAt(i);
					if(ch == ' ' || ch == '\t'){
						//script前面是空格或\t继续往前查找字符
					}else if( ch == '<'){
						//script前面是< 就认为它包含<script>
						return "";
					}else{
						//script前面是其他字符就不处理
						break;
					}
				}
				index  = index + 6;
			}
		}*/
		return key;
	}
	
	public static void main(String[]args) throws Exception{
		//正常
		String key = "<img alt=\"\" src='http://dsframe.10333.com/ckfinder/images/u=3603402794,2797358574&fm=116&gp=0.jpg' style=\"height:143px; width:220px\" /> ";
		// 
		//< >次数不正常
		//String key = "<img alt=\"\" src=\"http://dsframe.10333.com/ckfinder/images/u=3603402794,2797358574&fm=116&gp=0.jpg\" style=\"height:143px; width:220px\" />> ";
		//
		//< >在引号里面
		//String key = "<img alt=\"\" src=\"http://dsframe.10333.com/ckfinder/images/u=3603402794,2797358574&fm=116&gp=0.jpg<>\" style=\"height:143px; width:220px\" /> ";
		//
		//url不正常
		//String key = "<img alt=\"\" src=\"http://baidu.com:8080/dsframe/ckfinder/images/u=3603402794,2797358574&fm=116&gp=0.jpg\" style=\"height:143px; width:220px\" /> ";
		
		//url包含 <script>
		//String key = "<img alt=\"\" src=\"http://dsframe.10333.com/ckfinder/images/u=3603402794,2797358574&fm=116&gp=<		script>0.jpgscript\" style=\"height:143px; width:220px\" /> ";
		XssChecked4Img checkImg = new XssChecked4Img();
		System.out.println( checkImg.check(key));
		
	}
}
