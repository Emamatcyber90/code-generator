package com.zbss.code.generator.generator;

import com.alibaba.fastjson.JSONObject;
import com.zbss.code.generator.config.Config;
import com.zbss.code.generator.plugins.Plugin;
import com.zbss.code.generator.util.ObjectUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author zbss
 * @desc
 * @date 2018/8/15 13:33
 */
public abstract class Generator {

    Config config;
    JSONObject conf;

    Generator(Config config) {
        this.config = config;
        conf = config.getConfig();
    }

    public abstract void generateFile() throws Exception;

    public abstract void mergeFile() throws Exception;

    public abstract void writeFile() throws Exception;

    public abstract void executePlugin(Plugin plugin) throws Exception;

    public void generate() throws Exception {
        generateFile();
        pluginFile();
        mergeFile();
        writeFile();
    }

    private void pluginFile() throws Exception{
        List<Plugin> pluginList = config.getPluginList();
        if (ObjectUtils.isNotEmpty(pluginList)) {
            for (Plugin plugin : pluginList) {
                executePlugin(plugin);
            }
        }
    }

    File getDirectory(String targetProject, String targetPackage) throws Exception {
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

    void writeFile(File file, String content, String fileEncoding) throws Exception {
        FileOutputStream fos = new FileOutputStream(file, false);
        OutputStreamWriter osw;
        if (fileEncoding == null) {
            osw = new OutputStreamWriter(fos);
        } else {
            osw = new OutputStreamWriter(fos, fileEncoding);
        }

        BufferedWriter bw = new BufferedWriter(osw);
        bw.write(content);
        bw.close();
    }

}
