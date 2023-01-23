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
package net.sf.click.jquery.examples.control;

import java.util.List;
import net.sf.click.jquery.examples.control.panel.SimplePanel;
import org.apache.click.element.CssImport;
import org.apache.click.element.Element;
import org.apache.click.util.HtmlStringBuffer;

/**
 * Provides a Window control with a titlebar and content.
 */
public class Window extends SimplePanel {

    // -------------------------------------------------------------- Variables

    private int width;

    private int height;

    private int titlebarHeight;

   // ----------------------------------------------------------- Constructors

    public Window() {
    }

    public Window(String name) {
        super(name);
    }

    // ------------------------------------------------------ Public Attributes

    /**
     * Return the width CSS style.
     *
     * @return the width CSS style
     */
    public int getWidth() {
        return width;
    }

    /**
     * Set the width CSS style.
     *
     * @param width the CSS width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return the titlebarHeight
     */
    public int getTitlebarHeight() {
        return titlebarHeight;
    }

    /**
     * @param titlebarHeight the titlebarHeight to set
     */
    public void setTitlebarHeight(int titlebarHeight) {
        this.titlebarHeight = titlebarHeight;
    }

    // --------------------------------------------------------- Public Methods

    @Override
    public List<Element> getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();
            headElements.add(new CssImport("/click-jquery/example/window/window.css"));
        }
        return headElements;
    }

    @Override
    public void render(HtmlStringBuffer buffer) {
        int titlebarHeight = getTitlebarHeight();
        if (titlebarHeight <= 0) {
            titlebarHeight = 18; // default
        }

        HtmlStringBuffer windowStyle = new HtmlStringBuffer();
        HtmlStringBuffer titlebarStyle = new HtmlStringBuffer();
        HtmlStringBuffer contentStyle = new HtmlStringBuffer();

        String styles = getAttribute("style");
        if (styles != null) {
            windowStyle.append(styles);
        }
        if (getWidth() != 0) {
            if (windowStyle.length() > 0) {
                windowStyle.append("; ");
            }
            windowStyle.append("width:").append(getWidth()).append("px");
            titlebarStyle.append("width:").append(getWidth() - 10).append("px");
            contentStyle.append("width:").append(getWidth() - 10).append("px");
        }
        if (getHeight() != 0) {
            if (windowStyle.length() > 0) {
                windowStyle.append("; ");
                titlebarStyle.append("; ");
                contentStyle.append("; ");
            }
            windowStyle.append("height:").append(getHeight()).append("px");
            titlebarStyle.append("height:").append(titlebarHeight).append("px");
            int contentHeight = getHeight() - (titlebarHeight + 17);
            contentStyle.append("height:").append(contentHeight).append("px");
        }
        setAttribute("style", windowStyle.toString());

        String id = getId();
        buffer.elementStart("div");
        buffer.appendAttribute("name", getName());
        buffer.appendAttribute("id", id);
        buffer.appendAttribute("class", "window");
        appendAttributes(buffer);
        buffer.closeTag();

        buffer.elementStart("div");
        if (id != null) {
            buffer.appendAttribute("id", id + "-titlebar");
        }
        buffer.appendAttribute("class", "titlebar");
        buffer.appendAttribute("style", titlebarStyle);
        buffer.closeTag();
        buffer.append(getLabel());
        buffer.elementEnd("div");

        buffer.elementStart("div");
        if (id != null) {
            buffer.appendAttribute("id", id + "-content");
        }
        buffer.appendAttribute("class", "content");
        buffer.appendAttribute("style", contentStyle);
        buffer.closeTag();
        super.render(buffer);
        buffer.elementEnd("div");

        buffer.elementEnd("div");
    }
}
