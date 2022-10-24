package com.googlecode.chainer.model;

import java.beans.Introspector;
import java.lang.reflect.Method;
import com.googlecode.chainer.util.ChainerUtils;

public class SetterInfo {
    private Class type;
    private String typeName;
    private String setterName;
    private String chainName;

    public SetterInfo(Class type, Method writeMethod) {
        this.type = type;
        this.typeName = ChainerUtils.getShortName(type.getName());
        this.setterName = writeMethod.getName();
        this.chainName = Introspector.decapitalize(setterName.substring(3));
        if (ChainerUtils.isJavaReservedWord(this.chainName)) {
            this.chainName = this.chainName + "Value";
        }
    }

    public Class getType() {
        return type;
    }
    public void setType(Class type) {
        this.type = type;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public String getSetterName() {
        return setterName;
    }
    public void setSetterName(String setterName) {
        this.setterName = setterName;
    }
    public String getChainName() {
        return chainName;
    }
    public void setChainName(String chainName) {
        this.chainName = chainName;
    }
}
