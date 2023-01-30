package net.sf.click.ajax4click.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class Options extends HashMap<String,Object> {

  private JSONWriter jsonWriter;

  public Options() {
  }

  public Options(String key, Object value) {
    put(key, value);
  }

  public Options(String key, JSONLiteral value) {
    put(key, value);
  }

  public Options put(String key, boolean value) {
    super.put(key, value);
    return this;
  }

  public Options put(String key, int value) {
    super.put(key, value);
    return this;
  }

  public Options put(String key, long value) {
    super.put(key, value);
    return this;
  }

  public Options put(String key, double value) {
    super.put(key, value);
    return this;
  }

  public Options put(String key, Map value) {
    super.put(key, value);
    return this;
  }

  public Options put(String key, List value) {
    super.put(key, value);
    return this;
  }

  public Options putList(String key, Object... options) {
    super.put(key, Arrays.asList(options));
    return this;
  }

  public Options put(String key, Object bean) {
    super.put(key, bean);
    return this;
  }

  public Options put(String key, JSONLiteral literal) {
    super.put(key, literal);
    return this;
  }

  public Options putLiteral(String key, String literal) {
    super.put(key, new JSONLiteral(literal));
    return this;
  }

  public Options putFunction(String key, String function) {
    super.put(key, new JSONLiteral(function));
    return this;
  }

  public String toJson() {
    return getJSONWriter().write(this);
  }

  public String toFormattedJson() {
    return getJSONWriter().writeFormatted(this);
  }

  @Override
  public String toString() {
    return toJson();
  }

  // Private Methods --------------------------------------------------------

  private JSONWriter getJSONWriter() {
    if (jsonWriter == null) {
      jsonWriter = new JSONWriter();
    }
    return jsonWriter;
  }
}