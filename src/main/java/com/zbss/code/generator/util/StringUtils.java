package com.zbss.code.generator.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author zbss
 * @desc 字符串工具类
 * @date 2016-4-22 下午5:59:30
 */
public class StringUtils {
	
	// 判断字符串是否为空
	public static boolean isEmpty(String str){
		if (str == null || "".equals(str) || "null".equals(str)) {
			return true;
		}
		return false;
	}
	
	// 判断字符串是否为非空
	public static boolean isNotEmpty(String str){
		if (str != null && !"".equals(str) && !"null".equals(str)) {
			return true;
		}
		return false;
	}
	
	// 去除字符串开头的空格
	public static String trimLeft(String str){
		if (str == null || "".equals(str)) {
			return str;
		}
		int idx = 0;
		for (int i = 0, stop = str.length(); i < stop; i++){
			if (Character.isWhitespace(str.charAt(i))) {
				idx++;
			} else {
				break;
			}
		}
		return idx != 0 ? str.substring(idx) : str;
	}
	
	// 去除字符串结尾的空格
	public static String trimRight(String str){
		if (str == null || "".equals(str)) {
			return str;
		}
		int idx = str.length();
		int length = str.length();
		for (int i = str.length()-1; i > 0; i--){
			if (Character.isWhitespace(str.charAt(i))) {
				idx--;
			} else {
				break;
			}
		}
		return idx == length ? str : str.substring(0, idx);
	}
	
	// 去除字符串开头和结尾的空格
	public static String trim(String str){
		if (str == null || "".equals(str)) {
			return str;
		}
		return str.trim();
	}
	
	// 去除字符串中的所有空格
	public static String trimAll(String str){
		if (str == null || "".equals(str)) {
			return str;
		}
		return str.replaceAll(" ", "");
	}
	
	// 判断字符串是否是数字
	public static boolean isNumber(String str){
		if (isEmpty(str)) {
			return false;
		}
		
		for (int i = 0, stop = str.length(); i < stop; i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		
		return true;
	}

	// 获取异常的堆栈信息
	public static String getStackTrace(Throwable t){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		try{
			t.printStackTrace(pw);
			return sw.toString();
		}finally{
			pw.close();
		}
	}
	
}
