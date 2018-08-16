package com.zbss.code.generator.util;

import java.io.*;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
*@author zbss
*@desc
*@date 2017/12/12 14:35
*/
public class ObjectUtils {

    /**数字的正则表达式*/
    public static final String PATTERN_NUMBERIC = "^(([0-9]+)|(-[0-9]+))(.[0-9]+)?$";

    /**
     * 判断对象是否为NULL
     * @param obj 要判断的对象
     * @return
     */
    public static boolean isNull(Object obj){
        return obj == null;
    }

    /**
     * 判断对象是否不为NULL
     * @param obj 要判断的对象
     * @return
     */
    public static boolean isNotNull(Object obj){
        return obj != null;
    }

    /**
     * 判断对象是否为空
     * @param obj 要判断的对象
     * @return
     */
    public static boolean isEmpty(Object obj){
        if (obj == null){
            return true;
        }

        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }
        return false;
    }

    /**
     * 判断对象是否不为空
     * @param obj 要判断的对象
     * @return
     */
    public static boolean isNotEmpty(Object obj){
        return !isEmpty(obj);
    }

    /**
     * 判断字符串是否为数字，包括小数、负数
     * @param number 要匹配的字符串
     * @return
     */
    public static boolean isNumeric(String number){
        if (isEmpty(number)){
            return false;
        }
        return number.matches(PATTERN_NUMBERIC);
    }

    /**
     * 判断是否为中文字符串
     * @param cnString
     * @return
     */
    public static boolean isChinese(String cnString) {
        if (isEmpty(cnString)){
            return false;
        }
        char[] ch = cnString.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!isChinese(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否为中文字符
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    /**
     * 拷贝对象（要求对象实现了序列化接口）
     * @param object 要被赋值的对象
     * @param <T> 对象的类型（泛型）
     * @return
     */
    public static <T> T copy(T object){
        if (isNull(object)){
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ByteArrayInputStream bis = null;
        ObjectOutputStream os = null;
        ObjectInputStream is = null;
        try {
            os = new ObjectOutputStream(bos);
            os.writeObject(object);
            byte[] bytes = bos.toByteArray();
            bis = new ByteArrayInputStream(bytes);
            is = new ObjectInputStream(bis);
            return (T) is.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 序列化对象（要求对象实现了序列化接口，注意NPE的处理）
     * @param object 要序列化的对象
     * @return
     */
    public static byte[] serialize(Object object) {
        if (isNull(object)){
            return null;
        }
        ObjectOutputStream os = null;
        ByteArrayOutputStream bs = null;
        try {
            bs = new ByteArrayOutputStream();
            os = new ObjectOutputStream(bs);
            os.writeObject(object);
            byte[] bytes = bs.toByteArray();
            return bytes;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 反序列化（要求对象实现了序列化接口，注意NPE的处理）
     * @NPE
     * @param bytes 要被反序列化的字节数组
     * @return
     */
    public static Object unserialize(byte[] bytes) {
        if (isNull(bytes)){
            return null;
        }
        ByteArrayInputStream bs = null;
        try {
            bs = new ByteArrayInputStream(bytes);
            ObjectInputStream os = new ObjectInputStream(bs);
            return os.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取异常信息的字符串形式
     * @param t 异常对象
     * @return
     */
    public static String getStackTrace(Throwable t){
        if (isNotNull(t)){
            return null;
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try{
            t.printStackTrace(pw);
            return sw.toString();
        }finally{
            pw.close();
        }
    }

    public static void main(String[] args) {
        System.out.println(isNumeric("-1.1"));
        getStackTrace(new Exception("heee"));
        serialize(null);
        unserialize(null);
        isNotEmpty(null);
        copy(null);
        isChinese(null);
    }

}
