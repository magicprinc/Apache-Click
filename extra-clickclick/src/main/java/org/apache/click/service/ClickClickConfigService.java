package org.apache.click.service;

import org.apache.click.Page;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides a ConfigService that does not cache Page metadata
 * and enables hot reloading of Page classes.
 */
public class ClickClickConfigService extends XmlConfigService {

  private static final Object PAGE_LOAD_LOCK = new Object();


  private final Map manualPageByPathMap = new HashMap<>();

  private final Map manualPageByClassNameMap = new HashMap<>();

  // --------------------------------------------------------- Public Methods

  /**
   * Return the list of page packages.
   *
   * @return the list of page packages.
   */
  public List getPagesPackage() {
    return pagePackages;
  }

  /**
   * @see org.apache.click.service.ConfigService#getPageClass(java.lang.String)
   *
   * @param path the page path
   * @return the page class for the given path or null if no class is found
   */
  public Class<? extends Page> getPageClass(String path) {

    // If in production or profile mode.
    if (isProductionMode() || isProfileMode()) {
      return super.getPageClass(path);

    } else {
      // Else in development, debug or trace mode
      synchronized (PAGE_LOAD_LOCK) {

        // Try and load the manually mapped page first
        PageMetaData page = lookupManuallyStoredMetaData(path);

        if (page != null) {
          try {
            return ClickUtils.classForName(page.getPageClassName());
          } catch (ClassNotFoundException ex) {
            // ignore, this class is not available, so try and load
            // it from the classpath
          }
        }

        Class<? extends Page> pageClass = null;

        try {
          URL resource = getServletContext().getResource(path);

          if (resource != null) {
            for (Object pagePackage : pagePackages){
              String pagesPackage = pagePackage.toString();

              pageClass = getPageClass(path, pagesPackage);
            }
          } else {
            // No caching of this class or fields are done here.
          }

        } catch (MalformedURLException ex) {
          // ignore, will return null
        }

        return pageClass;
      }
    }
  }

  /**
   * @see org.apache.click.service.ConfigService#getPagePath(java.lang.Class)
   *
   * @param pageClass the page class
   * @return path the page path or null if no path is found
   * @throws IllegalArgumentException if the Page Class is not configured
   * with a unique path
   */
  public String getPagePath(Class pageClass) {
    // Try to lookup path from manually mapped pages first
    PageMetaData page = lookupManuallyStoredMetaData(pageClass);
    if (page != null) {
      return page.getPath();
    }

    // If not found we do a reverse algorithm lookup for the path
    return new PathLookupAlgorithm().getPagePath(pageClass);
  }

  /**
   * @see org.apache.click.service.ConfigService#getPageHeaders(java.lang.String)
   *
   * @param path the path of the page
   * @return a Map of headers for the given page path
   */
  public Map getPageHeaders(String path) {
    // Try and load the manually mapped page first
    PageMetaData page = lookupManuallyStoredMetaData(path);

    if (page != null) {
      return page.getHeaders();
    } else {
      // If path was not found in the manually loaded pages, return common headers
      return Collections.unmodifiableMap(commonHeaders);
    }
  }

  /**
   * @see org.apache.click.service.ConfigService#getPageField(java.lang.Class, java.lang.String)
   *
   * @param pageClass the page class
   * @param fieldName the name of the field
   * @return the public field of the pageClass with the given name or null
   */
  public Field getPageField(Class pageClass, String fieldName) {
    if (isProductionMode() || isProfileMode()) {
      return super.getPageField(pageClass, fieldName);
    }
    return (Field) getPageFields(pageClass).get(fieldName);
  }

  /**
   * @see org.apache.click.service.ConfigService#getPageFieldArray(java.lang.Class)
   *
   * @param pageClass the page class
   * @return an array public fields for the given page class
   */
  public Field[] getPageFieldArray(Class pageClass) {
    if (isProductionMode() || isProfileMode()) {
      return super.getPageFieldArray(pageClass);
    }
    return pageClass.getFields();
  }

  /**
   * @see org.apache.click.service.ConfigService#getPageFields(java.lang.Class)
   *
   * @param pageClass the page class
   * @return a Map of public fields for the given page class
   */
  public Map getPageFields(Class pageClass) {
    if (isProductionMode() || isProfileMode()) {
      return super.getPageFields(pageClass);
    }
    Field[] fieldArray = getPageFieldArray(pageClass);
    Map<String,Field> fields = new HashMap<>();
    for (Field field : fieldArray){
      fields.put(field.getName(), field);
    }
    return fields;
  }

  // ------------------------------------------------ Package Private Methods

  /**
   * In Production modes delegate to the super implementation. In development
   * modes add manually defined Pages to the {@link #manualPageByPathMap}.
   *
   * @param pagesElm the xml element containing manually defined Pages
   * @param pagesPackage the pages package prefix
   *
   * @throws java.lang.ClassNotFoundException if the specified Page class can
   * not be found on the classpath
   */
  void buildManualPageMapping(Element pagesElm, String pagesPackage) throws ClassNotFoundException {
    if (isProductionMode() || isProfileMode()) {
      super.buildManualPageMapping(pagesElm, pagesPackage);
      return;
    }

    List pageList = ClickUtils.getChildren(pagesElm, "page");

    if (!pageList.isEmpty() && getLogService().isDebugEnabled()) {
      getLogService().debug("click.xml pages:");
    }

    for (Object o : pageList){
      Element pageElm = (Element) o;

      PageMetaData page = new PageMetaData(pageElm, pagesPackage, commonHeaders);
      manualPageByPathMap.put(page.getPath(), page);
    }
  }

  /**
   * In Production modes delegate to the super implementation. In development
   * modes this method does <b>not</b> associate template files
   * with matching Java classes found on the classpath.
   * <p/>
   * This method also rebuilds the {@link #excludesList}. This list contains
   * URL paths that should not be auto-mapped.
   *
   * @param pagesElm the xml element containing the excluded URL paths
   * @param pagesPackage the pages package prefix
   * @param templates the list of templates to map to Page classes
   */
  void buildAutoPageMapping(Element pagesElm, String pagesPackage, List templates) throws ClassNotFoundException {

    if(isProductionMode() || isProfileMode()) {
      //Build and cache in production modes.
      super.buildAutoPageMapping(pagesElm, pagesPackage, templates);
      return;
    }

    // Build list of automap path page class overrides
    excludesList.clear();
    for (Element element : ClickUtils.getChildren(pagesElm, "excludes")){
      excludesList.add(new ExcludesElm(element));
    }
  }

  /**
   * In Production modes delegate to the super implementation. In development
   * modes this method builds the {@link #manualPageByClassNameMap} by using
   * the Page Class name instead of the Page Class. Thus no reference to the
   * class is stored and it can be reloaded. Further only manually mapped
   * pages will be stored by this method as automapped pages are looked up
   * dynamically.
   */
  void buildClassMap() {
    if (isProductionMode() || isProfileMode()) {
      super.buildClassMap();
      return;
    }

    // Build pages by className map
    for (Object o : pageByPathMap.values()){
      PageMetaData page = (PageMetaData) o;

      Object value = manualPageByClassNameMap.get(page.pageClassName);

      if (value == null){
        manualPageByClassNameMap.put(page.pageClassName, page);
      } else if (value instanceof List){
        ( (List) value ).add(value);
      } else if (value instanceof PageMetaData){
        List list = new ArrayList<>();
        list.add(value);
        list.add(page);
        manualPageByClassNameMap.put(page.pageClassName, list);
      } else {
        // should never occur
        throw new IllegalStateException();
      }
    }
  }

  /**
   * Return the {@link PageMetaData} associated with the given path
   *
   * @param path the page path
   * @return the PageMetaData object for the given path
   */
  private PageMetaData lookupManuallyStoredMetaData(String path) {
    //Try and load the manually mapped page
    PageMetaData page = (PageMetaData) manualPageByPathMap.get(path);
    if (page == null) {
      String jspPath = StringUtils.replace(path, ".htm", ".jsp");
      page = (PageMetaData) manualPageByPathMap.get(jspPath);
    }
    return page;
  }

  /**
   * Return the {@link PageMetaData} associated with the given page class.
   *
   * @param pageClass the page class
   * @return the PageMetaData object for the given page class
   */
  private PageMetaData lookupManuallyStoredMetaData(Class pageClass) {
    //Try and load the manually mapped page
    PageMetaData page = null;
    Object object = manualPageByClassNameMap.get(pageClass);
    if (object instanceof PageMetaData) {
      page = (PageMetaData) object;
      return page;

    } else if (object instanceof List) {
      String msg =
          "Page class resolves to multiple paths: " + pageClass.getName();
      throw new IllegalArgumentException(msg);

    }

    return page;
  }

  // ---------------------------------------------------------- Inner Classes

  /**
   * Encapsulate a Page metadata such as headers, classname and path.
   */
  static class PageMetaData {

    final Map headers;

    final String pageClassName;

    final String path;

    public PageMetaData(Element element, String pagesPackage, Map commonHeaders) throws ClassNotFoundException {

      // Set headers
      Map aggregationMap = new HashMap(commonHeaders);
      Map pageHeaders = loadHeadersMap(element);
      aggregationMap.putAll(pageHeaders);
      headers = Collections.unmodifiableMap(aggregationMap);

      // Set path
      String pathValue = element.getAttribute("path");
      if (pathValue.charAt(0) != '/') {
        path = "/" + pathValue;
      } else {
        path = pathValue;
      }

      // Set pageClass
      String value = element.getAttribute("classname");
      if (value != null) {
        if (pagesPackage.trim().length() > 0) {
          value = pagesPackage + "." + value;
        }
      } else {
        String msg = "No classname defined for page path " + path;
        throw new RuntimeException(msg);
      }

      pageClassName = value;
    }

    public Map getHeaders() {
      return headers;
    }

    public String getPageClassName() {
      return pageClassName;
    }

    public String getPath() {
      return path;
    }
  }

  /**
   * Encapsulates the algorithm used to lookup the Page path from a given
   * Page class.
   */
  class PathLookupAlgorithm {

    public String getPagePath(Class pageClass) {
      return constructPathFromClass(pageClass);
    }

    public String constructPathFromClass(Class pageClass) {
      String className = ClassUtils.getShortClassName(pageClass);
      String pageDir = calcPageDir(pageClass);
      String result = null;

      result = constructPathFromClass(pageDir, className);

      if (result != null) {
        return result;
      }

      //Not found? Try with/without 'Page'
      if (className.endsWith("Page")) {
        //Chop off the 'Page' string and try again
        String noPageClassName = className.substring(0, className.lastIndexOf("Page"));
        result = constructPathFromClass(pageDir, noPageClassName);
      } else {
        //Append the 'Page' string and try again
        String pageClassName = className + "Page";
        result = constructPathFromClass(pageDir, pageClassName);
      }

      return result;
    }

    /**
     * This method strips the pagesPackage from the class package.
     * Thus if clickApp.pagesPackage is 'com.mycorp', then the packageName
     * 'com.mycorp.contacts' becomes 'contacts'
     */
    public String calcPageDir(Class clazz) {
      int indexOfClassName = clazz.getName().lastIndexOf('.');
      String pageDir = "/";

      // If the clazz does not have a package return the root path
      if (indexOfClassName < 0) {
        return pageDir;
      }

      // Note the addition of the '.' after the package name below. This
      // ensures to check for a legal package instead of a false positive
      // like 'com.mycorp.con' which would also have qualified if the
      // package name was 'com.mycorp.contacts'.

      // The '+ 1' in the substring argument ensures the packageName
      // ends with '.' ie 'com.mycorp.'
      final String packageName = clazz.getName().substring(0,
          indexOfClassName + 1);

      // If the pagePackages is not specified return the converted packageName
      if (getPagePackages().isEmpty()) {
        return convertToAbsoluteDir(packageName);
      }

      boolean matchFound = false;

      for (int i = 0; i < getPagePackages().size(); i++) {
        String pagesPackage = pagePackages.get(i).toString();

        // Also append a '.' at the end of the pagesPackage
        // final String pagesPackage = getPagesPackage() + ".";

        // Check that pagesPackage is a substring of packageName
        if (packageName.startsWith(pagesPackage)) {

          matchFound = true;

          // Check that the pagesPackage and packageName is not equal
          if (packageName.length() != pagesPackage.length()) {

            // Subtract the pagesPackage from the specified class package
            pageDir = packageName.substring(pagesPackage.length() +
                1);
            break;
          }
        }
      }

      // If page directory was matched, return the pageDir
      if (matchFound) {
        return convertToAbsoluteDir(pageDir);
      } else {
        // If the page directory was not matched, return the packageName
        // as the path
        return convertToAbsoluteDir(packageName);
      }
    }

    public List getPagePackages() {
      return pagePackages;
    }

    /**
     * Prefix the name with a '/' and change any '.' to '/'
     */
    public String convertToAbsoluteDir(String packageName) {
      packageName = ensurePathStartsWithSlash(packageName);
      return packageName.replace('.', '/');
    }

    protected String constructPathFromClass(String pageDir,
        String className) {
      String result = _constructPathFromClass(pageDir, className + ".htm");
      if (result == null) {
        result = _constructPathFromClass(pageDir, className + ".jsp");
      }
      return result;
    }

    private String _constructPathFromClass(String pageDir, String className) {
      pageDir = ensurePathStartsWithSlash(pageDir);

      //The 'path from class' lookup strategy is as follows
      //1. do not change the classname, just do lookup
      String path = pageDir + className;
      URL resource = tryAndFindEntryForPath(path);
      if (resource != null) {
        return path;
      }

      //2. lower case the first character
      String lowercase = Character.toString(Character.toLowerCase(className.charAt(0)));
      path = pageDir + lowercase + className.substring(1);
      resource = tryAndFindEntryForPath(path);
      if (resource != null) {
        return path;
      }

      //3. normalize camel case class to path tokenized on '-'
      path = pageDir + camelCaseToPath(className, "-");
      resource = tryAndFindEntryForPath(path);
      if (resource != null) {
        return path;
      }

      //3. normalize camel case class to path tokenized on '-'
      path = pageDir + camelCaseToPath(className, "_");
      resource = tryAndFindEntryForPath(path);
      if (resource != null) {
        return path;
      }
      return null;
    }

    protected String camelCaseToPath(String camelString, String token) {
      HtmlStringBuffer buffer = new HtmlStringBuffer();
      char[] chars = camelString.toCharArray();
      int length = chars.length;

      //Append first char
      buffer.append(Character.toLowerCase(chars[0]));

      for (int i = 1; i < length; i++) {
        if (Character.isUpperCase(chars[i])) {
          buffer.append(token);
          buffer.append(Character.toLowerCase(chars[i]));
        } else {
          buffer.append(chars[i]);
        }
      }
      return buffer.toString();
    }

    protected URL tryAndFindEntryForPath(String path) {
      try {
        //path = ensurePathStartsWithSlash(path);
        URL resource = getServletContext().getResource(path);
        return resource;
      } catch (Exception ex) {
      }
      return null;
    }

    protected String ensurePathStartsWithSlash(String path) {
      if (StringUtils.isBlank(path)) {
        return "/";
      }
      if (path.charAt(0) != '/') {
        return '/' + path;
      } else {
        return path;
      }
    }
  }
}