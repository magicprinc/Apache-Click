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
     * Set the number of items per page. A value of 0 means there is no
     * items per page.
     *
     * @param itemsPerPage the number of items per page
     */
    public void setItemsPerPage(int itemsPerPage);

    /**
     * Set the total number of items the paginator is presenting.
     *
     * @param totalItems the total number of items the paginator is presenting
     */
    public void setTotalItems(int totalItems);

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
}
