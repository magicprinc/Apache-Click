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

import java.io.Serializable;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Provides a JQuery binding of CSS selectors to JavaScript events.
 * <p/>
 * A binding is synonymous with JQuery <a href="http://docs.jquery.com/Events/bind">bind</a>
 * / <a href="http://docs.jquery.com/Events/live">live</a> functionality.
 */
public class JQBinding implements Serializable {

    private static final long serialVersionUID = 1L;

    // -------------------------------------------------------------- Variables

    /**
     * The CSS selector of this binding.
     */
    protected String selector;

    /**
     * The JavaScript event of this binding.
     */
    protected JQEvent event;

    public JQBinding(String selector, JQEvent event) {
        this.selector = selector;
        this.event = event;
    }

    /**
     * Return the CSS selector.
     *
     * @return the CSS selector
     */
    public String getSelector() {
        return selector;
    }

    /**
     * Return the JavaScript event.
     *
     * @return the JavaScript event
     */
    public JQEvent getEvent() {
        return event;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     * @param o the object with which to compare this instance with
     * @return true if the specified object is the same as this object
     */
    public boolean equals(Object o) {

        //1. Use the == operator to check if the argument is a reference to this object.
        if (o == this) {
            return true;
        }

        //2. Use the instanceof operator to check if the argument is of the correct type.
        if (!(o instanceof JQBinding)) {
            return false;
        }

        //3. Cast the argument to the correct type.
        JQBinding that = (JQBinding) o;

        String selector = getSelector();
        String thatSelector = that.getSelector();
        boolean b = selector == null ? thatSelector == null : selector.equals(thatSelector);
        if (!b) {
            return b;
        }

        return getEvent() == that.getEvent();
    }

    /**
     * @see java.lang.Object#hashCode()
     *
     * @return a hash code value for this object
     */
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getSelector()).append(getEvent()).toHashCode();
    }
}
