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

import java.util.ArrayList;
import java.util.List;

import org.apache.click.Behavior;
import org.apache.click.Context;
import org.apache.click.Control;
import org.apache.click.ControlRegistry;
import org.apache.click.control.AbstractContainer;
import org.apache.click.control.AbstractLink;
import org.apache.click.control.Container;
import org.apache.click.control.Field;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Panel;
import org.apache.click.dataprovider.DataProvider;
import org.apache.click.util.ContainerUtils;
import org.apache.click.util.HtmlStringBuffer;

/**
 * Provides a Repeater control for displaying a list of items. For every item in
 * the list the Repeater will display the same specified components.
 * <p/>
 * <h3>Usage</h3>
 * When creating a Repeater you must implement the abstract method
 * {@link #buildRow(java.lang.Object, net.sf.clickclick.control.repeater.RepeaterRow, int)}
 * which is invoked by the Repeater for every item in the {@link #items} list.
 * <p/>
 * In the {@link #buildRow(java.lang.Object, net.sf.clickclick.control.repeater.RepeaterRow, int)}
 * method you setup the components for the given item and the Repeater will
 * display these components for every item in the {@link #items} list.
 * <p/>
 * <h3>Basic Example</h3>
 *
 * Given the Page <tt>MyPage.java</tt>:
 *
 * <pre class="prettyprint">
 * public class MyPage extends Page {
 *
 *     private Repeater repeater;
 *
 *     public void onInit() {
 *         // Create a Repeater and implement the buildRow method
 *         repeater = new Repeater("repeater") {
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
 *
 * <h3>How does the Repeater work?</h3>
 *
 * In the example above a TextField is displayed for each item in the list. Both
 * fields are called <span class="st">"name"</span> and one can expect the
 * fields to be rendered as:
 *
 * <pre class="prettyprint">
 * &lt;input type="text" name="name" id="name" ... /&gt;
 * &lt;input type="text" name="name" id="name" ... /&gt;</pre>
 *
 * The two fields will have the same <tt>id</tt> and <tt>name</tt>, meaning it's
 * not possible to differentiate the fields.
 * <p/>
 * The Repeater resolves this issue by ensuring the <tt>name</tt> of child
 * controls in each {@link RepeaterRow} is unique by adding the index of the row
 * to each control. Thus the Repeater renders the two Fields as:
 *
 * <pre class="prettyprint">
 * &lt;input type="text" name="name_0" id="name_0" ... /&gt;
 * &lt;input type="text" name="name_1" id="name_1" ... /&gt;</pre>
 */
public abstract class Repeater extends AbstractContainer {

    // -------------------------------------------------------------- Constants

    private static final long serialVersionUID = 1L;

    // -------------------------------------------------------------- Variables

    /** The list of items to be rendered. */
    protected List items = null;

    protected DataProvider dataProvider = null;

    private boolean behaviorRegistered = false;

    private Behavior behavior;

    // Constructors -----------------------------------------------------------

    /**
     * Create a default Repeater.
     */
    public Repeater() {
    }

    /**
     * Create a Repeater with the given name.
     *
     * @param name the repeater name
     */
    public Repeater(String name) {
        super(name);
    }

    // ------------------------------------------------------ Public Properties

    /**
     * Get the value of items.
     *
     * @return the value of items
     */
    List getItems() {
        // TODO need to replace "getItems()==null" check with getDataProvider check
        if (items == null) {
            items = new ArrayList();
        }
        return items;
    }

    // Consolodate setItems with setDataProvider
    void setItems(List items) {
        this.items = items;
    }

    public DataProvider getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
        buildRows();
    }

    // --------------------------------------------------------- Public Methods

    /**
     * @throws UnsupportedOperationException if invoked. Rather add controls
     * through the {@link #buildRow(java.lang.Object, net.sf.clickclick.control.repeater.RepeaterRow, int)}
     * method.
     */
    @Override
    public Control add(Control control) {
        throw new UnsupportedOperationException("Method not supported. Rather"
            + " add controls through the #buildRow method.");
    }

    /**
     * @throws UnsupportedOperationException if invoked. Rather add controls
     * through the {@link #buildRow(java.lang.Object, net.sf.clickclick.control.repeater.RepeaterRow, int)}
     * method.
     */
    @Override
    public Control insert(Control control, int index) {
        throw new UnsupportedOperationException("Method not supported. Rather"
            + " add controls through the #buildRow method.");
    }

    /**
     * Inserts the item at the given index.
     *
     * @param item the item to add
     * @param index the index to add the item to
     */
    public void insertItem(Object item, int index) {
        getItems().add(index, item);
        createRow(index);
    }

    /**
     * Add the item to the list of {@link #items}.
     *
     * @param item the item to add
     */
    public void addItem(Object item) {
        getItems().add(item);
        int index = getItems().size();
        createRow(index - 1);
    }

    /**
     * Remove the item from the list of {@link #items}.
     *
     * @param item the item to remove
     */
    public void removeItem(Object item) {
        int index = getItems().indexOf(item);
        boolean removed = getItems().remove(item);
        if (removed) {
            removed = removeRow(index);
            // TODO throw exception if row was not removed?
        }
    }

    /**
     * Move the item up one index in the list of {@link #items}.
     * <p/>
     * When moving the item up, the {@link RepeaterRow} for that item is also
     * moved up.
     *
     * @param item the item to move up in the list of {@link #items}
     */
    public void moveUp(Object item) {
        List localItems = getItems();
        int index = localItems.indexOf(item);

         // If item is already at top, exit early
        if (index == 0) {
            return;
        }

        boolean itemRemoved = localItems.remove(item);
        RepeaterRow row = (RepeaterRow) getControls().get(index);
        boolean rowRemoved = super.remove(row);

        // If item or row was not removed, exist early
        if (!itemRemoved || !rowRemoved) {
            return;
        }
        // Decrement index and add item and row at new index
        --index;
        localItems.add(index, item);
        super.insert(row, index);
    }

    /**
     * Move the item down one index in the list of {@link #items}.
     * <p/>
     * When moving the item down, the {@link RepeaterRow} for that item is also
     * moved down.
     *
     * @param item the item to move down in the list of {@link #items}
     */
    public void moveDown(Object item) {
        List localItems = getItems();
        int index = localItems.indexOf(item);

        // If item is already at bottom, exit early
        if (index == localItems.size() - 1) {
            return;
        }

        boolean itemRemoved = localItems.remove(item);
        RepeaterRow row = (RepeaterRow) getControls().get(index);
        boolean rowRemoved = super.remove(row);

        // If item or row was not removed, exist early
        if (!itemRemoved || !rowRemoved) {
            return;
        }

        // Increment index and add item and row at new index
        ++index;
        localItems.add(index, item);
        super.insert(row, index);
    }

    // TODO create actionLinks for moveUp and moveDown ???

    // ------------------------------------------------------ Protected Methods

    /**
     * This method is provided for users to build up the controls for the
     * specified item.
     * <p/>
     * For example:
     *
     * <pre class="prettyprint">
     * public MyPage extends Page {
     *
     *     Repeater repeater = new Repeater("repeater") {
     *
     *         public void buildRow(final Object item, RepeaterRow row, int index) {
     *
     *             final Customer customer = (Customer) item;
     *
     *             final Form form = new Form("form");
     *             row.add(form);
     *
     *             form.add(new TextField("firstname"));
     *             form.add(new TextField("lastname"));
     *             form.add(new TextField("age"));
     *
     *             Submit submit = new Submit("submit");
     *             submit.setActionListener(new ActionListener() {
     *
     *                 public boolean onAction(Control source) {
     *                     if (form.isValid()) {
     *                         form.copyTo(customer);
     *                         getCustomerService().save(customer);
     *                     }
     *                     return true;
     *                 }
     *             });
     *             form.add(submit);
     *         }
     *     }
     * }; </pre>
     *
     * @param item the item to build controls for
     * @param row the RepeaterRow for the given item
     * @param index the index of the given item
     */
    public abstract void buildRow(Object item, RepeaterRow row, int index);

    /**
     * @see org.apache.click.Control#onProcess().
     *
     * @return true if page processing should continue, false otherwise
     */
    @Override
    public boolean onProcess() {
        boolean result = super.onProcess();

        // Unwind control name indexes here so that new RepeaterRows added or
        // inserted after onProcess (e.g. with an action listener) won't end up with
        // indexes which are out of order.
        removeIndexFromControlNames();
        return result;
    }

    /**
     * Copy the values of all {@link org.apache.click.control.Field Fields}
     * contained in a Repeater row to its associated item.
     * <p/>
     * This method delegates to {@link #copyTo(java.lang.Object)} for every
     * item in the {@link #items} list.
     */
    public void copyToItems() {
        if (getItems() == null) {
            throw new IllegalStateException("Items have not been set.");
        }
        List localItems = getItems();
        for (int i = 0; i < localItems.size(); i++) {
            Object item = localItems.get(i);
            copyTo(item);
        }
    }

    /**
     * Copy the values of each item in the {@link #items} list to
     * the {@link org.apache.click.control.Field Fields} contained in the
     * associated RepeaterRow.
     * <p/>
     * This method delegates to {@link #copyFrom(java.lang.Object)} for every
     * item in the {@link #items} list.
     */
    public void copyFromItems() {
        List localItems = getItems();
        if (localItems == null) {
            throw new IllegalStateException("Items have not been set.");
        }
        for (int i = 0; i < localItems.size(); i++) {
            Object item = localItems.get(i);
            copyFrom(item);
        }
    }

    /**
     * Copy Field values from the item's associated RepeaterRow, to the given
     * item's attributes. The specified item can either be a POJO
     * (plain old java object) or a {@link java.util.Map}.
     * <p/>
     * For more information on how Fields and Objects are copied see
     * {@link org.apache.click.util.ContainerUtils#copyContainerToObject(org.apache.click.control.Container, java.lang.Object)}.
     *
     * @param item the item to populate with field values
     */
    public void copyTo(Object item) {
        List localItems = getItems();
        if (localItems == null) {
            throw new IllegalStateException("Items have not been set.");
        }
        //Object object = getItems().get(index);
        int index = localItems.indexOf(item);
        Container container = (Container) getControls().get(index);
        ContainerUtils.copyContainerToObject(container, item);
    }

    /**
     * Copy the given item's attributes into the Field values of the associated
     * RepeaterRow. The specified item can either be a POJO
     * (plain old java object) or a {@link java.util.Map}.
     * <p/>
     * For more information on how Fields and Objects are copied see
     * {@link org.apache.click.util.ContainerUtils#copyObjectToContainer(java.lang.Object, org.apache.click.control.Container)}.
     *
     * @param item the item to copy attribute values from
     */
    public void copyFrom(Object item) {
        if (getItems() == null) {
            throw new IllegalStateException("Items have not been set.");
        }
        //Object object = getItems().get(index);
        int index = getItems().indexOf(item);
        Container container = (Container) getControls().get(index);
        ContainerUtils.copyObjectToContainer(item, container);
    }

    /**
     * Override the default implementation to register an internal Behavior. The
     * behavior is used for adding indexes to child controls before the response
     * is rendered.
     */
    @Override
    public void onInit() {
        super.onInit();

        // Write unit tests for the following scenarios:
        // create repeater in constructor - repeater must buildRows and register with ControlRegistry
        // create repeater in onInit  - repeater must buildRows and register with ControlRegistry
        // create repeater in onRender  - repeater must buildRows and register with ControlRegistry

        registerInternalBehavior();
    }

    /**
     * @see org.apache.click.Control#onDestroy()
     */
    @Override
    public void onDestroy() {
        behaviorRegistered = false;
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Build the rows for every Repeater item.
     * <p/>
     * This method delegates to {@link #buildRow(java.lang.Object, net.sf.clickclick.control.repeater.RepeaterRow, int)}
     * for every item in the {@link #items} list.
     */
    protected void buildRows() {
        DataProvider localDataProvider = getDataProvider();
        if (localDataProvider == null) {
            throw new IllegalStateException("No data provider set.");
        }

        populateItems();

        List repeaterItems = getItems();

        registerInternalBehavior();

        for (int i = 0; i < repeaterItems.size(); i++ ) {
            createRow(i);
        }

        // TODO should the names be changed here or in a preOnProcess event???
        // Update control name indexes to match incoming request parameters
        addIndexToControlNames();
    }

    protected void populateItems() {
        Iterable it = getDataProvider().getData();
        if (it instanceof List) {
            setItems((List) it);
        } else {
            List localItems = getItems();
            for (Object item : it) {
                localItems.add(item);
            }
        }
    }

    /**
     * Adds an index to the name of child controls to ensure controls are unique
     * within the Repeater.
     * <p/>
     * Indexing is zero based, meaning controls contained in the first
     * RepeaterRow will have <tt>"_0"</tt> appended to their names and controls
     * contained in the second RepeaterRow will have <tt>"_1"</tt> appended.
     * <p/>
     * For example, a control contained in the first RepeaterRow called
     * <tt>"firstname"</tt> will be renamed to <tt>"firstname_0"</tt>, and
     * a control in the second RepeaterRow will be renamed to
     * <tt>"firstname_1"</tt> etc.
     */
    protected void addIndexToControlNames() {
        List localControls = getControls();
        for (int count = 0; count < localControls.size(); count++ ) {
            RepeaterRow container = (RepeaterRow) localControls.get(count);
            addIndexToControlNames(container, String.valueOf(count));
        }
    }

    /**
     * Removes the index from the name of child controls.
     */
    protected void removeIndexFromControlNames() {
        List localControls = getControls();
        for (int count = 0; count < localControls.size(); count++ ) {
            RepeaterRow container = (RepeaterRow) localControls.get(count);
            removeIndexFromControlNames(container);
        }
    }

    // -------------------------------------------------------- Private Methods

    /**
     * Adds the index to child controls contained in the given container.
     *
     * @param container the container which child controls must be indexed
     * @param index the index to apply to child control names
     */
    private void addIndexToControlNames(final Container container, String index) {
        List<Control> localControls = container.getControls();
        for (Control control : localControls) {
            if (control instanceof Field) {
                Field field = (Field) control;

                // Ensure the field label gets cached in case its index is changed
                field.getLabel();
                if (control instanceof FieldSet) {
                    // Ensure fieldSet legend gets cached in case its index is changed
                    ((FieldSet) field).getLegend();
                }
            } else if (control instanceof AbstractLink) {
                ((AbstractLink) control).getLabel();
            } else if (control instanceof Panel) {
                ((Panel) control).getLabel();
            }

            // Append the index to the Control name
            addIndex(control, index);

            // If control is a container, add the index to its child controls
            if (control instanceof Container) {
                Container childContainer = (Container) control;
                addIndexToControlNames(childContainer, index);
            }
        }
    }

    /**
     * Remove the index from the child controls contained in the given
     * container.
     *
     * @param container the container which child controls the index
     * must be removed from
     */
    private void removeIndexFromControlNames(final Container container) {
        List localControls = container.getControls();
        for (int i = 0; i < localControls.size(); i++) {
            Control control = (Control) localControls.get(i);
            removeIndex(control);
            if (control instanceof Container) {
               Container childContainer = (Container) control;
               removeIndexFromControlNames(childContainer);
            }
        }
    }

    /**
     * Adds the given index to the control.
     *
     * @param control the control which name must be indexed
     * @param index the index to add to the control name
     */
    private void addIndex(Control control, String index) {
        if (control.getName() == null) {
            return;
        }
        HtmlStringBuffer buffer = new HtmlStringBuffer();
        buffer.append(control.getName());
        buffer.append('_');
        buffer.append(index);
        control.setName(buffer.toString());
        control.setName(buffer.toString());
    }

    /**
     * Remove the index from the given control name.
     *
     * @param control the control form which the index must be removed from
     */
    private void removeIndex(Control control) {
        if (control.getName() == null) {
           return;
        }
        int index = control.getName().lastIndexOf("_");
        if (index >= 0) {
            control.setName(control.getName().substring(0, index));
        }
    }

    /**
     * Create a new RepeaterRow for the given index.
     *
     * @param index the index to create a row for
     */
    private void createRow(int index) {
        RepeaterRow row = new RepeaterRow();
        super.insert(row, index);
        Object item = getItems().get(index);
        buildRow(item, row, index);
    }

    /**
     * Remove the row at the given index.
     *
     * @param index the index to remove the row from
     * @return true if the row was removed, false otherwise
     */
    private boolean removeRow(int index) {
        RepeaterRow row = (RepeaterRow) getControls().get(index);
        return super.remove(row);
    }

    /**
     * Register the repeater behavior, unless it has been registered already.
     */
    private void registerInternalBehavior() {
        if (behaviorRegistered) {
            return;
        }

        ControlRegistry.registerInterceptor(this, getInternalBehavior());
        behaviorRegistered = true;
    }

    /**
     * Return the repeater internal behavior.
     *
     * @return the repeater internal behavior
     */
    private Behavior getInternalBehavior() {
        if (behavior == null) {
            behavior = new InternalBehavior();
        }

        return behavior;
    }

    /**
     * The repeater behavior implementation.
     */
    class InternalBehavior implements Behavior {

        public boolean isRequestTarget(Context context) {
            return false;
        }

        public void preDestroy(Control source) {
        }

        public void preRenderHeadElements(Control source) {
        }

        public void preResponse(Control source) {
            addIndexToControlNames();
        }
    }
}
