package net.sf.click.ajax4click.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Provides a JSON Writer that will output its content in the JSON format.
 * This class also support the following non-standard extensions:
 *
 * <ul>
 * <li>Support for <tt>literals</tt> - literal values won't be placed in quotes.
 * A common usage of literals are JavaScript functions.
 * </li>
 * <li>Support for <tt>unquoted</tt> names - makes reading the easier and cuts
 * down on some extra characters</li>
 * </ul>
 *
 * Original code adapter from : http://www.stringtree.org/stringtree-json.html
 */
public class JSONWriter {

  private final StringBuilder builder = new StringBuilder();

  private final ListStack<Object> calls = new ListStack<>();

  private boolean emitClassName = false;

  // ----------------------------------------------------------- constructors

  public JSONWriter(boolean emitClassName) {
    this.emitClassName = emitClassName;
  }

  public JSONWriter() {
  }

  // --------------------------------------------------------- public methods

  public String write(Object object) {
    builder.setLength(0);
    value(object);
    return builder.toString();
  }

  public String writeFormatted(Object object) {
    write(object);
    String json = builder.toString();
    return formatJson(json);
  }

  // -------------------------------------------------------- private methods

  @SuppressWarnings("unchecked")
  private void value(Object object) {
    if (object == null || cyclic(object)) {
      add("null");
    } else {
      calls.push(object);
      if (object instanceof Class) {
        string(object);
      } else if (object instanceof Boolean) {
        add(object.toString());
      } else if (object instanceof Number) {
        add(object);
      } else if (object instanceof String) {
        string(object);
      } else if (object instanceof Character) {
        string(object);
      } else if (object instanceof JSONLiteral) {
        add(object);
      } else if (object instanceof Map) {
        map((Map) object);
      } else if (object.getClass().isArray()) {
        array(object);
      } else if (object instanceof Iterator) {
        array((Iterator) object);
      } else if (object instanceof Collection) {
        array(((Collection) object).iterator());
      } else {
        bean(object);
      }
      calls.pop();
    }
  }

  private boolean cyclic(Object object) {
    for (Object called : calls){
      if (object == called){
        return true;
      }
    }
    return false;
  }

  private void bean(Object object) {
    add("{");
    BeanInfo info;
    boolean addedSomething = false;
    try {
      info = Introspector.getBeanInfo(object.getClass());
      PropertyDescriptor[] props = info.getPropertyDescriptors();
      for (PropertyDescriptor prop : props){
        String name = prop.getName();
        Method accessor = prop.getReadMethod();
        if (( emitClassName || !"class".equals(name) ) && accessor
            != null){
          if (!accessor.isAccessible()){
            accessor.setAccessible(true);
          }
          Object value = accessor.invoke(object, (Object[]) null);
          if (addedSomething){
            add(',');
          }
          add(name, value);
          addedSomething = true;
        }
      }
      Field[] ff = object.getClass().getFields();
      for (Field field : ff){
        if (addedSomething){
          add(',');
        }
        add(field.getName(), field.get(object));
        addedSomething = true;
      }
    } catch (IllegalAccessException | InvocationTargetException | IntrospectionException iae){
      throw new RuntimeException(iae);
    }
    add("}");
  }

  private void add(String name, Object value) {
    add('"');
    add(name);
    add("\":");
    value(value);
  }

  private void map(Map<String, Object> map) {
    add("{");
    Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<?, ?> e = it.next();
      value(e.getKey());
      add(":");
      value(e.getValue());
      if (it.hasNext()) {
        add(',');
      }
    }
    add("}");
  }

  private void array(Iterator<?> it) {
    add("[");
    while (it.hasNext()) {
      value(it.next());
      if (it.hasNext()) {
        add(",");
      }
    }
    add("]");
  }

  private void array(Object object) {
    add("[");
    int length = Array.getLength(object);
    for (int i = 0; i < length; ++i) {
      value(Array.get(object, i));
      if (i < length - 1) {
        add(',');
      }
    }
    add("]");
  }

  private void bool(boolean b) {
    add(b ? "true" : "false");
  }

  private void string(Object obj) {
    add('"');
    char prev;
    String str = obj.toString();
    int len = str.length();

    char c = 0;
    //for (char c : obj.toString().toCharArray()) {
    for (int i = 0; i < len; i++) {
      prev = c;
      c = str.charAt(i);
      switch (c) {
      case '\b':
        add("\\b");
        break;
      case '\t':
        add("\\t");
        break;
      case '\n':
        add("\\n");
        break;
      case '\f':
        add("\\f");
        break;
      case '\r':
        add("\\r");
        break;
      case '\\':
        add("\\\\");
        break;
      case '/':
        if (prev == '<') {
          add("\\/");
        } else {
          add('/');
        }
        break;
      case '"':
        add("\\\"");
        break;
      default:
        if (Character.isISOControl(c)) {
          unicode(c);
        } else {
          add(c);
        }
        break;
      }
    }
    add('"');
  }

  private void add(Object obj) {
    builder.append(obj);
  }

  private void add(char c) {
    builder.append(c);
  }
  static char[] hex = "0123456789ABCDEF".toCharArray();

  private void unicode(char c) {
    add("\\u");
    int n = c;
    for (int i = 0; i < 4; ++i) {
      int digit = (n & 0xf000) >> 12;
      add(hex[digit]);
      n <<= 4;
    }
  }

  private String formatJson(String json) {
    StringBuilder sb = new StringBuilder(json.length());
    try {
      formatJsonValue(new StringReader(json), 0, sb);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return sb.toString();
  }

  private void formatJsonValue(StringReader sr, int offset,
      StringBuilder sb) throws IOException {
    int local_offset = 0;
    while (true) {
      int i = sr.read();
      if (i == -1) {
        return;
      } else {
        char c = (char) i;
        switch (c) {
        case '{':
          sb.append("{ ");
          formatJsonValue(sr, offset + local_offset + 2, sb);
          sb.append(" }");
          break;
        case '[':
          sb.append('[');
          formatJsonArray(sr, offset + local_offset + 1, sb);
          sb.append(']');
          break;
        case '}':
          return;
        case ']':
          return;
        case ':':
          sb.append(": ");
          local_offset += 2;
          break;
        case '"':
          sb.append('"');
          String string = readJsonString(sr);
          sb.append(string);
          sb.append('"');
          local_offset += string.length() + 2;
          break;
        case ',':
          sb.append(",\n");
          indent(sb, offset);
          local_offset = 0;
          break;
        default:
          sb.append(c);
          local_offset++;
          break;
        }
      }
    }
  }

  private String readJsonString(StringReader sr) throws IOException {
    StringBuilder sb = new StringBuilder();
    while (true) {
      sr.mark(1);
      int i = sr.read();
      if (i == -1) {
        throw new RuntimeException("Should not reach here!");
      } else {
        char c = (char) i;
        switch (c) {
        case '"':
          return sb.toString();
        default:
          sb.append(c);
          break;
        }
      }
    }
  }

  private void formatJsonArray(StringReader sr, int offset,
      StringBuilder sb) throws IOException {
    boolean start = true;
    int local_offset = 0;
    while (true) {
      int i = sr.read();
      if (i == -1) {
        return;
      } else {
        char c = (char) i;
        switch (c) {
        case '{':
          if (!start) {
            sb.append('\n');
            indent(sb, offset);
            local_offset = 0;
          }
          sb.append("{ ");
          formatJsonValue(sr, offset + local_offset + 2, sb);
          sb.append(" }");
          break;
        case '[':
          sb.append('[');
          formatJsonArray(sr, offset + local_offset + 1, sb);
          sb.append(']');
          break;
        case ']':
          return;
        case '"':
          sb.append('"');
          String string = readJsonString(sr);
          sb.append(string);
          sb.append('"');
          local_offset += string.length() + 2;
          break;
        case ',':
          sb.append(", ");
          local_offset += 2;
          break;
        default:
          sb.append(c);
          local_offset++;
          break;
        }
      }
      start = false;
    }
  }

  private void indent(StringBuilder sb, int level) {
    for (int i = 0; i < level; i++)
      sb.append(' ');
  }

  /**
   * Provides an unsynchronized Stack.
   */
  static class ListStack<E> extends ArrayList<E> {
    private static final long serialVersionUID = -2082824836877291097L;


    /**
     * Creates a new ListStack instance with an initial capacity of 10.
     */
    private ListStack() {
    }

    /**
     * Create a new ListStack with the given initial capacity.
     *
     * @param initialCapacity specify initial capacity of this stack
     */
    private ListStack(int initialCapacity) {
      super(initialCapacity);
    }

    /**
     * Pushes the Object onto the top of this stack.
     *
     * @param value the Object to push onto this stack
     * @return the Object pushed on this stack
     */
    private Object push(E value) {
      add(value);

      return value;
    }

    /**
     * Removes and return the Object at the top of this stack.
     *
     * @return the Object at the top of this stack
     */
    private E pop() {
      E value = peek();

      remove(size() - 1);

      return value;
    }

    /**
     * Looks at the Object at the top of this stack without
     * removing it.
     *
     * @return the Object at the top of this stack
     */
    private E peek() {
      int length = size();

      if (length == 0) {
        return null;
      }

      return get(length - 1);
    }
  }
}