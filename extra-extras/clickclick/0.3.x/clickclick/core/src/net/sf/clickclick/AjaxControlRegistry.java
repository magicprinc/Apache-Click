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
package net.sf.clickclick;

import java.util.ArrayList;
import org.apache.click.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.sf.clickclick.util.Partial;
import org.apache.commons.lang.Validate;

/**
 * Enhances ActionEventDispatcher to provide a thread local registry for managing
 * Ajax controls and ActionListener events. AjaxControlRegistry also adds support
 * for registering listeners to fire <tt>after</tt> the <tt>onRender</tt> event.
 * <p/>
 * <b>Please note:</b> this class is meant for component development.
 * <p/>
 * Ajax events can be specified with the constant {@link #ON_AJAX_EVENT} and
 * <tt>onRender</tt> events can be specified through the constant
 * {@link #POST_ON_RENDER_EVENT}.
 * <p/>
 * The ClickClickServlet will notify the AjaxControlRegistry which ActionListeners
 * to fire. For example, before the <tt>onProcess</tt> event, the ClickClickServlet
 * will notify the registry to fire ActionListeners registered for the
 * {@link #ON_AJAX_EVENT}. Similarly, after the <tt>onRender</tt> event,
 * the ClickClickServlet will notify the registry to fire ActionListeners
 * registered for the {@link #POST_ON_RENDER_EVENT}.
 * <p/>
 * Registering Ajax Controls for processing is done as follows:
 *
 * <pre class="prettyprint">
 * public void onInit() {
 *     Form form = new Form("form");
 *
 *     // Ajaxify the form by registering it in the AjaxControlRegistry
 *     AjaxControlRegistry.registerAjaxControl(form);
 *
 *     Submit submit = new Submit("submit");
 *     submit.setListener(new AjaxListener() {
 *
 *         public Partial onAjaxAction(Control control) {
 *             return new Partial("Hello World!");
 *         }
 *     });
 * } </pre>
 *
 * On rare occasions one need to manipulate a Control's state right before it
 * is rendered. The {@link #POST_ON_RENDER_EVENT} callback can be used for this
 * situation. For example:
 *
 * <pre class="prettyprint">
 * public class MyForm extends Form {
 *
 *     public MyForm() {
 *         init();
 *     }
 *
 *     public MyForm(String name) {
 *         super(name);
 *         init();
 *     }
 *
 *     private void init() {
 *         ActionListener listener = new ActionListener() {
 *             public boolean onAction(Control source {
 *                 // Add a hidden field to hold state for MyForm
 *                 add(new HiddenField("my-form-name", getName() + '_' + "myform"));
 *                 return true;
 *             }
 *         };
 *
 *         AjaxControlRegistry.registerActionEvent(this, listener, AjaxControlRegistry.POST_ON_RENDER_EVENT);
 *     }
 *
 *     ...
 *
 * } </pre>
 *
 * The above example fires the ActionListener <tt>after</tt> the <tt>onRender</tt>
 * event. This ensures a HiddenField is added right before the MyForm is
 * streamed to the browser.
 * <p/>
 * Registering the listener in MyForm constructor guarantees that the
 * listener will be registered even if MyForm is subclassed because the compiler
 * forces subclasses to invoke their super constructor.
 */
public class AjaxControlRegistry extends ActionEventDispatcher {

    // -------------------------------------------------------------- Constants

    /**
     * Indicates the listener should fire in the Ajax phase which is processed
     * <tt>BEFORE</tt> the onProcess phase.
     * Listeners in this phase are <tt>guaranteed</tt> to trigger, even when
     * redirecting, forwarding or processing stopped.
     */
    public static final int ON_AJAX_EVENT = 250;

    /**
     * Indicates the listener should fire <tt>AFTER</tt> the onRender event.
     * Listeners fired in the <tt>POST_ON_RENDER_EVENT</tT> are
     * <tt>guaranteed</tt> to trigger, even when redirecting, forwarding or if
     * page processing is cancelled.
     */
    public static final int POST_ON_RENDER_EVENT = 400;

    // -------------------------------------------------------- Variables

    /** List of {@link net.sf.clickclick.util.Partial} Ajax responses. */
    private List partials;

    /** The set of unique registered Ajax Controls. */
    private Set ajaxControls;

    /** The POST_RENDER events holder. */
    private EventHolder postRenderEventHolder;

    /** The AJAX events holder. */
    private EventHolder ajaxEventHolder;

    /** Track the last event that was fired. */
    private int lastEventFired = -1;

    // --------------------------------------------------------- Public Methods

    /**
     * Register the control to be processed by the ClickServlet for Ajax
     * requests.
     *
     * @param control the control to register
     */
    public static void registerAjaxControl(Control control) {
        Validate.notNull(control, "Null control parameter");

        AjaxControlRegistry instance = getThreadLocalRegistry();
        Set controlList = instance.getAjaxControls();
        controlList.add(control);
    }

    /**
     * Return the thread local registry instance.
     *
     * @return the thread local registry instance.
     * @throws RuntimeException if an AjaxControlRegistry is not available on the
     * thread.
     */
    protected static AjaxControlRegistry getThreadLocalRegistry() {
        return (AjaxControlRegistry) ActionEventDispatcher.getThreadLocalDispatcher();
    }

    /**
     * Return the list of Partial Ajax responses. If no partial is available
     * this method returns an empty list
     *
     * @return list of Partial Ajax responses or an empty list of no partial
     * is available
     */
    public List getPartials() {
        if (partials == null) {
            partials = new ArrayList();
        }
        return partials;
    }

    /**
     * Checks if any Ajax controls have been registered.
     */
    public boolean hasAjaxControls() {
        if (ajaxControls == null || ajaxControls.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Return the set of registered Ajax Controls.
     *
     * @return set of registered Ajax Controls
     */
    public Set getAjaxControls() {
        if (ajaxControls == null) {
            ajaxControls = new LinkedHashSet();
        }
        return ajaxControls;
    }

    /**
     * For Ajax requests this method will fire the Ajax listener and if a
     * {@link net.sf.clickclick.util.Partial} is returned, stream it back to
     * the browser. This method will also ensure that any
     * {@link #POST_ON_RENDER_EVENT} listeners are executed for Ajax requests.
     *
     * @see org.apache.click.ActionEventDispatcher#fireActionEvent(org.apache.click.Context, org.apache.click.Control, org.apache.click.ActionListener, int)
     *
     * @param context the request context
     * @param source the source control
     * @param listener the listener to fire
     * @param event the specific event which events to fire
     *
     * @return true if the page should continue processing or false otherwise
     */
    protected boolean fireActionEvent(Context context, Control source,
        ActionListener listener, int event) {

        this.lastEventFired = event;

        boolean continueProcessing = true;

        if (context.isAjaxRequest() && listener instanceof AjaxListener) {

            Partial partial = ((AjaxListener) listener).onAjaxAction(source);

            if (partial != null) {
                // Add partials to process to the partials list
                getPartials().add(partial);
            }

            // Ajax requests stops further processing
            continueProcessing = false;

        } else {
            if (!listener.onAction(source)) {
                continueProcessing = false;
            }
        }

        return continueProcessing;
    }

    /**
     * @see org.apache.click.ActionEventDispatcher#fireActionEvents(org.apache.click.Context, int)
     *
     * @param context the request context
     * @param event the event which listeners to fire
     *
     * @return true if the page should continue processing or false otherwise
     */
    protected boolean fireActionEvents(Context context, int event) {
        return super.fireActionEvents(context, event);
    }

    /**
     * @see org.apache.click.ActionEventDispatcher#getEventHolder(int)
     *
     * @param event the event which EventHolder to retrieve
     *
     * @return the EventHolder for the specified event
     */
    protected EventHolder getEventHolder(int event) {
        if (event == POST_ON_RENDER_EVENT) {
            return getPostRenderEventHolder();
        } else if (event == ON_AJAX_EVENT) {
            return getAjaxEventHolder();
        } else {
           return super.getEventHolder(event);
        }
    }

    /**
     * Allow the Registry to handle the error that occurred.
     */
    protected void errorOccurred(Throwable throwable) {
        if (hasAjaxControls()) {
            ajaxControls.clear();
        }
        lastEventFired = -1;
        getEventHolder(ON_AJAX_EVENT).clear();
        super.errorOccurred(throwable);
    }

    /**
     * Clear the registry.
     */
    protected void clearRegistry() {
        if (hasAjaxControls()) {
            ajaxControls.clear();
        }
        lastEventFired = -1;
        getPostRenderEventHolder().clear();
        super.clearEvents();
    }

    /**
     * Create a new EventHolder instance.
     *
     * @param event the EventHolder's event
     * @return new EventHolder instance
     */
    protected EventHolder createEventHolder(int event) {
        return new AjaxEventHolder(event);
    }

    // ------------------------------------------------ Package Private Methods

    /**
     * Return the {@link #POST_ON_RENDER_EVENT} {@link EventHolder}.
     *
     * @return the {@link #POST_ON_RENDER_EVENT} {@link EventHolder}
     */
    EventHolder getPostRenderEventHolder() {
        if (postRenderEventHolder == null) {
            postRenderEventHolder = createEventHolder(POST_ON_RENDER_EVENT);
        }
        return postRenderEventHolder;
    }

    /**
     * Return the EventHolder for the {@link #ON_AJAX_EVENT}.
     *
     * @return the Ajax EventHolder
     */
    EventHolder getAjaxEventHolder() {
        if (ajaxEventHolder == null) {
            ajaxEventHolder = createEventHolder(ON_AJAX_EVENT);
        }
        return ajaxEventHolder;
    }

    // ---------------------------------------------------------- Inner Classes

    /**
     * Extends EventHolder to provide special Ajax handling.
     */
    public class AjaxEventHolder extends EventHolder {

        /**
         * Construct a new AjaxEventHolder for the given event.
         *
         * @param event the AjaxEventHolder's event 
         */
        public AjaxEventHolder(int event) {
            super(event);
        }

        /**
         * Register the event source and event ActionListener to be fired in the
         * specified event.
         *
         * @param source the action event source
         * @param listener the event action listener
         */
        public void registerActionEvent(Control source, ActionListener listener) {
            super.registerActionEvent(source, listener);

            if (event == ON_AJAX_EVENT && event < lastEventFired) {
                // If the Ajax event for which this listener is registering
                // already fired, trigger the listener immediately.
                // This feature is useful for stateful pages where controls are
                // added in the listener or onRender method
                AjaxControlRegistry.this.fireActionEvent(Context.getThreadLocalContext(),
                    source, listener, event);
            }
        }
    }
}
