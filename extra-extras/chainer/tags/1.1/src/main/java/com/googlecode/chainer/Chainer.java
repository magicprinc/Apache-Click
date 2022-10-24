package com.googlecode.chainer;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.googlecode.chainer.model.ClassInfo;
import com.googlecode.chainer.model.SetterInfo;
import com.googlecode.chainer.util.ChainerUtils;

public class Chainer {

    private ClassInfo sourceClassInfo;
    private ClassInfo targetClassInfo;
    private String indent1, indent2;

    private BufferedWriter writer;

    private List<SetterInfo> setterInfos = new ArrayList<SetterInfo>();
    private List imports = new ArrayList();

    public Chainer(String sourceClassFullName, String targetClassFullName) throws Exception {
        this(sourceClassFullName, targetClassFullName, 4);
    }
    
    public Chainer(String sourceClassFullName, String targetClassFullName, int indentSize)
        throws Exception
    {
        this.sourceClassInfo = new ClassInfo(Class.forName(sourceClassFullName));
        this.targetClassInfo = new ClassInfo(targetClassFullName);
        this.indent1 = "";

        for (int i = 0, t = indentSize; i < t; i++) {
            this.indent1 += " ";
        }
        this.indent2 = indent1 + indent1;

        collectSetterInfos();
        collectImports();
    }
 
    public void generate() throws Exception {
        new File(targetClassInfo.getPackagePath()).mkdirs();
        writer = new BufferedWriter(new FileWriter(targetClassInfo.getFileName()));

        writePackage();
        writeImports();
        writeClassDeclaration();
        writeConstructors();
        writeMethods();
        writeFooter();

        writer.close();
    }

    private void collectImports() {
        addImport(sourceClassInfo.getSourceClass());

        for (SetterInfo setterInfo : setterInfos) {
            addImport(setterInfo.getType());
        }
        Constructor[] constructors = sourceClassInfo.getSourceClass().getConstructors();

        for (Constructor cons : constructors) {
            Class[] types = cons.getParameterTypes();

            for (Class type : types) {
                addImport(type);
            }
        }
    }

    private void addImport(Class importedClass) {
        String importName = ChainerUtils.getImportName(importedClass);
        while (importName.endsWith("[]")) {
            importName = importName.substring(0, importName.length() - 2);
        }
        if (importName.indexOf(".") != -1 && !importName.startsWith("java.lang.")
                && !imports.contains(importName)) {
            imports.add(importName);
        }
    }

    private void writePackage() throws Exception {
        write("package ");
        write(targetClassInfo.getPackageName());
        writeln(";");
    }

    private void writeImports() throws Exception {
        Collections.sort(imports);

        writer.newLine();
        for (int i = 0, t = imports.size(); i < t; i++) {
            write("import ");
            write(imports.get(i).toString());
            writeln(";");
        }
        writer.newLine();
    }

    private void writeClassDeclaration() throws Exception {
        write("public class ");
        write(targetClassInfo.getShortName());
        write(" extends ");
        write(sourceClassInfo.getShortName());
        writeln(" {");
    }

    public void writeConstructors() throws Exception {
        Constructor[] constructors = sourceClassInfo.getSourceClass().getConstructors();

        for (Constructor cons : constructors) {
            Class[] types = cons.getParameterTypes();

            write(indent1);
            write("public ");
            write(targetClassInfo.getShortName());
            write("(");

            for (int j = 0, u = types.length; j < u; j++) {
                Class type = types[j];
                String typeName = ChainerUtils.getShortName(type);
                int argCounter = j + 1;
                write(typeName);
                write(" arg");
                write(String.valueOf(argCounter));
                if (j < u - 1) {
                    write(", ");
                }
            }
            writeln(") {");
            write(indent2);
            write("super(");

            for (int j = 0, u = types.length; j < u; j++) {
                int argCounter = j + 1;
                write("arg");
                write(String.valueOf(argCounter));
                if (j < u - 1) {
                    write(", ");
                }
            }
            writeln(");");
            write(indent1);
            writeln("}");
        }
        writer.newLine();
    }

    private void writeMethods() throws Exception {
        for (SetterInfo setterInfo : setterInfos) {
            write(indent1);
            write("public ");
            write(targetClassInfo.getShortName());
            write(" ");
            write(setterInfo.getChainName());
            write("(");
            write(setterInfo.getTypeName());
            writeln(" arg) {");
            write(indent2);
            write(setterInfo.getSetterName());
            writeln("(arg);");
            write(indent2);
            writeln("return this;");
            write(indent1);
            writeln("}");
        }
    }

    private void writeFooter() throws Exception {
        writeln("}");
    }

    private void collectSetterInfos() throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(sourceClassInfo.getSourceClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        for (PropertyDescriptor pd : propertyDescriptors) {
            Method writeMethod = pd.getWriteMethod();

            if (writeMethod != null) {
                setterInfos.add(new SetterInfo(pd.getPropertyType(), writeMethod));
            }
        }
    }

    private void writeln(String line) throws Exception {
        writer.write(line);
        writer.newLine();
    }

    private void write(String line) throws Exception {
        writer.write(line);
    }
}
