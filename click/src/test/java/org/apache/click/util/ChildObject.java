package org.apache.click.util;

import lombok.Getter;
import lombok.Setter;

public class ChildObject {

  @Getter @Setter private String name;
  @Getter private String email;

  public ChildObject() {}

  public ChildObject(String name, String email) {
    this.name = name;
    this.email = email;
  }
}