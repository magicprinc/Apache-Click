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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 */
public class JSTreeNode {

    // -------------------------------------------------------------- Constants

    /** default serial version id. */
    private static final long serialVersionUID = 1L;

    /**
     * Used internally to generate unique id's for tree nodes where id's are
     * not explicitly provided.
     */
    private final static Random RANDOM = new Random();

    // ----------------------------------------------------- Instance Variables

    /**
     * Each node must have a unique id in the tree. If a node is not provided
     * an id, one is generated using the java.util.Random class.
     */
    protected String id;

    /** Indicates if the treeNode is currently selected. */
    protected boolean selected = false;

    /** Indicates if the treeNode is currently expanded. */
    protected boolean expanded = false;

    /**
     * Indicates if the treeNode supports children or not. This is useful to
     * differentiate between files and directories with no children.
     */
    protected boolean childrenSupported = true;

    /**
     * Indicates if the treeNode child nodes should be loaded lazily or eagerly.
     */
    protected boolean loadLazily;

    /** User provided value of this node. */
    protected Object value;

    /** Each node except the top level node will have a parent. */
    protected JSTreeNode parent;

    /** List containing this nodes children. */
    protected List children;

    /** The node link href attribute. */
    protected String href = "#";

    protected String type;

    // ---------------------------------------------------- Public Constructors

    /**
     * Creates a default node with no value or id.
     * <p/>
     * <strong>Note:</strong> a default random id is generated using a static
     * instance of {@link java.util.Random}.
     */
    public JSTreeNode() {
        setId(generateId());
    }

    /**
     * Creates a node and sets the value to the specified argument.
     * <p/>
     * <strong>Note:</strong> a default random id is generated using a static
     * instance of {@link java.util.Random}.
     *
     * @param value the nodes value
     */
    public JSTreeNode(Object value) {
        setValue(value);
        setId(generateId());
    }

    /**
     * Creates a node and sets the value and id to the specified arguments.
     *
     * @param value the nodes value
     * @param id the nodes id
     */
    public JSTreeNode(Object value, String id) {
        setValue(value);
        setId(id);
    }

    /**
     * Creates a node and sets the value, id and parent to the specified arguments.
     *
     * @param value the nodes value
     * @param id the nodes id
     * @param parent specifies the parent of this node
     */
    public JSTreeNode(Object value, String id, JSTreeNode parent) {
        setValue(value);
        setId(id);
        parent.add(this);
    }

    /**
     * Creates a node and sets the value, id and parent to the specified arguments.
     *
     * @param value the nodes value
     * @param id the nodes id
     * @param parent specifies the parent of this node
     * @param childrenSupported indicates if the treeNode supports child nodes
     * or not.
     */
    public JSTreeNode(Object value, String id, JSTreeNode parent, boolean childrenSupported) {
        setValue(value);
        setId(id);
        setChildrenSupported(childrenSupported);
        parent.add(this);
    }

    // -------------------------------------------------------- Public Methods

    /**
     * Returns this node's type.
     *
     * @return this node's type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets this node's type
     *
     * @param type this node's type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns this node's parent object or null if parent is not specified.
     *
     * @return JSTreeNode this node's parent or null if parent is not specified
     */
    public JSTreeNode getParent() {
        return parent;
    }

    /**
     * Sets this node's parent to the specified argument. This node is not
     * added to the parent's children.
     *
     * @param parent this node's parent object
     */
    public void setParent(JSTreeNode parent) {
        this.parent = parent;
    }

    /**
     * Return this node's value.
     *
     * @return this node's value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Set this node's value.
     *
     * @param value the new value of this node
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Returns this node's id value.
     *
     * @return this node's id value
     */
    public String getId() {
        return id;
    }

    /**
     * Set this node's new id value.
     *
     * @param id this node's new id value
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the href
     */
    public String getHref() {
        return href;
    }

    /**
     * @param href the href to set
     */
    public void setHref(String href) {
        this.href = href;
    }

    /**
     * Returns true if this node is currently in the selected state, false otherwise.
     *
     * @return true if this node is currently selected, false otherwise.
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Sets this node to the specified selected state.
     *
     * @param selected new value for this node's selected state
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Returns true if this node is currently in the expanded state, false otherwise.
     *
     * @return true if this node is currently expanded, false otherwise.
     */
    public boolean isExpanded() {
        return expanded;
    }

    /**
     * Sets this node to the specified expanded state.
     *
     * @param expanded new value for this node's expanded state
     */
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    /**
     * Returns a unmodifiable list of this nodes children.
     *
     * @return the unmodifiable list of children.
     */
    public List getChildren() {
        if (children == null) {
            children = new ArrayList();
        }
        return children;
    }

    /**
     * Returns true if this node has any children nodes false otherwise.
     *
     * @return true if this node has any children false otherwise
     */
    public boolean hasChildren() {
        if (CollectionUtils.isEmpty(children)) {
            return false;
        }
        return true;
    }

    /**
     * Returns true if this node does not have any children, false otherwise.
     *
     * @return true if this node is a leaf node, false otherwise.
     */
    public boolean isLeaf() {
        return hasChildren();
    }

    /**
     * Returns true if this node supports children, false otherwise.
     *
     * @return true if this node supports children, false otherwise.
     */
    public boolean isChildrenSupported() {
        return childrenSupported;
    }

    /**
     * Sets whether this node supports child nodes or not. If set to false
     * this method will remove all this node's children.
     *
     * @param childrenSupported whether this node supports children or not
     */
    public void setChildrenSupported(boolean childrenSupported) {
        if (this.childrenSupported != childrenSupported) {
            this.childrenSupported = childrenSupported;
            if (!childrenSupported) {
                for (int i = 0; i < getChildren().size(); i++) {
                    remove((JSTreeNode) children.get(i));
                }
            }
        }
    }

    /**
     * Return true if the node is to be loaded lazily, false otherwise.
     *
     * @return true if the node is to be loaded lazily, false otherwise.
     */
    public boolean isLoadLazily() {
        return loadLazily;
    }

    /**
     * Set whether the node should be loaded lazily or eagerly.
     *
     * @param loadLazily true if the node should loaded lazily, false otherwise
     */
    public void setLoadLazily(boolean loadLazily) {
        this.loadLazily = loadLazily;
    }

    /**
     * Adds the specified node as a child of this node and sets the child's parent
     * to this node.
     *
     * @param child child node to add
     * @throws IllegalArgumentException if the argument is null or if this node
     * does not support child nodes
     */
    public void add(JSTreeNode child) {
       add(getChildren().size(), child);
    }

    /**
     * Adds the specified node at the specified index as a child of this node and sets
     * the child's parent to this node.
     *
     * @param index the index at which specified child must be added
     * @param child child node to add
     * @throws IllegalArgumentException if the argument is null or if this node
     * does not support child nodes
     */
    public void add(int index, JSTreeNode child) {
        getChildren().add(index, child);
        child.setParent(this);
    }

    /**
     * Removes the specified node from the list of children and sets child's parent
     * node to null.
     *
     * @param child child node to remove from this nodes children
     * @throws IllegalArgumentException if the argument is null
     */
    public void remove(JSTreeNode child) {
        if (child == null) {
            throw new IllegalArgumentException("null child specified");
        }
        if (getChildren() == null) {
            children = new ArrayList();
        }
        getChildren().remove(child);
        child.setParent(null);
    }

    /**
     * Returns true if this node is the root node. The root is the node with a
     * null parent.
     *
     * @return boolean true if this node is root, false otherwise
     */
    public boolean isRoot() {
        return getParent() == null;
    }

    public void render(HtmlStringBuffer buffer) {
        renderNodeItemStart(buffer, this);

        if (hasChildren()) {
            renderNodes(buffer, getChildren());
        }

        renderNodeItemEnd(buffer, this);
    }

    @Override
    public String toString() {
        HtmlStringBuffer buffer = new HtmlStringBuffer(100);
        render(buffer);
        return buffer.toString();
    }

    protected void renderNodes(HtmlStringBuffer buffer, List<JSTreeNode> children) {
        buffer.elementStart("ul");
        buffer.closeTag();
        buffer.append("\n");

        for (JSTreeNode child : children) {
            renderNodeItemStart(buffer, child);
            if (child.hasChildren()) {
                renderNodes(buffer, child.getChildren());
            }
            renderNodeItemEnd(buffer, child);
            buffer.append("\n");
        }
        buffer.elementEnd("ul");
        buffer.append("\n");
    }

    // ------------------------------------------------------ Protected Methods

    protected void renderNodeItemStart(HtmlStringBuffer buffer, JSTreeNode node) {
        buffer.elementStart("li");
        buffer.appendAttribute("id", node.getId());
        buffer.appendAttribute("rel", node.getType());
        buffer.append(" class=\"");
        if (node.isExpanded()) {
            buffer.append("open");
        } else {
            if (node.hasChildren() || node.isLoadLazily()) {
                buffer.append("closed");
            }
        }
        buffer.append("\"");

        buffer.closeTag();

        renderNodeLink(buffer, node);
    }

    protected void renderNodeItemEnd(HtmlStringBuffer buffer, JSTreeNode node) {
        buffer.elementEnd("li");
    }

    protected void renderNodeLink(HtmlStringBuffer buffer, JSTreeNode node) {
        buffer.elementStart("a");
        if (StringUtils.isNotBlank(node.getHref())) {
            buffer.appendAttribute("href", node.getHref());
        }
        buffer.closeTag();

        buffer.elementStart("ins");
        buffer.closeTag();
        buffer.append("&nbsp;");
        buffer.elementEnd("ins");

        buffer.append(node.getValue());

        buffer.elementEnd("a");
    }

    // ------------------------------------------------------- Private Behavior

    /**
     * Returns a randomized id for this node.
     * <p/>
     * Currently this implementation generates a random Long using
     * random.nextLong(). This means that 2 <sup>64</sup> possible
     * numbers can be generated.
     *
     * @return a randomized id for this node
     * @see java.util.Random
     */
    private String generateId() {
        return Long.toString(Math.abs(RANDOM.nextLong()));
    }
}
