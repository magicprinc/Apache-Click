package com.googlecode.chainer.util;

import java.util.Arrays;
import java.util.List;

public final class ChainerUtils {

    public static String getShortName(Class targetClass) {
        return targetClass.getSimpleName();
    }
    public static String getShortName(String classFullName) {
        return classFullName.substring(classFullName.lastIndexOf('.') + 1);
    }
    public static String getPackageName(String classFullName) {
        return classFullName.substring(0, classFullName.lastIndexOf('.'));
    }
    public static String getImportName(Class targetClass) {
        return targetClass.getCanonicalName();
    }
    public static boolean isJavaReservedWord(String word) {
        return reservedWords.contains(word);
    }

    private static final List reservedWords = Arrays.asList(
        "abstract", "do", "if", "package", "synchronized", 
        "boolean", "double", "implements", "private", "this", 
        "break", "else", "import", "protected", "throw", 
        "byte", "extends", "instanceof", "public", "throws", 
        "case", "false", "int", "return", "transient", 
        "catch", "final", "interface", "short", "true", 
        "char", "finally", "long", "static", "try", 
        "class", "float", "native", "strictfp", "void", 
        "const", "for", "new", "super", "volatile", 
        "continue", "goto", "null", "switch", "while", 
        "default", "assert");
}
