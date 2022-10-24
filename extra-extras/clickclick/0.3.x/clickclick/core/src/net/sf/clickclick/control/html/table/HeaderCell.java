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
package net.sf.clickclick.control.html.table;

import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang.StringUtils;

/**
 * Provide a table header cell control: <tt>&lt;th&gt;</tt>.
 */
public class HeaderCell extends Cell {

    // -------------------------------------------------------------- Variables

    private String headerTitle;

    // ------------------------------------------------------------ Constructor

    /**
     * Create a default table HeaderCell.
     */
    public HeaderCell() {
    }

    /**
     * Create a table HeaderCell for the given name.
     *
     * @param name the name of the table HeaderCell
     */
    public HeaderCell(String name) {
        super(name);
    }

    // ------------------------------------------------------ Public Properties

    /**
     * Return the table HeaderCell title value.
     * <p/>
     * If the header title value is null, this method will attempt to find a
     * localized label message in the parent messages using the key:
     * <blockquote>
     * <tt>getName() + ".headerTitle"</tt>
     * </blockquote>
     * If not found then the message will be looked up in the
     * <tt>/click-control.properties</tt> file using the same key.
     * If a value is still not found, the HeaderCell name will be converted
     * into a label using the method: {@link ClickUtils#toLabel(String)}
     *
     * @return the table header cell title
     */
    public String getHeaderTitle() {
        if (headerTitle == null) {
            headerTitle = getMessage(getName() + ".headerTitle");
        }
        if (headerTitle == null) {
            headerTitle = ClickUtils.toLabel(getName());
        }
        return headerTitle;
    }

    /**
     * Return the HeaderCell html tag: <tt>th</tt> .
     *
     * @see org.apache.click.control.AbstractControl#getTag()
     *
     * @return this controls html tag
     */
    public String getTag() {
        return "th";
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Render the HTML representation of the table HeaderCell.
     *
     * @param buffer the buffer to render to
     */
    public void render(HtmlStringBuffer buffer) {
        // If no child controls have been added and name is not blank, create a
        // default child from the Cell name
        if (!hasControls() && StringUtils.isNotBlank(getName())) {
            setText(getHeaderTitle());
        }

        super.render(buffer);
    }
}
