package com.zbss.code.generator.util;

import java.io.*;
import java.util.Properties;
import java.util.StringTokenizer;

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
     * @param file
     * @return
     * @throws Exception
     * @desc 读取绝对路径文件
     */
    public static String readFile(File file) throws Exception {
        return readFile(file, CHAESET);
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
     * @param file    文件路径
     * @param charset 读取编码
     * @return
     * @throws IOException
     * @desc 读取文件
     */
    public static String readFile(File file, String charset) throws IOException {
        if (charset == null || "".equals(charset)) {
            charset = CHAESET;
        }
        FileInputStream in = new FileInputStream(file);
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
    public static void writeFile(String filePath, String content, String charset) throws Exception {
        if (charset == null || "".equals(charset)) {
            charset = CHAESET;
        }
        File file = new File(filePath);
        writeFile(file, content, charset);
    }

    /**
     * @param file    文件
     * @param content 内容
     * @param charset 编码
     * @throws IOException
     * @desc 写文件
     */
    public static void writeFile(File file, String content, String charset) throws Exception {
        FileOutputStream fos = new FileOutputStream(file, false);
        OutputStreamWriter osw;
        if (charset == null) {
            osw = new OutputStreamWriter(fos);
        } else {
            osw = new OutputStreamWriter(fos, charset);
        }

        BufferedWriter bw = new BufferedWriter(osw);
        bw.write(content);
        bw.close();
    }

    /**
     * @param filePath
     * @return
     * @throws Exception
     * @desc 从classpath读取文件
     */
    public static String readFileFromClasspath(String filePath) throws Exception {
        InputStream stream = FileUtils.class.getClassLoader().getResourceAsStream(filePath);
        File targetFile = new File(filePath);
        org.apache.commons.io.FileUtils.copyInputStreamToFile(stream, targetFile);
        return readFile(targetFile, CHAESET);
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

    /**
     * @param targetProject
     * @param targetPackage
     * @return
     * @throws Exception
     * @desc 创建文件写入的目录
     */
    public static File getDirectory(String targetProject, String targetPackage) throws Exception {
        File project = new File(targetProject);
        if (!project.isDirectory()) {
            boolean rc = project.mkdirs();
            if (!rc) {
                throw new Exception("create targetProject dir failed !");
            }
        }

        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(targetPackage, ".");
        while (st.hasMoreTokens()) {
            sb.append(st.nextToken());
            sb.append(File.separatorChar);
        }

        File directory = new File(project, sb.toString());
        if (!directory.isDirectory()) {
            boolean rc = directory.mkdirs();
            if (!rc) {
                throw new Exception("create targetPackage dir failed !");
            }
        }

        return directory;
    }


}
