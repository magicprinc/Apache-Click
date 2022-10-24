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

import net.sf.clickclick.examples.jquery.control.jstree.JSTree;
import net.sf.clickclick.examples.jquery.control.jstree.JSTreeNode;
import net.sf.clickclick.examples.jquery.control.jstree.listener.ChangeListener;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.Command;
import net.sf.clickclick.jquery.Taconite;
import net.sf.clickclick.jquery.control.ajax.JQForm;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import org.apache.click.Control;
import org.apache.click.control.HiddenField;
import org.apache.click.control.Option;
import org.apache.click.control.Select;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;

/**
 *
 */
public class EditableTree extends BorderPage {

    private JSTree tree = new JSTree("tree");

    private JQForm form = new JQForm("form");

    private HiddenField idField = new HiddenField("id", String.class);

    public EditableTree() {
        buildTree(tree);
        addControl(tree);

        buildForm(form);
        addControl(form);
    }

    private void buildTree(JSTree tree) {

        JSTreeNode rootNode = new JSTreeNode("c:", "n_1");
        rootNode.add(new JSTreeNode("apps", "n_2_1"));
        rootNode.add(new JSTreeNode("dev", "n_2_2"));
        JSTreeNode programs = new JSTreeNode("Program Files", "n_2_3");
        rootNode.add(programs);

        programs.add(new JSTreeNode("program 1", "n_2_3_1"));
        programs.add(new JSTreeNode("program 2", "n_2_3_2"));

        programs.add(new JSTreeNode("program 3", "n_2_3_3"));
        programs.add(new JSTreeNode("program 4", "n_2_3_4"));

        tree.setRootNode(rootNode);

        tree.setChangeListener(new ChangeListener() {

            @Override
            public Partial change(String nodeId) {
                Taconite partial = new Taconite();
                form.getField("id").setValue(nodeId);
                form.getField("nodeLabel").setValue("Test");
                form.getField("href").setValue("#");
                form.getField("nodeType").setValue("folder");
                partial.replaceContent("#formHolder", form);
                return partial;
            }
        });
    }

    private void buildForm(final JQForm form) {

        // Replace default JS template with custom template to handle creation
        // of tree nodes
        form.getJQueryHelper().setTemplate("/ajax/editable-tree-form.js");

        // Setup fields
        form.add(new TextField("nodeLabel", true));
        form.add(new TextField("href", false));
        form.add(idField);

        Select type = new Select("nodeType");
        type.add(new Option("folder"));
        type.add(new Option("file"));
        type.add(new Option("drive"));
        form.add(type);

        Submit create = new Submit("new");
        form.add(create);
        Submit save = new Submit("save");
        form.add(save);
        Submit close = new Submit("close");
        form.add(close);

        // Set an Ajax listener on the create button that will be invoked when
        // form is submitted
        create.setActionListener(new AjaxAdapter() {

            @Override
            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();
                form.clearErrors();
                form.clearValues();

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
                    // Save the form data to the database
                    System.out.println("SAVED!");

                    if (StringUtils.isBlank(idField.getValue())) {
                        setEntityId(partial);
                    }
                }

                // Update the form
                partial.replace(form);

                return partial;
            }
        });

        // Set an Ajax listener on the close
        close.setActionListener(new AjaxAdapter() {

            @Override
            public Partial onAjaxAction(Control source) {
                Taconite partial = new Taconite();

                // Update the form
                partial.remove(form.getId());

                return partial;
            }
        });
    }

    private void setEntityId(Taconite partial) {
        // Create a random ID
        String nodeId = "n_" + Long.toString(RandomUtils.nextLong());

        // Create a custom command and set the ID as a name/value pair.
        // The nodeId will be extracted on the browser using JavaScript, see
        // the JS function #onSuccess in the file webapp/ajax/editable-tree-form.js
        // for details
        Command command = new Command(Taconite.CUSTOM);
        command.setName(JSTree.NODE_ID);
        command.setValue(nodeId);
        partial.add(command);

        // Update the Form's idField with the new ID
        idField.setValue(nodeId);
    }
}
