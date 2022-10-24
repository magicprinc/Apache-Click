/*
 * Licensed  under the  Apache License,  Version 2.0  (the "License");
 * you may not use  this file  except in  compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under the  License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.clickclick.reload;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.click.service.ConfigService;

/**
 * ClassLoader which enables specified classes to be reloaded without restarting
 * the web application.
 * <p/>
 * It does this by reversing the lookup order for classes.  First it checks for
 * classes locally before checking its parent loader.
 * <p/>
 * In addition it can be configured to include and exclude specific classes
 * and packages.
 * <p/>
 * <b>NOTE:</b> This class was adapted and modified from the Apache Cocoon<br>
 * implementation https://svn.apache.org/repos/asf/cocoon/tags/cocoon-2.2/cocoon-bootstrap/cocoon-bootstrap-1.0.0-M1/src/main/java/org/apache/cocoon/classloader/DefaultClassLoader.java
 * <p>
 * Articles of interest :
 * </p>
 * <ul>
 *      <li>http://tech.puredanger.com/2006/11/09/classloader/</li>
 *      <li>http://www.javaworld.com/javaworld/javaqa/2003-06/01-qa-0606-load.html</li>
 *      <li>http://www.javalobby.org/java/forums/t18345.html</li>
 * </ul>
 */
public class ReloadClassLoader extends URLClassLoader {

    // -------------------------------------------------------------- Variables

    /** The list of classes and folders to be reloaded. */
    private List includes = new ArrayList();

    /** The list of classes and folders to be excluded from reloading. */
    private List excludes = new ArrayList();

    /** The Click ConfigService. */
    private ConfigService configService;

    // ----------------------------------------------------------- Constructors

    /**
     * Creates a new ReloadClassLoader instance for the given URLs, parent
     * ClassLoader and ConfigService.
     *
     * @param classpath the classpath as an array of URLs
     * @param parent the parent ClassLoader
     * @param configService the Click ConfigService
     */
    public ReloadClassLoader(URL[] classpath, ClassLoader parent,
        ConfigService configService) {
        super(classpath, parent);
        this.configService = configService;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Add a class or folder to be reloaded by this ClassLoader.
     *
     * @param include a class or folder to be reloaded by this ClassLoader
     */
    public void addInclude(String include) {
        includes.add(include);
    }

    /**
     * Add a class or folder to be excluded from reloading by this ClassLoader.
     *
     * @param exclude a class or folder to be excluded from reloading by this
     * ClassLoader
     */
    public void addExclude(String exclude) {
        excludes.add(exclude);
    }

    /**
     * Promote this method to public.
     *
     * @param url the URL to be added to the search path of URLs
     */
    public void addURL(URL url) {
        super.addURL(url);
    }

    /**
     * Finds a resource with the given name. If the resource is not found in
     * this ClassLoader the parent ClassLoader will be checked.
     *
     * @param  name the resource name
     * @return a URL object for reading the resource, or null if no resource
     * could not be found or the invoker doesn't have adequate privileges to
     * get the resource.
     */
    public final URL getResource(String name) {
        //Try to find resource locally
        URL resource = findResource(name);

        if (resource == null) {
            //If not found try parent
            resource = getParent().getResource(name);
        }
        return resource;
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Return true if the given class should be loaded by this ClassLoader or
     * not.
     *
     * @param name the class to reload
     * @return true if the class should be loaded, false otherwise
     */
    protected boolean shouldLoadClass(String name) {
        if(name == null || name.length() == 0) {
            return false;
        }

        // Automatically exclude these common classes
        if (name.startsWith("java.") || name.startsWith("javax.servlet")) {
            return false;
        }

        // First check if class is excluded
        for(Iterator it = excludes.iterator(); it.hasNext(); ) {
            String packageName = (String) it.next();
            if(name.startsWith(packageName)) {
                return false;
            }
        }

        // Next check if class is included
        for(Iterator it = includes.iterator(); it.hasNext(); ) {
            String packageName = (String) it.next();
            if(name.startsWith(packageName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Loads the class with the given name. First try and load the class through
     * this ClassLoader before loading it through the parent ClassLoader.
     * <p/>
     * Only classes which are listed in the {@link #includes} list will be
     * loaded.
     * <p/>
     * This method delegates to {@link #shouldLoadClass(java.lang.String)}
     * to check if a class should be loaded.
     *
     * @param  name the binary name of the class
     * @param  resolve if true then resolve the class
     * @return the resulting Class object
     *
     * @throws  ClassNotFoundException if the class could not be found
     */
    protected Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
        //First, check if the class has already been loaded
        Class c = findLoadedClass(name);

        if (c == null) {

            //If not loaded yet, check if this class's package is included in the list
            //of allowed packages
            if(shouldLoadClass(name)) {
                try {
                    c = findClass(name);
                    configService.getLogService().trace("   Reloaded class '"
                        + name + "'");
                } catch (ClassNotFoundException ex) {
                    if(getParent() == null) {
                        throw ex;
                    }
                }
            }

            if(c == null) {

                if(getParent() == null) {
                    throw new ClassNotFoundException(name);
                } else {

                    //The class was not loaded so delegate to parent class loader
                    c = getParent().loadClass(name);
                }
            }
        }
        
        if (resolve) {
            resolveClass(c);
        }
        return c;
    }
}
