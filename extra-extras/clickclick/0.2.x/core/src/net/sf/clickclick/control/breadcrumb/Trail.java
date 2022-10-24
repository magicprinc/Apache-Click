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
package net.sf.clickclick.control.breadcrumb;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Provides a trail for the breadcrumb control.
 * <p/>
 * This class provides a FIFO (First In First Out) cache clearance. Thus the
 * first entry added will be the first entry being removed when necessary.
 */
public class Trail extends LinkedHashMap {

    // -------------------------------------------------------------- Variables

    /** The breadcrumb control. */
    private transient Breadcrumb breadcrumb;

    /**
     * Create a Trail for the given breadcrumb.
     *
     * @param breadcrumb the breadcrumb control
     */
    public Trail(Breadcrumb breadcrumb) {
        this.breadcrumb = breadcrumb;
    }

    // ------------------------------------------------------ Public Properties

    /**
     * Return the Trail's breadcrumb control.
     *
     * @return the Trail's breadcrumb control
     */
    public Breadcrumb getBreadcrumb() {
        return breadcrumb;
    }

    /**
     * Set the Trail's breadcrumb control.
     *
     * @param breadcrumb the Trail's breadcrumb control
     */
    public void setBreadcrumb(Breadcrumb breadcrumb) {
        this.breadcrumb = breadcrumb;
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Return true if the older entry must be removed, false otherwise.
     *
     * @param eldest the eldest entry in the Map
     * @return true if the eldest entry must be removed, false otherwise
     */
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > getBreadcrumb().getTrailLength();
    }
}
