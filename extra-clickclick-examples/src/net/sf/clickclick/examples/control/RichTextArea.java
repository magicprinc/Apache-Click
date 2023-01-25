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
package net.sf.clickclick.examples.control;

import java.util.List;

import org.apache.click.control.TextArea;
import org.apache.click.element.Element;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;
import org.apache.click.util.HtmlStringBuffer;

/**
 * Provides a HTML Rich TextArea editor control using the
 * <a href="http://tinymce.moxiecode.com/">TinyMCE</a>
 * JavaScript library.
 * <p/>
 * To utilize this control in your application include <tt>tiny_mce</tt>
 * JavaScript library in the web apps root directory.
 *
 * @see TextArea
 */
public class RichTextArea extends TextArea {

    private static final long serialVersionUID = 1L;

    public static final String THEME_SIMPLE = "simple";

    public static final String THEME_ADVANCED = "advanced";

    /**
     * The textarea TinyMCE theme [<tt>simple</tt> | <tt>advanced</tt>],
     * default value: &nbsp; <tt>"simple"</tt>
     */
    protected String theme = "simple";

    // ----------------------------------------------------------- Constructors

    /**
     * Create a TinyMCE rich TextArea control with the given name.
     *
     * @param name the name of the control
     */
    public RichTextArea(String name) {
        super(name);
    }

    /**
     * Default no-args constructor used to deploy control resources.
     */
    public RichTextArea() {
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Return the textarea TinyMCE theme.
     *
     * @return the textarea TinyMCE theme
     */
    public String getTheme() {
        return theme;
    }

    /**
     * Set the textarea TinyMCE theme: either {@link #THEME_SIMPLE} or
     * {@link #THEME_ADVANCED}.
     *
     * @param the textarea TinyMCE theme
     */
    public void setTheme(String theme) {
        this.theme = theme;
    }

    /**
     * Return the JavaScript include: &nbsp; <tt>"tiny_mce/tiny_mce.js"</tt>,
     * and TinyMCE JavaScript initialization code.
     *
     * @see org.apache.click.control.Field#getHeadElements()
     */
    @Override
    public List<Element> getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();
            headElements.add(new JsImport("/tiny_mce/tiny_mce.js"));
        }

        JsScript script = new JsScript();
        script.setId(getId() + "_js_setup");

        if (!headElements.contains(script)) {
            if (THEME_ADVANCED.equals(getTheme())) {
                script.setTemplate("/tiny_mce/template.js");
            } else {
                HtmlStringBuffer buffer = new HtmlStringBuffer();
                buffer.append("tinyMCE.init({theme : '").append(getTheme()).append("',");
                buffer.append("mode : 'exact', elements : '").append(getId()).append("'})");
                script.setContent(buffer.toString());
            }

            headElements.add(script);
        }
        return headElements;
    }
}
