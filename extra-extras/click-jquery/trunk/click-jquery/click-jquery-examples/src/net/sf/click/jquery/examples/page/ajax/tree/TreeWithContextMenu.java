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
package net.sf.click.jquery.examples.page.ajax.tree;

import net.sf.click.jquery.examples.control.jstree.JSTree;
import net.sf.click.jquery.examples.control.jstree.JSTreeNode;
import net.sf.click.jquery.examples.control.jstree.listener.ChangeListener;
import net.sf.click.jquery.examples.control.jstree.listener.CreateListener;
import net.sf.click.jquery.examples.control.jstree.listener.DeleteListener;
import net.sf.click.jquery.examples.control.jstree.listener.MoveListener;
import net.sf.click.jquery.examples.control.jstree.listener.OpenListener;
import net.sf.click.jquery.examples.control.jstree.listener.RenameListener;
import net.sf.click.jquery.examples.page.BorderPage;
import net.sf.click.jquery.taconite.JQTaconite;
import org.apache.click.ActionResult;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang.math.RandomUtils;

/**
 *
 */
public class TreeWithContextMenu extends BorderPage {

	private static final long serialVersionUID = 1L;

    private JSTree tree = new JSTree("tree");

    public TreeWithContextMenu() {
        tree.setContextMenuEnabled(true);

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
            public ActionResult change(String nodeId) {
                JQTaconite actionResult = new JQTaconite();
                actionResult.prepend("#response", "<p>changed to node '" + nodeId
                    + "', invoked at : " + format.currentDate("yyyy-MM-dd hh:mm:ss") + "</p>");
                return actionResult;
            }
        });

        tree.setDeleteListener(new DeleteListener(){

            @Override
            public ActionResult delete(String nodeId) {
                JQTaconite actionResult = new JQTaconite();
                actionResult.prepend("#response", "<p>deleted node '" + nodeId
                    + "', invoked at : " + format.currentDate("yyyy-MM-dd hh:mm:ss") + "</p>");
                return actionResult;
            }
        });

        tree.setRenameListener(new RenameListener() {

            @Override
            public ActionResult rename(String nodeId, String newValue) {
                JQTaconite actionResult = new JQTaconite();
                actionResult.prepend("#response", "<p>renamed node '" + nodeId + "' to '"
                    + newValue + "', invoked at : " + format.currentDate("yyyy-MM-dd hh:mm:ss") + "</p>");
                return actionResult;
            }
        });

        tree.setMoveListener(new MoveListener(){

            @Override
            public ActionResult move(String nodeId, String refNodeId, String type) {
                JQTaconite actionResult = new JQTaconite();
                actionResult.prepend("#response", "<p>moved node '" + nodeId + "' to '" + type
                    + "' prepend '" + refNodeId + "', invoked at : "
                    + format.currentDate("yyyy-MM-dd hh:mm:ss") + "</p>");
                return actionResult;
            }
        });

        tree.setCreateListener(new CreateListener(){

            @Override
            public JQTaconite create(String value) {
                JQTaconite actionResult = new JQTaconite();
                String nodeId = "n_" + Long.toString(RandomUtils.nextLong());
                actionResult.prepend("#response", "<p>created node '" + value + "' with ID '"
                    + nodeId + "', invoked at : " + format.currentDate("yyyy-MM-dd hh:mm:ss") + "</p>");
                setId(nodeId);
                return actionResult;
            }
        });

        addControl(tree);
    }
}
