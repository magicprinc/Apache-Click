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
package net.sf.clickclick.jquery.util;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides a JavaScript event enum.
 */
public enum JQEvent {

    /** The "<tt>blur</tt>" name enum. */
    BLUR("blur"),

    /** The "<tt>change</tt>" name enum. Ideal for Select controls. */
    CHANGE("change"),

    /** The "<tt>click</tt>" name enum. */
    CLICK("click"),

    /**
     * The custom "<tt>domready</tt> name enum. This name is fired as soon as
     * the dom is ready.
     */
    // This name is simply "null" and is triggered immediately
    DOMREADY(null),

    /** The "<tt>dblclick</tt>" name enum. */
    DOUBLE_CLICK("dblclick"),

     /** The "<tt>focus</tt>" name enum. */
    FOCUS("focus"),

    /** The "<tt>keydown</tt>" name enum. */
    KEYDOWN("keydown"),

    /** The "<tt>keypress</tt>" name enum. */
    KEYPRESS("keypress"),

    /** The "<tt>keyup</tt>" name enum. */
    KEYUP("keyup"),

    /** The "<tt>load</tt>" name enum. */
    LOAD("load"),

    /** The "<tt>mousedown</tt>" name enum. */
    MOUSEDOWN("mousedown"),

    /** The "<tt>mouseenter</tt>" name enum. */
    MOUSEENTER("mouseenter"),

    /** The "<tt>mouseleave</tt>" name enum. */
    MOUSELEAVE("mouseleave"),

    /** The "<tt>mousemove</tt>" name enum. */
    MOUSEMOVE("mousemove"),

    /** The "<tt>mouseout</tt>" name enum. */
    MOUSEOUT("mouseout"),

    /** The "<tt>mouseover</tt>" name enum. */
    OUSEOVER("mouseover"),

    /** The "<tt>mouseup</tt>" name enum. */
    MOUSEUP("mouseup"),

    /** The "<tt>resize</tt>" name enum. */
    RESIZE("resize"),

    /** The "<tt>scroll</tt>" name enum. */
    SCROLL("scroll"),

    /** The "<tt>select</tt>" name enum. */
    SELECT("select"),

    /** The "<tt>submit</tt>" name enum. */
    SUBMIT("submit"),

    /** The "<tt>unload</tt>" name enum. */
    UNLOAD("unload"),

    /** The "<tt>undefined</tt>" name enum. */
    UNDEFINED("undefined");

    /** The event request parameter value: <tt>"event"</tt>. */
    public static final String EVENT_PARAM = "event";

    /** The event name. */
    private String name;

    /** A reverse event lookup mechanism. */
    private static final Map<String, JQEvent> lookup = new HashMap<String, JQEvent>();

    static {
        for(JQEvent event : EnumSet.allOf(JQEvent.class)) {
            lookup.put(event.name, event);
        }
    }

    /**
     * Create a new event for the given name.
     *
     * @param name the name of the event
     */
    JQEvent(String name) {
        this.name = name;
    }

    /**
     * Returns the name of this enum constant.
     *
     * @see java.lang.Enum#toString()
     *
     * @return the name of this enum constant
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Lookup and return the matching event for the given name, or null if no
     * event was found.
     *
     * @param eventName the name of the event to lookup
     * @return the event for the given name, or null if no event matched the name
     */
    public static JQEvent lookup(String eventName) {
        JQEvent event = lookup.get(eventName);
        if (event == null) {
            event = UNDEFINED;
        }
        return event;
    }
}
