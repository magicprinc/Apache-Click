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
package net.sf.clickclick.control.html.list;

import org.apache.click.control.AbstractContainer;

/**
 * Provides an HTML ListItem control: <tt>&lt;li&gt;</tt> .
 */
public class ListItem extends AbstractContainer {

    // ----------------------------------------------------------- Constructors

    /**
     * Create a default ListItem.
     */
    public ListItem() {
    }

    /**
     * Create a ListItem with the given name.
     *
     * @param name the name of the ListItem
     */
    public ListItem(String name) {
        if(name != null) {
            setName(name);
        }
    }

    /**
     * Create a ListItem with the given name and id.
     *
     * @param name the name of the ListItem
     * @param id the id of the ListItem
     */
    public ListItem(String name, String id) {
        this(name);
        setAttribute("id", id);
    }

    // ------------------------------------------------------ Public Properties

    /**
     * Return the ListItem html tag: <tt>li</tt>.
     *
     * @see org.apache.click.control.AbstractControl#getTag()
     *
     * @return this controls html tag
     */
    public final String getTag() {
        return "li";
    }
}
