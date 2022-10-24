package com.googlecode.chainer.model;

import java.io.File;
import com.googlecode.chainer.util.ChainerUtils;

public class ClassInfo {
    private Class sourceClass;
    private String fullName;

    private String packageName;
    private String filePath;
    private String shortName;
    private String fileName;

    public ClassInfo(Class sourceClass) {
        this(sourceClass.getName());
        this.sourceClass = sourceClass;
    }

    public ClassInfo(String fullClassName) {
        this.fullName = fullClassName;
        this.packageName = ChainerUtils.getPackageName(fullName);
        this.filePath = packageName.replace('.', File.separatorChar);
        this.shortName = ChainerUtils.getShortName(fullName);
        this.fileName = filePath + File.separator + shortName + ".java";
    }

    public Class getSourceClass() {
        return sourceClass;
    }
    public void setSourceClass(Class sourceClass) {
        this.sourceClass = sourceClass;
    }
    public String getPackageName() {
        return packageName;
    }
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    public String getPackagePath() {
        return filePath;
    }
    public void setPackagePath(String packagePath) {
        this.filePath = packagePath;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getShortName() {
        return shortName;
    }
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
