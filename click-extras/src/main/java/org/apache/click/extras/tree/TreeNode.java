package org.apache.click.extras.tree;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Provides an implementation of a {@link org.apache.click.extras.tree.Tree} model.
 * <p/>
 * TreeNode's are used to store hierarchical data representations for example
 * directories, subdirectories and files.
 * <p/>
 * Nodes <strong>must</strong> have a <strong>unique</strong> id. If nodes
 * do not have a unique id, the tree might behave erratic.
 * <p/>
 * Each node can store a user defined value of type {@link java.lang.Object}.
 * <p/>
 * Nodes contain a reference to their parent node as well as their children.
 * <p/>
 * Two types of leaf nodes are supported by this implementation. A leaf node can
 * either support the addition of new child nodes or not. An example of a leaf node
 * where children are supported is a directory. A directory can be empty, meaning
 * it is a leaf node, but files and subdirectories can be added as children.
 * Another type of of leaf node does not support the addition of child nodes. A
 * file would be an example of such a node. A file does not support the addition
 * of new files or subdirectories as children. By default newly created nodes
 * supports the addition of child nodes. Use the appropriate constructor to
 * disable the support of child nodes, or call {@link #setChildrenSupported(boolean)}.
 * <p/>
 * <strong>Note:</strong> Since each TreeNode must have a
 * unique id within the tree, a default id is generated if one is not provided in
 * the constructors. Id's are generated by an instance of {@link java.util.Random}.
 * The current implementation generates a Long using random.nextLong(). This
 * means a total of 2 <sup>64</sup> numbers can be generated.
 */
public class TreeNode implements Serializable {
  @Serial private static final long serialVersionUID = -5820771127721699500L;

  /** Each node must have a unique id in the tree. If a node is not provided
   * an id, one is generated using the java.util.Random class.*/
  private String id;

  /** Indicates if the treeNode is currently selected. */
  private boolean selected = false;

  /** Indicates if the treeNode is currently expanded. */
  private boolean expanded = false;

  /** Indicates if the treeNode supports children or not. This is useful to
   * differentiate between files and directories with no children. */
  private boolean childrenSupported = true;

  /** User provided value of this node. */
  private Object value;

  /** Specifies the depth of this tree. */
  private int treeDepth = -1;

  /** Each node except the top level node will have a parent. */
  private TreeNode parent;

  /** List containing this nodes children. */
  private List<TreeNode> children;

  /** A custom icon the Tree will render for this node. */
  private String icon;

  // Public Constructors ----------------------------------------------------

  /**
   * Creates a default node with no value or id.
   * <p/>
   * <strong>Note:</strong> a default random id is generated using a static
   * instance of {@link java.util.Random}.
   */
  public TreeNode() {
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
  public TreeNode(Object value) {
    setValue(value);
    setId(generateId());
  }

  /**
   * Creates a node and sets the value and id to the specified arguments.
   *
   * @param value the nodes value
   * @param id the nodes id
   */
  public TreeNode(Object value, String id) {
    setValue(value);
    setId(id);
  }

  /**
   * @deprecated use {@link #TreeNode(java.lang.Object, java.lang.String)}
   * instead and add to parent node through {@link #add(org.apache.click.extras.tree.TreeNode)}.
   *
   * Creates a node and sets the value, id and parent to the specified arguments.
   *
   * @param value the nodes value
   * @param id the nodes id
   * @param parent specifies the parent of this node
   */
  @Deprecated
  public TreeNode(Object value, String id, TreeNode parent) {
    setValue(value);
    setId(id);
    parent.add(this);
  }

  /**
   * Creates a node and sets the value, id and childrenSupported to the
   * specified arguments.
   *
   * @param value the nodes value
   * @param id the nodes id
   * @param childrenSupported indicates if the treeNode supports child nodes
   * or not.
   */
  public TreeNode(Object value, String id, boolean childrenSupported) {
    setValue(value);
    setId(id);
    setChildrenSupported(childrenSupported);
  }

  /**
   * @deprecated use {@link #TreeNode(java.lang.Object, java.lang.String, boolean)}
   * instead and add to parent node through {@link #add(org.apache.click.extras.tree.TreeNode)}.
   *
   * Creates a node and sets the value, id and parent to the specified arguments.
   *
   * @param value the nodes value
   * @param id the nodes id
   * @param parent specifies the parent of this node
   * @param childrenSupported indicates if the treeNode supports child nodes
   * or not.
   */
  @Deprecated
  public TreeNode(Object value, String id, TreeNode parent, boolean childrenSupported) {
    setValue(value);
    setId(id);
    setChildrenSupported(childrenSupported);
    parent.add(this);
  }

  // Public Getters and Setters ---------------------------------------------

  /**
   * Returns this node's parent object or null if parent is not specified.
   *
   * @return TreeNode this node's parent or null if parent is not specified
   */
  public TreeNode getParent() {
    return parent;
  }

  /**
   * Sets this node's parent to the specified argument. This node is not
   * added to the parent's children.
   *
   * @param parent this node's parent object
   */
  public void setParent(TreeNode parent) {
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
   * Returns true if this node does not have any children, false otherwise.
   *
   * @return true if this node is a leaf node, false otherwise.
   */
  public boolean isLeaf() {
    return getChildren().isEmpty();
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
        for (TreeNode child : getChildren()) {
          remove(child);
        }
      }
    }
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
   * Returns a unmodifiable list of this nodes children.
   *
   * @return the unmodifiable list of children.
   */
  public List<TreeNode> getChildren() {
    if (children == null) {
      return Collections.emptyList();
    }
    return Collections.unmodifiableList(children);
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
   * Returns true if this node is currently in the expanded state, false otherwise.
   *
   * @return true if this node is currently expanded, false otherwise.
   */
  public boolean isExpanded() {
    return expanded;
  }

  /**
   * Returns the TreeNode's custom icon.
   *
   * @see #setIcon(java.lang.String)
   *
   * @return the TreeNode's custom icon
   */
  public String getIcon() {
    return icon;
  }

  /**
   * Set a custom icon for this TreeNode.
   * <p/>
   * The icon will be set as the <tt>src</tt> attribute of an HTML <tt>img</tt>
   * element. Ensure the icon value specifies the full path to the actual
   * icon resource.
   * <p/>
   * For example:
   * <pre class="prettyprint">
   * public TreeNode createNodes() {
   *     // Retrieve the web application context path
   *     String contextPath = getContext().getRequest().getContextPath();
   *
   *     TreeNode book = new TreeNode("book");
   *
   *     TreeNode chapter = new TreeNode("chapter", "1", book);
   *
   *     // Specify the full path to the chapter.png icon
   *     chapter.setIcon(contextPath + "/assets/chapter.png");
   *     ...
   *
   *     return book;
   * } </pre>
   *
   * @param icon the TreeNode icon
   */
  public void setIcon(String icon) {
    this.icon = icon;
  }

  // Public Behavior --------------------------------------------------------

  /**
   * Adds the specified node as a child of this node and sets the child's parent
   * to this node.
   *
   * @param child child node to add
   * @throws IllegalArgumentException if the argument is null or if this node
   * does not support child nodes
   */
  public void add(TreeNode child) {
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
  public void add(int index, TreeNode child) {
    addChildOnly(index, child);
    child.setParent(this);
  }

  /**
   * Adds the specified node as a child of this node but does NOT set the child's
   * parent to this node.
   *
   * @param child child node to add
   * @throws IllegalArgumentException if the argument is null or if this node
   * does not support child nodes
   */
  public void addChildOnly(TreeNode child) {
    addChildOnly(getChildren().size(), child);
  }

  /**
   * Adds the specified node at the specified index as a child of this node but
   * does NOT set the child's parent to this node.
   *
   * @param index of the child node to add to
   * @param child child node to add
   * @throws IllegalArgumentException if the argument is null or if this node
   * does not support child nodes
   */
  public void addChildOnly(int index, TreeNode child) {
    if (!isChildrenSupported()) {
      throw new IllegalStateException("this node does not support children nodes");
    }
    if (child == null) {
      throw new IllegalArgumentException("null child specified");
    }
    if (getMutableChildren() == null) {
      children = new ArrayList<>();
    }
    if (children.contains(child)) {
      return;
    }
    getMutableChildren().add(index, child);
  }

  /**
   * Removes the specified node from the list of children and sets child's parent
   * node to null.
   *
   * @param child child node to remove from this nodes children
   * @throws IllegalArgumentException if the argument is null
   */
  public void remove(TreeNode child) {
    if (child == null) {
      throw new IllegalArgumentException("null child specified");
    }
    if (getMutableChildren() == null) {
      children = new ArrayList<>();
    }
    getMutableChildren().remove(child);
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

  /**
   * Returns true if this node has any children nodes false otherwise.
   *
   * @return true if this node has any children false otherwise
   */
  public boolean hasChildren() {
    return !isLeaf();
  }

  /**
   * Returns true if either this node is the last child of its parent, or this
   * node is the root node. Else it returns false.
   *
   * @return true if this node is the last child of its parent or the
   * root node, false otherwise.
   */
  public boolean isLastChild() {
    if (isRoot()) {
      return true;
    }
    return getParent().isLastChild(this);
  }

  /**
   * Calculate and return the depth of the tree where this node is set as the
   * root of the calculation.
   * <p/>
   * This method guarantees to recalculate the depth of the tree and will
   * not use the cached value if it was set using {@link #cacheTreeDepth(int)}.
   * <p/>
   * <strong>Time complexity:</strong> This method performs in O(n) where n is
   * the number of nodes in the tree. In other words no optimized algorithm is
   * used by this method.
   *
   * @return depth of the tree
   * @see #cacheTreeDepth(int)
   */
  public int calcTreeDepth() {
    return calcTreeDepth(false);
  }

  /**
   * Calculate and return the depth of the tree where this node is set as the
   * root of the calculation.
   * <p/>
   * If the argument useCacheIfSet is true, this method will return the cached
   * value that was set by {@link #cacheTreeDepth(int)}. If useCacheIfSet is false
   * this method will recalculate the depth of the tree.
   * <p/>
   * <strong>Time complexity:</strong> This method performs in O(n) where n is
   * the number of nodes in the tree. In other words no optimized algorithm is
   * used by this method.
   *
   * @param useCacheIfSet if true use the cached value if it was set,
   * else recalculate the depth
   * @return depth of the tree
   * @see #cacheTreeDepth(int)
   */
  public int calcTreeDepth(boolean useCacheIfSet) {
    //lookup from cache. this will only be true if user explicitly sets the tree's depth using cacheTreeDepth(int)
    if (useCacheIfSet && treeDepth > -1) {
      return treeDepth;
    }

    //Iterate over the tree using a breadth first iterator. The level of the
    //last node found will indicate the depth of the tree.
    TreeNode o = null;
    for (Iterator<TreeNode> it = new Tree.BreadthTreeIterator(this); it.hasNext();){
      o = it.next();
    }
    //depth = level of last node - this node's level
    return o.getLevel() - getLevel();
  }

  /**
   * Stores the specified depth in a variable for fast retrieval.
   * The cached depth can be retrieved by calling {@link #calcTreeDepth(boolean)}
   * with 'true'.
   *
   * @param depth this node's depth to cache
   * @see #calcTreeDepth(boolean)
   */
  public void cacheTreeDepth(int depth) {
    this.treeDepth = depth;
  }

  /**
   * Returns this node's level in the tree structure.
   *
   * @return int indicating this node's level
   */
  public int getLevel() {
    int level = 0;
    TreeNode parentObj = this;
    while ((parentObj = parentObj.getParent()) != null) {
      level++;
    }
    return level;
  }

  /**
   * Checks if this node's id is equals to the specified node's id. Two
   * tree node's are the same if they have the same id.
   *
   * @param thatObject the specified object to check for equality
   * @return true if this node's id is equal to the specified node's id
   *
   * @see Object#equals(Object)
   */
  @Override
  public boolean equals(Object thatObject) {
    if (thatObject == this) {
      return true;
    }

    if (!( thatObject instanceof TreeNode that )) {
      return false;
    }

    return this.getId() == null ? that.getId() == null : this.getId().equals(that.getId());
  }

  /**
   * Returns the hashCode value for this node. The hashCode is calculated
   * from the node's id.
   *
   * @return a hash code value for this object.
   * @see Object#hashCode()
   */
  @Override
  public int hashCode() {
    return getId().hashCode();
  }

  /**
   * Renders a string representation of this node.
   *
   * @return string representation of this node
   */
  @Override
  public String toString() {
    return "TreeNode -> [" +
        "id=" + getId() + ", " +
        "value=" + getValue() + ", " +
        "expanded=" + isExpanded() + ", " +
        "selected=" + isSelected() + ", " +
        "supports children=" + isChildrenSupported() +
        "]";
  }

  // Package Private Methods ------------------------------------------------

  /**
   * Sets this node to the specified selected state.
   *
   * @param selected new value for this node's selected state
   */
  void setSelected(boolean selected) {
    this.selected = selected;
  }

  /**
   * Sets this node to the specified expanded state.
   *
   * @param expanded new value for this node's expanded state
   */
  void setExpanded(boolean expanded) {
    this.expanded = expanded;
  }

  // Private Behavior -------------------------------------------------------

  /**
   * Returns the list of mutable children of this node.
   *
   * @return mutable children of this node
   */
  private List<TreeNode> getMutableChildren() {
    return children;
  }

  /**
   * Checks if the specified node is the last child of this node. If this
   * node does not have any children, this method returns false.
   * <p/>
   * <strong>Note:</strong> The check is done comparing references -> node1 == node2,
   * and not equality -> node1.equals(node2).
   *
   * @param child the node to check if its the last child or not
   * @return true if the specified node is the last child of this node, false otherwise
   * @throws NullPointerException if the specified child is null
   */
  private boolean isLastChild(TreeNode child) {
    int size = getChildren().size();
    if (size == 0) {
      return false;
    }
    Object lastChild = getChildren().get(size - 1);
    return lastChild == child;
  }

  /**
   * Returns a randomized id for this node.
   * <p/>
   * Currently this implementation generates a random Long using
   * random.nextLong(). This means that 2 <sup>64</sup> possible
   * numbers can be generated.
   *
   Random is used internally to generate unique id's for tree nodes where id's are not explicitly provided.

   * @return a randomized id for this node
   * @see java.util.Random
   */
  private String generateId(){
    return Long.toString(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE) & Long.MAX_VALUE);
  }
}