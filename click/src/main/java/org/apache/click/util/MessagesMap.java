/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.click.util;

import lombok.NonNull;
import org.apache.click.Context;
import org.apache.click.service.ConfigService;
import org.apache.commons.lang.Validate;

import javax.servlet.ServletContext;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
 */
public class MessagesMap implements Map<String, String> {

    /** Cache of resource bundle and locales which were not found, with support for multiple class loaders. */
    private static final ConcurrentHashMap<ClassLoader, Set<String>> NOT_FOUND_CLASSLOADER_CACHE
        = new ConcurrentHashMap<>();

    /** Provides a synchronized cache of get value reflection methods, with support for multiple class loaders. */
    protected static final ConcurrentMap<ClassLoader, ConcurrentMap<CacheKey, Map<String,String>>> MESSAGES_CLASSLOADER_CACHE
        = new ConcurrentHashMap<>();

    /** The cache key set load lock. */
    protected static final Object[] CACHE_LOAD_LOCK = new Object[0];

    // ----------------------------------------------------- Instance Variables

    /** The base class. */
    protected final Class<?> baseClass;

    /** The class global resource bundle base name. */
    protected final String globalBaseName;

    /** The map of localized messages. */
    protected Map<String, String> messages;

    /** The resource bundle locale. */
    protected final Locale locale;

    // ----------------------------------------------------------- Constructors

    /**
     * Create a resource bundle messages <tt>Map</tt> adaptor for the given
     * object's class resource bundle, the global resource bundle and
     * <tt>Context</tt>.
     * <p/>
     * Messages located in the object's resource bundle will override any
     * messages defined in the global resource bundle.
     *
     * @param baseClass the target class
     * @param globalResource the global resource bundle name
     */
    public MessagesMap(Class<?> baseClass, String globalResource) {
        this(baseClass, globalResource, Context.getThreadLocalContext().getLocale());
    }

    /**
     * Create a resource bundle messages <tt>Map</tt> adaptor for the given
     * object's class resource bundle, the global resource bundle and
     * <tt>Context</tt>.
     * <p/>
     * Messages located in the object's resource bundle will override any
     * messages defined in the global resource bundle.
     *
     * @param baseClass the target class
     * @param globalResource the global resource bundle name
     * @param locale the resource bundle locale.
     */
    public MessagesMap(Class<?> baseClass, String globalResource, Locale locale) {
        Validate.notNull(baseClass, "Null object parameter");

        this.baseClass = baseClass;
        this.globalBaseName = globalResource;
        this.locale = locale;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * @see java.util.Map#size()
     */
    public int size() {
        ensureInitialized();
        return messages.size();
    }

    /**
     * @see java.util.Map#isEmpty()
     */
    public boolean isEmpty() {
        ensureInitialized();
        return messages.isEmpty();
    }

    /**
     * @see java.util.Map#containsKey(Object)
     */
    public boolean containsKey(Object key) {
        if (key != null) {
            ensureInitialized();
            return messages.containsKey(key.toString());
        }
        return false;
    }

    /**
     * @see java.util.Map#containsValue(Object)
     */
    public boolean containsValue(Object value) {
        ensureInitialized();
        return messages.containsValue(value);
    }

    /**
     * Return localized resource message for the given key. If the message is
     * not found a <tt>MissingResourceException</tt> will be thrown.
     *
     * @see java.util.Map#get(Object)
     * @throws MissingResourceException if the given key was not found
     */
    public String get(Object key) {
        String value = null;
        if (key != null) {
            ensureInitialized();
            value = messages.get(key.toString());
        }

        if (value == null) {
            String msg = "Message \"{0}\" not found in bundle \"{1}\" for locale \"{2}\"";
            String keyStr = (key != null) ? key.toString() : null;
            Object[] args = { keyStr, baseClass.getName(), locale };
            msg = MessageFormat.format(msg, args);
            throw new MissingResourceException(msg, baseClass.getName(), keyStr);
        }
        return value;
    }

    /**
     * This method is not supported and will throw
     * <tt>UnsupportedOperationException</tt> if invoked.
     *
     * @see java.util.Map#put(Object, Object)
     */
    public String put(String key, String value) {
        throw new UnsupportedOperationException();
    }

    /**
     * This method is not supported and will throw
     * <tt>UnsupportedOperationException</tt> if invoked.
     *
     * @see java.util.Map#remove(Object)
     */
    public String remove(Object key) {
        throw new UnsupportedOperationException();
    }

    /**
     * This method is not supported and will throw
     * <tt>UnsupportedOperationException</tt> if invoked.
     *
     * @see java.util.Map#putAll(Map)
     */
    public void putAll(Map<? extends String, ? extends String> map) {
        throw new UnsupportedOperationException();
    }

    /**
     * This method is not supported and will throw
     * <tt>UnsupportedOperationException</tt> if invoked.
     *
     * @see java.util.Map#clear()
     */
    public void clear() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see java.util.Map#keySet()
     */
    public Set<String> keySet() {
        ensureInitialized();
        return messages.keySet();
    }

    /**
     * @see java.util.Map#values()
     */
    public Collection<String> values() {
        ensureInitialized();
        return messages.values();
    }

    /**
     * @see java.util.Map#entrySet()
     */
    public Set<Map.Entry<String, String>> entrySet() {
        ensureInitialized();
        return messages.entrySet();
    }

    /**
     * @see #toString()
     */
    @Override
    public String toString() {
        ensureInitialized();
        return messages.toString();
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Return the ResourceBundle for the given resource name and locale. By
     * default this method will create a ResourceBundle using the standard JDK
     * method: {@link java.util.ResourceBundle#getBundle(java.lang.String, java.util.Locale, java.lang.ClassLoader)}.
     * <p/>
     * You can create your own custom ResourceBundle by overriding this method.
     * <p/>
     * In order for Click to use your custom MessagesMap implementation, you
     * need to provide your own {@link org.apache.click.service.MessagesMapService}
     * or extend {@link org.apache.click.service.DefaultMessagesMapService}.
     * <p/>
     * The method {@link org.apache.click.service.MessagesMapService#createMessagesMap(java.lang.Class, java.lang.String, java.util.Locale)  createMessagesMap},
     * can be implemented to return your custom MessagesMap instances.
     *
     * @param resourceName the resource bundle name
     * @param locale the resource bundle locale.
     *
     * @return the ResourceBundle for the given resource name and locale
     */
    protected ResourceBundle createResourceBundle(String resourceName, Locale locale) {
        return ClickUtils.getBundle(resourceName, locale);
    }

    /**
     * This method initializes and populates the internal{@link #messages} map
     * and {@link #MESSAGES_CLASSLOADER_CACHE} if it is not already initialized.
     * <p/>
     * <b>Please Note:</b> populating the cache messagesCache is not thread safe
     * and access to the cache must be properly synchronized.
     */
    protected void ensureInitialized() {
        if (messages == null) {

            CacheKey resourceKey = new CacheKey(globalBaseName, baseClass.getName(), locale.toString());

            ConcurrentMap<CacheKey, Map<String, String>> messageCache = ClickUtils.classLoaderCacheGET(MESSAGES_CLASSLOADER_CACHE);//messagesCache→Map<CacheKey, Map<String,String>>
            messages = messageCache.get(resourceKey);

            if (messages != null) {
                return;
            }

            messages = new HashMap<>();

            synchronized (CACHE_LOAD_LOCK) {

                loadResourceValuesIntoMap(globalBaseName, messages);

                List<String> classnameList = new ArrayList<>();

                // Build class list
                Class<?> aClass = baseClass;
                while (!aClass.getName().equals("java.lang.Object")) {
                    classnameList.add(aClass.getName());
                    aClass = aClass.getSuperclass();
                }

                // Load messages from parent to child order, so that child
                // class messages override parent messages.
                for (int i = classnameList.size() - 1; i >= 0; i--) {
                    String className = classnameList.get(i);
                    loadResourceValuesIntoMap(className, messages);
                }

                messages = Collections.unmodifiableMap(messages);

                ServletContext servletContext = Context.getThreadLocalContext().getServletContext();
                ConfigService configService = ClickUtils.getConfigService(servletContext);
                if (configService.isProductionMode() || configService.isProfileMode()) {
                    messageCache.put(resourceKey, messages);
                }
            }
        }
    }

    /**
     * Load the values of the given resourceBundleName into the map.
     *
     * @param resourceBundleName the resource bundle name
     * @param map the map to load resource values into
     */
    protected void loadResourceValuesIntoMap(String resourceBundleName, Map<String, String> map) {
        if (resourceBundleName == null) {
            return;
        }

        String resourceKey = resourceBundleName + locale.toString();

        Set<String> notFoundCache = ClickUtils.classLoaderCacheGET(NOT_FOUND_CLASSLOADER_CACHE, HashSet::new);//notFoundCache→Set<String>

        if (!notFoundCache.contains(resourceKey)) {
            try {
                ResourceBundle resources = createResourceBundle(resourceBundleName, locale);

                Enumeration<String> e = resources.getKeys();
                while (e.hasMoreElements()) {
                    String name = e.nextElement();
                    String value = resources.getString(name);
                    map.put(name, value);
                }
            } catch (MissingResourceException mre) {
                notFoundCache.add(resourceKey);
            }
        }
    }


    /**
     <a href="http://www.javaspecialists.eu/archive/Issue134.html">
     See DRY Performance article by Kirk Pepperdine.
     </a>@param globalBaseName  Global base name to encapsulate in cache key.

     @param baseClass Base class name to encapsulate in cache key.
     @param locale    Locale to encapsulate in cache key.
     */
     private record CacheKey(@NonNull String globalBaseName, @NonNull String baseClass, @NonNull String locale) {
        /**
         @param o the object with which to compare this instance with
         @return true if the specified object is the same as this object
         @see Object#equals(Object)
         */
        @Override
        public boolean equals (Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof CacheKey that)) {
                return false;
            }

            if (!globalBaseName.equals(that.globalBaseName)) {
                return false;
            }

            if (!baseClass.equals(that.baseClass)) {
                return false;
            }

            return locale.equals(that.locale);
        }
    }
}