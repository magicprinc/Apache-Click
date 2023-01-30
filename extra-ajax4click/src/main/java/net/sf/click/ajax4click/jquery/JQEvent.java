package net.sf.click.ajax4click.jquery;

/**
 * Provides an event object that is passed to the {@link JQBehavior#onAction(org.apache.click.Control, net.sf.click.ajax4click.jquery.JQEvent)}
 * method. JQEvent can be queried for the specific type of event that occurred
 * on the browser.
 * <p/>
 * JQEvent also provides constants for JavaScript/JQuery events. See the jQuery
 * documentation for further details:
 * <a href="http://api.jquery.com/category/events/">events</a>.
 */
public class JQEvent {

    // -------------------------------------------------------------- constants

    /** The "<tt>blur</tt>" event type. */
    public static final String BLUR = "blur";

    /** The "<tt>change</tt>" event type. Ideal for Select controls. */
    public static final String CHANGE = "change";

    /** The "<tt>click</tt>" event type. */
    public static final String CLICK = "click";

    /**
     * The "<tt>domready</tt>" event type. This event is fired as soon as
     * the dom is ready. This is a non-standard, jQuery specific event.
     */
    public static final String DOMREADY = "domready";

    /** The "<tt>dblclick</tt>" event type. */
    public static final String DOUBLE_CLICK = "dblclick";

    /** The "<tt>focus</tt>" event type. */
    public static final String FOCUS = "focus";

    /**
     * The "<tt>focusin</tt>" event type. This is a non-standard, jQuery
     * specific event.
     */
    public static final String FOCUSIN = "focusin";

    /**
     * The "<tt>focusout</tt>" event type. This is a non-standard, jQuery
     * specific event.
     */
    public static final String FOCUSOUT = "focusout";

    /** The "<tt>keydown</tt>" event type. */
    public static final String KEYDOWN = "keydown";

    /** The "<tt>keypress</tt>" event type. */
    public static final String KEYPRESS = "keypress";

    /** The "<tt>keyup</tt>" event type. */
    public static final String KEYUP = "keyup";

    /** The "<tt>load</tt>" event type. */
    public static final String LOAD = "load";

    /** The "<tt>mousedown</tt>" event type. */
    public static final String MOUSEDOWN = "mousedown";

    /** The "<tt>mouseenter</tt>" event type. */
    public static final String MOUSEENTER = "mouseenter";

    /** The "<tt>mouseleave</tt>" event type. */
    public static final String MOUSELEAVE = "mouseleave";

    /** The "<tt>mousemove</tt>" event type. */
    public static final String MOUSEMOVE = "mousemove";

    /** The "<tt>mouseout</tt>" event type. */
    public static final String MOUSEOUT = "mouseout";

    /** The "<tt>mouseover</tt>" event type. */
    public static final String MOUSEOVER = "mouseover";

    /** The "<tt>mouseup</tt>" event type. */
    public static final String MOUSEUP = "mouseup";

    /** The "<tt>resize</tt>" event type. */
    public static final String RESIZE = "resize";

    /** The "<tt>scroll</tt>" event type. */
    public static final String SCROLL = "scroll";

    /** The "<tt>select</tt>" event type. */
    public static final String SELECT = "select";

    /** The "<tt>submit</tt>" event type. */
    public static final String SUBMIT = "submit";

    /** The "<tt>unload</tt>" event type. */

    public static final String UNLOAD = "unload";
    // -------------------------------------------------------------- variables

    private String type;

    private String which;

    // --------------------------------------------------------- public methods

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the which
     */
    public String getWhich() {
        return which;
    }

    /**
     * @param which the which to set
     */
    public void setWhich(String which) {
        this.which = which;
    }
}
