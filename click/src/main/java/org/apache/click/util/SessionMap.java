package org.apache.click.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Provides a Map adaptor for HttpSession objects. A SessionMap instance is
 * available in each Velocity page using the name "<span class="blue">session</span>".
 * <p/>
 * For example suppose we have a User object in the session with the
 * attribute name "user" when a user is logged on.  We can display the users
 * name in the page when the are logged onto the system.
 *
 * <pre class="codeHtml">
 * <span class="red">#if</span> (<span class="blue">$session</span>.user)
 *   <span class="blue">$session</span>.user.fullname you are logged on.
 * <span class="red">#else</span>
 *   You are not logged on.
 * <span class="red">#end</span> </pre>
 *
 * The ClickServlet adds a SessionMap instance to the Velocity Context before
 * it is merged with the page template.
 * <p/>
 * The SessionMap supports {@link FlashAttribute} which when accessed via
 * {@link #get(Object)} are removed from the session.
 */
public class SessionMap implements Map<String, Object> {

  /** The internal session attribute. */
  protected HttpSession session;

  /**
   * Create a <tt>HttpSession</tt> <tt>Map</tt> adaptor.
   *
   * @param value the http session
   */
  public SessionMap(HttpSession value) {
    session = value;
  }

  /**
   * Returns the time when this session was created, measured in milliseconds
   * since midnight January 1, 1970 GMT.
   *
   * @see javax.servlet.http.HttpSession#getCreationTime()
   *
   * @return the session creation time, or -1 if no session is available
   */
  public long getCreationTime() {
    if (session != null) {
      return session.getCreationTime();
    } else {
      return -1;
    }
  }

  /**
   * Returns a string containing the unique identifier assigned to this
   * session. The identifier is assigned by the servlet container and is
   * implementation dependent.
   *
   * @see javax.servlet.http.HttpSession#getId()
   *
   * @return the session id, or null if no session is available
   */
  public String getId() {
    if (session != null) {
      return session.getId();
    } else {
      return null;
    }
  }

  /**
   * Returns the last time the client sent a request associated with this
   * session, as the number of milliseconds since midnight January 1, 1970 GMT,
   * and marked by the time the container received the request.
   *
   * @see javax.servlet.http.HttpSession#getLastAccessedTime()
   *
   * @return the session last accessed time, or -1 if no session is available
   */
  public long getLastAccessedTime() {
    if (session != null) {
      return session.getLastAccessedTime();
    } else {
      return -1;
    }
  }

  /**
   * Specifies the time, in seconds, between client requests before the servlet
   * container will invalidate this session. A negative time indicates the
   * session should never timeout.
   *
   * @see javax.servlet.http.HttpSession#getMaxInactiveInterval()
   *
   * @return the session max inactive interval in seconds, or -1 if no session is available
   */
  public int getMaxInactiveInterval()  {
    if (session != null) {
      return session.getMaxInactiveInterval();
    } else {
      return -1;
    }
  }

  /**
   * @see java.util.Map#size()
   */
  @Override public int size() {
    if (session != null) {
      int size = 0;
      Enumeration<?> enumeration = session.getAttributeNames();
      while (enumeration.hasMoreElements()) {
        enumeration.nextElement();
        size++;
      }
      return size;
    } else {
      return 0;
    }
  }

  /**
   * @see java.util.Map#isEmpty()
   */
  @Override public boolean isEmpty() {
    return size() == 0;
  }

  /**
   * @see java.util.Map#containsKey(Object)
   */
  @Override public boolean containsKey(Object key) {
    if (session != null && key != null) {
      return session.getAttribute(key.toString()) != null;
    } else {
      return false;
    }
  }

  /**
   * This method is not supported and will throw
   * <tt>UnsupportedOperationException</tt> if invoked.
   *
   * @see java.util.Map#containsValue(Object)
   */
  @Override public boolean containsValue(Object value) {
    throw new UnsupportedOperationException();
  }

  /**
   * If the stored object is a FlashObject this method will return the
   * FlashObject value and then remove it from the session.
   *
   * @see java.util.Map#get(Object)
   */
  @Override public Object get(Object key) {
    if (session != null && key != null) {
      Object object = session.getAttribute(key.toString());

      if (object instanceof FlashAttribute flashObject) {
        object = flashObject.getValue();
        session.removeAttribute(key.toString());
      }

      return object;

    } else {
      return null;
    }
  }

  /**
   * @see java.util.Map#put(Object, Object)
   */
  @Override public Object put(String key, Object value) {
    if (session != null && key != null) {
      Object out = session.getAttribute(key);

      session.setAttribute(key, value);

      return out;

    } else {
      return null;
    }
  }

  /**
   * @see java.util.Map#remove(Object)
   */
  @Override public Object remove(Object key) {
    if (session != null && key != null) {
      Object out = session.getAttribute(key.toString());
      session.removeAttribute(key.toString());

      return out;

    } else {
      return null;
    }
  }

  /**
   * @see java.util.Map#putAll(Map)
   */
  @Override public void putAll(Map<? extends String, ?> map) {
    if (session != null && map != null) {
      for (Map.Entry<? extends String, ?> entry : map.entrySet()) {
        String key = entry.getKey();
        Object value = entry.getValue();
        session.setAttribute(key, value);
      }
    }
  }

  /**
   * @see java.util.Map#clear()
   */
  @Override public void clear() {
    if (session != null) {
      Enumeration<?> enumeration = session.getAttributeNames();
      while (enumeration.hasMoreElements()) {
        String name = enumeration.nextElement().toString();
        session.removeAttribute(name);
      }
    }
  }

  /**
   * @see java.util.Map#keySet()
   */
  @Override public Set<String> keySet() {
    if (session != null) {
      Set<String> keySet = new HashSet<>();

      Enumeration<?> enumeration = session.getAttributeNames();
      while (enumeration.hasMoreElements()) {
        keySet.add(enumeration.nextElement().toString());
      }

      return keySet;

    } else {
      return Collections.emptySet();
    }
  }

  /**
   * @see java.util.Map#values()
   */
  @Override public Collection<Object> values() {
    if (session != null) {
      List<Object> values = new ArrayList<>();

      Enumeration<?> enumeration = session.getAttributeNames();
      while (enumeration.hasMoreElements()) {
        String name = enumeration.nextElement().toString();
        Object value = session.getAttribute(name);
        values.add(value);
      }

      return values;

    } else {
      return Collections.emptyList();
    }
  }

  /**
   * @see java.util.Map#entrySet()
   */
  @Override public Set<Map.Entry<String, Object>> entrySet() {
    if (session != null) {
      Set<Map.Entry<String, Object>> entrySet = new HashSet<>();

      Enumeration<?> enumeration = session.getAttributeNames();
      while (enumeration.hasMoreElements()) {
        String name = enumeration.nextElement().toString();
        Object value = session.getAttribute(name);
        entrySet.add(new Entry(name, value));
      }

      return entrySet;

    } else {
      return Collections.emptySet();
    }
  }

  @AllArgsConstructor
  static final class Entry implements Map.Entry<String, Object> {

    @Getter final String key;

    @Getter Object value;

    @Override public Object setValue (Object newValue){
      Object oldValue = value;
      value = newValue;
      return oldValue;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override public boolean equals (Object o){
      if (!( o instanceof Entry e )){
        return false;
      }
      return Objects.equals(getKey(), e.getKey()) && Objects.equals(getValue(), e.getValue());
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
      String k = getKey();
      Object v = getValue();

      int hash = 17;
      hash = hash * 37 + (k == null ? 0 : k.hashCode());
      hash = hash * 37 + (v == null ? 0 : v.hashCode());

      return hash;
    }
  }
}