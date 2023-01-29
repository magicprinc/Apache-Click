package org.apache.click.util;

import lombok.NonNull;
import lombok.val;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

/**
 * Provides a localized read only messages Map for Page and Control classes.
 * <p/>
 * A MessagesMap instance is available in each Velocity page using the name
 * "<span class="blue">messages</span>".
 * <p/>
 * For example suppose you have a localized page title, which is stored in the
 * Page's properties file. You can access page "title" message in your page
 * template via:
 *
 * <pre class="codeHtml">
 * <span class="blue">$messages.title</span> </pre>
 *
 * This is roughly equivalent to making the call:
 *
 * <pre class="codeJava">
 * <span class="kw">public void</span> onInit() {
 *    ..
 *    addModel(<span class="st">"title"</span>, getMessage(<span class="st">"title"</span>);
 * } </pre>
 *
 * Please note if the specified message does not exist in your Page's
 * properties file, or if the Page does not have a properties file, then
 * a <tt>MissingResourceException</tt> will be thrown.
 * <p/>
 * The ClickServlet adds a MessagesMap instance to the Velocity Context before
 * it is merged with the page template.
 * @see MessageFormat
 */
public class MessagesMap implements Map<String, String> {

  private final String bundle;
  /** The map of localized messages. */
  private final Map<String, String> messages;

  /**
   * Create a resource bundle messages <tt>Map</tt> adaptor for the given
   * object's class resource bundle, the global resource bundle and
   * <tt>Context</tt>.
   * <p/>
   * Messages located in the object's resource bundle will override any
   * messages defined in the global resource bundle.
   *
   * @param baseClass the target class
   * @param globalResource The class global resource bundle base name.
   * @param locale The resource bundle locale (detected if null).
   */
  public MessagesMap (String bundle, Map<String,String> messages){
    this.bundle = bundle;  this.messages = messages;
  }//new


  @Override public int size (){ return messages.size();}

  @Override public boolean isEmpty() {
    return messages.isEmpty();
  }

  @Override public boolean containsKey (Object key){
    if (key != null){
      return messages.containsKey(key.toString());
    }
    return false;
  }

  @Override public boolean containsValue(Object value){
    return messages.containsValue(value);
  }

  /**
   * Return a localized resource message for the given key. If the message is
   * not found a <tt>MissingResourceException</tt> will be thrown.
   *
   * @see java.util.Map#get(Object)
   * @throws MissingResourceException if the given key was not found
   */
  @Override public String get (@NonNull Object key){
    String value = messages.get(key.toString());

    if (value == null){
      //val msg = MessageFormat.format("Message \"{0}\" not found in bundle \"{1}\" for locale \"{2}\"", key, baseClass.getName(), locale);
      val msg = "Message key "+key+" not found in bundle "+bundle;
      int i = bundle.indexOf('|');
      val className = i>0? bundle.substring(0,i) : "unknown!";
      throw new MissingResourceException(msg, bundle, key.toString());
    }
    return value;
  }

  /**
   * This method is not supported and will throw
   * <tt>UnsupportedOperationException</tt> if invoked.
   *
   * @see java.util.Map#put(Object, Object)
   */
  @Override public String put(String key, String value) {
    throw new UnsupportedOperationException();
  }

  /**
   * This method is not supported and will throw
   * <tt>UnsupportedOperationException</tt> if invoked.
   *
   * @see java.util.Map#remove(Object)
   */
  @Override public String remove(Object key) {
    throw new UnsupportedOperationException();
  }

  /**
   * This method is not supported and will throw
   * <tt>UnsupportedOperationException</tt> if invoked.
   *
   * @see java.util.Map#putAll(Map)
   */
  @Override public void putAll (@NonNull Map<? extends String, ? extends String> map) {
    throw new UnsupportedOperationException();
  }

  /**
   * This method is not supported and will throw
   * <tt>UnsupportedOperationException</tt> if invoked.
   *
   * @see java.util.Map#clear()
   */
  @Override public void clear() {
    throw new UnsupportedOperationException();
  }

  @Override public Set<String> keySet() {
    return messages.keySet();
  }

  @Override public Collection<String> values() {
    return messages.values();
  }

  @Override public Set<Map.Entry<String, String>> entrySet() {
    return messages.entrySet();
  }

  @Override public String toString() {
    return messages.toString();
  }
}