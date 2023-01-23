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

import org.apache.click.Control;
import org.apache.click.control.AbstractContainer;
import org.apache.click.control.AbstractControl;

/**
 * Provides an HTML list control supporting both ordered and unordered lists:
 * &lt;ol&gt; and &lt;ul&gt;.
 * <p/>
 * HtmlList is quite a useful layout in HTML pages since its easy to style with
 * CSS.
 * <p/>
 * The following example shows how to use an HtmlList control to render a list
 * of Fields.
 * <p/>
 * Given the Page <tt>MyPage.java</tt>:
 *
 * <pre class="prettyprint">
 * public MyPage extends Page {
 *
 *     public onInit() {
 *         // Create a new HtmlList
 *         HtmlList list = new HtmlList("list");
 *
 *         // Add three Fields to the list
 *         addField(list, new TextField("firstname"));
 *         addField(list, new TextField("lastname"));
 *         addField(list, new TextField("age"));
 *
 *         // Add the HtmlList to the Page control list
 *         addControl(list);
 *     }
 *
 *     // A helper method creating ListItem entries containing a Label and Field
 *     private void addField(HtmlList list, Field field) {
 *         ListItem item = new ListItem();
 *         list.add(item);
 *         item.add(new FieldLabel(field));
 *         item.add(field);
 *     }
 * } </pre>
 *
 * and the template <tt>my-page.htm</tt>:
 *
 * <pre class="prettyprint">
 * $list </pre>
 *
 * will render as:
 *
 * <div class="border">
 * <ul name="list" id="list">
 *   <li>
 *     <label for="firstname">Firstname:</label>
 *     <input type="text" name="firstname" id="firstname" value="" size="20"/>
 *   </li>
 *   <li>
 *     <label for="lastname">Lastname:</label>
 *     <input type="text" name="lastname" id="lastname" value="" size="20"/>
 *   </li>
 *   <li>
 *     <label for="age">Age:</label>
 *     <input type="text" name="age" id="age" value="" size="20"/>
 *   </li>
 * </ul>
 * </div>
 *
 * The above example still needs to be style with CSS. Please see the
 * <a target="_blank" class="external" href="http://www.avoka.com/click-examples/form/contact-details.htm">Contact Details Demo</a>
 * for an example on how to style an HtmlList.
 */
public class HtmlList extends AbstractContainer {

    // -------------------------------------------------------------- Constants

    /** The unordered list type constant representing: &nbsp; <tt>"ul"</tt>. */
    public static final int UNORDERED_LIST = 0;

    /** The ordered list type constant representing: &nbsp; <tt>"ol"</tt>. */
    public static final int ORDERED_LIST = 1;

    // -------------------------------------------------------------- Variables

    /** The list type, default value is {@link #UNORDERED_LIST}. */
    private int listType = UNORDERED_LIST;

    // ----------------------------------------------------------- Constructors

    /**
     * Create a default HtmlList.
     */
    public HtmlList() {
    }

    /**
     * Create an HtmlList of the given list type, either {@link #ORDERED_LIST}
     * or {@link #UNORDERED_LIST}.
     *
     * @param listType the type of list
     */
    public HtmlList(int listType) {
        this.listType = listType;
    }

    /**
     * Create an HtmlList with the given name.
     *
     * @param name the name of the list
     */
    public HtmlList(String name) {
        super(name);
    }

    /**
     * Create an HtmlList with the given name and and list type, either
     * {@link #ORDERED_LIST} or {@link #UNORDERED_LIST}.
     *
     * @param name the name of the list
     * @param listType the type of list
     */
    public HtmlList(String name, int listType) {
        this(name);
        this.listType = listType;
    }

    // ------------------------------------------------------ Public Properties

    /**
     * Return the list's html tag: <tt>ul</tt> or <tt>ol</tt>.
     *
     * @see AbstractControl#getTag()
     *
     * @return this controls html tag
     */
    public String getTag() {
        if (isUnorderedList()) {
            return "ul";
        } else {
            return "ol";
        }
    }

    /**
     * Return the list type, either {@link #ORDERED_LIST} or
     * {@link #UNORDERED_LIST}.
     *
     * @return the list type
     */
    public int getListType() {
        return listType;
    }

    /**
     * Set the list type to either {@link #ORDERED_LIST} or
     * {@link #UNORDERED_LIST}.
     *
     * @param listType the list type
     */
    public void setListType(int listType) {
        this.listType = listType;
    }

    /**
     * Return true if the list is unordered, false if it is ordered.
     *
     * @return true if the list is unordered, false if it is ordered
     */
    public boolean isUnorderedList() {
        return listType == UNORDERED_LIST;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Add the given control object by creating and adding a new {@link ListItem}
     * to the list and adding the control to the ListItem.
     *
     * @param control the control to add
     * @return the control that was added
     */
    public Control add(Control control) {
        ListItem item = new ListItem();
        item.add(control);
        add(item);
        return control;
    }

    /**
     * Remove the control from the HtmlList. If the control is not an instance
     * of ListItem, the controls parent (which should be a ListItem) will be
     * removed from the list.
     *
     * @param control the control to remove
     * @return true if the control was removed, false otherwise
     */
    public boolean remove(Control control) {
        if (control instanceof ListItem) {
            return super.remove(control);
        } else {
            Object parent = control.getParent();
            if (parent instanceof ListItem) {
                return super.remove((ListItem) parent);
            } else {
                return false;
            }
        }
    }

    /**
     * Add the ListItem to the HtmlList.
     *
     * @param listItem the ListItem to add
     * @return the ListItem that was added
     */
    public ListItem add(ListItem listItem) {
        return (ListItem) super.add(listItem);
    }
}
