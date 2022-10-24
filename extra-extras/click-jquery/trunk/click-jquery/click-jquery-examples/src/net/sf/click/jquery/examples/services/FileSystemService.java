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
package net.sf.click.jquery.examples.services;

import net.sf.click.jquery.examples.domain.File;
import net.sf.click.jquery.examples.domain.Folder;
import org.apache.click.Context;

/**
 *
 */
public class FileSystemService {

    private static final String ROOT = "root";

    public Folder getFileSystem() {
        Context context = Context.getThreadLocalContext();
        Folder root = (Folder) context.getSessionAttribute(ROOT);

        if (root == null) {
            root = new Folder("n_1" , "(c:\\)", "#");
            new Folder("n_2", "apps", "#", root);
            new Folder("n_3", "dev", "#", root);
            Folder programFiles = new Folder("n_4", "programs files", "#", root);
            new Folder("n_4_1", "general", "#", programFiles);
            new Folder("n_4_2", "games", "#", programFiles);
            new File("n_4_3", "image.jpg", "#", programFiles);
            new File("n_5", "icon.png", "#", root);
            context.setSessionAttribute(ROOT, root);
        }

        return root;
    }

    public File load(Object id) {
        File fileSystem = getFileSystem();
        return findFile(fileSystem, id);
    }

    public void delete(Object id) {
        File fileSystem = getFileSystem();
        File file = findFile(fileSystem, id);
        file.getParent().remove(file);
    }

    private File findFile(File file, Object id) {
        if (id.equals(file.getId())) {
            return file;
        }
        if (file instanceof Folder) {
            Folder folder = (Folder) file;
            for(File child : folder.getChildren()) {
                File result = findFile(child, id);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}
