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

import org.apache.click.control.AbstractContainer;

/**
 * Provides a non-visible container for the {@link Repeater} control where
 * child controls can be added to. A Repeater contains a RepeaterRow for every
 * item it must render.
 * <p/>
 * <b>Please note:</b> RepeaterRows are automatically created by the Repeater
 * as needed, and is generally not managed by users.
 */
public class RepeaterRow extends AbstractContainer {

    /**
     * Create a default row.
     */
    public RepeaterRow() {
    }

    /**
     * Create a new row with the given name.
     *
     * @param name the name of the row
     */
    public RepeaterRow(String name) {
        super(name);
    }
}
