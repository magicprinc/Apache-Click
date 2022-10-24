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
package net.sf.click.jquery.examples.control.panel;

import java.net.MalformedURLException;
import org.apache.click.Context;
import org.apache.click.control.Panel;
import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;

/**
 * Provides a panel that renders its controls in the order they were added,
 * unless a template is specified in which case the panel will delegate
 * rendering to the template instead.
 * <p/>
 * SimplePanel first checks if the template is available on the servlet context
 * path or the classpath and if a template is not found, the panel controls are
 * rendered in the order they were added.
 */
public class SimplePanel extends Panel {

    // ----------------------------------------------------------- Constructors

    /**
     * Create a new default SimplePanel.
     */
    public SimplePanel() {
    }

    /**
     * Create a new SimplePanel with the specified name.
     *
     * @param name name of the panel
     */
    public SimplePanel(String name) {
        if (name != null) {
            setName(name);
        }
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Render the HTML representation of the SimplePanel. If a template is
     * specified, rendering will be delegated to that template. If no template
     * is specified, the child controls will be rendered in the order they were
     * added.
     *
     * @see org.apache.click.control.Panel#render(org.apache.click.util.HtmlStringBuffer)
     *
     * @param buffer the specified buffer to render the Panel's output to
     */
    public void render(HtmlStringBuffer buffer) {

        if (getTemplate() != null) {
            super.render(buffer);

        } else {
            boolean hasTemplate = hasTemplate();
            if (hasTemplate) {
                super.render(buffer);
            } else {
                renderControls(buffer);
            }
        }
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Return true if the panel template is available, false otherwise.
     *
     * @return true if the panel template is available, false otherwise
     */
    protected boolean hasTemplate() {
        boolean hasTemplate = false;
        
        if (getTemplate() != null) {
            hasTemplate = true;

        } else {
            String templatePath = getClass().getName();
            templatePath = '/' + templatePath.replace('.', '/') + ".htm";

            try {
                Context context = getContext();
                
                // First check on the servlet context path
                hasTemplate = context.getServletContext().getResource(templatePath) != null;
                if (!hasTemplate) {
                    // Second check on the classpath
                    hasTemplate = ClickUtils.getResource(templatePath, getClass()) != null;
                }

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        return hasTemplate;
    }

    /**
     * Render the panel child controls in the order they were added.
     * 
     * @param buffer to buffer to render output to
     */
    protected void renderControls(HtmlStringBuffer buffer) {
        // If a template cannot be found for the panel, use default container
        // rendering
        if (getTag() != null) {
            renderTagBegin(getTag(), buffer);
            buffer.closeTag();
            if (hasControls()) {
                buffer.append("\n");
            }
            renderContent(buffer);
            renderTagEnd(getTag(), buffer);

        } else {

            //render only content because no tag is specified
            renderContent(buffer);
        }
    }
 }
