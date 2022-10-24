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
import java.util.Collections;
import java.util.List;

/**
 * Domain class representing the  'Folder' entity.
 */
public class Folder extends File implements Serializable {

    private List<File> children;

    public Folder() {
    }

    public Folder(String id, String name, String path) {
        super(id, name, path);
    }

    public Folder(String id, String name, String path, Folder parent) {
        super(id, name, path, parent);
    }

    public List<File> getChildren() {
        return Collections.unmodifiableList(getModifiableChildren());
    }

    public void add(File file) {
        getModifiableChildren().add(file);
        file.setParent(this);
    }

    public void remove(File file) {
        getModifiableChildren().remove(file);
        file.setParent(null);
    }

    private List<File> getModifiableChildren() {
        if (children == null) {
             children = new ArrayList<File>();
        }
        return children;
    }
}
