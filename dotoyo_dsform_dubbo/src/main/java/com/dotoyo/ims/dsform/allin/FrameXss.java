package com.dotoyo.ims.dsform.allin;

import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

public class FrameXss extends AbstractFrame implements IFrameXss {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameXss.class));

	protected static FrameXss instance = null;

	protected FrameXss() {

	}

	public static FrameXss getInstance(Map<String, String> param) {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new FrameXss();
		}
	}

	public void shutdown() {

	}

	public void startup() {

	}

	public String xss(String s) throws Exception {
		if (s == null || "".equals(s)) {
			return s;
		}
		StringBuilder sb = new StringBuilder(s.length() + 16);
		for (int i = 0; i < s.length(); i++) {
			StringBuilder key = new StringBuilder(s.length() + 16);
			for (int j = i; j < s.length(); j++) {
				char c = s.charAt(j);
				if (c == '<') {
					String a = s.substring(j, s.length());
					a = a.toLowerCase();
					if (a.startsWith("<img")) {
						IXssChecked checked = new XssChecked();
						String value = checked.check(key.toString());
						sb.append(value);
						// 清空字符
						key.setLength(0);
						for (int k = j; k < s.length(); k++) {
							char c1 = s.charAt(k);
							if (c1 == '>' && s.charAt(k - 1) == '/') {
								String key1=s.substring(j,k+1);
								IXssChecked checked1=new XssChecked4Img();
								String value1 = checked1.check(key1);
								sb.append(value1);
								break;
							}
							i++;
						}
						break;
					}else if(a.startsWith("<p") || a.startsWith("</p>")) {
						String k = "";
						if(a.startsWith("<p")){
							k = a.substring(0,a.indexOf(">") + 1);
						}else{
							k = "</p>";
						}
						
						IXssChecked checked1=new XssChecked4P();
						handleCheck(s, sb, key, j, k ,checked1);
						i = i + k.length() - 1;
						break;
					}else if(a.startsWith("<div") || a.startsWith("</div>")) {
						String k = "";
						if(a.startsWith("<div")){
							k = a.substring(0,a.indexOf(">") + 1);
						}else{
							k = "</div>";
						}
						
						IXssChecked checked1=new XssChecked4Div();
						handleCheck(s, sb, key, j, k ,checked1);
						i = i + k.length() - 1;
						break;
					}else if(a.startsWith("<br>") ||  a.startsWith("</br>") ||  a.startsWith("<br/>") || a.startsWith("<br />")) {
						String k = "";
						if(a.startsWith("<br>")){
							k = "<br>";
						}else if(a.startsWith("<br />")){
							k = "<br />";
						}else{
							k = "<br/>";
						}
						IXssChecked checked1=new XssChecked4Br();
						handleCheck(s, sb, key, j, k ,checked1);
						i = i + k.length() - 1;
						break;
					}else if(a.startsWith("<span") || a.startsWith("</span>")){
						String k = "";
						if(a.startsWith("<span")){
							k = a.substring(0,a.indexOf(">") + 1);
						}else{
							k = "</span>";
						}
						
						IXssChecked checked1=new XssChecked4Span();
						handleCheck(s, sb, key, j, k ,checked1);
						i = i + k.length() - 1;
						break;
					}else if(a.startsWith("<strong>") || a.startsWith("</strong>")){
						String k = "<strong>";
						if(a.startsWith("</strong>")){
							k = "</strong>";
						}
						IXssChecked checked1=new XssChecked4Strong();
						handleCheck(s, sb, key, j, k ,checked1);
						i = i + k.length() - 1;
						break;					
					}else if(a.startsWith("<u>") || a.startsWith("</u>")){
						String k = "<u>";
						if(a.startsWith("</u>")){
							k = "</u>";
						}
						IXssChecked checked1=new XssChecked4U();
						handleCheck(s, sb, key, j, k ,checked1);
						i = i + k.length() - 1;
						break;
					}else if(a.startsWith("<h1>") || a.startsWith("<h2>") || a.startsWith("<h3>") || a.startsWith("<h4>")){
						String k = "<h1>";
						IXssChecked checked1=new XssChecked4H();
						handleCheck(s, sb, key, j, k ,checked1);
						i = i + k.length() - 1;
						break;
					}else if(a.startsWith("</h1>") || a.startsWith("</h2>") || a.startsWith("</h3>") || a.startsWith("</h4>")){
						String k = "</h1>";
						IXssChecked checked1=new XssChecked4H();
						handleCheck(s, sb, key, j, k ,checked1);
						i = i + k.length() - 1;
						break;
					}else if(a.startsWith("<em>") || a.startsWith("</em>")){
						String k = "<em>";
						if(a.startsWith("</em>")){
							k = "</em>";
						}
						IXssChecked checked1=new XssChecked4Em();
						handleCheck(s, sb, key, j, k ,checked1);
						i = i + k.length() - 1;
						break;
					}else if(a.startsWith("<s>") || a.startsWith("</s>")){
						String k = "<s>";
						if(a.startsWith("</s>")){
							k = "</s>";
						}
						IXssChecked checked1=new XssChecked4S();
						handleCheck(s, sb, key, j, k ,checked1);
						i = i + k.length() - 1;
						break;
					}else if(a.startsWith("<ul>") || a.startsWith("</ul>")){
						String k = "<ul>";
						if(a.startsWith("</ul>")){
							k = "</ul>";
						}
						IXssChecked checked1=new XssChecked4Ul();
						handleCheck(s, sb, key, j, k ,checked1);
						i = i + k.length() - 1;
						break;
					}else if(a.startsWith("<ol>") || a.startsWith("</ol>")){
						String k = "<ol>";
						if(a.startsWith("</ol>")){
							k = "</ol>";
						}
						IXssChecked checked1=new XssChecked4Ol();
						handleCheck(s, sb, key, j, k ,checked1);
						i = i + k.length() - 1;
						break;
					}else if(a.startsWith("<li>") || a.startsWith("</li>")){
						String k = "<li>";
						if(a.startsWith("</li>")){
							k = "</li>";
						}
						IXssChecked checked1=new XssChecked4Li();
						handleCheck(s, sb, key, j, k ,checked1);
						i = i + k.length() - 1;
						break;
					}
				}else if(c=='&'){
					String a = s.substring(j, s.length());
					if(a.toLowerCase().startsWith("&nbsp;")){
						String k = "&nbsp;";
						IXssChecked checked1=new XssChecked4Nbsp();
						handleCheck(s, sb, key, j, k ,checked1);
						i = i + k.length() - 1;
						break;
					}else if(a.toLowerCase().startsWith("&lt;")){
						String k = "&lt;";
						IXssChecked checked1=new XssChecked4Lt();
						handleCheck(s, sb, key, j, k ,checked1);
						i = i + k.length() - 1;
						break;
					}else if(a.toLowerCase().startsWith("&gt;")){
						String k = "&gt;";
						IXssChecked checked1=new XssChecked4Gt();
						handleCheck(s, sb, key, j, k ,checked1);
						i = i + k.length() - 1;
						break;
					}else if(a.toLowerCase().startsWith("&amp;")){
						String k = "&amp;";
						IXssChecked checked1=new XssChecked4Amp();
						handleCheck(s, sb, key, j, k ,checked1);
						i = i + k.length() - 1;
						break;
					}else if(a.toLowerCase().startsWith("&hellip;")){
						String k = "&hellip;";
						IXssChecked checked1=new XssChecked4Hellip();
						handleCheck(s, sb, key, j, k ,checked1);
						i = i + k.length() - 1;
						break;
					}else if(a.toLowerCase().startsWith("&ldquo;")){
						String k = "&ldquo;";
						IXssChecked checked1=new XssChecked4Ldquo();
						handleCheck(s, sb, key, j, k ,checked1);
						i = i + k.length() - 1;
						break;
					}else if(a.toLowerCase().startsWith("&lsquo;")){
						String k = "&lsquo;";
						IXssChecked checked1=new XssChecked4Lsquo();
						handleCheck(s, sb, key, j, k ,checked1);
						i = i + k.length() - 1;
						break;
					}else if(a.toLowerCase().startsWith("&mdash;")){
						String k = "&mdash;";
						IXssChecked checked1=new XssChecked4Mdash();
						handleCheck(s, sb, key, j, k ,checked1);
						i = i + k.length() - 1;
						break;
					}else if(a.toLowerCase().startsWith("&quot;")){
						String k = "&quot;";
						IXssChecked checked1=new XssChecked4Quot();
						handleCheck(s, sb, key, j, k ,checked1);
						i = i + k.length() - 1;
						break;
					}else if(a.toLowerCase().startsWith("&rdquo;")){
						String k = "&rdquo;";
						IXssChecked checked1=new XssChecked4Rdquo();
						handleCheck(s, sb, key, j, k ,checked1);
						i = i + k.length() - 1;
						break;
					}else if(a.toLowerCase().startsWith("&middot;")){
						String k = "&middot;";
						IXssChecked checked1=new XssChecked4Middot();
						handleCheck(s, sb, key, j, k ,checked1);
						i = i + k.length() - 1;
						break;
					}else if(a.toLowerCase().startsWith("&#39;")){
						String k = "&#39;";
						IXssChecked checked1=new XssChecked439();
						handleCheck(s, sb, key, j, k ,checked1);
						i = i + k.length() - 1;
						break;
					}
				}
				key.append(c);
				i++;
			}
			if (key.length() > 0) {
				IXssChecked checked = new XssChecked();
				String value = checked.check(key.toString());
				sb.append(value);
			}
		}
		return sb.toString();
	}

	private void handleCheck(String s, StringBuilder sb, StringBuilder key,
			int j,String k ,IXssChecked checked1) throws Exception {
		IXssChecked checked = new XssChecked();
		String value = checked.check(key.toString());
		sb.append(value);
		// 清空字符
		key.setLength(0);
		//
		String key1=s.substring(j,j+k.length());
		String value1 = checked1.check(key1);
		sb.append(value1);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		FrameXss xss = new FrameXss();
		String s = "<p>111<img alt=\"\" src=\"http://dsframe.10333.com/ckfinder/images/u=3603402794,2797358574&fm=116&gp=0.jpg\" style=\"height:143px; width:220px\" />2222<img alt=\"\" src=\"http://dsframe.10333.com/ckfinder/images/u=3907653277,3630795162&fm=116&gp=0.jpg\" style=\"height:123px; width:220px\" /></p>";
		String value = xss.xss(s);
		log.debug(value);
	}
}
