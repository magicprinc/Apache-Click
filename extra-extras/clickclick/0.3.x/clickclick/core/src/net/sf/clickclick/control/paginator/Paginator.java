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
package net.sf.clickclick.control.paginator;

import org.apache.click.control.Renderable;

/**
 *
 */
public interface Paginator extends Renderable {

    /**
     * Set the current page value.
     *
     * @param currentPage the current page value
     */
    public void setCurrentPage(int currentPage);

    /**
     * Return the current page value.
     *
     * @return the current page value
     */
    public int getCurrentPage();

    public int getNextPage();

    public void calcPageTotal(int pageSize, int rows);
}
