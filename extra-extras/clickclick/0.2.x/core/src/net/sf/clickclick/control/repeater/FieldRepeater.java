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
package net.sf.clickclick.control.repeater;

import java.util.List;
import org.apache.click.control.Container;

import org.apache.click.control.Field;
import org.apache.click.util.ContainerUtils;

/**
 * Provides a Repeater that displays a {@link org.apache.click.control.Field}
 * for each item in a list.
 * <p/>
 * <h3>Basic Example</h3>
 *
 * Given the Page <tt>MyPage.java</tt>:
 *
 * <pre class="prettyprint">
 * public class MyPage extends Page {
 *
 *     private FieldRepeater repeater;
 *
 *     public void onInit() {
 *         // Create a FieldRepeater and implement the buildRow method
 *         repeater = new FieldRepeater("repeater") {
 *             public void buildRow(Object item, RepeaterRow row, int index) {
 *                 // Form each item create a Field
 *                 Field field = new TextField("name");
 *
 *                 // Set the Field value to the item's String representation
 *                 field.setValue(item.toString());
 *
 *                 // Add the Field to the Repeater by adding it to the RepeaterRow
 *                 row.add(field);
 *             }
 *         };
 *
 *         // Add the repeater to the Page
 *         addControl(repeater);
 *
 *         // Create a list of items and set it as the Repeater item list
 *         List items = new ArrayList();
 *         items.add("one");
 *         items.add("two");
 *         repeater.setItems(items);
 *     }
 * } </pre>
 *
 * and the template <tt>my-page.htm</tt>:
 *
 * <pre class="prettyprint">
 * $repeater </pre>
 *
 * will render as:
 *
 * <div class="border">
 * <input type="text" name="name_0" id="name_0" value="one" size="20"/>
 * <input type="text" name="name_1" id="name_1" value="two" size="20"/>
 * </div>
 */
public abstract class FieldRepeater extends Repeater {

    // -------------------------------------------------------------- Variables

    /** The name of the field contained in the repeater. */
    private String fieldName;

    // ------------------------------------------------------------ Constructor

    /**
     * Create a FieldRepeater for the given name and fieldName.
     *
     * @param name the name of the repeater
     * @param fieldName the name of the field contained in the repeater
     */
    public FieldRepeater(String name, String fieldName) {
        super(name);
        this.fieldName = fieldName;
    }

    /**
     * Create a FieldRepeater for the given name.
     *
     * @param name the name of the repeater
     */
    public FieldRepeater(String name) {
        super(name);
    }

    /**
     * Create a default FieldRepeater.
     */
    public FieldRepeater() {
    }

    // --------------------------------------------------------

    /**
     * Return the name of the field that is contained in the repeater.
     *
     * @return the name of the field that is contained in the repeater
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Set the name of the field that is contained in the repeater.
     *
     * @param fieldName the name of the field that is contained in the repeater
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    // --------------------------------------------------------- Public methods

    /**
     * By default copyToItems will update the given item with the values of
     * any Field found in the repeater.
     * <p/>
     * However in this case item is an immutable String, thus instead of
     * updating the item, it is simply replaced.
     * <p/>
     * By default this method assumes a Field will be the target Control to copy
     * the String item from. You can override this method if a different Control
     * should be used to copy the String item from. For example:
     *
     * <pre class="prettyprint">
     * public void copyTo(Object item) {
     *     items.set(index, actionLink.getValue());
     * } </pre>
     */
    public void copyTo(Object item) {
        List items = getItems();
        if (items == null) {
            throw new IllegalStateException("Items cannot be null.");
        }
        int index = items.indexOf(item);

        Container container = (Container) getControls().get(index);

        // Find the first Field in the repeater
        Field field = (Field) ContainerUtils.findControlByName(container, getFieldName());

        if (field == null) {
            return;
        }

        // Replace the item at the index with the Field value
        items.set(index, field.getValueObject());
    }

    /**
     * By default copyFromItems will update the values of any Field found in
     * the repeater with matching properties of the given item.
     * <p/>
     * However in this case item is an immutable String, thus instead of
     * updating the field, its value is simply replaced with the item.
     * <p/>
     * By default this method assumes a Field will be the target Control to copy
     * the String item to. You can override this method if a different Control
     * should be used to the String item to. For example:
     *
     * <pre class="prettyprint">
     * public void copyTo(Object item) {
     *     actionLink.setValueObject(item);
     * }
     * </pre>
     */
    public void copyFrom(Object item) {
        List items = getItems();
        if (items == null) {
            throw new IllegalStateException("Items cannot be null.");
        }
        int index = items.indexOf(item);
        Container container = (Container) getControls().get(index);

        // Find the first Field in the repeater
        Field field = (Field) ContainerUtils.findControlByName(container, getFieldName());

        if (field == null) {
            return;
        }

        // Set the field value to the item
        field.setValueObject(item);
    }
}
