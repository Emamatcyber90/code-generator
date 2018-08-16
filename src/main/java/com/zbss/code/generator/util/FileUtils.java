package com.zbss.code.generator.util;

import java.io.*;
import java.util.Properties;

/**
 * @author zbss
 * @date 2017/11/25 12:12
 */
public class FileUtils {

    public static final String CHAESET = "UTF-8";

    /**
     * @param filePath
     * @return
     * @throws Exception
     * @desc 读取绝对路径文件
     */
    public static String readFile(String filePath) throws Exception {
        return readFile(filePath, CHAESET);
    }

    /**
     * @param filePath 文件路径
     * @param charset  读取编码
     * @return
     * @throws IOException
     * @desc 读取文件
     */
    public static String readFile(String filePath, String charset) throws IOException {
        if (charset == null || "".equals(charset)) {
            charset = CHAESET;
        }
        FileInputStream in = new FileInputStream(new File(filePath));
        BufferedReader br = new BufferedReader(new InputStreamReader(in, charset));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    /**
     * @param filePath 文件路径
     * @param content  内容
     * @param charset  编码
     * @throws IOException
     * @desc 写文件
     */
    public static void writeFile(String filePath, String content, String charset) throws IOException {
        if (charset == null || "".equals(charset)) {
            charset = CHAESET;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(file);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out, charset));
        bw.write(content);
        bw.flush();
        bw.close();
    }

    /**
     * @param filePath
     * @return
     * @throws Exception
     * @desc 从classpath读取文件
     */
    public static String readFileFromClasspath(String filePath) throws Exception {
        String classpath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        if (classpath.contains(":")) {
            classpath = classpath.substring(1);
        }
        return readFile(classpath + filePath, CHAESET);
    }

    /**
     * @param filePath
     * @return
     * @throws Exception
     * @desc 读取classpath下面的配置文件
     */
    public static Properties readPropertiesFromClassPath(String filePath) throws Exception {
        Properties p = new Properties();
        p.load(FileUtils.class.getClassLoader().getResourceAsStream(filePath));
        return p;
    }

    /**
     * @param filePath
     * @return
     * @throws Exception
     * @desc 读取绝对路径下面的配置文件
     */
    public static Properties readPropertiesFromAbsolutePath(String filePath) throws Exception {
        Properties p = new Properties();
        p.load(new FileInputStream(filePath));
        return p;
    }

}
