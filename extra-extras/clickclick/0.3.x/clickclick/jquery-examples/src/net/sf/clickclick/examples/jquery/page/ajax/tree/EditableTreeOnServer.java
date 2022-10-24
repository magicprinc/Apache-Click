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
package net.sf.clickclick.examples.jquery.page.ajax.tree;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.clickclick.examples.jquery.control.jstree.JSTree;
import net.sf.clickclick.examples.jquery.control.jstree.JSTreeNode;
import net.sf.clickclick.examples.jquery.control.jstree.listener.ChangeListener;
import net.sf.clickclick.examples.jquery.domain.File;
import net.sf.clickclick.examples.jquery.domain.Folder;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.examples.jquery.services.ApplicationRegistry;
import net.sf.clickclick.examples.jquery.services.FileSystemService;
import net.sf.clickclick.jquery.Taconite;
import net.sf.clickclick.jquery.control.ajax.JQForm;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import org.apache.click.Control;
import org.apache.click.control.Field;
import org.apache.click.control.HiddenField;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.element.JsScript;
import org.apache.click.util.ContainerUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;

/**
 *
 */
public class EditableTreeOnServer extends BorderPage {

    private JSTree tree = new JSTree("tree");

    private JQForm form = new JQForm("form");

    private HiddenField idField = new HiddenField("id", String.class);

    public EditableTreeOnServer() {
        FileSystemService service = getFileSystemService();
        Folder root = service.getFileSystem();

        buildTree(tree, root);
        addControl(tree);

        buildForm(form);
        addControl(form);
    }

    private void buildTree(JSTree tree, Folder fileSystem) {
        JSTreeNode rootNode = convertDomainTreeNodes(fileSystem);

        tree.setRootNode(rootNode);

        tree.setChangeListener(new ChangeListener() {

            @Override
            public Partial change(String nodeId) {
                Taconite partial = new Taconite();
                File file = getFileSystemService().load(nodeId);

                // Populate the form from the file object.
                form.copyFrom(file);
                form.getField("referenceNodeId").setValue(nodeId);

                // manually set the noteType, since form.copyFrom cannot populate
                // values based on class types
                if (file instanceof Folder) {
                    form.getField("nodeType").setValue("folder");
                } else {
                    form.getField("nodeType").setValue("file");

                    // Disable the new button if the type is file, since files cannot
                    // contain children
                    form.getField("new").setDisabled(true);
                }
                // Disable the nodeType select, since it cannot be changed once
                // set
                form.getField("nodeType").setDisabled(true);

                partial.replace(form);

                // NB: When replacing the form from the Tree "change event", the
                // Form submit event is not rebound (because the jQuery Form plugin
                // does not support "live" events), so submitting the form after
                // a change event, won't fire an Ajax call but a normal submit.
                // Note: when a Form is replaced from its own callback
                // (see buttons actions below) this step is not needed, because
                // the Form callback (on the client side) automatically rebinds
                // the Form after each submit.

                // Below we rebind the Form to the ajaxForm event, by applying
                // the original formOptions that is made available through
                // Click.jquery.form['$selector']
                // If/when jQuery Form plugin uses the "live" method of binding,
                // the workaround below can be removed
                partial.eval("var options = Click.jquery.form['#" + form.getId() + "'];"
                    + "jQuery('#" + form.getId() + "').ajaxForm(options);");
                return partial;
            }
        });
    }

    private void buildForm(final JQForm form) {
        // Setup fields
        form.add(new TextField("name", true));
        form.add(new TextField("path", "path (href)", false));
        form.add(idField);
        form.add(new HiddenField("referenceNodeId", String.class));

        Select type = new Select("nodeType");
        type.add(new Option("folder"));
        type.add(new Option("file"));
        form.add(type);

        Submit create = new Submit("new");
        form.add(create);
        Submit save = new Submit("save");
        form.add(save);
        Submit delete = new Submit("delete");
        form.add(delete);
        Submit cancel = new Submit("cancel");
        form.add(cancel);

        // Set an Ajax listener on the create button that will be invoked when
        // form is submitted
        create.setActionListener(new AjaxAdapter() {

            @Override
            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();
                form.clearErrors();
                clearVisibleFields();

                // Update the form
                partial.replace(form);

                return partial;
            }
        });

        // Set an Ajax listener on the create button
        save.setActionListener(new AjaxAdapter() {

            @Override
            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();

                if (form.isValid()) {
                    if (StringUtils.isBlank(idField.getValue())) {
                        // Do an insert to the database
                        // Normally an ORM will generate the ID
                        String referenceNodeId = form.getField("referenceNodeId").getValue();
                        File parentFile = (File) getFileSystemService().load(referenceNodeId);

                        // Only create new node if parent node allows children
                        if (parentFile instanceof Folder) {
                            Folder parent = (Folder) parentFile;
                            String entityId = generateEntityId();

                            File file = null;
                            if ("folder".equals(
                                form.getField("nodeType").getValue())) {
                                file = new Folder();
                            } else {
                                file = new File();

                                // Disable the new button if the type is file,
                                // since files cannot contain children
                                form.getField("new").setDisabled(true);
                            }

                            // Update the Form's idField with the new ID
                            // TODO this should be copyFrom
                            idField.setValue(entityId);

                            // Update file entity with form values
                            form.copyTo(file);

                            parent.add(file);
                            createJSTreeNode(partial, entityId);
                        }
                    } else {
                        // Do an update to the database
                        File file = getFileSystemService().load(idField.getValue());
                        if (file != null) {
                            form.copyTo(file);

                            if (file instanceof Folder) {
                                form.getField("nodeType").setValue("folder");
                            } else {
                                form.getField("nodeType").setValue("file");

                                // Disable the new button if the type is file,
                                // since files cannot contain children
                                form.getField("new").setDisabled(true);
                            }
                            // Disable the nodeType select, since it cannot be
                            // changed once set
                            form.getField("nodeType").setDisabled(true);

                            updateJSTreeNode(partial);
                        }
                    }
                }

                // Update the form
                partial.replace(form);

                return partial;
            }
        });

        // Set an Ajax listener on the delete button
        delete.setActionListener(new AjaxAdapter() {

            @Override
            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();
                String referenceNodeId = form.getField("referenceNodeId").getValue();
                if (StringUtils.isNotBlank(referenceNodeId)) {
                    getFileSystemService().delete(referenceNodeId);
                    form.clearErrors();
                    form.clearValues();
                    partial.replace(form);
                    partial.eval("var tree = jQuery.tree.focused(); if (tree) { tree.remove(tree.selected) }");
                }
       
                return partial;
            }
        });

        // Set an Ajax listener on the close
        cancel.setActionListener(new AjaxAdapter() {

            @Override
            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();
                form.clearErrors();
                form.clearValues();

                partial.replace(form);

                return partial;
            }
        });
    }

    private String generateEntityId() {
        // Create a random ID
        String nodeId = "n_" + Long.toString(RandomUtils.nextLong());
        return nodeId;
    }

    private void updateJSTreeNode(Taconite partial) {
        Map model = new HashMap();
        JsScript script = new JsScript("/ajax/editable-tree-on-server-partial.js", model);
        partial.eval(script);
    }

    private void createJSTreeNode(Taconite partial, String nodeId) {
        Map model = new HashMap();
        model.put("nodeId", nodeId);
        JsScript script = new JsScript("/ajax/editable-tree-on-server-partial.js", model);
        partial.eval(script);
    }

    private FileSystemService getFileSystemService() {
        return ApplicationRegistry.getInstance().getFileSystemService();
    }

    private JSTreeNode convertDomainTreeNodes(File file) {
        JSTreeNode node = createTreeNode(file);
        if (file instanceof Folder) {
            Folder folder = (Folder) file;
            Iterator children = folder.getChildren().iterator();
            while (children.hasNext()) {
                File child = (File) children.next();
                JSTreeNode childNode = convertDomainTreeNodes(child);
                node.add(childNode);
            }
        }
        return node;
    }

    private JSTreeNode createTreeNode(File file) {
        JSTreeNode node = new JSTreeNode(file.getName(), file.getId());
        node.setHref(file.getPath());
        if (file instanceof Folder) {
            node.setType("folder");
        } else {
            node.setType("file");
        }
        return node;
    }

    private void clearVisibleFields() {
        List fields = ContainerUtils.getInputFields(form);
        Field field = null;
        for (int i = 0, size = fields.size(); i < size; i++) {
            field = (Field) fields.get(i);

            if (!(field instanceof HiddenField)) {
                field.setValue(null);
            }
        }
        idField.setValue(null);
    }
}
