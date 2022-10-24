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
package net.sf.click.jquery.examples.control.jstree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.click.jquery.behavior.JQBehavior;
import net.sf.click.jquery.examples.control.jstree.listener.ChangeListener;
import net.sf.click.jquery.examples.control.jstree.listener.CloseListener;
import net.sf.click.jquery.examples.control.jstree.listener.CreateListener;
import net.sf.click.jquery.examples.control.jstree.listener.DeleteListener;
import net.sf.click.jquery.examples.control.jstree.listener.DeselectListener;
import net.sf.click.jquery.examples.control.jstree.listener.MoveListener;
import net.sf.click.jquery.examples.control.jstree.listener.OpenListener;
import net.sf.click.jquery.examples.control.jstree.listener.RenameListener;
import net.sf.click.jquery.examples.control.jstree.listener.SelectListener;
import net.sf.click.jquery.taconite.JQCommand;
import net.sf.click.jquery.taconite.JQTaconite;
import org.apache.click.Context;
import org.apache.click.Control;
import org.apache.click.ActionResult;
import org.apache.click.ajax.DefaultAjaxBehavior;
import org.apache.click.control.AbstractControl;
import org.apache.click.element.Element;
import org.apache.click.element.JsImport;
import org.apache.click.element.JsScript;
import org.apache.click.util.HtmlStringBuffer;

/**
 *
 */
public class JSTree extends AbstractControl {

    public static final String CALLBACK = "callback";

    public static final String NODE_ID = "nodeId";

    public static final String REFERENCE_NODE_ID = "refNodeId";

    public static final String TYPE = "type";

    public static final String VALUE = "nodeValue";

    // -------------------------------------------------------------- Variables

    /** The tree's hierarchical data model. */
    protected JSTreeNode rootNode;

    /**
     * Specifies if the root node should be displayed, or only its children.
     * By default this value is false.
     */
    protected boolean rootNodeDisplayed = false;

    protected boolean contextMenuEnabled = false;

    protected ChangeListener changeListener;

    protected CloseListener closeListener;

    protected CreateListener createListener;

    protected DeleteListener deleteListener;

    protected DeselectListener deselectListener;

    protected MoveListener moveListener;

    protected OpenListener openListener;

    protected RenameListener renameListener;

    protected SelectListener selectListener;

    // ----------------------------------------------------------- Constructors

    /**
     * Create a JSTree.
     */
    public JSTree() {
    }

    /**
     * Create a JSTree with the given name.
     *
     * @param name the name of the control
     */
    public JSTree(String name) {
        super(name);
    }

    @Override
    public List<Element> getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();

            JsImport jsImport = new JsImport(JQBehavior.jqueryPath);
            headElements.add(jsImport);

            jsImport = new JsImport(JQBehavior.jqueryClickPath);
            headElements.add(jsImport);

            jsImport = new JsImport("/click-jquery/example/jstree/jquery.tree.js");
            headElements.add(jsImport);

            if (isContextMenuEnabled()) {
                jsImport = new JsImport("/click-jquery/example/jstree/plugins/jquery.tree.contextmenu.js");
                headElements.add(jsImport);
            }

            /*
            jsImport = new JsImport("/click-jquery/example/jstree/lib/jquery.cookie.js");
            headElements.add(jsImport);

            jsImport = new JsImport("/click-jquery/example/jstree/plugins/jquery.tree.cookie.js");
            headElements.add(jsImport);
            */

            Context context = getContext();
            JsScript script = getJsTemplate(context);
            headElements.add(script);
        }
        return headElements;
    }

    @Override
    public String getTag() {
        return "div";
    }

    /**
     * @return the contextMenuEnabled
     */
    public boolean isContextMenuEnabled() {
        return contextMenuEnabled;
    }

    /**
     * @param contextMenuEnabled the contextMenuEnabled to set
     */
    public void setContextMenuEnabled(boolean contextMenuEnabled) {
        this.contextMenuEnabled = contextMenuEnabled;
    }

    public JSTreeNode getRootNode() {
        //Calculate the root node dynamically by finding the node where parent == null.
        //Thus if a new root node was created this method will still return
        //the correct node
        if (rootNode == null) {
            return null;
        }
        while ((rootNode.getParent()) != null) {
            rootNode = rootNode.getParent();
        }
        return rootNode;
    }

    /**
     * Set the tree's root JSTreeNode.
     *
     * @param rootNode node will be set as the root
     */
    public void setRootNode(JSTreeNode rootNode) {
        if (rootNode == null) {
            return;
        }
        this.rootNode = rootNode;
    }

    /**
     * Return if tree has a root node.
     *
     * @return boolean indicating if the tree's root has been set.
     */
    public boolean hasRootNode() {
        return getRootNode() != null;
    }

    /**
     * Return if the tree's root node should be displayed or not.
     *
     * @return if root node should be displayed
     */
    public boolean isRootNodeDisplayed() {
        return rootNodeDisplayed;
    }

    /**
     * Sets whether the tree's root node should be displayed or not.
     *
     * @param rootNodeDisplayed true if the root node should be displayed,
     * false otherwise
     */
    public void setRootNodeDisplayed(boolean rootNodeDisplayed) {
        this.rootNodeDisplayed = rootNodeDisplayed;
    }

    /**
     * @return the changeListener
     */
    public ChangeListener getChangeListener() {
        return changeListener;
    }

    /**
     * @param changeListener the changeListener to set
     */
    public void setChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    /**
     * @return the closeListener
     */
    public CloseListener getCloseListener() {
        return closeListener;
    }

    /**
     * @param closeListener the closeListener to set
     */
    public void setCloseListener(CloseListener closeListener) {
        this.closeListener = closeListener;
    }

    /**
     * @return the createListener
     */
    public CreateListener getCreateListener() {
        return createListener;
    }

    /**
     * @param createListener the createListener to set
     */
    public void setCreateListener(CreateListener createListener) {
        this.createListener = createListener;
    }

    /**
     * @return the deleteListener
     */
    public DeleteListener getDeleteListener() {
        return deleteListener;
    }

    /**
     * @param deleteListener the deleteListener to set
     */
    public void setDeleteListener(DeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    /**
     * @return the deselectListener
     */
    public DeselectListener getDeselectListener() {
        return deselectListener;
    }

    /**
     * @param deselectListener the deselectListener to set
     */
    public void setDeselectListener(DeselectListener deselectListener) {
        this.deselectListener = deselectListener;
    }

    /**
     * @return the moveListener
     */
    public MoveListener getMoveListener() {
        return moveListener;
    }

    /**
     * @param moveListener the moveListener to set
     */
    public void setMoveListener(MoveListener moveListener) {
        this.moveListener = moveListener;
    }

    /**
     * @return the openListener
     */
    public OpenListener getOpenListener() {
        return openListener;
    }

    /**
     * @param openListener the openListener to set
     */
    public void setOpenListener(OpenListener openListener) {
        this.openListener = openListener;
    }

    /**
     * @return the renameListener
     */
    public RenameListener getRenameListener() {
        return renameListener;
    }

    /**
     * @param renameListener the renameListener to set
     */
    public void setRenameListener(RenameListener renameListener) {
        this.renameListener = renameListener;
    }

    /**
     * @return the selectListener
     */
    public SelectListener getSelectListener() {
        return selectListener;
    }

    /**
     * @param selectListener the selectListener to set
     */
    public void setSelectListener(SelectListener selectListener) {
        this.selectListener = selectListener;
    }

    // --------------------------------------------------------- Public Methods

    @Override
    public void onInit() {
        super.onInit();
        addBehavior(new JSTreeListener());

        //AjaxControlRegistry.registerAjaxControl(this);
        //ActionEventDispatcher.dispatchActionEvent(this, new JSTreeListener(), ActionEventDispatcher.POST_ON_PROCESS_EVENT);
    }

    @Override
    public void render(HtmlStringBuffer buffer) {
        buffer.elementStart("div");
        buffer.appendAttribute("id", getId());
        appendAttributes(buffer);
        buffer.closeTag();
        buffer.append("\n");

        JSTreeNode localTootNode = getRootNode();
        if (isRootNodeDisplayed()) {
            buffer.elementStart("ul");
            buffer.closeTag();
            buffer.append("\n");
            localTootNode.render(buffer);
            buffer.elementEnd("ul");
            buffer.append("\n");
        } else {
            localTootNode.renderNodes(buffer, localTootNode.getChildren());
        }

        buffer.elementEnd("div");
        buffer.append("\n");
    }

    @Override
    public String toString() {
        HtmlStringBuffer buffer = new HtmlStringBuffer(100);
        render(buffer);
        return buffer.toString();
    }

    // ------------------------------------------------------ Protected Methods

    protected JsScript getJsTemplate(Context context) {
        String id = getId();
        Map model = new HashMap();
        model.put("context", context.getRequest().getContextPath());
        model.put("path", getPage().getPath());
        model.put("id", id);
        model.put("jstree", this);
        model.put("selector", '#' + id);
        StringBuilder sb = new StringBuilder();
        addSelectedNodeIds(getRootNode(), sb);
        model.put("selected", sb.toString());
        JsScript jsScript = new JsScript("/click-jquery/example/template/jquery.jstree.template.js", model);
        return jsScript;
    }

    private void addSelectedNodeIds(JSTreeNode node, StringBuilder sb) {
        if(node.isSelected()) {
            if (sb.length() != 0) {
                sb.append(',');
            }
            sb.append("'");
            sb.append(node.getId());
            sb.append("'");
        }

        if (node.hasChildren()) {
            List<JSTreeNode> children = node.getChildren();
            for (JSTreeNode child : children) {
                addSelectedNodeIds(child, sb);
            }
        }
    }

    enum TreeCallback {
        OPEN,
        CREATE,
        DELETE,
        RENAME,
        MOVE,
        COPY,
        SELECT,
        DESELECT,
        CHANGE,
        CLOSE,
        OPEN_ALL,
        CLOSE_ALL,
        SEARCH;

        public static TreeCallback lookup(String callback) {
            return TreeCallback.valueOf(callback);
        }
    }

    private class JSTreeListener extends DefaultAjaxBehavior {

        @Override
        public ActionResult onAction(Control source) {
            Context context = getContext();

            String callbackParam = context.getRequestParameter(CALLBACK);
            TreeCallback callback = TreeCallback.lookup(callbackParam);
            if (callback == null) {
                return null;
            }

            ActionResult actionResult = null;

            switch (callback) {
                case OPEN: {
                    OpenListener listener = getOpenListener();
                    if (listener != null) {
                        String nodeId = context.getRequestParameter(NODE_ID);
                        actionResult = new ActionResult();
                        HtmlStringBuffer buffer = new HtmlStringBuffer();
                        listener.open(nodeId, buffer);
                        actionResult.setContent(buffer.toString());
                    }
                    break;
                }
                case CLOSE: {
                    CloseListener listener = getCloseListener();
                    if (listener != null) {
                        String nodeId = context.getRequestParameter(NODE_ID);
                        actionResult = listener.close(nodeId);
                    }
                    break;
                }
                case CREATE: {
                    CreateListener listener = getCreateListener();
                    if (listener != null) {
                        String newValue = context.getRequestParameter(VALUE);
                        actionResult = listener.create(newValue);
                        String newNodeId = listener.getId();

                        // Send created nodeId to browser
                        JQCommand command = new JQCommand(JQTaconite.CUSTOM);
                        command.setName(NODE_ID);
                        command.setValue(newNodeId);
                        ((JQTaconite) actionResult).insert(command, 0);
                    }
                    break;
                }
                case DELETE: {
                    DeleteListener listener = getDeleteListener();
                    if (listener != null) {
                        String nodeId = context.getRequestParameter(NODE_ID);
                        actionResult = listener.delete(nodeId);
                    }
                    break;
                }
                case RENAME: {
                    RenameListener listener = getRenameListener();
                    if (listener != null) {
                        String nodeId = context.getRequestParameter(NODE_ID);
                        String newValue = context.getRequestParameter(VALUE);
                        actionResult = listener.rename(nodeId, newValue);
                    }
                    break;
                }
                case MOVE: {
                    MoveListener listener = getMoveListener();
                    if (listener != null) {
                        String nodeId = context.getRequestParameter(NODE_ID);
                        String refNodeId = context.getRequestParameter(
                            REFERENCE_NODE_ID);
                        String type = context.getRequestParameter(TYPE);
                        actionResult = new JQTaconite();
                        actionResult = listener.move(nodeId, refNodeId, type);
                    }
                    break;
                }
                case CHANGE: {
                    ChangeListener listener = getChangeListener();
                    if (listener != null) {
                        String nodeId = context.getRequestParameter(NODE_ID);
                        actionResult = listener.change(nodeId);
                    }
                    break;
                }
                case SELECT: {
                    SelectListener listener = getSelectListener();
                    if (listener != null) {
                        String nodeId = context.getRequestParameter(NODE_ID);
                        actionResult = listener.select(nodeId);
                    }
                    break;
                }
                case DESELECT: {
                    DeselectListener listener = getDeselectListener();
                    if (listener != null) {
                        String nodeId = context.getRequestParameter(NODE_ID);
                        actionResult = listener.deselect(nodeId);
                    }
                    break;
                }
            }

            return actionResult;
        }
    }
}
