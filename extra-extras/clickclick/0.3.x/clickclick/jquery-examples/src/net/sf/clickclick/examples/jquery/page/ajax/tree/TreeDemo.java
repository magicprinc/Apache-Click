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

import java.text.SimpleDateFormat;
import java.util.Date;
import net.sf.clickclick.examples.jquery.control.jstree.JSTree;
import net.sf.clickclick.examples.jquery.control.jstree.JSTreeNode;
import net.sf.clickclick.examples.jquery.control.jstree.listener.ChangeListener;
import net.sf.clickclick.examples.jquery.control.jstree.listener.CreateListener;
import net.sf.clickclick.examples.jquery.control.jstree.listener.DeleteListener;
import net.sf.clickclick.examples.jquery.control.jstree.listener.MoveListener;
import net.sf.clickclick.examples.jquery.control.jstree.listener.OpenListener;
import net.sf.clickclick.examples.jquery.control.jstree.listener.RenameListener;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.Taconite;
import net.sf.clickclick.util.Partial;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang.math.RandomUtils;

/**
 * Demonstrates how to update a label while editing a Field using Ajax.
 *
 * In this demo the JQHelper is used to "decorate" a TextField with Ajax
 * functionality.
 */
public class TreeDemo extends BorderPage {

    private JSTree tree = new JSTree("tree");

    public TreeDemo() {
        buildTree(tree);
        addControl(tree);
    }

    private void buildTree(JSTree tree) {
        JSTreeNode rootNode = new JSTreeNode("c:", "n_1");
        rootNode.add(new JSTreeNode("apps", "n_2_1"));
        rootNode.add(new JSTreeNode("dev", "n_2_2"));
        JSTreeNode programs = new JSTreeNode("Program Files", "n_2_3");
        rootNode.add(programs);

        programs.add(new JSTreeNode("program 1", "n_2_3_1"));
        JSTreeNode program2 = new JSTreeNode("program 2", "n_2_3_2", programs);
        program2.setLoadLazily(true);
        program2.setSelected(true);
        programs.add(new JSTreeNode("program 3", "n_2_3_3"));
        programs.add(new JSTreeNode("program 4", "n_2_3_4"));

        tree.setRootNode(rootNode);

        tree.setOpenListener(new OpenListener() {

            @Override
            public void open(String nodeId, HtmlStringBuffer buffer) {
                JSTreeNode node = new JSTreeNode("subprogram", nodeId + "_1");
                node.render(buffer);
                node = new JSTreeNode("subprogram2", nodeId + "_2");
                node.render(buffer);
            }
        });

        tree.setChangeListener(new ChangeListener(){

            @Override
            public Partial change(String nodeId) {
                Taconite partial = new Taconite();
                partial.prepend("#response", "<p>changed to node '" + nodeId + "', invoked at : " + getDate() + "</p>");
                return partial;
            }
        });

        tree.setDeleteListener(new DeleteListener(){

            @Override
            public Partial delete(String nodeId) {
                Taconite partial = new Taconite();
                partial.prepend("#response", "<p>deleted node '" + nodeId + "', invoked at : " + getDate() + "</p>");
                return partial;
            }
        });

        tree.setRenameListener(new RenameListener() {

            @Override
            public Partial rename(String nodeId, String newValue) {
                Taconite partial = new Taconite();
                partial.prepend("#response", "<p>renamed node '" + nodeId + "' to '" + newValue + "', invoked at : " + getDate() + "</p>");
                return partial;
            }
        });

        tree.setMoveListener(new MoveListener(){

            @Override
            public Partial move(String nodeId, String refNodeId, String type) {
                Taconite partial = new Taconite();
                partial.prepend("#response", "<p>moved node '" + nodeId + "' to '" + type
                    + "' prepend '" + refNodeId + "', invoked at : " + getDate() + "</p>");
                return partial;
            }
        });

        tree.setCreateListener(new CreateListener(){

            @Override
            public Taconite create(String value) {
                Taconite partial = new Taconite();
                String nodeId = "n_" + Long.toString(RandomUtils.nextLong());
                partial.prepend("#response", "<p>created node '" + value + "' with ID '"
                    + nodeId + "', invoked at : " + getDate() + "</p>");
                setId(nodeId);
                return partial;
            }
        });
    }

    private String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(new Date());
    }
}
