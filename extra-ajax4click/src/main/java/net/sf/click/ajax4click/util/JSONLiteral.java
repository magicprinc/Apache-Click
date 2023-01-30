package net.sf.click.ajax4click.util;

/** Provides a JSON literal which renders its value 'as-is', without quotes. */
public class JSONLiteral {

  private final String value;

  public JSONLiteral(String value) {
    this.value = value;
  }

  @Override public String toString(){ return value;}
}