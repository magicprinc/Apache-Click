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
package net.sf.click.jquery.examples.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Domain class representing the 'Book' entity.
 */
public class Book implements Serializable {

	private static final long serialVersionUID = 1L;

    private String name;
    
    private String author;

    private String isbn;
    
    private List categories;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public List getCategories() {
        if (categories == null) {
            categories = new ArrayList();
        }
        return categories;
    }

    public void setCategories(List categories) {
        this.categories = categories;
    }
}
