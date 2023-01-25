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

/**
 * Domain class representing the 'File' entity.
 */
public class File implements Serializable {

    private String name;

    private String path;

    private String id;

    private Folder parent;

    public File(){
    }

    public File(String id, String name, String path) {
        setId(id);
        setName(name);
        setPath(path);
    }

    public File(String id, String name, String path, Folder parent) {
        setId(id);
        setName(name);
        setPath(path);
        parent.add(this);
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the parent
     */
    public Folder getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Folder parent) {
        this.parent = parent;
    }
}
