/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.click.jquery.examples.util;

/**
 * Utility class for JQuery IU.
 * <p/>
 * All JQuery UI components are styled through {@link #style}. If you want to
 * change the look of JQuery UI components, set the value of {@link #style}
 * to a different style.
 */
public class JQUILibrary {

    // -------------------------------------------------------------- Constants

    /** The JQuery UI style, default value is "<tt>smoothness</tt>". */
    public static String style = "smoothness";

    // --------------------------------------------------------- Static Methods

    /**
     * Return the JQuery UI CSS:
     * "<tt>/click-jquery/example/ui/smoothness/jquery-ui-1.7.1.custom.css</tt>".
     */
    public static String jqueryUICssPath = "/click-jquery/example/ui/" + style + "/jquery-ui-1.7.1.custom.css";

    /**
     * Return the JQuery UI JavaScript:
     * "<tt>/click-jquery/example/ui/jquery-ui-1.7.1.custom.min.js</tt>".
     */
    public static String jqueryUIJsPath = "/click-jquery/example/ui/jquery-ui-1.7.1.custom.min.js";
}
