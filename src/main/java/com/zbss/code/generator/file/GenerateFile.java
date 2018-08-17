package com.zbss.code.generator.file;

/**
 * @author zbss
 * @desc
 * @date 2018/8/17 11:00
 */
public abstract class GenerateFile<T> {

    String targetProject;
    String targetPackage;
    String name;
    String content;
    FileTypeEnum type;
    FileTypeEnum flag;
    T data;

    abstract String convertDataToContent(T data);

    public String getContent() {
        content = content == null || content.equals("") ? convertDataToContent(data) : content;
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileTypeEnum getType() {
        return type;
    }

    public void setType(FileTypeEnum type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getTargetProject() {
        return targetProject;
    }

    public void setTargetProject(String targetProject) {
        this.targetProject = targetProject;
    }

    public String getTargetPackage() {
        return targetPackage;
    }

    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    public FileTypeEnum getFlag() {
        return flag;
    }

    public void setFlag(FileTypeEnum flag) {
        this.flag = flag;
    }
}
