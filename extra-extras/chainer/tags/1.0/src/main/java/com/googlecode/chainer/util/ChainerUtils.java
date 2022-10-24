package com.googlecode.chainer.util;

public final class ChainerUtils {

    public static String getShortName(Class targetClass) {
        return getShortName(targetClass.getName());
    }
    public static String getShortName(String classFullName) {
        return classFullName.substring(classFullName.lastIndexOf('.') + 1);
    }
    public static String getPackageName(String classFullName) {
        return classFullName.substring(0, classFullName.lastIndexOf('.'));
    }

}
