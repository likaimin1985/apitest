package utils;

import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

public class FunctionUtils {
	
	
	public static final String pattern = "#\\{(.+?)\\}"; // 非贪婪
	public static final Pattern r = Pattern.compile(pattern);

	
	
	
	public static String functionHandler(String str) {
		if(StringUtils.isNotBlank(str)) {
//			//参数化支持
			//str = ParamUtils.matcher2(str);
			//函数提取
			Matcher m = r.matcher(str);
			while (m.find()) {
				System.out.println("Found value: " + m.group(0));
				System.out.println("Found value: " + m.group(1));
				String functionValue = getFunctionValue(m.group(1));
				str = str.replace(m.group(0), functionValue);
			}
			return str;
		}
		return str;
	}
	
	

	private static String getFunctionValue(String functionKey) {
		if ("__UUID".equalsIgnoreCase(functionKey)) {
			return UUID.randomUUID().toString();
		} else if ("__time".equalsIgnoreCase(functionKey)) {
			return "" + System.currentTimeMillis();
		} else if (functionKey.startsWith("__md5")) {
			// System.out.println("md5");
			// 提取参数
			// 例如__md5(test,abc,test)
			// __md5(test,abc)
			// __md5(test)
			//test,abc,test->""testabctest
			String args = getFunctionArgs(functionKey);
			if (StringUtils.isNotBlank(args)) {
				try {
					return DigestUtils.md5Hex(args.getBytes("UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}
	
	private static String ags = "\\((.+?)\\)"; // 非贪婪
	private static Pattern agspattern = Pattern.compile(ags);

	private static String getFunctionArgs(String functionKey) {
		Matcher m = agspattern.matcher(functionKey);
		if (m.find()) {
			return m.group(1).replace(",", "");
		}
		return "";
	}

}
