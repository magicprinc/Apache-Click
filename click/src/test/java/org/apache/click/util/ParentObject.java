package org.apache.click.util;

import java.util.Date;

public class ParentObject {

  private String name;
  private Object value;
  private Date date;
  private ChildObject child;
  private Boolean valid;

  public ParentObject(String name, Object value, Date date, Boolean valid, ChildObject child) {
    this.name = name;
    this.value = value;
    this.date = date;
    this.valid = valid;
    this.child = child;
  }

  public ParentObject() {
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Boolean getValid() {
    return valid;
  }

  public Object getValue() {
    return value;
  }

  public void setValid(Boolean valid) {
    this.valid = valid;
  }

  public ChildObject getChild() {
    return child;
  }

  public void setChild(ChildObject child) {
    this.child = child;
  }
}